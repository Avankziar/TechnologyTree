package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class ItemConsumeListener implements Listener
{
	final private static EventType IC = EventType.ITEM_CONSUME;
	
	@EventHandler
	public void onItemConsume(PlayerItemConsumeEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer() == null
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(IC))
		{
			return;
		}
		final Player player = event.getPlayer();
		final Material mat = event.getItem().getType();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(ItemStack is : RewardHandler.getDrops(player, IC, ToolType.HAND, mat, null))
				{
					ItemHandler.dropItem(is, player, null);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), IC, ToolType.HAND, mat, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}