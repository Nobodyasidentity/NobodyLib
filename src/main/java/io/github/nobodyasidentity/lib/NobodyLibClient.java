package io.github.nobodyasidentity.lib;

import net.fabricmc.api.ClientModInitializer;
import io.github.nobodyasidentity.lib.client.ClientLoadEvent;
import io.github.nobodyasidentity.lib.client.overlay.OverlayDisplay;
import io.github.nobodyasidentity.lib.client.overlay.Overlay;
import io.github.nobodyasidentity.lib.client.overlay.OverlayKeybinds;
import io.github.nobodyasidentity.lib.config.ClientConfig;
import io.github.nobodyasidentity.lib.config.NobodyLibConfigs;

public class NobodyLibClient implements ClientModInitializer{
    @Override
    public void onInitializeClient(){
        NobodyLib.LOGGER.info("Initializing "+NobodyLib.NAME+" Client...");
        ClientConfig config=NobodyLibConfigs.CLIENT.get();
        NobodyLibConfigs.CLIENT.get();
		NobodyLibConfigs.CLIENT.save();
        OverlayKeybinds.register();
        ClientLoadEvent.register(client->{
            Overlay.init();
            if(config.overlay.display_enabled)OverlayDisplay.register();
        });
        NobodyLib.LOGGER.info(NobodyLib.NAME+" Client initialized.");
    }   
}