package io.github.nobodyasidentity.lib.client.overlay.platform;

import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.BLENDFUNCTION;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinUser.SIZE;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;
import java.nio.ByteBuffer;

public final class Win32LayeredWindow{
    private static HWND hwnd;
    private static HDC screenDC;
    private static HDC memDC;
    private static HBITMAP bitmap;
    private static Pointer bits;
    private static int bitmapWidth=-1;
    private static int bitmapHeight=-1;

    private Win32LayeredWindow(){}

    public static void init(HWND rawHwnd){
        hwnd=rawHwnd;
        screenDC=User32.INSTANCE.GetDC(null);
        memDC=GDI32.INSTANCE.CreateCompatibleDC(screenDC);
    }
    public static void present(int x,int y,int width,int height,ByteBuffer bgraStraightAlpha){
        if(width<=0||height<=0||hwnd==null)return;
        ensureBitmap(width,height);

        int length=width*height*4;
        byte[] pixels=new byte[length];
        bgraStraightAlpha.position(0);
        bgraStraightAlpha.get(pixels,0,length);
        premultiply(pixels);
        bits.write(0,pixels,0,length);

        HANDLE oldBitmap=GDI32.INSTANCE.SelectObject(memDC,bitmap);

        POINT pos=new POINT(x,y);
        SIZE size=new SIZE(width,height);
        POINT srcPos=new POINT(0,0);

        BLENDFUNCTION blend=new BLENDFUNCTION();
        blend.BlendOp=WinUser.AC_SRC_OVER;
        blend.BlendFlags=0;
        blend.SourceConstantAlpha=(byte)255;
        blend.AlphaFormat=WinUser.AC_SRC_ALPHA;

        boolean ok=User32.INSTANCE.UpdateLayeredWindow(hwnd,screenDC,pos,size,memDC,srcPos,0,blend,WinUser.ULW_ALPHA);
        if(!ok){
            System.err.println("[NobodyLib] UpdateLayeredWindow failed, error="+Kernel32.INSTANCE.GetLastError());
        }
        GDI32.INSTANCE.SelectObject(memDC,oldBitmap);
    }

    private static void premultiply(byte[] bgra){
        for(int i=0;i<bgra.length;i+=4){
            int a=bgra[i+3]&0xFF;
            if(a==255)continue;
            int b=bgra[i]&0xFF;
            int g=bgra[i+1]&0xFF;
            int r=bgra[i+2]&0xFF;
            bgra[i]=(byte)(b*a/255);
            bgra[i+1]=(byte)(g*a/255);
            bgra[i+2]=(byte)(r*a/255);
        }
    }
    private static void ensureBitmap(int width,int height){
        if(width==bitmapWidth&&height==bitmapHeight&&bitmap!=null)return;
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
            System.err.println("[NobodyLib] CreateDIBSection returned null, error="+Kernel32.INSTANCE.GetLastError());
        }
        bits=ppBits.getValue();
        bitmapWidth=width;
        bitmapHeight=height;
    }
}