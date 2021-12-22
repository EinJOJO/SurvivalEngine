package me.einjojo.survivalengine.listener;
import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.PlayerManager;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import me.einjojo.survivalengine.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


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
    public void applyCustomEnchants(PrepareAnvilEvent e) {
        ItemStack tool = e.getInventory().getItem(0);
        ItemStack enchantmentBook = e.getInventory().getItem(1);
        if(tool == null) return;
        if(enchantmentBook == null) return;
        if(enchantmentBook.getType() != Material.ENCHANTED_BOOK) return;
        ItemMeta enchantmentBookItemMeta = enchantmentBook.getItemMeta();
        if(enchantmentBookItemMeta == null) return;
        Map<Enchantment, Integer> enchantmentLevelMap = enchantmentBookItemMeta.getEnchants();
        if(enchantmentLevelMap.size() == 0) return;


        ItemStack result = new ItemStack(tool);
        ItemMeta resultItemMeta = result.getItemMeta();

        List<String> lore = new ArrayList<>();
        if(tool.getItemMeta() != null) {
            if(tool.getItemMeta().getLore() != null) {
                lore = tool.getItemMeta().getLore();
            }
        }
        List<String> finalLore = lore;
        enchantmentLevelMap.forEach(((enchantment, level) -> {
            String text = "§7" + enchantment.getName() + " " + TextUtil.toRoman(level);
            if(!finalLore.contains(text)) {
                finalLore.add(text);
            }
        }));


        resultItemMeta.setDisplayName(e.getInventory().getRenameText());
        resultItemMeta.setLore(finalLore);
        result.setItemMeta(resultItemMeta);
        result.addUnsafeEnchantments(enchantmentLevelMap);

        e.setResult(result);
    }

    @EventHandler
    public void onCloseAnvil(InventoryCloseEvent e) {
        if(e.getInventory().getType().equals(InventoryType.ANVIL)) {
            BossBar bossbar = playerManager.getPlayer((Player) e.getPlayer()).getBossBar();

            if(bossbar != null) {
                bossbar.setVisible(false);
            }
        }
    }
}
