package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.TelepathyBookRecipe;
import org.bukkit.Bukkit;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;

import java.util.Random;

public class VillagerListener implements Listener {

    public VillagerListener(SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onTradeUnlock(VillagerAcquireTradeEvent e) {
        if(!(e instanceof Villager)) return;
        Villager villager = (Villager) e.getEntity();
        if(villager.getProfession() != Villager.Profession.LIBRARIAN) {
            return;
        }

        Random random = new Random();
        if(random.nextInt(5000) == 0) {
            e.setRecipe(TelepathyBookRecipe.getMerchantRecipe());
        }
    }


}
