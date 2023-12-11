package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class GrindstoneListener implements Listener
{
	final private static EventType GR = EventType.GRINDING;
	@EventHandler
	public void onGrindstone(InventoryClickEvent event)
	{
		if(event.isCancelled()
				|| event.getCurrentItem() == null
				|| event.getClickedInventory() == null
				|| event.getClickedInventory().getType() != InventoryType.GRINDSTONE
				|| !(event.getClickedInventory() instanceof GrindstoneInventory)
				|| !(event.getWhoClicked() instanceof Player)
				|| event.getWhoClicked().getGameMode() == GameMode.CREATIVE
				|| event.getWhoClicked().getGameMode() == GameMode.SPECTATOR
				|| event.getSlotType() != SlotType.RESULT
				|| !EnumHandler.isEventActive(GR))
		{
			return;
		}
		final Player player = (Player) event.getWhoClicked();
		final Material mat = event.getCurrentItem().getType();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(player, "GRINDING:REWARD",
						GR, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(player, GR, ToolType.HAND, mat, null))
				{
					ItemHandler.dropItem(is, player, null);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), GR, ToolType.HAND, mat, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());	
	}
}