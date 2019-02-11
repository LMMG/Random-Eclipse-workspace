package me.kairos.ipunish;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Configuration {

    private final IPunish plugin;

    private final List<String> banReasons = new ArrayList<>();
    public boolean mariaDb = true; //TODO: Configurable
    private String banBroadcastMessage;
    private String banMessage;
    private String kickMessage;
    private String kickBroadcast;
    private String muteBroadcast;
    private String unmuteBroadcast;
    private String tempbanBroadcast;
    private String tempbanMessage;
    private String muteMessage;
    private String unbanBroadcast;
    private String defaultBanReason;

    public Configuration(IPunish plugin) {
        this.plugin = plugin;
        this.reload();
    }

    public void reload() {
        synchronized (this) {
            FileConfiguration configuration = plugin.getConfig();
            this.banReasons.clear();
            this.banReasons.addAll(configuration.getStringList("ban-reasons"));
            this.banBroadcastMessage = colourise(configuration.getString("ban-broadcast"));
            this.kickMessage = colourise(configuration.getString("kick-message"));
            this.muteBroadcast = colourise(configuration.getString("mute-broadcast"));
            this.muteMessage = colourise(configuration.getString("mute-message"));
            this.unmuteBroadcast = colourise(configuration.getString("unmute-broadcast"));
            this.kickBroadcast = colourise(configuration.getString("kick-broadcast"));
            this.tempbanBroadcast = colourise(configuration.getString("tempban-broadcast"));
            this.tempbanMessage = colourise(configuration.getString("tempban-message"));
            this.banMessage = colourise(configuration.getString("ban-message"));
            this.unbanBroadcast = colourise(configuration.getString("unban-broadcast"));
            this.defaultBanReason = colourise(configuration.getString("default-ban-reason", "Misconduct"));
        }
    }

    private static String colourise(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
