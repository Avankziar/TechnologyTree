package main.java.me.avankziar.tt.spigot.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.inventory.recipe.CookingBookCategory;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.ifh.ItemGenerator;

public class RecipeHandler
{
	public enum RecipeType
	{
		BLASTING, CAMPFIRE, FURNACE, SHAPED, SHAPELESS, SMITHING, SMOKING, STONECUTTING;
	}
	
	private static TT plugin = BaseConstructor.getPlugin();
	private static boolean haveAllRecipeUnlocked = true;
	
	public static LinkedHashMap<RecipeType, ArrayList<String>> recipeMap = new LinkedHashMap<>(); //Alle registrierte Recipe
	
	public static void init()
	{
		haveAllRecipeUnlocked = plugin.getYamlHandler().getConfig().getBoolean("Do.Recipe.HaveAllRecipeUnlocked");
		if(plugin.getYamlHandler().getConfig().getBoolean("Do.Recipe.LoadThePluginRecipe"))
		{
			Bukkit.clearRecipes();
			for(Entry<String, YamlConfiguration> a : plugin.getYamlHandler().getBlastingRecipe().entrySet())
			{
				BlastingRecipe recipe = getBlastingRecipe(a.getKey(), a.getValue());
				if(recipe == null)
				{
					continue;
				}
				Bukkit.addRecipe(recipe);
				registerRecipeInMap(RecipeType.BLASTING, a.getKey());
			}
			for(Entry<String, YamlConfiguration> a : plugin.getYamlHandler().getCampfireRecipe().entrySet())
			{
				CampfireRecipe recipe = getCampfireRecipe(a.getKey(), a.getValue());
				if(recipe == null)
				{
					continue;
				}
				Bukkit.addRecipe(recipe);
				registerRecipeInMap(RecipeType.CAMPFIRE, a.getKey());
			}
			for(Entry<String, YamlConfiguration> a : plugin.getYamlHandler().getFurnaceRecipe().entrySet())
			{
				FurnaceRecipe recipe = getFurnaceRecipe(a.getKey(), a.getValue());
				if(recipe == null)
				{
					continue;
				}
				Bukkit.addRecipe(recipe);
				registerRecipeInMap(RecipeType.FURNACE, a.getKey());
			}
			for(Entry<String, YamlConfiguration> a : plugin.getYamlHandler().getShapedRecipe().entrySet())
			{
				ShapedRecipe recipe = getShapedRecipe(a.getKey(), a.getValue());
				if(recipe == null)
				{
					continue;
				}
				Bukkit.addRecipe(recipe);
				registerRecipeInMap(RecipeType.SHAPED, a.getKey());
			}
			for(Entry<String, YamlConfiguration> a : plugin.getYamlHandler().getShapelessRecipe().entrySet())
			{
				ShapelessRecipe recipe = getShapelessRecipe(a.getKey(), a.getValue());
				if(recipe == null)
				{
					continue;
				}
				Bukkit.addRecipe(recipe);
				registerRecipeInMap(RecipeType.SHAPELESS, a.getKey());
			}
			for(Entry<String, YamlConfiguration> a : plugin.getYamlHandler().getSmithingRecipe().entrySet())
			{
				SmithingRecipe recipe = getSmithingRecipe(a.getKey(), a.getValue());
				if(recipe == null)
				{
					continue;
				}
				Bukkit.addRecipe(recipe);
				registerRecipeInMap(RecipeType.SMITHING, a.getKey());
			}
			for(Entry<String, YamlConfiguration> a : plugin.getYamlHandler().getSmokingRecipe().entrySet())
			{
				SmokingRecipe recipe = getSmokingRecipe(a.getKey(), a.getValue());
				if(recipe == null)
				{
					continue;
				}
				Bukkit.addRecipe(recipe);
				registerRecipeInMap(RecipeType.SMOKING, a.getKey());
			}
			for(Entry<String, YamlConfiguration> a : plugin.getYamlHandler().getStonecuttingRecipe().entrySet())
			{
				StonecuttingRecipe recipe = getStonecuttingRecipe(a.getKey(), a.getValue());
				if(recipe == null)
				{
					continue;
				}
				Bukkit.addRecipe(recipe);
				registerRecipeInMap(RecipeType.STONECUTTING, a.getKey());
			}
		} else
		{
			for(Iterator<Recipe> iterator = Bukkit.recipeIterator(); iterator.hasNext();) 
			{
			    Recipe r = iterator.next();
			    if(r instanceof BlastingRecipe)
			    {
			    	BlastingRecipe a = (BlastingRecipe) r;
			    	registerRecipeInMap(RecipeType.BLASTING, a.getKey().getKey());
			    } else if(r instanceof CampfireRecipe)
			    {
			    	CampfireRecipe a = (CampfireRecipe) r;
			    	registerRecipeInMap(RecipeType.CAMPFIRE, a.getKey().getKey());
			    } else if(r instanceof FurnaceRecipe)
			    {
			    	FurnaceRecipe a = (FurnaceRecipe) r;
			    	registerRecipeInMap(RecipeType.FURNACE, a.getKey().getKey());
			    } else if(r instanceof MerchantRecipe) //https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/inventory/MerchantRecipe.html
			    {
			    	//MerchantRecipe a = (MerchantRecipe) r;
			    	
			    } else if(r instanceof ShapedRecipe)
			    {
			    	ShapedRecipe a = (ShapedRecipe) r;
			    	registerRecipeInMap(RecipeType.SHAPED, a.getKey().getKey());
			    } else if(r instanceof ShapelessRecipe)
			    {
			    	ShapelessRecipe a = (ShapelessRecipe) r;
			    	registerRecipeInMap(RecipeType.SHAPELESS, a.getKey().getKey());
			    } else if(r instanceof SmithingRecipe)
			    {
			    	SmithingRecipe a = (SmithingRecipe) r;
			    	registerRecipeInMap(RecipeType.SMITHING, a.getKey().getKey());
			    } else if(r instanceof SmokingRecipe)
			    {
			    	SmokingRecipe a = (SmokingRecipe) r;
			    	registerRecipeInMap(RecipeType.SMOKING, a.getKey().getKey());
			    } else if(r instanceof StonecuttingRecipe)
			    {
			    	StonecuttingRecipe a = (StonecuttingRecipe) r;
			    	registerRecipeInMap(RecipeType.STONECUTTING, a.getKey().getKey());
			    }
			}
		}
	}
	
