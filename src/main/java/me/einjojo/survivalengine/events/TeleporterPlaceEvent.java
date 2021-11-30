package me.einjojo.survivalengine.events;

import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class TeleporterPlaceEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private boolean cancelled;
    private ItemStack itemStack;
    private final Block block;

    public TeleporterPlaceEvent(Player player, Block block) {
        this.player = player;
        this.block = block;
        this.cancelled = false;
    }

    public TeleporterPlaceEvent(Player player, Block block, ItemStack itemStack) {
        this.player = player;
        this.block = block;
        this.itemStack = itemStack;
        this.cancelled = false;

        itemStack.setAmount(itemStack.getAmount() - 1);
    }

    public Player getPlayer() {
        return player;
    }
    public Block getBlock() {
        return block;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() { return this.cancelled; }

    @Override
    public void setCancelled(boolean b) { this.cancelled = b; if (itemStack != null) { player.getInventory().addItem(TeleporterRecipe.getItemStack()); } }
}
