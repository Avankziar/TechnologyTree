package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class EnchantListener implements Listener
{
	final private static EventType EN = EventType.ENCHANTING;
	@EventHandler
	public void onPrepareEnchant(PrepareItemEnchantEvent event)
	{
		if(event.isCancelled()
				|| event.getEnchanter().getGameMode() == GameMode.CREATIVE
				|| event.getEnchanter().getGameMode() == GameMode.SPECTATOR
				|| event.getItem() == null
				|| !EnumHandler.isEventActive(EN))
		{
			return;
		}
		if(!RecipeHandler.hasAccessToRecipe(event.getEnchanter().getUniqueId(), RecipeType.ENCHANTING, event.getItem().getType().toString()))
		{
			event.setCancelled(true);
			event.getEnchanter().updateInventory();
			return;
		}
	}
	
	@EventHandler
	public void onPrepareEnchant(EnchantItemEvent event)
	{
		if(event.isCancelled()
				|| event.getEnchanter().getGameMode() == GameMode.CREATIVE
				|| event.getEnchanter().getGameMode() == GameMode.SPECTATOR
				|| event.getItem() == null
				|| !EnumHandler.isEventActive(EN))
		{
			return;
		}
		final Player player = event.getEnchanter();
		final Location loc = event.getEnchantBlock().getLocation();
		final Material mat = event.getItem().getType();
		new BukkitRunnable()
		{		
			@Override
			public void run()
			{
				for(ItemStack is : RewardHandler.getDrops(player, EN, ToolType.HAND, mat, null))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), EN, ToolType.HAND, mat, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}