// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package com.artemka091102.explosion;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
public class Main {
    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModsController::onCommonSetup);
    }

    public static final String MODID = "explosion";
}