package io.github.nobodyasidentity.nobodylib.client.overlay.font;

import io.github.nobodyasidentity.lib.client.overlay.font.OverlayFontProvider;

/** The tiny built-in 5x7 font ({@link OverlayFont}). Always available, so it makes a good last fallback. */
public final class BitmapFontProvider implements OverlayFontProvider{
    private final float scale;

    /** @param scale size relative to normal (1 = one font pixel per bitmap pixel) */
    public BitmapFontProvider(float scale){
        this.scale=scale;
    }
    @Override
    public boolean hasGlyph(int codePoint){
        return codePoint<=0xFFFF&&OverlayFont.hasGlyph((char)codePoint);
    }
    @Override
    public float charWidth(int codePoint){
        return (OverlayFont.GLYPH_WIDTH+1)*scale;
    }
    @Override
    public float lineHeight(){
        return (OverlayFont.GLYPH_HEIGHT+OverlayFont.LINE_SPACING)*scale;
    }
    @Override
    public void drawChar(int codePoint,float x,float y,int argb){
        char c=codePoint<=0xFFFF?(char)codePoint:OverlayFont.FALLBACK_CHAR;
        OverlayFont.drawText(String.valueOf(c),x,y,scale,argb);
    }
}