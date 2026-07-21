package io.github.nobodyasidentity.lib;

import net.fabricmc.api.ClientModInitializer;
import io.github.nobodyasidentity.lib.client.ClientLoadEvent;
import io.github.nobodyasidentity.lib.client.overlay.Overlay;
import io.github.nobodyasidentity.lib.config.NobodyLibConfigs;

public class NobodyLibClient implements ClientModInitializer{
    @Override
    public void onInitializeClient(){
        NobodyLib.LOGGER.info("Initializing "+NobodyLib.NAME+" Client...");
        NobodyLibConfigs.CLIENT.get();
		NobodyLibConfigs.CLIENT.save();
        ClientLoadEvent.register(client->{
            Overlay.init();
        });
        NobodyLib.LOGGER.info(NobodyLib.NAME+" Client initialized.");
    }   
}