package org.lushplugins.lushlib.utils;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

@SuppressWarnings("unused")
public record SimpleLocation(World world, double x, double y, double z) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleLocation that = (SimpleLocation) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0 && Objects.equals(world, that.world);
    }

    public static SimpleLocation adapt(Location location) {
        return new SimpleLocation(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }
}
