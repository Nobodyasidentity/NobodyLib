package io.github.nobodyasidentity.lib.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStarted;

public class ClientLoadEvent{
    public static void register(ClientStarted listener){
        ClientLifecycleEvents.CLIENT_STARTED.register(listener);
    }
    public static net.minecraft.client.Minecraft Minecraft(){
         return net.minecraft.client.Minecraft.getInstance();
    }
}