package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.events.TeleporterPlaceEvent;
import me.einjojo.survivalengine.manager.TeleportManager;
import me.einjojo.survivalengine.object.Teleporter;
import me.einjojo.survivalengine.recipe.BedrockPickaxeRecipe;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import me.einjojo.survivalengine.util.TeleportCrystalUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
public class PlayerInteractListener implements Listener {

    private final SurvivalEngine plugin;
    private final TeleportManager teleportManager;
    private int pickaxeTaskID;

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
        if(e.getHand() == null) return;
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

        if(e.getHand() == null) return;
        if(!e.getHand().equals(EquipmentSlot.HAND)) return;
        if(!e.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
        if(teleportManager.getINTERACT_BLACKLIST().contains(player)) return;
        if(itemStack.getItemMeta() == null) return;
        if(!itemStack.getItemMeta().getDisplayName().equals(TeleportCrystalRecipe.getItemStack().getItemMeta().getDisplayName())) return;

        TeleportCrystalUtil utility = new TeleportCrystalUtil();
        String teleporterName = utility.getTeleporterNameFromCrystal(itemStack);

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

        itemStack.setAmount(itemStack.getAmount() - 1);
        teleportManager.teleport(player, teleporter);
    }

    @EventHandler
    public void onBedrockPickaxeUse(PlayerInteractEvent e) {
        if(e.getHand() == null) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        if(e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if(e.getItem() == null) return;
        if(e.getItem().getItemMeta() == null) return;
        if(!e.getItem().isSimilar(BedrockPickaxeRecipe.getItemStack())) return;
        if(e.getClickedBlock() == null) return;
        Player player = e.getPlayer();
        Block clickedBlock = e.getClickedBlock();
        Location location = clickedBlock.getLocation();
        e.getItem().setAmount(0);

        player.sendTitle("§cLauf!", "§7Gleich macht es §cBoom§7...", 5, 20, 20);


        pickaxeTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int counter = 5;
            @Override
            public void run() {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1 ,1);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c" + counter));
                if(counter == 0) {
                    Bukkit.getScheduler().cancelTask(pickaxeTaskID);
                    clickedBlock.setType(Material.AIR);
                    location.getWorld().createExplosion(location, 7, false, true);
                    return;
                }
                counter--;
            }
        },0, 20);
    }

}
