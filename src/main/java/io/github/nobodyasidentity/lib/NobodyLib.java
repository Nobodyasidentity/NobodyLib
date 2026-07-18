package io.github.nobodyasidentity.lib;

import net.fabricmc.api.ModInitializer;

public class NobodyLib implements ModInitializer{
	public static final String MOD_ID="nobodylib";
	public static final String NAME="NobodyLib";
	public static final Logger LOGGER=Logger.create(NAME);
	public static String OS=System.getProperty("os.name","").toLowerCase();

	@Override
	public void onInitialize(){
		LOGGER.info("Initializing "+NAME+"...");
		LOGGER.info(NAME+" initialized.");
	}
}