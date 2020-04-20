package com.artemka091102.explosion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = Main.MODID, bus=EventBusSubscriber.Bus.FORGE)
public class EventsHandler {

	@SubscribeEvent
	public static void explosion(ExplosionEvent event) {
		
		//ЕСЛИ ВЗРЫВ НА СЕРВЕРЕ
		World world = event.getWorld();
		if (!world.isRemote) {
			
			//ОБРАБОТКА СПИСКА БЛОКОВ
			List<BlockPos> blocksPos = event.getExplosion().getAffectedBlockPositions();
			for (BlockPos blockPos : blocksPos) {
				
				//ЗАМЕНА ВЗОРВАННОГО БЛОКА НА ЕГО СЛОМАННУЮ ВЕРСИЮ
				BlockState newBlockState = crackedDictionary.get(world.getBlockState(blockPos).getBlock().getRegistryName().toString());
				if (newBlockState != null) world.setBlockState(blockPos, newBlockState);
				
				//ЗАМЕНЯЕМ БЛОКИ ВОКРУГ ВЗОРВАННОГО БЛОКА ЕСЛИ ОНИ НЕ ВЗОРВУТСЯ
				changeBlock(world, blocksPos, blockPos.up());
				changeBlock(world, blocksPos, blockPos.down());
				changeBlock(world, blocksPos, blockPos.east());
				changeBlock(world, blocksPos, blockPos.west());
				changeBlock(world, blocksPos, blockPos.south());
				changeBlock(world, blocksPos, blockPos.north());
				
				//ЕСЛИ БЛОК ВЫШЕ НЕ БУДЕТ ВЗОРВАН ТО ТРИ БЛОКА ВЫШЕ СТАНОВЯТСЯ ПАДАЮЩИМИ
				if (!blocksPos.contains(blockPos.up())) {
					checkFallable(world, blockPos.up());
					checkFallable(world, blockPos.up(2));
					checkFallable(world, blockPos.up(3));
					checkFallable(world, blockPos.up(4));
				}
			}
		}
	}
	
	//ЗАМЕНА БЛОКА
	public static void changeBlock(World world, List<BlockPos> blocksPos, BlockPos blockPos) {
		if (!blocksPos.contains(blockPos) && world.rand.nextFloat() > 0.4) {
			BlockState newBlockState = crackedDictionary.get(world.getBlockState(blockPos).getBlock().getRegistryName().toString());
			if (newBlockState != null) world.setBlockState(blockPos, newBlockState);
		}
	}
	
