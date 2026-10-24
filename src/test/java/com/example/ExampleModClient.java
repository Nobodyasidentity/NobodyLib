package com.example;

import io.github.nobodyasidentity.lib.client.overlay.Overlay;
import io.github.nobodyasidentity.lib.util.OverlayProjection;
//import io.github.nobodyasidentity.lib.util.Py;
//import net.minecraft.world.entity.player.Player;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import static org.lwjgl.opengl.GL11.*;

public class ExampleModClient implements ClientModInitializer{
    @Override
    public void onInitializeClient(){
        Overlay.register("esp_example",(width,height)->{
            Minecraft mc=Minecraft.getInstance();
            Entity self=null;
            if(mc.player!=null)self=mc.player.getLivingEntity();
            if(mc.player==null||mc.level==null)return;
            float tickDelta=mc.getDeltaTracker().getGameTimeDeltaPartialTick(true);
            for(Entity entity:mc.level.entitiesForRendering()){
                if(!entity.equals(self)){
                    double x=entity.xo+(entity.getX()-entity.xo)*tickDelta;
                    double y=entity.yo+(entity.getY()-entity.yo)*tickDelta;
                    double z=entity.zo+(entity.getZ()-entity.zo)*tickDelta;
                    OverlayProjection.ScreenPos[]corners=OverlayProjection.getRotatedBoundingBox(x,y,z,entity.getBbWidth(),entity.getBbHeight(),0.0f,width,height);
                    if(corners!=null)draw3DBox(corners);
                }
            }
        });
    }
    private static void draw3DBox(OverlayProjection.ScreenPos[]c){
        glDisable(GL_TEXTURE_2D);
        glLineWidth(2.0f);
        glColor4f(1.0f,0.0f,0.0f,1.0f);
        glBegin(GL_LINES);
        for(int i=0;i<4;i++){
            int next=(i+1)%4;
            glVertex2f((float)c[i].x,(float)c[i].y);glVertex2f((float)c[next].x,(float)c[next].y);
            glVertex2f((float)c[i+4].x,(float)c[i+4].y);glVertex2f((float)c[next+4].x,(float)c[next+4].y);
            glVertex2f((float)c[i].x,(float)c[i].y);glVertex2f((float)c[i+4].x,(float)c[i+4].y);
        }
        glEnd();
        glColor4f(1.0f,1.0f,1.0f,1.0f);
    }
}