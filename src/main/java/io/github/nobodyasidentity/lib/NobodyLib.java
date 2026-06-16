package io.github.nobodyasidentity.lib;

import net.fabricmc.api.ModInitializer;

public class NobodyLib implements ModInitializer{
	public static final String MOD_ID="nobody-lib";
	public static final Logger LOGGER=Logger.create(MOD_ID);

	@Override
	public void onInitialize(){
		LOGGER.info("Initializing NobodyLib...");
		LOGGER.info("NobodyLib has been initialized!");
	}
}