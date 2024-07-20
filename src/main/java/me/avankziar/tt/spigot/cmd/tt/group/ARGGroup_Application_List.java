package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandExecuteType;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandSuggest;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler.Position;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.Bypass.Permission;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.ModifierValueEntry;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGGroup_Application_List extends ArgumentModule
{	
	public ARGGroup_Application_List(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group application list [group]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String gn = null;
		if(args.length >= 4 && ModifierValueEntry.hasPermission(player, Permission.GROUP_INFO))
		{
			gn = args[3];
		}
		GroupData gd = gn == null ? GroupHandler.getGroup(player) : GroupHandler.getGroup(gn);
		if(gd == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.GroupDontExist")
					.replace("%group%", "N.A.")));
			return;
		}
		ArrayList<GroupPlayerAffiliation> gpa = GroupHandler.getAllAffiliateGroup(gd.getGroupName(), Position.APPLICANT);
		if(gpa == null || gpa.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.ApplicationList.NoApplicants")));
			return;
		}
		ArrayList<ArrayList<BaseComponent>> a = new ArrayList<>();
		ArrayList<BaseComponent> l = new ArrayList<>();
		l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Applicant.Applicants")));
		a.add(l);
		l = new ArrayList<>();
		for(int i = 0; i < gpa.size(); i++)
		{
			GroupPlayerAffiliation gpla = gpa.get(i);
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
			if(i+1 < gpa.size())
			{
				l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.Info.Applicant.Comma")));
			}
			a.add(l);
		}
		for(ArrayList<BaseComponent> bc : a)
		{
			TextComponent tx = ChatApi.tc("");
			tx.setExtra(bc);
			player.spigot().sendMessage(tx);
		}
	}
}