package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class SheepDyeListener implements Listener
{
	final private static EventType SD = EventType.SHEEP_DYE;
	
	@EventHandler
	public void onSheepDyeWool(SheepDyeWoolEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer() == null
				|| event.getEntity() == null
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(SD)
				|| event.getEntity().getColor() == event.getColor())
		{
			return;
		}
		final Player player = event.getPlayer();
		final Location loc = event.getEntity().getLocation();
		final EntityType ent = event.getEntityType();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(ItemStack is : RewardHandler.getDrops(player, SD, ToolType.HAND, null, ent))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), SD, ToolType.HAND, null, ent, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}