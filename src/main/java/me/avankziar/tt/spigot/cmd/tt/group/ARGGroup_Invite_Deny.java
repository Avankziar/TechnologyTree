package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGGroup_Invite_Deny extends ArgumentModule
{	
	public ARGGroup_Invite_Deny(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group invite deny <groupname>
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(GroupHandler.isInGroup(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.YouAreInAGroup")));
			return;
		}
		String gn = args[3];
		GroupData gd = GroupHandler.getGroup(gn);
		if(gd == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.GroupDontExist")
					.replace("%group%", gn)));
			return;
		}
		GroupPlayerAffiliation gpa = GroupHandler.getAffiliateGroup(player.getUniqueId(), gd.getGroupName());
		if(gpa == null || gpa.getRank().getRank() < 5)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Invite.Accept.NoInvitee")));
			return;
		}
		if(!gpa.getGroupName().equals(gd.getGroupName()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Invite.Accept.NoInviteeFromTheGroup")
					.replace("%group%", gd.getGroupName())
					.replace("%group2%", gpa.getGroupName())));
			return;
		}
		plugin.getMysqlHandler().deleteData(Type.GROUP_PLAYERAFFILIATION, "`player_uuid` = ? AND `group_name` = ?", player.getUniqueId().toString(), gd.getGroupName());
		String txt = ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Invite.Deny.DenyInvitation")
				.replace("%player%", player.getName())
				.replace("%group%", gd.getGroupName()));
		player.sendMessage(txt);
		ArrayList<UUID> l = new ArrayList<>();
		for(GroupPlayerAffiliation ga : GroupHandler.getAllAffiliateGroup(gpa.getGroupName()))
		{
			Player m = Bukkit.getPlayer(ga.getPlayerUUID());
			if(ga.getRank().getRank() > 2)
			{
				continue;
			}
			if(m != null)
			{
				m.sendMessage(txt);
			} else
			{
				l.add(ga.getPlayerUUID());
			}
		}
		if(plugin.getMessageToBungee() != null && !l.isEmpty())
		{
			plugin.getMessageToBungee().sendMessage(l, txt);
		}
	}
}