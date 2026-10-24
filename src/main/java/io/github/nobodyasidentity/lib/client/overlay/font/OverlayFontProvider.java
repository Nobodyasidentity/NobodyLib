package io.github.nobodyasidentity.lib.client.overlay.font;

public interface OverlayFontProvider{
    boolean hasGlyph(int codePoint);
    float charWidth(int codePoint);
    float lineHeight();
    void drawChar(int codePoint,float x,float y,int argb);
}