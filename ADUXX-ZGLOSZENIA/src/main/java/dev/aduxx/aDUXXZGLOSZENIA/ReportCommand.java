package dev.aduxx.aDUXXZGLOSZENIA;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import reports.CooldownManager;

public class ReportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!(sender instanceof Player) || sender.hasPermission(Main.getInstance().getConfig().getString("config-reload-permission"))) {
                Main.getInstance().reloadConfig();
                sender.sendMessage(Main.getInstance().getConfig().getString("config-reload").replace("&", "§"));
            } else {
                sender.sendMessage(Main.getInstance().getConfig().getString("no-permission-reload").replace("&", "§"));
            }
            return true;
        }

        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(Main.getInstance().getConfig().getString("usage").replace("&", "§"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Main.getInstance().getConfig().getString("not-found-player")
                    .replace("&", "§")
                    .replace("[PLAYER]", args[0]));
            return true;
        }


        int cooldown = Main.getInstance().getConfig().getInt("cooldown", 230);
        if (CooldownManager.hasCooldown(player.getName(), cooldown)) {
            long remaining = CooldownManager.getRemaining(player.getName(), cooldown);
            player.sendMessage(Main.getInstance().getConfig().getString("cooldown-message")
                    .replace("&", "§")
                    .replace("[COOLDOWN]", String.valueOf(remaining)));
            return true;
        }


        ReportGui.openReportGUI(player, target.getName());
        CooldownManager.setCooldown(player.getName());
        return true;
    }
}
