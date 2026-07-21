package io.github.nobodyasidentity.lib.client.overlay.platform;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import org.lwjgl.glfw.GLFWNativeX11;

public final class X11ClickThrough{
    private static final int SHAPE_INPUT=2;
    private static final int SHAPE_SET=0;

    private X11ClickThrough(){}

    private interface XExt extends Library{
        XExt INSTANCE=Native.load("Xext",XExt.class);
        void XShapeCombineRectangles(Pointer display,long window,int destKind,int xOff,int yOff,Pointer rects,int nRects,int op,int ordering);
    }
    public static void apply(long glfwHandle){
        long display=GLFWNativeX11.glfwGetX11Display();
        long window=GLFWNativeX11.glfwGetX11Window(glfwHandle);

        Pointer displayPtr=new Pointer(display);
        XExt.INSTANCE.XShapeCombineRectangles(displayPtr,window,SHAPE_INPUT,0,0,Pointer.NULL,0,SHAPE_SET,0);
    }
}