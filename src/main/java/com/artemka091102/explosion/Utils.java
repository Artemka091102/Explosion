// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package com.artemka091102.explosion;

import java.util.HashSet;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class Utils {

    public static final HashSet<BlockPos> degradedBlocksPos = new HashSet<>();

    /**
     * Writes something to chat
     *
     * @param world  - needs to detect players
     * @param object - may be anything, this will be written to chat
     */
    @SuppressWarnings("unused")
    public static void writeToChat(World world, Object object) {
        for (PlayerEntity player : world.getWorld().getPlayers()) {
            player.sendMessage(new StringTextComponent(object.toString()));
        }
    }

    /**
     * Returns the position near to the given by direction's id
     *
     * @param blockPos - position, near which one we need new one
     * @param id       - direction's id
     * @return new blockPos, if id isn't in [1..6] returns (0, 0, 0)
     */
    public static BlockPos blockPosNearby(BlockPos blockPos, int id) {
        if (id == 0) return blockPos.east();
        if (id == 1) return blockPos.up();
        if (id == 2) return blockPos.south();
        if (id == 3) return blockPos.west();
        if (id == 4) return blockPos.down();
        if (id == 5) return blockPos.north();
        return new BlockPos(0, 0, 0);
    }

    /**
     * Returns coordinates of given block's center in Vec3d
     *
     * @param blockPos - blockPos with center we need
     * @return center's coordinates
     */
    public static Vec3d centerOfBlock(BlockPos blockPos) {
        return new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
    }

    /**
     * Returns rounded value of Vec3d
     *
     * @param vec   - Vec3d we need to round
     * @param power - number of decimal numbers
     * @return rounded Vec3d
     */
    public static Vec3d roundVec3d(Vec3d vec, int power) {
        double pow = Math.pow(10, power);
        return new Vec3d(Math.round(vec.getX() * pow) / pow,
                Math.round(vec.getY() * pow) / pow,
                Math.round(vec.getZ() * pow) / pow);
    }

    /**
     * Returns true if block can be exploded
     *
     * @param block - block which can or can't be exploded
     * @return true if can, false if can't
     */
    public static boolean canNotBeExploded(Block block) {
        return block.getDefaultState().getMaterial().getPushReaction() == PushReaction.DESTROY;
    }

    /**
     * Returns true if block can be passed through
     *
     * @param block - block which can or can't be passed
     * @return true if can, false if can't
     */
    public static boolean canPassThrough(Block block) {
        return block == Blocks.AIR
                || block == Blocks.FIRE
                || block.getDefaultState().getMaterial().isLiquid()
                || block.getDefaultState().getMaterial().isReplaceable();
    }

    /**
     * Degrade block on coordinates to cracked version
     *
     * @param world    - in which block is situated
     * @param blockPos - position of block should be degraded
     * @param chance   - chance of degradation
     */
    public static void degradeBlock(World world, BlockPos blockPos, float chance) {
        if (world.rand.nextFloat() > chance || degradedBlocksPos.contains(blockPos)) return;
        BlockState newBlockState =
                ModsController.getFromCrackedDict(Objects.requireNonNull(world.getBlockState(blockPos).getBlock().getRegistryName()).toString());
        if (newBlockState == null) return;
        world.setBlockState(blockPos, newBlockState);
        degradedBlocksPos.add(blockPos);
    }

    public static void throwBlock(World world, BlockPos blockPos, Vec3d velocity) {
        FallingBlockEntity FBE = new FallingBlockEntity(world,
                blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, world.getBlockState(blockPos));
        FBE.setMotion(velocity);
        world.addEntity(FBE);
    }

    /**
     * Returns something's motion vector after explosion
     *
     * @param blockPos     - something's position
     * @param explosionPos - explosion's position
     * @param multiplier   - vector's multiplier
     * @return motion vector
     */
    public static Vec3d motion(Vec3d blockPos, Vec3d explosionPos, double multiplier) {
        double x = (blockPos.x - explosionPos.x);
        double y = (blockPos.y - explosionPos.y);
        double z = (blockPos.z - explosionPos.z);
        return new Vec3d(x == 0 ? 0 : multiplier / x,
                y == 0 ? 0 : multiplier / y,
                z == 0 ? 0 : multiplier / z);
    }
}
