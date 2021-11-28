package me.einjojo.survivalengine.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeleporterPlaceEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private boolean cancelled;
    private final Block block;

    public TeleporterPlaceEvent(Player player, Block block) {
        this.player = player;
        this.block = block;
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
    public void setCancelled(boolean b) { this.cancelled = b; }
}
