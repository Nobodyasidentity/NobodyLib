package io.github.nobodyasidentity.lib.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GenProvider implements DataProvider{
    public enum Type {ASSET,DATA}
    private record Entry(Type type,String path,String content){}
    private final FabricPackOutput output;
    private final List<Entry> entries=new ArrayList<>();
    public GenProvider(FabricPackOutput output){
        this.output=output;
    }
    public GenProvider write(Type type,String path,String content){
        entries.add(new Entry(type,path,content));
        return this;
    }
    @Override
    public CompletableFuture<?> run(CachedOutput cache){
        List<CompletableFuture<?>> futures=new ArrayList<>();
        for (Entry e:entries){
            Path resolved=output.getOutputFolder().resolve(e.path());
            futures.add(DataProvider.saveStable(cache,com.google.gson.JsonParser.parseString(e.content()),resolved));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }
    @Override
    public String getName(){return "NobodyLib GenProvider";}
    public static GenProvider register(FabricDataGenerator.Pack pack,FabricPackOutput output){
        GenProvider provider=new GenProvider(output);
        pack.addProvider((o,r)->provider);
        return provider;
    }
}