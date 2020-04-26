package com.artemka091102.explosion;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class ExplosionHandler {
    @SubscribeEvent
    public static void explosion(ExplosionEvent event) {
        Explosion explosion = event.getExplosion();
        List<BlockPos> blocksPos = explosion.getAffectedBlockPositions();
        Vec3d explosionPos = explosion.getPosition();
        World world = event.getWorld();
    	BlockPos newBlockPos;
    	Block newBlock;
        for (BlockPos blockPos : blocksPos) {
            for (int i = 0; i < 6; i++) {
            	newBlockPos = Utils.blockPosNearby(blockPos, i);
            	newBlock = world.getBlockState(newBlockPos).getBlock();
                if (!Utils.canBeExploded(newBlock)) continue;
            	if (Utils.canPassThrough(world.getBlockState(Utils.blockPosNearby(newBlockPos, i)).getBlock())) {
            		Utils.degradeBlock(world, newBlockPos, 1);
            		Utils.throwBlock(world, newBlockPos,
            				Utils.motion(Utils.centerOfBlock(newBlockPos), Utils.roundVec3d(explosionPos, 1),
            						1 / (world.getBlockState(newBlockPos).getHarvestLevel()+2)));
            	} else {
            		Utils.degradeBlock(world, newBlockPos, 0.5F);
            	}
            }
        	newBlock = world.getBlockState(blockPos).getBlock();
            if (!Utils.canBeExploded(newBlock)) continue;
            Utils.degradeBlock(world, blockPos, 1);
        }
        Utils.degradedBlocksPos.clear();
    }
}