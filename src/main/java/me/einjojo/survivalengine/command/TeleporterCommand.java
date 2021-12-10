package me.einjojo.survivalengine.command;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.PlayerManager;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import me.einjojo.survivalengine.object.Teleporter;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


public class TeleporterCommand implements CommandExecutor {

    private final PlayerManager playerManager;
    private final String PREFIX;

    public TeleporterCommand(SurvivalEngine plugin) {
        plugin.getCommand("teleporter").setExecutor(this);
        this.PREFIX = plugin.getPREFIX();
        this.playerManager = plugin.getPlayerManager();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;
        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);

        List<Teleporter> privateTeleporterList = survivalPlayer.getTeleporter();
        TextComponent textComponent = new TextComponent(PREFIX + "§7Deine Teleporter (§c"+ privateTeleporterList.size() +"§7): \n");
        privateTeleporterList.forEach((teleporter -> {
            textComponent.addExtra(teleporterComponent(teleporter));
        }));

        if(survivalPlayer.getTeam() != null) {
            List<Teleporter> teamTeleporterList = survivalPlayer.getTeam().getTeleporter();
            TextComponent textComponent2 = new TextComponent(PREFIX + "§7Team Teleporter (§c"+ teamTeleporterList.size() +"§7): \n");
            teamTeleporterList.forEach((teleporter -> {
                textComponent2.addExtra(teleporterComponent(teleporter));
            }));
            textComponent.addExtra(textComponent2);
        }
        player.spigot().sendMessage(textComponent);

        return true;
    }

    private TextComponent teleporterComponent(Teleporter teleporter) {
        TextComponent teleportComponent = new TextComponent("§8- §c"+ teleporter.getName() + "\n");
        teleportComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format("§7Position: §c%d %d %d", (int) teleporter.getLocation().getX(),(int) teleporter.getLocation().getY(),(int) teleporter.getLocation().getZ()))));
        return teleportComponent;
    }
}
