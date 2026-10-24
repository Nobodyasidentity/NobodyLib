package io.github.nobodyasidentity.nobodylib.client.overlay.platform;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

final class X11ClickThrough{
    private static final int SHAPE_INPUT=2;
    private static final int SHAPE_SET=0;

    private X11ClickThrough(){}
    private interface XExt extends Library{
        XExt INSTANCE=Native.load("Xext",XExt.class);
        void XShapeCombineRectangles(Pointer display,long window,int destKind,int xOff,int yOff,Pointer rects,int nRects,int op,int ordering);
    }
    static void apply(long x11Display,long x11Window){
        if(x11Display==0L||x11Window==0L)return;
        XExt.INSTANCE.XShapeCombineRectangles(new Pointer(x11Display),x11Window,SHAPE_INPUT,0,0,Pointer.NULL,0,SHAPE_SET,0);
    }
}