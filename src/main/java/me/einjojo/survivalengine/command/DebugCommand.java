package me.einjojo.survivalengine.command;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.entity.RamenSeller;
import me.einjojo.survivalengine.entity.TransportChicken;
import me.einjojo.survivalengine.manager.EnchantmentManager;
import me.einjojo.survivalengine.manager.TransporterManager;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class DebugCommand implements CommandExecutor {

    private final SurvivalEngine plugin;

    public DebugCommand(SurvivalEngine plugin) {
        plugin.getCommand("debug").setExecutor(this); this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            return true;
        }
        final Player p = (Player) sender;


        if(!p.hasPermission("survivalengine.debug")) {
            p.sendMessage("No perms.");
            return true;
        }

        if(args.length >= 1) {
            switch (args[0]) {
                case "teleporter":
                    p.getInventory().addItem(TeleporterRecipe.getItemStack());
                    break;
                case "crystal":
                    p.getInventory().addItem(TeleportCrystalRecipe.getItemStack());
                    break;
                case "chicken":

                    TransporterManager transporterManager = SurvivalEngine.getInstance().getTransportManager();
                    TransportChicken chicken = transporterManager.getTransportChicken(p.getUniqueId());
                    if(chicken == null) {
                        chicken = transporterManager.createTransportChicken(p.getLocation(), p.getUniqueId());
                    }

                    chicken.spawn(p);

                    p.sendMessage("spawned.");
                    break;
                case "save":
                    plugin.getTeamManager().save();
                    p.sendMessage("saved.");
                    break;
                case "slime":
                    Slime slime = (Slime) p.getWorld().spawnEntity(p.getLocation(), EntityType.SLIME);
                    slime.setSize(8);
                    slime.setCanPickupItems(true);
                    break;
                case "villager":
                    new RamenSeller(p);
                    break;
                case "enchant":
                    if(args.length != 2) {
                        p.sendMessage("enchantment missing");
                        return true;
                    }

                    ItemStack itemStack = p.getInventory().getItemInMainHand();
                    if(itemStack.getType() == Material.AIR) {
                        p.sendMessage("Item Missing");
                        return true;
                    }

                    switch (args[1]) {
                        case "telepathy":
                            plugin.getEnchantmentManager().enchant(itemStack, EnchantmentManager.Enchantments.TELEPATHY.getEnchantment());
                            break;
                        default:
                            p.sendMessage("Invalid enchantment");
                            break;
                    }

                    break;
                case "spawn":
                    if(args.length != 2) {
                        p.sendMessage("player missing-");
                        return true;
                    }
                    SurvivalPlayer survivalPlayer = plugin.getPlayerManager().getPlayer(args[1]);
                    if(survivalPlayer != null) {
                        survivalPlayer.setResetSpawn(!survivalPlayer.isResetSpawn());
                        p.sendMessage(String.valueOf(survivalPlayer.isResetSpawn()));
                    } else {
                        p.sendMessage("not exist");
                    }

                    break;
                case "snow":

                    ProtocolManager protocolManager = plugin.getProtocolManager();

                    if(protocolManager == null) {
                        Bukkit.broadcastMessage("null!");
                        return true;
                    }

                    Random random = new Random();
                    int radius = 8;
                    (new BukkitRunnable() {
                        @Override
                        public void run() {
                            float xAdditive = (random.nextFloat() - 0.5F) * radius * 2.0F;
                            float max = (float)Math.sqrt((radius * radius - xAdditive * xAdditive)) * 2.0F;
                            float yAdditive = (random.nextFloat() - 0.5F) * max;
                            float zAdditive = (random.nextFloat() - 0.5F) * max;
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                Location playerLoc = p.getLocation();
                                Location loc = new Location(player.getWorld(), playerLoc.getX() + xAdditive, playerLoc.getY() + yAdditive, playerLoc.getZ() + zAdditive);
                                if (loc.getWorld().getHighestBlockYAt(loc) < loc.getY()) {
                                    PacketContainer packetContainer = protocolManager.createPacket(PacketType.Play.Server.WORLD_PARTICLES);
                                    packetContainer.getIntegers().write(0, 25);
                                    try {
                                        protocolManager.sendServerPacket(p, packetContainer);
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }).runTaskTimer(plugin, 0L, 2L);
                    break;
            }
        }

        return true;
    }
}
