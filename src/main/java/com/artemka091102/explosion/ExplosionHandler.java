// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
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
    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void explosion(ExplosionEvent event) {
        Explosion explosion = event.getExplosion();
        List<BlockPos> blocksPos = explosion.getAffectedBlockPositions();
        Vec3d explosionPos = explosion.getPosition();
        World world = event.getWorld();
        BlockPos newBlockPos;
        Block newBlock;
        for (BlockPos blockPos : blocksPos) {
            //blocks near given
            for (int i = 0; i < 6; i++) {
                //getting block
                newBlockPos = Utils.blockPosNearby(blockPos, i);
                newBlock = world.getBlockState(newBlockPos).getBlock();
                //if we cant do anything continue
                if (Utils.canNotBeExploded(newBlock)) continue;
                //if we can fly through block behind
                if (Utils.canPassThrough(world.getBlockState(Utils.blockPosNearby(newBlockPos, i)).getBlock())) {
                    //if physics is off exit
                    if(Utils.physics) {
                        //degrade and throw this block
                        Utils.degradeBlock(world, newBlockPos, 1);
                        Utils.throwBlock(world, newBlockPos,
                                Utils.motion(Utils.centerOfBlock(newBlockPos), Utils.roundVec3d(explosionPos, 1),
                                        1.0 / (world.getBlockState(newBlockPos).getHarvestLevel() + 2)));
                    }
                }
                //if cant simply degrade
                else {
                    Utils.degradeBlock(world, newBlockPos, 0.5F);
                }
            }
            //degrade block which will be exploded
            newBlock = world.getBlockState(blockPos).getBlock();
            if (Utils.canNotBeExploded(newBlock)) continue;
            Utils.degradeBlock(world, blockPos, 1);
        }
        //clear list of degraded blocks
        Utils.degradedBlocksPos.clear();
    }
}