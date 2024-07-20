package main.java.me.avankziar.tt.spigot.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;
import me.avankziar.ifh.general.math.MathFormulaParser;

public class GroupHandler
{
	private static TT plugin = BaseConstructor.getPlugin();
	
	public enum Position
	{
		MASTER(0),
		VICE(1),
		COUNCILMEMBER(2),
		MEMBER(3),
		ADEPT(4),
		INVITEE(5), //Only for Players, which was invited into the group
		APPLICANT(6) //Only for Player, which has been applied a the group
		;
		
		private int rank;
		
		Position(int rank)
		{
			this.rank = rank;
		}
		
		public int getRank()
		{
			return this.rank;
		}
		
		public static Position getPosition(int rank)
		{
			switch(rank)
			{
			case 0: return MASTER;
			case 1: return VICE;
			case 2: return COUNCILMEMBER;
			case 3: return MEMBER;
			case 4: return ADEPT;
			case 5: return INVITEE;
			case 6: return APPLICANT;
			default: return APPLICANT;
			}
		}
	}
	
	public static String getPositionLocale(Position p)
	{
		return plugin.getYamlHandler().getLang().getString("Commands.Group.Position."+p.toString());
	}
	
	public static int getMaximumAchievableLevel()
	{
		return plugin.getYamlHandler().getConfig().getInt("Group.Level.MaximumAchievableLevel");
	}
	
	public static double calculateGroupDailyUpKeep(String groupname, int grouplevel, int groupmemberamount)
	{
		//Calculate the daily
		String formula = plugin.getYamlHandler().getConfig().getString("Group.DailyUpkeep.Fromula");
		HashMap<String, Double> map = new HashMap<>();
		map.put(PlayerHandler.GROUP_LEVEL, Double.valueOf(grouplevel));
		map.put(PlayerHandler.GROUP_MEMBERAMOUNT, Double.valueOf(groupmemberamount));
		map.put(PlayerHandler.GROUP_MEMBERTOTALAMOUNT, Double.valueOf(GroupHandler.getMemberTotalAmount(groupname, grouplevel, groupmemberamount)));
		map.put(PlayerHandler.GROUP_RESEARCHED_TOTALTECH, Double.valueOf(PlayerHandler.getTotalResearchGroupTech(groupname)));
		return new MathFormulaParser().parse(formula, map);
	}
	
	public static double getDefaultUpKeep(GroupHandler.Position pos)
	{
		switch(pos)
		{
		case MASTER:
		case VICE:
		case COUNCILMEMBER:
		case MEMBER:
		case ADEPT:
			return plugin.getYamlHandler().getConfig().getDouble("Group.DailyUpkeep.Default."+pos.toString());
		default:
			return 0;
		}
	}
	
	public static double getCostsForIncreasingLevel(String groupname, int grouplevel, int groupmemberamount)
	{
		String formula = plugin.getYamlHandler().getConfig().getString("Group.Level.CostsForIncreasingLevel");
		HashMap<String, Double> map = new HashMap<>();
		map.put(PlayerHandler.GROUP_LEVEL, Double.valueOf(grouplevel));
		map.put(PlayerHandler.GROUP_MEMBERAMOUNT, Double.valueOf(groupmemberamount));
		map.put(PlayerHandler.GROUP_MEMBERTOTALAMOUNT, Double.valueOf(GroupHandler.getMemberTotalAmount(groupname, grouplevel, groupmemberamount)));
		map.put(PlayerHandler.GROUP_RESEARCHED_TOTALTECH, Double.valueOf(PlayerHandler.getTotalResearchGroupTech(groupname)));
		return new MathFormulaParser().parse(formula, map);
	}
	
	public static int getGroupMemberTotalAmount(Player player)
	{
		GroupData gd = getGroup(player);
		if(gd == null)
		{
			return 0;
		} else 
		{
			return getMemberTotalAmount(gd.getGroupName(), gd.getGroupLevel(), getGroupMemberAmount(player));
		}
	}
	
	public static int getMemberTotalAmount(String groupname, int grouplevel, int groupmemberamount)
	{
		String formula = plugin.getYamlHandler().getConfig().getString("Group.Member.TotalAmountPerLevel.Player");
		HashMap<String, Double> map = new HashMap<>();
		map.put(PlayerHandler.GROUP_LEVEL, Double.valueOf(grouplevel));
		map.put(PlayerHandler.GROUP_MEMBERAMOUNT, Double.valueOf(groupmemberamount));
		map.put(PlayerHandler.GROUP_RESEARCHED_TOTALTECH, Double.valueOf(PlayerHandler.getTotalResearchGroupTech(groupname)));
		return (int) Math.floor(new MathFormulaParser().parse(formula, map));
	}
	
