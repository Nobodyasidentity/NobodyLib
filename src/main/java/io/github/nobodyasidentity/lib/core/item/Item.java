package io.github.nobodyasidentity.lib.core.item;

import io.github.nobodyasidentity.lib.NobodyLib;
import io.github.nobodyasidentity.lib.core.sound.Sound;
import io.github.nobodyasidentity.lib.data.GenProvider;
import io.github.nobodyasidentity.lib.data.ItemModelGen;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class Item extends net.minecraft.world.item.Item{
    public static void init(){
        NobodyLib.LOGGER.info("Initializing items...");
        NobodyLib.LOGGER.info("Initialized items!");
    }
    public Item(net.minecraft.world.item.Item.Properties properties){super(properties);}
    private static <T extends net.minecraft.world.item.Item> T register(String mod_id,String name,T item){return Registry.register(BuiltInRegistries.ITEM,Identifier.fromNamespaceAndPath(mod_id,name),item);}
    
    public static Item create(String mod_id,String name){
        return create(mod_id,name,new net.minecraft.world.item.Item.Properties());
    }
    public static Item create(String mod_id,String name,GenProvider gen){
        Item item=create(mod_id,name,new net.minecraft.world.item.Item.Properties());
        ItemModelGen.flat(gen,mod_id,name);
        return item;
    }
    public static Item create(String mod_id,String name,net.minecraft.world.item.Item.Properties properties){
        properties.setId(net.minecraft.resources.ResourceKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath(mod_id,name)));
        Item item=new Item(properties);
        return register(mod_id,name,item);
    }
    public static Item createMusicDisc(String mod_id,String name){
        return createMusicDisc(mod_id,name,new Item.Properties().stacksTo(1),true);
    }
    public static Item createMusicDisc(String mod_id,String name,Item.Properties properties,Boolean createSoundEvent){
        properties.setId(ResourceKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath(mod_id,name)));
        properties.jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG,Identifier.fromNamespaceAndPath(mod_id,name)));
        if(createSoundEvent){Sound.create(mod_id,name);}
        return register(mod_id,name,new Item(properties));
    }
}