	//БЛОК ПАДАЕТ ЕСЛИ МОЖЕТ
	public static void checkFallable(World world, BlockPos blockPos) {
		if (!world.isRemote && canFallThrough(world.getBlockState(blockPos.down())) && blockPos.getY() > 0) {
			FallingBlockEntity fallingblockentity = new FallingBlockEntity(world, (double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, world.getBlockState(blockPos));
			world.addEntity(fallingblockentity);
		}
	}
	
	//МОЖЕТ ЛИ БЛОК ПАДАТЬ
	public static boolean canFallThrough(BlockState state) {
		return state.getBlock() != Blocks.AIR || state.getBlock() == Blocks.FIRE || state.getMaterial().isLiquid() || state.getMaterial().isReplaceable();
	}

	//СЛОВАРЬ СООТВЕТСТВИЙ БЛОКОВ
	public static final Map<String, BlockState> crackedDictionary = new HashMap<String, BlockState>();

	//ДОБАВЛЕНИЕ СООТВЕТСТВИЙ БЛОКОВ В СЛОВАРЬ И ПРОВЕРКА НА СУЩЕСТВОВАНИЕ БЛОКА
	public static void putToDictionary(String oldBlockRegName, String newBlockRegName) {
		Block newBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(newBlockRegName));
		Block oldBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(oldBlockRegName));
		if (newBlock != Blocks.AIR && oldBlock != Blocks.AIR) {
			crackedDictionary.put(oldBlockRegName, newBlock.getDefaultState());
		}
	}
	
	//ДОБАВЛЕНИЕ СООТВЕТСТВИЙ БЛОКОВ В СЛОВАРЬ ПРИ ЗАГРУЗКЕ МАЙНКРАФТА
	@SubscribeEvent
	public static void onCommonSetup(FMLLoadCompleteEvent event) {
		putToDictionary("wildnature:basalt_slab", "wildnature:basalt_slab_cobble");
		putToDictionary("wildnature:basalt_slab_bricks", "wildnature:basalt_slab_bricks_cracked");
		putToDictionary("wildnature:basalt_slab_bricks_cracked", "wildnature:basalt_slab_cobble");
		putToDictionary("wildnature:basalt_slab_bricks_chiseled", "wildnature:basalt_slab_bricks_cracked");
		putToDictionary("wildnature:basalt_slab_bricks_mossy", "wildnature:basalt_slab_cobble_mossy");
		putToDictionary("wildnature:basalt_slab_cobble", "minecraft:gravel");
		putToDictionary("wildnature:basalt_stairs", "wildnature:basalt_stairs_cobble");
		putToDictionary("wildnature:basalt_stairs_bricks", "wildnature:basalt_stairs_bricks_cracked");
		putToDictionary("wildnature:basalt_stairs_bricks_cracked", "wildnature:basalt_stairs_cobble");
		putToDictionary("wildnature:basalt_stairs_bricks_chiseled", "wildnature:basalt_stairs_bricks_cracked");
		putToDictionary("wildnature:basalt_stairs_bricks_mossy", "wildnature:basalt_stairs_cobble_mossy");
		putToDictionary("wildnature:basalt_stairs_cobble", "minecraft:gravel");
		putToDictionary("wildnature:basalt_wall", "wildnature:basalt_wall_cobble");
		putToDictionary("wildnature:basalt_wall_bricks", "wildnature:basalt_wall_bricks_cracked");
		putToDictionary("wildnature:basalt_wall_bricks_cracked", "wildnature:basalt_wall_cobble");
		putToDictionary("wildnature:basalt_wall_bricks_chiseled", "wildnature:basalt_wall_bricks_cracked");
		putToDictionary("wildnature:basalt_wall_bricks_mossy", "wildnature:basalt_wall_cobble_mossy");
		putToDictionary("wildnature:basalt_wall_cobble", "minecraft:gravel");
		putToDictionary("wildnature:basalt", "wildnature:basalt_cobble");
		putToDictionary("wildnature:basalt_bricks", "wildnature:basalt_bricks_cracked");
		putToDictionary("wildnature:basalt_bricks_cracked", "wildnature:basalt_cobble");
		putToDictionary("wildnature:basalt_bricks_chiseled", "wildnature:basalt_bricks_cracked");
		putToDictionary("wildnature:basalt_bricks_mossy", "wildnature:basalt_cobble_mossy");
		putToDictionary("wildnature:basalt_cobble", "minecraft:gravel");
		putToDictionary("wildnature:basalt_polished", "wildnature:basalt_cobble");
		putToDictionary("wildnature:basalt_small_bricks", "wildnature:basalt_bricks_cracked");
		putToDictionary("wildnature:basalt_long_bricks", "wildnature:basalt_bricks_cracked");
		putToDictionary("wildnature:basalt_ancient_bricks", "wildnature:basalt_bricks_cracked");
		putToDictionary("wildnature:basalt_roof", "wildnature:basalt_cobble");
		putToDictionary("wildnature:basalt_roof_slab", "wildnature:basalt_slab_cobble");
		putToDictionary("wildnature:basalt_roof_stairs", "wildnature:basalt_stairs_cobble");
		putToDictionary("wildnature:basalt_trapdoor", "wildnature:basalt_cobble_trapdoor");
		putToDictionary("wildnature:basalt_fence", "wildnature:basalt_cobble_fence");
		putToDictionary("wildnature:conglomerate_slab", "wildnature:conglomerate_slab_cobble");
		putToDictionary("wildnature:conglomerate_slab_bricks", "wildnature:conglomerate_slab_bricks_cracked");
		putToDictionary("wildnature:conglomerate_slab_bricks_cracked", "wildnature:conglomerate_slab_cobble");
		putToDictionary("wildnature:conglomerate_slab_bricks_chiseled", "wildnature:conglomerate_slab_bricks_cracked");
		putToDictionary("wildnature:conglomerate_slab_bricks_mossy", "wildnature:conglomerate_slab_cobble_mossy");
		putToDictionary("wildnature:conglomerate_slab_cobble", "minecraft:gravel");
		putToDictionary("wildnature:conglomerate_stairs", "wildnature:conglomerate_stairs_cobble");
		putToDictionary("wildnature:conglomerate_stairs_bricks", "wildnature:conglomerate_stairs_bricks_cracked");
		putToDictionary("wildnature:conglomerate_stairs_bricks_cracked", "wildnature:conglomerate_stairs_cobble");
		putToDictionary("wildnature:conglomerate_stairs_bricks_chiseled", "wildnature:conglomerate_stairs_bricks_cracked");
		putToDictionary("wildnature:conglomerate_stairs_bricks_mossy", "wildnature:conglomerate_stairs_cobble_mossy");
		putToDictionary("wildnature:conglomerate_stairs_cobble", "minecraft:gravel");
		putToDictionary("wildnature:conglomerate_wall", "wildnature:conglomerate_wall_cobble");
		putToDictionary("wildnature:conglomerate_wall_bricks", "wildnature:conglomerate_wall_bricks_cracked");
		putToDictionary("wildnature:conglomerate_wall_bricks_cracked", "wildnature:conglomerate_wall_cobble");
		putToDictionary("wildnature:conglomerate_wall_bricks_chiseled", "wildnature:conglomerate_wall_bricks_cracked");
		putToDictionary("wildnature:conglomerate_wall_bricks_mossy", "wildnature:conglomerate_wall_cobble_mossy");
		putToDictionary("wildnature:conglomerate_wall_cobble", "minecraft:gravel");
		putToDictionary("wildnature:conglomerate", "wildnature:conglomerate_cobble");
		putToDictionary("wildnature:conglomerate_bricks", "wildnature:conglomerate_bricks_cracked");
		putToDictionary("wildnature:conglomerate_bricks_cracked", "wildnature:conglomerate_cobble");
		putToDictionary("wildnature:conglomerate_bricks_chiseled", "wildnature:conglomerate_bricks_cracked");
		putToDictionary("wildnature:conglomerate_bricks_mossy", "wildnature:conglomerate_cobble_mossy");
		putToDictionary("wildnature:conglomerate_cobble", "minecraft:gravel");
		putToDictionary("wildnature:conglomerate_polished", "wildnature:conglomerate_cobble");
		putToDictionary("wildnature:conglomerate_small_bricks", "wildnature:conglomerate_bricks_cracked");
		putToDictionary("wildnature:conglomerate_long_bricks", "wildnature:conglomerate_bricks_cracked");
		putToDictionary("wildnature:conglomerate_ancient_bricks", "wildnature:conglomerate_bricks_cracked");
		putToDictionary("wildnature:conglomerate_roof", "wildnature:conglomerate_cobble");
		putToDictionary("wildnature:conglomerate_roof_slab", "wildnature:conglomerate_slab_cobble");
		putToDictionary("wildnature:conglomerate_roof_stairs", "wildnature:conglomerate_stairs_cobble");
		putToDictionary("wildnature:conglomerate_trapdoor", "wildnature:conglomerate_cobble_trapdoor");
		putToDictionary("wildnature:conglomerate_fence", "wildnature:conglomerate_cobble_fence");
		putToDictionary("wildnature:gneiss_slab", "wildnature:gneiss_slab_cobble");
		putToDictionary("wildnature:gneiss_slab_bricks", "wildnature:gneiss_slab_bricks_cracked");
		putToDictionary("wildnature:gneiss_slab_bricks_cracked", "wildnature:gneiss_slab_cobble");
		putToDictionary("wildnature:gneiss_slab_bricks_chiseled", "wildnature:gneiss_slab_bricks_cracked");
		putToDictionary("wildnature:gneiss_slab_bricks_mossy", "wildnature:gneiss_slab_cobble_mossy");
		putToDictionary("wildnature:gneiss_slab_cobble", "minecraft:gravel");
		putToDictionary("wildnature:gneiss_stairs", "wildnature:gneiss_stairs_cobble");
		putToDictionary("wildnature:gneiss_stairs_bricks", "wildnature:gneiss_stairs_bricks_cracked");
		putToDictionary("wildnature:gneiss_stairs_bricks_cracked", "wildnature:gneiss_stairs_cobble");
		putToDictionary("wildnature:gneiss_stairs_bricks_chiseled", "wildnature:gneiss_stairs_bricks_cracked");
		putToDictionary("wildnature:gneiss_stairs_bricks_mossy", "wildnature:gneiss_stairs_cobble_mossy");
		putToDictionary("wildnature:gneiss_stairs_cobble", "minecraft:gravel");
		putToDictionary("wildnature:gneiss_wall", "wildnature:gneiss_wall_cobble");
		putToDictionary("wildnature:gneiss_wall_bricks", "wildnature:gneiss_wall_bricks_cracked");
		putToDictionary("wildnature:gneiss_wall_bricks_cracked", "wildnature:gneiss_wall_cobble");
		putToDictionary("wildnature:gneiss_wall_bricks_chiseled", "wildnature:gneiss_wall_bricks_cracked");
		putToDictionary("wildnature:gneiss_wall_bricks_mossy", "wildnature:gneiss_wall_cobble_mossy");
		putToDictionary("wildnature:gneiss_wall_cobble", "minecraft:gravel");
		putToDictionary("wildnature:gneiss", "wildnature:gneiss_cobble");
		putToDictionary("wildnature:gneiss_bricks", "wildnature:gneiss_bricks_cracked");
		putToDictionary("wildnature:gneiss_bricks_cracked", "wildnature:gneiss_cobble");
		putToDictionary("wildnature:gneiss_bricks_chiseled", "wildnature:gneiss_bricks_cracked");
		putToDictionary("wildnature:gneiss_bricks_mossy", "wildnature:gneiss_cobble_mossy");
		putToDictionary("wildnature:gneiss_cobble", "minecraft:gravel");
		putToDictionary("wildnature:gneiss_polished", "wildnature:gneiss_cobble");
		putToDictionary("wildnature:gneiss_small_bricks", "wildnature:gneiss_bricks_cracked");
		putToDictionary("wildnature:gneiss_long_bricks", "wildnature:gneiss_bricks_cracked");
		putToDictionary("wildnature:gneiss_ancient_bricks", "wildnature:gneiss_bricks_cracked");
		putToDictionary("wildnature:gneiss_roof", "wildnature:gneiss_cobble");
		putToDictionary("wildnature:gneiss_roof_slab", "wildnature:gneiss_slab_cobble");
		putToDictionary("wildnature:gneiss_roof_stairs", "wildnature:gneiss_stairs_cobble");
		putToDictionary("wildnature:gneiss_trapdoor", "wildnature:gneiss_cobble_trapdoor");
		putToDictionary("wildnature:gneiss_fence", "wildnature:gneiss_cobble_fence");
		putToDictionary("wildnature:limestone_slab", "wildnature:limestone_slab_cobble");
		putToDictionary("wildnature:limestone_slab_bricks", "wildnature:limestone_slab_bricks_cracked");
		putToDictionary("wildnature:limestone_slab_bricks_cracked", "wildnature:limestone_slab_cobble");
		putToDictionary("wildnature:limestone_slab_bricks_chiseled", "wildnature:limestone_slab_bricks_cracked");
		putToDictionary("wildnature:limestone_slab_bricks_mossy", "wildnature:limestone_slab_cobble_mossy");
		putToDictionary("wildnature:limestone_slab_cobble", "minecraft:gravel");
		putToDictionary("wildnature:limestone_stairs", "wildnature:limestone_stairs_cobble");
		putToDictionary("wildnature:limestone_stairs_bricks", "wildnature:limestone_stairs_bricks_cracked");
		putToDictionary("wildnature:limestone_stairs_bricks_cracked", "wildnature:limestone_stairs_cobble");
		putToDictionary("wildnature:limestone_stairs_bricks_chiseled", "wildnature:limestone_stairs_bricks_cracked");
		putToDictionary("wildnature:limestone_stairs_bricks_mossy", "wildnature:limestone_stairs_cobble_mossy");
		putToDictionary("wildnature:limestone_stairs_cobble", "minecraft:gravel");
		putToDictionary("wildnature:limestone_wall", "wildnature:limestone_wall_cobble");
		putToDictionary("wildnature:limestone_wall_bricks", "wildnature:limestone_wall_bricks_cracked");
		putToDictionary("wildnature:limestone_wall_bricks_cracked", "wildnature:limestone_wall_cobble");
		putToDictionary("wildnature:limestone_wall_bricks_chiseled", "wildnature:limestone_wall_bricks_cracked");
		putToDictionary("wildnature:limestone_wall_bricks_mossy", "wildnature:limestone_wall_cobble_mossy");
		putToDictionary("wildnature:limestone_wall_cobble", "minecraft:gravel");
		putToDictionary("wildnature:limestone", "wildnature:limestone_cobble");
		putToDictionary("wildnature:limestone_bricks", "wildnature:limestone_bricks_cracked");
		putToDictionary("wildnature:limestone_bricks_cracked", "wildnature:limestone_cobble");
		putToDictionary("wildnature:limestone_bricks_chiseled", "wildnature:limestone_bricks_cracked");
		putToDictionary("wildnature:limestone_bricks_mossy", "wildnature:limestone_cobble_mossy");
		putToDictionary("wildnature:limestone_cobble", "minecraft:gravel");
		putToDictionary("wildnature:limestone_polished", "wildnature:limestone_cobble");
		putToDictionary("wildnature:limestone_small_bricks", "wildnature:limestone_bricks_cracked");
		putToDictionary("wildnature:limestone_long_bricks", "wildnature:limestone_bricks_cracked");
		putToDictionary("wildnature:limestone_ancient_bricks", "wildnature:limestone_bricks_cracked");
		putToDictionary("wildnature:limestone_roof", "wildnature:limestone_cobble");
		putToDictionary("wildnature:limestone_roof_slab", "wildnature:limestone_slab_cobble");
		putToDictionary("wildnature:limestone_roof_stairs", "wildnature:limestone_stairs_cobble");
		putToDictionary("wildnature:limestone_trapdoor", "wildnature:limestone_cobble_trapdoor");
		putToDictionary("wildnature:limestone_fence", "wildnature:limestone_cobble_fence");
		putToDictionary("wildnature:marble_slab", "wildnature:marble_slab_cobble");
		putToDictionary("wildnature:marble_slab_bricks", "wildnature:marble_slab_bricks_cracked");
		putToDictionary("wildnature:marble_slab_bricks_cracked", "wildnature:marble_slab_cobble");
		putToDictionary("wildnature:marble_slab_bricks_chiseled", "wildnature:marble_slab_bricks_cracked");
		putToDictionary("wildnature:marble_slab_bricks_mossy", "wildnature:marble_slab_cobble_mossy");
		putToDictionary("wildnature:marble_slab_cobble", "minecraft:gravel");
		putToDictionary("wildnature:marble_stairs", "wildnature:marble_stairs_cobble");
		putToDictionary("wildnature:marble_stairs_bricks", "wildnature:marble_stairs_bricks_cracked");
		putToDictionary("wildnature:marble_stairs_bricks_cracked", "wildnature:marble_stairs_cobble");
		putToDictionary("wildnature:marble_stairs_bricks_chiseled", "wildnature:marble_stairs_bricks_cracked");
		putToDictionary("wildnature:marble_stairs_bricks_mossy", "wildnature:marble_stairs_cobble_mossy");
		putToDictionary("wildnature:marble_stairs_cobble", "minecraft:gravel");
		putToDictionary("wildnature:marble_wall", "wildnature:marble_wall_cobble");
		putToDictionary("wildnature:marble_wall_bricks", "wildnature:marble_wall_bricks_cracked");
		putToDictionary("wildnature:marble_wall_bricks_cracked", "wildnature:marble_wall_cobble");
		putToDictionary("wildnature:marble_wall_bricks_chiseled", "wildnature:marble_wall_bricks_cracked");
		putToDictionary("wildnature:marble_wall_bricks_mossy", "wildnature:marble_wall_cobble_mossy");
		putToDictionary("wildnature:marble_wall_cobble", "minecraft:gravel");
		putToDictionary("wildnature:marble", "wildnature:marble_cobble");
		putToDictionary("wildnature:marble_bricks", "wildnature:marble_bricks_cracked");
		putToDictionary("wildnature:marble_bricks_cracked", "wildnature:marble_cobble");
		putToDictionary("wildnature:marble_bricks_chiseled", "wildnature:marble_bricks_cracked");
		putToDictionary("wildnature:marble_bricks_mossy", "wildnature:marble_cobble_mossy");
		putToDictionary("wildnature:marble_cobble", "minecraft:gravel");
		putToDictionary("wildnature:marble_polished", "wildnature:marble_cobble");
		putToDictionary("wildnature:marble_small_bricks", "wildnature:marble_bricks_cracked");
		putToDictionary("wildnature:marble_long_bricks", "wildnature:marble_bricks_cracked");
		putToDictionary("wildnature:marble_ancient_bricks", "wildnature:marble_bricks_cracked");
		putToDictionary("wildnature:marble_roof", "wildnature:marble_cobble");
		putToDictionary("wildnature:marble_roof_slab", "wildnature:marble_slab_cobble");
		putToDictionary("wildnature:marble_roof_stairs", "wildnature:marble_stairs_cobble");
		putToDictionary("wildnature:marble_trapdoor", "wildnature:marble_cobble_trapdoor");
		putToDictionary("wildnature:marble_fence", "wildnature:marble_cobble_fence");
		putToDictionary("wildnature:pegmatite_slab", "wildnature:pegmatite_slab_cobble");
		putToDictionary("wildnature:pegmatite_slab_bricks", "wildnature:pegmatite_slab_bricks_cracked");
		putToDictionary("wildnature:pegmatite_slab_bricks_cracked", "wildnature:pegmatite_slab_cobble");
		putToDictionary("wildnature:pegmatite_slab_bricks_chiseled", "wildnature:pegmatite_slab_bricks_cracked");
		putToDictionary("wildnature:pegmatite_slab_bricks_mossy", "wildnature:pegmatite_slab_cobble_mossy");
		putToDictionary("wildnature:pegmatite_slab_cobble", "minecraft:gravel");
		putToDictionary("wildnature:pegmatite_stairs", "wildnature:pegmatite_stairs_cobble");
		putToDictionary("wildnature:pegmatite_stairs_bricks", "wildnature:pegmatite_stairs_bricks_cracked");
		putToDictionary("wildnature:pegmatite_stairs_bricks_cracked", "wildnature:pegmatite_stairs_cobble");
		putToDictionary("wildnature:pegmatite_stairs_bricks_chiseled", "wildnature:pegmatite_stairs_bricks_cracked");
		putToDictionary("wildnature:pegmatite_stairs_bricks_mossy", "wildnature:pegmatite_stairs_cobble_mossy");
		putToDictionary("wildnature:pegmatite_stairs_cobble", "minecraft:gravel");
		putToDictionary("wildnature:pegmatite_wall", "wildnature:pegmatite_wall_cobble");
		putToDictionary("wildnature:pegmatite_wall_bricks", "wildnature:pegmatite_wall_bricks_cracked");
		putToDictionary("wildnature:pegmatite_wall_bricks_cracked", "wildnature:pegmatite_wall_cobble");
		putToDictionary("wildnature:pegmatite_wall_bricks_chiseled", "wildnature:pegmatite_wall_bricks_cracked");
		putToDictionary("wildnature:pegmatite_wall_bricks_mossy", "wildnature:pegmatite_wall_cobble_mossy");
		putToDictionary("wildnature:pegmatite_wall_cobble", "minecraft:gravel");
		putToDictionary("wildnature:pegmatite", "wildnature:pegmatite_cobble");
		putToDictionary("wildnature:pegmatite_bricks", "wildnature:pegmatite_bricks_cracked");
		putToDictionary("wildnature:pegmatite_bricks_cracked", "wildnature:pegmatite_cobble");
		putToDictionary("wildnature:pegmatite_bricks_chiseled", "wildnature:pegmatite_bricks_cracked");
		putToDictionary("wildnature:pegmatite_bricks_mossy", "wildnature:pegmatite_cobble_mossy");
		putToDictionary("wildnature:pegmatite_cobble", "minecraft:gravel");
		putToDictionary("wildnature:pegmatite_polished", "wildnature:pegmatite_cobble");
		putToDictionary("wildnature:pegmatite_small_bricks", "wildnature:pegmatite_bricks_cracked");
		putToDictionary("wildnature:pegmatite_long_bricks", "wildnature:pegmatite_bricks_cracked");
		putToDictionary("wildnature:pegmatite_ancient_bricks", "wildnature:pegmatite_bricks_cracked");
		putToDictionary("wildnature:pegmatite_roof", "wildnature:pegmatite_cobble");
		putToDictionary("wildnature:pegmatite_roof_slab", "wildnature:pegmatite_slab_cobble");
		putToDictionary("wildnature:pegmatite_roof_stairs", "wildnature:pegmatite_stairs_cobble");
		putToDictionary("wildnature:pegmatite_trapdoor", "wildnature:pegmatite_cobble_trapdoor");
		putToDictionary("wildnature:pegmatite_fence", "wildnature:pegmatite_cobble_fence");
		putToDictionary("wildnature:slate_slab", "wildnature:slate_slab_cobble");
		putToDictionary("wildnature:slate_slab_bricks", "wildnature:slate_slab_bricks_cracked");
		putToDictionary("wildnature:slate_slab_bricks_cracked", "wildnature:slate_slab_cobble");
		putToDictionary("wildnature:slate_slab_bricks_chiseled", "wildnature:slate_slab_bricks_cracked");
		putToDictionary("wildnature:slate_slab_bricks_mossy", "wildnature:slate_slab_cobble_mossy");
		putToDictionary("wildnature:slate_slab_cobble", "minecraft:gravel");
		putToDictionary("wildnature:slate_stairs", "wildnature:slate_stairs_cobble");
		putToDictionary("wildnature:slate_stairs_bricks", "wildnature:slate_stairs_bricks_cracked");
		putToDictionary("wildnature:slate_stairs_bricks_cracked", "wildnature:slate_stairs_cobble");
		putToDictionary("wildnature:slate_stairs_bricks_chiseled", "wildnature:slate_stairs_bricks_cracked");
		putToDictionary("wildnature:slate_stairs_bricks_mossy", "wildnature:slate_stairs_cobble_mossy");
		putToDictionary("wildnature:slate_stairs_cobble", "minecraft:gravel");
		putToDictionary("wildnature:slate_wall", "wildnature:slate_wall_cobble");
		putToDictionary("wildnature:slate_wall_bricks", "wildnature:slate_wall_bricks_cracked");
		putToDictionary("wildnature:slate_wall_bricks_cracked", "wildnature:slate_wall_cobble");
		putToDictionary("wildnature:slate_wall_bricks_chiseled", "wildnature:slate_wall_bricks_cracked");
		putToDictionary("wildnature:slate_wall_bricks_mossy", "wildnature:slate_wall_cobble_mossy");
		putToDictionary("wildnature:slate_wall_cobble", "minecraft:gravel");
		putToDictionary("wildnature:slate", "wildnature:slate_cobble");
		putToDictionary("wildnature:slate_bricks", "wildnature:slate_bricks_cracked");
		putToDictionary("wildnature:slate_bricks_cracked", "wildnature:slate_cobble");
		putToDictionary("wildnature:slate_bricks_chiseled", "wildnature:slate_bricks_cracked");
		putToDictionary("wildnature:slate_bricks_mossy", "wildnature:slate_cobble_mossy");
		putToDictionary("wildnature:slate_cobble", "minecraft:gravel");
		putToDictionary("wildnature:slate_polished", "wildnature:slate_cobble");
		putToDictionary("wildnature:slate_small_bricks", "wildnature:slate_bricks_cracked");
		putToDictionary("wildnature:slate_long_bricks", "wildnature:slate_bricks_cracked");
		putToDictionary("wildnature:slate_ancient_bricks", "wildnature:slate_bricks_cracked");
		putToDictionary("wildnature:slate_roof", "wildnature:slate_cobble");
		putToDictionary("wildnature:slate_roof_slab", "wildnature:slate_slab_cobble");
		putToDictionary("wildnature:slate_roof_stairs", "wildnature:slate_stairs_cobble");
		putToDictionary("wildnature:slate_trapdoor", "wildnature:slate_cobble_trapdoor");
		putToDictionary("wildnature:slate_fence", "wildnature:slate_cobble_fence");
		putToDictionary("wildnature:overgrown_stone", "minecraft:mossy_cobblestone");

		putToDictionary("minecraft:cracked_stone_bricks", "minecraft:cobblestone");
		putToDictionary("minecraft:infested_cracked_stone_bricks", "minecraft:infested_cobblestone");
		putToDictionary("minecraft:cobblestone", "minecraft:gravel");
		putToDictionary("minecraft:chiseled_sandstone", "minecraft:sandstone");
		putToDictionary("minecraft:chiseled_red_sandstone", "minecraft:red_sandstone");
		putToDictionary("minecraft:chiseled_stone_bricks", "minecraft:stone_bricks");
		putToDictionary("minecraft:infested_chiseled_stone_bricks", "minecraft:infested_stone_bricks");
		putToDictionary("minecraft:chiseled_quartz_block", "minecraft:quartz_block");
		putToDictionary("minecraft:polished_granite", "minecraft:granite");
		putToDictionary("minecraft:polished_diorite", "minecraft:diorite");
		putToDictionary("minecraft:polished_andesite", "minecraft:andesite");
		putToDictionary("minecraft:polished_granite_stairs", "minecraft:granite_stairs");
		putToDictionary("minecraft:polished_diorite_stairs", "minecraft:diorite_stairs");
		putToDictionary("minecraft:polished_andesite_stairs", "minecraft:andesite_stairs");
		putToDictionary("minecraft:polished_granite_slab", "minecraft:granite_slab");
		putToDictionary("minecraft:polished_diorite_slab", "minecraft:diorite_slab");
		putToDictionary("minecraft:polished_andesite_slab", "minecraft:andesite_slab");
		putToDictionary("minecraft:smooth_stone", "minecraft:stone");
		putToDictionary("minecraft:smooth_red_sandstone", "minecraft:red_sandstone");
		putToDictionary("minecraft:smooth_stone_slab", "minecraft:stone_slab");
		putToDictionary("minecraft:smooth_red_sandstone_stairs", "minecraft:red_sandstone_stairs");
		putToDictionary("minecraft:smooth_sandstone_stairs", "minecraft:sandstone_stairs");
		putToDictionary("minecraft:smooth_red_sandstone_slab", "minecraft:red_sandstone_slab");
		putToDictionary("minecraft:smooth_sandstone_slab", "minecraft:sandstone_slab");
		putToDictionary("minecraft:sandstone", "minecraft:sand");
		putToDictionary("minecraft:red_sandstone", "minecraft:red_sand");
		putToDictionary("minecraft:smooth_sandstone", "minecraft:sandstone");
		putToDictionary("minecraft:sandstone_slab", "minecraft:sand_slab");
		putToDictionary("minecraft:cut_sandstone_slab", "minecraft:cut_sand_slab");
		putToDictionary("minecraft:sandstone_stairs", "minecraft:sand_stairs");
		putToDictionary("minecraft:red_sandstone_wall", "minecraft:red_sand_wall");
		putToDictionary("minecraft:sandstone_wall", "minecraft:sand_wall");
		putToDictionary("minecraft:bricks", "minecraft:cracked_bricks");
		putToDictionary("minecraft:stone_bricks", "minecraft:cracked_stone_bricks");
		putToDictionary("minecraft:infested_stone_bricks", "minecraft:infested_cracked_stone_bricks");
		putToDictionary("minecraft:nether_bricks", "minecraft:cracked_nether_bricks");
		putToDictionary("minecraft:prismarine_bricks", "minecraft:cracked_prismarine_bricks");
		putToDictionary("minecraft:end_stone_bricks", "minecraft:end_stone");
		putToDictionary("minecraft:red_nether_bricks", "minecraft:cracked_red_nether_bricks");
		putToDictionary("minecraft:stone", "minecraft:cobblestone");
		putToDictionary("minecraft:stone_slab", "minecraft:cobblestone_slab");
		putToDictionary("minecraft:stone_brick_slab", "minecraft:cobblestone_brick_slab");
		putToDictionary("minecraft:stone_pressure_plate", "minecraft:cobblestone_pressure_plate");
		putToDictionary("minecraft:stone_button", "minecraft:cobblestone_button");
		putToDictionary("minecraft:mossy_stone_bricks", "minecraft:mossy_cobblestone");
		putToDictionary("minecraft:infested_mossy_stone_bricks", "minecraft:infested_mossy_cobblestone_bricks");
		putToDictionary("minecraft:stone_brick_stairs", "minecraft:cobblestone_brick_stairs");
		putToDictionary("minecraft:end_stone", "minecraft:end_cobblestone");
		putToDictionary("minecraft:mossy_stone_brick_stairs", "minecraft:mossy_cobblestone_brick_stairs");
		putToDictionary("minecraft:end_stone_brick_stairs", "minecraft:end_cobblestone_brick_stairs");
		putToDictionary("minecraft:mossy_stone_brick_slab", "minecraft:mossy_cobblestone_brick_slab");
		putToDictionary("minecraft:end_stone_brick_slab", "minecraft:end_cobblestone_brick_slab");
		putToDictionary("minecraft:mossy_stone_brick_wall", "minecraft:mossy_cobblestone_brick_wall");
		putToDictionary("minecraft:stone_brick_wall", "minecraft:cobblestone_brick_wall");
		putToDictionary("minecraft:end_stone_brick_wall", "minecraft:end_cobblestone_brick_wall");
		putToDictionary("minecraft:prismarine_bricks", "minecraft:prismarine");
		putToDictionary("minecraft:prismarine_brick_stairs", "minecraft:prismarine_stairs");
		putToDictionary("minecraft:cut_sandstone", "minecraft:sandstone");
		putToDictionary("minecraft:mossy_cobblestone", "minecraft:gravel");
	}
}

