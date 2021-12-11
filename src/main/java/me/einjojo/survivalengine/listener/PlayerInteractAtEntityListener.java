package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.entity.TransportChicken;
import me.einjojo.survivalengine.inventory.TeleporterMainInventory;
import me.einjojo.survivalengine.manager.TransporterManager;
import me.einjojo.survivalengine.manager.TeleportManager;
import me.einjojo.survivalengine.object.Teleporter;
import me.einjojo.survivalengine.recipe.ChickenControllerRecipe;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.util.TeleportCrystalUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractAtEntityListener implements Listener {

    private final SurvivalEngine plugin;
    private final TeleportManager teleportManager;
    private final TransporterManager transporterManager;
    public PlayerInteractAtEntityListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.teleportManager = plugin.getTeleportManager();
        this.transporterManager = plugin.getTransportManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void openTeleporterGUI(PlayerInteractAtEntityEvent e) {
        if(e.getHand().equals(EquipmentSlot.HAND)) {
            if(isInteractingWithTeleporter(e)) {
                e.setCancelled(true);
                Entity entity = e.getRightClicked();
                Player player = e.getPlayer();
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                String teleporterName = entity.getCustomName();
                Teleporter teleporter = plugin.getTeleportManager().getTeleporter(teleporterName);
                if(itemStack.isSimilar(TeleportCrystalRecipe.getItemStack())) return;
                if(teleporter == null) {
                    teleportManager.malfunctionTeleporter(player, entity);
                    return;
                }

                entity.setInvulnerable(!teleporter.isActivated());
                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 0.5F, 1);

                try {
                    new TeleporterMainInventory().openInventory(player, teleporter);
                } catch (NullPointerException ex) {
                    teleportManager.malfunctionTeleporter(player, teleporter);
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void bindCrystalToTeleporter(PlayerInteractAtEntityEvent e)  {
        if(e.getHand().equals(EquipmentSlot.HAND)) {
            if(isInteractingWithTeleporter(e)) {
                e.setCancelled(true);
                Player player = e.getPlayer();
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                Entity teleporterEntity = e.getRightClicked();
                if(!itemStack.isSimilar(TeleportCrystalRecipe.getItemStack())) {
                    e.setCancelled(false);
                    return;
                }

                Teleporter teleporter = plugin.getTeleportManager().getTeleporter(teleporterEntity.getName());

                if(teleporter == null) {
                    teleportManager.malfunctionTeleporter(player, teleporterEntity);
                    return;
                }

                TeleportCrystalUtil crystalUtil = new TeleportCrystalUtil();
                ItemStack boundCrystal = crystalUtil.bind(itemStack, teleporter);

                player.sendMessage(plugin.getPREFIX() + "Der Kristall wurde an §c" + teleporter.getName() + " §7gebunden.");
                player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
            }
        }
    }

    @EventHandler
    public void linkTeleporter(PlayerInteractAtEntityEvent e) {
        if(e.getHand().equals(EquipmentSlot.HAND)) {
            if(!isInteractingWithTeleporter(e)) {
                return;
            }
            Player player = e.getPlayer();
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            Entity teleporterEntity = e.getRightClicked();

            if(itemStack.getItemMeta() == null) {
                return;
            }
            if(!itemStack.getItemMeta().getDisplayName().equals("§5Teleport Splitter")) {
                return;
            }
            e.setCancelled(true);

            //Get Teleporter
            Teleporter teleporter = teleportManager.getTeleporter(teleporterEntity.getName());
            if(teleporter == null) {
                teleportManager.malfunctionTeleporter(player, teleporterEntity);
                return;
            }
            String shardName = new TeleportCrystalUtil().getTeleporterNameFromShard(itemStack);
            Teleporter linkedTeleporter = teleportManager.getTeleporter(shardName);
            if( linkedTeleporter == null) {
                player.sendMessage("§cDer Teleporter existiert nicht mehr.");
                return;
            }

            try {
                teleporter.link(linkedTeleporter);
            } catch (Exception ex) {
                player.sendMessage(plugin.getPREFIX() + ex.getMessage());
                return;
            }
            player.sendMessage(plugin.getPREFIX() + "§aErfolgreich Verbunden mit " + shardName);
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
    }

    @EventHandler
    private void onInteractAtTransportChicken(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();

        TransportChicken transportChicken = transporterManager.getTransportChicken(entity);

        if(transportChicken == null) {
            return;
        }

        player.openInventory(transportChicken.getInventory());

    }

    private boolean isInteractingWithTeleporter(PlayerInteractAtEntityEvent e) {
        Entity entity = e.getRightClicked();
        Player player = e.getPlayer();
        if(entity.getType() != EntityType.ENDER_CRYSTAL) return false;
        if(entity.getCustomName() == null) return false;
        if(entity.getCustomName().equals("")) return false;
        if (entity.isCustomNameVisible()) {
            if(!plugin.getTeleportManager().getINTERACT_BLACKLIST().contains(player)){
                plugin.getTeleportManager().getINTERACT_BLACKLIST().add(e.getPlayer());
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                    plugin.getTeleportManager().getINTERACT_BLACKLIST().remove(e.getPlayer());
                },10L);
            }
            return true;
        } else {
            return false;
        }
    }

    @EventHandler
    public void useChickenController(PlayerInteractAtEntityEvent e) {
        Entity entity = e.getRightClicked();
        Player player = e.getPlayer();
        ItemStack itemStack = e.getPlayer().getInventory().getItemInMainHand();

        if(entity.getType() != EntityType.CHICKEN) {
            return;
        }

        if(!itemStack.isSimilar(ChickenControllerRecipe.getItemStack())) {
            return;
        }

        if((entity.isCustomNameVisible()) && entity.getCustomName().contains("Transporter")) {
            return;
        }

        TransportChicken transportChicken = transporterManager.createTransportChicken(player.getLocation(),player.getUniqueId());
        entity.getWorld().strikeLightningEffect(entity.getLocation());
        entity.remove();
        transportChicken.setSpawned(true);
        transportChicken.spawn(player);

        itemStack.setAmount(0);
    }


}
