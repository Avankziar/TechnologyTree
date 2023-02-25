package main.java.me.avankziar.tt.spigot.objects.ram.misc;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import main.java.me.avankziar.tt.spigot.objects.EventType;

public class UnlockableInteraction //per Technology
{	
	private EventType eventType;
	private Material material;
	private EntityType eventEntityType;
	private boolean canAccess;
	private double technologyExperience;
	private HashMap<String, Double> moneyMap = new HashMap<>();
	private double vanillaExperience;
	private HashMap<String, Double> commandMap = new HashMap<>();
	
	public UnlockableInteraction(EventType eventType, Material material, EntityType eventEntityType,
			boolean canAccess, double technologyExperience,
			HashMap<String, Double> moneyMap,
			double vanillaExperience, HashMap<String, Double> commandMap)
	{
		setEventType(eventType);
		setEventMaterial(material);
		setCanAccess(canAccess);
		setTechnologyExperience(technologyExperience);
		setMoneyMap(moneyMap);
		setVanillaExperience(vanillaExperience);
		setCommandMap(commandMap);
	}
	
	public UnlockableInteraction(EventType eventType, Material material, EntityType eventEntityType, boolean canAccess, double technologyExperience, double vanillaExperience)
	{
		setCanAccess(canAccess);
		setEventType(eventType);
		setEventMaterial(material);
		setTechnologyExperience(technologyExperience);
		setVanillaExperience(vanillaExperience);
	}

	public boolean isCanAccess()
	{
		return canAccess;
	}

	public void setCanAccess(boolean canAccess)
	{
		this.canAccess = canAccess;
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
		return material;
	}

	public void setEventMaterial(Material material)
	{
		this.material = material;
	}

	public EntityType getEventEntityType()
	{
		return eventEntityType;
	}

	public void setEventEntityType(EntityType eventEntityType)
	{
		this.eventEntityType = eventEntityType;
	}

	public double getTechnologyExperience()
	{
		return technologyExperience;
	}

	public void setTechnologyExperience(double technologyExperience)
	{
		this.technologyExperience = technologyExperience;
	}

	public HashMap<String, Double> getMoneyMap()
	{
		return moneyMap;
	}

	public void setMoneyMap(HashMap<String, Double> moneyMap)
	{
		this.moneyMap = moneyMap;
	}
	
	public UnlockableInteraction addMoneyValues(String currencyInternName, double value)
	{
		this.moneyMap.put(currencyInternName, value);
		return this;
	}

	public double getVanillaExperience()
	{
		return vanillaExperience;
	}

	public void setVanillaExperience(double vanillaExperience)
	{
		this.vanillaExperience = vanillaExperience;
	}

	public HashMap<String, Double> getCommandMap()
	{
		return commandMap;
	}

	public void setCommandMap(HashMap<String, Double> commandMap)
	{
		this.commandMap = commandMap;
	}
	
	public UnlockableInteraction addCommandValues(String command, double value)
	{
		this.commandMap.put(command, value);
		return this;
	}
}
