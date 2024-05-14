package main.java.me.avankziar.tt.spigot.cmd.tt.externbooster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.MatchApi;
import main.java.me.avankziar.tt.spigot.assistance.TimeHandler;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandExecuteType;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandSuggest;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.Bypass;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.Bypass.Permission;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.mysql.ExternBooster;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGExternBooster_List extends ArgumentModule
{	
	public ARGExternBooster_List(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt externbooster list [page] [playername]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		int page = 0;
		if(args.length >= 3)
		{
			String pages = args[2];
			if(MatchApi.isInteger(pages) && MatchApi.isPositivNumber(Integer.parseInt(pages)))
			{
				page = Integer.parseInt(pages);
			}
		}
		String playername = null;
		UUID uuid = null;
		if(args.length >= 4)
		{
			playername = args[3];
			uuid = Utility.convertNameToUUID(playername);
			if(uuid == null)
			{
				sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
				return;
			}
			if(sender instanceof Player)
			{
				Player player = (Player) sender;
				if(!playername.equals(player.getName()) && !player.hasPermission(Bypass.get(Permission.EXTERNBOOSTER_LIST_SEE_OTHERPLAYER)))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
			}
		} else
		{
			if(sender instanceof Player)
			{
				Player player = (Player) sender;
				if(!player.hasPermission(Bypass.get(Permission.EXTERNBOOSTER_LIST_SEE_ALL)))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
			}
		}
		if(uuid == null)
		{
			normal(sender, page);
		} else
		{
			player(sender, page, uuid, playername);
		}
	}
	
	private void normal(CommandSender sender, int page)
	{
		ArrayList<ExternBooster> exb = ExternBooster.convert(plugin.getMysqlHandler().getList(Type.EXTERN_BOOSTER, "`id` ASC", page*10, 10,
				"`id` > ?", 0));
		int size = plugin.getMysqlHandler().getCount(Type.EXTERN_BOOSTER, "`id` > ?", 0);
		ArrayList<TextComponent> tx = new ArrayList<>();
		tx.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Headline1")
				.replace("%page%", String.valueOf(page))));
		BaseConstructor ex_remove = BaseConstructor.getCommand("tt_externbooster_remove");
		String perm = ex_remove.getPermission();
		for(ExternBooster ex : exb)
		{
			String addUp = null;
			switch(ex.getPlayerAssociatedType())
			{
			case GLOBAL:
				addUp = plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.Permission")
					.replace("%perm%", ex.getPermission() != null ? ex.getPermission() : "/");
				break;
			case GROUP:
				addUp = plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.Group")
					.replace("%group%", ex.getGroupname() != null ? ex.getGroupname() : "/");
				break;
			case SOLO:
				addUp = plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.Player")
					.replace("%player%", ex.getPlayerUUIDText() != null ? Utility.convertUUIDToName(ex.getPlayerUUIDText()) : "/");
				break;
			}
			String time = ex.getExpiryDate() == Long.MAX_VALUE 
						? plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.Add.PermanentTime")
						: TimeHandler.getDateTime(ex.getExpiryDate());
			if(sender.hasPermission(perm))
			{
				tx.add(ChatApi.apiChat(ex.getId()+"/"+ex.getName(),
						ClickEvent.Action.SUGGEST_COMMAND, CommandSuggest.get(CommandExecuteType.TT_EXTERNBOOSTER_REMOVE)+ex.getId(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.EventType")
																	.replace("%eventtype%", Utility.tlEventType(ex.getEventType()))+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.PlayerAssociatedType")
																	.replace("%pat%", ex.getPlayerAssociatedType().toString())+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.RewardType")
																	.replace("%rt%", ex.getRewardType().toString())+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.Factor")
																	.replace("%factor%", String.valueOf(ex.getFactor()))+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.ExpiryDate")
																	.replace("%expirydate%", time)+"~!~"+
																	addUp
						));
			} else
			{
				tx.add(ChatApi.hoverEvent(ex.getId()+"/"+ex.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.EventType")
																	.replace("%eventtype%", Utility.tlEventType(ex.getEventType()))+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.PlayerAssociatedType")
																	.replace("%pat%", ex.getPlayerAssociatedType().toString())+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.RewardType")
																	.replace("%rt%", ex.getRewardType().toString())+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.Factor")
																	.replace("%factor%", String.valueOf(ex.getFactor()))+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.ExpiryDate")
																	.replace("%expirydate%", time)+"~!~"+
																	addUp
						));
			}
		}
		for(TextComponent txt : tx)
		{
			sender.spigot().sendMessage(txt);
		}
		pastNextPage(sender, page, page*10+10 < size, argumentConstructor);
	}
	
	private void player(CommandSender sender, int page, UUID uuid, String playername)
	{
		ArrayList<ExternBooster> externBooster = new ArrayList<>();
		if(GroupHandler.isInGroup(uuid))
		{
			GroupData gd = GroupHandler.getGroup(uuid);
			if(gd != null)
			{
				externBooster.addAll(ExternBooster.convert(plugin.getMysqlHandler().getFullList(Type.EXTERN_BOOSTER, "`id` ASC",
						"`player_associated_type` = ? AND `group_name` = ?", PlayerAssociatedType.GROUP.toString(), gd.getGroupName())));
			}					
		}
		externBooster.addAll(ExternBooster.convert(plugin.getMysqlHandler().getFullList(Type.EXTERN_BOOSTER, "`id` ASC",
				"`player_associated_type` = ? AND `player_uuid` = ?", PlayerAssociatedType.SOLO.toString(), uuid.toString())));
		Player player = Bukkit.getPlayer(uuid);
		for(ExternBooster ex : ExternBooster.convert(plugin.getMysqlHandler().getFullList(Type.EXTERN_BOOSTER, "`id` ASC",
				"`player_associated_type` = ?", PlayerAssociatedType.GLOBAL.toString())))
		{
			if(ex.getPlayerAssociatedType() == PlayerAssociatedType.GLOBAL)
			{
				if(ex.getPermission() != null)
				{
					if(player != null && !player.hasPermission(ex.getPermission()))
					{
						continue;
					}
				}
			}
			externBooster.add(ex);
		}
		ArrayList<TextComponent> tx = new ArrayList<>();
		tx.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Headline2")
				.replace("%page%", String.valueOf(page))
				.replace("%player%", playername)));
		if(player == null)
		{
			tx.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.PlayerNotOnline")
					.replace("%player%", playername)));
		}
		BaseConstructor ex_remove = BaseConstructor.getCommand("tt_externbooster_remove");
		String perm = ex_remove.getPermission();
		int down = page*10;
		int up = down+10;
		for(int i = 0; i < externBooster.size(); i++)
		{
			if(i < down)
			{
				continue;
			}
			if(i >= up)
			{
				break;
			}
			ExternBooster ex = externBooster.get(i);
			if(sender.hasPermission(perm))
			{
				tx.add(ChatApi.apiChat(ex.getId()+"/"+ex.getName(),
						ClickEvent.Action.SUGGEST_COMMAND, CommandSuggest.get(CommandExecuteType.TT_EXTERNBOOSTER_REMOVE)+ex.getId(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.EventType")
																	.replace("%eventtype%", Utility.tlEventType(ex.getEventType()))+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.PlayerAssociatedType")
																	.replace("%pat%", ex.getPlayerAssociatedType().toString())+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.RewardType")
																	.replace("%rt%", ex.getRewardType().toString())+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.Factor")
																	.replace("%factor%", String.valueOf(ex.getFactor()))+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.ExpiryDate")
																	.replace("%expirydate%", TimeHandler.getDateTime(ex.getExpiryDate()))
						));
			} else
			{
				tx.add(ChatApi.hoverEvent(ex.getId()+"/"+ex.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.EventType")
																	.replace("%eventtype%", Utility.tlEventType(ex.getEventType()))+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.PlayerAssociatedType")
																	.replace("%pat%", ex.getPlayerAssociatedType().toString())+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.RewardType")
																	.replace("%rt%", ex.getRewardType().toString())+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.Factor")
																	.replace("%factor%", String.valueOf(ex.getFactor()))+"~!~"+
						plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.List.Hover.ExpiryDate")
																	.replace("%expirydate%", TimeHandler.getDateTime(ex.getExpiryDate()))
						));
			}
		}
		for(TextComponent txt : tx)
		{
			sender.spigot().sendMessage(txt);
		}
		pastNextPage(sender, page, up < externBooster.size(), argumentConstructor, playername);
	}
	
	public void pastNextPage(CommandSender sender,
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
		sender.spigot().sendMessage(MSG);
	}
}