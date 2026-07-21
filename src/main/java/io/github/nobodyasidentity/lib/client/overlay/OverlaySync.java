package io.github.nobodyasidentity.lib.client.overlay;

import io.github.nobodyasidentity.lib.NobodyLib;
import io.github.nobodyasidentity.lib.client.overlay.platform.RawOverlayWindow;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

public final class OverlaySync{
    private OverlaySync(){}
    public static void register(long mcWindowHandle){
        ClientTickEvents.END_CLIENT_TICK.register(client->sync(mcWindowHandle));
    }
    public static void sync(long mcWindowHandle){
        try(MemoryStack stack=MemoryStack.stackPush()){
            IntBuffer x=stack.mallocInt(1);
            IntBuffer y=stack.mallocInt(1);
            IntBuffer w=stack.mallocInt(1);
            IntBuffer h=stack.mallocInt(1);

            glfwGetWindowPos(mcWindowHandle,x,y);
            glfwGetWindowSize(mcWindowHandle,w,h);

            if(NobodyLib.OS.contains("win")){
                RawOverlayWindow.setBounds(x.get(0),y.get(0),w.get(0),h.get(0));
            }else{
                long overlay=OverlayWindow.getHandle();
                if(overlay==-1L)return;
                glfwSetWindowPos(overlay,x.get(0),y.get(0));
                glfwSetWindowSize(overlay,w.get(0),h.get(0));
            }
        }
    }
}