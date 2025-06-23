package reports;

import dev.aduxx.aDUXXZGLOSZENIA.Main;
import dev.aduxx.aDUXXZGLOSZENIA.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class InfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Tylko gracze mogą używać tej komendy.");
            return true;
        }

        Player player = (Player) sender;
        FileConfiguration config = Main.getInstance().getConfig();


        String permission = config.getString("report-list-permission", "zgloszenia.show");
        if (!player.hasPermission(permission)) {
            player.sendMessage(Main.getInstance().getConfig().getString("no-permission-reload").replace("&","§"));
            return true;
        }


        if (args.length != 1) {
            player.sendMessage(Main.getInstance().getConfig().getString("info-usage").replace("&","§"));
            return true;
        }

        String targetName = args[0];


        int reportCount = ReportManager.getReportCount(targetName);
        String lastReason = ReportManager.getLastReason(targetName);


        List<String> messages = config.getStringList("report-list-messages");
        for (String line : messages) {
            line = line.replace("&", "§")
                    .replace("[PLAYER]", targetName)
                    .replace("[TIMES]", String.valueOf(reportCount))
                    .replace("[REASON]", lastReason != null ? lastReason : "§c§lBRAK");
            player.sendMessage(line);
        }

        return true;
    }
}
