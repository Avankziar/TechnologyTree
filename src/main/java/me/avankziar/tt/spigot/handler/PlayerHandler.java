package main.java.me.avankziar.tt.spigot.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.action.OrdererType;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.general.math.MathFormulaParser;
import main.java.me.avankziar.ifh.general.modifier.ModifierType;
import main.java.me.avankziar.ifh.general.valueentry.ValueType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.assistance.Experience;
import main.java.me.avankziar.tt.spigot.assistance.MatchApi;
import main.java.me.avankziar.tt.spigot.assistance.TimeHandler;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.gui.objects.SettingsLevel;
import main.java.me.avankziar.tt.spigot.handler.BlockHandler.BlockType;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.ifh.ItemGenerator;
import main.java.me.avankziar.tt.spigot.objects.EntryQueryType;
import main.java.me.avankziar.tt.spigot.objects.EntryStatusType;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.TechnologyType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;
import main.java.me.avankziar.tt.spigot.objects.mysql.GlobalEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
import main.java.me.avankziar.tt.spigot.objects.mysql.RegisteredBlock;
import main.java.me.avankziar.tt.spigot.objects.mysql.SoloEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.DropChance;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.MainCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SimpleDropChance;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SimpleUnlockedInteraction;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SubCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.Technology;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.UnlockableInteraction;

public class PlayerHandler
{
	private static TT plugin = BaseConstructor.getPlugin();
	
	public static LinkedHashMap<UUID, LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, SimpleUnlockedInteraction>>>> materialInteractionMap = new LinkedHashMap<>();
	public static LinkedHashMap<UUID, LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, SimpleUnlockedInteraction>>>> entityTypeInteractionMap = new LinkedHashMap<>();
	
	public static LinkedHashMap<UUID, LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>>>> materialDropMap = new LinkedHashMap<>(); //String ist der Internename vom SimpleDropChance
	public static LinkedHashMap<UUID, LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>>>> entityTypeDropMap = new LinkedHashMap<>();
	
	public static LinkedHashMap<UUID, LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>>>> materialSilkTouchDropMap = new LinkedHashMap<>(); //String ist der Internename vom SimpleDropChance
	public static LinkedHashMap<UUID, LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>>>> entityTypeSilkTouchDropMap = new LinkedHashMap<>();
	
	public static LinkedHashMap<UUID, LinkedHashMap<RecipeType, ArrayList<String>>> recipeMap = new LinkedHashMap<>(); //UUID, RecipeType und der Key des Recipe
	
	public static LinkedHashMap<UUID, LinkedHashMap<BlockType, ArrayList<String>>> registeredBlocks = new LinkedHashMap<>();//UUID, BlockType, Location as Text
	
	public final static String 
		TECHLEVEL = "techlev",
		TECHACQUIRED = "techacq",
		SOLOTOTALTECH = "solototaltech",
		GROUPTOTALTECH = "grouptotaltech",
		GLOBALTOTALTECH = "globaltotaltech";	
	
