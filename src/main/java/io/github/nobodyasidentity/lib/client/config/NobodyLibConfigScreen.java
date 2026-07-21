package io.github.nobodyasidentity.lib.client.config;

import io.github.nobodyasidentity.lib.NobodyLib;
import io.github.nobodyasidentity.lib.config.ClientConfig;
import io.github.nobodyasidentity.lib.config.NobodyLibConfigs;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class NobodyLibConfigScreen{
    private NobodyLibConfigScreen(){}

    public static MutableComponent translateable(String name){
        return Component.translatable("config."+NobodyLib.MOD_ID+"."+name);
    }

    public static Screen create(Screen parent){
        ClientConfig config=NobodyLibConfigs.CLIENT.get();
        ConfigBuilder builder=ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal(NobodyLib.NAME))
                .setSavingRunnable(NobodyLibConfigs.CLIENT::save);

        ConfigEntryBuilder entryBuilder=ConfigEntryBuilder.create();

        ConfigCategory client=builder.getOrCreateCategory(translateable("client"));
        ClientConfigScreen.init(client,parent,config,builder,entryBuilder);

        ConfigCategory server=builder.getOrCreateCategory(translateable("server"));
        ServerConfigScreen.init(server,parent,config,builder,entryBuilder);

        ConfigCategory core=builder.getOrCreateCategory(translateable("core"));
        CoreConfigScreen.init(core,parent,config,builder,entryBuilder);

        return builder.build();
    }
}