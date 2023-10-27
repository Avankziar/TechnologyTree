package main.java.me.avankziar.tt.spigot.objects.ram.misc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.handler.GuiHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.TechnologyType;

public class Technology
{
	private String internName;
	private String displayName;
	
	private TechnologyType technologyType;
	private int maximalTechnologyLevelToResearch;
	private long ifBoosterDurationUntilExpiration;
	
	private PlayerAssociatedType playerAssociatedType;
	private String overlyingSubCategory;
	
	private boolean ifResearchedApplyForNewPlayer;
	private double forUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent;
	private double forUninvolvedPollParticipants_RewardRecipesInPercent;
	private double forUninvolvedPollParticipants_RewardDropChancesInPercent;
	private double forUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent;
	private double forUninvolvedPollParticipants_RewardCommandsInPercent;
	private double forUninvolvedPollParticipants_RewardItemsInPercent;
	private double forUninvolvedPollParticipants_RewardModifiersInPercent;
	private double forUninvolvedPollParticipants_RewardValueEntryInPercent;
	
	private int guiSlot;
	
	private List<String> seeRequirementConditionQuery;
	private boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt;
	
	private List<String> researchRequirementConditionQuery;
	
	private String costTTExp;
	private String costVanillaExp;
	private String costMoney;
	private LinkedHashMap<Material, String> costMaterial;
	
	private ArrayList<UnlockableInteraction> rewardUnlockableInteractions;
	private LinkedHashMap<RecipeType, ArrayList<String>> rewardRecipes; 
	private ArrayList<DropChance> rewardDropChances;
	private ArrayList<DropChance> rewardSilkTouchDropChances;
	private ArrayList<String> rewardCommandList; //Angelegt wie spigot:player:/warp %player% oder bungee:console:/do do
	private ArrayList<String> rewardItemList; //Angelegt wie sword_y:64, sword_y ist der Dateiname in dem ItemOrdner
	private ArrayList<String> rewardModifierList; //Angelegt wie bonusmalusname:ADDITION:5
	private ArrayList<String> rewardValueEntryList; //Angelegt wie conditionname:Wert
	
