package io.github.nobodyasidentity.lib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public final class OverlayProjection{
    public static class ScreenPos{
        public final double x,y;
        public ScreenPos(double x,double y){this.x=x;this.y=y;}
    }
    public static ScreenPos getWaypointScreenPos(double worldX,double worldY,double worldZ,int overlayWidth,int overlayHeight){
        Minecraft mc=Minecraft.getInstance();
        if(mc.player==null||mc.level==null)return null;
        Camera camera=mc.gameRenderer.mainCamera();
        Vec3 camPos=camera.position();
        float camYaw=camera.entity().getYRot();
        float camPitch=camera.entity().getXRot();
        if (camera.isDetached()&&mc.options.getCameraType().isMirrored()){
            camYaw+=180.0f;
            camPitch=-camPitch;
        }
        double currentFov=camera.getFov();
        float fovRad=(float)Math.toRadians(currentFov);
        float aspectRatio=(float)overlayWidth/(float)overlayHeight;
        Matrix4f mat=new Matrix4f();
        mat.perspective(fovRad,aspectRatio,0.05f,1000.0f);
        mat.rotateX((float)Math.toRadians(camPitch));
        mat.rotateY((float)Math.toRadians(camYaw+180.0f));
        Vector4f clipPos=new Vector4f((float)(worldX-camPos.x),(float)(worldY-camPos.y),(float)(worldZ-camPos.z),1.0f);
        mat.transform(clipPos);
        if (clipPos.w<=0.0f)return null;
        float ndcX=clipPos.x/clipPos.w;
        float ndcY=clipPos.y/clipPos.w;
        double sx=((ndcX+1.0)/2.0)*overlayWidth;
        double sy=((1.0-ndcY)/2.0)*overlayHeight;
        return new ScreenPos(sx,sy);
    }
    public static ScreenPos[] getRotatedBoundingBox(double targetX,double targetY,double targetZ,double width,double height,float targetYaw,float targetPitch,int overlayWidth,int overlayHeight){
        Minecraft mc=Minecraft.getInstance();
        if(mc.player==null||mc.level==null)return null;
        Camera camera=mc.gameRenderer.mainCamera();
        Vec3 camPos=camera.position();
        float camYaw=camera.entity().getYRot();
        float camPitch=camera.entity().getXRot();
        if(camera.isDetached()&&mc.options.getCameraType().isMirrored()){
            camYaw+=180.0f;
            camPitch=-camPitch;
        }
        double currentFov=camera.getFov();
        double hw=width/2.0;
        double[][] localCorners={
            {-hw,0,-hw},{hw,0,-hw},{hw,0,hw},{-hw,0,hw},
            {-hw,height,-hw},{hw,height,-hw},{hw,height,hw},{-hw,height,hw}
        };
        double targetYawRad=Math.toRadians(targetYaw+180.0);
        double cosE=Math.cos(targetYawRad);
        double sinE=Math.sin(targetYawRad);
        float fovRad=(float) Math.toRadians(currentFov);
        float aspectRatio=(float)overlayWidth/(float)overlayHeight;
        Matrix4f mat=new Matrix4f();
        mat.perspective(fovRad,aspectRatio,0.05f,1000.0f);
        mat.rotateX((float)Math.toRadians(camPitch));
        mat.rotateY((float)Math.toRadians(camYaw+180.0f));
        ScreenPos[] screenCorners=new ScreenPos[8];
        Vector4f clipPos=new Vector4f();
        for(int i=0;i<8;i++){
            double lx=localCorners[i][0];
            double ly=localCorners[i][1];
            double lz=localCorners[i][2];
            double rx=lx*cosE-lz*sinE;
            double rz=lx*sinE+lz*cosE;
            float x=(float)((targetX+rx)-camPos.x);
            float y=(float)((targetY+ly)-camPos.y);
            float z=(float)((targetZ+rz)-camPos.z);
            mat.transform(x,y,z,1.0f,clipPos);
            if(clipPos.w<=0.0f)return null;
            float ndcX=clipPos.x/clipPos.w;
            float ndcY=clipPos.y/clipPos.w;
            double sx=((ndcX+1.0)/2.0)*overlayWidth;
            double sy=((1.0-ndcY)/2.0)*overlayHeight;
            screenCorners[i]=new ScreenPos(sx,sy);
        }
        return screenCorners;
    }
    public static ScreenPos[] getRotatedBoundingBox(double targetX,double targetY,double targetZ,double width,double height,float targetYaw,int overlayWidth,int overlayHeight){
        return getRotatedBoundingBox(targetX,targetY,targetZ,width,height,targetYaw,0.0f,overlayWidth,overlayHeight);
    }
}