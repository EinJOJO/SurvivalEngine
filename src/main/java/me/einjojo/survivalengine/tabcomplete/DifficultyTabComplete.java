package me.einjojo.survivalengine.tabcomplete;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DifficultyTabComplete implements TabCompleter {

    public DifficultyTabComplete(SurvivalEngine plugin) {
        plugin.getCommand("difficulty").setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();

        if(!(sender instanceof Player)) {
            return arrayList;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            arrayList.add("Schwierigkeit");
            arrayList.add("easy");
            arrayList.add("normal");
            arrayList.add("hard");
            arrayList.add("1");
            arrayList.add("2");
            arrayList.add("3");
            arrayList.add("noob");
            arrayList.add("asian");
            return arrayList;
        } else if (args.length == 2) {
            arrayList.add("Zeit");
            arrayList.add("10");
            arrayList.add("30");
            arrayList.add("60");
        }

        return null;
    }
}
