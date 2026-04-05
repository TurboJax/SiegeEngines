package com.github.alathra.siegeengines.api;

import com.github.alathra.siegeengines.SiegeEngine;
import com.github.alathra.siegeengines.SiegeEngines;
import com.github.alathra.siegeengines.config.Config;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

public class SiegeEnginesAPI {

    public static SiegeEngine getSiegeEngineFromEntity(Entity entity) {
        return SiegeEngines.activeSiegeEngines.getOrDefault(entity.getUniqueId(), null);
    }

    public static ItemStack getTrebuchetItem() {
        ItemStack trebuchetItem = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = trebuchetItem.getItemMeta();
        CustomModelDataComponent cmd = meta.getCustomModelDataComponent();
        cmd.setFloats(List.of(122f));
        meta.setCustomModelDataComponent(cmd);
        meta.setDisplayName(Config.trebuchetItemName);
        meta.setLore(Config.trebuchetItemLore);
        trebuchetItem.setItemMeta(meta);
        return trebuchetItem;
    }

    public static ItemStack getBallistaItem() {
        ItemStack ballistaItem = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = ballistaItem.getItemMeta();
        CustomModelDataComponent cmd = meta.getCustomModelDataComponent();
        cmd.setFloats(List.of(145f));
        meta.setCustomModelDataComponent(cmd);
        meta.setDisplayName(Config.ballistaItemName);
        meta.setLore(Config.ballistaItemLore);
        ballistaItem.setItemMeta(meta);
        return ballistaItem;
    }

    public static ItemStack getSwivelCannonItem() {
        ItemStack swivelCannonItem = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = swivelCannonItem.getItemMeta();
        CustomModelDataComponent cmd = meta.getCustomModelDataComponent();
        cmd.setFloats(List.of(141f));
        meta.setCustomModelDataComponent(cmd);
        meta.setDisplayName(Config.swivelCannonItemName);
        meta.setLore(Config.swivelCannonItemLore);
        swivelCannonItem.setItemMeta(meta);
        return swivelCannonItem;
    }

    public static ItemStack getBreachCannonItem() {
        ItemStack breachCannonItem = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = breachCannonItem.getItemMeta();
        CustomModelDataComponent cmd = meta.getCustomModelDataComponent();
        cmd.setFloats(List.of(142f));
        meta.setCustomModelDataComponent(cmd);
        meta.setDisplayName(Config.breachCannonItemName);
        meta.setLore(Config.breachCannonItemLore);
        breachCannonItem.setItemMeta(meta);
        return breachCannonItem;
    }
}