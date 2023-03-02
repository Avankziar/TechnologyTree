package main.java.me.avankziar.tt.spigot.listener.recipe;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;

import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;

public class PrepareItemListener implements Listener
{	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPrepareCraft(PrepareItemCraftEvent event)
	{
		boolean canAccess = false;
		for(HumanEntity h : event.getViewers())
		{
			if(!(h instanceof Player))
			{
				continue;
			}
			Player player = (Player) h;
			if(RecipeHandler.hasAccessToRecipe(player.getUniqueId(), event.getRecipe()))
			{
				canAccess = true;
				break;
			}
		}
		if(!canAccess)
		{
			event.getInventory().setResult(null);
			for(HumanEntity h : event.getViewers())
			{
				if(!(h instanceof Player))
				{
					continue;
				}
				Player player = (Player) h;
				player.updateInventory();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPrepareCraft(PrepareSmithingEvent event)
	{
		boolean canAccess = false;
		for(HumanEntity h : event.getViewers())
		{
			if(!(h instanceof Player))
			{
				continue;
			}
			Player player = (Player) h;
			if(RecipeHandler.hasAccessToRecipe(player.getUniqueId(), event.getInventory().getRecipe()))
			{
				canAccess = true;
				break;
			}
		}
		if(!canAccess)
		{
			event.setResult(null);
			for(HumanEntity h : event.getViewers())
			{
				if(!(h instanceof Player))
				{
					continue;
				}
				Player player = (Player) h;
				player.updateInventory();
			}
		}
	}
}