package org.hcgames.icefyre.util.command;

import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.util.file.ConfigFile;

public abstract class BaseCommand {

    public Icefyre main = Icefyre.getInstance();
    public ConfigFile configFile = main.getConfigFile();
    public ConfigFile langConfig = main.getLangFile();

    public BaseCommand() {
        main.getCommandFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs command);

}
