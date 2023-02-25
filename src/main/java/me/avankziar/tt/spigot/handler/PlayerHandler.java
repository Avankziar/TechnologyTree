package main.java.me.avankziar.tt.spigot.handler;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.mysql.EntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.EntryQueryStatus.EntryQueryType;
import main.java.me.avankziar.tt.spigot.objects.mysql.EntryQueryStatus.StatusType;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
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
	
	public static void joinPlayer(Player player)
	{
		if(!hasAccount(player))
		{
			createAccount(player);
			if(plugin.getYamlHandler().getConfig().get("Do.NewPlayer.AutoResearchTechnology") != null)
			{
				for(String s : plugin.getYamlHandler().getConfig().getStringList("Do.NewPlayer.AutoResearchTechnology"))
				{
					Technology t = CatTechHandler.technologyMapSolo.get(s);
					if(t == null)
					{
						continue;
					}
					researchTechnology(player, t, false);
				}
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
				continue;
			}
			for(UnlockableInteraction ui : t.getRewardUnlockableInteractions())
			{
				addInteraction(player.getUniqueId(), ui);
			}
			for(DropChance dc : t.getRewardDropChances())
			{
				addDropChances(player.getUniqueId(), dc);
			}
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
		EntryQueryStatus eqs = new EntryQueryStatus(0, t.getInternName(), player.getUniqueId(),
				EntryQueryType.TECHNOLOGY, StatusType.HAVE_RESEARCHED_IT);
		if(plugin.getMysqlHandler().exist(Type.ENTRYQUERYSTATUS,
				"`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ?",
				player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), StatusType.HAVE_RESEARCHED_IT.toString()))
		{
			return;
		}
		plugin.getMysqlHandler().create(Type.ENTRYQUERYSTATUS, eqs);
		if(doUpdate)
		{
			for(UnlockableInteraction ui : t.getRewardUnlockableInteractions())
			{
				addInteraction(player.getUniqueId(), ui);
			}
			for(DropChance dc : t.getRewardDropChances())
			{
				addDropChances(player.getUniqueId(), dc);
			}
		}		
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
	
	public static boolean canAccess(Player player, EventType eventType, Material material, EntityType entityType) //Return true, if player cannot maniplulate a Block/Material
	{
		if(material != null)
		{
			
		} else if(entityType != null)
		{
			
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
	
	public static double getDrops(Player player, EventType eventType, Material material, EntityType entityType)
	{
		if(material != null)
		{
			
		} else if(entityType != null)
		{
			
		}
		return 0;
	}
}