	public static int getMemberTotalAmount(GroupHandler.Position pos, String groupname, int grouplevel, int groupmemberamount)
	{
		String formula = "";
		switch(pos)
		{
		case MASTER:
		case VICE:
		case COUNCILMEMBER:
		case MEMBER:
		case ADEPT:
			formula = plugin.getYamlHandler().getConfig().getString("Group.Member.TotalAmountPerLevel."+pos.toString());
			break;
		default:
			break;
		}
		if(formula.isEmpty())
		{
			return 0;
		}
		HashMap<String, Double> map = new HashMap<>();
		map.put(PlayerHandler.GROUP_LEVEL, Double.valueOf(grouplevel));
		map.put(PlayerHandler.GROUP_MEMBERAMOUNT, Double.valueOf(groupmemberamount));
		map.put(PlayerHandler.GROUP_RESEARCHED_TOTALTECH, Double.valueOf(PlayerHandler.getTotalResearchGroupTech(groupname)));
		return (int) Math.floor(new MathFormulaParser().parse(formula, map));
	}
	
	public static boolean isInGroup(UUID uuid)
	{
		return plugin.getMysqlHandler().exist(Type.GROUP_PLAYERAFFILIATION, "`player_uuid` = ? AND `rank_ordinal` < ?", uuid.toString(), 5);
	}
	
	public static boolean isInGroup(UUID uuid, String groupname)
	{
		return plugin.getMysqlHandler().exist(Type.GROUP_PLAYERAFFILIATION,
				"`player_uuid` = ? AND `group_name` = ? AND `rank_ordinal` < ?", uuid.toString(), groupname, 5);
	}
	
	public static void createAffiliateGroup(UUID uuid, String groupname, GroupHandler.Position rank, boolean grandMaster)
	{
		GroupPlayerAffiliation gpa = new GroupPlayerAffiliation(0, groupname,
				uuid, rank,	getDefaultUpKeep(rank),
				grandMaster, grandMaster, grandMaster, grandMaster, grandMaster, grandMaster, grandMaster, grandMaster, grandMaster);
		plugin.getMysqlHandler().create(Type.GROUP_PLAYERAFFILIATION, gpa);
	}
	
	public static GroupPlayerAffiliation getAffiliateGroup(Player player)
	{
		return getAffiliateGroup(player.getUniqueId());
	}
	
	public static GroupPlayerAffiliation getAffiliateGroup(UUID uuid)
	{
		return (GroupPlayerAffiliation) plugin.getMysqlHandler().getData(Type.GROUP_PLAYERAFFILIATION, "`player_uuid` = ?", uuid.toString());
	}
	
	public static GroupPlayerAffiliation getAffiliateGroup(UUID uuid, String groupname)
	{
		if(groupname != null)
		{
			return (GroupPlayerAffiliation) plugin.getMysqlHandler().getData(Type.GROUP_PLAYERAFFILIATION,
					"`player_uuid` = ? AND `group_name` = ? AND `rank_ordinal` < ?", uuid.toString(), groupname, 5);
		}
		return (GroupPlayerAffiliation) plugin.getMysqlHandler().getData(Type.GROUP_PLAYERAFFILIATION,
				"`player_uuid` = ?", uuid.toString());
	}
	
	public static GroupPlayerAffiliation getAffiliateGroup(UUID uuid, String groupname, Position pos)
	{
		return (GroupPlayerAffiliation) plugin.getMysqlHandler().getData(Type.GROUP_PLAYERAFFILIATION,
				"`player_uuid` = ? AND `group_name` = ? AND `rank_ordinal` = ?", uuid.toString(), groupname, pos.getRank());
	}
	
	public static ArrayList<GroupPlayerAffiliation> getAllAffiliateGroup(String groupname)
	{
		return GroupPlayerAffiliation.convert(plugin.getMysqlHandler().getFullList(Type.GROUP_PLAYERAFFILIATION, "`id` ASC", "`group_name` = ?", groupname));
	}
	
	public static ArrayList<GroupPlayerAffiliation> getAllAffiliateGroup(String groupname, Position pos)
	{
		return GroupPlayerAffiliation.convert(plugin.getMysqlHandler().getFullList(Type.GROUP_PLAYERAFFILIATION,
				"`id` ASC", "`group_name` = ? AND `rank_ordinal` = ?", groupname, pos.getRank()));
	}
	
	public static void updatePlayerAffiliation(GroupPlayerAffiliation gpa)
	{
		plugin.getMysqlHandler().updateData(Type.GROUP_PLAYERAFFILIATION, gpa, "`id` = ?", gpa.getId());
	}
	
	public static boolean createGroup(Player player, String groupname)
	{
		if(isInGroup(player.getUniqueId()))
		{
			return false;
		}
		GroupData gd = new GroupData(0, groupname, player.getUniqueId(),
				System.currentTimeMillis(), "N.A.", 0, 1,
				getMemberTotalAmount(groupname, 0, 1),
				getMemberTotalAmount(Position.MASTER, groupname, 0, 1),
				getMemberTotalAmount(Position.VICE, groupname, 0, 1),
				getMemberTotalAmount(Position.COUNCILMEMBER, groupname, 0, 1),
				getMemberTotalAmount(Position.MEMBER, groupname, 0, 1),
				0,
				getDefaultUpKeep(Position.MASTER),
				getDefaultUpKeep(Position.VICE),
				getDefaultUpKeep(Position.COUNCILMEMBER),
				getDefaultUpKeep(Position.MEMBER),
				getDefaultUpKeep(Position.ADEPT));
		plugin.getMysqlHandler().create(Type.GROUP_DATA, gd);
		return true;
	}
	