	//DO ASYNC!
	public static void joinPlayer(final Player player)
	{
		final UUID uuid = player.getUniqueId();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				materialInteractionMap.remove(uuid);
				entityTypeInteractionMap.remove(uuid);
				
				materialDropMap.remove(uuid);
				entityTypeDropMap.remove(uuid);
				
				materialSilkTouchDropMap.remove(uuid);
				entityTypeSilkTouchDropMap.remove(uuid);
				
				recipeMap.remove(uuid);
				
				if(!hasAccount(player))
				{
					createAccount(player);
					for(String s : new ConfigHandler().getAutoResearchedTechnologies())
					{
						Technology t = CatTechHandler.technologyMapSolo.get(s);
						if(t == null)
						{
							continue;
						}
						researchSoloTechnology(player, t, false);
					}
				}
				PlayerData pd = getPlayer(player.getUniqueId());
				if(pd.isShowSyncMessage())
				{
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerHandler.SyncStart")));
				}
				for(SoloEntryQueryStatus eqs : SoloEntryQueryStatus.convert(plugin.getMysqlHandler().getFullList(Type.SOLOENTRYQUERYSTATUS,
						"`id` ASC", "`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ?",
						player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString())))
				{
					if(eqs == null)
					{
						continue;
					}
					Technology t = CatTechHandler.technologyMapSolo.get(eqs.getInternName());
					if(t == null)
					{
						t = CatTechHandler.technologyMapGroup.get(eqs.getInternName());
						if(t == null)
						{
							t = CatTechHandler.technologyMapGlobal.get(eqs.getInternName());
							if(t == null)
							{
								continue;
							}
						}
					}
					if(t.getRewardUnlockableInteractions().containsKey(eqs.getResearchLevel()))
					{
						for(UnlockableInteraction ui : t.getRewardUnlockableInteractions().get(eqs.getResearchLevel()))
						{
							addInteraction(uuid, ui, 100.0);
						}
					}
					if(t.getRewardRecipes().containsKey(eqs.getResearchLevel()))
					{
						for(Entry<RecipeType, ArrayList<String>> rtl : t.getRewardRecipes().get(eqs.getResearchLevel()).entrySet())
						{
							addRecipe(uuid, rtl.getKey(), 100.0, rtl.getValue().toArray(new String[rtl.getValue().size()]));
						}
					}
					if(t.getRewardDropChances().containsKey(eqs.getResearchLevel()))
					{
						for(DropChance dc : t.getRewardDropChances().get(eqs.getResearchLevel()))
						{
							addDropChances(uuid, dc, 100.0);
						}
					}					
					if(t.getRewardSilkTouchDropChances().containsKey(eqs.getResearchLevel()))
					{
						for(DropChance dc : t.getRewardSilkTouchDropChances().get(eqs.getResearchLevel()))
						{
							addSilkTouchDropChances(uuid, dc, 100.0);
						}
					}
				}
				for(GlobalEntryQueryStatus eqs : GlobalEntryQueryStatus.convert(plugin.getMysqlHandler().getFullList(Type.GLOBALENTRYQUERYSTATUS,
						"`id` ASC", "`entry_query_type` = ? AND `status_type` = ?",
						EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString())))
				{
					if(eqs == null)
					{
						continue;
					}
					Technology t = CatTechHandler.getTechnology(eqs.getInternName(), PlayerAssociatedType.GLOBAL);
					if(t == null)
					{
						continue;
					}
					boolean ifGlobal_HasParticipated = plugin.getMysqlHandler().exist(Type.GLOBALTECHNOLOGYPOLL,
							"`player_uuid` = ? AND `global_choosen_technology_id` = ?",
							player.getUniqueId().toString(), eqs.getId());
					if(t.getRewardUnlockableInteractions().containsKey(eqs.getResearchLevel()))
					{
						for(UnlockableInteraction ui : t.getRewardUnlockableInteractions().get(eqs.getResearchLevel()))
						{
							addInteraction(uuid, ui,
									ifGlobal_HasParticipated ? t.getForUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent() : 100.0);
						}
					}
					if(t.getRewardRecipes().containsKey(eqs.getResearchLevel()))
					{
						for(Entry<RecipeType, ArrayList<String>> rtl : t.getRewardRecipes().get(eqs.getResearchLevel()).entrySet())
						{
							addRecipe(uuid, rtl.getKey(),
									ifGlobal_HasParticipated ? t.getForUninvolvedPollParticipants_RewardRecipesInPercent() : 100.0,
									rtl.getValue().toArray(new String[rtl.getValue().size()]));
						}
					}
					if(t.getRewardDropChances().containsKey(eqs.getResearchLevel()))
					{
						for(DropChance dc : t.getRewardDropChances().get(eqs.getResearchLevel()))
						{
							addDropChances(uuid, dc,
									ifGlobal_HasParticipated ? t.getForUninvolvedPollParticipants_RewardDropChancesInPercent() : 100.0);
						}
					}					
					if(t.getRewardSilkTouchDropChances().containsKey(eqs.getResearchLevel()))
					{
						for(DropChance dc : t.getRewardSilkTouchDropChances().get(eqs.getResearchLevel()))
						{
							addSilkTouchDropChances(uuid, dc,
									ifGlobal_HasParticipated ? t.getForUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent() : 100.0);
						}
					}
				}		
				registeredBlocks.remove(uuid);
				for(RegisteredBlock rg : RegisteredBlock.convert(plugin.getMysqlHandler().getFullList(Type.REGISTEREDBLOCK, "`id` ASC",
						"`player_uuid` = ? AND `server` = ?", uuid.toString(), plugin.getServername())))
				{
					BlockHandler.registerBlock(player, rg.getBlockType(), rg.getLocation(), false);
				}
				RewardHandler.doRewardJoinTask(player, new ConfigHandler().rewardPayoutRepetitionRateForOnlinePlayer());
				if(pd.isShowSyncMessage())
				{
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerHandler.SyncEnd")));
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public static void quitPlayer(final UUID uuid)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				materialInteractionMap.remove(uuid);
				entityTypeInteractionMap.remove(uuid);
				
				materialDropMap.remove(uuid);
				entityTypeDropMap.remove(uuid);
				
				materialSilkTouchDropMap.remove(uuid);
				entityTypeSilkTouchDropMap.remove(uuid);
				
				recipeMap.remove(uuid);
				registeredBlocks.remove(uuid);
			}
		}.runTaskAsynchronously(plugin);	
	}
	
	public static boolean hasAccount(Player player)
	{
		if(plugin.getMysqlHandler().exist(Type.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString()))
		{
			return true;
		}
		return false;
	}
	
	public static void createAccount(Player player)
	{
		PlayerData pd = new PlayerData(0, player.getUniqueId(), player.getName(), 
				plugin.getYamlHandler().getConfig().getBoolean("Do.NewPlayer.ShowSyncMessage", true),
				0, 0, 0, SettingsLevel.BASE);
		plugin.getMysqlHandler().create(Type.PLAYERDATA, pd);
	}
	
	public static PlayerData getPlayer(UUID uuid)
	{
		return (PlayerData) plugin.getMysqlHandler().getData(Type.PLAYERDATA, "`player_uuid` = ?", uuid.toString());
	}
	
	public static void updatePlayer(PlayerData pd)
	{
		plugin.getMysqlHandler().updateData(Type.PLAYERDATA, pd, "`player_uuid` = ?", pd.getUUID().toString());
	}
	
	public static LinkedHashMap<ItemStack, Boolean> canSeeOrResearch_ForGUI(Player player, UUID uuid,
			PlayerAssociatedType pat, MainCategory mcat, SubCategory scat, Technology tech)
	/* HashMap with only 1 Entry.
	 * If Boolean is true, item can be research. Only works for techs
	 * 
	 * It is to determine, by SIMPLE/BOOSTER Tech if you can see or research it. Researchlevel = 1
	 * by MULTIPLE ResearchLevel = Level which you already has +1 
	 */
	{
		LinkedHashMap<ItemStack, Boolean> map = new LinkedHashMap<>();
		if(mcat != null)
		{
			switch(pat)
			{
			case SOLO:
				SoloEntryQueryStatus mseqs = (SoloEntryQueryStatus) plugin.getMysqlHandler().getData(Type.SOLOENTRYQUERYSTATUS,
						"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
						player.getUniqueId().toString(), mcat.getInternName(), EntryQueryType.MAIN_CATEGORY.toString());
				boolean exist = mseqs != null;
				if(!exist)
				{
					mseqs = new SoloEntryQueryStatus(0, mcat.getInternName(), uuid,
							EntryQueryType.MAIN_CATEGORY, EntryStatusType.CANNOT_SEE_IT, 0, Long.MAX_VALUE);
				}
				if(exist && mseqs.getStatusType() == EntryStatusType.CAN_SEE_IT)
				{
					map.put(mcat.getSeeRequirementItemIfYouCanSeeIt(player), true);
					return map;
				}
				ArrayList<String> msls = new ArrayList<>();
				for(String s : mcat.getSeeRequirementConditionQuery())
				{
					if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
					{
						msls.add(s);
						continue;
					}
					String r = getTTReplacerValues(uuid, s);
					msls.add(r);
				}
				ArrayList<String> msal = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, msls);
				if(msal != null && msal.get(0).equalsIgnoreCase("true"))
				{
					mseqs.setStatusType(EntryStatusType.CAN_SEE_IT);
					map.put(mcat.getSeeRequirementItemIfYouCanSeeIt(player), true);
				} else
				{
					if(!mcat.isSeeRequirementShowDifferentItemIfYouNormallyDontSeeIt())
					{
						return null;
					}
					map.put(mcat.getSeeRequirementItemIfYouCannotSeeIt(player), false);
				}
				if(exist)
				{					
					plugin.getMysqlHandler().updateData(Type.SOLOENTRYQUERYSTATUS, mseqs,
							"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
							player.getUniqueId().toString(), mcat.getInternName(), EntryQueryType.MAIN_CATEGORY.toString());
				} else
				{
					plugin.getMysqlHandler().create(Type.SOLOENTRYQUERYSTATUS, mseqs);
				}
				return map;
			case GROUP:
				return null;//TODO
			case GLOBAL:
				GlobalEntryQueryStatus mgeqs = (GlobalEntryQueryStatus) plugin.getMysqlHandler().getData(Type.GLOBALENTRYQUERYSTATUS,
						"`intern_name` = ? AND `entry_query_type` = ?",
						mcat.getInternName(), EntryQueryType.MAIN_CATEGORY.toString());
				boolean existII = mgeqs != null;
				if(!existII)
				{
					mgeqs = new GlobalEntryQueryStatus(0,
							mcat.getInternName(), EntryQueryType.MAIN_CATEGORY, EntryStatusType.CANNOT_SEE_IT, 0, Long.MAX_VALUE);
				}
				if(existII && mgeqs.getStatusType() == EntryStatusType.CAN_SEE_IT)
				{
					map.put(mcat.getSeeRequirementItemIfYouCanSeeIt(player), true);
					return map;
				}
				ArrayList<String> mgls = new ArrayList<>();
				for(String s : mcat.getSeeRequirementConditionQuery())
				{
					if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
					{
						mgls.add(s);
						continue;
					}
					mgls.add(getTTReplacerValues(uuid, s));
				}
				ArrayList<String> al = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, mgls);
				if(al != null && al.get(0).equalsIgnoreCase("true"))
				{
					mgeqs.setStatusType(EntryStatusType.CAN_SEE_IT);
					map.put(mcat.getSeeRequirementItemIfYouCanSeeIt(player), true);
				} else
				{
					if(!mcat.isSeeRequirementShowDifferentItemIfYouNormallyDontSeeIt())
					{
						return null;
					}
					map.put(mcat.getSeeRequirementItemIfYouCannotSeeIt(player), false);
				}
				if(existII)
				{
					plugin.getMysqlHandler().updateData(Type.GLOBALENTRYQUERYSTATUS, mgeqs,
							"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
							player.getUniqueId().toString(), mcat.getInternName(), EntryQueryType.MAIN_CATEGORY.toString());
				} else
				{
					plugin.getMysqlHandler().create(Type.GLOBALENTRYQUERYSTATUS, mgeqs);
				}
				return map;
			}
			
		} else if(scat != null)
		{
			switch(pat)
			{
			case SOLO:
				SoloEntryQueryStatus sseqs = (SoloEntryQueryStatus) plugin.getMysqlHandler().getData(Type.SOLOENTRYQUERYSTATUS,
						"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
						player.getUniqueId().toString(), scat.getInternName(), EntryQueryType.SUB_CATEGORY.toString());
				boolean exist = sseqs != null;
				if(!exist)
				{
					sseqs = new SoloEntryQueryStatus(0, scat.getInternName(), uuid,
							EntryQueryType.SUB_CATEGORY, EntryStatusType.CANNOT_SEE_IT, 0, Long.MAX_VALUE);
				}
				if(exist && sseqs.getStatusType() == EntryStatusType.CAN_SEE_IT)
				{
					map.put(scat.getSeeRequirementItemIfYouCanSeeIt(player), true);
					return map;
				}
				ArrayList<String> ssls = new ArrayList<>();
				for(String s : scat.getSeeRequirementConditionQuery())
				{
					if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
					{
						ssls.add(s);
						continue;
					}
					ssls.add(getTTReplacerValues(uuid, s));
				}
				ArrayList<String> ssal = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, ssls);
				if(ssal != null && ssal.get(0).equalsIgnoreCase("true"))
				{
					sseqs.setStatusType(EntryStatusType.CAN_SEE_IT);
					map.put(scat.getSeeRequirementItemIfYouCanSeeIt(player), true);
				} else
				{
					if(!scat.isSeeRequirementShowDifferentItemIfYouNormallyDontSeeIt())
					{
						return null;
					}
					map.put(scat.getSeeRequirementItemIfYouCannotSeeIt(player), false);
				}
				if(exist)
				{
					plugin.getMysqlHandler().updateData(Type.SOLOENTRYQUERYSTATUS, sseqs,
							"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
							player.getUniqueId().toString(), scat.getInternName(), EntryQueryType.SUB_CATEGORY.toString());
				} else
				{
					plugin.getMysqlHandler().create(Type.SOLOENTRYQUERYSTATUS, sseqs);
				}
				return map;
			case GROUP:
				return null;//TODO
			case GLOBAL:
				GlobalEntryQueryStatus sgeqs = (GlobalEntryQueryStatus) plugin.getMysqlHandler().getData(Type.GLOBALENTRYQUERYSTATUS,
						"`intern_name` = ? AND `entry_query_type` = ?",
						scat.getInternName(), EntryQueryType.SUB_CATEGORY.toString());
				boolean existII = sgeqs != null;
				if(!existII)
				{
					sgeqs = new GlobalEntryQueryStatus(0,
							scat.getInternName(), EntryQueryType.SUB_CATEGORY, EntryStatusType.CANNOT_SEE_IT, 0, Long.MAX_VALUE);
				}
				if(existII && sgeqs.getStatusType() == EntryStatusType.CAN_SEE_IT)
				{
					map.put(scat.getSeeRequirementItemIfYouCanSeeIt(player), null);
					return map;
				}
				ArrayList<String> sgls = new ArrayList<>();
				for(String s : scat.getSeeRequirementConditionQuery())
				{
					if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
					{
						sgls.add(s);
						continue;
					}
					sgls.add(getTTReplacerValues(uuid, s));
				}
				ArrayList<String> sgal = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, sgls);
				if(sgal != null && sgal.get(0).equalsIgnoreCase("true"))
				{
					sgeqs.setStatusType(EntryStatusType.CAN_SEE_IT);
					map.put(scat.getSeeRequirementItemIfYouCanSeeIt(player), null);
				} else
				{
					if(!scat.isSeeRequirementShowDifferentItemIfYouNormallyDontSeeIt())
					{
						return null;
					}
					map.put(scat.getSeeRequirementItemIfYouCannotSeeIt(player), null);
				}
				if(existII)
				{
					plugin.getMysqlHandler().updateData(Type.GLOBALENTRYQUERYSTATUS, sgeqs,
							"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
							player.getUniqueId().toString(), scat.getInternName(), EntryQueryType.SUB_CATEGORY.toString());
				} else
				{
					plugin.getMysqlHandler().create(Type.GLOBALENTRYQUERYSTATUS, sgeqs);
				}
				return map;
			}
		} else if(tech != null)
		{
			switch(pat)
			{
			case SOLO:
				return techcanSeeOrResearch_ForGUI(player, uuid, pat, mcat, scat, tech);
			case GROUP:
				return null;//TODO
			case GLOBAL:
				ArrayList<GlobalEntryQueryStatus> eqsList = GlobalEntryQueryStatus.convert(
						plugin.getMysqlHandler().getList(Type.GLOBALENTRYQUERYSTATUS,
						"`research_level` DESC", 0, 1,
						"`intern_name` = ? AND `entry_query_type` = ?",
						tech.getInternName(), EntryQueryType.TECHNOLOGY.toString()));
				GlobalEntryQueryStatus tgeqs = eqsList.size() == 0 ? null : eqsList.get(0);
				boolean existII = tgeqs != null;
				if(!existII)
				{
					tgeqs = new GlobalEntryQueryStatus(0, tech.getInternName(), EntryQueryType.TECHNOLOGY, EntryStatusType.CANNOT_SEE_IT, 0, 0);
				}
				if(existII)
				{
					int researchlevII = tgeqs.getResearchLevel() >= tech.getMaximalTechnologyLevelToResearch()
							? tech.getMaximalTechnologyLevelToResearch() 
							: (tgeqs.getResearchLevel()+1 >= tech.getMaximalTechnologyLevelToResearch()
								? tech.getMaximalTechnologyLevelToResearch() : tgeqs.getResearchLevel()+1);
					if(tgeqs.getStatusType() == EntryStatusType.CANNOT_SEE_IT)
					{
						ArrayList<String> tgls = new ArrayList<>();
						for(String s : tech.getSeeRequirementConditionQuery())
						{
							if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
							{
								tgls.add(s);
								continue;
							}
							tgls.add(getTTReplacerValues(uuid, s));
						}
						ArrayList<String> seeOrNot = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, tgls);
						if(seeOrNot != null && seeOrNot.get(0).equalsIgnoreCase("false"))
						{
							map.put(tech.getSeeRequirementItemIfYouCannotSeeIt(player), false);
							//Here not return, so it can see
						} else if(seeOrNot == null)
						{
							return null;
						}
						tgls.clear();
						for(String s : tech.getResearchRequirementConditionQuery().get(researchlevII))
						{
							if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
							{
								tgls.add(s);
								continue;
							}
							tgls.add(getTTReplacerValues(uuid, s));
						}
						ArrayList<String> researchOrNot = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, tgls);
						if(researchOrNot != null &&  researchOrNot.get(0).equalsIgnoreCase("true"))
						{
							tgeqs.setStatusType(EntryStatusType.CAN_RESEARCH_IT);
							map.put(tech.getResearchRequirementItemIfYouCanResearchIt(player, tgeqs.getResearchLevel()), false);
						} else if(researchOrNot != null &&  researchOrNot.get(0).equalsIgnoreCase("true")
								&& seeOrNot != null && seeOrNot.get(0).equalsIgnoreCase("false"))
						{
							tgeqs.setStatusType(EntryStatusType.CAN_SEE_IT);
							map.put(tech.getSeeRequirementItemIfYouCanSeeIt(player), false);
						}
						plugin.getMysqlHandler().updateData(Type.GLOBALENTRYQUERYSTATUS, tgeqs,
								"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
								player.getUniqueId().toString(), tech.getInternName(), EntryQueryType.TECHNOLOGY.toString());
						return map;
					} else if(tgeqs.getStatusType() == EntryStatusType.CAN_SEE_IT)
					{
						ArrayList<String> tgls = new ArrayList<>();
						for(String s : tech.getResearchRequirementConditionQuery().get(researchlevII))
						{
							if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
							{
								tgls.add(s);
								continue;
							}
							tgls.add(getTTReplacerValues(uuid, s));
						}
						ArrayList<String> researchOrNot = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, tgls);
						if(researchOrNot != null &&  researchOrNot.get(0).equalsIgnoreCase("true"))
						{
							tgeqs.setStatusType(EntryStatusType.CAN_RESEARCH_IT);
							plugin.getMysqlHandler().updateData(Type.GLOBALENTRYQUERYSTATUS, tgeqs,
									"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
									player.getUniqueId().toString(), tech.getInternName(), EntryQueryType.TECHNOLOGY.toString());
							map.put(tech.getResearchRequirementItemIfYouCanResearchIt(player, tgeqs.getResearchLevel()), false);
						} else
						{
							map.put(tech.getSeeRequirementItemIfYouCanSeeIt(player), false);
						}
						return map;
					} else if(tgeqs.getStatusType() == EntryStatusType.CAN_RESEARCH_IT)
					{
						map.put(tech.getResearchRequirementItemIfYouCanResearchIt(player,tgeqs.getResearchLevel()), false);
						return map;
					} else if(tgeqs.getStatusType() == EntryStatusType.HAVE_RESEARCHED_IT)
					{
						map.put(tech.getResearchRequirementItemIfYouHaveResearchedIt(player), false);
						return map;
					}
				}
				int researchlevII = 1;
				ArrayList<String> tgls = new ArrayList<>();
				for(String s : tech.getSeeRequirementConditionQuery())
				{
					if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
					{
						tgls.add(s);
						continue;
					}
					tgls.add(getTTReplacerValues(uuid, s));
				}
				ArrayList<String> seeOrRese = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, tgls);
				if(seeOrRese != null && seeOrRese.get(0).equalsIgnoreCase("true"))
				{
					tgls.clear();
					for(String s : tech.getResearchRequirementConditionQuery().get(researchlevII))
					{
						if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
						{
							tgls.add(s);
							continue;
						}
						tgls.add(getTTReplacerValues(uuid, s));
					}
					ArrayList<String> resOrNot = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, tgls);
					if(resOrNot != null && resOrNot.get(0).equalsIgnoreCase("true"))
					{
						tgeqs.setStatusType(EntryStatusType.CAN_RESEARCH_IT);
						map.put(tech.getResearchRequirementItemIfYouCanResearchIt(player, tgeqs.getResearchLevel()), true);
					} else
					{
						tgeqs.setStatusType(EntryStatusType.CAN_SEE_IT);
						map.put(tech.getSeeRequirementItemIfYouCanSeeIt(player), false);
					}
					plugin.getMysqlHandler().create(Type.GLOBALENTRYQUERYSTATUS, tgeqs);
					return map;
				} else
				{
					tgeqs.setStatusType(EntryStatusType.CANNOT_SEE_IT);
					plugin.getMysqlHandler().create(Type.GLOBALENTRYQUERYSTATUS, tgeqs);
					map.put(tech.getSeeRequirementItemIfYouCannotSeeIt(player), false);
					return map;
				}
			}
		}
		return null;
	}
	
	private static LinkedHashMap<ItemStack, Boolean> techcanSeeOrResearch_ForGUI(Player player, UUID uuid,
			PlayerAssociatedType pat, MainCategory mcat, SubCategory scat, Technology tech)
	{
		LinkedHashMap<ItemStack, Boolean> map = new LinkedHashMap<>();
		ArrayList<SoloEntryQueryStatus> highestNotResearchedEntryList = SoloEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.SOLOENTRYQUERYSTATUS,
				"`research_level` DESC", 0, 1,
				"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ? AND `status_type` != ?",
				player.getUniqueId().toString(), tech.getInternName(), EntryQueryType.TECHNOLOGY.toString(),
				EntryStatusType.HAVE_RESEARCHED_IT.toString()));
		SoloEntryQueryStatus hNRE = highestNotResearchedEntryList.size() == 0 ? null : highestNotResearchedEntryList.get(0); //highestNotResearchedEntry
		ArrayList<SoloEntryQueryStatus> highestEntryResearchedList = SoloEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.SOLOENTRYQUERYSTATUS,
				"`research_level` DESC", 0, 1,
				"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ? AND `status_type` = ?",
				player.getUniqueId().toString(), tech.getInternName(), EntryQueryType.TECHNOLOGY.toString(),
				EntryStatusType.HAVE_RESEARCHED_IT.toString()));
		SoloEntryQueryStatus hRE = highestEntryResearchedList.size() == 0 ? null : highestEntryResearchedList.get(0); //highestResearchedEntry
		
		if((hNRE == null && hRE == null) //Beide existieren nicht, somit ist der spieler höchstwahrscheinlich neu
				|| (hNRE != null && hRE == null) //Spieler hat zwar schonmal die Gui erforscht, aber noch nicht die Technology erforscht
				)
		{
			boolean exist = hNRE != null;
			ArrayList<String> tsls = new ArrayList<>();
			if(!exist)
			{
				hNRE = new SoloEntryQueryStatus(0, tech.getInternName(), uuid,
						EntryQueryType.TECHNOLOGY, EntryStatusType.CANNOT_SEE_IT, 1,
						Long.MAX_VALUE);
			}
			if(hNRE.getStatusType() != EntryStatusType.HAVE_RESEARCHED_IT)
			{
				if(new ConfigHandler().accessTechnology_IfCreative() && player.getGameMode() == GameMode.CREATIVE)
				{
					map.put(tech.getResearchRequirementItemIfYouCanResearchIt(player, hNRE.getResearchLevel()), false);
					return map;
				}
			}
			if(hNRE.getStatusType() == EntryStatusType.CANNOT_SEE_IT)
			{
				for(String s : tech.getSeeRequirementConditionQuery())
				{
					if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
					{
						tsls.add(s);
						continue;
					}
					tsls.add(getTTReplacerValues(uuid, s));
				}
				ArrayList<String> seeOrNot = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, tsls);
				if(seeOrNot == null //Error somewhere
						|| (!tech.isSeeRequirementShowDifferentItemIfYouNormallyDontSeeIt()
							&& seeOrNot != null && seeOrNot.get(0).equalsIgnoreCase("false"))) //If you cannot see it, show no item
				{
					TT.log.info("techcanSeeOrResearch_ForGUI 0"); //REMOVEME
					return null; 
				} else if(seeOrNot != null && seeOrNot.get(0).equalsIgnoreCase("false"))
				{
					map.put(tech.getSeeRequirementItemIfYouCannotSeeIt(player), false); //Result: Cannot See it
					return map;
				}
				tsls.clear();
			}
			for(String s : tech.getResearchRequirementConditionQuery().get(1))
			{
				if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
				{
					tsls.add(s);
					continue;
				}
				tsls.add(getTTReplacerValues(uuid, s));
			}
			ArrayList<String> researchOrNot = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, tsls);
			if(researchOrNot != null &&  researchOrNot.get(0).equalsIgnoreCase("true"))
			{
				hNRE.setStatusType(EntryStatusType.CAN_RESEARCH_IT);
				map.put(tech.getResearchRequirementItemIfYouCanResearchIt(player, hNRE.getResearchLevel()), false);
			} else if(researchOrNot != null &&  researchOrNot.get(0).equalsIgnoreCase("false"))
			{
				hNRE.setStatusType(EntryStatusType.CAN_SEE_IT);
				map.put(tech.getSeeRequirementItemIfYouCanSeeIt(player), false);
			} else
			{
				map.put(tech.getSeeRequirementItemIfYouCannotSeeIt(player), false); //Result: Cannot See it
				return map;
			}
			if(exist)
			{
				plugin.getMysqlHandler().updateData(Type.SOLOENTRYQUERYSTATUS, hNRE,
						"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
						player.getUniqueId().toString(), tech.getInternName(), EntryQueryType.TECHNOLOGY.toString());
			} else
			{
				plugin.getMysqlHandler().create(Type.SOLOENTRYQUERYSTATUS, hNRE);
			}
			return map;
		} else if(hNRE == null && hRE != null) //Spieler hat die Technology letztes Mal erforscht, Gui wird scheinbar neu geladen.
		{
			if(hRE.getResearchLevel() >= tech.getMaximalTechnologyLevelToResearch())
			{
				//Spieler hat schon alles erforscht
				map.put(tech.getResearchRequirementItemIfYouHaveResearchedIt(player), false);
				return map;
			}
			int researchLvl = hRE.getResearchLevel()+1;
			if(new ConfigHandler().accessTechnology_IfCreative() && player.getGameMode() == GameMode.CREATIVE)
			{
				map.put(tech.getResearchRequirementItemIfYouCanResearchIt(player, researchLvl), false);
				return map;
			}
			ArrayList<String> tsls = new ArrayList<>();
			for(String s : tech.getResearchRequirementConditionQuery().get(researchLvl))
			{
				if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
				{
					tsls.add(s);
					continue;
				}
				tsls.add(getTTReplacerValues(uuid, s));
			}
			hNRE = new SoloEntryQueryStatus(0, tech.getInternName(), uuid,
					EntryQueryType.TECHNOLOGY, EntryStatusType.CANNOT_SEE_IT, researchLvl,
					Long.MAX_VALUE);
			ArrayList<String> researchOrNot = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, tsls);
			if(researchOrNot != null &&  researchOrNot.get(0).equalsIgnoreCase("true"))
			{
				hNRE.setStatusType(EntryStatusType.CAN_RESEARCH_IT);
				map.put(tech.getResearchRequirementItemIfYouCanResearchIt(player, researchLvl), false);
			} else if(researchOrNot != null &&  researchOrNot.get(0).equalsIgnoreCase("false"))
			{
				hNRE.setStatusType(EntryStatusType.CAN_SEE_IT);
				map.put(tech.getSeeRequirementItemIfYouCanSeeIt(player), false);
			} else
			{
				TT.log.info("techcanSeeOrResearch_ForGUI 2"); //REMOVEME
				return null; //Error
			}
			plugin.getMysqlHandler().create(Type.SOLOENTRYQUERYSTATUS, hNRE);
			return map;
		} else if(hNRE != null && hRE != null) //Spieler hat die Technology schonmal erforscht
		{
			if(hRE.getResearchLevel() >= tech.getMaximalTechnologyLevelToResearch())
			{
				//Spieler hat schon alles erforscht
				map.put(tech.getResearchRequirementItemIfYouHaveResearchedIt(player), false);
				return map;
			}
			if(hRE.getResearchLevel() >= hNRE.getResearchLevel())
			{
				//Das Erforschte Lvl ist höher oder gleich groß, als das nicht erforschte lvl, was nicht sein darf.
				//Error, shouldnt happend
				map.put(tech.getResearchRequirementItemIfYouHaveResearchedIt(player), false);
				return map;
			}
			int researchLvl = hNRE.getResearchLevel();
			if(hNRE.getStatusType() != EntryStatusType.HAVE_RESEARCHED_IT)
			{
				if(new ConfigHandler().accessTechnology_IfCreative() && player.getGameMode() == GameMode.CREATIVE)
				{
					map.put(tech.getResearchRequirementItemIfYouCanResearchIt(player, researchLvl), false);
					return map;
				}
			}
			ArrayList<String> tsls = new ArrayList<>();
			if(hNRE.getStatusType() == EntryStatusType.CANNOT_SEE_IT)
			{
				for(String s : tech.getSeeRequirementConditionQuery())
				{
					if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
					{
						tsls.add(s);
						continue;
					}
					tsls.add(getTTReplacerValues(uuid, s));
				}
				ArrayList<String> seeOrNot = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, tsls);
				if(seeOrNot == null //Error somewhere
						|| (!tech.isSeeRequirementShowDifferentItemIfYouNormallyDontSeeIt()
							&& seeOrNot != null && seeOrNot.get(0).equalsIgnoreCase("false"))) //If you cannot see it, show no item
				{
					TT.log.info("techcanSeeOrResearch_ForGUI 3"); //REMOVEME
					return null; 
				} else if(seeOrNot != null && seeOrNot.get(0).equalsIgnoreCase("false"))
				{
					map.put(tech.getSeeRequirementItemIfYouCannotSeeIt(player), false); //Result: Cannot See it
					return map;
				}
				tsls.clear();
			}
			for(String s : tech.getResearchRequirementConditionQuery().get(researchLvl))
			{
				if(s.startsWith("if") || s.startsWith("else") || s.startsWith("output") || s.startsWith("event"))
				{
					tsls.add(s);
					continue;
				}
				tsls.add(getTTReplacerValues(uuid, s));
			}
			ArrayList<String> researchOrNot = plugin.getConditionQueryParser().parseBranchedConditionQuery(uuid, uuid, tsls);
			if(researchOrNot != null &&  researchOrNot.get(0).equalsIgnoreCase("true"))
			{
				hNRE.setStatusType(EntryStatusType.CAN_RESEARCH_IT);
				map.put(tech.getResearchRequirementItemIfYouCanResearchIt(player, researchLvl), false);
			} else if(researchOrNot != null &&  researchOrNot.get(0).equalsIgnoreCase("false"))
			{
				hNRE.setStatusType(EntryStatusType.CAN_SEE_IT);
				map.put(tech.getSeeRequirementItemIfYouCanSeeIt(player), false);
			} else
			{
				map.put(tech.getSeeRequirementItemIfYouCannotSeeIt(player), false); //Result: Cannot See it
				return map;
			}
			plugin.getMysqlHandler().updateData(Type.SOLOENTRYQUERYSTATUS, hNRE,"`id` = ?",	hNRE.getId());
			return map;			
		}
		return null; //Error
	}
	
	private static String getTTReplacerValues(UUID uuid, String s)
	{
		String[] a = s.split(":");
		String b = "";
		String xyz = "";
		if(a.length == 2)
		{
			b = a[1];
			xyz = a[0]+":%b%";
		} else if(a.length == 4) 
		{
			b = a[1];
			xyz = a[0]+":%b%:"+a[2]+":"+a[3];
		}
		if(b.startsWith("canseetech")) 
		{
			//canseetech,<maincategory/subcategory/technology>,<Name of that>
			String[] c = b.split(",");
			if(c.length != 3)
			{
				return s;
			}
			EntryQueryType eqt = c[1].equalsIgnoreCase("maincategory") ? EntryQueryType.MAIN_CATEGORY
					: (c[1].equalsIgnoreCase("subcategory") ? EntryQueryType.SUB_CATEGORY : EntryQueryType.TECHNOLOGY);
			SoloEntryQueryStatus eqs = (SoloEntryQueryStatus) plugin.getMysqlHandler().getData(Type.SOLOENTRYQUERYSTATUS,
					"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
					uuid.toString(), c[2], eqt.toString());
			b = eqs == null ? "false"
				: (eqs.getStatusType() == EntryStatusType.CANNOT_SEE_IT ? "false" : "true");
			xyz = xyz.replace("%b%", b);
		} else if(b.startsWith("hasresearchedtech"))
		{
			//hasresearchedtech,<Name of Tech>,<Researchlevel>
			String[] c = b.split(",");
			if(c.length != 3)
			{
				return s;
			}
			SoloEntryQueryStatus eqs = (SoloEntryQueryStatus) plugin.getMysqlHandler().getData(Type.SOLOENTRYQUERYSTATUS,
					"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ? AND `research_level` = ?",
					uuid.toString(), c[1], EntryQueryType.TECHNOLOGY.toString(), c[2]);
			b = eqs == null ? "false" :
				(eqs.getStatusType() == EntryStatusType.HAVE_RESEARCHED_IT ? "true" : "false");
			xyz = xyz.replace("%b%", b);
		} else if(b.startsWith("gethighestresearchedtechlevel"))
		{
			//gethighestresearchedtechlevel,<Tech>
			String[] c = b.split("");
			if(c.length != 2)
			{
				return s;
			}
			SoloEntryQueryStatus eqs = (SoloEntryQueryStatus) plugin.getMysqlHandler().getData(Type.SOLOENTRYQUERYSTATUS,
					"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
					uuid.toString(), c[1], EntryQueryType.TECHNOLOGY.toString());
			b = eqs == null ? "0" : String.valueOf(eqs.getResearchLevel());
			xyz = xyz.replace("%b%", b);
		} else if(b.startsWith("getplayeractualttexp"))
		{
			PlayerData pd = getPlayer(uuid);
			b = pd == null ? "0" : String.valueOf(pd.getActualTTExp());
			xyz.replace("%b%", b);
		} else if(b.startsWith("getplayertotalreceivedttexp"))
		{
			PlayerData pd = getPlayer(uuid);
			b = pd == null ? "0" : String.valueOf(pd.getTotalReceivedTTExp());
			xyz = xyz.replace("%b%", b);
		} else
		{
			return s;
		}
		return xyz;
	}
	
	public enum AcquireRespond
	{
		CAN_BE_RESEARCHED,
		TECH_IS_SIMPLE_AND_ALREADY_RESEARCHED,
		TECH_MAX_LEVEL_IS_REACHED,
		NOT_ENOUGH_TT_EXP,
		NOT_ENOUGH_VANILLA_EXP,
		NOT_ENOUGH_MONEY,
		NOT_ENOUGH_MATERIAL;
	}
	
	//Called if wished to buy the tech
	public static AcquireRespond haveAlreadyResearched(Player player, Technology t)
	{		
		ArrayList<SoloEntryQueryStatus> sEQSList = SoloEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.SOLOENTRYQUERYSTATUS,
				"`research_level` DESC", 0, 1,
				"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ? AND `status_type` != ?",
				player.getUniqueId().toString(), t.getInternName(), EntryQueryType.TECHNOLOGY.toString(),
				EntryStatusType.HAVE_RESEARCHED_IT.toString()));
		SoloEntryQueryStatus sEQS = sEQSList.size() == 0 ? null : sEQSList.get(0);
		if(sEQS == null)
		{
			//Nie etwas erforscht
			sEQS = new SoloEntryQueryStatus(0, t.getInternName(), player.getUniqueId(),
					EntryQueryType.TECHNOLOGY, EntryStatusType.HAVE_RESEARCHED_IT, 1,
					t.getTechnologyType() == TechnologyType.BOOSTER ?
							System.currentTimeMillis()+t.getIfBoosterDurationUntilExpiration() : Long.MAX_VALUE);
			plugin.getMysqlHandler().create(Type.SOLOENTRYQUERYSTATUS, sEQS);
		} else if(t.getTechnologyType() == TechnologyType.SIMPLE && sEQS.getResearchLevel() >= 1)
		{
			//Sollte nicht passieren, da der Spieler diese Technology schon komplett erforscht hat.
			return AcquireRespond.TECH_IS_SIMPLE_AND_ALREADY_RESEARCHED; //Error
		} else if(t.getTechnologyType() == TechnologyType.MULTIPLE && t.getMaximalTechnologyLevelToResearch() > sEQS.getResearchLevel())
		{
			//Spieler erforsch das nächste level
			sEQS.setResearchLevel(sEQS.getResearchLevel()+1);
			sEQS.setStatusType(EntryStatusType.HAVE_RESEARCHED_IT);
			sEQS.setDurationUntilExpiration(t.getTechnologyType() == TechnologyType.BOOSTER 
											? System.currentTimeMillis() + t.getIfBoosterDurationUntilExpiration() 
											: Long.MAX_VALUE);
			plugin.getMysqlHandler().updateData(Type.SOLOENTRYQUERYSTATUS, sEQS,"`id` = ?", sEQS.getId());
		} else if(t.getTechnologyType() == TechnologyType.MULTIPLE && sEQS.getResearchLevel() >= t.getMaximalTechnologyLevelToResearch())
		{
			//Spieler hat schon das maxlevel erreischt.
			return AcquireRespond.TECH_MAX_LEVEL_IS_REACHED; //Error
		}
		return AcquireRespond.CAN_BE_RESEARCHED;
	}
	
	//Should call haveAlreadyResearched Methode before this.
	public static AcquireRespond payTechnology(Player player, Technology t, double proportionateCosts)
	{
		if(player.getGameMode() == GameMode.CREATIVE)
		{
			return AcquireRespond.CAN_BE_RESEARCHED;
		}
		int techLevel = 0;
		int acquiredTech = 0;
		int totalSoloTechs = plugin.getMysqlHandler().getCount(Type.SOLOENTRYQUERYSTATUS,
				"`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ?",
				player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString());
		int totalGlobalTechs = plugin.getMysqlHandler().getCount(Type.GLOBALENTRYQUERYSTATUS,
				"`entry_query_type` = ? AND `status_type` = ?",
				player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString());
		if(t.getPlayerAssociatedType() == PlayerAssociatedType.SOLO)
		{
			ArrayList<SoloEntryQueryStatus> eeqsList = SoloEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.SOLOENTRYQUERYSTATUS,
					"`research_level` DESC", 0, 1,
					"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
					player.getUniqueId().toString(), t.getInternName(), EntryQueryType.TECHNOLOGY.toString()));
			SoloEntryQueryStatus eqs = eeqsList.size() == 0 ? null : eeqsList.get(0);
			techLevel = eqs == null ? 1 : eqs.getResearchLevel() + 1; //Tech which may to acquire
			acquiredTech = eqs == null ? 0 : techLevel - 1; //Tech which was already acquire
		} else
		{
			ArrayList<GlobalEntryQueryStatus> eeqsList = GlobalEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.GLOBALENTRYQUERYSTATUS,
					"`research_level` DESC", 0, 1,
					"`intern_name` = ? AND `entry_query_type` = ?",
					t.getInternName(), EntryQueryType.TECHNOLOGY.toString()));
			GlobalEntryQueryStatus eqs = eeqsList.size() == 0 ? null : eeqsList.get(0);
			techLevel = eqs == null ? 1 : eqs.getResearchLevel() + 1; //Tech which may to acquire
			acquiredTech = eqs == null ? 0 : techLevel - 1; //Tech which was already acquire
		}		
		HashMap<String, Double> map = new HashMap<>();
		map.put(TECHLEVEL, Double.valueOf(techLevel));
		map.put(TECHACQUIRED, Double.valueOf(acquiredTech));
		map.put(SOLOTOTALTECH, Double.valueOf(totalSoloTechs));
		map.put(GLOBALTOTALTECH, Double.valueOf(totalGlobalTechs));
		double ttexp = 0;
		boolean bttexp = true;
		PlayerData pd = getPlayer(player.getUniqueId());
		if(pd != null && !t.getCostTTExp().isEmpty())
		{
			ttexp =  new MathFormulaParser().parse(t.getCostTTExp().get(techLevel), map) * proportionateCosts;
			bttexp = pd.getActualTTExp() >= ttexp;
		}
		boolean bvexp = true;
		int vexp = 0;
		if(!t.getCostVanillaExp().isEmpty())
		{
			vexp = (int) Math.floor(new MathFormulaParser().parse(t.getCostVanillaExp().get(techLevel), map) * proportionateCosts);
			bvexp = Experience.getExp(player) > vexp;
		}
		double money = 0;
		boolean bmoney = true;
		if(!t.getCostMoney().isEmpty())
		{
			money = new MathFormulaParser().parse(t.getCostMoney().get(techLevel), map) * proportionateCosts;
			if(plugin.getIFHEco() != null)
			{
				Account acc = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN,
						plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
				bmoney = acc.getBalance() >= money;
			} else if(plugin.getVaultEco() != null)
			{
				bmoney = plugin.getVaultEco().has(player, money);
			}
		}
		boolean bmaterial = true;
		LinkedHashMap<Material, Integer> matmap = new LinkedHashMap<>();
		for(Entry<Material, String> e : t.getCostMaterial().get(techLevel).entrySet())
		{
			int material = (int) Math.floor(new MathFormulaParser().parse(e.getValue(), map) * proportionateCosts);
			matmap.put(e.getKey(), material);
			int has = 0;
			for(ItemStack is : player.getInventory().getContents())
			{
				if(is == null || is.getType() == Material.AIR || is.getType() != e.getKey())
				{
					continue;
				}
				if(is.hasItemMeta())
				{
					ItemMeta im = (ItemMeta) is.getItemMeta();
					if(im.hasDisplayName() || im.hasEnchants() || im.hasLore() || im.hasCustomModelData())
					{
						continue;
					}
				}
				has += is.getAmount();
			}
			if(material > has)
			{
				bmaterial = false;
				break;
			}
		}
		if(!bttexp)
		{
			return AcquireRespond.NOT_ENOUGH_TT_EXP; 
		}
		if(!bvexp)
		{
			return AcquireRespond.NOT_ENOUGH_VANILLA_EXP; 
		}
		if(!bmoney)
		{
			return AcquireRespond.NOT_ENOUGH_MONEY; 
		}
		if(!bmaterial)
		{
			return AcquireRespond.NOT_ENOUGH_MATERIAL;
		}
		pd.setActualTTExp(pd.getActualTTExp()-ttexp);
		updatePlayer(pd);
		Experience.changeExp(player, -vexp, true);
		if(money > 0)
		{
			if(plugin.getIFHEco() != null)
			{
				Account acc = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN,
						plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
				Account v = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.VOID,
						plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
				SubCategory sc = CatTechHandler.getSubCategory(t, t.getPlayerAssociatedType());
				MainCategory mc = CatTechHandler.getMainCategory(sc, sc.getPlayerAssociatedType());
				String category = plugin.getYamlHandler().getLang().getString("PlayerHandler.PayTechnology.Category");
				String comment = plugin.getYamlHandler().getLang().getString("PlayerHandler.PayTechnology.Comment")
						.replace("%technology%", t.getDisplayName())
						.replace("%subcategory%", sc.getDisplayName())
						.replace("%maincategory%", mc.getDisplayName());
				if(v != null)
				{
					plugin.getIFHEco().transaction(acc, v, money, OrdererType.PLAYER, player.getUniqueId().toString(),
							category,
							comment);
				} else
				{
					plugin.getIFHEco().withdraw(acc, money, OrdererType.PLAYER, player.getUniqueId().toString(),
							category,
							comment);
				}
			} else if(plugin.getVaultEco() != null)
			{
				plugin.getVaultEco().withdrawPlayer(player, money);
			}
		}
		for(Entry<Material, Integer> e : matmap.entrySet())
		{
			int toremove = e.getValue();
			for(ItemStack is : player.getInventory().getContents())
			{
				if(toremove == 0)
				{
					break;
				}
				if(is == null || is.getType() == Material.AIR || is.getType() != e.getKey())
				{
					continue;
				}
				if(is.hasItemMeta())
				{
					ItemMeta im = (ItemMeta) is.getItemMeta();
					if(im.hasDisplayName() || im.hasEnchants() || im.hasLore())
					{
						continue;
					}
				}
				if(toremove < is.getAmount())
				{
					is.setAmount(is.getAmount()-toremove);
					toremove = 0;
				} else if(toremove == is.getAmount() || toremove > is.getAmount())
				{
					toremove -= is.getAmount();
					is.setAmount(0);
				}
			}
		}
		return AcquireRespond.CAN_BE_RESEARCHED;
	}
	
	//The Payment for the cost should be called before this!
	public static int researchSoloTechnology(Player player, Technology t, boolean doUpdate)
	//Return researchLevel of the technology
	{
		ArrayList<SoloEntryQueryStatus> sEQSList = SoloEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.SOLOENTRYQUERYSTATUS,
				"`research_level` DESC", 0, 1,
				"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ? AND `status_type` != ?",
				player.getUniqueId().toString(), t.getInternName(), EntryQueryType.TECHNOLOGY.toString(),
				EntryStatusType.HAVE_RESEARCHED_IT.toString()));
		SoloEntryQueryStatus sEQS = sEQSList.size() == 0 ? null : sEQSList.get(0);
		int researchLevel = 0;
		if(sEQS == null)
		{
			//Nie etwas erforscht
			sEQS = new SoloEntryQueryStatus(0, t.getInternName(), player.getUniqueId(),
					EntryQueryType.TECHNOLOGY, EntryStatusType.HAVE_RESEARCHED_IT, 1,
					t.getTechnologyType() == TechnologyType.BOOSTER ?
							System.currentTimeMillis()+t.getIfBoosterDurationUntilExpiration() : Long.MAX_VALUE);
			researchLevel = 1;
			plugin.getMysqlHandler().create(Type.SOLOENTRYQUERYSTATUS, sEQS);
		} else if(t.getTechnologyType() == TechnologyType.SIMPLE && sEQS.getResearchLevel() >= 1)
		{
			//Sollte nicht passieren, da der Spieler diese Technology schon komplett erforscht hat.
			return 0; //Error
		} else if(t.getTechnologyType() == TechnologyType.MULTIPLE && t.getMaximalTechnologyLevelToResearch() > sEQS.getResearchLevel())
		{
			//Spieler erforsch das nächste level
			researchLevel = sEQS.getResearchLevel();
			sEQS.setResearchLevel(researchLevel);
			sEQS.setStatusType(EntryStatusType.HAVE_RESEARCHED_IT);
			sEQS.setDurationUntilExpiration(t.getTechnologyType() == TechnologyType.BOOSTER 
											? System.currentTimeMillis() + t.getIfBoosterDurationUntilExpiration() 
											: Long.MAX_VALUE);
			plugin.getMysqlHandler().updateData(Type.SOLOENTRYQUERYSTATUS, sEQS,"`id` = ?", sEQS.getId());
		} else if(t.getTechnologyType() == TechnologyType.MULTIPLE && sEQS.getResearchLevel() >= t.getMaximalTechnologyLevelToResearch())
		{
			//Spieler hat schon das maxlevel erreischt.
			return 0; //Error
		}
		if(doUpdate)
		{
			doUpdate(player, t, 0, researchLevel);
		}		
		return researchLevel;
	}
	
	//Return the researchlevel
	public static int researchGlobalTechnology(Technology t, boolean doUpdate)
	{
		ArrayList<GlobalEntryQueryStatus> eqsList = GlobalEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.GLOBALENTRYQUERYSTATUS,
				"`research_level` DESC", 0, 1,
				"`intern_name` = ? AND `entry_query_type` = ?",
				t.getInternName(), EntryQueryType.TECHNOLOGY.toString()));
		GlobalEntryQueryStatus eqs = eqsList.size() == 0 ? null : eqsList.get(0);
		if(eqs == null)
		{
			eqs = new GlobalEntryQueryStatus(0, t.getInternName(),
					EntryQueryType.TECHNOLOGY, EntryStatusType.HAVE_RESEARCHED_IT, 1,
					t.getTechnologyType() == TechnologyType.BOOSTER ?
							System.currentTimeMillis()+t.getIfBoosterDurationUntilExpiration() : Long.MAX_VALUE);
			plugin.getMysqlHandler().create(Type.GLOBALENTRYQUERYSTATUS, eqs);
			return 1;
		} else if(t.getTechnologyType() == TechnologyType.SIMPLE && eqs != null && eqs.getStatusType() == EntryStatusType.HAVE_RESEARCHED_IT)
		{
			return 0;
		} else if(t.getTechnologyType() == TechnologyType.MULTIPLE 
				&& eqs != null && eqs.getStatusType() == EntryStatusType.HAVE_RESEARCHED_IT
				&& eqs.getResearchLevel() < t.getMaximalTechnologyLevelToResearch())
		{
			GlobalEntryQueryStatus eqsNew = eqs;
			int rsl = eqs.getResearchLevel()+1;
			eqsNew.setResearchLevel(rsl);
			plugin.getMysqlHandler().create(Type.GLOBALENTRYQUERYSTATUS, eqsNew);
			return rsl;
		} else if(eqs != null && eqs.getStatusType() == EntryStatusType.HAVE_RESEARCHED_IT
				&& eqs.getResearchLevel() == t.getMaximalTechnologyLevelToResearch())
		{
			return 0;
		}
		return 0;
	}
	
	public static void doUpdate(Player player, Technology t, int globalTechnologyPollID, int researchLevel)
	{
		boolean ifGlobal_HasParticipated = true;
		if(t.getPlayerAssociatedType() == PlayerAssociatedType.GLOBAL || globalTechnologyPollID > 0)
		{
			ifGlobal_HasParticipated = plugin.getMysqlHandler().exist(Type.GLOBALTECHNOLOGYPOLL,
					"`player_uuid` = ? AND `global_choosen_technology_id` = ?",
					player.getUniqueId().toString(), globalTechnologyPollID);
		}
		for(UnlockableInteraction ui : t.getRewardUnlockableInteractions().get(researchLevel))
		{
			addInteraction(player.getUniqueId(), ui,
					ifGlobal_HasParticipated ? t.getForUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent() : 100.0);
		}
		for(Entry<RecipeType, ArrayList<String>> rtl : t.getRewardRecipes().get(researchLevel).entrySet())
		{
			addRecipe(player.getUniqueId(), rtl.getKey(),
					ifGlobal_HasParticipated ? t.getForUninvolvedPollParticipants_RewardRecipesInPercent() : 100.0,
					rtl.getValue().toArray(new String[rtl.getValue().size()]));
		}
		for(DropChance dc : t.getRewardDropChances().get(researchLevel))
		{
			addDropChances(player.getUniqueId(), dc,
					ifGlobal_HasParticipated ? t.getForUninvolvedPollParticipants_RewardDropChancesInPercent() : 100.0);
		}
		for(DropChance dc : t.getRewardSilkTouchDropChances().get(researchLevel))
		{
			addSilkTouchDropChances(player.getUniqueId(), dc,
					ifGlobal_HasParticipated ? t.getForUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent() : 100.0);
		}
		double rripM = (ifGlobal_HasParticipated ? t.getForUninvolvedPollParticipants_RewardModifiersInPercent() : 100.0)/100;
		for(String s : t.getRewardModifierList().get(researchLevel))
		{
			String[] split = s.split(":");
			if(split.length != 8)
			{
				continue;
			}
			String bm = split[0];
			ModifierType mt = null;
			double v = 0.0;
			String ir = split[3].replace(" ", "");
			String dr = split[4];
			String server = split[5].equalsIgnoreCase("null") ? null : split[5];
			String world = split[6].equalsIgnoreCase("null") ? null : split[6];
			long duration = -1;
			try
			{
				mt = ModifierType.valueOf(split[1]);
				v = Double.parseDouble(split[2]) * rripM;
				duration = TimeHandler.getRepeatingTimeShortV2(split[7]);
			} catch(Exception e)
			{
				continue;
			}
			if(!plugin.getModifier().isRegistered(bm))
			{
				continue;
			}
			plugin.getModifier().addFactor(player.getUniqueId(), bm, v, mt, ir, dr, server, world, duration);
		}
		double rripC = (ifGlobal_HasParticipated ? t.getForUninvolvedPollParticipants_RewardCommandsInPercent() : 100.0)/100;
		for(String s : t.getRewardCommandList().get(researchLevel))
		{
			String[] split = s.split(":");
			if(split.length != 3)
			{
				continue;
			}
			if("spigot".equalsIgnoreCase(split[0]))
			{
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						split[2].replace("%value%", String.valueOf(Double.parseDouble(split[1]) * rripC))
								.replace("%player%", player.getName()));
			} else if("bungee".equalsIgnoreCase(split[0]) && plugin.getCommandToBungee() != null)
			{
				plugin.getCommandToBungee().executeAsConsole(
						split[2].replace("%value%", String.valueOf(Double.parseDouble(split[1]) * rripC))
								.replace("%player%", player.getName()));
			}
		}
		double rripV = (ifGlobal_HasParticipated ? t.getForUninvolvedPollParticipants_RewardValueEntryInPercent() : 100.0)/100;
		for(String s : t.getRewardValueEntryList().get(researchLevel))
		{
			String[] split = s.split(":");
			if(split.length != 7)
			{
				continue;
			}
			String c = split[0];
			String value = split[1];
			ValueType vt = null;
			if(MatchApi.isBoolean(value))
			{
				vt = ValueType.BOOLEAN;
				value = String.valueOf(rripV == 100 ? Boolean.valueOf(value) : !Boolean.valueOf(value));
			} else if(MatchApi.isDouble(value))
			{
				vt = ValueType.NUMBER;
				value = String.valueOf(Double.valueOf(value) * rripV);
			} else
			{
				vt = ValueType.TEXT;
			}
			String ir = split[2].replace(" ", "");
			String dr = split[3];
			String server = split[4].equalsIgnoreCase("null") ? null : split[4];
			String world = split[5].equalsIgnoreCase("null") ? null : split[5];
			long duration = TimeHandler.getRepeatingTimeShortV2(split[6]);
			if(!plugin.getValueEntry().isRegistered(c))
			{
				continue;
			}
			plugin.getValueEntry().addValueEntry(player.getUniqueId(), c, value, vt, ir, dr, server, world, duration);
		}
		for(String s : t.getRewardItemList().get(researchLevel))
		{
			String[] split = s.split(":");
			if(split.length != 2)
			{
				continue;
			}
			ItemStack is = new ItemGenerator().generateItem(
					player, plugin.getYamlHandler().getItemGenerators().get(split[0]), "", Integer.parseInt(split[1]));
			if(is == null)
			{
				continue;
			}
			HashMap<Integer, ItemStack> map = player.getInventory().addItem(is);
			if(!map.isEmpty())
			{
				for(ItemStack i : map.values())
				{
					player.getWorld().dropItem(player.getLocation(), i);
				}
			}
		}
	}
	
	//deposit the return, because other member of the community have paid it.
	public static boolean repaymentGlobalTechnology(Player player, Technology t, int researchlevel)
	{
		int techLevel = researchlevel;
		int acquiredTech = researchlevel - 1;
		int totalSoloTechs = plugin.getMysqlHandler().getCount(Type.SOLOENTRYQUERYSTATUS,
				"`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ?",
				player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString());
		int totalGlobalTechs = plugin.getMysqlHandler().getCount(Type.GLOBALENTRYQUERYSTATUS,
				"`entry_query_type` = ? AND `status_type` = ?",
				player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString());
		HashMap<String, Double> map = new HashMap<>();
		map.put(TECHLEVEL, Double.valueOf(techLevel));
		map.put(TECHACQUIRED, Double.valueOf(acquiredTech));
		map.put(SOLOTOTALTECH, Double.valueOf(totalSoloTechs));
		map.put(GLOBALTOTALTECH, Double.valueOf(totalGlobalTechs));
		double ttexp = 0;
		PlayerData pd = getPlayer(player.getUniqueId());
		if(pd != null && !t.getCostTTExp().isEmpty())
		{
			ttexp =  new MathFormulaParser().parse(t.getCostTTExp().get(techLevel), map);
		}
		int vexp = 0;
		if(!t.getCostVanillaExp().isEmpty())
		{
			vexp = (int) Math.floor(new MathFormulaParser().parse(t.getCostVanillaExp().get(techLevel), map));
		}
		double money = 0;
		if(!t.getCostMoney().isEmpty())
		{
			money = new MathFormulaParser().parse(t.getCostMoney().get(techLevel), map);
		}
		LinkedHashMap<Material, String> matmap = t.getCostMaterial().get(techLevel);
		pd.setActualTTExp(pd.getActualTTExp()+ttexp);
		updatePlayer(pd);
		Experience.changeExp(player, +vexp, true);
		if(money > 0)
		{
			if(plugin.getIFHEco() != null)
			{
				Account acc = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN,
						plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
				Account v = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.VOID,
						plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
				SubCategory sc = CatTechHandler.getSubCategory(t, t.getPlayerAssociatedType());
				MainCategory mc = CatTechHandler.getMainCategory(sc, sc.getPlayerAssociatedType());
				String category = plugin.getYamlHandler().getLang().getString("PlayerHandler.PollReTechnology.Category");
				String comment = plugin.getYamlHandler().getLang().getString("PlayerHandler.PollReTechnology.Comment")
						.replace("%technology%", t.getDisplayName())
						.replace("%subcategory%", sc.getDisplayName())
						.replace("%maincategory%", mc.getDisplayName());
				if(v != null && v.getBalance() > money)
				{
					plugin.getIFHEco().transaction(v, acc, money, OrdererType.PLAYER, player.getUniqueId().toString(),
							category,
							comment);
				} else
				{
					plugin.getIFHEco().deposit(acc, money, OrdererType.PLAYER, player.getUniqueId().toString(),
							category,
							comment);
				}
			} else if(plugin.getVaultEco() != null)
			{
				plugin.getVaultEco().withdrawPlayer(player, money);
			}
		}
		for(Entry<Material, String> e : matmap.entrySet())
		{
			int toAdd = Integer.valueOf(e.getValue());
			HashMap<Integer, ItemStack> addMap = player.getInventory().addItem(new ItemStack(e.getKey(), toAdd));
			if(!addMap.isEmpty())
			{
				for(ItemStack is : addMap.values())
				{
					Item it = player.getLocation().getWorld().dropItem(player.getLocation(), is);
					ItemHandler.addItemToTask(it, player.getUniqueId());
				}
			}
		}
		return true;
	}
	
	public static void addInteraction(UUID uuid, UnlockableInteraction ui, double rewardReceivedInPercent)
	{
		double rrip = rewardReceivedInPercent/100; //value for calculation
		if(ui.getEventMaterial() != null)
		{
			LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, SimpleUnlockedInteraction>>> mapI = new LinkedHashMap<>();
			if(materialInteractionMap.containsKey(uuid))
			{
				mapI = materialInteractionMap.get(uuid); 
			}
			LinkedHashMap<Material, LinkedHashMap<EventType, SimpleUnlockedInteraction>> mapII = new LinkedHashMap<>();
			if(mapI.containsKey(ui.getToolType()))
			{
				mapII = mapI.get(ui.getToolType());
			}
			LinkedHashMap<EventType, SimpleUnlockedInteraction> mapIII = new LinkedHashMap<>();
			if(mapII.containsKey(ui.getEventMaterial()))
			{
				mapIII = mapII.get(ui.getEventMaterial());
			}
			SimpleUnlockedInteraction sui = null;
			HashMap<String, Double> calMoneyMap = new HashMap<>();
			for(Entry<String, Double> e : ui.getMoneyMap().entrySet())
			{
				calMoneyMap.put(e.getKey(), e.getValue() * rrip);
			}
			HashMap<String, Double> calCmdMap = new HashMap<>();
			for(Entry<String, Double> e : ui.getCommandMap().entrySet())
			{
				calCmdMap.put(e.getKey(), e.getValue() * rrip);
			}
			if(mapIII.containsKey(ui.getEventType()))
			{
				sui = mapIII.get(ui.getEventType());
				sui.add(
						rrip == 100 ? ui.isCanAccess() : !ui.isCanAccess(),
						ui.getTechnologyExperience() * rrip,
						calMoneyMap,
						ui.getVanillaExperience() * rrip,
						calCmdMap);
			} else
			{
				sui = new SimpleUnlockedInteraction(
						rrip == 100 ? ui.isCanAccess() : !ui.isCanAccess(),
						ui.getTechnologyExperience() * rrip,
						calMoneyMap,
						ui.getVanillaExperience() * rrip,
						calCmdMap);				
			}
			mapIII.put(ui.getEventType(), sui);
			mapII.put(ui.getEventMaterial(), mapIII);
			mapI.put(ui.getToolType(), mapII);
			materialInteractionMap.put(uuid, mapI);
		} else if(ui.getEventEntityType() != null)
		{
			LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, SimpleUnlockedInteraction>>> mapI = new LinkedHashMap<>();
			if(entityTypeInteractionMap.containsKey(uuid))
			{
				mapI = entityTypeInteractionMap.get(uuid); 
			}
			LinkedHashMap<EntityType, LinkedHashMap<EventType, SimpleUnlockedInteraction>> mapII = new LinkedHashMap<>();
			if(mapI.containsKey(ui.getToolType()))
			{
				mapII = mapI.get(ui.getToolType());
			}
			LinkedHashMap<EventType, SimpleUnlockedInteraction> mapIII = new LinkedHashMap<>();
			if(mapII.containsKey(ui.getEventEntityType()))
			{
				mapIII = mapII.get(ui.getEventEntityType());
			}
			SimpleUnlockedInteraction sui = null;
			HashMap<String, Double> calMoneyMap = new HashMap<>();
			for(Entry<String, Double> e : ui.getMoneyMap().entrySet())
			{
				calMoneyMap.put(e.getKey(), e.getValue() * rrip);
			}
			HashMap<String, Double> calCmdMap = new HashMap<>();
			for(Entry<String, Double> e : ui.getCommandMap().entrySet())
			{
				calCmdMap.put(e.getKey(), e.getValue() * rrip);
			}
			if(mapIII.containsKey(ui.getEventType()))
			{
				sui = mapIII.get(ui.getEventType());
				sui.add(
						rrip == 100 ? ui.isCanAccess() : !ui.isCanAccess(),
						ui.getTechnologyExperience() * rrip,
						calMoneyMap,
						ui.getVanillaExperience() * rrip,
						calCmdMap);
			} else
			{
				sui = new SimpleUnlockedInteraction(
						rrip == 100 ? ui.isCanAccess() : !ui.isCanAccess(),
						ui.getTechnologyExperience() * rrip,
						calMoneyMap,
						ui.getVanillaExperience() * rrip,
						calCmdMap);
			}
			mapIII.put(ui.getEventType(), sui);
			mapII.put(ui.getEventEntityType(), mapIII);
			mapI.put(ui.getToolType(), mapII);
			entityTypeInteractionMap.put(uuid, mapI);
		}
	}
	
	public static void addRecipe(UUID uuid, RecipeType rt, double rewardReceivedInPercent, String... key)
	{
		double rrip = rewardReceivedInPercent/100; //value for calculation
		LinkedHashMap<RecipeType, ArrayList<String>> mapI = new LinkedHashMap<>();
		if(recipeMap.containsKey(uuid))
		{
			recipeMap.get(uuid);
		}
		ArrayList<String> listI = new ArrayList<>();
		if(mapI.containsKey(rt))
		{
			listI = mapI.get(rt);
		}
		for(String k : key)
		{
			if(rrip <= 0 || k.isBlank() || k.isEmpty())
			{
				break;
			}
			if(!listI.contains(k))
			{
				listI.add(k);
			}
		}
		mapI.put(rt, listI);
		recipeMap.put(uuid, mapI);
	}
	
	public static void addDropChances(UUID uuid, DropChance dc, double rewardReceivedInPercent)
	{
		double rrip = rewardReceivedInPercent/100; //value for calculation
		if(dc.getEventMaterial() != null)
		{
			LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>>> map0 = new LinkedHashMap<>();
			if(materialDropMap.containsKey(uuid))
			{
				map0 = materialDropMap.get(uuid); 
			}
			LinkedHashMap<Material, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>> mapI = new LinkedHashMap<>();
			if(map0.containsKey(dc.getToolType()))
			{
				mapI = map0.get(dc.getToolType()); 
			}
			LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>> mapII = new LinkedHashMap<>();
			if(mapI.containsKey(dc.getEventMaterial()))
			{
				mapII = mapI.get(dc.getEventMaterial());
			}
			LinkedHashMap<String, SimpleDropChance> mapIII = new LinkedHashMap<>();
			if(mapII.containsKey(dc.getEventType()))
			{
				mapIII = mapII.get(dc.getEventType());
			}
			SimpleDropChance sdc = null;
			if(mapIII.containsKey(dc.getToDropItem()))
			{
				sdc = mapIII.get(dc.getToDropItem());
				sdc.add(
						((int) (dc.getToDropItemAmount() * rrip)), 
						dc.getDropChance() * rrip);
			} else
			{
				sdc = new SimpleDropChance(
						dc.getToDropItem(),
						((int) (dc.getToDropItemAmount() * rrip)), 
						dc.getDropChance() * rrip);
			}
			mapIII.put(sdc.getToDropItem(), sdc);
			mapII.put(dc.getEventType(), mapIII);
			mapI.put(dc.getEventMaterial(), mapII);
			map0.put(dc.getToolType(), mapI);
			materialDropMap.put(uuid, map0);
		} else if(dc.getEventEntityType() != null)
		{
			LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>>> map0 = new LinkedHashMap<>();
			if(entityTypeDropMap.containsKey(uuid))
			{
				map0 = entityTypeDropMap.get(uuid); 
			}
			LinkedHashMap<EntityType, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>> mapI = new LinkedHashMap<>();
			if(map0.containsKey(dc.getToolType()))
			{
				mapI = map0.get(dc.getToolType()); 
			}
			LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>> mapII = new LinkedHashMap<>();
			if(mapI.containsKey(dc.getEventEntityType()))
			{
				mapII = mapI.get(dc.getEventEntityType());
			}
			LinkedHashMap<String, SimpleDropChance> mapIII = new LinkedHashMap<>();
			if(mapII.containsKey(dc.getEventType()))
			{
				mapIII = mapII.get(dc.getEventType());
			}
			SimpleDropChance sdc = null;
			if(mapIII.containsKey(dc.getToDropItem()))
			{
				sdc = mapIII.get(dc.getToDropItem());
				sdc.add(
						((int) (dc.getToDropItemAmount() * rrip)), 
						dc.getDropChance() * rrip);
			} else
			{
				sdc = new SimpleDropChance(
						dc.getToDropItem(),
						((int) (dc.getToDropItemAmount() * rrip)), 
						dc.getDropChance() * rrip);
			}
			mapIII.put(sdc.getToDropItem(), sdc);
			mapII.put(dc.getEventType(), mapIII);
			mapI.put(dc.getEventEntityType(), mapII);
			map0.put(dc.getToolType(), mapI);
			entityTypeDropMap.put(uuid, map0);
		}
	}
	
	public static void addSilkTouchDropChances(UUID uuid, DropChance dc, double rewardReceivedInPercent)
	{
		double rrip = rewardReceivedInPercent/100; //value for calculation
		if(dc.getEventMaterial() != null)
		{
			LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>>> map0 = new LinkedHashMap<>();
			if(materialSilkTouchDropMap.containsKey(uuid))
			{
				map0 = materialSilkTouchDropMap.get(uuid); 
			}
			LinkedHashMap<Material, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>> mapI = new LinkedHashMap<>();
			if(map0.containsKey(dc.getToolType()))
			{
				mapI = map0.get(dc.getToolType()); 
			}
			LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>> mapII = new LinkedHashMap<>();
			if(mapI.containsKey(dc.getEventMaterial()))
			{
				mapII = mapI.get(dc.getEventMaterial());
			}
			LinkedHashMap<String, SimpleDropChance> mapIII = new LinkedHashMap<>();
			if(mapII.containsKey(dc.getEventType()))
			{
				mapIII = mapII.get(dc.getEventType());
			}
			SimpleDropChance sdc = null;
			if(mapIII.containsKey(dc.getToDropItem()))
			{
				sdc = mapIII.get(dc.getToDropItem());
				sdc.add(
						((int) (dc.getToDropItemAmount() * rrip)), 
						dc.getDropChance() * rrip);
			} else
			{
				sdc = new SimpleDropChance(
						dc.getToDropItem(),
						((int) (dc.getToDropItemAmount() * rrip)), 
						dc.getDropChance() * rrip);
			}
			mapIII.put(sdc.getToDropItem(), sdc);
			mapII.put(dc.getEventType(), mapIII);
			mapI.put(dc.getEventMaterial(), mapII);
			map0.put(dc.getToolType(), mapI);
			materialSilkTouchDropMap.put(uuid, map0);
		} else if(dc.getEventEntityType() != null)
		{
			LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>>> map0 = new LinkedHashMap<>();
			if(entityTypeSilkTouchDropMap.containsKey(uuid))
			{
				map0 = entityTypeSilkTouchDropMap.get(uuid); 
			}
			LinkedHashMap<EntityType, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>> mapI = new LinkedHashMap<>();
			if(map0.containsKey(dc.getToolType()))
			{
				mapI = map0.get(dc.getToolType()); 
			}
			LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>> mapII = new LinkedHashMap<>();
			if(mapI.containsKey(dc.getEventEntityType()))
			{
				mapII = mapI.get(dc.getEventEntityType());
			}
			LinkedHashMap<String, SimpleDropChance> mapIII = new LinkedHashMap<>();
			if(mapII.containsKey(dc.getEventType()))
			{
				mapIII = mapII.get(dc.getEventType());
			}
			SimpleDropChance sdc = null;
			if(mapIII.containsKey(dc.getToDropItem()))
			{
				sdc = mapIII.get(dc.getToDropItem());
				sdc.add(
						((int) (dc.getToDropItemAmount() * rrip)), 
						dc.getDropChance() * rrip);
			} else
			{
				sdc = new SimpleDropChance(
						dc.getToDropItem(),
						((int) (dc.getToDropItemAmount() * rrip)), 
						dc.getDropChance() * rrip);
			}
			mapIII.put(sdc.getToDropItem(), sdc);
			mapII.put(dc.getEventType(), mapIII);
			mapI.put(dc.getEventEntityType(), mapII);
			map0.put(dc.getToolType(), mapI);
			entityTypeSilkTouchDropMap.put(uuid, map0);
		}
	}
}