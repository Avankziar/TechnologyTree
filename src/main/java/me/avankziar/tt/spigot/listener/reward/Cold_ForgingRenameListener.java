package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class Cold_ForgingRenameListener implements Listener
{
	final private static EventType RN = EventType.RENAMING;
	final private static EventType CF = EventType.COLD_FORGING;
	@EventHandler
	public void onAnvil(InventoryClickEvent event)
	{
		if(event.isCancelled()
				|| event.getClickedInventory() == null
				|| event.getCurrentItem() == null
				|| !(event.getClickedInventory() instanceof AnvilInventory)
				|| event.getClickedInventory().getType() != InventoryType.ANVIL
				|| event.getSlotType() != SlotType.RESULT
				|| !(event.getWhoClicked() instanceof Player)
				|| event.getWhoClicked().getGameMode() == GameMode.CREATIVE
				|| event.getWhoClicked().getGameMode() == GameMode.SPECTATOR
				|| !(event.getView() instanceof AnvilView)
				)
		{
			return;
		}
		Player player = (Player) event.getWhoClicked();
		AnvilInventory ai = (AnvilInventory) event.getClickedInventory();
		AnvilView av = (AnvilView) event.getView();
		ItemStack base = ai.getContents()[0];
		ItemStack add = ai.getContents()[1];
		ItemStack result = event.getCurrentItem().clone();
		final Material mat = result.getType();
		if(add == null
				&& base.hasItemMeta()
				&& (	(base.getItemMeta().hasDisplayName()
						&& !base.getItemMeta().getDisplayName().equals(av.getRenameText())
						)
						|| !base.getItemMeta().hasDisplayName())
				)
		{
			if(!EnumHandler.isEventActive(RN))
			{
				ARGCheckEventAction.checkEventAction((Player) event.getWhoClicked(), "RENAMING:RETURN",
						CF, ToolType.HAND, null, null, Material.AIR);
				return;
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					ARGCheckEventAction.checkEventAction((Player) event.getWhoClicked(), "RENAMING:REWARD",
							CF, ToolType.HAND, null, null, mat);
					for(ItemStack is : RewardHandler.getDrops(player, RN, ToolType.HAND, mat, null))
					{
						ItemHandler.dropItem(is, player, null);
					}
					RewardHandler.rewardPlayer(player.getUniqueId(), RN, ToolType.HAND, mat, null, 1);
				}
			}.runTaskAsynchronously(TT.getPlugin());
		} else
		{
			if(!EnumHandler.isEventActive(CF))
			{
				ARGCheckEventAction.checkEventAction((Player) event.getWhoClicked(), "COLD_FORGING:RETURN2",
						CF, ToolType.HAND, null, null, mat);
				return;
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					ARGCheckEventAction.checkEventAction((Player) event.getWhoClicked(), "COLD_FORGING:REWARD",
							CF, ToolType.HAND, null, null, mat);
					for(ItemStack is : RewardHandler.getDrops(player, CF, ToolType.HAND, mat, null))
					{
						ItemHandler.dropItem(is, player, null);
					}
					RewardHandler.rewardPlayer(player.getUniqueId(), CF, ToolType.HAND, mat, null, 1);
				}
			}.runTaskAsynchronously(TT.getPlugin());
		}
	}
}