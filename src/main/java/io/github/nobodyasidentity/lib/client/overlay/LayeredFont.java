package io.github.nobodyasidentity.lib.client.overlay;

import java.util.List;

public final class LayeredFont{
    private final List<OverlayFontProvider> providers;

    public LayeredFont(List<OverlayFontProvider> providers){
        this.providers=providers;
    }

    private OverlayFontProvider providerFor(int codePoint){
        for(OverlayFontProvider p:providers){
            if(p.hasGlyph(codePoint))return p;
        }
        return providers.isEmpty()?null:providers.get(providers.size()-1);
    }

    public void drawText(String text,float x,float y,int argb){
        float cursorX=x;
        float cursorY=y;
        int i=0;
        while(i<text.length()){
            int codePoint=text.codePointAt(i);
            int count=Character.charCount(codePoint);
            if(codePoint=='\n'){
                cursorX=x;
                cursorY+=lineHeight();
            }else{
                OverlayFontProvider provider=providerFor(codePoint);
                if(provider!=null){
                    provider.drawChar(codePoint,cursorX,cursorY,argb);
                    cursorX+=provider.charWidth(codePoint);
                }
            }
            i+=count;
        }
    }

    public float width(String text){
        float maxWidth=0;
        float current=0;
        int i=0;
        while(i<text.length()){
            int codePoint=text.codePointAt(i);
            if(codePoint=='\n'){
                maxWidth=Math.max(maxWidth,current);
                current=0;
            }else{
                OverlayFontProvider provider=providerFor(codePoint);
                if(provider!=null)current+=provider.charWidth(codePoint);
            }
            i+=Character.charCount(codePoint);
        }
        return Math.max(maxWidth,current);
    }

    public float height(String text){
        int lines=1;
        for(int i=0;i<text.length();i++){
            if(text.charAt(i)=='\n')lines++;
        }
        return lines*lineHeight();
    }

    public float lineHeight(){
        float max=0;
        for(OverlayFontProvider p:providers)max=Math.max(max,p.lineHeight());
        return max;
    }
}