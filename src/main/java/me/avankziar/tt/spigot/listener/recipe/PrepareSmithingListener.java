package main.java.me.avankziar.tt.spigot.listener.recipe;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class PrepareSmithingListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPrepareCraft(PrepareSmithingEvent event)
	{
		boolean canAccess = false;
		if(!EnumHandler.isEventActive(EventType.PREPARE_SMITHING))
		{
			return;
		}
		TT.log.info("PrepareSmithing Start"); //REMOVEME
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