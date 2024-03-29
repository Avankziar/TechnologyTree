package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class BreedListener implements Listener
{
	final private static EventType BR = EventType.BREEDING;
	@EventHandler
	public void onBreed(EntityBreedEvent event)
	{
		if(event.isCancelled()
				|| event.getBreeder() == null
				|| event.getBredWith() == null
				|| !(event.getBreeder() instanceof Player)
				|| ((HumanEntity) event.getBreeder()).getGameMode() == GameMode.CREATIVE
				|| ((HumanEntity) event.getBreeder()).getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(BR))
		{
			if(event.getBreeder() != null && event.getBreeder() instanceof Player)
			{
				ARGCheckEventAction.checkEventAction((Player) event.getBreeder(), "BREEDING:RETURN",
						EventType.BREEDING, ToolType.HAND, null, event.getEntityType(), event.getBredWith() != null ? event.getBredWith().getType() : null);
			}
			return;
		}
		if(!RewardHandler.canAccessInteraction((Player) event.getBreeder(),
						ToolType.HAND, BR, null, event.getEntityType()))
		{
			event.setCancelled(true);
			ARGCheckEventAction.checkEventAction((Player) event.getBreeder(), "BREEDING:CANNOTACCESS",
					EventType.BREEDING, ToolType.HAND, null, event.getEntityType(), event.getBredWith().getType());
			return;
		}
		final Player player = (Player) event.getBreeder();
		final ToolType tool = ToolType.HAND;
		final Location loc = event.getEntity().getLocation();
		final EntityType ent = event.getEntityType();
		final Material mat = event.getBredWith().getType();
		if(!ConfigHandler.GAMERULE_UseVanillaExpDrops)
		{
			event.setExperience(0);
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(player, "BREEDING:REWARD",
						EventType.BREEDING, ToolType.HAND, null, ent, mat);
				for(ItemStack is : RewardHandler.getDrops(player, BR, tool, null, ent))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), BR, tool, null, ent, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}