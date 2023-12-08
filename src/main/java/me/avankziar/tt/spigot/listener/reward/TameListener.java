package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
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
		final Player player = (Player) event.getOwner();
		final Location loc = event.getEntity().getLocation();
		final EntityType ent = event.getEntityType();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(ItemStack is : RewardHandler.getDrops(player, TA, ToolType.HAND, null, ent))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), TA, ToolType.HAND, null, ent, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}