	private static void registerRecipeInMap(RecipeType rt, String key)
	{
		ArrayList<String> keylist = new ArrayList<>();
		if(recipeMap.containsKey(rt))
		{
			recipeMap.get(rt);
		}
		if(!keylist.contains(key))
		{
			keylist.add(key);
		}
		recipeMap.put(rt, keylist);
	}
	
	private static BlastingRecipe getBlastingRecipe(String key, YamlConfiguration y)
	{
		try
		{
			Material input = Material.valueOf(y.getString("Input.Material"));
			CookingBookCategory cbc = CookingBookCategory.valueOf(y.getString("Category"));
			ItemStack r = new ItemGenerator().generateItem(null, y, "Result", 0);
			BlastingRecipe recipe = new BlastingRecipe(
					new NamespacedKey(plugin, key), r, input, (float) y.getDouble("Experience"), y.getInt("CookingTime"));
			recipe.setCategory(cbc);
			recipe.setGroup(y.getString("Group"));
			return recipe;
		} catch (Exception e)
		{
			return null;
		}
	}
	
	private static CampfireRecipe getCampfireRecipe(String key, YamlConfiguration y)
	{
		try
		{
			Material input = Material.valueOf(y.getString("Input.Material"));
			CookingBookCategory cbc = CookingBookCategory.valueOf(y.getString("Category"));
			ItemStack r = new ItemGenerator().generateItem(null, y, "Result", 0);
			CampfireRecipe recipe = new CampfireRecipe(
					new NamespacedKey(plugin, key), r, input, (float) y.getDouble("Experience"), y.getInt("CookingTime"));
			recipe.setCategory(cbc);
			recipe.setGroup(y.getString("Group"));
			return recipe;
		} catch (Exception e)
		{
			return null;
		}		
	}
	
	private static FurnaceRecipe getFurnaceRecipe(String key, YamlConfiguration y)
	{
		try
		{
			Material input = Material.valueOf(y.getString("Input.Material"));
			CookingBookCategory cbc = CookingBookCategory.valueOf(y.getString("Category"));
			ItemStack r = new ItemGenerator().generateItem(null, y, "Result", 0);
			FurnaceRecipe recipe = new FurnaceRecipe(
					new NamespacedKey(plugin, key), r, input, (float) y.getDouble("Experience"), y.getInt("CookingTime"));
			recipe.setCategory(cbc);
			recipe.setGroup(y.getString("Group"));
			return recipe;
		} catch (Exception e)
		{
			return null;
		}
	}
	
