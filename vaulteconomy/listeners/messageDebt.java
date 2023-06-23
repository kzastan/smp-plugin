package me.cobble.vaulteconomy.listeners;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class messageDebt {

    private final Economy economy;
    private final Map<UUID, LocalDateTime> debtMap;
    private final Plugin plugin;

    public messageDebt(Economy economy, Plugin plugin) {
        this.economy = economy;
        this.debtMap = new HashMap<>();
        this.plugin = plugin;
    }

    public void checkDebt(Player player) {
        double balance = economy.getBalance(player);
        if (balance < 0) {
            LocalDateTime now = LocalDateTime.now();
            if (!debtMap.containsKey(player.getUniqueId())) {
                debtMap.put(player.getUniqueId(), now);
                player.sendMessage(ChatColor.DARK_RED + "You are now in debt. If you don't remove this debt within 7 days, you will be banned.");
            } else {
                LocalDateTime debtStart = debtMap.get(player.getUniqueId());
                Duration debtDuration = Duration.between(debtStart, now);
                if (debtDuration.toDays() >= 7) {
                    plugin.getServer().banIP(player.getAddress().getAddress().getHostAddress());
                    player.kickPlayer(ChatColor.RED + "" + ChatColor.BOLD + "You got banned as you couldn't pay your loan.");
                }
            }
        } else {
            if (debtMap.containsKey(player.getUniqueId())) {
                debtMap.remove(player.getUniqueId());
                economy.depositPlayer(player, 500);
                player.sendMessage(ChatColor.GREEN + "You are now out of debt. You are not getting banned. Your balance has been set to 500.");
            } else if (balance == 0) {
                economy.depositPlayer(player, 500);
                player.sendMessage(ChatColor.GREEN + "Welcome to the server! Your balance has been set to 500.");
            }
        }
    }
}
