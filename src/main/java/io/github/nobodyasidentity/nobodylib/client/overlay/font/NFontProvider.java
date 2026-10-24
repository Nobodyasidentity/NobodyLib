package io.github.nobodyasidentity.nobodylib.client.overlay.font;

import io.github.nobodyasidentity.lib.client.overlay.font.OverlayFontProvider;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import static org.lwjgl.opengl.GL11.*;

public final class NFontProvider implements OverlayFontProvider{
    private static final float UNITS_PER_FONT_PIXEL=100f;
    private final float unitScale;
    private final float ascent;
    private final float lineHeight;
    private final int[]cellSizes;
    private final int[]codePoints;
    private final int[]cellIndex;
    private final int[]advance;
    private final int[]xOffset;
    private final int[]yOffset;
    private final int[]width;
    private final int[]height;
    private final int[]bitOffset;
    private final byte[]data;

    private final Map<Integer,float[]> rectCache=new HashMap<>();

    private NFontProvider(float scale,byte[] data) throws IOException{
        this.data=data;
        this.unitScale=scale/UNITS_PER_FONT_PIXEL;
        Reader r=new Reader(data);
        if(r.remaining()<5||data[0]!='N'||data[1]!='F'||data[2]!='N'||data[3]!='T')throw new IOException("not an .nfont file");
        r.pos=4;
        int version=r.u8();
        if(version!=1)throw new IOException("unsupported .nfont version "+version);
        r.u16();
        int ascentUnits=r.u16();
        int descentUnits=r.u16();
        this.ascent=ascentUnits*unitScale;
        this.lineHeight=(ascentUnits+descentUnits)*unitScale;
        int cellCount=r.u8();
        cellSizes=new int[cellCount];
        for(int i=0;i<cellCount;i++)cellSizes[i]=r.u16();
        int glyphCount=r.u32();
        if(glyphCount<0||glyphCount>0x110000)throw new IOException("bad glyph count "+glyphCount);

        codePoints=new int[glyphCount];
        cellIndex=new int[glyphCount];
        advance=new int[glyphCount];
        xOffset=new int[glyphCount];
        yOffset=new int[glyphCount];
        width=new int[glyphCount];
        height=new int[glyphCount];
        bitOffset=new int[glyphCount];
        int cp=0;
        for(int i=0;i<glyphCount;i++){
            cp+=r.varuint();
            codePoints[i]=cp;
            cellIndex[i]=r.u8();
            advance[i]=r.varuint();
            xOffset[i]=r.varint();
            yOffset[i]=r.varint();
            width[i]=r.u8();
            height[i]=r.u8();
            if(cellIndex[i]>=cellCount&&width[i]*height[i]>0)throw new IOException("bad cell size index at glyph "+i);
            bitOffset[i]=r.pos;
            r.skip((width[i]*height[i]+7)/8);
        }
    }

    /** Reads an {@code .nfont} file (GZIP-compressed). {@code scale} sizes this font relative to the others. */
    public static NFontProvider load(InputStream in,float scale) throws IOException{
        byte[] raw;
        try(GZIPInputStream gz=new GZIPInputStream(in)){
            raw=gz.readAllBytes();
        }
        return new NFontProvider(scale,raw);
    }
    public static NFontProvider load(byte[] gzipped,float scale) throws IOException{
        return load(new ByteArrayInputStream(gzipped),scale);
    }

    public int glyphCount(){return codePoints.length;}

    private int indexOf(int codePoint){
        return Arrays.binarySearch(codePoints,codePoint);
    }

