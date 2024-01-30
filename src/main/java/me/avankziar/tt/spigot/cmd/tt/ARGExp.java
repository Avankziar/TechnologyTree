package main.java.me.avankziar.tt.spigot.cmd.tt;

import java.io.IOException;

import org.bukkit.command.CommandSender;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;

public class ARGExp extends ArgumentModule
{	
	public ARGExp(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt exp ... 
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.OtherCmd")));
	}
}