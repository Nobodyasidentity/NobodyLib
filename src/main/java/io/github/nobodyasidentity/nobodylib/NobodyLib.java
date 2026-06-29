package io.github.nobodyasidentity.nobodylib;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class NobodyLib implements ModInitializer{
	public static final String MOD_ID="nobodylib";
	public static final String NAME="NobodyLib";
	public static final Logger LOGGER=Logger.create(NAME);

	@Override
	public void onInitialize(){
		LOGGER.info("Initializing "+NAME);
	}
	public static Identifier id(String path){
		return Identifier.fromNamespaceAndPath(MOD_ID,path);
	}
}
