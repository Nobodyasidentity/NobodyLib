package io.github.nobodyasidentity.nobodylib.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.mojang.blaze3d.platform.InputConstants;
import io.github.nobodyasidentity.nobodylib.NobodyLib;
import io.github.nobodyasidentity.lib.util.Link;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.FormattedText;
import java.net.URI;
import java.util.Optional;

public class ModMenuIntegration implements ModMenuApi{
    @Override
    public ConfigScreenFactory<?>getModConfigScreenFactory(){
        if(!FabricLoader.getInstance().isModLoaded("cloth-config"))return EmptyConfigScreen::new;
        return NobodyLibConfigScreen::create;
    }
    public static class EmptyConfigScreen extends Screen{
        private final Component infoText;
        private int textX;
        private int textY;

        protected EmptyConfigScreen(Screen parent){
            super(Component.literal("Missing Dependency"));
            this.infoText=Component.translatable(
                "info."+NobodyLib.MOD_ID+".no_config",
                Component.translatable("info."+NobodyLib.MOD_ID+".no_config.name")
                    .withStyle(style->style
                        .withUnderlined(true)
                        .withColor(ChatFormatting.GREEN)
                        .withClickEvent(new ClickEvent.OpenUrl(URI.create(I18n.get("info."+NobodyLib.MOD_ID+".no_config.url"))))
                    )
            );
        }
        @Override
        protected void init(){
            super.init();
            int textWidth=this.font.width(this.infoText);
            this.textX=(this.width-textWidth)/2;
            this.textY=this.height/2;
            StringWidget textWidget=new StringWidget(this.textX,this.textY,textWidth,this.font.lineHeight,this.infoText,this.font);
            this.addRenderableWidget(textWidget);
        }
        @Override
        public boolean mouseClicked(MouseButtonEvent event,boolean doubleClick){
            double mouseX=event.x();
            double mouseY=event.y();
            if(event.button()==InputConstants.MOUSE_BUTTON_LEFT){
                if(mouseY>=this.textY&&mouseY<=this.textY+this.font.lineHeight){
                    int relativeMouseX=(int)mouseX-this.textX;
                    String plainText=this.infoText.getString();
                    int charIndex=this.font.getSplitter().plainIndexAtWidth(plainText,relativeMouseX,Style.EMPTY);
                    if(charIndex>=0&&charIndex<plainText.length()){
                        Style style=getStyleAtIndex(this.infoText,charIndex);
                        if(style!=null&&style.getClickEvent()!=null){
                            if(style.getClickEvent()instanceof ClickEvent.OpenUrl openUrl){
                                Link.open(this,openUrl.uri());
                                return true;
                            }
                        }
                    }
                }
            }
            return super.mouseClicked(event,doubleClick);
        }
        private Style getStyleAtIndex(FormattedText text,int targetIndex){
            int[]currentIndex=new int[]{0};
            Optional<Style>foundStyle=text.visit((style,string)->{
                int len=string.length();
                if(currentIndex[0]+len>targetIndex){
                    return Optional.of(style);
                }
                currentIndex[0]+=len;
                return Optional.empty();
            }, Style.EMPTY);
            return foundStyle.orElse(Style.EMPTY);
        }
    }
}