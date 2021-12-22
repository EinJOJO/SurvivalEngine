package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class TelepathyBookRecipe {

    private static ItemStack itemStack;

    public static ItemStack getItemStack () {
        if(itemStack == null) {
            itemStack = new ItemStack(Material.ENCHANTED_BOOK);
            SurvivalEngine.getInstance().getEnchantmentManager().enchant(itemStack, EnchantmentManager.Enchantments.TELEPATHY.getEnchantment());
        }
        return itemStack;
    }

    public static MerchantRecipe getMerchantRecipe() {
        MerchantRecipe merchantRecipe = new MerchantRecipe(getItemStack(), 1);
        merchantRecipe.addIngredient(new ItemStack(Material.BOOK));
        merchantRecipe.addIngredient(new ItemStack(Material.EMERALD_BLOCK, 16));

        return merchantRecipe;
    }





}
