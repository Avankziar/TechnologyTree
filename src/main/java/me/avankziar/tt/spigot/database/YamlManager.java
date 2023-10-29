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
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import main.java.me.avankziar.tt.spigot.database.Language.ISO639_2B;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickFunctionType;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickType;
import main.java.me.avankziar.tt.spigot.gui.objects.GuiType;
import main.java.me.avankziar.tt.spigot.gui.objects.SettingsLevel;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
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
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> mainCategoryKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> subCategoryKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> technologyKeys = new LinkedHashMap<>();
	
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> blastingRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> campfireRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> furnaceRecipeKeys = new LinkedHashMap<>();
	//private static LinkedHashMap<String, LinkedHashMap<String, Language>> merchantRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> shapedRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> shapelessRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> smithingRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> smokingRecipeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> stonecuttingRecipeKeys = new LinkedHashMap<>();
	
	public YamlManager()
	{
		initConfig();
		initCommands();
		initLanguage();
		initModifierValueEntryLanguage();
		initGuiStart();
		initGuiMainCat();
		initGuiSubCat();
		initGuiTechnology();
		initItemGenerator();
		initMainCategory();
		initSubCategory();
		initTechnology();
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
		return mainCategoryKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getMainCategoryKey()
	{
		return mainCategoryKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getSubCategoryKey()
	{
		return subCategoryKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getTechnologyKey()
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
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getSmithingRecipeKey()
	{
		return smithingRecipeKeys;
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
		configSpigotKeys.put("Do.DeleteExpireTechnologies.TaskRunInMinutes"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				10}));
		configSpigotKeys.put("Do.NewPlayer.ShowSyncMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.NewPlayer.AutoResearchTechnology"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dummyOne",
				"dummytwo"}));
		configSpigotKeys.put("Do.Block.OverrideAlreadyRegisteredBlocks"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Do.Drops.UsePluginDropsCalculation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.Drops.DoNotUsePluginDropsCalculationWorlds"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hub",
				"spawncity"}));
		configSpigotKeys.put("Do.Item.LoseDropItemOwnershipAfterTimeInSeconds"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				300}));
		configSpigotKeys.put("Do.Drops.BreakingThroughVanillaDropBarrier"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
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
		configSpigotKeys.put("Do.Reward.Placing.IfBlockIsManuallyPlacedBefore_RewardItByBreaking"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.Reward.Brewing.FinishBrewIfPlayerHasNotTheRecipeUnlocked"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Do.Reward.Placing.UseMetaDataToTrackPlayerPlacedBlocks"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
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
		commandsInput("path", "base", "perm.command.perm", 
				"/base [pagenumber]", "/base ", false,
				"&c/base &f| Infoseite für alle Befehle.",
				"&c/base &f| Info page for all commands.",
				"&bBefehlsrecht für &f/base",
				"&bCommandright for &f/base",
				"&eBasisbefehl für das BaseTemplate Plugin.",
				"&eGroundcommand for the BaseTemplate Plugin.");
		String basePermission = "perm.base.";
		argumentInput("base_argument", "argument", basePermission,
				"/base argument <id>", "/econ deletelog ", false,
				"&c/base argument &f| Ein Subbefehl",
				"&c/base argument &f| A Subcommand.",
				"&bBefehlsrecht für &f/base argument",
				"&bCommandright for &f/base argument",
				"&eBasisbefehl für das BaseTemplate Plugin.",
				"&eGroundcommand for the BaseTemplate Plugin.");
	}
	
	private void comBypass() //INFO:ComBypass
	{
		List<Bypass.Permission> list = new ArrayList<Bypass.Permission>(EnumSet.allOf(Bypass.Permission.class));
		for(Bypass.Permission ept : list)
		{
			commandsKeys.put("Bypass."+ept.toString().replace("_", ".")
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"base."+ept.toString().toLowerCase().replace("_", ".")}));
		}
		
		List<Bypass.Counter> list2 = new ArrayList<Bypass.Counter>(EnumSet.allOf(Bypass.Counter.class));
		for(Bypass.Counter ept : list2)
		{
			if(!ept.forPermission())
			{
				continue;
			}
			commandsKeys.put("Count."+ept.toString().replace("_", ".")
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"base."+ept.toString().toLowerCase().replace("_", ".")}));
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
						"&e=====&7[&6BungeeTeleportManager&7]&e=====",
						"&e=====&7[&6BungeeTeleportManager&7]&e====="}));
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
		initEnumHandlerLang();
		initPlayerHandlerLang();
		initBlockHandlerLang();
		initGuiHandlerLang();
	}
	
	public void initEnumHandlerLang() //INFO:EnumHandlerLang
	{
		String path = "EnumHandler.";
	}
	
	private void initPlayerHandlerLang() //INFO:PlayerHandlerLang
	{
		String path = "PlayerHandler.";
		languageKeys.put(path+"SyncStart", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&eTT&7] &bSync ist gestartet...",
						"&7[&eTT&7] &bSync started..."}));
		languageKeys.put(path+"SyncEnd", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&eTT&7] &bSync ist komplett!",
						"&7[&eTT&7] &bSync is complete!"}));
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
						"&bTT Gui",
						"&bTT Gui"}));
		languageKeys.put(path+"MainCategorys.Title", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bHauptkategorien",
						"&bMain Categorys"}));
		languageKeys.put(path+"MainCategorysSubCategorys.Title", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b%maincat% Subkategorien",
						"&b%maincat% Sub Categorys"}));
		languageKeys.put(path+"SubCategorysTechnologys.Title", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b%subcat% Technologien",
						"&b%subcat% Techologys"}));
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
		mvelanguageKeys.put(Bypass.Counter.REGISTER_BLOCK.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZählpermission für die Registrierung von Blöcken.",
						"&eCounting mission for the registration of blocks."}));
		mvelanguageKeys.put(Bypass.Counter.REGISTER_BLOCK.toString()+".Explanation",
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
						"&cDeine eingesetzte TTExp: &f%allocatedttexp%",
						
						"&cYour free TTExp: &f%freettexp%",
						"&cYour allocated TTExp: &f%allocatedttexp%"
						}));
		start.put(path+".Lore."+SettingsLevel.EXPERT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine freie TTExp: &f%freettexp%",
						"&cDeine eingesetzte TTExp: &f%allocatedttexp%",
						"",
						"&eRegistrierte Blöcke => &ahat man&7/&ckann man haben",
						"&cBraustand: &a%brewing_standfree%&7/&c%brewing_standmax%",
						"&cVerz. Tisch: &a%enchanting_tablefree%&7/&c%enchanting_tablemax%",
						"&cLagerfeuer: &a%campfirefree%&7/&c%campfiremax%",
						"&cOfen: &a%furnacefree%&7/&c%furnacemax%",
						"&cSchmelzofen: &a%blast_furnacefree%&7/&c%blast_furnacemax%",
						"&cRäucherofen: &a%smokerfree%&7/&c%smokermax%",
						
						"&cYour free TTExp: &f%freettexp%",
						"&cYour allocated TTExp: &f%allocatedttexp%",
						"",
						"&eRegistrated Blocks => &ahave&7/&ccan have",
						"&cBrewing Stand: &a%brewing_standfree%&7/&c%brewing_standmax%",
						"&cEnchanting Table: &a%enchanting_tablefree%&7/&c%enchanting_tablemax%",
						"&cCampfire: &a%campfirefree%&7/&c%campfiremax%",
						"&cFurnance: &a%furnacefree%&7/&c%furnacemax%",
						"&cBlastfurnace: &a%blast_furnacefree%&7/&c%blast_furnacemax%",
						"&cSmoker: &a%smokerfree%&7/&c%smokermax%"
						}));
		start.put(path+".Lore."+SettingsLevel.MASTER.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine freie TTExp: &f%freettexp%",
						"&cDeine eingesetzte TTExp: &f%allocatedttexp%",
						"",
						"&eTechnologie => &ahat man&f/&7sieht man&f/&cexistieren",
						"&cTotal Techs: &a%techhave%&f/&7%techsee%&f/&c%techexist%",
						"&cSolo Techs: &a%solotechhave%&f/&7%solotechsee%&f/&c%solotechexist%",
						"&cGruppen Techs: &a%grouptechhave%&f/&7%grouptechsee%&f/&c%grouptechexist%",
						"&cGlobale Techs: &a%globaltechhave%&f/&7%globaltechsee%&f/&c%globaltechexist%",
						"",
						"&eRegistrierte Blöcke => &ahat man&7/&ckann man haben",
						"&cBraustand: &a%brewing_standfree%&7/&c%brewing_standmax%",
						"&cVerz. Tisch: &a%enchanting_tablefree%&7/&c%enchanting_tablemax%",
						"&cLagerfeuer: &a%campfirefree%&7/&c%campfiremax%",
						"&cOfen: &a%furnacefree%&7/&c%furnacemax%",
						"&cSchmelzofen: &a%blast_furnacefree%&7/&c%blast_furnacemax%",
						"&cRäucherofen: &a%smokerfree%&7/&c%smokermax%",
						
						"&cYour free TTExp: &f%freettexp%",
						"&cYour allocated TTExp: &f%allocatedttexp%",
						"",
						"&eTechnology => &ahave&f/&7see&f/&cexist",
						"&cTotal Techs: &a%techhave%&f/&7%techsee%&f/&c%techexist%",
						"&cSolo Techs: &a%solotechhave%&f/&7%solotechsee%&f/&c%solotechexist%",
						"&cGroup Techs: &a%grouptechhave%&f/&7%grouptechsee%&f/&c%grouptechexist%",
						"&cGlobal Techs: &a%globaltechhave%&f/&7%globaltechsee%&f/&c%globaltechexist%",
						"",
						"&eRegistrated Blocks => &ahave&7/&ccan have",
						"&cBrewing Stand: &a%brewing_standfree%&7/&c%brewing_standmax%",
						"&cEnchanting Table: &a%enchanting_tablefree%&7/&c%enchanting_tablemax%",
						"&cCampfire: &a%campfirefree%&7/&c%campfiremax%",
						"&cFurnance: &a%furnacefree%&7/&c%furnacemax%",
						"&cBlastfurnace: &a%blast_furnacefree%&7/&c%blast_furnacemax%",
						"&cSmoker: &a%smokerfree%&7/&c%smokermax%"
						}));
		path = "16"; //ShowSyncMsg Button
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
						"",
						"&eReturn to the Start Menu.",
						""
						}));
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
						"",
						"&eReturn to the main categories.",
						""
						}));
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
						"",
						"&eReturn to the sub categories.",
						""
						}));
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
		String onekey = "";
		LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		one.put("",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		itemGeneratorKeys.put(onekey, one);
	}
	
	public void initMainCategory() //INFO:MainCategory
	{
		addMainCategory("mining",
				new String[] {"Bergbau", "Mining"},
				PlayerAssociatedType.SOLO, 0, 
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Bergbau","&7Mining"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Haupkategorie Bergbau",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Maincategory Mining",
						"&aThis category is always on display!"},
				new String[] {"&bBergbau","&bMining"}, Material.IRON_ORE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Haupkategorie Bergbau",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Maincategory Mining",
						"&aThis category is always on display!"});
		addMainCategory("forestry",
				new String[] {"Försterei", "Forestry"},
				PlayerAssociatedType.SOLO, 1, 
				new String[] {
						"if:(a):o_1",
						"elseif:(b):o_2",
						"output:o_1:true",
						"output:o_2:true",
						"a:var1:perm=here.your.first.permission",
						"b:var1:perm=here.your.other.permission"}, true,
				new String[] {"&7Försterei","&7Forestry"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Hauptkategory Försterei",
						"&cAnforderungen zum einsehen:",
						"&cPermission >here.your.first.permission< / >here.your.other.permission<",
						"&7Maincategory Forestry",
						"&cRequirements to view:",
						"&c>here.your.first.permission< / >here.your.other.permission<"},
				new String[] {"&bFörsterei","&bForestry"}, Material.OAK_SAPLING, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&bHauptkategory Försterei",
						"&fGibt Einsicht auf verschiedenste Bereiche der",
						"&fFörsterrei wie Setzlinge pflanzen und Bäume schlagen.",
						"&bMaincategory Forestry",
						"&fGives insight into various areas of the forestry",
						"&fwork such as planting seedlings and felling trees."});
		addMainCategory("stonemason",
				new String[] {"Steinmetz", "Stonemason"},
				PlayerAssociatedType.SOLO, 2,
				new String[] {
						"if:(a || b):o_1",
						"output:o_1:true",
						"a:var1=%tt_hastech,cleanstone%",
						"b:var1=%tt_hastech,sandstone%"}, true,
				new String[] {"&7Steinmetz","&7Stonemason"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Hauptkategory Steinmetz",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >Stein< oder >Sandstein< erforscht haben.",
						"&7Maincategory Stonemason",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Stone< or >Sandstone<."},
				new String[] {"",""}, Material.COBBLESTONE_STAIRS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&bHauptkategory Steinsmetz",
						"&fGibt Einsicht auf verschiedenste Bereiche des",
						"&fSteinmetzes wie Steinstufen und Treppen zum craften.",
						"&bMaincategory Stonemason",
						"&fGives insight into various areas of the stonemason",
						"&fto craft stoneslaps and stairs."});
		addMainCategory("booster",
				new String[] {"Booster", "Booster"},
				PlayerAssociatedType.GLOBAL, 0, 
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Booster","&7Booster"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Global Haupkategorie Booster",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Maincategory Booster",
						"&aThis category is always on display!"},
				new String[] {"&bBergbau","&bMining"}, Material.BEACON, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Global Haupkategorie Booster",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Maincategory Booster",
						"&aThis category is always on display!"});
	}
	
	public void addMainCategory(
			String key, String[] displayname, PlayerAssociatedType pat, int guiSlot,
			String[] conditionQuery, boolean showDifferentItemIfYouNormallyDontSeeIt,
			String[] notSeeDisplayname, Material notSeeMat, int notSeeAmount, String[] notSeeItemFlag, String[] notSeeEnchantments, String[] notSeeLore,
			String[] canSeeDisplayname, Material canSeeMat, int canSeeAmount, String[] canSeeItemFlag, String[] canSeeEnchantments, String[] canSeeLore)
	{
		LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		one.put("Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				displayname}));
		one.put("PlayerAssociatedType", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						pat.toString()}));
		one.put("GuiSlot", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						guiSlot}));
		one.put("RequirementToSee.ConditionQuery", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						conditionQuery}));
		one.put("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						showDifferentItemIfYouNormallyDontSeeIt}));
		//--- ItemIfYouCannotSee ---
		one.put("RequirementToSee.ItemIfYouCannotSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						notSeeDisplayname}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeMat}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeAmount}));
		one.put("RequirementToSee.ItemIfYouCannotSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeItemFlag}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeEnchantments}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						notSeeLore}));
		//--- ItemIfYouCanSee ---
		one.put("RequirementToSee.ItemIfYouCanSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						canSeeDisplayname}));
		one.put("RequirementToSee.ItemIfYouCanSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeMat}));
		one.put("RequirementToSee.ItemIfYouCanSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeAmount}));
		one.put("RequirementToSee.ItemIfYouCanSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeItemFlag}));
		one.put("RequirementToSee.ItemIfYouCanSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeEnchantments}));
		one.put("RequirementToSee.ItemIfYouCanSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						canSeeLore}));
		mainCategoryKeys.put(key, one);
	}
	
	public void initSubCategory() //INFO:SubCategory
	{
		addSubCategory("soil",
				new String[] {"Erde", "Soil"},
				PlayerAssociatedType.SOLO, 0,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "mining",
				new String[] {"&7Erde","&7Soil"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Erde",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Soil",
						"&aThis category is always on display!"},
				new String[] {"&bErde","&bSoil"}, Material.DIRT, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Haupkategorie Erde",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Maincategory Soil",
						"&aThis category is always on display!"});
		addSubCategory("stone",
				new String[] {"Steine", "Stone"},
				PlayerAssociatedType.SOLO, 1, 
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "mining",
				new String[] {"&7Erde","&7Soil"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Erde",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Soil",
						"&aThis category is always on display!"},
				new String[] {"&bErde","&bSoil"}, Material.STONE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Haupkategorie Erde",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Maincategory Soil",
						"&aThis category is always on display!"});
		addSubCategory("ore",
				new String[] {"Erze", "Ore"},
				PlayerAssociatedType.SOLO, 2,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "mining",
				new String[] {"&7Erze","&7Ores"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Erze",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Ores",
						"&aThis category is always on display!"},
				new String[] {"&bErze","&bOres"}, Material.IRON_ORE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Haupkategorie Bergbau",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Maincategory Mining",
						"&aThis category is always on display!"});
		addSubCategory("miningbooster",
				new String[] {"Abbaubooster", "Miningbooster"},
				PlayerAssociatedType.GLOBAL, 0,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "booster",
				new String[] {"&7Abbaubooster","&7Miningbooster"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Globale Subkategorie Abbaubooster",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Global Subcategory Miningbooster",
						"&aThis category is always on display!"},
				new String[] {"&bAbbaubooster","&bMining booster"}, Material.GOLDEN_PICKAXE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Haupkategorie Abbaubooster",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Maincategory Miningbooster",
						"&aThis category is always on display!"});
		addSubCategory("craftbooster",
				new String[] {"Herstellungsbooster", "Craftingbooster"},
				PlayerAssociatedType.GLOBAL, 1,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "booster",
				new String[] {"&7Herstellungsbooster","&7Craftingbooster"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Globale Subkategorie Herstellungsbooster",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Global Subcategory Craftingbooster",
						"&aThis category is always on display!"},
				new String[] {"&bHerstellungsbooster","&bCraftingbooster"}, Material.CRAFTING_TABLE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Haupkategorie Herstellungsbooster",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Maincategory Craftingbooster",
						"&aThis category is always on display!"});
	}
	
	public void addSubCategory(
			String key, String[] displayname, PlayerAssociatedType pat, int guiSlot, 
			String[] conditionQuery, boolean showDifferentItemIfYouNormallyDontSeeIt, String overlyingMainCategory,
			String[] notSeeDisplayname, Material notSeeMat, int notSeeAmount, String[] notSeeItemFlag, String[] notSeeEnchantments, String[] notSeeLore,
			String[] canSeeDisplayname, Material canSeeMat, int canSeeAmount, String[] canSeeItemFlag, String[] canSeeEnchantments, String[] canSeeLore)
	{
		LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		one.put("Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				displayname}));
		one.put("PlayerAssociatedType", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						pat.toString()}));
		one.put("GuiSlot", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						guiSlot}));
		one.put("RequirementToSee.ConditionQuery", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						conditionQuery}));
		one.put("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						showDifferentItemIfYouNormallyDontSeeIt}));
		one.put("IfSubCategory.OverlyingMainCategory", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				overlyingMainCategory}));
		//--- ItemIfYouCannotSee ---
		one.put("RequirementToSee.ItemIfYouCannotSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						notSeeDisplayname}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeMat}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeAmount}));
		one.put("RequirementToSee.ItemIfYouCannotSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeItemFlag}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeEnchantments}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						notSeeLore}));
		//--- ItemIfYouCanSee ---
		one.put("RequirementToSee.ItemIfYouCanSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						canSeeDisplayname}));
		one.put("RequirementToSee.ItemIfYouCanSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeMat}));
		one.put("RequirementToSee.ItemIfYouCanSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeAmount}));
		one.put("RequirementToSee.ItemIfYouCanSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeItemFlag}));
		one.put("RequirementToSee.ItemIfYouCanSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeEnchantments}));
		one.put("RequirementToSee.ItemIfYouCanSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						canSeeLore}));
		subCategoryKeys.put(key, one);
	}
	
	public void initTechnology()
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
			String[] toResConditionQuery,
			String toResCostTTExp, String toResCostVanillaExp, String toResCostMoney, String[] toResCostMaterial,
			String[] canResDisplayname, Material canResMat, int canResAmount, String[] canResItemFlag, String[] canResEnchantments, String[] canResLore,
			String[] hadResDisplayname, Material hadResMat, int hadResAmount, String[] hadResItemFlag, String[] hadResEnchantments, String[] hadResLore,
			String[] rewardUnlockableInteractions, String[] rewardUnlockableRecipe, String[] rewardDropChance, String[] rewardSilkTouchDropChance,
			String[] rewardCommand, String[] rewardItem, String[] rewardModifier, String[] rewardValueEntry) //INFO:Technology
	{
		String onekey = "";
		LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		one.put("Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						displayname}));
		one.put("TechnologyType", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						techType.toString()}));
		one.put("MaximalTechnologyLevelToResearch", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						maxTechLevToResearch}));
		one.put("PlayerAssociatedType", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						pat.toString()}));
		one.put("GuiSlot", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						guiSlot}));
		one.put("IfBoosterDurationUntilExpiration", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ifBoosterDurUntilExp}));
		one.put("OverlyingSubCategory", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						overlyingSubCategory}));
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
		one.put("RequirementToSee.ConditionQuery", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						toSeeConditionQuery}));
		one.put("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						showDifferentItemIfYouNormallyDontSeeIt}));
		//--- ToSee - ItemIfYouCannotSee ---
		one.put("RequirementToSee.ItemIfYouCannotSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						notSeeDisplayname}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeMat}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeAmount}));
		one.put("RequirementToSee.ItemIfYouCannotSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeItemFlag}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeEnchantments}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						notSeeLore}));
		//--- ToSee - ItemIfYouCanSee ---
		one.put("RequirementToSee.ItemIfYouCanSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						canSeeDisplayname}));
		one.put("RequirementToSee.ItemIfYouCanSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeMat}));
		one.put("RequirementToSee.ItemIfYouCanSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeAmount}));
		one.put("RequirementToSee.ItemIfYouCanSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeItemFlag}));
		one.put("RequirementToSee.ItemIfYouCanSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeEnchantments}));
		one.put("RequirementToSee.ItemIfYouCanSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						canSeeLore}));
		
		one.put("RequirementToResearch.ConditionQuery", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						toSeeConditionQuery}));
		one.put("RequirementToResearch.Costs.TTExp", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						toResCostTTExp}));
		one.put("RequirementToResearch.Costs.VanillaExp", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						toResCostVanillaExp}));
		one.put("RequirementToResearch.Costs.Money", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						toResCostMoney}));
		one.put("RequirementToResearch.Costs.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						toResCostMaterial}));
		//--- ToResearch - IfYouCanResearchIt ---
		one.put("RequirementToResearch.IfYouCanResearchIt.Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						canResDisplayname}));
		one.put("RequirementToResearch.IfYouCanResearchIt.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canResMat}));
		one.put("RequirementToResearch.IfYouCanResearchIt.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canResAmount}));
		one.put("RequirementToResearch.IfYouCanResearchIt.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canResItemFlag}));
		one.put("RequirementToResearch.IfYouCanResearchIt.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canResEnchantments}));
		one.put("RequirementToResearch.IfYouCanResearchIt.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						canResLore}));
		//--- ToResearch - ItemIfYouHaveResearchedIt ---
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						hadResDisplayname}));
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						hadResMat}));
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						hadResAmount}));
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						hadResItemFlag}));
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						hadResEnchantments}));
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						hadResLore}));
		technologyKeys.put(onekey, one);
	}
	
	public void initRecipe()
	{
		//int m = 0;
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
		    	one.put("Shape.Line1",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getShape()[0]}));
		    	one.put("Shape.Line2",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getShape()[1]}));
		    	one.put("Shape.Line3",
						new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
								a.getShape()[2]}));
		    	StringBuilder sb = new StringBuilder();
		    	for(Entry<Character, RecipeChoice> entry : a.getChoiceMap().entrySet())
		    	{
		    		sb.append(String.valueOf(entry.getKey()));
		    		String c = String.valueOf(entry.getKey());
		    		String path = "Ingredient."+c+".";
		    		ItemStack is = null;
		    		if(entry.getValue() instanceof RecipeChoice.ExactChoice)
		    		{
		    			RecipeChoice.ExactChoice rcec = (ExactChoice) entry.getValue();
		    			is = rcec.getItemStack();
		    		} else
		    		{
		    			RecipeChoice.MaterialChoice rcec = (MaterialChoice) entry.getValue();
		    			is = rcec.getItemStack();
		    		}
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
		    } else if(r instanceof SmithingRecipe)
		    {
		    	SmithingRecipe a = (SmithingRecipe) r;
		    	String onekey = a.getKey().getKey();
		    	LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		    	String path = "Base.";
		    	RecipeChoice.MaterialChoice rcecb = (MaterialChoice) a.getBase(); //TODO Checken ob das Fehler verursacht
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
				smithingRecipeKeys.put(onekey, one);
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
		    }
		}
	}
}