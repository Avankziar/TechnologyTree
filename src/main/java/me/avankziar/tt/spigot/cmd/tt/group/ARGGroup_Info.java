package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.assistance.TimeHandler;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler.Position;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.ModifierValueEntry;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.Bypass.Permission;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class ARGGroup_Info extends ArgumentModule
{
	private TT plugin;
	
	public ARGGroup_Info(TT plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
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
		boolean bypass = ((gpa != null || ModifierValueEntry.hasPermission(player, Permission.GROUP_INFO)) ? true : false);
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
		if(bypass)
		{			
			double costForNextLvl = GroupHandler.getCostsForIncreasingLevel(gd.getGroupName(), gd.getGroupLevel(), members);
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.CostForNextLevel")
					.replace("%cost%", String.valueOf(costForNextLvl))));
			a.add(l);
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.TTExp")
					.replace("%cost%", String.valueOf(gd.getGroupTechExp()))));
			a.add(l);
		}
		String grandmaster = Utility.convertUUIDToName(gd.getGrandmasterUUID().toString());
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Grandmaster")
				.replace("%level%", grandmaster)));
		a.add(l);
		
		int tmembers = GroupHandler.getMemberTotalAmount(gd.getGroupName(), gd.getGroupLevel(), members);
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Memberamount")
				.replace("%member%", String.valueOf(members))
				.replace("%maxmember%", String.valueOf(tmembers))));
		a.add(l);
		if(bypass)
		{
			double upkeep = GroupHandler.calculateGroupDailyUpKeep(gd.getGroupName(), gd.getGroupLevel(), members);
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Upkeep")
					.replace("%member%", String.valueOf(upkeep))));
			a.add(l);
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.CounterFailedUpkeep")
					.replace("%failedupkeep%", String.valueOf(gd.getGroupCounterFailedUpkeep()))));
			a.add(l);
		}
		Position pos = Position.MASTER;
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Info.Member."+pos.toString())
				.replace("%value%", String.valueOf(GroupHandler.getGroupAffiliates(gd.getGroupName(), pos)))
				.replace("%maxvalue%", String.valueOf(gd.getMaxAmountMaster()))));
		a.add(l);
		if(bypass)
		{
			ArrayList<GroupPlayerAffiliation> agpa = GroupHandler.getAllAffiliateGroup(gd.getGroupName(), pos);
			if(agpa != null && !agpa.isEmpty())
			{
				l = new ArrayList<>();
				for(int i = 0; i < agpa.size(); i++)
				{
					GroupPlayerAffiliation gpla = agpa.get(i);
					String n = Utility.convertUUIDToName(gpla.getPlayerUUID().toString());
					TextComponent tx = ChatApi.hoverEvent(gn, Action.SHOW_TEXT, grandmaster);
				}
				a.add(l);
			}
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Info.DefaultUpkeep."+pos.toString())
					.replace("%upkeep%", String.valueOf(gd.getDefaultGroupTechExpDailyUpkeep_Master()))));
			a.add(l);
		}
		
		GroupHandler.getGroupAffiliates(gd.getGroupName(), Position.INVITEE);
		GroupHandler.getGroupAffiliates(gd.getGroupName(), Position.APPLICANT);
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
}