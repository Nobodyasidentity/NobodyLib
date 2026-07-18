package io.github.nobodyasidentity.lib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.nobodyasidentity.lib.NobodyLib;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager{
    private static final Gson GSON=new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH=FabricLoader.getInstance().getConfigDir().resolve(NobodyLib.MOD_ID+"/config.json");

    private static NobodyLibConfig instance;
    private ConfigManager(){}

    public static NobodyLibConfig get(){
        if(instance==null)load();
        return instance;
    }

    public static void load(){
        NobodyLibConfig loaded=null;
        if (Files.exists(PATH)){
            try (Reader reader=Files.newBufferedReader(PATH)){
                loaded=GSON.fromJson(reader,NobodyLibConfig.class);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        instance=loaded!=null?loaded:new NobodyLibConfig();
    }
    public static void save(){
        try{
            Files.createDirectories(PATH.getParent());
            try(Writer writer=Files.newBufferedWriter(PATH)){
                GSON.toJson(get(), writer);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}