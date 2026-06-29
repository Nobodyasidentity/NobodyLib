package io.github.nobodyasidentity.nobodylib.core.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class Sound{
    public static SoundEvent create(String mod_id,String name){
        Identifier id=Identifier.fromNamespaceAndPath(mod_id,name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT,id,SoundEvent.createVariableRangeEvent(id));
    }
}