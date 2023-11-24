package main.java.me.avankziar.tt.spigot.cmd.tt;

import java.io.IOException;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.objects.sqllite.PlacedBlock;

public class ARGCheckPlacedBlocks extends ArgumentModule
{
	private static TT plugin = BaseConstructor.getPlugin();
	
	public ARGCheckPlacedBlocks(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt checkplacedblocks
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		Block b = getLineOfSight(player, 10, 20);
		if(b == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.CheckPlacedBlocks.NoBlockInSight")));
			return;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(PlacedBlock.wasPlaced(b.getLocation()))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.CheckPlacedBlocks.WasPlaced")));
				} else
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.CheckPlacedBlocks.WasNotPlaced")));
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	private Block getLineOfSight(Player player, int maxDistance, int maxLength) 
	{
        if (maxDistance > 120) 
        {
            maxDistance = 120;
        }
        Block b = null;
        Iterator<Block> itr = new BlockIterator(player, maxDistance);
        while (itr.hasNext()) 
        {
            Block block = itr.next();
            Material material = block.getType();
            if (material != Material.AIR) 
            {
            	b = block;
                break;
            }
        }
        return b;
    }
}