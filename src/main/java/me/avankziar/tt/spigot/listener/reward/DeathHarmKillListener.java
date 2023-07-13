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

public class DeathHarmKillListener implements Listener
{
	private static LinkedHashMap<UUID, LinkedHashMap<UUID, Double>> damageMap = new LinkedHashMap<>();
	final private static EventType HA = EventType.HARMING;
	final private static EventType KI = EventType.KILLING;
	final private static EventType DY = EventType.DYING;
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Player)
				|| ((Player) event.getDamager()).getGameMode() == GameMode.CREATIVE
				|| ((Player) event.getDamager()).getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(HA))
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
		for(ItemStack is : RewardHandler.getDrops((Player) event.getDamager(), HA, null, event.getEntityType(), true))
		{
			Item it = event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getDamager().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getDamager().getUniqueId(), HA, null, event.getEntityType(), 1);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		if(event.getEntity().getKiller() != null
				&& 
				(event.getEntity().getKiller().getGameMode() != GameMode.CREATIVE
				|| event.getEntity().getKiller().getGameMode() != GameMode.SPECTATOR
				|| EnumHandler.isEventActive(KI)))
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
					for(ItemStack is : RewardHandler.getDrops(event.getEntity().getKiller(), KI, null, EntityType.PLAYER, true))
					{
						Item it = event.getEntity().getWorld().dropItem(event.getEntity().getKiller().getLocation(), is);
						ItemHandler.addItemToTask(it, entry.getKey());
					}
					RewardHandler.rewardPlayer(event.getEntity().getKiller().getUniqueId(), KI, null, EntityType.PLAYER, percent);
				}
			} else
			{
				for(ItemStack is : RewardHandler.getDrops(event.getEntity().getKiller(), KI, null, EntityType.PLAYER, true))
				{
					Item it = event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), is);
					ItemHandler.addItemToTask(it, event.getEntity().getKiller().getUniqueId());
				}
				RewardHandler.rewardPlayer(event.getEntity().getKiller().getUniqueId(), KI, null, EntityType.PLAYER, 1);
			}			
		}
		if(event.getEntity().getGameMode() == GameMode.CREATIVE
				|| event.getEntity().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(DY))
		{
			return;
		}
		for(ItemStack is : RewardHandler.getDrops(event.getEntity(), DY, null, EntityType.PLAYER, true))
		{
			Item it = event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getEntity().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getEntity().getUniqueId(), DY, null, EntityType.PLAYER, 1);
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event)
	{
		if(event.getEntity().getKiller() == null
				|| event.getEntity().getKiller().getGameMode() == GameMode.CREATIVE
				|| event.getEntity().getKiller().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(KI))
		{
			return;
		}
		for(ItemStack is : RewardHandler.getDrops(event.getEntity().getKiller(), KI, null, event.getEntityType(), true))
		{
			Item it = event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getEntity().getKiller().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getEntity().getKiller().getUniqueId(), KI, null, event.getEntityType(), 1);
	}
}