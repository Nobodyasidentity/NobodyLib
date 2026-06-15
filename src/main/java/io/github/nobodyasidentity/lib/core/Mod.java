package io.github.nobodyasidentity.lib.core;

public final class Mod {
    private static String modid;
    public static void init(String modid){Mod.modid=modid;}
    public static String modid(){return modid;}
}