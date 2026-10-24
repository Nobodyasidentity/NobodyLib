package io.github.nobodyasidentity.lib.core.item;

import io.github.nobodyasidentity.nobodylib.datagen.GenProvider;
import io.github.nobodyasidentity.nobodylib.datagen.ItemModelGen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.function.Function;

public class Block extends net.minecraft.world.level.block.Block{
    public Block(BlockBehaviour.Properties properties){super(properties);}
    public static class BlockItem extends net.minecraft.world.item.BlockItem{
        public BlockItem(net.minecraft.world.level.block.Block block,net.minecraft.world.item.Item.Properties properties){
            super(block,properties);
        }
    }
    // Block creation
    public static Block create(String mod_id, String name){
        return create(mod_id,name,BlockBehaviour.Properties.of().strength(1.0f));
    }
    public static Block create(String mod_id,String name,BlockBehaviour.Properties properties){
        return create(mod_id,name,properties,Block::new);
    }
    public static<T extends net.minecraft.world.level.block.Block>T create(String mod_id,String name,Function<BlockBehaviour.Properties,T>factory){
        return create(mod_id,name,BlockBehaviour.Properties.of().strength(1.0f),factory);
    }
    public static<T extends net.minecraft.world.level.block.Block>T create(String mod_id,String name,BlockBehaviour.Properties properties,Function<BlockBehaviour.Properties,T>factory){
        Identifier id=Identifier.fromNamespaceAndPath(mod_id,name);
        properties.setId(ResourceKey.create(Registries.BLOCK,id));
        T block=factory.apply(properties);
        return Registry.register(BuiltInRegistries.BLOCK,id,block);
    }

    // BlockItem creation
    /** Registers the item for an already registered block, under the block's own id. */
    public static BlockItem register(net.minecraft.world.level.block.Block block){
        return register(block,new net.minecraft.world.item.Item.Properties());
    }
    public static BlockItem register(net.minecraft.world.level.block.Block block, net.minecraft.world.item.Item.Properties properties){
        Identifier id=BuiltInRegistries.BLOCK.getKey(block);
        if(id==null){throw new IllegalStateException("Block must be registered before creating BlockItem");}
        ResourceKey<net.minecraft.world.item.Item>itemKey=ResourceKey.create(Registries.ITEM,id);
        properties.setId(itemKey).useBlockDescriptionPrefix();
        BlockItem item=new BlockItem(block,properties);
        return Registry.register(BuiltInRegistries.ITEM,id,item);
    }
    /** Registers an item for a block under a name of your choosing. */
    public static BlockItem createBlockItem(String mod_id,String name,net.minecraft.world.level.block.Block block){
        return createBlockItem(mod_id,name,block,new net.minecraft.world.item.Item.Properties());
    }
    public static BlockItem createBlockItem(String mod_id,String name,net.minecraft.world.level.block.Block block,GenProvider gen){
        BlockItem item=createBlockItem(mod_id,name,block,new net.minecraft.world.item.Item.Properties());
        ItemModelGen.blockItem(gen,mod_id,name);
        return item;
    }
    public static BlockItem createBlockItem(String mod_id,String name,net.minecraft.world.level.block.Block block,net.minecraft.world.item.Item.Properties properties){
        Identifier id=Identifier.fromNamespaceAndPath(mod_id,name);
        properties.setId(ResourceKey.create(Registries.ITEM,id));
        BlockItem item=new BlockItem(block,properties);
        return Registry.register(BuiltInRegistries.ITEM,id,item);
    }
}