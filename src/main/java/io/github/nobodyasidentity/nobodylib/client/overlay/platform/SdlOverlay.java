package io.github.nobodyasidentity.nobodylib.client.overlay.platform;

import io.github.nobodyasidentity.nobodylib.NobodyLib;
import io.github.nobodyasidentity.nobodylib.client.overlay.OverlayBackend;
import io.github.nobodyasidentity.nobodylib.client.overlay.OverlayFrame;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

public final class SdlOverlay implements OverlayBackend{
    private long window=0L;
    private long context=0L;
    private GLCapabilities capabilities;
    private boolean shown=false;

    @Override
    public boolean init(long mcWindowHandle){
        boolean mac=NobodyLib.OS.contains("mac")||NobodyLib.OS.contains("darwin");
        String driver=SdlNative.videoDriver();
        if(!mac&&!"x11".equals(driver)){
            NobodyLib.LOGGER.warn("The overlay needs an X11 session on Linux, but Minecraft is using the '"+driver
                +"' video driver. Start the game with SDL_VIDEO_DRIVER=x11 to use the overlay.");
            return false;
        }
        long previousWindow=SdlNative.currentWindow();
        long previousContext=SdlNative.currentContext();
        window=SdlNative.createOverlayWindow(NobodyLib.NAME+" overlay");
        if(window==0L){
            NobodyLib.LOGGER.error("Overlay: SDL could not create the overlay window.");
            return false;
        }
        context=SdlNative.createGlContext(window);
        if(context==0L){
            NobodyLib.LOGGER.error("Overlay: SDL could not create an OpenGL context for the overlay window.");
            return false;
        }
        try{
            if(GL.getFunctionProvider()==null)GL.create();
            capabilities=GL.createCapabilities();
        }finally{
            SdlNative.makeCurrent(previousWindow,previousContext);
        }
        if(mac){
            MacClickThrough.apply(SdlNative.cocoaWindow(window));
        }else{
            X11ClickThrough.apply(SdlNative.x11Display(window),SdlNative.x11Window(window));
        }
        return true;
    }
    @Override
    public void setVisible(boolean visible){
        if(window==0L||visible==shown)return;
        shown=visible;
        if(visible)SdlNative.show(window);
        else SdlNative.hide(window);
    }
    @Override
    public void tick(long mcWindowHandle){
        if(window==0L)return;
        if(!shown){
            shown=true;
            SdlNative.show(window);
        }
        int[]pos=SdlNative.position(mcWindowHandle);
        int[]size=SdlNative.size(mcWindowHandle);
        SdlNative.setPosition(window,pos[0],pos[1]);
        SdlNative.setSize(window,size[0],size[1]);

        int[]pixels=SdlNative.sizeInPixels(window);
        if(pixels[0]<=0||pixels[1]<=0)return;

        long previousWindow=SdlNative.currentWindow();
        long previousContext=SdlNative.currentContext();
        GLCapabilities previousCapabilities=null;
        try{
            previousCapabilities=GL.getCapabilities();
        }catch(IllegalStateException noCapabilitiesYet){}
        SdlNative.makeCurrent(window,context);
        GL.setCapabilities(capabilities);
        try{
            OverlayFrame.render(pixels[0],pixels[1]);
            SdlNative.swap(window);
        }finally{
            SdlNative.makeCurrent(previousWindow,previousContext);
            GL.setCapabilities(previousCapabilities);
        }
    }
}