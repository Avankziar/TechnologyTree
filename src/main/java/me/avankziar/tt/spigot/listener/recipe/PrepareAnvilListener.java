package main.java.me.avankziar.tt.spigot.listener.recipe;

import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;

import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class PrepareAnvilListener implements Listener
{
	@EventHandler
	public void onPrepareAnvil(PrepareAnvilEvent event)
	{
		if(!EnumHandler.isEventActive(EventType.COLD_FORGING))
		{
			return;
		}
		boolean returning = false;
		for(HumanEntity he : event.getViewers())
		{
			Player player = (Player) he;
			if(player.getGameMode() == GameMode.CREATIVE
					|| player.getGameMode() == GameMode.SPECTATOR)
			{
				returning = true;
				break;
			}
			if(!RecipeHandler.hasAccessToRecipe(player.getUniqueId(), RecipeType.ANVIL, event.getResult().getType().toString()))
			{
				break;
			}
		}
		if(returning)
		{
			return;
		}
		event.setResult(null);
		for(HumanEntity he : event.getViewers())
		{
			Player player = (Player) he;
			player.updateInventory();
		}
	}
}