package main.java.me.avankziar.tt.spigot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;

public class JoinQuitListener implements Listener
{	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		PlayerHandler.joinPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event)
	{
		PlayerHandler.quitPlayer(event.getPlayer().getUniqueId());
	}
}