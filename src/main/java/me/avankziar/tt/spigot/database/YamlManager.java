package main.java.me.avankziar.tt.spigot.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingTransformRecipe;
import org.bukkit.inventory.SmithingTrimRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;

import main.java.me.avankziar.tt.spigot.database.Language.ISO639_2B;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickFunctionType;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickType;
import main.java.me.avankziar.tt.spigot.gui.objects.GuiType;
import main.java.me.avankziar.tt.spigot.gui.objects.SettingsLevel;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.Bypass;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.RewardType;
import main.java.me.avankziar.tt.spigot.objects.TechnologyType;

public class YamlManager
{
	private ISO639_2B languageType = ISO639_2B.GER;
	//The default language of your plugin. Mine is german.
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	
	//Per Flatfile a linkedhashmap.
	private static LinkedHashMap<String, Language> configSpigotKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> mvelanguageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<GuiType, LinkedHashMap<String, Language>> guiKeys = new LinkedHashMap<>();
	
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> itemGeneratorKeys = new LinkedHashMap<>();
	private static LinkedHashMap<PlayerAssociatedType, LinkedHashMap<String, LinkedHashMap<String, Language>>> mainCategoryKeys = new LinkedHashMap<>();
	private static LinkedHashMap<PlayerAssociatedType, LinkedHashMap<String, LinkedHashMap<String, Language>>> subCategoryKeys = new LinkedHashMap<>();
	private static LinkedHashMap<PlayerAssociatedType, LinkedHashMap<String, LinkedHashMap<String, Language>>> technologyKeys = new LinkedHashMap<>();
	
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> blastingRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> campfireRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> furnaceRecipeKeys = new LinkedHashMap<>();
	//private static LinkedHashMap<String, LinkedHashMap<String, Language>> merchantRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> shapedRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> shapelessRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> smithingTransformRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> smithingTrimRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> smokingRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> stonecuttingRecipeKeys = new LinkedHashMap<>();
	
	public YamlManager()
	{
		initConfig();
		initCommands();
		initLanguage();
		initModifierValueEntryLanguage(); //TODO
		initGuiStart();
		initGuiMainCat();
		initGuiSubCat();
		initGuiTechnology();
		initItemGenerator();
		initMainCategory();
		initTechnology();
		initRecipe();
	}
	
	public ISO639_2B getLanguageType()
	{
		return languageType;
	}

	public void setLanguageType(ISO639_2B languageType)
	{
		this.languageType = languageType;
	}
	
	public ISO639_2B getDefaultLanguageType()
	{
		return defaultLanguageType;
	}
	
	public LinkedHashMap<String, Language> getConfigSpigotKey()
	{
		return configSpigotKeys;
	}
	
	public LinkedHashMap<String, Language> getCommandsKey()
	{
		return commandsKeys;
	}
	
	public LinkedHashMap<String, Language> getLanguageKey()
	{
		return languageKeys;
	}
	
	public LinkedHashMap<String, Language> getGuiKey(GuiType guiType)
	{
		return guiKeys.get(guiType);
	}
	
	public LinkedHashMap<String, Language> getModifierValueEntryLanguageKey()
	{
		return mvelanguageKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getItemGeneratorKey()
	{
		return itemGeneratorKeys;
	}
	
	public LinkedHashMap<PlayerAssociatedType, LinkedHashMap<String, LinkedHashMap<String, Language>>> getMainCategoryKey()
	{
		return mainCategoryKeys;
	}
	
	public LinkedHashMap<PlayerAssociatedType, LinkedHashMap<String, LinkedHashMap<String, Language>>> getSubCategoryKey()
	{
		return subCategoryKeys;
	}
	
	public LinkedHashMap<PlayerAssociatedType, LinkedHashMap<String, LinkedHashMap<String, Language>>> getTechnologyKey()
	{
		return technologyKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getBlastingRecipeKey()
	{
		return blastingRecipeKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getCampfireRecipeKey()
	{
		return campfireRecipeKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getFurnaceRecipeKey()
	{
		return furnaceRecipeKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getShapedRecipeKey()
	{
		return shapedRecipeKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getShapelessRecipeKey()
	{
		return shapelessRecipeKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getSmithingTransformRecipeKey()
	{
		return smithingTransformRecipeKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getSmithingTrimRecipeKey()
	{
		return smithingTrimRecipeKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getSmokingRecipeKey()
	{
		return smokingRecipeKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getStonecuttingRecipeKey()
	{
		return stonecuttingRecipeKeys;
	}
	
	/*
	 * The main methode to set all paths in the yamls.
	 */
	public void setFileInput(YamlConfiguration yml, LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType)
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", ""));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(((String) o).replace("\r\n", ""));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	public void initConfig() //INFO:Config
	{
		configSpigotKeys.put("useIFHAdministration"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("IFHAdministrationPath"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"tt"}));
		/*
		 * The normale single path. In the config make it no sense to add other language as English
		 * But the ISO639_2B is here the default language from this plugin!
		 */
		configSpigotKeys.put("ServerName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hub"}));
		
		configSpigotKeys.put("Mysql.Status"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Mysql.Host"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"127.0.0.1"}));
		configSpigotKeys.put("Mysql.Port"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				3306}));
		configSpigotKeys.put("Mysql.DatabaseName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"mydatabase"}));
		configSpigotKeys.put("Mysql.SSLEnabled"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Mysql.AutoReconnect"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Mysql.VerifyServerCertificate"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Mysql.User"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"admin"}));
		configSpigotKeys.put("Mysql.Password"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"not_0123456789"}));
		
		configSpigotKeys.put("EnableMechanic.Modifier"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("EnableMechanic.ValueEntry"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("EnableMechanic.CommandToBungee"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("EnableMechanic.ConditionQueryParser"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		
		configSpigotKeys.put("EnableCommands.Base"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		
		configSpigotKeys.put("ValueEntry.OverrulePermission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Gamerule.UseVanilla.ExpDrops"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Gamerule.UseVanilla.ItemDrops"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Do.Access.MainCategory.BypassIfCreative"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.Access.SubCategory.BypassIfCreative"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.Access.Technology.BypassIfCreative"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.Block.OverrideAlreadyRegisteredBlocks"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Do.Drops.UsePluginDropsCalculation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.DeleteExpireTechnologies.TaskRunInMinutes"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				10}));
		configSpigotKeys.put("Do.DeleteExpirePlacedBlocks.TaskRunInMinutes"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				15}));
		configSpigotKeys.put("Do.Drops.DoNotUsePluginDropsCalculationWorlds"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hubdummy",
				"spawncitydummy"}));
		configSpigotKeys.put("Do.Drops.BreakingThroughVanillaDropBarrier"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));		
		configSpigotKeys.put("Do.Gui.FillNotDefineGuiSlots"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.Gui.FillNotDefineGuiSlots"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				Material.LIGHT_GRAY_STAINED_GLASS_PANE.toString()}));
		configSpigotKeys.put("Do.Item.LoseDropItemOwnershipAfterTimeInSeconds"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				300}));
		configSpigotKeys.put("Do.NewPlayer.ShowSyncMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.NewPlayer.ShowRewardMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.NewPlayer.AutoResearchTechnology"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"soil_I",
				"oaklog"}));
		configSpigotKeys.put("Do.Recipe.LoadThePluginRecipe"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.Recipe.HaveAllRecipeUnlocked"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		List<EventType> eventTypeList = new ArrayList<EventType>(EnumSet.allOf(EventType.class));
		ArrayList<String> eventTypeListII = new ArrayList<>();
		for(EventType e : eventTypeList) {eventTypeListII.add(e.toString());}
		configSpigotKeys.put("Do.Reward.ActiveEvents"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				eventTypeListII.toArray(new String[eventTypeListII.size()])}));
		configSpigotKeys.put("Do.Reward.Payout.RepetitionRateForOnlinePlayersInSeconds"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				60}));
		configSpigotKeys.put("Do.Reward.Payout.ForOfflinePlayerActive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Do.Reward.Payout.RepetitionRateForOfflinePlayersInSeconds"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				60}));
		configSpigotKeys.put("Do.Reward.Payout.TaxInPercent"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1.0}));
		configSpigotKeys.put("Do.Reward.Placing.TrackPlacedBlock"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.Reward.Placing.PlacedBlockExpirationDate"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"365d-0H-0m-0s"}));
		configSpigotKeys.put("Do.Reward.Placing.IfBlockIsManuallyPlacedBefore_RewardItByBreaking"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Do.Reward.Brewing.FinishBrewIfPlayerHasNotTheRecipeUnlocked"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Do.Reward.Smelting.StartSmeltIfPlayerIsNotOnline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.Reward.Smelting.FinishSmeltIfPlayerHasNotTheRecipeUnlocked"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Do.TechnologyPoll.ProcessPollOnMainServer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Do.TechnologyPoll.DaysOfTheMonth_ToProcessThePoll"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"GROUP:18-00:WEDNESDAY:FIRST",
				"GROUP:18-00:14",
				"GROUP:18-00:WEDNESDAY:THIRD",
				"GLOBAL:20-00:SUNDAY:SECOND",
				"GLOBAL:20-00:SUNDAY:FOURTH"}));
	}
	
	@SuppressWarnings("unused") //INFO:Commands
	public void initCommands()
	{
		comBypass();
		String path = "";
		commandsInput("tt", "tt", "tt.cmd.tt", 
				"/tt [pagenumber]", "/tt ", false,
				"&c/tt &f| Infoseite für alle Befehle.",
				"&c/tt &f| Info page for all commands.",
				"&bBefehlsrecht für &f/tt",
				"&bCommandright for &f/tt",
				"&eBasisbefehl für das TechnologyTree Plugin.",
				"&eGroundcommand for the TechnologyTree Plugin.");
		String basePermission = "tt.cmd.tt";
		argumentInput("tt_techinfo", "techinfo", basePermission,
				"/tt techinfo <technology> [level]", "/tt techinfo ", false,
				"&c/tt techinfo <technology> [level] &f| Ein InfoBefehl für Technologien. Bei keiner Angabe eines Level, wird nach dem Spieler geschaut, welche Level er nun zu erforschen hätte.",
				"&c/tt techinfo <technology> [level] &f| An info command for technologies. If no level is specified, the system looks for the player to see which levels they should research.",
				"&bBefehlsrecht für &f/tt techinfo",
				"&bCommandright for &f/tt techinfo",
				"&eEin InfoBefehl für Technologien. Bei keiner Angabe eines Level, wird nach dem Spieler geschaut, welche Level er nun zu erforschen hätte.",
				"&eAn info command for technologies. If no level is specified, the system looks for the player to see which levels they should research..");
		argumentInput("tt_checkeventaction", "checkeventaction", basePermission,
				"/tt checkeventaction", "/tt checkeventaction ", false,
				"&c/tt checkeventaction &f| Toggelt eine Infonachricht, die über ausgeführete Aktion und bestimmte involvierte Dinge berichtet.",
				"&c/tt checkeventaction &f| Checks the viewed block to see if it has been placed. (And therefore no longer counts for rewards)",
				"&bBefehlsrecht für &f/tt checkeventaction",
				"&bCommandright for &f/tt checkeventaction",
				"&eToggelt eine Infonachricht, die über ausgeführete Aktion und bestimmte involvierte Dinge berichtet.",
				"&eToggles an info message that reports on the action performed and certain things involved.");
		argumentInput("tt_checkplacedblocks", "checkplacedblocks", basePermission,
				"/tt checkplacedblocks", "/tt checkplacedblocks ", false,
				"&c/tt checkplacedblocks &f| Checkt den angeschauten block ob er plaziert wurde. (Und somit für Belohnungen nicht mehr zählt)",
				"&c/tt checkplacedblocks &f| Checks the viewed block to see if it has been placed. (And therefore no longer counts for rewards)",
				"&bBefehlsrecht für &f/tt checkplacedblocks",
				"&bCommandright for &f/tt checkplacedblocks",
				"&eCheckt den angeschauten block ob er plaziert wurde. (Und somit für Belohnungen nicht mehr zählt)",
				"&eChecks the viewed block to see if it has been placed. (And therefore no longer counts for rewards)");
		commandsInput("techgui", "techgui", "techgui.command.techgui", 
				"/techgui", "/techgui ", false,
				"&c/techgui &f| Infoseite für alle Befehle.",
				"&c/techgui &f| Info page for all commands.",
				"&bBefehlsrecht für &f/techgui",
				"&bCommandright for &f/techgui",
				"&eGuizugriffsbefehl für das TechnologyTree Plugin.",
				"&eGuiAccesscommand for the TechnologyTree Plugin.");
	}
	
	private void comBypass() //INFO:ComBypass
	{
		List<Bypass.Permission> list = new ArrayList<Bypass.Permission>(EnumSet.allOf(Bypass.Permission.class));
		for(Bypass.Permission ept : list)
		{
			commandsKeys.put("Bypass."+ept.toString()
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"tt."+ept.toString().toLowerCase().replace("_", ".")}));
		}
		
		List<Bypass.Counter> list2 = new ArrayList<Bypass.Counter>(EnumSet.allOf(Bypass.Counter.class));
		for(Bypass.Counter ept : list2)
		{
			if(!ept.forPermission())
			{
				continue;
			}
			commandsKeys.put("Count."+ept.toString()
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"tt."+ept.toString().toLowerCase().replace("_", ".")}));
		}
	}
	
	private void commandsInput(String path, String name, String basePermission, 
			String suggestion, String commandString, boolean putUpCmdPermToValueEntrySystem,
			String helpInfoGerman, String helpInfoEnglish,
			String dnGerman, String dnEnglish,
			String exGerman, String exEnglish)
	{
		commandsKeys.put(path+".Name"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				name}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
		commandsKeys.put(path+".ValueEntry.PutUpCommandPerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				putUpCmdPermToValueEntrySystem}));
		commandsKeys.put(path+".ValueEntry.Displayname"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				dnGerman,
				dnEnglish}));
		commandsKeys.put(path+".ValueEntry.Explanation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				exGerman,
				exEnglish}));
	}
	
	private void argumentInput(String path, String argument, String basePermission, 
			String suggestion, String commandString, boolean putUpCmdPermToValueEntrySystem,
			String helpInfoGerman, String helpInfoEnglish,
			String dnGerman, String dnEnglish,
			String exGerman, String exEnglish)
	{
		commandsKeys.put(path+".Argument"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				argument}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission+"."+argument}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
		commandsKeys.put(path+".ValueEntry.PutUpCommandPerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				putUpCmdPermToValueEntrySystem}));
		commandsKeys.put(path+".ValueEntry.Displayname"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				dnGerman,
				dnEnglish}));
		commandsKeys.put(path+".ValueEntry.Explanation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				exGerman,
				exEnglish}));
	}
	
	public void initLanguage() //INFO:Languages
	{
		languageKeys.put("InputIsWrong",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine Eingabe ist fehlerhaft! Klicke hier auf den Text, um weitere Infos zu bekommen!",
						"&cYour input is incorrect! Click here on the text to get more information!"}));
		languageKeys.put("NoPermission",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast dafür keine Rechte!",
						"&cYou dont not have the rights!"}));
		languageKeys.put("NoPlayerExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler existiert nicht!",
						"&cThe player does not exist!"}));
		languageKeys.put("NoNumber",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%value% &cmuss eine ganze Zahl sein.",
						"&cThe argument &f%value% &must be an integer."}));
		languageKeys.put("NoDouble",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%value% &cmuss eine Gleitpunktzahl sein!",
						"&cThe argument &f%value% &must be a floating point number!"}));
		languageKeys.put("IsNegativ",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%value% &cmuss eine positive Zahl sein!",
						"&cThe argument &f%value% &must be a positive number!"}));
		languageKeys.put("GeneralHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick mich!",
						"&eClick me!"}));
		languageKeys.put("Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6TechnologyTree&7]&e=====",
						"&e=====&7[&6TechnologyTree&7]&e====="}));
		languageKeys.put("Next", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e&nnächste Seite &e==>",
						"&e&nnext page &e==>"}));
		languageKeys.put("Past", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e<== &nvorherige Seite",
						"&e<== &nprevious page"}));
		languageKeys.put("IsTrue", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&a✔",
						"&a✔"}));
		languageKeys.put("IsFalse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&c✖",
						"&c✖"}));
		initCommandsLang();
		initPlayerHandlerLang();
		initBlockHandlerLang();
		initGuiHandlerLang();
		initRewardHandlerLang();
	}
	
	public void initCommandsLang() //INFO:CommandsLang
	{
		String path = "Commands.";
		languageKeys.put(path+"PlayerNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler %player% existiert nicht!",
						"&cThe player %player% does not exist!"}));
		languageKeys.put(path+"TechInfo.TechNotFound", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDie Technologie %tech% existiert nicht!",
						"&cThe %tech% technology does not exist!"}));
		languageKeys.put(path+"CheckPlacedBlocks.NoBlockInSight", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu schaust nur in die Luft! Und Luft kann man nicht setzten.",
						"&cYou are just looking at the air! And you cant place air."}));
		languageKeys.put(path+"CheckPlacedBlocks.WasPlaced", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Block &cwurde schon mal von einem anderen Spieler gesetzt!",
						"&eThe block &chas already been set by another player!"}));
		languageKeys.put(path+"CheckPlacedBlocks.WasNotPlaced", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Block &aist natürlich entstanden und nicht von einem Spieler gesetzt worden!",
						"&eThe block &awas created naturally and was not placed by a player!"}));
		languageKeys.put(path+"CheckEventAction.Added", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu bekommst nun eine Infonachricht, bei einer ausgeführten Aktion, welche Aktion dies war und welche Dinge involviert sind.",
						"&eYou will now receive an information message when an action is carried out, which action it was and which things are involved."}));
		languageKeys.put(path+"CheckEventAction.Removed", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu bekommst nun keine Infonachricht über die ausgeführte Aktion mehr.",
						"&eYou will no longer receive an info message about the action performed."}));
		languageKeys.put(path+"CheckEventAction.BREAKING.Return", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cAktionsabbruch! &eEventType: &#ff8c00%eventtype%&e, Werkzeug: &#ff8c00%tool%&e, Block: &#ff8c00%block%",
						"&7[CheckEventAction] &cCancel action! &eEventType: &#ff8c00%eventtype%&e, Tool: &#ff8c00%tool%&e, Block: &#ff8c00%block%"}));
		languageKeys.put(path+"CheckEventAction.BREAKING.CantAccess", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cZugriff zur Aktion wurde verweigert! &eEventType: &#ff8c00%eventtype%&e, Werkzeug: &#ff8c00%tool%&e, Block: &#ff8c00%block%",
						"&7[CheckEventAction] &cAccess to the action was denied! &eEventType: &#ff8c00%eventtype%&e, Tool: &#ff8c00%tool%&e, Block: &#ff8c00%block%"}));
		languageKeys.put(path+"CheckEventAction.BREAKING.PlacedBlock", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cBlock war von Spieler platziert! Nur Items droppen! &eEventType: &#ff8c00%eventtype%&e, Werkzeug: &#ff8c00%tool%&e, Block: &#ff8c00%block%",
						"&7[CheckEventAction] &cBlock was placed by player! Only items drop! &eEventType: &#ff8c00%eventtype%&e, Tool: &#ff8c00%tool%&e, Block: &#ff8c00%block%"}));
		languageKeys.put(path+"CheckEventAction.BREAKING.Reward", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cAktion erfolgreich! &eEventType: &#ff8c00%eventtype%&e, Werkzeug: &#ff8c00%tool%&e, Block: &#ff8c00%block%",
						"&7[CheckEventAction] &cAction successful! &eEventType: &#ff8c00%eventtype%&e, Tool: &#ff8c00%tool%&e, Block: &#ff8c00%block%"}));
		languageKeys.put(path+"CheckEventAction.BREEDING.Return", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cAktionsabbruch! &eEventType: &#ff8c00%eventtype%&e, EntityType: &#ff8c00%entitytype%&e, Material: &#ff8c00%material%",
						"&7[CheckEventAction] &cCancel action! &eEventType: &#ff8c00%eventtype%&e, EntityType: &#ff8c00%entitytype%&e, Material: &#ff8c00%material%"}));
		languageKeys.put(path+"CheckEventAction.BREEDING.CantAccess", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cZugriff zur Aktion wurde verweigert! &eEventType: &#ff8c00%eventtype%&e, EntityType: &#ff8c00%entitytype%&e, Material: &#ff8c00%material%",
						"&7[CheckEventAction] &cAccess to the action was denied! &eEventType: &#ff8c00%eventtype%&e, EntityType: &#ff8c00%entitytype%&e, Material: &#ff8c00%material%"}));
		languageKeys.put(path+"CheckEventAction.BREEDING.Reward", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cAktion erfolgreich! &eEventType: &#ff8c00%eventtype%&e, EntityType: &#ff8c00%entitytype%&e, Material: &#ff8c00%material%",
						"&7[CheckEventAction] &cAction successful! &eEventType: &#ff8c00%eventtype%&e, EntityType: &#ff8c00%entitytype%&e, Material: &#ff8c00%material%"}));
		languageKeys.put(path+"CheckEventAction.COLD_FORGING.Return", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cAktionsabbruch! &eEventType: &#ff8c00%eventtype%&e, Rezept: &#ff8c00%r%&e",
						"&7[CheckEventAction] &cCancel action! &eEventType: &#ff8c00%eventtype%&e, Recipe: &#ff8c00%r%&e"}));
		languageKeys.put(path+"CheckEventAction.COLD_FORGING.CantAccess", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cZugriff zur Aktion wurde verweigert! &eEventType: &#ff8c00%eventtype%&e, Rezept: &#ff8c00%r%&e",
						"&7[CheckEventAction] &cAccess to the action was denied! &eEventType: &#ff8c00%eventtype%&e, Recipe: &#ff8c00%r%&e"}));
		languageKeys.put(path+"CheckEventAction.CRAFTING.Return", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cAktionsabbruch! &eEventType: &#ff8c00%eventtype%&e, Rezept: &#ff8c00%r%&e",
						"&7[CheckEventAction] &cCancel action! &eEventType: &#ff8c00%eventtype%&e, Recipe: &#ff8c00%r%&e"}));
		languageKeys.put(path+"CheckEventAction.CRAFTING.CantAccess", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cZugriff zur Aktion wurde verweigert! &eEventType: &#ff8c00%eventtype%&e, Rezept: &#ff8c00%r%&e",
						"&7[CheckEventAction] &cAccess to the action was denied! &eEventType: &#ff8c00%eventtype%&e, Recipe: &#ff8c00%r%&e"}));
		languageKeys.put(path+"CheckEventAction.GRINDING.Return", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cAktionsabbruch! &eEventType: &#ff8c00%eventtype%&e, Rezept: &#ff8c00%r%&e",
						"&7[CheckEventAction] &cCancel action! &eEventType: &#ff8c00%eventtype%&e, Recipe: &#ff8c00%r%&e"}));
		languageKeys.put(path+"CheckEventAction.GRINDING.CantAccess", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cZugriff zur Aktion wurde verweigert! &eEventType: &#ff8c00%eventtype%&e, Rezept: &#ff8c00%r%&e",
						"&7[CheckEventAction] &cAccess to the action was denied! &eEventType: &#ff8c00%eventtype%&e, Recipe: &#ff8c00%r%&e"}));
		languageKeys.put(path+"CheckEventAction.INTERACT.Return", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cAktionsabbruch! &eEventType: &#ff8c00%eventtype%&e, Werkzeug: &#ff8c00%tool%&e, Block: &#ff8c00%block%",
						"&7[CheckEventAction] &cCancel action! &eEventType: &#ff8c00%eventtype%&e, Tool: &#ff8c00%tool%&e, Block: &#ff8c00%block%"}));
		languageKeys.put(path+"CheckEventAction.INTERACT.BypassInteraction", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &eInteraktion Bypass! &eEventType: &#ff8c00%eventtype%&e, Werkzeug: &#ff8c00%tool%&e, Block: &#ff8c00%block%",
						"&7[CheckEventAction] &eInteraction bypass! &eEventType: &#ff8c00%eventtype%&e, Tool: &#ff8c00%tool%&e, Block: &#ff8c00%block%"}));
		languageKeys.put(path+"CheckEventAction.INTERACT.EventNotFound", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cEvent beim Interagier nicht gefunden! &eWerkzeug: &#ff8c00%tool%&e, Block: &#ff8c00%block%",
						"&7[CheckEventAction] &cEvent not found when interacting! &eTool: &#ff8c00%tool%&e, Block: &#ff8c00%block%"}));
		languageKeys.put(path+"CheckEventAction.INTERACT.Reward",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &aAktion erfolgreich! &eEventType: &#ff8c00%eventtype%&e, Werkzeug: &#ff8c00%tool%&e, Block: &#ff8c00%block%",
						"&7[CheckEventAction] &aAction successful! &eEventType: &#ff8c00%eventtype%&e, Tool: &#ff8c00%tool%&e, Block: &#ff8c00%block%"}));
		languageKeys.put(path+"CheckEventAction.INTERACTENTITY.Return", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cAktionsabbruch oder Event konnte nicht gefunden werden! &eEventType: &#ff8c00%eventtype%&e, Werkzeug: &#ff8c00%tool%&e, EntityType: &#ff8c00%entitytype%",
						"&7[CheckEventAction] &cCancel action or event couldnt found! &eEventType: &#ff8c00%eventtype%&e, Tool: &#ff8c00%tool%&e, EntityType: &#ff8c00%entitytype%"}));
		languageKeys.put(path+"CheckEventAction.INTERACTENTITY.CantAccess", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cZugriff zur Aktion wurde verweigert! &eEventType: &#ff8c00%eventtype%&e, Werkzeug: &#ff8c00%tool%&e, EntityType: &#ff8c00%entitytype%",
						"&7[CheckEventAction] &cAccess to the action was denied! &eEventType: &#ff8c00%eventtype%&e, Tool: &#ff8c00%tool%&e, EntityType: &#ff8c00%entitytype%"}));
		languageKeys.put(path+"CheckEventAction.INTERACTENTITY.Reward", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &aAktion erfolgreich! &eEventType: &#ff8c00%eventtype%&e, Werkzeug: &#ff8c00%tool%&e, EntityType: &#ff8c00%entitytype%",
						"&7[CheckEventAction] &aAction successful! &eEventType: &#ff8c00%eventtype%&e, Tool: &#ff8c00%tool%&e, EntityType: &#ff8c00%entitytype%"}));
		languageKeys.put(path+"CheckEventAction.PLACING.Return", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cAktionsabbruch! &eEventType: &#ff8c00%eventtype%&e, Werkzeug: &#ff8c00%tool%&e, Block: &#ff8c00%block%",
						"&7[CheckEventAction] &cCancel action! &eEventType: &#ff8c00%eventtype%&e, Tool: &#ff8c00%tool%&e, Block: &#ff8c00%block%"}));
		languageKeys.put(path+"CheckEventAction.PLACING.Reward", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &aAktion erfolgreich! &eEventType: &#ff8c00%eventtype%&e, Werkzeug: &#ff8c00%tool%&e, Block: &#ff8c00%block%",
						"&7[CheckEventAction] &aAction successful! &eEventType: &#ff8c00%eventtype%&e, Tool: &#ff8c00%tool%&e, Block: &#ff8c00%block%"}));
		languageKeys.put(path+"CheckEventAction.SMITHING.Return", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cAktionsabbruch! &eEventType: &#ff8c00%eventtype%&e, Rezept: &#ff8c00%r%&e",
						"&7[CheckEventAction] &cCancel action! &eEventType: &#ff8c00%eventtype%&e, Recipe: &#ff8c00%r%&e"}));
		languageKeys.put(path+"CheckEventAction.SMITHING.CantAccess", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[CheckEventAction] &cZugriff zur Aktion wurde verweigert! &eEventType: &#ff8c00%eventtype%&e, Rezept: &#ff8c00%r%&e",
						"&7[CheckEventAction] &cAccess to the action was denied! &eEventType: &#ff8c00%eventtype%&e, Recipe: &#ff8c00%r%&e"}));
	}
	
	private void initPlayerHandlerLang() //INFO:PlayerHandlerLang
	{
		String path = "PlayerHandler.";
		languageKeys.put(path+"SyncStart", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&#ff8c00TT&7] &#c6a664Sync ist gestartet...",
						"&7[&#ff8c00TT&7] &#c6a664Sync started..."}));
		languageKeys.put(path+"SyncEnd", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&#ff8c00TT&7] &#c6a664Sync ist komplett!",
						"&7[&#ff8c00TT&7] &#c6a664Sync is complete!"}));
		languageKeys.put(path+"PayTechnology.Category", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6Technologiekauf",
						"&6Technologypurchase"}));
		languageKeys.put(path+"PayTechnology.Comment", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bKauf von &e%technology% &bin &7%subcategory%&f/&7%maincategory%",
						"&bPurchase of &e%technology% &bin &7%subcategory%&f/&7%maincategory%"}));
		languageKeys.put(path+"PollTechnology.Category", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6Technologiewahl",
						"&6TechnologyVote"}));
		languageKeys.put(path+"PollTechnology.Comment", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bWahlkosten von &e%technology% &bin &7%subcategory%&f/&7%maincategory%",
						"&bVotecosts of &e%technology% &bin &7%subcategory%&f/&7%maincategory%"}));
		languageKeys.put(path+"PollReTechnology.Category", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6Technologiewahlwiedererstattung",
						"&6TechnologyVoteRefund"}));
		languageKeys.put(path+"PollReTechnology.Comment", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bWahlkostenerstattung von &e%technology% &bin &7%subcategory%&f/&7%maincategory%",
						"&bTechnology choice refund of &e%technology% &bin &7%subcategory%&f/&7%maincategory%"}));
	}
	
	public void initBlockHandlerLang() //INFO:BlockHandlerLang
	{
		String path = "BlockHandler.";
		languageKeys.put(path+"Event.ThirdPartyRegistered", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDieser Block ist schon registeriert!",
						"&cThis block is already registered!"}));
		languageKeys.put(path+"Event.OverrideRegisterBlock", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDieser Block wurde auf dich überschrieben!",
						"&eThis block has been overwritten to you!"}));
		languageKeys.put(path+"Event.NewRegisterBlock", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDieser Block wurde nun auf dich registriert!",
						"&eThis block has now been registered to you!"}));
		languageKeys.put(path+"Event.DeregisterBlock", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer abgebaute Block war auf einem Spieler registriert! Die Registrierung wurde nun entfernt.",
						"&cThe removed block was registered to a player! The registration has now been removed."}));
	}
	
	public void initGuiHandlerLang() //INFO:GuiHandlerLang
	{
		String path = "GuiHandler.";
		languageKeys.put(path+"Main.Title", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eTT Gui",
						"&eTT Gui"}));
		languageKeys.put(path+"MainCategorys.Title", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eHauptkategorien",
						"&eMain Categorys"}));
		languageKeys.put(path+"MainCategorysSubCategorys.Title", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e%maincat% Subkategorien",
						"&e%maincat% Sub Categorys"}));
		languageKeys.put(path+"SubCategorysTechnologys.Title", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e%subcat% Technologien",
						"&e%subcat% Techologys"}));
		languageKeys.put(path+"Technology.NotEnoughTTExp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast nicht genug TTExp!",
						"&cYou dont have enough TTExp!"}));
		languageKeys.put(path+"Technology.NotEnoughVanillaExp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast nicht genug VanillaExp!",
						"&cYou dont have enough TTExp!"}));
		languageKeys.put(path+"Technology.NotEnoughMoney", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast nicht genug Geld!",
						"&cYou dont have enough money!"}));
		languageKeys.put(path+"Technology.NotEnoughMaterial", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast nicht genug Materialien!",
						"&cYou dont have enough materials!"}));
		languageKeys.put(path+"Technology.IsSimpleAndAlreadyResearched", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast die Technologie %tech% schon erforscht!",
						"&cYou have already researched the Technology %tech%!"}));
		languageKeys.put(path+"Technology.MaxResearchLevelAlreadyReached", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das maximale Level der Technologie %tech% erforscht!",
						"&cYou have already researched the maximum level of technology %tech%!"}));
		languageKeys.put(path+"Technology.TechnologyResearched", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6Du hast das Level &f%level% &6der Technologie &f%tech% &6erfolgreich erforscht!",
						"&6You have successfully researched the level &f%level% &6of the technology &f%tech%&6!"}));
		languageKeys.put(path+"Technology.Info.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&f====> &6Tech %tech% &f<====",
						"&f====> &6Tech %tech% &f<===="}));
		languageKeys.put(path+"Technology.Info.Internname", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cInternname: &f%name%",
						"&cInternname: &f%name%"}));
		languageKeys.put(path+"Technology.Info.OverlyingSubCategory", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cÜbergeordnete SubCategorie: &f%name%",
						"&cOverlyingSubCategory: &f%name%"}));
		languageKeys.put(path+"Technology.Info.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&f====> &6Tech %tech% &f<====",
						"&f====> &6Tech %tech% &f<===="}));
		languageKeys.put(path+"Technology.Info.LevelResearched", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cErforschtes Level: &f%lvl%",
						"&cResearched Level: &f%lvl%"}));
		languageKeys.put(path+"Technology.Info.LevelDisplayed", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cTheoretisch Erforschtes Level: &f%lvl%",
						"&cTheoretical researched Level: &f%lvl%"}));
		languageKeys.put(path+"Technology.Info.MaxTechLvlToResearchAndGuiSlot", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cMaxTechLvlToResearch: &f%lvl% &1| &cGuiSlot: &f%slot%",
						"&cMaxTechLvlToResearch: &f%lvl% &1| &cGuiSlot: &f%slot%"}));
		languageKeys.put(path+"Technology.Info.PlayerAssociatedTypeAndTechType", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cPlayerAssociatedType: &f%pat% &1| &cTechType: &f%ttype%",
						"&cPlayerAssociatedType: &f%pat% &1| &cTechType: &f%ttype%"}));
		languageKeys.put(path+"Technology.Info.BoosterExpireTimes", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cNach Erforschungs Verfallszeiten: &f%exp%",
						"&cAccording to research expiration times: &f%exp%"}));
		languageKeys.put(path+"Technology.Info.GlobalTechPollParticipants.Info", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cUninvolvierte Teilnehmer der Umfrage, Belohung in Prozent Hover",
						"&cUninvolved participants in the survey, reward in percent Hover"}));
		languageKeys.put(path+"Technology.Info.GlobalTechPollParticipants.Interaction", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBelohnung Interaktion: &f%v% %",
						"&cReward Interaction: &f%v% %"}));
		languageKeys.put(path+"Technology.Info.GlobalTechPollParticipants.Recipe", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBelohnung Rezepte: &f%v% %",
						"&cReward Recipe: &f%v% %"}));
		languageKeys.put(path+"Technology.Info.GlobalTechPollParticipants.DropChance", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBelohnung DropChance: &f%v% %",
						"&cReward DropChance: &f%v% %"}));
		languageKeys.put(path+"Technology.Info.GlobalTechPollParticipants.SilkTouchDropChance", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBelohnung SilkTouchDropChance: &f%v% %",
						"&cReward SilkTouchDropChance: &f%v% %"}));
		languageKeys.put(path+"Technology.Info.GlobalTechPollParticipants.Commands", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBelohnung Befehle: &f%v% %",
						"&cReward Commands: &f%v% %"}));
		languageKeys.put(path+"Technology.Info.GlobalTechPollParticipants.Items", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBelohnung Items: &f%v% %",
						"&cReward Items: &f%v% %"}));
		languageKeys.put(path+"Technology.Info.GlobalTechPollParticipants.Modifier", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBelohnung Items: &f%v% %",
						"&cReward Items: &f%v% %"}));
		languageKeys.put(path+"Technology.Info.GlobalTechPollParticipants.ValueEntry", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBelohnung Items: &f%v% %",
						"&cReward Items: &f%v% %"}));
		languageKeys.put(path+"Technology.Info.Lvl.CostTTExp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cTTExp Kosten: &e%v% &1| &f(%f%)",
						"&cTTExp Costs: &e%v% &1| &f(%f%)"}));
		languageKeys.put(path+"Technology.Info.Lvl.CostVExp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cVanillaExp Kosten: &e%v% &1| &f(%f%)",
						"&cVanillaExp costs: &e%v% &1| &f(%f%)"}));
		languageKeys.put(path+"Technology.Info.Lvl.CostMoney", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cGeldkosten: &e%v% &1| &f(%f%)",
						"&cMoney costs: &e%v% &1| &f(%f%)"}));
		languageKeys.put(path+"Technology.Info.Lvl.CostMaterial", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cMaterialkosten:",
						"&cMaterial costs:"}));
		languageKeys.put(path+"Technology.Info.Lvl.ResearchRequirementConditionQuery", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAnforderungen für die Forschung:",
						"&cRequirements for research:"}));
		languageKeys.put(path+"Technology.Info.Lvl.RewardInteraction", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cInteraktionsbelohnung:",
						"&cInteraction reward:"}));
		languageKeys.put(path+"Technology.Info.Lvl.RewardRecipe", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cRezeptbelohnung:",
						"&cRecipe reward:"}));
		languageKeys.put(path+"Technology.Info.Lvl.RewardDropChance", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDropchancebelohnung:",
						"&cDropChance reward:"}));
		languageKeys.put(path+"Technology.Info.Lvl.RewardSilkTouchDropChance", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBehutsamkeitDropchancebelohnung:",
						"&cSilkTouchDropChance reward:"}));
		languageKeys.put(path+"Technology.Info.Lvl.RewardCommand", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBefehlsbelohnung:",
						"&cCommand reward:"}));
		languageKeys.put(path+"Technology.Info.Lvl.RewardItem", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cItembelohnung:",
						"&cItem reward:"}));
		languageKeys.put(path+"Technology.Info.Lvl.RewardModifier", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cModifierbelohnung:",
						"&cModifier reward:"}));
		languageKeys.put(path+"Technology.Info.Lvl.RewardValueEntry", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cValueEntrybelohnung:",
						"&cValueEntry reward:"}));
	}
	
	public void initRewardHandlerLang() //INFO:RewardHandlerLang
	{
		String path = "Reward.";
		languageKeys.put(path+"TaskMsg.AllThree", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[%times%] &#c6a664Belohnung verdient: &f%money%&#c6a664, &f%ttexp% &#c6a664TTExp, &f%vaexp% &#c6a664VanillaExp",
						"&7[%times%] &#c6a664Reward earned: &f%money%&#c6a664, &f%ttexp% &#c6a664TTExp, &f%vaexp% &#c6a664VanillaExp"}));
		languageKeys.put(path+"TaskMsg.TTAndVaExp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[%times%] &#c6a664Belohnung verdient: &f%ttexp% &#c6a664TTExp, &f%vaexp% &#c6a664VanillaExp",
						"&7[%times%] &#c6a664Reward earned: &f%ttexp% &#c6a664TTExp, &f%vaexp% &#c6a664VanillaExp"}));
		languageKeys.put(path+"TaskMsg.TTExp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[%times%] &#c6a664Belohnung verdient: &f%ttexp% &#c6a664TTExp",
						"&7[%times%] &#c6a664Reward earned: &f%ttexp% &#c6a664TTExp"}));
	}
	
	public void initModifierValueEntryLanguage() //INFO:ModifierValueEntryLanguages
	{
		mvelanguageKeys.put(Bypass.Permission.SEE_MAIN_CATEGORYS.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eByasspermission um alle Hauptkategorien zu sehen.",
						"&eBypasspermission to see all maincategories."}));
		mvelanguageKeys.put(Bypass.Permission.SEE_MAIN_CATEGORYS.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eByasspermission für",
						"&edas Plugin TechnologyTree.",
						"&eErmöglich das Sehen aller",
						"&eHauptkategorien.",
						"&eBypasspermission for",
						"&ethe plugin TechnologyTree.",
						"&eAllows you to see all",
						"&ethe main categories."}));
		mvelanguageKeys.put(Bypass.Permission.SEE_SUB_CATEGORYS.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eByasspermission um alle Unterkategorien zu sehen.",
						"&eBypasspermission to see all subcategories."}));
		mvelanguageKeys.put(Bypass.Permission.SEE_SUB_CATEGORYS.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eByasspermission für",
						"&edas Plugin TechnologyTree.",
						"&eErmöglich das Sehen aller",
						"&eUnterkategorien.",
						"&eBypasspermission for",
						"&ethe plugin TechnologyTree.",
						"&eAllows you to see all",
						"&ethe sub categories."}));
		mvelanguageKeys.put(Bypass.Counter.REGISTER_BLOCK_.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZählpermission für die Registrierung von Blöcken.",
						"&eCounting mission for the registration of blocks."}));
		mvelanguageKeys.put(Bypass.Counter.REGISTER_BLOCK_.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZählpermission für",
						"&edas Plugin TechnologyTree.",
						"&eHandhabt die Anzahl an Blöcken",
						"&ewelche der Spieler registeriern darf.",
						"&e(Für Furnace/Blastfurnace/Smoker/Campfire)",
						"&eCountpermission for",
						"&ethe plugin TechnologyTree.",
						"&eHandles the number of blocks",
						"&ethe player may register.",
						"&e(For Furnace/Blastfurnace/Smoker/Campfire)"}));
		List<RewardType> rewardTypeList = new ArrayList<RewardType>(EnumSet.allOf(RewardType.class));
		List<EventType> eventTypeList = new ArrayList<EventType>(EnumSet.allOf(EventType.class));
		List<Material> materialList = new ArrayList<Material>(EnumSet.allOf(Material.class));
		List<EntityType> entityTypeList = new ArrayList<EntityType>(EnumSet.allOf(EntityType.class));
		LinkedHashMap<EventType, String> eventTypeMap = new LinkedHashMap<>();
		LinkedHashMap<RewardType, String> rewardTypeMap = new LinkedHashMap<>();
		for(EventType e : eventTypeList)
		{
			String s = EnumHandler.getName(e);
			eventTypeMap.put(e, s);
		}
		for(RewardType r : rewardTypeList)
		{
			String s = EnumHandler.getName(r);
			rewardTypeMap.put(r, s);
		}
		for(Material ma : materialList)
		{
			String mat = EnumHandler.getName(ma);
			for(EventType e : eventTypeList)
			{
				String ev = eventTypeMap.get(e);
				for(RewardType r : rewardTypeList)
				{
					String rt = rewardTypeMap.get(r);
					if(r == RewardType.ACCESS)
					{
						mvelanguageKeys.put(ma.toString()+"."+e.toString()+"."+r.toString()+".Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eValueEntry für das Material %ma%, des Events %e% des Belohnungstyp %r%."
										.replace("%m%", mat).replace("%e%", ev).replace("%r%", rt),
										"&eValueEntry for the material %ma%, of the event %e% of the reward type %r%."
										.replace("%m%", mat).replace("%e%", ev).replace("%r%", rt)}));
						mvelanguageKeys.put(ma.toString()+"."+e.toString()+"."+r.toString()+".Explanation",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eValueEntry für",
										"&edas Plugin TechnologyTree.",
										"&eValueEntry for",
										"&ethe plugin TechnologyTree."}));
					} else
					{
						mvelanguageKeys.put(ma.toString()+"."+e.toString()+"."+r.toString()+".Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eModifier für das Material %ma%, des Events %e% des Belohnungstyp %r%."
										.replace("%m%", mat).replace("%e%", ev).replace("%r%", rt),
										"&eModifier for the material %ma%, of the event %e% of the reward type %r%."
										.replace("%m%", mat).replace("%e%", ev).replace("%r%", rt)}));
						mvelanguageKeys.put(ma.toString()+"."+e.toString()+"."+r.toString()+".Explanation",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eModifier für",
										"&edas Plugin TechnologyTree.",
										"&eModifier for",
										"&ethe plugin TechnologyTree."}));
					}
				}
			}
		}
		for(EntityType et : entityTypeList)
		{
			String ent = EnumHandler.getName(et);
			for(EventType e : eventTypeList)
			{
				String ev = eventTypeMap.get(e);
				for(RewardType r : rewardTypeList)
				{
					String rt = rewardTypeMap.get(r);
					if(r == RewardType.ACCESS)
					{
						mvelanguageKeys.put(et.toString()+"."+e.toString()+"."+r.toString()+".Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eValueEntry für das Entity %ma%, des Events %e% des Belohnungstyp %r%."
										.replace("%m%", ent).replace("%e%", ev).replace("%r%", rt),
										"&eValueEntry for the entity %ma%, of the event %e% of the reward type %r%."
										.replace("%m%", ent).replace("%e%", ev).replace("%r%", rt)}));
						mvelanguageKeys.put(et.toString()+"."+e.toString()+"."+r.toString()+".Explanation",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eValueEntry für",
										"&edas Plugin TechnologyTree.",
										"&eValueEntry for",
										"&ethe plugin TechnologyTree."}));
					} else
					{
						mvelanguageKeys.put(et.toString()+"."+e.toString()+"."+r.toString()+".Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eModifier für das Entity %ma%, des Events %e% des Belohnungstyp %r%."
										.replace("%m%", ent).replace("%e%", ev).replace("%r%", rt),
										"&eModifier for the entity %ma%, of the event %e% of the reward type %r%."
										.replace("%m%", ent).replace("%e%", ev).replace("%r%", rt)}));
						mvelanguageKeys.put(et.toString()+"."+e.toString()+"."+r.toString()+".Explanation",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eModifier für",
										"&edas Plugin TechnologyTree.",
										"&eModifier for",
										"&ethe plugin TechnologyTree."}));
					}
				}
			}
		}
	}
	
	public void initGuiStart() //INFO:GuiStart
	{
		LinkedHashMap<String, Language> start = new LinkedHashMap<>();
		String path = "";
		path = "0"; //SettingsLevelToggle
		start.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		start.put(path+".Material."+SettingsLevel.NOLEVEL.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.OAK_WOOD.toString()}));
		start.put(path+".Material."+SettingsLevel.BASE.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.OAK_WOOD.toString()}));
		start.put(path+".Material."+SettingsLevel.ADVANCED.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.STONE.toString()}));
		start.put(path+".Material."+SettingsLevel.EXPERT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.DIAMOND.toString()}));
		start.put(path+".Material."+SettingsLevel.MASTER.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.NETHERITE_INGOT.toString()}));
		start.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dSwitcht die Gui-Level-Ansicht",
						"&dSwitch the Gui level view"}));
		start.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cLinksklick &bfür das Basis Level.",
						"&cRechtsklick &bfür das Fortgeschrittene Level.",
						"&cShift Linksklick &bfür das Experten Level.",
						"&cShift Rechtsklick &bfür das Meister Level.",
						"&cLeftclick &bfor the basic level.",
						"&cRightclick &bfor the advanced level.",
						"&cShift-Leftclick  &bfor the expert level.",
						"&cShift-Rightclick &bfor the master level."}));
		start.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETTINGSLEVEL_SETTO_BASE.toString()}));
		start.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETTINGSLEVEL_SETTO_ADVANCED.toString()}));
		start.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETTINGSLEVEL_SETTO_EXPERT.toString()}));
		start.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETTINGSLEVEL_SETTO_MASTER.toString()}));
		path = "13"; //Infobutton für die Tech wieviel man hat. Wieviel Exp hat. Und wieviel Registriert Blöcke etc.
		start.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		start.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.BOOK.toString()}));
		start.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&c%player%",
						"&c%player%"}));
		start.put(path+".Lore."+SettingsLevel.BASE.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine freie TTExp: &f%freettexp%",
						
						"&cYour free TTExp: &f%freettexp%"
						}));
		start.put(path+".Lore."+SettingsLevel.ADVANCED.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine freie TTExp: &f%freettexp%",
						"&cDeine verbrauchten TTExp: &f%allocatedttexp%",
						
						"&cYour free TTExp: &f%freettexp%",
						"&cYour used TTExp: &f%allocatedttexp%"
						}));
		start.put(path+".Lore."+SettingsLevel.EXPERT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine freie TTExp: &f%freettexp%",
						"&cDeine verbrauchten TTExp: &f%allocatedttexp%",
						"",
						"&eRegistrierte Blöcke => &ahat man&7/&bkann man haben",
						"&cBraustand: &a%brewing_standfree%&7/&b%brewing_standmax%",
						"&cVerz. Tisch: &a%enchanting_tablefree%&7/&b%enchanting_tablemax%",
						"&cLagerfeuer: &a%campfirefree%&7/&b%campfiremax%",
						"&cOfen: &a%furnacefree%&7/&b%furnacemax%",
						"&cSchmelzofen: &a%blast_furnacefree%&7/&b%blast_furnacemax%",
						"&cRäucherofen: &a%smokerfree%&7/&b%smokermax%",
						
						"&cYour free TTExp: &f%freettexp%",
						"&cYour used TTExp: &f%allocatedttexp%",
						"",
						"&eRegistrated Blocks => &ahave&7/&bcan have",
						"&cBrewing Stand: &a%brewing_standfree%&7/&b%brewing_standmax%",
						"&cEnchanting Table: &a%enchanting_tablefree%&7/&b%enchanting_tablemax%",
						"&cCampfire: &a%campfirefree%&7/&b%campfiremax%",
						"&cFurnance: &a%furnacefree%&7/&b%furnacemax%",
						"&cBlastfurnace: &a%blast_furnacefree%&7/&b%blast_furnacemax%",
						"&cSmoker: &a%smokerfree%&7/&b%smokermax%"
						}));
		start.put(path+".Lore."+SettingsLevel.MASTER.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine freie TTExp: &f%freettexp%",
						"&cDeine verbrauchten TTExp: &f%allocatedttexp%",
						"",
						"&eTechnologie => &ahat man&f/&bexistieren",
						"&cTotal Techs: &a%techhave%&f/&b%techexist%",
						"&cSolo Techs: &a%solotechhave%&f/&b%solotechexist%",
						"&cGruppen Techs: &a%grouptechhave%&f/&b%grouptechexist%",
						"&cGlobale Techs: &a%globaltechhave%&f/&b%globaltechexist%",
						"",
						"&eRegistrierte Blöcke => &ahat man&7/&bkann man haben",
						"&cBraustand: &a%brewing_standhas%&7/&b%brewing_standmax%",
						"&cVerz. Tisch: &a%enchanting_tablehas%&7/&b%enchanting_tablemax%",
						"&cLagerfeuer: &a%campfirehas%&7/&b%campfiremax%",
						"&cOfen: &a%furnacehas%&7/&b%furnacemax%",
						"&cSchmelzofen: &a%blast_furnacehas%&7/&b%blast_furnacemax%",
						"&cRäucherofen: &a%smokerhas%&7/&b%smokermax%",
						
						"&cYour free TTExp: &f%freettexp%",
						"&cYour used TTExp: &f%allocatedttexp%",
						"",
						"&eTechnology => &ahave&f/&bexist",
						"&cTotal Techs: &a%techhave%&f/&b%techexist%",
						"&cSolo Techs: &a%solotechhave%&f/&b%solotechexist%",
						"&cGroup Techs: &a%grouptechhave%&f/&b%grouptechexist%",
						"&cGlobal Techs: &a%globaltechhave%&f/&b%globaltechexist%",
						"",
						"&eRegistrated Blocks => &ahave&7/&bcan have",
						"&cBrewing Stand: &a%brewing_standhas%&7/&b%brewing_standmax%",
						"&cEnchanting Table: &a%enchanting_tablehas%&7/&b%enchanting_tablemax%",
						"&cCampfire: &a%campfirehas%&7/&b%campfiremax%",
						"&cFurnance: &a%furnacehas%&7/&b%furnacemax%",
						"&cBlastfurnace: &a%blast_furnacehas%&7/&b%blast_furnacemax%",
						"&cSmoker: &a%smokerhas%&7/&b%smokermax%"
						}));
		path = "7"; //ShowSyncMsg Button
		start.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		start.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.PAPER.toString()}));
		start.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cSync Nachrichten aus/an",
						"&cSync Message on/off"}));
		start.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dStatus: %syncmsg%",
						"&bToggelt, ob man, größtenteils beim joinen,",
						"&bdie Synchronisationsnachricht vom TT Plugin bekommt.",
						
						"&bStatus: %syncmsg%",
						"&bToggles whether to get, for the most part when joining,",
						"&bthe synchronization message from the TT plugin."
						}));
		start.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.START_SYNCMESSAGE.toString()}));
		start.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.START_SYNCMESSAGE.toString()}));
		path = "8"; //ShowRewardMsg Button
		start.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.ADVANCED.toString()}));
		start.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.PAPER.toString()}));
		start.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBelohungsnachrichten aus/an",
						"&cReward Message on/off"}));
		start.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dStatus: %rewardmsg%",
						"&bToggelt, ob man, nach der Verarbeitung der Belohnungen",
						"&bdie Informationsnachricht vom TT Plugin bekommt.",
						
						"&bStatus: %syncmsg%",
						"&bToggles whether you receive the information message",
						"&bfrom the TT plugin after processing the rewards."}));
		start.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.START_REWARDMESSAGE.toString()}));
		start.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.START_REWARDMESSAGE.toString()}));
		path = "38"; //SOLO MainCat Zugang
		start.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.BASE.toString()}));
		start.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.DIAMOND.toString()}));
		start.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cSolo Hauptkategorien",
						"&cSolo Main Categorys"}));
		start.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bListet alle Hauptkategorien in einem",
						"&bneuen Menu auf, welche du anklicken kannst,",
						"&bum zu dessen Subkategorien und schließlich",
						"&bzu den von dir erforschbaren Technologien zu kommen.",
						
						"&bLists all the main categories in a new menu,",
						"&bwhich you can click to go to its subcategories ",
						"&band finally to the technologies you can research.",
						""
						}));
		start.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.START_MAINCATEGORYS_SOLO.toString()}));
		start.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.START_MAINCATEGORYS_SOLO.toString()}));
		path = "40"; //GROUP MainCat Zugang
		start.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.BASE.toString()}));
		start.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.ENCHANTING_TABLE.toString()}));
		start.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cGruppe Hauptkategorien",
						"&cGroup Main Categorys"}));
		start.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bListet alle Hauptkategorien in einem",
						"&bneuen Menu auf, welche du anklicken kannst,",
						"&bum zu dessen Subkategorien und schließlich",
						"&bzu den von dir und deiner Gruppe",
						"&bzusammen erforschbaren Technologien zu kommen.",
						
						"&bLists all the main categories in a new menu,",
						"&bwhich you can click to go to its subcategories ",
						"&band finally to the technologies",
						"&byou and your group can research together.",
						""
						}));
		start.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.START_MAINCATEGORYS_GROUP.toString()}));
		start.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.START_MAINCATEGORYS_GROUP.toString()}));
		path = "42"; //GLOBAL MainCat Zugang
		start.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.BASE.toString()}));
		start.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.BEACON.toString()}));
		start.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cGlobale Hauptkategorien",
						"&cGlobal Main Categorys"}));
		start.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bListet alle Hauptkategorien in einem",
						"&bneuen Menu auf, welche du anklicken kannst,",
						"&bum zu dessen Subkategorien und schließlich",
						"&bzu den von allen Spielern zusammen",
						"&berforschbaren Technologien zu kommen.",
						
						"&bLists all the main categories in a new menu,",
						"&bwhich you can click to go to its subcategories ",
						"&band finally to the technologies",
						"&ball Player can research together.",
						""
						}));
		start.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.START_MAINCATEGORYS_GLOBAL.toString()}));
		start.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.START_MAINCATEGORYS_GLOBAL.toString()}));		
		guiKeys.put(GuiType.START, start);
		
		//------------------------------------
		/*admin.put(path+".IsInfoItem",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".Permission",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".UseASH",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						false}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&d",
						"&d"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b",
						"&b",
						"&b",
						"&b"}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.CTRL_DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.SWAP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));*/
	}
	
	public void initGuiMainCat() //INFO:GuiMainCat
	{
		LinkedHashMap<String, Language> maincat = new LinkedHashMap<>();
		String path = "";
		path = "53"; //Back
		maincat.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		maincat.put(path+".Material."+SettingsLevel.NOLEVEL.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.ARROW.toString()}));
		maincat.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&c<<< Zurück",
						"&c<<< Back"}));
		maincat.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"",
						"&eKehre zurück zum Start Menu.",
						"",
						"&eReturn to the Start Menu."}));
		maincat.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.RETURN_TOSTART.toString()}));
		maincat.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.RETURN_TOSTART.toString()}));
		guiKeys.put(GuiType.MAIN_CATEGORY, maincat);
	}
	
	public void initGuiSubCat() //INFO:GuiSubCat
	{
		LinkedHashMap<String, Language> subcat = new LinkedHashMap<>();
		String path = "";
		path = "53"; //Back
		subcat.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		subcat.put(path+".Material."+SettingsLevel.NOLEVEL.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.ARROW.toString()}));
		subcat.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&c<<< Zurück",
						"&c<<< Back"}));
		subcat.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"",
						"&eKehre zurück den Hauptkategorien.",
						"",
						"&eReturn to the main categories."}));
		subcat.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.RETURN_TOMAINCATEGORY.toString()}));
		subcat.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.RETURN_TOMAINCATEGORY.toString()}));
		guiKeys.put(GuiType.SUB_CATEGORY, subcat);
	}
	
	public void initGuiTechnology() //INFO:GuiTechnology
	{
		LinkedHashMap<String, Language> tech = new LinkedHashMap<>();
		String path = "";
		path = "53"; //Back
		tech.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		tech.put(path+".Material."+SettingsLevel.NOLEVEL.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.ARROW.toString()}));
		tech.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&c<<< Zurück",
						"&c<<< Back"}));
		tech.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"",
						"&eKehre zurück zu den Subkategorien.",
						"",
						"&eReturn to the sub categories."}));
		tech.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.RETURN_TOSUBCATEGORY.toString()}));
		tech.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.RETURN_TOSUBCATEGORY.toString()}));
		guiKeys.put(GuiType.TECHNOLOGY, tech);
	}
	
	public void initItemGenerator() //INFO:ItemGenerator
	{
		/*String onekey = "";
		LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		one.put("",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		itemGeneratorKeys.put(onekey, one);*/
	}
	
	public void initMainCategory() //INFO:MainCategory
	{
		addMainCategory("miscellaneous",
				new String[] {"Sonstiges", "Miscellaneous"},
				PlayerAssociatedType.SOLO, 0, 
				null, true,
				new String[] {"&8Hauptkategorie Sonstiges","&8Maincategory Maincategory Miscellaneous"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fHier sind alle Werkzeuge, Waffen,",
						"&fRüstungen, Interaktionsblock und mehr untergebracht.",
						"",
						"&fThey house all the tools, weapons,",
						"&farmor and more."},
				new String[] {"&5Hauptkategorie Sonstiges","&5Maincategory Maincategory Miscellaneous"}, Material.WOODEN_PICKAXE, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fHier sind alle Werkzeuge, Waffen,",
						"&fRüstungen und mehr untergebracht.",
						"",
						"&fThey house all the tools, weapons,",
						"&farmor, interactionblocks and more."});
		addMainCategory("mining",
				new String[] {"Bergbau", "Mining"},
				PlayerAssociatedType.SOLO, 1, 
				null, true,
				new String[] {"&8Hauptkategorie Bergbau","&8Maincategory Mining"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fAlles rund ums Blöcke abbauen.",
						"",
						"&fEverything to do with breaking blocks."},
				new String[] {"&5Hauptkategorie Bergbau","&5Maincategory Mining"}, Material.IRON_ORE, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fAlles rund ums Blöcke abbauen.",
						"",
						"&fEverything to do with breaking blocks."});
		addMainCategory("woodworking",
				new String[] {"Holzarbeiten", "Woodworking"},
				PlayerAssociatedType.SOLO, 2, 
				null, true,
				new String[] {"&8Hauptkategorie Holzarbeiten","&8Maincategory Woodworking"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fGibt Einsicht auf verschiedenste Bereiche der",
						"&fHolzarbeiten wie Setzlinge pflanzen, Bäume schlagen, etc.",
						"",
						"&fGives insight into various areas of the woodworking",
						"&fwork such as planting seedlings and felling trees."},
				new String[] {"&5Hauptkategorie Holzarbeiten","&5Maincategory Woodworking"}, Material.OAK_PLANKS, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fGibt Einsicht auf verschiedenste Bereiche der",
						"&fHolzarbeiten wie Setzlinge pflanzen, Bäume schlagen, etc.",
						"",
						"&fGives insight into various areas of the woodworking",
						"&fwork such as planting seedlings and felling trees."});
		addMainCategory("stonemason",
				new String[] {"Steinmetz", "Stonemason"},
				PlayerAssociatedType.SOLO, 3,
				new String[] {
						"a:hasresearchedtech,stone_I,1:==:true",
						"b:var1=perm=here.your.first.permission:==:true",
						"c:var1=perm=here.your.other.permission:==:true",
						"if:(a&&b&&c):o_1",
						"else:o_2",
						"output:o_1:true",
						"output:o_2:false"}, true,
				new String[] {"&8Hauptkategorie Steinmetz","&8Maincategory Stonemason"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >&#ff8c00Stein I:1&c< erforscht haben.",
						"&cPermission >&#ff8c00here.your.first.permission&c<",
						"&cPermission >&#ff8c00here.your.other.permission&c<",
						"",
						"&fGibt Einsicht auf verschiedenste Bereiche des",
						"&fSteinmetzes wie Steinstufen und Treppen zum craften.",
						"",
						"&cRequirements to view:",
						"&cMust have researched the Technology >&#ff8c00Stone I:1&c<.",
						"&cPermission >&#ff8c00here.your.first.permission&c<",
						"&cPermission >&#ff8c00here.your.other.permission&c<",
						"",
						"&fGives insight into various areas of the stonemason",
						"&fto craft stoneslaps and stairs."},
				new String[] {"&5Hauptkategorie Steinmetz","&5Maincategory Stonemason"}, Material.COBBLESTONE_STAIRS, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fGibt Einsicht auf verschiedenste Bereiche des",
						"&fSteinmetzes wie Steinstufen und Treppen zum craften.",
						"",
						"&fGives insight into various areas of the stonemason",
						"&fto craft stoneslaps and stairs."});
		addMainCategory("tablerecipe",
				new String[] {"Tischrezepte", "Tablerecipe"},
				PlayerAssociatedType.SOLO, 4,
				new String[] {
						"a:hasresearchedtech,stone_I,1:==:true",
						"b:hasresearchedtech,ironore,1:==:true",
						"if:(a||b):o_1", "else:o_2",
						"output:o_1:true",
						"output:o_2:false"}, true,
				new String[] {"&8Hauptkategorie Tischrezepte","&8Maincategory Tablerecipe"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >&#ff8c00Stein I:1&c<",
						"&coder >&#ff8c00Eisenerz:1&c< erforscht haben.",
						"",
						"&cRequirements to view:",
						"&cMust have researched the Technology",
						"&c>&#ff8c00Stone I:1&c< or <&#ff8c00Ironore:1&c>."},
				new String[] {"&5Hauptkategorie Tischrezepte","&5Maincategory Tablerecipe"}, Material.ENCHANTING_TABLE, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fGibt Einsicht auf verschiedenste Bereiche des",
						"&fTischrezepte wie brennen im Ofen, Verzauben, Tränkebrauen etc...",
						"",
						"&fGives insight into various areas of table recipes",
						"&fsuch as smelting in the furnace, enchanting, potion brewing etc..."});
		addMainCategory("booster",
				new String[] {"Booster", "Booster"},
				PlayerAssociatedType.GLOBAL, 0, 
				null, true,
				new String[] {"&8Hauptkategorie Booster","&8Maincategory Booster"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fGibt Einsicht auf verschiedenste Bereiche des",
						"&ferforschbaren Booster für bspw. Abbauen oder der Herstellung.",
						"",
						"&fGives insight into various areas of table recipes",
						"&fresearchable boosters for e.g. mining or production."},
				new String[] {"&5Hauptkategorie Booster","&5Maincategory Booster"}, Material.BEACON, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fGibt Einsicht auf verschiedenste Bereiche des",
						"&ferforschbaren Booster für bspw. Abbauen oder der Herstellung.",
						"",
						"&fGives insight into various areas of table recipes",
						"&fresearchable boosters for e.g. mining or production."});
	}
	
	public void addMainCategory(
			String key, String[] displayname, PlayerAssociatedType pat, int guiSlot,
			String[] conditionQuery, boolean showDifferentItemIfYouNormallyDontSeeIt,
			String[] notSeeDisplayname, Material notSeeMat, int notSeeAmount, String[] notSeeItemFlag, String[] notSeeEnchantments, String[] notSeeLore,
			String[] canSeeDisplayname, Material canSeeMat, int canSeeAmount, String[] canSeeItemFlag, String[] canSeeEnchantments, String[] canSeeLore)
	{
		LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		one.put("Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG},
				displayname));
		one.put("PlayerAssociatedType", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						pat.toString()}));
		one.put("GuiSlot", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						guiSlot}));
		if(conditionQuery != null)
		{
			one.put("RequirementToSee.ConditionQuery", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					conditionQuery));
		}
		one.put("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						showDifferentItemIfYouNormallyDontSeeIt}));
		//--- ItemIfYouCannotSee ---
		one.put("RequirementToSee.ItemIfYouCannotSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						notSeeDisplayname));
		one.put("RequirementToSee.ItemIfYouCannotSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeMat.toString()}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeAmount}));
		if(notSeeItemFlag != null)
		{
			one.put("RequirementToSee.ItemIfYouCannotSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					notSeeItemFlag));
		}
		if(notSeeEnchantments != null)
		{
			one.put("RequirementToSee.ItemIfYouCannotSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					notSeeEnchantments));
		}
		one.put("RequirementToSee.ItemIfYouCannotSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						notSeeLore));
		//--- ItemIfYouCanSee ---
		one.put("RequirementToSee.ItemIfYouCanSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						canSeeDisplayname));
		one.put("RequirementToSee.ItemIfYouCanSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeMat.toString()}));
		one.put("RequirementToSee.ItemIfYouCanSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeAmount}));
		if(canSeeItemFlag != null)
		{
			one.put("RequirementToSee.ItemIfYouCanSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					canSeeItemFlag));
		}
		if(canSeeEnchantments != null)
		{
			one.put("RequirementToSee.ItemIfYouCanSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					canSeeEnchantments));
		}
		one.put("RequirementToSee.ItemIfYouCanSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG},
						canSeeLore));
		LinkedHashMap<String, LinkedHashMap<String, Language>> map = new LinkedHashMap<>();
		if(mainCategoryKeys.containsKey(pat))
		{
			map = mainCategoryKeys.get(pat);
		}
		map.put(key, one);
		mainCategoryKeys.put(pat, map);
	}
	
	public void subc_Miscellaneous(String[] itemflag) //INFO:SubCategory
	{
		/*
		 * Werkzeuge(Schaufel, Hacke, Spitzhacke, Axt, Eimer(Wasser, etc.), Angel, Feuerzeug, Schere)
		 * WaffenRüstungen(Schwert, Schild, Dreizack, Bogen, Armbrust,
		 */
		addSubCategory("interactionblocks",
				new String[] {"Interaktionsblöcke", "Interactionblocks"},
				PlayerAssociatedType.SOLO, 0,
				null, true, "miscellaneous",
				new String[] {"&8Subkategorie Interaktionsblöcke","&8Subcategory Interactionblocks"},
				Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Herstellungsrezepten von Werkbank, Öfen etc.",
						"",
						"&fAccess to the manufacturing recipes for workbenches, ovens, etc."},
				new String[] {"&5Subkategorie Interaktionsblöcke","&5Subcategory Interactionblocks"},
				Material.CRAFTING_TABLE, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Herstellungsrezepten von Werkbank, Öfen etc.",
						"",
						"&fAccess to the manufacturing recipes for workbenches, ovens, etc."});
		addSubCategory("tools",
				new String[] {"Werkzeuge", "Tools"},
				PlayerAssociatedType.SOLO, 1,
				new String[] {
						"a:hasresearchedtech,crafting_table,1:==:true",
						"if:(a):o_1", "else:o_2",
						"output:o_1:true",
						"output:o_2:false"}, true, "miscellaneous",
				new String[] {"&8Subkategorie Werkzeuge","&8Subcategory Tools"},
				Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&cAnforderungen zum einsehen:",
						"&cTechnologie >&#ff8c00Werkbank&c< erforscht haben.",
						"",
						"&fZugang zu den Herstellungsrezepten aller Werkzeuge.",
						"",
						"&cRequirements to view:",
						"&cTechnology >&#ff8c00Craftingtable&c< researched.",
						"",
						"&fAccess to the manufacturing recipes for all tools."},
				new String[] {"&5Subkategorie Werkzeuge","&5Subcategory Tools"},
				Material.WOODEN_PICKAXE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"",
						"&fZugang zu den Herstellungsrezepten aller Werkzeuge.",
						"",
						"&fAccess to the manufacturing recipes for all tools."});
		addSubCategory("weapons_armor",
				new String[] {"Waffen_Rüstungen", "Weapons_Armor"},
				PlayerAssociatedType.SOLO, 2,
				new String[] {
						"if:(a):o_1", "else:o_2",
						"output:o_1:true",
						"output:o_2:false",
						"a:hasresearchedtech,crafting_table,1:==:true"}, true, "miscellaneous",
				new String[] {"&8Subkategorie Waffen und Rüstungen","&8Subcategory Weapons and Armor"},
				Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&cAnforderungen zum einsehen:",
						"&cTechnologie >&#ff8c00Werkbank&c< erforscht haben.",
						"",
						"&fZugang zu den Herstellungsrezepten aller Waffen und Rüstungen.",
						"",
						"&cRequirements to view:",
						"&cTechnology >&#ff8c00Craftingtable&c< researched.",
						"",
						"&fAccess to the manufacturing recipes for all weapons and armors."},
				new String[] {"&5Subkategorie Waffen und Rüstungen","&5Subcategory Weapons and Armor"},
				Material.WOODEN_SWORD, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Herstellungsrezepten aller Waffen und Rüstungen.",
						"",
						"&fAccess to the manufacturing recipes for all weapons and armors."});
	}
	
	public void subc_Mining(String[] itemflag)
	{
		/*
		 * Erde,
		 * Steine
		 * Erze
		 */
		addSubCategory("soil",
				new String[] {"Erde", "Soil"},
				PlayerAssociatedType.SOLO, 0,
				null, true, "mining",
				new String[] {"&8Subkategorie Erde","&8Subcategory Soil"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Abbauerlaubnis von Erde, Sand, Ton etc.",
						"",
						"&fAccess to excavation permits for dirt, sand, clay, etc."},
				new String[] {"&5Subkategorie Erde","&5Subcategory Soil"}, Material.DIRT, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Abbauerlaubnis von Erde, Sand, Ton etc.",
						"",
						"&fAccess to excavation permits for dirt, sand, clay, etc."});
		addSubCategory("stone",
				new String[] {"Steine", "Stone"},
				PlayerAssociatedType.SOLO, 1, 
				null, true, "mining",
				new String[] {"&8Subkategorie Erde","&8Subcategory Soil"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Abbauerlaubnis von Stein, Tiefenschiefer etc.",
						"",
						"&fAccess to quarrying permits for stone, deep slate, etc."},
				new String[] {"&5Subkategorie Erde","&5Subcategory Soil"}, Material.STONE, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Abbauerlaubnis von Stein, Tiefenschiefer etc.",
						"",
						"&fAccess to quarrying permits for stone, deep slate, etc."});
		addSubCategory("ore",
				new String[] {"Erze", "Ore"},
				PlayerAssociatedType.SOLO, 2,
				null, true, "mining",
				new String[] {"&8Subkategorie Erze","&8Subcategory Ores"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Abbauerlaubnis von Erzen aller Art etc.",
						"",
						"&fAccess to mining permits for ores of all kinds, etc."},
				new String[] {"&5Subkategorie Erze","&5Subcategory Ores"}, Material.IRON_ORE, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Abbauerlaubnis von Erzen aller Art etc.",
						"",
						"&fAccess to mining permits for ores of all kinds, etc."});
	}
	
	public void subc_Woodworking(String[] itemflag)
	{
		/*
		 * Setzlinge
		 * Holzstämme
		 * Holzbretter
		 */
		addSubCategory("sapling",
				new String[] {"Setzlinge", "Saplings"},
				PlayerAssociatedType.SOLO, 0,
				null, true, "woodworking",
				new String[] {"&8Subkategorie Setzlinge","&8Subcategory Saplings"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zum pflanzen von Setztlingen etc.",
						"",
						"&fAccess for planting seedlings etc."},
				new String[] {"&5Subkategorie Setzlinge","&5Subcategory Sapling"}, Material.OAK_SAPLING, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zum pflanzen von Setztlingen etc.",
						"",
						"&fAccess for planting seedlings etc."});
		addSubCategory("wood",
				new String[] {"Holz", "Wood"},
				PlayerAssociatedType.SOLO, 1,
				null, true, "woodworking",
				new String[] {"&8Subkategorie Holz","&8Subcategory Wood"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Schlagen aller Holzer aller Art etc.",
						"",
						"&fAccess to all types of woodworking shops etc."},
				new String[] {"&5Subkategorie Holz","&5Subcategory Wood"}, Material.OAK_LOG, 1, null, null, new String[] {
						"",
						"&fZugang zu den Schlagen aller Holzer aller Art etc.",
						"",
						"&fAccess to all types of woodworking shops etc."});
	}
	
	public void subc_Stonemason(String[] itemflag)
	{
		/*
		 * Stufen
		 * Treppen
		 */
		addSubCategory("stoneslaps",
				new String[] {"Steinstufen", "Stoneslaps"},
				PlayerAssociatedType.SOLO, 0,
				null, true, "stonemason",
				new String[] {"&8Subkategorie Steinstufen","&8Subcategory Stoneslaps"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Herstellung von Steinstufen aller Art etc.",
						"",
						"&fAccess to the manufacture of all types of stone slaps etc."},
				new String[] {"&5Subkategorie Steinstufen","&5Subcategory Stoneslaps"}, Material.STONE_SLAB, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Herstellung von Steinstufen aller Art etc.",
						"",
						"&fAccess to the manufacture of all types of stone slaps etc."});
		addSubCategory("stonestairs",
				new String[] {"Steintreppen", "Stonestairs"},
				PlayerAssociatedType.SOLO, 1,
				null, true, "stonemason",
				new String[] {"&8Subkategorie Steintreppen","&8Subcategory Stonestairs"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Herstellung von Steintreppen aller Art etc.",
						"",
						"&fAccess to the manufacture of all types of stone stairs etc."},
				new String[] {"&5Subkategorie Steintreppen","&5Subcategory Stonestairs"}, Material.STONE_STAIRS, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu den Herstellung von Steintreppen aller Art etc.",
						"",
						"&fAccess to the manufacture of all types of stone stairs etc."});
	}
	
	public void subc_TableRecipes(String[] itemflag)
	{
		/*
		 * Brennen
		 * Verzaubern
		 * Tränke
		 * Amboss
		 * Schmieden
		 */
		addSubCategory("furnancerecipe",
				new String[] {"Ofenrezepte", "Furnacerecipe"},
				PlayerAssociatedType.SOLO, 0,
				new String[] {
						"if:(a):o_1", "else:o_2",
						"output:o_1:true",
						"output:o_2:false",
						"a:hasresearchedtech,furnace,1:==:true"}, true, "tablerecipe",
				new String[] {"&8Subkategorie Ofenrezepte","&dSubcategory Furnacerecipe"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&cAnforderungen zum einsehen:",
						"&cTechnologie >&#ff8c00Ofen&c< erforscht haben.",
						"",
						"&fZugang zu allerlei Ofenrezepten.",
						"",
						"&cRequirements to view:",
						"&cTechnology >&#ff8c00Furnace&c< researched.",
						"",
						"&fAccess to all kinds of oven recipes."},
				new String[] {"&5Subkategorie Ofenrezepte","&5Subcategory Furnacerecipe"}, Material.FURNACE, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu allerlei Ofenrezepten.",
						"",
						"&fAccess to all kinds of oven recipes."});
		addSubCategory("enchantmentrecipe",
				new String[] {"Verzauberungsrezepte", "Enchantmentrecipe"},
				PlayerAssociatedType.SOLO, 1,
				new String[] {
						"if:(a):o_1", "else:o_2",
						"output:o_1:true",
						"output:o_2:false",
						"a:true"}, true, "tablerecipe",
				new String[] {"&8Subkategorie Verzauberungsrezepte","&8Subcategory Enchantmentrecipe"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu allerlei Verzauberungsrezepten.",
						"",
						"&fAccess to all kinds of enchantment recipes."},
				new String[] {"&5Subkategorie Verzauberungsrezepte","&5Subcategory Enchantmentrecipe"}, Material.ENCHANTING_TABLE, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu allerlei Verzauberungsrezepten.",
						"",
						"&fAccess to all kinds of enchantment recipes."});
		addSubCategory("brewingrecipe",
				new String[] {"Braurezepte", "Brewingrecipe"},
				PlayerAssociatedType.SOLO, 2,
				new String[] {
						"if:(a):o_1", "else:o_2",
						"output:o_1:true",
						"output:o_2:false",
						"a:true"}, true, "tablerecipe",
				new String[] {"&8Subkategorie Braurezepte","&8Subcategory Brewingrecipe"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu allerlei Braurezepten.",
						"",
						"&fAccess to all kinds of brewing recipes."},
				new String[] {"&5Subkategorie Braurezepte","&5Subcategory Furnacerecipe"}, Material.BREWING_STAND, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu allerlei Braurezepten.",
						"",
						"&fAccess to all kinds of brewing recipes."});
		addSubCategory("anvilrecipe",
				new String[] {"Ambossrezepte", "Anvilrecipe"},
				PlayerAssociatedType.SOLO, 3,
				new String[] {
						"if:(a):o_1", "else:o_2",
						"output:o_1:true",
						"output:o_2:false",
						"a:true"}, true, "tablerecipe",
				new String[] {"&8Subkategorie Ambossrezepte","&8Subcategory Anvilrecipe"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu allerlei Ambossrezepten.",
						"",
						"&fAccess to all kinds of anvil recipes."},
				new String[] {"&5Subkategorie Ambossrezepte","&5Subcategory Anvilrecipe"}, Material.ANVIL, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu allerlei Ambossrezepten.",
						"",
						"&fAccess to all kinds of anvil recipes."});
		addSubCategory("smithingrecipe",
				new String[] {"Schmiederezepte", "Smithingcerecipe"},
				PlayerAssociatedType.SOLO, 4,
				new String[] {
						"if:(a):o_1", "else:o_2",
						"output:o_1:true",
						"output:o_2:false",
						"a:true"}, true, "tablerecipe",
				new String[] {"&8Subkategorie Schmiederezepte","&8Subcategory Smithingrecipe"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu allerlei Schmiederezepten.",
						"",
						"&fAccess to all kinds of smithing recipes."},
				new String[] {"&5Subkategorie Schmiederezepte","&5Subcategory Smithingrecipe"}, Material.SMITHING_TABLE, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu allerlei Schmiederezepten.",
						"",
						"&fAccess to all kinds of smithing recipes."});
	}
	
	public void subc_Booster(String[] itemflag)
	{
		addSubCategory("miningbooster",
				new String[] {"Abbaubooster", "Miningbooster"},
				PlayerAssociatedType.GLOBAL, 0,
				new String[] {
						"if:(a):o_1", "else:o_2",
						"output:o_1:true",
						"output:o_2:false",
						"a:true"}, true, "booster",
				new String[] {"&8Subkategorie Abbaubooster","&8Subcategory Miningbooster"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu allerlei Abbaubooster.",
						"",
						"&fAccess to all kinds of miningbooster."},
				new String[] {"&5Subkategorie Abbaubooster","&5Subcategory Mining booster"}, Material.GOLDEN_PICKAXE, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu allerlei Abbaubooster.",
						"",
						"&fAccess to all kinds of miningbooster."});
		addSubCategory("craftbooster",
				new String[] {"Herstellungsbooster", "Craftingbooster"},
				PlayerAssociatedType.GLOBAL, 1,
				new String[] {
						"if:(a):o_1", "else:o_2",
						"output:o_1:true",
						"output:o_2:false",
						"a:true"}, true, "booster",
				new String[] {"&8Subkategorie Herstellungsbooster","&8Subcategory Craftingbooster"}, Material.BARRIER, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu allerlei Herstellungsbooster.",
						"",
						"&fAccess to all kinds of craftingbooster."},
				new String[] {"&5Subkategorie Herstellungsbooster","&5Subcategory Craftingbooster"}, Material.CRAFTING_TABLE, 1,
				new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()}, null, new String[] {
						"",
						"&fZugang zu allerlei Herstellungsbooster.",
						"",
						"&fAccess to all kinds of craftingbooster."});
	}
	
	public void addSubCategory(
			String key, String[] displayname, PlayerAssociatedType pat, int guiSlot, 
			String[] conditionQuery, boolean showDifferentItemIfYouNormallyDontSeeIt, String overlyingMainCategory,
			String[] notSeeDisplayname, Material notSeeMat, int notSeeAmount, String[] notSeeItemFlag, String[] notSeeEnchantments, String[] notSeeLore,
			String[] canSeeDisplayname, Material canSeeMat, int canSeeAmount, String[] canSeeItemFlag, String[] canSeeEnchantments, String[] canSeeLore)
	{
		LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		one.put("Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG},
				displayname));
		one.put("PlayerAssociatedType", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						pat.toString()}));
		one.put("GuiSlot", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						guiSlot}));
		if(conditionQuery != null)
		{
			one.put("RequirementToSee.ConditionQuery", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					conditionQuery));
		}
		one.put("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						showDifferentItemIfYouNormallyDontSeeIt}));
		one.put("IfSubCategory.OverlyingMainCategory", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				overlyingMainCategory}));
		//--- ItemIfYouCannotSee ---
		one.put("RequirementToSee.ItemIfYouCannotSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						notSeeDisplayname));
		one.put("RequirementToSee.ItemIfYouCannotSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeMat.toString()}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeAmount}));
		if(notSeeItemFlag != null)
		{
			one.put("RequirementToSee.ItemIfYouCannotSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					notSeeItemFlag));
		}
		if(notSeeEnchantments != null)
		{
			one.put("RequirementToSee.ItemIfYouCannotSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					notSeeEnchantments));
		}
		one.put("RequirementToSee.ItemIfYouCannotSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						notSeeLore));
		//--- ItemIfYouCanSee ---
		one.put("RequirementToSee.ItemIfYouCanSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						canSeeDisplayname));
		one.put("RequirementToSee.ItemIfYouCanSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeMat.toString()}));
		one.put("RequirementToSee.ItemIfYouCanSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeAmount}));
		if(canSeeItemFlag != null)
		{
			one.put("RequirementToSee.ItemIfYouCanSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					canSeeItemFlag));
		}
		if(canSeeEnchantments != null)
		{
			one.put("RequirementToSee.ItemIfYouCanSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					canSeeEnchantments));
		}
		one.put("RequirementToSee.ItemIfYouCanSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						canSeeLore));
		LinkedHashMap<String, LinkedHashMap<String, Language>> map = new LinkedHashMap<>();
		if(subCategoryKeys.containsKey(pat))
		{
			map = subCategoryKeys.get(pat);
		}
		map.put(key, one);
		subCategoryKeys.put(pat, map);
	}
	
	public void initTechnology() //INFO:Technology
	{
		String[] itemflag = new String[] {ItemFlag.HIDE_ATTRIBUTES.toString(),ItemFlag.HIDE_ENCHANTS.toString()};
		String[] enchantment = new String[] {Enchantment.KNOCKBACK.toString()+":1", ""};
		subc_Miscellaneous(itemflag);
		tech_Miscellaneous_Interactionblocks(itemflag, enchantment);
		tech_Miscellaneous_Tools(itemflag, enchantment);
		tech_Miscellaneous_Weapons_Armor(itemflag, enchantment);
		
		subc_Mining(itemflag);
		tech_Mining_Soil(itemflag, enchantment);
		tech_Mining_Stone(itemflag, enchantment);
		tech_Mining_Ore(itemflag, enchantment);
		
		subc_Woodworking(itemflag);
		tech_Woodworking_Sapling(itemflag, enchantment);
		tech_Woodworking_Wood(itemflag, enchantment);
		
		subc_Stonemason(itemflag);
		tech_Stonemason_Slabs(itemflag, enchantment);
		tech_Stonemason_Stonestairs(itemflag, enchantment);
		
		subc_TableRecipes(itemflag);
		tech_Tablerecipe_Furnancerecipe(itemflag, enchantment);
		tech_Tablerecipe_Enchantmentrecipe(itemflag, enchantment);
		tech_Tablerecipe_Brewingrecipe(itemflag, enchantment);
		tech_Tablerecipe_Anvilrecipe(itemflag, enchantment);
		tech_Tablerecipe_Forgingcerecipe(itemflag, enchantment);
		
		subc_Booster(itemflag);
		tech_Booster_Miningbooster(itemflag, enchantment);
		tech_Booster_Craftingbooster(itemflag, enchantment);
	}
	
	private void tech_Miscellaneous_Interactionblocks(String[] itemflag, String[] enchantment) //INFO:Miscellaneous_Interactionblocks
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1", "else:o_2",
				"output:o_1:true",
				"output:o_2:false",
				"a:hasresearchedtech,woodenplanks_I,1:==:true"});
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"INTERACT:CRAFTING_TABLE:null",""});
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {"SHAPED:crafting_table",""});
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:HAND:CRAFTING_TABLE:null:mat=CRAFTING_TABLE:1:1.0",
				"BREAKING:WOODEN_PICKAXE:CRAFTING_TABLE:null:mat=CRAFTING_TABLE:1:1.0",
				"BREAKING:STONE_PICKAXE:CRAFTING_TABLE:null:mat=CRAFTING_TABLE:1:1.0",
				"BREAKING:IRON_PICKAXE:CRAFTING_TABLE:null:mat=CRAFTING_TABLE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:CRAFTING_TABLE:null:mat=CRAFTING_TABLE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:CRAFTING_TABLE:null:mat=CRAFTING_TABLE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:CRAFTING_TABLE:null:mat=CRAFTING_TABLE:1:1.0"});
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&cAnforderungen zum einsehen:",
				"&cMuss die Technology >&#ff8c00Holzbretter&c< erforscht haben.",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney% Geld",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellung & Interaktion mit der Werkbank",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&cRequirements to view:",
				"&cMust have researched the Technology >&#ff8c00Planks&c<.",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney% Money",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting & Interaction with Craftingtable",
				"",
				"&cRightclick &bfor a more detailed view."
				});
		addTechnology(
				"crafting_table", new String[] {"Werkbank", "Craftingtable"},
				TechnologyType.SIMPLE, 1, PlayerAssociatedType.SOLO, 0, "", "interactionblocks", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Werkbank","&8Tech Craftingtable"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >&#ff8c00Holzbretter&c< erforscht haben.",
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellung & Interaktion mit der Werkbank",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&cRequirements to view:",
						"&cMust have researched the Technology >&#ff8c00Woodenplanks&c<.",
						"",
						"&eUnlocks the following:",
						"&fCrafting & Interaction with Craftingtable",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Tech Werkbank","&7Tech Craftingtable"},
				Material.CRAFTING_TABLE, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dTech Werkbank","&dTech Craftingtable"},
				Material.CRAFTING_TABLE, 1, itemflag, null, canResLore,
				new String[] {"&5Tech Werkbank","&5Tech Craftingtable"},
				Material.CRAFTING_TABLE, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellung & Interaktion mit der Werkbank",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fCrafting & Interaction with Craftingtable",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1", "else:o_2",
				"output:o_1:true",
				"output:o_2:false",
				"a:hasresearchedtech,stone_I,1:==:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMaterial = new LinkedHashMap<>();
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"INTERACT:FURNACE:null",""});
		rewardUnlockableInteractions.put(2, new String[] {
				"INTERACT:BLAST_FURNACE:null",
				"INTERACT:FURNACE_MINECART:null"});
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {"SHAPED:furnace",""});
		rewardUnlockableRecipe.put(2, new String[] {"SHAPED:blast_furnace","SHAPELESS:furnace_minecart"});
		rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:WOODEN_PICKAXE:FURNACE:null:mat=FURNACE:1:1.0",
				"BREAKING:STONE_PICKAXE:FURNACE:null:mat=FURNACE:1:1.0",
				"BREAKING:IRON_PICKAXE:FURNACE:null:mat=FURNACE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:FURNACE:null:mat=FURNACE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:FURNACE:null:mat=FURNACE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:FURNACE:null:mat=FURNACE:1:1.0"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:WOODEN_PICKAXE:BLAST_FURNACE:null:mat=BLAST_FURNACE:1:1.0",
				"BREAKING:STONE_PICKAXE:BLAST_FURNACE:null:mat=BLAST_FURNACE:1:1.0",
				"BREAKING:IRON_PICKAXE:BLAST_FURNACE:null:mat=BLAST_FURNACE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:BLAST_FURNACE:null:mat=BLAST_FURNACE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:BLAST_FURNACE:null:mat=BLAST_FURNACE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:BLAST_FURNACE:null:mat=BLAST_FURNACE:1:1.0",
				"BREAKING:WOODEN_PICKAXE:FURNACE_MINECART:null:mat=FURNACE_MINECART:1:1.0",
				"BREAKING:STONE_PICKAXE:FURNACE_MINECART:null:mat=FURNACE_MINECART:1:1.0",
				"BREAKING:IRON_PICKAXE:FURNACE_MINECART:null:mat=FURNACE_MINECART:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:FURNACE_MINECART:null:mat=FURNACE_MINECART:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:FURNACE_MINECART:null:mat=FURNACE_MINECART:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:FURNACE_MINECART:null:mat=FURNACE_MINECART:1:1.0"});
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:WOODEN_PICKAXE:FURNACE:null:mat=FURNACE:1:1.0",
				"BREAKING:STONE_PICKAXE:FURNACE:null:mat=FURNACE:1:1.0",
				"BREAKING:IRON_PICKAXE:FURNACE:null:mat=FURNACE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:FURNACE:null:mat=FURNACE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:FURNACE:null:mat=FURNACE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:FURNACE:null:mat=FURNACE:1:1.0"});
		rewardSilkTouchDropChance.put(2, new String[] {
				"BREAKING:WOODEN_PICKAXE:BLAST_FURNACE:null:mat=BLAST_FURNACE:1:1.0",
				"BREAKING:STONE_PICKAXE:BLAST_FURNACE:null:mat=BLAST_FURNACE:1:1.0",
				"BREAKING:IRON_PICKAXE:BLAST_FURNACE:null:mat=BLAST_FURNACE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:BLAST_FURNACE:null:mat=BLAST_FURNACE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:BLAST_FURNACE:null:mat=BLAST_FURNACE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:BLAST_FURNACE:null:mat=BLAST_FURNACE:1:1.0",
				"BREAKING:WOODEN_PICKAXE:FURNACE_MINECART:null:mat=FURNACE_MINECART:1:1.0",
				"BREAKING:STONE_PICKAXE:FURNACE_MINECART:null:mat=FURNACE_MINECART:1:1.0",
				"BREAKING:IRON_PICKAXE:FURNACE_MINECART:null:mat=FURNACE_MINECART:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:FURNACE_MINECART:null:mat=FURNACE_MINECART:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:FURNACE_MINECART:null:mat=FURNACE_MINECART:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:FURNACE_MINECART:null:mat=FURNACE_MINECART:1:1.0"});
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellung & Interaktion mit dem Ofen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"",
				"&eUnlocks the following:",
				"&fCrafting & Interaction with Furnace.",
				"",
				"&cRightclick &bfor a more detailed view."
				});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellung & Interaktion mit dem",
				"&fSchmelzofen und Ofenlore",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney% Money",
				"",
				"&eUnlocks the following:",
				"&fCrafting & Interaction with",
				"&fBlastfurnace and Furnaceminecart",
				"",
				"&cRightclick &bfor a more detailed view."
				});
		addTechnology(
				"furnace", new String[] {"Ofen", "Furnace"},
				TechnologyType.MULTIPLE, 2, PlayerAssociatedType.SOLO, 1, "", "interactionblocks", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Ofen","&8Tech Furnace"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >&#ff8c00Stein I:1&c< erforscht haben.",
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellung & Interaktion mit dem",
						"&fOfen, Schmelzofen und Ofenlore",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&cRequirements to view:",
						"&cMust have researched the Technology >&#ff8c00Stone I&c<.",
						"",
						"&eUnlocks the following:",
						"&fCrafting & Interaction with",
						"&fFurnace, Blastfurnace and Furnaceminecart",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Tech Ofen","&7Tech Furnace"},
				Material.FURNACE, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dTech Ofen","&dTech Furnace"},
				Material.FURNACE, 1, itemflag, null, canResLore,
				new String[] {"&5Tech Ofen","&5Tech Furnace"},
				Material.FURNACE, 1, itemflag, enchantment, new String[] {
						"",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >&#ff8c00Stein I:1< erforscht haben.",
						"",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellung & Interaktion mit dem",
						"&fOfen, Schmelzofen und Ofenlore",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Stone I<.",
						"",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"",
						"&eUnlocks the following:",
						"&fCrafting & Interaction with",
						"&fFurnace, Blastfurnace and Furnaceminecart",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
		//REMOVEME TestTech
		toResCondition = new LinkedHashMap<>();
		toResCostTTExp.put(1, "1 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMaterial = new LinkedHashMap<>();
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"BREEDING:null:COW:ttexp=1.01:vaexp=1:default=1.0",
				"BREWING:NETHER_WART:null:ttexp=1.02:vaexp=1:default=1.0",
				"BUCKET_EMPTYING:WATER_BUCKET:null:ttexp=1.03:vaexp=1:default=1.0",
				"BUCKET_FILLING:WATER_BUCKET:null:ttexp=1.04:vaexp=1:default=1.0",
				"COLD_FORGING:NETHERITE_HOE:null:ttexp=1.05:vaexp=1:default=1.0",
				"COMPOSTING:OAK_SAPLING:null:ttexp=0.99:vaexp=1:default=1.0",
				"COOKING:BAKED_POTATO:null:ttexp=1.06:vaexp=1:default=1.0",
				"CRAFTING:STICK:null:ttexp=1.07:vaexp=1:default=1.0",
				"CREATE_PATH:GRASS_BLOCK:null:ttexp=1.08:vaexp=1:default=1.0",
				"DEBARKING:OAK_LOG:null:ttexp=1.09:vaexp=1:default=1.0",
				"DRYING:SPONGE:null:ttexp=1.10:vaexp=1:default=1.0",
				"DYING:null:PLAYER:ttexp=1.11:vaexp=1:default=1.0",
				"ENCHANTING:NETHERITE_HOE:null:ttexp=1.12:vaexp=1:default=1.0",
				"EXPLODING:TNT:null:ttexp=1.13:vaexp=1:default=1.0",
				"FERTILIZING:OAK_SAPLING:null:ttexp=1.14:vaexp=1:default=1.0",
				"FISHING:COD:null:tool=FISHING_ROD:ttexp=1.15:vaexp=1:default=1.0",
				"GRINDING:NETHERITE_HOE:null:ttexp=1.16:vaexp=1:default=1.0",
				"HARMING:null:COW:ttexp=1.17:vaexp=1:default=1.0",
				"HARVEST:SWEET_BERRY_BUSH:null:ttexp=1.18:vaexp=1:default=1.0",
				"IGNITING:TNT:null:ttexp=1.19:vaexp=1:default=1.0",
				"ITEM_BREAKING:WOODEN_SHOVEL:null:ttexp=1.20:vaexp=1:default=1.0",
				"ITEM_CONSUME:BAKED_POTATO:null:ttexp=1.21:vaexp=1:default=1.0",
				"INTERACT:BLAST_FURNACE:null:ttexp=0.42",
				"INTERACT:FURNACE:null:ttexp=0.42",
				"INTERACT:SMOKER:null",
				"INTERACT:ENCHANTING_TABLE:null:ttexp=0.42",
				"INTERACT:BREWING_STAND:null:ttexp=0.42",
				"INTERACT:GRINDSTONE:null:ttexp=0.42",
				"INTERACT:ANVIL:null:ttexp=0.42",
				"INTERACT:GRINDSTONE:null:ttexp=0.42",
				"INTERACT:STONECUTTER:null:ttexp=0.42",
				"INTERACT:CARTOGRAPHY_TABLE:null:ttexp=0.42",
				"INTERACT:STONECUTTER:null:ttexp=0.42",
				"INTERACT:FLETCHING_TABLE:null:ttexp=0.42",
				"INTERACT:SMITHING_TABLE:null:ttexp=0.42",
				"INTERACT:CAMPFIRE:null:ttexp=0.42",
				"INTERACT:SOUL_CAMPFIRE:null:ttexp=0.42",
				"INTERACT:CRAFTING_TABLE:null:ttexp=1.22:vaexp=1:default=1.0",
				"KILLING:null:COW:ttexp=1.23:vaexp=1:default=1.0",
				"MELTING:BAKED_POTATO:null:ttexp=1.24:vaexp=1:default=1.0",
				"MILKING:null:MUSHROOM_COW:ttexp=1.25:vaexp=1:default=1.0",
				"PLACING:DIRT:null:ttexp=1.26:vaexp=1:default=1.0",
				"RENAMING:NETHERITE_HOE:null:ttexp=1.27:vaexp=1:default=1.0",
				"SHEARING:null:SHEEP:ttexp=1.28:vaexp=1:default=1.0",
				"SHEEP_DYE:null:SHEEP:ttexp=1.29:vaexp=1:default=1.0",
				"SMELTING:EMERALD:null:ttexp=1.30:vaexp=1:default=1.0",
				"SMITHING:NETHERITE_HOE:null:ttexp=1.31:vaexp=1:default=1.0",
				"SMOKING:BAKED_POTATO:null:ttexp=1.32:vaexp=1:default=1.0",
				"TAMING:null:WOLF:ttexp=1.33:vaexp=1:default=1.0"});
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {
				"BLASTING:emerald_from_blasting_emerald_ore",
				"CAMPFIRE:baked_potato_from_campfire_cooking",
				"FURNACE:baked_potato",
				"SHAPED:bow",
				"SHAPELESS:bone_meal",
				"SMITHING:netherite_hoe_smithing",
				"SMOKING:baked_potato_from_smoking",
				"STONECUTTING:quartz_stairs_from_quartz_block_stonecutting",
				"ANVIL:NETHERITE_HOE",
				"BREWING:NETHER_WART",
				"ENCHANTING:NETHERITE_HOE",
				"GRINDING:NETHERITE_HOE"});
		rewardDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&bDas ist eine Test Technologie!",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney% Geld",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fVon allen Events eine Sache frei.",
				"&fSowie von allen Rezepttypen eines frei.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&bThat is a test technology!",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney% Money",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fVon allen Events eine Sache frei.",
				"&fSowie von allen Rezepttypen eines frei.",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"testing", new String[] {"Testen", "Testing"},
				TechnologyType.SIMPLE, 1, PlayerAssociatedType.SOLO, 42, "", "interactionblocks", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Testen","&8Tech Testing"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
						"",
						"&bDas ist eine Test Technologie!",
						"",
						"&eSchaltet folgendes frei:",
						"&fVon allen Events eine Sache frei.",
						"&fSowie von allen Rezepttypen eines frei.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
						"",
						"&bThat is a test technology!",
						"",
						"&eUnlocks the following:",
						"&fVon allen Events eine Sache frei.",
						"&fSowie von allen Rezepttypen eines frei.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Tech Testen","&7Tech Testing"},
				Material.BEACON, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dTech Testen","&dTech Testing"},
				Material.BEACON, 1, itemflag, null, canResLore,
				new String[] {"&5Tech Testen","&5Tech Testing"},
				Material.BEACON, 1, itemflag, enchantment, new String[] {
						"",
						"&bDas ist eine Test Technologie!",
						"",
						"&eSchaltet folgendes frei:",
						"&fVon allen Events eine Sache frei.",
						"&fSowie von allen Rezepttypen eines frei.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&bThat is a test technology!",
						"",
						"&eUnlocks the following:",
						"&fVon allen Events eine Sache frei.",
						"&fSowie von allen Rezepttypen eines frei.",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
	}
	
	private void tech_Miscellaneous_Tools(String[] itemflag, String[] enchantment) //INFO:Miscellaneous_Tools
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(7, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(8, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(9, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(10, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(11, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(7, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(8, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(9, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(10, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(11, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(1, new String[] {
				"OAK_PLANKS;16",
				"STICK;16"});
		toResCostMaterial.put(2, new String[] {
				"COBBLESTONE;16",
				"STICK;16"});
		toResCostMaterial.put(3, new String[] {
				"IRON_INGOT;16",
				"STICK;16"});
		toResCostMaterial.put(4, new String[] {
				"GOLD_INGOT;16",
				"STICK;16"});
		toResCostMaterial.put(5, new String[] {
				"DIAMOND;16",
				"STICK;16"});
		toResCostMaterial.put(6, new String[] {
				"NETHERITE_INGOT;16",
				"STICK;16"});
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"CRAFTING:WOODEN_PICKAXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(2, new String[] {
				"CRAFTING:STONE_PICKAXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(3, new String[] {
				"CRAFTING:IRON_PICKAXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(4, new String[] {
				"CRAFTING:GOLDEN_PICKAXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(5, new String[] {
				"CRAFTING:DIAMOND_PICKAXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(6, new String[] {
				"SMITHING:NETHERITE_PICKAXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		String[] rui = new String[] {
				"CRAFTING:WOODEN_PICKAXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:STONE_PICKAXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:IRON_PICKAXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:DIAMOND_PICKAXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:GOLDEN_PICKAXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"SMITHING:NETHERITE_PICKAXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1"};
		rewardUnlockableInteractions.put(7, rui);
		rewardUnlockableInteractions.put(8, rui);
		rewardUnlockableInteractions.put(9, rui);
		rewardUnlockableInteractions.put(10, rui);
		rewardUnlockableInteractions.put(11, rui);
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {"SHAPED:wooden_pickaxe",""});
		rewardUnlockableRecipe.put(2, new String[] {"SHAPED:stone_pickaxe",""});
		rewardUnlockableRecipe.put(3, new String[] {"SHAPED:iron_pickaxe",""});
		rewardUnlockableRecipe.put(4, new String[] {"SHAPED:golden_pickaxe",""});
		rewardUnlockableRecipe.put(5, new String[] {"SHAPED:diamond_pickaxe",""});
		rewardUnlockableRecipe.put(6, new String[] {"SMITHING:netherite_pickaxe_smithing",""});
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(4, new String[] {
				"CRAFTING:HAND:IRON_PICKAXE:null:mat=IRON_INGOT:1:0.005",""});
		rewardDropChance.put(5, new String[] {
				"CRAFTING:HAND:IRON_PICKAXE:null:mat=IRON_INGOT:1:0.02",""});
		rewardDropChance.put(6, new String[] {
				"CRAFTING:HAND:GOLDEN_PICKAXE:null:mat=GOLD_INGOT:1:0.005",""});
		rewardDropChance.put(7, new String[] {
				"CRAFTING:HAND:GOLDEN_PICKAXE:null:mat=GOLD_INGOT:1:0.02",""});
		rewardDropChance.put(8, new String[] {
				"CRAFTING:HAND:DIAMOND_PICKAXE:null:mat=DIAMOND:1:0.005",""});
		rewardDropChance.put(9, new String[] {
				"CRAFTING:HAND:DIAMOND_PICKAXE:null:mat=DIAMOND:1:0.02",""});
		rewardDropChance.put(10, new String[] {
				"SMITHING:HAND:NETHERITE_PICKAXE:null:mat=NETHERITE_INGOT:1:0.005",""});
		rewardDropChance.put(11, new String[] {
				"SMITHING:HAND:NETHERITE_PICKAXE:null:mat=NETHERITE_INGOT:1:0.02",""});
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Holzspitzhacke",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,pickaxe,1,CRAFTING,HAND,WOODEN_PICKAXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,pickaxe,1,CRAFTING,HAND,WOODEN_PICKAXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,pickaxe,1,CRAFTING,HAND,WOODEN_PICKAXE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664woodenpickaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,pickaxe,1,CRAFTING,HAND,WOODEN_PICKAXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,pickaxe,1,CRAFTING,HAND,WOODEN_PICKAXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,pickaxe,1,CRAFTING,HAND,WOODEN_PICKAXE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Steinspitzhacke",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,pickaxe,2,CRAFTING,HAND,STONE_PICKAXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,pickaxe,2,CRAFTING,HAND,STONE_PICKAXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,pickaxe,2,CRAFTING,HAND,STONE_PICKAXE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664stonepickaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,pickaxe,2,CRAFTING,HAND,STONE_PICKAXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,pickaxe,2,CRAFTING,HAND,STONE_PICKAXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,pickaxe,2,CRAFTING,HAND,STONE_PICKAXE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Eisenspitzhacke",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,pickaxe,3,CRAFTING,HAND,IRON_PICKAXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,pickaxe,3,CRAFTING,HAND,IRON_PICKAXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,pickaxe,3,CRAFTING,HAND,IRON_PICKAXE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664ironpickaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,pickaxe,3,CRAFTING,HAND,IRON_PICKAXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,pickaxe,3,CRAFTING,HAND,IRON_PICKAXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,pickaxe,3,CRAFTING,HAND,IRON_PICKAXE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Goldspitzhacke",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,pickaxe,4,CRAFTING,HAND,GOLDEN_PICKAXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,pickaxe,4,CRAFTING,HAND,GOLDEN_PICKAXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,pickaxe,4,CRAFTING,HAND,GOLDEN_PICKAXE% Dollar",
				"&f+0,5 % Chance 1 Eisenbarren bei Herstellen einer Eisenspitzhacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664goldenpickaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,pickaxe,4,CRAFTING,HAND,GOLDEN_PICKAXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,pickaxe,4,CRAFTING,HAND,GOLDEN_PICKAXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,pickaxe,4,CRAFTING,HAND,GOLDEN_PICKAXE% Dollar",
				"&f+0.5% chance to drop 1 iron ingot when crafting a iron pickaxe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Diamantspitzhacke",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,pickaxe,5,CRAFTING,HAND,DIAMOND_PICKAXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,pickaxe,5,CRAFTING,HAND,DIAMOND_PICKAXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,pickaxe,5,CRAFTING,HAND,DIAMOND_PICKAXE% Dollar",
				"&f+2 % Chance 1 Eisenbarren bei Herstellen einer Eisenspitzhacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664diamondpickaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,pickaxe,5,CRAFTING,HAND,DIAMOND_PICKAXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,pickaxe,5,CRAFTING,HAND,DIAMOND_PICKAXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,pickaxe,5,CRAFTING,HAND,DIAMOND_PICKAXE% Dollar",
				"&f+2% chance to drop 1 iron ingot when crafting a iron pickaxe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Netheritespitzhacke",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,pickaxe,6,SMITHING,HAND,NETHERITE_PICKAXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,pickaxe,6,SMITHING,HAND,NETHERITE_PICKAXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,pickaxe,6,SMITHING,HAND,NETHERITE_PICKAXE% Dollar",
				"&f+0,5 % Chance 1 Goldbarren bei Herstellen einer Goldspitzhacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664netheritepickaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,pickaxe,6,SMITHING,HAND,NETHERITE_PICKAXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,pickaxe,6,SMITHING,HAND,NETHERITE_PICKAXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,pickaxe,6,SMITHING,HAND,NETHERITE_PICKAXE% Dollar",
				"&f+0.5% chance to drop 1 gold ingot when crafting a gold pickaxe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(7, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Goldbarren bei Herstellen einer Goldspitzhacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 gold ingot when crafting a gold pickaxe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(8, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+0,5 % Chance 1 Diamand bei Herstellen einer Diamantspitzhacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+0.5% chance to drop 1 diamond when crafting a diamond pickaxe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(9, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Diamand bei Herstellen einer Diamantspitzhacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 diamond when crafting a diamond pickaxe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(10, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+0,5 % Chance 1 Netheritebarren bei Herstellen einer Netheritespitzhacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+0.5% chance to drop 1 netherite ingot when crafting a netherite pickaxe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(11, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Netheritebarren bei Herstellen einer Netheritespitzhacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 netherite ingot when crafting a netherite pickaxe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"pickaxe", new String[] {"Spitzhacke", "Pickaxe"},
				TechnologyType.MULTIPLE, 11, PlayerAssociatedType.SOLO, 0, "", "tools", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Spitzhacke","&8Tech Pickaxe"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von &#c6a664Spitzhacke",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fCrafting of &#c6a664pickaxe",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Tech Spitzhacke","&7Tech Pickaxe"},
				Material.WOODEN_PICKAXE, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dTech Spitzhacke","&dTech Pickaxe"},
				Material.WOODEN_PICKAXE, 1, itemflag, null, canResLore,
				new String[] {"&5Tech Spitzhacke","&5Tech Pickaxe"},
				Material.WOODEN_PICKAXE, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&f◦ Herstellung:",
						"&f◦◦ Holzspitzhacke &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,pickaxe,CRAFTING,HAND,WOODEN_PICKAXE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,pickaxe,CRAFTING,HAND,WOODEN_PICKAXE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,pickaxe,CRAFTING,HAND,WOODEN_PICKAXE% Dollar",
						"&f◦◦ Steinspitzhacke &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,pickaxe,CRAFTING,HAND,STONE_PICKAXE% TTExp | "
										   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,pickaxe,CRAFTING,HAND,STONE_PICKAXE% VExp | "
										   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,pickaxe,CRAFTING,HAND,STONE_PICKAXE% Dollar",
						"&f◦◦ Eisenspitzhacke &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,pickaxe,CRAFTING,HAND,IRON_PICKAXE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,pickaxe,CRAFTING,HAND,IRON_PICKAXE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,pickaxe,CRAFTING,HAND,IRON_PICKAXE% Dollar",
						"&f◦◦ Goldspitzhacke &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,pickaxe,CRAFTING,HAND,GOLDEN_PICKAXE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,pickaxe,CRAFTING,HAND,GOLDEN_PICKAXE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,pickaxe,CRAFTING,HAND,GOLDEN_PICKAXE% Dollar",
						"&f◦◦ Diamandspitzhacke &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,pickaxe,CRAFTING,HAND,DIAMOND_PICKAXE% TTExp | "
										     + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,pickaxe,CRAFTING,HAND,DIAMOND_PICKAXE% VExp | "
										     + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,pickaxe,CRAFTING,HAND,DIAMOND_PICKAXE% Dollar",
						"&f◦ Schmieden:",
						"&f◦◦ Netheritespitzhacke &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,pickaxe,SMITHING,HAND,NETHERITE_PICKAXE% TTExp | "
										       + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,pickaxe,SMITHING,HAND,NETHERITE_PICKAXE% VExp | "
										       + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,pickaxe,SMITHING,HAND,NETHERITE_PICKAXE% Dollar",
						"&f◦ Drops:",
						"&f◦◦ Eisenbarren beim herstellen einer Eisenspitzhacke &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,pickaxe,CRAFTING,HAND,IRON_PICKAXE,mat=IRON_INGOT%",
						"&f◦◦ Goldbarren beim herstellen einer Goldspitzhacke &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,pickaxe,CRAFTING,HAND,GOLDEN_PICKAXE,mat=GOLD_INGOT%",
						"&f◦◦ Diamant beim herstellen einer Diamantspitzhacke &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,pickaxe,CRAFTING,HAND,DIAMOND_PICKAXE,mat=DIAMOND%",
						"&f◦◦ Netheritebarren beim herstellen einer Netheritespitzhacke &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,pickaxe,CRAFTING,HAND,NETHERITE_PICKAXE,mat=NETHERITE_INGOT%",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&f◦ Crafting:",
						"&f◦◦ Woodenpickaxe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,pickaxe,CRAFTING,HAND,WOODEN_PICKAXE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,pickaxe,CRAFTING,HAND,WOODEN_PICKAXE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,pickaxe,CRAFTING,HAND,WOODEN_PICKAXE% Dollar",
						"&f◦◦ Stonepickaxe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,pickaxe,CRAFTING,HAND,STONE_PICKAXE% TTExp | "
										   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,pickaxe,CRAFTING,HAND,STONE_PICKAXE% VExp | "
										   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,pickaxe,CRAFTING,HAND,STONE_PICKAXE% Dollar",
						"&f◦◦ Ironpickaxe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,pickaxe,CRAFTING,HAND,IRON_PICKAXE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,pickaxe,CRAFTING,HAND,IRON_PICKAXE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,pickaxe,CRAFTING,HAND,IRON_PICKAXE% Dollar",
						"&f◦◦ Goldenpickaxe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,pickaxe,CRAFTING,HAND,GOLDEN_PICKAXE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,pickaxe,CRAFTING,HAND,GOLDEN_PICKAXE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,pickaxe,CRAFTING,HAND,GOLDEN_PICKAXE% Dollar",
						"&f◦◦ Diamondpickaxe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,pickaxe,CRAFTING,HAND,DIAMOND_PICKAXE% TTExp | "
										     + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,pickaxe,CRAFTING,HAND,DIAMOND_PICKAXE% VExp | "
										     + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,pickaxe,CRAFTING,HAND,DIAMOND_PICKAXE% Dollar",
						"&f◦ Smithing:",
						"&f◦◦ Netheritepickaxe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,pickaxe,SMITHING,HAND,NETHERITE_PICKAXE% TTExp | "
										       + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,pickaxe,SMITHING,HAND,NETHERITE_PICKAXE% VExp | "
										       + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,pickaxe,SMITHING,HAND,NETHERITE_PICKAXE% Dollar",
						"&f◦ Drops:",
						"&f◦◦ Ironingots during the production of an ironpickaxe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,pickaxe,CRAFTING,HAND,IRON_PICKAXE,mat=IRON_INGOT",
						"&f◦◦ Goldingots during the production of an goldenpickaxe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,pickaxe,CRAFTING,HAND,GOLDEN_PICKAXE,mat=GOLD_INGOT",
						"&f◦◦ Diamonds during the production of an diamondpickaxe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,pickaxe,CRAFTING,HAND,DIAMOND_PICKAXE,mat=DIAMOND_INGOT",
						"&f◦◦ Netheriteingots during the production of an netheritepickaxe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,pickaxe,CRAFTING,HAND,NETHERITE_PICKAXE,mat=NETHERITE_INGOT%",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(7, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(8, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(9, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(10, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(11, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(7, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(8, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(9, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(10, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(11, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(1, new String[] {
				"OAK_PLANKS;16",
				"STICK;16"});
		toResCostMaterial.put(2, new String[] {
				"COBBLESTONE;16",
				"STICK;16"});
		toResCostMaterial.put(3, new String[] {
				"IRON_INGOT;16",
				"STICK;16"});
		toResCostMaterial.put(4, new String[] {
				"GOLD_INGOT;16",
				"STICK;16"});
		toResCostMaterial.put(5, new String[] {
				"DIAMOND;16",
				"STICK;16"});
		toResCostMaterial.put(6, new String[] {
				"NETHERITE_INGOT;16",
				"STICK;16"});
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"CRAFTING:WOODEN_SHOVEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(2, new String[] {
				"CRAFTING:STONE_SHOVEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(3, new String[] {
				"CRAFTING:IRON_SHOVEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(4, new String[] {
				"CRAFTING:GOLDEN_SHOVEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(5, new String[] {
				"CRAFTING:DIAMOND_SHOVEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(6, new String[] {
				"CRAFTING:NETHERITE_SHOVEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rui = new String[] {
				"CRAFTING:WOODEN_SHOVEL:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:STONE_SHOVEL:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:IRON_SHOVEL:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:DIAMOND_SHOVEL:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:GOLDEN_SHOVEL:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:NETHERITE_SHOVEL:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1"};
		rewardUnlockableInteractions.put(7, rui);
		rewardUnlockableInteractions.put(8, rui);
		rewardUnlockableInteractions.put(9, rui);
		rewardUnlockableInteractions.put(10, rui);
		rewardUnlockableInteractions.put(11, rui);
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {"SHAPED:wooden_shovel",""});
		rewardUnlockableRecipe.put(2, new String[] {"SHAPED:stone_shovel",""});
		rewardUnlockableRecipe.put(3, new String[] {"SHAPED:iron_shovel",""});
		rewardUnlockableRecipe.put(4, new String[] {"SHAPED:golden_shovel",""});
		rewardUnlockableRecipe.put(5, new String[] {"SHAPED:diamond_shovel",""});
		rewardUnlockableRecipe.put(6, new String[] {"SMITHING:netherite_shovel_smithing",""});
		rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(4, new String[] {
				"CRAFTING:HAND:IRON_SHOVEL:null:mat=IRON_INGOT:1:0.005",""});
		rewardDropChance.put(5, new String[] {
				"CRAFTING:HAND:IRON_SHOVEL:null:mat=IRON_INGOT:1:0.02",""});
		rewardDropChance.put(6, new String[] {
				"CRAFTING:HAND:GOLDEN_SHOVEL:null:mat=GOLD_INGOT:1:0.005",""});
		rewardDropChance.put(7, new String[] {
				"CRAFTING:HAND:GOLDEN_SHOVEL:null:mat=GOLD_INGOT:1:0.02",""});
		rewardDropChance.put(8, new String[] {
				"CRAFTING:HAND:DIAMOND_SHOVEL:null:mat=DIAMOND:1:0.005",""});
		rewardDropChance.put(9, new String[] {
				"CRAFTING:HAND:DIAMOND_SHOVEL:null:mat=DIAMOND:1:0.02",""});
		rewardDropChance.put(10, new String[] {
				"SMITHING:HAND:NETHERITE_SHOVEL:null:mat=NETHERITE_INGOT:1:0.005",""});
		rewardDropChance.put(11, new String[] {
				"SMITHING:HAND:NETHERITE_SHOVEL:null:mat=NETHERITE_INGOT:1:0.02",""});
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Holzschaufel",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,shovel,1,CRAFTING,HAND,WOODEN_SHOVEL% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,shovel,1,CRAFTING,HAND,WOODEN_SHOVEL% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,shovel,1,CRAFTING,HAND,WOODEN_SHOVEL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664woodenshovel",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,shovel,1,CRAFTING,HAND,WOODEN_SHOVEL% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,shovel,1,CRAFTING,HAND,WOODEN_SHOVEL% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,shovel,1,CRAFTING,HAND,WOODEN_SHOVEL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Steinschaufel",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,shovel,2,CRAFTING,HAND,STONE_SHOVEL% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,shovel,2,CRAFTING,HAND,STONE_SHOVEL% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,shovel,2,CRAFTING,HAND,STONE_SHOVEL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664stoneshovel",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,shovel,2,CRAFTING,HAND,STONE_SHOVEL% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,shovel,2,CRAFTING,HAND,STONE_SHOVEL% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,shovel,2,CRAFTING,HAND,STONE_SHOVEL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Eisenschaufel",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,shovel,3,CRAFTING,HAND,IRON_SHOVEL% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,shovel,3,CRAFTING,HAND,IRON_SHOVEL% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,shovel,3,CRAFTING,HAND,IRON_SHOVEL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664ironshovel",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,shovel,3,CRAFTING,HAND,IRON_SHOVEL% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,shovel,3,CRAFTING,HAND,IRON_SHOVEL% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,shovel,3,CRAFTING,HAND,IRON_SHOVEL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Goldschaufel",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,shovel,1,CRAFTING,HAND,GOLDEN_SHOVEL% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,shovel,1,CRAFTING,HAND,GOLDEN_SHOVEL% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,shovel,1,CRAFTING,HAND,GOLDEN_SHOVEL% Dollar",
				"&f+0,5 % Chance 1 Eisenbarren bei Herstellen einer Eisenschaufel zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664goldenshovel",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,shovel,4,CRAFTING,HAND,GOLDEN_SHOVEL% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,shovel,4,CRAFTING,HAND,GOLDEN_SHOVEL% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,shovel,4,CRAFTING,HAND,GOLDEN_SHOVEL% Dollar",
				"&f+0.5% chance to drop 1 iron ingot when crafting a iron shovel.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Diamantschaufel",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,shovel,5,CRAFTING,HAND,DIAMOND_SHOVEL% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,shovel,5,CRAFTING,HAND,DIAMOND_SHOVEL% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,shovel,5,CRAFTING,HAND,DIAMOND_SHOVEL% Dollar",
				"&f+2 % Chance 1 Eisenbarren bei Herstellen einer Eisenschaufel zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664diamondshovel",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,shovel,5,CRAFTING,HAND,DIAMOND_SHOVEL% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,shovel,5,CRAFTING,HAND,DIAMOND_SHOVEL% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,shovel,5,CRAFTING,HAND,DIAMOND_SHOVEL% Dollar",
				"&f+2% chance to drop 1 iron ingot when crafting a iron shovel.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Netheriteschaufel",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,shovel,6,SMITHING,HAND,NETHERITE_SHOVEL% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,shovel,6,SMITHING,HAND,NETHERITE_SHOVEL% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,shovel,6,SMITHING,HAND,NETHERITE_SHOVEL% Dollar",
				"&f+0,5 % Chance 1 Goldbarren bei Herstellen einer Goldschaufel zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664netheriteshovel",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,shovel,6,SMITHING,HAND,NETHERITE_SHOVEL% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,shovel,6,SMITHING,HAND,NETHERITE_SHOVEL% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,shovel,6,SMITHING,HAND,NETHERITE_SHOVEL% Dollar",
				"&f+0.5% chance to drop 1 gold ingot when crafting a gold shovel.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(7, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Goldbarren bei Herstellen einer Goldschaufel zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 gold ingot when crafting a gold shovel.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(8, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+0,5 % Chance 1 Diamand bei Herstellen einer Diamantschaufel zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+0.5% chance to drop 1 diamond when crafting a diamond shovel.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(9, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Diamand bei Herstellen einer Diamantschaufel zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 diamond when crafting a diamond shovel.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(10, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+0,5 % Chance 1 Netheritebarren bei Herstellen einer Netheriteschaufel zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+0.5% chance to drop 1 netherite ingot when crafting a netherite shovel.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(11, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Netheritebarren bei Herstellen einer Netheriteschaufel zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 netherite ingot when crafting a netherite shovel.",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"shovel", new String[] {"Schaufel", "Shovel"},
				TechnologyType.MULTIPLE, 11, PlayerAssociatedType.SOLO, 1, "", "tools", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Schaufel","&8Tech Shovel"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von &#c6a664Holzschaufel",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fCrafting of &#c6a664woodenshovel",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Tech Schaufel","&7Tech Shovel"},
				Material.WOODEN_SHOVEL, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dTech Schaufel","&dTech Shovel"},
				Material.WOODEN_SHOVEL, 1, itemflag, null, canResLore,
				new String[] {"&5Tech Schaufel","&5Tech Shovel"},
				Material.WOODEN_SHOVEL, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&f◦ Herstellung:",
						"&f◦◦ Holzsschaufel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,shovel,CRAFTING,HAND,WOODEN_SHOVEL% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,shovel,CRAFTING,HAND,WOODEN_SHOVEL% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,shovel,CRAFTING,HAND,WOODEN_SHOVEL% Dollar",
						"&f◦◦ Steinschaufel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,shovel,CRAFTING,HAND,STONE_SHOVEL% TTExp | "
										   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,shovel,CRAFTING,HAND,STONE_SHOVEL% VExp | "
										   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,shovel,CRAFTING,HAND,STONE_SHOVEL% Dollar",
						"&f◦◦ Eisenschaufel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,shovel,CRAFTING,HAND,IRON_SHOVEL% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,shovel,CRAFTING,HAND,IRON_SHOVEL% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,shovel,CRAFTING,HAND,IRON_SHOVEL% Dollar",
						"&f◦◦ Goldschaufel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,shovel,CRAFTING,HAND,GOLDEN_SHOVEL% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,shovel,CRAFTING,HAND,GOLDEN_SHOVEL% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,shovel,CRAFTING,HAND,GOLDEN_SHOVEL% Dollar",
						"&f◦◦ Diamandschaufel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,shovel,CRAFTING,HAND,DIAMOND_SHOVEL% TTExp | "
										     + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,shovel,CRAFTING,HAND,DIAMOND_SHOVEL% VExp | "
										     + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,shovel,CRAFTING,HAND,DIAMOND_SHOVEL% Dollar",
						"&f◦ Schmieden:",
						"&f◦◦ Netheriteschaufel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,shovel,SMITHING,HAND,NETHERITE_SHOVEL% TTExp | "
										       + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,shovel,SMITHING,HAND,NETHERITE_SHOVEL% VExp | "
										       + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,shovel,SMITHING,HAND,NETHERITE_SHOVEL% Dollar",
						"&f◦ Drops:",
						"&f◦◦ Eisenbarren beim herstellen einer Eisenschaufel &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,shovel,CRAFTING,HAND,IRON_SHOVEL,mat=IRON_INGOT%",
						"&f◦◦ Goldbarren beim herstellen einer Goldschaufel &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,shovel,CRAFTING,HAND,GOLDEN_SHOVEL,mat=GOLD_INGOT%",
						"&f◦◦ Diamant beim herstellen einer Diamantschaufel &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,shovel,CRAFTING,HAND,DIAMOND_SHOVEL,mat=DIAMOND%",
						"&f◦◦ Netheritebarren beim herstellen einer Netheriteschaufel &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,shovel,CRAFTING,HAND,NETHERITE_SHOVEL,mat=NETHERITE_INGOT%",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&f◦ Crafting:",
						"&f◦◦ Woodenshovel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,shovel,CRAFTING,HAND,WOODEN_SHOVEL% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,shovel,CRAFTING,HAND,WOODEN_SHOVEL% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,shovel,CRAFTING,HAND,WOODEN_SHOVEL% Dollar",
						"&f◦◦ Stoneshovel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,shovel,CRAFTING,HAND,STONE_SHOVEL% TTExp | "
										   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,shovel,CRAFTING,HAND,STONE_SHOVEL% VExp | "
										   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,shovel,CRAFTING,HAND,STONE_SHOVEL% Dollar",
						"&f◦◦ Ironshovel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,shovel,CRAFTING,HAND,IRON_SHOVEL% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,shovel,CRAFTING,HAND,IRON_SHOVEL% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,shovel,CRAFTING,HAND,IRON_SHOVEL% Dollar",
						"&f◦◦ Goldenshovel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,shovel,CRAFTING,HAND,GOLDEN_SHOVEL% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,shovel,CRAFTING,HAND,GOLDEN_SHOVEL% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,shovel,CRAFTING,HAND,GOLDEN_SHOVEL% Dollar",
						"&f◦◦ Diamondshovel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,shovel,CRAFTING,HAND,DIAMOND_SHOVEL% TTExp | "
										     + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,shovel,CRAFTING,HAND,DIAMOND_SHOVEL% VExp | "
										     + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,shovel,CRAFTING,HAND,DIAMOND_SHOVEL% Dollar",
						"&f◦ Smithing:",
						"&f◦◦ Netheriteshovel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,shovel,SMITHING,HAND,NETHERITE_SHOVEL% TTExp | "
										       + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,shovel,SMITHING,HAND,NETHERITE_SHOVEL% VExp | "
										       + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,shovel,SMITHING,HAND,NETHERITE_SHOVEL% Dollar",
						"&f◦ Drops:",
						"&f◦◦ Ironingots during the production of an ironshovel &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,shovel,CRAFTING,HAND,IRON_SHOVEL,mat=IRON_INGOT%",
						"&f◦◦ Goldingots during the production of an goldenshovel &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,shovel,CRAFTING,HAND,GOLDEN_SHOVEL,mat=GOLD_INGOT%",
						"&f◦◦ Diamonds during the production of an diamondshovel &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,shovel,CRAFTING,HAND,DIAMOND_SHOVEL,mat=DIAMOND%",
						"&f◦◦ Netheriteingots during the production of an netheriteshovel &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,shovel,CRAFTING,HAND,NETHERITE_SHOVEL,mat=NETHERITE_INGOT%",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
		toResCondition = new LinkedHashMap<>();
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(7, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(8, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(9, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(10, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(11, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(7, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(8, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(9, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(10, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(11, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(1, new String[] {
				"OAK_PLANKS;16",
				"STICK;16"});
		toResCostMaterial.put(2, new String[] {
				"COBBLESTONE;16",
				"STICK;16"});
		toResCostMaterial.put(3, new String[] {
				"IRON_INGOT;16",
				"STICK;16"});
		toResCostMaterial.put(4, new String[] {
				"GOLD_INGOT;16",
				"STICK;16"});
		toResCostMaterial.put(5, new String[] {
				"DIAMOND;16",
				"STICK;16"});
		toResCostMaterial.put(6, new String[] {
				"NETHERITE_INGOT;16",
				"STICK;16"});
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"CRAFTING:WOODEN_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(2, new String[] {
				"CRAFTING:STONE_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(3, new String[] {
				"CRAFTING:IRON_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(4, new String[] {
				"CRAFTING:GOLDEN_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(5, new String[] {
				"CRAFTING:DIAMOND_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(6, new String[] {
				"CRAFTING:NETHERITE_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rui = new String[] {
				"CRAFTING:WOODEN_HOE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:STONE_HOE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:IRON_HOE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:DIAMOND_HOE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:GOLDEN_HOE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:NETHERITE_HOE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1"};
		rewardUnlockableInteractions.put(7, rui);
		rewardUnlockableInteractions.put(8, rui);
		rewardUnlockableInteractions.put(9, rui);
		rewardUnlockableInteractions.put(10, rui);
		rewardUnlockableInteractions.put(11, rui);
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {"SHAPED:wooden_hoe",""});
		rewardUnlockableRecipe.put(2, new String[] {"SHAPED:stone_hoe",""});
		rewardUnlockableRecipe.put(3, new String[] {"SHAPED:iron_hoe",""});
		rewardUnlockableRecipe.put(4, new String[] {"SHAPED:golden_hoe",""});
		rewardUnlockableRecipe.put(5, new String[] {"SHAPED:diamond_hoe",""});
		rewardUnlockableRecipe.put(6, new String[] {"SMITHING:netherite_hoe_smithing",""});
		rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(4, new String[] {
				"CRAFTING:HAND:IRON_HOE:null:mat=IRON_INGOT:1:0.005",""});
		rewardDropChance.put(5, new String[] {
				"CRAFTING:HAND:IRON_HOE:null:mat=IRON_INGOT:1:0.02",""});
		rewardDropChance.put(6, new String[] {
				"CRAFTING:HAND:GOLDEN_HOE:null:mat=GOLD_INGOT:1:0.005",""});
		rewardDropChance.put(7, new String[] {
				"CRAFTING:HAND:GOLDEN_HOE:null:mat=GOLD_INGOT:1:0.02",""});
		rewardDropChance.put(8, new String[] {
				"CRAFTING:HAND:DIAMOND_HOE:null:mat=DIAMOND:1:0.005",""});
		rewardDropChance.put(9, new String[] {
				"CRAFTING:HAND:DIAMOND_HOE:null:mat=DIAMOND:1:0.02",""});
		rewardDropChance.put(10, new String[] {
				"SMITHING:HAND:NETHERITE_HOE:null:mat=NETHERITE_INGOT:1:0.005",""});
		rewardDropChance.put(11, new String[] {
				"SMITHING:HAND:NETHERITE_HOE:null:mat=NETHERITE_INGOT:1:0.02",""});
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Holzhacke",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,hoe,1,CRAFTING,HAND,WOODEN_HOE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,hoe,1,CRAFTING,HAND,WOODEN_HOE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,hoe,1,CRAFTING,HAND,WOODEN_HOE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664woodenhoe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,hoe,1,CRAFTING,HAND,WOODEN_HOE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,hoe,1,CRAFTING,HAND,WOODEN_HOE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,hoe,1,CRAFTING,HAND,WOODEN_HOE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Steinhacke",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,hoe,2,CRAFTING,HAND,STONE_HOE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,hoe,2,CRAFTING,HAND,STONE_HOE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,hoe,2,CRAFTING,HAND,STONE_HOE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664stonehoe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,hoe,2,CRAFTING,HAND,STONE_HOE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,hoe,2,CRAFTING,HAND,STONE_HOE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,hoe,2,CRAFTING,HAND,STONE_HOE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Eisenhacke",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,hoe,3,CRAFTING,HAND,IRON_HOE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,hoe,3,CRAFTING,HAND,IRON_HOE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,hoe,3,CRAFTING,HAND,IRON_HOE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664ironhoe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,hoe,3,CRAFTING,HAND,IRON_HOE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,hoe,3,CRAFTING,HAND,IRON_HOE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,hoe,3,CRAFTING,HAND,IRON_HOE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Goldhacke",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,hoe,4,CRAFTING,HAND,GOLDEN_HOE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,hoe,4,CRAFTING,HAND,GOLDEN_HOE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,hoe,4,CRAFTING,HAND,GOLDEN_HOE% Dollar",
				"&f+0,5 % Chance 1 Eisenbarren bei Herstellen einer Eisenhacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664goldenhoe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,hoe,4,CRAFTING,HAND,GOLDEN_HOE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,hoe,4,CRAFTING,HAND,GOLDEN_HOE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,hoe,4,CRAFTING,HAND,GOLDEN_HOE% Dollar",
				"&f+0.5% chance to drop 1 iron ingot when crafting a iron hoe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Diamanthacke",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,hoe,5,CRAFTING,HAND,DIAMOND_HOE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,hoe,5,CRAFTING,HAND,DIAMOND_HOE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,hoe,5,CRAFTING,HAND,DIAMOND_HOE% Dollar",
				"&f+2 % Chance 1 Eisenbarren bei Herstellen einer Eisenhacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664diamondhoe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,hoe,5,CRAFTING,HAND,DIAMOND_HOE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,hoe,5,CRAFTING,HAND,DIAMOND_HOE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,hoe,5,CRAFTING,HAND,DIAMOND_HOE% Dollar",
				"&f+2% chance to drop 1 iron ingot when crafting a iron hoe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Netheritehacke",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,hoe,6,SMITHING,HAND,NETHERITE_HOE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,hoe,6,SMITHING,HAND,NETHERITE_HOE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,hoe,6,SMITHING,HAND,NETHERITE_HOE% Dollar",
				"&f+0,5 % Chance 1 Goldbarren bei Herstellen einer Goldhacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664netheritehoe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,hoe,6,SMITHING,HAND,NETHERITE_HOE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,hoe,6,SMITHING,HAND,NETHERITE_HOE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,hoe,6,SMITHING,HAND,NETHERITE_HOE% Dollar",
				"&f+0.5% chance to drop 1 gold ingot when crafting a gold hoe",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(7, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Goldbarren bei Herstellen einer Goldhacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 gold ingot when crafting a gold hoe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(8, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+0,5 % Chance 1 Diamand bei Herstellen einer Diamanthacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+0.5% chance to drop 1 diamond when crafting a diamond hoe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(9, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Diamand bei Herstellen einer Diamanthacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 diamond when crafting a diamond hoe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(10, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+0,5 % Chance 1 Netheritebarren bei Herstellen einer Netheritehacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+0.5% chance to drop 1 netherite ingot when crafting a netherite hoe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(11, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Netheritebarren bei Herstellen einer Netheritehacke zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 netherite ingot when crafting a netherite hoe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"hoe", new String[] {"Hacke", "Hoe"},
				TechnologyType.MULTIPLE, 11, PlayerAssociatedType.SOLO, 2, "", "tools", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Hacke","&8Tech Shovel"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von &#c6a664Holzhacke",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fCrafting of &#c6a664woodenhoe",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&dTech Hacke","&dTech Hoe"},
				Material.WOODEN_HOE, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dTech Hacke","&dTech Hoe"},
				Material.WOODEN_HOE, 1, itemflag, null, canResLore,
				new String[] {"&5Tech Hacke","&5Tech Hoe"},
				Material.WOODEN_HOE, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&f◦ Herstellung:",
						"&f◦◦ Holzshacke &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,hoe,CRAFTING,HAND,WOODEN_HOE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,hoe,CRAFTING,HAND,WOODEN_HOE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,hoe,CRAFTING,HAND,WOODEN_HOE% Dollar",
						"&f◦◦ Steinhacke &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,hoe,CRAFTING,HAND,STONE_HOE% TTExp | "
										   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,hoe,CRAFTING,HAND,STONE_HOE% VExp | "
										   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,hoe,CRAFTING,HAND,STONE_HOE% Dollar",
						"&f◦◦ Eisenhacke &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,hoe,CRAFTING,HAND,IRON_HOE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,hoe,CRAFTING,HAND,IRON_HOE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,hoe,CRAFTING,HAND,IRON_HOE% Dollar",
						"&f◦◦ Goldhacke &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,hoe,CRAFTING,HAND,GOLDEN_HOE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,hoe,CRAFTING,HAND,GOLDEN_HOE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,hoe,CRAFTING,HAND,GOLDEN_HOE% Dollar",
						"&f◦◦ Diamandhacke &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,hoe,CRAFTING,HAND,DIAMOND_HOE% TTExp | "
										     + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,hoe,CRAFTING,HAND,DIAMOND_HOE% VExp | "
										     + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,hoe,CRAFTING,HAND,DIAMOND_HOE% Dollar",
						"&f◦ Schmieden:",
						"&f◦◦ Netheritehacke &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,hoe,SMITHING,HAND,NETHERITE_HOE% TTExp | "
										       + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,hoe,SMITHING,HAND,NETHERITE_HOE% VExp | "
										       + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,hoe,SMITHING,HAND,NETHERITE_HOE% Dollar",
						"&f◦ Drops:",
						"&f◦◦ Eisenbarren beim herstellen einer Eisenhacke &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,hoe,CRAFTING,HAND,IRON_HOE,mat=IRON_INGOT%",
						"&f◦◦ Goldbarren beim herstellen einer Goldhacke &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,hoe,CRAFTING,HAND,GOLDEN_HOE,mat=GOLD_INGOT%",
						"&f◦◦ Diamant beim herstellen einer Diamanthacke &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,hoe,CRAFTING,HAND,DIAMOND_HOE,mat=DIAMOND%",
						"&f◦◦ Netheritebarren beim herstellen einer Netheritehacke &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,hoe,CRAFTING,HAND,NETHERITE_HOE,mat=NETHERITE_INGOT%",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&f◦ Crafting:",
						"&f◦◦ Woodenhoe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,hoe,CRAFTING,HAND,WOODEN_HOE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,hoe,CRAFTING,HAND,WOODEN_HOE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,hoe,CRAFTING,HAND,WOODEN_HOE% Dollar",
						"&f◦◦ Stonehoe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,hoe,CRAFTING,HAND,STONE_HOE% TTExp | "
										   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,hoe,CRAFTING,HAND,STONE_HOE% VExp | "
										   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,hoe,CRAFTING,HAND,STONE_HOE% Dollar",
						"&f◦◦ Ironhoe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,hoe,CRAFTING,HAND,IRON_HOE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,hoe,CRAFTING,HAND,IRON_HOE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,hoe,CRAFTING,HAND,IRON_HOE% Dollar",
						"&f◦◦ Goldenhoe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,hoe,CRAFTING,HAND,GOLDEN_HOE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,hoe,CRAFTING,HAND,GOLDEN_HOE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,hoe,CRAFTING,HAND,GOLDEN_HOE% Dollar",
						"&f◦◦ Diamondhoe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,hoe,CRAFTING,HAND,DIAMOND_HOE% TTExp | "
										     + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,hoe,CRAFTING,HAND,DIAMOND_HOE% VExp | "
										     + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,hoe,CRAFTING,HAND,DIAMOND_HOE% Dollar",
						"&f◦ Smithing:",
						"&f◦◦ Netheritehoe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,hoe,SMITHING,HAND,NETHERITE_HOE% TTExp | "
										       + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,hoe,SMITHING,HAND,NETHERITE_HOE% VExp | "
										       + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,hoe,SMITHING,HAND,NETHERITE_HOE% Dollar",
						"&f◦ Drops:",
						"&f◦◦ Ironingots during the production of an ironhoe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,hoe,CRAFTING,HAND,IRON_HOE,mat=IRON_INGOT%",
						"&f◦◦ Goldingots during the production of an goldenhoe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,hoe,CRAFTING,HAND,GOLDEN_HOE,mat=GOLD_INGOT%",
						"&f◦◦ Diamonds during the production of an diamondhoe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,hoe,CRAFTING,HAND,DIAMOND_HOE,mat=DIAMOND%",
						"&f◦◦ Netheriteingots during the production of an netheritehoe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,hoe,CRAFTING,HAND,NETHERITE_HOE,mat=NETHERITE_INGOT%",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(7, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(8, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(9, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(10, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(11, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(7, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(8, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(9, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(10, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(11, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(1, new String[] {
				"OAK_PLANKS;16",
				"STICK;16"});
		toResCostMaterial.put(2, new String[] {
				"COBBLESTONE;16",
				"STICK;16"});
		toResCostMaterial.put(3, new String[] {
				"IRON_INGOT;16",
				"STICK;16"});
		toResCostMaterial.put(4, new String[] {
				"GOLD_INGOT;16",
				"STICK;16"});
		toResCostMaterial.put(5, new String[] {
				"DIAMOND;16",
				"STICK;16"});
		toResCostMaterial.put(6, new String[] {
				"NETHERITE_INGOT;16",
				"STICK;16"});
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"CRAFTING:WOODEN_AXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(2, new String[] {
				"CRAFTING:STONE_AXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(3, new String[] {
				"CRAFTING:IRON_AXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(4, new String[] {
				"CRAFTING:GOLDEN_AXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(5, new String[] {
				"CRAFTING:DIAMOND_AXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(6, new String[] {
				"CRAFTING:NETHERITE_AXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rui = new String[] {
				"CRAFTING:WOODEN_AXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:STONE_AXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:IRON_AXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:DIAMOND_AXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:GOLDEN_AXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:NETHERITE_AXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1"};
		rewardUnlockableInteractions.put(7, rui);
		rewardUnlockableInteractions.put(8, rui);
		rewardUnlockableInteractions.put(9, rui);
		rewardUnlockableInteractions.put(10, rui);
		rewardUnlockableInteractions.put(11, rui);
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {"SHAPED:wooden_axe",""});
		rewardUnlockableRecipe.put(2, new String[] {"SHAPED:stone_axe",""});
		rewardUnlockableRecipe.put(3, new String[] {"SHAPED:iron_axe",""});
		rewardUnlockableRecipe.put(4, new String[] {"SHAPED:golden_axe",""});
		rewardUnlockableRecipe.put(5, new String[] {"SHAPED:diamond_axe",""});
		rewardUnlockableRecipe.put(6, new String[] {"SMITHING:netherite_axe_smithing",""});
		rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(4, new String[] {
				"CRAFTING:HAND:IRON_AXE:null:mat=IRON_INGOT:1:0.005",""});
		rewardDropChance.put(5, new String[] {
				"CRAFTING:HAND:IRON_AXE:null:mat=IRON_INGOT:1:0.02",""});
		rewardDropChance.put(6, new String[] {
				"CRAFTING:HAND:GOLDEN_AXE:null:mat=GOLD_INGOT:1:0.005",""});
		rewardDropChance.put(7, new String[] {
				"CRAFTING:HAND:GOLDEN_AXE:null:mat=GOLD_INGOT:1:0.02",""});
		rewardDropChance.put(8, new String[] {
				"CRAFTING:HAND:DIAMOND_AXE:null:mat=DIAMOND:1:0.005",""});
		rewardDropChance.put(9, new String[] {
				"CRAFTING:HAND:DIAMOND_AXE:null:mat=DIAMOND:1:0.02",""});
		rewardDropChance.put(10, new String[] {
				"SMITHING:HAND:NETHERITE_AXE:null:mat=NETHERITE_INGOT:1:0.005",""});
		rewardDropChance.put(11, new String[] {
				"SMITHING:HAND:NETHERITE_AXE:null:mat=NETHERITE_INGOT:1:0.02",""});
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Holzaxt",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,axe,1,CRAFTING,HAND,WOODEN_AXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,axe,1,CRAFTING,HAND,WOODEN_AXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,axe,1,CRAFTING,HAND,WOODEN_AXE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664woodenaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,axe,1,CRAFTING,HAND,WOODEN_AXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,axe,1,CRAFTING,HAND,WOODEN_AXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,axe,1,CRAFTING,HAND,WOODEN_AXE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Steinaxt",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,axe,2,CRAFTING,HAND,STONE_AXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,axe,2,CRAFTING,HAND,STONE_AXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,axe,2,CRAFTING,HAND,STONE_AXE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664stoneaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,axe,2,CRAFTING,HAND,STONE_AXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,axe,2,CRAFTING,HAND,STONE_AXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,axe,2,CRAFTING,HAND,STONE_AXE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Eisenaxt",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,axe,3,CRAFTING,HAND,IRON_AXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,axe,3,CRAFTING,HAND,IRON_AXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,axe,3,CRAFTING,HAND,IRON_AXE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664ironaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,axe,3,CRAFTING,HAND,IRON_AXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,axe,3,CRAFTING,HAND,IRON_AXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,axe,3,CRAFTING,HAND,IRON_AXE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Goldaxt",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,axe,4,CRAFTING,HAND,GOLDEN_AXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,axe,4,CRAFTING,HAND,GOLDEN_AXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,axe,4,CRAFTING,HAND,GOLDEN_AXE% Dollar",
				"&f+0,5 % Chance 1 Eisenbarren bei Herstellen einer Eisenaxt zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664goldenaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,axe,4,CRAFTING,HAND,GOLDEN_AXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,axe,4,CRAFTING,HAND,GOLDEN_AXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,axe,4,CRAFTING,HAND,GOLDEN_AXE% Dollar",
				"&f+0.5% chance to drop 1 iron ingot when crafting a iron axe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Diamantaxt",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,axe,5,CRAFTING,HAND,DIAMOND_AXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,axe,5,CRAFTING,HAND,DIAMOND_AXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,axe,5,CRAFTING,HAND,DIAMOND_AXE% Dollar",
				"&f+2 % Chance 1 Eisenbarren bei Herstellen einer Eisenaxt zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664diamondaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,axe,5,CRAFTING,HAND,DIAMOND_AXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,axe,5,CRAFTING,HAND,DIAMOND_AXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,axe,5,CRAFTING,HAND,DIAMOND_AXE% Dollar",
				"&f+2% chance to drop 1 iron ingot when crafting a iron axe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Netheriteaxt",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,axe,6,SMITHING,HAND,NETHERITE_AXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,axe,6,SMITHING,HAND,NETHERITE_AXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,axe,6,SMITHING,HAND,NETHERITE_AXE% Dollar",
				"&f+0,5 % Chance 1 Goldbarren bei Herstellen einer Goldaxt zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664netheriteaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,axe,6,SMITHING,HAND,NETHERITE_AXE% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,axe,6,SMITHING,HAND,NETHERITE_AXE% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,axe,6,SMITHING,HAND,NETHERITE_AXE% Dollar",
				"&f+0.5% chance to drop 1 gold ingot when crafting a gold axe",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(7, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Goldbarren bei Herstellen einer Goldaxt zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 gold ingot when crafting a gold axe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(8, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+0,5 % Chance 1 Diamand bei Herstellen einer Diamantaxt zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+0.5% chance to drop 1 diamond when crafting a diamond axe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(9, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Diamand bei Herstellen einer Diamantaxt zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 diamond when crafting a diamond axe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(10, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+0,5 % Chance 1 Netheritebarren bei Herstellen einer Netheriteaxt zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+0.5% chance to drop 1 netherite ingot when crafting a netherite axe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(11, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Netheritebarren bei Herstellen einer Netheriteaxt zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 netherite ingot when crafting a netherite axe.",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"axe", new String[] {"Axt", "Axe"},
				TechnologyType.MULTIPLE, 11, PlayerAssociatedType.SOLO, 3, "", "tools", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Axt","&8Tech Axe"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von &#c6a664Holzaxt",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fCrafting of &#c6a664woodenaxe",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&dTech Axt","&dTech Axe"},
				Material.WOODEN_AXE, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dTech Axt","&dTech Axe"},
				Material.WOODEN_AXE, 1, itemflag, null, canResLore,
				new String[] {"&5Tech Axt","&5Tech Axe"},
				Material.WOODEN_AXE, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&f◦ Herstellung:",
						"&f◦◦ Holzaxt &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,axe,CRAFTING,HAND,WOODEN_AXE% TTExp | "
								   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,axe,CRAFTING,HAND,WOODEN_AXE% VExp | "
								   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,axe,CRAFTING,HAND,WOODEN_AXE% Dollar",
						"&f◦◦ Steinaxt &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,axe,CRAFTING,HAND,STONE_AXE% TTExp | "
								    + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,axe,CRAFTING,HAND,STONE_AXE% VExp | "
									+ "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,axe,CRAFTING,HAND,STONE_AXE% Dollar",
						"&f◦◦ Eisenaxt &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,axe,CRAFTING,HAND,IRON_AXE% TTExp | "
									+ "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,axe,CRAFTING,HAND,IRON_AXE% VExp | "
									+ "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,axe,CRAFTING,HAND,IRON_AXE% Dollar",
						"&f◦◦ Goldaxt &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,axe,CRAFTING,HAND,GOLDEN_AXE% TTExp | "
								   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,axe,CRAFTING,HAND,GOLDEN_AXE% VExp | "
								   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,axe,CRAFTING,HAND,GOLDEN_AXE% Dollar",
						"&f◦◦ Diamandaxt &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,axe,CRAFTING,HAND,DIAMOND_AXE% TTExp | "
									  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,axe,CRAFTING,HAND,DIAMOND_AXE% VExp | "
									  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,axe,CRAFTING,HAND,DIAMOND_AXE% Dollar",
						"&f◦ Schmieden:",
						"&f◦◦ Netheriteaxt &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,axe,SMITHING,HAND,NETHERITE_AXE% TTExp | "
										+ "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,axe,SMITHING,HAND,NETHERITE_AXE% VExp | "
										+ "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,axe,SMITHING,HAND,NETHERITE_AXE% Dollar",
						"&f◦ Drops:",
						"&f◦◦ Eisenbarren beim herstellen einer Eisenspitzhacke &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,axe,CRAFTING,HAND,IRON_AXE,mat=IRON_INGOT%",
						"&f◦◦ Goldbarren beim herstellen einer Goldspitzhacke &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,axe,CRAFTING,HAND,GOLDEN_AXE,mat=GOLD_INGOT%",
						"&f◦◦ Diamant beim herstellen einer Diamantspitzhacke &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,axe,CRAFTING,HAND,DIAMOND_AXE,mat=DIAMOND%",
						"&f◦◦ Netheritebarren beim herstellen einer Netheritespitzhacke &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,axe,CRAFTING,HAND,NETHERITE_AXE,mat=NETHERITE_INGOT%",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&f◦ Crafting:",
						"&f◦◦ Woodenaxe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,axe,CRAFTING,HAND,WOODEN_AXE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,axe,CRAFTING,HAND,WOODEN_AXE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,axe,CRAFTING,HAND,WOODEN_AXE% Dollar",
						"&f◦◦ Stoneaxe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,axe,CRAFTING,HAND,STONE_AXE% TTExp | "
										   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,axe,CRAFTING,HAND,STONE_AXE% VExp | "
										   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,axe,CRAFTING,HAND,STONE_AXE% Dollar",
						"&f◦◦ Ironaxe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,axe,CRAFTING,HAND,IRON_AXE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,axe,CRAFTING,HAND,IRON_AXE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,axe,CRAFTING,HAND,IRON_AXE% Dollar",
						"&f◦◦ Goldenaxe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,axe,CRAFTING,HAND,GOLDEN_AXE% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,axe,CRAFTING,HAND,GOLDEN_AXE% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,axe,CRAFTING,HAND,GOLDEN_AXE% Dollar",
						"&f◦◦ Diamondaxe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,axe,CRAFTING,HAND,DIAMOND_AXE% TTExp | "
										     + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,axe,CRAFTING,HAND,DIAMOND_AXE% VExp | "
										     + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,axe,CRAFTING,HAND,DIAMOND_AXE% Dollar",
						"&f◦ Smithing:",
						"&f◦◦ Netheriteaxe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,axe,SMITHING,HAND,NETHERITE_AXE% TTExp | "
										       + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,axe,SMITHING,HAND,NETHERITE_AXE% VExp | "
										       + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,axe,SMITHING,HAND,NETHERITE_AXE% Dollar",
						"&f◦ Drops:",
						"&f◦◦ Ironingots during the production of an ironpickaxe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,axe,CRAFTING,HAND,IRON_AXE,mat=IRON_INGOT%",
						"&f◦◦ Goldingots during the production of an goldenpickaxe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,axe,CRAFTING,HAND,GOLDEN_AXE,mat=GOLD_INGOT%",
						"&f◦◦ Diamonds during the production of an diamondpickaxe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,axe,CRAFTING,HAND,DIAMOND_AXE,mat=DIAMOND%",
						"&f◦◦ Netheriteingots during the production of an netheritepickaxe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,axe,CRAFTING,HAND,NETHERITE_AXE,mat=NETHERITE_INGOT%",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Miscellaneous_Weapons_Armor(String[] itemflag, String[] enchantment) //INFO:Miscellaneous_Weapons_Armor
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(7, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(8, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(9, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(10, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(11, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(7, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(8, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(9, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(10, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(11, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(1, new String[] {
				"OAK_PLANKS;16",
				"STICK;16"});
		toResCostMaterial.put(2, new String[] {
				"COBBLESTONE;16",
				"STICK;16"});
		toResCostMaterial.put(3, new String[] {
				"IRON_INGOT;16",
				"STICK;16"});
		toResCostMaterial.put(4, new String[] {
				"GOLD_INGOT;16",
				"STICK;16"});
		toResCostMaterial.put(5, new String[] {
				"DIAMOND;16",
				"STICK;16"});
		toResCostMaterial.put(6, new String[] {
				"NETHERITE_INGOT;16",
				"STICK;16"});
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"CRAFTING:WOODEN_SWORD:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(2, new String[] {
				"CRAFTING:STONE_SWORD:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(3, new String[] {
				"CRAFTING:IRON_SWORD:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(4, new String[] {
				"CRAFTING:GOLDEN_SWORD:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(5, new String[] {
				"CRAFTING:DIAMOND_SWORD:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(6, new String[] {
				"CRAFTING:NETHERITE_SWORD:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		String[] rui = new String[] {
				"CRAFTING:WOODEN_SWORD:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:STONE_SWORD:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:IRON_SWORD:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:DIAMOND_SWORD:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:GOLDEN_SWORD:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:NETHERITE_SWORD:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1"};
		rewardUnlockableInteractions.put(7, rui);
		rewardUnlockableInteractions.put(8, rui);
		rewardUnlockableInteractions.put(9, rui);
		rewardUnlockableInteractions.put(10, rui);
		rewardUnlockableInteractions.put(11, rui);
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {"SHAPED:wooden_sword",""});
		rewardUnlockableRecipe.put(2, new String[] {"SHAPED:stone_sword",""});
		rewardUnlockableRecipe.put(3, new String[] {"SHAPED:iron_sword",""});
		rewardUnlockableRecipe.put(4, new String[] {"SHAPED:golden_sword",""});
		rewardUnlockableRecipe.put(5, new String[] {"SHAPED:diamond_sword",""});
		rewardUnlockableRecipe.put(6, new String[] {"SMITHING:netherite_sword_smithing",""});
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(4, new String[] {
				"CRAFTING:HAND:IRON_SWORD:null:mat=IRON_INGOT:1:0.005",""});
		rewardDropChance.put(5, new String[] {
				"CRAFTING:HAND:IRON_SWORD:null:mat=IRON_INGOT:1:0.02",""});
		rewardDropChance.put(6, new String[] {
				"CRAFTING:HAND:GOLDEN_SWORD:null:mat=GOLD_INGOT:1:0.005",""});
		rewardDropChance.put(7, new String[] {
				"CRAFTING:HAND:GOLDEN_SWORD:null:mat=GOLD_INGOT:1:0.02",""});
		rewardDropChance.put(8, new String[] {
				"CRAFTING:HAND:DIAMOND_SWORD:null:mat=DIAMOND:1:0.005",""});
		rewardDropChance.put(9, new String[] {
				"CRAFTING:HAND:DIAMOND_SWORD:null:mat=DIAMOND:1:0.02",""});
		rewardDropChance.put(10, new String[] {
				"SMITHING:HAND:NETHERITE_SWORD:null:mat=NETHERITE_INGOT:1:0.005",""});
		rewardDropChance.put(11, new String[] {
				"SMITHING:HAND:NETHERITE_SWORD:null:mat=NETHERITE_INGOT:1:0.02",""});
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Holzaxt",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sword,1,CRAFTING,HAND,WOODEN_SWORD% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sword,1,CRAFTING,HAND,WOODEN_SWORD% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sword,1,CRAFTING,HAND,WOODEN_SWORD% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664woodenaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sword,1,CRAFTING,HAND,WOODEN_SWORD% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sword,1,CRAFTING,HAND,WOODEN_SWORD% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sword,1,CRAFTING,HAND,WOODEN_SWORD% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Steinaxt",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sword,2,CRAFTING,HAND,STONE_SWORD% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sword,2,CRAFTING,HAND,STONE_SWORD% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sword,2,CRAFTING,HAND,STONE_SWORD% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664stoneaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sword,2,CRAFTING,HAND,STONE_SWORD% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sword,2,CRAFTING,HAND,STONE_SWORD% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sword,2,CRAFTING,HAND,STONE_SWORD% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Eisenaxt",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sword,3,CRAFTING,HAND,IRON_SWORD% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sword,3,CRAFTING,HAND,IRON_SWORD% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sword,3,CRAFTING,HAND,IRON_SWORD% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664ironaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sword,3,CRAFTING,HAND,IRON_SWORD% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sword,3,CRAFTING,HAND,IRON_SWORD% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sword,3,CRAFTING,HAND,IRON_SWORD% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Goldaxt",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sword,4,CRAFTING,HAND,GOLDEN_SWORD% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sword,4,CRAFTING,HAND,GOLDEN_SWORD% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sword,4,CRAFTING,HAND,GOLDEN_SWORD% Dollar",
				"&f+0,5 % Chance 1 Eisenbarren bei Herstellen einer Eisenschwert zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664goldenaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sword,4,CRAFTING,HAND,GOLDEN_SWORD% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sword,4,CRAFTING,HAND,GOLDEN_SWORD% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sword,4,CRAFTING,HAND,GOLDEN_SWORD% Dollar",
				"&f+0.5% chance to drop 1 iron ingot when crafting a iron sword.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Diamantaxt",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sword,5,CRAFTING,HAND,DIAMOND_SWORD% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sword,5,CRAFTING,HAND,DIAMOND_SWORD% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sword,5,CRAFTING,HAND,DIAMOND_SWORD% Dollar",
				"&f+2 % Chance 1 Eisenbarren bei Herstellen einer Eisenschwert zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664diamondaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sword,5,CRAFTING,HAND,DIAMOND_SWORD% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sword,5,CRAFTING,HAND,DIAMOND_SWORD% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sword,5,CRAFTING,HAND,DIAMOND_SWORD% Dollar",
				"&f+2% chance to drop 1 iron ingot when crafting a iron sword.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Netheriteaxt",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sword,6,SMITHING,HAND,NETHERITE_SWORD% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sword,6,SMITHING,HAND,NETHERITE_SWORD% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sword,6,SMITHING,HAND,NETHERITE_SWORD% Dollar",
				"&f+0,5 % Chance 1 Goldbarren bei Herstellen einer Goldschwert zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664netheriteaxe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sword,6,SMITHING,HAND,NETHERITE_SWORD% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sword,6,SMITHING,HAND,NETHERITE_SWORD% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sword,6,SMITHING,HAND,NETHERITE_SWORD% Dollar",
				"&f+0.5% chance to drop 1 gold ingot when crafting a gold axe",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(7, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Goldbarren bei Herstellen einer Goldschwert zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 gold ingot when crafting a gold sword.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(8, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+0,5 % Chance 1 Diamand bei Herstellen einer Diamantschwert zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+0.5% chance to drop 1 diamond when crafting a diamond sword.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(9, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Diamand bei Herstellen einer Diamantschwert zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 diamond when crafting a diamond sword.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(10, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+0,5 % Chance 1 Netheritebarren bei Herstellen einer Netheriteschwert zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+0.5% chance to drop 1 netherite ingot when crafting a netherite sword.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(11, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Netheritebarren bei Herstellen einer Netheriteschwert zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 netherite ingot when crafting a netherite sword.",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"sword", new String[] {"Schwert", "Sword"},
				TechnologyType.MULTIPLE, 11, PlayerAssociatedType.SOLO, 0, "", "weapons_armor", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Schwert","&8Tech Sword"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von &#c6a664Holzschwert",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fCrafting of &#c6a664woodensword",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Schwert","&7Sword"},
				Material.WOODEN_SWORD, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dSchwert","&dSword"},
				Material.WOODEN_SWORD, 1, itemflag, null, canResLore,
				new String[] {"&5Schwert","&5Sword"},
				Material.WOODEN_SWORD, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&f◦ Herstellung:",
						"&f◦◦ Holzschwert &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sword,CRAFTING,HAND,WOODEN_SWORD% TTExp | "
								   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sword,CRAFTING,HAND,WOODEN_SWORD% VExp | "
								   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sword,CRAFTING,HAND,WOODEN_SWORD% Dollar",
						"&f◦◦ Steinschwert &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sword,CRAFTING,HAND,STONE_SWORD% TTExp | "
								    + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sword,CRAFTING,HAND,STONE_SWORD% VExp | "
									+ "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sword,CRAFTING,HAND,STONE_SWORD% Dollar",
						"&f◦◦ Eisenschwert &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sword,CRAFTING,HAND,IRON_SWORD% TTExp | "
									+ "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sword,CRAFTING,HAND,IRON_SWORD% VExp | "
									+ "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sword,CRAFTING,HAND,IRON_SWORD% Dollar",
						"&f◦◦ Goldschwert &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sword,CRAFTING,HAND,GOLDEN_SWORD% TTExp | "
								   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sword,CRAFTING,HAND,GOLDEN_SWORD% VExp | "
								   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sword,CRAFTING,HAND,GOLDEN_SWORD% Dollar",
						"&f◦◦ Diamandschwert &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sword,CRAFTING,HAND,DIAMOND_SWORD% TTExp | "
									  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sword,CRAFTING,HAND,DIAMOND_SWORD% VExp | "
									  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sword,CRAFTING,HAND,DIAMOND_SWORD% Dollar",
						"&f◦ Schmieden:",
						"&f◦◦ Netheriteschwert &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sword,SMITHING,HAND,NETHERITE_SWORD% TTExp | "
										+ "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sword,SMITHING,HAND,NETHERITE_SWORD% VExp | "
										+ "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sword,SMITHING,HAND,NETHERITE_SWORD% Dollar",
						"&f◦ Drops:",
						"&f◦◦ Eisenbarren beim herstellen einer Eisenschwert &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,sword,CRAFTING,HAND,IRON_SWORD,mat=IRON_INGOT%",
						"&f◦◦ Goldbarren beim herstellen einer Goldschwert &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,sword,CRAFTING,HAND,GOLDEN_SWORD,mat=GOLD_INGOT%",
						"&f◦◦ Diamant beim herstellen einer Diamantschwert &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,sword,CRAFTING,HAND,DIAMOND_SWORD,mat=DIAMOND%",
						"&f◦◦ Netheritebarren beim herstellen einer Netheriteschwert &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,sword,CRAFTING,HAND,NETHERITE_SWORD,mat=NETHERITE_INGOT%",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&f◦ Crafting:",
						"&f◦◦ Woodensword &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sword,CRAFTING,HAND,WOODEN_SWORD% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sword,CRAFTING,HAND,WOODEN_SWORD% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sword,CRAFTING,HAND,WOODEN_SWORD% Dollar",
						"&f◦◦ Stonesword &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sword,CRAFTING,HAND,STONE_SWORD% TTExp | "
										   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sword,CRAFTING,HAND,STONE_SWORD% VExp | "
										   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sword,CRAFTING,HAND,STONE_SWORD% Dollar",
						"&f◦◦ Ironsword &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sword,CRAFTING,HAND,IRON_SWORD% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sword,CRAFTING,HAND,IRON_SWORD% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sword,CRAFTING,HAND,IRON_SWORD% Dollar",
						"&f◦◦ Goldensword &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sword,CRAFTING,HAND,GOLDEN_SWORD% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sword,CRAFTING,HAND,GOLDEN_SWORD% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sword,CRAFTING,HAND,GOLDEN_SWORD% Dollar",
						"&f◦◦ Diamondsword &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sword,CRAFTING,HAND,DIAMOND_SWORD% TTExp | "
										     + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sword,CRAFTING,HAND,DIAMOND_SWORD% VExp | "
										     + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sword,CRAFTING,HAND,DIAMOND_SWORD% Dollar",
						"&f◦ Smithing:",
						"&f◦◦ Netheritesword &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sword,SMITHING,HAND,NETHERITE_SWORD% TTExp | "
										       + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sword,SMITHING,HAND,NETHERITE_SWORD% VExp | "
										       + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sword,SMITHING,HAND,NETHERITE_SWORD% Dollar",
						"&f◦ Drops:",
						"&f◦◦ Ironingots during the production of an ironsword &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,sword,CRAFTING,HAND,IRON_SWORD,mat=IRON_INGOT%",
						"&f◦◦ Goldingots during the production of an goldensword &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,sword,CRAFTING,HAND,GOLDEN_SWORD,mat=GOLD_INGOT%",
						"&f◦◦ Diamonds during the production of an diamondsword &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,sword,CRAFTING,HAND,DIAMOND_SWORD,mat=DIAMOND%",
						"&f◦◦ Netheriteingots during the production of an netheritesword &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,sword,CRAFTING,HAND,NETHERITE_SWORD,mat=NETHERITE_INGOT%",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1", "else:o_2",
				"output:o_1:true",
				"a:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(7, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(8, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(9, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(10, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostTTExp.put(11, "10 * (100 * techlev + 50 * techacq + 25 * solototaltech)");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(7, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(8, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(9, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(10, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMoney.put(11, "5 * (1 * techlev + 0.5 * techacq + 0.25 * solototaltech)");
		toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(1, new String[] {
				"LEATHER;2",
				"STRING;2"});
		toResCostMaterial.put(2, new String[] {
				"IRON_INGOT;4",
				"STRING;8"});
		toResCostMaterial.put(3, new String[] {
				"GOLD_INGOT;4",
				"STRING;16"});
		toResCostMaterial.put(4, new String[] {
				"DIAMOND;8",
				"STRING;32"});
		toResCostMaterial.put(5, new String[] {
				"NETHERITE_INGOT;8",
				"STRING;64"});
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"CRAFTING:LEATHER_BOOTS:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(2, new String[] {
				"CRAFTING:IRON_BOOTS:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(3, new String[] {
				"CRAFTING:GOLDEN_BOOTS:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(4, new String[] {
				"CRAFTING:DIAMOND_BOOTS:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rewardUnlockableInteractions.put(5, new String[] {
				"CRAFTING:NETHERITE_BOOTS:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1", ""});
		rui = new String[] {
				"CRAFTING:LEATHER_BOOTS:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:IRON_BOOTS:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:GOLDEN_BOOTS:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:DIAMOND_BOOTS:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:NETHERITE_BOOTS:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1"};
		rewardUnlockableInteractions.put(6, rui);
		rewardUnlockableInteractions.put(7, rui);
		rewardUnlockableInteractions.put(8, rui);
		rewardUnlockableInteractions.put(9, rui);
		rewardUnlockableInteractions.put(10, rui);
		rewardUnlockableInteractions.put(11, rui);
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {"SHAPED:leather_boots",""});
		rewardUnlockableRecipe.put(2, new String[] {"SHAPED:iron_boots",""});
		rewardUnlockableRecipe.put(3, new String[] {"SHAPED:golden_boots",""});
		rewardUnlockableRecipe.put(4, new String[] {"SHAPED:diamond_boots",""});
		rewardUnlockableRecipe.put(5, new String[] {"SMITHING:netherite_boots_smithing",""});
		rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(4, new String[] {
				"CRAFTING:HAND:IRON_BOOTS:null:mat=IRON_INGOT:1:0.005",""});
		rewardDropChance.put(5, new String[] {
				"CRAFTING:HAND:IRON_BOOTS:null:mat=IRON_INGOT:1:0.02",""});
		rewardDropChance.put(6, new String[] {
				"CRAFTING:HAND:GOLDEN_BOOTS:null:mat=GOLD_INGOT:1:0.005",""});
		rewardDropChance.put(7, new String[] {
				"CRAFTING:HAND:GOLDEN_BOOTS:null:mat=GOLD_INGOT:1:0.02",""});
		rewardDropChance.put(8, new String[] {
				"CRAFTING:HAND:DIAMOND_BOOTS:null:mat=DIAMOND:1:0.005",""});
		rewardDropChance.put(9, new String[] {
				"CRAFTING:HAND:DIAMOND_BOOTS:null:mat=DIAMOND:1:0.02",""});
		rewardDropChance.put(10, new String[] {
				"SMITHING:HAND:NETHERITE_BOOTS:null:mat=NETHERITE_INGOT:1:0.005",""});
		rewardDropChance.put(11, new String[] {
				"SMITHING:HAND:NETHERITE_BOOTS:null:mat=NETHERITE_INGOT:1:0.02",""});
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Lederschuhe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,boots,1,CRAFTING,HAND,LEATHER_BOOTS% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,boots,1,CRAFTING,HAND,LEATHER_BOOTS% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,boots,1,CRAFTING,HAND,LEATHER_BOOTS% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664leatherboots",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,boots,1,CRAFTING,HAND,LEATHER_BOOTS% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,boots,1,CRAFTING,HAND,LEATHER_BOOTS% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,boots,1,CRAFTING,HAND,LEATHER_BOOTS% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Eisenschuhe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,boots,2,CRAFTING,HAND,IRON_BOOTS% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,boots,2,CRAFTING,HAND,IRON_BOOTS% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,boots,2,CRAFTING,HAND,IRON_BOOTS% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664ironboots",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,boots,2,CRAFTING,HAND,IRON_BOOTS% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,boots,2,CRAFTING,HAND,IRON_BOOTS% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,boots,2,CRAFTING,HAND,IRON_BOOTS% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Goldschuhe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,boots,3,CRAFTING,HAND,GOLDEN_BOOTS% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,boots,3,CRAFTING,HAND,GOLDEN_BOOTS% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,boots,3,CRAFTING,HAND,GOLDEN_BOOTS% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664goldenboots",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,boots,3,CRAFTING,HAND,GOLDEN_BOOTS% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,boots,3,CRAFTING,HAND,GOLDEN_BOOTS% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,boots,3,CRAFTING,HAND,GOLDEN_BOOTS% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Diamantschuhe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,boots,4,CRAFTING,HAND,DIAMOND_BOOTS% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,boots,4,CRAFTING,HAND,DIAMOND_BOOTS% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,boots,4,CRAFTING,HAND,DIAMOND_BOOTS% Dollar",
				"&f+0,5 % Chance 1 Eisenbarren bei Herstellen einer Eisenschuhe zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664diamondboots",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,boots,4,CRAFTING,HAND,DIAMOND_BOOTS% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,boots,4,CRAFTING,HAND,DIAMOND_BOOTS% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,boots,4,CRAFTING,HAND,DIAMOND_BOOTS% Dollar",
				"&f+0.5% chance to drop 1 iron ingot when crafting a iron boots.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fHerstellen von &#c6a664Netheriteschuhe",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,boots,5,CRAFTING,HAND,NETHERITE_BOOTS% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,boots,5,CRAFTING,HAND,NETHERITE_BOOTS% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,boots,5,CRAFTING,HAND,NETHERITE_BOOTS% Dollar",
				"&f+2 % Chance 1 Eisenbarren bei Herstellen einer Eisenschuhe zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fCrafting of &#c6a664netheriteboots",
				"&f◦ &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,boots,5,CRAFTING,HAND,NETHERITE_BOOTS% TTExp | "
				  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,boots,5,CRAFTING,HAND,NETHERITE_BOOTS% VExp | "
				  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,boots,5,CRAFTING,HAND,NETHERITE_BOOTS% Dollar",
				"&f+2% chance to drop 1 iron ingot when crafting a iron boots.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+0,5 % Chance 1 Goldbarren bei Herstellen einer Goldschuhe zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+0.5% chance to drop 1 gold ingot when crafting a gold boots",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(7, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Goldbarren bei Herstellen einer Goldschuhe zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 gold ingot when crafting a gold boots.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(8, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+0,5 % Chance 1 Diamand bei Herstellen einer Diamantschuhe zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+0.5% chance to drop 1 diamond when crafting a diamond boots.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(9, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Diamand bei Herstellen einer Diamantschuhe zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 diamond when crafting a diamond boots.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(10, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+0,5 % Chance 1 Netheritebarren bei Herstellen einer Netheriteschuhe zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+0.5% chance to drop 1 netherite ingot when crafting a netherite boots.",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(11, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f+2 % Chance 1 Netheritebarren bei Herstellen einer Netheriteschuhe zu droppen.",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f+2% chance to drop 1 netherite ingot when crafting a netherite boots.",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"boots", new String[] {"Schuhe", "Boots"},
				TechnologyType.MULTIPLE, 11, PlayerAssociatedType.SOLO, 1, "", "weapons_armor", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Schuhe","&8Tech Boots"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von &#c6a664Lederschuhe",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fCrafting of &#c6a664leatherboots",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Schuhe","&7Boots"},
				Material.LEATHER_BOOTS, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dSchuhe","&dBoots"},
				Material.LEATHER_BOOTS, 1, itemflag, null, canResLore,
				new String[] {"&5Schuhe","&5Boots"},
				Material.LEATHER_BOOTS, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&f◦ Herstellung:",
						"&f◦◦ Lederschuhe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,boots,CRAFTING,HAND,LEATHER_BOOTS% TTExp | "
								   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,boots,CRAFTING,HAND,LEATHER_BOOTS% VExp | "
								   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,boots,CRAFTING,HAND,LEATHER_BOOTS% Dollar",
						"&f◦◦ Eisenschuhe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,boots,CRAFTING,HAND,IRON_SWORD% TTExp | "
									+ "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,boots,CRAFTING,HAND,IRON_SWORD% VExp | "
									+ "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,boots,CRAFTING,HAND,IRON_SWORD% Dollar",
						"&f◦◦ Goldschuhe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,boots,CRAFTING,HAND,GOLDEN_SWORD% TTExp | "
								   + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,boots,CRAFTING,HAND,GOLDEN_SWORD% VExp | "
								   + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,boots,CRAFTING,HAND,GOLDEN_SWORD% Dollar",
						"&f◦◦ Diamandschuhe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,boots,CRAFTING,HAND,DIAMOND_SWORD% TTExp | "
									  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,boots,CRAFTING,HAND,DIAMOND_SWORD% VExp | "
									  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,boots,CRAFTING,HAND,DIAMOND_SWORD% Dollar",
						"&f◦ Schmieden:",
						"&f◦◦ Netheriteschuhe &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,boots,SMITHING,HAND,NETHERITE_SWORD% TTExp | "
										+ "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,boots,SMITHING,HAND,NETHERITE_SWORD% VExp | "
										+ "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,boots,SMITHING,HAND,NETHERITE_SWORD% Dollar",
						"&f◦ Drops:",
						"&f◦◦ Eisenbarren beim herstellen einer Eisenschuhe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,boots,CRAFTING,HAND,IRON_SWORD,mat=IRON_INGOT%",
						"&f◦◦ Goldbarren beim herstellen einer Goldschuhe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,boots,CRAFTING,HAND,GOLDEN_SWORD,mat=GOLD_INGOT%",
						"&f◦◦ Diamant beim herstellen einer Diamantschuhe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,boots,CRAFTING,HAND,DIAMOND_SWORD,mat=DIAMOND%",
						"&f◦◦ Netheritebarren beim herstellen einer Netheriteschuhe &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,boots,CRAFTING,HAND,NETHERITE_SWORD,mat=NETHERITE_INGOT%",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&f◦ Crafting:",
						"&f◦◦ Leatherboots &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,boots,CRAFTING,HAND,LEATHER_BOOTS% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,boots,CRAFTING,HAND,LEATHER_BOOTS% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,boots,CRAFTING,HAND,LEATHER_BOOTS% Dollar",
						"&f◦◦ Ironboots &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,boots,CRAFTING,HAND,IRON_SWORD% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,boots,CRAFTING,HAND,IRON_SWORD% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,boots,CRAFTING,HAND,IRON_SWORD% Dollar",
						"&f◦◦ Goldenboots &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,boots,CRAFTING,HAND,GOLDEN_SWORD% TTExp | "
										  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,boots,CRAFTING,HAND,GOLDEN_SWORD% VExp | "
										  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,boots,CRAFTING,HAND,GOLDEN_SWORD% Dollar",
						"&f◦◦ Diamondboots &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,boots,CRAFTING,HAND,DIAMOND_SWORD% TTExp | "
										     + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,boots,CRAFTING,HAND,DIAMOND_SWORD% VExp | "
										     + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,boots,CRAFTING,HAND,DIAMOND_SWORD% Dollar",
						"&f◦ Smithing:",
						"&f◦◦ Netheriteboots &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,boots,SMITHING,HAND,NETHERITE_SWORD% TTExp | "
										       + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,boots,SMITHING,HAND,NETHERITE_SWORD% VExp | "
										       + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,boots,SMITHING,HAND,NETHERITE_SWORD% Dollar",
						"&f◦ Drops:",
						"&f◦◦ Ironingots during the production of an ironboots &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,boots,CRAFTING,HAND,IRON_SWORD,mat=IRON_INGOT%",
						"&f◦◦ Goldingots during the production of an goldenboots &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,boots,CRAFTING,HAND,GOLDEN_SWORD,mat=GOLD_INGOT%",
						"&f◦◦ Diamonds during the production of an diamondboots &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,boots,CRAFTING,HAND,DIAMOND_SWORD,mat=DIAMOND%",
						"&f◦◦ Netheriteingots during the production of an netheriteboots &#546f42%tt_reward_techtotal_dropchance_mat,SOLO,boots,CRAFTING,HAND,NETHERITE_SWORD,mat=NETHERITE_INGOT%",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Mining_Soil(String[] itemflag, String[] enchantment) //INFO:Mining_Soil
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(7, "100 * techlev + 50 * techacq + 25 * solototaltech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(7, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(7, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(2, new String[] {
				"WOODEN_SHOVEL;8",
				"STICK;8"});
		toResCostMaterial.put(2, new String[] {
				"STONE_SHOVEL;8",
				"STICK;8"});
		toResCostMaterial.put(3, new String[] {
				"IRON_SHOVEL;4",
				"STICK;8"});
		toResCostMaterial.put(4, new String[] {
				"GOLDEN_SHOVEL;4",
				"STICK;8"});
		toResCostMaterial.put(5, new String[] {
				"DIAMOND_SHOVEL;3",
				"STICK;8"});
		toResCostMaterial.put(6, new String[] {
				"NETHERITE_SHOVEL;2",
				"STICK;8"});
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"BREAKING:DIRT:null:tool=HAND:ttexp=10.0:vaexp=10:vault=0.1:default=1.0", //TODO Change back
				"BREAKING:GRASS_BLOCK:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SAND:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRAVEL:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"BREAKING:DIRT:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRASS_BLOCK:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SAND:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRAVEL:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"BREAKING:DIRT:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRASS_BLOCK:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SAND:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRAVEL:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"BREAKING:DIRT:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRASS_BLOCK:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SAND:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRAVEL:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"BREAKING:DIRT:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRASS_BLOCK:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SAND:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRAVEL:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"BREAKING:DIRT:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRASS_BLOCK:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SAND:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRAVEL:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(7, new String[] {
				"BREAKING:DIRT:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRASS_BLOCK:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SAND:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRAVEL:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:HAND:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:HAND:GRASS_BLOCK:null:mat=DIRT:1:1.0",
				"BREAKING:HAND:SAND:null:mat=SAND:1:1.0",
				"BREAKING:HAND:GRAVEL:null:mat=GRAVEL:1:0.75",
				"BREAKING:HAND:GRAVEL:null:mat=FLINT:1:0.25"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:WOODEN_SHOVEL:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:WOODEN_SHOVEL:GRASS_BLOCK:null:mat=DIRT:1:1.0",
				"BREAKING:WOODEN_SHOVEL:SAND:null:mat=SAND:1:1.0",
				"BREAKING:WOODEN_SHOVEL:GRAVEL:null:mat=GRAVEL:1:0.75",
				"BREAKING:WOODEN_SHOVEL:GRAVEL:null:mat=FLINT:1:0.25"});
		rewardDropChance.put(3, new String[] {
				"BREAKING:STONE_SHOVEL:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:STONE_SHOVEL:GRASS_BLOCK:null:mat=DIRT:1:1.0",
				"BREAKING:STONE_SHOVEL:SAND:null:mat=SAND:1:1.0",
				"BREAKING:STONE_SHOVEL:GRAVEL:null:mat=GRAVEL:1:0.75",
				"BREAKING:STONE_SHOVEL:GRAVEL:null:mat=FLINT:1:0.25"});
		rewardDropChance.put(4, new String[] {
				"BREAKING:IRON_SHOVEL:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:IRON_SHOVEL:GRASS_BLOCK:null:mat=DIRT:1:1.0",
				"BREAKING:IRON_SHOVEL:SAND:null:mat=SAND:1:1.0",
				"BREAKING:IRON_SHOVEL:GRAVEL:null:mat=GRAVEL:1:0.75",
				"BREAKING:IRON_SHOVEL:GRAVEL:null:mat=FLINT:1:0.25"});
		rewardDropChance.put(5, new String[] {
				"BREAKING:GOLDEN_SHOVEL:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:GRASS_BLOCK:null:mat=DIRT:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:SAND:null:mat=SAND:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:GRAVEL:null:mat=GRAVEL:1:0.75",
				"BREAKING:GOLDEN_SHOVEL:GRAVEL:null:mat=FLINT:1:0.25"});
		rewardDropChance.put(6, new String[] {
				"BREAKING:DIAMOND_SHOVEL:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:GRASS_BLOCK:null:mat=DIRT:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:SAND:null:mat=SAND:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:GRAVEL:null:mat=GRAVEL:1:0.75",
				"BREAKING:DIAMOND_SHOVEL:GRAVEL:null:mat=FLINT:1:0.25"});
		rewardDropChance.put(7, new String[] {
				"BREAKING:NETHERITE_SHOVEL:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:GRASS_BLOCK:null:mat=DIRT:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:SAND:null:mat=SAND:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:GRAVEL:null:mat=GRAVEL:1:0.75",
				"BREAKING:NETHERITE_SHOVEL:GRAVEL:null:mat=FLINT:1:0.25"});
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:HAND:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:HAND:GRASS_BLOCK:null:mat=DIRT:1:1.0",
				"BREAKING:HAND:SAND:null:mat=SAND:1:1.0",
				"BREAKING:HAND:GRAVEL:null:mat=GRAVEL:1:1.0"});
		rewardSilkTouchDropChance.put(2, new String[] {
				"BREAKING:WOODEN_SHOVEL:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:WOODEN_SHOVEL:GRASS_BLOCK:null:mat=GRASS_BLOCK:1:1.0",
				"BREAKING:WOODEN_SHOVEL:SAND:null:mat=SAND:1:1.0",
				"BREAKING:WOODEN_SHOVEL:GRAVEL:null:mat=GRAVEL:1:1.0"});
		rewardSilkTouchDropChance.put(3, new String[] {
				"BREAKING:STONE_SHOVEL:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:STONE_SHOVEL:GRASS_BLOCK:null:mat=GRASS_BLOCK:1:1.0",
				"BREAKING:STONE_SHOVEL:SAND:null:mat=SAND:1:1.0",
				"BREAKING:STONE_SHOVEL:GRAVEL:null:mat=GRAVEL:1:1.0"});
		rewardSilkTouchDropChance.put(4, new String[] {
				"BREAKING:IRON_SHOVEL:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:IRON_SHOVEL:GRASS_BLOCK:null:mat=GRASS_BLOCK:1:1.0",
				"BREAKING:IRON_SHOVEL:SAND:null:mat=SAND:1:1.0",
				"BREAKING:IRON_SHOVEL:GRAVEL:null:mat=GRAVEL:1:1.0"});
		rewardSilkTouchDropChance.put(5, new String[] {
				"BREAKING:GOLDEN_SHOVEL:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:GRASS_BLOCK:null:mat=GRASS_BLOCK:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:SAND:null:mat=SAND:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:GRAVEL:null:mat=GRAVEL:1:1.0"});
		rewardSilkTouchDropChance.put(6, new String[] {
				"BREAKING:DIAMOND_SHOVEL:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:GRASS_BLOCK:null:mat=GRASS_BLOCK:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:SAND:null:mat=SAND:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:GRAVEL:null:mat=GRAVEL:1:1.0"});
		rewardSilkTouchDropChance.put(7, new String[] {
				"BREAKING:NETHERITE_SHOVEL:DIRT:null:mat=DIRT:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:GRASS_BLOCK:null:mat=GRASS_BLOCK:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:SAND:null:mat=SAND:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:GRAVEL:null:mat=GRAVEL:1:1.0"});
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Erde/Erde(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,1,BREAKING,HAND,DIRT,mat=DIRT%",
				"&fBehuts. % von &#c6a664Erde/Erde(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,1,BREAKING,HAND,DIRT,mat=DIRT%",
				"&fAbbauen von &#c6a664Erde(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,1,BREAKING,HAND,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,1,BREAKING,HAND,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,1,BREAKING,HAND,DIRT% Dollar",
						  
				"&f% von &#c6a664Grasblock/Erde(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,1,BREAKING,HAND,GRASS_BLOCK,mat=DIRT%",
				"&fBehuts. % von &#c6a664Grasblock/Grasblock(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,1,BREAKING,HAND,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fAbbauen von &#c6a664Grasblock(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,1,BREAKING,HAND,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,1,BREAKING,HAND,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,1,BREAKING,HAND,GRASS_BLOCK% Dollar",
						  
				"&f% von &#c6a664Sand/Sand(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,1,BREAKING,HAND,SAND,mat=SAND%",
				"&fBehuts. % von &#c6a664Sand/Sand(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,1,BREAKING,HAND,SAND,mat=SAND%",
				"&fAbbauen von &#c6a664Sand(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,1,BREAKING,HAND,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,1,BREAKING,HAND,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,1,BREAKING,HAND,SAND% Dollar",
						  
				"&f% von &#c6a664Kies/Kies(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,1,BREAKING,HAND,GRAVEL,mat=GRAVEL%",
				"&fBehuts. % von &#c6a664Kies/Kies(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,1,BREAKING,HAND,GRAVEL,mat=GRAVEL%",
				"&f% von &#c6a664Kies/Feuerstein(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,1,BREAKING,HAND,GRAVEL,mat=FLINT%",
				"&fAbbauen von &#c6a664Kies(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,1,BREAKING,HAND,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,1,BREAKING,HAND,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,1,BREAKING,HAND,GRAVEL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Dirt/Dirt(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,1,BREAKING,HAND,DIRT,mat=DIRT%",
				"&fSilkT% of &#c6a664Dirt/Dirt(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,1,BREAKING,HAND,DIRT,mat=DIRT%",
				"&fMining of &#c6a664Dirt(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,1,BREAKING,HAND,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,1,BREAKING,HAND,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,1,BREAKING,HAND,DIRT% Dollar",
						  
				"&f% of &#c6a664Grasblock/Dirt(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,1,BREAKING,HAND,GRASS_BLOCK,mat=DIRT%",
				"&fSilkT% of &#c6a664Grasblock/Grasblock(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,1,BREAKING,HAND,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fMining of &#c6a664Grasblock(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,1,BREAKING,HAND,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,1,BREAKING,HAND,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,1,BREAKING,HAND,GRASS_BLOCK% Dollar",
						  
				"&f% of &#c6a664Sand/Sand(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,1,BREAKING,HAND,SAND,mat=SAND%",
				"&fSilkT% of &#c6a664Sand/Sand(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,1,BREAKING,HAND,SAND,mat=SAND%",
				"&fMining of &#c6a664Sand(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,1,BREAKING,HAND,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,1,BREAKING,HAND,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,1,BREAKING,HAND,SAND% Dollar",
						  
				"&f% of &#c6a664Kies/Kies(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,1,BREAKING,HAND,GRAVEL,mat=GRAVEL%",
				"&fSilkT% of &#c6a664Kies/Kies(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,1,BREAKING,HAND,GRAVEL,mat=GRAVEL%",
				"&f% of &#c6a664Kies/Feuerstein(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,1,BREAKING,HAND,GRAVEL,mat=FLINT%",
				"&fMining of &#c6a664Kies(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,1,BREAKING,HAND,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,1,BREAKING,HAND,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,1,BREAKING,HAND,GRAVEL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664&#c6a664Erde/Erde(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,DIRT,mat=DIRT%",
				"&fBehuts. % von &#c6a664&#c6a664Erde/Erde(Holzschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,DIRT,mat=DIRT%",
				"&fAbbauen von &#c6a664&#c6a664Erde(Holzschaufel) &#546f42&#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,DIRT% Dollar",
						  
				"&f% von &#c6a664&#c6a664Grasblock/Erde(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRASS_BLOCK,mat=DIRT%",
				"&fBehuts. % von &#c6a664Grasblock/Grasblock(Holzschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fAbbauen von &#c6a664Grasblock(Holzschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRASS_BLOCK% Dollar",
						  
				"&f% von &#c6a664Sand/Sand(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,SAND,mat=SAND%",
				"&fBehuts. % von &#c6a664Sand/Sand(Holzschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,SAND,mat=SAND%",
				"&fAbbauen von &#c6a664Sand(Holzschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,SAND% Dollar",
						  
				"&f% von &#c6a664Kies/Kies(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&fBehuts. % von &#c6a664Kies/Kies(Holzschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&f% von &#c6a664Kies/Feuerstein(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRAVEL,mat=FLINT%",
				"&fAbbauen von &#c6a664Kies(Holzschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRAVEL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Dirt/Dirt(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,DIRT,mat=DIRT%",
				"&fSilkT% of &#c6a664Dirt/Dirt(Woodenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,DIRT,mat=DIRT%",
				"&fMining of &#c6a664Dirt(Woodenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,DIRT% Dollar",
						  
				"&f% of &#c6a664Grasblock/Dirt(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRASS_BLOCK,mat=DIRT%",
				"&fSilkT% of &#c6a664Grasblock/Grasblock(Woodenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fMining of &#c6a664Grasblock(Woodenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRASS_BLOCK% Dollar",
						  
				"&f% of &#c6a664Sand/Sand(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,SAND,mat=SAND%",
				"&fSilkT% of &#c6a664Sand/Sand(Woodenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,SAND,mat=SAND%",
				"&fMining of &#c6a664Sand(Woodenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,SAND% Dollar",
						  
				"&f% of &#c6a664Kies/Kies(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&fSilkT% of &#c6a664Kies/Kies(Woodenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&f% of &#c6a664Kies/Feuerstein(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRAVEL,mat=FLINT%",
				"&fMining of &#c6a664Kies(Woodenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,2,BREAKING,WOODEN_SHOVEL,GRAVEL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Erde/Erde(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,DIRT,mat=DIRT%",
				"&fBehuts. % von &#c6a664Erde/Erde(Steinschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,DIRT,mat=DIRT%",
				"&fAbbauen von &#c6a664Erde(Steinschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,DIRT% Dollar",
						  
				"&f% von &#c6a664Grasblock/Erde(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRASS_BLOCK,mat=DIRT%",
				"&fBehuts. % von &#c6a664Grasblock/Grasblock(Steinschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fAbbauen von &#c6a664Grasblock(Steinschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRASS_BLOCK% Dollar",
						  
				"&f% von &#c6a664Sand/Sand(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,SAND,mat=SAND%",
				"&fBehuts. % von &#c6a664Sand/Sand(Steinschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,SAND,mat=SAND%",
				"&fAbbauen von &#c6a664Sand(Steinschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,SAND% Dollar",
						  
				"&f% von &#c6a664Kies/Kies(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&fBehuts. % von &#c6a664Kies/Kies(Steinschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&f% von &#c6a664Kies/Feuerstein(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRAVEL,mat=FLINT%",
				"&fAbbauen von &#c6a664Kies(Steinschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRAVEL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Dirt/Dirt(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,DIRT,mat=DIRT%",
				"&fSilkT% of &#c6a664Dirt/Dirt(Stoneshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,DIRT,mat=DIRT%",
				"&fMining of &#c6a664Dirt(Stoneshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,DIRT% Dollar",
						  
				"&f% of &#c6a664Grasblock/Dirt(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRASS_BLOCK,mat=DIRT%",
				"&fSilkT% of &#c6a664Grasblock/Grasblock(Stoneshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fMining of &#c6a664Grasblock(Stoneshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRASS_BLOCK% Dollar",
						  
				"&f% of &#c6a664Sand/Sand(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,SAND,mat=SAND%",
				"&fSilkT% of &#c6a664Sand/Sand(Stoneshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,SAND,mat=SAND%",
				"&fMining of &#c6a664Sand(Stoneshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,SAND% Dollar",
						  
				"&f% of &#c6a664Kies/Kies(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&fSilkT% of &#c6a664Kies/Kies(Stoneshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&f% of &#c6a664Kies/Feuerstein(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRAVEL,mat=FLINT%",
				"&fMining of &#c6a664Kies(Stoneshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,3,BREAKING,STONE_SHOVEL,GRAVEL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Erde/Erde(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,DIRT,mat=DIRT%",
				"&fBehuts. % von &#c6a664Erde/Erde(Eisenschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,DIRT,mat=DIRT%",
				"&fAbbauen von &#c6a664Erde(Eisenschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,DIRT% Dollar",
						  
				"&f% von &#c6a664Grasblock/Erde(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRASS_BLOCK,mat=DIRT%",
				"&fBehuts. % von &#c6a664Grasblock/Grasblock(Eisenschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fAbbauen von &#c6a664Grasblock(Eisenschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRASS_BLOCK% Dollar",
						  
				"&f% von &#c6a664Sand/Sand(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,SAND,mat=SAND%",
				"&fBehuts. % von &#c6a664Sand/Sand(Eisenschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,SAND,mat=SAND%",
				"&fAbbauen von &#c6a664Sand(Eisenschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,SAND% Dollar",
						  
				"&f% von &#c6a664Kies/Kies(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&fBehuts. % von &#c6a664Kies/Kies(Eisenschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&f% von &#c6a664Kies/Feuerstein(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRAVEL,mat=FLINT%",
				"&fAbbauen von &#c6a664Kies(Eisenschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRAVEL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Dirt/Dirt(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,DIRT,mat=DIRT%",
				"&fSilkT% of &#c6a664Dirt/Dirt(Ironshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,DIRT,mat=DIRT%",
				"&fMining of &#c6a664Dirt(Ironshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,DIRT% Dollar",
						  
				"&f% of &#c6a664Grasblock/Dirt(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRASS_BLOCK,mat=DIRT%",
				"&fSilkT% of &#c6a664Grasblock/Grasblock(Ironshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fMining of &#c6a664Grasblock(Ironshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRASS_BLOCK% Dollar",
						  
				"&f% of &#c6a664Sand/Sand(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,SAND,mat=SAND%",
				"&fSilkT% of &#c6a664Sand/Sand(Ironshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,SAND,mat=SAND%",
				"&fMining of &#c6a664Sand(Ironshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,SAND% Dollar",
						  
				"&f% of &#c6a664Kies/Kies(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&fSilkT% of &#c6a664Kies/Kies(Ironshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&f% of &#c6a664Kies/Feuerstein(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRAVEL,mat=FLINT%",
				"&fMining of &#c6a664Kies(Ironshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,4,BREAKING,IRON_SHOVEL,GRAVEL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Erde/Erde(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,DIRT,mat=DIRT%",
				"&fBehuts. % von &#c6a664Erde/Erde(Goldschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,DIRT,mat=DIRT%",
				"&fAbbauen von &#c6a664Erde(Goldschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,DIRT% Dollar",
						  
				"&f% von &#c6a664Grasblock/Erde(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRASS_BLOCK,mat=DIRT%",
				"&fBehuts. % von &#c6a664Grasblock/Grasblock(Goldschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fAbbauen von &#c6a664Grasblock(Goldschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRASS_BLOCK% Dollar",
						  
				"&f% von &#c6a664Sand/Sand(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,SAND,mat=SAND%",
				"&fBehuts. % von &#c6a664Sand/Sand(Goldschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,SAND,mat=SAND%",
				"&fAbbauen von &#c6a664Sand(Goldschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,SAND% Dollar",
						  
				"&f% von &#c6a664Kies/Kies(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&fBehuts. % von &#c6a664Kies/Kies(Goldschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&f% von &#c6a664Kies/Feuerstein(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRAVEL,mat=FLINT%",
				"&fAbbauen von &#c6a664Kies(Goldschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRAVEL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Dirt/Dirt(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,DIRT,mat=DIRT%",
				"&fSilkT% of &#c6a664Dirt/Dirt(Goldenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,DIRT,mat=DIRT%",
				"&fMining of &#c6a664Dirt(Goldenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,DIRT% Dollar",
						  
				"&f% of &#c6a664Grasblock/Dirt(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRASS_BLOCK,mat=DIRT%",
				"&fSilkT% of &#c6a664Grasblock/Grasblock(Goldenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fMining of &#c6a664Grasblock(Goldenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRASS_BLOCK% Dollar",
						  
				"&f% of &#c6a664Sand/Sand(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,SAND,mat=SAND%",
				"&fSilkT% of &#c6a664Sand/Sand(Goldenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,SAND,mat=SAND%",
				"&fMining of &#c6a664Sand(Goldenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,SAND% Dollar",
						  
				"&f% of &#c6a664Kies/Kies(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&fSilkT% of &#c6a664Kies/Kies(Goldenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&f% of &#c6a664Kies/Feuerstein(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRAVEL,mat=FLINT%",
				"&fMining of &#c6a664Kies(Goldenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,5,BREAKING,GOLDEN_SHOVEL,GRAVEL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Erde/Erde(Diamandschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,DIRT,mat=DIRT%",
				"&fBehuts. % von &#c6a664Erde/Erde(Diamandschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,DIRT,mat=DIRT%",
				"&fAbbauen von &#c6a664Erde(Diamandschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,DIRT% Dollar",
						  
				"&f% von &#c6a664Grasblock/Erde(Diamandschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRASS_BLOCK,mat=DIRT%",
				"&fBehuts. % von &#c6a664Grasblock/Grasblock(Diamandschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fAbbauen von &#c6a664Grasblock(Diamandschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRASS_BLOCK% Dollar",
						  
				"&f% von &#c6a664Sand/Sand(Diamandschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,SAND,mat=SAND%",
				"&fBehuts. % von &#c6a664Sand/Sand(Diamandschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,SAND,mat=SAND%",
				"&fAbbauen von &#c6a664Sand(Diamandschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,SAND% Dollar",
						  
				"&f% von &#c6a664Kies/Kies(Diamandschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&fBehuts. % von &#c6a664Kies/Kies(Diamandschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&f% von &#c6a664Kies/Feuerstein(Diamandschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRAVEL,mat=FLINT%",
				"&fAbbauen von &#c6a664Kies(Diamandschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRAVEL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Dirt/Dirt(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,DIRT,mat=DIRT%",
				"&fSilkT% of &#c6a664Dirt/Dirt(Diamondshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,DIRT,mat=DIRT%",
				"&fMining of &#c6a664Dirt(Diamondshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,DIRT% Dollar",
						  
				"&f% of &#c6a664Grasblock/Dirt(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRASS_BLOCK,mat=DIRT%",
				"&fSilkT% of &#c6a664Grasblock/Grasblock(Diamondshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fMining of &#c6a664Grasblock(Diamondshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRASS_BLOCK% Dollar",
						  
				"&f% of &#c6a664Sand/Sand(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,SAND,mat=SAND%",
				"&fSilkT% of &#c6a664Sand/Sand(Diamondshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,SAND,mat=SAND%",
				"&fMining of &#c6a664Sand(Diamondshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,SAND% Dollar",
						  
				"&f% of &#c6a664Kies/Kies(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&fSilkT% of &#c6a664Kies/Kies(Diamondshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&f% of &#c6a664Kies/Feuerstein(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRAVEL,mat=FLINT%",
				"&fMining of &#c6a664Kies(Diamondshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,6,BREAKING,DIAMOND_SHOVEL,GRAVEL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(7, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Erde/Erde(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,DIRT,mat=DIRT%",
				"&fBehuts. % von &#c6a664Erde/Erde(Netheriteschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,DIRT,mat=DIRT%",
				"&fAbbauen von &#c6a664Erde(Netheriteschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,DIRT% Dollar",
						  
				"&f% von &#c6a664Grasblock/Erde(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK,mat=DIRT%",
				"&fBehuts. % von &#c6a664Grasblock/Grasblock(Netheriteschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fAbbauen von &#c6a664Grasblock(Netheriteschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK% Dollar",
						  
				"&f% von &#c6a664Sand/Sand(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,SAND,mat=SAND%",
				"&fBehuts. % von &#c6a664Sand/Sand(Netheriteschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,SAND,mat=SAND%",
				"&fAbbauen von &#c6a664Sand(Netheriteschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,SAND% Dollar",
						  
				"&f% von &#c6a664Kies/Kies(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&fBehuts. % von &#c6a664Kies/Kies(Netheriteschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&f% von &#c6a664Kies/Feuerstein(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRAVEL,mat=FLINT%",
				"&fAbbauen von &#c6a664Kies(Netheriteschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRAVEL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Dirt/Dirt(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,DIRT,mat=DIRT%",
				"&fSilkT% of &#c6a664Dirt/Dirt(Netheriteshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,DIRT,mat=DIRT%",
				"&fMining of &#c6a664Dirt(Netheriteshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,DIRT% Dollar",
						  
				"&f% of &#c6a664Grasblock/Dirt(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK,mat=DIRT%",
				"&fSilkT% of &#c6a664Grasblock/Grasblock(Netheriteshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK,mat=GRASS_BLOCK%",
				"&fMining of &#c6a664Grasblock(Netheriteshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK% Dollar",
						  
				"&f% of &#c6a664Sand/Sand(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,SAND,mat=SAND%",
				"&fSilkT% of &#c6a664Sand/Sand(Netheriteshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,SAND,mat=SAND%",
				"&fMining of &#c6a664Sand(Netheriteshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,SAND% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,SAND% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,SAND% Dollar",
						  
				"&f% of &#c6a664Kies/Kies(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&fSilkT% of &#c6a664Kies/Kies(Netheriteshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRAVEL,mat=GRAVEL%",
				"&f% of &#c6a664Kies/Feuerstein(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRAVEL,mat=FLINT%",
				"&fMining of &#c6a664Kies(Netheriteshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRAVEL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRAVEL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_I,7,BREAKING,NETHERITE_SHOVEL,GRAVEL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"soil_I", new String[] {"Böden_I", "Dirt"}, TechnologyType.MULTIPLE, 7, PlayerAssociatedType.SOLO, 0, "", "soil", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Böden I","&8Tech Soil I"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Erde, Grasblock, Sand & Kies.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Dirt, Grassblock, Sand & Gravel.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Böden I","&7Soil I"},
				Material.DIRT, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dBöden I","&dDirt"},
				Material.DIRT, 1, itemflag, null, canResLore,
				new String[] {"&5Böden I","&5Soil I"},
				Material.DIRT, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbauen von &#c6a664Erde &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,DIRT% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,DIRT% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,DIRT% Dollar",
						"&fAbbauen von &#c6a664Grasblock &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK% Dollar",
						"&fAbbauen von &#c6a664Sand &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,SAND% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,SAND% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,SAND% Dollar",
						"&fAbbauen von &#c6a664Kies &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,GRAVEL% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,GRAVEL% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,GRAVEL% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Dirt &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,DIRT% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,DIRT% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,DIRT% Dollar",
						"&fMining of &#c6a664Grasblock &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,GRASS_BLOCK% Dollar",
						"&fMining of &#c6a664Sand &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,SAND% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,SAND% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,SAND% Dollar",
						"&fMining of &#c6a664Kies &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,GRAVEL% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,GRAVEL% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_I,BREAKING,NETHERITE_SHOVEL,GRAVEL% Dollar",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
						"if:(a):o_1", "else:o_2",
						"output:o_1:true",
						"a:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(7, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(7, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(7, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(2, new String[] {
				"WOODEN_SHOVEL;8",
				"STICK;8"});
		toResCostMaterial.put(2, new String[] {
				"STONE_SHOVEL;8",
				"STICK;8"});
		toResCostMaterial.put(3, new String[] {
				"IRON_SHOVEL;4",
				"STICK;8"});
		toResCostMaterial.put(4, new String[] {
				"GOLDEN_SHOVEL;4",
				"STICK;8"});
		toResCostMaterial.put(5, new String[] {
				"DIAMOND_SHOVEL;3",
				"STICK;8"});
		toResCostMaterial.put(6, new String[] {
				"NETHERITE_SHOVEL;2",
				"STICK;8"});
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"BREAKING:COARSE_DIRT:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ROOTED_DIRT:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DIRT_PATH:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:PODZOL:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"BREAKING:COARSE_DIRT:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ROOTED_DIRT:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DIRT_PATH:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:PODZOL:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"BREAKING:COARSE_DIRT:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ROOTED_DIRT:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DIRT_PATH:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:PODZOL:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"BREAKING:COARSE_DIRT:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ROOTED_DIRT:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DIRT_PATH:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:PODZOL:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"BREAKING:COARSE_DIRT:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ROOTED_DIRT:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DIRT_PATH:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:PODZOL:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"BREAKING:COARSE_DIRT:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ROOTED_DIRT:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DIRT_PATH:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:PODZOL:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(7, new String[] {
				"BREAKING:COARSE_DIRT:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ROOTED_DIRT:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DIRT_PATH:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:PODZOL:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:HAND:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:HAND:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:HAND:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:HAND:PODZOL:null:mat=DIRT:1:1.0"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:WOODEN_SHOVEL:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:WOODEN_SHOVEL:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:WOODEN_SHOVEL:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:WOODEN_SHOVEL:PODZOL:null:mat=DIRT:1:1.0"});
		rewardDropChance.put(3, new String[] {
				"BREAKING:STONE_SHOVEL:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:STONE_SHOVEL:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:STONE_SHOVEL:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:STONE_SHOVEL:PODZOL:null:mat=DIRT:1:1.0"});
		rewardDropChance.put(4, new String[] {
				"BREAKING:IRON_SHOVEL:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:IRON_SHOVEL:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:IRON_SHOVEL:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:IRON_SHOVEL:PODZOL:null:mat=DIRT:1:1.0"});
		rewardDropChance.put(5, new String[] {
				"BREAKING:GOLDEN_SHOVEL:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:PODZOL:null:mat=DIRT:1:1.0"});
		rewardDropChance.put(6, new String[] {
				"BREAKING:DIAMOND_SHOVEL:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:PODZOL:null:mat=DIRT:1:1.0"});
		rewardDropChance.put(7, new String[] {
				"BREAKING:NETHERITE_SHOVEL:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:PODZOL:null:mat=DIRT:1:1.0"});
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:HAND:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:HAND:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:HAND:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:HAND:PODZOL:null:mat=PODZOL:1:1.0"});
		rewardSilkTouchDropChance.put(2, new String[] {
				"BREAKING:WOODEN_SHOVEL:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:WOODEN_SHOVEL:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:WOODEN_SHOVEL:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:WOODEN_SHOVEL:PODZOL:null:mat=PODZOL:1:1.0"});
		rewardSilkTouchDropChance.put(3, new String[] {
				"BREAKING:STONE_SHOVEL:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:STONE_SHOVEL:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:STONE_SHOVEL:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:STONE_SHOVEL:PODZOL:null:mat=PODZOL:1:1.0"});
		rewardSilkTouchDropChance.put(4, new String[] {
				"BREAKING:IRON_SHOVEL:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:IRON_SHOVEL:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:IRON_SHOVEL:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:IRON_SHOVEL:PODZOL:null:mat=PODZOL:1:1.0"});
		rewardSilkTouchDropChance.put(5, new String[] {
				"BREAKING:GOLDEN_SHOVEL:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:PODZOL:null:mat=PODZOL:1:1.0"});
		rewardSilkTouchDropChance.put(6, new String[] {
				"BREAKING:DIAMOND_SHOVEL:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:PODZOL:null:mat=PODZOL:1:1.0"});
		rewardSilkTouchDropChance.put(7, new String[] {
				"BREAKING:NETHERITE_SHOVEL:COARSE_DIRT:null:mat=COARSE_DIRT:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:ROOTED_DIRT:null:mat=ROOTED_DIRT:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:DIRT_PATH:null:mat=DIRT:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:PODZOL:null:mat=PODZOL:1:1.0"});
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Grobe Erde/Grobe Erde(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,1,BREAKING,HAND,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fBehuts. % von &#c6a664Grobe Erde/Grobe Erde(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,1,BREAKING,HAND,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fAbbauen von &#c6a664Grobe Erde(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,1,BREAKING,HAND,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,1,BREAKING,HAND,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,1,BREAKING,HAND,COARSE_DIRT% Dollar",
						  
				"&f% von &#c6a664Wurzelerde/Wurzelerde(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,1,BREAKING,HAND,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fBehuts. % von &#c6a664Wurzelerde/Wurzelerde(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,1,BREAKING,HAND,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fAbbauen von &#c6a664Wurzelerde(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,1,BREAKING,HAND,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,1,BREAKING,HAND,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,1,BREAKING,HAND,ROOTED_DIRT% Dollar",
						  
				"&f% von &#c6a664Trampelpfad/Erde(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,1,BREAKING,HAND,DIRT_PATH,mat=DIRT%",
				"&fBehuts. % von &#c6a664Trampelpfad/Erde(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,1,BREAKING,HAND,DIRT_PATH,mat=DIRT%",
				"&fAbbauen von &#c6a664Trampelpfad(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,1,BREAKING,HAND,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,1,BREAKING,HAND,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,1,BREAKING,HAND,DIRT_PATH% Dollar",
						  
				"&f% von &#c6a664Podsol/Erde(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,1,BREAKING,HAND,PODZOL,mat=DIRT%",
				"&fBehuts. % von &#c6a664Podsol/Erde(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,1,BREAKING,HAND,PODZOL,mat=PODZOL%",
				"&fAbbauen von &#c6a664Podsol(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,1,BREAKING,HAND,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,1,BREAKING,HAND,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,1,BREAKING,HAND,PODZOL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Coarsedirt/Coarsedirt(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,1,BREAKING,HAND,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fSilkT% of &#c6a664Coarsedirt/Coarsedirt(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,1,BREAKING,HAND,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fMining of &#c6a664Coarsedirt(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,1,BREAKING,HAND,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,1,BREAKING,HAND,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,1,BREAKING,HAND,COARSE_DIRT% Dollar",
						  
				"&f% of &#c6a664Rooteddirt/Rooteddirt(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,1,BREAKING,HAND,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fSilkT% of &#c6a664Rooteddirt/Rooteddirt(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,1,BREAKING,HAND,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fMining of &#c6a664Rooteddirt(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,1,BREAKING,HAND,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,1,BREAKING,HAND,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,1,BREAKING,HAND,ROOTED_DIRT% Dollar",
						  
				"&f% of &#c6a664Dirtpath/Dirt(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,1,BREAKING,HAND,DIRT_PATH,mat=DIRT%",
				"&fSilkT% of &#c6a664Sand/Sand(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,1,BREAKING,HAND,DIRT_PATH,mat=DIRT_PATH%",
				"&fMining of &#c6a664Dirtpath(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,1,BREAKING,HAND,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,1,BREAKING,HAND,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,1,BREAKING,HAND,DIRT_PATH% Dollar",
						  
				"&f% of &#c6a664Podzol/Podzol(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,1,BREAKING,HAND,PODZOL,mat=PODZOL%",
				"&fSilkT% of &#c6a664Podzol/Podzol(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,1,BREAKING,HAND,PODZOL,mat=DIRT%",
				"&fMining of &#c6a664Kies(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,1,BREAKING,HAND,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,1,BREAKING,HAND,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,1,BREAKING,HAND,PODZOL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Grobe Erde/Grobe Erde(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fBehuts. % von &#c6a664Grobe Erde/Grobe Erde(Holzschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fAbbauen von &#c6a664Grobe Erde(Holzschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,COARSE_DIRT% Dollar",
						  
				"&f% von &#c6a664Wurzelerde/Wurzelerde(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fBehuts. % von &#c6a664Wurzelerde/Wurzelerde(Holzschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fAbbauen von &#c6a664Wurzelerde(Holzschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,ROOTED_DIRT% Dollar",
						  
				"&f% von &#c6a664Trampelpfad/Erde(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fBehuts. % von &#c6a664Trampelpfad/Erde(Holzschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fAbbauen von &#c6a664Trampelpfad(Holzschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,DIRT_PATH% Dollar",
						  
				"&f% von &#c6a664Podsol/Erde(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,PODZOL,mat=DIRT%",
				"&fBehuts. % von &#c6a664Podsol/Erde(Holzschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,PODZOL,mat=PODZOL%",
				"&fAbbauen von &#c6a664Podsol(Holzschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,PODZOL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Coarsedirt/Coarsedirt(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fSilkT% of &#c6a664Coarsedirt/Coarsedirt(Woodenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fMining of &#c6a664Coarsedirt(Woodenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,COARSE_DIRT% Dollar",
						  
				"&f% of &#c6a664Rooteddirt/Rooteddirt(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fSilkT% of &#c6a664Rooteddirt/Rooteddirt(Woodenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fMining of &#c6a664Rooteddirt(Woodenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,ROOTED_DIRT% Dollar",
						  
				"&f% of &#c6a664Dirtpath/Dirt(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fSilkT% of &#c6a664Sand/Sand(Woodenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,DIRT_PATH,mat=DIRT_PATH%",
				"&fMining of &#c6a664Dirtpath(Woodenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,DIRT_PATH% Dollar",
						  
				"&f% of &#c6a664Podzol/Podzol(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,PODZOL,mat=PODZOL%",
				"&fSilkT% of &#c6a664Podzol/Podzol(Woodenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,PODZOL,mat=DIRT%",
				"&fMining of &#c6a664Kies(Woodenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,2,BREAKING,WOODEN_SHOVEL,PODZOL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Grobe Erde/Grobe Erde(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fBehuts. % von &#c6a664Grobe Erde/Grobe Erde(Steinschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fAbbauen von &#c6a664Grobe Erde(Steinschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,COARSE_DIRT% Dollar",
						  
				"&f% von &#c6a664Wurzelerde/Wurzelerde(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fBehuts. % von &#c6a664Wurzelerde/Wurzelerde(Steinschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fAbbauen von &#c6a664Wurzelerde(Steinschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,ROOTED_DIRT% Dollar",
						  
				"&f% von &#c6a664Trampelpfad/Erde(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fBehuts. % von &#c6a664Trampelpfad/Erde(Steinschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fAbbauen von &#c6a664Trampelpfad(Steinschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,DIRT_PATH% Dollar",
						  
				"&f% von &#c6a664Podsol/Erde(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,PODZOL,mat=DIRT%",
				"&fBehuts. % von &#c6a664Podsol/Erde(Steinschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,PODZOL,mat=PODZOL%",
				"&fAbbauen von &#c6a664Podsol(Steinschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,PODZOL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Coarsedirt/Coarsedirt(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fSilkT% of &#c6a664Coarsedirt/Coarsedirt(Stoneshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fMining of &#c6a664Coarsedirt(Stoneshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,COARSE_DIRT% Dollar",
						  
				"&f% of &#c6a664Rooteddirt/Rooteddirt(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fSilkT% of &#c6a664Rooteddirt/Rooteddirt(Stoneshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fMining of &#c6a664Rooteddirt(Stoneshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,ROOTED_DIRT% Dollar",
						  
				"&f% of &#c6a664Dirtpath/Dirt(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fSilkT% of &#c6a664Sand/Sand(Stoneshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,DIRT_PATH,mat=DIRT_PATH%",
				"&fMining of &#c6a664Dirtpath(Stoneshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,DIRT_PATH% Dollar",
						  
				"&f% of &#c6a664Podzol/Podzol(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,PODZOL,mat=PODZOL%",
				"&fSilkT% of &#c6a664Podzol/Podzol(Stoneshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,PODZOL,mat=DIRT%",
				"&fMining of &#c6a664Kies(Stoneshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,3,BREAKING,STONE_SHOVEL,PODZOL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Grobe Erde/Grobe Erde(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fBehuts. % von &#c6a664Grobe Erde/Grobe Erde(Eisenschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fAbbauen von &#c6a664Grobe Erde(Eisenschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,COARSE_DIRT% Dollar",
						  
				"&f% von &#c6a664Wurzelerde/Wurzelerde(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fBehuts. % von &#c6a664Wurzelerde/Wurzelerde(Eisenschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fAbbauen von &#c6a664Wurzelerde(Eisenschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,ROOTED_DIRT% Dollar",
						  
				"&f% von &#c6a664Trampelpfad/Erde(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fBehuts. % von &#c6a664Trampelpfad/Erde(Eisenschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fAbbauen von &#c6a664Trampelpfad(Eisenschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,DIRT_PATH% Dollar",
						  
				"&f% von &#c6a664Podsol/Erde(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,PODZOL,mat=DIRT%",
				"&fBehuts. % von &#c6a664Podsol/Erde(Eisenschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,PODZOL,mat=PODZOL%",
				"&fAbbauen von &#c6a664Podsol(Eisenschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,PODZOL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Coarsedirt/Coarsedirt(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fSilkT% of &#c6a664Coarsedirt/Coarsedirt(Ironshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fMining of &#c6a664Coarsedirt(Ironshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,COARSE_DIRT% Dollar",
						  
				"&f% of &#c6a664Rooteddirt/Rooteddirt(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fSilkT% of &#c6a664Rooteddirt/Rooteddirt(Ironshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fMining of &#c6a664Rooteddirt(Ironshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,ROOTED_DIRT% Dollar",
						  
				"&f% of &#c6a664Dirtpath/Dirt(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fSilkT% of &#c6a664Sand/Sand(Ironshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,DIRT_PATH,mat=DIRT_PATH%",
				"&fMining of &#c6a664Dirtpath(Ironshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,DIRT_PATH% Dollar",
						  
				"&f% of &#c6a664Podzol/Podzol(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,PODZOL,mat=PODZOL%",
				"&fSilkT% of &#c6a664Podzol/Podzol(Ironshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,PODZOL,mat=DIRT%",
				"&fMining of &#c6a664Kies(Ironshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,4,BREAKING,IRON_SHOVEL,PODZOL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Grobe Erde/Grobe Erde(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fBehuts. % von &#c6a664Grobe Erde/Grobe Erde(Goldschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fAbbauen von &#c6a664Grobe Erde(Goldschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,COARSE_DIRT% Dollar",
						  
				"&f% von &#c6a664Wurzelerde/Wurzelerde(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fBehuts. % von &#c6a664Wurzelerde/Wurzelerde(Goldschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fAbbauen von &#c6a664Wurzelerde(Goldschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,ROOTED_DIRT% Dollar",
						  
				"&f% von &#c6a664Trampelpfad/Erde(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fBehuts. % von &#c6a664Trampelpfad/Erde(Goldschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fAbbauen von &#c6a664Trampelpfad(Goldschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,DIRT_PATH% Dollar",
						  
				"&f% von &#c6a664Podsol/Erde(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,PODZOL,mat=DIRT%",
				"&fBehuts. % von &#c6a664Podsol/Erde(Goldschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,PODZOL,mat=PODZOL%",
				"&fAbbauen von &#c6a664Podsol(Goldschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,PODZOL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Coarsedirt/Coarsedirt(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fSilkT% of &#c6a664Coarsedirt/Coarsedirt(Goldenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fMining of &#c6a664Coarsedirt(Goldenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,COARSE_DIRT% Dollar",
						  
				"&f% of &#c6a664Rooteddirt/Rooteddirt(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fSilkT% of &#c6a664Rooteddirt/Rooteddirt(Goldenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fMining of &#c6a664Rooteddirt(Goldenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,ROOTED_DIRT% Dollar",
						  
				"&f% of &#c6a664Dirtpath/Dirt(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fSilkT% of &#c6a664Sand/Sand(Goldenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,DIRT_PATH,mat=DIRT_PATH%",
				"&fMining of &#c6a664Dirtpath(Goldenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,DIRT_PATH% Dollar",
						  
				"&f% of &#c6a664Podzol/Podzol(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,PODZOL,mat=PODZOL%",
				"&fSilkT% of &#c6a664Podzol/Podzol(Goldenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,PODZOL,mat=DIRT%",
				"&fMining of &#c6a664Kies(Goldenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,5,BREAKING,GOLDEN_SHOVEL,PODZOL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Grobe Erde/Grobe Erde(Diamandschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fBehuts. % von &#c6a664Grobe Erde/Grobe Erde(Diamandschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fAbbauen von &#c6a664Grobe Erde(Diamandschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,COARSE_DIRT% Dollar",
						  
				"&f% von &#c6a664Wurzelerde/Wurzelerde(Diamandschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fBehuts. % von &#c6a664Wurzelerde/Wurzelerde(Diamandschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fAbbauen von &#c6a664Wurzelerde(Diamandschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,ROOTED_DIRT% Dollar",
						  
				"&f% von &#c6a664Trampelpfad/Erde(Diamandschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fBehuts. % von &#c6a664Trampelpfad/Erde(Diamandschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fAbbauen von &#c6a664Trampelpfad(Diamandschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,DIRT_PATH% Dollar",
						  
				"&f% von &#c6a664Podsol/Erde(Diamandschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,PODZOL,mat=DIRT%",
				"&fBehuts. % von &#c6a664Podsol/Erde(Diamandschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,PODZOL,mat=PODZOL%",
				"&fAbbauen von &#c6a664Podsol(Diamandschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,PODZOL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Coarsedirt/Coarsedirt(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fSilkT% of &#c6a664Coarsedirt/Coarsedirt(Diamondshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fMining of &#c6a664Coarsedirt(Diamondshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,COARSE_DIRT% Dollar",
						  
				"&f% of &#c6a664Rooteddirt/Rooteddirt(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fSilkT% of &#c6a664Rooteddirt/Rooteddirt(Diamondshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fMining of &#c6a664Rooteddirt(Diamondshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,ROOTED_DIRT% Dollar",
						  
				"&f% of &#c6a664Dirtpath/Dirt(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fSilkT% of &#c6a664Sand/Sand(Diamondshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,DIRT_PATH,mat=DIRT_PATH%",
				"&fMining of &#c6a664Dirtpath(Diamondshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,DIRT_PATH% Dollar",
						  
				"&f% of &#c6a664Podzol/Podzol(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,PODZOL,mat=PODZOL%",
				"&fSilkT% of &#c6a664Podzol/Podzol(Diamondshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,PODZOL,mat=DIRT%",
				"&fMining of &#c6a664Kies(Diamondshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,6,BREAKING,DIAMOND_SHOVEL,PODZOL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(7, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Grobe Erde/Grobe Erde(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fBehuts. % von &#c6a664Grobe Erde/Grobe Erde(Netheriteschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fAbbauen von &#c6a664Grobe Erde(Netheriteschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT% Dollar",
						  
				"&f% von &#c6a664Wurzelerde/Wurzelerde(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fBehuts. % von &#c6a664Wurzelerde/Wurzelerde(Netheriteschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fAbbauen von &#c6a664Wurzelerde(Netheriteschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT% Dollar",
						  
				"&f% von &#c6a664Trampelpfad/Erde(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fBehuts. % von &#c6a664Trampelpfad/Erde(Netheriteschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fAbbauen von &#c6a664Trampelpfad(Netheriteschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,DIRT_PATH% Dollar",
						  
				"&f% von &#c6a664Podsol/Erde(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,PODZOL,mat=DIRT%",
				"&fBehuts. % von &#c6a664Podsol/Erde(Netheriteschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,PODZOL,mat=PODZOL%",
				"&fAbbauen von &#c6a664Podsol(Netheriteschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,PODZOL% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Coarsedirt/Coarsedirt(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fSilkT% of &#c6a664Coarsedirt/Coarsedirt(Netheriteshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT,mat=COARSE_DIRT%",
				"&fMining of &#c6a664Coarsedirt(Netheriteshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT% Dollar",
						  
				"&f% of &#c6a664Rooteddirt/Rooteddirt(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fSilkT% of &#c6a664Rooteddirt/Rooteddirt(Netheriteshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT,mat=ROOTED_DIRT%",
				"&fMining of &#c6a664Rooteddirt(Netheriteshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT% Dollar",
						  
				"&f% of &#c6a664Dirtpath/Dirt(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,DIRT_PATH,mat=DIRT%",
				"&fSilkT% of &#c6a664Sand/Sand(Netheriteshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,DIRT_PATH,mat=DIRT_PATH%",
				"&fMining of &#c6a664Dirtpath(Netheriteshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,DIRT_PATH% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,DIRT_PATH% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,DIRT_PATH% Dollar",
						  
				"&f% of &#c6a664Podzol/Podzol(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,PODZOL,mat=PODZOL%",
				"&fSilkT% of &#c6a664Podzol/Podzol(Netheriteshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,PODZOL,mat=DIRT%",
				"&fMining of &#c6a664Kies(Netheriteshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,PODZOL% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,PODZOL% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_II,7,BREAKING,NETHERITE_SHOVEL,PODZOL% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"soil_II", new String[] {"Böden_II", "Soil_II"}, TechnologyType.MULTIPLE, 7, PlayerAssociatedType.SOLO, 1, "", "soil", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Böden II","&8Tech Soil II"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Grobe Erde, Wurzelerde, Trampelpfad & Podsol.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Coarsedirt, Rooteddirt, Dirtpath & Podzol.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Böden II","&7Soil II"},
				Material.ROOTED_DIRT, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dBöden II","&dSoil II"},
				Material.ROOTED_DIRT, 1, itemflag, null, canResLore,
				new String[] {"&5Böden II","&5Soil II"},
				Material.ROOTED_DIRT, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbauen von &#c6a664Erde &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT% Dollar",
						"&fAbbauen von &#c6a664Grasblock &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT% Dollar",
						"&fAbbauen von &#c6a664Sand &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,DIRT_PATH% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,DIRT_PATH% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,DIRT_PATH% Dollar",
						"&fAbbauen von &#c6a664Kies &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,PODZOL% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,PODZOL% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,PODZOL% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Dirt &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,COARSE_DIRT% Dollar",
						"&fMining of &#c6a664Grasblock &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,ROOTED_DIRT% Dollar",
						"&fMining of &#c6a664Sand &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,DIRT_PATH% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,DIRT_PATH% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,DIRT_PATH% Dollar",
						"&fMining of &#c6a664Kies &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,PODZOL% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,PODZOL% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_II,BREAKING,NETHERITE_SHOVEL,PODZOL% Dollar",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(7, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(7, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(7, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(2, new String[] {
				"WOODEN_SHOVEL;8",
				"STICK;8"});
		toResCostMaterial.put(2, new String[] {
				"STONE_SHOVEL;8",
				"STICK;8"});
		toResCostMaterial.put(3, new String[] {
				"IRON_SHOVEL;4",
				"STICK;8"});
		toResCostMaterial.put(4, new String[] {
				"GOLDEN_SHOVEL;4",
				"STICK;8"});
		toResCostMaterial.put(5, new String[] {
				"DIAMOND_SHOVEL;3",
				"STICK;8"});
		toResCostMaterial.put(6, new String[] {
				"NETHERITE_SHOVEL;2",
				"STICK;8"});
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"BREAKING:CLAY:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MUD:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MYCELIUM:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW_BLOCK:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"BREAKING:CLAY:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MUD:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MYCELIUM:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW_BLOCK:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"BREAKING:CLAY:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MUD:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MYCELIUM:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW_BLOCK:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"BREAKING:CLAY:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MUD:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MYCELIUM:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW_BLOCK:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"BREAKING:CLAY:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MUD:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MYCELIUM:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW_BLOCK:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"BREAKING:CLAY:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MUD:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MYCELIUM:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW_BLOCK:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(7, new String[] {
				"BREAKING:CLAY:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MUD:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MYCELIUM:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW_BLOCK:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:HAND:CLAY:null:mat=CLAY_BALL:4:1.0",
				"BREAKING:HAND:MUD:null:mat=MUD:1:1.0",
				"BREAKING:HAND:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:HAND:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:HAND:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:WOODEN_SHOVEL:CLAY:null:mat=CLAY_BALL:4:1.0",
				"BREAKING:WOODEN_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:WOODEN_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:WOODEN_SHOVEL:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:WOODEN_SHOVEL:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0"});
		rewardDropChance.put(3, new String[] {
				"BREAKING:STONE_SHOVEL:CLAY:null:mat=CLAY_BALL:4:1.0",
				"BREAKING:STONE_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:STONE_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:STONE_SHOVEL:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:STONE_SHOVEL:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0"});
		rewardDropChance.put(4, new String[] {
				"BREAKING:IRON_SHOVEL:CLAY:null:mat=CLAY_BALL:4:1.0",
				"BREAKING:IRON_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:IRON_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:IRON_SHOVEL:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:IRON_SHOVEL:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0"});
		rewardDropChance.put(5, new String[] {
				"BREAKING:GOLDEN_SHOVEL:CLAY:null:mat=CLAY_BALL:4:1.0",
				"BREAKING:GOLDEN_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0"});
		rewardDropChance.put(6, new String[] {
				"BREAKING:DIAMOND_SHOVEL:CLAY:null:mat=CLAY_BALL:4:1.0",
				"BREAKING:DIAMOND_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0"});
		rewardDropChance.put(7, new String[] {
				"BREAKING:NETHERITE_SHOVEL:CLAY:null:mat=CLAY_BALL:4:1.0",
				"BREAKING:NETHERITE_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0"});
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(2, new String[] {
				"BREAKING:WOODEN_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:WOODEN_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:WOODEN_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:WOODEN_SHOVEL:SNOW:null:mat=SNOW:1:1.0",
				"BREAKING:WOODEN_SHOVEL:SNOW_BLOCK:null:mat=SNOW_BLOCK:1:1.0"});
		rewardSilkTouchDropChance.put(3, new String[] {
				"BREAKING:STONE_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:STONE_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:STONE_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:STONE_SHOVEL:SNOW:null:mat=SNOW:1:1.0",
				"BREAKING:STONE_SHOVEL:SNOW_BLOCK:null:mat=SNOW_BLOCK:1:1.0"});
		rewardSilkTouchDropChance.put(4, new String[] {
				"BREAKING:IRON_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:IRON_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:IRON_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:IRON_SHOVEL:SNOW:null:mat=SNOW:1:1.0",
				"BREAKING:IRON_SHOVEL:SNOW_BLOCK:null:mat=SNOW_BLOCK:1:1.0"});
		rewardSilkTouchDropChance.put(5, new String[] {
				"BREAKING:GOLDEN_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:SNOW:null:mat=SNOW:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:SNOW_BLOCK:null:mat=SNOW_BLOCK:1:1.0"});
		rewardSilkTouchDropChance.put(6, new String[] {
				"BREAKING:DIAMOND_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:SNOW:null:mat=SNOW:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:SNOW_BLOCK:null:mat=SNOW_BLOCK:1:1.0"});
		rewardSilkTouchDropChance.put(7, new String[] {
				"BREAKING:NETHERITE_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:SNOW:null:mat=SNOW:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:SNOW_BLOCK:null:mat=SNOW_BLOCK:1:1.0"});
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Ton/Tonklumpen(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,1,BREAKING,HAND,CLAY,mat=CLAY_BALL%",
				"&fBehuts. % von &#c6a664Ton/Ton(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,1,BREAKING,HAND,CLAY,mat=CLAY%",
				"&fAbbauen von &#c6a664Ton(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,1,BREAKING,HAND,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,1,BREAKING,HAND,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,1,BREAKING,HAND,CLAY% Dollar",
						  
				"&f% von &#c6a664Schlamm/Schlamm(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,1,BREAKING,HAND,MUD,mat=MUD%",
				"&fBehuts. % von &#c6a664Schlamm/Schlamm(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,1,BREAKING,HAND,MUD,mat=MUD%",
				"&fAbbauen von &#c6a664Schlamm(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,1,BREAKING,HAND,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,1,BREAKING,HAND,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,1,BREAKING,HAND,MUD% Dollar",	  
						  
				"&f% von &#c6a664Myzel/Myzel(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,1,BREAKING,HAND,MYCELIUM,mat=MYCELIUM%",
				"&fBehuts. % von &#c6a664Myzel/Myzel(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,1,BREAKING,HAND,MYCELIUM,mat=MYCELIUM%",
				"&fAbbauen von &#c6a664Myzel(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,1,BREAKING,HAND,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,1,BREAKING,HAND,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,1,BREAKING,HAND,MYCELIUM% Dollar",
						  
				"&f% von &#c6a664Schnee/Schneeball(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schnee/Schnee(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW,mat=SNOW%",
				"&fAbbauen von &#c6a664Podsol(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW% Dollar",
						  
			    "&f% von &#c6a664Schneeblock/Schneeball(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schneeblock/Schneeblock(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fAbbauen von &#c6a664Schneeblock(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW_BLOCK% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Clay/Clayball(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,1,BREAKING,HAND,CLAY,mat=CLAY_BALL%",
				"&fSilkT% of &#c6a664Clay/Clay(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,1,BREAKING,HAND,CLAY,mat=CLAY%",
				"&fMining of &#c6a664Clay(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,1,BREAKING,HAND,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,1,BREAKING,HAND,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,1,BREAKING,HAND,CLAY% Dollar",
						  
				"&f% of &#c6a664Mud/Mud(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,1,BREAKING,HAND,MUD,mat=MUD%",
				"&fSilkT% of &#c6a664Mud/Mud(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,1,BREAKING,HAND,MUD,mat=MUD%",
				"&fMining of &#c6a664Mud(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,1,BREAKING,HAND,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,1,BREAKING,HAND,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,1,BREAKING,HAND,MUD% Dollar",
						  
				"&f% of &#c6a664Mycelium/Mycelium(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,1,BREAKING,HAND,MYCELIUM,mat=MYCELIUM%",
				"&fSilkT% of &#c6a664Mycelium/Mycelium(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,1,BREAKING,HAND,MYCELIUM,mat=MYCELIUM%",
				"&fMining of &#c6a664Mycelium(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,1,BREAKING,HAND,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,1,BREAKING,HAND,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,1,BREAKING,HAND,MYCELIUM% Dollar",
						  
				"&f% of &#c6a664Snow/Snowball(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snow/Snow(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW% Dollar",
						  
			    "&f% of &#c6a664Snowblock/Snowball(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snowblock/Snowblock(Hand) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,1,BREAKING,HAND,SNOW_BLOCK% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Ton/Tonklumpen(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,CLAY,mat=CLAY_BALL%",
				"&fBehuts. % von &#c6a664Ton/Ton(Holzschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,CLAY,mat=CLAY%",
				"&fAbbauen von &#c6a664Ton(Holzschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,CLAY% Dollar",
						  
				"&f% von &#c6a664Schlamm/Schlamm(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MUD,mat=MUD%",
				"&fBehuts. % von &#c6a664Schlamm/Schlamm(Holzschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MUD,mat=MUD%",
				"&fAbbauen von &#c6a664Schlamm(Holzschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MUD% Dollar",	  
						  
				"&f% von &#c6a664Myzel/Myzel(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fBehuts. % von &#c6a664Myzel/Myzel(Holzschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fAbbauen von &#c6a664Myzel(Holzschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MYCELIUM% Dollar",
						  
				"&f% von &#c6a664Schnee/Schneeball(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schnee/Schnee(Holzschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW,mat=SNOW%",
				"&fAbbauen von &#c6a664Podsol(Holzschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW% Dollar",
						  
			    "&f% von &#c6a664Schneeblock/Schneeball(Holzschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schneeblock/Schneeblock(Holzschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fAbbauen von &#c6a664Schneeblock(Holzschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW_BLOCK% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Clay/Clayball(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,CLAY,mat=CLAY_BALL%",
				"&fSilkT% of &#c6a664Clay/Clay(Woodenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,CLAY,mat=CLAY%",
				"&fMining of &#c6a664Clay(Woodenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,CLAY% Dollar",
						  
				"&f% of &#c6a664Mud/Mud(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MUD,mat=MUD%",
				"&fSilkT% of &#c6a664Mud/Mud(Woodenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MUD,mat=MUD%",
				"&fMining of &#c6a664Mud(Woodenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MUD% Dollar",
						  
				"&f% of &#c6a664Mycelium/Mycelium(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fSilkT% of &#c6a664Mycelium/Mycelium(Woodenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fMining of &#c6a664Mycelium(Woodenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,MYCELIUM% Dollar",
						  
				"&f% of &#c6a664Snow/Snowball(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snow/Snow(Woodenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Woodenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW% Dollar",
						  
			    "&f% of &#c6a664Snowblock/Snowball(Woodenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snowblock/Snowblock(Woodenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Woodenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,2,BREAKING,WOODEN_SHOVEL,SNOW_BLOCK% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Ton/Tonklumpen(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,CLAY,mat=CLAY_BALL%",
				"&fBehuts. % von &#c6a664Ton/Ton(Steinschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,CLAY,mat=CLAY%",
				"&fAbbauen von &#c6a664Ton(Steinschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,CLAY% Dollar",
						  
				"&f% von &#c6a664Schlamm/Schlamm(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MUD,mat=MUD%",
				"&fBehuts. % von &#c6a664Schlamm/Schlamm(Steinschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MUD,mat=MUD%",
				"&fAbbauen von &#c6a664Schlamm(Steinschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MUD% Dollar",	  
						  
				"&f% von &#c6a664Myzel/Myzel(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fBehuts. % von &#c6a664Myzel/Myzel(Steinschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fAbbauen von &#c6a664Myzel(Steinschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MYCELIUM% Dollar",
						  
				"&f% von &#c6a664Schnee/Schneeball(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schnee/Schnee(Steinschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW,mat=SNOW%",
				"&fAbbauen von &#c6a664Podsol(Steinschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW% Dollar",
						  
			    "&f% von &#c6a664Schneeblock/Schneeball(Steinschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schneeblock/Schneeblock(Steinschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fAbbauen von &#c6a664Schneeblock(Steinschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW_BLOCK% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Clay/Clayball(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,CLAY,mat=CLAY_BALL%",
				"&fSilkT% of &#c6a664Clay/Clay(Stoneshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,CLAY,mat=CLAY%",
				"&fMining of &#c6a664Clay(Stoneshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,CLAY% Dollar",
						  
				"&f% of &#c6a664Mud/Mud(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MUD,mat=MUD%",
				"&fSilkT% of &#c6a664Mud/Mud(Stoneshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MUD,mat=MUD%",
				"&fMining of &#c6a664Mud(Stoneshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MUD% Dollar",
						  
				"&f% of &#c6a664Mycelium/Mycelium(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fSilkT% of &#c6a664Mycelium/Mycelium(Stoneshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fMining of &#c6a664Mycelium(Stoneshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,MYCELIUM% Dollar",
						  
				"&f% of &#c6a664Snow/Snowball(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snow/Snow(Stoneshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Stoneshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW% Dollar",
						  
			    "&f% of &#c6a664Snowblock/Snowball(Stoneshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snowblock/Snowblock(Stoneshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Stoneshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,3,BREAKING,STONE_SHOVEL,SNOW_BLOCK% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Ton/Tonklumpen(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,CLAY,mat=CLAY_BALL%",
				"&fBehuts. % von &#c6a664Ton/Ton(Eisenschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,CLAY,mat=CLAY%",
				"&fAbbauen von &#c6a664Ton(Eisenschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,CLAY% Dollar",
						  
				"&f% von &#c6a664Schlamm/Schlamm(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MUD,mat=MUD%",
				"&fBehuts. % von &#c6a664Schlamm/Schlamm(Eisenschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MUD,mat=MUD%",
				"&fAbbauen von &#c6a664Schlamm(Eisenschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MUD% Dollar",	  
						  
				"&f% von &#c6a664Myzel/Myzel(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fBehuts. % von &#c6a664Myzel/Myzel(Eisenschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fAbbauen von &#c6a664Myzel(Eisenschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MYCELIUM% Dollar",
						  
				"&f% von &#c6a664Schnee/Schneeball(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schnee/Schnee(Eisenschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW,mat=SNOW%",
				"&fAbbauen von &#c6a664Podsol(Eisenschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW% Dollar",
						  
			    "&f% von &#c6a664Schneeblock/Schneeball(Eisenschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schneeblock/Schneeblock(Eisenschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fAbbauen von &#c6a664Schneeblock(Eisenschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW_BLOCK% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Clay/Clayball(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,CLAY,mat=CLAY_BALL%",
				"&fSilkT% of &#c6a664Clay/Clay(Ironshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,CLAY,mat=CLAY%",
				"&fMining of &#c6a664Clay(Ironshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,CLAY% Dollar",
						  
				"&f% of &#c6a664Mud/Mud(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MUD,mat=MUD%",
				"&fSilkT% of &#c6a664Mud/Mud(Ironshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MUD,mat=MUD%",
				"&fMining of &#c6a664Mud(Ironshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MUD% Dollar",
						  
				"&f% of &#c6a664Mycelium/Mycelium(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fSilkT% of &#c6a664Mycelium/Mycelium(Ironshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fMining of &#c6a664Mycelium(Ironshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,MYCELIUM% Dollar",
						  
				"&f% of &#c6a664Snow/Snowball(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snow/Snow(Ironshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Ironshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW% Dollar",
						  
			    "&f% of &#c6a664Snowblock/Snowball(Ironshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snowblock/Snowblock(Ironshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Ironshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,4,BREAKING,IRON_SHOVEL,SNOW_BLOCK% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Ton/Tonklumpen(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,CLAY,mat=CLAY_BALL%",
				"&fBehuts. % von &#c6a664Ton/Ton(Goldschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,CLAY,mat=CLAY%",
				"&fAbbauen von &#c6a664Ton(Goldschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,CLAY% Dollar",
						  
				"&f% von &#c6a664Schlamm/Schlamm(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MUD,mat=MUD%",
				"&fBehuts. % von &#c6a664Schlamm/Schlamm(Goldschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MUD,mat=MUD%",
				"&fAbbauen von &#c6a664Schlamm(Goldschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MUD% Dollar",	  
						  
				"&f% von &#c6a664Myzel/Myzel(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fBehuts. % von &#c6a664Myzel/Myzel(Goldschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fAbbauen von &#c6a664Myzel(Goldschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MYCELIUM% Dollar",
						  
				"&f% von &#c6a664Schnee/Schneeball(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schnee/Schnee(Goldschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW,mat=SNOW%",
				"&fAbbauen von &#c6a664Podsol(Goldschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW% Dollar",
						  
			    "&f% von &#c6a664Schneeblock/Schneeball(Goldschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schneeblock/Schneeblock(Goldschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fAbbauen von &#c6a664Schneeblock(Goldschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW_BLOCK% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Clay/Clayball(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,CLAY,mat=CLAY_BALL%",
				"&fSilkT% of &#c6a664Clay/Clay(Goldenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,CLAY,mat=CLAY%",
				"&fMining of &#c6a664Clay(Goldenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,CLAY% Dollar",
						  
				"&f% of &#c6a664Mud/Mud(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MUD,mat=MUD%",
				"&fSilkT% of &#c6a664Mud/Mud(Goldenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MUD,mat=MUD%",
				"&fMining of &#c6a664Mud(Goldenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MUD% Dollar",
						  
				"&f% of &#c6a664Mycelium/Mycelium(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fSilkT% of &#c6a664Mycelium/Mycelium(Goldenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fMining of &#c6a664Mycelium(Goldenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,MYCELIUM% Dollar",
						  
				"&f% of &#c6a664Snow/Snowball(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snow/Snow(Goldenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Goldenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW% Dollar",
						  
			    "&f% of &#c6a664Snowblock/Snowball(Goldenshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snowblock/Snowblock(Goldenshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Goldenshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,5,BREAKING,GOLDEN_SHOVEL,SNOW_BLOCK% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Ton/Tonklumpen(Diamantschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,CLAY,mat=CLAY_BALL%",
				"&fBehuts. % von &#c6a664Ton/Ton(Diamantschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,CLAY,mat=CLAY%",
				"&fAbbauen von &#c6a664Ton(Diamantschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,CLAY% Dollar",
						  
				"&f% von &#c6a664Schlamm/Schlamm(Diamantschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MUD,mat=MUD%",
				"&fBehuts. % von &#c6a664Schlamm/Schlamm(Diamantschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MUD,mat=MUD%",
				"&fAbbauen von &#c6a664Schlamm(Diamantschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MUD% Dollar",	  
						  
				"&f% von &#c6a664Myzel/Myzel(Diamantschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fBehuts. % von &#c6a664Myzel/Myzel(Diamantschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fAbbauen von &#c6a664Myzel(Diamantschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MYCELIUM% Dollar",
						  
				"&f% von &#c6a664Schnee/Schneeball(Diamantschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schnee/Schnee(Diamantschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW,mat=SNOW%",
				"&fAbbauen von &#c6a664Podsol(Diamantschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW% Dollar",
						  
			    "&f% von &#c6a664Schneeblock/Schneeball(Diamantschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schneeblock/Schneeblock(Diamantschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fAbbauen von &#c6a664Schneeblock(Diamantschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW_BLOCK% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Clay/Clayball(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,CLAY,mat=CLAY_BALL%",
				"&fSilkT% of &#c6a664Clay/Clay(Diamondshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,CLAY,mat=CLAY%",
				"&fMining of &#c6a664Clay(Diamondshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,CLAY% Dollar",
						  
				"&f% of &#c6a664Mud/Mud(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MUD,mat=MUD%",
				"&fSilkT% of &#c6a664Mud/Mud(Diamondshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MUD,mat=MUD%",
				"&fMining of &#c6a664Mud(Diamondshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MUD% Dollar",
						  
				"&f% of &#c6a664Mycelium/Mycelium(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fSilkT% of &#c6a664Mycelium/Mycelium(Diamondshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fMining of &#c6a664Mycelium(Diamondshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,MYCELIUM% Dollar",
						  
				"&f% of &#c6a664Snow/Snowball(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snow/Snow(Diamondshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Diamondshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW% Dollar",
						  
			    "&f% of &#c6a664Snowblock/Snowball(Diamondshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snowblock/Snowblock(Diamondshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Diamondshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,6,BREAKING,DIAMOND_SHOVEL,SNOW_BLOCK% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(7, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Ton/Tonklumpen(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,CLAY,mat=CLAY_BALL%",
				"&fBehuts. % von &#c6a664Ton/Ton(Netheriteschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,CLAY,mat=CLAY%",
				"&fAbbauen von &#c6a664Ton(Netheriteschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,CLAY% Dollar",
						  
				"&f% von &#c6a664Schlamm/Schlamm(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MUD,mat=MUD%",
				"&fBehuts. % von &#c6a664Schlamm/Schlamm(Netheriteschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MUD,mat=MUD%",
				"&fAbbauen von &#c6a664Schlamm(Netheriteschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MUD% Dollar",	  
						  
				"&f% von &#c6a664Myzel/Myzel(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fBehuts. % von &#c6a664Myzel/Myzel(Netheriteschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fAbbauen von &#c6a664Myzel(Netheriteschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MYCELIUM% Dollar",
						  
				"&f% von &#c6a664Schnee/Schneeball(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schnee/Schnee(Netheriteschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW,mat=SNOW%",
				"&fAbbauen von &#c6a664Podsol(Netheriteschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW% Dollar",
						  
			    "&f% von &#c6a664Schneeblock/Schneeball(Netheriteschaufel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fBehuts. % von &#c6a664Schneeblock/Schneeblock(Netheriteschaufel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fAbbauen von &#c6a664Schneeblock(Netheriteschaufel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Clay/Clayball(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,CLAY,mat=CLAY_BALL%",
				"&fSilkT% of &#c6a664Clay/Clay(Netheriteshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,CLAY,mat=CLAY%",
				"&fMining of &#c6a664Clay(Netheriteshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,CLAY% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,CLAY% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,CLAY% Dollar",
						  
				"&f% of &#c6a664Mud/Mud(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MUD,mat=MUD%",
				"&fSilkT% of &#c6a664Mud/Mud(Netheriteshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MUD,mat=MUD%",
				"&fMining of &#c6a664Mud(Netheriteshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MUD% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MUD% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MUD% Dollar",
						  
				"&f% of &#c6a664Mycelium/Mycelium(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fSilkT% of &#c6a664Mycelium/Mycelium(Netheriteshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MYCELIUM,mat=MYCELIUM%",
				"&fMining of &#c6a664Mycelium(Netheriteshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MYCELIUM% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MYCELIUM% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,MYCELIUM% Dollar",
						  
				"&f% of &#c6a664Snow/Snowball(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snow/Snow(Netheriteshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Netheriteshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW% Dollar",
						  
			    "&f% of &#c6a664Snowblock/Snowball(Netheriteshovel) &#546f42%tt_reward_tech_dropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK,mat=SNOW_BALL%",
				"&fSilkT% of &#c6a664Snowblock/Snowblock(Netheriteshovel) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK,mat=SNOW_BLOCK%",
				"&fMining of &#c6a664Snow(Netheriteshovel) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,soil_III,7,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"soil_III", new String[] {"Böden_III", "Soil_III"}, TechnologyType.MULTIPLE, 7, PlayerAssociatedType.SOLO, 2, "", "soil", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Böden III","8Tech Soil III"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Ton, Schlamm, Myzel, Schnee & Schneeblock.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Clay, Mud, Mycelium, Snow & Snowblock.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Böden III","&7Soil III"},
				Material.CLAY, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dBöden III","&dSoil III"},
				Material.CLAY, 1, itemflag, null, canResLore,
				new String[] {"&5Böden III","&5Soil III"},
				Material.CLAY, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbauen von &#c6a664Ton &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,CLAY% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,CLAY% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,CLAY% Dollar",
						"&fAbbauen von &#c6a664Schlamm &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,MUD% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,MUD% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,MUD% Dollar",
						"&fAbbauen von &#c6a664Myzel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,MYCELIUM% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,MYCELIUM% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,MYCELIUM% Dollar",
						"&fAbbauen von &#c6a664Schnee &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,SNOW% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,SNOW% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,SNOW% Dollar",
						"&fAbbauen von &#c6a664Schneeblock &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Clay &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,CLAY% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,CLAY% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,CLAY% Dollar",
						"&fMining of &#c6a664Mud &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,MUD% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,MUD% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,MUD% Dollar",
						"&fMining of &#c6a664Myzel &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,MYCELIUM% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,MYCELIUM% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,MYCELIUM% Dollar",
						"&fMining of &#c6a664Snow &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,SNOW% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,SNOW% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,SNOW% Dollar",
						"&fMining of &#c6a664Snowblock &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,soil_III,BREAKING,NETHERITE_SHOVEL,SNOW_BLOCK% Dollar",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Mining_Stone(String[] itemflag, String[] enchantment) //INFO:Mining_Stone
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * solototaltech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(1, new String[] {
				"WOODEN_PICKAXE;8",
				"STICK;8"});
		toResCostMaterial.put(2, new String[] {
				"STONE_PICKAXE;8",
				"STICK;8"});
		toResCostMaterial.put(3, new String[] {
				"IRON_PICKAXE;4",
				"STICK;8"});
		toResCostMaterial.put(4, new String[] {
				"GOLDEN_PICKAXE;4",
				"STICK;8"});
		toResCostMaterial.put(5, new String[] {
				"DIAMOND_PICKAXE;3",
				"STICK;8"});
		toResCostMaterial.put(6, new String[] {
				"NETHERITE_PICKAXE;2",
				"STICK;8"});
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"BREAKING:STONE:null:tool=WOODEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:COBBLESTONE:null:tool=WOODEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"BREAKING:STONE:null:tool=STONE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:COBBLESTONE:null:tool=STONE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"BREAKING:STONE:null:tool=IRON_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:COBBLESTONE:null:tool=IRON_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"BREAKING:STONE:null:tool=GOLDEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:COBBLESTONE:null:tool=GOLDEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"BREAKING:STONE:null:tool=DIAMOND_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:COBBLESTONE:null:tool=DIAMOND_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"BREAKING:STONE:null:tool=NETHERITE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:COBBLESTONE:null:tool=NETHERITE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:WOODEN_PICKAXE:STONE:null:mat=COBBLESTONE:1:1.0",
				"BREAKING:WOODEN_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:STONE_PICKAXE:STONE:null:mat=COBBLESTONE:1:1.0",
				"BREAKING:STONE_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardDropChance.put(3, new String[] {
				"BREAKING:IRON_PICKAXE:STONE:null:mat=COBBLESTONE:1:1.0",
				"BREAKING:IRON_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardDropChance.put(4, new String[] {
				"BREAKING:GOLDEN_PICKAXE:STONE:null:mat=COBBLESTONE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardDropChance.put(5, new String[] {
				"BREAKING:DIAMOND_PICKAXE:STONE:null:mat=COBBLESTONE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardDropChance.put(6, new String[] {
				"BREAKING:NETHERITE_PICKAXE:STONE:null:mat=COBBLESTONE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:WOODEN_PICKAXE:STONE:null:mat=STONE:1:1.0",
				"BREAKING:WOODEN_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardSilkTouchDropChance.put(2, new String[] {
				"BREAKING:STONE_PICKAXE:STONE:null:mat=STONE:1:1.0",
				"BREAKING:STONE_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardSilkTouchDropChance.put(3, new String[] {
				"BREAKING:IRON_PICKAXE:STONE:null:mat=STONE:1:1.0",
				"BREAKING:IRON_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardSilkTouchDropChance.put(4, new String[] {
				"BREAKING:GOLDEN_PICKAXE:STONE:null:mat=STONE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardSilkTouchDropChance.put(5, new String[] {
				"BREAKING:DIAMOND_PICKAXE:STONE:null:mat=STONE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardSilkTouchDropChance.put(6, new String[] {
				"BREAKING:NETHERITE_PICKAXE:STONE:null:mat=STONE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Stein/Bruchstein(Holzspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,STONE,mat=COBBLESTONE%",
				"&fBehuts. % von &#c6a664Stein/Stein(Holzspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,STONE,mat=STONE%",
				"&fAbbauen von &#c6a664Stein(Holzspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,STONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,STONE% Dollar",
						  
				"&f% von &#c6a664Bruchstein/Bruchstein(Holzspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fBehuts. % von &#c6a664Schlamm/Schlamm(Holzspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fAbbauen von &#c6a664Schlamm(Holzspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,COBBLESTONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,COBBLESTONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,COBBLESTONE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Stone/Cobblestone(Woodenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,STONE,mat=COBBLESTONE%",
				"&fSilkT% of &#c6a664Stone/Stone(Woodenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,STONE,mat=STONE%",
				"&fMining of &#c6a664Stone(Woodenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,STONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,STONE% Dollar",
						  
				"&f% of &#c6a664Cobblestone/Cobblestone(Woodenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fSilkT% of &#c6a664Cobblestone/Cobblestone(Woodenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fMining of &#c6a664Cobblestone(Woodenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,COBBLESTONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,COBBLESTONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,1,BREAKING,WOODEN_PICKAXE,COBBLESTONE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Stein/Bruchstein(Steinspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,STONE,mat=COBBLESTONE%",
				"&fBehuts. % von &#c6a664Stein/Stein(Steinspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,STONE,mat=STONE%",
				"&fAbbauen von &#c6a664Stein(Steinspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,STONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,STONE% Dollar",
						  
				"&f% von &#c6a664Bruchstein/Bruchstein(Steinspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fBehuts. % von &#c6a664Schlamm/Schlamm(Steinspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fAbbauen von &#c6a664Schlamm(Steinspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,COBBLESTONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,COBBLESTONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,COBBLESTONE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Stone/Cobblestone(Stonepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,STONE,mat=COBBLESTONE%",
				"&fSilkT% of &#c6a664Stone/Stone(Stonepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,STONE,mat=STONE%",
				"&fMining of &#c6a664Stone(Stonepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,STONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,STONE% Dollar",
						  
				"&f% of &#c6a664Cobblestone/Cobblestone(Stonepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fSilkT% of &#c6a664Cobblestone/Cobblestone(Stonepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fMining of &#c6a664Cobblestone(Stonepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,COBBLESTONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,COBBLESTONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,2,BREAKING,STONE_PICKAXE,COBBLESTONE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Stein/Bruchstein(Eisenspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,STONE,mat=COBBLESTONE%",
				"&fBehuts. % von &#c6a664Stein/Stein(Eisenspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,STONE,mat=STONE%",
				"&fAbbauen von &#c6a664Stein(Eisenspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,STONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,STONE% Dollar",
						  
				"&f% von &#c6a664Bruchstein/Bruchstein(Eisenspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fBehuts. % von &#c6a664Schlamm/Schlamm(Eisenspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fAbbauen von &#c6a664Schlamm(Eisenspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,COBBLESTONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,COBBLESTONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,COBBLESTONE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Stone/Cobblestone(Ironpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,STONE,mat=COBBLESTONE%",
				"&fSilkT% of &#c6a664Stone/Stone(Ironpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,STONE,mat=STONE%",
				"&fMining of &#c6a664Stone(Ironpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,STONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,STONE% Dollar",
						  
				"&f% of &#c6a664Cobblestone/Cobblestone(Ironpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fSilkT% of &#c6a664Cobblestone/Cobblestone(Ironpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fMining of &#c6a664Cobblestone(Ironpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,COBBLESTONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,COBBLESTONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,3,BREAKING,IRON_PICKAXE,COBBLESTONE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Stein/Bruchstein(Goldspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,STONE,mat=COBBLESTONE%",
				"&fBehuts. % von &#c6a664Stein/Stein(Goldspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,STONE,mat=STONE%",
				"&fAbbauen von &#c6a664Stein(Goldspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,STONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,STONE% Dollar",
						  
				"&f% von &#c6a664Bruchstein/Bruchstein(Goldspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fBehuts. % von &#c6a664Schlamm/Schlamm(Goldspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fAbbauen von &#c6a664Schlamm(Goldspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,COBBLESTONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,COBBLESTONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,COBBLESTONE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Stone/Cobblestone(Goldenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,STONE,mat=COBBLESTONE%",
				"&fSilkT% of &#c6a664Stone/Stone(Goldenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,STONE,mat=STONE%",
				"&fMining of &#c6a664Stone(Goldenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,STONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,STONE% Dollar",
						  
				"&f% of &#c6a664Cobblestone/Cobblestone(Goldenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fSilkT% of &#c6a664Cobblestone/Cobblestone(Goldenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fMining of &#c6a664Cobblestone(Goldenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,COBBLESTONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,COBBLESTONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,4,BREAKING,GOLDEN_PICKAXE,COBBLESTONE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Stein/Bruchstein(Diamantspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,STONE,mat=COBBLESTONE%",
				"&fBehuts. % von &#c6a664Stein/Stein(Diamantspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,STONE,mat=STONE%",
				"&fAbbauen von &#c6a664Stein(Diamantspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,STONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,STONE% Dollar",
						  
				"&f% von &#c6a664Bruchstein/Bruchstein(Diamantspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fBehuts. % von &#c6a664Schlamm/Schlamm(Diamantspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fAbbauen von &#c6a664Schlamm(Diamantspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,COBBLESTONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,COBBLESTONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,COBBLESTONE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Stone/Cobblestone(Diamondpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,STONE,mat=COBBLESTONE%",
				"&fSilkT% of &#c6a664Stone/Stone(Diamondpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,STONE,mat=STONE%",
				"&fMining of &#c6a664Stone(Diamondpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,STONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,STONE% Dollar",
						  
				"&f% of &#c6a664Cobblestone/Cobblestone(Diamondpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fSilkT% of &#c6a664Cobblestone/Cobblestone(Diamondpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fMining of &#c6a664Cobblestone(Diamondpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,COBBLESTONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,COBBLESTONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,5,BREAKING,DIAMOND_PICKAXE,COBBLESTONE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Stein/Bruchstein(Netheritespitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,STONE,mat=COBBLESTONE%",
				"&fBehuts. % von &#c6a664Stein/Stein(Netheritespitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,STONE,mat=STONE%",
				"&fAbbauen von &#c6a664Stein(Netheritespitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,STONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,STONE% Dollar",
						  
				"&f% von &#c6a664Bruchstein/Bruchstein(Netheritespitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fBehuts. % von &#c6a664Schlamm/Schlamm(Netheritespitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fAbbauen von &#c6a664Schlamm(Netheritespitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,COBBLESTONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,COBBLESTONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,COBBLESTONE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Stone/Cobblestone(Netheritepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,STONE,mat=COBBLESTONE%",
				"&fSilkT% of &#c6a664Stone/Stone(Netheritepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,STONE,mat=STONE%",
				"&fMining of &#c6a664Stone(Netheritepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,STONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,STONE% Dollar",
						  
				"&f% of &#c6a664Cobblestone/Cobblestone(Netheritepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fSilkT% of &#c6a664Cobblestone/Cobblestone(Netheritepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,COBBLESTONE,mat=COBBLESTONE%",
				"&fMining of &#c6a664Cobblestone(Netheritepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,COBBLESTONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,COBBLESTONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_I,6,BREAKING,NETHERITE_PICKAXE,COBBLESTONE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"stone_I", new String[] {"Stein_I", "Stone_I"}, TechnologyType.MULTIPLE, 6, PlayerAssociatedType.SOLO, 0, "", "stone", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Stein I","&8Tech Stone I"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Stein & Bruchstein.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Stone & Cobblestone.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Stein I","&7Stone I"},
				Material.STONE, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dStein I","&dStone I"},
				Material.STONE, 1, itemflag, null, canResLore,
				new String[] {"&5Stein I","&5Stone I"},
				Material.STONE, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbauen von &#c6a664Stein &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stone_I,BREAKING,NETHERITE_PICKAXE,STONE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stone_I,BREAKING,NETHERITE_PICKAXE,STONE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stone_I,BREAKING,NETHERITE_PICKAXE,STONE% Dollar",
						"&fAbbauen von &#c6a664Bruchstein &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stone_I,BREAKING,NETHERITE_PICKAXE,COBBLESTONE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stone_I,BREAKING,NETHERITE_PICKAXE,COBBLESTONE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stone_I,BREAKING,NETHERITE_PICKAXE,COBBLESTONE% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Stone &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stone_I,BREAKING,NETHERITE_PICKAXE,STONE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stone_I,BREAKING,NETHERITE_PICKAXE,STONE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stone_I,BREAKING,NETHERITE_PICKAXE,STONE% Dollar",
						"&fMining of &#c6a664Cobblestone &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stone_I,BREAKING,NETHERITE_PICKAXE,COBBLESTONE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stone_I,BREAKING,NETHERITE_PICKAXE,COBBLESTONE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stone_I,BREAKING,NETHERITE_PICKAXE,COBBLESTONE% Dollar",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
		toResCondition = new LinkedHashMap<>();
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(1, new String[] {
				"WOODEN_PICKAXE;8",
				"STICK;8"});
		toResCostMaterial.put(2, new String[] {
				"STONE_PICKAXE;8",
				"STICK;8"});
		toResCostMaterial.put(3, new String[] {
				"IRON_PICKAXE;4",
				"STICK;8"});
		toResCostMaterial.put(4, new String[] {
				"GOLDEN_PICKAXE;4",
				"STICK;8"});
		toResCostMaterial.put(5, new String[] {
				"DIAMOND_PICKAXE;3",
				"STICK;8"});
		toResCostMaterial.put(6, new String[] {
				"NETHERITE_PICKAXE;2",
				"STICK;8"});
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"BREAKING:GRANITE:null:tool=WOODEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ANDESITE:null:tool=WOODEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DIORITE:null:tool=WOODEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"BREAKING:GRANITE:null:tool=STONE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ANDESITE:null:tool=STONE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DIORITE:null:tool=STONE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"BREAKING:GRANITE:null:tool=IRON_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ANDESITE:null:tool=IRON_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DIORITE:null:tool=IRON_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"BREAKING:GRANITE:null:tool=GOLDEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ANDESITE:null:tool=GOLDEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DIORITE:null:tool=GOLDEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"BREAKING:GRANITE:null:tool=DIAMOND_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ANDESITE:null:tool=DIAMOND_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DIORITE:null:tool=DIAMOND_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"BREAKING:GRANITE:null:tool=NETHERITE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ANDESITE:null:tool=NETHERITE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DIORITE:null:tool=NETHERITE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:WOODEN_PICKAXE:GRANITE:null:mat=GRANITE:1:1.0",
				"BREAKING:WOODEN_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0",
				"BREAKING:WOODEN_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:STONE_PICKAXE:GRANITE:null:mat=GRANITE:1:1.0",
				"BREAKING:STONE_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0",
				"BREAKING:STONE_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardDropChance.put(3, new String[] {
				"BREAKING:IRON_PICKAXE:GRANITE:null:mat=GRANITE:1:1.0",
				"BREAKING:IRON_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0",
				"BREAKING:IRON_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardDropChance.put(4, new String[] {
				"BREAKING:GOLDEN_PICKAXE:GRANITE:null:mat=GRANITE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardDropChance.put(5, new String[] {
				"BREAKING:DIAMOND_PICKAXE:GRANITE:null:mat=GRANITE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardDropChance.put(6, new String[] {
				"BREAKING:NETHERITE_PICKAXE:GRANITE:null:mat=GRANITE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:COBBLESTONE:null:mat=COBBLESTONE:1:1.0"});
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:WOODEN_PICKAXE:GRANITE:null:mat=GRANITE:1:1.0",
				"BREAKING:WOODEN_PICKAXE:ANDESITE:null:mat=ANDESITE:1:1.0",
				"BREAKING:WOODEN_PICKAXE:DIORITE:null:mat=DIORITE:1:1.0"});
		rewardSilkTouchDropChance.put(2, new String[] {
				"BREAKING:STONE_PICKAXE:GRANITE:null:mat=GRANITE:1:1.0",
				"BREAKING:STONE_PICKAXE:ANDESITE:null:mat=ANDESITE:1:1.0",
				"BREAKING:STONE_PICKAXE:DIORITE:null:mat=DIORITE:1:1.0"});
		rewardSilkTouchDropChance.put(3, new String[] {
				"BREAKING:IRON_PICKAXE:GRANITE:null:mat=GRANITE:1:1.0",
				"BREAKING:IRON_PICKAXE:ANDESITE:null:mat=ANDESITE:1:1.0",
				"BREAKING:IRON_PICKAXE:DIORITE:null:mat=DIORITE:1:1.0"});
		rewardSilkTouchDropChance.put(4, new String[] {
				"BREAKING:GOLDEN_PICKAXE:GRANITE:null:mat=GRANITE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:ANDESITE:null:mat=ANDESITE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:DIORITE:null:mat=DIORITE:1:1.0"});
		rewardSilkTouchDropChance.put(5, new String[] {
				"BREAKING:DIAMOND_PICKAXE:GRANITE:null:mat=GRANITE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:ANDESITE:null:mat=ANDESITE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:DIORITE:null:mat=DIORITE:1:1.0"});
		rewardSilkTouchDropChance.put(6, new String[] {
				"BREAKING:NETHERITE_PICKAXE:GRANITE:null:mat=GRANITE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:ANDESITE:null:mat=ANDESITE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:DIORITE:null:mat=DIORITE:1:1.0"});
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Granit/Granit(Holzspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,GRANITE,mat=GRANITE%",
				"&fBehuts. % von &#c6a664Granit/Granit(Holzspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,GRANITE,mat=GRANITE%",
				"&fAbbauen von &#c6a664Granit(Holzspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,GRANITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,GRANITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,GRANITE% Dollar",
						  
				"&f% von &#c6a664Andesit/Andesit(Holzspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fBehuts. % von &#c6a664Andesit/Andesit(Holzspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fAbbauen von &#c6a664Andesit(Holzspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,ANDESITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,ANDESITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,ANDESITE% Dollar",
						  
				"&f% von &#c6a664Diorit/Diorit(Holzspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,DIORITE,mat=DIORITE%",
				"&fBehuts. % von &#c6a664Diorit/Diorit(Holzspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,DIORITE,mat=DIORITE%",
				"&fAbbauen von &#c6a664Diorit(Holzspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,DIORITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,DIORITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,DIORITE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Granite/Granite(Woodenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,GRANITE,mat=GRANITE%",
				"&fSilkT% of &#c6a664Granite/Granite(Woodenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,GRANITE,mat=GRANITE%",
				"&fMining of &#c6a664Granite(Woodenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,GRANITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,GRANITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,GRANITE% Dollar",
						  
				"&f% of &#c6a664Andesite/Andesite(Woodenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fSilkT% of &#c6a664Andesite/Andesite(Woodenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fMining of &#c6a664Andesite(Woodenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,ANDESITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,ANDESITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,ANDESITE% Dollar",
						  
				"&f% of &#c6a664Diorite/Diorite(Woodenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,DIORITE,mat=DIORITE%",
				"&fSilkT% of &#c6a664Diorite/Diorite(Woodenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,DIORITE,mat=DIORITE%",
				"&fMining of &#c6a664Diorite(Woodenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,DIORITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,DIORITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,1,BREAKING,WOODEN_PICKAXE,DIORITE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Granit/Granit(Steinspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,GRANITE,mat=GRANITE%",
				"&fBehuts. % von &#c6a664Granit/Granit(Steinspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,GRANITE,mat=GRANITE%",
				"&fAbbauen von &#c6a664Granit(Steinspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,GRANITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,GRANITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,GRANITE% Dollar",
						  
				"&f% von &#c6a664Andesit/Andesit(Steinspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fBehuts. % von &#c6a664Andesit/Andesit(Steinspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fAbbauen von &#c6a664Andesit(Steinspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,ANDESITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,ANDESITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,ANDESITE% Dollar",
						  
				"&f% von &#c6a664Diorit/Diorit(Steinspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,DIORITE,mat=DIORITE%",
				"&fBehuts. % von &#c6a664Diorit/Diorit(Steinspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,DIORITE,mat=DIORITE%",
				"&fAbbauen von &#c6a664Diorit(Steinspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,DIORITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,DIORITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,DIORITE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Granite/Granite(Stonepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,GRANITE,mat=GRANITE%",
				"&fSilkT% of &#c6a664Granite/Granite(Stonepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,GRANITE,mat=GRANITE%",
				"&fMining of &#c6a664Granite(Stonepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,GRANITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,GRANITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,GRANITE% Dollar",
						  
				"&f% of &#c6a664Andesite/Andesite(Stonepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fSilkT% of &#c6a664Andesite/Andesite(Stonepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fMining of &#c6a664Andesite(Stonepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,ANDESITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,ANDESITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,ANDESITE% Dollar",
						  
				"&f% of &#c6a664Diorite/Diorite(Stonepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,DIORITE,mat=DIORITE%",
				"&fSilkT% of &#c6a664Diorite/Diorite(Stonepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,DIORITE,mat=DIORITE%",
				"&fMining of &#c6a664Diorite(Stonepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,DIORITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,DIORITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,2,BREAKING,STONE_PICKAXE,DIORITE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Granit/Granit(Eisenspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,GRANITE,mat=GRANITE%",
				"&fBehuts. % von &#c6a664Granit/Granit(Eisenspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,GRANITE,mat=GRANITE%",
				"&fAbbauen von &#c6a664Granit(Eisenspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,GRANITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,GRANITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,GRANITE% Dollar",
						  
				"&f% von &#c6a664Andesit/Andesit(Eisenspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fBehuts. % von &#c6a664Andesit/Andesit(Eisenspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fAbbauen von &#c6a664Andesit(Eisenspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,ANDESITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,ANDESITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,ANDESITE% Dollar",
						  
				"&f% von &#c6a664Diorit/Diorit(Eisenspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,DIORITE,mat=DIORITE%",
				"&fBehuts. % von &#c6a664Diorit/Diorit(Eisenspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,DIORITE,mat=DIORITE%",
				"&fAbbauen von &#c6a664Diorit(Eisenspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,DIORITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,DIORITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,DIORITE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Granite/Granite(Ironpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,GRANITE,mat=GRANITE%",
				"&fSilkT% of &#c6a664Granite/Granite(Ironpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,GRANITE,mat=GRANITE%",
				"&fMining of &#c6a664Granite(Ironpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,GRANITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,GRANITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,GRANITE% Dollar",
						  
				"&f% of &#c6a664Andesite/Andesite(Ironpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fSilkT% of &#c6a664Andesite/Andesite(Ironpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fMining of &#c6a664Andesite(Ironpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,ANDESITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,ANDESITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,ANDESITE% Dollar",
						  
				"&f% of &#c6a664Diorite/Diorite(Ironpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,DIORITE,mat=DIORITE%",
				"&fSilkT% of &#c6a664Diorite/Diorite(Ironpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,DIORITE,mat=DIORITE%",
				"&fMining of &#c6a664Diorite(Ironpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,DIORITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,DIORITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,3,BREAKING,IRON_PICKAXE,DIORITE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Granit/Granit(Goldspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,GRANITE,mat=GRANITE%",
				"&fBehuts. % von &#c6a664Granit/Granit(Goldspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,GRANITE,mat=GRANITE%",
				"&fAbbauen von &#c6a664Granit(Goldspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,GRANITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,GRANITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,GRANITE% Dollar",
						  
				"&f% von &#c6a664Andesit/Andesit(Goldspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fBehuts. % von &#c6a664Andesit/Andesit(Goldspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fAbbauen von &#c6a664Andesit(Goldspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,ANDESITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,ANDESITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,ANDESITE% Dollar",
						  
				"&f% von &#c6a664Diorit/Diorit(Goldspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,DIORITE,mat=DIORITE%",
				"&fBehuts. % von &#c6a664Diorit/Diorit(Goldspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,DIORITE,mat=DIORITE%",
				"&fAbbauen von &#c6a664Diorit(Goldspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,DIORITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,DIORITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,DIORITE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Granite/Granite(Goldenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,GRANITE,mat=GRANITE%",
				"&fSilkT% of &#c6a664Granite/Granite(Goldenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,GRANITE,mat=GRANITE%",
				"&fMining of &#c6a664Granite(Goldenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,GRANITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,GRANITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,GRANITE% Dollar",
						  
				"&f% of &#c6a664Andesite/Andesite(Goldenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fSilkT% of &#c6a664Andesite/Andesite(Goldenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fMining of &#c6a664Andesite(Goldenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,ANDESITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,ANDESITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,ANDESITE% Dollar",
						  
				"&f% of &#c6a664Diorite/Diorite(Goldenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,DIORITE,mat=DIORITE%",
				"&fSilkT% of &#c6a664Diorite/Diorite(Goldenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,DIORITE,mat=DIORITE%",
				"&fMining of &#c6a664Diorite(Goldenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,DIORITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,DIORITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,4,BREAKING,GOLDEN_PICKAXE,DIORITE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Granit/Granit(Diamandspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,GRANITE,mat=GRANITE%",
				"&fBehuts. % von &#c6a664Granit/Granit(Diamandspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,GRANITE,mat=GRANITE%",
				"&fAbbauen von &#c6a664Granit(Diamandspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,GRANITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,GRANITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,GRANITE% Dollar",
						  
				"&f% von &#c6a664Andesit/Andesit(Diamandspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fBehuts. % von &#c6a664Andesit/Andesit(Diamandspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fAbbauen von &#c6a664Andesit(Diamandspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,ANDESITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,ANDESITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,ANDESITE% Dollar",
						  
				"&f% von &#c6a664Diorit/Diorit(Diamandspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,DIORITE,mat=DIORITE%",
				"&fBehuts. % von &#c6a664Diorit/Diorit(Diamandspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,DIORITE,mat=DIORITE%",
				"&fAbbauen von &#c6a664Diorit(Diamandspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,DIORITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,DIORITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,DIORITE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Granite/Granite(Diamondpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,GRANITE,mat=GRANITE%",
				"&fSilkT% of &#c6a664Granite/Granite(Diamondpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,GRANITE,mat=GRANITE%",
				"&fMining of &#c6a664Granite(Diamondpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,GRANITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,GRANITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,GRANITE% Dollar",
						  
				"&f% of &#c6a664Andesite/Andesite(Diamondpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fSilkT% of &#c6a664Andesite/Andesite(Diamondpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fMining of &#c6a664Andesite(Diamondpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,ANDESITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,ANDESITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,ANDESITE% Dollar",
						  
				"&f% of &#c6a664Diorite/Diorite(Diamondpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,DIORITE,mat=DIORITE%",
				"&fSilkT% of &#c6a664Diorite/Diorite(Diamondpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,DIORITE,mat=DIORITE%",
				"&fMining of &#c6a664Diorite(Diamondpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,DIORITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,DIORITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,5,BREAKING,DIAMOND_PICKAXE,DIORITE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Granit/Granit(Netheritespitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,GRANITE,mat=GRANITE%",
				"&fBehuts. % von &#c6a664Granit/Granit(Netheritespitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,GRANITE,mat=GRANITE%",
				"&fAbbauen von &#c6a664Granit(Netheritespitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,GRANITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,GRANITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,GRANITE% Dollar",
						  
				"&f% von &#c6a664Andesit/Andesit(Netheritespitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fBehuts. % von &#c6a664Andesit/Andesit(Netheritespitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fAbbauen von &#c6a664Andesit(Netheritespitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,ANDESITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,ANDESITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,ANDESITE% Dollar",
						  
				"&f% von &#c6a664Diorit/Diorit(Netheritespitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,DIORITE,mat=DIORITE%",
				"&fBehuts. % von &#c6a664Diorit/Diorit(Netheritespitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,DIORITE,mat=DIORITE%",
				"&fAbbauen von &#c6a664Diorit(Netheritespitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,DIORITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,DIORITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,DIORITE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Granite/Granite(Netheritepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,GRANITE,mat=GRANITE%",
				"&fSilkT% of &#c6a664Granite/Granite(Netheritepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,GRANITE,mat=GRANITE%",
				"&fMining of &#c6a664Granite(Netheritepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,GRANITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,GRANITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,GRANITE% Dollar",
						  
				"&f% of &#c6a664Andesite/Andesite(Netheritepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fSilkT% of &#c6a664Andesite/Andesite(Netheritepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,ANDESITE,mat=ANDESITE%",
				"&fMining of &#c6a664Andesite(Netheritepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,ANDESITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,ANDESITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,ANDESITE% Dollar",
						  
				"&f% of &#c6a664Diorite/Diorite(Netheritepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,DIORITE,mat=DIORITE%",
				"&fSilkT% of &#c6a664Diorite/Diorite(Netheritepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,DIORITE,mat=DIORITE%",
				"&fMining of &#c6a664Diorite(Netheritepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,DIORITE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,DIORITE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stone_II,6,BREAKING,NETHERITE_PICKAXE,DIORITE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"stone_II", new String[] {"Stein_II", "Stone_II"}, TechnologyType.MULTIPLE, 6, PlayerAssociatedType.SOLO, 1, "", "stone", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Stein_II","&8Tech Stone_II"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Granit, Andesit & Diorit.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Granite, Andesite & Diorite.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Stein_II","&7Stone_II"},
				Material.GRANITE, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dStein_II","&dStone_II"},
				Material.GRANITE, 1, itemflag, null, canResLore,
				new String[] {"&5Stein_II","&5Stone_II"},
				Material.GRANITE, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbauen von &#c6a664Granit &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,GRANITE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,GRANITE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,GRANITE% Dollar",
						"&fAbbauen von &#c6a664Andesit &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,ANDESITE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,ANDESITE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,ANDESITE% Dollar",
						"&fAbbauen von &#c6a664Diorit &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,DIORITE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,DIORITE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,DIORITE% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Granite &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,GRANITE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,GRANITE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,GRANITE% Dollar",
						"&fMining of &#c6a664Andesite &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,ANDESITE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,ANDESITE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,ANDESITE% Dollar",
						"&fMining of &#c6a664Diorite &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,DIORITE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,DIORITE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stone_II,BREAKING,NETHERITE_PICKAXE,DIORITE% Dollar",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Mining_Ore(String[] itemflag, String[] enchantment) //INFO:Mining_Ore
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * solototaltech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(1, new String[] {
				"WOODEN_PICKAXE;8",
				"STICK;8"});
		toResCostMaterial.put(2, new String[] {
				"STONE_PICKAXE;8",
				"STICK;8"});
		toResCostMaterial.put(3, new String[] {
				"IRON_PICKAXE;4",
				"STICK;8"});
		toResCostMaterial.put(4, new String[] {
				"GOLDEN_PICKAXE;4",
				"STICK;8"});
		toResCostMaterial.put(5, new String[] {
				"DIAMOND_PICKAXE;3",
				"STICK;8"});
		toResCostMaterial.put(6, new String[] {
				"NETHERITE_PICKAXE;2",
				"STICK;8"});
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"BREAKING:COAL_ORE:null:tool=WOODEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DEEPSLATE_COAL_ORE:null:tool=WOODEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"BREAKING:COAL_ORE:null:tool=STONE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DEEPSLATE_COAL_ORE:null:tool=STONE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"BREAKING:COAL_ORE:null:tool=IRON_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DEEPSLATE_COAL_ORE:null:tool=IRON_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"BREAKING:COAL_ORE:null:tool=GOLDEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DEEPSLATE_COAL_ORE:null:tool=GOLDEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"BREAKING:COAL_ORE:null:tool=DIAMOND_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DEEPSLATE_COAL_ORE:null:tool=DIAMOND_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"BREAKING:COAL_ORE:null:tool=NETHERITE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DEEPSLATE_COAL_ORE:null:tool=NETHERITE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:WOODEN_PICKAXE:COAL_ORE:null:mat=COAL:1:1.0",
				"BREAKING:WOODEN_PICKAXE:COAL_ORE:null:mat=COAL:2:0.5",
				"BREAKING:WOODEN_PICKAXE:COAL_ORE:null:mat=COAL:3:0.25",
				"BREAKING:WOODEN_PICKAXE:COAL_ORE:null:mat=COAL:4:0.125",
				"BREAKING:WOODEN_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:1:1.0",
				"BREAKING:WOODEN_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:2:0.5",
				"BREAKING:WOODEN_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:3:0.25",
				"BREAKING:WOODEN_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:4:0.125"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:STONE_PICKAXE:COAL_ORE:null:mat=COAL:1:1.0",
				"BREAKING:STONE_PICKAXE:COAL_ORE:null:mat=COAL:2:0.5",
				"BREAKING:STONE_PICKAXE:COAL_ORE:null:mat=COAL:3:0.25",
				"BREAKING:STONE_PICKAXE:COAL_ORE:null:mat=COAL:4:0.125",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:1:1.0",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:2:0.5",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:3:0.25",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:4:0.125"});
		rewardDropChance.put(3, new String[] {
				"BREAKING:IRON_PICKAXE:COAL_ORE:null:mat=COAL:1:1.0",
				"BREAKING:IRON_PICKAXE:COAL_ORE:null:mat=COAL:2:0.5",
				"BREAKING:IRON_PICKAXE:COAL_ORE:null:mat=COAL:3:0.25",
				"BREAKING:IRON_PICKAXE:COAL_ORE:null:mat=COAL:4:0.125",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:1:1.0",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:2:0.5",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:3:0.25",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:4:0.125"});
		rewardDropChance.put(4, new String[] {
				"BREAKING:GOLDEN_PICKAXE:COAL_ORE:null:mat=COAL:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:COAL_ORE:null:mat=COAL:2:0.5",
				"BREAKING:GOLDEN_PICKAXE:COAL_ORE:null:mat=COAL:3:0.25",
				"BREAKING:GOLDEN_PICKAXE:COAL_ORE:null:mat=COAL:4:0.125",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:2:0.5",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:3:0.25",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:4:0.125"});
		rewardDropChance.put(5, new String[] {
				"BREAKING:DIAMOND_PICKAXE:COAL_ORE:null:mat=COAL:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:COAL_ORE:null:mat=COAL:2:0.5",
				"BREAKING:DIAMOND_PICKAXE:COAL_ORE:null:mat=COAL:3:0.25",
				"BREAKING:DIAMOND_PICKAXE:COAL_ORE:null:mat=COAL:4:0.125",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:2:0.5",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:3:0.25",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:4:0.125"});
		rewardDropChance.put(6, new String[] {
				"BREAKING:NETHERITE_PICKAXE:COAL_ORE:null:mat=COAL:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:COAL_ORE:null:mat=COAL:2:0.5",
				"BREAKING:NETHERITE_PICKAXE:COAL_ORE:null:mat=COAL:3:0.25",
				"BREAKING:NETHERITE_PICKAXE:COAL_ORE:null:mat=COAL:4:0.125",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:2:0.5",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:3:0.25",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=COAL:4:0.125"});
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:WOODEN_PICKAXE:COAL_ORE:null:mat=COAL_ORE:1:1.0",
				"BREAKING:WOODEN_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=DEEPSLATE_COAL_ORE:1:1.0"});
		rewardSilkTouchDropChance.put(2, new String[] {
				"BREAKING:STONE_PICKAXE:COAL_ORE:null:mat=COAL_ORE:1:1.0",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=DEEPSLATE_COAL_ORE:1:1.0"});
		rewardSilkTouchDropChance.put(3, new String[] {
				"BREAKING:IRON_PICKAXE:COAL_ORE:null:mat=COAL_ORE:1:1.0",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=DEEPSLATE_COAL_ORE:1:1.0"});
		rewardSilkTouchDropChance.put(4, new String[] {
				"BREAKING:GOLDEN_PICKAXE:COAL_ORE:null:mat=COAL_ORE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=DEEPSLATE_COAL_ORE:1:1.0"});
		rewardSilkTouchDropChance.put(5, new String[] {
				"BREAKING:DIAMOND_PICKAXE:COAL_ORE:null:mat=COAL_ORE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=DEEPSLATE_COAL_ORE:1:1.0"});
		rewardSilkTouchDropChance.put(6, new String[] {
				"BREAKING:NETHERITE_PICKAXE:COAL_ORE:null:mat=COAL_ORE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_COAL_ORE:null:mat=DEEPSLATE_COAL_ORE:1:1.0"});
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Kohleerz/Kohle(Holzspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,COAL_ORE,mat=COAL%",
				"&fBehuts. % von &#c6a664Kohleerz/Kohleerz(Holzspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,COAL_ORE,mat=COAL_ORE%",
				"&fAbbauen von &#c6a664Kohleerz(Holzspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,COAL_ORE% Dollar",
						  
				"&f% von &#c6a664Tiefenschieferkohleerz/Kohle(Holzspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,DEEPSLATE_COAL_ORE,mat=COAL%",
				"&fBehuts. % von &#c6a664Tiefenschieferkohleerz/Tiefenschieferkohleerz(Holzspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,DEEPSLATE_COAL_ORE,mat=DEEPSLATE_COAL_ORE%",
				"&fAbbauen von &#c6a664Tiefenschieferkohleerz(Holzspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Coalore/Coal(Woodenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,COAL_ORE,mat=COAL%",
				"&fSilkT% of &#c6a664Coalore/Coalore(Woodenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,COAL_ORE,mat=COAL_ORE%",
				"&fMining of &#c6a664Coalore(Woodenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,COAL_ORE% Dollar",
						  
				"&f% of &#c6a664Deepslatecoalore/Coal(Woodenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,DEEPSLATE_COAL_ORE,mat=COAL%",
				"&fSilkT% of &#c6a664Deepslatecoalore/Deepslatecoalore(Woodenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,DEEPSLATE_COAL_ORE,mat=DEEPSLATE_COAL_ORE%",
				"&fMining of &#c6a664Deepslatecoalore(Woodenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,1,BREAKING,WOODEN_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Kohleerz/Kohle(Steinspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,COAL_ORE,mat=COAL%",
				"&fBehuts. % von &#c6a664Kohleerz/Kohleerz(Steinspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,COAL_ORE,mat=COAL_ORE%",
				"&fAbbauen von &#c6a664Kohleerz(Steinspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,COAL_ORE% Dollar",
						  
				"&f% von &#c6a664Tiefenschieferkohleerz/Kohle(Steinspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,DEEPSLATE_COAL_ORE,mat=COAL%",
				"&fBehuts. % von &#c6a664Tiefenschieferkohleerz/Tiefenschieferkohleerz(Steinspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,DEEPSLATE_COAL_ORE,mat=DEEPSLATE_COAL_ORE%",
				"&fAbbauen von &#c6a664Tiefenschieferkohleerz(Steinspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Coalore/Coal(Stonepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,COAL_ORE,mat=COAL%",
				"&fSilkT% of &#c6a664Coalore/Coalore(Stonepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,COAL_ORE,mat=COAL_ORE%",
				"&fMining of &#c6a664Coalore(Stonepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,COAL_ORE% Dollar",
						  
				"&f% of &#c6a664Deepslatecoalore/Coal(Stonepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,DEEPSLATE_COAL_ORE,mat=COAL%",
				"&fSilkT% of &#c6a664Deepslatecoalore/Deepslatecoalore(Stonepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,DEEPSLATE_COAL_ORE,mat=DEEPSLATE_COAL_ORE%",
				"&fMining of &#c6a664Deepslatecoalore(Stonepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,2,BREAKING,STONE_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Kohleerz/Kohle(Eisenspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,COAL_ORE,mat=COAL%",
				"&fBehuts. % von &#c6a664Kohleerz/Kohleerz(Eisenspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,COAL_ORE,mat=COAL_ORE%",
				"&fAbbauen von &#c6a664Kohleerz(Eisenspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,COAL_ORE% Dollar",
						  
				"&f% von &#c6a664Tiefenschieferkohleerz/Kohle(Eisenspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,DEEPSLATE_COAL_ORE,mat=COAL%",
				"&fBehuts. % von &#c6a664Tiefenschieferkohleerz/Tiefenschieferkohleerz(Eisenspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,DEEPSLATE_COAL_ORE,mat=DEEPSLATE_COAL_ORE%",
				"&fAbbauen von &#c6a664Tiefenschieferkohleerz(Eisenspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Coalore/Coal(Ironpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,COAL_ORE,mat=COAL%",
				"&fSilkT% of &#c6a664Coalore/Coalore(Ironpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,COAL_ORE,mat=COAL_ORE%",
				"&fMining of &#c6a664Coalore(Ironpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,COAL_ORE% Dollar",
						  
				"&f% of &#c6a664Deepslatecoalore/Coal(Ironpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,DEEPSLATE_COAL_ORE,mat=COAL%",
				"&fSilkT% of &#c6a664Deepslatecoalore/Deepslatecoalore(Ironpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,DEEPSLATE_COAL_ORE,mat=DEEPSLATE_COAL_ORE%",
				"&fMining of &#c6a664Deepslatecoalore(Ironpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,3,BREAKING,IRON_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Kohleerz/Kohle(Goldspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,COAL_ORE,mat=COAL%",
				"&fBehuts. % von &#c6a664Kohleerz/Kohleerz(Goldspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,COAL_ORE,mat=COAL_ORE%",
				"&fAbbauen von &#c6a664Kohleerz(Goldspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,COAL_ORE% Dollar",
						  
				"&f% von &#c6a664Tiefenschieferkohleerz/Kohle(Goldspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_COAL_ORE,mat=COAL%",
				"&fBehuts. % von &#c6a664Tiefenschieferkohleerz/Tiefenschieferkohleerz(Goldspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_COAL_ORE,mat=DEEPSLATE_COAL_ORE%",
				"&fAbbauen von &#c6a664Tiefenschieferkohleerz(Goldspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Coalore/Coal(Goldenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,COAL_ORE,mat=COAL%",
				"&fSilkT% of &#c6a664Coalore/Coalore(Goldenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,COAL_ORE,mat=COAL_ORE%",
				"&fMining of &#c6a664Coalore(Goldenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,COAL_ORE% Dollar",
						  
				"&f% of &#c6a664Deepslatecoalore/Coal(Goldenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_COAL_ORE,mat=COAL%",
				"&fSilkT% of &#c6a664Deepslatecoalore/Deepslatecoalore(Goldenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_COAL_ORE,mat=DEEPSLATE_COAL_ORE%",
				"&fMining of &#c6a664Deepslatecoalore(Goldenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,4,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Kohleerz/Kohle(Diamantspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,COAL_ORE,mat=COAL%",
				"&fBehuts. % von &#c6a664Kohleerz/Kohleerz(Diamantspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,COAL_ORE,mat=COAL_ORE%",
				"&fAbbauen von &#c6a664Kohleerz(Diamantspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,COAL_ORE% Dollar",
						  
				"&f% von &#c6a664Tiefenschieferkohleerz/Kohle(Diamantspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_COAL_ORE,mat=COAL%",
				"&fBehuts. % von &#c6a664Tiefenschieferkohleerz/Tiefenschieferkohleerz(Diamantspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_COAL_ORE,mat=DEEPSLATE_COAL_ORE%",
				"&fAbbauen von &#c6a664Tiefenschieferkohleerz(Diamantspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Coalore/Coal(Diamondpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,COAL_ORE,mat=COAL%",
				"&fSilkT% of &#c6a664Coalore/Coalore(Diamondpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,COAL_ORE,mat=COAL_ORE%",
				"&fMining of &#c6a664Coalore(Diamondpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,COAL_ORE% Dollar",
						  
				"&f% of &#c6a664Deepslatecoalore/Coal(Diamondpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_COAL_ORE,mat=COAL%",
				"&fSilkT% of &#c6a664Deepslatecoalore/Deepslatecoalore(Diamondpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_COAL_ORE,mat=DEEPSLATE_COAL_ORE%",
				"&fMining of &#c6a664Deepslatecoalore(Diamondpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,5,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Kohleerz/Kohle(Netheritespitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,COAL_ORE,mat=COAL%",
				"&fBehuts. % von &#c6a664Kohleerz/Kohleerz(Netheritespitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,COAL_ORE,mat=COAL_ORE%",
				"&fAbbauen von &#c6a664Kohleerz(Netheritespitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,COAL_ORE% Dollar",
						  
				"&f% von &#c6a664Tiefenschieferkohleerz/Tiefenschieferkohleerz(Netheritespitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE,mat=COAL%",
				"&fBehuts. % von &#c6a664Tiefenschieferkohleerz/Tiefenschieferkohleerz(Netheritespitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE,mat=DEEPSLATE_COAL_ORE%",
				"&fAbbauen von &#c6a664Tiefenschieferkohleerz(Netheritespitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Coalore/(Netheritepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,COAL_ORE,mat=COAL%",
				"&fSilkT% of &#c6a664Coalore/Coalore(Netheritepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,COAL_ORE,mat=COAL_ORE%",
				"&fMining of &#c6a664Coalore(Netheritepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,COAL_ORE% Dollar",
						  
				"&f% of &#c6a664Deepslatecoalore/Coal(Netheritepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE,mat=COAL%",
				"&fSilkT% of &#c6a664Deepslatecoalore/Deepslatecoalore(Netheritepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE,mat=DEEPSLATE_COAL_ORE%",
				"&fMining of &#c6a664Deepslatecoalore(Netheritepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,coalore,6,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"coalore", new String[] {"Kohleerz", "Coalore"}, TechnologyType.MULTIPLE, 6, PlayerAssociatedType.SOLO, 0, "", "ore", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Kohleerz","&8Tech Coalore"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Kohleerz & Tiefenschieferkohleerz.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Coalore & Deepslatecoalore.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Kohleerz","&7Coalore"},
				Material.COAL_ORE, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dKohleerz","&dCoalore"},
				Material.COAL_ORE, 1, itemflag, null, canResLore,
				new String[] {"&5Kohleerz","&5Coalore"},
				Material.COAL_ORE, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbauen von &#c6a664Kohleerz &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,coalore,BREAKING,NETHERITE_PICKAXE,COAL_ORE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,coalore,BREAKING,NETHERITE_PICKAXE,COAL_ORE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,coalore,BREAKING,NETHERITE_PICKAXE,COAL_ORE% Dollar",
						"&fAbbauen von &#c6a664Tiefenschieferkohleerz &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,coalore,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,coalore,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,coalore,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Coalore &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,coalore,BREAKING,NETHERITE_PICKAXE,COAL_ORE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,coalore,BREAKING,NETHERITE_PICKAXE,COAL_ORE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,coalore,BREAKING,NETHERITE_PICKAXE,COAL_ORE% Dollar",
						"&fMining of &#c6a664Deepslatecoalore &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,coalore,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,coalore,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,coalore,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_COAL_ORE% Dollar",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
		toResCondition = new LinkedHashMap<>();
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(1, new String[] {
				"STONE_PICKAXE;8",
				"STICK;8"});
		toResCostMaterial.put(2, new String[] {
				"IRON_PICKAXE;4",
				"STICK;8"});
		toResCostMaterial.put(3, new String[] {
				"GOLDEN_PICKAXE;4",
				"STICK;8"});
		toResCostMaterial.put(4, new String[] {
				"DIAMOND_PICKAXE;3",
				"STICK;8"});
		toResCostMaterial.put(5, new String[] {
				"NETHERITE_PICKAXE;2",
				"STICK;8"});
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"BREAKING:IRON_ORE:null:tool=STONE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DEEPSLATE_IRON_ORE:null:tool=STONE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"BREAKING:IRON_ORE:null:tool=IRON_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DEEPSLATE_IRON_ORE:null:tool=IRON_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"BREAKING:IRON_ORE:null:tool=GOLDEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DEEPSLATE_IRON_ORE:null:tool=GOLDEN_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"BREAKING:IRON_ORE:null:tool=DIAMOND_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DEEPSLATE_IRON_ORE:null:tool=DIAMOND_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"BREAKING:IRON_ORE:null:tool=NETHERITE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DEEPSLATE_IRON_ORE:null:tool=NETHERITE_PICKAXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:STONE_PICKAXE:IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:STONE_PICKAXE:IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:STONE_PICKAXE:IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:STONE_PICKAXE:IRON_ORE:null:mat=RAW_IRON:4:0.125",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:4:0.125"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:IRON_PICKAXE:IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:IRON_PICKAXE:IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:IRON_PICKAXE:IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:IRON_PICKAXE:IRON_ORE:null:mat=RAW_IRON:4:0.125",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:4:0.125"});
		rewardDropChance.put(3, new String[] {
				"BREAKING:GOLDEN_PICKAXE:IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:GOLDEN_PICKAXE:IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:GOLDEN_PICKAXE:IRON_ORE:null:mat=RAW_IRON:4:0.125",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:4:0.125"});
		rewardDropChance.put(4, new String[] {
				"BREAKING:DIAMOND_PICKAXE:IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:DIAMOND_PICKAXE:IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:DIAMOND_PICKAXE:IRON_ORE:null:mat=RAW_IRON:4:0.125",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:4:0.125"});
		rewardDropChance.put(5, new String[] {
				"BREAKING:NETHERITE_PICKAXE:IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:NETHERITE_PICKAXE:IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:NETHERITE_PICKAXE:IRON_ORE:null:mat=RAW_IRON:4:0.125",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:4:0.125"});
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:STONE_PICKAXE:IRON_ORE:null:mat=IRON_ORE:1:1.0",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=DEEPSLATE_IRON_ORE:1:1.0"});
		rewardSilkTouchDropChance.put(2, new String[] {
				"BREAKING:IRON_PICKAXE:IRON_ORE:null:mat=IRON_ORE:1:1.0",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=DEEPSLATE_IRON_ORE:1:1.0"});
		rewardSilkTouchDropChance.put(3, new String[] {
				"BREAKING:GOLDEN_PICKAXE:IRON_ORE:null:mat=IRON_ORE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=DEEPSLATE_IRON_ORE:1:1.0"});
		rewardSilkTouchDropChance.put(4, new String[] {
				"BREAKING:DIAMOND_PICKAXE:IRON_ORE:null:mat=IRON_ORE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=DEEPSLATE_IRON_ORE:1:1.0"});
		rewardSilkTouchDropChance.put(5, new String[] {
				"BREAKING:NETHERITE_PICKAXE:IRON_ORE:null:mat=IRON_ORE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=DEEPSLATE_IRON_ORE:1:1.0"});
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eisenerz/Roheisen(Steinspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,IRON_ORE,mat=RAW_IRON%",
				"&fBehuts. % von &#c6a664Eisenerz/Eisenerz(Steinspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,IRON_ORE,mat=IRON_ORE%",
				"&fAbbauen von &#c6a664Eisenerz(Steinspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,IRON_ORE% Dollar",
						  
				"&f% von &#c6a664Tiefenschieferkohleerz/Roheisen(Steinspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,DEEPSLATE_IRON_ORE,mat=RAW_IRON%",
				"&fBehuts. % von &#c6a664Tiefenschieferkohleerz/Tiefenschieferkohleerz(Steinspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,DEEPSLATE_IRON_ORE,mat=DEEPSLATE_IRON_ORE%",
				"&fAbbauen von &#c6a664Tiefenschieferkohleerz(Steinspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,DEEPSLATE_IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,DEEPSLATE_IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,DEEPSLATE_IRON_ORE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Ironore/Rawiron(Stonepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,IRON_ORE,mat=RAW_IRON%",
				"&fSilkT% of &#c6a664Ironore/Ironore(Stonepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,IRON_ORE,mat=IRON_ORE%",
				"&fMining of &#c6a664Coalore(Stonepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,IRON_ORE% Dollar",
						  
				"&f% of &#c6a664Deepslatecoalore/Rawiron(Stonepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,DEEPSLATE_IRON_ORE,mat=RAW_IRON%",
				"&fSilkT% of &#c6a664Deepslatecoalore/Deepslatecoalore(Stonepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,DEEPSLATE_IRON_ORE,mat=DEEPSLATE_IRON_ORE%",
				"&fMining of &#c6a664Deepslatecoalore(Stonepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,DEEPSLATE_IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,DEEPSLATE_IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,1,BREAKING,STONE_PICKAXE,DEEPSLATE_IRON_ORE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eisenerz/Roheisen(Eisenspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,IRON_ORE,mat=RAW_IRON%",
				"&fBehuts. % von &#c6a664Eisenerz/Eisenerz(Eisenspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,IRON_ORE,mat=IRON_ORE%",
				"&fAbbauen von &#c6a664Eisenerz(Eisenspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,IRON_ORE% Dollar",
						  
				"&f% von &#c6a664Tiefenschieferkohleerz/Roheisen(Eisenspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,DEEPSLATE_IRON_ORE,mat=RAW_IRON%",
				"&fBehuts. % von &#c6a664Tiefenschieferkohleerz/Tiefenschieferkohleerz(Eisenspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,DEEPSLATE_IRON_ORE,mat=DEEPSLATE_IRON_ORE%",
				"&fAbbauen von &#c6a664Tiefenschieferkohleerz(Eisenspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,DEEPSLATE_IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,DEEPSLATE_IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,DEEPSLATE_IRON_ORE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Ironore/Rawiron(Ironpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,IRON_ORE,mat=RAW_IRON%",
				"&fSilkT% of &#c6a664Ironore/Ironore(Ironpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,IRON_ORE,mat=IRON_ORE%",
				"&fMining of &#c6a664Coalore(Ironpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,IRON_ORE% Dollar",
						  
				"&f% of &#c6a664Deepslatecoalore/Rawiron(Ironpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,DEEPSLATE_IRON_ORE,mat=RAW_IRON%",
				"&fSilkT% of &#c6a664Deepslatecoalore/Deepslatecoalore(Ironpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,DEEPSLATE_IRON_ORE,mat=DEEPSLATE_IRON_ORE%",
				"&fMining of &#c6a664Deepslatecoalore(Ironpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,DEEPSLATE_IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,DEEPSLATE_IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,2,BREAKING,IRON_PICKAXE,DEEPSLATE_IRON_ORE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eisenerz/Roheisen(Goldspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,IRON_ORE,mat=RAW_IRON%",
				"&fBehuts. % von &#c6a664Eisenerz/Eisenerz(Goldspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,IRON_ORE,mat=IRON_ORE%",
				"&fAbbauen von &#c6a664Eisenerz(Goldspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,IRON_ORE% Dollar",
						  
				"&f% von &#c6a664Tiefenschieferkohleerz/Roheisen(Goldspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_IRON_ORE,mat=RAW_IRON%",
				"&fBehuts. % von &#c6a664Tiefenschieferkohleerz/Tiefenschieferkohleerz(Goldspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_IRON_ORE,mat=DEEPSLATE_IRON_ORE%",
				"&fAbbauen von &#c6a664Tiefenschieferkohleerz(Goldspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_IRON_ORE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Ironore/Rawiron(Woodenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,IRON_ORE,mat=RAW_IRON%",
				"&fSilkT% of &#c6a664Ironore/Ironore(Woodenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,IRON_ORE,mat=IRON_ORE%",
				"&fMining of &#c6a664Coalore(Woodenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,IRON_ORE% Dollar",
						  
				"&f% of &#c6a664Deepslatecoalore/Rawiron(Woodenpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_IRON_ORE,mat=RAW_IRON%",
				"&fSilkT% of &#c6a664Deepslatecoalore/Deepslatecoalore(Woodenpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_IRON_ORE,mat=DEEPSLATE_IRON_ORE%",
				"&fMining of &#c6a664Deepslatecoalore(Woodenpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,3,BREAKING,GOLDEN_PICKAXE,DEEPSLATE_IRON_ORE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eisenerz/Roheisen(Diamantspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,IRON_ORE,mat=RAW_IRON%",
				"&fBehuts. % von &#c6a664Eisenerz/Eisenerz(Diamantspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,IRON_ORE,mat=IRON_ORE%",
				"&fAbbauen von &#c6a664Eisenerz(Diamantspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,IRON_ORE% Dollar",
						  
				"&f% von &#c6a664Tiefenschieferkohleerz/Roheisen(Diamantspitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_IRON_ORE,mat=RAW_IRON%",
				"&fBehuts. % von &#c6a664Tiefenschieferkohleerz/Tiefenschieferkohleerz(Diamantspitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_IRON_ORE,mat=DEEPSLATE_IRON_ORE%",
				"&fAbbauen von &#c6a664Tiefenschieferkohleerz(Diamantspitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_IRON_ORE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Ironore/Rawiron(Diamondpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,IRON_ORE,mat=RAW_IRON%",
				"&fSilkT% of &#c6a664Ironore/Ironore(Diamondpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,IRON_ORE,mat=IRON_ORE%",
				"&fMining of &#c6a664Coalore(Diamondpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,IRON_ORE% Dollar",
						  
				"&f% of &#c6a664Deepslatecoalore/Rawiron(Diamondpickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_IRON_ORE,mat=RAW_IRON%",
				"&fSilkT% of &#c6a664Deepslatecoalore/Deepslatecoalore(Diamondpickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_IRON_ORE,mat=DEEPSLATE_IRON_ORE%",
				"&fMining of &#c6a664Deepslatecoalore(Diamondpickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,4,BREAKING,DIAMOND_PICKAXE,DEEPSLATE_IRON_ORE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eisenerz/Roheisen(Netheritespitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,IRON_ORE,mat=RAW_IRON%",
				"&fBehuts. % von &#c6a664Eisenerz/Eisenerz(Netheritespitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,IRON_ORE,mat=IRON_ORE%",
				"&fAbbauen von &#c6a664Eisenerz(Netheritespitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,IRON_ORE% Dollar",
						  
				"&f% von &#c6a664Tiefenschieferkohleerz/Roheisen(Netheritespitzhacke) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE,mat=RAW_IRON%",
				"&fBehuts. % von &#c6a664Tiefenschieferkohleerz/Tiefenschieferkohleerz(Netheritespitzhacke) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE,mat=DEEPSLATE_IRON_ORE%",
				"&fAbbauen von &#c6a664Tiefenschieferkohleerz(Netheritespitzhacke) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Ironore/Rawiron(Netheritepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,IRON_ORE,mat=RAW_IRON%",
				"&fSilkT% of &#c6a664Ironore/Ironore(Netheritepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,IRON_ORE,mat=IRON_ORE%",
				"&fMining of &#c6a664Coalore(Netheritepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,IRON_ORE% Dollar",
						  
				"&f% of &#c6a664Deepslatecoalore/Rawiron(Netheritepickaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE,mat=RAW_IRON%",
				"&fSilkT% of &#c6a664Deepslatecoalore/Deepslatecoalore(Netheritepickaxe) &#546f42%tt_reward_tech_silktouchdropchance_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE,mat=DEEPSLATE_IRON_ORE%",
				"&fMining of &#c6a664Deepslatecoalore(Netheritepickaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,ironore,5,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"ironore", new String[] {"Eisenerz", "Ironore"},
				TechnologyType.MULTIPLE, 5, PlayerAssociatedType.SOLO, 1, "", "ore", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Eisenerz","&8Tech Ironore"}, 
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Eisenerz & Tiefenschiefereisenerz.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Ironore & Deepslateironore.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Eisenerz","&7Ironore"},
				Material.IRON_ORE, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dEisenerz","&dIronore"},
				Material.IRON_ORE, 1, itemflag, null, canResLore,
				new String[] {"&5Eisenerz","&5Ironore"},
				Material.IRON_ORE, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbauen von &#c6a664Eisenerz &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,ironore,BREAKING,NETHERITE_PICKAXE,IRON_ORE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,ironore,BREAKING,NETHERITE_PICKAXE,IRON_ORE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,ironore,BREAKING,NETHERITE_PICKAXE,IRON_ORE% Dollar",
						"&fAbbauen von &#c6a664Tiefenschiefereisenerz &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,ironore,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,ironore,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,ironore,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Ironore &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,ironore,BREAKING,NETHERITE_PICKAXE,IRON_ORE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,ironore,BREAKING,NETHERITE_PICKAXE,IRON_ORE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,ironore,BREAKING,NETHERITE_PICKAXE,IRON_ORE% Dollar",
						"&fMining of &#c6a664Deepslateironore &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,ironore,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,ironore,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,ironore,BREAKING,NETHERITE_PICKAXE,DEEPSLATE_IRON_ORE% Dollar",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Woodworking_Sapling(String[] itemflag, String[] enchantment) //INFO:Woodworking_Sapling
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		String cTTExp = "100 * techlev + 25 * solototaltech";
		toResCostTTExp.put(1, cTTExp);
		toResCostTTExp.put(2, cTTExp);
		toResCostTTExp.put(3, cTTExp);
		toResCostTTExp.put(4, cTTExp);
		toResCostTTExp.put(5, cTTExp);
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		String cVExp = "10 * techlev + 2.5 * solototaltech";
		toResCostVanillaExp.put(1, cVExp);
		toResCostVanillaExp.put(2, cVExp);
		toResCostVanillaExp.put(3, cVExp);
		toResCostVanillaExp.put(4, cVExp);
		toResCostVanillaExp.put(5, cVExp);
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		String cM = "1 * techlev + 0.25 * solototaltech";
		toResCostMoney.put(1, cM);
		toResCostMoney.put(2, cM);
		toResCostMoney.put(3, cM);
		toResCostMoney.put(4, cM);
		toResCostMoney.put(5, cM);
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		String[] rui = new String[] {"PLACING:OAK_SAPLING:null:tool=HAND:ttexp=10.0:vaexp=10:vault=10.0:default=10.0",
									 "BREAKING:OAK_SAPLING:null:tool=HAND"};
		rewardUnlockableInteractions.put(1, rui);
		rewardUnlockableInteractions.put(2, rui);
		rewardUnlockableInteractions.put(3, rui);
		rewardUnlockableInteractions.put(4, rui);
		rewardUnlockableInteractions.put(5, rui);
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		String[] rdc = new String[] {"BREAKING:HAND:OAK_SAPLING:null:mat=OAK_SAPLING:1:1.0",""};
		rewardDropChance.put(1, rdc);
		rewardDropChance.put(2, rdc);
		rewardDropChance.put(3, rdc);
		rewardDropChance.put(4, rdc);
		rewardDropChance.put(5, rdc);
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eichensetzling/Eichensetzling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaksapling,1,BREAKING,HAND,OAK_SAPLING,mat=OAK_SAPLING%",
				"&fAbbauen von &#c6a664Eichensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,1,BREAKING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,1,BREAKING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,1,BREAKING,HAND,OAK_SAPLING% Dollar",
				"&fSetzten von Eichensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,1,PLACING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,1,PLACING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,1,PLACING,HAND,OAK_SAPLING% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Oaksapling/Oaksapling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaksapling,1,BREAKING,HAND,OAK_SAPLING,mat=OAK_SAPLING%",
				"&fMining of &#c6a664Oaksapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,1,BREAKING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,1,BREAKING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,1,BREAKING,HAND,OAK_SAPLING% Dollar",
				"&fPlacing of &#c6a664Oaksapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,1,PLACING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,1,PLACING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,1,PLACING,HAND,OAK_SAPLING% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eichensetzling/Eichensetzling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaksapling,2,BREAKING,HAND,OAK_SAPLING,mat=OAK_SAPLING%",
				"&fAbbauen von &#c6a664Eichensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,2,BREAKING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,2,BREAKING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,2,BREAKING,HAND,OAK_SAPLING% Dollar",
				"&fSetzten von Eichensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,2,PLACING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,2,PLACING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,2,PLACING,HAND,OAK_SAPLING% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Oaksapling/Oaksapling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaksapling,2,BREAKING,HAND,OAK_SAPLING,mat=OAK_SAPLING%",
				"&fMining of &#c6a664Oaksapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,2,BREAKING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,2,BREAKING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,2,BREAKING,HAND,OAK_SAPLING% Dollar",
				"&fPlacing of &#c6a664Oaksapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,2,PLACING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,2,PLACING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,2,PLACING,HAND,OAK_SAPLING% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eichensetzling/Eichensetzling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaksapling,3,BREAKING,HAND,OAK_SAPLING,mat=OAK_SAPLING%",
				"&fAbbauen von &#c6a664Eichensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,3,BREAKING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,3,BREAKING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,3,BREAKING,HAND,OAK_SAPLING% Dollar",
				"&fSetzten von Eichensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,3,PLACING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,3,PLACING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,3,PLACING,HAND,OAK_SAPLING% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Oaksapling/Oaksapling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaksapling,3,BREAKING,HAND,OAK_SAPLING,mat=OAK_SAPLING%",
				"&fMining of &#c6a664Oaksapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,3,BREAKING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,3,BREAKING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,3,BREAKING,HAND,OAK_SAPLING% Dollar",
				"&fPlacing of &#c6a664Oaksapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,3,PLACING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,3,PLACING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,3,PLACING,HAND,OAK_SAPLING% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eichensetzling/Eichensetzling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaksapling,4,BREAKING,HAND,OAK_SAPLING,mat=OAK_SAPLING%",
				"&fAbbauen von &#c6a664Eichensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,4,BREAKING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,4,BREAKING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,4,BREAKING,HAND,OAK_SAPLING% Dollar",
				"&fSetzten von Eichensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,4,PLACING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,4,PLACING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,4,PLACING,HAND,OAK_SAPLING% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Oaksapling/Oaksapling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaksapling,4,BREAKING,HAND,OAK_SAPLING,mat=OAK_SAPLING%",
				"&fMining of &#c6a664Oaksapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,4,BREAKING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,4,BREAKING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,4,BREAKING,HAND,OAK_SAPLING% Dollar",
				"&fPlacing of &#c6a664Oaksapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,4,PLACING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,4,PLACING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,4,PLACING,HAND,OAK_SAPLING% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eichensetzling/Eichensetzling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaksapling,5,BREAKING,HAND,OAK_SAPLING,mat=OAK_SAPLING%",
				"&fAbbauen von &#c6a664Eichensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,5,BREAKING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,5,BREAKING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,5,BREAKING,HAND,OAK_SAPLING% Dollar",
				"&fSetzten von Eichensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,5,PLACING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,5,PLACING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,5,PLACING,HAND,OAK_SAPLING% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Oaksapling/Oaksapling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaksapling,5,BREAKING,HAND,OAK_SAPLING,mat=OAK_SAPLING%",
				"&fMining of &#c6a664Oaksapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,5,BREAKING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,5,BREAKING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,5,BREAKING,HAND,OAK_SAPLING% Dollar",
				"&fPlacing of &#c6a664Oaksapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaksapling,5,PLACING,HAND,OAK_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaksapling,5,PLACING,HAND,OAK_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaksapling,5,PLACING,HAND,OAK_SAPLING% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"oaksapling", new String[] {"Eichensetzling", "Oaksapling"},
				TechnologyType.MULTIPLE, 5, PlayerAssociatedType.SOLO, 0, "", "sapling", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Eichensetzling","&8Tech Oaksapling"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Eichensetzling.",
						"&fSetzen von &#c6a664Eichensetzling.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Oaksapling.",
						"&fPlacing of &#c6a664Oaksapling.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Eichensetzling","&7Oaksapling"},
				Material.OAK_SAPLING, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dEichensetzling","&dOaksapling"},
				Material.OAK_SAPLING, 1, itemflag, null, canResLore,
				new String[] {"&5Eichensetzling","&5Oaksapling"},
				Material.OAK_SAPLING, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbauen von &#c6a664Eichensetzling &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,oaksapling,BREAKING,HAND,OAK_SAPLING% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,oaksapling,BREAKING,HAND,OAK_SAPLING% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,oaksapling,BREAKING,HAND,OAK_SAPLING% Dollar",
						"&fSetzen von &#c6a664Eichensetzling &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,oaksapling,PLACING,HAND,OAK_SAPLING% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,oaksapling,PLACING,HAND,OAK_SAPLING% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,oaksapling,PLACING,HAND,OAK_SAPLING% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Oaksapling &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,oaksapling,BREAKING,HAND,OAK_SAPLING% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,oaksapling,BREAKING,HAND,OAK_SAPLING% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,oaksapling,BREAKING,HAND,OAK_SAPLING% Dollar",
						"&fPlacing of &#c6a664Oaksapling &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,oaksapling,PLACING,HAND,OAK_SAPLING% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,oaksapling,PLACING,HAND,OAK_SAPLING% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,oaksapling,PLACING,HAND,OAK_SAPLING% Dollar",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
		toResCondition = new LinkedHashMap<>();
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, cTTExp);
		toResCostTTExp.put(2, cTTExp);
		toResCostTTExp.put(3, cTTExp);
		toResCostTTExp.put(4, cTTExp);
		toResCostTTExp.put(5, cTTExp);
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, cVExp);
		toResCostVanillaExp.put(2, cVExp);
		toResCostVanillaExp.put(3, cVExp);
		toResCostVanillaExp.put(4, cVExp);
		toResCostVanillaExp.put(5, cVExp);
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, cM);
		toResCostMoney.put(2, cM);
		toResCostMoney.put(3, cM);
		toResCostMoney.put(4, cM);
		toResCostMoney.put(5, cM);
		toResCostMaterial = new LinkedHashMap<>();
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rui = new String[] {"PLACING:SPRUCE_SAPLING:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",""};
		rewardUnlockableInteractions.put(1, rui);
		rewardUnlockableInteractions.put(2, rui);
		rewardUnlockableInteractions.put(3, rui);
		rewardUnlockableInteractions.put(4, rui);
		rewardUnlockableInteractions.put(5, rui);
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardDropChance = new LinkedHashMap<>();
		rdc = new String[] {"BREAKING:SPRUCE_SAPLING:null:mat=SPRUCE_SAPLING:1:1.0",""};
		rewardDropChance.put(1, rdc);
		rewardDropChance.put(2, rdc);
		rewardDropChance.put(3, rdc);
		rewardDropChance.put(4, rdc);
		rewardDropChance.put(5, rdc);
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, rdc);
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Fichtensetzling/Fichtensetzling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,sprucesapling,1,BREAKING,HAND,SPRUCE_SAPLING,mat=SPRUCE_SAPLING%",
				"&fAbbauen von &#c6a664Fichtensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,1,BREAKING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,1,BREAKING,HAND,SPRUCE_SAPLING% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,1,BREAKING,HAND,SPRUCE_SAPLING% Dollar",
				"&fSetzten von Fichtensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,1,PLACING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,1,PLACING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,1,PLACING,HAND,SPRUCE_SAPLING% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Sprucesapling/Sprucesapling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,sprucesapling,1,BREAKING,HAND,SPRUCE_SAPLING,mat=SPRUCE_SAPLING%",
				"&fMining of &#c6a664Sprucesapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,1,BREAKING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,1,BREAKING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,1,BREAKING,HAND,SPRUCE_SAPLING% Dollar",
				"&fPlacing of &#c6a664Sprucesapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,1,PLACING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,1,PLACING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,1,PLACING,HAND,SPRUCE_SAPLING% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Fichtensetzling/Fichtensetzling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,sprucesapling,2,BREAKING,HAND,SPRUCE_SAPLING,mat=SPRUCE_SAPLING%",
				"&fAbbauen von &#c6a664Fichtensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,2,BREAKING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,2,BREAKING,HAND,SPRUCE_SAPLING% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,2,BREAKING,HAND,SPRUCE_SAPLING% Dollar",
				"&fSetzten von Fichtensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,2,PLACING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,2,PLACING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,2,PLACING,HAND,SPRUCE_SAPLING% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Sprucesapling/Sprucesapling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,sprucesapling,2,BREAKING,HAND,SPRUCE_SAPLING,mat=SPRUCE_SAPLING%",
				"&fMining of &#c6a664Sprucesapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,2,BREAKING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,2,BREAKING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,2,BREAKING,HAND,SPRUCE_SAPLING% Dollar",
				"&fPlacing of &#c6a664Sprucesapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,2,PLACING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,2,PLACING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,2,PLACING,HAND,SPRUCE_SAPLING% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Fichtensetzling/Fichtensetzling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,sprucesapling,3,BREAKING,HAND,SPRUCE_SAPLING,mat=SPRUCE_SAPLING%",
				"&fAbbauen von &#c6a664Fichtensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,3,BREAKING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,3,BREAKING,HAND,SPRUCE_SAPLING% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,3,BREAKING,HAND,SPRUCE_SAPLING% Dollar",
				"&fSetzten von Fichtensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,3,PLACING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,3,PLACING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,3,PLACING,HAND,SPRUCE_SAPLING% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Sprucesapling/Sprucesapling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,sprucesapling,3,BREAKING,HAND,SPRUCE_SAPLING,mat=SPRUCE_SAPLING%",
				"&fMining of &#c6a664Sprucesapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,3,BREAKING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,3,BREAKING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,3,BREAKING,HAND,SPRUCE_SAPLING% Dollar",
				"&fPlacing of &#c6a664Sprucesapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,3,PLACING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,3,PLACING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,3,PLACING,HAND,SPRUCE_SAPLING% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Fichtensetzling/Fichtensetzling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,sprucesapling,4,BREAKING,HAND,SPRUCE_SAPLING,mat=SPRUCE_SAPLING%",
				"&fAbbauen von &#c6a664Fichtensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,4,BREAKING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,4,BREAKING,HAND,SPRUCE_SAPLING% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,4,BREAKING,HAND,SPRUCE_SAPLING% Dollar",
				"&fSetzten von Fichtensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,4,PLACING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,4,PLACING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,4,PLACING,HAND,SPRUCE_SAPLING% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Sprucesapling/Sprucesapling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,sprucesapling,4,BREAKING,HAND,SPRUCE_SAPLING,mat=SPRUCE_SAPLING%",
				"&fMining of &#c6a664Sprucesapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,4,BREAKING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,4,BREAKING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,4,BREAKING,HAND,SPRUCE_SAPLING% Dollar",
				"&fPlacing of &#c6a664Sprucesapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,4,PLACING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,4,PLACING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,4,PLACING,HAND,SPRUCE_SAPLING% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Fichtensetzling/Fichtensetzling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,sprucesapling,5,BREAKING,HAND,SPRUCE_SAPLING,mat=SPRUCE_SAPLING%",
				"&fAbbauen von &#c6a664Fichtensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,5,BREAKING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,5,BREAKING,HAND,SPRUCE_SAPLING% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,5,BREAKING,HAND,SPRUCE_SAPLING% Dollar",
				"&fSetzten von Fichtensetzling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,5,PLACING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,5,PLACING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,5,PLACING,HAND,SPRUCE_SAPLING% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Sprucesapling/Sprucesapling(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,sprucesapling,5,BREAKING,HAND,SPRUCE_SAPLING,mat=SPRUCE_SAPLING%",
				"&fMining of &#c6a664Sprucesapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,5,BREAKING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,5,BREAKING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,5,BREAKING,HAND,SPRUCE_SAPLING% Dollar",
				"&fPlacing of &#c6a664Sprucesapling(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,sprucesapling,5,PLACING,HAND,SPRUCE_SAPLING% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,sprucesapling,5,PLACING,HAND,SPRUCE_SAPLING% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,sprucesapling,5,PLACING,HAND,SPRUCE_SAPLING% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"sprucesapling", new String[] {"Fichtensetzling", "Sprucesapling"},
				TechnologyType.SIMPLE, 1, PlayerAssociatedType.SOLO, 1, "", "sapling", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Fichtensetzling","&8Tech Sprucesapling"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Fichtensetzling.",
						"&fSetzen von &#c6a664Fichtensetzling.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Sprucesapling.",
						"&fPlacing of &#c6a664Sprucesapling.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Fichtensetzling","&7Sprucesapling"},
				Material.SPRUCE_SAPLING, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dFichtensetzling","&dSprucesapling"},
				Material.SPRUCE_SAPLING, 1, itemflag, null, canResLore,
				new String[] {"&5Fichtensetzling","&5Sprucesapling"},
				Material.SPRUCE_SAPLING, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbauen von &#c6a664Fichtensetzling &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sprucesapling,BREAKING,HAND,SPRUCE_SAPLING% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sprucesapling,BREAKING,HAND,SPRUCE_SAPLING% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sprucesapling,BREAKING,HAND,SPRUCE_SAPLING% Dollar",
						"&fSetzen von &#c6a664Fichtensetzling &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sprucesapling,PLACING,HAND,SPRUCE_SAPLING% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sprucesapling,PLACING,HAND,SPRUCE_SAPLING% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sprucesapling,PLACING,HAND,SPRUCE_SAPLING% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Sprucesapling &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sprucesapling,BREAKING,HAND,SPRUCE_SAPLING% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sprucesapling,BREAKING,HAND,SPRUCE_SAPLING% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sprucesapling,BREAKING,HAND,SPRUCE_SAPLING% Dollar",
						"&fPlacing of &#c6a664Sprucesapling &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,sprucesapling,PLACING,HAND,SPRUCE_SAPLING% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,sprucesapling,PLACING,HAND,SPRUCE_SAPLING% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,sprucesapling,PLACING,HAND,SPRUCE_SAPLING% Dollar",
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
		//ADDME Alle Setzlinge
	}
	
	private void tech_Woodworking_Wood(String[] itemflag, String[] enchantment) //INFO:Woodworking_Wood
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		String cTTExp = "100 * techlev + 50 * techacq + 25 * solototaltech";
		toResCostTTExp.put(1, cTTExp);
		toResCostTTExp.put(2, cTTExp);
		toResCostTTExp.put(3, cTTExp);
		toResCostTTExp.put(4, cTTExp);
		toResCostTTExp.put(5, cTTExp);
		toResCostTTExp.put(6, cTTExp);
		toResCostTTExp.put(7, cTTExp);
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		String cVExp = "10 * techlev + 5 * techacq + 2.5 * solototaltech";
		toResCostVanillaExp.put(1, cVExp);
		toResCostVanillaExp.put(2, cVExp);
		toResCostVanillaExp.put(3, cVExp);
		toResCostVanillaExp.put(4, cVExp);
		toResCostVanillaExp.put(5, cVExp);
		toResCostVanillaExp.put(6, cVExp);
		toResCostVanillaExp.put(7, cVExp);
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		String cM = "1 * techlev + 0.5 * techacq + 0.25 * solototaltech";
		toResCostMoney.put(1, cM);
		toResCostMoney.put(2, cM);
		toResCostMoney.put(3, cM);
		toResCostMoney.put(4, cM);
		toResCostMoney.put(5, cM);
		toResCostMoney.put(6, cM);
		toResCostMoney.put(7, cM);
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		toResCostMaterial.put(2, new String[] {
				"WOODEN_PICKAXE;8",
				"STICK;8"});
		toResCostMaterial.put(3, new String[] {
				"STONE_PICKAXE;8",
				"STICK;8"});
		toResCostMaterial.put(4, new String[] {
				"IRON_PICKAXE;4",
				"STICK;8"});
		toResCostMaterial.put(5, new String[] {
				"GOLDEN_PICKAXE;4",
				"STICK;8"});
		toResCostMaterial.put(6, new String[] {
				"DIAMOND_PICKAXE;3",
				"STICK;8"});
		toResCostMaterial.put(7, new String[] {
				"NETHERITE_PICKAXE;2",
				"STICK;8"});
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"BREAKING:OAK_LOG:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"PLACING:OAK_LOG:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"BREAKING:OAK_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"PLACING:OAK_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"BREAKING:OAK_LOG:null:tool=STONE_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"PLACING:OAK_LOG:null:tool=STONE_AXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"BREAKING:OAK_LOG:null:tool=IRON_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"PLACING:OAK_LOG:null:tool=IRON_AXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"BREAKING:OAK_LOG:null:tool=GOLDEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"PLACING:OAK_LOG:null:tool=GOLDEN_AXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"BREAKING:OAK_LOG:null:tool=DIAMOND_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"PLACING:OAK_LOG:null:tool=DIAMOND_AXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(7, new String[] {
				"BREAKING:OAK_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"PLACING:OAK_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1"});
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		String[] rdc = new String[] {
				"BREAKING:HAND:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:HAND:OAK_LOG:null:mat=OAK_LOG:1:1.0"};
		rewardDropChance.put(1, rdc);
		rewardDropChance.put(2, rdc);
		rewardDropChance.put(3, rdc);
		rewardDropChance.put(4, rdc);
		rewardDropChance.put(5, rdc);
		rewardDropChance.put(6, rdc);
		rewardDropChance.put(7, rdc);
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, rdc);
		rewardSilkTouchDropChance.put(2, rdc);
		rewardSilkTouchDropChance.put(3, rdc);
		rewardSilkTouchDropChance.put(4, rdc);
		rewardSilkTouchDropChance.put(5, rdc);
		rewardSilkTouchDropChance.put(6, rdc);
		rewardSilkTouchDropChance.put(7, rdc);
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eichenstamm/Eichenstamm(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,1,BREAKING,HAND,OAK_LOG,mat=OAK_LOG%",
				"&fBehuts. % von &#c6a664Eichenstamm/Eichenstamm(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,1,BREAKING,HAND,OAK_LOG,mat=OAK_LOG%",
				"&fAbbauen von &#c6a664Eichenstamm(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,1,BREAKING,HAND,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,1,BREAKING,HAND,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,1,BREAKING,HAND,OAK_LOG% Dollar",
				"&fSetzen von &#c6a664Eichenstamm(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,1,PLACING,HAND,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,1,PLACING,HAND,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,1,PLACING,HAND,OAK_LOG% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Oaklog/Oaklog(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,1,BREAKING,HAND,OAK_LOG,mat=OAK_LOG%",
				"&fSilkT% of &#c6a664Oaklog/Oaklog(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,1,BREAKING,HAND,OAK_LOG,mat=OAK_LOG%",
				"&fMining of &#c6a664Oaklog(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,1,BREAKING,HAND,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,1,BREAKING,HAND,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,1,BREAKING,HAND,OAK_LOG% Dollar",
				"&fPlacing of &#c6a664Oaklog(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,1,PLACING,HAND,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,1,PLACING,HAND,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,1,PLACING,HAND,OAK_LOG% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eichenstamm/Eichenstamm(Holzaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,2,BREAKING,WOODEN_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fBehuts. % von &#c6a664Eichenstamm/Eichenstamm(Holzaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,2,BREAKING,WOODEN_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fAbbauen von &#c6a664Eichenstamm(Holzaxt) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,2,BREAKING,WOODEN_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,2,BREAKING,WOODEN_AXE,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,2,BREAKING,WOODEN_AXE,OAK_LOG% Dollar",
				"&fSetzen von &#c6a664Eichenstamm(Holzaxt) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,2,PLACING,WOODEN_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,2,PLACING,WOODEN_AXE,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,2,PLACING,WOODEN_AXE,OAK_LOG% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Oaklog/Oaklog(Woodenaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,2,BREAKING,WOODEN_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fSilkT% of &#c6a664Oaklog/Oaklog(Woodenaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,2,BREAKING,WOODEN_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fMining of &#c6a664Oaklog(Woodenaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,2,BREAKING,WOODEN_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,2,BREAKING,WOODEN_AXE,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,2,BREAKING,WOODEN_AXE,OAK_LOG% Dollar",
				"&fPlacing of &#c6a664Oaklog(Woodenaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,2,PLACING,WOODEN_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,2,PLACING,WOODEN_AXE,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,2,PLACING,WOODEN_AXE,OAK_LOG% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eichenstamm/Eichenstamm(Steinaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,3,BREAKING,STONE_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fBehuts. % von &#c6a664Eichenstamm/Eichenstamm(Steinaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,3,BREAKING,STONE_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fAbbauen von &#c6a664Eichenstamm(Steinaxt) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,3,BREAKING,STONE_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,3,BREAKING,STONE_AXE,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,3,BREAKING,STONE_AXE,OAK_LOG% Dollar",
				"&fSetzen von &#c6a664Eichenstamm(Steinaxt) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,3,PLACING,STONE_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,3,PLACING,STONE_AXE,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,3,PLACING,STONE_AXE,OAK_LOG% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Oaklog/Oaklog(Stoneaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,3,BREAKING,STONE_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fSilkT% of &#c6a664Oaklog/Oaklog(Stoneaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,3,BREAKING,STONE_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fMining of &#c6a664Oaklog(Stoneaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,3,BREAKING,STONE_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,3,BREAKING,STONE_AXE,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,3,BREAKING,STONE_AXE,OAK_LOG% Dollar",
				"&fPlacing of &#c6a664Oaklog(Stoneaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,3,PLACING,STONE_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,3,PLACING,STONE_AXE,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,3,PLACING,STONE_AXE,OAK_LOG% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eichenstamm/Eichenstamm(Eisenaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,4,BREAKING,IRON_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fBehuts. % von &#c6a664Eichenstamm/Eichenstamm(Eisenaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,4,BREAKING,IRON_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fAbbauen von &#c6a664Eichenstamm(Eisenaxt) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,4,BREAKING,IRON_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,4,BREAKING,IRON_AXE,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,4,BREAKING,IRON_AXE,OAK_LOG% Dollar",
				"&fSetzen von &#c6a664Eichenstamm(Eisenaxt) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,4,PLACING,IRON_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,4,PLACING,IRON_AXE,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,4,PLACING,IRON_AXE,OAK_LOG% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Oaklog/Oaklog(Ironaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,4,BREAKING,IRON_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fSilkT% of &#c6a664Oaklog/Oaklog(Ironaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,4,BREAKING,IRON_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fMining of &#c6a664Oaklog(Ironaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,4,BREAKING,IRON_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,4,BREAKING,IRON_AXE,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,4,BREAKING,IRON_AXE,OAK_LOG% Dollar",
				"&fPlacing of &#c6a664Oaklog(Ironaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,4,PLACING,IRON_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,4,PLACING,IRON_AXE,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,4,PLACING,IRON_AXE,OAK_LOG% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eichenstamm/Eichenstamm(Goldaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,5,BREAKING,GOLDEN_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fBehuts. % von &#c6a664Eichenstamm/Eichenstamm(Goldaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,5,BREAKING,GOLDEN_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fAbbauen von &#c6a664Eichenstamm(Goldaxt) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,5,BREAKING,GOLDEN_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,5,BREAKING,GOLDEN_AXE,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,5,BREAKING,GOLDEN_AXE,OAK_LOG% Dollar",
				"&fSetzen von &#c6a664Eichenstamm(Goldaxt) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,5,PLACING,GOLDEN_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,5,PLACING,GOLDEN_AXE,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,5,PLACING,GOLDEN_AXE,OAK_LOG% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Oaklog/Oaklog(Goldenaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,5,BREAKING,GOLDEN_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fSilkT% of &#c6a664Oaklog/Oaklog(Goldenaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,5,BREAKING,GOLDEN_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fMining of &#c6a664Oaklog(Goldenaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,5,BREAKING,GOLDEN_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,5,BREAKING,GOLDEN_AXE,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,5,BREAKING,GOLDEN_AXE,OAK_LOG% Dollar",
				"&fPlacing of &#c6a664Oaklog(Goldenaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,5,PLACING,GOLDEN_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,5,PLACING,GOLDEN_AXE,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,5,PLACING,GOLDEN_AXE,OAK_LOG% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eichenstamm/Eichenstamm(Diamantaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,6,BREAKING,DIAMOND_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fBehuts. % von &#c6a664Eichenstamm/Eichenstamm(Diamantaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,6,BREAKING,DIAMOND_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fAbbauen von &#c6a664Eichenstamm(Diamantaxt) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,6,BREAKING,DIAMOND_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,6,BREAKING,DIAMOND_AXE,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,6,BREAKING,DIAMOND_AXE,OAK_LOG% Dollar",
				"&fSetzen von &#c6a664Eichenstamm(Diamantaxt) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,6,PLACING,DIAMOND_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,6,PLACING,DIAMOND_AXE,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,6,PLACING,DIAMOND_AXE,OAK_LOG% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Oaklog/Oaklog(Diamondaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,6,BREAKING,DIAMOND_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fSilkT% of &#c6a664Oaklog/Oaklog(Diamondaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,6,BREAKING,DIAMOND_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fMining of &#c6a664Oaklog(Diamondaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,6,BREAKING,DIAMOND_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,6,BREAKING,DIAMOND_AXE,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,6,BREAKING,DIAMOND_AXE,OAK_LOG% Dollar",
				"&fPlacing of &#c6a664Oaklog(Diamondaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,6,PLACING,DIAMOND_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,6,PLACING,DIAMOND_AXE,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,6,PLACING,DIAMOND_AXE,OAK_LOG% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(7, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Eichenstamm/Eichenstamm(Netheriteaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,7,BREAKING,NETHERITE_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fBehuts. % von &#c6a664Eichenstamm/Eichenstamm(Netheriteaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,7,BREAKING,NETHERITE_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fAbbauen von &#c6a664Eichenstamm(Netheriteaxt) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,7,BREAKING,NETHERITE_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,7,BREAKING,NETHERITE_AXE,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,7,BREAKING,NETHERITE_AXE,OAK_LOG% Dollar",
				"&fSetzen von &#c6a664Eichenstamm(Netheriteaxt) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,7,PLACING,NETHERITE_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,7,PLACING,NETHERITE_AXE,OAK_LOG% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,7,PLACING,NETHERITE_AXE,OAK_LOG% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Oaklog/Oaklog(Netheriteaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,7,BREAKING,NETHERITE_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fSilkT% of &#c6a664Oaklog/Oaklog(Netheriteaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,7,BREAKING,NETHERITE_AXE,OAK_LOG,mat=OAK_LOG%",
				"&fMining of &#c6a664Oaklog(Netheriteaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,7,BREAKING,NETHERITE_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,7,BREAKING,NETHERITE_AXE,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,7,BREAKING,NETHERITE_AXE,OAK_LOG% Dollar",
				"&fPlacing of &#c6a664Oaklog(Netheriteaxe) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,oaklog,7,PLACING,NETHERITE_AXE,OAK_LOG% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,oaklog,7,PLACING,NETHERITE_AXE,OAK_LOG% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,oaklog,7,PLACING,NETHERITE_AXE,OAK_LOG% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"oaklog", new String[] {"Eichenstamm", "Oaklog"},
				TechnologyType.MULTIPLE, 7, PlayerAssociatedType.SOLO, 0, "", "wood", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Holzstamm","&8Tech Woodenlog"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Eichenstamm.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Oaklog.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Eichenstamm","&7Oaklog"},
				Material.OAK_LOG, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dEichenstamm","&dOaklog"},
				Material.OAK_LOG, 1, itemflag, null, canResLore,
				new String[] {"&5Eichenstamm","&5Oaklog"},
				Material.OAK_LOG, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fAbbauen von &#c6a664Eichenstamm &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,oaklog,BREAKING,NETHERITE_AXE,OAK_LOG% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,oaklog,BREAKING,NETHERITE_AXE,OAK_LOG% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,oaklog,BREAKING,NETHERITE_AXE,OAK_LOG% Dollar",
						"&fSetzen von &#c6a664Eichenstamm &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,oaklog,BREAKING,NETHERITE_AXE,OAK_LOG% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,oaklog,BREAKING,NETHERITE_AXE,OAK_LOG% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,oaklog,BREAKING,NETHERITE_AXE,OAK_LOG% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMining of &#c6a664Oaklog &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,oaklog,BREAKING,NETHERITE_AXE,OAK_LOG% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,oaklog,BREAKING,NETHERITE_AXE,OAK_LOG% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,oaklog,BREAKING,NETHERITE_AXE,OAK_LOG% Dollar",
						"&fPlacing of &#c6a664Oaklog &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,oaklog,BREAKING,NETHERITE_AXE,OAK_LOG% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,oaklog,BREAKING,NETHERITE_AXE,OAK_LOG% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,oaklog,BREAKING,NETHERITE_AXE,OAK_LOG% Dollar",
						
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
		toResCondition = new LinkedHashMap<>();
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostTTExp.put(7, "100 * techlev + 50 * techacq + 25 * solototaltech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostVanillaExp.put(7, "10 * techlev + 5 * techacq + 2.5 * solototaltech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMoney.put(7, "1 * techlev + 0.5 * techacq + 0.25 * solototaltech");
		toResCostMaterial = new LinkedHashMap<>();
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"CRAFTING:OAK_PLANKS:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",
				"CRAFTING:SPRUCE_PLANKS:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",
				"CRAFTING:BIRCH_PLANKS:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",
				"CRAFTING:JUNGLE_PLANKS:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",
				"CRAFTING:ACACIA_PLANKS:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",
				"CRAFTING:DARK_OAK_PLANKS:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",
				"CRAFTING:MANGROVE_PLANKS:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",
				"CRAFTING:CHERRY_PLANKS:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5"});
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {
				"SHAPELESS:oak_planks",
				"SHAPELESS:spruce_planks",
				"SHAPELESS:birch_planks",
				"SHAPELESS:jungle_planks",
				"SHAPELESS:acacia_planks",
				"SHAPELESS:dark_oak_planks",
				"SHAPELESS:mangrove_planks",
				"SHAPELESS:cherry_planks",});
		rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:HAND:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:HAND:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:HAND:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:HAND:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:HAND:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:HAND:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:HAND:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:HAND:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:WOODEN_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardDropChance.put(3, new String[] {
				"BREAKING:STONE_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardDropChance.put(4, new String[] {
				"BREAKING:IRON_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardDropChance.put(5, new String[] {
				"BREAKING:GOLDEN_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardDropChance.put(6, new String[] {
				"BREAKING:DIAMOND_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardDropChance.put(7, new String[] {
				"BREAKING:NETHERITE_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:HAND:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:HAND:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:HAND:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:HAND:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:HAND:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:HAND:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:HAND:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:HAND:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardSilkTouchDropChance.put(2, new String[] {
				"BREAKING:WOODEN_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardSilkTouchDropChance.put(3, new String[] {
				"BREAKING:STONE_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardSilkTouchDropChance.put(4, new String[] {
				"BREAKING:IRON_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardSilkTouchDropChance.put(5, new String[] {
				"BREAKING:GOLDEN_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardSilkTouchDropChance.put(6, new String[] {
				"BREAKING:DIAMOND_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardSilkTouchDropChance.put(7, new String[] {
				"BREAKING:NETHERITE_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:NETHERITE_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0"});
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Bretter/Bretter(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,1,BREAKING,HAND,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fBehuts. % von &#c6a664Bretter/Bretter(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,1,BREAKING,HAND,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fHerstellen von &#c6a664Brettern &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,woodenplanks_I,1,CRAFTING,HAND,OAK_PLANKS% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,woodenplanks_I,1,CRAFTING,HAND,OAK_PLANKS% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,woodenplanks_I,1,CRAFTING,HAND,OAK_PLANKS% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Planks/Planks(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,1,BREAKING,HAND,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fSilkT% of &#c6a664Planks/Planks(Hand) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,1,BREAKING,HAND,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fCrafting of &#c6a664Planks(Hand) &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,woodenplanks_I,1,CRAFTING,HAND,OAK_PLANKS% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,woodenplanks_I,1,CRAFTING,HAND,OAK_PLANKS% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,woodenplanks_I,1,CRAFTING,HAND,OAK_PLANKS% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Bretter/Bretter(Holzaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,2,BREAKING,WOODEN_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fBehuts. % von &#c6a664Bretter/Bretter(Holzaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,2,BREAKING,WOODEN_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Planks/Planks(Woodenaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,2,BREAKING,WOODEN_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fSilkT% of &#c6a664Planks/Planks(Woodenaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,2,BREAKING,WOODEN_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(3, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Bretter/Bretter(Steinaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,3,BREAKING,STONE_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fBehuts. % von &#c6a664Bretter/Bretter(Steinaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,3,BREAKING,STONE_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Planks/Planks(Stoneaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,3,BREAKING,STONE_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fSilkT% of &#c6a664Planks/Planks(Stoneaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,3,BREAKING,STONE_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(4, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Bretter/Bretter(Eisenaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,4,BREAKING,IRON_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fBehuts. % von &#c6a664Bretter/Bretter(Eisenaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,4,BREAKING,IRON_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Planks/Planks(Ironaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,4,BREAKING,IRON_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fSilkT% of &#c6a664Planks/Planks(Ironaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,4,BREAKING,IRON_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(5, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Bretter/Bretter(Goldaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,5,BREAKING,GOLDEN_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fBehuts. % von &#c6a664Bretter/Bretter(Goldaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,5,BREAKING,GOLDEN_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Planks/Planks(Goldenaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,5,BREAKING,GOLDEN_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fSilkT% of &#c6a664Planks/Planks(Goldenaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,5,BREAKING,GOLDEN_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(6, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Bretter/Bretter(Diamantaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,6,BREAKING,DIAMOND_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fBehuts. % von &#c6a664Bretter/Bretter(Diamantaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,6,BREAKING,DIAMOND_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Planks/Planks(Diamondaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,6,BREAKING,DIAMOND_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fSilkT% of &#c6a664Planks/Planks(Diamondaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,6,BREAKING,DIAMOND_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"",
				"&cRightclick &bfor a more detailed view."});
		canResLore.put(7, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Bretter/Bretter(Netheriteaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,7,BREAKING,NETHERITE_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fBehuts. % von &#c6a664Bretter/Bretter(Netheriteaxt) &#546f42%tt_reward_tech_dropchance_mat,SOLO,oaklog,7,BREAKING,NETHERITE_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Planks/Planks(Netheriteaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,7,BREAKING,NETHERITE_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"&fSilkT% of &#c6a664Planks/Planks(Netheriteaxe) &#546f42%tt_reward_tech_dropchance_mat,SOLO,woodenplanks_I,7,BREAKING,NETHERITE_AXE,OAK_PLANKS,mat=OAK_PLANKS%",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"woodenplanks_I", new String[] {"Holzbretter_I", "Woodenplanks_I"},
				TechnologyType.SIMPLE, 7, PlayerAssociatedType.SOLO, 1, "", "wood", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a||b||c||d||e||f||g||h):o_1", "else:o_2",
						"output:o_1:true",
						"output:o_2:false",
						"a:hasresearchedtech,oaklog,1:==:true",
						"b:hasresearchedtech,sprucelog,1:==:true",
						"c:hasresearchedtech,birchlog,1:==:true",
						"d:hasresearchedtech,junglelog,1:==:true",
						"e:hasresearchedtech,acacialog,1:==:true",
						"f:hasresearchedtech,darkoaklog,1:==:true",
						"g:hasresearchedtech,mangrovelog,1:==:true",
						"h:hasresearchedtech,cherrylog,1:==:true"}, true,
				new String[] {"&8Tech Holzbretter I","&8Tech Woodenplanks I"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >&#ff8c00Eichenstamm&c< oder >&#ff8c00Fichtenstamm&c<",
						"&coder >&#ff8c00Birkenstamm&c< oder >&#ff8c00Tropenstamm&c<",
						"&coder >&#ff8c00Akazienstamm&c< oder >&#ff8c00Schwarzeichenstamm&c<",
						"&coder >&#ff8c00Mangrovenstamm&c< oder >&#ff8c00Kirschstamm&c<",
						"&cerforscht haben.",
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von &#c6a664Holzbrettern.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&cRequirements to view:",
						"&cMust have researched the Technology >&#ff8c00Oaklog<",
						"&cor >&#ff8c00Sprucelog&c< or >&#ff8c00Birchlog&c<",
						"&cor >&#ff8c00Junglelog&c< or >&#ff8c00Acacialog&c<",
						"&cor >&#ff8c00Darkoaklog&c< or >&#ff8c00Mangrovelog&c<",
						"&cor >&#ff8c00Cherrylog&c<",
						"",
						"&eUnlocks the following:",
						"&fCrafting of &#c6a664Planks.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Holzbretter I","&7Woodenplanks I"},
				Material.OAK_PLANKS, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dHolzbretter I","&dWoodenplanks I"},
				Material.OAK_PLANKS, 1, itemflag, null, canResLore,
				new String[] {"&5Holzbretter I","&5Woodenplanks I"},
				Material.OAK_PLANKS, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von &#c6a664Holzbretter &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,woodenplanks_I,CRAFTING,HAND,OAK_PLANKS% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,woodenplanks_I,CRAFTING,HAND,OAK_PLANKS% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,woodenplanks_I,CRAFTING,HAND,OAK_PLANKS% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fCrafting of &#c6a664Planks &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,woodenplanks_I,CRAFTING,HAND,OAK_PLANKS% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,woodenplanks_I,CRAFTING,HAND,OAK_PLANKS% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,woodenplanks_I,CRAFTING,HAND,OAK_PLANKS% Dollar",						
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
	}
	
	private void tech_Stonemason_Slabs(String[] itemflag, String[] enchantment) //INFO:Stonemason_Slabs
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 25 * solototaltech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 2.5 * solototaltech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.25 * solototaltech");
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {"CRAFTING:COBBLESTONE_SLAB:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",""});
		rewardUnlockableInteractions.put(2, new String[] {"CRAFTING:STONE_SLAB:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",""});
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {
				"SHAPED:cobblestone_slab",
				"STONECUTTING:cobblestone_slab_from_cobblestone_stonecutting"});
		rewardUnlockableRecipe.put(2, new String[] {
				"SHAPED:stone_slab",
				"STONECUTTING:stone_slab_from_stone_stonecutting"});
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:HAND:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:WOODEN_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:STONE_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:IRON_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:HAND:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:WOODEN_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:STONE_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:IRON_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0"});
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:HAND:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:WOODEN_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:STONE_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:IRON_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:COBBLESTONE_SLAB:null:mat=COBBLESTONE_SLAB:1:1.0"});
		rewardSilkTouchDropChance.put(2, new String[] {
				"BREAKING:HAND:STONE_SLAB:null:mat=STONE_SLAB:1:1.0",
				"BREAKING:WOODEN_PICKAXE:STONE_SLAB:null:mat=STONE_SLAB:1:1.0",
				"BREAKING:STONE_PICKAXE:STONE_SLAB:null:mat=STONE_SLAB:1:1.0",
				"BREAKING:IRON_PICKAXE:STONE_SLAB:null:mat=STONE_SLAB:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:STONE_SLAB:null:mat=STONE_SLAB:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:STONE_SLAB:null:mat=STONE_SLAB:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:STONE_SLAB:null:mat=STONE_SLAB:1:1.0"});
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Bruchsteinstufe/Bruchsteinstufe &#546f42%tt_reward_tech_dropchance_mat,SOLO,stoneslab_I,1,BREAKING,HAND,COBBLESTONE_SLAB,mat=COBBLESTONE_SLAB%",
				"&fBehuts. % von &#c6a664Bruchsteinstufe/Bruchsteinstufe &#546f42%tt_reward_tech_dropchance_mat,SOLO,stoneslab_I,1,BREAKING,HAND,COBBLESTONE_SLAB,mat=COBBLESTONE_SLAB%",
				"&fHerstellen von &#c6a664Bruchsteinstufe &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stoneslab_I,1,CRAFTING,HAND,COBBLESTONE_SLAB% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stoneslab_I,1,CRAFTING,HAND,COBBLESTONE_SLAB% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stoneslab_I,1,CRAFTING,HAND,COBBLESTONE_SLAB% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Cobblestoneslabs/Cobblestoneslabs &#546f42%tt_reward_tech_dropchance_mat,SOLO,stoneslab_I,1,BREAKING,HAND,COBBLESTONE_SLAB,mat=COBBLESTONE_SLAB%",
				"&fSilkT% of &#c6a664Cobblestoneslabs/Cobblestoneslabs &#546f42%tt_reward_tech_dropchance_mat,SOLO,stoneslab_I,1,BREAKING,HAND,COBBLESTONE_SLAB,mat=COBBLESTONE_SLAB%",
				"&fCrafting of &#c6a664Cobblestoneslabs &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stoneslab_I,1,CRAFTING,HAND,COBBLESTONE_SLAB% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stoneslab_I,1,CRAFTING,HAND,COBBLESTONE_SLAB% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stoneslab_I,1,CRAFTING,HAND,COBBLESTONE_SLAB% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});		
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Steinstufe/Steinstufe &#546f42%tt_reward_tech_dropchance_mat,SOLO,stoneslab_I,1,BREAKING,HAND,STONE_SLAB,mat=STONE_SLAB%",
				"&fBehuts. % von &#c6a664Steinstufe/Steinstufe &#546f42%tt_reward_tech_dropchance_mat,SOLO,stoneslab_I,1,BREAKING,HAND,STONE_SLAB,mat=STONE_SLAB%",
				"&fHerstellen von &#c6a664Steinstufe &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stoneslab_I,1,CRAFTING,HAND,STONE_SLAB% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stoneslab_I,1,CRAFTING,HAND,STONE_SLAB% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stoneslab_I,1,CRAFTING,HAND,STONE_SLAB% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Stoneslaps/Stoneslaps &#546f42%tt_reward_tech_dropchance_mat,SOLO,stoneslab_I,1,BREAKING,HAND,STONE_SLAB,mat=STONE_SLAB%",
				"&fSilkT% of &#c6a664Stoneslabs/Stoneslabs &#546f42%tt_reward_tech_dropchance_mat,SOLO,stoneslab_I,1,BREAKING,HAND,STONE_SLAB,mat=STONE_SLAB%",
				"&fCrafting of &#c6a664Stoneslaps &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stoneslab_I,1,CRAFTING,HAND,STONE_SLAB% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stoneslab_I,1,CRAFTING,HAND,STONE_SLAB% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stoneslab_I,1,CRAFTING,HAND,STONE_SLAB% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"stoneslab_I", new String[] {"Steinstufen_I", "Stoneslaps_I"},
				TechnologyType.MULTIPLE, 2, PlayerAssociatedType.SOLO, 0, "", "stoneslaps", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1", "else:o_2",
						"output:o_1:true",
						"output:o_2:false",
						"a:hasresearchedtech,stone_I,1:==:true"}, true,
				new String[] {"&8Tech Steinstufen I","&8Tech Stoneslaps I"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >&#ff8c00Stein I&c< erforscht haben.",
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von &#c6a664Bruchsteinstufen.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&cRequirements to view:",
						"&cMust have researched the Technology >&#ff8c00Stone I<.",
						"",
						"&eUnlocks the following:",
						"&fCrafting of &#c6a664Cobblestoneslabs.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Steinstufen I","&7Stoneslaps I"},
				Material.STONE_SLAB, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&dSteinstufen I","&dStoneslaps I"},
				Material.STONE_SLAB, 1, itemflag, null, canResLore,
				new String[] {"&5Steinstufen I","&5Stoneslaps I"},
				Material.STONE_SLAB, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von &#c6a664Bruchsteinstufen &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stoneslab_I,CRAFTING,HAND,COBBLESTONE_SLAB% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stoneslab_I,CRAFTING,HAND,COBBLESTONE_SLAB% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stoneslab_I,CRAFTING,HAND,COBBLESTONE_SLAB% Dollar",
						"&fHerstellen von &#c6a664Steinstufen &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stoneslab_I,CRAFTING,HAND,STONE_SLAB% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stoneslab_I,CRAFTING,HAND,STONE_SLAB% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stoneslab_I,CRAFTING,HAND,STONE_SLAB% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fCrafting of &#c6a664Cobblestoneslab &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stoneslab_I,CRAFTING,HAND,COBBLESTONE_SLAB% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stoneslab_I,CRAFTING,HAND,COBBLESTONE_SLAB% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stoneslab_I,CRAFTING,HAND,COBBLESTONE_SLAB% Dollar",
						"&fCrafting of &#c6a664Stoneslab &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stoneslab_I,CRAFTING,HAND,STONE_SLAB% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stoneslab_I,CRAFTING,HAND,STONE_SLAB% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stoneslab_I,CRAFTING,HAND,STONE_SLAB% Dollar",						
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Stonemason_Stonestairs(String[] itemflag, String[] enchantment) //INFO:Stonemason_Stonestairs
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 25 * solototaltech");
		toResCostTTExp.put(2, "100 * techlev + 25 * solototaltech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 2.5 * solototaltech");
		toResCostVanillaExp.put(2, "10 * techlev + 2.5 * solototaltech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.25 * solototaltech");
		toResCostMoney.put(2, "1 * techlev + 0.25 * solototaltech");
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {"CRAFTING:COBBLESTONE_STAIRS:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",""});
		rewardUnlockableInteractions.put(2, new String[] {"CRAFTING:STONE_STAIRS:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",""});
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {
				"SHAPED:cobblestone_stairs",
				"STONECUTTING:cobblestone_stairs_from_cobblestone_stonecutting"});
		rewardUnlockableRecipe.put(2, new String[] {
				"SHAPED:stone_stairs",
				"STONECUTTING:stone_stairs_from_stone_stonecutting"});
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:HAND:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:WOODEN_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:STONE_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:IRON_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:HAND:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0",
				"BREAKING:WOODEN_PICKAXE:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0",
				"BREAKING:STONE_PICKAXE:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0",
				"BREAKING:IRON_PICKAXE:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0"});
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:HAND:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:WOODEN_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:STONE_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:IRON_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0"});
		rewardSilkTouchDropChance.put(2, new String[] {
				"BREAKING:HAND:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0",
				"BREAKING:WOODEN_PICKAXE:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0",
				"BREAKING:STONE_PICKAXE:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0",
				"BREAKING:IRON_PICKAXE:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:STONE_STAIRS:null:mat=STONE_STAIRS:1:1.0"});
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Bruchsteintreppe/Bruchsteintreppe &#546f42%tt_reward_tech_dropchance_mat,SOLO,stonestairs_I,1,BREAKING,HAND,COBBLESTONE_STAIRS,mat=COBBLESTONE_STAIRS%",
				"&fBehuts. % von &#c6a664Bruchsteintreppe/Bruchsteintreppe &#546f42%tt_reward_tech_dropchance_mat,SOLO,stonestairs_I,1,BREAKING,HAND,COBBLESTONE_STAIRS,mat=COBBLESTONE_STAIRS%",
				"&fHerstellen von &#c6a664Bruchsteintreppe &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stonestairs_I,1,CRAFTING,HAND,COBBLESTONE_STAIRS% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stonestairs_I,1,CRAFTING,HAND,COBBLESTONE_STAIRS% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stonestairs_I,1,CRAFTING,HAND,COBBLESTONE_STAIRS% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Cobblestonestairs/Cobblestonestairs &#546f42%tt_reward_tech_dropchance_mat,SOLO,stonestairs_I,1,BREAKING,HAND,COBBLESTONE_STAIRS,mat=COBBLESTONE_STAIRS%",
				"&fSilkT% of &#c6a664Cobblestonestairs/Cobblestonestairs &#546f42%tt_reward_tech_dropchance_mat,SOLO,stonestairs_I,1,BREAKING,HAND,COBBLESTONE_STAIRS,mat=COBBLESTONE_STAIRS%",
				"&fCrafting of &#c6a664Cobblestonestairs &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stonestairs_I,1,CRAFTING,HAND,COBBLESTONE_STAIRS% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stonestairs_I,1,CRAFTING,HAND,COBBLESTONE_STAIRS% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stonestairs_I,1,CRAFTING,HAND,COBBLESTONE_STAIRS% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});		
		canResLore.put(2, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&f% von &#c6a664Steintreppe/Steintreppe &#546f42%tt_reward_tech_dropchance_mat,SOLO,stonestairs_I,1,BREAKING,HAND,STONE_STAIRS,mat=STONE_STAIRS%",
				"&fBehuts. % von &#c6a664Steintreppe/Steintreppe &#546f42%tt_reward_tech_dropchance_mat,SOLO,stonestairs_I,1,BREAKING,HAND,STONE_STAIRS,mat=STONE_STAIRS%",
				"&fHerstellen von &#c6a664Steintreppe &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stonestairs_I,1,CRAFTING,HAND,STONE_STAIRS% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stonestairs_I,1,CRAFTING,HAND,STONE_STAIRS% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stonestairs_I,1,CRAFTING,HAND,STONE_STAIRS% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&f% of &#c6a664Stonestairs/Stonestairs &#546f42%tt_reward_tech_dropchance_mat,SOLO,stonestairs_I,1,BREAKING,HAND,STONE_STAIRS,mat=STONE_STAIRS%",
				"&fSilkT% of &#c6a664Stonestairs/Stonestairs &#546f42%tt_reward_tech_dropchance_mat,SOLO,stonestairs_I,1,BREAKING,HAND,STONE_STAIRS,mat=STONE_STAIRS%",
				"&fCrafting of &#c6a664Stoneslaps &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,stonestairs_I,1,CRAFTING,HAND,STONE_STAIRS% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,stonestairs_I,1,CRAFTING,HAND,STONE_STAIRS% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,stonestairs_I,1,CRAFTING,HAND,STONE_STAIRS% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"stonestairs_I", new String[] {"Steintreppen_I", "Stonestairs_I"},
				TechnologyType.MULTIPLE, 2, PlayerAssociatedType.SOLO, 0, "", "stonestairs", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1", "else:o_2",
						"output:o_1:true",
						"output:o_2:false",
						"a:hasresearchedtech,stone_I,1:==:true"}, true,
				new String[] {"&8Tech Steintreppen I","&8Tech Stonestairs I"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >&#ff8c00Stein I&c< erforscht haben.",
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von &#c6a664Bruchsteintreppen.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&cRequirements to view:",
						"&cMust have researched the Technology >&#ff8c00Stone I<.",
						"",
						"&eUnlocks the following:",
						"&fCrafting of &#c6a664Cobblestonestairs.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Steintreppen I","&7Stonestairs I"},
				Material.STONE_STAIRS, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Steintreppen I","&7Stonestairs I"},
				Material.STONE_STAIRS, 1, itemflag, null, canResLore,
				new String[] {"&5Steintreppen I","&5Stonestairs I"},
				Material.STONE_STAIRS, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von &#c6a664Bruchsteintreppen &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stonestairs_I,CRAFTING,HAND,COBBLESTONE_STAIRS% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stonestairs_I,CRAFTING,HAND,COBBLESTONE_STAIRS% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stonestairs_I,CRAFTING,HAND,COBBLESTONE_STAIRS% Dollar",
						"&fHerstellen von &#c6a664Steintreppen &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stonestairs_I,CRAFTING,HAND,STONE_STAIRS% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stonestairs_I,CRAFTING,HAND,STONE_STAIRS% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stonestairs_I,CRAFTING,HAND,STONE_STAIRS% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fCrafting of &#c6a664Cobblestonestairs &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stonestairs_I,CRAFTING,HAND,COBBLESTONE_STAIRS% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stonestairs_I,CRAFTING,HAND,COBBLESTONE_STAIRS% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stonestairs_I,CRAFTING,HAND,COBBLESTONE_STAIRS% Dollar",
						"&fCrafting of &#c6a664Stonestairs &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,stonestairs_I,CRAFTING,HAND,STONE_STAIRS% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,stonestairs_I,CRAFTING,HAND,STONE_STAIRS% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,stonestairs_I,CRAFTING,HAND,STONE_STAIRS% Dollar",						
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Tablerecipe_Furnancerecipe(String[] itemflag, String[] enchantment) //INFO:Tablerecipe_Furnancerecipe
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1", "else:o_2",
				"output:o_1:true",
				"output:o_2:false",
				"a:hasresearchedtech,furnace,1:==:true"});
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 25 * solototaltech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 2.5 * solototaltech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.25 * solototaltech");
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[]{"MELTING:STONE:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",""});
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {"FURNACE:stone",""});
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fBrennen von &#c6a664Bruchstein &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,melting_cobblestone,1,CRAFTING,HAND,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,melting_cobblestone,1,CRAFTING,HAND,STONE% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,melting_cobblestone,1,CRAFTING,HAND,STONE% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fMelting of &#c6a664Cobblestone &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,melting_cobblestone,1,CRAFTING,HAND,STONE% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,melting_cobblestone,1,CRAFTING,HAND,STONE% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,melting_cobblestone,1,CRAFTING,HAND,STONE% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});	
		addTechnology(
				"melting_cobblestone", new String[] {"Ofenrezept_Stein", "Furnacerecipe_Stone"},
				TechnologyType.SIMPLE, 1, PlayerAssociatedType.SOLO, 0, "", "furnancerecipe", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Ofenrezept Stein","&8Tech Furnacerecipe Stone"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >&#ff8c00Ofen&c< erforscht haben.",
						"",
						"&eSchaltet folgendes frei:",
						"&fOfenrezept Stein.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&cRequirements to view:",
						"&cMust have researched the Technology >&#ff8c00Furnace<.",
						"",
						"&eUnlocks the following:",
						"&fFurnacerecipe Stone.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Ofenrezept Stein","&7Furnacerecipe Stone"},
				Material.STONE, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&bOfenrezept Stein","&bFurnacerecipe Stone"},
				Material.STONE, 1, itemflag, null, canResLore,
				new String[] {"&5Ofenrezept Stein","&5Furnacerecipe Stone"},
				Material.STONE, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fBrennen von &#c6a664Bruchstein &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,melting_cobblestone,CRAFTING,HAND,STONE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,melting_cobblestone,CRAFTING,HAND,STONE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,melting_cobblestone,CRAFTING,HAND,STONE% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMelting of &#c6a664Cobblestone &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,melting_cobblestone,CRAFTING,HAND,STONE% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,melting_cobblestone,CRAFTING,HAND,STONE% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,melting_cobblestone,CRAFTING,HAND,STONE% Dollar",			
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1", "else:o_2",
				"output:o_1:true",
				"output:o_2:false",
				"a:hasresearchedtech,furnace,1:==:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 25 * solototaltech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 2.5 * solototaltech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.25 * solototaltech");
		toResCostMaterial = new LinkedHashMap<>();
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] 
				{"MELTING:IRON_INGOT:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",""});
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {"FURNACE:iron_ingot_from_smelting_raw_iron",""});
		rewardDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		canResLore = new LinkedHashMap<>();
		canResLore.put(1, new String[] {
				"&eErforschtes Level: &a%acquiredtechlev% &fvon &2%maxtechlev%",
				"",
				"&eKosten:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eSchaltet folgendes frei:",
				"&fBrennen von &#c6a664Roheisen &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,melting_rawiron,1,CRAFTING,HAND,IRON_INGOT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,melting_rawiron,1,CRAFTING,HAND,IRON_INGOT% VExp |"
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,melting_rawiron,1,CRAFTING,HAND,IRON_INGOT% Dollar",
				"",
				"&cRechtskick &bfür eine detailiertere Ansicht.",
				"&eResearched Level: &a%acquiredtechlev% &fof &2%maxtechlev%",
				"",
				"&eCosts:",
				"&f%costttexp% | %costvanillaexp%",
				"&f%costmoney%",
				"&f%costmaterial%",
				"",
				"&eUnlocks the following:",
				"&fMelting of &#c6a664Rawiron &#546f42%tt_raw_reward_tech_ttexp_mat,SOLO,melting_rawiron,1,CRAFTING,HAND,IRON_INGOT% TTExp | "
						  + "&#546f42%tt_raw_reward_tech_vexp_mat,SOLO,melting_rawiron,1,CRAFTING,HAND,IRON_INGOT% VExp | "
						  + "&#546f42%tt_raw_reward_tech_money_mat,SOLO,melting_rawiron,1,CRAFTING,HAND,IRON_INGOT% Dollar",
				"",
				"&cRightclick &bfor a more detailed view."});
		addTechnology(
				"melting_iron", new String[] {"Ofenrezept_Eisenbarren", "Furnacerecipe_Ironingot"},
				TechnologyType.SIMPLE, 1, PlayerAssociatedType.SOLO, 1, "", "furnancerecipe", 
				0, 0, 0, 0, 0, 0, 0, 0,
				null, true,
				new String[] {"&8Tech Ofenrezept Eisenbarren","&8Tech Furnacerecipe Ironingot"},
				Material.BARRIER, 1, itemflag, null, new String[] {
						"",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >&#ff8c00Ofen&c< erforscht haben.",
						"",
						"&eSchaltet folgendes frei:",
						"&fOfenrezept Eisenbarren.",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&cRequirements to view:",
						"&cMust have researched the Technology >&#ff8c00Furnace<.",
						"",
						"&eUnlocks the following:",
						"&fFurnacerecipe Ironingot.",
						"",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Ofenrezept Eisenbarren","&7Furnacerecipe Ironingot"},
				Material.IRON_INGOT, 1, itemflag, null, canResLore.get(1),
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Ofenrezept Eisenbarren","&7Furnacerecipe Ironingot"},
				Material.IRON_INGOT, 1, itemflag, null, canResLore,
				new String[] {"&bOfenrezept Eisenbarren","&bFurnacerecipe Ironingot"},
				Material.IRON_INGOT, 1, itemflag, enchantment, new String[] {
						"",
						"&eSchaltet folgendes frei:",
						"&fBrennen von &#c6a664Roheisen &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,melting_rawiron,CRAFTING,HAND,IRON_INGOT% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,melting_rawiron,CRAFTING,HAND,IRON_INGOT% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,melting_rawiron,CRAFTING,HAND,IRON_INGOT% Dollar",
						"",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"",
						"&eUnlocks the following:",
						"&fMelting of &#c6a664Rawiron &#546f42%tt_raw_reward_techtotal_ttexp_mat,SOLO,melting_rawiron,CRAFTING,HAND,IRON_INGOT% TTExp | "
								  + "&#546f42%tt_raw_reward_techtotal_vexp_mat,SOLO,melting_rawiron,CRAFTING,HAND,IRON_INGOT% VExp | "
								  + "&#546f42%tt_raw_reward_techtotal_money_mat,SOLO,melting_rawiron,CRAFTING,HAND,IRON_INGOT% Dollar",			
						"",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}

	private void tech_Tablerecipe_Enchantmentrecipe(String[] itemflag, String[] enchantment) //INFO:Tablerecipe_Enchantmentrecipe
	{
		
	}

	private void tech_Tablerecipe_Brewingrecipe(String[] itemflag, String[] enchantment) //INFO:Tablerecipe_Brewingrecipe
	{
		
	}

	private void tech_Tablerecipe_Anvilrecipe(String[] itemflag, String[] enchantment) //INFO:ablerecipe_Anvilrecipe
	{
		
	}
	
	private void tech_Tablerecipe_Forgingcerecipe(String[] itemflag, String[] enchantment) //INFO:Tablerecipe_Forgingcerecipe
	{
		
	}
	
	private void tech_Booster_Miningbooster(String[] itemflag, String[] enchantment) //INFO:Booster_Miningbooster
	{
		
	}
	
	private void tech_Booster_Craftingbooster(String[] itemflag, String[] enchantment) //INFO:Booster_Craftingbooster
	{
		
	}
	
	public void addTechnology(
			String key, String[] displayname, TechnologyType techType, int maxTechLevToResearch,
			PlayerAssociatedType pat, int guiSlot, String ifBoosterDurUntilExp, String overlyingSubCategory,
			double fUPP_RUInteractionsIP, double fUPP_RRecipesIP, double fUPP_RDropChancesIP, double fUPP_RSilkTouchDropChancesIP,
			double fUPP_RCommandsIP, double fUPP_RItemsIP, double fUPP_RModifiersInPercent, double fUPP_RValueEntryIP,
			String[] toSeeConditionQuery, boolean showDifferentItemIfYouNormallyDontSeeIt,
			String[] notSeeDisplayname, Material notSeeMat, int notSeeAmount, String[] notSeeItemFlag, String[] notSeeEnchantments, String[] notSeeLore,
			String[] canSeeDisplayname, Material canSeeMat, int canSeeAmount, String[] canSeeItemFlag, String[] canSeeEnchantments, String[] canSeeLore,
			LinkedHashMap<Integer, String[]> toResConditionQuery,
			LinkedHashMap<Integer, String> toResCostTTExp,
			LinkedHashMap<Integer, String> toResCostVanillaExp,
			LinkedHashMap<Integer, String> toResCostMoney,
			LinkedHashMap<Integer, String[]> toResCostMaterial,
			String[] canResDisplayname, Material canResMat, int canResAmount, String[] canResItemFlag, String[] canResEnchantments, LinkedHashMap<Integer, String[]> canResLore,
			String[] hadResDisplayname, Material hadResMat, int hadResAmount, String[] hadResItemFlag, String[] hadResEnchantments, String[] hadResLore,
			LinkedHashMap<Integer, String[]> rewardUnlockableInteractions, LinkedHashMap<Integer, String[]> rewardUnlockableRecipe,
			LinkedHashMap<Integer, String[]> rewardDropChance, LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance,
			LinkedHashMap<Integer, String[]> rewardCommand, LinkedHashMap<Integer, String[]> rewardItem, 
			LinkedHashMap<Integer, String[]> rewardModifier, LinkedHashMap<Integer, String[]> rewardValueEntry) //INFO:Technology
	{
		LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		one.put("Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						displayname));
		one.put("TechnologyType", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						techType.toString()}));
		one.put("MaximalTechnologyLevelToResearch", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						maxTechLevToResearch}));
		one.put("PlayerAssociatedType", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						pat.toString()}));
		one.put("GuiSlot", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						guiSlot}));
		if(techType == TechnologyType.BOOSTER)
		{
			one.put("IfBoosterDurationUntilExpiration", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					ifBoosterDurUntilExp}));
		}
		one.put("OverlyingSubCategory", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						overlyingSubCategory}));
		if(pat == PlayerAssociatedType.GLOBAL)
		{
			one.put("OnlyForGlobal.ForUninvolvedPollParticipants.RewardUnlockableInteractionsInPercent",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					fUPP_RUInteractionsIP}));
			one.put("OnlyForGlobal.ForUninvolvedPollParticipants.RewardRecipesInPercent",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					fUPP_RRecipesIP}));
			one.put("OnlyForGlobal.ForUninvolvedPollParticipants.RewardDropChancesInPercent",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					fUPP_RDropChancesIP}));
			one.put("OnlyForGlobal.ForUninvolvedPollParticipants.RewardSilkTouchDropChancesInPercent",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					fUPP_RSilkTouchDropChancesIP}));
			one.put("OnlyForGlobal.ForUninvolvedPollParticipants.RewardCommandsInPercent",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					fUPP_RCommandsIP}));
			one.put("OnlyForGlobal.ForUninvolvedPollParticipants.RewardItemsInPercent",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					fUPP_RItemsIP}));
			one.put("OnlyForGlobal.ForUninvolvedPollParticipants.RewardModifiersInPercent",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					fUPP_RModifiersInPercent}));
			one.put("OnlyForGlobal.ForUninvolvedPollParticipants.RewardValueEntryInPercent",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					fUPP_RValueEntryIP}));
		}
		if(toSeeConditionQuery != null)
		{
			one.put("RequirementToSee.ConditionQuery", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					toSeeConditionQuery));
		}
		one.put("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						showDifferentItemIfYouNormallyDontSeeIt}));
		//--- ToSee - ItemIfYouCannotSee ---
		one.put("RequirementToSee.ItemIfYouCannotSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						notSeeDisplayname));
		one.put("RequirementToSee.ItemIfYouCannotSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeMat.toString()}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeAmount}));
		if(notSeeItemFlag != null)
		{
			one.put("RequirementToSee.ItemIfYouCannotSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					notSeeItemFlag));
		}
		if(notSeeEnchantments != null)
		{
			one.put("RequirementToSee.ItemIfYouCannotSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					notSeeEnchantments));
		}
		one.put("RequirementToSee.ItemIfYouCannotSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						notSeeLore));
		//--- ToSee - ItemIfYouCanSee ---
		one.put("RequirementToSee.ItemIfYouCanSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						canSeeDisplayname));
		one.put("RequirementToSee.ItemIfYouCanSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeMat.toString()}));
		one.put("RequirementToSee.ItemIfYouCanSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeAmount}));
		if(canSeeItemFlag != null)
		{
			one.put("RequirementToSee.ItemIfYouCanSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					canSeeItemFlag));
		}
		if(canSeeEnchantments != null)
		{
			one.put("RequirementToSee.ItemIfYouCanSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					canSeeEnchantments));
		}
		one.put("RequirementToSee.ItemIfYouCanSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						canSeeLore));
		
		for(Entry<Integer, String[]> e : toResConditionQuery.entrySet())
		{
			if(e.getValue() != null)
			{
				one.put("RequirementToResearch.ConditionQuery."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						e.getValue()));
			}
		}
		
		for(Entry<Integer, String> e : toResCostTTExp.entrySet())
		{
			if(e.getValue() != null)
			{
				one.put("RequirementToResearch.Costs.TTExp."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						e.getValue()}));
			}
		}
		for(Entry<Integer, String> e : toResCostVanillaExp.entrySet())
		{
			if(e.getValue() != null)
			{
				one.put("RequirementToResearch.Costs.VanillaExp."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						e.getValue()}));
			}
		}
		for(Entry<Integer, String> e : toResCostMoney.entrySet())
		{
			if(e.getValue() != null)
			{
				one.put("RequirementToResearch.Costs.Money."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						e.getValue()}));
			}
		}
		for(Entry<Integer, String[]> e : toResCostMaterial.entrySet())
		{
			if(e.getValue() != null)
			{
				one.put("RequirementToResearch.Costs.Material."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						e.getValue()));
			}
		}
		//--- ToResearch - IfYouCanResearchIt ---
		for(int i = 1; i <= maxTechLevToResearch; i++)
		{
			one.put("RequirementToResearch.IfYouCanResearchIt."+i+".Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
					canResDisplayname));
			one.put("RequirementToResearch.IfYouCanResearchIt."+i+".Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					canResMat.toString()}));
			one.put("RequirementToResearch.IfYouCanResearchIt."+i+".Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					canResAmount}));
			if(canResItemFlag != null)
			{
				one.put("RequirementToResearch.IfYouCanResearchIt."+i+".ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						canResItemFlag));
			}
			if(canResEnchantments != null)
			{
				one.put("RequirementToResearch.IfYouCanResearchIt."+i+".Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						canResEnchantments));
			}
			if(canResLore.get(i) != null)
			{
				one.put("RequirementToResearch.IfYouCanResearchIt."+i+".Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						canResLore.get(i)));
			}
		}
		//--- ToResearch - ItemIfYouHaveResearchedIt ---
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						hadResDisplayname));
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						hadResMat.toString()}));
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						hadResAmount}));
		if(hadResItemFlag != null)
		{
			one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					hadResItemFlag));
		}
		if(hadResEnchantments != null)
		{
			one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					hadResEnchantments));
		}
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						hadResLore));
		
		for(Entry<Integer, String[]> e : rewardUnlockableInteractions.entrySet())
		{
			if(e.getValue() != null)
			{
				one.put("Rewards.UnlockableInteractions."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
						e.getValue()));
			}
		}
		for(Entry<Integer, String[]> e : rewardUnlockableRecipe.entrySet())
		{
			if(e.getValue() != null)
			{
				one.put("Rewards.UnlockableRecipe."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
						e.getValue()));
			}			
		}
		for(Entry<Integer, String[]> e : rewardDropChance.entrySet())
		{
			if(e.getValue() != null)
			{
				one.put("Rewards.DropChance."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
						e.getValue()));
			}
		}
		for(Entry<Integer, String[]> e : rewardSilkTouchDropChance.entrySet())
		{
			if(e.getValue() != null)
			{
				one.put("Rewards.SilkTouchDropChance."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
						e.getValue()));
			}			
		}
		for(Entry<Integer, String[]> e : rewardCommand.entrySet())
		{
			if(e.getValue() != null)
			{
				one.put("Rewards.Command."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
						e.getValue()));
			}
		}
		for(Entry<Integer, String[]> e : rewardItem.entrySet())
		{
			if(e.getValue() != null)
			{
				one.put("Rewards.Item."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
						e.getValue()));
			}
		}
		for(Entry<Integer, String[]> e : rewardModifier.entrySet())
		{
			if(e.getValue() != null)
			{
				one.put("Rewards.Modifier."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
						e.getValue()));
			}
		}
		for(Entry<Integer, String[]> e : rewardValueEntry.entrySet())
		{
			if(e.getValue() != null)
			{
				one.put("Rewards.ValueEntry."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
						e.getValue()));
			}
		}
		LinkedHashMap<String, LinkedHashMap<String, Language>> map = new LinkedHashMap<>();
		if(technologyKeys.containsKey(pat))
		{
			map = technologyKeys.get(pat);
		}
		map.put(key, one);
		technologyKeys.put(pat, map);
	}
	
	public void initRecipe()//INFO:Recipe
	{
		for(Iterator<Recipe> iterator = Bukkit.recipeIterator(); iterator.hasNext();) 
		{
		    Recipe r = iterator.next();
		    if(r instanceof BlastingRecipe)
		    {
		    	BlastingRecipe a = (BlastingRecipe) r;
		    	String onekey = a.getKey().getKey();
		    	LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		    	one.put("Category",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getCategory().toString()}));
		    	one.put("CookingTime",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getCookingTime()}));
		    	one.put("Experience",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getExperience()}));
		    	one.put("Group",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getGroup()}));
		    	one.put("Input.Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getInput().getType().toString()}));
		    	String path = "Result.";
		    	one.put(path+"Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getType().toString()}));
		    	one.put(path+"Amount",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getAmount()}));
		    	if(a.getResult().hasItemMeta())
		    	{
		    		ItemMeta im = a.getResult().getItemMeta();
		    		if(im.hasCustomModelData())
		    		{
		    			one.put(path+"CustomModelData",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getCustomModelData()}));
		    		}
			    	if(im.hasDisplayName())
			    	{
			    		one.put(path+"Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getDisplayName()}));
			    	}
		    	}
				blastingRecipeKeys.put(onekey, one);
		    } else if(r instanceof CampfireRecipe)
		    {
		    	CampfireRecipe a = (CampfireRecipe) r;
		    	String onekey = a.getKey().getKey();
		    	LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		    	one.put("Category",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getCategory().toString()}));
		    	one.put("CookingTime",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getCookingTime()}));
		    	one.put("Experience",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getExperience()}));
		    	one.put("Group",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getGroup()}));
		    	one.put("Input.Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getInput().getType().toString()}));
		    	String path = "Result.";
		    	one.put(path+"Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getType().toString()}));
		    	one.put(path+"Amount",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getAmount()}));
		    	if(a.getResult().hasItemMeta())
		    	{
		    		ItemMeta im = a.getResult().getItemMeta();
		    		if(im.hasCustomModelData())
		    		{
		    			one.put(path+"CustomModelData",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getCustomModelData()}));
		    		}
			    	if(im.hasDisplayName())
			    	{
			    		one.put(path+"Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getDisplayName()}));
			    	}
		    	}
				campfireRecipeKeys.put(onekey, one);
		    } else if(r instanceof FurnaceRecipe)
		    {
		    	FurnaceRecipe a = (FurnaceRecipe) r;
		    	String onekey = a.getKey().getKey();
		    	LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		    	one.put("Category",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getCategory().toString()}));
		    	one.put("CookingTime",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getCookingTime()}));
		    	one.put("Experience",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getExperience()}));
		    	one.put("Group",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getGroup()}));
		    	one.put("Input.Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getInput().getType().toString()}));
		    	String path = "Result.";
		    	one.put(path+"Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getType().toString()}));
		    	one.put(path+"Amount",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getAmount()}));
		    	if(a.getResult().hasItemMeta())
		    	{
		    		ItemMeta im = a.getResult().getItemMeta();
		    		if(im.hasCustomModelData())
		    		{
		    			one.put(path+"CustomModelData",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getCustomModelData()}));
		    		}
			    	if(im.hasDisplayName())
			    	{
			    		one.put(path+"Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getDisplayName()}));
			    	}
		    	}
				furnaceRecipeKeys.put(onekey, one);
		    } else if(r instanceof MerchantRecipe) //https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/inventory/MerchantRecipe.html
		    {
		    	RecipeHandler.toSaveRecipe.add(r);
		    	/*MerchantRecipe a = (MerchantRecipe) r;
		    	String onekey = "m"+m;
		    	LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		    	one.put("MaxUses",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getMaxUses()}));
		    	one.put("ExperienceReward",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.hasExperienceReward()}));
		    	one.put("VillagerExperience",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getVillagerExperience()}));
		    	one.put("PriceMultiplier",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getPriceMultiplier()}));
		    	one.put("Demand",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getDemand()}));
		    	one.put("SpecialPrice",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getSpecialPrice()}));
		    	String path = "Ingredient.";
		    	int i = 1;
		    	for(ItemStack ing : a.getIngredients())
		    	{
		    		one.put(path+i+".Material",
							new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
									ing.getType().toString()}));
			    	one.put(path+i+".Amount",
							new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
									ing.getAmount()}));
			    	if(ing.hasItemMeta())
			    	{
			    		ItemMeta im = ing.getItemMeta();
			    		if(im.hasCustomModelData())
			    		{
			    			one.put(path+i+".CustomModelData",
									new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
											im.getCustomModelData()}));
			    		}
				    	if(im.hasDisplayName())
				    	{
				    		one.put(path+i+".Displayname",
									new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
											im.getDisplayName()}));
				    	}
			    	}
			    	i++;
		    	}
		    	path = "Result.";
		    	one.put(path+"Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getType().toString()}));
		    	one.put(path+"Amount",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getAmount()}));
		    	if(a.getResult().hasItemMeta())
		    	{
		    		ItemMeta im = a.getResult().getItemMeta();
		    		if(im.hasCustomModelData())
		    		{
		    			one.put(path+"CustomModelData",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getCustomModelData()}));
		    		}
			    	if(im.hasDisplayName())
			    	{
			    		one.put(path+"Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getDisplayName()}));
			    	}
		    	}
		    	merchantRecipeKeys.put(onekey, one);
		    	m++;*/
		    } else if(r instanceof ShapedRecipe)
		    {
		    	ShapedRecipe a = (ShapedRecipe) r;
		    	String onekey = a.getKey().getKey();
		    	LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		    	one.put("Category",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getCategory().toString()}));
		    	one.put("Group",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getGroup()}));
		    	if(a.getShape().length >= 1)
		    	{
		    		one.put("Shape.Line1",
							new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
									a.getShape()[0]}));
		    	}
		    	if(a.getShape().length >= 2)
		    	{
		    		one.put("Shape.Line2",
							new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
									a.getShape()[1]}));
		    	}
		    	if(a.getShape().length >= 3)
		    	{
		    		one.put("Shape.Line3",
							new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
									a.getShape()[2]}));
		    	}
		    	StringBuilder sb = new StringBuilder();
		    	for(Entry<Character, RecipeChoice> entry : a.getChoiceMap().entrySet())
		    	{
		    		sb.append(String.valueOf(entry.getKey()));
		    		String c = String.valueOf(entry.getKey());
		    		String path = "Ingredient."+c+".";
		    		ItemStack is = null;
		    		if(entry.getValue() instanceof RecipeChoice.ExactChoice)
		    		{
		    			path = path + "ExactChoice.";
		    			RecipeChoice.ExactChoice rcec = (ExactChoice) entry.getValue();
		    			if(rcec != null)
		    			{
		    				for(int i = 0; i < rcec.getChoices().size(); i++)
		    				{
		    					ItemStack iss = rcec.getChoices().get(i);
		    					if(iss == null)
			    				{
		    						is = new ItemStack(Material.AIR, 1);
			    				}
		    					one.put(path+i+".Material",
										new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
												is.getType().toString()}));
		    					if(a.getResult().hasItemMeta())
						    	{
						    		ItemMeta im = is.getItemMeta();
						    		if(im.hasCustomModelData())
						    		{
						    			one.put(path+i+".CustomModelData",
												new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
														im.getCustomModelData()}));
						    		}
							    	if(im.hasDisplayName())
							    	{
							    		one.put(path+i+".Displayname",
												new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
														im.getDisplayName()}));
							    	}
						    	}
		    				}
		    			}
		    		} else
		    		{
		    			RecipeChoice.MaterialChoice rcmc = (MaterialChoice) entry.getValue();
		    			if(rcmc != null)
		    			{
		    				ArrayList<String> als = new ArrayList<>();
		    				for(Material mat : rcmc.getChoices())
		    				{
		    					if(mat == null)
		    					{
		    						als.add(Material.AIR.toString());
		    					} else
		    					{
		    						als.add(mat.toString());
		    					}
		    				}
		    				one.put(path+"MaterialChoice",
									new Language(new ISO639_2B[] {ISO639_2B.GER}, 
											als.toArray(new String[als.size()])));
		    			}
		    		}
		    	}
		    	one.put("Ingredient.CharacterList",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								sb.toString()}));
		    	String path = "Result.";
	    		ItemStack is = a.getResult();
		    	one.put(path+"Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								is.getType().toString()}));
		    	one.put(path+"Amount",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								is.getAmount()}));
		    	if(a.getResult().hasItemMeta())
		    	{
		    		ItemMeta im = is.getItemMeta();
		    		if(im.hasCustomModelData())
		    		{
		    			one.put(path+"CustomModelData",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getCustomModelData()}));
		    		}
			    	if(im.hasDisplayName())
			    	{
			    		one.put(path+"Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getDisplayName()}));
			    	}
		    	}
		    	shapedRecipeKeys.put(onekey, one);
		    } else if(r instanceof ShapelessRecipe)
		    {
		    	ShapelessRecipe a = (ShapelessRecipe) r;
		    	String onekey = a.getKey().getKey();
		    	LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		    	one.put("Category",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getCategory().toString()}));
		    	one.put("Group",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getGroup()}));
		    	one.put("Ingredient.ListSize",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getIngredientList().size()}));
		    	for(int i = 0; i < a.getIngredientList().size(); i++)
		    	{
		    		
		    		String c = String.valueOf(i);
		    		String path = "Ingredient."+c+".";
		    		ItemStack is = a.getIngredientList().get(i);
			    	one.put(path+"Material",
							new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
									is.getType().toString()}));
			    	one.put(path+"Amount",
							new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
									is.getAmount()}));
			    	if(a.getResult().hasItemMeta())
			    	{
			    		ItemMeta im = is.getItemMeta();
			    		if(im.hasCustomModelData())
			    		{
			    			one.put(path+"CustomModelData",
									new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
											im.getCustomModelData()}));
			    		}
				    	if(im.hasDisplayName())
				    	{
				    		one.put(path+"Displayname",
									new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
											im.getDisplayName()}));
				    	}
			    	}
		    	}
		    	String path = "Result.";
	    		ItemStack is = a.getResult();
		    	one.put(path+"Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								is.getType().toString()}));
		    	one.put(path+"Amount",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								is.getAmount()}));
		    	if(a.getResult().hasItemMeta())
		    	{
		    		ItemMeta im = is.getItemMeta();
		    		if(im.hasCustomModelData())
		    		{
		    			one.put(path+"CustomModelData",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getCustomModelData()}));
		    		}
			    	if(im.hasDisplayName())
			    	{
			    		one.put(path+"Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getDisplayName()}));
			    	}
		    	}
		    	shapelessRecipeKeys.put(onekey, one);
		    } else if(r instanceof SmithingTransformRecipe)
		    {
		    	SmithingTransformRecipe a = (SmithingTransformRecipe) r;
		    	String onekey = a.getKey().getKey();
		    	LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		    	String path = "Base.";
		    	RecipeChoice.MaterialChoice rcecb = (MaterialChoice) a.getBase();
		    	ArrayList<String> alb = new ArrayList<>();
		    	int i = 0;
		    	for(Material mat : rcecb.getChoices())
		    	{
		    		alb.add(mat.toString());
		    	}
		    	one.put(path+i,
						new Language(new ISO639_2B[] {ISO639_2B.GER},
								alb.toArray(new String[alb.size()])));
		    	path = "Addition.";
		    	ArrayList<String> ala = new ArrayList<>();
		    	i = 0;
		    	RecipeChoice.MaterialChoice rceca = (MaterialChoice) a.getAddition();
		    	for(Material mat : rceca.getChoices())
		    	{
		    		ala.add(mat.toString());
		    	}
		    	one.put(path+i,
						new Language(new ISO639_2B[] {ISO639_2B.GER},
								ala.toArray(new String[ala.size()])));
		    	path = "Template.";
		    	ArrayList<String> alt = new ArrayList<>();
		    	i = 0;
		    	RecipeChoice.MaterialChoice rcect = (MaterialChoice) a.getAddition();
		    	for(Material mat : rcect.getChoices())
		    	{
		    		alt.add(mat.toString());
		    	}
		    	one.put(path+i,
						new Language(new ISO639_2B[] {ISO639_2B.GER},
								alt.toArray(new String[alt.size()])));
		    	path = "Result.";
		    	one.put(path+"Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getType().toString()}));
		    	one.put(path+"Amount",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getAmount()}));
		    	if(a.getResult().hasItemMeta())
		    	{
		    		ItemMeta im = a.getResult().getItemMeta();
		    		if(im.hasCustomModelData())
		    		{
		    			one.put(path+"CustomModelData",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getCustomModelData()}));
		    		}
			    	if(im.hasDisplayName())
			    	{
			    		one.put(path+"Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getDisplayName()}));
			    	}
			    	if(im instanceof ArmorMeta)
			    	{
			    		ArmorMeta am = (ArmorMeta) im;
			    		if(am.hasTrim())
			    		{
			    			one.put(path+"ArmorMeta.TrimMaterial",
									new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
											am.getTrim().getMaterial().getKey().getKey()}));
				    		one.put(path+"ArmorMeta.TrimPattern",
									new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
											am.getTrim().getPattern().getKey().getKey()}));
			    		}
			    	}
		    	}
				smithingTransformRecipeKeys.put(onekey, one);
		    } else if(r instanceof SmithingTrimRecipe)
		    {
		    	RecipeHandler.toSaveRecipe.add(r);
		    	//Diese Rezepte sind für die Config nicht wirklich machbar.
		    	/*SmithingTrimRecipe a = (SmithingTrimRecipe) r;
		    	String onekey = a.getKey().getKey();
		    	LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		    	String path = "Base.";
		    	RecipeChoice.MaterialChoice rcecb = (MaterialChoice) a.getBase();
		    	one.put(path+"Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								rcecb.getItemStack().getType().toString()}));
		    	one.put(path+"Amount",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								rcecb.getItemStack().getAmount()}));
		    	if(rcecb.getItemStack().hasItemMeta())
		    	{
		    		ItemMeta im = rcecb.getItemStack().getItemMeta();
		    		if(im.hasCustomModelData())
		    		{
		    			one.put(path+"CustomModelData",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getCustomModelData()}));
		    		}
			    	if(im.hasDisplayName())
			    	{
			    		one.put(path+"Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getDisplayName()}));
			    	}
		    	}
		    	path = "Addition.";
		    	RecipeChoice.MaterialChoice rceca = (MaterialChoice) a.getAddition();
		    	one.put(path+"Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								rceca.getItemStack().getType().toString()}));
		    	one.put(path+"Amount",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								rceca.getItemStack().getAmount()}));
		    	if(rceca.getItemStack().hasItemMeta())
		    	{
		    		ItemMeta im = rceca.getItemStack().getItemMeta();
		    		if(im.hasCustomModelData())
		    		{
		    			one.put(path+"CustomModelData",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getCustomModelData()}));
		    		}
			    	if(im.hasDisplayName())
			    	{
			    		one.put(path+"Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getDisplayName()}));
			    	}
		    	}
				smithingTrimRecipeKeys.put(onekey, one);*/
		    } else if(r instanceof SmokingRecipe)
		    {
		    	SmokingRecipe a = (SmokingRecipe) r;
		    	String onekey = a.getKey().getKey();
		    	LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		    	one.put("Category",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getCategory().toString()}));
		    	one.put("CookingTime",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getCookingTime()}));
		    	one.put("Experience",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getExperience()}));
		    	one.put("Group",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getGroup()}));
		    	one.put("Input.Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getInput().getType().toString()}));
		    	String path = "Result.";
		    	one.put(path+"Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getType().toString()}));
		    	one.put(path+"Amount",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getAmount()}));
		    	if(a.getResult().hasItemMeta())
		    	{
		    		ItemMeta im = a.getResult().getItemMeta();
		    		if(im.hasCustomModelData())
		    		{
		    			one.put(path+"CustomModelData",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getCustomModelData()}));
		    		}
			    	if(im.hasDisplayName())
			    	{
			    		one.put(path+"Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getDisplayName()}));
			    	}
		    	}
				smokingRecipeKeys.put(onekey, one);
		    } else if(r instanceof StonecuttingRecipe)
		    {
		    	StonecuttingRecipe a = (StonecuttingRecipe) r;
		    	String onekey = a.getKey().getKey();
		    	LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		    	one.put("Group",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getGroup()}));
		    	one.put("Input.Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getInput().getType().toString()}));
		    	String path = "Result.";
		    	one.put(path+"Material",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getType().toString()}));
		    	one.put(path+"Amount",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getResult().getAmount()}));
		    	if(a.getResult().hasItemMeta())
		    	{
		    		ItemMeta im = a.getResult().getItemMeta();
		    		if(im.hasCustomModelData())
		    		{
		    			one.put(path+"CustomModelData",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getCustomModelData()}));
		    		}
			    	if(im.hasDisplayName())
			    	{
			    		one.put(path+"Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
										im.getDisplayName()}));
			    	}
		    	}
				stonecuttingRecipeKeys.put(onekey, one);
		    } else
		    {
		    	RecipeHandler.toSaveRecipe.add(r);
		    }
		    /* else if(r instanceof CraftComplexRecipe)
		    {
		    	CraftComplexRecipe ccr = (CraftComplexRecipe) r;
		    	TT.log.info("Cannot be define: "+ccr.getKey().getKey());
		    }*/
		}
	}
}