package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler.Position;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGGroup_Invite_Accept extends ArgumentModule
{	
	public ARGGroup_Invite_Accept(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group invite accept <groupname>
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
		int gda = plugin.getMysqlHandler().getCount(Type.GROUP_PLAYERAFFILIATION, "`group_name` = ? AND `rank_ordinal` < ?", gd.getGroupName(), 5);
		int gdta = GroupHandler.getMemberTotalAmount(gd.getGroupName(), gd.getGroupLevel(), gda);
		GroupPlayerAffiliation gpa = GroupHandler.getAffiliateGroup(player.getUniqueId(), gd.getGroupName(), Position.INVITEE);
		if(gpa == null || gpa.getRank().getRank() != 5)
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
		if(gdta <= gda)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.GroupHasToManyMember")));
			return;
		}
		plugin.getMysqlHandler().deleteData(Type.GROUP_PLAYERAFFILIATION, "`player_uuid` = ?", player.getUniqueId().toString());
		GroupHandler.createAffiliateGroup(player.getUniqueId(), gd.getGroupName(), Position.ADEPT, false);
		String txt = ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Invite.Accept.PlayerJointGroup")
				.replace("%player%", player.getName())
				.replace("%group%", gd.getGroupName()));
		GroupHandler.sendMembersText(gn, txt, player.getUniqueId());
		PlayerHandler.quitPlayer(player.getUniqueId());
		PlayerHandler.joinPlayer(player);
	}
}