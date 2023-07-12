package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class FishingListener implements Listener
{	
	final private static EventType FI = EventType.FISHING;
	@EventHandler
	public void onFishing(PlayerFishEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer() == null
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| event.getState() != State.CAUGHT_FISH
				|| !EnumHandler.isEventActive(FI))
		{
			return;
		}
		event.setExpToDrop(0);
		ItemStack ismat = null;
		Entity e = event.getCaught();
		if(e != null && e instanceof Item)
		{
			Item it = (Item) e;
			ismat = it.getItemStack();
		} else
		{
			return;
		}
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), FI, ismat.getType(), null, true))
		{
			Item it = event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), FI,	ismat.getType(), null, ismat.getAmount());
	}
}