package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class PotionEffectListener implements Listener {


    private final SurvivalEngine plugin;
    private final Map<Player, ArrayList<PotionEffect>> EFFECT_MAP;

    public PotionEffectListener(SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.EFFECT_MAP = new HashMap<>();
        this.plugin = plugin;
    }

    @EventHandler
    public void onBeaconEffect(EntityPotionEffectEvent e) {
        if(e.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) e.getEntity();

        if(e.getCause() != EntityPotionEffectEvent.Cause.BEACON) {
            return;
        }

        if(e.getAction() == EntityPotionEffectEvent.Action.CHANGED) {
            e.setOverride(true);
            PotionEffect newEffect = e.getNewEffect();
            if(newEffect == null) {
                return;
            }


            ArrayList<PotionEffect> potionEffects = EFFECT_MAP.get(player);
            if(potionEffects == null) {
                potionEffects = new ArrayList<>();

                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

                    ArrayList<PotionEffect> potionEffectArrayList = EFFECT_MAP.get(player);
                    Map<PotionEffect, Integer> potionEffectAmplifierMap = new HashMap<>();
                    potionEffectArrayList.forEach((effect)->{

                        if(!potionEffectAmplifierMap.containsKey(effect)) {
                            potionEffectAmplifierMap.put(effect, effect.getAmplifier());
                            return;
                        }
                        int amplifier = potionEffectAmplifierMap.get(effect) + effect.getAmplifier() + 1;
                        if(amplifier > 10) {
                            return;
                        }
                        potionEffectAmplifierMap.put(effect, amplifier);
                    });

                    potionEffectAmplifierMap.forEach((key, value) -> {
                        player.addPotionEffect(key.getType().createEffect(key.getDuration(), value));
                    });

                    EFFECT_MAP.remove(player);
                }, 2);
            }

            potionEffects.add(e.getNewEffect());
            EFFECT_MAP.put(player, potionEffects);
        }
    }
}
