package me.cobble.vaulteconomy.listeners;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class OnPlayerDeath implements Listener {

    private final Economy economy;
    private final Map<UUID, LocalDateTime> debtMap;
    private final Plugin plugin;

    public OnPlayerDeath(Economy economy, Plugin plugin) {
        this.economy = economy;
        this.debtMap = new HashMap<>();
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (economy != null) {
            double balance = economy.getBalance(player);
            if (balance < 0) {
                LocalDateTime now = LocalDateTime.now();
                if (!debtMap.containsKey(player.getUniqueId())) {
                    debtMap.put(player.getUniqueId(), now);
                } else {
                    LocalDateTime debtStart = debtMap.get(player.getUniqueId());
                    Duration debtDuration = Duration.between(debtStart, now);
                    if (debtDuration.toDays() >= 5) {
                        plugin.getServer().banIP(Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress());
                        player.kickPlayer(ChatColor.RED + "" + ChatColor.BOLD + "You got banned as you couldn't pay your loan.");
                        return;
                    }
                }
            } else debtMap.remove(player.getUniqueId());
            economy.withdrawPlayer(player, 1000);
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You lost $1000 upon death.");
        } else {
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Error: Economy not initialized.");
        }
    }
}
