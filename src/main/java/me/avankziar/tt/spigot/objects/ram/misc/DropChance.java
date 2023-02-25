package main.java.me.avankziar.tt.spigot.objects.ram.misc;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.ifh.ItemGenerator;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class DropChance
{
	private EventType eventType;
	private Material eventMaterial;
	private EntityType eventEntity;
	private String toDropItem;
	private int toDropItemAmount;
	private double dropChance;
	
	public DropChance(EventType eventType, Material eventMaterial, EntityType eventEntity,
			String toDropItem, int toDropItemAmount, double dropChance)
	{
		setEventType(eventType);
		setEventMaterial(eventMaterial);
		setToDropItem(toDropItem);
		setToDropItemAmount(toDropItemAmount);
		setDropChance(dropChance);
	}

	public EventType getEventType()
	{
		return eventType;
	}

	public void setEventType(EventType eventType)
	{
		this.eventType = eventType;
	}

	public Material getEventMaterial()
	{
		return eventMaterial;
	}

	public void setEventMaterial(Material eventMaterial)
	{
		this.eventMaterial = eventMaterial;
	}

	public EntityType getEventEntity()
	{
		return eventEntity;
	}

	public void setEventEntity(EntityType eventEntity)
	{
		this.eventEntity = eventEntity;
	}

	public int getToDropItemAmount()
	{
		return toDropItemAmount;
	}

	public String getToDropItem()
	{
		return toDropItem;
	}

	public void setToDropItem(String toDropItem)
	{
		this.toDropItem = toDropItem;
	}
	
	public void setToDropItemAmount(int toDropItemAmount)
	{
		this.toDropItemAmount = toDropItemAmount;
	}

	public double getDropChance()
	{
		return dropChance;
	}

	public void setDropChance(double dropChance)
	{
		this.dropChance = dropChance;
	}
	
	public ItemStack getItem(Player player)
	{
		ItemStack is = null;
		String[] split = this.toDropItem.split("=");
		if(split.length != 2)
		{
			return is;
		}
		if(split[0].startsWith("mat"))
		{
			is = new ItemStack(Material.valueOf(split[1]), this.toDropItemAmount);
		} else if(split[0].startsWith("item"))
		{
			is = new ItemGenerator().generateItem(player, 
					BaseConstructor.getPlugin().getYamlHandler().getItemGenerators().get(split[1]), "", this.toDropItemAmount);
		}
		return is;
	}
}