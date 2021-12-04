package me.einjojo.survivalengine.command;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FixCommand implements CommandExecutor {

    public FixCommand(SurvivalEngine plugin) {
        plugin.getCommand("fix").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if(itemStack.getItemMeta() == null) {
            player.sendMessage("§cDas ist kein Teleport Kristall.");
            return true;
        }

        if(!(itemStack.getItemMeta().getDisplayName().equals("§dTeleport Kristall"))) {
            player.sendMessage("§cDas ist kein Teleport Kristall.");
            return true;
        }

        ItemStack COPIED = new ItemStack(TeleportCrystalRecipe.getItemStack());
        COPIED.setAmount(itemStack.getAmount());

        itemStack.setAmount(0);
        player.getInventory().addItem(COPIED);
        player.sendMessage("§aTeleport Kristall repariert.");


        return true;
    }
}
