package com.example;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.ToolMaterial;
import io.github.nobodyasidentity.lib.Logger;
import io.github.nobodyasidentity.lib.core.item.Block;
import io.github.nobodyasidentity.lib.core.item.Item;

public class ExampleMod implements ModInitializer{
    public static final String MOD_ID="example_mod";
    public static final Logger LOGGER=Logger.create(MOD_ID);

    public static final Block TEST_BLOCK=Block.create(MOD_ID,"test_block");
    public static final Item TEST_BLOCK_ITEM=Block.register(TEST_BLOCK);

    @Override
	public void onInitialize(){
        LOGGER.log("Example Mod initializing...");
    }
}