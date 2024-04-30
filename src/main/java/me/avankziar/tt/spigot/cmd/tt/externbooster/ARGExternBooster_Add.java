package main.java.me.avankziar.tt.spigot.cmd.tt.externbooster;

import java.io.IOException;
import java.util.ArrayList;
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
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.mysql.ExternBooster;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;

public class ARGExternBooster_Add extends ArgumentModule
{	
	public ARGExternBooster_Add(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt externbooster add <ExternBoosterName> <EventType> <PlayerAssociatedType> <factor(double)> <time|-1|00d-00H-00m> [playername/group/permission] [associateplayer]
	//<EventType> can be 
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		String name = args[2];
		ArrayList<EventType> eta = new ArrayList<>();
		try
		{
			if(args[3].split(";").length > 1)
			{
				for(String s : args[3].split(";"))
				{
					eta.add(EventType.valueOf(s));
				}
			} else
			{
				eta.add(EventType.valueOf(args[3]));
			}
			
		} catch(Exception e)
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.Add.NoEventType")
					.replace("%value%", args[3])));
			return;
		}
		PlayerAssociatedType pat = null;
		try
		{
			pat = PlayerAssociatedType.valueOf(args[4]);
		} catch(Exception e)
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.Add.NoPlayerAssociatedType")
					.replace("%value%", args[4])));
			return;
		}
		if(!MatchApi.isDouble(args[5]))
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoDouble").replace("%value%", args[5])));
			return;
		}
		double f = Double.parseDouble(args[5]);
		if(!MatchApi.isPositivNumber(f))
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("IsNegativ").replace("%value%", args[5])));
			return;
		}
		long t = args[6].equals("-1") ? Long.MAX_VALUE : TimeHandler.getTiming(args[6]);
		if(t <= 0L)
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.Add.NoTiming")));
			return;
		}
		String group = null;
		String playeruuid = null;
		String permission = null;
		if(args.length < 8 && (pat == PlayerAssociatedType.GROUP || pat == PlayerAssociatedType.SOLO))
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.Add.NeedAnArgument")));
			return;
		}
		if(args.length >= 8)
		{
			switch(pat)
			{
			case GLOBAL:
				permission = args[7];
				break;
			case SOLO:
				UUID uuid = Utility.convertNameToUUID(args[7]);
				if(uuid == null)
				{
					sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.PlayerNotExist")
							.replace("%player%", args[7])));
					return;
				}
				playeruuid = uuid.toString();
				break;
			case GROUP:
				group = args[7];
				if(!plugin.getMysqlHandler().exist(Type.GROUP_DATA, "`group_name` = ?", group))
				{
					sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.Add.GroupDontExist")
							.replace("%value%", group)));
					return;
				}
				break;
			}
		}
		String associatedplayer = plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.Add.AssociatedConsole");
		if(args.length >= 9)
		{
			associatedplayer = args[8];
		}
		String times = args[6].equals("-1") ? plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.Add.PermanentTime") : args[6];
		StringBuilder sb = new StringBuilder();
		for(EventType et : eta)
		{
			if(!sb.isEmpty())
			{
				sb.append(", ");
			}
			sb.append(plugin.getYamlHandler().getLang().getString("Events."+et.toString()));
		}
		ArrayList<String> al = new ArrayList<>();
		switch(pat)
		{
		case GLOBAL:
			for(String s : plugin.getYamlHandler().getLang().getStringList("Commands.ExternBooster.Add.Adding.Global"))
			{
				al.add(s
						.replace("%associatedplayer%", associatedplayer)
						.replace("%time%", times)
						.replace("%factor%", String.valueOf(f))
						.replace("%event%", sb.toString()));
			}
			if(plugin.getMessageToBungee() != null)
			{
				if(permission != null)
				{
					plugin.getMessageToBungee().sendMessage(permission, true, al.toArray(new String[al.size()]));
				} else
				{
					plugin.getMessageToBungee().sendMessage(al.toArray(new String[al.size()]));
				}
			} else
			{
				for(Player player : Bukkit.getOnlinePlayers())
				{
					for(String s : al)
					{
						if(permission != null)
						{
							if(player.hasPermission(permission))
							{
								player.sendMessage(ChatApi.tl(s));
							}
						} else
						{
							player.sendMessage(ChatApi.tl(s));
						}
					}
				}
			}
			break;
		case GROUP:
			GroupData gd = GroupHandler.getGroup(group);
			if(gd == null)
			{
				sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.GroupDontExist")
						.replace("%group%", group)));
				return;
			}
			for(String s : plugin.getYamlHandler().getLang().getStringList("Commands.ExternBooster.Add.Adding.Group"))
			{
				al.add(s
						.replace("%group%", gd.getGroupName())
						.replace("%associatedplayer%", associatedplayer)
						.replace("%time%", times)
						.replace("%factor%", String.valueOf(f))
						.replace("%event%", sb.toString()));
			}
			UUID suuid = null;
			if(sender instanceof Player)
			{
				suuid = ((Player) sender).getUniqueId();
				if(GroupHandler.isInGroup(suuid, group))
				{
					suuid = null;
				}
			}
			GroupHandler.sendMembersText(group, al, suuid);
			break;
		case SOLO:
			for(String s : plugin.getYamlHandler().getLang().getStringList("Commands.ExternBooster.Add.Adding.Solo"))
			{
				al.add(s
						.replace("%player%", args[7])
						.replace("%associatedplayer%", associatedplayer)
						.replace("%time%", times)
						.replace("%factor%", String.valueOf(f))
						.replace("%event%", sb.toString()));
			}
			if(Bukkit.getPlayer(UUID.fromString(playeruuid)) != null)
			{
				Player player = Bukkit.getPlayer(UUID.fromString(playeruuid));
				for(String s : al)
				{
					player.sendMessage(ChatApi.tl(s));
					sender.sendMessage(ChatApi.tl(s));
				}
			} else
			{
				if(plugin.getMessageToBungee() != null)
				{
					plugin.getMessageToBungee().sendMessage(UUID.fromString(playeruuid), al.toArray(new String[al.size()]));
				}
				for(String s : al)
				{
					sender.sendMessage(ChatApi.tl(s));
				}
			}
			break;
		}
		for(EventType et : eta)
		{
			ExternBooster exb = new ExternBooster(0, name, et, pat, f, t, permission, playeruuid, group);
			plugin.getMysqlHandler().create(Type.EXTERN_BOOSTER, exb);
		}
	}
}