package io.github.nobodyasidentity.lib.config;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ConfigManager{
    private static final Map<String,ConfigFile<?>>files=new LinkedHashMap<>();
    private ConfigManager(){}
    public static <T>ConfigFile<T>register(String id,Class<T>type){
        if(files.containsKey(id)){
            throw new IllegalStateException("Config category '"+id+"' is already registered");
        }
        ConfigFile<T> file=new ConfigFile<>(id,type);
        files.put(id,file);
        return file;
    }
    public static void saveAll(){
        for(ConfigFile<?>file:files.values())file.save();
    }
}