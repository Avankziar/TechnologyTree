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
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class FertilizeListener implements Listener
{
	final private static EventType FE = EventType.FERTILIZING;
	@EventHandler
	public void onStructurGrow(BlockFertilizeEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer() == null
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(FE)
				|| !RewardHandler.canAccessInteraction(event.getPlayer(),
						ToolType.getToolType(event.getPlayer().getInventory().getItemInMainHand().getType()), FE, event.getBlock().getType(), null))
		{
			return;
		}
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), FE, event.getBlock().getType(), null, true))
		{
			Item it = event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), FE, event.getBlock().getType(), null, 1);
	}
	
	//Do not needed
	//public void on(StructureGrowEvent|BlockGrowEvent event)
}