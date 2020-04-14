package com.artemka091102.explosion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = Explosion.MODID, bus=EventBusSubscriber.Bus.FORGE)

public class EventsHandler {
    static final Logger LOGGER = LogManager.getLogger();
	public static final Map<String, ResourceLocation> crackedDictionary = new HashMap<String, ResourceLocation>();

	@SubscribeEvent
	public static void onCommonSetup(FMLLoadCompleteEvent event) {
	    crackedDictionary.put("minecraft:stone_bricks", new ResourceLocation("minecraft:cracked_stone_bricks"));
	    crackedDictionary.put("minecraft:cracked_stone_bricks", new ResourceLocation("minecraft:cobblestone"));
	    crackedDictionary.put("minecraft:stone_slab", new ResourceLocation("minecraft:cobblestone_slab"));
	    crackedDictionary.put("minecraft:stone_brick_slab", new ResourceLocation("minecraft:cobblestone_slab"));
	    crackedDictionary.put("minecraft:mossy_stone_brick_slab", new ResourceLocation("minecraft:mossy_cobblestone_slab"));
	    crackedDictionary.put("minecraft:mossy_stone_bricks", new ResourceLocation("minecraft:mossy_cobblestone"));
	    crackedDictionary.put("minecraft:polished_andesite_slab", new ResourceLocation("minecraft:andesite_slab"));
	    crackedDictionary.put("minecraft:polished_granite_slab", new ResourceLocation("minecraft:granite_slab"));
	    crackedDictionary.put("minecraft:sandstone", new ResourceLocation("wildnature:cracked_sandstone"));
	    crackedDictionary.put("minecraft:mossy_stone_brick_wall", new ResourceLocation("minecraft:mossy_cobblestone_wall"));
	}
	
	@SubscribeEvent
	public static void explosion(ExplosionEvent event) {
	    DimensionType dimension = event.getWorld().getDimension().getType();
	    World world = event.getWorld().getServer().getWorld(dimension);
	    List<BlockPos> blocksPos = event.getExplosion().getAffectedBlockPositions();
	    List<BlockPos> exploded = new ArrayList<>();
	    for (BlockPos blockPos : blocksPos) {
	        blockReplace(world, blockPos, exploded, false);
	        blockReplace(world, blockPos.east(), exploded, true);
	        blockReplace(world, blockPos.west(), exploded, true);
	        blockReplace(world, blockPos.north(), exploded, true);
	        blockReplace(world, blockPos.south(), exploded, true);
	        blockReplace(world, blockPos.up(), exploded, true);
	        blockReplace(world, blockPos.down(), exploded, true);
	    }
	}

	public static void blockReplace(World world, BlockPos blockPos, List<BlockPos> exploded, boolean check) {
	    if (check) {
	        for (BlockPos pos : exploded) {
	            if (pos.getX() == blockPos.getX() && pos.getY() == blockPos.getY() && pos.getZ() == blockPos.getZ())
	                return;
	        }
	    }
	    if (world.getRandom().nextFloat() < 0.4F) return;
	    exploded.add(blockPos);
	    ResourceLocation blockResourceLocation = crackedDictionary.get(world.getBlockState(blockPos).getBlock().getRegistryName().toString());
	    if (blockResourceLocation != null) {
	        Block block = ForgeRegistries.BLOCKS.getValue(blockResourceLocation);
	        if (block != Blocks.AIR)
	            world.setBlockState(blockPos, block.getDefaultState());
	    }
	}
}