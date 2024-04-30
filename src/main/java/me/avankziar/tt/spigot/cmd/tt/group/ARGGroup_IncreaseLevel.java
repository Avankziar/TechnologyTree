package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler.Position;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.Bypass.Permission;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.ModifierValueEntry;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGGroup_IncreaseLevel extends ArgumentModule
{	
	public ARGGroup_IncreaseLevel(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group increaselevel [groupname]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String gn = null;
		boolean bypass = ModifierValueEntry.hasPermission(player, Permission.GROUP_INCREASELEVEL);
		if(args.length >= 3)
		{
			if(!bypass)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return;
			}
			gn = args[2];
		}
		GroupData gd = gn == null ? GroupHandler.getGroup(player) : GroupHandler.getGroup(gn);
		if(gd == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.GroupDontExist")
					.replace("%group%", "N.A.")));
			return;
		}
		if(!bypass || gn == null)
		{
			GroupPlayerAffiliation gpa = GroupHandler.getAffiliateGroup(player.getUniqueId(), gd.getGroupName());
			if(gpa == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.YouAreInNoGroup")));
				return;
			}
			if(!gpa.isCanIncreaseLevel())
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Privilege.CanNotIncreaseLevel")));
				return;
			}
		}
		int maxLvl = GroupHandler.getMaximumAchievableLevel();
		if(maxLvl <= gd.getGroupLevel())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.IncreaseLevel.HaveMaxLevel")));
			return;
		}
		double cost = 0;
		if(!bypass)
		{
			cost = GroupHandler.getCostsForIncreasingLevel(gd.getGroupName(), gd.getGroupLevel(), GroupHandler.getGroupMemberAmount(player));
			if(gd.getGroupTechExp() < cost)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.IncreaseLevel.NotEnoughTTExp")));
				return;
			}
			gd.setGroupTechExp(gd.getGroupTechExp()-cost);
		}
		int members = GroupHandler.getGroupMemberAmount(gd.getGroupName());
		gd.setGroupLevel(gd.getGroupLevel()+1);
		gd.setMaxAmountPlayer(GroupHandler.getMemberTotalAmount(gd.getGroupName(), gd.getGroupLevel(), members));
		gd.setMaxAmountMaster(GroupHandler.getMemberTotalAmount(Position.MASTER, gd.getGroupName(), gd.getGroupLevel(), members));
		gd.setMaxAmountVice(GroupHandler.getMemberTotalAmount(Position.VICE, gd.getGroupName(), gd.getGroupLevel(), members));
		gd.setMaxAmountCouncilMember(GroupHandler.getMemberTotalAmount(Position.COUNCILMEMBER, gd.getGroupName(), gd.getGroupLevel(), members));
		gd.setMaxAmountMember(GroupHandler.getMemberTotalAmount(Position.MEMBER, gd.getGroupName(), gd.getGroupLevel(), members));
		GroupHandler.sendMembersText(gd.getGroupName(), plugin.getYamlHandler().getLang().getString("Commands.Group.IncreaseLevel.Increased")
				.replace("%pre%", String.valueOf(gd.getGroupLevel()-1))
				.replace("%post%", String.valueOf(gd.getGroupLevel()))
				.replace("%cost%", String.valueOf(cost)),
				player.getUniqueId());
		GroupHandler.updateGroup(gd);
	}
}