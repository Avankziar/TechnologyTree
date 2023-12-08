package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class ShearListener implements Listener
{
	final private static EventType SH = EventType.SHEARING;
	
	@EventHandler
	public void onShearing(PlayerShearEntityEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(SH))
		{
			return;
		}
		if(!RewardHandler.canAccessInteraction(event.getPlayer(), 
				ToolType.getToolType(event.getPlayer().getInventory().getItemInMainHand().getType()), SH, null, event.getEntity().getType()))
		{
			event.setCancelled(true);
			return;
		}
		final Player player = event.getPlayer();
		final EntityType ent = event.getEntity().getType();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(ItemStack is : RewardHandler.getDrops(player, SH, ToolType.SHEARS, null, ent))
				{
					ItemHandler.dropItem(is, player, null);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), SH, ToolType.SHEARS, null, ent, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}