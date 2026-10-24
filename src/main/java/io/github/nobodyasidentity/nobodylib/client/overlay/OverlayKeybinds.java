package io.github.nobodyasidentity.nobodylib.client.overlay;

import io.github.nobodyasidentity.lib.client.overlay.Overlay;

import io.github.nobodyasidentity.nobodylib.NobodyLib;
import io.github.nobodyasidentity.lib.client.keybind.KeyBindCreator;
import net.minecraft.client.KeyMapping;

public final class OverlayKeybinds{
    public static KeyMapping toggleVisibility;
    private OverlayKeybinds(){}

    public static void register(){
        KeyMapping.Category category=KeyBindCreator.createCategory(NobodyLib.MOD_ID,"client");
        
        toggleVisibility=KeyBindCreator.create(NobodyLib.MOD_ID,"toggle_overlay","f9",category);
        KeyBindCreator.registerGlobalEvent(toggleVisibility,client->{Overlay.toggleVisible();});
    }
}