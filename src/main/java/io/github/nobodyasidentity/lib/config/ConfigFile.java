package io.github.nobodyasidentity.lib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import io.github.nobodyasidentity.lib.NobodyLib;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigFile<T>{
    private static final Gson GSON=new GsonBuilder().setPrettyPrinting().create();

    private final String id;
    private final Class<T> type;
    private final Path path;
    private T value;

    ConfigFile(String id,Class<T> type){
        this.id=id;
        this.type=type;
        this.path=FabricLoader.getInstance().getConfigDir().resolve(NobodyLib.MOD_ID).resolve(id+".json");
    }
    public String id(){
        return id;
    }
    public T get(){
        if(value==null)load();
        return value;
    }
    public void load(){
        T loaded=null;
        if(Files.exists(path)){
            try(Reader reader=Files.newBufferedReader(path)){
                loaded=GSON.fromJson(reader,type);
            }catch(IOException|JsonParseException e){
                NobodyLib.LOGGER.error("Failed to load config '"+id+"', falling back to defaults",e);
            }
        }
        value=loaded!=null?loaded:newDefault();
    }
    public void save(){
        try{
            Files.createDirectories(path.getParent());
            try(Writer writer=Files.newBufferedWriter(path)){
                GSON.toJson(get(),writer);
            }
        }catch(IOException e){
            NobodyLib.LOGGER.error("Failed to save config '"+id+"'",e);
        }
    }
    private T newDefault(){
        try{
            return type.getDeclaredConstructor().newInstance();
        }catch(ReflectiveOperationException e){
            throw new RuntimeException("Config class "+type.getName()+" needs a public no-args constructor",e);
        }
    }
}