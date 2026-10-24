package io.github.nobodyasidentity.nobodylib.client.overlay.platform;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.BLENDFUNCTION;
import com.sun.jna.platform.win32.WinUser.SIZE;
import com.sun.jna.ptr.PointerByReference;
import io.github.nobodyasidentity.nobodylib.NobodyLib;
import org.lwjgl.system.MemoryUtil;

final class Win32LayeredWindow{
    private static HWND hwnd;
    private static HDC screenDC;
    private static HDC memDC;
    private static HBITMAP bitmap;
    private static long bitsAddress;
    private static int bitmapWidth=-1;
    private static int bitmapHeight=-1;

    // reused every frame instead of allocating new ones
    private static final POINT dst=new POINT(0,0);
    private static final POINT src=new POINT(0,0);
    private static final SIZE size=new SIZE(0,0);
    private static final BLENDFUNCTION blend=new BLENDFUNCTION();
    private static boolean loggedFailure=false;

    private Win32LayeredWindow(){}

    static void init(HWND rawHwnd){
        hwnd=rawHwnd;
        screenDC=User32.INSTANCE.GetDC(null);
        memDC=GDI32.INSTANCE.CreateCompatibleDC(screenDC);
        blend.BlendOp=WinUser.AC_SRC_OVER;
        blend.BlendFlags=0;
        blend.SourceConstantAlpha=(byte)255;
        blend.AlphaFormat=WinUser.AC_SRC_ALPHA;
    }

    /** @param bgraPremultiplied address of width*height*4 bytes, bottom row first (the order glReadPixels gives) */
    static void present(int x,int y,int width,int height,long bgraPremultiplied){
        if(width<=0||height<=0||hwnd==null)return;
        if(!ensureBitmap(width,height))return;

        MemoryUtil.memCopy(bgraPremultiplied,bitsAddress,(long)width*height*4);

        dst.x=x;
        dst.y=y;
        size.cx=width;
        size.cy=height;
        boolean ok=User32.INSTANCE.UpdateLayeredWindow(hwnd,screenDC,dst,size,memDC,src,0,blend,WinUser.ULW_ALPHA);
        if(!ok&&!loggedFailure){
            loggedFailure=true;
            NobodyLib.LOGGER.error("UpdateLayeredWindow failed, error "+Kernel32.INSTANCE.GetLastError());
        }
    }

    private static boolean ensureBitmap(int width,int height){
        if(width==bitmapWidth&&height==bitmapHeight&&bitmap!=null)return true;
        if(bitmap!=null){
            GDI32.INSTANCE.DeleteObject(bitmap);
            bitmap=null;
        }
        BITMAPINFO bmi=new BITMAPINFO();
        bmi.bmiHeader.biWidth=width;
        bmi.bmiHeader.biHeight=height;
        bmi.bmiHeader.biPlanes=1;
        bmi.bmiHeader.biBitCount=32;
        bmi.bmiHeader.biCompression=WinGDI.BI_RGB;
        bmi.bmiHeader.biSizeImage=width*height*4;

        PointerByReference ppBits=new PointerByReference();
        bitmap=GDI32.INSTANCE.CreateDIBSection(memDC,bmi,WinGDI.DIB_RGB_COLORS,ppBits,null,0);
        if(bitmap==null){
            NobodyLib.LOGGER.error("CreateDIBSection failed, error "+Kernel32.INSTANCE.GetLastError());
            return false;
        }
        bitsAddress=Pointer.nativeValue(ppBits.getValue());
        GDI32.INSTANCE.SelectObject(memDC,bitmap);
        bitmapWidth=width;
        bitmapHeight=height;
        return true;
    }
}