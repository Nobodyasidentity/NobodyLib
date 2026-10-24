package io.github.nobodyasidentity.nobodylib.client.overlay;

import io.github.nobodyasidentity.lib.client.overlay.Overlay;
import io.github.nobodyasidentity.nobodylib.config.NobodyLibConfigs;
import org.lwjgl.opengl.GL14;

import static org.lwjgl.opengl.GL11.*;

/** Draws one frame of the overlay into whatever framebuffer is currently bound. Shared by all backends. */
public final class OverlayFrame{
    private OverlayFrame(){}

    public static boolean isOutlineEnabled(){
        return NobodyLibConfigs.CLIENT.get().overlay.outline_enabled;
    }

    public static void render(int width,int height){
        glViewport(0,0,width,height);
        glClearColor(0f,0f,0f,0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glEnable(GL_BLEND);
        // Blend into *premultiplied* alpha. That is what Windows' UpdateLayeredWindow and desktop compositors
        // expect, so nothing has to be converted on the CPU afterwards.
        GL14.glBlendFuncSeparate(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA,GL_ONE,GL_ONE_MINUS_SRC_ALPHA);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0,width,height,0,-1,1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glColor4f(1f,1f,1f,1f);

        Overlay.renderLayers(width,height);
        if(isOutlineEnabled())drawDebugOutline(width,height);
    }

    private static void drawDebugOutline(int width,int height){
        glDisable(GL_BLEND);
        glLineWidth(2f);
        glColor4f(1f,0f,0f,1f);
        glBegin(GL_LINE_LOOP);
        glVertex2f(1,1);
        glVertex2f(width-1,1);
        glVertex2f(width-1,height-1);
        glVertex2f(1,height-1);
        glEnd();
        glColor4f(1f,1f,1f,1f);
        glEnable(GL_BLEND);
    }
}