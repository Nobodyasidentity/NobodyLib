package io.github.nobodyasidentity.lib.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CustomScreen extends Screen{
    public CustomScreen(){super(Component.empty());}
    public CustomScreen(Component title){super(title);}
}