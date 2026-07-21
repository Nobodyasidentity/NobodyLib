package io.github.nobodyasidentity.lib.client.config;

import io.github.nobodyasidentity.lib.config.ClientConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;

public class ServerConfigScreen{
    public static MutableComponent translateable(String name){
        return NobodyLibConfigScreen.translateable("server."+name);
    }
    public static void init(ConfigCategory server,Screen parent,ClientConfig config,ConfigBuilder builder,ConfigEntryBuilder entryBuilder){
        SubCategoryBuilder debug=entryBuilder.startSubCategory(translateable("debug"));
        debug.setExpanded(false);
        server.addEntry(debug.build());
    }
}