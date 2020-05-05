package com.artemka091102.explosion.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Settings {
    public static ForgeConfigSpec.BooleanValue physics;
    public static ForgeConfigSpec.DoubleValue crackInChance;
    public static ForgeConfigSpec.DoubleValue crackOutChance;

    public static void init(ForgeConfigSpec.Builder common) {
        physics = common
                .comment("Turn physics on?")
                .define("settings.physics", true);
        crackInChance = common
                .comment("Crack blocks into crater chance")
                .defineInRange("settings.crackInChance",0.5,0.0, 1.0);
        crackOutChance = common
                .comment("Crack blocks out of crater chance (if physics is on this parameter should be 1.0)")
                .defineInRange("settings.crackOutChance",1.0,0.0, 1.0);
    }
}