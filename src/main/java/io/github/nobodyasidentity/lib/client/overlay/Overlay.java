package io.github.nobodyasidentity.lib.client.overlay;

import io.github.nobodyasidentity.lib.NobodyLib;
import io.github.nobodyasidentity.lib.client.overlay.platform.RawOverlayWindow;
import io.github.nobodyasidentity.lib.client.overlay.platform.Win32LayeredWindow;
import io.github.nobodyasidentity.lib.config.NobodyLibConfigs;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Overlay{
    private static final Map<String,Content> layers=new LinkedHashMap<>();
    private static boolean initialized=false;

    private Overlay(){}

    @FunctionalInterface
    public interface Content{
        void render(int width,int height);
    }

    public static boolean isInitialized(){return initialized;}

    public static void init(){
        if(initialized)return;
        initialized=true;
        if(!NobodyLibConfigs.CLIENT.get().overlay.enabled){return;}
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
        ClientTickEvents.END_CLIENT_TICK.register(client->OverlayRenderer.renderFrame(mcHandle));
    }
    public static void register(String id,Content content){
        layers.put(id,content);
    }
    public static void unregister(String id){
        layers.remove(id);
    }
    public static boolean isRegistered(String id){
        return layers.containsKey(id);
    }
    static void renderLayers(int width,int height){
        for(Content content:layers.values()){
            content.render(width,height);
        }
    }
}