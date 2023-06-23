package me.cobble.vaulteconomy.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AuthorCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("author")) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "The author is " + ChatColor.GREEN + "Cobble#0002");
            return true;
        }
        return false;
    }
    }