    @Override
    public boolean hasGlyph(int codePoint){
        return indexOf(codePoint)>=0;
    }
    @Override
    public float charWidth(int codePoint){
        int i=indexOf(codePoint);
        return i<0?0f:advance[i]*unitScale;
    }
    @Override
    public float lineHeight(){
        return lineHeight;
    }
    @Override
    public void drawChar(int codePoint,float x,float y,int argb){
        int i=indexOf(codePoint);
        if(i<0||width[i]==0||height[i]==0)return;
        float[] rects=rectCache.computeIfAbsent(i,this::buildRects);
        glColor4f(((argb>>>16)&0xFF)/255f,((argb>>>8)&0xFF)/255f,(argb&0xFF)/255f,((argb>>>24)&0xFF)/255f);
        glBegin(GL_QUADS);
        for(int k=0;k<rects.length;k+=4){
            float x0=x+rects[k];
            float y0=y+rects[k+1];
            float x1=x+rects[k+2];
            float y1=y+rects[k+3];
            glVertex2f(x0,y0);
            glVertex2f(x1,y0);
            glVertex2f(x1,y1);
            glVertex2f(x0,y1);
        }
        glEnd();
        glColor4f(1f,1f,1f,1f);
    }

    private boolean bit(int glyph,int row,int col){
        int n=row*width[glyph]+col;
        return (data[bitOffset[glyph]+(n>>3)]>>(7-(n&7))&1)!=0;
    }

    /** Merges lit cells into rectangles, returned as {x0,y0,x1,y1,...} in font pixels relative to the line's top-left. */
    private float[] buildRects(int glyph){
        int w=width[glyph];
        int h=height[glyph];
        float cell=cellSizes[cellIndex[glyph]]*unitScale;
        boolean[] used=new boolean[w*h];
        float[] out=new float[16];
        int count=0;
        for(int row=0;row<h;row++){
            for(int col=0;col<w;col++){
                if(used[row*w+col]||!bit(glyph,row,col))continue;
                int runEnd=col+1;
                while(runEnd<w&&!used[row*w+runEnd]&&bit(glyph,row,runEnd))runEnd++;
                int rowEnd=row+1;
                while(rowEnd<h&&rowIsLit(glyph,rowEnd,col,runEnd,used))rowEnd++;
                for(int rr=row;rr<rowEnd;rr++)for(int cc=col;cc<runEnd;cc++)used[rr*w+cc]=true;

                if(count+4>out.length)out=Arrays.copyOf(out,out.length*2);
                out[count++]=(xOffset[glyph]+col)*cell;
                out[count++]=ascent-(yOffset[glyph]+h-row)*cell;
                out[count++]=(xOffset[glyph]+runEnd)*cell;
                out[count++]=ascent-(yOffset[glyph]+h-rowEnd)*cell;
            }
        }
        return Arrays.copyOf(out,count);
    }

    private boolean rowIsLit(int glyph,int row,int from,int to,boolean[] used){
        int w=width[glyph];
        for(int c=from;c<to;c++){
            if(used[row*w+c]||!bit(glyph,row,c))return false;
        }
        return true;
    }

    private static final class Reader{
        final byte[] d;
        int pos;
        Reader(byte[] d){this.d=d;}
        int remaining(){return d.length-pos;}
        void need(int n)throws IOException{
            if(n<0||pos+n>d.length)throw new IOException("truncated .nfont file");
        }
        void skip(int n)throws IOException{
            need(n);
            pos+=n;
        }
        int u8()throws IOException{
            need(1);
            return d[pos++]&0xFF;
        }
        int u16()throws IOException{
            need(2);
            int v=((d[pos]&0xFF)<<8)|(d[pos+1]&0xFF);
            pos+=2;
            return v;
        }
        int u32()throws IOException{
            need(4);
            int v=((d[pos]&0xFF)<<24)|((d[pos+1]&0xFF)<<16)|((d[pos+2]&0xFF)<<8)|(d[pos+3]&0xFF);
            pos+=4;
            return v;
        }
        int varuint()throws IOException{
            int value=0;
            int shift=0;
            while(true){
                int b=u8();
                value|=(b&0x7F)<<shift;
                if((b&0x80)==0)return value;
                shift+=7;
                if(shift>28)throw new IOException("bad varint in .nfont file");
            }
        }
        int varint()throws IOException{
            int n=varuint();
            return (n>>>1)^-(n&1);
        }
    }
}