package main.java.me.avankziar.tt.spigot.listener.recipe;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;

public class PrepareItemCraftListener implements Listener
{
	private TT plugin = BaseConstructor.getPlugin();
	
	public PrepareItemCraftListener()
	{
		// TODO Auto-generated constructor stub
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPrepareCraft(PrepareItemCraftEvent event)
	{
		Recipe r = event.getRecipe();
		event.getInventory().setResult(null);
		if(r instanceof BlastingRecipe)
		{
			
		} else if(r instanceof CampfireRecipe)
		{
			
		} else if(r instanceof FurnaceRecipe)
		{
			
		} else if(r instanceof ShapedRecipe)
		{
			
		} else if(r instanceof ShapelessRecipe)
		{
			
		} else if(r instanceof SmithingRecipe)
		{
			
		} else if(r instanceof SmokingRecipe)
		{
			
		} else if(r instanceof StonecuttingRecipe)
		{
			
		}
	}
}