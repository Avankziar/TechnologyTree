package main.java.me.avankziar.tt.spigot.listener.recipe;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.handler.BlockHandler;
import main.java.me.avankziar.tt.spigot.handler.BlockHandler.BlockType;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class RegisterBlockListener implements Listener
{
	private TT plugin = BaseConstructor.getPlugin();
	
	//Here no active Event check. It muss be active always!
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteractBlockToCheckRegister(PlayerInteractEvent event)
	{
		if(event.useInteractedBlock() == Result.DENY
				|| event.useItemInHand() == Result.DENY
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| event.getClickedBlock() == null)
		{
			return;
		}
		TT.log.info("InteractBlockToCheckRegister Start"); //REMOVEME
		Location loc = event.getClickedBlock().getLocation();
		BlockType bt = BlockHandler.getBlockType(event.getClickedBlock().getType());
		if(bt == BlockType.UNKNOW)
		{
			TT.log.info("InteractBlockToCheckRegister BlockType Unknow Type:"+event.getClickedBlock().getType()); //REMOVEME
			return;
		}
		if(!RewardHandler.canAccessInteraction(event.getPlayer(),
				ToolType.HAND, 
				EventType.INTERACT,
				event.getClickedBlock().getType(), null))
		{
			event.setCancelled(true);
			return;
		}
		if(!BlockHandler.canRegisterBlock(event.getPlayer(), bt))
		{
			return;
		}
		if(BlockHandler.isAlreadyRegisteredBlock(event.getPlayer().getUniqueId(), bt, loc))
		{
			return;
		} else
		{
			if(BlockHandler.isAlreadyRegisteredBlock(bt, loc))
			{
				if(new ConfigHandler().overrideAlreadyRegisteredBlocks())
				{
					BlockHandler.deRegisterBlock(bt, loc, true);
					BlockHandler.registerBlock(event.getPlayer(), bt, loc, true);
					event.getPlayer().spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getLang().getString("BlockHandler.Event.OverrideRegisterBlock")));
					return;
				}
				event.setCancelled(true);
				event.setUseInteractedBlock(Result.DENY);
				event.setUseItemInHand(Result.DENY);
				event.getPlayer().spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getLang().getString("BlockHandler.Event.ThirdPartyRegistered")));
			} else
			{
				BlockHandler.registerBlock(event.getPlayer(), bt, loc, true);
				event.getPlayer().spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getLang().getString("BlockHandler.Event.NewRegisterBlock")));
			}
		}		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRegisterBlockBreak(BlockBreakEvent event)
	{
		if(event.isCancelled()
				|| event.getBlock() == null)
		{
			return;
		}
		final Block block = event.getBlock();
		BlockType bt = BlockHandler.getBlockType(block.getType());
		if(bt == BlockType.UNKNOW)
		{
			return;
		}
		if(!BlockHandler.isAlreadyRegisteredBlock(bt, block.getLocation()))
		{
			return;
		}
		BlockHandler.deRegisterBlock(bt, block.getLocation(), true);
		event.getPlayer().spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("BlockHandler.Event.DeregisterBlock")));
	}
}