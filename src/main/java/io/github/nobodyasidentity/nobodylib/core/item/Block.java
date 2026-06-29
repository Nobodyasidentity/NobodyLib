package io.github.nobodyasidentity.nobodylib.core.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class Block extends net.minecraft.world.level.block.Block{
    public Block(BlockBehaviour.Properties properties){super(properties);}
    public static Block create(String mod_id,String name){
        return create(mod_id,name,BlockBehaviour.Properties.of().strength(1.0f));
    }
    public static Block create(String mod_id,String name,BlockBehaviour.Properties properties){
        Identifier id=Identifier.fromNamespaceAndPath(mod_id,name);
        properties.setId(ResourceKey.create(Registries.BLOCK,id));
        Block block=new Block(properties);
        return Registry.register(BuiltInRegistries.BLOCK,Identifier.fromNamespaceAndPath(mod_id,name),block);
    }
    public static BlockItem register(Block block){
        return register(block,new net.minecraft.world.item.Item.Properties());
    }
    public static BlockItem register(Block block,net.minecraft.world.item.Item.Properties properties){
        Identifier id=BuiltInRegistries.BLOCK.getKey(block);
        if(id==null){throw new IllegalStateException("Block must be registered before creating BlockItem");}
        ResourceKey<net.minecraft.world.item.Item> itemKey=ResourceKey.create(Registries.ITEM,id);
        properties.setId(itemKey);
        BlockItem item=new BlockItem(block,properties);
        return Registry.register(BuiltInRegistries.ITEM,id,item);
    }
}