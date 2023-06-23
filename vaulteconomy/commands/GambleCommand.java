package me.cobble.vaulteconomy.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.bukkit.attribute.Attribute;

import java.util.Objects;
import java.util.Random;

public class GambleCommand implements CommandExecutor {

    private final Economy economy;
    private final Random random;

    public GambleCommand(@NotNull Economy economy) {
        Objects.requireNonNull(economy, "Economy cannot be null.");
        this.economy = economy;
        this.random = new Random();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!economy.has(player, 1000)) {
            player.sendMessage(ChatColor.RED + "You don't have enough money to gamble! You need $1000 to play.");
            return true;
        }

        boolean win = random.nextBoolean();
        if (win) {
            double maxHealth = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
            int heartsWon = 1;
            if (maxHealth < 180) {
                maxHealth += 2.0;
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth + heartsWon);
                player.setHealth(maxHealth);
            }

            player.sendMessage(ChatColor.GREEN + "You won! You gained " + heartsWon + " hearts!");
        } else {
            economy.withdrawPlayer(player, 1000);
            player.sendMessage(ChatColor.RED + "You lost! $1000 has been deducted from your balance.");
        }

        return true;
    } }

