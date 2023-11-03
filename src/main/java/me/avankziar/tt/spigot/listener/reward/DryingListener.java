package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SpongeAbsorbEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.BlockHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class DryingListener implements Listener
{
	private static LinkedHashMap<String, UUID> placedSponge = new LinkedHashMap<>();
	final private static EventType DR = EventType.DRYING;
	
	@EventHandler(priority = EventPriority.LOW)
	public void onSponePlace(BlockPlaceEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer() == null
				|| event.getBlock() == null
				|| event.getBlock().getType() != Material.SPONGE
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(DR))
		{
			return;
		}
		placedSponge.replace(BlockHandler.getLocationText(event.getBlock().getLocation()), event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onSponeAbsorb(SpongeAbsorbEvent event)
	{
		if(event.isCancelled()
				|| event.getBlock() == null
				|| event.getBlock().getType() != Material.WET_SPONGE
				|| !EnumHandler.isEventActive(DR))
		{
			return;
		}
		UUID uuid = placedSponge.get(BlockHandler.getLocationText(event.getBlock().getLocation()));
		if(uuid == null)
		{
			return;
		}
		Player player = Bukkit.getPlayer(uuid);
		if(player != null
				&& player.getGameMode() != GameMode.CREATIVE
				&& player.getGameMode() != GameMode.SPECTATOR)
		{
			for(ItemStack is : RewardHandler.getDrops(player, DR, Material.SPONGE, null, true))
			{
				Item it = player.getWorld().dropItem(player.getLocation(), is);
				ItemHandler.addItemToTask(it, uuid);
			}
		}
		RewardHandler.rewardPlayer(uuid, DR, Material.SPONGE, null, event.getBlocks().size());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onSponeBreak(BlockBreakEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer() == null
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| event.getBlock() == null
				|| event.getBlock().getType() != Material.WET_SPONGE
				|| !EnumHandler.isEventActive(DR))
		{
			return;
		}
		placedSponge.remove(BlockHandler.getLocationText(event.getBlock().getLocation()));
	}
}