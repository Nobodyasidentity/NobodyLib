package io.github.nobodyasidentity.lib.client.overlay;

public final class BitmapFontProvider implements OverlayFontProvider{
    private final float pixelSize;
    public BitmapFontProvider(float pixelSize){
        this.pixelSize=pixelSize;
    }
    @Override
    public boolean hasGlyph(int codePoint){
        return codePoint<=0xFFFF&&OverlayFont.hasGlyph((char)codePoint);
    }
    @Override
    public float charWidth(int codePoint){
        return (OverlayFont.GLYPH_WIDTH+1)*pixelSize;
    }
    @Override
    public float lineHeight(){
        return OverlayFont.GLYPH_HEIGHT*pixelSize;
    }
    @Override
    public void drawChar(int codePoint,float x,float y,int argb){
        if(codePoint>0xFFFF)return;
        OverlayFont.drawText(String.valueOf((char)codePoint),x,y,pixelSize,argb);
    }
}