package io.github.nobodyasidentity.lib.client.overlay;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.nobodyasidentity.lib.NobodyLib;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class OverlayFontLoader{
    private OverlayFontLoader(){}

    public static LayeredFont loadResource(String jsonResourcePath) throws IOException{
        String basePath=jsonResourcePath.substring(0,jsonResourcePath.lastIndexOf('/')+1);
        JsonObject root;
        try(InputStream in=OverlayFontLoader.class.getResourceAsStream(jsonResourcePath)){
            if(in==null)throw new IOException("Font descriptor not found: "+jsonResourcePath);
            root=JsonParser.parseReader(new InputStreamReader(in,StandardCharsets.UTF_8)).getAsJsonObject();
        }

        List<OverlayFontProvider> providers=new ArrayList<>();
        for(JsonElement element:root.getAsJsonArray("providers")){
            JsonObject entry=element.getAsJsonObject();
            String type=entry.get("type").getAsString();

            switch(type){
                case "builtin" -> {
                    float size=entry.has("size")?entry.get("size").getAsFloat():2f;
                    providers.add(new BitmapFontProvider(size));
                }
                case "ttf" -> {
                    String file=entry.get("file").getAsString();
                    float size=entry.has("size")?entry.get("size").getAsFloat():16f;
                    int firstCodepoint=entry.has("first_codepoint")?entry.get("first_codepoint").getAsInt():32;
                    int numCodepoints=entry.has("num_codepoints")?entry.get("num_codepoints").getAsInt():95;
                    int atlasSize=entry.has("atlas_size")?entry.get("atlas_size").getAsInt():512;
                    try{
                        providers.add(OverlayTrueTypeFont.loadResource(basePath+file,size,firstCodepoint,numCodepoints,atlasSize));
                    }catch(IOException|IllegalStateException e){
                        NobodyLib.LOGGER.warn("Font provider '"+file+"' in "+jsonResourcePath+" failed to load, skipping it: "+e.getMessage());
                    }
                }
                default -> NobodyLib.LOGGER.warn("Unknown font provider type '"+type+"' in "+jsonResourcePath+", skipping it");
            }
        }

        return new LayeredFont(providers);
    }
}