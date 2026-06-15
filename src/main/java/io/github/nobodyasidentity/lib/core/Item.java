package io.github.nobodyasidentity.lib.core;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class Item extends net.minecraft.world.item.Item{
    public Item(Properties properties){super(properties);}
    private static <T extends net.minecraft.world.item.Item> T register(String name,T item){
        return Registry.register(BuiltInRegistries.ITEM,Identifier.fromNamespaceAndPath(Mod.modid(),name),item);
    }
    public static Item create(String name){return create(name,new Properties());}
    public static Item create(String name,Properties properties){
        properties.setId(ResourceKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath(Mod.modid(),name)));
        Item item=new Item(properties);
        return register(name,item);
    }
}