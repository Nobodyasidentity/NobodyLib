package io.github.nobodyasidentity.nobodylib.client.overlay.platform;

import com.sun.jna.platform.win32.WinDef.HWND;
import io.github.nobodyasidentity.lib.client.overlay.Overlay;
import io.github.nobodyasidentity.nobodylib.NobodyLib;
import io.github.nobodyasidentity.nobodylib.client.overlay.OverlayBackend;
import io.github.nobodyasidentity.nobodylib.client.overlay.OverlayFrame;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL30.*;

public final class Win32Overlay implements OverlayBackend{
    private static final int RING=3;
    private static final int FREE=0,PENDING=1,INFLIGHT=2;

    private final int[]pbo=new int[RING];
    private final long[]fence=new long[RING];
    private final int[]state=new int[RING];
    private final long[]seq=new long[RING];
    private final int[]frameX=new int[RING];
    private final int[]frameY=new int[RING];
    private final long[]mappedAddress=new long[RING];

    private int fbo=0;
    private int fboTexture=0;
    private int width=-1;
    private int height=-1;
    private long nextSeq=1;
    private boolean useFences=false;
    private boolean blankShown=false;

    private long cachedMcHandle=0L;
    private HWND mcHwnd;

    @Override
    public boolean init(long mcWindowHandle){
        if(!WglContext.create())return false;
        RawOverlayWindow.create(0,0,1,1);
        if(RawOverlayWindow.getHwnd()==null)return false;
        Win32LayeredWindow.init(RawOverlayWindow.getHwnd());
        Win32Presenter.start();
        useFences=WglContext.capabilities()!=null&&WglContext.capabilities().OpenGL32;
        NobodyLib.LOGGER.info("Overlay started (Windows, asynchronous read-back"+(useFences?", fenced":"")+").");
        return true;
    }

    @Override
    public void setVisible(boolean visible){
        RawOverlayWindow.setVisible(visible);
    }

    @Override
    public boolean isMinecraftCovered(long mcWindowHandle){
        HWND hwnd=mcHwnd(mcWindowHandle);
        return hwnd!=null&&Win32WindowOcclusion.isMinimizedOrFullyCovered(hwnd);
    }

    private HWND mcHwnd(long mcWindowHandle){
        if(mcHwnd==null||cachedMcHandle!=mcWindowHandle){
            cachedMcHandle=mcWindowHandle;
            mcHwnd=Win32Native.hwndOf(mcWindowHandle);
        }
        return mcHwnd;
    }

    @Override
    public void tick(long mcWindowHandle){
        HWND hwnd=mcHwnd(mcWindowHandle);
        if(hwnd==null)return;
        int[] bounds=Win32Native.clientBounds(hwnd);
        if(bounds==null||bounds[2]<=0||bounds[3]<=0)return;
        RawOverlayWindow.setBounds(bounds[0],bounds[1],bounds[2],bounds[3]);
        boolean nothingToDraw=!Overlay.hasLayers()&&!OverlayFrame.isOutlineEnabled();
        if(nothingToDraw&&blankShown&&!pipelineBusy())return;

        if(!WglContext.makeCurrent())return;
        try{
            recycleFinished();
            if(!ensureTargets(bounds[2],bounds[3]))return;
            if(!(nothingToDraw&&blankShown)){
                glBindFramebuffer(GL_FRAMEBUFFER,fbo);
                OverlayFrame.render(width,height);
                boolean queued=queueReadback(bounds[0],bounds[1]);
                glBindFramebuffer(GL_FRAMEBUFFER,0);
                if(queued)blankShown=nothingToDraw;
            }
            handOffNewestFinished();
        }finally{
            WglContext.restore();
        }
    }

    private boolean pipelineBusy(){
        for(int i=0;i<RING;i++){
            if(state[i]!=FREE)return true;
        }
        return false;
    }

    /** Unmaps buffers whose frame the presenter is done with. */
    private void recycleFinished(){
        long done=Win32Presenter.completedSeq();
        for(int i=0;i<RING;i++){
            if(state[i]==INFLIGHT&&done>=seq[i]){
                glBindBuffer(GL_PIXEL_PACK_BUFFER,pbo[i]);
                GL15.glUnmapBuffer(GL_PIXEL_PACK_BUFFER);
                glBindBuffer(GL_PIXEL_PACK_BUFFER,0);
                mappedAddress[i]=0L;
                state[i]=FREE;
            }
        }
    }

