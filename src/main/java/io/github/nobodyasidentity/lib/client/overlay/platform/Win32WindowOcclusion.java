package io.github.nobodyasidentity.lib.client.overlay.platform;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.RECT;
import org.lwjgl.glfw.GLFWNativeWin32;

public final class Win32WindowOcclusion{
    private Win32WindowOcclusion(){}
    private interface User32Ext extends Library{
        User32Ext INSTANCE=Native.load("user32",User32Ext.class);
        boolean IsIconic(HWND hWnd);
        HWND WindowFromPoint(POINT.ByValue point);
    }
    public static boolean isMinimizedOrFullyCovered(long mcGlfwHandle){
        long hwndPtr=GLFWNativeWin32.glfwGetWin32Window(mcGlfwHandle);
        HWND hwnd=new HWND(Pointer.createConstant(hwndPtr));
        if(User32Ext.INSTANCE.IsIconic(hwnd))return true;
        RECT rect=new RECT();
        if(!User32.INSTANCE.GetWindowRect(hwnd,rect))return false;
        int[][] samplePoints={
                {rect.left+1,rect.top+1},
                {rect.right-1,rect.top+1},
                {rect.left+1,rect.bottom-1},
                {rect.right-1,rect.bottom-1},
                {(rect.left+rect.right)/2,(rect.top+rect.bottom)/2}
        };
        for(int[] p:samplePoints){
            POINT.ByValue point=new POINT.ByValue();
            point.x=p[0];
            point.y=p[1];
            HWND topWindow=User32Ext.INSTANCE.WindowFromPoint(point);
            if(topWindow!=null&&topWindow.getPointer().equals(hwnd.getPointer())){
                return false;
            }
        }
        return true;
    }
}