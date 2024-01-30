package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.MatchApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandExecuteType;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandSuggest;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGGroup_List extends ArgumentModule
{	
	public ARGGroup_List(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group list [page]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		int page = 0;
		if(args.length >= 3)
		{
			if(!MatchApi.isInteger(args[2]))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%value%", args[2])));
				return;
			}
			page = Integer.valueOf(args[2]);
			if(!MatchApi.isPositivNumber(page))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("IsNegativ")
						.replace("%value%", args[2])));
				return;
			}
		}
		ArrayList<GroupData> lgd = GroupData.convert(plugin.getMysqlHandler().getList(Type.GROUP_DATA, "`group_name` ASC", page*10, 10, "`id` > ?", 1));
		if(lgd == null || lgd.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.List.NoGroups")));
			return;
		}
		ArrayList<ArrayList<BaseComponent>> a = new ArrayList<>();
		ArrayList<BaseComponent> l = new ArrayList<>();
		l.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.Group.List.Headline").replace("%page%", String.valueOf(page))));
		a.add(l);
		for(GroupData gd : lgd)
		{
			if(gd == null)
			{
				continue;
			}
			l = new ArrayList<>();
			String name = Utility.convertUUIDToName(gd.getGrandmasterUUID().toString());
			if(name == null)
			{
				continue;
			}
			String desc = "/";
			if(gd.getDisplayDescription() != null && !gd.getDisplayDescription().isEmpty())
			{
				desc = "";
				String[] sp = gd.getDisplayDescription().split(" ");
				StringBuilder sb = new StringBuilder();
				for(String s : sp)
				{
					sb.append(" ");
					if(sb.length() > 60)
					{
						desc += "~!~"+sb.toString();
						sb = new StringBuilder();
					}
					sb.append(s);
				}
			}
			l.add(ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("Commands.Group.List.Groups")
					.replace("%group%", gd.getGroupName()),
					ClickEvent.Action.SUGGEST_COMMAND, 
					CommandSuggest.get(CommandExecuteType.TT_GROUP_APPLICATION_SEND)+" "+gd.getGroupName(),
					HoverEvent.Action.SHOW_TEXT,
					plugin.getYamlHandler().getLang().getString("Commands.Group.List.GroupsHover")
					.replace("%grandmaster%", name)
					.replace("%desc%", desc)
					));
			a.add(l);
		}
		boolean lastpage = plugin.getMysqlHandler().exist(Type.GROUP_DATA, "`id` > ?", lgd.get(lgd.size()-1).getId());
		for(ArrayList<BaseComponent> bc : a)
		{
			TextComponent tx = ChatApi.tc("");
			tx.setExtra(bc);
			player.spigot().sendMessage(tx);
		}
		pastNextPage(player, page, lastpage, argumentConstructor);
	}
	
	public void pastNextPage(Player player,
			int page, boolean lastpage, BaseConstructor bc, String...objects)
	{
		if(page==0 && lastpage)
		{
			return;
		}
		int i = page+1;
		int j = page-1;
		TextComponent MSG = ChatApi.tctl("");
		List<BaseComponent> pages = new ArrayList<BaseComponent>();
		if(page!=0)
		{
			TextComponent msg2 = ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("Past"));
			String cmd = bc.getCommandString()+" "+String.valueOf(j);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			msg2.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		if(!lastpage)
		{
			TextComponent msg1 = ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("Next"));
			String cmd = bc.getCommandString()+" "+String.valueOf(i);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			msg1.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			if(pages.size()==1)
			{
				pages.add(ChatApi.tc(" | "));
			}
			pages.add(msg1);
		}
		MSG.setExtra(pages);	
		player.spigot().sendMessage(MSG);
	}
}