    /** Starts copying the framebuffer into a free pixel buffer without waiting for it. */
    private boolean queueReadback(int x,int y){
        int slot=-1;
        for(int i=0;i<RING;i++){
            if(state[i]==FREE){
                slot=i;
                break;
            }
        }
        if(slot<0)return false;
        glBindBuffer(GL_PIXEL_PACK_BUFFER,pbo[slot]);
        glReadPixels(0,0,width,height,GL_BGRA,GL_UNSIGNED_BYTE,0L);
        glBindBuffer(GL_PIXEL_PACK_BUFFER,0);
        if(useFences){
            fence[slot]=GL32.glFenceSync(GL32.GL_SYNC_GPU_COMMANDS_COMPLETE,0);
        }
        glFlush();
        state[slot]=PENDING;
        seq[slot]=nextSeq++;
        frameX[slot]=x;
        frameY[slot]=y;
        return true;
    }

    private boolean isReady(int slot){
        if(!useFences)return nextSeq-seq[slot]>=3;
        int result=GL32.glClientWaitSync(fence[slot],0,0L);
        return result==GL32.GL_ALREADY_SIGNALED||result==GL32.GL_CONDITION_SATISFIED;
    }

    private void releaseFence(int slot){
        if(useFences&&fence[slot]!=0L){
            GL32.glDeleteSync(fence[slot]);
            fence[slot]=0L;
        }
    }

    /** Gives the newest finished read-back to the presenter thread; older ones are dropped. */
    private void handOffNewestFinished(){
        int best=-1;
        for(int i=0;i<RING;i++){
            if(state[i]==PENDING&&isReady(i)&&(best<0||seq[i]>seq[best]))best=i;
        }
        if(best<0)return;
        for(int i=0;i<RING;i++){
            if(state[i]==PENDING&&seq[i]<seq[best]){
                releaseFence(i);
                state[i]=FREE;
            }
        }
        if(Win32Presenter.isBusy())return;

        long size=(long)width*height*4;
        glBindBuffer(GL_PIXEL_PACK_BUFFER,pbo[best]);
        ByteBuffer mapped=GL30.glMapBufferRange(GL_PIXEL_PACK_BUFFER,0L,size,GL30.GL_MAP_READ_BIT);
        glBindBuffer(GL_PIXEL_PACK_BUFFER,0);
        releaseFence(best);
        if(mapped==null){
            state[best]=FREE;
            return;
        }
        mappedAddress[best]=MemoryUtil.memAddress(mapped);
        state[best]=INFLIGHT;
        Win32Presenter.submit(mappedAddress[best],frameX[best],frameY[best],width,height,seq[best]);
    }

    /** Creates or resizes the framebuffer and pixel buffers. Returns false if it has to wait for the presenter first. */
    private boolean ensureTargets(int w,int h){
        if(fbo!=0&&w==width&&h==height)return true;
        for(int i=0;i<RING;i++){
            if(state[i]==INFLIGHT)return false;
        }
        destroyTargets();

        fboTexture=glGenTextures();
        glBindTexture(GL_TEXTURE_2D,fboTexture);
        glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA8,w,h,0,GL_RGBA,GL_UNSIGNED_BYTE,(ByteBuffer)null);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
        fbo=glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER,fbo);
        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,fboTexture,0);
        int status=glCheckFramebufferStatus(GL_FRAMEBUFFER);
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        if(status!=GL_FRAMEBUFFER_COMPLETE){
            NobodyLib.LOGGER.error("Overlay framebuffer is incomplete, status 0x"+Integer.toHexString(status));
            destroyTargets();
            throw new IllegalStateException("overlay framebuffer incomplete");
        }

        long size=(long)w*h*4;
        for(int i=0;i<RING;i++){
            pbo[i]=GL15.glGenBuffers();
            glBindBuffer(GL_PIXEL_PACK_BUFFER,pbo[i]);
            GL15.glBufferData(GL_PIXEL_PACK_BUFFER,size,GL15.GL_STREAM_READ);
            state[i]=FREE;
        }
        glBindBuffer(GL_PIXEL_PACK_BUFFER,0);
        width=w;
        height=h;
        blankShown=false;
        return true;
    }

    private void destroyTargets(){
        for(int i=0;i<RING;i++){
            releaseFence(i);
            if(pbo[i]!=0){
                GL15.glDeleteBuffers(pbo[i]);
                pbo[i]=0;
            }
            state[i]=FREE;
        }
        if(fbo!=0){
            glDeleteFramebuffers(fbo);
            fbo=0;
        }
        if(fboTexture!=0){
            glDeleteTextures(fboTexture);
            fboTexture=0;
        }
        width=-1;
        height=-1;
    }
}