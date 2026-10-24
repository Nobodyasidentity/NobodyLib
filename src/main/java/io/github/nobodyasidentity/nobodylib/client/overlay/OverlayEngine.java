package io.github.nobodyasidentity.nobodylib.client.overlay;

import io.github.nobodyasidentity.lib.client.overlay.Overlay;
import io.github.nobodyasidentity.nobodylib.NobodyLib;
import io.github.nobodyasidentity.nobodylib.client.overlay.platform.SdlOverlay;
import io.github.nobodyasidentity.nobodylib.client.overlay.platform.Win32Overlay;
import io.github.nobodyasidentity.nobodylib.config.NobodyLibConfigs;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

public final class OverlayEngine{
    private static boolean initialized=false;
    private static OverlayBackend backend;
    private static boolean currentlyShown=true;
    private static boolean minecraftCovered=false;
    private static int coverCheckCounter=0;

    private OverlayEngine(){}

    public static boolean isInitialized(){return initialized;}

    public static void init(){
        if(initialized)return;
        initialized=true;
        if(!NobodyLibConfigs.CLIENT.get().overlay.enabled){return;}
        long mcHandle=Minecraft.getInstance().getWindow().handle();
        try{
            OverlayBackend candidate=NobodyLib.OS.contains("win")?new Win32Overlay():new SdlOverlay();
            if(!candidate.init(mcHandle)){
                NobodyLib.LOGGER.error("The overlay could not start on this system and is disabled.");
                return;
            }
            backend=candidate;
        }catch(Throwable t){
            NobodyLib.LOGGER.error("The overlay failed to start and is disabled.",t);
            return;
        }
        ClientTickEvents.END_CLIENT_TICK.register(client->tick(mcHandle));
    }

    private static void tick(long mcHandle){
        OverlayBackend current=backend;
        if(current==null)return;
        try{
            if((coverCheckCounter++)%5==0){
                minecraftCovered=current.isMinecraftCovered(mcHandle);
            }
            boolean shouldShow=Overlay.isVisible()&&!minecraftCovered;
            if(shouldShow!=currentlyShown){
                currentlyShown=shouldShow;
                current.setVisible(shouldShow);
            }
            if(shouldShow)current.tick(mcHandle);
        }catch(Throwable t){
            NobodyLib.LOGGER.error("The overlay hit an error and has been turned off until the game restarts.",t);
            backend=null;
        }
    }
}