package com.github.alathra.siegeengines.command;

import com.github.alathra.siegeengines.SiegeEngine;
import com.github.alathra.siegeengines.config.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import static com.github.alathra.siegeengines.SiegeEngines.*;

public class SiegeEnginesCommand {
    public SiegeEnginesCommand() {
        new CommandAPICommand("siegeengines")
            .withFullDescription("Example command.")
            .withShortDescription("Example command.")
            .withPermission("siegeengines.command")
            .withSubcommands(
                commandGet(),
                commandGetAll(),
                commandReload()
            )
            .executesPlayer(this::onExecute)
            .register();
    }

    private void onExecute(CommandSender sender, CommandArguments args) {
        sender.sendMessage(Component.text("Incorrect usage, /siegeengines get, /siegeengines getAll, /siegeengines reload", NamedTextColor.YELLOW));
    }

    private CommandAPICommand commandGet() {
        return new CommandAPICommand("get")
            .withPermission("siegeengines.command.get")
            .withArguments(
                new StringArgument("equipmentid")
                    .replaceSuggestions(
                        ArgumentSuggestions.strings(
                            definedSiegeEngines.values().stream().map(SiegeEngine::getId).toList()
                        )
                    ),
                new PlayerArgument("target")
                    .setOptional(true)
            )
            .executesPlayer(this::onGet);
    }

    private CommandAPICommand commandGetAll() {
        return new CommandAPICommand("getAll")
            .withPermission("siegeengines.command.getAll")
            .executesPlayer(this::onGetAll);
    }

    private CommandAPICommand commandReload() {
        return new CommandAPICommand("reload")
            .withPermission("siegeengines.command.reload")
            .executesPlayer(this::onReload);
    }

    private void onGet(CommandSender sender, CommandArguments args) throws WrapperCommandSyntaxException {
        if (!(args.get("equipmentid") instanceof String equipmentId))
            throw CommandAPIBukkit.failWithAdventureComponent(Component.text("Invalid SiegeEngine id specified.", NamedTextColor.RED));

        Player player = (Player) args.getOptional("target").orElse(sender);

        for (SiegeEngine SiegeEquipment : definedSiegeEngines.values()) {
            if (SiegeEquipment.equals(equipmentId)) {
                giveSiegeEngine(player, SiegeEquipment);
                break;
            }
        }
    }

    private void onGetAll(CommandSender sender, CommandArguments args) throws WrapperCommandSyntaxException {
        if (!(sender instanceof Player player))
            throw CommandAPIBukkit.failWithAdventureComponent(Component.text("Only players can use this command.", NamedTextColor.RED));

        for (SiegeEngine i : definedSiegeEngines.values()) {
            giveSiegeEngine(player, i);
        }
    }

    private void onReload(CommandSender sender, CommandArguments args) throws WrapperCommandSyntaxException {
        if (!(sender instanceof Player))
            throw CommandAPIBukkit.failWithAdventureComponent(Component.text("Only players can use this command.", NamedTextColor.RED));
        Config.reload();
        activeSiegeEngines.clear();
        siegeEngineEntitiesPerPlayer.clear();
        definedSiegeEngines.clear();
        addDefaults();
        for (SiegeEngine i : definedSiegeEngines.values()) {
            if (Config.doDebug) {
                sender.sendMessage(Component.text("Enabled SiegeEngine : " + i.getEngineName(), NamedTextColor.YELLOW));
                sender.sendMessage(Component.text("SiegeEngine Propellant/\"Fuel\" ItemStacks : " + i.getFuelItem().toString(), NamedTextColor.YELLOW));
                for (ItemStack proj : i.getProjectiles().keySet()) {
                    sender.sendMessage(Component.text("SiegeEngine Projectile ItemStacks : " + proj.toString(), NamedTextColor.YELLOW));
                }
            }
        }
        sender.sendMessage(Component.text("SiegeEngine configs reloaded", NamedTextColor.YELLOW));
    }

    private void giveSiegeEngine(Player player, SiegeEngine siegeEngine) {
        ItemStack item = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = item.getItemMeta();
        CustomModelDataComponent cmd = meta.getCustomModelDataComponent();
        cmd.setFloats(List.of((float) siegeEngine.getReadyModelNumber()));
        meta.setCustomModelDataComponent(cmd);
        meta.setDisplayName(siegeEngine.getItemName());
        meta.setLore(siegeEngine.getItemLore());
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
    }
}