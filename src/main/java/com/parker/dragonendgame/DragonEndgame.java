package com.parker.dragonendgame;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class DragonEndgame extends JavaPlugin {
    @Override
    public void onEnable() {
        ItemStack twoEggs = new ItemStack(Material.DRAGON_EGG, 2);

        ShapedRecipe duplicateEgg = new ShapedRecipe(
                new NamespacedKey(this, "dragon_egg_duplication"), twoEggs);

        duplicateEgg.shape("NNN", "NEN", "NNN");
        duplicateEgg.setIngredient('N', Material.NETHERITE_BLOCK);
        duplicateEgg.setIngredient('E', Material.DRAGON_EGG);

        getServer().addRecipe(duplicateEgg);
        getLogger().info("Dragon Egg duplication recipe enabled.");
    }
}
