package me.einjojo.survivalengine.command;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetCommand implements CommandExecutor {

    private final SurvivalEngine plugin;

    public GetCommand(SurvivalEngine plugin) {
        plugin.getCommand("get").setExecutor(this); this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            return true;
        }
        final Player p = (Player) sender;

        if(args.length == 1) {
            switch (args[0]) {
                case "teleporter":
                    p.getInventory().addItem(TeleporterRecipe.getItemStack());
                    break;
                case "crystal":
                    p.getInventory().addItem(TeleportCrystalRecipe.getItemStack());
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
