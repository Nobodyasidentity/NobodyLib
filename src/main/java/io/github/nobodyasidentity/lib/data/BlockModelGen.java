package io.github.nobodyasidentity.lib.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

public final class BlockModelGen{
    private BlockModelGen(){}
    public static void cube(GenProvider gen, Block block){
        Identifier id=BuiltInRegistries.BLOCK.getKey(block);
        if (id==null)throw new IllegalStateException("Block must be registered before generating its model");
        cube(gen,id.getNamespace(),id.getPath());
    }
    public static void cube(GenProvider gen,String mod_id,String name){
        gen.write(GenProvider.Type.ASSET,
            "assets/"+mod_id+"/blockstates/"+name+".json",
            "{\n  \"variants\": {\n    \"\": {\"model\": \""+mod_id+":block/"+name+"\"}\n  }\n}"
        );
        gen.write(GenProvider.Type.ASSET,
            "assets/"+mod_id+"/models/block/"+name+".json",
            "{\n  \"parent\": \"minecraft:block/cube_all\",\n  \"textures\": {\n    \"all\": \""+mod_id+":block/"+name+"\"\n  }\n}"
        );
        ItemModelGen.blockItem(gen,mod_id,name);
    }
}