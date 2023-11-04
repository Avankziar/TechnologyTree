package main.java.me.avankziar.tt.spigot.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import main.java.me.avankziar.tt.spigot.objects.sqllite.PlacedBlock;

public class BlockFormListener implements Listener
{	
	@EventHandler
	public void onForm(BlockFormEvent event)
	{
		final Location l = event.getBlock().getLocation();
		if(PlacedBlock.wasPlaced(l))
		{
			PlacedBlock.delete(l);
		}
	}
}