package com.github.alathra.siegeengines.listeners;

import com.github.alathra.siegeengines.SiegeEngine;
import com.github.alathra.siegeengines.SiegeEngines;
import com.github.alathra.siegeengines.config.Config;
import com.github.alathra.siegeengines.data.SiegeEnginesData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class SiegeEnginePlaceListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onSiegeEnginePlace(org.bukkit.event.block.BlockPlaceEvent event) {
        Player thePlayer = event.getPlayer();
        Material replaced = event.getBlockReplacedState().getType();
        ItemStack item = null;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.CARVED_PUMPKIN) {
            item = event.getPlayer().getInventory().getItemInMainHand();
        } else if (event.getPlayer().getInventory().getItemInOffHand().getType() == Material.CARVED_PUMPKIN) {
            item = event.getPlayer().getInventory().getItemInOffHand();
        }

        if (item == null) return;
        if (item.getItemMeta() == null) return;
        if (!item.getItemMeta().hasCustomModelData()) return;

        int customModel = item.getItemMeta().getCustomModelData();
        SiegeEngine siegeEngine = null;
        // Search for match in custom model data value in defined siege engines
        for (SiegeEngine entry : SiegeEngines.definedSiegeEngines.values()) {
            try {
                if (entry.getCustomModelID() == customModel) {
                    siegeEngine = entry.clone();
                } else if (entry.getFiringModelNumbers().contains(customModel)) {
                    // if siege engine was broken during one of its firing stages
                    siegeEngine = entry.clone();
                }
            } catch (CloneNotSupportedException e) {
                break;
            }
        }

        // If SiegeEngine found, place it
        if (siegeEngine == null) return;

        if (Config.disabledWorlds.contains(thePlayer.getWorld())) {
            thePlayer.sendMessage("§eSiege Engines cannot be placed in this World.");
            event.setCancelled(true);
        }
        
        if (SiegeEnginesData.fluidMaterials.contains(replaced)) {
            thePlayer.sendMessage("§eSiege Engines cannot be placed in Fluid Blocks.");
            event.setCancelled(true);
        }
        
        if (event.isCancelled()) return;
        
        if (siegeEngine.place(thePlayer, event.getBlockAgainst().getLocation())) {
            item.subtract();
            thePlayer.sendMessage("§eSiege Engine placed!");
        } else {
            thePlayer.sendMessage("§eSiege Engine cannot be placed within a " + Config.placementDensity + " Block-Radius of other Siege Engines.");
        }
        event.setCancelled(true);
    }
}
