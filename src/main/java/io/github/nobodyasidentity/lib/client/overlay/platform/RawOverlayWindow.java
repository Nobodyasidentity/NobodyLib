package io.github.nobodyasidentity.lib.client.overlay.platform;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HINSTANCE;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser.WNDCLASSEX;
import com.sun.jna.platform.win32.WinUser.WindowProc;

public final class RawOverlayWindow{
    private static final String CLASS_NAME="NobodyLibOverlayWindowClass";

    private static final int WS_POPUP=0x80000000;
    private static final int WS_EX_LAYERED=0x00080000;
    private static final int WS_EX_TRANSPARENT=0x00000020;
    private static final int WS_EX_TOOLWINDOW=0x00000080;
    private static final int WS_EX_NOACTIVATE=0x08000000;
    private static final int WS_EX_TOPMOST=0x00000008;
    private static final int SW_SHOWNOACTIVATE=4;
    private static final WindowProc WND_PROC=(hwnd,uMsg,wParam,lParam)->User32.INSTANCE.DefWindowProc(hwnd,uMsg,wParam,lParam);

    private static HWND hwnd;
    private static int lastX,lastY;

    private RawOverlayWindow(){}

    public static void create(int x,int y,int width,int height){
        if(hwnd!=null)return;

        HMODULE hModule=Kernel32.INSTANCE.GetModuleHandle(null);
        HINSTANCE hInstance=new HINSTANCE();
        hInstance.setPointer(hModule.getPointer());

        WNDCLASSEX wClass=new WNDCLASSEX();
        wClass.hInstance=hModule;
        wClass.lpfnWndProc=WND_PROC;
        wClass.lpszClassName=CLASS_NAME;
        User32.INSTANCE.RegisterClassEx(wClass);

        int exStyle=WS_EX_LAYERED|WS_EX_TRANSPARENT|WS_EX_TOOLWINDOW|WS_EX_NOACTIVATE|WS_EX_TOPMOST;
        hwnd=User32.INSTANCE.CreateWindowEx(exStyle,CLASS_NAME,"",WS_POPUP,x,y,Math.max(width,1),Math.max(height,1),null,null,hInstance,null);
        if(hwnd==null){
            System.err.println("[NobodyLib] CreateWindowEx failed, error="+Kernel32.INSTANCE.GetLastError());
            return;
        }

        lastX=x;
        lastY=y;
        User32.INSTANCE.ShowWindow(hwnd,SW_SHOWNOACTIVATE);
    }
    public static void setBounds(int x,int y,int width,int height){
        if(hwnd==null)return;
        lastX=x;
        lastY=y;
    }
    public static HWND getHwnd(){return hwnd;}
    public static int getX(){return lastX;}
    public static int getY(){return lastY;}
}