package main.java.me.avankziar.tt.spigot.objects.ram.misc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.ifh.ItemGenerator;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.TechnologyType;

public class Technology
{
	private String internName;
	private String displayName;
	
	private TechnologyType technologyType;
	private int maximalTechnologyLevelToResearch;
	
	private PlayerAssociatedType playerAssociatedType;
	private String overlyingSubCategory;
	
	private int guiSlot;
	
	private List<String> seeRequirementConditionQuery;
	private boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt;
	
	private List<String> researchRequirementConditionQuery;
	
	private ArrayList<UnlockableInteraction> rewardUnlockableInteractions;
	private LinkedHashMap<RecipeType, ArrayList<String>> rewardRecipes; 
	private ArrayList<DropChance> rewardDropChances;
	private ArrayList<DropChance> rewardSilkTouchDropChances;
	private ArrayList<String> rewardCommandList; //Angelegt wie spigot:player:/warp %player% oder bungee:console:/do do
	private ArrayList<String> rewardItemList; //Angelegt wie sword_y:64, sword_y ist der Dateiname in dem ItemOrdner
	private ArrayList<String> rewardBonusMalusList; //Angelegt wie bonusmalusname:ADDITION:5
	private ArrayList<String> rewardConditionEntryList; //Angelegt wie conditionname:Wert
	
	public Technology(String internName, String displayName,
			TechnologyType technologyType, int maximalTechnologyLevelToResearch,
			PlayerAssociatedType playerAssociatedType, String overlyingSubCategory,
			int guiSlot,
			List<String> seeRequirementConditionQuery, boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt,
			List<String> researchRequirementConditionQuery,
			ArrayList<UnlockableInteraction> rewardUnlockableInteractions,
			LinkedHashMap<RecipeType, ArrayList<String>> rewardRecipes,
			ArrayList<DropChance> rewardDropChances,
			ArrayList<DropChance> rewardSilkTouchDropChances,
			ArrayList<String> rewardCommandList,
			ArrayList<String> rewardItemList,
			ArrayList<String> rewardBonusMalusList,
			ArrayList<String> rewardConditionEntryList)
	{
		setInternName(internName);
		setDisplayName(displayName);
		
		setTechnologyType(technologyType);
		setMaximalTechnologyLevelToResearch(maximalTechnologyLevelToResearch);
		
		setPlayerAssociatedType(playerAssociatedType);
		setOverlyingSubCategory(overlyingSubCategory);
		
		setGuiSlot(guiSlot);
		
		setSeeRequirementConditionQuery(seeRequirementConditionQuery);
		setSeeRequirementShowDifferentItemIfYouNormallyDontSeeIt(seeRequirementShowDifferentItemIfYouNormallyDontSeeIt);
		
		setResearchRequirementConditionQuery(researchRequirementConditionQuery);
		
		setRewardUnlockableInteractions(rewardUnlockableInteractions);
		setRewardRecipes(rewardRecipes);
		setRewardDropChances(rewardDropChances);
		setRewardSilkTouchDropChances(rewardSilkTouchDropChances);
		setRewardCommandList(rewardCommandList);
		setRewardItemList(rewardItemList);
		setRewardBonusMalusList(rewardBonusMalusList);
		setRewardConditionEntryList(rewardConditionEntryList);
	}

	public String getInternName()
	{
		return internName;
	}

