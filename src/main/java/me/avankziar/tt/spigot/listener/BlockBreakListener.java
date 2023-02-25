package main.java.me.avankziar.tt.spigot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;

public class BlockBreakListener
{
	private TT plugin = BaseConstructor.getPlugin();
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		
	}
}
