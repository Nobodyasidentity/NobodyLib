package io.github.nobodyasidentity.nobodylib.config;

import io.github.nobodyasidentity.lib.config.ConfigFile;
import io.github.nobodyasidentity.lib.config.ConfigManager;

public final class NobodyLibConfigs{
    public static final ConfigFile<CoreConfig> CORE=ConfigManager.register("core",CoreConfig.class);
    public static final ConfigFile<ClientConfig> CLIENT=ConfigManager.register("client",ClientConfig.class);
    public static final ConfigFile<ServerConfig> SERVER=ConfigManager.register("server",ServerConfig.class);

    private NobodyLibConfigs(){}
}