package io.github.nobodyasidentity.lib.client.overlay;

import io.github.nobodyasidentity.lib.NobodyLib;
import io.github.nobodyasidentity.lib.config.NobodyLibConfigs;
import net.minecraft.client.Minecraft;
import java.util.Arrays;

public final class OverlayDisplay{
    private static final String LAYER_ID=NobodyLib.MOD_ID+":display";
    private static final FpsTracker tracker=new FpsTracker();
    private OverlayDisplay(){}

    public static void register(){
        Overlay.register(LAYER_ID,OverlayDisplay::render);
    }
    public static void unregister() {
        Overlay.unregister(LAYER_ID);
        tracker.reset();
    }
    private static void render(int width,int height){
        Minecraft mc=Minecraft.getInstance();
        int currentFps=mc.getFps();
        tracker.record(currentFps);
        int low1pc=tracker.get1PercentLow();
        int avg=tracker.getAverage();
        int med=tracker.getMedian();
        int fpsCap=mc.options.framerateLimit().get();
        String capString;
        if(fpsCap>=260){
            capString="∞ (Inf)";
        }else{
            capString=String.valueOf(fpsCap);
        }
        if(mc.options.enableVsync().get()){
            capString+=" [VSync]";
        }
        String text=String.format("1%% Low: %d | Avg: %d | Med: %d\nFPS: %d | Cap: %s",low1pc,avg,med,currentFps,capString);
        float pixelWidth;
        try{
            pixelWidth=((float)mc.getWindow().getGuiScale())*NobodyLibConfigs.CLIENT.get().overlay.display_scale;
        }catch(Exception e){
            pixelWidth=2f;
        }
        float padding=6f;
        float textWidth=OverlayFont.width(text,pixelWidth);
        float textHeight=OverlayFont.height(text,pixelWidth);

        OverlayGraphics.fillRect(padding-4,padding-4,textWidth+8,textHeight+8,0x90000000);
        OverlayFont.drawText(text,padding,padding,pixelWidth,0xFFFFFFFF);
    }
    private static class FpsTracker{
        private final int[] history=new int[300];
        private int index=0;
        private int total=0;
        public synchronized void record(int fps){
            history[index]=fps;
            index=(index+1)%history.length;
            if(total<history.length)total++;
        }
        public synchronized void reset(){
            index=0;
            total=0;
        }
        private int[] getSortedCopy(){
            if(total==0)return new int[]{0};
            int[] copy=Arrays.copyOf(history,total);
            Arrays.sort(copy);
            return copy;
        }
        public synchronized int get1PercentLow(){
            int[] sorted=getSortedCopy();
            int targetIdx=(int)(sorted.length*0.01);
            return sorted[targetIdx];
        }
        public synchronized int getAverage(){
            if(total==0)return 0;
            long sum=0;
            for(int i=0;i<total;i++)sum+=history[i];
            return (int)(sum/total);
        }
        public synchronized int getMedian(){
            int[] sorted=getSortedCopy();
            return sorted[sorted.length/2];
        }
    }
}