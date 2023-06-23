package me.cobble.vaulteconomy.listeners;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerKill implements Listener {

    private final Economy economy;

    public OnPlayerKill(Economy economy, messageDebt messageDebt) {
        this.economy = economy;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer != null && economy != null) {
            economy.depositPlayer(killer, 1000);
            killer.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You earned $1000 for killing " + victim.getName() + ".");
        }
    }
}
