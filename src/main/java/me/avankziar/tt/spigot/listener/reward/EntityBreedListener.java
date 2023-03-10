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

public class EntityBreedListener implements Listener
{
	@EventHandler
	public void onBreed(EntityBreedEvent event)
	{
		if(event.isCancelled()
				|| !(event.getBreeder() instanceof Player)
				|| ((HumanEntity) event.getBreeder()).getGameMode() == GameMode.CREATIVE
				|| ((HumanEntity) event.getBreeder()).getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(EventType.BREEDING)
				|| !RewardHandler.canAccessInteraction((Player) event.getBreeder(), EventType.BREEDING, null, event.getEntityType()))
		{
			return;
		}
		Player player = (Player) event.getBreeder();
		event.setExperience(0);
		for(ItemStack is : RewardHandler.getDrops(player, EventType.BREEDING, null, event.getEntityType(), false))
		{
			Item it = player.getWorld().dropItem(event.getEntity().getLocation(), is);
			ItemHandler.addItemToTask(it, player.getUniqueId());
		}
		RewardHandler.rewardPlayer(player.getUniqueId(), EventType.BREEDING, null, event.getEntityType(), 1);
	}
}