package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.BlockHandler.BlockType;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

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
		if(!RecipeHandler.hasAccessToRecipe(event.getEnchanter().getUniqueId(), BlockType.ENCHANTING_TABLE, event.getItem().getType().toString()))
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
		for(ItemStack is : RewardHandler.getDrops(event.getEnchanter(), EN, event.getItem().getType(), null, true))
		{
			Item it = event.getEnchantBlock().getWorld().dropItem(event.getEnchantBlock().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getEnchanter().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getEnchanter().getUniqueId(), EN, event.getItem().getType(), null, 1);
	}
}