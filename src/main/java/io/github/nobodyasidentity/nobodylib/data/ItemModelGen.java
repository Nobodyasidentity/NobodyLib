package io.github.nobodyasidentity.nobodylib.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public final class ItemModelGen{
    private ItemModelGen(){}
    public static void flat(GenProvider gen, Item item){
        Identifier id=BuiltInRegistries.ITEM.getKey(item);
        if (id==null) throw new IllegalStateException("Item must be registered before generating its model");
        flat(gen,id.getNamespace(), id.getPath());
    }
    public static void flat(GenProvider gen,String mod_id,String name) {
        gen.write(GenProvider.Type.ASSET,
            "assets/"+mod_id+"/items/"+name+".json",
            "{\n  \"model\": {\n    \"type\": \"minecraft:model\",\n    \"model\": \""+mod_id+":item/"+name+"\"\n  }\n}"
        );
        gen.write(GenProvider.Type.ASSET,
            "assets/"+mod_id+"/models/item/"+name+".json",
            "{\n  \"parent\": \"minecraft:item/generated\",\n  \"textures\": {\n    \"layer0\": \""+mod_id+":item/"+name+"\"\n  }\n}"
        );
    }
    public static void blockItem(GenProvider gen, String mod_id, String name) {
        gen.write(GenProvider.Type.ASSET,
            "assets/"+mod_id+"/items/"+name+".json",
            "{\n  \"model\": {\n    \"type\": \"minecraft:model\",\n    \"model\": \""+mod_id+":item/"+name+"\"\n  }\n}"
        );
        gen.write(GenProvider.Type.ASSET,
            "assets/"+mod_id+"/models/item/"+name+".json",
            "{\n  \"parent\": \""+mod_id+":block/"+name+"\"\n}"
        );
    }
}