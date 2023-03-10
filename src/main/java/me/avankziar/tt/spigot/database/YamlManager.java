package main.java.me.avankziar.tt.spigot.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
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

import main.java.me.avankziar.tt.spigot.conditionbonusmalus.Bypass;
import main.java.me.avankziar.tt.spigot.database.Language.ISO639_2B;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.RewardType;

public class YamlManager
{
	private ISO639_2B languageType = ISO639_2B.GER;
	//The default language of your plugin. Mine is german.
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	
	//Per Flatfile a linkedhashmap.
	private static LinkedHashMap<String, Language> configSpigotKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> cbmlanguageKeys = new LinkedHashMap<>();
	
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
		initConditionBonusMalusLanguage();
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
	
	public LinkedHashMap<String, Language> getConditionBonusMalusLanguageKey()
	{
		return cbmlanguageKeys;
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
		
		configSpigotKeys.put("EnableMechanic.BonusMalus"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("EnableMechanic.CommandToBungee"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("EnableMechanic.Condition"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("EnableMechanic.ConditionQueryParser"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		
		configSpigotKeys.put("EnableCommands.Base"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		
		configSpigotKeys.put("Condition.ConditionOverrulePermission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
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
				true}));
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
			String suggestion, String commandString, boolean putUpCmdPermToBonusMalusSystem,
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
		commandsKeys.put(path+".PutUpCommandPermToBonusMalusSystem"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				putUpCmdPermToBonusMalusSystem}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
		commandsKeys.put(path+".Displayname"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				dnGerman,
				dnEnglish}));
		commandsKeys.put(path+".Explanation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				exGerman,
				exEnglish}));
	}
	
	private void argumentInput(String path, String argument, String basePermission, 
			String suggestion, String commandString, boolean putUpCmdPermToBonusMalusSystem,
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
		commandsKeys.put(path+".PutUpCommandPermToBonusMalusSystem"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				putUpCmdPermToBonusMalusSystem}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
		commandsKeys.put(path+".Displayname"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				dnGerman,
				dnEnglish}));
		commandsKeys.put(path+".Explanation"
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
	}
	
	public void initEnumHandlerLang() //INFO:EnumHandlerLang
	{
		String path = "EnumHandler.";
	}
	
	public void initPlayerHandlerLang() //INFO:PlayerHandlerLang
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
	
	public void initConditionBonusMalusLanguage() //INFO:BonusMalusLanguages
	{
		cbmlanguageKeys.put(Bypass.Permission.SEE_MAIN_CATEGORYS.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eByasspermission um alle Hauptkategorien zu sehen.",
						"&eBypasspermission to see all maincategories."}));
		cbmlanguageKeys.put(Bypass.Permission.SEE_MAIN_CATEGORYS.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eByasspermission für",
						"&edas Plugin TechnologyTree.",
						"&eErmöglich das Sehen aller",
						"&eHauptkategorien.",
						"&eBypasspermission for",
						"&ethe plugin TechnologyTree.",
						"&eAllows you to see all",
						"&ethe main categories."}));
		cbmlanguageKeys.put(Bypass.Permission.SEE_SUB_CATEGORYS.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eByasspermission um alle Unterkategorien zu sehen.",
						"&eBypasspermission to see all subcategories."}));
		cbmlanguageKeys.put(Bypass.Permission.SEE_SUB_CATEGORYS.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eByasspermission für",
						"&edas Plugin TechnologyTree.",
						"&eErmöglich das Sehen aller",
						"&eUnterkategorien.",
						"&eBypasspermission for",
						"&ethe plugin TechnologyTree.",
						"&eAllows you to see all",
						"&ethe sub categories."}));
		cbmlanguageKeys.put(Bypass.Counter.REGISTER_BLOCK.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZählpermission für die Registrierung von Blöcken.",
						"&eCounting mission for the registration of blocks."}));
		cbmlanguageKeys.put(Bypass.Counter.REGISTER_BLOCK.toString()+".Explanation",
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
		HashMap<EventType, String> eventTypeMap = new HashMap<>();
		HashMap<RewardType, String> rewardTypeMap = new HashMap<>();
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
						cbmlanguageKeys.put(ma.toString()+"."+e.toString()+"."+r.toString()+".Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eConditionEntry für das Material %ma%, des Events %e% des Belohnungstyp %r%."
										.replace("%m%", mat).replace("%e%", ev).replace("%r%", rt),
										"&eConditionEntry for the material %ma%, of the event %e% of the reward type %r%."
										.replace("%m%", mat).replace("%e%", ev).replace("%r%", rt)}));
						cbmlanguageKeys.put(ma.toString()+"."+e.toString()+"."+r.toString()+".Explanation",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eConditionEntry für",
										"&edas Plugin TechnologyTree.",
										"&eConditionEntry for",
										"&ethe plugin TechnologyTree."}));
					} else
					{
						cbmlanguageKeys.put(ma.toString()+"."+e.toString()+"."+r.toString()+".Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eBonusMalus für das Material %ma%, des Events %e% des Belohnungstyp %r%."
										.replace("%m%", mat).replace("%e%", ev).replace("%r%", rt),
										"&eBonusMalus for the material %ma%, of the event %e% of the reward type %r%."
										.replace("%m%", mat).replace("%e%", ev).replace("%r%", rt)}));
						cbmlanguageKeys.put(ma.toString()+"."+e.toString()+"."+r.toString()+".Explanation",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eBonusMalus für",
										"&edas Plugin TechnologyTree.",
										"&eBonusMalus for",
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
						cbmlanguageKeys.put(et.toString()+"."+e.toString()+"."+r.toString()+".Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eConditionEntry für das Entity %ma%, des Events %e% des Belohnungstyp %r%."
										.replace("%m%", ent).replace("%e%", ev).replace("%r%", rt),
										"&eConditionEntry for the entity %ma%, of the event %e% of the reward type %r%."
										.replace("%m%", ent).replace("%e%", ev).replace("%r%", rt)}));
						cbmlanguageKeys.put(et.toString()+"."+e.toString()+"."+r.toString()+".Explanation",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eConditionEntry für",
										"&edas Plugin TechnologyTree.",
										"&eConditionEntry for",
										"&ethe plugin TechnologyTree."}));
					} else
					{
						cbmlanguageKeys.put(et.toString()+"."+e.toString()+"."+r.toString()+".Displayname",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eBonusMalus für das Entity %ma%, des Events %e% des Belohnungstyp %r%."
										.replace("%m%", ent).replace("%e%", ev).replace("%r%", rt),
										"&eBonusMalus for the entity %ma%, of the event %e% of the reward type %r%."
										.replace("%m%", ent).replace("%e%", ev).replace("%r%", rt)}));
						cbmlanguageKeys.put(et.toString()+"."+e.toString()+"."+r.toString()+".Explanation",
								new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
										"&eBonusMalus für",
										"&edas Plugin TechnologyTree.",
										"&eBonusMalus for",
										"&ethe plugin TechnologyTree."}));
					}
				}
			}
		}
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
		String onekey = "";
		LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		one.put("",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		mainCategoryKeys.put(onekey, one);
	}
	
	public void initSubCategory() //INFO:SubCategory
	{
		String onekey = "";
		LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		one.put("",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		subCategoryKeys.put(onekey, one);
	}
	
	public void initTechnology() //INFO:Technology
	{
		String onekey = "";
		LinkedHashMap<String, Language> one = new LinkedHashMap<>();
		one.put("",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
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