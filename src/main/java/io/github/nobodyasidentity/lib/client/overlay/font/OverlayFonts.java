package io.github.nobodyasidentity.lib.client.overlay.font;

import io.github.nobodyasidentity.nobodylib.NobodyLib;
import io.github.nobodyasidentity.nobodylib.client.overlay.font.BitmapFontProvider;
import io.github.nobodyasidentity.nobodylib.client.overlay.font.OverlayFontLoader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OverlayFonts{
    public static final String DEFAULT_DESCRIPTOR="/assets/nobodylib/font/default.json";
    private static final Map<String,LayeredFont> cache=new HashMap<>();

    private OverlayFonts(){}
    public static LayeredFont getDefault(){
        return load(DEFAULT_DESCRIPTOR);
    }
    public static LayeredFont load(String descriptorResourcePath){
        return cache.computeIfAbsent(descriptorResourcePath,path->{
            try{
                return OverlayFontLoader.loadResource(path);
            }catch(IOException|RuntimeException e){
                NobodyLib.LOGGER.error("Could not load overlay font '"+path+"', using the built-in font instead",e);
                return new LayeredFont(List.of(new BitmapFontProvider(1f)));
            }
        });
    }
}