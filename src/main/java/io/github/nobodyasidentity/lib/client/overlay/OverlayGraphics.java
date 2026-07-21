package io.github.nobodyasidentity.lib.client.overlay;

import static org.lwjgl.opengl.GL11.*;

public final class OverlayGraphics{
    private OverlayGraphics(){}

    public static void fillRect(float x,float y,float width,float height,float r,float g,float b,float a){
        glColor4f(r,g,b,a);
        glBegin(GL_QUADS);
        glVertex2f(x,y);
        glVertex2f(x+width,y);
        glVertex2f(x+width,y+height);
        glVertex2f(x,y+height);
        glEnd();
        resetColor();
    }
    public static void fillRect(float x,float y,float width,float height,int argb){
        fillRect(x,y,width,height,red(argb),green(argb),blue(argb),alpha(argb));
    }
    public static void drawRect(float x,float y,float width,float height,float lineWidth,float r,float g,float b,float a){
        glLineWidth(lineWidth);
        glColor4f(r,g,b,a);
        glBegin(GL_LINE_LOOP);
        glVertex2f(x,y);
        glVertex2f(x+width,y);
        glVertex2f(x+width,y+height);
        glVertex2f(x,y+height);
        glEnd();
        resetColor();
    }
    public static void drawRect(float x,float y,float width,float height,float lineWidth,int argb){
        drawRect(x,y,width,height,lineWidth,red(argb),green(argb),blue(argb),alpha(argb));
    }
    public static void drawLine(float x1,float y1,float x2,float y2,float lineWidth,float r,float g,float b,float a){
        glLineWidth(lineWidth);
        glColor4f(r,g,b,a);
        glBegin(GL_LINES);
        glVertex2f(x1,y1);
        glVertex2f(x2,y2);
        glEnd();
        resetColor();
    }
    public static void drawLine(float x1,float y1,float x2,float y2,float lineWidth,int argb){
        drawLine(x1,y1,x2,y2,lineWidth,red(argb),green(argb),blue(argb),alpha(argb));
    }
    private static void resetColor(){
        glColor4f(1f,1f,1f,1f);
    }
    private static float alpha(int argb){return((argb>>>24)&0xFF)/255f;}
    private static float red(int argb){return((argb>>16)&0xFF)/255f;}
    private static float green(int argb){return((argb>>8)&0xFF)/255f;}
    private static float blue(int argb){return(argb&0xFF)/255f;}
}