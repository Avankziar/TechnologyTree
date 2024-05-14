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
import main.java.me.avankziar.tt.spigot.objects.RewardType;
import main.java.me.avankziar.tt.spigot.objects.mysql.ExternBooster;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;

public class ARGExternBooster_Add extends ArgumentModule
{	
	public ARGExternBooster_Add(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt externbooster add <ExternBoosterName> <EventType> <PlayerAssociatedType> <rewardType> <factor(double)> <time|-1|00d-00H-00m> [playername/group/permission] [associateplayer]
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
		ArrayList<RewardType> rta = new ArrayList<>();
		try
		{
			if(args[5].split(";").length > 1)
			{
				for(String s : args[5].split(";"))
				{
					rta.add(RewardType.valueOf(s));
				}
			} else
			{
				rta.add(RewardType.valueOf(args[5]));
			}
			
		} catch(Exception e)
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.Add.NoRewardType")
					.replace("%value%", args[4])));
			return;
		}
		if(!MatchApi.isDouble(args[6]))
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoDouble").replace("%value%", args[6])));
			return;
		}
		double f = Double.parseDouble(args[6]);
		if(!MatchApi.isPositivNumber(f))
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("IsNegativ").replace("%value%", args[6])));
			return;
		}
		long t = args[7].equals("-1") ? Long.MAX_VALUE : System.currentTimeMillis() + TimeHandler.getTiming(args[7]);
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
		if(args.length >= 9)
		{
			switch(pat)
			{
			case GLOBAL:
				permission = args[8];
				break;
			case SOLO:
				UUID uuid = Utility.convertNameToUUID(args[8]);
				if(uuid == null)
				{
					sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.PlayerNotExist")
							.replace("%player%", args[7])));
					return;
				}
				playeruuid = uuid.toString();
				break;
			case GROUP:
				group = args[8];
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
		if(args.length >= 10)
		{
			associatedplayer = args[9];
		}
		String times = args[7].equals("-1") ? plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.Add.PermanentTime") : args[7];
		StringBuilder sb1 = new StringBuilder();
		for(EventType et : eta)
		{
			if(!sb1.isEmpty())
			{
				sb1.append(", ");
			}
			sb1.append(plugin.getYamlHandler().getLang().getString("Events."+et.toString()));
		}
		StringBuilder sb2 = new StringBuilder();
		for(RewardType rt : rta)
		{
			if(!sb2.isEmpty())
			{
				sb2.append(", ");
			}
			sb2.append(rt.toString());
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
						.replace("%event%", sb1.toString())
						.replace("%reward%", sb2.toString()));
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
						.replace("%event%", sb1.toString())
						.replace("%reward%", sb2.toString()));
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
						.replace("%event%", sb1.toString())
						.replace("%reward%", sb2.toString()));
			}
			if(Bukkit.getPlayer(UUID.fromString(playeruuid)) != null)
			{
				Player player = Bukkit.getPlayer(UUID.fromString(playeruuid));
				for(String s : al)
				{
					if(sender instanceof Player)
					{
						Player ps = (Player) sender;
						if(ps.getUniqueId().toString().equals(player.getUniqueId().toString()))
						{
							player.sendMessage(ChatApi.tl(s));
						} else
						{
							player.sendMessage(ChatApi.tl(s));
							sender.sendMessage(ChatApi.tl(s));
						}
					} else
					{
						player.sendMessage(ChatApi.tl(s));
						sender.sendMessage(ChatApi.tl(s));
					}
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
			for(RewardType rt : rta)
			{
				ExternBooster exb = new ExternBooster(0, name, et, pat, rt, f, t, permission, playeruuid, group);
				plugin.getMysqlHandler().create(Type.EXTERN_BOOSTER, exb);
			}
		}
	}
}