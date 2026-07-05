package io.github.nobodyasidentity.lib;

import io.github.nobodyasidentity.lib.data.GenProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class NobodyLibDataGenerator implements DataGeneratorEntrypoint{
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator){
    	FabricDataGenerator.Pack pack=generator.createPack();
	    pack.addProvider((output,registries)->{
    	    GenProvider gen=new GenProvider(output);
    	    return gen;
	    });
	}
}