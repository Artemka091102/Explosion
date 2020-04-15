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
		
		//MINECRAFT VANILLA
		
		crackedDictionary.put("minecraft:cracked_stone_bricks", new ResourceLocation("minecraft:cobblestone"));
		crackedDictionary.put("minecraft:infested_cracked_stone_bricks", new ResourceLocation("minecraft:infested_cobblestone"));

		crackedDictionary.put("minecraft:cobblestone", new ResourceLocation("minecraft:gravel"));

		crackedDictionary.put("minecraft:chiseled_sandstone", new ResourceLocation("minecraft:sandstone"));
		crackedDictionary.put("minecraft:chiseled_red_sandstone", new ResourceLocation("minecraft:red_sandstone"));
		crackedDictionary.put("minecraft:chiseled_stone_bricks", new ResourceLocation("minecraft:stone_bricks"));
		crackedDictionary.put("minecraft:infested_chiseled_stone_bricks", new ResourceLocation("minecraft:infested_stone_bricks"));
		crackedDictionary.put("minecraft:chiseled_quartz_block", new ResourceLocation("minecraft:quartz_block"));

		crackedDictionary.put("minecraft:polished_granite", new ResourceLocation("minecraft:granite"));
		crackedDictionary.put("minecraft:polished_diorite", new ResourceLocation("minecraft:diorite"));
		crackedDictionary.put("minecraft:polished_andesite", new ResourceLocation("minecraft:andesite"));
		crackedDictionary.put("minecraft:polished_granite_stairs", new ResourceLocation("minecraft:granite_stairs"));
		crackedDictionary.put("minecraft:polished_diorite_stairs", new ResourceLocation("minecraft:diorite_stairs"));
		crackedDictionary.put("minecraft:polished_andesite_stairs", new ResourceLocation("minecraft:andesite_stairs"));
		crackedDictionary.put("minecraft:polished_granite_slab", new ResourceLocation("minecraft:granite_slab"));
		crackedDictionary.put("minecraft:polished_diorite_slab", new ResourceLocation("minecraft:diorite_slab"));
		crackedDictionary.put("minecraft:polished_andesite_slab", new ResourceLocation("minecraft:andesite_slab"));

		crackedDictionary.put("minecraft:smooth_stone", new ResourceLocation("minecraft:stone"));
		crackedDictionary.put("minecraft:smooth_red_sandstone", new ResourceLocation("minecraft:red_sandstone"));
		crackedDictionary.put("minecraft:smooth_stone_slab", new ResourceLocation("minecraft:stone_slab"));
		crackedDictionary.put("minecraft:smooth_red_sandstone_stairs", new ResourceLocation("minecraft:red_sandstone_stairs"));
		crackedDictionary.put("minecraft:smooth_sandstone_stairs", new ResourceLocation("minecraft:sandstone_stairs"));
		crackedDictionary.put("minecraft:smooth_red_sandstone_slab", new ResourceLocation("minecraft:red_sandstone_slab"));
		crackedDictionary.put("minecraft:smooth_sandstone_slab", new ResourceLocation("minecraft:sandstone_slab"));

		crackedDictionary.put("minecraft:sandstone", new ResourceLocation("minecraft:sand"));
		crackedDictionary.put("minecraft:red_sandstone", new ResourceLocation("minecraft:red_sand"));
		crackedDictionary.put("minecraft:smooth_sandstone", new ResourceLocation("minecraft:smooth_sand"));
		crackedDictionary.put("minecraft:sandstone_slab", new ResourceLocation("minecraft:sand_slab"));
		crackedDictionary.put("minecraft:cut_sandstone_slab", new ResourceLocation("minecraft:cut_sand_slab"));
		crackedDictionary.put("minecraft:sandstone_stairs", new ResourceLocation("minecraft:sand_stairs"));
		crackedDictionary.put("minecraft:red_sandstone_wall", new ResourceLocation("minecraft:red_sand_wall"));
		crackedDictionary.put("minecraft:sandstone_wall", new ResourceLocation("minecraft:sand_wall"));

		crackedDictionary.put("minecraft:bricks", new ResourceLocation("minecraft:cracked_bricks"));
		crackedDictionary.put("minecraft:stone_bricks", new ResourceLocation("minecraft:cracked_stone_bricks"));
		crackedDictionary.put("minecraft:infested_stone_bricks", new ResourceLocation("minecraft:infested_cracked_stone_bricks"));
		crackedDictionary.put("minecraft:nether_bricks", new ResourceLocation("minecraft:cracked_nether_bricks"));
		crackedDictionary.put("minecraft:prismarine_bricks", new ResourceLocation("minecraft:cracked_prismarine_bricks"));
		crackedDictionary.put("minecraft:end_stone_bricks", new ResourceLocation("minecraft:cracked_end_stone_bricks"));
		crackedDictionary.put("minecraft:red_nether_bricks", new ResourceLocation("minecraft:cracked_red_nether_bricks"));

		crackedDictionary.put("minecraft:stone", new ResourceLocation("minecraft:cobblestone"));
		crackedDictionary.put("minecraft:stone_slab", new ResourceLocation("minecraft:cobblestone_slab"));
		crackedDictionary.put("minecraft:stone_brick_slab", new ResourceLocation("minecraft:cobblestone_brick_slab"));
		crackedDictionary.put("minecraft:stone_pressure_plate", new ResourceLocation("minecraft:cobblestone_pressure_plate"));
		crackedDictionary.put("minecraft:stone_button", new ResourceLocation("minecraft:cobblestone_button"));
		crackedDictionary.put("minecraft:mossy_stone_bricks", new ResourceLocation("minecraft:mossy_cobblestone_bricks"));
		crackedDictionary.put("minecraft:infested_mossy_stone_bricks", new ResourceLocation("minecraft:infested_mossy_cobblestone_bricks"));
		crackedDictionary.put("minecraft:stone_brick_stairs", new ResourceLocation("minecraft:cobblestone_brick_stairs"));
		crackedDictionary.put("minecraft:end_stone", new ResourceLocation("minecraft:end_cobblestone"));
		crackedDictionary.put("minecraft:mossy_stone_brick_stairs", new ResourceLocation("minecraft:mossy_cobblestone_brick_stairs"));
		crackedDictionary.put("minecraft:end_stone_brick_stairs", new ResourceLocation("minecraft:end_cobblestone_brick_stairs"));
		crackedDictionary.put("minecraft:mossy_stone_brick_slab", new ResourceLocation("minecraft:mossy_cobblestone_brick_slab"));
		crackedDictionary.put("minecraft:end_stone_brick_slab", new ResourceLocation("minecraft:end_cobblestone_brick_slab"));
		crackedDictionary.put("minecraft:mossy_stone_brick_wall", new ResourceLocation("minecraft:mossy_cobblestone_brick_wall"));
		crackedDictionary.put("minecraft:stone_brick_wall", new ResourceLocation("minecraft:cobblestone_brick_wall"));
		crackedDictionary.put("minecraft:end_stone_brick_wall", new ResourceLocation("minecraft:end_cobblestone_brick_wall"));

		crackedDictionary.put("minecraft:prismarine_bricks", new ResourceLocation("minecraft:prismarine"));
		crackedDictionary.put("minecraft:prismarine_brick_stairs", new ResourceLocation("minecraft:prismarine_stairs"));
		
		//WILDNATURE MOD
		
		crackedDictionary.put("wildnature:basalt_bricks_cracked", new ResourceLocation("wildnature:basalt_cobble"));
		crackedDictionary.put("wildnature:basalt_slab_bricks_cracked", new ResourceLocation("wildnature:basalt_slab_cobble"));
		crackedDictionary.put("wildnature:basalt_stairs_bricks_cracked", new ResourceLocation("wildnature:basalt_stairs_cobble"));
		crackedDictionary.put("wildnature:conglomerate_bricks_cracked", new ResourceLocation("wildnature:conglomerate_cobble"));
		crackedDictionary.put("wildnature:conglomerate_slab_bricks_cracked", new ResourceLocation("wildnature:conglomerate_slab_cobble"));
		crackedDictionary.put("wildnature:conglomerate_stairs_bricks_cracked", new ResourceLocation("wildnature:conglomerate_stairs_cobble"));
		crackedDictionary.put("wildnature:gneiss_bricks_cracked", new ResourceLocation("wildnature:gneiss_cobble"));
		crackedDictionary.put("wildnature:gneiss_slab_bricks_cracked", new ResourceLocation("wildnature:gneiss_slab_cobble"));
		crackedDictionary.put("wildnature:gneiss_stairs_bricks_cracked", new ResourceLocation("wildnature:gneiss_stairs_cobble"));
		crackedDictionary.put("wildnature:limestone_bricks_cracked", new ResourceLocation("wildnature:limestone_cobble"));
		crackedDictionary.put("wildnature:limestone_slab_bricks_cracked", new ResourceLocation("wildnature:limestone_slab_cobble"));
		crackedDictionary.put("wildnature:limestone_stairs_bricks_cracked", new ResourceLocation("wildnature:limestone_stairs_cobble"));
		crackedDictionary.put("wildnature:marble_bricks_cracked", new ResourceLocation("wildnature:marble_cobble"));
		crackedDictionary.put("wildnature:marble_slab_bricks_cracked", new ResourceLocation("wildnature:marble_slab_cobble"));
		crackedDictionary.put("wildnature:marble_stairs_bricks_cracked", new ResourceLocation("wildnature:marble_stairs_cobble"));
		crackedDictionary.put("wildnature:pegmatite_bricks_cracked", new ResourceLocation("wildnature:pegmatite_cobble"));
		crackedDictionary.put("wildnature:pegmatite_slab_bricks_cracked", new ResourceLocation("wildnature:pegmatite_slab_cobble"));
		crackedDictionary.put("wildnature:pegmatite_stairs_bricks_cracked", new ResourceLocation("wildnature:pegmatite_stairs_cobble"));
		crackedDictionary.put("wildnature:slate_bricks_cracked", new ResourceLocation("wildnature:slate_cobble"));
		crackedDictionary.put("wildnature:slate_slab_bricks_cracked", new ResourceLocation("wildnature:slate_slab_cobble"));
		crackedDictionary.put("wildnature:slate_stairs_bricks_cracked", new ResourceLocation("wildnature:slate_stairs_cobble"));

		crackedDictionary.put("wildnature:basalt_cobble", new ResourceLocation("minecraft:gravel"));
		crackedDictionary.put("wildnature:conglomerate_cobble", new ResourceLocation("minecraft:gravel"));
		crackedDictionary.put("wildnature:gneiss_cobble", new ResourceLocation("minecraft:gravel"));
		crackedDictionary.put("wildnature:limestone_cobble", new ResourceLocation("minecraft:gravel"));
		crackedDictionary.put("wildnature:marble_cobble", new ResourceLocation("minecraft:gravel"));
		crackedDictionary.put("wildnature:pegmatite_cobble", new ResourceLocation("minecraft:gravel"));
		crackedDictionary.put("wildnature:slate_cobble", new ResourceLocation("minecraft:gravel"));

		crackedDictionary.put("wildnature:basalt_bricks_chiseled", new ResourceLocation("wildnature:basalt_bricks"));
		crackedDictionary.put("wildnature:basalt_slab_bricks_chiseled", new ResourceLocation("wildnature:basalt_slab_bricks"));
		crackedDictionary.put("wildnature:basalt_stairs_bricks_chiseled", new ResourceLocation("wildnature:basalt_stairs_bricks"));
		crackedDictionary.put("wildnature:conglomerate_bricks_chiseled", new ResourceLocation("wildnature:conglomerate_bricks"));
		crackedDictionary.put("wildnature:conglomerate_slab_bricks_chiseled", new ResourceLocation("wildnature:conglomerate_slab_bricks"));
		crackedDictionary.put("wildnature:conglomerate_stairs_bricks_chiseled", new ResourceLocation("wildnature:conglomerate_stairs_bricks"));
		crackedDictionary.put("wildnature:gneiss_bricks_chiseled", new ResourceLocation("wildnature:gneiss_bricks"));
		crackedDictionary.put("wildnature:gneiss_slab_bricks_chiseled", new ResourceLocation("wildnature:gneiss_slab_bricks"));
		crackedDictionary.put("wildnature:gneiss_stairs_bricks_chiseled", new ResourceLocation("wildnature:gneiss_stairs_bricks"));
		crackedDictionary.put("wildnature:limestone_bricks_chiseled", new ResourceLocation("wildnature:limestone_bricks"));
		crackedDictionary.put("wildnature:limestone_slab_bricks_chiseled", new ResourceLocation("wildnature:limestone_slab_bricks"));
		crackedDictionary.put("wildnature:limestone_stairs_bricks_chiseled", new ResourceLocation("wildnature:limestone_stairs_bricks"));
		crackedDictionary.put("wildnature:marble_bricks_chiseled", new ResourceLocation("wildnature:marble_bricks"));
		crackedDictionary.put("wildnature:marble_slab_bricks_chiseled", new ResourceLocation("wildnature:marble_slab_bricks"));
		crackedDictionary.put("wildnature:marble_stairs_bricks_chiseled", new ResourceLocation("wildnature:marble_stairs_bricks"));
		crackedDictionary.put("wildnature:pegmatite_bricks_chiseled", new ResourceLocation("wildnature:pegmatite_bricks"));
		crackedDictionary.put("wildnature:pegmatite_slab_bricks_chiseled", new ResourceLocation("wildnature:pegmatite_slab_bricks"));
		crackedDictionary.put("wildnature:pegmatite_stairs_bricks_chiseled", new ResourceLocation("wildnature:pegmatite_stairs_bricks"));
		crackedDictionary.put("wildnature:slate_bricks_chiseled", new ResourceLocation("wildnature:slate_bricks"));
		crackedDictionary.put("wildnature:slate_slab_bricks_chiseled", new ResourceLocation("wildnature:slate_slab_bricks"));
		crackedDictionary.put("wildnature:slate_stairs_bricks_chiseled", new ResourceLocation("wildnature:slate_stairs_bricks"));

		crackedDictionary.put("wildnature:basalt_polished", new ResourceLocation("wildnature:basalt"));
		crackedDictionary.put("wildnature:basalt_polished_slab", new ResourceLocation("wildnature:basalt_slab"));
		crackedDictionary.put("wildnature:basalt_polished_stairs", new ResourceLocation("wildnature:basalt_stairs"));
		crackedDictionary.put("wildnature:basalt_polished_wall", new ResourceLocation("wildnature:basalt_wall"));
		crackedDictionary.put("wildnature:conglomerate_polished", new ResourceLocation("wildnature:conglomerate"));
		crackedDictionary.put("wildnature:conglomerate_polished_slab", new ResourceLocation("wildnature:conglomerate_slab"));
		crackedDictionary.put("wildnature:conglomerate_polished_stairs", new ResourceLocation("wildnature:conglomerate_stairs"));
		crackedDictionary.put("wildnature:conglomerate_polished_wall", new ResourceLocation("wildnature:conglomerate_wall"));
		crackedDictionary.put("wildnature:gneiss_polished", new ResourceLocation("wildnature:gneiss"));
		crackedDictionary.put("wildnature:gneiss_polished_slab", new ResourceLocation("wildnature:gneiss_slab"));
		crackedDictionary.put("wildnature:gneiss_polished_stairs", new ResourceLocation("wildnature:gneiss_stairs"));
		crackedDictionary.put("wildnature:gneiss_polished_wall", new ResourceLocation("wildnature:gneiss_wall"));
		crackedDictionary.put("wildnature:limestone_polished", new ResourceLocation("wildnature:limestone"));
		crackedDictionary.put("wildnature:limestone_polished_slab", new ResourceLocation("wildnature:limestone_slab"));
		crackedDictionary.put("wildnature:limestone_polished_stairs", new ResourceLocation("wildnature:limestone_stairs"));
		crackedDictionary.put("wildnature:limestone_polished_wall", new ResourceLocation("wildnature:limestone_wall"));
		crackedDictionary.put("wildnature:marble_polished", new ResourceLocation("wildnature:marble"));
		crackedDictionary.put("wildnature:marble_polished_slab", new ResourceLocation("wildnature:marble_slab"));
		crackedDictionary.put("wildnature:marble_polished_stairs", new ResourceLocation("wildnature:marble_stairs"));
		crackedDictionary.put("wildnature:marble_polished_wall", new ResourceLocation("wildnature:marble_wall"));
		crackedDictionary.put("wildnature:pegmatite_polished", new ResourceLocation("wildnature:pegmatite"));
		crackedDictionary.put("wildnature:pegmatite_polished_slab", new ResourceLocation("wildnature:pegmatite_slab"));
		crackedDictionary.put("wildnature:pegmatite_polished_stairs", new ResourceLocation("wildnature:pegmatite_stairs"));
		crackedDictionary.put("wildnature:pegmatite_polished_wall", new ResourceLocation("wildnature:pegmatite_wall"));
		crackedDictionary.put("wildnature:slate_polished", new ResourceLocation("wildnature:slate"));
		crackedDictionary.put("wildnature:slate_polished_slab", new ResourceLocation("wildnature:slate_slab"));
		crackedDictionary.put("wildnature:slate_polished_stairs", new ResourceLocation("wildnature:slate_stairs"));
		crackedDictionary.put("wildnature:slate_polished_wall", new ResourceLocation("wildnature:slate_wall"));

		crackedDictionary.put("wildnature:white_sandstone", new ResourceLocation("wildnature:white_sand"));
		crackedDictionary.put("wildnature:cracked_sandstone", new ResourceLocation("wildnature:cracked_sand"));

		crackedDictionary.put("wildnature:basalt_bricks", new ResourceLocation("wildnature:basalt_bricks_cracked"));
		crackedDictionary.put("wildnature:basalt_small_bricks", new ResourceLocation("wildnature:basalt_small_bricks_cracked"));
		crackedDictionary.put("wildnature:basalt_ancient_bricks", new ResourceLocation("wildnature:basalt_ancient_bricks_cracked"));
		crackedDictionary.put("wildnature:basalt_slab_bricks", new ResourceLocation("wildnature:basalt_slab_bricks_cracked"));
		crackedDictionary.put("wildnature:basalt_stairs_bricks", new ResourceLocation("wildnature:basalt_stairs_bricks_cracked"));
		crackedDictionary.put("wildnature:basalt_wall_bricks", new ResourceLocation("wildnature:basalt_wall_bricks_cracked"));
		crackedDictionary.put("wildnature:conglomerate_bricks", new ResourceLocation("wildnature:conglomerate_bricks_cracked"));
		crackedDictionary.put("wildnature:conglomerate_small_bricks", new ResourceLocation("wildnature:conglomerate_small_bricks_cracked"));
		crackedDictionary.put("wildnature:conglomerate_ancient_bricks", new ResourceLocation("wildnature:conglomerate_ancient_bricks_cracked"));
		crackedDictionary.put("wildnature:conglomerate_slab_bricks", new ResourceLocation("wildnature:conglomerate_slab_bricks_cracked"));
		crackedDictionary.put("wildnature:conglomerate_stairs_bricks", new ResourceLocation("wildnature:conglomerate_stairs_bricks_cracked"));
		crackedDictionary.put("wildnature:conglomerate_wall_bricks", new ResourceLocation("wildnature:conglomerate_wall_bricks_cracked"));
		crackedDictionary.put("wildnature:gneiss_bricks", new ResourceLocation("wildnature:gneiss_bricks_cracked"));
		crackedDictionary.put("wildnature:gneiss_small_bricks", new ResourceLocation("wildnature:gneiss_small_bricks_cracked"));
		crackedDictionary.put("wildnature:gneiss_ancient_bricks", new ResourceLocation("wildnature:gneiss_ancient_bricks_cracked"));
		crackedDictionary.put("wildnature:gneiss_slab_bricks", new ResourceLocation("wildnature:gneiss_slab_bricks_cracked"));
		crackedDictionary.put("wildnature:gneiss_stairs_bricks", new ResourceLocation("wildnature:gneiss_stairs_bricks_cracked"));
		crackedDictionary.put("wildnature:gneiss_wall_bricks", new ResourceLocation("wildnature:gneiss_wall_bricks_cracked"));
		crackedDictionary.put("wildnature:limestone_bricks", new ResourceLocation("wildnature:limestone_bricks_cracked"));
		crackedDictionary.put("wildnature:limestone_small_bricks", new ResourceLocation("wildnature:limestone_small_bricks_cracked"));
		crackedDictionary.put("wildnature:limestone_ancient_bricks", new ResourceLocation("wildnature:limestone_ancient_bricks_cracked"));
		crackedDictionary.put("wildnature:limestone_slab_bricks", new ResourceLocation("wildnature:limestone_slab_bricks_cracked"));
		crackedDictionary.put("wildnature:limestone_stairs_bricks", new ResourceLocation("wildnature:limestone_stairs_bricks_cracked"));
		crackedDictionary.put("wildnature:limestone_wall_bricks", new ResourceLocation("wildnature:limestone_wall_bricks_cracked"));
		crackedDictionary.put("wildnature:marble_bricks", new ResourceLocation("wildnature:marble_bricks_cracked"));
		crackedDictionary.put("wildnature:marble_small_bricks", new ResourceLocation("wildnature:marble_small_bricks_cracked"));
		crackedDictionary.put("wildnature:marble_ancient_bricks", new ResourceLocation("wildnature:marble_ancient_bricks_cracked"));
		crackedDictionary.put("wildnature:marble_slab_bricks", new ResourceLocation("wildnature:marble_slab_bricks_cracked"));
		crackedDictionary.put("wildnature:marble_stairs_bricks", new ResourceLocation("wildnature:marble_stairs_bricks_cracked"));
		crackedDictionary.put("wildnature:marble_wall_bricks", new ResourceLocation("wildnature:marble_wall_bricks_cracked"));
		crackedDictionary.put("wildnature:pegmatite_bricks", new ResourceLocation("wildnature:pegmatite_bricks_cracked"));
		crackedDictionary.put("wildnature:pegmatite_small_bricks", new ResourceLocation("wildnature:pegmatite_small_bricks_cracked"));
		crackedDictionary.put("wildnature:pegmatite_ancient_bricks", new ResourceLocation("wildnature:pegmatite_ancient_bricks_cracked"));
		crackedDictionary.put("wildnature:pegmatite_slab_bricks", new ResourceLocation("wildnature:pegmatite_slab_bricks_cracked"));
		crackedDictionary.put("wildnature:pegmatite_stairs_bricks", new ResourceLocation("wildnature:pegmatite_stairs_bricks_cracked"));
		crackedDictionary.put("wildnature:pegmatite_wall_bricks", new ResourceLocation("wildnature:pegmatite_wall_bricks_cracked"));
		crackedDictionary.put("wildnature:slate_bricks", new ResourceLocation("wildnature:slate_bricks_cracked"));
		crackedDictionary.put("wildnature:slate_small_bricks", new ResourceLocation("wildnature:slate_small_bricks_cracked"));
		crackedDictionary.put("wildnature:slate_ancient_bricks", new ResourceLocation("wildnature:slate_ancient_bricks_cracked"));
		crackedDictionary.put("wildnature:slate_slab_bricks", new ResourceLocation("wildnature:slate_slab_bricks_cracked"));
		crackedDictionary.put("wildnature:slate_stairs_bricks", new ResourceLocation("wildnature:slate_stairs_bricks_cracked"));
		crackedDictionary.put("wildnature:slate_wall_bricks", new ResourceLocation("wildnature:slate_wall_bricks_cracked"));

		crackedDictionary.put("wildnature:stone_lamp", new ResourceLocation("wildnature:cobblestone_lamp"));
		crackedDictionary.put("wildnature:stone_grass", new ResourceLocation("wildnature:cobblestone_grass"));
		crackedDictionary.put("wildnature:mossy_stone_lamp", new ResourceLocation("wildnature:mossy_cobblestone_lamp"));
		crackedDictionary.put("wildnature:overgrown_stone", new ResourceLocation("wildnature:overgrown_cobblestone"));
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
