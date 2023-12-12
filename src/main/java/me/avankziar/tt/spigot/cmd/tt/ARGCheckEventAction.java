package main.java.me.avankziar.tt.spigot.cmd.tt;

import java.io.IOException;
import java.util.HashSet;
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
	public static HashSet<UUID> checkEventAction = new HashSet<>();
	
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
				if(checkEventAction.contains(player.getUniqueId()))
				{
					checkEventAction.remove(player.getUniqueId());
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.CheckEventAction.Removed")));
				} else
				{
					checkEventAction.add(player.getUniqueId());
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.CheckEventAction.Added")));
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public static void checkEventAction(Player player, String value, EventType et, ToolType tool, Material mat, EntityType ent, Material mat2)
	{//TODO Die anderen Events hinzufügen
		if(player == null)
		{
			return;
		}
		if(checkEventAction.contains(player.getUniqueId()))
		{
			switch(value)
			{
			default:
				player.sendMessage(ChatApi.tl(value//TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREEDING.Return")
						.replace("%eventtype%", et.toString())));
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
			case "BREEDING:RETURN":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREEDING.Return")
						.replace("%eventtype%", et.toString())
						.replace("%material%", mat2.toString())
						.replace("%entitytype%", ent.toString())));
				break;
			case "BREEDING:CANNOTACCESS":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREEDING.CantAccess")
						.replace("%eventtype%", et.toString())
						.replace("%material%", mat2.toString())
						.replace("%entitytype%", ent.toString())));
				break;
			case "BREEDING:REWARD":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREEDING.CantAccess")
						.replace("%eventtype%", et.toString())
						.replace("%material%", mat2.toString())
						.replace("%entitytype%", ent.toString())));
				break;
			case "BREWING:RETURN":
				player.sendMessage(ChatApi.tl("BREWING:RETURN"//TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREEDING.Return")
						.replace("%eventtype%", et.toString())
						.replace("%material%", mat2.toString())));
				break;
			case "BREWING:REWARD":
				player.sendMessage(ChatApi.tl("BREWING:REWARD"//TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREEDING.CantAccess")
						.replace("%eventtype%", et.toString())
						.replace("%material%", mat2.toString())));
				break;
			case "BUCKET_EMPTYING:RETURN":
				player.sendMessage(ChatApi.tl("BUCKET_EMPTYING:RETURN"//TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREEDING.Return")
						.replace("%eventtype%", et.toString())));
				break;
			case "BUCKET_EMPTYING:CANNOTACCESS":
				player.sendMessage(ChatApi.tl("BUCKET_EMPTYING:CANNOTACCESS"//TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREEDING.CantAccess")
						.replace("%eventtype%", et.toString())));
				break;
			case "BUCKET_EMPTYING:REWARD":
				player.sendMessage(ChatApi.tl("BUCKET_EMPTYING:REWARD"//TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREEDING.CantAccess")
						.replace("%eventtype%", et.toString())));
				break;
			case "BUCKET_FILLING:RETURN":
				player.sendMessage(ChatApi.tl("BUCKET_FILLING:RETURN"//TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREEDING.Return")
						.replace("%eventtype%", et.toString())
						.replace("%material%", mat.toString())));
				break;
			case "BUCKET_FILLING:CANNOTACCESS":
				player.sendMessage(ChatApi.tl("BUCKET_FILLING:CANNOTACCESS"//TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREEDING.Return")
						.replace("%eventtype%", et.toString())
						.replace("%material%", mat.toString())));
				break;
			case "BUCKET_FILLING:REWARD":
				player.sendMessage(ChatApi.tl("BUCKET_FILLING:REWARD"//TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.BREEDING.Return")
						.replace("%eventtype%", et.toString())
						.replace("%material%", mat.toString())));
				break;
			case "COLD_FORGING:RETURN":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.COLD_FORGING.Return")
						.replace("%eventtype%", et.toString())
						.replace("%r%", mat2.toString())));
				break;
			case "COLD_FORGING:CANNOTACCESS":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.COLD_FORGING.CantAccess")
						.replace("%eventtype%", et.toString())
						.replace("%r%", mat2.toString())));
				break;
			case "CRAFTING:RETURN":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.CRAFTING.Return")
						.replace("%eventtype%", et.toString())
						.replace("%r%", mat2.toString())));
				break;
			case "CRAFTING:CANNOTACCESS":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.CRAFTING.CantAccess")
						.replace("%eventtype%", et.toString())
						.replace("%r%", mat2.toString())));
				break;
			case "GRINDING:RETURN":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.GRINDING.Return")
						.replace("%eventtype%", et.toString())
						.replace("%r%", mat2.toString())));
				break;
			case "GRINDING:CANNOTACCESS":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.GRINDING.CantAccess")
						.replace("%eventtype%", et.toString())
						.replace("%r%", mat2.toString())));
				break;
			case "INTERACT:RETURN":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.INTERACT.Return")
						.replace("%eventtype%", et.toString())
						.replace("%tool%", mat2.toString())
						.replace("%block%", mat.toString())));
				break;
			case "INTERACT:BYPASSINTERACT":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.INTERACT.BypassInteraction")
						.replace("%eventtype%", et.toString())
						.replace("%tool%", mat2.toString())
						.replace("%block%", mat.toString())));
				break;
			case "INTERACT:EVENTNOTFOUND":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.INTERACT.EventNotFound")
						.replace("%tool%", mat2.toString())
						.replace("%block%", mat.toString())));
				break;
			case "INTERACT:REWARD":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.INTERACT.Reward")
						.replace("%eventtype%", et.toString())
						.replace("%tool%", mat2.toString())
						.replace("%block%", mat.toString())));
				break;
			case "INTERACTENTITY:RETURN":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.INTERACTENTITY.Return")
						.replace("%eventtype%", et != null ? et.toString() : "null")
						.replace("%tool%", mat2.toString())
						.replace("%entitytype%", ent.toString())));
				break;
			case "INTERACTENTITY:CANNOTACCESS":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.INTERACTENTITY.CantAccess")
						.replace("%eventtype%", et.toString())
						.replace("%tool%", mat2.toString())
						.replace("%entitytype%", ent.toString())));
				break;
			case "INTERACTENTITY:REWARD":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.INTERACTENTITY.Reward")
						.replace("%eventtype%", et.toString())
						.replace("%tool%", mat2.toString())
						.replace("%entitytype%", ent.toString())));
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
			case "SMITHING:RETURN":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.SMITHING.Return")
						.replace("%eventtype%", et.toString())
						.replace("%r%", mat2.toString())));
				break;
			case "SMITHING:CANNOTACCESS":
				player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("Commands.CheckEventAction.SMITHING.CantAccess")
						.replace("%eventtype%", et.toString())
						.replace("%r%", mat2.toString())));
				break;
			}
		}
	}
}
