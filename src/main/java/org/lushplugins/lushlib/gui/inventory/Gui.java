package org.lushplugins.lushlib.gui.inventory;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.LushLib;
import org.lushplugins.lushlib.gui.button.Button;
import org.lushplugins.lushlib.gui.button.ItemButton;
import org.lushplugins.lushlib.gui.button.SimpleItemButton;
import org.lushplugins.lushlib.manager.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.lushplugins.lushlib.utils.DisplayItemStack;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public abstract class Gui {
    static {
        LushLib.getInstance().getPlugin().registerManager(new GuiManager());
    }

    private final Inventory inventory;
    private final Player player;
    private final ConcurrentHashMap<Integer, Button> buttons = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Boolean> slotLockMap = new ConcurrentHashMap<>();

    public Gui(int size, String title, Player player) {
        inventory = Bukkit.getServer().createInventory(null, size, title);
        this.player = player;
    }

    public Gui(InventoryType inventoryType, String title, Player player) {
        inventory = Bukkit.getServer().createInventory(null, inventoryType, title);
        this.player = player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    @Deprecated(since = "0.7.0",forRemoval = true)
    protected void addItem(int slot, @Nullable ItemStack item) {
        setItem(slot, item);
    }

    protected void setItem(int slot, @Nullable ItemStack item) {
        if (item == null) {
            item = new ItemStack(Material.AIR);
        }

        inventory.setItem(slot, item);
    }

    @Nullable
    public Button getButton(int slot) {
        return buttons.get(slot);
    }

    public ImmutableMap<Integer, Button> getButtons() {
        return ImmutableMap.copyOf(buttons);
    }

    public void addButton(int slot, ItemStack item, Consumer<InventoryClickEvent> task) {
        addButton(slot, new SimpleItemButton(DisplayItemStack.builder(item).build(), task));
    }

    public void addButton(int slot, DisplayItemStack item, Consumer<InventoryClickEvent> task) {
        addButton(slot, new SimpleItemButton(item, task));
    }

    public void addButton(int slot, Consumer<InventoryClickEvent> task) {
        addButton(slot, new SimpleItemButton(null, task));
    }

    public void addButton(int slot, Button button) {
        buttons.put(slot, button);
    }

    public void removeButton(int slot) {
        buttons.remove(slot);
    }

    public void clearButtons() {
        buttons.clear();
    }

    public void lockSlot(int slot) {
        lockSlot(slot, true);
    }

    public void unlockSlot(int slot) {
        lockSlot(slot, false);
    }

    public void lockSlot(int slot, boolean locked) {
        slotLockMap.put(slot, locked);
    }

    public void lockSlots(int... slots) {
        for (int slot : slots) {
            lockSlot(slot, true);
        }
    }

    public void unlockSlots(int... slots) {
        for (int slot : slots) {
            lockSlot(slot, false);
        }
    }

    public boolean isSlotLocked(int slot) {
        return slotLockMap.getOrDefault(slot, true);
    }

    public void refresh(int slot) {
        Button button = buttons.get(slot);
        if (button instanceof ItemButton itemButton) {
            setItem(slot, itemButton.getItemStack(player));
        }
    }

    public void refresh() {
        buttons.forEach((slot, button) -> {
            if (button instanceof ItemButton itemButton) {
                setItem(slot, itemButton.getItemStack(player));
            }
        });
    }

    /**
     * @see Gui#refresh()
     */
    @Deprecated(since = "0.7.2")
    public void recalculateContents() {}

    public void open() {
        refresh();

        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(LushLib.getInstance().getPlugin(), () -> player.openInventory(inventory));
        } else {
            player.openInventory(inventory);
        }

        LushLib.getInstance().getPlugin().getManager(GuiManager.class).ifPresent(guiManager -> guiManager.addGui(player.getUniqueId(), this));
    }

    public void close() {
        player.closeInventory();
    }


    // Event Methods
    public void onOpen(InventoryOpenEvent event) {}

    public void onClose(InventoryCloseEvent event) {
        LushLib.getInstance().getPlugin().getManager(GuiManager.class).ifPresent(guiManager -> guiManager.removeGui(player.getUniqueId()));
    }

    public void onClick(InventoryClickEvent event) {
        this.onClick(event, false);
    }

    public void onClick(InventoryClickEvent event, boolean cancelAll) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        int slot = event.getRawSlot();

        Button button = buttons.get(slot);
        if (button != null) {
            try {
                button.click(event);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        if (cancelAll) {
            event.setCancelled(true);
            return;
        }

        switch (event.getAction()) {
            case COLLECT_TO_CURSOR -> event.setCancelled(true);
            case DROP_ALL_SLOT, DROP_ONE_SLOT, PLACE_ALL, PLACE_SOME, PLACE_ONE, PICKUP_ALL, PICKUP_HALF, PICKUP_SOME, PICKUP_ONE, SWAP_WITH_CURSOR, CLONE_STACK, HOTBAR_SWAP, HOTBAR_MOVE_AND_READD  -> {
                if (clickedInventory.equals(inventory) && isSlotLocked(slot)) {
                    event.setCancelled(true);
                }
            }
            case MOVE_TO_OTHER_INVENTORY -> {
                event.setCancelled(true);
                if (!clickedInventory.equals(inventory)) {
                    List<Integer> unlockedSlots = slotLockMap.entrySet()
                            .stream()
                            .filter(entry -> !entry.getValue())
                            .map(Map.Entry::getKey)
                            .sorted()
                            .toList();

                    ItemStack clickedItem = event.getCurrentItem();
                    if (clickedItem != null) {
                        int remainingToDistribute = clickedItem.getAmount();
                        int backupDestinationSlot = -1;
                        boolean complete = false;
                        for (int unlockedSlot : unlockedSlots) {
                            if (complete) {
                                break;
                            }

                            ItemStack slotItem = inventory.getItem(unlockedSlot);
                            if ((slotItem == null || slotItem.getType() == Material.AIR) && backupDestinationSlot == -1) {
                                backupDestinationSlot = unlockedSlot;
                                continue;
                            }

                            if (slotItem != null && slotItem.isSimilar(clickedItem)) {
                                int spaceInStack = slotItem.getMaxStackSize() - slotItem.getAmount();

                                if (spaceInStack <= remainingToDistribute) {
                                    slotItem.setAmount(slotItem.getAmount() + remainingToDistribute);
                                    clickedItem.setType(Material.AIR);
                                    complete = true;
                                } else if (spaceInStack > 0) {
                                    remainingToDistribute -= spaceInStack;
                                    slotItem.setAmount(slotItem.getMaxStackSize());
                                    clickedItem.setAmount(clickedItem.getAmount() - spaceInStack);
                                }
                            }
                        }

                        if (!complete && backupDestinationSlot != -1) {
                            inventory.setItem(backupDestinationSlot, clickedItem);
                            event.getInventory().setItem(event.getSlot(), null);
                        }
                    }
                }
            }
        }
    }

    public void onDrag(InventoryDragEvent event) {
        for (int slot : event.getRawSlots()) {
            if (slot <= 53 && isSlotLocked(slot)) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
