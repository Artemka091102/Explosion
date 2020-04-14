package com.artemka091102.explosion;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Explosion.MODID)
public class Explosion {
	public Explosion() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventsHandler::onCommonSetup);
    }

    public static final String MODID = "explosion";
}