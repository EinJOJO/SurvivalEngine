package me.einjojo.survivalengine.command;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.entity.TransportChicken;
import me.einjojo.survivalengine.manager.TransporterManager;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        if(args.length == 1) {
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
            }
        }

        return true;
    }
}
