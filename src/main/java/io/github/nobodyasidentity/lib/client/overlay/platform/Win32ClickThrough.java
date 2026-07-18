package io.github.nobodyasidentity.lib.client.overlay.platform;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import org.lwjgl.glfw.GLFWNativeWin32;

public final class Win32ClickThrough{
    private static final int GWL_EXSTYLE=-20;
    private static final int WS_EX_TOOLWINDOW=0x00000080;

    private Win32ClickThrough(){}

    public static void apply(long glfwHandle){
        long hwndPtr=GLFWNativeWin32.glfwGetWin32Window(glfwHandle);
        WinDef.HWND hwnd=new WinDef.HWND(Pointer.createConstant(hwndPtr));

        int exStyle=User32.INSTANCE.GetWindowLong(hwnd,GWL_EXSTYLE);
        User32.INSTANCE.SetWindowLong(hwnd,GWL_EXSTYLE,exStyle|WS_EX_TOOLWINDOW);
    }
}