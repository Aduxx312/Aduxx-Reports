package dev.aduxx.aDUXXZGLOSZENIA;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.bukkit.configuration.file.FileConfiguration;

public class DiscordWebhookSender {

    public static String stripColorCodes(String input) {
        if (input == null) return "";
        return input.replaceAll("(?i)§[0-9A-FK-OR]", "").replace("&", "");
    }

    public static void sendReport(String reporter, String target, String reason) {
        try {
            FileConfiguration config = Main.getInstance().getConfig();
            String webhookUrl = config.getString("webhook.url");
            if (webhookUrl == null || webhookUrl.isEmpty()) return;

            String title = stripColorCodes(config.getString("webhook.report-title", " "));
            String description = stripColorCodes(
                    config.getString("webhook.report-description", "**🚀Gracz [REPORTER] Zgłosił gracza: [PLAYER] , powód: [REASON]**")
                            .replace("[REPORTER]", reporter)
                            .replace("[PLAYER]", target)
                            .replace("[REASON]", reason)
            );

            String footer = stripColorCodes(config.getString("webhook.report-footer", "System zgłoszeń: dc.lemonhost.pl"));


            int color;
            try {
                color = config.getInt("webhook.color", 1807612);
            } catch (Exception e) {
                color = 1807612;
            }

            boolean hasTitle = title != null && !title.trim().isEmpty();

            String json = "{ \"embeds\": [{";

            if (hasTitle) {
                json += "\"title\": \"" + title + "\",";
            }

            json += "\"description\": \"" + description + "\","
                    + "\"color\": " + color + ","
                    + "\"footer\": {\"text\": \"" + footer + "\"}"
                    + "}]}";

            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            byte[] postData = json.getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write(postData);
            os.flush();
            os.close();

            connection.getInputStream().close();
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
