package me.einjojo.survivalengine.command;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DifficultyCommand implements CommandExecutor {

    private final SurvivalEngine plugin;
    private final HashMap<Player, Long> cooldown;

    public DifficultyCommand(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.cooldown = new HashMap<>();
        plugin.getCommand("difficulty").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            return true;
        }
        final Player p = (Player) sender;

        if(hasCooldown(p)){
            sendCooldownMessage(p);
            return true;
        }

        if(args.length != 2) {
            sendUsageMessage(p);
            return true;
        }


        Difficulty difficulty;
        switch (args[0]) {
            case "easy":
            case "ez":
            case "1":
            case "noob":
            case "e":
                difficulty = Difficulty.EASY;
                break;
            case "normal":
            case "2":
            case "n":
                difficulty = Difficulty.NORMAL;
                break;
            case "hard":
            case "3":
            case "h":
            case "asian":
                difficulty = Difficulty.HARD;
                break;
            default:
                sendUsageMessage(p);
                return true;
        }

        long time;
        try {
             time = Long.parseLong(args[1]); //in minutes
        } catch (NumberFormatException e) {
            sendUsageMessage(p);
            return true;
        }

        if(time > 60) {
            sendUsageMessage(p);
        }

        setDifficulty(p, difficulty, time);


        return true;
    }

    private void sendUsageMessage(Player player) {
        player.sendMessage(plugin.getPREFIX() + "Nutze: §e/difficulty <Schwierigkeit> <Zeit in Minuten>");
        player.sendMessage(plugin.getPREFIX() + "Die maximale Zeit beträgt §ceine Stunde");
    }
    private void sendCooldownMessage(Player player) {
        player.sendMessage(plugin.getPREFIX() + "Oh nein! Ein Cooldown...");
        long time = cooldown.get(player);

        long millis = time % 1000;
        long second = (time / 1000) % 60;
        long minute = (time / (1000 * 60)) % 60;
        long hour = (time / (1000 * 60 * 60)) % 24;

        String remaining = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);

        player.sendMessage(plugin.getPREFIX() + "Verbleibende Zeit: §c" + remaining);
    }

    private void setDifficulty(Player player, Difficulty difficulty, long time) {
        World world = player.getWorld();
        world.setDifficulty(difficulty);
        setCooldown(player, time);

        Bukkit.broadcastMessage(plugin.getPREFIX() + "Die Schwierigkeit wurde von §e " + player.getName() + " §7auf §c" + difficulty.name() + " §7gesetzt");

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            world.setDifficulty(Difficulty.NORMAL);
            Bukkit.broadcastMessage(plugin.getPREFIX() + "Die Schwierigkeit wurde auf §cNormal §7zurückgesetzt.");
        }, time * 60 * 20);
    }


    private void setCooldown(Player player, long minutes) {
        long time = minutes * 2 * 60000 + System.currentTimeMillis();
        cooldown.put(player, time);
    }

    private boolean hasCooldown(Player player) {
        if(cooldown.containsKey(player)) {
            if(cooldown.get(player) < System.currentTimeMillis()) {
                cooldown.remove(player);
                return false;
            }
            return true;
        }
        return false;
    }
}
