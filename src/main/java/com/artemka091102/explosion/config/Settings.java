package com.artemka091102.explosion.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Settings {
    public static ForgeConfigSpec.BooleanValue physics;
    public static ForgeConfigSpec.BooleanValue fallBlocks;
    public static ForgeConfigSpec.DoubleValue crackChance;

    public static void init(ForgeConfigSpec.Builder common) {
        physics = common
                .comment("Turn physics on?")
                .define("settings.physics", true);
        fallBlocks = common
                .comment("Turn falling from up to down blocks on?")
                .define("settings.fallBlocks", true);
        crackChance = common
                .comment("Crack blocks chance")
                .defineInRange("settings.crackChance",0.5,0.0, 1.0);
    }
}