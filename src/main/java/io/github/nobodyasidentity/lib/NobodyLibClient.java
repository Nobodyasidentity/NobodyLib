package io.github.nobodyasidentity.lib;

import net.fabricmc.api.ClientModInitializer;

import io.github.nobodyasidentity.lib.client.ClientLoadEvent;
import io.github.nobodyasidentity.lib.client.overlay.Overlay;

public class NobodyLibClient implements ClientModInitializer{
    @Override
    public void onInitializeClient(){
        NobodyLib.LOGGER.info("Initializing "+NobodyLib.NAME+" Client...");
        ClientLoadEvent.register(client->{
            Overlay.init();
        });
        NobodyLib.LOGGER.info(NobodyLib.NAME+" Client initialized.");
    }   
}