	private static ShapedRecipe getShapedRecipe(String key, YamlConfiguration y)
	{
		try
		{
			CraftingBookCategory cbc = CraftingBookCategory.valueOf(y.getString("Category"));
			ItemStack r = new ItemGenerator().generateItem(null, y, "Result", 0);
			ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, key), r);
			recipe.setCategory(cbc);
			recipe.setGroup(y.getString("Group"));
			recipe.shape(y.getString("Shape.Line1"), y.getString("Shape.Line2"), y.getString("Shape.Line3"));
			for(int i = 0; i < y.getString("Ingredient.CharacterList").length(); i++)
			{
				char c = y.getString("Ingredient.CharacterList").charAt(i);
				ItemStack ing = new ItemGenerator().generateItem(null, y, "Ingredient."+String.valueOf(c), 0);
				recipe.setIngredient(c, new RecipeChoice.ExactChoice(ing));
			}
			return recipe;
		} catch (Exception e)
		{
			return null;
		}
	}
	
	private static ShapelessRecipe getShapelessRecipe(String key, YamlConfiguration y)
	{
		try
		{
			CraftingBookCategory cbc = CraftingBookCategory.valueOf(y.getString("Category"));
			ItemStack r = new ItemGenerator().generateItem(null, y, "Result", 0);
			ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(plugin, key), r);
			recipe.setCategory(cbc);
			recipe.setGroup(y.getString("Group"));
			for(int i = 0; i < y.getInt("Ingredient.ListSize"); i++)
			{
				ItemStack ing = new ItemGenerator().generateItem(null, y, "Ingredient."+String.valueOf(i), 0);
				recipe.addIngredient(new RecipeChoice.ExactChoice(ing));
			}
			return recipe;
		} catch (Exception e)
		{
			return null;
		}
	}
	
	private static SmithingRecipe getSmithingRecipe(String key, YamlConfiguration y)
	{
		try
		{
			ItemStack r = new ItemGenerator().generateItem(null, y, "Result", 0);
			ItemStack base = new ItemGenerator().generateItem(null, y, "Base", 0);
			ItemStack add = new ItemGenerator().generateItem(null, y, "Addition", 0);
			SmithingRecipe recipe = new SmithingRecipe(new NamespacedKey(plugin, key), r,
					new RecipeChoice.MaterialChoice(base.getType()), new RecipeChoice.MaterialChoice(add.getType()));
			return recipe;
		} catch (Exception e)
		{
			return null;
		}
	}
	
	private static SmokingRecipe getSmokingRecipe(String key, YamlConfiguration y)
	{
		try
		{
			Material input = Material.valueOf(y.getString("Input.Material"));
			CookingBookCategory cbc = CookingBookCategory.valueOf(y.getString("Category"));
			ItemStack r = new ItemGenerator().generateItem(null, y, "Result", 0);
			SmokingRecipe recipe = new SmokingRecipe(
					new NamespacedKey(plugin, key), r, input, (float) y.getDouble("Experience"), y.getInt("CookingTime"));
			recipe.setCategory(cbc);
			recipe.setGroup(y.getString("Group"));
			return recipe;
		} catch (Exception e)
		{
			return null;
		}		
	}
	
	private static StonecuttingRecipe getStonecuttingRecipe(String key, YamlConfiguration y)
	{
		try
		{
			ItemStack r = new ItemGenerator().generateItem(null, y, "Result", 0);
			Material input = Material.valueOf(y.getString("Input.Material"));
			StonecuttingRecipe recipe = new StonecuttingRecipe(new NamespacedKey(plugin, key), r, input);
			recipe.setGroup(y.getString("Group"));
			return recipe;
		} catch (Exception e)
		{
			return null;
		}
	}
	
	public static boolean hasAccessToRecipe(Player player, Recipe r)
	{
		if(haveAllRecipeUnlocked)
		{
			return true;
		}
		UUID uuid = player.getUniqueId();
		if(r instanceof BlastingRecipe)
	    {
	    	BlastingRecipe a = (BlastingRecipe) r;
	    	return hasAccessToRecipe(uuid, RecipeType.BLASTING, a.getKey().getKey());
	    }  else if(r instanceof CampfireRecipe)
	    {
	    	CampfireRecipe a = (CampfireRecipe) r;
	    	return hasAccessToRecipe(uuid, RecipeType.CAMPFIRE, a.getKey().getKey());
	    } else if(r instanceof FurnaceRecipe)
	    {
	    	FurnaceRecipe a = (FurnaceRecipe) r;
	    	return hasAccessToRecipe(uuid, RecipeType.FURNACE, a.getKey().getKey());
	    } else if(r instanceof MerchantRecipe) //https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/inventory/MerchantRecipe.html
	    {
	    	//MerchantRecipe a = (MerchantRecipe) r;
	    	
	    } else if(r instanceof ShapedRecipe)
	    {
	    	ShapedRecipe a = (ShapedRecipe) r;
	    	return hasAccessToRecipe(uuid, RecipeType.SHAPED, a.getKey().getKey());
	    } else if(r instanceof ShapelessRecipe)
	    {
	    	ShapelessRecipe a = (ShapelessRecipe) r;
	    	return hasAccessToRecipe(uuid, RecipeType.SHAPELESS, a.getKey().getKey());
	    } else if(r instanceof SmithingRecipe)
	    {
	    	SmithingRecipe a = (SmithingRecipe) r;
	    	return hasAccessToRecipe(uuid, RecipeType.SMITHING, a.getKey().getKey());
	    } else if(r instanceof SmokingRecipe)
	    {
	    	SmokingRecipe a = (SmokingRecipe) r;
	    	return hasAccessToRecipe(uuid, RecipeType.SMOKING, a.getKey().getKey());
	    } else if(r instanceof StonecuttingRecipe)
	    {
	    	StonecuttingRecipe a = (StonecuttingRecipe) r;
	    	return hasAccessToRecipe(uuid, RecipeType.STONECUTTING, a.getKey().getKey());
	    }
		return haveAllRecipeUnlocked;
	}
	
	public static boolean hasAccessToRecipe(UUID uuid, RecipeType rt, String key)
	{
		if(PlayerHandler.recipeMap.containsKey(uuid)
				&& PlayerHandler.recipeMap.get(uuid).containsKey(rt)
				&& PlayerHandler.recipeMap.get(uuid).get(rt).contains(key))
		{
			return true;
		}
		return false;
	}
}