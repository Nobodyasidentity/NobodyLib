package io.github.nobodyasidentity.nobodylib.client.config;

import io.github.nobodyasidentity.lib.client.keybind.KeyBindCreator;
import io.github.nobodyasidentity.lib.util.Py;
import io.github.nobodyasidentity.nobodylib.client.overlay.OverlayDisplay;
import io.github.nobodyasidentity.nobodylib.client.overlay.OverlayKeybinds;
import io.github.nobodyasidentity.nobodylib.config.ClientConfig;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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
        BooleanListEntry overlayDisplayToggle=entryBuilder.startBooleanToggle(translateable("option.overlay.display"),config.overlay.display_enabled)
            .setDefaultValue(false)
            .setTooltip(translateable("option.overlay.display.desc"))
            .setSaveConsumer(value->{
                config.overlay.display_enabled=value;
                if(value)OverlayDisplay.register();
                else{OverlayDisplay.unregister();}
            })
            .setRequirement(Requirement.isTrue(overlayToggle::getValue))
            .build();
        overlay.setExpanded(true).add(overlayToggle);
        overlay.add(
            entryBuilder.startKeyCodeField(Component.translatable(OverlayKeybinds.toggleVisibility.getName()),KeyBindCreator.getKey(OverlayKeybinds.toggleVisibility))
                .setDefaultValue(OverlayKeybinds.toggleVisibility.getDefaultKey())
                .setTooltip(Component.translatable(OverlayKeybinds.toggleVisibility.getName()+".desc"))
                .setKeySaveConsumer(value->{
                    OverlayKeybinds.toggleVisibility.setKey(value);
                })
                .setRequirement(Requirement.isTrue(overlayToggle::getValue))
                .build()
        );
        overlay.add(overlayDisplayToggle);
        overlay.add(
            entryBuilder.startFloatField(translateable("option.overlay.display_scale"),config.overlay.display_scale)
                .setDefaultValue(1.0f)
                .setTooltip(translateable("option.overlay.display_scale.desc"))
                .setSaveConsumer(value->config.overlay.display_scale=value)
                .setRequirement(Requirement.isTrue(()->Py.all(overlayDisplayToggle.getValue(),overlayToggle.getValue())))
                .build()
        );
        debug.setExpanded(false).add(
            entryBuilder.startBooleanToggle(translateable("option.debug.overlay_outline"),config.overlay.outline_enabled)
                .setDefaultValue(false)
                .setTooltip(translateable("option.debug.overlay_outline.desc"))
                .setSaveConsumer(value->config.overlay.outline_enabled=value)
                .setRequirement(Requirement.isTrue(overlayToggle::getValue))
                .build()
        );
        client.addEntry(overlay.build());
        client.addEntry(debug.build());
    }
}