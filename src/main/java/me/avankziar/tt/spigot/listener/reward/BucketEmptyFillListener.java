package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
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
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "BUCKET_EMPTYING:RETURN",
					BE, ToolType.HAND, null, null, Material.AIR);
			return;
		}
		if(!RewardHandler.canAccessInteraction(event.getPlayer(),
				ToolType.HAND, BE, event.getBucket(), null))
		{
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "BUCKET_EMPTYING:CANNOTACCESS",
					BE, ToolType.HAND, null, null, Material.AIR);
			event.setCancelled(true);
			return;
		}
		final Player player = event.getPlayer();
		final UUID uuid = player.getUniqueId();
		final Material bucket = event.getBucket();
		final Location loc = player.getLocation();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(player, "BUCKET_EMPTYING:REWARD",
						BE, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(player, BE, ToolType.HAND, bucket, null))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(uuid, BE, ToolType.HAND, bucket, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
	
	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent event)
	{
		
		if(event.isCancelled()
				|| event.getItemStack() == null
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(BF))
		{
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "BUCKET_FILLING:RETURN",
					BF, ToolType.HAND, event.getBucket(), null, Material.AIR);
			return;
		}
		if(!RewardHandler.canAccessInteraction(event.getPlayer(),
				ToolType.HAND, BF, event.getItemStack().getType(), null))
		{
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "BUCKET_FILLING:CANNOTACCESS",
					BF, ToolType.HAND, event.getItemStack().getType(), null, Material.AIR);
			event.setCancelled(true);
			return;
		}
		final Player player = event.getPlayer();
		final Location loc = event.getBlockClicked().getLocation();
		final Material bucket = event.getItemStack().getType();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(player, "BUCKET_FILLING:REWARD",
						BF, ToolType.HAND, bucket, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(player, BF, null, bucket, null))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), BF, ToolType.HAND, bucket, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());	
	}
}