	public static GroupData getGroup(String groupname)
	{
		return (GroupData) plugin.getMysqlHandler().getData(Type.GROUP_DATA, "`group_name` = ?", groupname);
	}
	
	public static GroupData getGroup(Player player)
	{
		return getGroup(player.getUniqueId());
	}
	
	public static GroupData getGroup(UUID uuid)
	{
		GroupPlayerAffiliation gpa = getAffiliateGroup(uuid);
		if(gpa == null || gpa.getRank().getRank() > 4)
		{
			return null;
		}
		return (GroupData) plugin.getMysqlHandler().getData(Type.GROUP_DATA, "`group_name` = ?", gpa.getGroupName());
	}
	
	public static void updateGroup(GroupData gd)
	{
		plugin.getMysqlHandler().updateData(Type.GROUP_DATA, gd, "`id` = ?", gd.getId());
	}
	
	public static int getGroupLevel(Player player)
	{
		GroupData gd = getGroup(player);
		int i = 0;
		if(gd != null)
		{
			i = gd.getGroupLevel();
		}
		return i;
	}
	
	public static int getGroupMemberAmount(Player player)
	{
		GroupData gd = getGroup(player);
		int i = 0;
		if(gd != null)
		{
			i = plugin.getMysqlHandler().getCount(Type.GROUP_PLAYERAFFILIATION, "`group_name` = ? AND `rank_ordinal` < ?", gd.getGroupName(), 5);
		}
		return i;
	}
	
	public static int getGroupMemberAmount(String groupname)
	{
		return plugin.getMysqlHandler().getCount(Type.GROUP_PLAYERAFFILIATION, "`group_name` = ? AND `rank_ordinal` < ?", groupname, 5);
	}
	
	public static int getTotalGroupPlayerAffiliate()
	{
		return plugin.getMysqlHandler().getCount(Type.GROUP_PLAYERAFFILIATION, "`id` > ?", 0);
	}
	
	public static int getTotalGroupAmount()
	{
		return plugin.getMysqlHandler().getCount(Type.GROUP_DATA, "`id` > ?", 0);
	}
	
	public static void sendMembersText(String groupname, String txt, UUID...uuid)
	{
		ArrayList<UUID> l = new ArrayList<>();
		ArrayList<String> already = new ArrayList<>();
		for(UUID ui : uuid)
		{
			Player p = Bukkit.getPlayer(ui);
			if(p != null)
			{
				p.sendMessage(ChatApi.tl(txt));
				already.add(ui.toString());
			} else
			{
				l.add(ui);
			}
		}
		for(GroupPlayerAffiliation ga : GroupHandler.getAllAffiliateGroup(groupname))
		{
			Player m = Bukkit.getPlayer(ga.getPlayerUUID());
			if(!already.contains(ga.getPlayerUUID().toString()) && ga.getRank().getRank() < 5)
			{
				if(m != null)
				{
					m.sendMessage(ChatApi.tl(txt));
				} else
				{
					l.add(ga.getPlayerUUID());
				}
			}
		}
		if(plugin.getMessageToBungee() != null && !l.isEmpty())
		{
			plugin.getMessageToBungee().sendMessage(l, txt);
		}
	}
	
	public static void sendMembersText(String groupname, ArrayList<String> txt, UUID...uuid)
	{
		ArrayList<UUID> l = new ArrayList<>();
		ArrayList<String> already = new ArrayList<>();
		for(UUID ui : uuid)
		{
			Player p = Bukkit.getPlayer(ui);
			if(p != null)
			{
				for(String s : txt)
				{
					p.sendMessage(ChatApi.tl(s));
				}
				already.add(ui.toString());
			} else
			{
				l.add(ui);
			}
		}
		for(GroupPlayerAffiliation ga : GroupHandler.getAllAffiliateGroup(groupname))
		{
			Player m = Bukkit.getPlayer(ga.getPlayerUUID());
			if(!already.contains(ga.getPlayerUUID().toString()) && ga.getRank().getRank() < 5)
			{
				if(m != null)
				{
					for(String s : txt)
					{
						m.sendMessage(ChatApi.tl(s));
					}
				} else
				{
					l.add(ga.getPlayerUUID());
				}
			}
		}
		if(plugin.getMessageToBungee() != null && !l.isEmpty())
		{
			plugin.getMessageToBungee().sendMessage(l, txt.toArray(new String[txt.size()]));
		}
	}
	
	public static void sendMemberText(UUID uuid, String txt)
	{
		if(Bukkit.getPlayer(uuid) != null)
		{
			Bukkit.getPlayer(uuid).sendMessage(ChatApi.tl(txt));
		} else if(plugin.getMessageToBungee() != null)
		{
			plugin.getMessageToBungee().sendMessage(uuid, txt);
		}
	}
	
	public static int getGroupAffiliates(String groupname, Position pos)
	{
		return plugin.getMysqlHandler().getCount(Type.GROUP_PLAYERAFFILIATION, "`group_name` = ? AND `rank_ordinal` = ?", groupname, pos.getRank());
	}
}