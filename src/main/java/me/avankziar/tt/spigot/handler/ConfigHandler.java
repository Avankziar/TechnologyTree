package main.java.me.avankziar.tt.spigot.handler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import main.java.me.avankziar.tt.spigot.TT;

public class ConfigHandler
{	
	public static TT plugin = TT.getPlugin();
	
	public static boolean 
			GAMERULE_UseVanillaExpDrops = false,
			GAMERULE_UseVanillaItemDrops = false,
			GAMERULE_UseVanillaAccessToAnvil = false,
			GAMERULE_UseVanillaAccessToBrewingStand = false,
			GAMERULE_UseVanillaAccessToBlastFurnace = false,
			GAMERULE_UseVanillaAccessToCampfire = false,
			GAMERULE_UseVanillaAccessToCartographyTable = false,
			GAMERULE_UseVanillaAccessToComposter= false,
			GAMERULE_UseVanillaAccessToCrafingTable = false,
			GAMERULE_UseVanillaAccessToGrindstone = false,
			GAMERULE_UseVanillaAccessToEnchantingTable = false,
			GAMERULE_UseVanillaAccessToFurnace = false,
			GAMERULE_UseVanillaAccessToSmithingTable = false,
			GAMERULE_UseVanillaAccessToSmoker = false,
			GAMERULE_UseVanillaAccessToStoneCutter = false
			;
	
	public ConfigHandler(){}
	
	public enum CountType
	{
		HIGHEST, ADDUP;
	}
	
	public static void init()
	{
		GAMERULE_UseVanillaExpDrops = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.ExpDrops", false);
		GAMERULE_UseVanillaItemDrops = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.ItemDrops", false);
		GAMERULE_UseVanillaAccessToAnvil = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.AccessTo.Anvil", false);
		GAMERULE_UseVanillaAccessToBrewingStand = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.AccessTo.BrewingStand", false);
		GAMERULE_UseVanillaAccessToBlastFurnace = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.AccessTo.BlastFurnace", false);
		GAMERULE_UseVanillaAccessToCampfire = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.AccessTo.Campfire", false);
		GAMERULE_UseVanillaAccessToCartographyTable = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.AccessTo.CartographyTable", false);
		GAMERULE_UseVanillaAccessToComposter = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.AccessTo.Composter", false);
		GAMERULE_UseVanillaAccessToCrafingTable = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.AccessTo.CrafingTable", false);
		GAMERULE_UseVanillaAccessToGrindstone = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.AccessTo.Grindstone", false);
		GAMERULE_UseVanillaAccessToEnchantingTable = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.AccessTo.EnchantingTable", false);
		GAMERULE_UseVanillaAccessToFurnace = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.AccessTo.Furnace", false);
		GAMERULE_UseVanillaAccessToSmithingTable = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.AccessTo.SmithingTable", false);
		GAMERULE_UseVanillaAccessToSmoker = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.AccessTo.Smoker", false);
		GAMERULE_UseVanillaAccessToStoneCutter = plugin.getYamlHandler().getConfig().getBoolean("Gamerule.UseVanilla.AccessTo.StoneCutter", false);
	}
	
	public CountType getCountPermType()
	{
		String s = plugin.getYamlHandler().getConfig().getString("Mechanic.CountPerm", "HIGHEST");
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
	
	public boolean isMechanicModifierEnabled()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("EnableMechanic.Modifier", false);
	}
	
