package main.java.me.avankziar.tt.spigot.gui.listener;

import main.java.me.avankziar.tt.spigot.TT;

public class FunctionHandler
{
	private TT plugin;
	
	public FunctionHandler(TT plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * default function handler
	 */
	public void bottomFunction1()
	{
		plugin.getLogger().info("");
		return;
	}
	
	public void upperFunction1()
	{
		plugin.getLogger().info("");
		return;
	}
}