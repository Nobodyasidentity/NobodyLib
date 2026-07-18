package io.github.nobodyasidentity.lib.client.config;

import io.github.nobodyasidentity.lib.NobodyLib;
import io.github.nobodyasidentity.lib.config.ConfigManager;
import io.github.nobodyasidentity.lib.config.NobodyLibConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class NobodyLibConfigScreen{
    private NobodyLibConfigScreen(){}
    public static Screen create(Screen parent){
        NobodyLibConfig config=ConfigManager.get();

        ConfigBuilder builder=ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal(NobodyLib.NAME))
                .setSavingRunnable(ConfigManager::save);

        ConfigEntryBuilder entryBuilder=ConfigEntryBuilder.create();
        ConfigCategory client=builder.getOrCreateCategory(Component.literal("Client"));
        SubCategoryBuilder overlay=entryBuilder.startSubCategory(Component.literal("Overlay"));
        overlay.setExpanded(true).add(
            entryBuilder.startBooleanToggle(Component.literal("§cEnable Overlay (Currently does not work)"),config.overlay.enabled)
                .setDefaultValue(false)
                .setTooltip(Component.literal("Enables the experimental click-through overlay window - requires restart."))
                .setSaveConsumer(value->config.overlay.enabled=value)
                .build()
        );
        client.addEntry(overlay.build());
        return builder.build();
    }
}