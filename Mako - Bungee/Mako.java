package gg.mist;

import gg.mist.listeners.StaffActionListener;
import net.md_5.bungee.api.plugin.Plugin;

public class Mako extends Plugin
{

	public static Mako mako;
	
	public void onEnable() {
		mako = this;
        mako.getProxy().getPluginManager().registerListener(mako, new StaffActionListener());
	}
	
	public void onDisable() {
		
	}
	
	public static Mako getInstance() {
		return mako;
	}
}
