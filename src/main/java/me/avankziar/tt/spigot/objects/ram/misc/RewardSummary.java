package main.java.me.avankziar.tt.spigot.objects.ram.misc;

import java.util.LinkedHashMap;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import main.java.me.avankziar.tt.spigot.objects.EventType;

public class RewardSummary
{
	private EventType eventType;
	private Material material;
	private EntityType entityType;
	private double amount;
	
	private double vanillaExpReward;
	private double ttExpReward;
	//String = currency (vault = VaultEconomy)
	private LinkedHashMap<String, Double> moneyReward;
	/*
	 * String = cmd
	 * Example: spigot:/command here input
	 *          bungee:/command here input
	 */
	private LinkedHashMap<String, Double> cmdReward;
	
	
	public RewardSummary(EventType eventType, Material material, EntityType entityType, double amount,
			double vanillaExp, double ttExp, LinkedHashMap<String, Double> moneyReward, LinkedHashMap<String, Double> cmdReward)
	{
		setEventType(eventType);
		setMaterial(material);
		setEntityType(entityType);
		setAmount(amount);
		setVanillaExpReward(vanillaExp);
		setTTExpReward(ttExp);
		setMoneyReward(moneyReward);
		setCmdReward(cmdReward);
	}


	public EventType getEventType()
	{
		return eventType;
	}


	public void setEventType(EventType eventType)
	{
		this.eventType = eventType;
	}


	public Material getMaterial()
	{
		return material;
	}


	public void setMaterial(Material material)
	{
		this.material = material;
	}


	public EntityType getEntityType()
	{
		return entityType;
	}


	public void setEntityType(EntityType entityType)
	{
		this.entityType = entityType;
	}


	public double getAmount()
	{
		return amount;
	}


	public void setAmount(double amount)
	{
		this.amount = amount;
	}


	public double getVanillaExpReward()
	{
		return vanillaExpReward;
	}


	public void setVanillaExpReward(double vanillaExpReward)
	{
		this.vanillaExpReward = vanillaExpReward;
	}


	public double getTTExpReward()
	{
		return ttExpReward;
	}


	public void setTTExpReward(double ttExpReward)
	{
		this.ttExpReward = ttExpReward;
	}


	public LinkedHashMap<String, Double> getMoneyReward()
	{
		return moneyReward;
	}


	public void setMoneyReward(LinkedHashMap<String, Double> moneyReward)
	{
		this.moneyReward = moneyReward;
	}


	public LinkedHashMap<String, Double> getCmdReward()
	{
		return cmdReward;
	}


	public void setCmdReward(LinkedHashMap<String, Double> cmdReward)
	{
		this.cmdReward = cmdReward;
	}
}