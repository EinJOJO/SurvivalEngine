package me.einjojo.survivalengine.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ChickenControllerRecipe extends CustomRecipe{

    public ChickenControllerRecipe() {
        super(NamespacedKey.minecraft("chicken_controller"));
    }
    private static ItemStack item;

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.IRON_HORSE_ARMOR);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§6§lChickennator");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7Verwandel ein Hünchen in");
        lore.add("§7deinen Transportsklaven");
        lore.add("");
        lore.add("§eRechtsklicke ein Hünchen");
        lore.add("§edamit es verwandelt wird.");
        lore.add("");

        itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setLore(lore);


        item = itemStack;
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public Recipe getRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(getNamespace(), getItem());
        shapedRecipe.shape(" M ", "BSB", "CCC");
        shapedRecipe.setIngredient('M', Material.CHEST_MINECART);
        shapedRecipe.setIngredient('B', Material.LIGHTNING_ROD);
        shapedRecipe.setIngredient('C', Material.COPPER_INGOT);
        shapedRecipe.setIngredient('S', Material.SADDLE);

        return shapedRecipe;
    }

    public static ItemStack getItemStack() {
        return item;
    }
}
