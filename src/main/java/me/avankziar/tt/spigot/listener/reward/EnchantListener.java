package main.java.me.avankziar.tt.spigot.listener.reward;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class EnchantListener implements Listener
{
	final private static EventType EN = EventType.ENCHANTING;
	
	@EventHandler
	public void onPrepareEnchant(PrepareItemEnchantEvent event)
	{
		if(event.isCancelled()
				|| event.getEnchanter().getGameMode() == GameMode.CREATIVE
				|| event.getEnchanter().getGameMode() == GameMode.SPECTATOR
				|| event.getItem() == null
				|| !EnumHandler.isEventActive(EN))
		{
			ARGCheckEventAction.checkEventAction(event.getEnchanter(), "ENCHANTING:RETURN",
					EN, ToolType.HAND, null, null, Material.AIR);
			return;
		}
		UUID uuid = event.getEnchanter().getUniqueId();
		/*if(!RecipeHandler.hasAccessToRecipe(uuid, RecipeType.ENCHANTING, event.getItem().getType().toString()))
		{
			ARGCheckEventAction.checkEventAction(event.getEnchanter(), "ENCHANTING:CANNOTACCESS",
					EN, ToolType.HAND, null, null, Material.AIR);
			event.setCancelled(true);
			event.getEnchanter().updateInventory();
			return;
		}*/
		int heo = RewardHandler.getHighestEnchantmentOffer(uuid);
		if(heo <= 0)
		{
			event.getOffers()[0] = null;
			event.getOffers()[1] = null;
			event.getOffers()[2] = null;
			return;
		} else if(heo == 1)
		{
			if(!RewardHandler.canAccessEnchantment(uuid, event.getItem().getType(), event.getOffers()[0].getEnchantment()))
			{
				event.getOffers()[0] = new EnchantmentOffer(Enchantment.VANISHING_CURSE, 1, event.getOffers()[0].getCost());
			}
			event.getOffers()[1] = null;
			event.getOffers()[2] = null;
		} else if(heo == 2)
		{
			if(!RewardHandler.canAccessEnchantment(uuid, event.getItem().getType(), event.getOffers()[0].getEnchantment()))
			{
				event.getOffers()[0] = new EnchantmentOffer(Enchantment.VANISHING_CURSE, 1, event.getOffers()[0].getCost());
			}
			if(!RewardHandler.canAccessEnchantment(uuid, event.getItem().getType(), event.getOffers()[1].getEnchantment()))
			{
				event.getOffers()[1] = new EnchantmentOffer(Enchantment.VANISHING_CURSE, 1, event.getOffers()[1].getCost());
			}
			event.getOffers()[2] = null;
		} else if(heo == 3)
		{
			if(!RewardHandler.canAccessEnchantment(uuid, event.getItem().getType(), event.getOffers()[0].getEnchantment()))
			{
				event.getOffers()[0] = new EnchantmentOffer(Enchantment.VANISHING_CURSE, 1, event.getOffers()[0].getCost());
			}
			if(!RewardHandler.canAccessEnchantment(uuid, event.getItem().getType(), event.getOffers()[1].getEnchantment()))
			{
				event.getOffers()[1] = new EnchantmentOffer(Enchantment.VANISHING_CURSE, 1, event.getOffers()[1].getCost());
			}
			if(!RewardHandler.canAccessEnchantment(uuid, event.getItem().getType(), event.getOffers()[2].getEnchantment()))
			{
				event.getOffers()[2] = new EnchantmentOffer(Enchantment.VANISHING_CURSE, 1, event.getOffers()[2].getCost());
			}
		}
	}
	
	@EventHandler
	public void onEnchant(EnchantItemEvent event)
	{
		if(event.isCancelled()
				|| event.getEnchanter().getGameMode() == GameMode.CREATIVE
				|| event.getEnchanter().getGameMode() == GameMode.SPECTATOR
				|| event.getItem() == null
				|| !EnumHandler.isEventActive(EN))
		{
			ARGCheckEventAction.checkEventAction(event.getEnchanter(), "ENCHANTING:RETURN",
					EN, ToolType.HAND, null, null, Material.AIR);
			return;
		}
		final Player player = event.getEnchanter();
		final Location loc = event.getEnchantBlock().getLocation();
		final Material mat = event.getItem().getType();
		ArrayList<Enchantment> elist = new ArrayList<>();
		for(Entry<Enchantment, Integer> e : event.getEnchantsToAdd().entrySet())
		{
			if(!Enchantment.VANISHING_CURSE.getKey().getKey().equalsIgnoreCase(e.getKey().getKey().getKey()) //VanishingCurse is default ench
					&& !RewardHandler.canAccessEnchantment(player.getUniqueId(), mat, e.getKey()))
			{
				elist.add(e.getKey());
			}
		}
		for(Enchantment e : elist)
		{
			event.getEnchantsToAdd().remove(e);
		}
		new BukkitRunnable()
		{		
			@Override
			public void run()
			{
				ARGCheckEventAction.checkEventAction(player, "ENCHANTING:REWARD",
						EN, ToolType.HAND, null, null, Material.AIR);
				for(ItemStack is : RewardHandler.getDrops(player, EN, ToolType.HAND, mat, null))
				{
					ItemHandler.dropItem(is, player, loc);
				}
				RewardHandler.rewardPlayer(player.getUniqueId(), EN, ToolType.HAND, mat, null, 1);
			}
		}.runTaskAsynchronously(TT.getPlugin());
	}
}