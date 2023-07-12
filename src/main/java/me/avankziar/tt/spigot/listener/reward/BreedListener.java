package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class BreedListener implements Listener
{
	final private static EventType BR = EventType.BREEDING;
	@EventHandler
	public void onBreed(EntityBreedEvent event)
	{
		if(event.isCancelled()
				|| !(event.getBreeder() instanceof Player)
				|| ((HumanEntity) event.getBreeder()).getGameMode() == GameMode.CREATIVE
				|| ((HumanEntity) event.getBreeder()).getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(BR)
				|| !RewardHandler.canAccessInteraction((Player) event.getBreeder(), BR, null, event.getEntityType()))
		{
			return;
		}
		Player player = (Player) event.getBreeder();
		event.setExperience(0);
		for(ItemStack is : RewardHandler.getDrops(player, BR, null, event.getEntityType(), false))
		{
			Item it = player.getWorld().dropItem(event.getEntity().getLocation(), is);
			ItemHandler.addItemToTask(it, player.getUniqueId());
		}
		RewardHandler.rewardPlayer(player.getUniqueId(), BR, null, event.getEntityType(), 1);
	}
}