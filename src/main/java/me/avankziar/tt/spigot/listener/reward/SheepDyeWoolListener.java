package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class SheepDyeWoolListener implements Listener
{
	@EventHandler
	public void onSheepDyeWool(SheepDyeWoolEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer() == null
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(EventType.SHEEP_COLORING))
		{
			return;
		}
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), EventType.SHEEP_COLORING, null, event.getEntityType(), false))
		{
			Item it = event.getPlayer().getWorld().dropItem(event.getEntity().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), EventType.SHEEP_COLORING, null, event.getEntityType(), 1);
	}
}