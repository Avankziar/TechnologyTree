package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler.Position;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGGroup_Application_Deny extends ArgumentModule
{	
	public ARGGroup_Application_Deny(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group application deny <playername>
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(!GroupHandler.isInGroup(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.YouAreInNoGroup")));
			return;
		}
		if(!GroupHandler.getAffiliateGroup(player).isCanAcceptApplication())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Privilege.CanNotAcceptApplication")));
			return;
		}
		String p2 = args[3];
		UUID uuid = Utility.convertNameToUUID(p2);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		if(GroupHandler.isInGroup(uuid))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerIsInAGroup")
					.replace("%player%", p2)));
			return;
		}
		GroupData gd = GroupHandler.getGroup(player);
		GroupPlayerAffiliation gpa = GroupHandler.getAffiliateGroup(uuid, gd.getGroupName(), Position.APPLICANT);
		if(gpa == null || gpa.getRank().getRank() != 6)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Application.Accept.NoApplicant")
					.replace("%player%", p2)));
			return;
		}
		if(!gpa.getGroupName().equals(gd.getGroupName()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Application.Accept.NoInviteeFromTheGroup")));
			return;
		}
		plugin.getMysqlHandler().deleteData(Type.GROUP_PLAYERAFFILIATION, "`player_uuid` = ? AND `group_name` = ?", uuid.toString(), gd.getGroupName());
		String txt = ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Application.Deny.DenyEntryInTheGroup")
				.replace("%player%", p2)
				.replace("%group%", gd.getGroupName()));
		GroupHandler.sendMembersText(gd.getGroupName(), txt, uuid);
	}
}