package com.artemka091102.explosion.mods_integration;
import static com.artemka091102.explosion.ModsController.putToCrackedDict;

public class ExtraCobble {
    public static void Setup() {
        SetupCrackedDict();
    }
    public static void SetupCrackedDict() {
        //Andesite
        putToCrackedDict("minecraft:andesite", "extracobble:andesite_cobble");
        putToCrackedDict("minecraft:andesite_slab", "extracobble:andesite_cobble_slab");
        putToCrackedDict("minecraft:andesite_stairs", "extracobble:andesite_cobble_stairs");
        putToCrackedDict("extracobble:andesite_cobble", "extracobble:andesite_gravel");
        putToCrackedDict("extracobble:andesite_gravel", "extracobble:andesite_sand");
        //Mossy Andesite
        putToCrackedDict("extracobble:mossy_andesite_cobble", "extracobble:andesite_gravel");
        //Diorite
        putToCrackedDict("minecraft:diorite", "extracobble:diorite_cobble");
        putToCrackedDict("minecraft:diorite_slab", "extracobble:diorite_cobble_slab");
        putToCrackedDict("minecraft:diorite_stairs", "extracobble:diorite_cobble_stairs");
        putToCrackedDict("extracobble:diorite_cobble", "extracobble:diorite_gravel");
        putToCrackedDict("extracobble:diorite_gravel", "extracobble:diorite_sand");
        //Mossy Diorite
        putToCrackedDict("extracobble:mossy_diorite_cobble", "extracobble:diorite_gravel");
        //Granite
        putToCrackedDict("minecraft:granite", "extracobble:granite_cobble");
        putToCrackedDict("minecraft:granite_slab", "extracobble:granite_cobble_slab");
        putToCrackedDict("minecraft:granite_stairs", "extracobble:granite_cobble_stairs");
        putToCrackedDict("extracobble:granite_cobble", "extracobble:granite_gravel");
        putToCrackedDict("extracobble:granite_gravel", "extracobble:granite_sand");
        //Mossy Granite
        putToCrackedDict("extracobble:mossy_granite_cobble", "extracobble:granite_gravel");
    }
}
