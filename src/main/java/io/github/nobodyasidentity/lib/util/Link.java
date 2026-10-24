package io.github.nobodyasidentity.lib.util;

import java.net.URI;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;

public class Link{
    public static final void open(Screen parent,URI url){
        ConfirmLinkScreen.confirmLinkNow(parent,url);
    }
    public static final void open(Screen parent,String url){
        open(parent,create(url));
    }
    public static final URI create(String url){
        return URI.create(url);
    }
}