	public boolean isMechanicValueEntryEnabled()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("EnableMechanic.ValueEntry", false);
	}
	
	public List<String> getAutoResearchedTechnologies()
	{
		if(plugin.getYamlHandler().getConfig().get("Do.NewPlayer.AutoResearchTechnology") != null)
		{
			return plugin.getYamlHandler().getConfig().getStringList("Do.NewPlayer.AutoResearchTechnology");
		}
		return new ArrayList<>();
	}
	
	public boolean overrideAlreadyRegisteredBlocks()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Do.Block.OverrideAlreadyRegisteredBlocks", false);
	}
	
	public long loseDropItemOwnershipAfterTime()
	{
		return plugin.getYamlHandler().getConfig().getLong("Do.Item.LoseDropItemOwnershipAfterTimeInSeconds", 300);
	}
	
	public List<String> activeEvents()
	{
		if(plugin.getYamlHandler().getConfig().get("Do.Reward.ActiveEvents") != null)
		{
			return plugin.getYamlHandler().getConfig().getStringList("Do.Reward.ActiveEvents");
		}
		return new ArrayList<>();
	}
	
	public boolean ifBlockIsManuallyPlacedBefore_RewardItByBreaking()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Do.Reward.Placing.IfBlockIsManuallyPlacedBefore_RewardItByBreaking", false);
	}
	
	public boolean trackPlacedBlocks()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Do.Reward.Placing.TrackPlacedBlock", false);
	}
	
	public String placedBlocksExpirationDate()
	{
		return plugin.getYamlHandler().getConfig().getString("Do.Reward.Placing.PlacedBlockExpirationDate", "365d-0H-0m-0s");
	}
	
	public boolean finishBrewIfPlayerHasNotTheRecipeUnlocked()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Do.Reward.Brewing.FinishBrewIfPlayerHasNotTheRecipeUnlocked", false);
	}
	
	public boolean finishSmeltIfPlayerHasNotTheRecipeUnlocked()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Do.Reward.Smelting.FinishSmeltIfPlayerHasNotTheRecipeUnlocked", false);
	}
	
	public long rewardPayoutRepetitionRateForOnlinePlayer()
	{
		return 20L*plugin.getYamlHandler().getConfig().getLong("Do.Reward.Payout.RepetitionRateForOnlinePlayersInSeconds", 30);
	}
	
	public boolean rewardPayoutForOfflinePlayerActive()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Do.Reward.Payout.ForOfflinePlayerActive", false);
	}
	
	public long rewardPayoutRepetitionRateForOfflinePlayer()
	{
		return 20L*plugin.getYamlHandler().getConfig().getLong("Do.Reward.Payout.RepetitionRateForOfflinePlayersInSeconds", 60*30);
	}
	
	public double rewardPayoutTaxInPercent()
	{
		return plugin.getYamlHandler().getConfig().getLong("Do.Reward.Payout.TaxInPercent", 0);
	}
	
	public boolean rewardPayoutIfAfkFurnaceAndBrewingStand()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Do.Reward.Payout.IfAfk.FuranceAndBrewingStand", false);
	}
	
	public boolean dropsIfEntitySpawnedFromSpawner()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Do.Reward.Payout.EntitySpawnedFromSpawner", false);
	}
	
	public boolean fillNotDefineGuiSlots()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Gui.FillNotDefineGuiSlots", true);
	}
	
	public Material fillerItemMaterial()
	{
		return Material.valueOf(plugin.getConfig().getString("Gui.FillerItemMaterial", "LIGHT_GRAY_STAINED_GLASS_PANE"));
	}
	
	public boolean jobsRebornImportIsActive()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Do.Import.JobsReborn.Active", false);
	}
	
	public int jobsRebornImportMaxJobsPerPlayer()
	{
		return plugin.getYamlHandler().getConfig().getInt("Do.Import.JobsReborn.MaxJobsPerPlayer", 3);
	}
	
	public boolean accessMainCategory_IfCreative()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Do.Access.MainCategory.BypassIfCreative", true);
	}
	
	public boolean accessSubCategory_IfCreative()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Do.Access.SubCategory.BypassIfCreative", true);
	}
	
	public boolean accessTechnology_IfCreative()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Do.Access.Technology.BypassIfCreative", true);
	}
	
	public int getUpkeepActiveLvl()
	{
		return plugin.getYamlHandler().getConfig().getInt("Group.DailyUpkeep.ActiveFromLevel", 2);
	}
	
	public boolean breakingThroughVanillaDropBarrier()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Gamerule.Drops.BreakingThroughVanillaDropBarrier", true);
	}
	
	public String getSwitchModeCooldownNormalPlayer()
	{
		return plugin.getYamlHandler().getConfig().getString("SwitchMode.Cooldown.NormalPlayer", "14d-0H-0m-0s");
	}
	
	public String getSwitchModeCooldownVIP()
	{
		return plugin.getYamlHandler().getConfig().getString("SwitchMode.Cooldown.VIP", "1d-0H-0m-0s");
	}
	
	public String getSwitchModeCooldownVIPPermission()
	{
		return plugin.getYamlHandler().getConfig().getString("SwitchMode.Cooldown.VIPPermission", "tt.switchmode.cooldown");
	}
}