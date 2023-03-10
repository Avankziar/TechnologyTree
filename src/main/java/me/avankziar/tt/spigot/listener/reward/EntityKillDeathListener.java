package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class EntityKillDeathListener implements Listener
{
	private static LinkedHashMap<UUID, LinkedHashMap<UUID, Double>> damageMap = new LinkedHashMap<>();
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Player)
				|| ((Player) event.getDamager()).getGameMode() == GameMode.CREATIVE
				|| ((Player) event.getDamager()).getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(EventType.HARMING))
		{
			return;
		}
		LinkedHashMap<UUID, Double> mapI = new LinkedHashMap<>();
		if(damageMap.containsKey(event.getEntity().getUniqueId()))
		{
			mapI = damageMap.get(event.getEntity().getUniqueId());
		}
		double dam = 0;
		if(mapI.containsKey(event.getDamager().getUniqueId()))
		{
			dam = mapI.get(event.getDamager().getUniqueId());
		}
		dam = dam + event.getDamage();
		mapI.put(event.getDamager().getUniqueId(), dam);
		damageMap.put(event.getEntity().getUniqueId(), mapI);
		for(ItemStack is : RewardHandler.getDrops((Player) event.getDamager(), EventType.HARMING, null, event.getEntityType(), true))
		{
			Item it = event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getDamager().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getDamager().getUniqueId(), EventType.HARMING, null, event.getEntityType(), 1);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		if(event.getEntity().getKiller() != null
				&& 
				(event.getEntity().getKiller().getGameMode() != GameMode.CREATIVE
				|| event.getEntity().getKiller().getGameMode() != GameMode.SPECTATOR
				|| EnumHandler.isEventActive(EventType.KILLING)))
		{
			if(damageMap.containsKey(event.getEntity().getUniqueId()))
			{
				double totaldamage = 0;
				for(double d : damageMap.get(event.getEntity().getUniqueId()).values())
				{
					totaldamage = totaldamage + d;
				}
				for(Entry<UUID, Double> entry : damageMap.get(event.getEntity().getUniqueId()).entrySet())
				{
					double percent = entry.getValue()/totaldamage;
					for(ItemStack is : RewardHandler.getDrops(event.getEntity().getKiller(), EventType.KILLING, null, EntityType.PLAYER, true))
					{
						Item it = event.getEntity().getWorld().dropItem(event.getEntity().getKiller().getLocation(), is);
						ItemHandler.addItemToTask(it, entry.getKey());
					}
					RewardHandler.rewardPlayer(event.getEntity().getKiller().getUniqueId(), EventType.KILLING, null, EntityType.PLAYER, percent);
				}
			} else
			{
				for(ItemStack is : RewardHandler.getDrops(event.getEntity().getKiller(), EventType.KILLING, null, EntityType.PLAYER, true))
				{
					Item it = event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), is);
					ItemHandler.addItemToTask(it, event.getEntity().getKiller().getUniqueId());
				}
				RewardHandler.rewardPlayer(event.getEntity().getKiller().getUniqueId(), EventType.KILLING, null, EntityType.PLAYER, 1);
			}			
		}
		if(event.getEntity().getGameMode() == GameMode.CREATIVE
				|| event.getEntity().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(EventType.DYING))
		{
			return;
		}
		for(ItemStack is : RewardHandler.getDrops(event.getEntity(), EventType.DYING, null, EntityType.PLAYER, true))
		{
			Item it = event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getEntity().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getEntity().getUniqueId(), EventType.DYING, null, EntityType.PLAYER, 1);
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event)
	{
		if(event.getEntity().getKiller() == null
				|| event.getEntity().getKiller().getGameMode() == GameMode.CREATIVE
				|| event.getEntity().getKiller().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(EventType.KILLING))
		{
			return;
		}
		for(ItemStack is : RewardHandler.getDrops(event.getEntity().getKiller(), EventType.KILLING, null, event.getEntityType(), true))
		{
			Item it = event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getEntity().getKiller().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getEntity().getKiller().getUniqueId(), EventType.KILLING, null, event.getEntityType(), 1);
	}
}