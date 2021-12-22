package me.einjojo.survivalengine.enchantment;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class TelepathyEnchantment extends EnchantmentWrapper implements Listener {

    public TelepathyEnchantment(SurvivalEngine plugin) {
        super("telekinese", "Telekinese", 1);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Block block = e.getBlock();

        if(itemStack.getItemMeta() == null) return;
        if(!itemStack.getItemMeta().hasEnchant(this)) return;
        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if(block.getState() instanceof Container) return;
        e.setDropItems(false);

        Collection<ItemStack> drops = e.getBlock().getDrops(itemStack, player);
        handleDrops(player, drops);
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {
        Player player = e.getEntity().getKiller();
        if(player == null) return;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if(itemStack.getItemMeta() == null) return;
        if(!itemStack.getItemMeta().hasEnchant(this)) return;
        List<ItemStack> drops = e.getDrops();
        handleDrops(player, drops);
        drops.clear();
        player.setTotalExperience(player.getTotalExperience() + e.getDroppedExp());
    }


    private void handleDrops(Player receiver, Collection<ItemStack> drop) {
        if(drop.size() == 0) {
            return;
        }
        drop.forEach((item) -> {
            HashMap<Integer, ItemStack> toDrop = receiver.getInventory().addItem(item);
            if(toDrop.size() > 0) {
                toDrop.forEach((number, lostItem) -> {
                    receiver.getWorld().dropItem(receiver.getLocation(), lostItem);
                });
            }
        });
        receiver.playSound(receiver.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.7f, 2);
    }


}

