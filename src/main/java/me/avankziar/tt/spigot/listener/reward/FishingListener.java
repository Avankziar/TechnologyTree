package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class FishingListener implements Listener
{	
	final private static EventType FI = EventType.FISHING;
	@EventHandler
	public void onFishing(PlayerFishEvent event)
	{
		if(event.isCancelled()
				|| event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR
				|| event.getState() != State.CAUGHT_FISH
				|| !EnumHandler.isEventActive(FI))
		{
			ARGCheckEventAction.checkEventAction(event.getPlayer(), "FISHING:RETURN",
					FI, ToolType.HAND, null, null, Material.AIR);
			return;
		}
		event.setExpToDrop(0);
		ItemStack ismat = null;
		Entity e = event.getCaught();
		if(e != null && e instanceof Item)
		{
			Item it = (Item) e;
			ismat = it.getItemStack();
		} else
		{
			return;
		}
		final Player player = event.getPlayer();
		final Material mat = ismat.getType();
		final int amount = ismat.getAmount();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(player, "FISHING:REWARD",
						FI, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(event.getPlayer(), FI, ToolType.FISHING_ROD, mat, null))
				{
					ItemHandler.dropItem(is, player, null);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), FI, ToolType.FISHING_ROD, mat, null, amount);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}