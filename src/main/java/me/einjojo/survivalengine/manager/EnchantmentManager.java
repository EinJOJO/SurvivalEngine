package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.enchantment.EnchantmentWrapper;
import me.einjojo.survivalengine.enchantment.TelepathyEnchantment;
import me.einjojo.survivalengine.util.TextUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnchantmentManager {

    private final List<Enchantment> enchantmentList;
    private final SurvivalEngine plugin;

    public EnchantmentManager(SurvivalEngine plugin) {
        this.enchantmentList = new ArrayList<>();
        this.plugin = plugin;
        loadEnchantments();
        register();
    }

    private void loadEnchantments() {
        getEnchantmentList().add(Enchantments.TELEPATHY.getEnchantment());
    }

    private void register() {
        enchantmentList.forEach((enchantment -> {
            boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(enchantment);

            if(!registered){
                registerEnchantment(enchantment);
            }
        }));
    }

    public void enchant(ItemStack itemStack, Enchantment enchantment) {
        enchant(itemStack, enchantment, 1);
    }

    public void enchant(ItemStack itemStack, Enchantment enchantment, int level) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        assert itemMeta != null;
        if(itemMeta.hasLore()) {
            lore = itemMeta.getLore();
        }
        String loreText = "§7" + enchantment.getName() + " " + TextUtil.toRoman(level);
        if(!lore.contains(loreText)) {
            lore.add(loreText);
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        itemStack.addUnsafeEnchantment(enchantment, level);

    }

    private void registerEnchantment(Enchantment enchantment) {
        try {
            Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public List<Enchantment> getEnchantmentList() {
        return enchantmentList;
    }

    public enum Enchantments {
        TELEPATHY(new TelepathyEnchantment(SurvivalEngine.getInstance()));

        private final EnchantmentWrapper enchantment;

        Enchantments(EnchantmentWrapper enchantmentWrapper) {
            this.enchantment = enchantmentWrapper;
        }

        public EnchantmentWrapper getEnchantment() {
            return enchantment;
        }
    }

}
