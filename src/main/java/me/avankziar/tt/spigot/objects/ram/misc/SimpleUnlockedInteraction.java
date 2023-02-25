package main.java.me.avankziar.tt.spigot.objects.ram.misc;

import java.util.HashMap;
import java.util.Map.Entry;

public class SimpleUnlockedInteraction
{
	private boolean canAccess;
	private double technologyExperience;
	private HashMap<String, Double> moneyMap = new HashMap<>();
	private double vanillaExperience;
	private HashMap<String, Double> commandMap = new HashMap<>();
	
	public SimpleUnlockedInteraction(boolean canAccess, double technologyExperience,
			String moneyTerm, double moneyValue,
			double vanillaExperience,
			String commandTerm, double commandValue)
	{
		setCanAccess(canAccess);
		setTechnologyExperience(technologyExperience);
		moneyMap.put(moneyTerm, moneyValue);
		setVanillaExperience(vanillaExperience);
		commandMap.put(commandTerm, commandValue);
	}
	
	public SimpleUnlockedInteraction(boolean canAccess, double technologyExperience,
			HashMap<String, Double> moneyMap,
			double vanillaExperience,
			HashMap<String, Double> commandMap)
	{
		setCanAccess(canAccess);
		setTechnologyExperience(technologyExperience);
		setMoneyMap(moneyMap);
		setVanillaExperience(vanillaExperience);
		setCommandMap(commandMap);
	}
	
	public void add(boolean canAccess, double technologyExperience,
			HashMap<String, Double> moneyMap,
			double vanillaExperience,
			HashMap<String, Double> commandMap)
	{
		if(!this.canAccess && canAccess)
		{
			setCanAccess(canAccess);
		}
		setTechnologyExperience(getTechnologyExperience() + technologyExperience);
		setVanillaExperience(getVanillaExperience() + vanillaExperience);
		for(Entry<String, Double> e : moneyMap.entrySet())
		{
			String moneyTerm = e.getKey();
			double moneyValue = e.getValue();
			if(moneyMap.containsKey(moneyTerm))
			{
				double erg = moneyMap.get(moneyTerm) + moneyValue;
				moneyMap.put(moneyTerm, erg);
			} else
			{
				moneyMap.put(moneyTerm, moneyValue);
			}
		}		
		for(Entry<String, Double> e : commandMap.entrySet())
		{
			String commandTerm = e.getKey();
			double commandValue = e.getValue();
			if(commandMap.containsKey(commandTerm))
			{
				double erg = commandMap.get(commandTerm) + commandValue;
				commandMap.put(commandTerm, erg);
			} else
			{
				commandMap.put(commandTerm, commandValue);
			}
		}
	}

	public boolean isCanAccess()
	{
		return canAccess;
	}

	public void setCanAccess(boolean canAccess)
	{
		this.canAccess = canAccess;
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

}
