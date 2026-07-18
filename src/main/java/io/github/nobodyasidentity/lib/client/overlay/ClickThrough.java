package io.github.nobodyasidentity.lib.client.overlay;

import io.github.nobodyasidentity.lib.NobodyLib;
import io.github.nobodyasidentity.lib.client.overlay.platform.MacClickThrough;
import io.github.nobodyasidentity.lib.client.overlay.platform.Win32ClickThrough;
import io.github.nobodyasidentity.lib.client.overlay.platform.X11ClickThrough;

public final class ClickThrough{
    private ClickThrough(){}
    public static void apply(long glfwHandle){
        if(NobodyLib.OS.contains("win")){
            Win32ClickThrough.apply(glfwHandle);
        }else if(NobodyLib.OS.contains("mac")||NobodyLib.OS.contains("darwin")){
            MacClickThrough.apply(glfwHandle);
        }else{
            X11ClickThrough.apply(glfwHandle);
        }
    }
}