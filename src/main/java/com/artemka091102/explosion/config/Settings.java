package com.artemka091102.explosion.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Settings {

    //ОБВАЛИВАТЬ ЛИ БЛОКИ РЯДОМ С ВОРОНКОЙ
    public static ForgeConfigSpec.BooleanValue fallBlocksNearCrater;

    //РАЗЛЕТАТЬ ЛИ БЛОКИ РЯДОМ С ВОРОНКОЙ
    public static ForgeConfigSpec.BooleanValue flyBlocksNearCrater;

    //ИНИЦИИРОВАНИЕ КОНФИГА
    public static void init(ForgeConfigSpec.Builder common) {

        fallBlocksNearCrater = common
                .comment("Make blocks near crater fall?")
                .define("settings.fallBlocksNearCrater", true);

        flyBlocksNearCrater = common
                .comment("Make blocks near crater fly away?")
                .define("settings.flyBlocksNearCrater", true);
    }
}
