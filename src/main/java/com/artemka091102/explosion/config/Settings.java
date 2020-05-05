package com.artemka091102.explosion.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Settings {
    //PHYSICS SETTING
    public static ForgeConfigSpec.BooleanValue physics;

    public static void init(ForgeConfigSpec.Builder common) {
        physics = common
                .comment("Turn physics on?")
                .define("settings.physics", true);
    }
}
