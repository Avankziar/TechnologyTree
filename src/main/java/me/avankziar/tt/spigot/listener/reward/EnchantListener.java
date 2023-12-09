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
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
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
			ARGCheckEventAction.checkEventAction(event.getEnchanter(), "ENCHANTING:RETURN",
					EN, ToolType.HAND, null, null, Material.AIR);
			return;
		}
		if(!RecipeHandler.hasAccessToRecipe(event.getEnchanter().getUniqueId(), RecipeType.ENCHANTING, event.getItem().getType().toString()))
		{
			ARGCheckEventAction.checkEventAction(event.getEnchanter(), "ENCHANTING:CANNOTACCESS",
					EN, ToolType.HAND, null, null, Material.AIR);
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
			ARGCheckEventAction.checkEventAction(event.getEnchanter(), "ENCHANTING:RETURN",
					EN, ToolType.HAND, null, null, Material.AIR);
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
				ARGCheckEventAction.checkEventAction(player, "ENCHANTING:REWARD",
						EN, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(player, EN, ToolType.HAND, mat, null))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), EN, ToolType.HAND, mat, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}