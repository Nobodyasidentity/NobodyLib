package io.github.nobodyasidentity.lib.client.overlay;
import io.github.nobodyasidentity.lib.NobodyLib;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import static org.lwjgl.glfw.GLFW.*;

public final class OverlayWindow{
    private static long handle=-1L;
    private static GLCapabilities overlayCapabilities;
    private static GLCapabilities mcCapabilities;
    private OverlayWindow(){}
    public static void create(long mcWindowHandle,int width,int height){
        create(mcWindowHandle,width,height,NobodyLib.NAME+" overlay");
    }
    public static void create(long mcWindowHandle,int width,int height,String window_name){
        if(handle!=-1L)return;
        mcCapabilities=GL.getCapabilities();

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_FALSE);
        glfwWindowHint(GLFW_FLOATING,GLFW_TRUE);
        glfwWindowHint(GLFW_FOCUS_ON_SHOW,GLFW_FALSE);
        glfwWindowHint(GLFW_ALPHA_BITS,8);
        if(!NobodyLib.OS.contains("win")){
            glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER,GLFW_TRUE);
        }
        glfwWindowHint(GLFW_MOUSE_PASSTHROUGH,GLFW_TRUE);

        handle=glfwCreateWindow(Math.max(width,1),Math.max(height,1),window_name,0L,0L);
        if(handle==0L){
            handle=-1L;
            throw new IllegalStateException("Failed to create overlay window");
        }
        glfwMakeContextCurrent(handle);
        overlayCapabilities=GL.createCapabilities();
        glfwMakeContextCurrent(mcWindowHandle);
        GL.setCapabilities(mcCapabilities);
    }

    public static void show(){
        if(handle!=-1L){
            glfwShowWindow(handle);
        }
    }

    public static long getHandle(){return handle;}
    public static GLCapabilities getOverlayCapabilities(){return overlayCapabilities;}
    public static GLCapabilities getMcCapabilities(){return mcCapabilities;}
    public static void destroy(){
        if(handle!=-1L){
            glfwDestroyWindow(handle);
            handle=-1L;
            overlayCapabilities=null;
        }
    }
}