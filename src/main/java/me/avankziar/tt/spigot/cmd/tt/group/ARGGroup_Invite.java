package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandExecuteType;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandSuggest;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGGroup_Invite extends ArgumentModule
{	
	public ARGGroup_Invite(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group invite ...
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(GroupHandler.isInGroup(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.YouAreInAGroup")));
			return;
		}
		ArrayList<GroupPlayerAffiliation> gpa = GroupPlayerAffiliation.convert(plugin.getMysqlHandler().getFullList(
				Type.GROUP_PLAYERAFFILIATION, "`id` ASC", "`player_uuid` = ? AND `rank_ordinal` = ?", player.getUniqueId().toString(), 5));
		if(gpa == null || gpa.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Invite.List.NoInvites")));
			return;
		}
		ArrayList<ArrayList<BaseComponent>> a = new ArrayList<>();
		ArrayList<BaseComponent> l = new ArrayList<>();
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Invite.List.InvitingGroups")));
		a.add(l);
		l = new ArrayList<>();
		for(int i = 0; i < gpa.size(); i++)
		{
			GroupPlayerAffiliation gpla = gpa.get(i);
			String n = Utility.convertUUIDToName(gpla.getPlayerUUID().toString());
			TextComponent tx0 = ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("Commands.Group.Invite.List.Groups")
					.replace("%group%", n));
			TextComponent txI = ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("Commands.Group.Invite.List.Accept"),
					ClickEvent.Action.SUGGEST_COMMAND,
					CommandSuggest.get(CommandExecuteType.TT_GROUP_INVITE_ACCEPT)+n
					);
			TextComponent txII = ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("Commands.Group.Invite.List.Seperator"));
			TextComponent txIII = ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("Commands.GroupInvite.List.Deny"),
					ClickEvent.Action.SUGGEST_COMMAND,
					CommandSuggest.get(CommandExecuteType.TT_GROUP_INVITE_DENY)+n
					);
			l.add(tx0);
			l.add(txI);
			l.add(txII);
			l.add(txIII);
			if(i+1 < gpa.size())
			{
				l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Applicant.Comma")));
			}
		}
		for(ArrayList<BaseComponent> bc : a)
		{
			TextComponent tx = ChatApi.tc("");
			tx.setExtra(bc);
			player.spigot().sendMessage(tx);
		}
	}
}