package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class TameListener implements Listener
{
	final private static EventType TA = EventType.TAMING;
	@EventHandler
	public void onEntityTame(EntityTameEvent event)
	{
		if(!(event.getOwner() instanceof Player)
				|| event.getEntity().isDead()
				|| ((Player) event.getOwner()).getGameMode() == GameMode.CREATIVE
				|| ((Player) event.getOwner()).getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(TA)
				)
		{
			return;
		}
		Player player = (Player) event.getOwner();
		for(ItemStack is : RewardHandler.getDrops(player, TA, ToolType.ALL, null, event.getEntityType()))
		{
			Item it = event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), is);
			ItemHandler.addItemToTask(it, player.getUniqueId());
		}
		RewardHandler.rewardPlayer(player.getUniqueId(), TA, ToolType.ALL, null, event.getEntityType(), 1);
	}
}