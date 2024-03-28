package me.dave.lushlib.placeholder;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderHandler {
    private final String identifier;
    private final Pattern placeholderPattern;
    private final ConcurrentHashMap<String, Placeholder> placeholders = new ConcurrentHashMap<>();

    public PlaceholderHandler(String identifier) {
        this.identifier = identifier;
        this.placeholderPattern = Pattern.compile("%" + identifier + "_([a-zA-Z0-9_ ]+)%");
    }

    public String getIdentifier() {
        return identifier;
    }

    public ItemStack parseItemStack(Player player, ItemStack itemStack) {
        ItemStack item = itemStack.clone();
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(parseString(itemMeta.getDisplayName(), player));

            List<String> lore = itemMeta.getLore();
            if (lore != null) {
                List<String> newLore = new ArrayList<>();
                for (String loreLine : lore) {
                    newLore.add(parseString(loreLine, player));
                }
                itemMeta.setLore(newLore);
            }
            item.setItemMeta(itemMeta);
        }

        return item;
    }

    public String parseString(String string, Player player) {
        Matcher matcher = placeholderPattern.matcher(string);
        Set<String> matches = new HashSet<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }

        for (String match : matches) {
            String parsed = parsePlaceholder(match, player);
            if (parsed == null) {
                continue;
            }
            string = string.replaceAll(match, parsed);
        }

        return string;
    }

    public String parsePlaceholder(String params, Player player) {
        String[] paramsArr = params.split("_");

        Placeholder currentPlaceholder = null;
        String currParams = params;
        for (int i = 0; i < paramsArr.length; i++) {
            boolean found = false;

            for (Placeholder subPlaceholder : currentPlaceholder != null ? currentPlaceholder.getChildren() : placeholders.values()) {
                if (subPlaceholder.matches(params)) {
                    currentPlaceholder = subPlaceholder;
                    currParams = currParams.replace(subPlaceholder.getContent() + "_", "");

                    found = true;
                    break;
                }
            }

            if (!found) {
                break;
            }
        }

        if (currentPlaceholder != null) {
            try {
                return currentPlaceholder.parse(paramsArr, player);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    public PlaceholderHandler registerPlaceholder(Placeholder placeholder) {
        placeholders.put(placeholder.getContent(), placeholder);
        return this;
    }

    public PlaceholderHandler unregisterPlaceholder(String content) {
        placeholders.remove(content);
        return this;
    }
}