package io.github.nobodyasidentity.nobodylib.client.overlay.platform;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.RECT;

/**
 * The few user32 calls the overlay needs that JNA's stock User32 doesn't declare (or declares awkwardly),
 * plus helpers for finding and measuring Minecraft's window.
 */
final class Win32Native{
    private Win32Native(){}

    interface User32X extends Library{
        User32X INSTANCE=Native.load("user32",User32X.class);
        boolean IsIconic(HWND hWnd);
        HWND WindowFromPoint(POINT.ByValue point);
        boolean GetClientRect(HWND hWnd,RECT rect);
        boolean ClientToScreen(HWND hWnd,POINT point);
    }

    /** The HWND of Minecraft's window, or null. */
    static HWND hwndOf(long mcWindowHandle){
        long raw=SdlNative.win32Hwnd(mcWindowHandle);
        if(raw==0L)return null;
        HWND hwnd=new HWND();
        hwnd.setPointer(Pointer.createConstant(raw));
        return hwnd;
    }

    /** {x, y, width, height} of a window's client area in screen pixels, or null if it can't be read. */
    static int[] clientBounds(HWND hwnd){
        RECT rect=new RECT();
        if(!User32X.INSTANCE.GetClientRect(hwnd,rect))return null;
        POINT origin=new POINT(0,0);
        if(!User32X.INSTANCE.ClientToScreen(hwnd,origin))return null;
        return new int[]{origin.x,origin.y,rect.right-rect.left,rect.bottom-rect.top};
    }
}