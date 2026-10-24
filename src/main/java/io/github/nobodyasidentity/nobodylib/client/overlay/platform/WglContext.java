package io.github.nobodyasidentity.nobodylib.client.overlay.platform;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HINSTANCE;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.HWND;
import io.github.nobodyasidentity.nobodylib.NobodyLib;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

final class WglContext{
    private interface Gdi extends Library{
        Gdi INSTANCE=Native.load("gdi32",Gdi.class);
        int ChoosePixelFormat(Pointer hdc,Pointer pfd);
        boolean SetPixelFormat(Pointer hdc,int format,Pointer pfd);
    }
    private interface Wgl extends Library{
        Wgl INSTANCE=Native.load("opengl32",Wgl.class);
        Pointer wglCreateContext(Pointer hdc);
        boolean wglMakeCurrent(Pointer hdc,Pointer hglrc);
        Pointer wglGetCurrentContext();
        Pointer wglGetCurrentDC();
    }

    private static final int WS_POPUP=0x80000000;
    private static final int PFD_DRAW_TO_WINDOW=0x00000004;
    private static final int PFD_SUPPORT_OPENGL=0x00000020;

    private static Pointer dc;
    private static Pointer context;
    private static GLCapabilities capabilities;

    private static Pointer savedDc;
    private static Pointer savedContext;
    private static GLCapabilities savedCapabilities;
    private static boolean entered=false;

    private WglContext(){}
    static boolean create(){
        if(context!=null)return true;
        HMODULE hModule=Kernel32.INSTANCE.GetModuleHandle(null);
        HINSTANCE hInstance=new HINSTANCE();
        hInstance.setPointer(hModule.getPointer());
        HWND window=User32.INSTANCE.CreateWindowEx(0,"STATIC","",WS_POPUP,0,0,1,1,null,null,hInstance,null);
        if(window==null){
            NobodyLib.LOGGER.error("Overlay: could not create the helper window for OpenGL, error "+Kernel32.INSTANCE.GetLastError());
            return false;
        }
        HDC hdc=User32.INSTANCE.GetDC(window);
        if(hdc==null){
            NobodyLib.LOGGER.error("Overlay: GetDC failed for the helper window");
            return false;
        }

        // PIXELFORMATDESCRIPTOR, 40 bytes: only the fields that matter are filled in.
        Memory pfd=new Memory(40);
        pfd.clear();
        pfd.setShort(0,(short)40);                          // nSize
        pfd.setShort(2,(short)1);                           // nVersion
        pfd.setInt(4,PFD_DRAW_TO_WINDOW|PFD_SUPPORT_OPENGL);// dwFlags
        pfd.setByte(8,(byte)0);                             // iPixelType = PFD_TYPE_RGBA
        pfd.setByte(9,(byte)32);                            // cColorBits
        pfd.setByte(16,(byte)8);                            // cAlphaBits
        pfd.setByte(23,(byte)24);                           // cDepthBits
        int format=Gdi.INSTANCE.ChoosePixelFormat(hdc.getPointer(),pfd);
        if(format==0||!Gdi.INSTANCE.SetPixelFormat(hdc.getPointer(),format,pfd)){
            NobodyLib.LOGGER.error("Overlay: no usable pixel format for OpenGL, error "+Kernel32.INSTANCE.GetLastError());
            return false;
        }
        Pointer created=Wgl.INSTANCE.wglCreateContext(hdc.getPointer());
        if(created==null){
            NobodyLib.LOGGER.error("Overlay: wglCreateContext failed, error "+Kernel32.INSTANCE.GetLastError());
            return false;
        }
        dc=hdc.getPointer();
        context=created;

        if(!makeCurrent()){
            context=null;
            return false;
        }
        try{
            if(GL.getFunctionProvider()==null)GL.create();
            capabilities=GL.createCapabilities();
        }finally{
            restore();
        }
        return true;
    }

    static GLCapabilities capabilities(){return capabilities;}

    /** Makes the overlay's context current on this thread, remembering what was current before. */
    static boolean makeCurrent(){
        if(context==null||entered)return false;
        savedDc=Wgl.INSTANCE.wglGetCurrentDC();
        savedContext=Wgl.INSTANCE.wglGetCurrentContext();
        savedCapabilities=null;
        try{
            savedCapabilities=GL.getCapabilities();
        }catch(IllegalStateException noCapabilitiesYet){
        }
        if(!Wgl.INSTANCE.wglMakeCurrent(dc,context))return false;
        entered=true;
        if(capabilities!=null)GL.setCapabilities(capabilities);
        return true;
    }

    /** Puts back whatever {@link #makeCurrent()} replaced. */
    static void restore(){
        if(!entered)return;
        entered=false;
        Wgl.INSTANCE.wglMakeCurrent(savedDc,savedContext);
        GL.setCapabilities(savedCapabilities);
        savedDc=null;
        savedContext=null;
        savedCapabilities=null;
    }
}