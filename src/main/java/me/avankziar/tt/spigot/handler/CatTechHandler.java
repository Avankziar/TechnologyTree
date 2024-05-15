package main.java.me.avankziar.tt.spigot.handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.assistance.MatchApi;
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
	
	public static int totalTech = 0;
	public static int totalSoloTech = 0;
	public static int totalGroupTech = 0;
	public static int totalGlobalTech = 0;
	
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
				double forUninvolvedPollParticipants_RewardEnchantmentOffersInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardEnchantmentOffersInPercent", 100);
				double forUninvolvedPollParticipants_RewardEnchantmentsInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardEnchantmentsInPercent", 100);
				double forUninvolvedPollParticipants_RewardCommandsInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardCommandsInPercent", 100);
				double forUninvolvedPollParticipants_RewardItemsInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardItemsInPercent", 100);
				double forUninvolvedPollParticipants_RewardModifiersInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardModifiersInPercent", 100);
				double forUninvolvedPollParticipants_RewardValueEntryInPercent
				= y.getDouble("OnlyForGlobal.ForUninvolvedPollParticipants.RewardValueEntryInPercent", 100);
				
				List<String> seeRequirementConditionQuery = new ArrayList<>();
				if(y.get("RequirementToSee.ConditionQuery") != null)
				{
					seeRequirementConditionQuery = y.getStringList("RequirementToSee.ConditionQuery");
				} else
				{
					seeRequirementConditionQuery.add("if:(a):o_1");
					seeRequirementConditionQuery.add("else:o_2");
					seeRequirementConditionQuery.add("output:o_1:true");
					seeRequirementConditionQuery.add("output:o_2:false");
					seeRequirementConditionQuery.add("a:true");
				}
				boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt 
				= y.getBoolean("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt");
				
				LinkedHashMap<Integer, List<String>> researchRequirementConditionQuery = new LinkedHashMap<>();
				List<String> standartConQ = new ArrayList<>();
				standartConQ.add("if:(a):o_1");
				standartConQ.add("else:o_2");
				standartConQ.add("output:o_1:true");
				standartConQ.add("output:o_2:false");
				standartConQ.add("a:true");
				if(y.get("RequirementToResearch.ConditionQuery") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("RequirementToResearch.ConditionQuery."+i) == null)
						{
							//If for specific level are no conditionquerys stated, do always return standart query.
							researchRequirementConditionQuery.put(i, standartConQ);
							continue;
						}
						researchRequirementConditionQuery.put(i, y.getStringList("RequirementToResearch.ConditionQuery."+i));
					}
				} else
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						researchRequirementConditionQuery.put(i, standartConQ);
					}
				}
				
				LinkedHashMap<Integer, String> costTTExp = new LinkedHashMap<>();
				if(y.get("RequirementToResearch.Costs.TTExp") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("RequirementToResearch.Costs.TTExp."+i) != null)
						{
							costTTExp.put(i, y.getString("RequirementToResearch.Costs.TTExp."+i, ""));
						}
					}
				}
				LinkedHashMap<Integer, String> costVanillaExp = new LinkedHashMap<>();
				if(y.get("RequirementToResearch.Costs.VanillaExp") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("RequirementToResearch.Costs.VanillaExp."+i) != null)
						{
							costVanillaExp.put(i, y.getString("RequirementToResearch.Costs.VanillaExp."+i, ""));
						}
					}
				}
				LinkedHashMap<Integer, String> costMoney = new LinkedHashMap<>();
				if(y.get("RequirementToResearch.Costs.Money") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("RequirementToResearch.Costs.Money."+i) != null)
						{
							costMoney.put(i, y.getString("RequirementToResearch.Costs.Money."+i, ""));
						}
					}
				}
				LinkedHashMap<Integer, LinkedHashMap<Material, String>> costMaterial = new LinkedHashMap<>();
				if(y.get("RequirementToResearch.Costs.Material") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("RequirementToResearch.Costs.Material."+i) == null)
						{
							continue;
						}
						LinkedHashMap<Material, String> m = new LinkedHashMap<>();
						for(String s : y.getStringList("RequirementToResearch.Costs.Material."+i))
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
								m.put(mat, amount);
							} catch(Exception e)
							{
								TT.log.info("Technology "+internName+" has a Typing Error!");
								continue;
							}
						}
						costMaterial.put(i, m);
					}
				}
				
				LinkedHashMap<Integer, ArrayList<UnlockableInteraction>> rewardUnlockableInteractions = new LinkedHashMap<>();
				if(y.get("Rewards.UnlockableInteractions") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("Rewards.UnlockableInteractions."+i) == null)
						{
							continue;
						}
						ArrayList<UnlockableInteraction> rui = new ArrayList<>();
						for(String s : y.getStringList("Rewards.UnlockableInteractions."+i))
						{
							String[] split  = s.split(":");
							if(split.length < 3)
							{
								continue;
							}
							try
							{
								EventType eventType = EventType.valueOf(split[0]);
								Material material = split[1].equals("null") ? null : Material.valueOf(split[1]);
								EntityType entityType = split[2].equals("null") ? null : EntityType.valueOf(split[2]);
								UnlockableInteraction ui = new UnlockableInteraction(eventType, ToolType.HAND, material, entityType, true, 0.0, 0.0);
								for(int ii = 3; ii < split.length; ii++)
								{
									String[] sp = split[ii].split("=");
									if(split[ii].startsWith("tool") && sp.length == 2)
									{
										try
										{
											ToolType.valueOf(sp[1]);
										} catch(Exception e)
										{
											TT.log.warning("Tech '"+internName+"' hasnt found by tool a ToolType where it should be!");
											continue;
										}
										ui.setToolType(ToolType.valueOf(sp[1]));
									} else if(split[ii].startsWith("canAccess") && sp.length == 2)
									{
										if(!MatchApi.isBoolean(sp[1]))
										{
											TT.log.warning("Tech '"+internName+"' hasnt found by canAccess a boolean(true/false) where it should be!");
											continue;
										}
										ui.setCanAccess(Boolean.valueOf(sp[1]));
									} else if(split[ii].startsWith("ttexp") && sp.length == 2)
									{
										if(!MatchApi.isDouble(sp[1]))
										{
											TT.log.warning("Tech '"+internName+"' hasnt found by ttexp a double where it should be!");
											continue;
										}
										ui.setTechnologyExperience(Double.parseDouble(sp[1]));
									} else if(split[ii].startsWith("vaexp") && sp.length == 2)
									{
										if(!MatchApi.isInteger(sp[1]))
										{
											TT.log.warning("Tech '"+internName+"' hasnt found by vanilla exp a integer where it should be!");
											continue;
										}
										ui.setVanillaExperience(Integer.valueOf(sp[1]));
									} else if(split[ii].startsWith("cmd") && sp.length == 3)
									{
										if(!MatchApi.isDouble(sp[2]))
										{
											TT.log.warning("Tech '"+internName+"' hasnt found by command a double where it should be!");
											continue;
										}
										ui.addCommandValues(sp[1], Double.parseDouble(sp[2]));
									} else if(sp.length == 2) //Money
									{
										if(!MatchApi.isDouble(sp[1]))
										{
											TT.log.warning("Tech '"+internName+"' hasnt found by money a double where it should be!");
											continue;
										}
										ui.addMoneyValues(sp[0], Double.parseDouble(sp[1]));
									} 
								}
								rui.add(ui);
							} catch(Exception e)
							{
								TT.log.log(Level.WARNING ,"Tech '"+internName+"' Error by UnlockableInteractions, Lvl: "+i);
								continue;
							}
						}
						rewardUnlockableInteractions.put(i, rui);
					}
				}
				LinkedHashMap<Integer, LinkedHashMap<RecipeType, ArrayList<String>>> rewardRecipes = new LinkedHashMap<>();
				if(y.get("Rewards.UnlockableRecipe") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("Rewards.UnlockableRecipe."+i) == null)
						{
							continue;
						}
						LinkedHashMap<RecipeType, ArrayList<String>> rr = new LinkedHashMap<>();
						for(String s : y.getStringList("Rewards.UnlockableRecipe."+i))
						{
							String[] split  = s.split(":");
							if(s.isBlank() || s.isEmpty() || split.length != 2)
							{
								continue;
							}
							try
							{
								RecipeType rt = RecipeType.valueOf(split[0]);
								String key = split[1];
								String keys = key;
								if(rt == RecipeType.BLASTING || rt == RecipeType.CAMPFIRE || rt == RecipeType.FURNACE
										|| rt == RecipeType.SHAPED || rt == RecipeType.SHAPELESS || rt == RecipeType.SMOKING
										) //Annotation, STONECUTTING is not there, because is has no Event to grab. It only added more Recipes.
								{
									if(!RecipeHandler.recipeMap.containsKey(rt))
									{	
										TT.log.log(Level.WARNING ,"Tech '"+internName+"' Error by UnlockableRecipe, Lvl: "+i);
										TT.log.log(Level.WARNING ,"Recipe '"+key+"' in RecipeType '"+rt.toString()+"' are not found!");
										continue;
									}
									ArrayList<String> rtal = RecipeHandler.recipeMap.get(rt);
									if(!key.contains("-") && Character.isLowerCase(key.charAt(0)))
									{
										keys = "minecraft-"+key;
									}
									if(!rtal.contains(keys))
									{
										TT.log.log(Level.WARNING ,"Tech '"+internName+"' Error by UnlockableRecipe, Lvl: "+i);
										TT.log.log(Level.WARNING ,"Recipe '"+keys+"' in RecipeType '"+rt.toString()+"' are not found!");
										continue;
									}
								}
								ArrayList<String> list = new ArrayList<>();
								if(rr.containsKey(rt))
								{
									list = rr.get(rt);
								}
								if(!list.contains(keys))
								{
									list.add(keys);
								}
								rr.put(rt, list);
							} catch(Exception e)
							{
								TT.log.warning("Tech '"+internName+"' hasnt found by UnlockableRecipe a RecipeType where it should be!");
								continue;
							}
						}
						rewardRecipes.put(i, rr);
					}
				}
				LinkedHashMap<Integer, ArrayList<DropChance>> rewardDropChances = new LinkedHashMap<>();
				if(y.get("Rewards.DropChance") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("Rewards.DropChance."+i) == null)
						{
							continue;
						}
						ArrayList<DropChance> adc = new ArrayList<>();
						for(String s : y.getStringList("Rewards.DropChance."+i))
						{							
							String[] split  = s.split(":");
							if(s.isBlank() || s.isEmpty() || split.length != 7)
							{
								continue;
							}
							try
							{
								EventType eventType = EventType.valueOf(split[0]);
								ToolType tool = ToolType.valueOf(split[1]);
								Material material = null;
								EntityType entityType = null;
								if(!split[2].equalsIgnoreCase("null"))
								{
									material = Material.valueOf(split[2]);
								}
								if(!split[3].equalsIgnoreCase("null"))
								{
									entityType = EntityType.valueOf(split[3]);
								}
								String item = split[4];
								int amount = Integer.parseInt(split[5]);
								DropChance dc = new DropChance(eventType, tool, material, entityType, item, amount, Double.parseDouble(split[6]));
								adc.add(dc);
							} catch(Exception e)
							{
								TT.log.log(Level.WARNING ,"Tech '"+internName+"' Error by DropChance, Lvl: "+i);
								continue;
							}
						}
						rewardDropChances.put(i, adc);
					}
				}
				LinkedHashMap<Integer, ArrayList<DropChance>> rewardSilkTouchDropChances = new LinkedHashMap<>();
				if(y.get("Rewards.SilkTouchDropChance") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("Rewards.SilkTouchDropChance."+i) == null)
						{
							continue;
						}
						ArrayList<DropChance> adc = new ArrayList<>();
						for(String s : y.getStringList("Rewards.SilkTouchDropChance."+i))
						{
							String[] split  = s.split(":");
							if(s.isBlank() || s.isEmpty() || split.length != 7)
							{
								continue;
							}
							try
							{
								EventType eventType = EventType.valueOf(split[0]);
								ToolType tool = ToolType.valueOf(split[1]);
								Material material = null;
								EntityType entityType = null;
								if(!split[2].equalsIgnoreCase("null"))
								{
									material = Material.valueOf(split[2]);
								}
								if(!split[3].equalsIgnoreCase("null"))
								{
									entityType = EntityType.valueOf(split[3]);
								}
								String item = split[4];
								int amount = Integer.parseInt(split[5]);
								DropChance dc = new DropChance(eventType, tool, material, entityType, item, amount, Double.parseDouble(split[6]));
								adc.add(dc);								
							} catch(Exception e)
							{
								TT.log.log(Level.WARNING ,"Tech '"+internName+"' Error by SilkTouchDropChance, Lvl: "+i);
								continue;
							}
						}
						rewardSilkTouchDropChances.put(i, adc);
					}
				}
				LinkedHashMap<Integer, ArrayList<Integer>> rewardEnchantmentOffers = new LinkedHashMap<>();
				if(y.get("Rewards.UnlockableEnchantmentOffers") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("Rewards.UnlockableEnchantmentOffers."+i) == null)
						{
							continue;
						}
						ArrayList<Integer> ilist = new ArrayList<>();
						for(String s : y.getStringList("Rewards.UnlockableEnchantmentOffers."+i))
						{
							if(MatchApi.isInteger(s))
							{
								ilist.add(Integer.parseInt(s));
							}
						}
						rewardEnchantmentOffers.put(i, ilist);
					}
				}
				LinkedHashMap<Integer, LinkedHashMap<Material, ArrayList<String>>> rewardEnchantments = new LinkedHashMap<>();
				if(y.get("Rewards.UnlockableEnchantments") != null)
				{
					for(int i = 1; i <= maximalTechnologyLevelToResearch; i++)
					{
						if(y.get("Rewards.UnlockableEnchantments."+i) == null)
						{
							continue;
						}
						LinkedHashMap<Material, ArrayList<String>> matmap = new LinkedHashMap<>();
						for(String a : y.getStringList("Rewards.UnlockableEnchantments."+i))
						{
							String[] s = a.split(":");
							if(s.length != 2)
							{
								continue;
							}
							try
							{
								ArrayList<String> l = new ArrayList<>();
								Material mat = Material.valueOf(s[0]);
								String ench = s[1];
								if(matmap.containsKey(mat))
								{
									l = matmap.get(mat);
								}
								if(!l.contains(ench))
								{
									l.add(ench);
								}
								matmap.put(mat, l);
							} catch(Exception e)
							{
								TT.log.log(Level.WARNING ,"Tech '"+internName+"' Error by UnlockableEnchantments, Lvl: "+i);
								continue;
							}
						}
						rewardEnchantments.put(i, matmap);
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
						forUninvolvedPollParticipants_RewardEnchantmentOffersInPercent,
						forUninvolvedPollParticipants_RewardEnchantmentsInPercent,
						forUninvolvedPollParticipants_RewardCommandsInPercent,
						forUninvolvedPollParticipants_RewardItemsInPercent,
						forUninvolvedPollParticipants_RewardModifiersInPercent,
						forUninvolvedPollParticipants_RewardValueEntryInPercent,
						guiSlot,
						seeRequirementConditionQuery, seeRequirementShowDifferentItemIfYouNormallyDontSeeIt, researchRequirementConditionQuery, 
						costTTExp, costVanillaExp, costMoney, costMaterial,
						rewardUnlockableInteractions, rewardRecipes, rewardDropChances, rewardSilkTouchDropChances,
						rewardEnchantmentOffers, rewardEnchantments,
						rewardCommandList, rewardItemList, rewardModifierList, rewardValueEntryList);
				totalTech += t.getMaximalTechnologyLevelToResearch();
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
					totalSoloTech += t.getMaximalTechnologyLevelToResearch();
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
					totalGroupTech += t.getMaximalTechnologyLevelToResearch();
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
					totalGlobalTech += t.getMaximalTechnologyLevelToResearch();
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
				int guiSlot = y.getInt("GuiSlot");
				List<String> seeRequirementConditionQuery = new ArrayList<>();
				if(y.get("RequirementToSee.ConditionQuery") != null)
				{
					seeRequirementConditionQuery = y.getStringList("RequirementToSee.ConditionQuery");
				} else
				{
					seeRequirementConditionQuery.add("if:(a):o_1");
					seeRequirementConditionQuery.add("output:o_1:true");
					seeRequirementConditionQuery.add("a:true");
				}
				boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt = y.getBoolean("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt");
				String overlyingCategory = y.getString("IfSubCategory.OverlyingMainCategory");
				SubCategory sc = new SubCategory(internName, displayName,
						playerAssociatedType,
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
				List<String> seeRequirementConditionQuery = new ArrayList<>();
				if(y.get("RequirementToSee.ConditionQuery") != null)
				{
					seeRequirementConditionQuery = y.getStringList("RequirementToSee.ConditionQuery");
				} else
				{
					seeRequirementConditionQuery.add("if:(a):o_1");
					seeRequirementConditionQuery.add("output:o_1:true");
					seeRequirementConditionQuery.add("a:true");
				}
				boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt = y.getBoolean("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt");
				MainCategory mc = new MainCategory(internName, displayName,
						playerAssociatedType, groupAssociatedPermission,
						guiSlot,
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
		logLoadedTechs("Loaded Global Maincategories: ", mainCategoryMapGlobal.keySet());
		logLoadedTechs("Loaded Global Subcategories: ", subCategoryMapGlobal.keySet());
		logLoadedTechs("Loaded Global Technologies: ", technologyMapGlobal.keySet());
		logLoadedTechs("Loaded Group Maincategories: ", mainCategoryMapGroup.keySet());
		logLoadedTechs("Loaded Group Subcategories: ", subCategoryMapGroup.keySet());
		logLoadedTechs("Loaded Group Technologies: ", technologyMapGroup.keySet());
		logLoadedTechs("Loaded Solo Maincategories: ", mainCategoryMapSolo.keySet());
		logLoadedTechs("Loaded Solo Subcategories: ", subCategoryMapSolo.keySet());
		logLoadedTechs("Loaded Solo Technologies: ", technologyMapSolo.keySet());
		return true;
	}
	
	private static void logLoadedTechs(String a, Set<String> set)
	{
		ArrayList<String> arr = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(String s : set)
		{
			if(!sb.isEmpty())
			{
				sb.append(", ");
			}
			sb.append(s);
			i++;
			if(i >= 10)
			{
				arr.add(sb.toString());
				sb = new StringBuilder();
				i = 0;
			}
		}
		if(!sb.isEmpty())
		{
			arr.add(sb.toString());
		}
		TT.log.info(a);
		for(String s : arr)
		{
			TT.log.info(s);
		}
	}
	
	public static Technology getTechnology(String uniqueIdentifier, PlayerAssociatedType pat)
	{
		switch(pat)
		{
		case GLOBAL: return technologyMapGlobal.get(uniqueIdentifier);
		case GROUP: return technologyMapGroup.get(uniqueIdentifier);
		case SOLO: return technologyMapSolo.get(uniqueIdentifier);
		}
		return null;
	}
	
	public static Technology getTechnology(String uniqueIdentifier)
	{
		Technology t = technologyMapGlobal.get(uniqueIdentifier);
		if(t == null)
		{
			t = technologyMapGroup.get(uniqueIdentifier);
			if(t == null)
			{
				t = technologyMapSolo.get(uniqueIdentifier);
			}
		}
		return t;
	}
	
	public static SubCategory getSubCategory(Technology t, PlayerAssociatedType pat)
	{
		switch(pat)
		{
		case GLOBAL:
			return subCategoryMapGlobal.get(t.getOverlyingSubCategory());
		case GROUP:
			return subCategoryMapGroup.get(t.getOverlyingSubCategory());
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
		case GROUP:
			return mainCategoryMapGroup.get(sc.getOverlyingCategory());
		case SOLO:
			return mainCategoryMapSolo.get(sc.getOverlyingCategory());
		}
		return null;
	}
	
	public static String getModifier(RewardType rewardType, EventType eventType, Material material, EntityType entityType)
	{
		if(material != null)
		{
			return BaseConstructor.getPlugin().pluginName.toLowerCase()
					+"-"+material.toString().toLowerCase()
					+"-"+eventType.toString().toLowerCase()
					+"-"+rewardType.toString().toLowerCase();
		} else if(entityType != null)
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