package main.java.me.avankziar.tt.spigot.cmd.tt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class ARGCheckEventAction extends ArgumentModule
{
	private static TT plugin = BaseConstructor.getPlugin();
	public static ArrayList<UUID> checkEventType = new ArrayList<>();
	
	public ARGCheckEventAction(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt checkplacedblocks
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(checkEventType.contains(player.getUniqueId()))
				{
					checkEventType.remove(player.getUniqueId());
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.CheckEventAction.Removed")));
				} else
				{
					checkEventType.add(player.getUniqueId());
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.CheckEventAction.Added")));
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public static void checkEventAction(Player player, String value, EventType et, ToolType tool, Material mat, EntityType ent, Material mat2)
	{
		if(checkEventType.contains(player.getUniqueId()))
		{
			switch(value)
			{
			default:
				break;
			case "BREAKING:RETURN":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREAKING.Return")
						.replace("%eventtype%", et.toString())
						.replace("%tool%", tool.toString())
						.replace("%block%", mat.toString())));
				break;
			case "BREAKING:CANNOTACCESS":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREAKING.CantAccess")
						.replace("%eventtype%", et.toString())
						.replace("%tool%", tool.toString())
						.replace("%block%", mat.toString())));
				break;
			case "BREAKING:PLACEDBLOCK":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREAKING.PlacedBlock")
						.replace("%eventtype%", et.toString())
						.replace("%tool%", tool.toString())
						.replace("%block%", mat.toString())));
				break;
			case "BREAKING:REWARD":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREAKING.CantAccess")
						.replace("%eventtype%", et.toString())
						.replace("%tool%", tool.toString())
						.replace("%block%", mat.toString())));
				break;
			case "INTERACT:RETURN":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.INTERACT.Return")
						.replace("%eventtype%", et.toString())
						.replace("%tool%", mat2.toString())
						.replace("%block%", mat.toString())));
				break;
			case "PLACING:RETURN":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.PLACING.Return")
						.replace("%eventtype%", et.toString())
						.replace("%tool%", tool.toString())
						.replace("%block%", mat.toString())));
				break;
			case "PLACING:REWARD":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.PLACING.Return")
						.replace("%eventtype%", et.toString())
						.replace("%tool%", tool.toString())
						.replace("%block%", mat.toString())));
				break;
			}
		}
	}
}
