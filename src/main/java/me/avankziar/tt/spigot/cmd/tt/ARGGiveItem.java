package main.java.me.avankziar.tt.spigot.cmd.tt;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.MatchApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.ifh.ItemGenerator;

public class ARGGiveItem extends ArgumentModule
{	
	public ARGGiveItem(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt giveitem <Itemname> <amount>
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		final String item = args[1];
		final String amount = args[2];
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(!MatchApi.isInteger(amount))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber").replace("%value%", amount)));
					return;
				}
				ItemStack is = new ItemGenerator().generateItem(
						player, plugin.getYamlHandler().getItemGenerators().get(item), item, Integer.parseInt(amount));
				if(is == null)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.GiveItem.ItemNotExist")));
					return;
				}
				HashMap<Integer, ItemStack> map = player.getInventory().addItem(is);
				if(!map.isEmpty())
				{
					for(ItemStack i : map.values())
					{
						player.getWorld().dropItem(player.getLocation(), i);
					}
				}
			}
		}.runTaskAsynchronously(plugin);
	}
}