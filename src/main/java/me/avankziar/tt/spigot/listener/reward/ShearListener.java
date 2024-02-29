package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class ShearListener implements Listener
{
	final private static EventType SH = EventType.SHEARING;
	private static LinkedHashMap<UUID, UUID> sheared = new LinkedHashMap<>(); //EntityUUID, PlayerUUID
	
	@EventHandler
	public void onShearing(PlayerShearEntityEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(SH))
		{
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "SHEARING:RETURN",
					SH, ToolType.HAND, null, null, Material.AIR);
			return;
		}
		if(!RewardHandler.canAccessInteraction(event.getPlayer(), 
				ToolType.HAND, SH, null, event.getEntity().getType()))
		{
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "SHEARING:CANNOTACCESS",
					SH, ToolType.HAND, null, null, Material.AIR);
			event.setCancelled(true);
			return;
		}
		final Player player = event.getPlayer();
		final EntityType ent = event.getEntity().getType();
		sheared.put(event.getEntity().getUniqueId(), player.getUniqueId());
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(player, "SHEARING:REWARD",
						SH, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(player, SH, ToolType.HAND, null, ent))
				{
					ItemHandler.dropItem(is, player, null);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), SH, ToolType.HAND, null, ent, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
	
	@EventHandler
	public void onEntityDrop(EntityDropItemEvent event)
	{
		if(event.isCancelled()
				|| !(event.getEntity() instanceof Sheep))
		{
			return;
		}
		event.getItemDrop().remove();
		if(!sheared.containsKey(event.getEntity().getUniqueId()))
		{
			return;
		}
		final Material mat = event.getItemDrop().getItemStack().getType();
		UUID uuid = sheared.get(event.getEntity().getUniqueId());
		if(uuid == null)
		{
			return;
		}
		sheared.remove(event.getEntity().getUniqueId());
		final Player player = Bukkit.getPlayer(uuid);
		if(player == null)
		{
			return;
		}
		if(!RewardHandler.canAccessInteraction(player, 
				ToolType.HAND, SH, mat, null))
		{
			ARGCheckEventAction.checkEventAction(player, "SHEARING:CANNOTACCESS2",
					SH, ToolType.HAND, null, null, Material.AIR);
			event.setCancelled(true);
			return;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(player, "SHEARING:REWARD2",
						SH, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(player, SH, ToolType.HAND, mat, null))
				{
					ItemHandler.dropItem(is, player, null);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), SH, ToolType.HAND, mat, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}