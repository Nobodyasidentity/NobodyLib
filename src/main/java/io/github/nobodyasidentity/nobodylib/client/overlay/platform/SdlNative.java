package io.github.nobodyasidentity.nobodylib.client.overlay.platform;

import org.lwjgl.sdl.SDLProperties;
import org.lwjgl.sdl.SDLVideo;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

final class SdlNative{
    private SdlNative(){}
    static long win32Hwnd(long sdlWindow){
        return SDLProperties.SDL_GetPointerProperty(SDLVideo.SDL_GetWindowProperties(sdlWindow),SDLVideo.SDL_PROP_WINDOW_WIN32_HWND_POINTER,0L);
    }
    static long x11Display(long sdlWindow){
        return SDLProperties.SDL_GetPointerProperty(SDLVideo.SDL_GetWindowProperties(sdlWindow),SDLVideo.SDL_PROP_WINDOW_X11_DISPLAY_POINTER,0L);
    }
    static long x11Window(long sdlWindow){
        return SDLProperties.SDL_GetNumberProperty(SDLVideo.SDL_GetWindowProperties(sdlWindow),SDLVideo.SDL_PROP_WINDOW_X11_WINDOW_NUMBER,0L);
    }
    static long cocoaWindow(long sdlWindow){
        return SDLProperties.SDL_GetPointerProperty(SDLVideo.SDL_GetWindowProperties(sdlWindow),SDLVideo.SDL_PROP_WINDOW_COCOA_WINDOW_POINTER,0L);
    }
    static String videoDriver(){
        return SDLVideo.SDL_GetCurrentVideoDriver();
    }
    static int[] position(long sdlWindow){
        try(MemoryStack stack=MemoryStack.stackPush()){
            IntBuffer x=stack.mallocInt(1);
            IntBuffer y=stack.mallocInt(1);
            SDLVideo.SDL_GetWindowPosition(sdlWindow,x,y);
            return new int[]{x.get(0),y.get(0)};
        }
    }
    static int[] size(long sdlWindow){
        try(MemoryStack stack=MemoryStack.stackPush()){
            IntBuffer w=stack.mallocInt(1);
            IntBuffer h=stack.mallocInt(1);
            SDLVideo.SDL_GetWindowSize(sdlWindow,w,h);
            return new int[]{w.get(0),h.get(0)};
        }
    }
    static int[] sizeInPixels(long sdlWindow){
        try(MemoryStack stack=MemoryStack.stackPush()){
            IntBuffer w=stack.mallocInt(1);
            IntBuffer h=stack.mallocInt(1);
            SDLVideo.SDL_GetWindowSizeInPixels(sdlWindow,w,h);
            return new int[]{w.get(0),h.get(0)};
        }
    }

    static long createOverlayWindow(String title){
        SDLVideo.SDL_GL_SetAttribute(SDLVideo.SDL_GL_ALPHA_SIZE,8);
        long flags=SDLVideo.SDL_WINDOW_OPENGL|SDLVideo.SDL_WINDOW_HIDDEN|SDLVideo.SDL_WINDOW_BORDERLESS
            |SDLVideo.SDL_WINDOW_TRANSPARENT|SDLVideo.SDL_WINDOW_NOT_FOCUSABLE|SDLVideo.SDL_WINDOW_ALWAYS_ON_TOP;
        return SDLVideo.SDL_CreateWindow(title,1,1,flags);
    }
    static long createGlContext(long window){
        return SDLVideo.SDL_GL_CreateContext(window);
    }
    static void makeCurrent(long window,long context){
        SDLVideo.SDL_GL_MakeCurrent(window,context);
    }
    static long currentWindow(){
        return SDLVideo.SDL_GL_GetCurrentWindow();
    }
    static long currentContext(){
        return SDLVideo.SDL_GL_GetCurrentContext();
    }
    static void swap(long window){
        SDLVideo.SDL_GL_SwapWindow(window);
    }
    static void show(long window){
        SDLVideo.SDL_ShowWindow(window);
    }
    static void hide(long window){
        SDLVideo.SDL_HideWindow(window);
    }
    static void setPosition(long window,int x,int y){
        SDLVideo.SDL_SetWindowPosition(window,x,y);
    }
    static void setSize(long window,int w,int h){
        SDLVideo.SDL_SetWindowSize(window,w,h);
    }
}