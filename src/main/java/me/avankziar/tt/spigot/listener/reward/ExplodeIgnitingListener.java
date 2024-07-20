package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.BlockHandler;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class ExplodeIgnitingListener implements Listener
{
	private static LinkedHashMap<UUID, String> ignitingMap = new LinkedHashMap<>();
	final private static EventType IG = EventType.IGNITING;
	final private static EventType EX = EventType.EXPLODING;
	
	//Do not tracks TNT
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer() == null
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(IG))
		{
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "IGNITING:RETURN",
					IG, ToolType.HAND, null, null, Material.AIR);
			return;
		}
		if(!RewardHandler.canAccessInteraction(event.getPlayer(),
				ToolType.getToolType(event.getPlayer().getInventory().getItemInMainHand().getType()), IG, event.getBlock().getType(), null))
		{
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "IGNITING:CANNOTACCESS",
					IG, ToolType.HAND, null, null, Material.AIR);
			event.setCancelled(true);
			return;
		}
		ignitingMap.put(event.getPlayer().getUniqueId(), BlockHandler.getLocationText(event.getBlock().getLocation()));
		final Player player = event.getPlayer();
		final Location loc = event.getBlock().getLocation();
		final Material mat = event.getBlock().getType();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(player, "IGNITING:REWARD",
						IG, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(player, IG, ToolType.HAND, mat, null))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), IG, ToolType.HAND, mat, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
	
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent event)
	{
		if(event.isCancelled()
				|| !EnumHandler.isEventActive(EX)
				|| !ignitingMap.containsValue(BlockHandler.getLocationText(event.getBlock().getLocation())))
		{
			return;
		}
		UUID uuid = null;
		for(Entry<UUID, String> entry : ignitingMap.entrySet())
		{
			if(entry.getValue().equals(BlockHandler.getLocationText(event.getBlock().getLocation())))
			{
				continue;
			}
			uuid = entry.getKey();
			break;
		}
		if(uuid == null)
		{
			return;
		}
		ignitingMap.remove(uuid);
		if(!ConfigHandler.GAMERULE_UseVanillaExpDrops)
		{
			event.blockList().clear();
		}
		final Player player = Bukkit.getPlayer(uuid);
		final Location loc = event.getBlock().getLocation();
		final Material mat = event.getBlock().getType();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(player == null)
				{
					return;
				}
				ARGCheckEventAction.checkEventAction(player, "EXPLODING:REWARD",
						EX, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(player, EX, ToolType.HAND, mat, null))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), EX, ToolType.HAND, mat, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
		
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event)
	{
		if(event.isCancelled()
				|| !EnumHandler.isEventActive(EX)
				|| event.getEntityType() != EntityType.TNT)
		{
			return;
		}
		UUID uuid = null;
		double d = 999_999_999_999.0;
		for(Entry<UUID, String> entry : ignitingMap.entrySet())
		{
			Location l = BlockHandler.getLocation(entry.getValue());
			if(!event.getLocation().getWorld().getName().equals(l.getWorld().getName()))
			{
				continue;
			}
			double xyz = Math.max(event.getLocation().getBlockX(), l.getBlockX()) - Math.min(event.getLocation().getBlockX(), l.getBlockX())
					+ Math.max(event.getLocation().getBlockY(), l.getBlockY()) - Math.min(event.getLocation().getBlockY(), l.getBlockY())
					+ Math.max(event.getLocation().getBlockZ(), l.getBlockZ()) - Math.min(event.getLocation().getBlockZ(), l.getBlockZ());
			if(xyz < 10 && xyz < d)
			{
				d = xyz;
				uuid = entry.getKey();
			}
		}
		if(uuid == null)
		{
			return;
		}
		ignitingMap.remove(uuid);
		final Player player = Bukkit.getPlayer(uuid);
		for(Block b : event.blockList())
		{
			final Material mat = b.getType();
			final Location loc = b.getLocation();
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					if(player == null)
					{
						return;
					}
					ARGCheckEventAction.checkEventAction(player, "EXPLODING:REWARD",
							EX, ToolType.HAND, null, null, Material.AIR);
					for(ItemStack is : RewardHandler.getDrops(player, EX, ToolType.HAND, mat, null))
					{
						ItemHandler.dropItem(is, player, loc);
					}			
					RewardHandler.rewardPlayer(player.getUniqueId(), EX, ToolType.HAND, mat, null, 1);
				}
			}.runTaskAsynchronously(TT.getPlugin());
		}
	}
}