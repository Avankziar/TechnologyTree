package main.java.me.avankziar.tt.spigot.handler;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import main.java.me.avankziar.ifh.general.modifier.ModificationType;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.RewardType;
import main.java.me.avankziar.tt.spigot.objects.TechnologyType;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.DropChance;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.MainCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SubCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.Technology;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.UnlockableInteraction;

public class CatTechHandler
{
	private static TT plugin = BaseConstructor.getPlugin();
	//Um direkt auf Kategorien und Techs per namen zuzugreifen
	public static LinkedHashMap<String, MainCategory> mainCategoryMapSolo;
	public static LinkedHashMap<String, SubCategory> subCategoryMapSolo;
	public static LinkedHashMap<String, Technology> technologyMapSolo;
	
	public static LinkedHashMap<String, MainCategory> mainCategoryMapGroup;
	public static LinkedHashMap<String, SubCategory> subCategoryMapGroup;
	public static LinkedHashMap<String, Technology> technologyMapGroup;
	
	public static LinkedHashMap<String, MainCategory> mainCategoryMapGlobal;
	public static LinkedHashMap<String, SubCategory> subCategoryMapGlobal;
	public static LinkedHashMap<String, Technology> technologyMapGlobal;
	
	//Um den Baum der kategorien bis zu den Technologien runterzugehen. Vorallem für das Gui
	public static LinkedHashMap<String, LinkedHashMap<Integer, Technology>> subCategoryTechnologyMapSolo;
	public static LinkedHashMap<String, LinkedHashMap<Integer, SubCategory>> mainCategorySubCategoryMapSolo;
	
	public static LinkedHashMap<String, LinkedHashMap<Integer, Technology>> subCategoryTechnologyMapGroup;
	public static LinkedHashMap<String, LinkedHashMap<Integer, SubCategory>> mainCategorySubCategoryMapGroup;
	
	public static LinkedHashMap<String, LinkedHashMap<Integer, Technology>> subCategoryTechnologyMapGlobal;
	public static LinkedHashMap<String, LinkedHashMap<Integer, SubCategory>> mainCategorySubCategoryMapGlobal;
	
	//Um für das Gui für die drei Arten auf die MainCategories zu kommen. Für das Hauptguibild.
	public static LinkedHashMap<PlayerAssociatedType, LinkedHashMap<Integer, MainCategory>> playerAssocMainCategoryMap;
	
