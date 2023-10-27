package me.dave.platyutils.utils;

import org.bukkit.Location;

@SuppressWarnings("unused")
public record SimpleLocation(double x, double y, double z) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleLocation that = (SimpleLocation) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0;
    }

    public static SimpleLocation from(Location location) {
        return new SimpleLocation(location.getX(), location.getY(), location.getZ());
    }
}
