package net.psychz.gga.bungee;

import java.io.File;
import net.md_5.bungee.api.plugin.Plugin;
import net.psychz.gga.GGA;

public class BungeeSide extends Plugin {

    @Override
    public void onEnable() {

        if (!GGA.init(getLogger(), new File("gga.conf"), getDescription().getVersion())) {
            // TODO disable plugin (I don't know how to that yet, I have to google it)
            return;
        }

        getProxy().getPluginManager().registerListener(this, new HandshakeListener());

    }

}
