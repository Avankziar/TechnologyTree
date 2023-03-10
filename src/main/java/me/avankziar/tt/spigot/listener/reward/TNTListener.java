package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.BlockHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class TNTListener implements Listener
{
	private static LinkedHashMap<UUID, String> ignitingMap = new LinkedHashMap<>();
	
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR)
		{
			return;
		}
		if(!EnumHandler.isEventActive(EventType.IGNITING))
		{
			return;
		}
		if(!RewardHandler.canAccessInteraction(event.getPlayer(), EventType.IGNITING, event.getBlock().getType(), null))
		{
			return;
		}
		ignitingMap.put(event.getPlayer().getUniqueId(), BlockHandler.getLocationText(event.getBlock().getLocation()));
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), EventType.EXPLODING, event.getBlock().getType(), null, false))
		{
			Item it = event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), EventType.EXPLODING, event.getBlock().getType(), null, 1);
	}
	
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(!EnumHandler.isEventActive(EventType.EXPLODING))
		{
			return;
		}
		if(!ignitingMap.containsValue(BlockHandler.getLocationText(event.getBlock().getLocation())))
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
		Player player = Bukkit.getPlayer(uuid);
		if(player != null)
		{
			for(ItemStack is : RewardHandler.getDrops(player, EventType.EXPLODING, event.getBlock().getType(), null, false))
			{
				Item it = player.getWorld().dropItem(event.getBlock().getLocation(), is);
				ItemHandler.addItemToTask(it, player.getUniqueId());
			}
		}
		RewardHandler.rewardPlayer(uuid, EventType.EXPLODING, event.getBlock().getType(), null, 1);
	}
		
	
	@EventHandler
	public void onBlockExplode(EntityExplodeEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(!EnumHandler.isEventActive(EventType.EXPLODING))
		{
			return;
		}
		if(event.getEntityType() != EntityType.PRIMED_TNT)
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
		Player player = Bukkit.getPlayer(uuid);
		for(Block b : event.blockList())
		{
			if(player != null)
			{
				for(ItemStack is : RewardHandler.getDrops(player, EventType.EXPLODING, b.getType(), null, false))
				{
					Item it = player.getWorld().dropItem(b.getLocation(), is);
					ItemHandler.addItemToTask(it, player.getUniqueId());
				}
			}			
			RewardHandler.rewardPlayer(uuid, EventType.EXPLODING, b.getType(), null, 1);
		}
	}
}