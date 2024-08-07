package main.java.me.avankziar.tt.spigot.hook;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.objects.EntryQueryType;
import main.java.me.avankziar.tt.spigot.objects.EntryStatusType;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.DropChance;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SimpleDropChance;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.Technology;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.UnlockableInteraction;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PAPIHook extends PlaceholderExpansion
{
	private TT plugin;
	
	public PAPIHook(TT plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean persist()
	{
		return true;
	}
	
	@Override
	public boolean canRegister()
	{
		return true;
	}
	
	@Override
	public String getAuthor()
	{
		return plugin.getDescription().getAuthors().toString();
	}
	
	@Override
	public String getIdentifier()
	{
		return "tt";
	}
	
	@Override
	public String getVersion()
	{
		return plugin.getDescription().getVersion();
	}
	
	/*
	 * tt_hastech,<PAT>,<Tech>
	 * 
	 * Return the free TTExp of the Player
	 * tt_player_ttexp_free
	 * Return the total TTExp alltimes earned of the player
	 * tt_player_ttexp_total
	 * Return the vanillaexp still to be optained
	 * tt_player_vaexp_stilltobeobtained
	 * 
	 * Return the raw number of what ttexp reward per tech and lvl or per player.
	 * tt_raw_reward_tech_ttexp_mat,<PAT>,<Tech>,<Lvl>,<Event>,<Tool>,<Material>
	 * tt_raw_reward_tech_ttexp_ent,<PAT>,<Tech>,<Lvl>,<Event>,<Tool>,<Entity>
	 * tt_raw_reward_player_ttexp_mat,<Event>,<Tool>,<Material>
	 * tt_raw_reward_player_ttexp_ent,<Event>,<Tool>,<Entity>
	 * tt_raw_reward_techtotal_ttexp_mat,<PAT>,<Tech>,<Event>,<Tool>,<Material>
	 * tt_raw_reward_techtotal_ttexp_ent,<PAT>,<Tech>,<Event>,<Tool>,<Entity>
	 * 
	 * Return the raw number of what vanillaexp reward per tech and lvl or per player.
	 * tt_raw_reward_tech_vexp_mat,<PAT>,<Tech>,<Lvl>,<Event>,<Tool>,<Material>
	 * tt_raw_reward_tech_vexp_ent,<PAT>,<Tech>,<Lvl>,<Event>,<Tool>,<Entity>
	 * tt_raw_reward_player_vexp_mat,<Event>,<Tool>,<Material>
	 * tt_raw_reward_player_vexp_ent,<Event>,<Tool>,<Entity>
	 * tt_raw_reward_techtotal_vexp_mat,<PAT>,<Tech>,<Event>,<Tool>,<Material>
	 * tt_raw_reward_techtotal_vexp_ent,<PAT>,<Tech>,<Event>,<Tool>,<Entity>
	 * 
	 * Return the raw number of what money reward per tech and lvl or per player.
	 * tt_raw_reward_tech_money_mat,<PAT>,<Tech>,<Lvl>,<Event>,<Tool>,<Material>,<Currencyname|vault|default|other something other>
	 * tt_raw_reward_tech_money_ent,<PAT>,<Tech>,<Lvl>,<Event>,<Tool>,<Entity>,<Currencyname|vault|default|other something other>
	 * tt_raw_reward_player_money_mat,<Event>,<Tool>,<Material>,<Currencyname|vault|default|other something other>
	 * tt_raw_reward_player_money_ent,<Event>,<Tool>,<Entity>,<Currencyname|vault|default|other something other>
	 * tt_raw_reward_techtotal_money_mat,<PAT>,<Tech>,<Event>,<Tool>,<Material>,<Currencyname|vault|default|other something other>
	 * tt_raw_reward_techtotal_money_ent,<PAT>,<Tech>,<Event>,<Tool>,<Entity>,<Currencyname|vault|default|other something other>
	 * 
	 * Return the raw number in percent how much dropchance
	 * tt_reward_tech_dropchance_mat,<PAT>,<Tech>,<Lvl>,<Event>,<Tool>,<Material>,<DropMaterial>
	 * tt_reward_tech_dropchance_ent,<PAT>,<Tech>,<Lvl>,<Event>,<Tool>,<Material>,<DropMaterial>
	 * tt_reward_techtotal_dropchance_mat,<PAT>,<Tech>,<Event>,<Tool>,<Material>,<DropMaterial>
	 * tt_reward_techtotal_dropchance_ent,<PAT>,<Tech>,<Event>,<Tool>,<Material>,<DropMaterial>
	 * tt_reward_player_dropchance_mat,<Event>,<Tool>,<Material>,<DropMaterial>
	 * tt_reward_player_dropchance_ent,<Event>,<Tool>,<Material>,<DropMaterial>
	 * 
	 * tt_reward_tech_silktouchdropchance_mat,<PAT>,<Tech>,<Lvl>,<Event>,<Tool>,<Material>,<DropMaterial>
	 * tt_reward_tech_silktouchdropchance_ent,<PAT>,<Tech>,<Lvl>,<Event>,<Tool>,<Material>,<DropMaterial>
	 * tt_reward_techtotal_silktouchdropchance_mat,<PAT>,<Tech>,<Event>,<Tool>,<Material>,<DropMaterial>
	 * tt_reward_techtotal_silktouchdropchance_ent,<PAT>,<Tech>,<Event>,<Tool>,<Material>,<DropMaterial>
	 * tt_reward_player_silktouchdropchance_mat,<Event>,<Tool>,<Material>,<DropMaterial>
	 * tt_reward_player_silktouchdropchance_ent,<Event>,<Tool>,<Material>,<DropMaterial>
	 */
	
	@Override
	public String onPlaceholderRequest(Player player, String idf)
	{
		if(player == null)
		{
			return "";
		}
		final UUID uuid = player.getUniqueId();
		PlayerData pd = null;
		switch(idf) //Für simple Placeholders
		{
		default:
			break;
		case "player_ttexp_free":
			pd = PlayerHandler.getPlayer(uuid);
			return String.valueOf(pd.getActualTTExp());
		case "player_ttexp_total":
			pd = PlayerHandler.getPlayer(uuid);
			return String.valueOf(pd.getTotalReceivedTTExp());
		case "player_vaexp_stilltobeobtained":
			pd = PlayerHandler.getPlayer(uuid);
			return String.valueOf(pd.getVanillaExpStillToBeObtained());
		}
		if(idf.startsWith("hastech") && idf.split(",").length == 2)
		{
			String[] sp = idf.split(",");
			if(sp.length != 3)
			{
				return "Not correct Placeholder Input";
			}
			PlayerAssociatedType pat = PlayerAssociatedType.valueOf(sp[1]);
			String tech = sp[2];
			switch(pat)
			{
			case GLOBAL:
				return String.valueOf(plugin.getMysqlHandler().exist(MysqlHandler.Type.GLOBAL_ENTRYQUERYSTATUS,
						"`entry_query_type` = ? AND `status_type` = ? AND `intern_name` = ?",
						EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString(), tech));
			case GROUP:
			case SOLO: 
				return String.valueOf(plugin.getMysqlHandler().exist(MysqlHandler.Type.SOLO_ENTRYQUERYSTATUS,
					"`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ? AND `intern_name` = ?",
					player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString(), tech));
			}
		} else if(idf.startsWith("raw_reward_tech_ttexp_mat") || idf.startsWith("raw_reward_tech_ttexp_ent"))
		{
			return reward_tech_ttexp(idf);
		} else if(idf.startsWith("raw_reward_techtotal_ttexp_mat") || idf.startsWith("raw_reward_techtotal_ttexp_ent"))
		{
			return reward_techtotal_ttexp(idf);
		} else if(idf.startsWith("raw_reward_player_ttexp_mat") || idf.startsWith("raw_reward_player_ttexp_ent"))
		{
			return reward_player_ttexp(uuid, idf);
		} else if(idf.startsWith("raw_reward_tech_vexp_mat") || idf.startsWith("raw_reward_tech_vexp_ent"))
		{
			return reward_tech_vexp(idf);
		} else if(idf.startsWith("raw_reward_techtotal_vexp_mat") || idf.startsWith("raw_reward_techtotal_vexp_ent"))
		{
			return reward_techtotal_vexp(idf);
		} else if(idf.startsWith("raw_reward_player_vexp_mat") || idf.startsWith("raw_reward_player_vexp_ent"))
		{
			return reward_player_vexp(uuid, idf);
		} else if(idf.startsWith("raw_reward_tech_money_mat") || idf.startsWith("raw_reward_tech_money_ent"))
		{
			return reward_tech_money(idf);
		} else if(idf.startsWith("raw_reward_techtotal_money_mat") || idf.startsWith("raw_reward_techtotal_money_ent"))
		{
			return reward_techtotal_money(idf);
		} else if(idf.startsWith("raw_reward_player_money_mat") || idf.startsWith("raw_reward_player_money_ent"))
		{
			return reward_player_money(uuid, idf);
		} else if(idf.startsWith("reward_tech_dropchance_mat") || idf.startsWith("reward_tech_dropchance_ent"))
		{
			return reward_tech_dropchance(idf);
		} else if(idf.startsWith("reward_techtotal_dropchance_mat") || idf.startsWith("reward_techtotal_dropchance_ent"))
		{
			return reward_techtotal_dropchance(idf);
		} else if(idf.startsWith("reward_player_dropchance_mat") || idf.startsWith("reward_player_dropchance_ent"))
		{
			return reward_player_dropchance(uuid, idf);
		} else if(idf.startsWith("reward_tech_silktouchdropchance_mat") || idf.startsWith("reward_tech_silktouchdropchance_ent"))
		{
			return reward_tech_silktouchdropchance(idf);
		} else if(idf.startsWith("reward_techtotal_silktouchdropchance_mat") || idf.startsWith("reward_techtotal_silktouchdropchance_ent"))
		{
			return reward_techtotal_silktouchdropchance(idf);
		} else if(idf.startsWith("reward_player_silktouchdropchance_mat") || idf.startsWith("reward_player_silktouchdropchance_ent"))
		{
			return reward_player_silktouchdropchance(uuid, idf);
		}
		return null;
	}
	
	private String reward_tech_ttexp(String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 7)
		{
			return "!Error split.length isnt 7!";
		}
		PlayerAssociatedType pat = null;
		try
		{
			pat = PlayerAssociatedType.valueOf(sp[1]);
		} catch(IllegalArgumentException e)
		{
			return "!PlayerAssociatedType can only be GLOBAL, GROUP or SOLO!";
		}
		Technology t = null;
		switch(pat)
		{
		case SOLO: t = CatTechHandler.technologyMapSolo.get(sp[2]); break;
		case GROUP: t = CatTechHandler.technologyMapGroup.get(sp[2]); break;
		case GLOBAL: t = CatTechHandler.technologyMapGlobal.get(sp[2]); break;
		}
		if(t == null)
		{
			return "!Technology dont exist!";
		}
		int lvl = 0;
		EventType evt = null;
		ToolType tool = null;
		try
		{
			lvl = Integer.parseInt(sp[3]);
			evt = EventType.valueOf(sp[4]);
			tool = ToolType.valueOf(sp[5]);
		} catch(NumberFormatException e)
		{
			return "!Level is not are number!";
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		if(!t.getRewardUnlockableInteractions().containsKey(lvl))
		{
			return "Error Tech dont contains in that Lvl TTExp Reward for that input!";
		}
		return idf.startsWith("raw_reward_tech_ttexp_mat")
				? reward_tech_ttexp_mat(sp, pat, t, lvl, evt, tool)
				: reward_tech_ttexp_ent(sp, pat, t, lvl, evt, tool);
	}
	
	private String reward_tech_ttexp_mat(String[] sp,
			PlayerAssociatedType pat, Technology t, int lvl, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[6]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		for(UnlockableInteraction ui : t.getRewardUnlockableInteractions().get(lvl))
		{
			if(evt == ui.getEventType() && tool == ui.getToolType()
					&& ui.getEventMaterial() != null && mat == ui.getEventMaterial())
			{
				return String.valueOf(ui.getTechnologyExperience());
			}
		}
		return "/";
	}
	
	private String reward_tech_ttexp_ent(String[] sp,
			PlayerAssociatedType pat, Technology t, int lvl, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[6]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		for(UnlockableInteraction ui : t.getRewardUnlockableInteractions().get(lvl))
		{
			if(evt == ui.getEventType() && tool == ui.getToolType()
					&& ui.getEventEntityType() != null && ent == ui.getEventEntityType())
			{
				return String.valueOf(ui.getTechnologyExperience());
			}
		}
		return "/";
	}
	
	private String reward_techtotal_ttexp(String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 6)
		{
			return "Error split.length isnt 6!";
		}
		PlayerAssociatedType pat = null;
		try
		{
			pat = PlayerAssociatedType.valueOf(sp[1]);
		} catch(IllegalArgumentException e)
		{
			return "!PlayerAssociatedType can only be GLOBAL, GROUP or SOLO!";
		}
		Technology t = null;
		switch(pat)
		{
		case SOLO: t = CatTechHandler.technologyMapSolo.get(sp[2]); break;
		case GROUP: t = CatTechHandler.technologyMapGroup.get(sp[2]); break;
		case GLOBAL: t = CatTechHandler.technologyMapGlobal.get(sp[2]); break;
		}
		if(t == null)
		{
			return "!Technology dont exist!";
		}
		EventType evt = null;
		ToolType tool = null;
		try
		{
			evt = EventType.valueOf(sp[3]);
			tool = ToolType.valueOf(sp[4]);
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		return idf.startsWith("raw_reward_techtotal_ttexp_mat")
				? reward_techtotal_ttexp_mat(sp, pat, t, evt, tool)
				: reward_techtotal_ttexp_ent(sp, pat, t, evt, tool);
	}
	
	private String reward_techtotal_ttexp_mat(String[] sp,
			PlayerAssociatedType pat, Technology t, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[5]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		double d = 0.0;
		for(Entry<Integer, ArrayList<UnlockableInteraction>> e : t.getRewardUnlockableInteractions().entrySet())
		{
			for(UnlockableInteraction ui : e.getValue())
			{
				if(evt == ui.getEventType() && tool == ui.getToolType()
						&& ui.getEventMaterial() != null && mat == ui.getEventMaterial())
				{
					d += ui.getTechnologyExperience();
				}
			}
		}
		return String.valueOf(d);
	}
	
	private String reward_techtotal_ttexp_ent(String[] sp,
			PlayerAssociatedType pat, Technology t, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[5]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		double d = 0.0;
		for(Entry<Integer, ArrayList<UnlockableInteraction>> e : t.getRewardUnlockableInteractions().entrySet())
		{
			for(UnlockableInteraction ui : e.getValue())
			{
				if(evt == ui.getEventType() && tool == ui.getToolType()
						&& ui.getEventEntityType() != null && ent == ui.getEventEntityType())
				{
					d += ui.getTechnologyExperience();
				}
			}
		}
		return String.valueOf(d);
	}
	
	private String reward_player_ttexp(UUID uuid, String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 4)
		{
			return "Error split.length isnt 4!";
		}
		EventType evt = null;
		ToolType tool = null;
		try
		{
			evt = EventType.valueOf(sp[1]);
			tool = ToolType.valueOf(sp[2]);
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		return idf.startsWith("raw_reward_player_ttexp_mat")
				? reward_player_ttexp_mat(uuid, sp, evt, tool)
				: reward_player_ttexp_ent(uuid, sp, evt, tool);
	}
	
	private String reward_player_ttexp_mat(UUID uuid, String[] sp, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[3]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		if(PlayerHandler.materialInteractionMap.containsKey(uuid)
				&& PlayerHandler.materialInteractionMap.get(uuid).containsKey(tool)
				&& PlayerHandler.materialInteractionMap.get(uuid).get(tool).containsKey(mat)
				&& PlayerHandler.materialInteractionMap.get(uuid).get(tool).get(mat).containsKey(evt))
		{
			return String.valueOf(PlayerHandler.materialInteractionMap.get(uuid).get(tool).get(mat).get(evt).getTechnologyExperience());
		}
		return "/";
	}
	
	private String reward_player_ttexp_ent(UUID uuid, String[] sp, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[3]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		if(PlayerHandler.entityTypeInteractionMap.containsKey(uuid)
				&& PlayerHandler.entityTypeInteractionMap.get(uuid).containsKey(tool)
				&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).containsKey(ent)
				&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).get(ent).containsKey(evt))
		{
			return String.valueOf(PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).get(ent).get(evt).getTechnologyExperience());
		}
		return "/";
	}
	
	private String reward_tech_vexp(String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 7)
		{
			return "Error split.length isnt 7!";
		}
		PlayerAssociatedType pat = null;
		try
		{
			pat = PlayerAssociatedType.valueOf(sp[1]);
		} catch(IllegalArgumentException e)
		{
			return "!PlayerAssociatedType can only be GLOBAL, GROUP or SOLO!";
		}
		Technology t = null;
		switch(pat)
		{
		case SOLO: t = CatTechHandler.technologyMapSolo.get(sp[2]); break;
		case GROUP: t = CatTechHandler.technologyMapGroup.get(sp[2]); break;
		case GLOBAL: t = CatTechHandler.technologyMapGlobal.get(sp[2]); break;
		}
		if(t == null)
		{
			return "!Technology dont exist!";
		}
		int lvl = 0;
		EventType evt = null;
		ToolType tool = null;
		try
		{
			lvl = Integer.parseInt(sp[3]);
			evt = EventType.valueOf(sp[4]);
			tool = ToolType.valueOf(sp[5]);
		} catch(NumberFormatException e)
		{
			return "!Level is not are number!";
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		if(!t.getRewardUnlockableInteractions().containsKey(lvl))
		{
			return "Error Tech dont contains in that Lvl TTExp Reward for that input!";
		}
		return idf.startsWith("raw_reward_tech_vexp_mat")
				? reward_tech_vexp_mat(sp, pat, t, lvl, evt, tool)
				: reward_tech_vexp_ent(sp, pat, t, lvl, evt, tool);
	}
	
	private String reward_tech_vexp_mat(String[] sp,
			PlayerAssociatedType pat, Technology t, int lvl, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[6]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		for(UnlockableInteraction ui : t.getRewardUnlockableInteractions().get(lvl))
		{
			if(evt == ui.getEventType() && tool == ui.getToolType()
					&& ui.getEventMaterial() != null && mat == ui.getEventMaterial())
			{
				return String.valueOf(ui.getVanillaExperience());
			}
		}
		return "/";
	}
	
	private String reward_tech_vexp_ent(String[] sp,
			PlayerAssociatedType pat, Technology t, int lvl, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[6]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		for(UnlockableInteraction ui : t.getRewardUnlockableInteractions().get(lvl))
		{
			if(evt == ui.getEventType() && tool == ui.getToolType()
					&& ui.getEventEntityType() != null && ent == ui.getEventEntityType())
			{
				return String.valueOf(ui.getVanillaExperience());
			}
		}
		return "/";
	}
	
	private String reward_techtotal_vexp(String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 6)
		{
			return "Error split.length isnt 6!";
		}
		PlayerAssociatedType pat = null;
		try
		{
			pat = PlayerAssociatedType.valueOf(sp[1]);
		} catch(IllegalArgumentException e)
		{
			return "!PlayerAssociatedType can only be GLOBAL, GROUP or SOLO!";
		}
		Technology t = null;
		switch(pat)
		{
		case SOLO: t = CatTechHandler.technologyMapSolo.get(sp[2]); break;
		case GROUP: t = CatTechHandler.technologyMapGroup.get(sp[2]); break;
		case GLOBAL: t = CatTechHandler.technologyMapGlobal.get(sp[2]); break;
		}
		if(t == null)
		{
			return "!Technology dont exist!";
		}
		EventType evt = null;
		ToolType tool = null;
		try
		{
			evt = EventType.valueOf(sp[3]);
			tool = ToolType.valueOf(sp[4]);
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		return idf.startsWith("raw_reward_techtotal_vexp_mat")
				? reward_techtotal_vexp_mat(sp, pat, t, evt, tool)
				: reward_techtotal_vexp_ent(sp, pat, t, evt, tool);
	}
	
	private String reward_techtotal_vexp_mat(String[] sp,
			PlayerAssociatedType pat, Technology t, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[5]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		double d = 0.0;
		for(Entry<Integer, ArrayList<UnlockableInteraction>> e : t.getRewardUnlockableInteractions().entrySet())
		{
			for(UnlockableInteraction ui : e.getValue())
			{
				if(evt == ui.getEventType() && tool == ui.getToolType()
						& ui.getEventMaterial() != null && mat == ui.getEventMaterial())
				{
					d += ui.getTechnologyExperience();
				}
			}
		}
		return String.valueOf(d);
	}
	
	private String reward_techtotal_vexp_ent(String[] sp,
			PlayerAssociatedType pat, Technology t, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[5]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		double d = 0.0;
		for(Entry<Integer, ArrayList<UnlockableInteraction>> e : t.getRewardUnlockableInteractions().entrySet())
		{
			for(UnlockableInteraction ui : e.getValue())
			{
				if(evt == ui.getEventType() && tool == ui.getToolType()
						&& ui.getEventEntityType() != null && ent == ui.getEventEntityType())
				{
					d += ui.getTechnologyExperience();
				}
			}
		}
		return String.valueOf(d);
	}
	
	private String reward_player_vexp(UUID uuid, String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 4)
		{
			return "Error split.length isnt 4!";
		}
		EventType evt = null;
		ToolType tool = null;
		try
		{
			evt = EventType.valueOf(sp[1]);
			tool = ToolType.valueOf(sp[2]);
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		return idf.startsWith("raw_reward_player_vexp_mat")
				? reward_player_vexp_mat(uuid, sp, evt, tool)
				: reward_player_vexp_ent(uuid, sp, evt, tool);
	}
	
	private String reward_player_vexp_mat(UUID uuid, String[] sp, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[3]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		if(PlayerHandler.materialInteractionMap.containsKey(uuid)
				&& PlayerHandler.materialInteractionMap.get(uuid).containsKey(tool)
				&& PlayerHandler.materialInteractionMap.get(uuid).get(tool).containsKey(mat)
				&& PlayerHandler.materialInteractionMap.get(uuid).get(tool).get(mat).containsKey(evt))
		{
			return String.valueOf(PlayerHandler.materialInteractionMap.get(uuid).get(tool).get(mat).get(evt).getVanillaExperience());
		}
		return "/";
	}
	
	private String reward_player_vexp_ent(UUID uuid, String[] sp, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[3]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		if(PlayerHandler.entityTypeInteractionMap.containsKey(uuid)
				&& PlayerHandler.entityTypeInteractionMap.get(uuid).containsKey(tool)
				&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).containsKey(ent)
				&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).get(ent).containsKey(evt))
		{
			return String.valueOf(PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).get(ent).get(evt).getVanillaExperience());
		}
		return "/";
	}
	
	private String reward_tech_money(String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 7 && sp.length != 8)
		{
			return "Error split.length isnt 7!";
		}
		PlayerAssociatedType pat = null;
		try
		{
			pat = PlayerAssociatedType.valueOf(sp[1]);
		} catch(IllegalArgumentException e)
		{
			return "!PlayerAssociatedType can only be GLOBAL, GROUP or SOLO!";
		}
		Technology t = null;
		switch(pat)
		{
		case SOLO: t = CatTechHandler.technologyMapSolo.get(sp[2]); break;
		case GROUP: t = CatTechHandler.technologyMapGroup.get(sp[2]); break;
		case GLOBAL: t = CatTechHandler.technologyMapGlobal.get(sp[2]); break;
		}
		if(t == null)
		{
			return "!Technology dont exist!";
		}
		int lvl = 0;
		EventType evt = null;
		ToolType tool = null;
		try
		{
			lvl = Integer.parseInt(sp[3]);
			evt = EventType.valueOf(sp[4]);
			tool = ToolType.valueOf(sp[5]);
		} catch(NumberFormatException e)
		{
			return "!Level is not are number!";
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		if(!t.getRewardUnlockableInteractions().containsKey(lvl))
		{
			return "Error Tech dont contains in that Lvl TTExp Reward for that input!";
		}
		return idf.startsWith("raw_reward_tech_money_mat")
				? reward_tech_money_mat(sp, pat, t, lvl, evt, tool)
				: reward_tech_money_ent(sp, pat, t, lvl, evt, tool);
	}
	
	private String reward_tech_money_mat(String[] sp,
			PlayerAssociatedType pat, Technology t, int lvl, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[6]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		String cun = "";
		if(sp.length == 7)
		{
			if(plugin.getIFHEco() != null)
			{
				cun = "default";
			} else
			{ 
				cun = "vault";
			}
		} else
		{
			cun = sp[7];
		}
		for(UnlockableInteraction ui : t.getRewardUnlockableInteractions().get(lvl))
		{
			if(evt == ui.getEventType() && tool == ui.getToolType()
					&& ui.getEventMaterial() != null && mat == ui.getEventMaterial()
							&& ui.getMoneyMap().containsKey(cun))
			{
				return String.valueOf(ui.getMoneyMap().get(cun));
			}
		}
		return "/";
	}
	
	private String reward_tech_money_ent(String[] sp,
			PlayerAssociatedType pat, Technology t, int lvl, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[6]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		String cun = "";
		if(sp.length == 7)
		{
			if(plugin.getIFHEco() != null)
			{
				cun = "default";
			} else
			{ 
				cun = "vault";
			}
		} else
		{
			cun = sp[7];
		}
		for(UnlockableInteraction ui : t.getRewardUnlockableInteractions().get(lvl))
		{
			if(evt == ui.getEventType() && tool == ui.getToolType()
					&& ui.getEventEntityType() != null && ent == ui.getEventEntityType()
					&& ui.getMoneyMap().containsKey(cun))
			{
				return String.valueOf(ui.getMoneyMap().get(cun));
			}
		}
		return "/";
	}
	
	private String reward_techtotal_money(String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 6 && sp.length != 7)
		{
			return "Error split.length isnt 6!";
		}
		PlayerAssociatedType pat = null;
		try
		{
			pat = PlayerAssociatedType.valueOf(sp[1]);
		} catch(IllegalArgumentException e)
		{
			return "!PlayerAssociatedType can only be GLOBAL, GROUP or SOLO!";
		}
		Technology t = null;
		switch(pat)
		{
		case SOLO: t = CatTechHandler.technologyMapSolo.get(sp[2]); break;
		case GROUP: t = CatTechHandler.technologyMapGroup.get(sp[2]); break;
		case GLOBAL: t = CatTechHandler.technologyMapGlobal.get(sp[2]); break;
		}
		if(t == null)
		{
			return "!Technology dont exist!";
		}
		EventType evt = null;
		ToolType tool = null;
		try
		{
			evt = EventType.valueOf(sp[3]);
			tool = ToolType.valueOf(sp[4]);
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		return idf.startsWith("raw_reward_techtotal_money_mat")
				? reward_techtotal_money_mat(sp, pat, t, evt, tool)
				: reward_techtotal_money_ent(sp, pat, t, evt, tool);
	}
	
	private String reward_techtotal_money_mat(String[] sp,
			PlayerAssociatedType pat, Technology t, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[5]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		String cun = "";
		if(sp.length == 6)
		{
			if(plugin.getIFHEco() != null)
			{
				cun = "default";
			} else
			{ 
				cun = "vault";
			}
		} else
		{
			cun = sp[6];
		}
		double d = 0.0;
		for(Entry<Integer, ArrayList<UnlockableInteraction>> e : t.getRewardUnlockableInteractions().entrySet())
		{
			for(UnlockableInteraction ui : e.getValue())
			{
				if(evt == ui.getEventType() && tool == ui.getToolType()
						&& ui.getEventMaterial() != null && mat == ui.getEventMaterial()
						&& ui.getMoneyMap().containsKey(cun))
				{
					d += ui.getMoneyMap().get(cun);
				}
			}
		}
		return String.valueOf(d);
	}
	
	private String reward_techtotal_money_ent(String[] sp,
			PlayerAssociatedType pat, Technology t, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[5]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		String cun = "";
		if(sp.length == 6)
		{
			if(plugin.getIFHEco() != null)
			{
				cun = "default";
			} else
			{ 
				cun = "vault";
			}
		} else
		{
			cun = sp[6];
		}
		double d = 0.0;
		for(Entry<Integer, ArrayList<UnlockableInteraction>> e : t.getRewardUnlockableInteractions().entrySet())
		{
			for(UnlockableInteraction ui : e.getValue())
			{
				if(evt == ui.getEventType() && tool == ui.getToolType()
						&& ui.getEventEntityType() != null && ent == ui.getEventEntityType()
						&& ui.getMoneyMap().containsKey(cun))
				{
					d += ui.getMoneyMap().get(cun);
				}
			}
		}
		return String.valueOf(d);
	}
	
	private String reward_player_money(UUID uuid, String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 4 && sp.length != 5)
		{
			return "Error split.length isnt 4!";
		}
		EventType evt = null;
		ToolType tool = null;
		try
		{
			evt = EventType.valueOf(sp[1]);
			tool = ToolType.valueOf(sp[2]);
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		return idf.startsWith("raw_reward_player_money_mat")
				? reward_player_money_mat(uuid, sp, evt, tool)
				: reward_player_money_ent(uuid, sp, evt, tool);
	}
	
	private String reward_player_money_mat(UUID uuid, String[] sp, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[3]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		String cun = "";
		if(sp.length == 4)
		{
			if(plugin.getIFHEco() != null)
			{
				cun = "default";
			} else
			{ 
				cun = "vault";
			}
		} else
		{
			cun = sp[4];
		}
		if(PlayerHandler.materialInteractionMap.containsKey(uuid)
				&& PlayerHandler.materialInteractionMap.get(uuid).containsKey(tool)
				&& PlayerHandler.materialInteractionMap.get(uuid).get(tool).containsKey(mat)
				&& PlayerHandler.materialInteractionMap.get(uuid).get(tool).get(mat).containsKey(evt)
				&& PlayerHandler.materialInteractionMap.get(uuid).get(tool).get(mat).get(evt).getMoneyMap().containsKey(cun))
		{
			return String.valueOf(PlayerHandler.materialInteractionMap.get(uuid).get(tool).get(mat).get(evt).getMoneyMap().get(cun));
		}
		return "/";
	}
	
	private String reward_player_money_ent(UUID uuid, String[] sp, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[3]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		String cun = "";
		if(sp.length == 4)
		{
			if(plugin.getIFHEco() != null)
			{
				cun = "default";
			} else
			{ 
				cun = "vault";
			}
		} else
		{
			cun = sp[4];
		}
		if(PlayerHandler.entityTypeInteractionMap.containsKey(uuid)
				&& PlayerHandler.entityTypeInteractionMap.get(uuid).containsKey(tool)
				&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).containsKey(ent)
				&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).get(ent).containsKey(evt)
				&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).get(ent).get(evt).getMoneyMap().containsKey(cun))
		{
			return String.valueOf(PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).get(ent).get(evt).getMoneyMap().get(cun));
		}
		return "/";
	}
	
	private String reward_tech_dropchance(String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 8)
		{
			return "Error split.length isnt 8!";
		}
		PlayerAssociatedType pat = null;
		try
		{
			pat = PlayerAssociatedType.valueOf(sp[1]);
		} catch(IllegalArgumentException e)
		{
			return "!PlayerAssociatedType can only be GLOBAL, GROUP or SOLO!";
		}
		Technology t = null;
		switch(pat)
		{
		case SOLO: t = CatTechHandler.technologyMapSolo.get(sp[2]); break;
		case GROUP: t = CatTechHandler.technologyMapGroup.get(sp[2]); break;
		case GLOBAL: t = CatTechHandler.technologyMapGlobal.get(sp[2]); break;
		}
		if(t == null)
		{
			return "!Technology dont exist!";
		}
		int lvl = 0;
		EventType evt = null;
		ToolType tool = null;
		try
		{
			lvl = Integer.parseInt(sp[3]);
			evt = EventType.valueOf(sp[4]);
			tool = ToolType.valueOf(sp[5]);
		} catch(NumberFormatException e)
		{
			return "!Level is not are number!";
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		return idf.startsWith("reward_tech_dropchance_mat")
				? reward_tech_dropchance_mat(sp, pat, t, lvl, evt, tool)
				: reward_tech_dropchance_ent(sp, pat, t, lvl, evt, tool);
	}
	
	private String reward_tech_dropchance_mat(String[] sp,
			PlayerAssociatedType pat, Technology t, int lvl, EventType evt, ToolType tool)
	{
		
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[6]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		String dropmat = sp[7];
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(); //Amount to drop, Dropchance
		for(DropChance dc : t.getRewardDropChances().get(lvl))
		{
			if(evt == dc.getEventType() && tool == dc.getToolType()
					&& dc.getEventMaterial() != null && mat == dc.getEventMaterial()
					&& dropmat.equals(dc.getToDropItem()))
			{
				double d = 0.0;
				if(map.containsKey(dc.getToDropItemAmount()))
				{
					d = map.get(dc.getToDropItemAmount());
				}
				d += dc.getDropChance();
				map.put(dc.getToDropItemAmount(), d);
			}
		}
		map = map.entrySet().stream()
			    .sorted(Entry.comparingByKey())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		
		for(Entry<Integer, Double> e : map.entrySet())
		{
			if(sb.length() > 0)
			{
				sb.append(", ");
			}
			sb.append(e.getKey()+":"+(e.getValue() >= 100 ? 100 : e.getValue()*100)+" %");
		}
		return sb.toString();
	}
	
	private String reward_tech_dropchance_ent(String[] sp,
			PlayerAssociatedType pat, Technology t, int lvl, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[6]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		String dropmat = sp[7];
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(); //Amount to drop, Dropchance
		for(DropChance dc : t.getRewardDropChances().get(lvl))
		{
			if(evt == dc.getEventType() && tool == dc.getToolType()
					&& dc.getEventEntityType() != null && ent == dc.getEventEntityType()
					&& dropmat.equals(dc.getToDropItem()))
			{
				double d = 0.0;
				if(map.containsKey(dc.getToDropItemAmount()))
				{
					d = map.get(dc.getToDropItemAmount());
				}
				d += dc.getDropChance();
				map.put(dc.getToDropItemAmount(), d);
			}
		}
		map = map.entrySet().stream()
			    .sorted(Entry.comparingByKey())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		for(Entry<Integer, Double> e : map.entrySet())
		{
			if(sb.length() > 0)
			{
				sb.append(", ");
			}
			sb.append(e.getKey()+":"+(e.getValue() >= 100 ? 100 : e.getValue()*100)+" %");
		}
		return sb.toString();
	}
	
	private String reward_techtotal_dropchance(String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 7)
		{
			return "Error split.length isnt 7!";
		}
		PlayerAssociatedType pat = null;
		try
		{
			pat = PlayerAssociatedType.valueOf(sp[1]);
		} catch(IllegalArgumentException e)
		{
			return "!PlayerAssociatedType can only be GLOBAL, GROUP or SOLO!";
		}
		Technology t = null;
		switch(pat)
		{
		case SOLO: t = CatTechHandler.technologyMapSolo.get(sp[2]); break;
		case GROUP: t = CatTechHandler.technologyMapGroup.get(sp[2]); break;
		case GLOBAL: t = CatTechHandler.technologyMapGlobal.get(sp[2]); break;
		}
		if(t == null)
		{
			return "!Technology dont exist!";
		}
		EventType evt = null;
		ToolType tool = null;
		try
		{
			evt = EventType.valueOf(sp[3]);
			tool = ToolType.valueOf(sp[4]);
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		return idf.startsWith("reward_techtotal_dropchance_mat")
				? reward_techtotal_dropchance_mat(sp, pat, t, evt, tool)
				: reward_techtotal_dropchance_ent(sp, pat, t, evt, tool);
	}
	
	private String reward_techtotal_dropchance_mat(String[] sp,
			PlayerAssociatedType pat, Technology t, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[5]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		String dropmat = sp[6];
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(); //Amount to drop, Dropchance
		for(Entry<Integer, ArrayList<DropChance>> e : t.getRewardDropChances().entrySet())
		{
			for(DropChance dc : e.getValue())
			{
				if(evt == dc.getEventType() && tool == dc.getToolType()
						&& dc.getEventMaterial() != null && mat == dc.getEventMaterial()
						&& dropmat.equals(dc.getToDropItem()))
				{
					double d = 0.0;
					if(map.containsKey(dc.getToDropItemAmount()))
					{
						d = map.get(dc.getToDropItemAmount());
					}
					d += dc.getDropChance();
					map.put(dc.getToDropItemAmount(), d);
				}
			}
		}
		map = map.entrySet().stream()
			    .sorted(Entry.comparingByKey())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		for(Entry<Integer, Double> e : map.entrySet())
		{
			if(sb.length() > 0)
			{
				sb.append(", ");
			}
			sb.append(e.getKey()+":"+(e.getValue() >= 100 ? 100 : e.getValue()*100)+" %");
		}
		return sb.toString();
	}
	
	private String reward_techtotal_dropchance_ent(String[] sp,
			PlayerAssociatedType pat, Technology t, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[5]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		String dropmat = sp[6];
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(); //Amount to drop, Dropchance
		for(Entry<Integer, ArrayList<DropChance>> e : t.getRewardDropChances().entrySet())
		{
			for(DropChance dc : e.getValue())
			{
				if(evt == dc.getEventType() && tool == dc.getToolType()
						&& dc.getEventEntityType() != null && ent == dc.getEventEntityType()
						&& dropmat.equals(dc.getToDropItem()))
				{
					double d = 0.0;
					if(map.containsKey(dc.getToDropItemAmount()))
					{
						d = map.get(dc.getToDropItemAmount());
					}
					d += dc.getDropChance();
					map.put(dc.getToDropItemAmount(), d);
				}
			}
		}
		map = map.entrySet().stream()
			    .sorted(Entry.comparingByKey())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		for(Entry<Integer, Double> e : map.entrySet())
		{
			if(sb.length() > 0)
			{
				sb.append(", ");
			}
			sb.append(e.getKey()+":"+(e.getValue() >= 100 ? 100 : e.getValue()*100)+" %");
		}
		return sb.toString();
	}
	
	private String reward_player_dropchance(UUID uuid, String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 5)
		{
			return "Error split.length isnt 5!";
		}
		EventType evt = null;
		ToolType tool = null;
		try
		{
			evt = EventType.valueOf(sp[1]);
			tool = ToolType.valueOf(sp[2]);
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		return idf.startsWith("reward_player_dropchance_mat")
				? reward_player_dropchance_mat(uuid, sp, evt, tool)
				: reward_player_dropchance_ent(uuid, sp, evt, tool);
	}
	
	private String reward_player_dropchance_mat(UUID uuid, String[] sp, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[3]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		String dropmat = sp[4];
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(); //Amount to drop, Dropchance
		if(PlayerHandler.materialDropMap.containsKey(uuid)
				&& PlayerHandler.materialDropMap.get(uuid).containsKey(tool)
				&& PlayerHandler.materialDropMap.get(uuid).get(tool).containsKey(mat)
				&& PlayerHandler.materialDropMap.get(uuid).get(tool).get(mat).containsKey(evt))
		{
			for(Entry<String, SimpleDropChance> e : PlayerHandler.materialDropMap.get(uuid).get(tool).get(mat).get(evt).entrySet())
			{
				if(!dropmat.equals(e.getKey()))
				{
					continue;
				}
				for(Entry<Integer, Double> ee : e.getValue().getAmountToDropChance().entrySet())
				{
					double d = 0.0;
					if(map.containsKey(ee.getKey()))
					{
						d = map.get(ee.getKey());
					}
					d += ee.getValue();
					map.put(ee.getKey(), d);
				}
			}
		}
		map = map.entrySet().stream()
			    .sorted(Entry.comparingByKey())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		for(Entry<Integer, Double> e : map.entrySet())
		{
			if(sb.length() > 0)
			{
				sb.append(", ");
			}
			sb.append(e.getKey()+":"+(e.getValue() >= 100 ? 100 : e.getValue()*100)+" %");
		}
		return "/";
	}
	
	private String reward_player_dropchance_ent(UUID uuid, String[] sp, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[3]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		String dropmat = sp[4];
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(); //Amount to drop, Dropchance
		if(PlayerHandler.entityTypeDropMap.containsKey(uuid)
				&& PlayerHandler.entityTypeDropMap.get(uuid).containsKey(tool)
				&& PlayerHandler.entityTypeDropMap.get(uuid).get(tool).containsKey(ent)
				&& PlayerHandler.entityTypeDropMap.get(uuid).get(tool).get(ent).containsKey(evt))
		{
			for(Entry<String, SimpleDropChance> e : PlayerHandler.entityTypeDropMap.get(uuid).get(tool).get(ent).get(evt).entrySet())
			{
				if(!dropmat.equals(e.getKey()))
				{
					continue;
				}
				for(Entry<Integer, Double> ee : e.getValue().getAmountToDropChance().entrySet())
				{
					double d = 0.0;
					if(map.containsKey(ee.getKey()))
					{
						d = map.get(ee.getKey());
					}
					d += ee.getValue();
					map.put(ee.getKey(), d);
				}
			}
		}
		map = map.entrySet().stream()
			    .sorted(Entry.comparingByKey())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		for(Entry<Integer, Double> e : map.entrySet())
		{
			if(sb.length() > 0)
			{
				sb.append(", ");
			}
			sb.append(e.getKey()+":"+(e.getValue() >= 100 ? 100 : e.getValue()*100)+" %");
		}
		return "/";
	}
	
	private String reward_tech_silktouchdropchance(String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 8)
		{
			return "Error split.length isnt 8!";
		}
		PlayerAssociatedType pat = null;
		try
		{
			pat = PlayerAssociatedType.valueOf(sp[1]);
		} catch(IllegalArgumentException e)
		{
			return "!PlayerAssociatedType can only be GLOBAL, GROUP or SOLO!";
		}
		Technology t = null;
		switch(pat)
		{
		case SOLO: t = CatTechHandler.technologyMapSolo.get(sp[2]); break;
		case GROUP: t = CatTechHandler.technologyMapGroup.get(sp[2]); break;
		case GLOBAL: t = CatTechHandler.technologyMapGlobal.get(sp[2]); break;
		}
		if(t == null)
		{
			return "!Technology dont exist!";
		}
		int lvl = 0;
		EventType evt = null;
		ToolType tool = null;
		try
		{
			lvl = Integer.parseInt(sp[3]);
			evt = EventType.valueOf(sp[4]);
			tool = ToolType.valueOf(sp[5]);
		} catch(NumberFormatException e)
		{
			return "!Level is not are number!";
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		return idf.startsWith("reward_tech_silktouchdropchance_mat")
				? reward_tech_silktouchdropchance_mat(sp, pat, t, lvl, evt, tool)
				: reward_tech_silktouchdropchance_ent(sp, pat, t, lvl, evt, tool);
	}
	
	private String reward_tech_silktouchdropchance_mat(String[] sp,
			PlayerAssociatedType pat, Technology t, int lvl, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[6]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		String dropmat = sp[7];
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(); //Amount to drop, Dropchance
		if(t.getRewardSilkTouchDropChances().get(lvl) == null)
		{
			return "";
		}
		for(DropChance dc : t.getRewardSilkTouchDropChances().get(lvl))
		{
			if(evt == dc.getEventType() && tool == dc.getToolType()
					&& dc.getEventMaterial() != null && mat == dc.getEventMaterial()
					&& dropmat.equals(dc.getToDropItem()))
			{
				double d = 0.0;
				if(map.containsKey(dc.getToDropItemAmount()))
				{
					d = map.get(dc.getToDropItemAmount());
				}
				d += dc.getDropChance();
				map.put(dc.getToDropItemAmount(), d);
			}
		}
		map = map.entrySet().stream()
			    .sorted(Entry.comparingByKey())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		for(Entry<Integer, Double> e : map.entrySet())
		{
			if(sb.length() > 0)
			{
				sb.append(", ");
			}
			sb.append(e.getKey()+":"+(e.getValue() >= 100 ? 100 : e.getValue()*100)+" %");
		}
		return sb.toString();
	}
	
	private String reward_tech_silktouchdropchance_ent(String[] sp,
			PlayerAssociatedType pat, Technology t, int lvl, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[6]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		String dropmat = sp[7];
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(); //Amount to drop, Dropchance
		if(t.getRewardSilkTouchDropChances().get(lvl) == null)
		{
			return "";
		}
		for(DropChance dc : t.getRewardSilkTouchDropChances().get(lvl))
		{
			if(evt == dc.getEventType() && tool == dc.getToolType()
					&& dc.getEventEntityType() != null && ent == dc.getEventEntityType()
					&& dropmat.equals(dc.getToDropItem()))
			{
				double d = 0.0;
				if(map.containsKey(dc.getToDropItemAmount()))
				{
					d = map.get(dc.getToDropItemAmount());
				}
				d += dc.getDropChance();
				map.put(dc.getToDropItemAmount(), d);
			}
		}
		map = map.entrySet().stream()
			    .sorted(Entry.comparingByKey())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		for(Entry<Integer, Double> e : map.entrySet())
		{
			if(sb.length() > 0)
			{
				sb.append(", ");
			}
			sb.append(e.getKey()+":"+(e.getValue() >= 100 ? 100 : e.getValue()*100)+" %");
		}
		return sb.toString();
	}
	
	private String reward_techtotal_silktouchdropchance(String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 7)
		{
			return "Error split.length isnt 7!";
		}
		PlayerAssociatedType pat = null;
		try
		{
			pat = PlayerAssociatedType.valueOf(sp[1]);
		} catch(IllegalArgumentException e)
		{
			return "!PlayerAssociatedType can only be GLOBAL, GROUP or SOLO!";
		}
		Technology t = null;
		switch(pat)
		{
		case SOLO: t = CatTechHandler.technologyMapSolo.get(sp[2]); break;
		case GROUP: t = CatTechHandler.technologyMapGroup.get(sp[2]); break;
		case GLOBAL: t = CatTechHandler.technologyMapGlobal.get(sp[2]); break;
		}
		if(t == null)
		{
			return "!Technology dont exist!";
		}
		EventType evt = null;
		ToolType tool = null;
		try
		{
			evt = EventType.valueOf(sp[3]);
			tool = ToolType.valueOf(sp[4]);
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		return idf.startsWith("reward_techtotal_silktouchdropchance_mat")
				? reward_techtotal_silktouchdropchance_mat(sp, pat, t, evt, tool)
				: reward_techtotal_silktouchdropchance_ent(sp, pat, t, evt, tool);
	}
	
	private String reward_techtotal_silktouchdropchance_mat(String[] sp,
			PlayerAssociatedType pat, Technology t, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[5]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		String dropmat = sp[6];
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(); //Amount to drop, Dropchance
		for(Entry<Integer, ArrayList<DropChance>> e : t.getRewardSilkTouchDropChances().entrySet())
		{
			for(DropChance dc : e.getValue())
			{
				if(evt == dc.getEventType() && tool == dc.getToolType()
						&& dc.getEventMaterial() != null && mat == dc.getEventMaterial()
						&& dropmat.equals(dc.getToDropItem()))
				{
					double d = 0.0;
					if(map.containsKey(dc.getToDropItemAmount()))
					{
						d = map.get(dc.getToDropItemAmount());
					}
					d += dc.getDropChance();
					map.put(dc.getToDropItemAmount(), d);
				}
			}
		}
		map = map.entrySet().stream()
			    .sorted(Entry.comparingByKey())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		for(Entry<Integer, Double> e : map.entrySet())
		{
			if(sb.length() > 0)
			{
				sb.append(", ");
			}
			sb.append(e.getKey()+":"+(e.getValue() >= 100 ? 100 : e.getValue()*100)+" %");
		}
		return sb.toString();
	}
	
	private String reward_techtotal_silktouchdropchance_ent(String[] sp,
			PlayerAssociatedType pat, Technology t, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[5]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		String dropmat = sp[6];
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(); //Amount to drop, Dropchance
		for(Entry<Integer, ArrayList<DropChance>> e : t.getRewardSilkTouchDropChances().entrySet())
		{
			for(DropChance dc : e.getValue())
			{
				if(evt == dc.getEventType() && tool == dc.getToolType()
						&& dc.getEventEntityType() != null && ent == dc.getEventEntityType()
						&& dropmat.equals(dc.getToDropItem()))
				{
					double d = 0.0;
					if(map.containsKey(dc.getToDropItemAmount()))
					{
						d = map.get(dc.getToDropItemAmount());
					}
					d += dc.getDropChance();
					map.put(dc.getToDropItemAmount(), d);
				}
			}
		}
		map = map.entrySet().stream()
			    .sorted(Entry.comparingByKey())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		for(Entry<Integer, Double> e : map.entrySet())
		{
			if(sb.length() > 0)
			{
				sb.append(", ");
			}
			sb.append(e.getKey()+":"+(e.getValue() >= 100 ? 100 : e.getValue()*100)+" %");
		}
		return sb.toString();
	}
	
	private String reward_player_silktouchdropchance(UUID uuid, String idf)
	{
		String[] sp = idf.split(",");
		if(sp.length != 5)
		{
			return "Error split.length isnt 5!";
		}
		EventType evt = null;
		ToolType tool = null;
		try
		{
			evt = EventType.valueOf(sp[1]);
			tool = ToolType.valueOf(sp[2]);
		} catch(IllegalArgumentException e)
		{
			if(evt == null)
			{
				return "!EventType dont exist!";
			} else
			{
				return "!ToolType dont exist!";
			}
		}
		return idf.startsWith("reward_player_silktouchdropchance_mat")
				? reward_player_silktouchdropchance_mat(uuid, sp, evt, tool)
				: reward_player_silktouchdropchance_ent(uuid, sp, evt, tool);
	}
	
	private String reward_player_silktouchdropchance_mat(UUID uuid, String[] sp, EventType evt, ToolType tool)
	{
		Material mat = null;
		try
		{
			mat = Material.valueOf(sp[3]);
		} catch(IllegalArgumentException e)
		{
			return "!Material dont exist!";
		}
		String dropmat = sp[4];
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(); //Amount to drop, Dropchance
		if(PlayerHandler.materialSilkTouchDropMap.containsKey(uuid)
				&& PlayerHandler.materialSilkTouchDropMap.get(uuid).containsKey(tool)
				&& PlayerHandler.materialSilkTouchDropMap.get(uuid).get(tool).containsKey(mat)
				&& PlayerHandler.materialSilkTouchDropMap.get(uuid).get(tool).get(mat).containsKey(evt))
		{
			for(Entry<String, SimpleDropChance> e : PlayerHandler.materialSilkTouchDropMap.get(uuid).get(tool).get(mat).get(evt).entrySet())
			{
				if(!dropmat.equals(e.getKey()))
				{
					continue;
				}
				for(Entry<Integer, Double> ee : e.getValue().getAmountToDropChance().entrySet())
				{
					double d = 0.0;
					if(map.containsKey(ee.getKey()))
					{
						d = map.get(ee.getKey());
					}
					d += ee.getValue();
					map.put(ee.getKey(), d);
				}
			}
		}
		map = map.entrySet().stream()
			    .sorted(Entry.comparingByKey())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		for(Entry<Integer, Double> e : map.entrySet())
		{
			if(sb.length() > 0)
			{
				sb.append(", ");
			}
			sb.append(e.getKey()+":"+(e.getValue() >= 100 ? 100 : e.getValue()*100)+" %");
		}
		return "/";
	}
	
	private String reward_player_silktouchdropchance_ent(UUID uuid, String[] sp, EventType evt, ToolType tool)
	{
		EntityType ent = null;
		try
		{
			ent = EntityType.valueOf(sp[3]);
		} catch(IllegalArgumentException e)
		{
			return "!EntityType dont exist!";
		}
		String dropmat = sp[4];
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<Integer, Double> map = new LinkedHashMap<>(); //Amount to drop, Dropchance
		if(PlayerHandler.entityTypeSilkTouchDropMap.containsKey(uuid)
				&& PlayerHandler.entityTypeSilkTouchDropMap.get(uuid).containsKey(tool)
				&& PlayerHandler.entityTypeSilkTouchDropMap.get(uuid).get(tool).containsKey(ent)
				&& PlayerHandler.entityTypeSilkTouchDropMap.get(uuid).get(tool).get(ent).containsKey(evt))
		{
			for(Entry<String, SimpleDropChance> e : PlayerHandler.entityTypeSilkTouchDropMap.get(uuid).get(tool).get(ent).get(evt).entrySet())
			{
				if(!dropmat.equals(e.getKey()))
				{
					continue;
				}
				for(Entry<Integer, Double> ee : e.getValue().getAmountToDropChance().entrySet())
				{
					double d = 0.0;
					if(map.containsKey(ee.getKey()))
					{
						d = map.get(ee.getKey());
					}
					d += ee.getValue();
					map.put(ee.getKey(), d);
				}
			}
		}
		map = map.entrySet().stream()
			    .sorted(Entry.comparingByKey())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		for(Entry<Integer, Double> e : map.entrySet())
		{
			if(sb.length() > 0)
			{
				sb.append(", ");
			}
			sb.append(e.getKey()+":"+(e.getValue() >= 100 ? 100 : e.getValue()*100)+" %");
		}
		return "/";
	}
}