package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class BreakPlaceInteractListener implements Listener
{
	private static boolean USE_METADATA = new ConfigHandler().useMetaDataToTrackPlayerPlacedBlocks();
	private static boolean REWARD_IF_MANUALLY_PLACED_BEFORE = new ConfigHandler().ifBlockIsManuallyPlacedBefore_RewardItByBreaking();
	private static String IS_PLACED_MANUALLY = BaseConstructor.getPlugin().pluginName+":"+"IsPlacedManually";
	final private static EventType BR = EventType.BREAKING;
	final private static EventType PL = EventType.PLACING;
	final private static EventType IA = EventType.INTERACT;
	
	@EventHandler
	public void onBreak(BlockBreakEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(BR))
		{
			return;
		}
		event.setExpToDrop(0);
		event.setDropItems(false);
		if(USE_METADATA && event.getBlock().hasMetadata(IS_PLACED_MANUALLY)) //MetaDaten ins Mysql versetzten.
		{
			if(!REWARD_IF_MANUALLY_PLACED_BEFORE)
			{
				return;
			}
		}
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), BR, event.getBlock().getType(), null, true))
		{
			Item it = event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), BR, event.getBlock().getType(), null, 1);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event)
	{
		if(event.isCancelled()
				|| !event.canBuild()
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(PL))
		{
			return;
		}
		if(USE_METADATA)
		{
			//TODO Hier die wachsenden Block (Weizen etc.) beachten. Da sonst beim Abbau nicht vergütet wird.
			event.getBlock().setMetadata(IS_PLACED_MANUALLY, new FixedMetadataValue(BaseConstructor.getPlugin(), true));
		}
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), PL, event.getBlockPlaced().getType(), null, false))
		{
			Item it = event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), PL, event.getBlockPlaced().getType(), null, 1);
	}
	
	/*Do not needed. Do it in BlockBreakEvent
	@EventHandler
	public void onDropItem(BlockDropItemEvent event)
	{
		
		
	}*/
	
	/*Do not needed...	
	@EventHandler
	public void onExp(BlockExpEvent event)
	{
		
	}*/
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.LEFT_CLICK_AIR
				|| event.getAction() == Action.RIGHT_CLICK_AIR
				|| event.getAction() == Action.PHYSICAL
				|| event.getClickedBlock() == null
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(IA))
		{
			return;
		}
		//ADDME Hier noch die Logik
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), IA, event.getClickedBlock().getType(), null, true))
		{
			Item it = event.getPlayer().getWorld().dropItem(event.getClickedBlock().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), IA, event.getClickedBlock().getType(), null, 1);
	}
}