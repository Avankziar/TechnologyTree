package main.java.me.avankziar.tt.spigot.listener.recipe;

import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;

import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class PrepareGrindstoneListener implements Listener
{
	@EventHandler
	public void onPrepareGrindstone(PrepareGrindstoneEvent event)
	{
		if(!EnumHandler.isEventActive(EventType.GRINDING))
		{
			return;
		}
		boolean returning = true;
		for(HumanEntity he : event.getViewers())
		{
			Player player = (Player) he;
			if(player.getGameMode() == GameMode.CREATIVE
					|| player.getGameMode() == GameMode.SPECTATOR
					|| ConfigHandler.GAMERULE_UseVanillaAccessToGrindstone)
			{
				ARGCheckEventAction.checkEventAction(player, "GRINDING:RETURN",
						EventType.GRINDING, ToolType.HAND, null, null, event.getResult().getType());
				break;
			}
			if(!RecipeHandler.hasAccessToRecipe(player.getUniqueId(), RecipeType.GRINDING, event.getResult().getType().toString()))
			{
				ARGCheckEventAction.checkEventAction(player, "GRINDING:CANNOTACCESS",
						EventType.GRINDING, ToolType.HAND, null, null, event.getResult().getType());
				returning = false;
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