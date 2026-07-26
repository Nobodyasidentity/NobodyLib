package io.github.nobodyasidentity.lib.client.overlay;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL11.*;

public final class OverlayTrueTypeFont implements OverlayFontProvider{
    private final STBTTFontinfo fontInfo;
    //private final ByteBuffer fontBuffer;
    private final STBTTBakedChar.Buffer bakedChars;
    private final int textureId;
    private final int atlasSize;
    private final int firstCodepoint;
    private final int numCodepoints;
    private final float pixelHeight;
    private final float scale;
    private final float ascentPixels;

    private OverlayTrueTypeFont(STBTTFontinfo fontInfo,ByteBuffer fontBuffer,STBTTBakedChar.Buffer bakedChars,int textureId,int atlasSize,int firstCodepoint,int numCodepoints,float pixelHeight){
        this.fontInfo=fontInfo;
        //this.fontBuffer=fontBuffer;
        this.bakedChars=bakedChars;
        this.textureId=textureId;
        this.atlasSize=atlasSize;
        this.firstCodepoint=firstCodepoint;
        this.numCodepoints=numCodepoints;
        this.pixelHeight=pixelHeight;
        this.scale=STBTruetype.stbtt_ScaleForPixelHeight(fontInfo,pixelHeight);

        try(MemoryStack stack=MemoryStack.stackPush()){
            IntBuffer ascent=stack.mallocInt(1);
            STBTruetype.stbtt_GetFontVMetrics(fontInfo,ascent,null,null);
            this.ascentPixels=ascent.get(0)*scale;
        }
    }
    public static OverlayTrueTypeFont loadResource(String resourcePath,float pixelHeight) throws IOException{
        return loadResource(resourcePath,pixelHeight,32,95,512);
    }
    public static OverlayTrueTypeFont loadResource(String resourcePath,float pixelHeight,int firstCodepoint,int numCodepoints,int atlasSize) throws IOException{
        try(InputStream in=OverlayTrueTypeFont.class.getResourceAsStream(resourcePath)){
            if(in==null)throw new IOException("Font resource not found: "+resourcePath);
            return load(in.readAllBytes(),pixelHeight,firstCodepoint,numCodepoints,atlasSize);
        }
    }
    public static OverlayTrueTypeFont loadFile(Path path,float pixelHeight) throws IOException{
        return load(Files.readAllBytes(path),pixelHeight,32,95,512);
    }
    public static OverlayTrueTypeFont loadFile(Path path,float pixelHeight,int firstCodepoint,int numCodepoints,int atlasSize) throws IOException{
        return load(Files.readAllBytes(path),pixelHeight,firstCodepoint,numCodepoints,atlasSize);
    }
    public static OverlayTrueTypeFont load(byte[] ttfBytes,float pixelHeight,int firstCodepoint,int numCodepoints,int atlasSize){
        ByteBuffer fontBuffer=BufferUtils.createByteBuffer(ttfBytes.length);
        fontBuffer.put(ttfBytes);
        fontBuffer.flip();

        STBTTFontinfo fontInfo=STBTTFontinfo.create();
        if(!STBTruetype.stbtt_InitFont(fontInfo,fontBuffer)){
            throw new IllegalStateException("stbtt_InitFont failed — not a valid TTF/OTF file?");
        }

        ByteBuffer bitmap=BufferUtils.createByteBuffer(atlasSize*atlasSize);
        STBTTBakedChar.Buffer chars=STBTTBakedChar.malloc(numCodepoints);

        int result=STBTruetype.stbtt_BakeFontBitmap(fontBuffer,pixelHeight,bitmap,atlasSize,atlasSize,firstCodepoint,chars);
        if(result<=0){
            throw new IllegalStateException("Font atlas too small (pixelHeight="+pixelHeight+", numCodepoints="+numCodepoints
                    +", atlasSize="+atlasSize+") — try a smaller pixelHeight, fewer codepoints, or a bigger atlasSize");
        }
        int textureId=glGenTextures();
        glBindTexture(GL_TEXTURE_2D,textureId);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D,0,GL_ALPHA,atlasSize,atlasSize,0,GL_ALPHA,GL_UNSIGNED_BYTE,bitmap);

        return new OverlayTrueTypeFont(fontInfo,fontBuffer,chars,textureId,atlasSize,firstCodepoint,numCodepoints,pixelHeight);
    }
    @Override
    public boolean hasGlyph(int codePoint){
        if(codePoint<firstCodepoint||codePoint>=firstCodepoint+numCodepoints)return false;
        return STBTruetype.stbtt_FindGlyphIndex(fontInfo,codePoint)!=0;
    }
    @Override
    public float charWidth(int codePoint){
        try(MemoryStack stack=MemoryStack.stackPush()){
            IntBuffer advance=stack.mallocInt(1);
            STBTruetype.stbtt_GetCodepointHMetrics(fontInfo,codePoint,advance,null);
            return advance.get(0)*scale;
        }
    }
    @Override
    public float lineHeight(){
        return pixelHeight;
    }
    @Override
    public void drawChar(int codePoint,float x,float y,int argb){
        if(codePoint<firstCodepoint||codePoint>=firstCodepoint+numCodepoints)return;

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D,textureId);
        setColor(argb);

        try(MemoryStack stack=MemoryStack.stackPush()){
            FloatBuffer xpos=stack.floats(x);
            FloatBuffer ypos=stack.floats(y+ascentPixels);
            STBTTAlignedQuad quad=STBTTAlignedQuad.malloc(stack);

            STBTruetype.stbtt_GetBakedQuad(bakedChars,atlasSize,atlasSize,codePoint-firstCodepoint,xpos,ypos,quad,true);
            glBegin(GL_QUADS);
            glTexCoord2f(quad.s0(),quad.t0()); glVertex2f(quad.x0(),quad.y0());
            glTexCoord2f(quad.s1(),quad.t0()); glVertex2f(quad.x1(),quad.y0());
            glTexCoord2f(quad.s1(),quad.t1()); glVertex2f(quad.x1(),quad.y1());
            glTexCoord2f(quad.s0(),quad.t1()); glVertex2f(quad.x0(),quad.y1());
            glEnd();
        }

        glColor4f(1f,1f,1f,1f);
        glDisable(GL_TEXTURE_2D);
    }
    public void drawText(String text,float x,float y,int argb){
        float cursorX=x;
        int i=0;
        while(i<text.length()){
            int codePoint=text.codePointAt(i);
            drawChar(codePoint,cursorX,y,argb);
            cursorX+=charWidth(codePoint);
            i+=Character.charCount(codePoint);
        }
    }
    public float width(String text){
        float w=0;
        int i=0;
        while(i<text.length()){
            int codePoint=text.codePointAt(i);
            w+=charWidth(codePoint);
            i+=Character.charCount(codePoint);
        }
        return w;
    }
    public float pixelHeight(){return pixelHeight;}
    private static void setColor(int argb){
        float a=((argb>>>24)&0xFF)/255f;
        float r=((argb>>16)&0xFF)/255f;
        float g=((argb>>8)&0xFF)/255f;
        float b=(argb&0xFF)/255f;
        glColor4f(r,g,b,a);
    }
}