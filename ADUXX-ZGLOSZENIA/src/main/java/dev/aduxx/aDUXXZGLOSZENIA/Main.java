package dev.aduxx.aDUXXZGLOSZENIA;

import org.bukkit.plugin.java.JavaPlugin;
import reports.ReportTabCompleter;

public class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getCommand("zglos").setExecutor(new ReportCommand());
        getServer().getPluginManager().registerEvents(new ReportGui(), this);
        getServer().getPluginManager().registerEvents(new ReportListener(), this);
        this.getCommand("zglos").setTabCompleter(new ReportTabCompleter());
    }

    public static Main getInstance() {
        return instance;
    }
}