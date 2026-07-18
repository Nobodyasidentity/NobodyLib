package io.github.nobodyasidentity.lib.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory(){
        if(!FabricLoader.getInstance().isModLoaded("cloth-config")){
            return null;
        }
        return NobodyLibConfigScreen::create;
    }
}