package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.events.TeleporterPlaceEvent;
import me.einjojo.survivalengine.manager.TeleportManager;
import me.einjojo.survivalengine.object.Teleporter;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import me.einjojo.survivalengine.util.TeleportCrystalUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerInteractListener implements Listener {

    private final SurvivalEngine plugin;
    private final TeleportManager teleportManager;

    public PlayerInteractListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.teleportManager = plugin.getTeleportManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void placeTeleporter(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        ItemStack teleportItem = e.getItem();
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(teleportItem == null) return;
        if(!teleportItem.isSimilar(TeleporterRecipe.getItemStack())) return;
        if(block == null) return;
        e.setCancelled(true); // Don't Place it

        if(e.getBlockFace().equals(BlockFace.UP)) {
            TeleporterPlaceEvent teleporterPlaceEvent = new TeleporterPlaceEvent(player, block, teleportItem);
            Bukkit.getPluginManager().callEvent(teleporterPlaceEvent);
        };
    }

    @EventHandler
    public void useCrystal(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if(!e.getHand().equals(EquipmentSlot.HAND)) return;
        if(!e.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
        if(teleportManager.getINTERACT_BLACKLIST().contains(player)) return;
        if(itemStack == null) return;
        if(!itemStack.getItemMeta().getDisplayName().equals(TeleportCrystalRecipe.getItemStack().getItemMeta().getDisplayName())) return;

        TeleportCrystalUtil utility = new TeleportCrystalUtil();
        String teleporterName = utility.getTeleporterName(itemStack);

        if(teleporterName == null) {
            player.sendMessage(plugin.getPREFIX() + "§cDer Kristall ist an keinen Teleporter gebunden.");
            return;
        }

        Teleporter teleporter = teleportManager.getTeleporter(teleporterName);
        if(teleporter == null) {
            player.sendMessage(plugin.getPREFIX() + "§cDer Teleporter existiert nicht mehr!");
            itemStack.setAmount(itemStack.getAmount() - 1);
            player.getInventory().addItem(TeleportCrystalRecipe.getItemStack());
            return;
        }



        teleportManager.getTELEPORTING_PLAYERS().add(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 255));
        player.teleport(teleporter.getLocation());
        player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5f, 3);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        itemStack.setAmount(itemStack.getAmount() - 1);
        teleporter.setUsedCounter(teleporter.getUsedCounter() + 1);
        teleportManager.getTELEPORTING_PLAYERS().remove(player);
    }

    @EventHandler
    public void onEnderEyeThrow(PlayerInteractEvent e) {
        if(e.getItem() == null) return;
        if(!e.getItem().getType().equals(Material.ENDER_EYE)) return;
        e.setCancelled(true);
    }
}
