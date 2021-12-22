package me.einjojo.survivalengine.enchantment;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.EnchantmentManager;
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

        if(itemStack.getItemMeta() == null) {
            return;
        }
        if(!itemStack.getItemMeta().hasEnchant(this)){
            return;
        }

        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if(player.getInventory().firstEmpty() == -1) {
            return;
        }
        if(block.getState() instanceof Container) {
            return;
        }
        e.setDropItems(false);

        Collection<ItemStack> drops = e.getBlock().getDrops(itemStack, player);
        if(drops.size() == 0) {
            return;
        }
        player.getInventory().addItem(drops.iterator().next());
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.7f, 2);
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {
        Player player = e.getEntity().getKiller();

        if(player == null) {
            return;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if(itemStack.getItemMeta() == null) {
            return;
        }
        if(!itemStack.getItemMeta().hasEnchant(this)){
            return;
        }

        if(player.getInventory().firstEmpty() == -1) {
            return;
        }

        List<ItemStack> drops = e.getDrops();
        if(drops.size() == 0) {
            return;
        }
        player.getInventory().addItem(drops.iterator().next());
        drops.clear();
        player.setTotalExperience(player.getTotalExperience() + e.getDroppedExp());
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.7f, 2);
    }



}

