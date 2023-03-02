package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;

import main.java.me.avankziar.tt.spigot.handler.BlockHandler;
import main.java.me.avankziar.tt.spigot.handler.BlockHandler.BlockType;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class SmeltListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCamfireStart(FurnaceStartSmeltEvent event)
	{
		if(event.getBlock() == null)
		{
			return;
		}
		BlockType bt = BlockHandler.getBlockType(event.getBlock().getType());
		BlockHandler.startSmelt(event.getBlock().getLocation(), bt, event.getRecipe().getKey().getKey());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCamfireStart(FurnaceSmeltEvent event)
	{
		if(event.isCancelled() || event.getResult() == null)
		{
			return;
		}
		String[] key = BlockHandler.getSmeltRecipe(event.getBlock().getLocation());
		if(key == null)
		{
			return;
		}
		BlockType bt = BlockType.valueOf(key[0]);
		UUID uuid = BlockHandler.getRegisterBlockOwner(bt, event.getBlock().getLocation());
		if(uuid == null)
		{
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
		RewardHandler.rewardPlayer(uuid, EventType.FURNACE_SMELT, event.getResult().getType(), null, event.getResult().getAmount());
	}
}