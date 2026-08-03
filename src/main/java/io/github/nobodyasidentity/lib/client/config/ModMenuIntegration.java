package io.github.nobodyasidentity.lib.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import io.github.nobodyasidentity.lib.NobodyLib;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
//import net.minecraft.network.chat.ClickEvent.OpenUrl;
//import java.net.URI;

public class ModMenuIntegration implements ModMenuApi{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory(){
        if (!FabricLoader.getInstance().isModLoaded("cloth-config")){
            return EmptyConfigScreen::new;
        }
        return NobodyLibConfigScreen::create;
    }
    public static class EmptyConfigScreen extends Screen{
        private final Component infoText;
        protected EmptyConfigScreen(Screen parent){
            super(Component.literal("Missing Dependency"));
            this.infoText=Component.translatable(
                "info."+NobodyLib.MOD_ID+".no_config",
                Component.literal("Cloth Config API")
                    .withStyle(
                        style->style // Note to self: change to underline and green once link works
                            .withUnderlined(false)
                            .withColor(ChatFormatting.RESET)
                    )
            );
        }
        @Override
        protected void init(){
            super.init();
            int textWidth=this.font.width(this.infoText);
            int x=(this.width-textWidth)/2;
            int y=this.height/2;
            StringWidget textWidget=new StringWidget(x,y,textWidth,this.font.lineHeight,this.infoText,this.font);
            this.addRenderableWidget(textWidget);
        }
    }
}