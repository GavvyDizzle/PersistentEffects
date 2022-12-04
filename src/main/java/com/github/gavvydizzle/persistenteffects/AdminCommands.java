package com.github.gavvydizzle.persistenteffects;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminCommands implements TabExecutor {

    private final ArrayList<String> argsList;

    public AdminCommands() {
        argsList = new ArrayList<>(1);
        argsList.add("help");
        argsList.add("reload");
        argsList.add("toggle");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /peff help");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            boolean preCommand = PersistentEffects.getInstance().getDeathHandler().isEnabled();

            PersistentEffects.getInstance().reloadConfig();
            PersistentEffects.getInstance().getDeathHandler().reload();
            sender.sendMessage(ChatColor.GREEN + "[PersistentEffects] Reloaded");

            boolean postCommand = PersistentEffects.getInstance().getDeathHandler().isEnabled();

            if (preCommand == postCommand) {
                sender.sendMessage(ChatColor.YELLOW + " - Status did not change");
            }
            else if (postCommand) {
                sender.sendMessage(ChatColor.GREEN + " - Persistent effects ENABLED");
            }
            else {
                sender.sendMessage(ChatColor.RED + " - Persistent effects DISABLED");
            }
        }
        else if (args[0].equalsIgnoreCase("toggle")) {
            boolean val = !PersistentEffects.getInstance().getDeathHandler().isEnabled();
            PersistentEffects.getInstance().getDeathHandler().setEnabled(val);

            PersistentEffects.getInstance().getConfig().set("enabled", val);
            PersistentEffects.getInstance().saveConfig();

            if (val) {
                sender.sendMessage(ChatColor.GREEN + "[PersistentEffects] ENABLED");
            }
            else {
                sender.sendMessage(ChatColor.GREEN + "[PersistentEffects]" + ChatColor.RED + " DISABLED");
            }
        }
        else {
            if (!args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatColor.RED + "Unknown command");
            }
            sender.sendMessage(ChatColor.GOLD +  "-----(PersistentEffects Admin Commands)-----");
            sender.sendMessage(ChatColor.YELLOW + "/peff help - Displays this menu");
            sender.sendMessage(ChatColor.YELLOW + "/peff reload - Reloads the config file");
            sender.sendMessage(ChatColor.YELLOW + "/peff toggle - Toggles plugin state");
            sender.sendMessage(ChatColor.GOLD +  "-----(PersistentEffects Admin Commands)-----");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], argsList, list);
        }

        return list;
    }
}
