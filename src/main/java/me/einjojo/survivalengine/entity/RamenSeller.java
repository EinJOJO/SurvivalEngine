package me.einjojo.survivalengine.entity;

import me.einjojo.survivalengine.recipe.RamenItemStack;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class RamenSeller  {

    static List<MerchantRecipe> merchantRecipes;

    public RamenSeller(Player player) {
        Villager seller = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);

        seller.setProfession(Villager.Profession.BUTCHER);
        seller.setAI(false);
        seller.setVillagerType(Villager.Type.TAIGA);
        seller.setInvulnerable(true);
        seller.setCustomName("§cIchiraku");
        seller.setCustomNameVisible(true);
        seller.setVillagerLevel(5);
        seller.setRecipes(getRecipes());
    }



    private List<MerchantRecipe> getRecipes() {
        List<MerchantRecipe> recipes = new ArrayList<>();

        recipes.add(createRecipe(RamenItemStack.getItemStack(), new ItemStack(Material.EMERALD, 3)));

        merchantRecipes = recipes;
        return recipes;
    }

    private MerchantRecipe createRecipe(ItemStack selling, ItemStack price) {
        List<ItemStack> priceList = new ArrayList<>();
        priceList.add(price);
        return createRecipe(selling, priceList);
    }

    private MerchantRecipe createRecipe(ItemStack selling, List<ItemStack> price){
        MerchantRecipe recipe = new MerchantRecipe(selling, Integer.MAX_VALUE);
        recipe.setIngredients(price);
        return recipe;
    }

}
