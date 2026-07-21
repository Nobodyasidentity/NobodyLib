package io.github.nobodyasidentity.lib.client.overlay;

import io.github.nobodyasidentity.lib.NobodyLib;
import io.github.nobodyasidentity.lib.client.overlay.platform.RawOverlayWindow;
import io.github.nobodyasidentity.lib.client.overlay.platform.Win32LayeredWindow;
import io.github.nobodyasidentity.lib.config.NobodyLibConfigs;
import org.lwjgl.opengl.GL;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL30.*;

public final class OverlayRenderer{
    private static ByteBuffer pixelBuffer;

    private static int fbo=0;
    private static int fboTexture=0;
    private static int fboWidth=-1;
    private static int fboHeight=-1;

    private OverlayRenderer(){}

    @FunctionalInterface
    public interface Content{
        void render(int width,int height);
    }

    public static void renderFrame(long mcWindowHandle,Content content){
        long overlay=OverlayWindow.getHandle();
        if(overlay==-1L)return;

        glfwMakeContextCurrent(overlay);
        GL.setCapabilities(OverlayWindow.getOverlayCapabilities());

        int[] w=new int[1];
        int[] h=new int[1];

        if(NobodyLib.OS.contains("win")){
            glfwGetFramebufferSize(mcWindowHandle,w,h);
            renderAndPresentWindows(w[0],h[0],content);
        }else{
            glfwGetFramebufferSize(overlay,w,h);
            glClearColor(0f,0f,0f,0f);
            glClear(GL_COLOR_BUFFER_BIT);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
            setupPixelProjection(w[0],h[0]);
            content.render(w[0],h[0]);
            if(NobodyLibConfigs.CLIENT.get().overlay.outline_enabled){
                drawDebugOutline(w[0],h[0]);
            }
            glfwSwapBuffers(overlay);
        }

        glfwMakeContextCurrent(mcWindowHandle);
        GL.setCapabilities(OverlayWindow.getMcCapabilities());
    }

    private static void renderAndPresentWindows(int width,int height,Content content){
        if(width<=0||height<=0)return;

        ensureFbo(width,height);
        glBindFramebuffer(GL_FRAMEBUFFER,fbo);
        glViewport(0,0,width,height);

        glClearColor(0f,0f,0f,0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        setupPixelProjection(width,height);
        content.render(width,height);
        if(NobodyLibConfigs.CLIENT.get().overlay.outline_enabled){
            drawDebugOutline(width,height);
        }

        int needed=width*height*4;
        if(pixelBuffer==null||pixelBuffer.capacity()<needed){
            pixelBuffer=ByteBuffer.allocateDirect(needed);
        }
        pixelBuffer.clear();
        glReadPixels(0,0,width,height,GL_BGRA,GL_UNSIGNED_BYTE,pixelBuffer);
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        Win32LayeredWindow.present(RawOverlayWindow.getX(),RawOverlayWindow.getY(),width,height,pixelBuffer);
    }

    private static void setupPixelProjection(int width,int height){
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0,width,height,0,-1,1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    private static void drawDebugOutline(int width,int height){
        glDisable(GL_BLEND);
        glLineWidth(2f);
        glColor4f(1f,0f,0f,1f);
        glBegin(GL_LINE_LOOP);
        glVertex2f(1,1);
        glVertex2f(width-1,1);
        glVertex2f(width-1,height-1);
        glVertex2f(1,height-1);
        glEnd();
        glColor4f(1f,1f,1f,1f);
        glEnable(GL_BLEND);
    }

    private static void ensureFbo(int width,int height){
        if(fbo!=0&&width==fboWidth&&height==fboHeight)return;

        if(fbo!=0){
            glDeleteFramebuffers(fbo);
            glDeleteTextures(fboTexture);
        }

        fboTexture=glGenTextures();
        glBindTexture(GL_TEXTURE_2D,fboTexture);
        glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA8,width,height,0,GL_RGBA,GL_UNSIGNED_BYTE,(ByteBuffer)null);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);

        fbo=glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER,fbo);
        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,fboTexture,0);

        int status=glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if(status!=GL_FRAMEBUFFER_COMPLETE){
            System.err.println("[NobodyLib] Overlay FBO incomplete, status=0x"+Integer.toHexString(status));
        }
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        fboWidth=width;
        fboHeight=height;
    }
}