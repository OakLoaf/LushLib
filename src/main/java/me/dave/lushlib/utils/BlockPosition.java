package me.dave.lushlib.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

@SuppressWarnings("unused")
public record BlockPosition(World world, int x, int y, int z) {

    public Block getBlock() {
        return world.getBlockAt(x, y, z);
    }

    public static BlockPosition adapt(Location location) {
        return new BlockPosition(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
