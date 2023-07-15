package main.java.me.avankziar.tt.spigot.hook;

import java.util.UUID;

import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PAPIHook extends PlaceholderExpansion
{
	private TT plugin;
	
	public PAPIHook(TT plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean persist()
	{
		return true;
	}
	
	@Override
	public boolean canRegister()
	{
		return true;
	}
	
	@Override
	public String getAuthor()
	{
		return plugin.getDescription().getAuthors().toString();
	}
	
	@Override
	public String getIdentifier()
	{
		return "tt";
	}
	
	@Override
	public String getVersion()
	{
		return plugin.getDescription().getVersion();
	}
	
	/*
	 * tt_hastech,<Tech>
	 * 
	 */
	
	@Override
	public String onPlaceholderRequest(Player player, String idf)
	{
		if(player == null)
		{
			return "";
		}
		final UUID uuid = player.getUniqueId();
		switch(idf) //Für simple Placeholders
		{
		default:
			break;
		}
		if(idf.startsWith("hastech") && idf.split(",").length == 2)
		{
			String tech = idf.split(",")[1];
			
		}
		return null;
	}
}