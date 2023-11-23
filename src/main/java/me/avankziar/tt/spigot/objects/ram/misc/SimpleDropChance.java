package main.java.me.avankziar.tt.spigot.objects.ram.misc;

import java.util.LinkedHashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.ifh.ItemGenerator;

public class SimpleDropChance
{
	private String toDropItem;
	private LinkedHashMap<Integer, Double> amountToDropChance = new LinkedHashMap<>();
	
	public SimpleDropChance(String toDropItem, LinkedHashMap<Integer, Double> amountToDropChance)
	{
		setToDropItem(toDropItem);
		setAmountToDropChance(amountToDropChance);
	}
	
	public SimpleDropChance(String toDropItem, int toDropItemAmount, double dropChance)
	{
		setToDropItem(toDropItem);
		if(amountToDropChance.containsKey(toDropItemAmount))
		{
			double erg = amountToDropChance.get(toDropItemAmount) + dropChance;
			erg = erg > 1 ? 1 : erg;
			erg = erg < 0 ? 0 : erg;
			amountToDropChance.put(toDropItemAmount, erg);
		} else
		{
			amountToDropChance.put(toDropItemAmount, dropChance);
		}
	}
	
	public String getToDropItem()
	{
		return toDropItem;
	}

	public void setToDropItem(String toDropItem)
	{
		this.toDropItem = toDropItem;
	}

	public LinkedHashMap<Integer, Double> getAmountToDropChance()
	{
		return amountToDropChance;
	}

	public void setAmountToDropChance(LinkedHashMap<Integer, Double> amountToDropChance)
	{
		this.amountToDropChance = amountToDropChance;
	}
	
	public void add(int toDropItemAmount, double dropChance)
	{
		if(amountToDropChance.containsKey(toDropItemAmount))
		{
			double erg = amountToDropChance.get(toDropItemAmount) + dropChance;
			erg = erg > 1 ? 1 : erg;
			erg = erg < 0 ? 0 : erg;
			amountToDropChance.put(toDropItemAmount, erg);
		} else
		{
			amountToDropChance.put(toDropItemAmount, dropChance);
		}
	}

	public ItemStack getItem(Player player, int amount)
	{
		ItemStack is = null;
		String[] split = this.toDropItem.split("=");
		if(split.length != 2)
		{
			return is;
		}
		if(split[0].startsWith("mat"))
		{
			is = new ItemStack(Material.valueOf(split[1]), amount);
		} else if(split[0].startsWith("item"))
		{
			is = new ItemGenerator().generateItem(player, 
					BaseConstructor.getPlugin().getYamlHandler().getItemGenerators().get(split[1]), "", amount);
		}
		return is;
	}
}