/*
		//ОБРАБОТКА ЕДИНИЧНОГО БЛОКА
	public static void changeBlock(HashSet<BlockPos> changedPoses, List<BlockPos> blocksPos, World world, BlockPos blockPos) {
		if (!blocksPos.contains(blockPos) && !changedPoses.contains(blockPos)) {
			if (world.rand.nextFloat() > 0.3) {
				BlockState newBlockState = crackedDictionary.get(world.getBlockState(blockPos).getBlock().getRegistryName().toString());
				if (newBlockState != null) world.setBlockState(blockPos, newBlockState);
			}
			checkFallable(world, blockPos);
			changedPoses.add(blockPos);
		}
	}
	
	//БЛОК ПАДАЕТ ЕСЛИ МОЖЕТ
	public static void checkFallable(World worldIn, BlockPos pos) {
		if (!worldIn.isRemote && pos.getY() > 0) {
			FallingBlockEntity fallingblockentity = new FallingBlockEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
			worldIn.addEntity(fallingblockentity);
		}
	}
	
	//МОЖЕТ ЛИ БЛОК ПАДАТЬ
	public static boolean canFallThrough(BlockState state) {
		Block block = state.getBlock();
		Material material = state.getMaterial();
		return state.getBlock() != Blocks.AIR || block == Blocks.FIRE || material.isLiquid() || material.isReplaceable();
	}
	

	
	//ОБРАБОТЧИК ВЗРЫВА
	@SubscribeEvent
	public static void explosion(ExplosionEvent event) {
		
		//ОБРАБОТКА БЛОКОВ ПЕРЕД ВЗРЫВОМ
	    World world = event.getWorld();
	    List<BlockPos> blocksPos = event.getExplosion().getAffectedBlockPositions();
	    HashSet<Triple> exploded = new HashSet<Triple>();
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
	
		//ЗАМЕНА БЛОКОВ
	public static void blockReplace(World world, BlockPos blockPos, HashSet<Triple> exploded, boolean check) {
			
		//ОБРАБОТКА БЛОКОВ
		Triple tripleBlockPos = blockPosToTriple(blockPos);
		if (check && exploded.contains(tripleBlockPos)) return;
	    exploded.add(tripleBlockPos);
	    
		//ФУНКЦИОНАЛ ОБВАЛА
		checkFallable(world, blockPos);

	    if (world.getRandom().nextFloat() < 0.4F) return;
	    BlockState blockState = crackedDictionary.get(world.getBlockState(blockPos).getBlock().getRegistryName().toString());
	    if (blockState != null) world.setBlockState(blockPos, blockState);
	}
	
		
	//ЛОГГЕР
	static final Logger LOGGER = LogManager.getLogger();

	//ПРЕВРАЩЕНИЕ BLOCKPOS В TRIPLE
	public static Triple blockPosToTriple(BlockPos blockPos) {
		return new Triple(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	//БЛОК ПАДАЕТ ЕСЛИ МОЖЕТ
	public static void checkFallable(World worldIn, BlockPos pos) {
		if (!worldIn.isRemote && canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() > 0) {
			FallingBlockEntity fallingblockentity = new FallingBlockEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
			worldIn.addEntity(fallingblockentity);
		}
	}
	
	//МОЖЕТ ЛИ БЛОК ПАДАТЬ
	public static boolean canFallThrough(BlockState state) {
		Block block = state.getBlock();
		Material material = state.getMaterial();
		return state.getBlock() != Blocks.AIR || block == Blocks.FIRE || material.isLiquid() || material.isReplaceable();
	}
	
*/