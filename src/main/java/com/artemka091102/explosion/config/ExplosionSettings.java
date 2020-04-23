package com.artemka091102.explosion.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ExplosionSettings {
	
	//ЗАМЕНЯТЬ ЛИ БЛОКИ ВНУТРИ ВОРОНКИ
	public static ForgeConfigSpec.BooleanValue changeBlocksInCrater;
	
	//ЗАМЕНЯТЬ ЛИ БЛОКИ РЯДОМ С ВОРОНКОЙ
	public static ForgeConfigSpec.BooleanValue changeBlocksNearCrater;
	
	//ОБВАЛИВАТЬ ЛИ БЛОКИ РЯДОМ С ВОРОНКОЙ
	public static ForgeConfigSpec.BooleanValue fallBlocksNearCrater;
	
	//РАЗЛЕТАТЬ ЛИ БЛОКИ РЯДОМ С ВОРОНКОЙ
	public static ForgeConfigSpec.BooleanValue flyBlocksNearCrater;
	
	//ИНИЦИИРОВАНИЕ КОНФИГА
	public static void init(ForgeConfigSpec.Builder common) {		
		changeBlocksInCrater = common
				.comment("Change blocks in crater?")
				.define("settings.changeBlocksInCrater", true);
		
		changeBlocksNearCrater = common
				.comment("Change blocks near crater?")
				.define("settings.changeBlocksNearCrater", true);
		
		fallBlocksNearCrater = common
				.comment("Make blocks near crater fall?")
				.define("settings.fallBlocksNearCrater", true);
		
		flyBlocksNearCrater = common
				.comment("Make blocks near crater fly away?")
				.define("settings.flyBlocksNearCrater", true);
	}
}
