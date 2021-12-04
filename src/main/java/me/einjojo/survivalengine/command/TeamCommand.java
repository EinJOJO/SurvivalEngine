package me.einjojo.survivalengine.command;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.PlayerManager;
import me.einjojo.survivalengine.manager.TeamManager;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import me.einjojo.survivalengine.object.Team;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import me.einjojo.survivalengine.util.PlayerChatInput;
import me.einjojo.survivalengine.util.TextUtil;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamCommand implements CommandExecutor {

    private final SurvivalEngine plugin;
    private final PlayerManager playerManager;
    private final TeamManager teamManager;
    private final List<Team> DELETE_TEAM;

    public TeamCommand(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.teamManager = plugin.getTeamManager();
        this.DELETE_TEAM = new ArrayList<>();
        plugin.getCommand("team").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;

        if(args.length < 1) {
            sendUsage(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "c":
            case "chat":
                chat(player, args);
                break;
            case "leave":
                leaveTeam(player);
                break;
            case "create":
                if(args.length == 2) {
                    createTeam(player, args[1]);
                } else {
                    createTeamDialog(player);
                }
                break;
            case "delete":
                deleteTeam(player);
                break;
            case "setbase":
                setBase(player);
                break;
            case "info":
                sendInfo(player, args);
                break;
            case "invite":
                if(!(args.length >= 2)) {
                    sendUsage(player);
                    return true;
                }
                invitePlayer(player, args[1]);
                break;
            case "kick":
                if(!(args.length >= 2)) {
                    sendUsage(player);
                    return true;
                }
                kickPlayer(player, args[1]);
                break;
            case "join":
                if(args.length < 2) {
                    sendUsage(player);
                    return true;
                }
                joinTeam(player, args[1]);
                break;
            default:
                sendUsage(player);
                break;
        }
        return true;
    }

    private void chat(Player player, String[] args) {

        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);
        Team team = survivalPlayer.getTeam();

        if(team == null) {
            player.sendMessage(plugin.getPREFIX() + "§cDu bist in keinem Team.");
            return;
        }

        if(args.length == 1) {
            survivalPlayer.setTeamChat(!survivalPlayer.isTeamChat());
            player.sendMessage(plugin.getPREFIX() + "Der Chat wurde auf den " + (survivalPlayer.isTeamChat() ? "§cTeamchat" : "§eGlobalenchat") + " §7umgestellt.");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String snippet : args) {
            stringBuilder.append(snippet);
        }

        String message = TextUtil.toTeamChat(player.getName(), stringBuilder.toString());

        team.chat(message);
    }


    private void sendInfo(Player player, String[] args) {
        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);
        Team team = survivalPlayer.getTeam();
        if(team == null) {
            player.sendMessage(plugin.getPREFIX() + "§cDu bist in keinem Team.");
            return;
        }

        if(args.length >= 2) {
            if(args[1].toLowerCase().equals("members")) {
                TextComponent textComponent = new TextComponent(String.format("%s§f%s §7» §bMitglieder §7(§3%d§7) \n", plugin.getPREFIX(), team.getName(), team.getMembers().size()));
                for (UUID memberUUID : team.getMembers()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(memberUUID);
                    TextComponent userComponent = new TextComponent(String.format(" §8- §b%s §7(%s§7)\n", offlinePlayer.getName(), ((offlinePlayer.isOnline()) ? "§aOnline": "§cOffline")));
                    if(offlinePlayer.getPlayer() != null) {
                        Player oPlayer = offlinePlayer.getPlayer();
                        Location loc = oPlayer.getLocation();

                        userComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format("§eHP: §f%d\n §eLevel: §f%d\n §ePosition: §f%d %d %d", (int) oPlayer.getHealth(), oPlayer.getLevel(), (int) loc.getX(), (int) loc.getY(), (int) loc.getZ()))));
                    }
                    textComponent.addExtra(userComponent);
                }
                player.spigot().sendMessage(textComponent);
                return;
            }
        }

        TextComponent line1 = new TextComponent("-------------------------------\n");
        TextComponent line2 = new TextComponent("§bTeam Info §7» §f " + team.getName() + "\n");
        TextComponent line3 = new TextComponent("§7\n");
        TextComponent line4 = new TextComponent("§7Owner: §b"+ Bukkit.getOfflinePlayer(team.getOwner()).getName() +"\n");
        TextComponent line5 = new TextComponent("§7Mitglieder: §7(§3" + team.getMembers().size() + "§7)\n");
        TextComponent line6 = new TextComponent("§7Base: ");
        TextComponent line7 = new TextComponent("§7\n");
        TextComponent line8 = new TextComponent("§7Identifikationsnummer: \n");
        TextComponent line9 = new TextComponent("§b" +team.getId().toString() + "\n");
        TextComponent line10 = new TextComponent("-------------------------------\n");

        line5.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7Klicke für mehr Infos...")));
        line5.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team info members"));
        if(team.getBaseLocation() == null) {
            line6.addExtra("§cNicht gesetzt \n");
        } else {
            Location location = team.getBaseLocation();
            line6.addExtra(String.format("§b%d %d %d \n", (int) location.getX(), (int) location.getX(), (int) location.getY() ));
        }
        line9.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7Kopieren")));
        line9.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, team.getId().toString()));

        TextComponent text = TextUtil.combineTextComponents(line1,line2,line3,line4,line5,line6,line7,line8,line9,line10);

        player.spigot().sendMessage(text);

    }

    private void joinTeam(Player player, String arg) {
        UUID teamID;
        try {
             teamID = UUID.fromString(arg);
        } catch (IllegalArgumentException e) {
            player.sendMessage(plugin.getPREFIX() + "§cUngültige Team-Identifikationsnummer");
            return;
        }
        Team team = teamManager.getTeam(teamID);

        if(team == null) {
            player.sendMessage(plugin.getPREFIX() + "§cDas Team existiert nicht");
            return;
        }

        if(!team.isInvited(player.getUniqueId())) {
            player.sendMessage(plugin.getPREFIX() + "§cDu bist nicht eingeladen");
            return;
        }

        team.getInvites().remove(player.getUniqueId());
        teamManager.addPlayerToTeam(player, team);

        player.sendMessage(plugin.getPREFIX() + String.format("Du bist §b%s §7beigreten", team.getName()));
        for (UUID teamMemberUUID : team.getMembers()) {
            Player teamPlayer = Bukkit.getPlayer(teamMemberUUID);
            if(teamPlayer != null) {
                teamPlayer.sendMessage(plugin.getPREFIX() + String.format("§b%s ist dem Team beigetreten", player.getName()));
            };
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

        if(targetPlayer.getUuid().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getPREFIX() + "§cDu kannst dich nicht selbst kicken.");
            return;
        }

        targetPlayer.leaveTeam();
        if(targetPlayer.getPlayer() != null) {
            targetPlayer.getPlayer().sendMessage(plugin.getPREFIX() + "§cDu wurdest aus dem Team gekickt.");
        }

        player.sendMessage(plugin.getPREFIX() + String.format("Du hast §b%s §7aus deinem Team gekickt.", targetPlayer.getName()));


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

        team.invite(targetPlayer.getUuid());
        player.sendMessage(plugin.getPREFIX() + "§b" + target + "§7 wurde eingeladen.");

        TextComponent prefix = new TextComponent(plugin.getPREFIX());
        TextComponent accept = new TextComponent("§7[§aKlick mich§7]");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team join " + team.getId().toString()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Trete §b" + team.getName() + "§7 bei").create()));
        Player targetP = targetPlayer.getPlayer();
        targetP.sendMessage(plugin.getPREFIX() + "Du wurdest in ein Team eingeladen!");
        targetP.sendMessage(plugin.getPREFIX() + String.format("Team §b%s von §b%s",team.getName(), survivalPlayer.getName()));

        prefix.addExtra(accept);
        targetP.spigot().sendMessage(prefix);
    }

    private void sendUsage(Player player) {
        TextComponent line1 = new TextComponent("§7§m----------------------------------------------------\n");
        TextComponent line2 = new TextComponent("§bTeam §8» §3Hilfestellung\n");
        TextComponent line3 = new TextComponent("§7Du kannst auf die Befehle klicken.\n");
        TextComponent line4 = new TextComponent("§7 \n");
        TextComponent line5 = createUsageComponent(player, "/team info", " §8» §7Zeige Informationen über dein Team an", "/team info", "§b/team info");
        TextComponent line6 = createUsageComponent(player, "/team create", " §8» §7Erstelle dein eigenes Team", "/team create", "§b/team create");
        TextComponent line7 = createUsageComponent(player, "/team join <ID>", " §8» §7Trete einem Team bei", "/team join", "bsp: §b/team join 0a00f59b-9f84-4dfe-8163-d71a3e7bd90a");
        TextComponent line8 = createUsageComponent(player, "/team invite <Spieler>", " §8» §7Lade einen Spieler ein", "/team invite ", "z.B §b/team invite Ein_Jojo");
        TextComponent line9 = createUsageComponent(player, "/team setbase", " §8» §7Setze die Base auf deine Position", "/team setbase", "§b/team setbase");
        TextComponent line10 = createUsageComponent(player, "/team kick <Spieler>", " §8» §7Schmeiße jemanden aus deinem Team raus", "/team kick ", "z.B §b/team kick Ein_Jojo");
        TextComponent line10_1 = createUsageComponent(player, "/team delete", " §8» §7Lösche dein jetziges Team", "/team delete", "§b/team delete");
        TextComponent line11 = createUsageComponent(player, "/team leave", " §8» §7Verlasse dein jetziges Team", "/team leave", "§b/team leave");
        TextComponent line12 = createUsageComponent(player, "/team chat", " §8» §7Schalte den Chat auf den Teamchat um", "/team chat", "/team chat");
        TextComponent line13 = createUsageComponent(player, "/team chat <Nachricht>", "§8» §7Schreibe im Teamchat", "/team chat ", "z.B /team chat Bla bla bla");
        TextComponent line14 = new TextComponent("§7§m----------------------------------------------------");

        TextComponent text = TextUtil.combineTextComponents(line1,line2,line3,line4,line5,line6,line7,line8,line9,line10,line10_1,line11,line12);

        player.spigot().sendMessage(text);
    }

    private TextComponent createUsageComponent(Player player, String command, String description, String suggest, String hover) {
        return TextUtil.createUsageComponent(player,command,description,suggest,hover);
    }

    private void setBase(Player player) {
        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);
        Team team = survivalPlayer.getTeam();

        if(team == null) {
            player.sendMessage(plugin.getPREFIX() + "§cDu bist in keinem Team.");
            return;
        }

        Location location = player.getLocation();
        team.setBaseLocation(location);
        team.getMembers().forEach((memberUUID) -> {
            Player player1 = Bukkit.getPlayer(memberUUID);
            if(player1 != null) {
                player1.sendMessage(plugin.getPREFIX() + String.format("Die Base Location auf §b%d %d %d §7gesetzt", (int) location.getX(), (int) location.getY(), (int) location.getZ()));
            }
        });
    }

    private void deleteTeam(Player player) {
        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);
        Team team = survivalPlayer.getTeam();

        if (team == null) {
            player.sendMessage(plugin.getPREFIX() + "§cDu bist ein keinem Team.");
            return;
        }

        if(!team.isOwner(player)) {
            player.sendMessage(plugin.getPREFIX() + "§cNur der Inhaber kann Löschen.");
            return;
        }

        if(DELETE_TEAM.contains(team)) {
            DELETE_TEAM.remove(team);
            team.getMembers().forEach((uuid)->{
                Player member = Bukkit.getPlayer(uuid);
                if(member != null) {
                    member.sendMessage(plugin.getPREFIX() + "Das Team §b" + team.getName() + " §7wurde gelöscht");
                }
            });

            teamManager.deleteTeam(team);
        } else {
            DELETE_TEAM.add(team);

            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, ()->{
                DELETE_TEAM.remove(team);
            },20 * 30);

            TextComponent line1 = new TextComponent(plugin.getPREFIX() + "Bestätige die Löschung des Teams \n");
            TextComponent line2_1 = new TextComponent(plugin.getPREFIX());
            TextComponent line2_2 = new TextComponent("§a[Bestätigen]");
            line2_2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§b/team delete")));
            line2_2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team delete"));
            line2_1.addExtra(line2_2);
            line1.addExtra(line2_1);
            player.spigot().sendMessage(line1);
        }


    }

    private void leaveTeam(Player player) {
        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);
        Team playerTeam = survivalPlayer.getTeam();
        if(playerTeam == null) {
            player.sendMessage(plugin.getPREFIX() + "§cDu bist in keinem Team.");
            return;
        };

        if(playerTeam.isOwner(player.getUniqueId())) {
            player.sendMessage(plugin.getPREFIX() + "§cDu kannst dein eigenes Team nicht verlassen");
            return;
        }

        teamManager.removePlayer(player.getUniqueId());
        playerTeam.getMembers().forEach((member)->{
            Player p = Bukkit.getPlayer(member);
            if(p != null) {
                p.sendMessage(plugin.getPREFIX() + "§b" + player.getName() + "§7 hat das Team verlassen.");
            }
        });
        player.sendMessage(plugin.getPREFIX() + "Du hast das Team §b" + playerTeam.getName() + "§7 verlassen.");
    }

    private void createTeamDialog(Player player) {
        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);
        Team team = survivalPlayer.getTeam();
        if(team == null) {
            new PlayerChatInput(plugin, player, "§7Wähle einen Namen", (input) -> {
                createTeam(player, input);
            });
        } else {
            player.sendMessage(plugin.getPREFIX() + "§cDu bist bereits in einem Team.");
        }
    }

    private void createTeam(Player player, String input) {
        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);
        Team team = survivalPlayer.getTeam();

        if(team != null) {
            player.sendMessage(plugin.getPREFIX() + "§cDu bist bereits in einem Team.");
            return;
        }

        if(input == null) {
            return;
        }
        if(input.length() < 4) {
            player.sendMessage(plugin.getPREFIX() + "Der Name darf nicht kürzer als 4 Zeichen sein.");
            return;
        }
        if(input.length() > 14) {
            player.sendMessage(plugin.getPREFIX() + "Der Name darf nicht länger als 14 Zeichen sein.");
            return;
        }
        Team newTeam = new Team(input, player.getUniqueId());
        if(teamManager.createTeam(newTeam)) {
            player.sendMessage(plugin.getPREFIX() + String.format("Das Team §b%s §7wurde erstellt!", input));

            if(!survivalPlayer.hasReward("first_team")) {
                player.sendMessage(plugin.getPREFIX()+ "§6[BELOHNUNG FREIGESCHALTET]");
                player.sendMessage(plugin.getPREFIX() + "Du erhälst 1x §5TELEPORTER §7und 4x §dTeleport Kristall");
                player.getInventory().addItem(TeleporterRecipe.getItemStack());
                ItemStack itemStack = new ItemStack(TeleportCrystalRecipe.getItemStack());
                itemStack.setAmount(4);
                player.getInventory().addItem(itemStack);
                survivalPlayer.claimReward("first_team");
            }
        } else {
            player.sendMessage(plugin.getPREFIX() + "§cEin Fehler ist aufgetreten. Versuche es erneut.");
        };
    }
}

