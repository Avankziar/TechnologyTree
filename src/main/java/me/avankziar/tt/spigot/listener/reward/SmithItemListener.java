package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class SmithItemListener implements Listener
{
	@EventHandler
	public void onSmithItem(SmithItemEvent event)
	{
		if(event.isCancelled()
				|| !(event.getWhoClicked() instanceof Player)
				|| event.getWhoClicked().getGameMode() == GameMode.CREATIVE
				|| event.getWhoClicked().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(EventType.BREAKING)
				|| event.getCurrentItem() == null)
		{
			return;
		}
		final ItemStack result = event.getCurrentItem().clone();
		Player player = (Player) event.getWhoClicked();
		for(ItemStack is : RewardHandler.getDrops(player, EventType.SMITHING, result.getType(), null, false))
		{
			Item it = player.getWorld().dropItem(player.getLocation(), is);
			ItemHandler.addItemToTask(it, player.getUniqueId());
		}
		RewardHandler.rewardPlayer(player.getUniqueId(), EventType.SMITHING, result.getType(), null, 1);
	}
}