package io.github.nobodyasidentity.lib.client.config;

import io.github.nobodyasidentity.lib.config.ClientConfig;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;

public class ClientConfigScreen{
    public static MutableComponent translateable(String name){
        return NobodyLibConfigScreen.translateable("client."+name);
    }
    public static void init(ConfigCategory client,Screen parent,ClientConfig config,ConfigBuilder builder,ConfigEntryBuilder entryBuilder){
        SubCategoryBuilder overlay=entryBuilder.startSubCategory(translateable("overlay"));
        SubCategoryBuilder debug=entryBuilder.startSubCategory(translateable("debug"));
        BooleanListEntry overlayToggle=entryBuilder.startBooleanToggle(translateable("option.overlay"),config.overlay.enabled)
                .setDefaultValue(false)
                .setTooltip(translateable("option.overlay.desc"))
                .setSaveConsumer(value->config.overlay.enabled=value)
                .build();
        overlay.setExpanded(true).add(overlayToggle);
        debug.setExpanded(false).add(
            entryBuilder.startBooleanToggle(translateable("option.debug.overlay_outline"),config.overlay.outline_enabled)
                .setDefaultValue(false)
                .setTooltip(translateable("option.debug.overlay_outline.desc"))
                .setSaveConsumer(value->config.overlay.outline_enabled=value)
                .setDisplayRequirement(Requirement.isTrue(overlayToggle::getValue))
                .build()
        );
        client.addEntry(overlay.build());
        client.addEntry(debug.build());
    }
}