package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.database.SQLiteHandler;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;
import main.java.me.avankziar.tt.spigot.objects.sqllite.PlacedBlock;

public class BreakPlaceInteractListener implements Listener
{
	private static boolean TRACK_PLACED_BLOCKS = new ConfigHandler().trackPlacedBlocks();
	private static boolean REWARD_IF_MANUALLY_PLACED_BEFORE = new ConfigHandler().ifBlockIsManuallyPlacedBefore_RewardItByBreaking();
	private static long EXPIRATION_DATE = 0;
	final private static EventType BR = EventType.BREAKING;
	final private static EventType PL = EventType.PLACING;
	
	static
	{
		String parse = new ConfigHandler().placedBlocksExpirationDate();
		String[] split = parse.split("-");
		if(split.length == 4)
		{
			if(split[0].endsWith("d"))
			{
				EXPIRATION_DATE += Long.valueOf(split[0].substring(0, split[0].length()-1)) * 24 * 60 * 60 * 1000;
			}
			if(split[1].endsWith("H"))
			{
				EXPIRATION_DATE += Long.valueOf(split[1].substring(0, split[1].length()-1)) * 60 * 60 * 1000;
			}
			if(split[2].endsWith("m"))
			{
				EXPIRATION_DATE += Long.valueOf(split[2].substring(0, split[2].length()-1)) * 60 * 1000;
			}
			if(split[3].endsWith("s"))
			{
				EXPIRATION_DATE += Long.valueOf(split[3].substring(0, split[3].length()-1)) * 1000;
			}
		}
	}
	
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
		final Player player = event.getPlayer();
		final UUID uuid = player.getUniqueId();
		final Material blockType = event.getBlock().getType();
		final ToolType tool = ToolType.getHandToolType(player);
		final Location loc = event.getBlock().getLocation();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(!RewardHandler.canAccessInteraction(player,
						ToolType.getToolType(player.getInventory().getItemInMainHand().getType()), BR, blockType, null))
				{
					return;
				}
				if(TRACK_PLACED_BLOCKS)
				{
					if(PlacedBlock.wasPlaced(loc) && !REWARD_IF_MANUALLY_PLACED_BEFORE)
					{
						return;
					}
				}
				for(ItemStack is : RewardHandler.getDrops(player, BR, tool, blockType, null))
				{
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							Item it = event.getPlayer().getWorld().dropItem(loc, is);
							ItemHandler.addItemToTask(it, uuid);
						}
					}.runTask(TT.getPlugin());
				}
				RewardHandler.rewardPlayer(uuid, BR, tool, blockType, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
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
		final Player player = event.getPlayer();
		final UUID uuid = player.getUniqueId();
		final Material blockType = event.getBlock().getType();
		final ToolType tool = ToolType.getHandToolType(player);
		final Location loc = event.getBlock().getLocation();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(TRACK_PLACED_BLOCKS)
				{
					TT.getPlugin().getSQLLiteHandler().create(SQLiteHandler.Type.PLACEDBLOCKS,
							new PlacedBlock(0,
									loc.getWorld().getName(),
									loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
									System.currentTimeMillis()+EXPIRATION_DATE));
				}
				for(ItemStack is : RewardHandler.getDrops(player, PL, tool, blockType, null))
				{
					new BukkitRunnable()
					{
						
						@Override
						public void run()
						{
							Item it = event.getPlayer().getWorld().dropItem(loc, is);
							ItemHandler.addItemToTask(it, uuid);
						}
					}.runTask(TT.getPlugin());
				}
				RewardHandler.rewardPlayer(uuid, PL, tool, blockType, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
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
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR)
		{
			return;
		}
		final EventType et = isAppropiateTool(
				event.getPlayer().getInventory().getItemInMainHand() != null 
					? event.getPlayer().getInventory().getItemInMainHand().getType() 
					: Material.AIR,
				event.getClickedBlock() != null
					? event.getClickedBlock().getType()
					: Material.AIR);
		if(et == null)
		{
			return;
		}
		final Player player = event.getPlayer();
		final UUID uuid = player.getUniqueId();
		final Material blockType = event.getClickedBlock().getType();
		final ToolType tool = ToolType.getHandToolType(player);
		final Location loc = event.getClickedBlock().getLocation();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(ItemStack is : RewardHandler.getDrops(player, et, tool, blockType, null))
				{
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							Item it = event.getPlayer().getWorld().dropItem(loc, is);
							ItemHandler.addItemToTask(it, uuid);
						}
					}.runTask(TT.getPlugin());
				}
				RewardHandler.rewardPlayer(uuid, et, tool, blockType, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
	
	public EventType isAppropiateTool(Material toolmat, Material blockmat)
	{
		switch(blockmat)
		{
		default:
			return null;
		case DIRT:
		case GRASS_BLOCK:
			switch(toolmat)
			{
				default:
					return null;
				case DIAMOND_SHOVEL:
				case GOLDEN_SHOVEL:
				case IRON_SHOVEL:
				case NETHERITE_SHOVEL:
				case STONE_SHOVEL:
				case WOODEN_SHOVEL:
					return EventType.CREATE_PATH;
			}
		case ACACIA_LOG:
		case BIRCH_LOG:
		case CHERRY_LOG:
		case DARK_OAK_LOG:
		case JUNGLE_LOG:
		case MANGROVE_LOG:
		case OAK_LOG:
		case SPRUCE_LOG:
		case ACACIA_WOOD:
		case BIRCH_WOOD:
		case CHERRY_WOOD:
		case DARK_OAK_WOOD:
		case JUNGLE_WOOD:
		case MANGROVE_WOOD:
		case OAK_WOOD:
		case SPRUCE_WOOD:
		case CRIMSON_HYPHAE:
		case WARPED_HYPHAE:
			switch(toolmat)
			{
			default:
				return null;
			case DIAMOND_AXE:
			case GOLDEN_AXE:
			case IRON_AXE:
			case NETHERITE_AXE:
			case STONE_AXE:
			case WOODEN_AXE:
				return EventType.DEBARKING;
			}
		}
	}
}