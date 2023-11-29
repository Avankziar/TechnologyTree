package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
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
		if(!RewardHandler.canAccessInteraction(event.getPlayer(),
				ToolType.getToolType(event.getPlayer().getInventory().getItemInMainHand().getType()), BF, event.getBucket(), null))
		{
			event.setCancelled(true);
			return;
		}
		final Player player = event.getPlayer();
		final UUID uuid = player.getUniqueId();
		final Material bucket = event.getBucket();
		final ToolType tool = ToolType.getToolType(bucket);
		final Location loc = player.getLocation();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(ItemStack is : RewardHandler.getDrops(player, BE, tool, bucket, null))
				{
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							Item it = event.getPlayer().getWorld().dropItem(loc, is);
							ItemHandler.addItemToTask(it, uuid);
						}
					}.runTask(TT.getPlugin());
				}
				RewardHandler.rewardPlayer(uuid, BE, tool, bucket, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
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
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), BF, null, event.getBlockClicked().getType(), null))
				{
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							Item it = event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), is);
							ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
						}
					}.runTask(TT.getPlugin());
				}
				RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), BF, ToolType.HAND, event.getBlockClicked().getType(), null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());	
	}
}