	public static boolean reload()
	{
		mainCategoryMapSolo = new LinkedHashMap<>();
		subCategoryMapSolo = new LinkedHashMap<>();
		technologyMapSolo = new LinkedHashMap<>();
		
		mainCategoryMapGroup = new LinkedHashMap<>();
		subCategoryMapGroup = new LinkedHashMap<>();
		technologyMapGroup = new LinkedHashMap<>();
		
		mainCategoryMapGlobal = new LinkedHashMap<>();
		subCategoryMapGlobal = new LinkedHashMap<>();
		technologyMapGlobal = new LinkedHashMap<>();
		
		subCategoryTechnologyMapSolo = new LinkedHashMap<>();
		mainCategorySubCategoryMapSolo = new LinkedHashMap<>();
		
		subCategoryTechnologyMapGroup = new LinkedHashMap<>();
		mainCategorySubCategoryMapGroup = new LinkedHashMap<>();
		
		subCategoryTechnologyMapGlobal = new LinkedHashMap<>();
		mainCategorySubCategoryMapGlobal = new LinkedHashMap<>();
		
		playerAssocMainCategoryMap = new LinkedHashMap<>();
		
		for(Entry<String, YamlConfiguration> entry : plugin.getYamlHandler().getTechnologies().entrySet())
		{
			YamlConfiguration y = entry.getValue();
			if(y.get("PlayerAssociatedType") == null)
			{
				continue;
			}
			try
			{
				String internName = entry.getKey();
				if(technologyMapSolo.containsKey(internName)
						&& technologyMapGroup.containsKey(internName)
						&& technologyMapGlobal.containsKey(internName))
				{
					continue;
				}
				String displayName = y.getString("Displayname");
				
				TechnologyType technologyType = TechnologyType.valueOf(y.getString("TechnologyType"));
				int maximalTechnologyLevelToResearch = 
						y.get("MaximalTechnologyLevelToResearch") != null 
						? (y.getInt("MaximalTechnologyLevelToResearch") <= 0 ? 1 : y.getInt("MaximalTechnologyLevelToResearch")) 
						: 1;
				PlayerAssociatedType playerAssociatedType = PlayerAssociatedType.valueOf(y.getString("PlayerAssociatedType"));
				String overlyingSubCategory = y.getString("OverlyingSubCategory");
				
				int guiSlot = y.getInt("MaximalTechnologyLevelToResearch");
				guiSlot = guiSlot < 0 ? 0 : guiSlot;
				guiSlot = guiSlot > 54 ? 54 : guiSlot;
				
				List<String> seeRequirementConditionQuery = y.getStringList("RequirementToSee.ConditionQuery");
				boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt = y.getBoolean("RequirementToSee.ConditionQuery");
				
				List<String> researchRequirementConditionQuery = y.getStringList("RequirementToResearch.ConditionQuery");
				
				String costTTExp = y.getString("RequirementToResearch.Costs.TTExp");
				String costVanillaExp = y.getString("RequirementToResearch.Costs.VanillaExp");
				String costMoney = y.getString("RequirementToResearch.Costs.Money");
				LinkedHashMap<Material, String> costMaterial = new LinkedHashMap<>();
				if(y.get("RequirementToResearch.Costs.Material") != null)
				{
					for(String s : y.getStringList("RequirementToResearch.Costs.Material"))
					{
						String[] split = s.split(";");
						if(split.length != 2)
						{
							continue;
						}
						try
						{
							Material mat = Material.valueOf(split[0]);
							String amount = split[1];
							costMaterial.put(mat, amount);
						} catch(Exception e)
						{
							continue;
						}
					}
				}
				
				ArrayList<UnlockableInteraction> rewardUnlockableInteractions = new ArrayList<>();
				if(y.get("Rewards.UnlockableInteractions") != null)
				{
					for(String s : y.getStringList("Rewards.UnlockableInteractions"))
					{
						String[] split  = s.split(":");
						if(split.length < 2)
						{
							continue;
						}
						try
						{
							EventType eventType = EventType.valueOf(split[0]);
							Material material = split[1].equals("null") ? null : Material.valueOf(split[1]);
							EntityType entityType = split[2].equals("null") ? null : EntityType.valueOf(split[2]);
							UnlockableInteraction ui = new UnlockableInteraction(eventType, material, entityType, false, 0.0, 0.0);
							for(int i = 3; i < split.length; i++)
							{
								String[] sp = split[i].split("=");
								if(split[i].startsWith("canAccess") && sp.length == 2)
								{
									ui.setCanAccess(Boolean.parseBoolean(sp[1]));
								} else if(split[i].startsWith("ttexp") && sp.length == 2)
								{
									ui.setTechnologyExperience(Double.parseDouble(sp[1]));
								} else if(split[i].startsWith("vaexp") && sp.length == 2)
								{
									ui.setVanillaExperience(i);
								} else if(split[i].startsWith("cmd") && sp.length == 3)
								{
									ui.addCommandValues(sp[1], Double.parseDouble(sp[2]));
								} else if(sp.length == 2) //Money
								{
									ui.addMoneyValues(sp[0], Double.parseDouble(sp[1]));
								} 
							}
							rewardUnlockableInteractions.add(ui);
						} catch(Exception e)
						{
							continue;
						}
					}
				}
				LinkedHashMap<RecipeType, ArrayList<String>> rewardRecipes = new LinkedHashMap<>();
				if(y.get("Rewards.UnlockableRecipe") != null)
				{
					for(String s : y.getStringList("Rewards.UnlockableRecipe"))
					{
						String[] split  = s.split(":");
						if(split.length != 2)
						{
							continue;
						}
						try
						{
							RecipeType rt = RecipeType.valueOf(split[0]);
							String key = split[1];
							if(!RecipeHandler.recipeMap.containsKey(rt)
									|| !RecipeHandler.recipeMap.get(rt).contains(key))
							{
								continue;
							}
							ArrayList<String> list = new ArrayList<>();
							if(rewardRecipes.containsKey(rt))
							{
								list = rewardRecipes.get(rt);
							}
							if(!list.contains(key))
							{
								list.add(key);
							}
							rewardRecipes.put(rt, list);
						} catch(Exception e)
						{
							continue;
						}
					}
				}
				ArrayList<DropChance> rewardDropChances = new ArrayList<>();
				if(y.get("Rewards.DropChance") != null)
				{
					for(String s : y.getStringList("Rewards.DropChance"))
					{
						String[] split  = s.split(":");
						if(split.length != 6)
						{
							continue;
						}
						try
						{
							EventType eventType = EventType.valueOf(split[0]);
							Material material = null;
							EntityType entityType = null;
							if(!split[1].equalsIgnoreCase("null"))
							{
								material = Material.valueOf(split[1]);
							}
							if(!split[2].equalsIgnoreCase("null"))
							{
								entityType = EntityType.valueOf(split[2]);
							}
							String item = split[3];
							int amount = Integer.parseInt(split[4]);
							DropChance dc = new DropChance(eventType, material, entityType, item, amount, Double.parseDouble(split[5]));
							rewardDropChances.add(dc);
						} catch(Exception e)
						{
							continue;
						}
					}
				}
				ArrayList<DropChance> rewardSilkTouchDropChances = new ArrayList<>();
				if(y.get("Rewards.SilkTouchDropChance") != null)
				{
					for(String s : y.getStringList("Rewards.SilkTouchDropChance"))
					{
						String[] split  = s.split(":");
						if(split.length != 6)
						{
							continue;
						}
						try
						{
							EventType eventType = EventType.valueOf(split[0]);
							Material material = null;
							EntityType entityType = null;
							if(!split[1].equalsIgnoreCase("null"))
							{
								material = Material.valueOf(split[1]);
							}
							if(!split[2].equalsIgnoreCase("null"))
							{
								entityType = EntityType.valueOf(split[2]);
							}
							String item = split[3];
							int amount = Integer.parseInt(split[4]);
							DropChance dc = new DropChance(eventType, material, entityType, item, amount, Double.parseDouble(split[5]));
							rewardSilkTouchDropChances.add(dc);
						} catch(Exception e)
						{
							continue;
						}
					}
				}
				ArrayList<String> rewardCommandList = new ArrayList<>();
				if(y.get("Rewards.Command") != null)
				{
					rewardCommandList = (ArrayList<String>) y.getStringList("Rewards.Command");
				}
				ArrayList<String> rewardItemList = new ArrayList<>();
				if(y.get("Rewards.Item") != null)
				{
					rewardItemList = (ArrayList<String>) y.getStringList("Rewards.Item");
				}
				ArrayList<String> rewardModifierList = new ArrayList<>();
				if(y.get("Rewards.Modifier") != null && plugin.getModifier() != null)
				{
					rewardModifierList = (ArrayList<String>) y.getStringList("Rewards.Modifier");
				}
				ArrayList<String> rewardValueEntryList = new ArrayList<>();
				if(y.get("Rewards.ValueEntry") != null && plugin.getValueEntry() != null)
				{
					rewardValueEntryList = (ArrayList<String>) y.getStringList("Rewards.ValueEntry");
				}
				Technology t = new Technology(internName, displayName, technologyType, maximalTechnologyLevelToResearch,
						playerAssociatedType, overlyingSubCategory, guiSlot,
						seeRequirementConditionQuery, seeRequirementShowDifferentItemIfYouNormallyDontSeeIt, researchRequirementConditionQuery, 
						costTTExp, costVanillaExp, costMoney, costMaterial,
						rewardUnlockableInteractions, rewardRecipes, rewardDropChances, rewardSilkTouchDropChances,
						rewardCommandList, rewardItemList, rewardModifierList, rewardValueEntryList);
				if(playerAssociatedType == PlayerAssociatedType.SOLO)
				{
					technologyMapSolo.put(internName, t);
					LinkedHashMap<Integer, Technology> map = new LinkedHashMap<>();
					if(subCategoryTechnologyMapSolo.containsKey(overlyingSubCategory))
					{
						map = subCategoryTechnologyMapSolo.get(overlyingSubCategory);
					}
					map.put(guiSlot, t);
					subCategoryTechnologyMapSolo.put(overlyingSubCategory, map);
				} else if(playerAssociatedType == PlayerAssociatedType.GROUP)
				{
					technologyMapGroup.put(internName, t);
					LinkedHashMap<Integer, Technology> map = new LinkedHashMap<>();
					if(subCategoryTechnologyMapGroup.containsKey(overlyingSubCategory))
					{
						map = subCategoryTechnologyMapGroup.get(overlyingSubCategory);
					}
					map.put(guiSlot, t);
					subCategoryTechnologyMapGroup.put(overlyingSubCategory, map);
				} else if(playerAssociatedType == PlayerAssociatedType.GLOBAL)
				{
					technologyMapGlobal.put(internName, t);
					LinkedHashMap<Integer, Technology> map = new LinkedHashMap<>();
					if(subCategoryTechnologyMapGlobal.containsKey(overlyingSubCategory))
					{
						map = subCategoryTechnologyMapGlobal.get(overlyingSubCategory);
					}
					map.put(guiSlot, t);
					subCategoryTechnologyMapGlobal.put(overlyingSubCategory, map);
				}
			} catch(Exception e)
			{
				continue;
			}
		}
		for(Entry<String, YamlConfiguration> entry : plugin.getYamlHandler().getSubCategories().entrySet())
		{
			YamlConfiguration y = entry.getValue();
			if(y.get("PlayerAssociatedType") == null)
			{
				continue;
			}
			try
			{
				String internName = entry.getKey();
				String displayName = y.getString("Displayname");
				
				PlayerAssociatedType playerAssociatedType = PlayerAssociatedType.valueOf(y.getString("PlayerAssociatedType"));
				String groupAssociatedPermission = y.getString("GroupAssociatedPermission", null);
				int guiSlot = y.getInt("GuiSlot");
				boolean useFixGuiSlot = y.getBoolean("UseFixGuiSlots");
				List<String> seeRequirementConditionQuery = y.getStringList("RequirementToSee.ConditionQuery");
				boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt = y.getBoolean("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt");
				String overlyingCategory = y.getString("IfSubCategory.OverlyingCategory");
				SubCategory sc = new SubCategory(internName, displayName,
						playerAssociatedType, groupAssociatedPermission,
						guiSlot, useFixGuiSlot, 
						seeRequirementConditionQuery, seeRequirementShowDifferentItemIfYouNormallyDontSeeIt, 
						overlyingCategory);
				if(playerAssociatedType == PlayerAssociatedType.SOLO)
				{
					subCategoryMapSolo.put(internName, sc);
					LinkedHashMap<Integer, SubCategory> map = new LinkedHashMap<>();
					if(mainCategorySubCategoryMapSolo.containsKey(overlyingCategory))
					{
						map = mainCategorySubCategoryMapSolo.get(overlyingCategory);
					}
					map.put(guiSlot, sc);
					mainCategorySubCategoryMapSolo.put(overlyingCategory, map);
				} else if(playerAssociatedType == PlayerAssociatedType.GROUP)
				{
					subCategoryMapGroup.put(internName, sc);
					LinkedHashMap<Integer, SubCategory> map = new LinkedHashMap<>();
					if(mainCategorySubCategoryMapGroup.containsKey(overlyingCategory))
					{
						map = mainCategorySubCategoryMapGroup.get(overlyingCategory);
					}
					map.put(guiSlot, sc);
					mainCategorySubCategoryMapGroup.put(overlyingCategory, map);
				} else if(playerAssociatedType == PlayerAssociatedType.GLOBAL)
				{
					subCategoryMapGlobal.put(internName, sc);
					LinkedHashMap<Integer, SubCategory> map = new LinkedHashMap<>();
					if(mainCategorySubCategoryMapGlobal.containsKey(overlyingCategory))
					{
						map = mainCategorySubCategoryMapGlobal.get(overlyingCategory);
					}
					map.put(guiSlot, sc);
					mainCategorySubCategoryMapGlobal.put(overlyingCategory, map);
				}
			} catch(Exception e)
			{
				continue;
			}
		}
		for(Entry<String, YamlConfiguration> entry : plugin.getYamlHandler().getMainCategories().entrySet())
		{
			YamlConfiguration y = entry.getValue();
			if(y.get("PlayerAssociatedType") == null)
			{
				continue;
			}
			try
			{
				String internName = entry.getKey();
				String displayName = y.getString("Displayname");
				
				PlayerAssociatedType playerAssociatedType = PlayerAssociatedType.valueOf(y.getString("PlayerAssociatedType"));
				String groupAssociatedPermission = y.getString("GroupAssociatedPermission", null);
				int guiSlot = y.getInt("GuiSlot");
				boolean useFixGuiSlot = y.getBoolean("UseFixGuiSlots");
				List<String> seeRequirementConditionQuery = y.getStringList("RequirementToSee.ConditionQuery");
				boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt = y.getBoolean("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt");
				MainCategory mc = new MainCategory(internName, displayName,
						playerAssociatedType, groupAssociatedPermission,
						guiSlot, useFixGuiSlot, 
						seeRequirementConditionQuery, seeRequirementShowDifferentItemIfYouNormallyDontSeeIt);
				if(playerAssociatedType == PlayerAssociatedType.SOLO)
				{
					mainCategoryMapSolo.put(internName, mc);
				} else if(playerAssociatedType == PlayerAssociatedType.GROUP)
				{
					mainCategoryMapGroup.put(internName, mc);
				} else if(playerAssociatedType == PlayerAssociatedType.GLOBAL)
				{
					mainCategoryMapGlobal.put(internName, mc);
				}
				LinkedHashMap<Integer, MainCategory> map = new LinkedHashMap<>();
				if(playerAssocMainCategoryMap.containsKey(playerAssociatedType))
				{
					map = playerAssocMainCategoryMap.get(playerAssociatedType);
				}
				map.put(guiSlot, mc);
				playerAssocMainCategoryMap.put(playerAssociatedType, map);
			} catch(Exception e)
			{
				continue;
			}
		}
		registerBonusMalus();
		registerCondition();
		return true;
	}
	
