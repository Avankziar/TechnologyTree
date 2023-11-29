package main.java.me.avankziar.tt.spigot.listener.recipe;

import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class PrepareItemCraftListener implements Listener
{	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPrepareCraft(CraftItemEvent event)
	{
		boolean canAccess = true;
		if(!EnumHandler.isEventActive(EventType.CRAFTING))
		{
			return;
		}
		for(HumanEntity h : event.getViewers())
		{
			if(!(h instanceof Player))
			{
				continue;
			}
			Player player = (Player) h;
			if(player.getGameMode() == GameMode.CREATIVE
					|| player.getGameMode() == GameMode.SPECTATOR
					|| ConfigHandler.GAMERULE_UseVanillaAccessToCrafingTable)
			{
				ARGCheckEventAction.checkEventAction(player, "CRAFTING:RETURN",
						EventType.CRAFTING, ToolType.HAND, null, null, event.getRecipe().getResult().getType());
				break;
			}
			if(!RecipeHandler.hasAccessToRecipe(player.getUniqueId(), event.getRecipe()))
			{
				ARGCheckEventAction.checkEventAction(player, "CRAFTING:CANNOTACCESS",
						EventType.CRAFTING, ToolType.HAND, null, null, event.getRecipe().getResult().getType());
				canAccess = false;
				break;
			}
		}
		if(!canAccess)
		{
			event.setCancelled(true);
			event.setResult(Result.DENY);
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
}