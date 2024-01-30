package main.java.me.avankziar.tt.spigot.cmd.tt;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;

public class ARGReload extends ArgumentModule
{
	private static Long cooldown = System.currentTimeMillis();
	
	public ARGReload(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt reload
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(cooldown > System.currentTimeMillis())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Reload.Cooldown")));
			return;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Reload.Start")));
		TT.getPlugin().reload();
		addCooldown();
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Reload.End")));
	}
	
	private void addCooldown()
	{
		cooldown = System.currentTimeMillis()+1000L*20;
	}
}
