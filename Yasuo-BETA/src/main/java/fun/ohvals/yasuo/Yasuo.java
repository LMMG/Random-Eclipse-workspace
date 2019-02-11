package fun.ohvals.yasuo;

import fun.ohvals.yasuo.commands.*;
import fun.ohvals.yasuo.listeners.ProfileCheckListener;
import fun.ohvals.yasuo.listeners.StaffActionListener;
import fun.ohvals.yasuo.listeners.StaffChatListener;
import fun.ohvals.yasuo.listeners.TabCompleteListener;
import fun.ohvals.yasuo.profile.ProfileManager;
import fun.ohvals.yasuo.util.AnsiUtils;
import fun.ohvals.yasuo.util.Config;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Yasuo was created by dylan on 06/2017.
 * Please do not redistribute without permission of the developer.
 */

public class Yasuo extends Plugin {

    private static Yasuo yasuo;
    private Config langConfig, mainConfig;
    private ProfileManager profileManager;

    
    public void onEnable() {
        yasuo = this;

        langConfig = new Config("lang");
        mainConfig = new Config("config");
        profileManager = new ProfileManager();
        registerCommands();
        registerListeners();
        profileManager.loadAllProfiles();
        yasuo.getProxy().getScheduler().schedule(yasuo, new Runnable() {
            
            public void run() {
                profileManager.saveAllProfiles();
            }
        }, 0, 300, TimeUnit.SECONDS);
    }

    
    public void onDisable() {
        profileManager.saveAllProfiles();
        yasuo = null;
    }

    private void registerListeners() {
        yasuo.getProxy().getPluginManager().registerListener(yasuo, new StaffChatListener());
        yasuo.getProxy().getPluginManager().registerListener(yasuo, new ProfileCheckListener());
        yasuo.getProxy().getPluginManager().registerListener(yasuo, new StaffActionListener());
        yasuo.getProxy().getPluginManager().registerListener(yasuo, new TabCompleteListener());
    }

    private void registerCommands() {
        yasuo.getProxy().getPluginManager().registerCommand(yasuo, new StaffChatCommand());
        yasuo.getProxy().getPluginManager().registerCommand(yasuo, new ReportCommand());
        yasuo.getProxy().getPluginManager().registerCommand(yasuo, new RequestCommand());
        yasuo.getProxy().getPluginManager().registerCommand(yasuo, new ToggleReportsCommand());
        yasuo.getProxy().getPluginManager().registerCommand(yasuo, new ToggleRequestsCommand());
        yasuo.getProxy().getPluginManager().registerCommand(yasuo, new ToggleStaffChatCommand());
    }

    public static Yasuo getYasuo() {
        return yasuo;
    }

    public Config getLangConfig() {
        return langConfig;
    }

    public Config getMainConfig() {
        return mainConfig;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }
}
