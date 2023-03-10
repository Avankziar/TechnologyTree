package main.java.me.avankziar.tt.spigot.handler;

import java.util.ArrayList;
import java.util.List;

import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;

public class ConfigHandler
{	
	public enum CountType
	{
		HIGHEST, ADDUP;
	}
	
	public CountType getCountPermType()
	{
		String s = BaseConstructor.getPlugin().getYamlHandler().getConfig().getString("Mechanic.CountPerm", "HIGHEST");
		CountType ct;
		try
		{
			ct = CountType.valueOf(s);
		} catch (Exception e)
		{
			ct = CountType.HIGHEST;
		}
		return ct;
	}
	
	public boolean isMechanicBonusMalusEnabled()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("EnableMechanic.BonusMalus", false);
	}
	
	public boolean isMechanicCommandToBungeeEnabled()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("EnableMechanic.CommandToBungee", false);
	}
	
	public boolean isMechanicConditionEnabled()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("EnableMechanic.Condition", false);
	}
	
	public boolean isMechanicConditionQueryParserEnabled()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("EnableMechanic.ConditionQueryParser", false);
	}
	
	public List<String> getAutoResearchedTechnologies()
	{
		if(BaseConstructor.getPlugin().getYamlHandler().getConfig().get("Do.NewPlayer.AutoResearchTechnology") != null)
		{
			return BaseConstructor.getPlugin().getYamlHandler().getConfig().getStringList("Do.NewPlayer.AutoResearchTechnology");
		}
		return new ArrayList<>();
	}
	
	public boolean overrideAlreadyRegisteredBlocks()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("Do.Block.OverrideAlreadyRegisteredBlocks", false);
	}
	
	public long loseDropItemOwnershipAfterTime()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getLong("Do.Item.LoseDropItemOwnershipAfterTimeInSeconds", 300);
	}
	
	public List<String> activeEvents()
	{
		if(BaseConstructor.getPlugin().getYamlHandler().getConfig().get("Do.Reward.ActiveEvents") != null)
		{
			return BaseConstructor.getPlugin().getYamlHandler().getConfig().getStringList("Do.Reward.ActiveEvents");
		}
		return new ArrayList<>();
	}
	
	public boolean ifBlockIsManuallyPlacedBefore_RewardItByBreaking()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("Do.Reward.Placing.IfBlockIsManuallyPlacedBefore_RewardItByBreaking", false);
	}
	
	public boolean finishBrewIfPlayerHasNotTheRecipeUnlocked()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("Do.Reward.Brewing.FinishBrewIfPlayerHasNotTheRecipeUnlocked", false);
	}
	
	public boolean useMetaDataToTrackPlayerPlacedBlocks()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("Do.Reward.Placing.UseMetaDataToTrackPlayerPlacedBlocks", true);
	}
	
	public boolean startSmeltIfPlayerIsNotOnline()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("Do.Reward.Smelting.StartSmeltIfPlayerIsNotOnline", true);
	}
	
	public boolean finishSmeltIfPlayerHasNotTheRecipeUnlocked()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("Do.Reward.Smelting.FinishSmeltIfPlayerHasNotTheRecipeUnlocked", false);
	}
	
	public long rewardPayoutRepetitionRateForOnlinePlayer()
	{
		return 20L*BaseConstructor.getPlugin().getYamlHandler().getConfig().getLong("Do.Reward.Payout.RepetitionRateForOnlinePlayersInSeconds", 30);
	}
	
	public boolean rewardPayoutForOfflinePlayerActive()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("Do.Reward.Payout.ForOfflinePlayerActive", false);
	}
	
	public long rewardPayoutRepetitionRateForOfflinePlayer()
	{
		return 20L*BaseConstructor.getPlugin().getYamlHandler().getConfig().getLong("Do.Reward.Payout.RepetitionRateForOfflinePlayersInSeconds", 60*30);
	}
	
	public double rewardPayoutTaxInPercent()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getLong("Do.Reward.Payout.TaxInPercent", 0);
	}
}