package main.java.me.avankziar.tt.spigot.listener.recipe;

import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class PrepareGrindstoneListener implements Listener
{
	@EventHandler
	public void onPrepareGrindstone(PrepareGrindstoneEvent event)
	{
		if(!EnumHandler.isEventActive(EventType.GRINDING))
		{
			return;
		}
		TT.log.info("PrepareGrindstone Start"); //REMOVEME
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
			if(!RecipeHandler.hasAccessToRecipe(player.getUniqueId(), RecipeType.GRINDING, event.getResult().getType().toString()))
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