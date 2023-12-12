package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.database.SQLiteHandler;
import main.java.me.avankziar.tt.spigot.handler.BlockHandler;
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
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "BREAKING:RETURN",
					BR, ToolType.getHandToolType(event.getPlayer()), event.getBlock().getType(), null, null);
			return;
		}
		if(!ConfigHandler.GAMERULE_UseVanillaExpDrops)
		{
			event.setExpToDrop(0);
		}
		if(RewardHandler.useTTDropMechanicCalculation(event.getPlayer()))
		{
			event.setDropItems(false);
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
				if(!RewardHandler.canAccessInteraction(player,
						ToolType.getToolType(player.getInventory().getItemInMainHand().getType()), BR, blockType, null))
				{
					ARGCheckEventAction.checkEventAction(event.getPlayer(), "BREAKING:CANNOTACCESS",
							BR, ToolType.getHandToolType(event.getPlayer()), event.getBlock().getType(), null, null);
					if(TRACK_PLACED_BLOCKS)
					{
						if(PlacedBlock.wasPlaced(loc) && !REWARD_IF_MANUALLY_PLACED_BEFORE)
						{
							PlacedBlock.delete(loc);
						}
					}
					return;
				}
				if(TRACK_PLACED_BLOCKS)
				{
					if(PlacedBlock.wasPlaced(loc) && !REWARD_IF_MANUALLY_PLACED_BEFORE)
					{
						ARGCheckEventAction.checkEventAction(event.getPlayer(), "BREAKING:PLACEDBLOCK",
								BR, ToolType.getHandToolType(event.getPlayer()), event.getBlock().getType(), null, null);
						PlacedBlock.delete(loc);
						for(ItemStack is : RewardHandler.getDrops(player, BR, tool, blockType, null))
						{
							ItemHandler.dropItem(is, player, loc);
						}
						return;
					}
				}
				ARGCheckEventAction.checkEventAction(event.getPlayer(), "BREAKING:REWARD",
						BR, ToolType.getHandToolType(event.getPlayer()), event.getBlock().getType(), null, null);
				for(ItemStack is : RewardHandler.getDrops(player, BR, tool, blockType, null))
				{
					ItemHandler.dropItem(is, player, loc);
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
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "PLACING:RETURN",
					PL, ToolType.HAND, event.getBlock().getType(), null, null);
			return;
		}
		final Player player = event.getPlayer();
		final UUID uuid = player.getUniqueId();
		final Material blockType = event.getBlock().getType();
		final ToolType tool = ToolType.HAND;
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
				ARGCheckEventAction.checkEventAction(event.getPlayer(), "PLACING:REWARD",
						PL, ToolType.getHandToolType(event.getPlayer()), event.getBlock().getType(), null, null);
				for(ItemStack is : RewardHandler.getDrops(player, PL, tool, blockType, null))
				{
					ItemHandler.dropItem(is, player, loc);
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
	public void onInteractWithBlocks(PlayerInteractEvent event)
	{
		if(event.getClickedBlock() == null
				|| event.getAction() == Action.LEFT_CLICK_AIR
				|| event.getAction() == Action.RIGHT_CLICK_AIR
				|| event.getAction() == Action.LEFT_CLICK_BLOCK
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR)
		{
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "INTERACT:RETURN",
				EventType.INTERACT, ToolType.HAND, event.getClickedBlock() != null ? event.getClickedBlock().getType() : Material.AIR, null,
				event.getPlayer().getInventory().getItemInMainHand() != null ? event.getPlayer().getInventory().getItemInMainHand().getType() : Material.AIR);
			return;
		}
		final EventType et = isAppropiateTool(
				event.getPlayer().getInventory().getItemInMainHand() != null 
					? event.getPlayer().getInventory().getItemInMainHand().getType() 
					: Material.AIR,
				event.getClickedBlock() != null
					? event.getClickedBlock().getType()
					: Material.AIR);
		final Material mat = getAppropiateTool(
				event.getPlayer().getInventory().getItemInMainHand() != null 
					? event.getPlayer().getInventory().getItemInMainHand().getType() 
					: Material.AIR,
				event.getClickedBlock() != null
					? event.getClickedBlock().getType()
					: Material.AIR);
		if(byPassInteraction(event.getClickedBlock()))
		{
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "INTERACT:BYPASSINTERACT",
					EventType.INTERACT, ToolType.HAND, event.getClickedBlock() != null ? event.getClickedBlock().getType() : Material.AIR, null,
					event.getPlayer().getInventory().getItemInMainHand() != null ? event.getPlayer().getInventory().getItemInMainHand().getType() : Material.AIR);
			return;
		} else
		{
			if(!BlockHandler.bypassAccessIfGamerule(event.getClickedBlock().getType()))
			{
				if(!RewardHandler.canAccessInteraction(event.getPlayer(), ToolType.HAND, et, mat, null))
				{
					ARGCheckEventAction.checkEventAction(event.getPlayer(), et.toString()+":CANNOTACCESS",
							et, ToolType.HAND, mat, null, Material.AIR);
					event.getPlayer().spigot().sendMessage(ChatApi.tctl(
							TT.getPlugin().getYamlHandler().getLang().getString("BlockHandler.Event.DontKnowToInteract")));
					event.setCancelled(true);
					return;
				}
			}			
		}
		
		if(et == null)
		{
			/*ARGCheckEventAction.checkEventAction(event.getPlayer(), "INTERACT:EVENTNOTFOUND",
					null, ToolType.HAND, event.getClickedBlock() != null ? event.getClickedBlock().getType() : Material.AIR, null,
					event.getPlayer().getInventory().getItemInMainHand() != null ? event.getPlayer().getInventory().getItemInMainHand().getType() : Material.AIR);*/
			return;
		}
		final Block block = event.getClickedBlock();
		final Player player = event.getPlayer();
		final UUID uuid = player.getUniqueId();
		final ToolType tool = ToolType.getHandToolType(player);
		final Location loc = event.getClickedBlock().getLocation();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(event.getPlayer(), "INTERACT:REWARD",
						et, ToolType.HAND, block != null ? block.getType() : Material.AIR, null, mat);
				for(ItemStack is : RewardHandler.getDrops(player, et, tool, mat, null))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(uuid, et, tool, mat, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
	
	public static boolean byPassInteraction(Block block)
	{
		if(block.getState() instanceof Sign
				|| block.getState() instanceof Bed
				|| block.getState().getBlockData() instanceof Gate
				|| block.getState().getBlockData() instanceof Door
				|| block.getState().getBlockData() instanceof TrapDoor)
		{
			return true;
		}
		switch(block.getType())
		{
		default:
			return false;
		case CHEST:
		case CHEST_MINECART:
		case TRAPPED_CHEST:
		case BARREL:
		case NOTE_BLOCK:
		case JUKEBOX:
		case CHISELED_BOOKSHELF:
		case LECTERN:
		case TRIPWIRE:
		case TARGET:
		case LODESTONE:
			return true;
		}
	}
	
	public EventType isAppropiateTool(Material toolmat, Material blockmat)
	{
		switch(blockmat)
		{
		default:
			switch(toolmat)
			{
			default:
				return EventType.INTERACT;
			case AXOLOTL_BUCKET:
			case COD_BUCKET:
			case PUFFERFISH_BUCKET:
			case SALMON_BUCKET:
			case TADPOLE_BUCKET:
			case TROPICAL_FISH_BUCKET:
			case LAVA_BUCKET:
			case WATER_BUCKET:
			case POWDER_SNOW_BUCKET:
				return EventType.BUCKET_EMPTYING;
			}
		case COMPOSTER:
			switch(toolmat)
			{
			default:
				return EventType.INTERACT;
			case OAK_SAPLING:
			case ACACIA_SAPLING:
			case BAMBOO_SAPLING:
			case BIRCH_SAPLING:
			case CHERRY_SAPLING:
			case DARK_OAK_SAPLING:
			case JUNGLE_SAPLING:
			case SPRUCE_SAPLING:
			case BEETROOT_SEEDS:
			case MELON_SEEDS:
			case PUMPKIN_SEEDS:
			case TORCHFLOWER_SEEDS:
			case WHEAT_SEEDS:
			case GRASS:
			case HANGING_ROOTS:
			case CAVE_VINES_PLANT:
			case CHORUS_PLANT:
			case KELP_PLANT:
			case PITCHER_PLANT:
			case TWISTING_VINES_PLANT:
			case WEEPING_VINES_PLANT:
			case ACACIA_LEAVES:
			case AZALEA_LEAVES:
			case BIRCH_LEAVES:
			case CHERRY_LEAVES:
			case DARK_OAK_LEAVES:
			case FLOWERING_AZALEA_LEAVES:
			case JUNGLE_LEAVES:
			case MANGROVE_LEAVES:
			case OAK_LEAVES:
			case SPRUCE_LEAVES:
			case MOSS_BLOCK:
			case MOSS_CARPET:
			case SMALL_DRIPLEAF:
			case BIG_DRIPLEAF:
			case PITCHER_POD:
			case SWEET_BERRIES:
			case GLOW_BERRIES:
			case PINK_PETALS:
			case SEAGRASS:
			case TALL_SEAGRASS:
			case KELP:
			case DRIED_KELP:
			case DRIED_KELP_BLOCK:
			case CACTUS:
			case MELON:
			case MELON_SLICE:
			case NETHER_SPROUTS:
			case VINE:
			case CAVE_VINES:
			case TWISTING_VINES:
			case WEEPING_VINES:
			case SUGAR_CANE:
			case APPLE:
			case AZALEA:
			case FLOWERING_AZALEA:
			case FERN:
			case LARGE_FERN:
			case DANDELION:
			case POPPY:
			case BLUE_ORCHID:
			case AZURE_BLUET:
			case ORANGE_TULIP:
			case PINK_TULIP:
			case RED_TULIP:
			case WHITE_TULIP:
			case OXEYE_DAISY:
			case CORNFLOWER:
			case LILY_OF_THE_VALLEY:
			case WITHER_ROSE:
			case TORCHFLOWER:
			case SUNFLOWER:
			case LILAC:
			case ROSE_BUSH:
			case PEONY:
			case COCOA_BEANS:
			case CARROT:
			case POTATO:
			case CRIMSON_FUNGUS:
			case WARPED_FUNGUS:
			case BEETROOT:
			case CRIMSON_ROOTS:
			case WARPED_ROOTS:
			case PUMPKIN:
			case CARVED_PUMPKIN:
			case SEA_PICKLE:
			case BROWN_MUSHROOM:
			case RED_MUSHROOM:
			case SHROOMLIGHT:
			case LILY_PAD:
			case SPORE_BLOSSOM:
			case BREAD:
			case COOKIE:
			case NETHER_WART:
			case NETHER_WART_BLOCK:
			case BAKED_POTATO:
			case MUSHROOM_STEM:
			case BROWN_MUSHROOM_BLOCK:
			case RED_MUSHROOM_BLOCK:
			case WHEAT:
			case HAY_BLOCK:
			case WARPED_WART_BLOCK:
			case CAKE:
			case PUMPKIN_PIE:
				return EventType.COMPOSTING;
			}
		case POWDER_SNOW_CAULDRON:
		case LAVA_CAULDRON:
		case WATER_CAULDRON:
			switch(toolmat)
			{
			default:
				return EventType.INTERACT;
			case BUCKET:
				return EventType.BUCKET_FILLING;
			}
		case DIRT:
		case GRASS_BLOCK:
			switch(toolmat)
			{
				default:
					return EventType.INTERACT;
				case DIAMOND_SHOVEL:
				case GOLDEN_SHOVEL:
				case IRON_SHOVEL:
				case NETHERITE_SHOVEL:
				case STONE_SHOVEL:
				case WOODEN_SHOVEL:
					return EventType.CREATE_PATH;
				case BONE_MEAL:
					return EventType.FERTILIZING;
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
				return EventType.INTERACT;
			case DIAMOND_AXE:
			case GOLDEN_AXE:
			case IRON_AXE:
			case NETHERITE_AXE:
			case STONE_AXE:
			case WOODEN_AXE:
				return EventType.DEBARKING;
			}
		case TNT:
		case TNT_MINECART:
			switch(toolmat)
			{
			default:
				return EventType.INTERACT;
			case FLINT_AND_STEEL:
				return EventType.IGNITING;
			}
		case OAK_SAPLING:
		case ACACIA_SAPLING:
		case BAMBOO_SAPLING:
		case BIRCH_SAPLING:
		case CHERRY_SAPLING:
		case DARK_OAK_SAPLING:
		case JUNGLE_SAPLING:
		case SPRUCE_SAPLING:
		case BEETROOTS:
		case MELON_STEM:
		case PUMPKIN_STEM:
		case TORCHFLOWER_SEEDS:
		case WHEAT:
		case GRASS:
		case FERN:
		case CARROTS:
		case POTATOES:
		case AZALEA_LEAVES:
		case FLOWERING_AZALEA_LEAVES:
		case MOSS_BLOCK:
		case BROWN_MUSHROOM:
		case RED_MUSHROOM:
		case PINK_PETALS:
		case TWISTING_VINES:
		case WEEPING_VINES:
		case BIG_DRIPLEAF:
		case BIG_DRIPLEAF_STEM:
		case GLOW_LICHEN:
		case COCOA:
		case CAVE_VINES:
		case SEAGRASS:
		case SEA_PICKLE:
		case KELP:
		case KELP_PLANT:
		case OCHRE_FROGLIGHT:
		case PEARLESCENT_FROGLIGHT:
		case VERDANT_FROGLIGHT:
		case SUNFLOWER:
		case LILAC:
		case ROSE_BUSH:
		case PEONY:
			switch(toolmat)
			{
			default:
				return EventType.INTERACT;
			case BONE_MEAL:
				return EventType.FERTILIZING;
			}
		}
	}
	
	public Material getAppropiateTool(Material toolmat, Material blockmat)
	{
		switch(blockmat)
		{
		default:
			switch(toolmat)
			{
			default:
				return blockmat;
			case AXOLOTL_BUCKET:
			case COD_BUCKET:
			case PUFFERFISH_BUCKET:
			case SALMON_BUCKET:
			case TADPOLE_BUCKET:
			case TROPICAL_FISH_BUCKET:
			case LAVA_BUCKET:
			case WATER_BUCKET:
			case POWDER_SNOW_BUCKET:
				return toolmat;
			}
		case COMPOSTER:
			switch(toolmat)
			{
			default:
				return null;
			case OAK_SAPLING:
			case ACACIA_SAPLING:
			case BAMBOO_SAPLING:
			case BIRCH_SAPLING:
			case CHERRY_SAPLING:
			case DARK_OAK_SAPLING:
			case JUNGLE_SAPLING:
			case SPRUCE_SAPLING:
			case BEETROOT_SEEDS:
			case MELON_SEEDS:
			case PUMPKIN_SEEDS:
			case TORCHFLOWER_SEEDS:
			case WHEAT_SEEDS:
			case GRASS:
			case HANGING_ROOTS:
			case CAVE_VINES_PLANT:
			case CHORUS_PLANT:
			case KELP_PLANT:
			case PITCHER_PLANT:
			case TWISTING_VINES_PLANT:
			case WEEPING_VINES_PLANT:
			case ACACIA_LEAVES:
			case AZALEA_LEAVES:
			case BIRCH_LEAVES:
			case CHERRY_LEAVES:
			case DARK_OAK_LEAVES:
			case FLOWERING_AZALEA_LEAVES:
			case JUNGLE_LEAVES:
			case ACACIA_BOAT:
			case MANGROVE_LEAVES:
			case OAK_LEAVES:
			case SPRUCE_LEAVES:
			case MOSS_BLOCK:
			case MOSS_CARPET:
			case SMALL_DRIPLEAF:
			case BIG_DRIPLEAF:
			case PITCHER_POD:
			case SWEET_BERRIES:
			case GLOW_BERRIES:
			case PINK_PETALS:
			case SEAGRASS:
			case TALL_SEAGRASS:
			case KELP:
			case DRIED_KELP:
			case DRIED_KELP_BLOCK:
			case CACTUS:
			case MELON:
			case MELON_SLICE:
			case NETHER_SPROUTS:
			case VINE:
			case CAVE_VINES:
			case TWISTING_VINES:
			case WEEPING_VINES:
			case SUGAR_CANE:
			case APPLE:
			case AZALEA:
			case FLOWERING_AZALEA:
			case FERN:
			case LARGE_FERN:
			case DANDELION:
			case POPPY:
			case BLUE_ORCHID:
			case AZURE_BLUET:
			case ORANGE_TULIP:
			case PINK_TULIP:
			case RED_TULIP:
			case WHITE_TULIP:
			case OXEYE_DAISY:
			case CORNFLOWER:
			case LILY_OF_THE_VALLEY:
			case WITHER_ROSE:
			case TORCHFLOWER:
			case SUNFLOWER:
			case LILAC:
			case ROSE_BUSH:
			case PEONY:
			case COCOA_BEANS:
			case CARROT:
			case POTATO:
			case CRIMSON_FUNGUS:
			case WARPED_FUNGUS:
			case BEETROOT:
			case CRIMSON_ROOTS:
			case WARPED_ROOTS:
			case PUMPKIN:
			case CARVED_PUMPKIN:
			case SEA_PICKLE:
			case BROWN_MUSHROOM:
			case RED_MUSHROOM:
			case SHROOMLIGHT:
			case LILY_PAD:
			case SPORE_BLOSSOM:
			case BREAD:
			case COOKIE:
			case NETHER_WART:
			case NETHER_WART_BLOCK:
			case BAKED_POTATO:
			case MUSHROOM_STEM:
			case BROWN_MUSHROOM_BLOCK:
			case RED_MUSHROOM_BLOCK:
			case WHEAT:
			case HAY_BLOCK:
			case WARPED_WART_BLOCK:
			case CAKE:
			case PUMPKIN_PIE:
				return toolmat;
			}
		case POWDER_SNOW_CAULDRON:
		case LAVA_CAULDRON:
		case WATER_CAULDRON:
			switch(toolmat)
			{
			default:
				return null;
			case BUCKET:
				return blockmat;
			}
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
					return blockmat;
				case BONE_MEAL:
					return blockmat;
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
				return blockmat;
			}
		case TNT:
		case TNT_MINECART:
			switch(toolmat)
			{
			default:
				return null;
			case FLINT_AND_STEEL:
				return blockmat;
			}
		case OAK_SAPLING:
		case ACACIA_SAPLING:
		case BAMBOO_SAPLING:
		case BIRCH_SAPLING:
		case CHERRY_SAPLING:
		case DARK_OAK_SAPLING:
		case JUNGLE_SAPLING:
		case SPRUCE_SAPLING:
		case BEETROOTS:
		case MELON_STEM:
		case PUMPKIN_STEM:
		case TORCHFLOWER_SEEDS:
		case WHEAT:
		case GRASS:
		case FERN:
		case CARROTS:
		case POTATOES:
		case AZALEA_LEAVES:
		case FLOWERING_AZALEA_LEAVES:
		case MOSS_BLOCK:
		case BROWN_MUSHROOM:
		case RED_MUSHROOM:
		case PINK_PETALS:
		case TWISTING_VINES:
		case WEEPING_VINES:
		case BIG_DRIPLEAF:
		case BIG_DRIPLEAF_STEM:
		case GLOW_LICHEN:
		case COCOA:
		case CAVE_VINES:
		case SEAGRASS:
		case SEA_PICKLE:
		case KELP:
		case KELP_PLANT:
		case OCHRE_FROGLIGHT:
		case PEARLESCENT_FROGLIGHT:
		case VERDANT_FROGLIGHT:
		case SUNFLOWER:
		case LILAC:
		case ROSE_BUSH:
		case PEONY:
		case CRIMSON_NYLIUM:
		case WARPED_NYLIUM:
			switch(toolmat)
			{
			default:
				return null;
			case BONE_MEAL:
				return blockmat;
			}
		}
	}
}