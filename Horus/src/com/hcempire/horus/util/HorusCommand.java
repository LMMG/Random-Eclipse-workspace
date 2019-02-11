package com.hcempire.horus.util;

import com.alexandeh.ekko.Ekko;
import com.alexandeh.ekko.files.ConfigFile;
import com.alexandeh.ekko.utils.command.CommandArgs;
import com.hcempire.horus.Horus;

public abstract class HorusCommand {

    public Horus main = Horus.getInstance();
    public ConfigFile configFile = main.getConfigFile();
    public ConfigFile langFile = main.getLangFile();
    public ConfigFile scoreboardFile = main.getScoreboardFile();

    public HorusCommand() {
        Ekko.getInstance().getFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs command);

}
