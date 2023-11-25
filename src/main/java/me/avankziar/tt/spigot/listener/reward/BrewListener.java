package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.handler.BlockHandler;
import main.java.me.avankziar.tt.spigot.handler.BlockHandler.BlockType;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class BrewListener implements Listener
{
	final private static EventType BR = EventType.BREWING;
	final private static boolean FINISHBREW_IFPLAYERHASNOTTHERECIPEUNLOCKED = new ConfigHandler().finishBrewIfPlayerHasNotTheRecipeUnlocked();
	@EventHandler
	public void onBrewingStart(BrewingStartEvent event)
	{
		if(event.getBlock() == null
				|| !EnumHandler.isEventActive(BR))
		{
			return;
		}
		TT.log.info("BrewingStart Start"); //REMOVEME
		BrewingStand bs = (BrewingStand) event.getBlock();
		BlockHandler.startBrew(event.getBlock().getLocation(), bs.getInventory().getIngredient().getType());
	}
	
	@EventHandler
	public void onBrewingFinish(BrewEvent event)
	{
		if(event.isCancelled()
				|| !EnumHandler.isEventActive(BR))
		{
			return;
		}
		TT.log.info("Brew Start"); //REMOVEME
		String recipeKey = BlockHandler.getBrewRecipe(event.getBlock().getLocation());
		if(recipeKey == null)
		{
			return;
		}
		final BlockType bt = BlockType.BREWING_STAND;
		final UUID uuid = BlockHandler.getRegisterBlockOwner(bt, event.getBlock().getLocation());
		if(uuid == null)
		{
			return;
		}
		if(!RecipeHandler.hasAccessToRecipe(uuid, bt, recipeKey))
		{
			if(!FINISHBREW_IFPLAYERHASNOTTHERECIPEUNLOCKED)
			{
				return;
			}
			event.setCancelled(true);
			return;
		}
		//ADDME Async!
		int i = 0;
		Player player = Bukkit.getPlayer(uuid);
		for(ItemStack is : event.getContents().getContents())
		{
			if(is != null)
			{
				i++;
				if(player != null)
				{
					for(ItemStack iss : RewardHandler.getDrops(player, BR, ToolType.HAND, is.getType(), null))
					{
						Item it = event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), iss);
						ItemHandler.addItemToTask(it, uuid);
					}
				}
			}
		}
		RewardHandler.rewardPlayer(uuid, BR, ToolType.HAND, Material.valueOf(recipeKey), null, i);
	}
}