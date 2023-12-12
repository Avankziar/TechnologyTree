package main.java.me.avankziar.tt.spigot.handler.ifh;

import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.spigot.TT;

public class PlayerTimesHandler
{
	public static boolean isAfk(Player player)
	{
		if(TT.getPlugin().getPlayerTimes() == null)
		{
			return false;
		}
		return !TT.getPlugin().getPlayerTimes().isActive(player.getUniqueId());
	}
}