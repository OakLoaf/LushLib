package org.lushplugins.lushlib.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.joml.Vector3i;

import java.util.Objects;

@SuppressWarnings("unused")
public record BlockPosition(World world, int x, int y, int z) {

    public Block getBlock() {
        return world.getBlockAt(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockPosition that = (BlockPosition) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0 && Objects.equals(world, that.world);
    }

    public Vector3i asVector() {
        return new Vector3i(x, y, z);
    }

    public static BlockPosition from(Block block) {
        return new BlockPosition(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    public static BlockPosition from(BlockState blockState) {
        return new BlockPosition(blockState.getWorld(), blockState.getX(), blockState.getY(), blockState.getZ());
    }

    public static BlockPosition from(Location location) {
        return new BlockPosition(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * @see BlockPosition#from(Location)
     */
    @Deprecated
    public static BlockPosition adapt(Location location) {
        return BlockPosition.from(location);
    }

    public static BlockPosition from(World world, Vector3i position) {
        return new BlockPosition(world, position.x(), position.y(), position.z());
    }
}
