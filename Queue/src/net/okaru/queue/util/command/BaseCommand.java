
package net.okaru.queue.util.command;

import net.okaru.queue.oQueue;
import net.okaru.queue.ConfigFile;

public abstract class BaseCommand {
    public oQueue main;
    public ConfigFile configFile;

    public BaseCommand(oQueue main) {
        this.main = main;
        this.main.getCommandFramework().registerCommands(this);
        this.configFile = main.getConfigFile();
    }

    public abstract void onCommand(CommandArgs var1);
}

