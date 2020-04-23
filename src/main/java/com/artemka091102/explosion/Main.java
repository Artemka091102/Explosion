package com.artemka091102.explosion;

import com.artemka091102.explosion.config.Config;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Main.MODID)
public class Main {
	
	//КОНСТРУКТОР КЛАССА
	public Main() {
		
		//ЗАГРУЗКА КОНФИГА
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.config);
        Config.loadConfig(Config.config, FMLPaths.CONFIGDIR.get().resolve("explosion-common.toml").toString());

        //ПОДКЛЮЧЕНИЕ ШИНЫ СОБЫТИЙ FML
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventsHandler::onCommonSetup);   
	}

	//ID МОДА
    public static final String MODID = "explosion";
}