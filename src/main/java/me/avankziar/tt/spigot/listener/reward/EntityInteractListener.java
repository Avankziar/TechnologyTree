package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class EntityInteractListener implements Listener
{	
	@EventHandler
	public void onInteractWithEntity(PlayerInteractEntityEvent event)
	{
		final EventType et = getEventPerEntity(event.getPlayer().getInventory().getItemInMainHand() != null
				? event.getPlayer().getInventory().getItemInMainHand().getType()
				: null, event.getRightClicked().getType());
		if(et == null 
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(et))
		{
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "INTERACTENTITY:RETURN",
					et, ToolType.HAND, null, event.getRightClicked().getType(),
					event.getPlayer().getInventory().getItemInMainHand() != null
					? event.getPlayer().getInventory().getItemInMainHand().getType()
					: null);
			return;
		}
		if(event.getRightClicked() instanceof Breedable)
		{
			Breedable br = (Breedable) event.getRightClicked();
			if(et == EventType.BREEDING && !br.canBreed())
			{
				event.setCancelled(true);
				return;
			}
		}
		if(!RewardHandler.canAccessInteraction(event.getPlayer(),
				ToolType.HAND, et, null, event.getRightClicked().getType()))
		{
			event.setCancelled(true);
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "INTERACTENTITY:CANNOTACCESS",
					et, ToolType.HAND, null, event.getRightClicked().getType(),
					event.getPlayer().getInventory().getItemInMainHand() != null
					? event.getPlayer().getInventory().getItemInMainHand().getType()
					: null);
			return;
		}
		final Player player = event.getPlayer();
		final Location loc = event.getRightClicked().getLocation();
		final EntityType ent = event.getRightClicked().getType();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(event.getPlayer(), "INTERACTENTITY:REWARD",
						EventType.BREEDING, ToolType.HAND, null, event.getRightClicked().getType(),
						event.getPlayer().getInventory().getItemInMainHand() != null
						? event.getPlayer().getInventory().getItemInMainHand().getType()
						: null);
				for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), et, ToolType.HAND, null, ent))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(event.getPlayer().getUniqueId(), et, ToolType.HAND, null, ent, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
	
	public EventType getEventPerEntity(Material toolmat, EntityType ent)
	{
		if(toolmat == null)
		{
			return EventType.INTERACT;
		}
		switch(ent)
		{
		default:
			return null;
		case AXOLOTL:
			switch(toolmat)
			{
			default:
				return null;
			case TROPICAL_FISH_BUCKET:
				return EventType.BREEDING;
			}
		case BEE:
			switch(toolmat)
			{
			default:
				return null;
			case DANDELION:
			case POPPY:
			case BLUE_ORCHID:
			case AZURE_BLUET:
			case ORANGE_TULIP:
			case WHITE_TULIP:
			case PINK_TULIP:
			case OXEYE_DAISY:
			case CORNFLOWER:
			case LILY_OF_THE_VALLEY:
			case WITHER_ROSE:
			case TORCHFLOWER:
			case SUNFLOWER:
			case LILAC:
			case ROSE_BUSH:
			case PEONY:
			case PITCHER_PLANT:
				return EventType.BREEDING;
			}
		case CAMEL:
			switch(toolmat)
			{
			default:
				return null;
			case CACTUS:
				return EventType.BREEDING;
			}
		case FROG:
			switch(toolmat)
			{
			default:
				return null;
			case SLIME_BALL:
				return EventType.BREEDING;
			}
		case FOX:
			switch(toolmat)
			{
			default:
				return null;
			case SWEET_BERRIES:
			case GLOW_BERRIES:
				return EventType.BREEDING;
			}
		case HOGLIN:
			switch(toolmat)
			{
			default:
				return null;
			case CRIMSON_FUNGUS:
				return EventType.BREEDING;
			}
		case CHICKEN:
			switch(toolmat)
			{
			default:
				return null;
			case WHEAT_SEEDS:
			case MELON_SEEDS:
			case PUMPKIN_SEEDS:
			case BEETROOT_SEEDS:
				return EventType.BREEDING;
			}
		case WOLF:
			switch(toolmat)
			{
			default:
				return null;
			case PORKCHOP:
			case COOKED_PORKCHOP:
			case CHICKEN:
			case COOKED_CHICKEN:
			case BEEF:
			case COOKED_BEEF:
			case MUTTON:
			case COOKED_MUTTON:
			case RABBIT:
			case COOKED_RABBIT:
			case COD:
			case COOKED_COD:
			case ROTTEN_FLESH:
				return EventType.BREEDING;
			}
		case PANDA:
			switch(toolmat)
			{
			default:
				return null;
			case BAMBOO:
				return EventType.BREEDING;
			}
		case RABBIT:
			switch(toolmat)
			{
			default:
				return null;
			case CARROT:
			case DANDELION:
			case GOLDEN_CARROT:
				return EventType.BREEDING;
			}
		case CAT:
			switch(toolmat)
			{
			default:
				return null;
			case COD:
			case SALMON:
			case TROPICAL_FISH:
			case PUFFERFISH:
				return EventType.BREEDING;
			}
		case COW:
		case MUSHROOM_COW:
		case SHEEP:
		case GOAT:
			switch(toolmat)
			{
			default:
				return null;
			case BUCKET:
				return EventType.BUCKET_FILLING;
			case WHEAT:
				return EventType.BREEDING;
			}
		case LLAMA:
			switch(toolmat)
			{
			default:
				return null;
			case WHEAT:
			case HAY_BLOCK:
				return EventType.BREEDING;
			}
		case OCELOT:
			switch(toolmat)
			{
			default:
				return null;
			case COD:
			case SALMON:
				return EventType.BREEDING;
			}
		case HORSE:
		case MULE:
			switch(toolmat)
			{
			default:
				return null;
			case WHEAT:
			case APPLE:
			case SUGAR:
			case HAY_BLOCK:
			case GOLDEN_CARROT:
			case GOLDEN_APPLE:
			case ENCHANTED_GOLDEN_APPLE:
				return EventType.BREEDING;
			}
		case TURTLE:
			switch(toolmat)
			{
			default:
				return null;
			case SEAGRASS:
				return EventType.BREEDING;
			}
		case SNIFFER:
			switch(toolmat)
			{
			default:
				return null;
			case TORCHFLOWER_SEEDS:
				return EventType.BREEDING;
			}
		case STRIDER:
			switch(toolmat)
			{
			default:
				return null;
			case WARPED_FUNGUS:
				return EventType.BREEDING;
			}
		case PIG:
			switch(toolmat)
			{
			default:
				return null;
			case CARROT:
				return EventType.BREEDING;
			}
		}
	}
}
