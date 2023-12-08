package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class HarvestListener implements Listener
{
	final private static EventType HA = EventType.HARVEST;
	@EventHandler
	public void onHarvest(PlayerHarvestBlockEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(HA))
		{
			return;
		}
		final ToolType tool = ToolType.getHandToolType(event.getPlayer());
		final Player player = event.getPlayer();
		final Material mat = event.getHarvestedBlock().getType();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), HA, tool, mat, null))
				{
					ItemHandler.dropItem(is, player, null);
				}
				RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), HA, tool, mat, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}