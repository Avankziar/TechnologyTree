package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandExecuteType;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandSuggest;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGGroup_PlayerInfo extends ArgumentModule
{	
	public ARGGroup_PlayerInfo(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group playerinfo [playername]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String p2 = player.getName();
		UUID uuid = player.getUniqueId();
		if(args.length >= 3)
		{
			p2 = args[2];
			uuid = Utility.convertNameToUUID(p2);
			if(uuid == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
				return;
			}
		}
		ArrayList<ArrayList<BaseComponent>> a = new ArrayList<>();
		ArrayList<BaseComponent> l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerInfo.Headline")
				.replace("%player%", p2)));
		a.add(l);
		GroupData gd = GroupHandler.getGroup(uuid);
		GroupPlayerAffiliation gpa = GroupHandler.getAffiliateGroup(uuid, gd == null ? "" : gd.getGroupName());
		if(GroupHandler.isInGroup(uuid) && gpa != null)
		{
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerInfo.Group")
					.replace("%group%", gd.getGroupName())
					.replace("%lvl%", String.valueOf(gd.getGroupLevel()))));
			a.add(l);
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerInfo.GroupPosition")
					.replace("%rank%", GroupHandler.getPositionLocale(gpa.getRank()))));
			a.add(l);
			String gm = Utility.convertUUIDToName(gd.getGrandmasterUUID().toString());
			if(gm == null)
			{
				gm = "/";
			}
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerInfo.GroupGrandMaster")
					.replace("%gm%", gm)));
			a.add(l);
			l = new ArrayList<>();
			int gmember = GroupHandler.getGroupMemberAmount(gd.getGroupName());
			int gtmember = GroupHandler.getMemberTotalAmount(gd.getGroupName(), gd.getGroupLevel(), gmember);
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerInfo.GroupMember")
					.replace("%member%", String.valueOf(gmember))
					.replace("%totalmember%", String.valueOf(gtmember))));
			a.add(l);
			l = new ArrayList<>();
			l.add(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerInfo.GroupInfo"),
					Action.SUGGEST_COMMAND, CommandSuggest.get(CommandExecuteType.TT_GROUP_INFO)+gd.getGroupName()));
			a.add(l);
			if(!p2.equals(player.getName()) && !GroupHandler.isInGroup(player.getUniqueId())
					&& gtmember > gmember)
			{
				l = new ArrayList<>();
				l.add(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerInfo.SendApplication"),
						Action.SUGGEST_COMMAND, CommandSuggest.get(CommandExecuteType.TT_GROUP_APPLICATION_SEND)+gd.getGroupName()));
				a.add(l);
			}
		} else
		{
			l = new ArrayList<>();
			l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerInfo.InNoGroup")));
			a.add(l);
		}
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerInfo.Bottomline")));
		a.add(l);
		for(ArrayList<BaseComponent> bc : a)
		{
			TextComponent tx = ChatApi.tc("");
			tx.setExtra(bc);
			player.spigot().sendMessage(tx);
		}
	}
}