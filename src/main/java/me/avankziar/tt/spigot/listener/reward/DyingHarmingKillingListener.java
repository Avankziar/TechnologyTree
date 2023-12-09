package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class DyingHarmingKillingListener implements Listener
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
		final ToolType tool = ToolType.getHandToolType((Player) event.getDamager());
		final Location loc = event.getEntity().getLocation();
		final Player damager = (Player) event.getDamager();
		final EntityType ent = event.getEntityType();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(damager, "HARMING:REWARD",
						HA, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(damager, HA, tool, null, ent))
				{
					ItemHandler.dropItem(is, damager, loc);
				}
				RewardHandler.rewardPlayer(event.getDamager().getUniqueId(), HA, tool, null, ent, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		if(event.getEntity().getKiller() != null
				|| event.getEntity().getKiller().getGameMode() != GameMode.CREATIVE
				|| event.getEntity().getKiller().getGameMode() != GameMode.SPECTATOR
				|| EnumHandler.isEventActive(KI))
		{
			if(damageMap.containsKey(event.getEntity().getUniqueId()))
			{
				double totaldamage = 0;
				for(double d : damageMap.get(event.getEntity().getUniqueId()).values())
				{
					totaldamage = totaldamage + d;
				}
				final double td = totaldamage;
				final UUID uuid = event.getEntity().getUniqueId();
				final Player killer = event.getEntity().getKiller();
				final ToolType tool = ToolType.getHandToolType(killer);
				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						for(Entry<UUID, Double> entry : damageMap.get(uuid).entrySet())
						{
							double percent = entry.getValue()/td;
							if(Bukkit.getPlayer(entry.getKey()) != null)
							{
								ARGCheckEventAction.checkEventAction(Bukkit.getPlayer(entry.getKey()), "KILLING:REWARD",
										KI, ToolType.HAND, null, null, Material.AIR);
								for(ItemStack is : RewardHandler.getDrops(Bukkit.getPlayer(entry.getKey()), KI, tool, null, EntityType.PLAYER))
								{
									Player player = Bukkit.getPlayer(entry.getKey());
									ItemHandler.dropItem(is, player, null);
								}
							}
							RewardHandler.rewardPlayer(entry.getKey(), KI, tool, null, EntityType.PLAYER, percent);
						}
					}
				}.runTaskAsynchronously(TT.getPlugin());
			} else
			{
				final ToolType tool = ToolType.getHandToolType(event.getEntity().getKiller());
				final Player player = event.getEntity().getKiller();
				final Location loc = event.getEntity().getLastDeathLocation();
				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						ARGCheckEventAction.checkEventAction(player, "KILLING:REWARD",
								KI, ToolType.HAND, null, null, Material.AIR);
						for(ItemStack is : RewardHandler.getDrops(player, KI, tool, null, EntityType.PLAYER))
						{
							ItemHandler.dropItem(is, player, loc);
						}
						RewardHandler.rewardPlayer(player.getUniqueId(), KI, tool, null, EntityType.PLAYER, 1);
					}
				}.runTaskAsynchronously(TT.getPlugin());
			}			
		}
		if(event.getEntity().getGameMode() == GameMode.CREATIVE
				|| event.getEntity().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(DY))
		{
			ARGCheckEventAction.checkEventAction(event.getEntity(), "DYING:RETURN",
					HA, ToolType.HAND, null, null, Material.AIR);
			return;
		}
		final Player player = event.getEntity();
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(player, "DYING:REWARD",
						DY, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(event.getEntity(), DY, ToolType.HAND, null, EntityType.PLAYER))
				{
					ItemHandler.dropItem(is, player, null);
				}
				RewardHandler.rewardPlayer(event.getEntity().getUniqueId(), DY, ToolType.HAND, null, EntityType.PLAYER, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
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
		if(damageMap.containsKey(event.getEntity().getUniqueId()))
		{
			double totaldamage = 0;
			for(double d : damageMap.get(event.getEntity().getUniqueId()).values())
			{
				totaldamage = totaldamage + d;
			}
			final double td = totaldamage;
			final UUID uuid = event.getEntity().getUniqueId();
			final Player killer = event.getEntity().getKiller();
			final ToolType tool = ToolType.getHandToolType(killer);
			final EntityType ent = event.getEntityType();
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					for(Entry<UUID, Double> entry : damageMap.get(uuid).entrySet())
					{
						double percent = entry.getValue()/td;
						if(Bukkit.getPlayer(entry.getKey()) != null)
						{
							ARGCheckEventAction.checkEventAction(Bukkit.getPlayer(entry.getKey()), "KILLING:REWARD",
									KI, ToolType.HAND, null, null, Material.AIR);
							for(ItemStack is : RewardHandler.getDrops(Bukkit.getPlayer(entry.getKey()), KI, tool, null, ent))
							{
								Player player = Bukkit.getPlayer(entry.getKey());
								ItemHandler.dropItem(is, player, null);
							}
						}
						RewardHandler.rewardPlayer(entry.getKey(), KI, tool, null, ent, percent);
					}
				}
			}.runTaskAsynchronously(TT.getPlugin());
		} else
		{
			final ToolType tool = ToolType.getHandToolType(event.getEntity().getKiller());
			final Player player = event.getEntity().getKiller();
			final Location loc = event.getEntity().getLocation();
			final EntityType ent = event.getEntityType();
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					ARGCheckEventAction.checkEventAction(player, "KILLING:REWARD",
							KI, ToolType.HAND, null, null, Material.AIR);
					for(ItemStack is : RewardHandler.getDrops(player, KI, tool, null, ent))
					{
						ItemHandler.dropItem(is, player, loc);
					}
					RewardHandler.rewardPlayer(player.getUniqueId(), KI, tool, null, ent, 1);
				}
			}.runTaskAsynchronously(TT.getPlugin());
		}
	}
}