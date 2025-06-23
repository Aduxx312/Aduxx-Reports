package dev.aduxx.aDUXXZGLOSZENIA;

import org.bukkit.plugin.java.JavaPlugin;
import reports.BanListener;
import reports.InfoCommand;
import reports.ReportStorage;
import reports.ReportTabCompleter;

import java.util.logging.Level;

public class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        if (!getDescription().getAuthors().contains("Aduxx")) {
            getLogger().log(Level.WARNING,"Wylaczono plugin! Plugin zawiera błednego autora!!!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        saveDefaultConfig();
        getCommand("zglos").setExecutor(new ReportCommand());
        getServer().getPluginManager().registerEvents(new ReportGui(), this);
        getServer().getPluginManager().registerEvents(new ReportListener(), this);
        getServer().getPluginManager().registerEvents(new BanListener(),this);
        this.getCommand("zglos").setTabCompleter(new ReportTabCompleter());
        this.getCommand("zglosinfo").setExecutor(new InfoCommand());

        ReportStorage.setup(this);
    }

    public static Main getInstance() {
        return instance;
    }
}