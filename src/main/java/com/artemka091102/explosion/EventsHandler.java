package com.artemka091102.explosion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
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
			
			//ПЕРЕМЕННЫЕ
			Explosion explosion = event.getExplosion();
			List<BlockPos> blocksPos = explosion.getAffectedBlockPositions();
			Vec3d center = explosion.getPosition();
			
			HashMap<Pair, Triple> sizeX = new HashMap<Pair, Triple>(); //(Y, Z), (Xmin, Xmax, Xlen)
			HashMap<Pair, Triple> sizeY = new HashMap<Pair, Triple>(); //(X, Z), (Ymin, Ymax, Ylen)
			HashMap<Pair, Triple> sizeZ = new HashMap<Pair, Triple>(); //(X, Y), (Zmin, Zmax, Zlen)
			
			//ОБРАБОТКА СПИСКА БЛОКОВ
			for (BlockPos blockPos : blocksPos) {
				
				//ВОЗДУХ ОТСЕКАЕМ СРАЗУ
				if (world.getBlockState(blockPos).getBlock() == Blocks.AIR) continue;
				if (world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK) continue;
				
				//НАХОЖДЕНИЕ ГЛУБИНЫ ВОРОНКИ В ПРОЕКЦИИ НА ПЛОСКОСТЬ
				Pair coordsX = new Pair(blockPos.getY(), blockPos.getZ());
				Pair coordsY = new Pair(blockPos.getX(), blockPos.getZ());
				Pair coordsZ = new Pair(blockPos.getX(), blockPos.getY());

				//НАХОЖДЕНИЕ ПАРАМЕТРОВ ВОРОНКИ
				if (sizeX.containsKey(coordsX)) {
					if (sizeX.get(coordsX).a > blockPos.getX()) sizeX.get(coordsX).a = blockPos.getX();
					if (sizeX.get(coordsX).b < blockPos.getX()) sizeX.get(coordsX).a = blockPos.getX();
					sizeX.get(coordsX).c += 1;
				} else {
					Triple x = new Triple(blockPos.getX(), blockPos.getX(), 1);
					sizeX.put(coordsX, x);
				}
				if (sizeY.containsKey(coordsY)) {
					if (sizeY.get(coordsY).a > blockPos.getY()) sizeY.get(coordsY).a = blockPos.getY();
					if (sizeY.get(coordsY).b < blockPos.getY()) sizeY.get(coordsY).a = blockPos.getY();
					sizeY.get(coordsY).c += 1;
				} else {
					Triple y = new Triple(blockPos.getY(), blockPos.getY(), 1);
					sizeY.put(coordsY, y);
				}
				if (sizeZ.containsKey(coordsZ)) {
					if (sizeZ.get(coordsZ).a > blockPos.getZ()) sizeZ.get(coordsZ).a = blockPos.getZ();
					if (sizeZ.get(coordsZ).b < blockPos.getZ()) sizeZ.get(coordsZ).a = blockPos.getZ();
					sizeZ.get(coordsZ).c += 1;
				} else {
					Triple z = new Triple(blockPos.getZ(), blockPos.getZ(), 1);
					sizeZ.put(coordsZ, z);
				}
				
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
			}
			
			//ОБВАЛИВАЕМ ВСЕ БЛОКИ ВЫШЕ
			
			double k = 4;
			double dx;
			double dy;
			double dz;
			double distance;

			//(Y, Z), (Xmin, Xmax, Xlen)
			for (Entry<Pair, Triple> entry : sizeX.entrySet()) {
				for (int i = 1; i <= entry.getValue().c; i++) {
					BlockPos min = new BlockPos(entry.getValue().a-i+0.5D, entry.getKey().a, entry.getKey().b+0.5D);
					BlockPos max = new BlockPos(entry.getValue().b+i+0.5D, entry.getKey().a, entry.getKey().b+0.5D);
					FallingBlockEntity minFBE = new FallingBlockEntity(world, min.getX(), min.getY(), min.getZ(), world.getBlockState(min));
					FallingBlockEntity maxFBE = new FallingBlockEntity(world, max.getX(), max.getY(), max.getZ(), world.getBlockState(max));
					if (world.getBlockState(min).getBlock() == Blocks.AIR || world.getBlockState(min).getBlock() == Blocks.BEDROCK || world.getBlockState(max).getBlock() == Blocks.AIR || world.getBlockState(max).getBlock() == Blocks.BEDROCK) continue;
					dx = min.getX() - center.x;
					dy = min.getY() - center.y;
					dz = min.getZ() - center.z;
					distance = dx*dx+dy*dy+dz*dz;
					minFBE.setVelocity(k*dx/distance, k*dy/distance, k*dz/distance);
					world.addEntity(minFBE);
					dx = max.getX() - center.x;
					dy = max.getY() - center.y;
					dz = max.getZ() - center.z;
					distance = dx*dx+dy*dy+dz*dz;
					maxFBE.setVelocity(k*dx/distance, k*dy/distance, k*dz/distance);
					world.addEntity(maxFBE);
				}
		    }
			//(X, Z), (Ymin, Ymax, Ylen)
			for (Entry<Pair, Triple> entry : sizeY.entrySet()) {
				for (int i = 1; i <= entry.getValue().c; i++) {
					BlockPos min = new BlockPos(entry.getKey().a+0.5D, entry.getValue().a-i, entry.getKey().b+0.5D);
					BlockPos max = new BlockPos(entry.getKey().a+0.5D, entry.getValue().b+i, entry.getKey().b+0.5D);
					FallingBlockEntity minFBE = new FallingBlockEntity(world, min.getX(), min.getY(), min.getZ(), world.getBlockState(min));
					FallingBlockEntity maxFBE = new FallingBlockEntity(world, max.getX(), max.getY(), max.getZ(), world.getBlockState(max));
					if (world.getBlockState(min).getBlock() == Blocks.AIR || world.getBlockState(min).getBlock() == Blocks.BEDROCK || world.getBlockState(max).getBlock() == Blocks.AIR || world.getBlockState(max).getBlock() == Blocks.BEDROCK) continue;
					dx = min.getX() - center.x;
					dy = min.getY() - center.y;
					dz = min.getZ() - center.z;
					distance = dx*dx+dy*dy+dz*dz;
					minFBE.setVelocity(k*dx/distance, k*dy/distance, k*dz/distance);
					world.addEntity(minFBE);
					dx = max.getX() - center.x;
					dy = max.getY() - center.y;
					dz = max.getZ() - center.z;
					distance = dx*dx+dy*dy+dz*dz;
					maxFBE.setVelocity(k*dx/distance, k*dy/distance, k*dz/distance);
					world.addEntity(maxFBE);
				}
		    }
			//(X, Y), (Zmin, Zmax, Zlen)
			for (Entry<Pair, Triple> entry : sizeZ.entrySet()) {
				for (int i = 1; i <= entry.getValue().c; i++) {
					BlockPos min = new BlockPos(entry.getKey().a+0.5D, entry.getKey().b, entry.getValue().a-i+0.5D);
					BlockPos max = new BlockPos(entry.getKey().a+0.5D, entry.getKey().b, entry.getValue().b+i+0.5D);
					FallingBlockEntity minFBE = new FallingBlockEntity(world, min.getX(), min.getY(), min.getZ(), world.getBlockState(min));
					FallingBlockEntity maxFBE = new FallingBlockEntity(world, max.getX(), max.getY(), max.getZ(), world.getBlockState(max));
					if (world.getBlockState(min).getBlock() == Blocks.AIR || world.getBlockState(min).getBlock() == Blocks.BEDROCK || world.getBlockState(max).getBlock() == Blocks.AIR || world.getBlockState(max).getBlock() == Blocks.BEDROCK) continue;
					dx = min.getX() - center.x;
					dy = min.getY() - center.y;
					dz = min.getZ() - center.z;
					distance = dx*dx+dy*dy+dz*dz;
					minFBE.setVelocity(k*dx/distance, k*dy/distance, k*dz/distance);
					world.addEntity(minFBE);
					dx = max.getX() - center.x;
					dy = max.getY() - center.y;
					dz = max.getZ() - center.z;
					distance = dx*dx+dy*dy+dz*dz;
					maxFBE.setVelocity(k*dx/distance, k*dy/distance, k*dz/distance);
					world.addEntity(maxFBE);
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
	public static void checkFallable(World world, BlockPos blockPos, Vec3d center) {
		if (!world.isRemote && blockPos.getY() > 0 && world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
			double k = 4;
			double dx = blockPos.getX() - center.x;
			double dy = blockPos.getY() - center.y;
			double dz = blockPos.getZ() - center.z;
			double distance = dx*dx+dy*dy+dz*dz;
			FallingBlockEntity fallingblockentity = new FallingBlockEntity(world, (double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, world.getBlockState(blockPos));
			fallingblockentity.setVelocity(k*dx/distance, k*dy/distance, k*dz/distance);
			world.addEntity(fallingblockentity);
		}
	}
	
	//МОЖЕТ ЛИ БЛОК ПАДАТЬ
	public static boolean canFallThrough(BlockState state) {
		return state.getBlock() != Blocks.AIR || state.getBlock() == Blocks.FIRE || state.getMaterial().isLiquid() || state.getMaterial().isReplaceable();
	}

	//ПИШЕМ В ЧАТ
	public static void writeToChat(World world, Object object) {
		if (!debug) return ;
		List<? extends PlayerEntity> players = world.getWorld().getPlayers();
		for (PlayerEntity player : players) {
			player.sendMessage(new StringTextComponent(object.toString()));

		}
	}
	
	//СЛОВАРЬ СООТВЕТСТВИЙ БЛОКОВ
	public static final Map<String, BlockState> crackedDictionary = new HashMap<String, BlockState>();

	//ДЕБАГ РЕЖИМ
	private static final boolean debug = true;
	
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

		putToDictionary("minecraft:grass_block", "minecraft:dirt");
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
//ПОЛУЧЕНИЕ ВЫСОТЫ ОБВАЛА
Pair xz = new Pair(0, 0);
xz.a = blockPos.getX();
xz.b = blockPos.getZ();
if (deep.containsKey(xz)) {
	if (deep.get(xz).a < blockPos.getY()) {
		deep.get(xz).a = blockPos.getY();
	}
} else {
	Pair yd = new Pair(0, 0);
	yd.a = blockPos.getY();
	yd.b = 0;
	deep.put(xz, yd);
}
deep.get(xz).b += 1;
*/