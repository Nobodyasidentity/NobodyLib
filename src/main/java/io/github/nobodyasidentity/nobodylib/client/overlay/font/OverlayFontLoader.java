package io.github.nobodyasidentity.nobodylib.client.overlay.font;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.nobodyasidentity.lib.client.overlay.font.LayeredFont;
import io.github.nobodyasidentity.lib.client.overlay.font.OverlayFontProvider;
import io.github.nobodyasidentity.nobodylib.NobodyLib;

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
            float scale=entry.has("scale")?entry.get("scale").getAsFloat():1f;

            switch(type){
                case "builtin" -> providers.add(new BitmapFontProvider(scale));
                case "nfont" -> {
                    String file=entry.get("file").getAsString();
                    try(InputStream in=OverlayFontLoader.class.getResourceAsStream(basePath+file)){
                        if(in==null)throw new IOException("not found: "+basePath+file);
                        providers.add(NFontProvider.load(in,scale));
                    }catch(IOException|RuntimeException e){
                        NobodyLib.LOGGER.warn("Font provider '"+file+"' in "+jsonResourcePath+" failed to load, skipping it: "+e.getMessage());
                    }
                }
                default -> NobodyLib.LOGGER.warn("Unknown font provider type '"+type+"' in "+jsonResourcePath+", skipping it");
            }
        }
        if(providers.isEmpty()){
            NobodyLib.LOGGER.warn("Font "+jsonResourcePath+" has no usable providers, using the built-in font");
            providers.add(new BitmapFontProvider(1f));
        }
        return new LayeredFont(providers);
    }
}