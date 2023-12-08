package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class ItemBreakListener implements Listener
{	
	final private static EventType IB = EventType.ITEM_BREAKING;
	@EventHandler
	public void onItemBreak(PlayerItemBreakEvent event)
	{
		if(event.getPlayer() == null
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(IB))
		{
			return;
		}
		final Player player = event.getPlayer();
		final Material mat = event.getBrokenItem().getType();
		final ToolType tool = ToolType.getToolType(mat);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(ItemStack is : RewardHandler.getDrops(player, IB, tool, mat, null))
				{
					ItemHandler.dropItem(is, player, null);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), IB, tool, mat, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}