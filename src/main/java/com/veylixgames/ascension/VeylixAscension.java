package com.veylixgames.ascension;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class VeylixAscension extends JavaPlugin implements Listener {

    private NamespacedKey enderiteKey;
    private NamespacedKey ascendantKey;

    @Override
    public void onEnable() {
        enderiteKey = new NamespacedKey(this, "enderite");
        ascendantKey = new NamespacedKey(this, "ascendant");

        getServer().getPluginManager().registerEvents(this, this);
        registerRecipes();

        getLogger().info("Veylix Ascension enabled!");
    }

    private ItemStack taggedItem(Material material, NamespacedKey key,
                                  String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

        item.setItemMeta(meta);
        return item;
    }

    private boolean isTagged(ItemStack item, NamespacedKey key) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        return meta.getPersistentDataContainer().has(
                key,
                PersistentDataType.BYTE
        );
    }

    private ItemStack enderite() {
        return taggedItem(
                Material.NETHERITE_INGOT,
                enderiteKey,
                "§5Enderite Ingot",
                List.of("§7Forged from the power of the End.")
        );
    }

    private ItemStack ascendant() {
        return taggedItem(
                Material.NETHERITE_INGOT,
                ascendantKey,
                "§dAscendant Ingot",
                List.of("§7The ultimate endgame material.")
        );
    }

    private RecipeChoice exact(ItemStack item) {
        return new RecipeChoice.ExactChoice(item);
    }

    private void registerRecipes() {

        /*
         * DRAGON EGG DUPLICATION
         *
         * N N N
         * N E N
         * N N N
         *
         * 1 Dragon Egg -> 2 Dragon Eggs
         */

        ShapedRecipe dragonEgg = new ShapedRecipe(
                new NamespacedKey(this, "dragon_egg_duplication"),
                new ItemStack(Material.DRAGON_EGG, 2)
        );

        dragonEgg.shape("NNN", "NEN", "NNN");
        dragonEgg.setIngredient('N', Material.NETHERITE_BLOCK);
        dragonEgg.setIngredient('E', Material.DRAGON_EGG);

        getServer().addRecipe(dragonEgg);


        /*
         * NETHER STAR DUPLICATION
         *
         * N N N
         * N S N
         * N N N
         *
         * 1 Nether Star -> 2 Nether Stars
         */

        ShapedRecipe netherStar = new ShapedRecipe(
                new NamespacedKey(this, "nether_star_duplication"),
                new ItemStack(Material.NETHER_STAR, 2)
        );

        netherStar.shape("NNN", "NSN", "NNN");
        netherStar.setIngredient('N', Material.NETHERITE_BLOCK);
        netherStar.setIngredient('S', Material.NETHER_STAR);

        getServer().addRecipe(netherStar);


        /*
         * ENDERITE INGOT
         *
         * S E S
         * E N E
         * S E S
         *
         * S = Shulker Shell
         * E = Ender Pearl
         * N = Netherite Ingot
         */

        ShapedRecipe enderiteRecipe = new ShapedRecipe(
                new NamespacedKey(this, "enderite_ingot"),
                enderite()
        );

        enderiteRecipe.shape("SES", "ENE", "SES");
        enderiteRecipe.setIngredient('S', Material.SHULKER_SHELL);
        enderiteRecipe.setIngredient('E', Material.ENDER_PEARL);
        enderiteRecipe.setIngredient('N', Material.NETHERITE_INGOT);

        getServer().addRecipe(enderiteRecipe);


        /*
         * ASCENDANT INGOT
         *
         * A A A
         * A N A
         * A A A
         *
         * A = Enderite
         * N = Nether Star
         */

        ShapedRecipe ascendantRecipe = new ShapedRecipe(
                new NamespacedKey(this, "ascendant_ingot"),
                ascendant()
        );

        ascendantRecipe.shape("AAA", "ANA", "AAA");
        ascendantRecipe.setIngredient('A', exact(enderite()));
        ascendantRecipe.setIngredient('N', Material.NETHER_STAR);

        getServer().addRecipe(ascendantRecipe);


        registerArmor();
        registerTools();
        registerAscendantElytra();
    }


    private ItemStack gear(Material material, String name, String keyName,
                           double armor, double toughness, double knockback) {

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(List.of("§7Forged by Veylix Games."));

        NamespacedKey key = new NamespacedKey(this, keyName);
        meta.getPersistentDataContainer().set(
                key,
                PersistentDataType.BYTE,
                (byte) 1
        );

        if (armor != 0) {
            meta.addAttributeModifier(
                    Attribute.ARMOR,
                    new AttributeModifier(
                            new NamespacedKey(this, keyName + "_armor"),
                            armor,
                            AttributeModifier.Operation.ADD_NUMBER,
                            org.bukkit.inventory.EquipmentSlotGroup.ARMOR
                    )
            );
        }

        if (toughness != 0) {
            meta.addAttributeModifier(
                    Attribute.ARMOR_TOUGHNESS,
                    new AttributeModifier(
                            new NamespacedKey(this, keyName + "_toughness"),
                            toughness,
                            AttributeModifier.Operation.ADD_NUMBER,
                            org.bukkit.inventory.EquipmentSlotGroup.ARMOR
                    )
            );
        }

        if (knockback != 0) {
            meta.addAttributeModifier(
                    Attribute.KNOCKBACK_RESISTANCE,
                    new AttributeModifier(
                            new NamespacedKey(this, keyName + "_knockback"),
                            knockback,
                            AttributeModifier.Operation.ADD_NUMBER,
                            org.bukkit.inventory.EquipmentSlotGroup.ARMOR
                    )
            );
        }

        item.setItemMeta(meta);
        return item;
    }


    private void registerArmor() {

        ItemStack helmet = gear(
                Material.NETHERITE_HELMET,
                "§dAscendant Helmet",
                "ascendant_helmet",
                4, 4, 0.1
        );

        ShapedRecipe helmetRecipe = new ShapedRecipe(
                new NamespacedKey(this, "ascendant_helmet"),
                helmet
        );

        helmetRecipe.shape("AAA", "A A", "   ");
        helmetRecipe.setIngredient('A', exact(ascendant()));
        getServer().addRecipe(helmetRecipe);


        ItemStack chestplate = gear(
                Material.NETHERITE_CHESTPLATE,
                "§dAscendant Chestplate",
                "ascendant_chestplate",
                9, 4, 0.1
        );

        ShapedRecipe chestRecipe = new ShapedRecipe(
                new NamespacedKey(this, "ascendant_chestplate"),
                chestplate
        );

        chestRecipe.shape("A A", "AAA", "AAA");
        chestRecipe.setIngredient('A', exact(ascendant()));
        getServer().addRecipe(chestRecipe);


        ItemStack leggings = gear(
                Material.NETHERITE_LEGGINGS,
                "§dAscendant Leggings",
                "ascendant_leggings",
                7, 4, 0.1
        );

        ShapedRecipe legsRecipe = new ShapedRecipe(
                new NamespacedKey(this, "ascendant_leggings"),
                leggings
        );

        legsRecipe.shape("AAA", "A A", "A A");
        legsRecipe.setIngredient('A', exact(ascendant()));
        getServer().addRecipe(legsRecipe);


        ItemStack boots = gear(
                Material.NETHERITE_BOOTS,
                "§dAscendant Boots",
                "ascendant_boots",
                4, 4, 0.1
        );

        ShapedRecipe bootsRecipe = new ShapedRecipe(
                new NamespacedKey(this, "ascendant_boots"),
                boots
        );

        bootsRecipe.shape("   ", "A A", "A A");
        bootsRecipe.setIngredient('A', exact(ascendant()));
        getServer().addRecipe(bootsRecipe);
    }


    private void registerTools() {

        registerTool(
                Material.NETHERITE_SWORD,
                "§dAscendant Sword",
                "ascendant_sword",
                new String[]{"A", "A", "S"}
        );

        registerTool(
                Material.NETHERITE_PICKAXE,
                "§dAscendant Pickaxe",
                "ascendant_pickaxe",
                new String[]{"AAA", " S ", " S "}
        );

        registerTool(
                Material.NETHERITE_AXE,
                "§dAscendant Axe",
                "ascendant_axe",
                new String[]{"AA ", "AS ", " S "}
        );

        registerTool(
                Material.NETHERITE_SHOVEL,
                "§dAscendant Shovel",
                "ascendant_shovel",
                new String[]{"A", "S", "S"}
        );

        registerTool(
                Material.NETHERITE_HOE,
                "§dAscendant Hoe",
                "ascendant_hoe",
                new String[]{"AA ", " S ", " S "}
        );
    }


    private void registerTool(Material material, String name,
                              String keyName, String[] shape) {

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(List.of("§7An Ascendant-tier tool."));

        NamespacedKey key = new NamespacedKey(this, keyName);

        meta.getPersistentDataContainer().set(
                key,
                PersistentDataType.BYTE,
                (byte) 1
        );

        meta.addAttributeModifier(
                Attribute.ATTACK_DAMAGE,
                new AttributeModifier(
                        new NamespacedKey(this, keyName + "_damage"),
                        3,
                        AttributeModifier.Operation.ADD_NUMBER,
                        org.bukkit.inventory.EquipmentSlotGroup.HAND
                )
        );

        item.setItemMeta(meta);


        ShapedRecipe recipe = new ShapedRecipe(
                new NamespacedKey(this, keyName),
                item
        );

        recipe.shape(shape);

        recipe.setIngredient('A', exact(ascendant()));
        recipe.setIngredient('S', Material.STICK);

        getServer().addRecipe(recipe);
    }


    private void registerAscendantElytra() {

        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemMeta meta = elytra.getItemMeta();

        meta.setDisplayName("§dAscendant Elytra");
        meta.setLore(List.of(
                "§7Ascendant armor fused with an Elytra.",
                "§7Flight retained."
        ));

        meta.getPersistentDataContainer().set(
                new NamespacedKey(this, "ascendant_elytra"),
                PersistentDataType.BYTE,
                (byte) 1
        );

        meta.addAttributeModifier(
                Attribute.ARMOR,
                new AttributeModifier(
                        new NamespacedKey(this, "ascendant_elytra_armor"),
                        12,
                        AttributeModifier.Operation.ADD_NUMBER,
                        org.bukkit.inventory.EquipmentSlotGroup.CHEST
                )
        );

        meta.addAttributeModifier(
                Attribute.ARMOR_TOUGHNESS,
                new AttributeModifier(
                        new NamespacedKey(this, "ascendant_elytra_toughness"),
                        8,
                        AttributeModifier.Operation.ADD_NUMBER,
                        org.bukkit.inventory.EquipmentSlotGroup.CHEST
                )
        );

        meta.addAttributeModifier(
                Attribute.KNOCKBACK_RESISTANCE,
                new AttributeModifier(
                        new NamespacedKey(this, "ascendant_elytra_knockback"),
                        0.25,
                        AttributeModifier.Operation.ADD_NUMBER,
                        org.bukkit.inventory.EquipmentSlotGroup.CHEST
                )
        );

        elytra.setItemMeta(meta);


        ShapelessRecipe recipe = new ShapelessRecipe(
                new NamespacedKey(this, "ascendant_elytra"),
                elytra
        );

        recipe.addIngredient(exact(
                gear(
                        Material.NETHERITE_CHESTPLATE,
                        "§dAscendant Chestplate",
                        "ascendant_chestplate",
                        9, 4, 0.1
                )
        ));

        recipe.addIngredient(Material.ELYTRA);

        getServer().addRecipe(recipe);
    }


    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {

        ItemStack[] contents = event.getInventory().getMatrix();

        for (ItemStack item : contents) {
            if (item != null && item.getType() == Material.NETHERITE_INGOT) {

                if (isTagged(item, ascendantKey)) {
                    continue;
                }

                if (isTagged(item, enderiteKey)) {
                    continue;
                }
            }
        }
    }
}