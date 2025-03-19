package org.lushplugins.lushlib.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

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

    public static BlockPosition from(Block block) {
        return new BlockPosition(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    public static BlockPosition adapt(Location location) {
        return new BlockPosition(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
