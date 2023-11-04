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
import main.java.me.avankziar.tt.spigot.objects.ToolType;
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
				
				int guiSlot = y.getInt("GuiSlot");
				guiSlot = guiSlot < 0 ? 0 : guiSlot;
				guiSlot = guiSlot > 54 ? 54 : guiSlot;
				
				long ifBoosterDurationUntilExpiration = -1;
				if(technologyType == TechnologyType.BOOSTER)
				{
					String parse = y.getString("IfBoosterDurationUntilExpiration");
					String[] split = parse.split("-");
					if(split.length == 4)
					{
						if(split[0].endsWith("d"))
						{
							ifBoosterDurationUntilExpiration += Long.valueOf(split[0].substring(0, split[0].length()-1)) * 24 * 60 * 60 * 1000;
						}
						if(split[1].endsWith("H"))
						{
							ifBoosterDurationUntilExpiration += Long.valueOf(split[1].substring(0, split[1].length()-1)) * 60 * 60 * 1000;
						}
						if(split[2].endsWith("m"))
						{
							ifBoosterDurationUntilExpiration += Long.valueOf(split[2].substring(0, split[2].length()-1)) * 60 * 1000;
						}
						if(split[3].endsWith("s"))
						{
							ifBoosterDurationUntilExpiration += Long.valueOf(split[3].substring(0, split[3].length()-1)) * 1000;
						}
					}
				}
				
				String overlyingSubCategory = y.getString("OverlyingSubCategory");
				
				double forUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardUnlockableInteractionsInPercent", 100);
				double forUninvolvedPollParticipants_RewardRecipesInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardRecipesInPercent", 100);
				double forUninvolvedPollParticipants_RewardDropChancesInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardDropChancesInPercent", 100);
				double forUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardSilkTouchDropChancesInPercent", 100);
				double forUninvolvedPollParticipants_RewardCommandsInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardCommandsInPercent", 100);
				double forUninvolvedPollParticipants_RewardItemsInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardItemsInPercent", 100);
				double forUninvolvedPollParticipants_RewardModifiersInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardModifiersInPercent", 100);
				double forUninvolvedPollParticipants_RewardValueEntryInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardValueEntryInPercent", 100);
				
				if(y.get("RequirementToSee.ConditionQuery") == null)
				{
					continue;
				}
				List<String> seeRequirementConditionQuery = y.getStringList("RequirementToSee.ConditionQuery");
				boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt 
				= y.getBoolean("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt");
				
				if(y.get("RequirementToResearch.ConditionQuery") == null)
				{
					continue;
				}
				LinkedHashMap<Integer, List<String>> researchRequirementConditionQuery = new LinkedHashMap<>();
				if(y.get("RequirementToResearch.ConditionQuery") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("RequirementToResearch.ConditionQuery."+i) == null)
						{
							continue;
						}
						researchRequirementConditionQuery.put(i, y.getStringList("RequirementToResearch.ConditionQuery."+i));
					}
				}
				
				LinkedHashMap<Integer, String> costTTExp = new LinkedHashMap<>();
				if(y.get("RequirementToResearch.Costs.TTExp") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						costTTExp.put(i, y.getString("RequirementToResearch.Costs.TTExp."+i, ""));
					}
				}
				LinkedHashMap<Integer, String> costVanillaExp = new LinkedHashMap<>();
				if(y.get("RequirementToResearch.Costs.VanillaExp") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						costVanillaExp.put(i, y.getString("RequirementToResearch.Costs.VanillaExp."+i, ""));
					}
				}
				LinkedHashMap<Integer, String> costMoney = new LinkedHashMap<>();
				if(y.get("RequirementToResearch.Costs.Money") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						costMoney.put(i, y.getString("RequirementToResearch.Costs.Money."+i, ""));
					}
				}
				LinkedHashMap<Integer, LinkedHashMap<Material, String>> costMaterial = new LinkedHashMap<>();
				if(y.get("RequirementToResearch.Costs.Material") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.getStringList("RequirementToResearch.Costs.Material."+i) == null)
						{
							continue;
						}
						for(String s : y.getStringList("RequirementToResearch.Costs.Material."+i))
						{
							LinkedHashMap<Material, String> m = new LinkedHashMap<>();
							String[] split = s.split(";");
							if(split.length != 2)
							{
								continue;
							}
							try
							{
								Material mat = Material.valueOf(split[0]);
								String amount = split[1];
								m.put(mat, amount);
								costMaterial.put(i, m);
							} catch(Exception e)
							{
								continue;
							}
						}
					}
				}
				
				LinkedHashMap<Integer, ArrayList<UnlockableInteraction>> rewardUnlockableInteractions = new LinkedHashMap<>();
				if(y.get("Rewards.UnlockableInteractions") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						for(String s : y.getStringList("Rewards.UnlockableInteractions."+i))
						{
							ArrayList<UnlockableInteraction> rui = new ArrayList<>();
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
								UnlockableInteraction ui = new UnlockableInteraction(eventType, ToolType.ALL, material, entityType, true, 0.0, 0.0);
								for(int ii = 3; i < split.length; i++)
								{
									String[] sp = split[i].split("=");
									if(split[ii].startsWith("tooltype") && sp.length == 2)
									{
										ui.setToolType(ToolType.valueOf(sp[1]));
									}if(split[ii].startsWith("canAccess") && sp.length == 2)
									{
										ui.setCanAccess(Boolean.valueOf(sp[1]));
									} else if(split[ii].startsWith("ttexp") && sp.length == 2)
									{
										ui.setTechnologyExperience(Double.parseDouble(sp[1]));
									} else if(split[ii].startsWith("vaexp") && sp.length == 2)
									{
										ui.setVanillaExperience(Integer.valueOf(sp[1]));
									} else if(split[ii].startsWith("cmd") && sp.length == 3)
									{
										ui.addCommandValues(sp[1], Double.parseDouble(sp[2]));
									} else if(sp.length == 2) //Money
									{
										ui.addMoneyValues(sp[0], Double.parseDouble(sp[1]));
									} 
								}
								rui.add(ui);
								rewardUnlockableInteractions.put(i, rui);
							} catch(Exception e)
							{
								continue;
							}
						}
					}
				}
				LinkedHashMap<Integer, LinkedHashMap<RecipeType, ArrayList<String>>> rewardRecipes = new LinkedHashMap<>();
				if(y.get("Rewards.UnlockableRecipe") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						for(String s : y.getStringList("Rewards.UnlockableRecipe."+i))
						{
							LinkedHashMap<RecipeType, ArrayList<String>> rr = new LinkedHashMap<>();
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
								if(rr.containsKey(rt))
								{
									list = rr.get(rt);
								}
								if(!list.contains(key))
								{
									list.add(key);
								}
								rr.put(rt, list);
								rewardRecipes.put(i, rr);
							} catch(Exception e)
							{
								continue;
							}
						}
					}
				}
				LinkedHashMap<Integer, ArrayList<DropChance>> rewardDropChances = new LinkedHashMap<>();
				if(y.get("Rewards.DropChance") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						for(String s : y.getStringList("Rewards.DropChance"))
						{
							ArrayList<DropChance> adc = new ArrayList<>();
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
								adc.add(dc);
								rewardDropChances.put(i, adc);
							} catch(Exception e)
							{
								continue;
							}
						}
					}
				}
				LinkedHashMap<Integer, ArrayList<DropChance>> rewardSilkTouchDropChances = new LinkedHashMap<>();
				if(y.get("Rewards.SilkTouchDropChance") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						for(String s : y.getStringList("Rewards.SilkTouchDropChance."+i))
						{
							ArrayList<DropChance> adc = new ArrayList<>();
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
								adc.add(dc);
								rewardSilkTouchDropChances.put(i, adc);
							} catch(Exception e)
							{
								continue;
							}
						}
					}
				}
				LinkedHashMap<Integer, ArrayList<String>> rewardCommandList = new LinkedHashMap<>();
				if(y.get("Rewards.Command") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("Rewards.Command."+i) == null)
						{
							continue;
						}
						rewardCommandList.put(i, (ArrayList<String>) y.getStringList("Rewards.Command."+i));
					}
				}
				LinkedHashMap<Integer, ArrayList<String>> rewardItemList = new LinkedHashMap<>();
				if(y.get("Rewards.Item") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("Rewards.Item."+i) == null)
						{
							continue;
						}
						rewardItemList.put(i, (ArrayList<String>) y.getStringList("Rewards.Item."+i));
					}
				}
				LinkedHashMap<Integer, ArrayList<String>> rewardModifierList = new LinkedHashMap<>();
				if(plugin.getModifier() != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("Rewards.Modifier."+i) == null)
						{
							continue;
						}
						rewardModifierList.put(i, (ArrayList<String>) y.getStringList("Rewards.Modifier."+i));
					}
				}
				LinkedHashMap<Integer, ArrayList<String>> rewardValueEntryList = new LinkedHashMap<>();
				if(plugin.getValueEntry() != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("Rewards.ValueEntry."+i) == null)
						{
							continue;
						}
						rewardValueEntryList.put(i, (ArrayList<String>) y.getStringList("Rewards.ValueEntry."+i));
					}
				}
				Technology t = new Technology(internName, displayName, technologyType, maximalTechnologyLevelToResearch,
						ifBoosterDurationUntilExpiration,
						playerAssociatedType, overlyingSubCategory,
						forUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent,
						forUninvolvedPollParticipants_RewardRecipesInPercent,
						forUninvolvedPollParticipants_RewardDropChancesInPercent,
						forUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent,
						forUninvolvedPollParticipants_RewardCommandsInPercent,
						forUninvolvedPollParticipants_RewardItemsInPercent,
						forUninvolvedPollParticipants_RewardModifiersInPercent,
						forUninvolvedPollParticipants_RewardValueEntryInPercent,
						guiSlot,
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
				}/* else if(playerAssociatedType == PlayerAssociatedType.GROUP)
				{
					technologyMapGroup.put(internName, t);
					LinkedHashMap<Integer, Technology> map = new LinkedHashMap<>();
					if(subCategoryTechnologyMapGroup.containsKey(overlyingSubCategory))
					{
						map = subCategoryTechnologyMapGroup.get(overlyingSubCategory);
					}
					map.put(guiSlot, t);
					subCategoryTechnologyMapGroup.put(overlyingSubCategory, map);
				}*/ else if(playerAssociatedType == PlayerAssociatedType.GLOBAL)
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
				List<String> seeRequirementConditionQuery = y.getStringList("RequirementToSee.ConditionQuery");
				boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt = y.getBoolean("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt");
				String overlyingCategory = y.getString("IfSubCategory.OverlyingMainCategory");
				SubCategory sc = new SubCategory(internName, displayName,
						playerAssociatedType, groupAssociatedPermission,
						guiSlot,
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
				}/* else if(playerAssociatedType == PlayerAssociatedType.GROUP)
				{
					subCategoryMapGroup.put(internName, sc);
					LinkedHashMap<Integer, SubCategory> map = new LinkedHashMap<>();
					if(mainCategorySubCategoryMapGroup.containsKey(overlyingCategory))
					{
						map = mainCategorySubCategoryMapGroup.get(overlyingCategory);
					}
					map.put(guiSlot, sc);
					mainCategorySubCategoryMapGroup.put(overlyingCategory, map);
				}*/ else if(playerAssociatedType == PlayerAssociatedType.GLOBAL)
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
				List<String> seeRequirementConditionQuery = y.getStringList("RequirementToSee.ConditionQuery");
				boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt = y.getBoolean("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt");
				MainCategory mc = new MainCategory(internName, displayName,
						playerAssociatedType, groupAssociatedPermission,
						guiSlot,
						seeRequirementConditionQuery, seeRequirementShowDifferentItemIfYouNormallyDontSeeIt);
				if(playerAssociatedType == PlayerAssociatedType.SOLO)
				{
					mainCategoryMapSolo.put(internName, mc);
				}/* else if(playerAssociatedType == PlayerAssociatedType.GROUP)
				{
					mainCategoryMapGroup.put(internName, mc);
				}*/ else if(playerAssociatedType == PlayerAssociatedType.GLOBAL)
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
	
	public static Technology getTechnology(String uniqueIdentifier, PlayerAssociatedType pat)
	{
		switch(pat)
		{
		case GLOBAL:
			return technologyMapGlobal.get(uniqueIdentifier);
		case SOLO:
			return technologyMapSolo.get(uniqueIdentifier);
		}
		return null;
	}
	
	public static SubCategory getSubCategory(Technology t, PlayerAssociatedType pat)
	{
		switch(pat)
		{
		case GLOBAL:
			return subCategoryMapGlobal.get(t.getOverlyingSubCategory());
		/*case GROUP:
			return subCategoryMapGroup.get(t.getOverlyingSubCategory());*/
		case SOLO:
			return subCategoryMapSolo.get(t.getOverlyingSubCategory());
		}
		return null;
	}
	
	public static MainCategory getMainCategory(SubCategory sc, PlayerAssociatedType pat)
	{
		switch(pat)
		{
		case GLOBAL:
			return mainCategoryMapGlobal.get(sc.getOverlyingCategory());
		/*case GROUP:
			return mainCategoryMapGroup.get(sc.getOverlyingCategory());*/
		case SOLO:
			return mainCategoryMapSolo.get(sc.getOverlyingCategory());
		}
		return null;
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