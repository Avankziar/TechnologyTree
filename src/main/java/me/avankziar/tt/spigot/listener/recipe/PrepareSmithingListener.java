package main.java.me.avankziar.tt.spigot.listener.recipe;

import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;

import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class PrepareSmithingListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPrepareCraft(PrepareSmithingEvent event)
	{
		boolean canAccess = true;
		if(!EnumHandler.isEventActive(EventType.SMITHING)
				|| event.getResult() == null)
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
					|| ConfigHandler.GAMERULE_UseVanillaAccessToSmithingTable)
			{
				ARGCheckEventAction.checkEventAction(player, "SMITHING:RETURN",
						EventType.CRAFTING, ToolType.HAND, null, null, event.getResult().getType());
				break;
			}
			if(!RecipeHandler.hasAccessToRecipe(player.getUniqueId(), event.getInventory().getRecipe()))
			{
				ARGCheckEventAction.checkEventAction(player, "SMITHING:CANNOTACCESS",
						EventType.CRAFTING, ToolType.HAND, null, null, event.getResult().getType());
				canAccess = false;
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