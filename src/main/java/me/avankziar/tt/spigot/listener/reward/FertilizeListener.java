package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class FertilizeListener implements Listener
{
	final private static EventType FE = EventType.FERTILIZING;
	
	@EventHandler
	public void onFertilize(BlockFertilizeEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer() == null
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| !EnumHandler.isEventActive(FE)
				|| !RewardHandler.canAccessInteraction(event.getPlayer(),
						ToolType.getToolType(event.getPlayer().getInventory().getItemInMainHand().getType()), FE, event.getBlock().getType(), null))
		{
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "FERTILIZING:RETURN",
					FE, ToolType.HAND, null, null, Material.AIR);
			return;
		}
		final Player player = event.getPlayer();
		final Location loc = event.getBlock().getLocation();
		final Material mat = event.getBlock().getType();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(player, "FERTILIZING:REWARD",
						FE, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(player, FE, ToolType.HAND, mat, null))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), FE, ToolType.HAND, mat, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
	
	//Do not needed
	//public void on(StructureGrowEvent|BlockGrowEvent event)
}