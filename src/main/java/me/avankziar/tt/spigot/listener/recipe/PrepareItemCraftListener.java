package main.java.me.avankziar.tt.spigot.listener.recipe;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class PrepareItemCraftListener implements Listener
{	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPrepareCraft(PrepareItemCraftEvent event)
	{
		boolean canAccess = true;
		if(!EnumHandler.isEventActive(EventType.CRAFTING)
				|| (event.getInventory().getType() != InventoryType.WORKBENCH && event.getInventory().getType() != InventoryType.CRAFTING))
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
					|| player.getGameMode() == GameMode.SPECTATOR)
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
			event.getInventory().setResult(new ItemStack(Material.AIR));
		}
	}
}