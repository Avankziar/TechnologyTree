package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SpongeAbsorbEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.BlockHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class DryingListener implements Listener
{
	private static LinkedHashMap<String, String> placedSponge = new LinkedHashMap<>();
	final private static EventType DR = EventType.DRYING;
	
	@EventHandler(priority = EventPriority.LOW)
	public void onSponePlace(BlockPlaceEvent event)
	{
		if(event.isCancelled()
				|| event.getBlock().getType() != Material.SPONGE
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(DR))
		{
			return;
		}
		placedSponge.put(BlockHandler.getLocationText(event.getBlock().getLocation()), event.getPlayer().getUniqueId().toString());
	}
	
	@EventHandler
	public void onSponeAbsorb(SpongeAbsorbEvent event)
	{
		if(event.isCancelled()
				|| !EnumHandler.isEventActive(DR))
		{
			return;
		}
		final Location loc = event.getBlock().getLocation();
		final int size = event.getBlocks().size();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				final UUID uuid = UUID.fromString(placedSponge.get(BlockHandler.getLocationText(loc)));
				if(uuid == null)
				{
					return;
				}
				Player player = Bukkit.getPlayer(uuid);
				if(player == null
						|| player.getGameMode() == GameMode.CREATIVE
						|| player.getGameMode() == GameMode.SPECTATOR)
				{
					ARGCheckEventAction.checkEventAction(player, "DRYING:RETURN",
							DR, ToolType.HAND, null, null, Material.AIR);
					return;
				}
				ARGCheckEventAction.checkEventAction(player, "DRYING:REWARD",
						DR, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(player, DR, ToolType.HAND, Material.SPONGE, null))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(uuid, DR, ToolType.HAND, Material.SPONGE, null, size);
				placedSponge.remove(BlockHandler.getLocationText(loc));
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}