// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package com.artemka091102.explosion;

import com.artemka091102.explosion.config.Config;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Main.MODID)
public class Main {
    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModsController::onCommonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.config);
        Config.loadConfig(Config.config, FMLPaths.CONFIGDIR.get().resolve("explosion-common.toml").toString());
    }
    public static final String MODID = "explosion";
}