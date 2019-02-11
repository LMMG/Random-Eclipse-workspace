package me.vertises.aztec.tablist.example;

import org.bukkit.plugin.java.*;
import java.util.concurrent.*;
import me.vertises.aztec.tablist.*;

public class TablistPlugin extends JavaPlugin
{
    public void onEnable() {
        new TablistManager(this, new ExampleSupplier(), TimeUnit.SECONDS.toMillis(5L));
    }
}