	public Technology(String internName, String displayName,
			TechnologyType technologyType, int maximalTechnologyLevelToResearch, long ifBoosterDurationUntilExpiration,
			PlayerAssociatedType playerAssociatedType, String overlyingSubCategory,
			boolean ifResearchedApplyForNewPlayer,
			double forUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent,
			double forUninvolvedPollParticipants_RewardRecipesInPercent,
			double forUninvolvedPollParticipants_RewardDropChancesInPercent,
			double forUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent,
			double forUninvolvedPollParticipants_RewardCommandsInPercent,
			double forUninvolvedPollParticipants_RewardItemsInPercent,
			double forUninvolvedPollParticipants_RewardModifiersInPercent,
			double forUninvolvedPollParticipants_RewardValueEntryInPercent,
			int guiSlot,
			List<String> seeRequirementConditionQuery, boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt,
			List<String> researchRequirementConditionQuery,
			String costTTExp, String costVanillaExp, String costMoney, LinkedHashMap<Material, String> costMaterial,
			ArrayList<UnlockableInteraction> rewardUnlockableInteractions,
			LinkedHashMap<RecipeType, ArrayList<String>> rewardRecipes,
			ArrayList<DropChance> rewardDropChances,
			ArrayList<DropChance> rewardSilkTouchDropChances,
			ArrayList<String> rewardCommandList,
			ArrayList<String> rewardItemList,
			ArrayList<String> rewardModifierList,
			ArrayList<String> rewardValueEntryList)
	{
		setInternName(internName);
		setDisplayName(displayName);
		
		setTechnologyType(technologyType);
		setMaximalTechnologyLevelToResearch(maximalTechnologyLevelToResearch);
		setIfBoosterDurationUntilExpiration(ifBoosterDurationUntilExpiration);
		
		setPlayerAssociatedType(playerAssociatedType);
		setOverlyingSubCategory(overlyingSubCategory);
		
		setIfResearchedApplyForNewPlayer(ifResearchedApplyForNewPlayer);
		setForUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent(forUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent);
		setForUninvolvedPollParticipants_RewardRecipesInPercent(forUninvolvedPollParticipants_RewardRecipesInPercent);
		setForUninvolvedPollParticipants_RewardDropChancesInPercent(forUninvolvedPollParticipants_RewardDropChancesInPercent);
		setForUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent(forUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent);
		setForUninvolvedPollParticipants_RewardCommandsInPercent(forUninvolvedPollParticipants_RewardCommandsInPercent);
		setForUninvolvedPollParticipants_RewardItemsInPercent(forUninvolvedPollParticipants_RewardItemsInPercent);
		setForUninvolvedPollParticipants_RewardModifiersInPercent(forUninvolvedPollParticipants_RewardModifiersInPercent);
		setForUninvolvedPollParticipants_RewardValueEntryInPercent(forUninvolvedPollParticipants_RewardValueEntryInPercent);
		
		setGuiSlot(guiSlot);
		
		setSeeRequirementConditionQuery(seeRequirementConditionQuery);
		setSeeRequirementShowDifferentItemIfYouNormallyDontSeeIt(seeRequirementShowDifferentItemIfYouNormallyDontSeeIt);
		
		setResearchRequirementConditionQuery(researchRequirementConditionQuery);
		
		setCostTTExp(costTTExp);
		setCostVanillaExp(costVanillaExp);
		setCostMoney(costMoney);
		setCostMaterial(costMaterial);
		
		setRewardUnlockableInteractions(rewardUnlockableInteractions);
		setRewardRecipes(rewardRecipes);
		setRewardDropChances(rewardDropChances);
		setRewardSilkTouchDropChances(rewardSilkTouchDropChances);
		setRewardCommandList(rewardCommandList);
		setRewardItemList(rewardItemList);
		setRewardModifierList(rewardModifierList);
		setRewardValueEntryList(rewardValueEntryList);
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

	public long getIfBoosterDurationUntilExpiration()
	{
		return ifBoosterDurationUntilExpiration;
	}

	public void setIfBoosterDurationUntilExpiration(long ifBoosterDurationUntilExpiration)
	{
		this.ifBoosterDurationUntilExpiration = ifBoosterDurationUntilExpiration;
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

	public boolean isIfResearchedApplyForNewPlayer()
	{
		return ifResearchedApplyForNewPlayer;
	}

	public double getForUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent()
	{
		return forUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent;
	}

	public void setForUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent(
			double forUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent)
	{
		this.forUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent = forUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent;
	}

	public double getForUninvolvedPollParticipants_RewardRecipesInPercent()
	{
		return forUninvolvedPollParticipants_RewardRecipesInPercent;
	}

	public void setForUninvolvedPollParticipants_RewardRecipesInPercent(
			double forUninvolvedPollParticipants_RewardRecipesInPercent)
	{
		this.forUninvolvedPollParticipants_RewardRecipesInPercent = forUninvolvedPollParticipants_RewardRecipesInPercent;
	}

	public double getForUninvolvedPollParticipants_RewardDropChancesInPercent()
	{
		return forUninvolvedPollParticipants_RewardDropChancesInPercent;
	}

	public void setForUninvolvedPollParticipants_RewardDropChancesInPercent(
			double forUninvolvedPollParticipants_RewardDropChancesInPercent)
	{
		this.forUninvolvedPollParticipants_RewardDropChancesInPercent = forUninvolvedPollParticipants_RewardDropChancesInPercent;
	}

	public double getForUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent()
	{
		return forUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent;
	}

	public void setForUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent(
			double forUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent)
	{
		this.forUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent = forUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent;
	}

	public double getForUninvolvedPollParticipants_RewardCommandsInPercent()
	{
		return forUninvolvedPollParticipants_RewardCommandsInPercent;
	}

	public void setForUninvolvedPollParticipants_RewardCommandsInPercent(
			double forUninvolvedPollParticipants_RewardCommandsInPercent)
	{
		this.forUninvolvedPollParticipants_RewardCommandsInPercent = forUninvolvedPollParticipants_RewardCommandsInPercent;
	}

	public double getForUninvolvedPollParticipants_RewardItemsInPercent()
	{
		return forUninvolvedPollParticipants_RewardItemsInPercent;
	}

	public void setForUninvolvedPollParticipants_RewardItemsInPercent(
			double forUninvolvedPollParticipants_RewardItemsInPercent)
	{
		this.forUninvolvedPollParticipants_RewardItemsInPercent = forUninvolvedPollParticipants_RewardItemsInPercent;
	}

	public double getForUninvolvedPollParticipants_RewardModifiersInPercent()
	{
		return forUninvolvedPollParticipants_RewardModifiersInPercent;
	}

	public void setForUninvolvedPollParticipants_RewardModifiersInPercent(
			double forUninvolvedPollParticipants_RewardModifiersInPercent)
	{
		this.forUninvolvedPollParticipants_RewardModifiersInPercent = forUninvolvedPollParticipants_RewardModifiersInPercent;
	}

	public double getForUninvolvedPollParticipants_RewardValueEntryInPercent()
	{
		return forUninvolvedPollParticipants_RewardValueEntryInPercent;
	}

	public void setForUninvolvedPollParticipants_RewardValueEntryInPercent(
			double forUninvolvedPollParticipants_RewardValueEntryInPercent)
	{
		this.forUninvolvedPollParticipants_RewardValueEntryInPercent = forUninvolvedPollParticipants_RewardValueEntryInPercent;
	}

	public void setIfResearchedApplyForNewPlayer(boolean ifResearchedApplyForNewPlayer)
	{
		this.ifResearchedApplyForNewPlayer = ifResearchedApplyForNewPlayer;
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
		return GuiHandler.generateItem(BaseConstructor.getPlugin().getYamlHandler().getTechnologies().get(this.getInternName()),
				"RequirementToSee.ItemIfYouCanSee", 0, null, null, this, getPlayerAssociatedType(), player);
	}

	public ItemStack getSeeRequirementItemIfYouCannotSeeIt(Player player)
	{
		return GuiHandler.generateItem(BaseConstructor.getPlugin().getYamlHandler().getTechnologies().get(this.getInternName()),
				"RequirementToSee.ItemIfYouCannotSee", 0, null, null, this, getPlayerAssociatedType(), player);
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
		return GuiHandler.generateItem(BaseConstructor.getPlugin().getYamlHandler().getTechnologies().get(this.getInternName()),
				"RequirementToResearch.IfYouCanResearchIt", 0, null, null, this, getPlayerAssociatedType(), player);
	}

	public ItemStack getResearchRequirementItemIfYouHaveResearchedIt(Player player)
	{
		return GuiHandler.generateItem(BaseConstructor.getPlugin().getYamlHandler().getTechnologies().get(this.getInternName()),
				"RequirementToResearch.ItemIfYouHaveResearchedIt", 0, null, null, this, getPlayerAssociatedType(), player);
	}
	
	public String getCostTTExp()
	{
		return costTTExp;
	}

	public void setCostTTExp(String costTTExp)
	{
		this.costTTExp = costTTExp;
	}

	public String getCostVanillaExp()
	{
		return costVanillaExp;
	}

	public void setCostVanillaExp(String costVanillaExp)
	{
		this.costVanillaExp = costVanillaExp;
	}

	public String getCostMoney()
	{
		return costMoney;
	}

	public void setCostMoney(String costMoney)
	{
		this.costMoney = costMoney;
	}

	public LinkedHashMap<Material, String> getCostMaterial()
	{
		return costMaterial;
	}

	public void setCostMaterial(LinkedHashMap<Material, String> costMaterial)
	{
		this.costMaterial = costMaterial;
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

	public ArrayList<String> getRewardModifierList()
	{
		return rewardModifierList;
	}

	public void setRewardModifierList(ArrayList<String> rewardModifierList)
	{
		this.rewardModifierList = rewardModifierList;
	}

	public ArrayList<String> getRewardValueEntryList()
	{
		return rewardValueEntryList;
	}

	public void setRewardValueEntryList(ArrayList<String> rewardValueEntryList)
	{
		this.rewardValueEntryList = rewardValueEntryList;
	}
}