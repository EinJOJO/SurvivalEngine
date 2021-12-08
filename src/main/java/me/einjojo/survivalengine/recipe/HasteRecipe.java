package me.einjojo.survivalengine.recipe;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public class HasteRecipe extends CustomRecipe {


    private static ItemStack item;

    public HasteRecipe() {
        super(NamespacedKey.minecraft("potion_haste"));
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();

        potionMeta.setColor(Color.YELLOW);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20*300, 2), true);
        potionMeta.setDisplayName("§rPotion of Haste");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§eEin magisches Getränk,");
        lore.add("§ewelches so nicht existiert.");
        potionMeta.setLore(lore);

        itemStack.setItemMeta(potionMeta);


        item = itemStack;
        return itemStack;
    }


    private RecipeChoice.ExactChoice getStrengthPotion() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();

        potionMeta.setBasePotionData(new PotionData(PotionType.STRENGTH, false, true));

        itemStack.setItemMeta(potionMeta);


        return new RecipeChoice.ExactChoice(itemStack);

    }

    @Override
    public Recipe getRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(getNamespace(), getItem());

        shapedRecipe.shape(" P ","GSZ"," C ");
        shapedRecipe.setIngredient('P', Material.GOLDEN_PICKAXE);
        shapedRecipe.setIngredient('G', Material.GLOWSTONE_DUST);
        shapedRecipe.setIngredient('S', getStrengthPotion());
        shapedRecipe.setIngredient('Z', Material.SUGAR);
        shapedRecipe.setIngredient('C', Material.GOLDEN_CARROT);

        return shapedRecipe;
    }


    public static ItemStack getItemStack() {
        return item;
    }

}
