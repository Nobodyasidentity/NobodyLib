package io.github.nobodyasidentity.lib.client.overlay;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Overlay{
    private static final Map<String,Content> layers=new LinkedHashMap<>();
    private static boolean userWantsVisible=true;

    private Overlay(){}

    @FunctionalInterface
    public interface Content{
        void render(int width,int height);
    }

    public static void register(String id,Content content){
        layers.put(id,content);
    }
    public static void unregister(String id){
        layers.remove(id);
    }
    public static boolean isRegistered(String id){
        return layers.containsKey(id);
    }
    public static boolean hasLayers(){
        return !layers.isEmpty();
    }
    /** Internal: called by the overlay renderer once per frame. Mods have no reason to call this. */
    public static void renderLayers(int width,int height){
        for(Content content:layers.values()){
            content.render(width,height);
        }
    }
    public static boolean isVisible(){return userWantsVisible;}
    public static void setVisible(boolean visible){
        userWantsVisible=visible;
    }
    public static void toggleVisible(){
        userWantsVisible=!userWantsVisible;
    }
}