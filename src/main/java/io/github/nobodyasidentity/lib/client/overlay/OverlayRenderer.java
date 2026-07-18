package io.github.nobodyasidentity.lib.client.overlay;

import io.github.nobodyasidentity.lib.NobodyLib;
import io.github.nobodyasidentity.lib.client.overlay.platform.Win32LayeredWindow;
import org.lwjgl.opengl.GL;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGRA;

public final class OverlayRenderer{
    private static ByteBuffer pixelBuffer;

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

        glClearColor(0f,0f,0f,0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        int[] w=new int[1];
        int[] h=new int[1];
        glfwGetFramebufferSize(overlay,w,h);
        content.render(w[0],h[0]);

        if(NobodyLib.OS.contains("win")){
            presentWindows(overlay,w[0],h[0]);
        }else{
            glfwSwapBuffers(overlay);
        }

        glfwMakeContextCurrent(mcWindowHandle);
        GL.setCapabilities(OverlayWindow.getMcCapabilities());
    }

    private static void presentWindows(long overlay,int width,int height){
        if(width<=0||height<=0)return;

        int needed=width*height*4;
        if(pixelBuffer==null||pixelBuffer.capacity()<needed){
            pixelBuffer=ByteBuffer.allocateDirect(needed);
        }
        pixelBuffer.clear();
        glReadPixels(0,0,width,height,GL_BGRA,GL_UNSIGNED_BYTE,pixelBuffer);

        int[] x=new int[1];
        int[] y=new int[1];
        glfwGetWindowPos(overlay,x,y);

        Win32LayeredWindow.present(x[0],y[0],width,height,pixelBuffer);
        glfwSwapBuffers(overlay);
    }
}