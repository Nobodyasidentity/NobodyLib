package io.github.nobodyasidentity.nobodylib.client.overlay.font;

import io.github.nobodyasidentity.lib.client.overlay.OverlayGraphics;

import java.util.HashMap;
import java.util.Map;

public final class OverlayFont{
    private OverlayFont(){}
    public static final int GLYPH_WIDTH=5;
    public static final int GLYPH_HEIGHT=7;
    public static final int LINE_SPACING=2; 
    public static final char FALLBACK_CHAR='�';

    public static final Map<Character,int[]>GLYPHS=new HashMap<>();
        static{
            put(FALLBACK_CHAR,0b11111,0b10001,0b10001,0b10001,0b10001,0b10001,0b11111);
            DefaultFont.init();
    }
    public static void put(char c,int r0,int r1,int r2,int r3,int r4,int r5,int r6){
        GLYPHS.put(c,new int[]{r0,r1,r2,r3,r4,r5,r6});
    }
    public static boolean hasGlyph(char c){
        return GLYPHS.containsKey(c);
    }
    public static void drawText(String text,float x,float y,float pixelSize,int argb){
        float cursorX=x;
        float cursorY=y;
        
        for(int i=0;i<text.length();i++){
            char c=text.charAt(i);
            if(c=='\n'){
                cursorX=x;
                cursorY+=(GLYPH_HEIGHT+LINE_SPACING)*pixelSize;
                continue;
            }
            int[] glyph=GLYPHS.get(c);
            if (glyph!=null){
                drawGlyph(glyph,cursorX,cursorY,pixelSize,argb);
            } else {
                drawGlyph(GLYPHS.get(FALLBACK_CHAR),cursorX,cursorY,pixelSize,argb);
            }
            cursorX+=(GLYPH_WIDTH+1)*pixelSize;
        }
    }
    public static float width(String text,float pixelWidth) {
        int maxLen=0;
        String[] lines=text.split("\n",-1);
        for (String line:lines){
            if(line.length()>maxLen){
                maxLen=line.length();
            }
        }
        return maxLen*(GLYPH_WIDTH+1)*pixelWidth;
    }
    public static float height(String text,float pixelSize){
    int lineCount=text.length()-text.replace("\n","").length()+1;
    float totalGlyphHeight=lineCount*GLYPH_HEIGHT*pixelSize;
    float totalLineSpacing=(lineCount-1)*LINE_SPACING*pixelSize;
    return totalGlyphHeight+totalLineSpacing;
}
    private static void drawGlyph(int[] glyph,float x,float y,float pixelSize,int argb){
        for (int row=0;row<GLYPH_HEIGHT;row++){
            int bits=glyph[row];
            for(int col=0;col<GLYPH_WIDTH;col++){
                boolean on=((bits>>(GLYPH_WIDTH-1-col))&1)!=0;
                if(on){
                    OverlayGraphics.fillRect(x+col*pixelSize,y+row*pixelSize,pixelSize,pixelSize,argb);
                }
            }
        }
    }
}