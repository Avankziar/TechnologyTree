package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

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
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), SH, ToolType.SHEARS, null, event.getEntity().getType()))
		{
			Item it = event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), SH,	ToolType.SHEARS, null, event.getEntity().getType(), 1);
	}
}