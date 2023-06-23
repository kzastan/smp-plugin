package me.cobble.vaulteconomy;

import me.cobble.vaulteconomy.commands.BalanceCommand;
import me.cobble.vaulteconomy.commands.GambleCommand;
import me.cobble.vaulteconomy.commands.AuthorCommand; // Import the AuthorCommand class
import me.cobble.vaulteconomy.listeners.OnPlayerDeath;
import me.cobble.vaulteconomy.listeners.OnPlayerKill;
import me.cobble.vaulteconomy.listeners.messageDebt;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class VaultEconomy extends JavaPlugin {

    private static Economy economy = null;
    private messageDebt messageDebt;

    @Override
    public void onEnable() {
        // Plugin startup logic here
        if (!setupEconomy()) {
            System.out.println("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("gamble").setExecutor(new GambleCommand(economy));
        messageDebt = new messageDebt(economy, this);
        Objects.requireNonNull(getCommand("economy")).setExecutor(new BalanceCommand());
        Bukkit.getPluginManager().registerEvents(new OnPlayerDeath(economy, this), this);
        getServer().getPluginManager().registerEvents(new OnPlayerKill(economy, messageDebt), this);
        getCommand("author").setExecutor(new AuthorCommand()); // Set the AuthorCommand executor

        // Schedule a repeating task to check for debt
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                messageDebt.checkDebt(player);
            }
        }, 0, 2 * 60); // Check debt every 5 minutes
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
                .getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    public static Economy getEconomy() {
        return economy;
    }

}
