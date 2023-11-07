package main.java.me.avankziar.tt.spigot.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandConstructor;
import main.java.me.avankziar.tt.spigot.gui.objects.GuiType;
import main.java.me.avankziar.tt.spigot.handler.GuiHandler;

public class TechGuiCommandExecutor implements CommandExecutor
{
	private TT plugin;
	private static CommandConstructor cc;
	
	public TechGuiCommandExecutor(TT plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		TechGuiCommandExecutor.cc = cc;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(cc == null)
		{
			return false;
		}
		if (!(sender instanceof Player)) 
		{
			plugin.getLogger().info("Cmd is only for Player!");
			return false;
		}
		Player player = (Player) sender;
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				GuiHandler.openCatOrTech(player, GuiType.START, null, null, null, null, true);
			}
		}.runTaskAsynchronously(plugin);
		return true;
	}
}