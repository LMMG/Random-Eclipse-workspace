package server.wenjapvp;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.jni.cipher.BungeeCipher;

public class Mako extends Plugin implements Listener
{

	private static Mako instance;

	public void onEnable()
	{
		instance = this;
		
		System.out.println("WORKS");

		getProxy().getPluginManager().registerListener(this, this);
	}

	public void onDisable()
	{

	}

	public static Mako getInstance()
	{
		return instance;
	}


	/**
	 * 
	 * SPOOF
	 * 
	 * @param e
	 */
	
	
	@EventHandler
	public void onProxy(ProxyPingEvent e)
	{
		ServerPing ping = e.getResponse();
		ServerPing.Players player = ping.getPlayers();
		double fakers = getProxy().getOnlineCount() * 2;
		int RoundedUp = (int) Math.ceil(fakers);
		
		ping.setPlayers(new ServerPing.Players(player.getMax(), RoundedUp, player.getSample()));
		e.setResponse(ping);
	}
}
