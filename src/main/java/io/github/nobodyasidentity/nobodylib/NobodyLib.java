package io.github.nobodyasidentity.nobodylib;

import io.github.nobodyasidentity.lib.Logger;

import io.github.nobodyasidentity.nobodylib.config.NobodyLibConfigs;
import net.fabricmc.api.ModInitializer;

public class NobodyLib implements ModInitializer{
	public static final String MOD_ID="nobodylib";
	public static final String NAME="NobodyLib";
	public static final Logger LOGGER=Logger.create(NAME);
	public static String OS=System.getProperty("os.name","").toLowerCase();

	@Override
	public void onInitialize(){
		LOGGER.info("Initializing "+NAME+"...");
		NobodyLibConfigs.CORE.get();
		NobodyLibConfigs.SERVER.get();
		NobodyLibConfigs.CORE.save();
		NobodyLibConfigs.SERVER.save();
		LOGGER.info(NAME+" initialized.");
	}
}