package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class BreakPlaceListener implements Listener
{
	private static boolean USE_METADATA = new ConfigHandler().useMetaDataToTrackPlayerPlacedBlocks();
	private static boolean REWARD_IF_MANUALLY_PLACED_BEFORE = new ConfigHandler().ifBlockIsManuallyPlacedBefore_RewardItByBreaking();
	private static String IS_PLACED_MANUALLY = BaseConstructor.getPlugin().pluginName+":"+"IsPlacedManually";
	
	@EventHandler
	public void onBreak(BlockBreakEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(EventType.BREAKING))
		{
			return;
		}
		event.setExpToDrop(0);
		event.setDropItems(false);
		if(!RewardHandler.canAccessInteraction(event.getPlayer(), EventType.BREAKING, event.getBlock().getType(), null))
		{
			return;
		}
		if(USE_METADATA && event.getBlock().hasMetadata(IS_PLACED_MANUALLY))
		{
			if(!REWARD_IF_MANUALLY_PLACED_BEFORE)
			{
				return;
			}
		}
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), EventType.BREAKING, event.getBlock().getType(), null, true))
		{
			Item it = event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), EventType.BREAKING, event.getBlock().getType(), null, 1);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(EventType.PLACING))
		{
			return;
		}
		if(USE_METADATA)
		{
			event.getBlock().setMetadata(IS_PLACED_MANUALLY, new FixedMetadataValue(BaseConstructor.getPlugin(), true));
		}
		for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), EventType.PLACING, event.getBlock().getType(), null, false))
		{
			Item it = event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), is);
			ItemHandler.addItemToTask(it, event.getPlayer().getUniqueId());
		}
		RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), EventType.PLACING, event.getBlock().getType(), null, 1);
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
}