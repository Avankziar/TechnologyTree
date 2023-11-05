package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.block.CampfireStartEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.BlockHandler;
import main.java.me.avankziar.tt.spigot.handler.BlockHandler.BlockType;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class CookMeltSmeltSmokeListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onFurnaceStartSmelt(FurnaceStartSmeltEvent event)
	{
		if(event.getBlock() == null
				|| !EnumHandler.isEventActive(BlockHandler.getEventType(event.getBlock().getType())))
		{
			return;
		}
		BlockType bt = BlockHandler.getBlockType(event.getBlock().getType());
		BlockHandler.startSmelt(event.getBlock().getLocation(), bt, event.getRecipe().getKey().getKey());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCampfireStartSmelt(CampfireStartEvent event)
	{
		if(event.getBlock() == null
				|| !EnumHandler.isEventActive(EventType.COOKING))
		{
			return;
		}
		BlockType bt = BlockHandler.getBlockType(event.getBlock().getType());
		BlockHandler.startSmelt(event.getBlock().getLocation(), bt, event.getRecipe().getKey().getKey());
	}
	
	/* Do not needed
	@EventHandler
	public void onFurnaceExtract(FurnaceExtractEvent event)
	{
		event.setExpToDrop(0);
	}*/
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onFurnaceSmelt(BlockCookEvent event)
	{
		EventType et = BlockHandler.getEventType(event.getBlock().getType());
		if(event.isCancelled() || event.getBlock() == null || event.getResult() == null
				|| !EnumHandler.isEventActive(et))
		{
			return;
		}
		String[] key = BlockHandler.getSmeltRecipe(event.getBlock().getLocation());
		if(key == null)
		{
			event.setCancelled(true);
			return;
		}
		BlockType bt = BlockType.valueOf(key[0]);
		UUID uuid = BlockHandler.getRegisterBlockOwner(bt, event.getBlock().getLocation());
		if(uuid == null)
		{
			if(!new ConfigHandler().finishSmeltIfPlayerHasNotTheRecipeUnlocked())
			{
				return;
			}
			event.setCancelled(true);
			return;
		}
		String recipeKey = key[1];
		if(!RecipeHandler.hasAccessToRecipe(uuid, bt, recipeKey))
		{
			if(!new ConfigHandler().finishSmeltIfPlayerHasNotTheRecipeUnlocked())
			{
				return;
			}
			event.setCancelled(true);
			return;
		}
		//Items can drops additionally, if it is so configurated
		Player player = Bukkit.getPlayer(uuid);
		if(player != null)
		{
			for(ItemStack is : RewardHandler.getDrops(player, et, ToolType.ALL, event.getBlock().getType(), null))
			{
				Item it = event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), is);
				ItemHandler.addItemToTask(it, uuid);
			}
		}
		RewardHandler.rewardPlayer(uuid, et, ToolType.ALL, event.getResult().getType(), null, event.getResult().getAmount());
	}
}