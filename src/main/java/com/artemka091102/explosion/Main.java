package com.artemka091102.explosion;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
public class Main {
    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CrackedDict::onCommonSetup);
    }
    public static final String MODID = "explosion";
}