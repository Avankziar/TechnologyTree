package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class BucketEmptyFillListener implements Listener
{
	final private static EventType BE = EventType.BUCKET_EMPTYING;
	final private static EventType BF = EventType.BUCKET_FILLING;
	
	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(BE))
		{
			return;
		}
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), BE, event.getBucket(), null, true))
		{
			Item it = event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), BE,	event.getBucket(), null, 1);
	}
	
	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer() == null
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(BF))
		{
			return;
		}
		if(!RewardHandler.canAccessInteraction(event.getPlayer(),
				ToolType.getToolType(event.getPlayer().getInventory().getItemInMainHand().getType()), BF, event.getBucket(), null))
		{
			event.setCancelled(true);
			return;
		}
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), BF, event.getBlockClicked().getType(), null, true))
		{
			Item it = event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), BF,	event.getBlockClicked().getType(), null, 1);
	}
}