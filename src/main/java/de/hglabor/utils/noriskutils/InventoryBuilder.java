package de.hglabor.utils.noriskutils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.function.Consumer;

public final class InventoryBuilder {

    private String inventoryName;
    private InventoryType inventoryType;
    private int inventorySlots;
    private Plugin plugin;
    private HashMap<Integer, ItemStack> items;
    private HashMap<ItemStack, Consumer<InventoryClickEvent>> clickActions;

    public InventoryBuilder(Plugin plugin) {
        inventoryType = null;
        inventorySlots = 9;
        inventoryName = "Inventory Title";
        items = new HashMap<>();
        clickActions = new HashMap<>();
        this.plugin = plugin;
    }

    public InventoryBuilder withItem(ItemStack itemStack, int slot) {
        items.put(slot, itemStack);
        clickActions.put(itemStack, clickEvent -> {});
        return this;
    }

    public InventoryBuilder withItem(ItemStack itemStack, int slot, Consumer<InventoryClickEvent> clickAction) {
        items.put(slot, itemStack);
        clickActions.put(itemStack, clickAction);
        return this;
    }

    public InventoryBuilder withType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
        return this;
    }

    public InventoryBuilder withSlots(int slots) {
        this.inventorySlots = slots;
        return this;
    }

    public InventoryBuilder withName(String name) {
        this.inventoryName = name;
        return this;
    }

    public Inventory build() {
        Inventory inventory;
        if(inventoryType != null) {
            inventory = Bukkit.createInventory(null, inventoryType, inventoryName);
        } else {
            inventory = Bukkit.createInventory(null, inventorySlots, inventoryName);
        }
        for (int i : items.keySet()) {
            inventory.setItem(i, items.get(i));
            Consumer<InventoryClickEvent> consumer = clickActions.get(items.get(i));
            Bukkit.getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void onInventoryClick(InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    if(event.getClickedInventory() != null) {
                        if(event.getView().getTitle().equalsIgnoreCase(inventoryName)) {
                            if(event.getCurrentItem() != null) {
                                if(event.getCurrentItem().isSimilar(items.get(i))) {
                                    event.setCancelled(true);
                                    consumer.accept(event);
                                    event.getHandlers().unregister(this);
                                }
                            }
                        }
                    }
                }
            }, plugin);
        }
        return inventory;
    }
}
