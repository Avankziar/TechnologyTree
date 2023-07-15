package main.java.me.avankziar.tt.spigot.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.ifh.general.modifier.ModifierType;
import main.java.me.avankziar.ifh.general.valueentry.ValueType;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.assistance.MatchApi;
import main.java.me.avankziar.tt.spigot.assistance.TimeHandler;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.BlockHandler.BlockType;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.ifh.ItemGenerator;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.TechnologyType;
import main.java.me.avankziar.tt.spigot.objects.mysql.EntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.EntryQueryStatus.EntryQueryType;
import main.java.me.avankziar.tt.spigot.objects.mysql.EntryQueryStatus.StatusType;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
import main.java.me.avankziar.tt.spigot.objects.mysql.RegisteredBlock;
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
	
	public static LinkedHashMap<UUID, LinkedHashMap<Material, LinkedHashMap<EventType, SimpleUnlockedInteraction>>> materialInteractionMap = new LinkedHashMap<>();
	public static LinkedHashMap<UUID, LinkedHashMap<EntityType, LinkedHashMap<EventType, SimpleUnlockedInteraction>>> entityTypeInteractionMap = new LinkedHashMap<>();
	
	public static LinkedHashMap<UUID, LinkedHashMap<Material, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>>> materialDropMap = new LinkedHashMap<>(); //String ist der Internename vom SimpleDropChance
	public static LinkedHashMap<UUID, LinkedHashMap<EntityType, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>>> entityTypeDropMap = new LinkedHashMap<>();
	
	public static LinkedHashMap<UUID, LinkedHashMap<Material, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>>> materialSilkTouchDropMap = new LinkedHashMap<>(); //String ist der Internename vom SimpleDropChance
	public static LinkedHashMap<UUID, LinkedHashMap<EntityType, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>>> entityTypeSilkTouchDropMap = new LinkedHashMap<>();
	
	public static LinkedHashMap<UUID, LinkedHashMap<RecipeType, ArrayList<String>>> recipeMap = new LinkedHashMap<>(); //UUID, RecipeType und der Key des Recipe
	
	public static LinkedHashMap<UUID, LinkedHashMap<BlockType, ArrayList<String>>> registeredBlocks = new LinkedHashMap<>();//UUID, BlockType, Location as Text
	
	public static void joinPlayer(Player player)
	{
		UUID uuid = player.getUniqueId();
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
				researchTechnology(player, t, false);
			}
		}
		PlayerData pd = getPlayer(player.getUniqueId());
		if(pd.isShowSyncMessage())
		{
			player.spigot().sendMessage(ChatApi.tctl("PlayerHandler.SyncStart"));
		}
		for(EntryQueryStatus eqs : EntryQueryStatus.convert(plugin.getMysqlHandler().getFullList(Type.ENTRYQUERYSTATUS,
				"`id` ASC", "`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ?",
				player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), StatusType.HAVE_RESEARCHED_IT.toString())))
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
			for(UnlockableInteraction ui : t.getRewardUnlockableInteractions())
			{
				addInteraction(uuid, ui);
			}
			for(Entry<RecipeType, ArrayList<String>> rtl : t.getRewardRecipes().entrySet())
			{
				addRecipe(uuid, rtl.getKey(), rtl.getValue().toArray(new String[rtl.getValue().size()]));
			}
			for(DropChance dc : t.getRewardDropChances())
			{
				addDropChances(uuid, dc);
			}
			for(DropChance dc : t.getRewardSilkTouchDropChances())
			{
				addSilkTouchDropChances(uuid, dc);
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
			player.spigot().sendMessage(ChatApi.tctl("PlayerHandler.SyncEnd"));
		}
	}
	
	public static void quitPlayer(final UUID uuid)
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
				0, 0, 0);
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
	
	public static ItemStack canSeeOrResearch(UUID uuid, MainCategory mcat, SubCategory scat, Technology tech)
	{
		if(mcat != null)
		{
			EntryQueryStatus eqs = (EntryQueryStatus) plugin.getMysqlHandler().getData(Type.ENTRYQUERYSTATUS,
					"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
					uuid.toString(), mcat.getInternName(), EntryQueryType.MAIN_CATEGORY.toString());
			if(eqs == null)
			{
				
			}
		} else if(scat != null)
		{
			
		} else if(tech != null)
		{
			
		}
		return null;
	}
	
	public static void researchTechnology(Player player, Technology t, boolean doUpdate)
	{
		EntryQueryStatus eeqs = (EntryQueryStatus) plugin.getMysqlHandler().getData(Type.ENTRYQUERYSTATUS,
				"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ? AND `status_type` = ?",
				player.getUniqueId().toString(), t.getInternName(), EntryQueryType.TECHNOLOGY.toString(), StatusType.HAVE_RESEARCHED_IT.toString());
		if(t.getTechnologyType() == TechnologyType.SIMPLE && eeqs != null)
		{
			return;
		} else if(t.getTechnologyType() == TechnologyType.MULTIPLE 
				&& eeqs != null 
				&& eeqs.getResearchLevel() < t.getMaximalTechnologyLevelToResearch())
		{
			eeqs.setResearchLevel(eeqs.getResearchLevel()+1);
			plugin.getMysqlHandler().updateData(Type.ENTRYQUERYSTATUS, eeqs,
					"`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ?",
					player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), StatusType.HAVE_RESEARCHED_IT.toString());
		} else
		{
			EntryQueryStatus eqs = new EntryQueryStatus(0, t.getInternName(), player.getUniqueId(),
					EntryQueryType.TECHNOLOGY, StatusType.HAVE_RESEARCHED_IT, 1);
			plugin.getMysqlHandler().create(Type.ENTRYQUERYSTATUS, eqs);
		}
		if(doUpdate)
		{
			for(UnlockableInteraction ui : t.getRewardUnlockableInteractions())
			{
				addInteraction(player.getUniqueId(), ui);
			}
			for(Entry<RecipeType, ArrayList<String>> rtl : t.getRewardRecipes().entrySet())
			{
				addRecipe(player.getUniqueId(), rtl.getKey(), rtl.getValue().toArray(new String[rtl.getValue().size()]));
			}
			for(DropChance dc : t.getRewardDropChances())
			{
				addDropChances(player.getUniqueId(), dc);
			}
			for(DropChance dc : t.getRewardSilkTouchDropChances())
			{
				addSilkTouchDropChances(player.getUniqueId(), dc);
			}
			for(String s : t.getRewardModifierList())
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
					v = Double.parseDouble(split[2]);
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
			for(String s : t.getRewardCommandList())
			{
				String[] split = s.split(":");
				if(split.length != 3)
				{
					continue;
				}
				if("spigot".equalsIgnoreCase(split[0]))
				{
					if("player".equalsIgnoreCase(split[1]))
					{
						Bukkit.dispatchCommand(player, split[2].replace("%player%", player.getName()));
					} else if("console".equalsIgnoreCase(split[1]))
					{
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), split[2].replace("%player%", player.getName()));
					}
				} else if("bungee".equalsIgnoreCase(split[0]) && plugin.getCommandToBungee() != null)
				{
					if("player".equalsIgnoreCase(split[1]))
					{
						plugin.getCommandToBungee().executeAsPlayer(split[2].replace("%player%", player.getName()), player.getUniqueId());
					} else if("console".equalsIgnoreCase(split[1]))
					{
						plugin.getCommandToBungee().executeAsConsole(split[2].replace("%player%", player.getName()));
					}
				}
			}
			for(String s : t.getRewardValueEntryList())
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
				} else if(MatchApi.isDouble(value))
				{
					vt = ValueType.NUMBER;
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
			for(String s : t.getRewardItemList())
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
	}
	
	public static void addInteraction(UUID uuid, UnlockableInteraction ui)
	{
		if(ui.getEventMaterial() != null)
		{
			LinkedHashMap<Material, LinkedHashMap<EventType, SimpleUnlockedInteraction>> mapI = new LinkedHashMap<>();
			if(materialInteractionMap.containsKey(uuid))
			{
				mapI = materialInteractionMap.get(uuid); 
			}
			LinkedHashMap<EventType, SimpleUnlockedInteraction> mapII = new LinkedHashMap<>();
			if(mapI.containsKey(ui.getEventMaterial()))
			{
				mapII = mapI.get(ui.getEventMaterial());
			}
			SimpleUnlockedInteraction sui = null;
			if(mapII.containsKey(ui.getEventType()))
			{
				sui = mapII.get(ui.getEventType());
				sui.add(ui.isCanAccess(), ui.getTechnologyExperience(), ui.getMoneyMap(), ui.getVanillaExperience(), ui.getCommandMap());
			} else
			{
				sui = new SimpleUnlockedInteraction(ui.isCanAccess(),
						ui.getTechnologyExperience(),
						ui.getMoneyMap(),
						ui.getVanillaExperience(), ui.getCommandMap());
			}
			mapII.put(ui.getEventType(), sui);
			mapI.put(ui.getEventMaterial(), mapII);
			materialInteractionMap.put(uuid, mapI);
		} else if(ui.getEventEntityType() != null)
		{
			LinkedHashMap<EntityType, LinkedHashMap<EventType, SimpleUnlockedInteraction>> mapI = new LinkedHashMap<>();
			if(entityTypeInteractionMap.containsKey(uuid))
			{
				mapI = entityTypeInteractionMap.get(uuid); 
			}
			LinkedHashMap<EventType, SimpleUnlockedInteraction> mapII = new LinkedHashMap<>();
			if(mapI.containsKey(ui.getEventEntityType()))
			{
				mapII = mapI.get(ui.getEventEntityType());
			}
			SimpleUnlockedInteraction sui = null;
			if(mapII.containsKey(ui.getEventType()))
			{
				sui = mapII.get(ui.getEventType());
				sui.add(ui.isCanAccess(), ui.getTechnologyExperience(), ui.getMoneyMap(), ui.getVanillaExperience(), ui.getCommandMap());
			} else
			{
				sui = new SimpleUnlockedInteraction(ui.isCanAccess(),
						ui.getTechnologyExperience(),
						ui.getMoneyMap(),
						ui.getVanillaExperience(), ui.getCommandMap());
			}
			mapII.put(ui.getEventType(), sui);
			mapI.put(ui.getEventEntityType(), mapII);
			entityTypeInteractionMap.put(uuid, mapI);
		}
	}
	
	public static void addRecipe(UUID uuid, RecipeType rt, String... key)
	{
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
			if(!listI.contains(k))
			{
				listI.add(k);
			}
		}
		mapI.put(rt, listI);
		recipeMap.put(uuid, mapI);
	}
	
	public static void addDropChances(UUID uuid, DropChance dc)
	{
		if(dc.getEventMaterial() != null)
		{
			LinkedHashMap<Material, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>> mapI = new LinkedHashMap<>();
			if(materialDropMap.containsKey(uuid))
			{
				mapI = materialDropMap.get(uuid); 
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
				sdc.add(dc.getToDropItemAmount(), dc.getDropChance());
			} else
			{
				sdc = new SimpleDropChance(dc.getToDropItem(), dc.getToDropItemAmount(), dc.getDropChance());
			}
			mapIII.put(sdc.getToDropItem(), sdc);
			mapII.put(dc.getEventType(), mapIII);
			mapI.put(dc.getEventMaterial(), mapII);
			materialDropMap.put(uuid, mapI);
		} else if(dc.getEventEntity() != null)
		{
			LinkedHashMap<EntityType, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>> mapI = new LinkedHashMap<>();
			if(entityTypeDropMap.containsKey(uuid))
			{
				mapI = entityTypeDropMap.get(uuid); 
			}
			LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>> mapII = new LinkedHashMap<>();
			if(mapI.containsKey(dc.getEventEntity()))
			{
				mapII = mapI.get(dc.getEventEntity());
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
				sdc.add(dc.getToDropItemAmount(), dc.getDropChance());
			} else
			{
				sdc = new SimpleDropChance(dc.getToDropItem(), dc.getToDropItemAmount(), dc.getDropChance());
			}
			mapIII.put(sdc.getToDropItem(), sdc);
			mapII.put(dc.getEventType(), mapIII);
			mapI.put(dc.getEventEntity(), mapII);
			entityTypeDropMap.put(uuid, mapI);
		}
	}
	
	public static void addSilkTouchDropChances(UUID uuid, DropChance dc)
	{
		if(dc.getEventMaterial() != null)
		{
			LinkedHashMap<Material, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>> mapI = new LinkedHashMap<>();
			if(materialSilkTouchDropMap.containsKey(uuid))
			{
				mapI = materialSilkTouchDropMap.get(uuid); 
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
				sdc.add(dc.getToDropItemAmount(), dc.getDropChance());
			} else
			{
				sdc = new SimpleDropChance(dc.getToDropItem(), dc.getToDropItemAmount(), dc.getDropChance());
			}
			mapIII.put(sdc.getToDropItem(), sdc);
			mapII.put(dc.getEventType(), mapIII);
			mapI.put(dc.getEventMaterial(), mapII);
			materialSilkTouchDropMap.put(uuid, mapI);
		} else if(dc.getEventEntity() != null)
		{
			LinkedHashMap<EntityType, LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>>> mapI = new LinkedHashMap<>();
			if(entityTypeSilkTouchDropMap.containsKey(uuid))
			{
				mapI = entityTypeSilkTouchDropMap.get(uuid); 
			}
			LinkedHashMap<EventType, LinkedHashMap<String, SimpleDropChance>> mapII = new LinkedHashMap<>();
			if(mapI.containsKey(dc.getEventEntity()))
			{
				mapII = mapI.get(dc.getEventEntity());
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
				sdc.add(dc.getToDropItemAmount(), dc.getDropChance());
			} else
			{
				sdc = new SimpleDropChance(dc.getToDropItem(), dc.getToDropItemAmount(), dc.getDropChance());
			}
			mapIII.put(sdc.getToDropItem(), sdc);
			mapII.put(dc.getEventType(), mapIII);
			mapI.put(dc.getEventEntity(), mapII);
			entityTypeSilkTouchDropMap.put(uuid, mapI);
		}
	}
}