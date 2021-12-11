package me.einjojo.survivalengine.listener;
import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.PlayerManager;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;


public class AnvilListener implements Listener {

    private final PlayerManager playerManager;

    public AnvilListener (SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void removeLevelCap(PrepareAnvilEvent e) {
        AnvilInventory inventory = e.getInventory();
        Player player = (Player) e.getView().getPlayer();


        inventory.setMaximumRepairCost(Integer.MAX_VALUE);


        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);
        BossBar bossBar = survivalPlayer.getBossBar();
        if(bossBar == null) {
            bossBar = Bukkit.createBossBar(
                    "",
                    BarColor.WHITE,
                    BarStyle.SOLID
                    );
            bossBar.addPlayer(player);
            survivalPlayer.setBossBar(bossBar);
        }
        String textColor;
        double progress = (double) player.getLevel() / (double) inventory.getRepairCost();

        if(Double.isNaN(progress)) {
            progress = 0D;
        }

        if(progress >= 1) {
            progress = 1D;
            bossBar.setColor(BarColor.GREEN);
            textColor = "§a";
        } else {
            bossBar.setColor(BarColor.RED);
            textColor = "§c";
        }
        bossBar.setProgress(progress);
        bossBar.setTitle("§7Kosten: " + textColor + inventory.getRepairCost());
        bossBar.setVisible(true);
    }

    @EventHandler
    public void onCloseAnvil(InventoryCloseEvent e) {
        if(e.getInventory().getType().equals(InventoryType.ANVIL)) {
            playerManager.getPlayer((Player) e.getPlayer()).getBossBar().setVisible(false);

        }
    }
}
