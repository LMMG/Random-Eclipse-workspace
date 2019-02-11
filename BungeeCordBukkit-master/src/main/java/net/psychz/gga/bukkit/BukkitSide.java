package net.psychz.gga.bukkit;

import com.comphenix.protocol.ProtocolLibrary;
import net.psychz.gga.GGA;
import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitSide extends JavaPlugin {

    @Override
    public void onEnable() {

        ProtocolLibPacketAdapter adapter = new ProtocolLibPacketAdapter(this);
        ProtocolLibrary.getProtocolManager().addPacketListener(adapter);

        if (!GGA.init(getLogger(), new File("gga.conf"), getDescription().getVersion())) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

    }

}
