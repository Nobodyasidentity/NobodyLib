package io.github.nobodyasidentity.lib.client.overlay;

import io.github.nobodyasidentity.lib.NobodyLib;
import net.minecraft.client.Minecraft;

public final class OverlayDisplay{
    private static final String LAYER_ID=NobodyLib.MOD_ID+":display";
    private OverlayDisplay(){}
    public static void register(){
        Overlay.register(LAYER_ID,OverlayDisplay::render);
    }
    public static void unregister(){
        Overlay.unregister(LAYER_ID);
    }
    private static void render(int width,int height){
        Minecraft mc=Minecraft.getInstance();
        String text="FPS: {fps}"
            .replace("{fps}",String.valueOf(mc.getFps()));
        
        float pixelWidth;
        try{
            pixelWidth=(float)mc.getWindow().getGuiScale();
        }catch(Exception e){
            pixelWidth=2f;
        }
        float padding=6f;
        float textWidth=OverlayFont.width(text,pixelWidth);
        float textHeight=OverlayFont.height(text,pixelWidth);

        OverlayGraphics.fillRect(padding-4,padding-4,textWidth+8,textHeight+8,0x90000000);
        OverlayFont.drawText(text,padding,padding,pixelWidth,0xFFFFFFFF);
    }
}