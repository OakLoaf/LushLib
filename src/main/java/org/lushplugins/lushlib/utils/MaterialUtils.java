package org.lushplugins.lushlib.utils;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class MaterialUtils {

    @NotNull
    public static List<Material> from(List<String> stringList) {
        return TagUtils.parseStringList(stringList, TagUtils.TagType.MATERIALS);
    }

    @NotNull
    public static Collection<Material> from(String string) {
        return TagUtils.parseString(string, TagUtils.TagType.MATERIALS);
    }
}
