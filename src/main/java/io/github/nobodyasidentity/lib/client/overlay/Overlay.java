package io.github.nobodyasidentity.lib.client.overlay;

import io.github.nobodyasidentity.lib.NobodyLib;
import io.github.nobodyasidentity.lib.client.overlay.platform.RawOverlayWindow;
import io.github.nobodyasidentity.lib.client.overlay.platform.Win32LayeredWindow;
import io.github.nobodyasidentity.lib.config.ConfigManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

public final class Overlay{
    private static OverlayRenderer.Content content=(w,h)->{};
    private static boolean initialized=false;
    private Overlay(){}
    public static boolean isInitialized(){return initialized;}
    public static void init(){
        if(initialized)return;
        initialized=true;

        if(!ConfigManager.get().overlay.enabled){return;}

        long mcHandle=Minecraft.getInstance().getWindow().handle();

        OverlayWindow.create(mcHandle,1,1);

        if(NobodyLib.OS.contains("win")){
            RawOverlayWindow.create(0,0,1,1);
            Win32LayeredWindow.init(RawOverlayWindow.getHwnd());
        }else{
            ClickThrough.apply(OverlayWindow.getHandle());
            OverlayWindow.show();
        }

        OverlaySync.register(mcHandle);
        ClientTickEvents.END_CLIENT_TICK.register(client->OverlayRenderer.renderFrame(mcHandle,content));
    }
    public static void setContent(OverlayRenderer.Content newContent){
        content=newContent;
    }
}