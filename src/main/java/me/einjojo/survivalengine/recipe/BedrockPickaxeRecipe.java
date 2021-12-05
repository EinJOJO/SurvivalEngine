package me.einjojo.survivalengine.recipe;

import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BedrockPickaxeRecipe extends CustomRecipe {

    private static ItemStack item;

    public BedrockPickaxeRecipe() {
        super(NamespacedKey.minecraft("bedrock_pickaxe"));
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.NETHERITE_PICKAXE);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§c§lExplosive Spitzhacke");
        List<String> lore = new ArrayList<>();

        lore.add("§7Diese Spitzhacke ist so explosiv,");
        lore.add("§7dass §cjeder §7Block mit sicherheit");
        lore.add("§7zerstört werden kann.");
        lore.add("");
        lore.add("§cInfo: Nur einmal nutzbar");

        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemStack.setItemMeta(itemMeta);

        item = itemStack;

        return itemStack;
    }

    public static ItemStack getItemStack() {
        return item;
    }

    @Override
    public Recipe getRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(getNamespace(), getItem());
        shapedRecipe.shape("CTC"," N ", " B ");
        shapedRecipe.setIngredient('C', Material.CRYING_OBSIDIAN);
        shapedRecipe.setIngredient('T', Material.TNT);
        shapedRecipe.setIngredient('N', Material.NETHERITE_PICKAXE);
        shapedRecipe.setIngredient('B', Material.BLAZE_ROD);

        return shapedRecipe;
    }
}
