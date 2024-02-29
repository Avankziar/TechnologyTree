package main.java.me.avankziar.tt.spigot.listener.recipe;

import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;

import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class PrepareAnvilListener implements Listener
{
	@EventHandler
	public void onPrepareAnvil(PrepareAnvilEvent event)
	{
		if(!EnumHandler.isEventActive(EventType.COLD_FORGING)
				|| event.getResult() == null)
		{
			return;
		}
		boolean returning = true;
		for(HumanEntity he : event.getViewers())
		{
			Player player = (Player) he;
			if(player.getGameMode() == GameMode.CREATIVE
					|| player.getGameMode() == GameMode.SPECTATOR)
			{
				ARGCheckEventAction.checkEventAction(player, "COLD_FORGING:RETURN",
						EventType.COLD_FORGING, ToolType.HAND, null, null, event.getResult().getType());
				break;
			}
			if(!RecipeHandler.hasAccessToRecipe(player.getUniqueId(), RecipeType.ANVIL, event.getResult().getType().toString()))
			{
				ARGCheckEventAction.checkEventAction(player, "COLD_FORGING:CANNOTACCESS",
						EventType.COLD_FORGING, ToolType.HAND, null, null, event.getResult().getType());
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