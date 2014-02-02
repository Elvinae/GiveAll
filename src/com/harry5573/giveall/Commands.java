package com.harry5573.giveall;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {

    public static Main plugin;

    public Commands(Main instance) {
        Commands.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }

        if (p == null) {
            plugin.log("Thats not a console command!");
            return true;
        }

        if (!p.isOp() && !p.hasPermission("giveall.give")) {
            p.sendMessage(plugin.prefix + ChatColor.RED + "Permission denied!");
            return true;
        }

        if (label.equalsIgnoreCase("giveall")) {

            if (args.length == 0) {
                p.sendMessage(plugin.prefix + ChatColor.YELLOW + "Usage: /giveall <itemid|cash|hand|command> <amount>");
                return true;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("hand")) {
                    ItemStack handstack = p.getItemInHand();

                    if (handstack == null) {
                        p.sendMessage(plugin.prefix + ChatColor.RED + "You have nothing in your hand!");
                        return true;
                    }

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(plugin.prefix + ChatColor.GREEN + p.getName() + ChatColor.WHITE + " has given everyone " + ChatColor.AQUA + handstack.getAmount() + ChatColor.WHITE + " x " + ChatColor.AQUA + handstack.getType().toString() + ChatColor.WHITE + "!");
                        player.getInventory().addItem(handstack);
                        player.updateInventory();
                    }

                    return true;
                }
                p.sendMessage(plugin.prefix + ChatColor.YELLOW + "Usage: /giveall <itemid|cash|hand|command> <amount>");
                return true;
            }

            //Giveall command crate add {NAME} 5
            if (args.length > 1 && (args[0].equalsIgnoreCase("command") || args[0].equalsIgnoreCase("cmd"))) {
                StringBuilder message = new StringBuilder("");
                for (String part : args) {
                    if (!part.equals(args[0])) {
                        if (!message.toString().equals("")) {
                            message.append(" ");
                        }
                        message.append(part);
                    }
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(plugin.prefix + ChatColor.GREEN + p.getName() + ChatColor.WHITE + " has executed a command for all users!");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message.toString().replace("[NAME]", player.getName()));
                }

                p.sendMessage(plugin.prefix + ChatColor.YELLOW + "Command executed for all users.");
                return true;
            }

            if (args.length == 2) {

                if (args[0].equalsIgnoreCase("money") || args[0].equalsIgnoreCase("cash")) {
                    int cash = 0;

                    try {
                        cash = Integer.valueOf(args[1]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.prefix + ChatColor.RED + "Thats not a number!");
                        return true;
                    }

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(plugin.prefix + ChatColor.GREEN + p.getName() + ChatColor.WHITE + " has given everyone " + ChatColor.GREEN + "$" + cash + ChatColor.WHITE + "!");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + player.getName() + " " + cash);
                    }
                    return true;
                }

                String itemid = String.valueOf(args[0]);

                int amount = 0;
                int data = 0;
                int itemidint = 0;

                if (itemid.contains(":")) {
                    String[] split = itemid.split(":");

                    try {
                        itemidint = Integer.valueOf(split[0]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.prefix + ChatColor.RED + "Thats not a number!");
                        return true;
                    }

                    try {
                        data = Integer.valueOf(split[1]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.prefix + ChatColor.RED + "Thats not a number!");
                        return true;
                    }
                } else {
                    try {
                        itemidint = Integer.valueOf(args[0]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.prefix + ChatColor.RED + "Thats not a number!");
                        return true;
                    }
                }

                try {
                    amount = Integer.valueOf(args[1]);
                } catch (NumberFormatException e) {
                    p.sendMessage(plugin.prefix + ChatColor.RED + "Thats not a number!");
                    return true;
                }

                Material item = Material.getMaterial(itemidint);

                if (item == null) {
                    p.sendMessage(plugin.prefix + ChatColor.GOLD + "Thats not a valid item ID!");
                    return true;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(plugin.prefix + ChatColor.GREEN + p.getName() + ChatColor.WHITE + " has given everyone " + ChatColor.AQUA + amount + ChatColor.WHITE + " x " + ChatColor.AQUA + item.toString() + ChatColor.WHITE + "!");
                    player.getInventory().addItem(new ItemStack(item, amount, (short) data));
                }
                return true;
            }
        }

        return false;
    }
}