	private static void registerBonusMalus()
	{
		if(plugin.getModifier() == null)
		{
			return;
		}
		List<RewardType> rewardTypeList = new ArrayList<RewardType>(EnumSet.allOf(RewardType.class));
		List<EventType> eventTypeList = new ArrayList<EventType>(EnumSet.allOf(EventType.class));
		List<Material> materialList = new ArrayList<Material>(EnumSet.allOf(Material.class));
		List<EntityType> entityTypeList = new ArrayList<EntityType>(EnumSet.allOf(EntityType.class));
		ModificationType bmt = ModificationType.UP;
		for(Material ma : materialList)
		{
			for(EventType e : eventTypeList)
			{
				for(RewardType r : rewardTypeList)
				{
					if(r == RewardType.ACCESS)
					{
						continue;
					}
					String bm = getModifier(r, e, ma, null);
					if(bm == null)
					{
						continue;
					}
					if(plugin.getModifier().isRegistered(bm))
					{
						continue;
					}
					List<String> lar = plugin.getYamlHandler().getMVELang().getStringList(
							ma.toString()+"."+e.toString()+"."+r.toString()+".Explanation");
					plugin.getModifier().register(
							bm,
							plugin.getYamlHandler().getMVELang().getString(
									ma.toString()+"."+e.toString()+"."+r.toString()+".Displayname", ma.toString()+"_"+e.toString()+"_"+r.toString()),
							bmt,
							lar.toArray(new String[lar.size()]));
				}
			}
		}
		for(EntityType et : entityTypeList)
		{
			for(EventType e : eventTypeList)
			{
				for(RewardType r : rewardTypeList)
				{
					if(r == RewardType.ACCESS)
					{
						continue;
					}
					String bm = getModifier(r, e, null, et);
					if(bm == null)
					{
						continue;
					}
					if(plugin.getModifier().isRegistered(bm))
					{
						continue;
					}
					List<String> lar = plugin.getYamlHandler().getMVELang().getStringList(
							et.toString()+"."+e.toString()+"."+r.toString()+".Explanation");
					plugin.getModifier().register(
							bm,
							plugin.getYamlHandler().getMVELang().getString(
									et.toString()+"."+e.toString()+"."+r.toString()+".Displayname", et.toString()+"_"+e.toString()+"_"+r.toString()),
							bmt,
							lar.toArray(new String[lar.size()]));
				}
			}
		}
	}
	
