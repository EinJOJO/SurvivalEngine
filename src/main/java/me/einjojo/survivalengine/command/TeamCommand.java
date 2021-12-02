package me.einjojo.survivalengine.command;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.PlayerManager;
import me.einjojo.survivalengine.manager.TeamManager;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import me.einjojo.survivalengine.object.Team;
import me.einjojo.survivalengine.util.PlayerChatInput;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamCommand implements CommandExecutor {

    private final SurvivalEngine plugin;
    private final PlayerManager playerManager;
    private final TeamManager teamManager;

    public TeamCommand(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.teamManager = plugin.getTeamManager();
        plugin.getCommand("team").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;

        switch (args[0].toLowerCase()) {
            case "create":
                createTeam(player);
                break;
            case "delete":
                deleteTeam(player);
                break;
            case "setbase":
                setBase(player);
                break;
            case "info":
                sendInfo(player);
                break;
            case "invite":
                if(!(args.length >= 2)) return true;
                invitePlayer(player, args[1]);
                break;
            case "kick":
                if(!(args.length >= 2)) return true;
                kickPlayer(player, args[1]);
                break;
            case "join":
                if(args.length >= 2) return true;
                break;
            default:
                sendUsage(player);
                break;
        }
        return true;
    }


    private void sendInfo(Player player) {
        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);

        if(survivalPlayer.getTeam() == null) {
            player.sendMessage(plugin.getPREFIX() + "§cDu bist in keinem Team.");
        }
    }

    private void kickPlayer(Player player, String target) {
        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);
        SurvivalPlayer targetPlayer = playerManager.getPlayer(target);
        Team team = survivalPlayer.getTeam();

        if(team == null) {
            player.sendMessage(plugin.getPREFIX() + "§cDu bist in keinem Team.");
            return;
        }

        if(!team.isOwner(player.getUniqueId())) {
            player.sendMessage(plugin.getPREFIX() + "§cNur der Inhaber kann Kicken.");
            return;
        }

        if(targetPlayer == null) {
            player.sendMessage(plugin.getPREFIX() + "§c"+ target + " existiert nicht.");
            return;
        }

        if((targetPlayer.getTeam() == null) || (!targetPlayer.getTeam().equals(survivalPlayer.getTeam()))) {
            player.sendMessage(plugin.getPREFIX() + "§cDieser Spieler ist nicht in deinem Team.");
            return;
        }

        targetPlayer.leaveTeam();
        if(targetPlayer.getPlayer() != null) {
            targetPlayer.getPlayer().sendMessage(plugin.getPREFIX() + "§cDu wurdest aus dem Team gekickt.");
        }

        player.sendMessage(plugin.getPREFIX() + String.format("Du hast §b%s §7aus deinem Team gekickt."));


        //TODO: Kick Event

    }

    private void invitePlayer(Player player, String target) {
        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);
        SurvivalPlayer targetPlayer = playerManager.getPlayer(target);
        Team team = survivalPlayer.getTeam();

        if(team == null) {
            player.sendMessage(plugin.getPREFIX() + "§cDu bist in keinem Team.");
            return;
        }

        if(!team.isOwner(player.getUniqueId())) {
            player.sendMessage(plugin.getPREFIX() + "§cNur der Inhaber kann Einladen.");
            return;
        }

        if(targetPlayer == null) {
            player.sendMessage(plugin.getPREFIX() + "§c"+ target + " existiert nicht.");
            return;
        }

        if((targetPlayer.getTeam() != null)) {
            player.sendMessage(plugin.getPREFIX() + "§cDieser Spieler ist bereits in einem Team");
            return;
        }

        if(targetPlayer.getPlayer() == null) {
            player.sendMessage(plugin.getPREFIX() + "§cDieser Spieler ist nicht online");
            return;
        }
    }

    private void sendUsage(Player player) {

    }

    private void setBase(Player player) {

    }

    private void deleteTeam(Player player) {

    }

    private void createTeam(Player player) {
        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);
        Team team = survivalPlayer.getTeam();
        if(team == null) {
            new PlayerChatInput(plugin, player, "§7Wähle einen Namen", (input) -> {
                if(input == null) {
                    return;
                }
                if(input.length() > 14) {
                    player.sendMessage(plugin.getPREFIX() + "Der Name darf nicht länger als 14 Zeichen sein.");
                    return;
                }
               Team newTeam = new Team(input, player.getUniqueId());
                if(teamManager.createTeam(newTeam)) {
                    player.sendMessage(plugin.getPREFIX() + String.format("Das Team §b%s §7wurde erstellt!", input));
                } else {
                    player.sendMessage(plugin.getPREFIX() + "§cEin Fehler ist aufgetreten. Versuche es erneut.");
                };
            });
        } else {
            player.sendMessage(plugin.getPREFIX() + "§cDu bist bereits in einem Team.");
        }
    }
}

