package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class SmithingListener implements Listener
{
	final private static EventType SM = EventType.SMITHING;
	@EventHandler
	public void onSmithItem(SmithItemEvent event)
	{
		if(event.isCancelled()
				|| !(event.getWhoClicked() instanceof Player)
				|| event.getWhoClicked().getGameMode() == GameMode.CREATIVE
				|| event.getWhoClicked().getGameMode() == GameMode.SPECTATOR
				|| event.getCurrentItem() == null
				|| !EnumHandler.isEventActive(SM))
		{
			ARGCheckEventAction.checkEventAction((Player) event.getWhoClicked(), "SMITHING:RETURN",
					SM, ToolType.HAND, null, null, Material.AIR);
			return;
		}
		final ItemStack result = event.getCurrentItem().clone();
		final Player player = (Player) event.getWhoClicked();
		final Material mat = result.getType();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(player, "SMITHING:REWARD",
						SM, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(player, SM, ToolType.HAND, mat, null))
				{
					ItemHandler.dropItem(is, player, null);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), SM, ToolType.HAND, mat, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}