	public void setInternName(String internName)
	{
		this.internName = internName;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public TechnologyType getTechnologyType()
	{
		return technologyType;
	}

	public void setTechnologyType(TechnologyType technologyType)
	{
		this.technologyType = technologyType;
	}

	public int getMaximalTechnologyLevelToResearch()
	{
		return maximalTechnologyLevelToResearch;
	}

	public void setMaximalTechnologyLevelToResearch(int maximalTechnologyLevelToResearch)
	{
		this.maximalTechnologyLevelToResearch = maximalTechnologyLevelToResearch;
	}

	public PlayerAssociatedType getPlayerAssociatedType()
	{
		return playerAssociatedType;
	}

	public void setPlayerAssociatedType(PlayerAssociatedType playerAssociatedType)
	{
		this.playerAssociatedType = playerAssociatedType;
	}

	public String getOverlyingSubCategory()
	{
		return overlyingSubCategory;
	}

	public void setOverlyingSubCategory(String overlyingSubCategory)
	{
		this.overlyingSubCategory = overlyingSubCategory;
	}

	public int getGuiSlot()
	{
		return guiSlot;
	}

	public void setGuiSlot(int guiSlot)
	{
		this.guiSlot = guiSlot;
	}

	public List<String> getSeeRequirementConditionQuery()
	{
		return seeRequirementConditionQuery;
	}

	public void setSeeRequirementConditionQuery(List<String> seeRequirementConditionQuery)
	{
		this.seeRequirementConditionQuery = seeRequirementConditionQuery;
	}

	public boolean isSeeRequirementShowDifferentItemIfYouNormallyDontSeeIt()
	{
		return seeRequirementShowDifferentItemIfYouNormallyDontSeeIt;
	}

	public void setSeeRequirementShowDifferentItemIfYouNormallyDontSeeIt(
			boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt)
	{
		this.seeRequirementShowDifferentItemIfYouNormallyDontSeeIt = seeRequirementShowDifferentItemIfYouNormallyDontSeeIt;
	}

	public ItemStack getSeeRequirementItemIfYouCanSeeIt(Player player)
	{
		return new ItemGenerator().generateItem(player,
				BaseConstructor.getPlugin().getYamlHandler().getTechnologies().get(this.getInternName()),
				"RequirementToSee.ItemIfYouCanSee", 0);
	}

	public ItemStack getSeeRequirementItemifYouCannotSeeIt(Player player)
	{
		return new ItemGenerator().generateItem(player,
				BaseConstructor.getPlugin().getYamlHandler().getTechnologies().get(this.getInternName()),
				"RequirementToSee.ItemIfYouCannotSee", 0);
	}

	public List<String> getResearchRequirementConditionQuery()
	{
		return researchRequirementConditionQuery;
	}

	public void setResearchRequirementConditionQuery(List<String> researchRequirementConditionQuery)
	{
		this.researchRequirementConditionQuery = researchRequirementConditionQuery;
	}

	public ItemStack getResearchRequirementItemIfYouCanResearchIt(Player player)
	{
		return new ItemGenerator().generateItem(player,
				BaseConstructor.getPlugin().getYamlHandler().getTechnologies().get(this.getInternName()),
				"RequirementToResearch.IfYouCanResearchIt", 0);
	}

	public ItemStack getResearchRequirementItemifYouHaveResearchedIt(Player player)
	{
		return new ItemGenerator().generateItem(player,
				BaseConstructor.getPlugin().getYamlHandler().getTechnologies().get(this.getInternName()),
				"RequirementToResearch.ItemIfYouHaveResearchedIt", 0);
	}
	
	public ArrayList<UnlockableInteraction> getRewardUnlockableInteractions()
	{
		return rewardUnlockableInteractions;
	}

	public void setRewardUnlockableInteractions(ArrayList<UnlockableInteraction> rewardUnlockableInteractions)
	{
		this.rewardUnlockableInteractions = rewardUnlockableInteractions;
	}

	public LinkedHashMap<RecipeType, ArrayList<String>> getRewardRecipes()
	{
		return rewardRecipes;
	}

	public void setRewardRecipes(LinkedHashMap<RecipeType, ArrayList<String>> rewardRecipes)
	{
		this.rewardRecipes = rewardRecipes;
	}

	public ArrayList<DropChance> getRewardDropChances()
	{
		return rewardDropChances;
	}

	public void setRewardDropChances(ArrayList<DropChance> rewardDropChances)
	{
		this.rewardDropChances = rewardDropChances;
	}

	public ArrayList<DropChance> getRewardSilkTouchDropChances()
	{
		return rewardSilkTouchDropChances;
	}

	public void setRewardSilkTouchDropChances(ArrayList<DropChance> rewardSilkTouchDropChances)
	{
		this.rewardSilkTouchDropChances = rewardSilkTouchDropChances;
	}

	public ArrayList<String> getRewardCommandList()
	{
		return rewardCommandList;
	}

	public void setRewardCommandList(ArrayList<String> rewardCommandList)
	{
		this.rewardCommandList = rewardCommandList;
	}

	public ArrayList<String> getRewardItemList()
	{
		return rewardItemList;
	}

	public void setRewardItemList(ArrayList<String> rewardItemList)
	{
		this.rewardItemList = rewardItemList;
	}

	public ArrayList<String> getRewardBonusMalusList()
	{
		return rewardBonusMalusList;
	}

	public void setRewardBonusMalusList(ArrayList<String> rewardBonusMalusList)
	{
		this.rewardBonusMalusList = rewardBonusMalusList;
	}

	public ArrayList<String> getRewardConditionEntryList()
	{
		return rewardConditionEntryList;
	}

	public void setRewardConditionEntryList(ArrayList<String> rewardConditionEntryList)
	{
		this.rewardConditionEntryList = rewardConditionEntryList;
	}

}
