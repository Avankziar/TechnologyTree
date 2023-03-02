package main.java.me.avankziar.tt.spigot.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.ifh.general.bonusmalus.BonusMalusValueType;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.assistance.MatchApi;
import main.java.me.avankziar.tt.spigot.assistance.TimeHandler;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.conditionbonusmalus.Bypass;
import main.java.me.avankziar.tt.spigot.conditionbonusmalus.ConditionBonusMalus;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.BlockHandler.BlockType;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.ifh.ItemGenerator;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.RewardType;
import main.java.me.avankziar.tt.spigot.objects.mysql.EntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.EntryQueryStatus.EntryQueryType;
import main.java.me.avankziar.tt.spigot.objects.mysql.EntryQueryStatus.StatusType;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
import main.java.me.avankziar.tt.spigot.objects.mysql.RegisteredBlock;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.DropChance;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SimpleDropChance;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SimpleUnlockedInteraction;
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
		PlayerData pd = getPlayer(player);
		
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
		if(pd.isShowSyncMessage())
		{
			player.spigot().sendMessage(ChatApi.tctl("PlayerHandler.SyncEnd"));
		}
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
				plugin.getYamlHandler().getConfig().getBoolean("Do.NewPlayer.ShowSyncMessage", true));
		plugin.getMysqlHandler().create(Type.PLAYERDATA, pd);
	}
	
	public static PlayerData getPlayer(Player player)
	{
		return (PlayerData) plugin.getMysqlHandler().getData(Type.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
	}
	
	public static void researchTechnology(Player player, Technology t, boolean doUpdate)
	{
		if(plugin.getMysqlHandler().exist(Type.ENTRYQUERYSTATUS,
				"`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ?",
				player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), StatusType.HAVE_RESEARCHED_IT.toString()))
		{
			return;
		}
		EntryQueryStatus eqs = new EntryQueryStatus(0, t.getInternName(), player.getUniqueId(),
				EntryQueryType.TECHNOLOGY, StatusType.HAVE_RESEARCHED_IT);
		plugin.getMysqlHandler().create(Type.ENTRYQUERYSTATUS, eqs);
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
			for(String s : t.getRewardBonusMalusList())
			{
				String[] split = s.split(":");
				if(split.length != 8)
				{
					continue;
				}
				String bm = split[0];
				BonusMalusValueType bmvt = null;
				double v = 0.0;
				String ir = split[3].replace(" ", "");
				String dr = split[4];
				String server = split[5].equalsIgnoreCase("null") ? null : split[5];
				String world = split[6].equalsIgnoreCase("null") ? null : split[6];
				long duration = -1;
				try
				{
					bmvt = BonusMalusValueType.valueOf(split[1]);
					v = Double.parseDouble(split[2]);
					duration = TimeHandler.getRepeatingTimeShortV2(split[7]);
				} catch(Exception e)
				{
					continue;
				}
				if(!plugin.getBonusMalus().isRegistered(bm))
				{
					continue;
				}
				if(bmvt == BonusMalusValueType.ADDITION)
				{
					plugin.getBonusMalus().addAdditionFactor(player.getUniqueId(), bm, v, ir, dr, server, world, duration);
				} else 
				{
					plugin.getBonusMalus().addMultiplicationFactor(player.getUniqueId(), bm, v, ir, dr, server, world, duration);
				}
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
			for(String s : t.getRewardConditionEntryList())
			{
				String[] split = s.split(":");
				if(split.length != 7)
				{
					continue;
				}
				String c = split[0];
				String value = split[1];
				String ir = split[2].replace(" ", "");
				String dr = split[3];
				String server = split[4].equalsIgnoreCase("null") ? null : split[4];
				String world = split[5].equalsIgnoreCase("null") ? null : split[5];
				long duration = TimeHandler.getRepeatingTimeShortV2(split[6]);
				if(!plugin.getCondition().isRegistered(c))
				{
					continue;
				}
				plugin.getCondition().addConditionEntry(player.getUniqueId(), c, value, ir, dr, server, world, duration);
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
	
	public static boolean canAccessInteraction(Player player, EventType eventType, Material material, EntityType entityType) //Return true, if player cannot maniplulate a Block/Material
	{
		if(player == null || eventType == null)
		{
			return false;
		}
		UUID uuid = player.getUniqueId();
		if(material != null)
		{
			if(materialInteractionMap.containsKey(uuid)
					&& materialInteractionMap.get(uuid).containsKey(material)
					&& materialInteractionMap.get(uuid).get(material).containsKey(eventType))
			{
				boolean b = materialInteractionMap.get(uuid).get(material).get(eventType).isCanAccess();
				return b ? b : conditionResult(plugin.getCondition().getConditionEntry(uuid, 
							CatTechHandler.getCondition(RewardType.ACCESS, eventType, material, entityType),
							plugin.getServername(), player.getWorld().getName()));
			}
		} else if(entityType != null)
		{
			if(entityTypeInteractionMap.containsKey(uuid)
					&& entityTypeInteractionMap.get(uuid).containsKey(entityType)
					&& entityTypeInteractionMap.get(uuid).get(entityType).containsKey(eventType))
			{
				boolean b = entityTypeInteractionMap.get(uuid).get(entityType).get(eventType).isCanAccess();
				return b ? b : conditionResult(plugin.getCondition().getConditionEntry(uuid, 
						CatTechHandler.getCondition(RewardType.ACCESS, eventType, material, entityType),
						plugin.getServername(), player.getWorld().getName()));
			}
		}
		return false;
	}
	
	private static boolean conditionResult(String[] condition)
	{
		int t = 0;
		int f = 0;
		for(String c : condition)
		{
			if(MatchApi.isBoolean(c))
			{
				if(MatchApi.getBoolean(c))
				{
					t++;
				} else
				{
					f++;
				}
			}
		}
		return t > 0 && t > f;
	}
	
	public static boolean canAccessRecipe(UUID uuid, RecipeType rt, String key)
	{
		if(recipeMap.containsKey(uuid)
				&& recipeMap.get(uuid).containsKey(rt)
				&& recipeMap.get(uuid).get(rt).contains(key))
		{
			return true;
		}
		return false;
	}
	
	public static boolean UseTTDropMechanicCalculation(Player player)
	{
		if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
		{
			return false;
		}
		if(!plugin.getYamlHandler().getConfig().getBoolean("Do.Drops.UsePluginDropsCalculation", false))
		{
			return false;
		}
		if(plugin.getYamlHandler().getConfig().getStringList("Do.Drops.DoNotUsePluginDropsCalculationWorlds").contains(player.getWorld().getName()))
		{
			return false;
		}
		//TODO Do Config.WorldList And Worldguard
		return true;
	}
	
	public static ArrayList<ItemStack> getDrops(Player player, EventType eventType, Material material, EntityType entityType)
	{
		//https://minecraft.fandom.com/wiki/Fortune
		boolean breakingThroughVanillaDropBarrier = plugin.getYamlHandler().getConfig().getBoolean("Do.Drops.BreakingThroughVanillaDropBarrier", true);
		boolean silkTouch = (player.getInventory().getItemInMainHand() != null &&
				player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0);
		//https://minecraft.fandom.com/wiki/Luck
		int lucklevel = (player.getPotionEffect(PotionEffectType.LUCK) == null 
						? -1 
						: player.getPotionEffect(PotionEffectType.LUCK).getAmplifier()) + 1;
		UUID uuid = player.getUniqueId();
		ArrayList<ItemStack> list = new ArrayList<>();
		if(material != null)
		{
			//https://minecraft.fandom.com/wiki/Fortune
			int fortunelevel = 0;
			if(player.getInventory().getItemInMainHand() != null &&
					player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) > 0)
			{
				fortunelevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
			}
			if(silkTouch)
			{
				if(materialSilkTouchDropMap.containsKey(uuid)
						&& materialSilkTouchDropMap.get(uuid).containsKey(material)
						&& materialSilkTouchDropMap.get(uuid).get(material).containsKey(eventType))
				{
					for(SimpleDropChance sdc : materialSilkTouchDropMap.get(uuid).get(material).get(eventType).values())
					{
						ItemStack is = getSingleDrops(player, sdc, fortunelevel, lucklevel, 
								breakingThroughVanillaDropBarrier, eventType, material, entityType);
						list.add(is);
					}
				}
			} else
			{
				if(materialDropMap.containsKey(uuid)
						&& materialDropMap.get(uuid).containsKey(material)
						&& materialDropMap.get(uuid).get(material).containsKey(eventType))
				{
					for(SimpleDropChance sdc : materialDropMap.get(uuid).get(material).get(eventType).values())
					{
						ItemStack is = getSingleDrops(player, sdc, fortunelevel, lucklevel,
								breakingThroughVanillaDropBarrier, eventType, material, entityType);
						list.add(is);
					}
				}
			}
		} else if(entityType != null)
		{
			int lootlevel = 0;
			if(eventType != EventType.PLAYER_FISH)
			{
				if(player.getInventory().getItemInMainHand() != null &&
						player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) > 0)
				{
					lootlevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
				}
			} else
			{
				if(player.getInventory().getItemInMainHand() != null &&
						player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LUCK) > 0)
				{
					lootlevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LUCK);
				}
			}
			if(silkTouch)
			{
				if(entityTypeSilkTouchDropMap.containsKey(uuid)
						&& entityTypeSilkTouchDropMap.get(uuid).containsKey(entityType)
						&& entityTypeSilkTouchDropMap.get(uuid).get(entityType).containsKey(eventType))
				{
					for(SimpleDropChance sdc : entityTypeSilkTouchDropMap.get(uuid).get(entityType).get(eventType).values())
					{
						ItemStack is = getSingleDrops(player, sdc, lootlevel, lucklevel,
								breakingThroughVanillaDropBarrier, eventType, material, entityType);
						list.add(is);
					}
				}
			} else
			{
				if(entityTypeDropMap.containsKey(uuid)
						&& entityTypeDropMap.get(uuid).containsKey(entityType)
						&& entityTypeDropMap.get(uuid).get(entityType).containsKey(eventType))
				{
					for(SimpleDropChance sdc : entityTypeDropMap.get(uuid).get(entityType).get(eventType).values())
					{
						ItemStack is = getSingleDrops(player, sdc, lootlevel, lucklevel,
								breakingThroughVanillaDropBarrier, eventType, material, entityType);
						list.add(is);
					}
				}
			}
		}
		return list;
	}
	
	private static ItemStack getSingleDrops(Player player, SimpleDropChance sdc,
			int fortunelootlevel, int potionlucklevel,
			boolean breakingThroughVanillaDropBarrier,
			EventType eventType, Material material, EntityType entityType)
	{
		int i = 0;
		Map<Integer, Double> sortedMap = 
	    	     sdc.getAmountToDropChance().entrySet().stream()
	    	    .sorted(Entry.comparingByKey())
	    	    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
	    	                              (e1, e2) -> e1, LinkedHashMap::new));
		
		for(Entry<Integer, Double> e : sortedMap.entrySet())
		{
			double chance = e.getValue();
			if(fortunelootlevel > 0 && potionlucklevel > 0)
			{
				chance = chance 
						* (1.0/((double)fortunelootlevel+2.0)+((double)fortunelootlevel+1.0)/2.0)
						* (1.0/((double)potionlucklevel+2.0) +((double)potionlucklevel*2)/2.0);
			} else if(fortunelootlevel > 0)
			{
				chance = chance * (1.0/((double)fortunelootlevel+2.0)+((double)fortunelootlevel+1.0)/2.0);
			} else if(potionlucklevel > 0)
			{
				chance = chance * (1.0/((double)potionlucklevel+2.0)+((double)potionlucklevel*2)/2.0);
			}
			if(chance >= 1.0)
			{
				i = e.getKey();
			} else if(new Random().nextDouble() < chance)
			{
				i = e.getKey();
			}
		}
		if(plugin.getBonusMalus() != null)
		{
			i = (int) plugin.getBonusMalus().getResult(player.getUniqueId(), (double) i,
					CatTechHandler.getBonusMalus(RewardType.DROPS, eventType, material, entityType),
					plugin.getServername(), player.getWorld().getName());
		}
		ItemStack is = sdc.getItem(player, i);
		if(!breakingThroughVanillaDropBarrier)
		{
			is.setAmount(getVanillaDropBarrier(is.getType(), i));
		}
		return is;
	}
	
	private static int getVanillaDropBarrier(Material material, int i)
	{
		switch(material)
		{
		default:
			return i;
		case PRISMARINE_CRYSTALS:
			return 3;
		case PRISMARINE_SHARD:
		case GLOWSTONE_DUST:
		case COAL:
		case DIAMOND:
		case EMERALD:
		case RAW_IRON:
		case RAW_GOLD:
		case QUARTZ:
			return 4;
		case SWEET_BERRIES:
		case BEETROOT_SEEDS:
			return 6;
		case NETHER_WART:
		case WHEAT_SEEDS:
			return 7;
		case REDSTONE:
			return 8;
		case MELON_SLICE:
			return 9;
		case AMETHYST_SHARD:
			return 16;
		case RAW_COPPER:
			return 20;
		case GOLD_NUGGET:
			return 24;
		case LAPIS_LAZULI:
			return 36;
		}
	}
}