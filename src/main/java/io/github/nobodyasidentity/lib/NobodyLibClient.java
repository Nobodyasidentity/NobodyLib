package io.github.nobodyasidentity.lib;

import net.fabricmc.api.ClientModInitializer;
//import io.github.nobodyasidentity.lib.client.ClientLoadEvent;
//import net.minecraft.client.Minecraft;
//import com.mojang.blaze3d.platform.Window;

public class NobodyLibClient implements ClientModInitializer{
    @Override
    public void onInitializeClient(){
        NobodyLib.LOGGER.info("Initializing "+NobodyLib.NAME+" Client...");
        //ClientLoadEvent.register(client->{
        //    Window window=ClientLoadEvent.Minecraft().getWindow();
        //});
    }
    
}