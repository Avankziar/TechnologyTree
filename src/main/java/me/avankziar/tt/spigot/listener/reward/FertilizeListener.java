package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class FertilizeListener implements Listener
{
	@EventHandler
	public void onStructurGrow(BlockFertilizeEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer() == null
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(EventType.FERTILIZING)
				|| !RewardHandler.canAccessInteraction(event.getPlayer(), EventType.FERTILIZING, event.getBlock().getType(), null))
		{
			return;
		}
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), EventType.FERTILIZING, event.getBlock().getType(), null, true))
		{
			Item it = event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), EventType.FERTILIZING, event.getBlock().getType(), null, 1);
	}
	
	//Do not needed
	//public void on(StructureGrowEvent|BlockGrowEvent event)
}