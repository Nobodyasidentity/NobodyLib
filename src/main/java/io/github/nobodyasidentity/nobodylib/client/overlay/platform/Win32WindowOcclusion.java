package io.github.nobodyasidentity.nobodylib.client.overlay.platform;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.RECT;

final class Win32WindowOcclusion{
    private Win32WindowOcclusion(){}

    static boolean isMinimizedOrFullyCovered(HWND hwnd){
        if(Win32Native.User32X.INSTANCE.IsIconic(hwnd))return true;

        RECT rect=new RECT();
        if(!User32.INSTANCE.GetWindowRect(hwnd,rect))return false;

        int[][]samplePoints={
                {rect.left+1,rect.top+1},
                {rect.right-1,rect.top+1},
                {rect.left+1,rect.bottom-1},
                {rect.right-1,rect.bottom-1},
                {(rect.left+rect.right)/2,(rect.top+rect.bottom)/2}
        };

        for(int[]p:samplePoints){
            POINT.ByValue point=new POINT.ByValue();
            point.x=p[0];
            point.y=p[1];
            HWND topWindow=Win32Native.User32X.INSTANCE.WindowFromPoint(point);
            if(topWindow!=null&&topWindow.getPointer().equals(hwnd.getPointer()))return false;
        }
        return true;
    }
}