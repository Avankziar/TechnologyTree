package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class HarvestListener implements Listener
{
	final private static EventType HA = EventType.HARVEST;
	@EventHandler
	public void onHarvest(PlayerHarvestBlockEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(HA))
		{
			return;
		}
		final ToolType tool = ToolType.getHandToolType(event.getPlayer());
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), HA, tool, event.getHarvestedBlock().getType(), null))
		{
			Item it = event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), HA, tool, event.getHarvestedBlock().getType(), null, 1);
	}
}