package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements Listener {

    public PingListener(SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void sendMOTDonPing(ServerListPingEvent e) {
        e.setMotd("§8» §6§lEinJOJO§7.§e§lIT   §aSurvival Server §8» §7[§c1.18-1.18.1§7]\n" +
                "     §8➥ §f§lSeit dem 25.12 §c§l20GB Ram");

    }

}
