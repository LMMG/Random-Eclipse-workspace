/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.configuration.file.FileConfiguration
 */
package com.parapvp.base.manager;

import com.parapvp.base.BasePlugin;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ServerHandler {
    private final List<String> announcements = new ArrayList<String>();
    private final List<String> serverRules = new ArrayList<String>();
    private final BasePlugin plugin;
    public boolean useProtocolLib;
    private int announcementDelay;
    private long chatSlowedMillis;
    private long chatDisabledMillis;
    private int chatSlowedDelay;
    private String broadcastFormat;
    private FileConfiguration config;
    private boolean decreasedLagMode;

    public ServerHandler(BasePlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.reloadServerData();
    }

    public int getAnnouncementDelay() {
        return this.announcementDelay;
    }

    public void setAnnouncementDelay(int delay) {
        this.announcementDelay = delay;
    }

    public List<String> getAnnouncements() {
        return this.announcements;
    }

    public boolean isChatSlowed() {
        return this.getRemainingChatSlowedMillis() > 0;
    }

    public long getChatSlowedMillis() {
        return this.chatSlowedMillis;
    }

    public void setChatSlowedMillis(long ticks) {
        this.chatSlowedMillis = System.currentTimeMillis() + ticks;
    }

    public long getRemainingChatSlowedMillis() {
        return this.chatSlowedMillis - System.currentTimeMillis();
    }

    public boolean isChatDisabled() {
        return this.getRemainingChatDisabledMillis() > 0;
    }

    public long getChatDisabledMillis() {
        return this.chatDisabledMillis;
    }

    public void setChatDisabledMillis(long ticks) {
        long millis = System.currentTimeMillis();
        this.chatDisabledMillis = millis + ticks;
    }

    public long getRemainingChatDisabledMillis() {
        return this.chatDisabledMillis - System.currentTimeMillis();
    }

    public int getChatSlowedDelay() {
        return this.chatSlowedDelay;
    }

    public void setChatSlowedDelay(int delay) {
        this.chatSlowedDelay = delay;
    }

    public String getBroadcastFormat() {
        return this.broadcastFormat;
    }

    public void setBroadcastFormat(String broadcastFormat) {
        this.broadcastFormat = broadcastFormat;
    }

    public boolean isDecreasedLagMode() {
        return this.decreasedLagMode;
    }

    public void setDecreasedLagMode(boolean decreasedLagMode) {
        this.decreasedLagMode = decreasedLagMode;
    }

    public void reloadServerData() {
        this.plugin.reloadConfig();
        this.config = this.plugin.getConfig();
        this.serverRules.clear();
        this.announcementDelay = this.config.getInt("announcements.delay", 180);
        this.announcements.clear();
        this.config.set("announcements.list", "&ePlease set shit in the config");
        for (String each : this.config.getStringList("announcements.list")) {
            this.announcements.add(ChatColor.translateAlternateColorCodes((char)'&', (String)each));
        }
        this.chatDisabledMillis = this.config.getLong("chat.disabled.millis", 0);
        this.chatSlowedMillis = this.config.getLong("chat.slowed.millis", 0);
        this.chatSlowedDelay = this.config.getInt("chat.slowed.delay", 15);
        this.useProtocolLib = this.config.getBoolean("use-protocol-lib", true);
        this.decreasedLagMode = this.config.getBoolean("decreased-lag-mode");
        this.broadcastFormat = ChatColor.translateAlternateColorCodes((char)'&', (String)this.config.getString("broadcast.format", (Object)ChatColor.YELLOW + "[" + (Object)ChatColor.GOLD + "*" + (Object)ChatColor.YELLOW + "]" + (Object)ChatColor.GRAY + " &7%1$s"));
    }

    public void saveServerData() {
        this.config.set("server-rules", this.serverRules);
        this.config.set("use-protocol-lib", (Object)this.useProtocolLib);
        this.config.set("chat.disabled.millis", (Object)this.chatDisabledMillis);
        this.config.set("chat.slowed.millis", (Object)this.chatSlowedMillis);
        this.config.set("chat.slowed-delay", (Object)this.chatSlowedDelay);
        this.config.set("announcements.delay", (Object)this.announcementDelay);
        this.config.set("announcements.list", this.announcements);
        this.config.set("decreased-lag-mode", (Object)this.decreasedLagMode);
        this.plugin.saveConfig();
    }
}

