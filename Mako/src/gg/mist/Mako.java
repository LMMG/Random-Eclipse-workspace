package gg.mist;

import gg.mist.listener.StaffActionListener;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class Mako extends Plugin
{

	public static Mako mako;
	
	public void onEnable() {
		mako = this;
		BungeeCord.getInstance().getPluginManager().registerListener(this, new StaffActionListener());	}
	
	public void onDisable() {
		
	}
	
	public static Mako getInstance() {
		return mako;
	}
}
