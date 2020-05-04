// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package com.artemka091102.explosion;

import java.util.HashMap;
import java.util.Objects;

import com.artemka091102.explosion.mods_integration.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class ModsController {

    private static final HashMap<String, BlockState> DICT = new HashMap<>();

    private static boolean acceptChanges = true;

    public static void putToDict(String oldBlockRegName, String newBlockRegName) {
        if (!acceptChanges)
            return;
        Block newBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(newBlockRegName));
        Block oldBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(oldBlockRegName));
        if (newBlock != Blocks.AIR && oldBlock != Blocks.AIR) {
            DICT.put(oldBlockRegName, Objects.requireNonNull(newBlock).getDefaultState());
        }
    }

    public static BlockState get(String key) {
        return DICT.get(key);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLLoadCompleteEvent event) {
        Minecraft.SetupCrackedDict();
        WildNature.SetupCrackedDict();
        acceptChanges = false;
    }
}