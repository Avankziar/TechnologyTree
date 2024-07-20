package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.assistance.Numbers;
import main.java.me.avankziar.tt.spigot.assistance.TimeHandler;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandExecuteType;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandSuggest;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler.Position;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.Bypass.Permission;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.ModifierValueEntry;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGGroup_Info extends ArgumentModule
{	
	public ARGGroup_Info(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group info [group]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String gn = null;
		if(args.length >= 3)
		{
			gn = args[2];
		}
		GroupData gd = gn == null ? GroupHandler.getGroup(player) : GroupHandler.getGroup(gn);
		if(gd == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.GroupDontExist")
					.replace("%group%", "N.A.")));
			return;
		}
		GroupPlayerAffiliation gpa = GroupHandler.getAffiliateGroup(player.getUniqueId(), gd.getGroupName());
		boolean bypass0 = ((gpa != null || ModifierValueEntry.hasPermission(player, Permission.GROUP_INFO)) ? true : false);
		boolean bypassI = (((gpa != null && gpa.getRank().getRank() < 3) || ModifierValueEntry.hasPermission(player, Permission.GROUP_INFO)) ? true : false);
		ArrayList<ArrayList<BaseComponent>> a = new ArrayList<>();
		ArrayList<BaseComponent> l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Headline")
				.replace("%group%", gd.getGroupName())
				.replace("%id%", String.valueOf(gd.getId()))));
		a.add(l);
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Creation")
				.replace("%creation%", TimeHandler.getDateTime(gd.getCreationTime()))));
		a.add(l);
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Description")));
		a.add(l);
		l = new ArrayList<>();
		l.add(ChatApi.tctl(gd.getDisplayDescription()));
		a.add(l);
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Level")
				.replace("%level%", String.valueOf(gd.getGroupLevel()))
				.replace("%maxlevel%", String.valueOf(GroupHandler.getMaximumAchievableLevel()))));
		a.add(l);
		int members = GroupHandler.getGroupMemberAmount(gd.getGroupName());
		if(bypass0 || bypassI)
		{			
			double costForNextLvl = GroupHandler.getCostsForIncreasingLevel(gd.getGroupName(), gd.getGroupLevel(), members);
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.CostForNextLevel")
					.replace("%cost%", Numbers.format(costForNextLvl))));
			a.add(l);
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.TTExp")
					.replace("%ttexp%", Numbers.format(gd.getGroupTechExp()))));
			a.add(l);
		}
		String grandmaster = Utility.convertUUIDToName(gd.getGrandmasterUUID().toString());
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Grandmaster")
				.replace("%grandmaster%", grandmaster)));
		a.add(l);
		
		int tmembers = GroupHandler.getMemberTotalAmount(gd.getGroupName(), gd.getGroupLevel(), members);
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Memberamount")
				.replace("%member%", String.valueOf(members))
				.replace("%maxmember%", String.valueOf(tmembers))));
		a.add(l);
		ArrayList<GroupPlayerAffiliation> agpa = GroupPlayerAffiliation.convert(
				plugin.getMysqlHandler().getFullList(Type.GROUP_PLAYERAFFILIATION,
						"`rank_ordinal` ASC, `id` ASC", "`group_name` = ? AND `rank_ordinal` < ?", gd.getGroupName(), 5));
		if(bypass0 || bypassI)
		{
			double getUpkeep = 0;
			for(GroupPlayerAffiliation gpla : agpa)
			{
				if(gpla != null)
				{
					getUpkeep += gpla.getIndividualTechExpDailyUpkeep();
				}
			}
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.GetUpkeep")
					.replace("%upkeep%", Numbers.format(getUpkeep))));
			a.add(l);
			if(gd.getGroupLevel() >= new ConfigHandler().getUpkeepActiveLvl())
			{
				double upkeep = GroupHandler.calculateGroupDailyUpKeep(gd.getGroupName(), gd.getGroupLevel(), members);
				l = new ArrayList<>();
				l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Upkeep")
						.replace("%upkeep%", Numbers.format(upkeep))));
				a.add(l);
			}
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.CounterFailedUpkeep")
					.replace("%failedupkeep%", String.valueOf(gd.getGroupCounterFailedUpkeep()))));
			a.add(l);
		}
		getLines(a, gd, Position.MASTER, bypassI);
		getLines(a, gd, Position.VICE, bypassI);
		getLines(a, gd, Position.COUNCILMEMBER, bypassI);
		getLines(a, gd, Position.MEMBER, bypassI);
		getLines(a, gd, Position.ADEPT, bypassI);
		if(bypass0)
		{
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.Line")));
			a.add(l);
			if(agpa != null && !agpa.isEmpty())
			{
				l = new ArrayList<>();
				for(int i = 0; i < agpa.size(); i++)
				{
					GroupPlayerAffiliation gpla = agpa.get(i);
					String n = Utility.convertUUIDToName(gpla.getPlayerUUID().toString());
					TextComponent tx = null;
					if(!bypassI)
					{
						tx = ChatApi.hoverEvent(
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.Members")
								.replace("%member%", n), 
								Action.SHOW_TEXT, 
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.Hover0")
								.replace("%rank%", GroupHandler.getPositionLocale(gpla.getRank()))
								);
					} else if(bypassI)
					{
						tx = ChatApi.hoverEvent(
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.Members")
								.replace("%member%", n), 
								Action.SHOW_TEXT, 
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.Hover0")
								.replace("%rank%", GroupHandler.getPositionLocale(gpla.getRank()))
								+"~!~"+
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.HoverI")
								.replace("%value%", String.valueOf(gpla.getIndividualTechExpDailyUpkeep()))
								+"~!~"+
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.HoverII")
								.replace("%value%", getBoolean(gpla.isCanSetIndividualDailyUpkeep()))
								+"~!~"+
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.HoverIII")
								.replace("%value%", getBoolean(gpla.isCanKick()))
								+"~!~"+
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.HoverIV")
								.replace("%value%", getBoolean(gpla.isCanSetDefaultDailyUpkeep()))
								+"~!~"+
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.HoverV")
								.replace("%value%", getBoolean(gpla.isCanPromote()))
								+"~!~"+
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.HoverVI")
								.replace("%value%", getBoolean(gpla.isCanDemote()))
								+"~!~"+
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.HoverVII")
								.replace("%value%", getBoolean(gpla.isCanIncreaseLevel()))
								+"~!~"+
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.HoverVIII")
								.replace("%value%", getBoolean(gpla.isCanResearch()))
								+"~!~"+
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.HoverIX")
								.replace("%value%", getBoolean(gpla.isCanInvite()))
								+"~!~"+
								plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.HoverX")
								.replace("%value%", getBoolean(gpla.isCanAcceptApplication()))
								);
					}
					l.add(tx);
					if(i+1 < agpa.size())
					{
						l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Members.Comma")));
					}
				}
				a.add(l);
			}
		}
		int inv = GroupHandler.getGroupAffiliates(gd.getGroupName(), Position.INVITEE);
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Invitee.ToJugde")
				.replace("%value%", String.valueOf(inv))));
		a.add(l);
		if(inv > 0 && bypass0)
		{
			ArrayList<GroupPlayerAffiliation> agpaa = GroupHandler.getAllAffiliateGroup(gd.getGroupName(), Position.APPLICANT);
			if(agpaa != null && !agpaa.isEmpty())
			{
				l = new ArrayList<>();
				l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Invitee.Invitees")));
				a.add(l);
				l = new ArrayList<>();
				for(int i = 0; i < agpaa.size(); i++)
				{
					GroupPlayerAffiliation gpla = agpaa.get(i);
					String n = Utility.convertUUIDToName(gpla.getPlayerUUID().toString());
					TextComponent tx0 = ChatApi.tctl(
							plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Invitee.Members")
							.replace("%member%", n));
					l.add(tx0);
					if(i+1 < agpa.size())
					{
						l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Invitee.Comma")));
					}
				}
				a.add(l);
			}
		}
		int app = GroupHandler.getGroupAffiliates(gd.getGroupName(), Position.APPLICANT);
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Applicant.ToJugde")
				.replace("%value%", String.valueOf(app))));
		a.add(l);
		if(app > 0 && bypass0)
		{
			ArrayList<GroupPlayerAffiliation> agpaa = GroupHandler.getAllAffiliateGroup(gd.getGroupName(), Position.APPLICANT);
			if(agpaa != null && !agpaa.isEmpty())
			{
				l = new ArrayList<>();
				l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Applicant.Applicants")));
				a.add(l);
				l = new ArrayList<>();
				for(int i = 0; i < agpaa.size(); i++)
				{
					GroupPlayerAffiliation gpla = agpaa.get(i);
					String n = Utility.convertUUIDToName(gpla.getPlayerUUID().toString());
					TextComponent tx0 = ChatApi.tctl(
							plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Applicant.Members")
							.replace("%member%", n));
					TextComponent txI = ChatApi.clickEvent(
							plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Applicant.Accept"),
							ClickEvent.Action.SUGGEST_COMMAND,
							CommandSuggest.get(CommandExecuteType.TT_GROUP_APPLICATION_ACCEPT)+n
							);
					TextComponent txII = ChatApi.tctl(
							plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Applicant.Seperator"));
					TextComponent txIII = ChatApi.clickEvent(
							plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Applicant.Deny"),
							ClickEvent.Action.SUGGEST_COMMAND,
							CommandSuggest.get(CommandExecuteType.TT_GROUP_APPLICATION_DENY)+n
							);
					l.add(tx0);
					l.add(txI);
					l.add(txII);
					l.add(txIII);
					if(i+1 < agpa.size())
					{
						l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Applicant.Comma")));
					}
				}
				a.add(l);
			}
		}
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Bottomline")));
		a.add(l);
		for(ArrayList<BaseComponent> bc : a)
		{
			TextComponent tx = ChatApi.tc("");
			tx.setExtra(bc);
			player.spigot().sendMessage(tx);
		}
	}
	
	public static void getLines(ArrayList<ArrayList<BaseComponent>> a, GroupData gd, Position pos, boolean bypassI)
	{
		String max = "";
		int maxvalue = 0;
		double upkeep = 0;
		switch(pos)
		{
		default: 
			break;
		case MASTER:
			maxvalue = gd.getMaxAmountMaster();
			upkeep = gd.getDefaultGroupTechExpDailyUpkeep_Master();
			break;
		case VICE:
			maxvalue = gd.getMaxAmountVice();
			upkeep = gd.getDefaultGroupTechExpDailyUpkeep_Vice();
			break;
		case COUNCILMEMBER:
			maxvalue = gd.getMaxAmountCouncilMember();
			upkeep = gd.getDefaultGroupTechExpDailyUpkeep_CouncilMember();
			break;
		case MEMBER:
			maxvalue = gd.getMaxAmountMember();
			upkeep = gd.getDefaultGroupTechExpDailyUpkeep_Member();
			break;
		case ADEPT:
			max = "∞";
			upkeep = gd.getDefaultGroupTechExpDailyUpkeep_Adept();
			break;
		}
		ArrayList<BaseComponent> l = new ArrayList<>();
		l.add(ChatApi.tctl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.Group.Info.Member."+pos.toString())
				.replace("%value%", String.valueOf(GroupHandler.getGroupAffiliates(gd.getGroupName(), pos)))
				.replace("%maxvalue%", pos == Position.ADEPT ? max : String.valueOf(maxvalue))));
		a.add(l);
		if(bypassI)
		{
			l = new ArrayList<>();
			l.add(ChatApi.tctl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.Group.Info.DefaultUpkeep."+pos.toString())
					.replace("%upkeep%", Numbers.format(upkeep))));
			a.add(l);
		}
	}
	
	public static String getBoolean(boolean boo)
	{
		return boo ? TT.getPlugin().getYamlHandler().getLang().getString("IsTrue") : TT.getPlugin().getYamlHandler().getLang().getString("IsFalse");
	}
}