	private static void registerCondition()
	{
		if(plugin.getValueEntry() == null)
		{
			return;
		}
		List<RewardType> rewardTypeList = new ArrayList<RewardType>(EnumSet.allOf(RewardType.class));
		List<EventType> eventTypeList = new ArrayList<EventType>(EnumSet.allOf(EventType.class));
		List<Material> materialList = new ArrayList<Material>(EnumSet.allOf(Material.class));
		List<EntityType> entityTypeList = new ArrayList<EntityType>(EnumSet.allOf(EntityType.class));
		for(Material ma : materialList)
		{
			for(EventType e : eventTypeList)
			{
				for(RewardType r : rewardTypeList)
				{
					if(r != RewardType.ACCESS)
					{
						continue;
					}
					String bm = getValueEntry(r, e, ma, null);
					if(bm == null)
					{
						continue;
					}
					if(plugin.getValueEntry().isRegistered(bm))
					{
						continue;
					}
					List<String> lar = plugin.getYamlHandler().getMVELang().getStringList(
							ma.toString()+"."+e.toString()+"."+r.toString()+".Explanation");
					plugin.getValueEntry().register(
							bm,
							plugin.getYamlHandler().getMVELang().getString(
									ma.toString()+"."+e.toString()+"."+r.toString()+".Displayname", ma.toString()+"_"+e.toString()+"_"+r.toString()),
							lar.toArray(new String[lar.size()]));
				}
			}
		}
		for(EntityType et : entityTypeList)
		{
			for(EventType e : eventTypeList)
			{
				for(RewardType r : rewardTypeList)
				{
					if(r == RewardType.ACCESS)
					{
						continue;
					}
					String c = getValueEntry(r, e, null, et);
					if(c == null)
					{
						continue;
					}
					if(plugin.getValueEntry().isRegistered(c))
					{
						continue;
					}
					List<String> lar = plugin.getYamlHandler().getMVELang().getStringList(
							et.toString()+"."+e.toString()+"."+r.toString()+".Explanation");
					plugin.getValueEntry().register(
							c,
							plugin.getYamlHandler().getMVELang().getString(
									et.toString()+"."+e.toString()+"."+r.toString()+".Displayname", et.toString()+"_"+e.toString()+"_"+r.toString()),
							lar.toArray(new String[lar.size()]));
				}
			}
		}
	}
	
	public static String getModifier(RewardType rewardType, EventType eventType, Material material, EntityType entityType)
	{
		if(material != null)
		{
			return BaseConstructor.getPlugin().pluginName.toLowerCase()
					+"-"+material.toString().toLowerCase()
					+"-"+eventType.toString().toLowerCase()
					+"-"+rewardType.toString().toLowerCase();
		} else if(eventType != null)
		{
			return BaseConstructor.getPlugin().pluginName.toLowerCase()
					+"-"+entityType.toString().toLowerCase()
					+"-"+eventType.toString().toLowerCase()
					+"-"+rewardType.toString().toLowerCase();
		}
		return null;
	}
	
	public static String getValueEntry(RewardType rewardType, EventType eventType, Material material, EntityType entityType)
	{
		return getModifier(rewardType, eventType, material, entityType);
	}
}