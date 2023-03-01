package main.java.me.avankziar.tt.spigot.listener.recipe;

import org.bukkit.event.inventory.PrepareSmithingEvent;

public class PrepareSmithingListener
{

	public PrepareSmithingListener()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void onPrepareSmithing(PrepareSmithingEvent event)
	{
		event.getInventory().getRecipe();
		event.setResult(null);
	}
}