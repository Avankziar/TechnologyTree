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
		//initModifierValueEntryLanguage(); TODO
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
		configSpigotKeys.put("Do.DeleteExpireTechnologies.TaskRunInMinutes"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				10}));
		configSpigotKeys.put("Do.DeleteExpirePlacedBlocks.TaskRunInMinutes"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				15}));
		configSpigotKeys.put("Do.NewPlayer.ShowSyncMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.NewPlayer.AutoResearchTechnology"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"soil_I",
				"woodenlog"}));
		configSpigotKeys.put("Do.Block.OverrideAlreadyRegisteredBlocks"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpigotKeys.put("Do.Drops.UsePluginDropsCalculation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("Do.Drops.DoNotUsePluginDropsCalculationWorlds"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hubdummy",
				"spawncitydummy"}));
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
	}
	
	public void initCommandsLang() //INFO:CommandsLang
	{
		String path = "Commands.";
		languageKeys.put(path+"TechInfo.TechNotFound", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDie Technologie %tech% existiert nicht!",
						"&cThe %tech% technology does not exist!"}));
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
						"&cÜbergeordnete SubCategory: &f%name%",
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
						"&cMaxTechLvlToResearch: &f%lvl% &e| &cGuiSlot: &f%slot%",
						"&cMaxTechLvlToResearch: &f%lvl% &e| &cGuiSlot: &f%slot%"}));
		languageKeys.put(path+"Technology.Info.PlayerAssociatedTypeAndTechType", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cPlayerAssociatedType: &f%pat% &e| &cTechType: &f%ttype%",
						"&cPlayerAssociatedType: &f%pat% &e| &cTechType: &f%ttype%"}));
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
						"&cTTExp Kosten: &f%v% | &f(%f%)",
						"&cTTExp Costs: &f%v% | &f(%f%)"}));
		languageKeys.put(path+"Technology.Info.Lvl.CostVExp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cVanillaExp Kosten: &f%v% | &f(%f%)",
						"&cVanillaExp costs: &f%v% | &f(%f%)"}));
		languageKeys.put(path+"Technology.Info.Lvl.CostMoneyHover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cGeldkosten: &f%v% | &f(%f%)",
						"&cMoney costs: &f%v% | &f(%f%)"}));
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
						"&cDeine eingesetzte TTExp: &f%allocatedttexp%",
						
						"&cYour free TTExp: &f%freettexp%",
						"&cYour allocated TTExp: &f%allocatedttexp%"
						}));
		start.put(path+".Lore."+SettingsLevel.EXPERT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine freie TTExp: &f%freettexp%",
						"&cDeine eingesetzte TTExp: &f%allocatedttexp%",
						"",
						"&eRegistrierte Blöcke => &ahat man&7/&bkann man haben",
						"&cBraustand: &a%brewing_standfree%&7/&b%brewing_standmax%",
						"&cVerz. Tisch: &a%enchanting_tablefree%&7/&b%enchanting_tablemax%",
						"&cLagerfeuer: &a%campfirefree%&7/&b%campfiremax%",
						"&cOfen: &a%furnacefree%&7/&b%furnacemax%",
						"&cSchmelzofen: &a%blast_furnacefree%&7/&b%blast_furnacemax%",
						"&cRäucherofen: &a%smokerfree%&7/&b%smokermax%",
						
						"&cYour free TTExp: &f%freettexp%",
						"&cYour allocated TTExp: &f%allocatedttexp%",
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
						"&cDeine eingesetzte TTExp: &f%allocatedttexp%",
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
						"&cYour allocated TTExp: &f%allocatedttexp%",
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
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Sonstiges","&7Miscellaneous"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Haupkategorie Sonstiges",
						"&aDiese Kategorie ist immer zu sehen!",
						"&f====================",
						"&eHier sind alle Werkzeuge, Waffen,",
						"&eRüstungen und mehr untergebracht.",
						"&7Maincategory Miscellaneous",
						"&aThis category is always on display!",
						"&f====================",
						"&eThey house all the tools, weapons,",
						"&earmor and more.",},
				new String[] {"&bBergbau","&bMiscellaneous"}, Material.WOODEN_PICKAXE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Haupkategorie Sonstiges",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Maincategory Miscellaneous",
						"&aThis category is always on display!"});
		addMainCategory("mining",
				new String[] {"Bergbau", "Mining"},
				PlayerAssociatedType.SOLO, 1, 
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
		addMainCategory("woodworking",
				new String[] {"Holzarbeiten", "Woodworking"},
				PlayerAssociatedType.SOLO, 2, 
				new String[] {
						"if:(a||b):o_1",
						"output:o_1:true",
						"a:var1=perm=here.your.first.permission",
						"b:var1=perm=here.your.other.permission"}, true,
				new String[] {"&7Holzarbeiten","&7Woodworking"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Hauptkategory Holzarbeiten",
						"&cAnforderungen zum einsehen:",
						"&cPermission >here.your.first.permission< / >here.your.other.permission<",
						"&7Maincategory Woodworking",
						"&cRequirements to view:",
						"&c>here.your.first.permission< / >here.your.other.permission<"},
				new String[] {"&bHolzarbeiten","&bWoodworking"}, Material.OAK_PLANKS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&bHauptkategorie Holzarbeiten",
						"&fGibt Einsicht auf verschiedenste Bereiche der",
						"&fHolzarbeiten wie Setzlinge pflanzen, Bäume schlagen, etc.",
						"&bMaincategory Woodworking",
						"&fGives insight into various areas of the woodworking",
						"&fwork such as planting seedlings and felling trees."});
		addMainCategory("stonemason",
				new String[] {"Steinmetz", "Stonemason"},
				PlayerAssociatedType.SOLO, 3,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:hasresearchedtech,stone_I,1:==:true"}, true,
				new String[] {"&7Steinmetz","&7Stonemason"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Hauptkategory Steinmetz",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >Stein I< erforscht haben.",
						"&7Maincategory Stonemason",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Stone I<."},
				new String[] {"&bSteinmetz","&bStonemason"}, Material.COBBLESTONE_STAIRS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&bHauptkategory Steinsmetz",
						"&fGibt Einsicht auf verschiedenste Bereiche des",
						"&fSteinmetzes wie Steinstufen und Treppen zum craften.",
						"&bMaincategory Stonemason",
						"&fGives insight into various areas of the stonemason",
						"&fto craft stoneslaps and stairs."});
		addMainCategory("tablerecipe",
				new String[] {"Tischrezepte", "Tablerecipe"},
				PlayerAssociatedType.SOLO, 4,
				new String[] {
						"if:(a||b):o_1",
						"output:o_1:true",
						"a:hasresearchedtech,stone_I,1:==:true",
						"b:hasresearchedtech,ironore,1:==:true"}, true,
				new String[] {"&7Tischrezepte","&7Tablerecipe"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Hauptkategory Tischrezepte",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >Stein I< oder >Eisenerz< erforscht haben.",
						"&7Maincategory Tablerecipe",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Stone I< or <Ironore>."},
				new String[] {"&bTischrezepte","&bTablerecipe"}, Material.ENCHANTING_TABLE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Hauptkategory Tischrezepte",
						"&fGibt Einsicht auf verschiedenste Bereiche des",
						"&fTischrezepte wie brennen im Ofen, Verzauben, Tränkebrauen etc...",
						"&7Maincategory Tablerecipe",
						"&fGives insight into various areas of table recipes",
						"&fsuch as smelting in the furnace, enchanting, potion brewing etc..."});
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
		one.put("Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG},
				displayname));
		one.put("PlayerAssociatedType", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						pat.toString()}));
		one.put("GuiSlot", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						guiSlot}));
		one.put("RequirementToSee.ConditionQuery", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						conditionQuery));
		one.put("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						showDifferentItemIfYouNormallyDontSeeIt}));
		//--- ItemIfYouCannotSee ---
		one.put("RequirementToSee.ItemIfYouCannotSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						notSeeDisplayname));
		one.put("RequirementToSee.ItemIfYouCannotSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeMat.toString()}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeAmount}));
		one.put("RequirementToSee.ItemIfYouCannotSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						notSeeItemFlag));
		one.put("RequirementToSee.ItemIfYouCannotSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						notSeeEnchantments));
		one.put("RequirementToSee.ItemIfYouCannotSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						notSeeLore));
		//--- ItemIfYouCanSee ---
		one.put("RequirementToSee.ItemIfYouCanSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						canSeeDisplayname));
		one.put("RequirementToSee.ItemIfYouCanSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeMat.toString()}));
		one.put("RequirementToSee.ItemIfYouCanSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeAmount}));
		one.put("RequirementToSee.ItemIfYouCanSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						canSeeItemFlag));
		one.put("RequirementToSee.ItemIfYouCanSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						canSeeEnchantments));
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
	
	public void subc_Miscellaneous()
	{
		/*
		 * Werkzeuge(Schaufel, Hacke, Spitzhacke, Axt, Eimer(Wasser, etc.), Angel, Feuerzeug, Schere)
		 * WaffenRüstungen(Schwert, Schild, Dreizack, Bogen, Armbrust,
		 */
		addSubCategory("tools",
				new String[] {"Werkzeuge", "Tools"},
				PlayerAssociatedType.SOLO, 0,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "miscellaneous",
				new String[] {"&7Werkzeuge","&7Tools"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Werkzeuge",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Tools",
						"&aThis category is always on display!"},
				new String[] {"&bWerkzeuge","&bTools"},
				Material.WOODEN_PICKAXE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Werkzeuge",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Tools",
						"&aThis category is always on display!"});
		addSubCategory("weapons_armor",
				new String[] {"Waffen_Rüstungen", "Weapons_Armor"},
				PlayerAssociatedType.SOLO, 1,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "miscellaneous",
				new String[] {"&7Waffen und Rüstungen","&7Weapons and Armor"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Waffen und Rüstungen",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Weapons and Armor",
						"&aThis category is always on display!"},
				new String[] {"&bWaffen und Rüstungen","&bWeapons and Armor"},
				Material.WOODEN_SWORD, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Waffen und Rüstungen",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Weapons and Armor",
						"&aThis category is always on display!"});
		addSubCategory("interactionblocks",
				new String[] {"Interaktionsblöcke", "Interactionblocks"},
				PlayerAssociatedType.SOLO, 2,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "miscellaneous",
				new String[] {"&7Interaktionsblöcke","&7Interactionblocks"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Interaktionsblöcke",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Interactionblocks",
						"&aThis category is always on display!"},
				new String[] {"&bInteraktionsblöcke","&bInteractionblocks"},
				Material.CRAFTING_TABLE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Interaktionsblöcke",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Interactionblocks",
						"&aThis category is always on display!"});
	}
	
	public void subc_Mining()
	{
		/*
		 * Erde,
		 * Steine
		 * Erze
		 */
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
	}
	
	public void subc_Woodworking()
	{
		/*
		 * Setzlinge
		 * Holzstämme
		 * Holzbretter
		 */
		addSubCategory("sapling",
				new String[] {"Setzlinge", "Saplings"},
				PlayerAssociatedType.SOLO, 1,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "woodworking",
				new String[] {"&7Setzlinge","&7Saplings"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Setzlinge",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Saplings",
						"&aThis category is always on display!"},
				new String[] {"&bSetzlinge","&bSapling"}, Material.OAK_SAPLING, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Haupkategorie Setzling",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Maincategory Sapling",
						"&aThis category is always on display!"});
		addSubCategory("wood",
				new String[] {"Holz", "Wood"},
				PlayerAssociatedType.SOLO, 2,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "woodworking",
				new String[] {"&7Holz","&7Wood"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Holz",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Wood",
						"&aThis category is always on display!"},
				new String[] {"&bHolz","&bWood"}, Material.OAK_LOG, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Holz",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Wood",
						"&aThis category is always on display!"});
	}
	
	public void subc_Stonemason()
	{
		/*
		 * Stufen
		 * Treppen
		 */
		addSubCategory("stoneslaps",
				new String[] {"Steinstufen", "Stoneslaps"},
				PlayerAssociatedType.SOLO, 0,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "stonemason",
				new String[] {"&7Steinstufen","&7Stoneslaps"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Steinstufen",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Stoneslaps",
						"&aThis category is always on display!"},
				new String[] {"&bSteinstufen","&bStoneslaps"}, Material.STONE_SLAB, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Steinstufen",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Stoneslaps",
						"&aThis category is always on display!"});
		addSubCategory("stonestairs",
				new String[] {"Steintreppen", "Stonestairs"},
				PlayerAssociatedType.SOLO, 1,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "stonemason",
				new String[] {"&7Steintreppen","&7Stonestairs"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Steintreppen",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Stonestairs",
						"&aThis category is always on display!"},
				new String[] {"&bSteintreppen","&bStonestairs"}, Material.STONE_STAIRS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Steintreppen",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Stonestairs",
						"&aThis category is always on display!"});
	}
	
	public void subc_TableRecipes()
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
						"if:(a):o_1",
						"output:o_1:true",
						"a:var1=hasresearchedtech,furnace,1:==:true"}, true, "tablerecipe",
				new String[] {"&7Ofenrezepte","&7Furnacerecipe"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Ofenrezepte",
						"&cAnforderungen zum einsehen:",
						"&cTechnologie >Ofen< erforscht haben.",
						"&7Maincategory Furnacerecipe",
						"&cRequirements to view:",
						"&cTechnology >Furnace< researched."},
				new String[] {"&bOfenrezepte","&bFurnacerecipe"}, Material.FURNACE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Ofenrezepte",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Furnacerecipe",
						"&aThis category is always on display!"});
		addSubCategory("enchantmentrecipe",
				new String[] {"Verzauberungsrezepte", "Enchantmentrecipe"},
				PlayerAssociatedType.SOLO, 1,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "tablerecipe",
				new String[] {"&7Verzauberungsrezepte","&7Enchantmentrecipe"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Verzauberungsrezepte",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Enchantmentrecipe",
						"&aThis category is always on display!"},
				new String[] {"&bVerzauberungsrezepte","&bEnchantmentrecipe"}, Material.ENCHANTING_TABLE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Verzauberungsrezepte",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Enchantmentrecipe",
						"&aThis category is always on display!"});
		addSubCategory("brewingrecipe",
				new String[] {"Braurezepte", "Brewingrecipe"},
				PlayerAssociatedType.SOLO, 2,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "tablerecipe",
				new String[] {"&7Braurezepte","&7Brewingrecipe"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Braurezepte",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Brewingrecipe",
						"&aThis category is always on display!"},
				new String[] {"&bBraurezepte","&bFurnacerecipe"}, Material.BREWING_STAND, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Braurezepte",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Brewingrecipe",
						"&aThis category is always on display!"});
		addSubCategory("anvilrecipe",
				new String[] {"Ambossrezepte", "Anvilrecipe"},
				PlayerAssociatedType.SOLO, 3,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "tablerecipe",
				new String[] {"&7Ambossrezepte","&7Anvilrecipe"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Ambossrezepte",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Anvilrecipe",
						"&aThis category is always on display!"},
				new String[] {"&bAmbossrezepte","&bAnvilrecipe"}, Material.ANVIL, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Ambossrezepte",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Anvilrecipe",
						"&aThis category is always on display!"});
		addSubCategory("forgingcerecipe",
				new String[] {"Schmiederezepte", "Forgingcerecipe"},
				PlayerAssociatedType.SOLO, 4,
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true, "tablerecipe",
				new String[] {"&7Schmiederezepte","&7Forgingcerecipe"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Schmiederezepte",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Forgingcerecipe",
						"&aThis category is always on display!"},
				new String[] {"&bSchmiederezepte","&bForgingcerecipe"}, Material.SMITHING_TABLE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Subkategorie Schmiederezepte",
						"&aDiese Kategorie ist immer zu sehen!",
						"&7Subcategory Forgingcerecipe",
						"&aThis category is always on display!"});
	}
	
	public void subc_Booster()
	{
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
		one.put("Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG},
				displayname));
		one.put("PlayerAssociatedType", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						pat.toString()}));
		one.put("GuiSlot", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						guiSlot}));
		one.put("RequirementToSee.ConditionQuery", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						conditionQuery));
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
		one.put("RequirementToSee.ItemIfYouCannotSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						notSeeItemFlag));
		one.put("RequirementToSee.ItemIfYouCannotSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						notSeeEnchantments));
		one.put("RequirementToSee.ItemIfYouCannotSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						notSeeLore));
		//--- ItemIfYouCanSee ---
		one.put("RequirementToSee.ItemIfYouCanSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						canSeeDisplayname));
		one.put("RequirementToSee.ItemIfYouCanSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeMat.toString()}));
		one.put("RequirementToSee.ItemIfYouCanSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeAmount}));
		one.put("RequirementToSee.ItemIfYouCanSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						canSeeItemFlag));
		one.put("RequirementToSee.ItemIfYouCanSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						canSeeEnchantments));
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
		subc_Miscellaneous();
		tech_Miscellaneous_Tools();
		tech_Miscellaneous_Weapons_Armor();
		tech_Miscellaneous_Interactionblocks();
		
		subc_Mining();
		tech_Mining_Soil();
		tech_Mining_Stone();
		tech_Mining_Ore();
		
		subc_Woodworking();
		tech_Woodworking_Sapling();
		tech_Woodworking_Wood();
		
		subc_Stonemason();
		tech_Stonemason_Slabs();
		tech_Stonemason_Stonestairs();
		
		subc_TableRecipes();
		tech_Tablerecipe_Furnancerecipe();
		tech_Tablerecipe_Enchantmentrecipe();
		tech_Tablerecipe_Brewingrecipe();
		tech_Tablerecipe_Anvilrecipe();
		tech_Tablerecipe_Forgingcerecipe();
		
		subc_Booster();
		tech_Booster_Miningbooster();
		tech_Booster_Craftingbooster();
	}
	
	private void tech_Miscellaneous_Tools() //INFO:Miscellaneous_Tools
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(7, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(8, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(9, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(10, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(11, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(7, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(8, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(9, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(10, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(11, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
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
				"CRAFTING:WOODEN_PICKAXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"CRAFTING:STONE_PICKAXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"CRAFTING:IRON_PICKAXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"CRAFTING:GOLDEN_PICKAXEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"CRAFTING:DIAMOND_PICKAXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"CRAFTING:NETHERITE_PICKAXE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		String[] rui = new String[] {
				"CRAFTING:WOODEN_PICKAXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:STONE_PICKAXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:IRON_PICKAXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:DIAMOND_PICKAXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:GOLDEN_PICKAXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1",
				"CRAFTING:NETHERITE_PICKAXE:null:tool=HAND:ttexp=1:vaexp=2:vault=1:default=1"};
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
		rewardUnlockableRecipe.put(6, new String[] {"SMITHING:netherite_pickaxe",""});
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
		addTechnology(
				"pickaxe", new String[] {"Spitzhacke", "Pickaxe"},
				TechnologyType.MULTIPLE, 11, PlayerAssociatedType.SOLO, 0, "", "tools", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Spitzhacke","&7Pickaxe"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Spitzhacke",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Spitzhacke",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Pickaxe",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of pickaxe",
						"&cRightclick &bfor a more detailed view.",},
				new String[] {"&7Spitzhacke","&7Pickaxe"},
				Material.WOODEN_PICKAXE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Spitzhacke",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Spitzhacke",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Pickaxe",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of pickaxe",
						"&cRightclick &bfor a more detailed view.",},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Spitzhacke","&7Pickaxe"},
				Material.WOODEN_PICKAXE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Spitzhacke",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Spitzhacke",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Pickaxe",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of pickaxe",
						"&cRightclick &bfor a more detailed view.",},
				new String[] {"&bSpitzhacke","&bPickaxe"},
				Material.WOODEN_PICKAXE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Spitzhacke",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Spitzhacke",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Pickaxe",
						"&eUnlocks the following:",
						"&fCrafting of pickaxe",
						"&cRightclick &bfor a more detailed view.",},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(7, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(8, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(9, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(10, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(11, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(7, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(8, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(9, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(10, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(11, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
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
				"CRAFTING:WOODEN_SHOVEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"CRAFTING:STONE_SHOVEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"CRAFTING:IRON_SHOVEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"CRAFTING:GOLDEN_SHOVEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"CRAFTING:DIAMOND_SHOVEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"CRAFTING:NETHERITE_SHOVEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
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
		rewardUnlockableRecipe.put(6, new String[] {"SMITHING:netherite_shovel",""});
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
		addTechnology(
				"shovel", new String[] {"Schaufel", "Shovel"},
				TechnologyType.MULTIPLE, 11, PlayerAssociatedType.SOLO, 1, "", "tools", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Schaufel","&7Shovel"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Schaufel",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Schaufel",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Shovel",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of Shovel",
						"&cRightclick &bfor a more detailed view.",},
				new String[] {"&7Schaufel","&7Shovel"},
				Material.WOODEN_SHOVEL, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Schaufel",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Schaufel",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Shovel",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of Shovel",
						"&cRightclick &bfor a more detailed view.",},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Schaufel","&7Shovel"},
				Material.WOODEN_SHOVEL, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Schaufel",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Schaufel",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Shovel",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of Shovel",
						"&cRightclick &bfor a more detailed view.",},
				new String[] {"&bSchaufel","&bShovel"},
				Material.WOODEN_SHOVEL, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Schaufel",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Schaufel",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Shovel",
						"&eUnlocks the following:",
						"&fCrafting of Shovel",
						"&cRightclick &bfor a more detailed view.",},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(7, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(8, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(9, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(10, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(11, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(7, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(8, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(9, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(10, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(11, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
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
				"CRAFTING:WOODEN_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"CRAFTING:STONE_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"CRAFTING:IRON_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"CRAFTING:GOLDEN_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"CRAFTING:DIAMOND_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"CRAFTING:NETHERITE_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
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
		rewardUnlockableRecipe.put(6, new String[] {"SMITHING:netherite_hoe",""});
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
		addTechnology(
				"hoe", new String[] {"Hacke", "Hoe"},
				TechnologyType.MULTIPLE, 11, PlayerAssociatedType.SOLO, 2, "", "tools", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Hacke","&7Shovel"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Hacke",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Hacke",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Hoe",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of hoe",
						"&cRightclick &bfor a more detailed view.",},
				new String[] {"&7Hacke","&7Hoe"},
				Material.WOODEN_HOE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Hacke",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Hacke",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Hoe",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of hoe",
						"&cRightclick &bfor a more detailed view.",},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Hacke","&7Hoe"},
				Material.WOODEN_HOE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Hacke",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Hacke",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Hoe",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of hoe",
						"&cRightclick &bfor a more detailed view.",},
				new String[] {"&bHacke","&bHoe"},
				Material.WOODEN_HOE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Hacke",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Hacke",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Hoe",
						"&eUnlocks the following:",
						"&fCrafting of hoe",
						"&cRightclick &bfor a more detailed view.",},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(7, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(8, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(9, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(10, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(11, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(7, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(8, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(9, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(10, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(11, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
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
				"CRAFTING:WOODEN_HOEL:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"CRAFTING:STONE_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"CRAFTING:IRON_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"CRAFTING:GOLDEN_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"CRAFTING:DIAMOND_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"CRAFTING:NETHERITE_HOE:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
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
		rewardUnlockableRecipe.put(6, new String[] {"SMITHING:netherite_hoe",""});
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
		addTechnology(
				"axe", new String[] {"Axt", "Axe"},
				TechnologyType.MULTIPLE, 11, PlayerAssociatedType.SOLO, 3, "", "tools", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Axt","&7Axe"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Axt",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Axt",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Axe",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of axe",
						"&cRightclick &bfor a more detailed view.",},
				new String[] {"&7Axt","&7Axe"},
				Material.WOODEN_AXE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Axt",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Axt",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Axe",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of axe",
						"&cRightclick &bfor a more detailed view.",},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Axt","&7Axe"},
				Material.WOODEN_AXE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Axt",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Axt",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Axe",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of axe",
						"&cRightclick &bfor a more detailed view.",},
				new String[] {"&bAxt","&bAxe"},
				Material.WOODEN_AXE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Axt",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Axt",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Axe",
						"&eUnlocks the following:",
						"&fCrafting of axe",
						"&cRightclick &bfor a more detailed view.",},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Miscellaneous_Weapons_Armor() //INFO:Miscellaneous_Weapons_Armor
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(7, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(8, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(9, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(10, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(11, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(7, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(8, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(9, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(10, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(11, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
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
				"CRAFTING:WOODEN_SWORD:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"CRAFTING:STONE_SWORD:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"CRAFTING:IRON_SWORD:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"CRAFTING:GOLDEN_SWORD:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"CRAFTING:DIAMOND_SWORD:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"CRAFTING:NETHERITE_SWORD:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
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
		rewardUnlockableRecipe.put(6, new String[] {"SMITHING:netherite_sword",""});
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
		addTechnology(
				"sword", new String[] {"Schwert", "Sword"},
				TechnologyType.MULTIPLE, 11, PlayerAssociatedType.SOLO, 0, "", "weapons_armor", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Schwert","&7Sword"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Schwert",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Schwert",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Sword",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of sword",
						"&cRightclick &bfor a more detailed view.",},
				new String[] {"&7Schwert","&7Sword"},
				Material.WOODEN_SWORD, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Schwert",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Schwert",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Sword",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of sword",
						"&cRightclick &bfor a more detailed view.",},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Schwert","&7Sword"},
				Material.WOODEN_SWORD, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Schwert",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Schwert",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Sword",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of sword",
						"&cRightclick &bfor a more detailed view.",},
				new String[] {"&bSchwert","&bSword"},
				Material.WOODEN_SWORD, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Schwert",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Schwert",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Sword",
						"&eUnlocks the following:",
						"&fCrafting of sword",
						"&cRightclick &bfor a more detailed view.",},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(7, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(8, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(9, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(10, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostTTExp.put(11, "10 * (100 * techlev + 50 * techacq + 25 * totalsolotech)");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(7, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(8, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(9, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(10, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
		toResCostMoney.put(11, "5 * (1 * techlev + 0.5 * techacq + 0.25 * totalsolotech)");
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
				"CRAFTING:LEATHER_BOOTS:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"CRAFTING:IRON_BOOTS:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"CRAFTING:GOLDEN_BOOTS:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"CRAFTING:DIAMOND_BOOTS:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"CRAFTING:NETHERITE_BOOTS:null:tool=HAND:ttexp=1:vaexp=10:vault=1:default=1"});
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
		rewardUnlockableRecipe.put(5, new String[] {"SMITHING:netherite_boots",""});
		rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(2, new String[] {
				"CRAFTING:HAND:LEATHER_BOOTS:null:mat=LEATHER:1:0.005",""});
		rewardDropChance.put(3, new String[] {
				"CRAFTING:HAND:LEATHER_BOOTS:null:mat=LEATHER:1:0.02",""});
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
		addTechnology(
				"boots", new String[] {"Schuhe", "Boots"},
				TechnologyType.MULTIPLE, 11, PlayerAssociatedType.SOLO, 1, "", "weapons_armor", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Schuhe","&7Boots"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Schuhe",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Schuhe",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Boots",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of boots",
						"&cRightclick &bfor a more detailed view.",},
				new String[] {"&7Schuhe","&7Boots"},
				Material.LEATHER_BOOTS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Schuhe",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Schuhe",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Boots",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of boots",
						"&cRightclick &bfor a more detailed view.",},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Schuhe","&7Boots"},
				Material.LEATHER_BOOTS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Schuhe",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Schuhe",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Boots",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting of boots",
						"&cRightclick &bfor a more detailed view.",},
				new String[] {"&bSchuhe","&bBoots"},
				Material.LEATHER_BOOTS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Schuhe",
						"&eSchaltet folgendes frei:",
						"&fHerstellung von Schuhe",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Boots",
						"&eUnlocks the following:",
						"&fCrafting of boots",
						"&cRightclick &bfor a more detailed view.",},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Miscellaneous_Interactionblocks() //INFO:Miscellaneous_Interactionblocks
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:var1=hasresearchedtech,woodenplanks,1:==:true"});
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
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
		addTechnology(
				"crafting_table", new String[] {"Werkbank", "Craftingtable"},
				TechnologyType.SIMPLE, 1, PlayerAssociatedType.SOLO, 0, "", "interactionblocks", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Werkbank","&7Craftingtable"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Werkbank",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >Holzbretter< erforscht haben.",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung & Interaktion mit der Werkbank",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Craftingtable",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Woodenplanks<.",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting & Interaction with Craftingtable",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Werkbank","&7Craftingtable"},
				Material.CRAFTING_TABLE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Werkbank",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >Holzbretter< erforscht haben.",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung & Interaktion mit der Werkbank",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Craftingtable",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Planks<.",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting & Interaction with Craftingtable",
						"&cRightclick &bfor a more detailed view."},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Werkbank","&7Craftingtable"},
				Material.CRAFTING_TABLE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Werkbank",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >Holzbretter< erforscht haben.",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung & Interaktion mit der Werkbank",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Craftingtable",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Planks<.",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fCrafting & Interaction with Craftingtable",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&bWerkbank","&bCraftingtable"},
				Material.CRAFTING_TABLE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Werkbank",
						"&eSchaltet folgendes frei:",
						"&fHerstellung & Interaktion mit der Werkbank",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Craftingtable",
						"&eUnlocks the following:",
						"&fCrafting & Interaction with Craftingtable",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:var1=hasresearchedtech,stone_I,1:==:true"});
		toResCondition.put(2, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
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
		addTechnology(
				"furnace", new String[] {"Ofen", "Furnace"},
				TechnologyType.MULTIPLE, 2, PlayerAssociatedType.SOLO, 1, "", "interactionblocks", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Ofen","&7Furnace"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Ofen",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >Stein I< erforscht haben.",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&eSchaltet folgendes frei:",
						"&fHerstellung & Interaktion mit dem",
						"&fOfen, Schmelzofen und Ofenlore",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Furnace",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Stone I<.",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&eUnlocks the following:",
						"&fCrafting & Interaction with",
						"&fFurnace, Blastfurnace and Furnaceminecart",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&7Ofen","&7Furnace"},
				Material.FURNACE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Ofen",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >Stein I< erforscht haben.",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&eSchaltet folgendes frei:",
						"&fHerstellung & Interaktion mit dem",
						"&fOfen, Schmelzofen und Ofenlore",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Furnace",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Stone I<.",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&eUnlocks the following:",
						"&fCrafting & Interaction with",
						"&fFurnace, Blastfurnace and Furnaceminecart",
						"&cRightclick &bfor a more detailed view."},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Ofen","&7Furnace"},
				Material.FURNACE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Ofen",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >Stein I< erforscht haben.",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fHerstellung & Interaktion mit dem",
						"&fOfen, Schmelzofen und Ofenlore",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Furnace",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Stone I<.",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eUnlocks the following:",
						"&fCrafting & Interaction with",
						"&fFurnace, Blastfurnace and Furnaceminecart",
						"&cRightclick &bfor a more detailed view."},
				new String[] {"&bOfen","&bFurnace"},
				Material.FURNACE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Ofen",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >Stein I< erforscht haben.",
						"&eSchaltet folgendes frei:",
						"&fHerstellung & Interaktion mit dem",
						"&fOfen, Schmelzofen und Ofenlore",
						"&cRechtskick &bfür eine detailiertere Ansicht.",
						"&7Technology Furnace",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Stone I<.",
						"&eUnlocks the following:",
						"&fCrafting & Interaction with",
						"&fFurnace, Blastfurnace and Furnaceminecart",
						"&cRightclick &bfor a more detailed view."},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Mining_Soil() //INFO:Mining_Soil
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, 
				new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"});
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(7, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(7, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(7, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
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
				"BREAKING:DIRT:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
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
				"BREAKING:DIRT:null:tool=NETHERITHE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRASS_BLOCK:null:tool=NETHERITHE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SAND:null:tool=NETHERITHE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:GRAVEL:null:tool=NETHERITHE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
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
		addTechnology(
				"soil_I", new String[] {"Böden_I", "Dirt"}, TechnologyType.MULTIPLE, 7, PlayerAssociatedType.SOLO, 0, "", "soil", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Böden I","&7Soil I"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Böden I",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Erde/Grass/Sand/Kies",
						"&7Technology Soil I",
						"&eCost:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of dirt/grassblock/sand/gravel"},
				new String[] {"&7Böden I","&7Soil I"},
				Material.DIRT, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Böden I",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Erde/Grass/Sand/Kies",
						"&7Technology Soil I",
						"&eCost:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of dirt/grassblock/sand/gravel"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Böden I","&7Dirt"},
				Material.DIRT, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Böden I",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Erde/Grass/Sand/Kies",
						"&7Technology Soil I",
						"&eCost:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of dirt/grassblock/sand/gravel"},
				new String[] {"&bBöden I","&bSoil I"},
				Material.DIRT, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Böden I",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Erde/Grass/Sand/Kies",
						"&7Technology Soil I",
						"&eUnlocks the following:",
						"&fMining of dirt/grassblock/sand/gravel"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(7, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(7, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(7, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMaterial = new LinkedHashMap<>();
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
		addTechnology(
				"soil_II", new String[] {"Böden_II", "Soil_II"}, TechnologyType.MULTIPLE, 7, PlayerAssociatedType.SOLO, 1, "", "soil", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Böden II","&7Soil II"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Böden II",
						"",
						"&7Technology Soil II",
						""},
				new String[] {"&7Böden II","&7Soil II"},
				Material.ROOTED_DIRT, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Böden II",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Grobe Erde/Wurzelerde/Erdpfad/Podsol",
						"&7Technology Soil II",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining coarsedirt/rooteddirt/dirtpath/podzol"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Böden II","&7Soil II"}, Material.ROOTED_DIRT, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Böden II",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Grobe Erde/Wurzelerde/Erdpfad/Podsol",
						"&7Technology Soil II",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining coarsedirt/rooteddirt/dirtpath/podzol"},
				new String[] {"&bBöden II","&bSoil II"}, Material.ROOTED_DIRT, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Böden II",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Grobe Erde/Wurzelerde/Erdpfad/Podsol",
						"&7Technology Soil II",
						"&eUnlocks the following:",
						"&fMining coarsedirt/rooteddirt/dirtpath/podzol"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(7, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(7, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(7, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMaterial = new LinkedHashMap<>();
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
				"BREAKING:SNOW_BLOCK:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:TUFF:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"BREAKING:CLAY:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MUD:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MYCELIUM:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW_BLOCK:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:TUFF:null:tool=WOODEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"BREAKING:CLAY:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MUD:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MYCELIUM:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW_BLOCK:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:TUFF:null:tool=STONE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"BREAKING:CLAY:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MUD:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MYCELIUM:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW_BLOCK:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:TUFF:null:tool=IRON_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"BREAKING:CLAY:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MUD:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MYCELIUM:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW_BLOCK:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:TUFF:null:tool=GOLDEN_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"BREAKING:CLAY:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MUD:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MYCELIUM:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW_BLOCK:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:TUFF:null:tool=DIAMOND_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(7, new String[] {
				"BREAKING:CLAY:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MUD:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MYCELIUM:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SNOW_BLOCK:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:TUFF:null:tool=NETHERITE_SHOVEL:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:HAND:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:HAND:MUD:null:mat=MUD:1:1.0",
				"BREAKING:HAND:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:HAND:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:HAND:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0",
				"BREAKING:HAND:TUFF:null:mat=TUFF:1:1.0"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:WOODEN_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:WOODEN_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:WOODEN_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:WOODEN_SHOVEL:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:WOODEN_SHOVEL:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0",
				"BREAKING:WOODEN_SHOVEL:TUFF:null:mat=TUFF:1:1.0"});
		rewardDropChance.put(3, new String[] {
				"BREAKING:STONE_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:STONE_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:STONE_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:STONE_SHOVEL:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:STONE_SHOVEL:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0",
				"BREAKING:STONE_SHOVEL:TUFF:null:mat=TUFF:1:1.0"});
		rewardDropChance.put(4, new String[] {
				"BREAKING:IRON_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:IRON_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:IRON_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:IRON_SHOVEL:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:IRON_SHOVEL:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0",
				"BREAKING:IRON_SHOVEL:TUFF:null:mat=TUFF:1:1.0"});
		rewardDropChance.put(5, new String[] {
				"BREAKING:GOLDEN_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0",
				"BREAKING:GOLDEN_SHOVEL:TUFF:null:mat=TUFF:1:1.0"});
		rewardDropChance.put(6, new String[] {
				"BREAKING:DIAMOND_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0",
				"BREAKING:DIAMOND_SHOVEL:TUFF:null:mat=TUFF:1:1.0"});
		rewardDropChance.put(7, new String[] {
				"BREAKING:NETHERITE_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:SNOW:null:mat=SNOWBALL:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:SNOW_BLOCK:null:mat=SNOWBALL:4:1.0",
				"BREAKING:NETHERITE_SHOVEL:TUFF:null:mat=TUFF:1:1.0"});
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(2, new String[] {
				"BREAKING:WOODEN_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:WOODEN_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:WOODEN_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:WOODEN_SHOVEL:SNOW:null:mat=SNOW:1:1.0",
				"BREAKING:WOODEN_SHOVEL:SNOW_BLOCK:null:mat=SNOW_BLOCK:1:1.0",
				"BREAKING:WOODEN_SHOVEL:TUFF:null:mat=TUFF:1:1.0"});
		rewardSilkTouchDropChance.put(3, new String[] {
				"BREAKING:STONE_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:STONE_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:STONE_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:STONE_SHOVEL:SNOW:null:mat=SNOW:1:1.0",
				"BREAKING:STONE_SHOVEL:SNOW_BLOCK:null:mat=SNOW_BLOCK:1:1.0",
				"BREAKING:STONE_SHOVEL:TUFF:null:mat=TUFF:1:1.0"});
		rewardSilkTouchDropChance.put(4, new String[] {
				"BREAKING:IRON_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:IRON_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:IRON_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:IRON_SHOVEL:SNOW:null:mat=SNOW:1:1.0",
				"BREAKING:IRON_SHOVEL:SNOW_BLOCK:null:mat=SNOW_BLOCK:1:1.0",
				"BREAKING:IRON_SHOVEL:TUFF:null:mat=TUFF:1:1.0"});
		rewardSilkTouchDropChance.put(5, new String[] {
				"BREAKING:GOLDEN_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:SNOW:null:mat=SNOW:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:SNOW_BLOCK:null:mat=SNOW_BLOCK:1:1.0",
				"BREAKING:GOLDEN_SHOVEL:TUFF:null:mat=TUFF:1:1.0"});
		rewardSilkTouchDropChance.put(6, new String[] {
				"BREAKING:DIAMOND_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:SNOW:null:mat=SNOW:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:SNOW_BLOCK:null:mat=SNOW_BLOCK:1:1.0",
				"BREAKING:DIAMOND_SHOVEL:TUFF:null:mat=TUFF:1:1.0"});
		rewardSilkTouchDropChance.put(7, new String[] {
				"BREAKING:NETHERITE_SHOVEL:CLAY:null:mat=CLAY:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:MUD:null:mat=MUD:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:MYCELIUM:null:mat=MYCELIUM:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:SNOW:null:mat=SNOW:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:SNOW_BLOCK:null:mat=SNOW_BLOCK:1:1.0",
				"BREAKING:NETHERITE_SHOVEL:TUFF:null:mat=TUFF:1:1.0"});
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		addTechnology(
				"soil_III", new String[] {"Böden_III", "Soil_III"}, TechnologyType.MULTIPLE, 7, PlayerAssociatedType.SOLO, 2, "", "soil", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Böden III","&7Soil III"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Böden II",
						"",
						"&7Technology Soil II",
						""},
				new String[] {"&7Böden III","&7Soil III"},
				Material.CLAY, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Böden III",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Ton/Schlamm/Myzel/Schnee/Schneeblock/Tuffstein",
						"&7Technology Soil II",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining Clay/Mud/Mycelium/Snow/Snowblock/Tuff"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Böden III","&7Soil III"},
				Material.CLAY, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Böden III",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Ton/Schlamm/Myzel/Schnee/Schneeblock/Tuffstein",
						"&7Technology Soil II",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining Clay/Mud/Mycelium/Snow/Snowblock/Tuff"},
				new String[] {"&bBöden III","&bSoil III"},
				Material.CLAY, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Böden III",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Ton/Schlamm/Myzel/Schnee/Schneeblock/Tuffstein",
						"&7Technology Soil II",
						"&eUnlocks the following:",
						"&fMining Clay/Mud/Mycelium/Snow/Snowblock/Tuff"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Mining_Stone() //INFO:Mining_Stone
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
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
		addTechnology(
				"stone_I", new String[] {"Stein_I", "Stone_I"}, TechnologyType.MULTIPLE, 6, PlayerAssociatedType.SOLO, 0, "", "stone", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Stein I","&7Stone I"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Stein I",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Stein/Bruchstein",
						"&7Technology Stone I",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of Stone/Cobblestone"},
				new String[] {"&7Stein I","&7Stone I"},
				Material.STONE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Stein I",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Stein/Bruchstein",
						"&7Technology Stone I",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of Stone/Cobblestone"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Stein I","&7Stone I"},
				Material.STONE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Stein I",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Stein/Bruchstein",
						"&7Technology Stone I",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of Stone/Cobblestone"},
				new String[] {"&bStein I","&bStone I"},
				Material.STONE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Stein I",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Stein/Bruchstein",
						"&7Technology Stone I",
						"&eUnlocks the following:",
						"&fMining of Stone/Cobblestone"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
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
		addTechnology(
				"stone_II", new String[] {"Stein_II", "Stone_II"}, TechnologyType.MULTIPLE, 6, PlayerAssociatedType.SOLO, 1, "", "stone", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Stein_II","&7Stone_II"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Stein II",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Granit/Andesit/Diorit",
						"&7Technology Stone II",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of Granite/Andesite/Diorite"},
				new String[] {"&7Stein_II","&7Stone_II"},
				Material.GRANITE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Stein II",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Granit/Andesit/Diorit",
						"&7Technology Stone II",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of Granite/Andesite/Diorite"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Stein_II","&7Stone_II"},
				Material.GRANITE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Stein II",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Granit/Andesit/Diorit",
						"&7Technology Stone II",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of Granite/Andesite/Diorite"},
				new String[] {"&bStein_II","&7Stone_II"},
				Material.GRANITE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Stein II",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Granit/Andesit/Diorit",
						"&7Technology Stone II",
						"&eUnlocks the following:",
						"&fMining of Granite/Andesite/Diorite"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Mining_Ore() //INFO:Mining_Ore
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
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
		addTechnology(
				"coalore", new String[] {"Kohleerz", "Coalore"}, TechnologyType.MULTIPLE, 6, PlayerAssociatedType.SOLO, 0, "", "ore", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Kohleerz","&7Coalore"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Kohleerz",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Kohleerz/Tiefenschieferkohleerz",
						"&7Technology Coalore",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of coalore/deepslatecoalore"},
				new String[] {"&7Kohleerz","&7Coalore"},
				Material.COAL_ORE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Kohleerz",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f64x Bruchstein, 16x Erde",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Kohleerz/Tiefenschieferkohleerz",
						"&7Technology Coalore",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f64x Cobblestone, 16x Dirt",
						"&eUnlocks the following:",
						"&fMining of coalore/deepslatecoalore"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Kohleerz","&7Coalore"},
				Material.COAL_ORE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Kohleerz",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Kohleerz/Tiefenschieferkohleerz",
						"&7Technology Coalore",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of coalore/deepslatecoalore"},
				new String[] {"&7Kohleerz","&7Coalore"},
				Material.COAL_ORE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Kohleerz",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Kohleerz/Tiefenschieferkohleerz",
						"&7Technology Coalore",
						"&eUnlocks the following:",
						"&fMining of coalore/deepslatecoalore"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
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
				"BREAKING:STONE_PICKAXE:IRONL_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:STONE_PICKAXE:IRON_ORE:null:mat=RAW_IRON:4:0.125",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:4:0.125"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:IRON_PICKAXE:IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:IRON_PICKAXE:IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:IRON_PICKAXE:IRONL_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:IRON_PICKAXE:IRON_ORE:null:mat=RAW_IRON:4:0.125",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:4:0.125"});
		rewardDropChance.put(3, new String[] {
				"BREAKING:GOLDEN_PICKAXE:IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:GOLDEN_PICKAXE:IRONL_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:GOLDEN_PICKAXE:IRON_ORE:null:mat=RAW_IRON:4:0.125",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:4:0.125"});
		rewardDropChance.put(4, new String[] {
				"BREAKING:DIAMOND_PICKAXE:IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:DIAMOND_PICKAXE:IRONL_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:DIAMOND_PICKAXE:IRON_ORE:null:mat=RAW_IRON:4:0.125",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:4:0.125"});
		rewardDropChance.put(5, new String[] {
				"BREAKING:NETHERITE_PICKAXE:IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:NETHERITE_PICKAXE:IRONL_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:NETHERITE_PICKAXE:IRON_ORE:null:mat=RAW_IRON:4:0.125",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:2:0.5",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:3:0.25",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=RAW_IRON:4:0.125"});
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:STONE_PICKAXE:IRON_ORE:null:mat=IRON_ORE:1:1.0",
				"BREAKING:STONE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=DEEPSLATE_IRON_ORE:1:1.0"});
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:IRON_PICKAXE:IRON_ORE:null:mat=IRON_ORE:1:1.0",
				"BREAKING:IRON_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=DEEPSLATE_IRON_ORE:1:1.0"});
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:GOLDEN_PICKAXE:IRON_ORE:null:mat=IRON_ORE:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=DEEPSLATE_IRON_ORE:1:1.0"});
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:DIAMOND_PICKAXE:IRON_ORE:null:mat=IRON_ORE:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=DEEPSLATE_IRON_ORE:1:1.0"});
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:NETHERITE_PICKAXE:IRON_ORE:null:mat=IRON_ORE:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:DEEPSLATE_IRON_ORE:null:mat=DEEPSLATE_IRON_ORE:1:1.0"});
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		addTechnology(
				"ironore", new String[] {"Eisenerz", "Ironore"}, TechnologyType.MULTIPLE, 5, PlayerAssociatedType.SOLO, 1, "", "ore", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Eisenerz","&7Ironore"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Eisenerz",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Eisenerz/Tiefenschiefereisenerz",
						"&7Technology Ironore",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of ironore/deepslateironore"},
				new String[] {"&7Eisenerz","&7Ironore"}, Material.IRON_ORE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Eisenerz",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Eisenerz/Tiefenschiefereisenerz",
						"&7Technology Ironore",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of ironore/deepslateironore"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Eisenerz","&7Ironore"}, Material.IRON_ORE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Eisenerz",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Eisenerz/Tiefenschiefereisenerz",
						"&7Technology Ironore",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of ironore/deepslateironore"},
				new String[] {"&7Eisenerz","&7Coalore"}, Material.IRON_ORE, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Eisenerz",
						"&eSchaltet folgendes frei:",
						"&fAbbau von Eisenerz/Tiefenschiefereisenerz",
						"&7Technology Ironore",
						"&eUnlocks the following:",
						"&fMining of ironore/deepslateironore"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Woodworking_Sapling() //INFO:Woodworking_Sapling
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 25 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 2.5 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.25 * totalsolotech");
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"PLACING:OAK_SAPLING:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:OAK_SAPLING:null:tool=HAND"});
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:HAND:OAK_SAPLING:null:mat=OAK_SAPLING:1:1.0",
				""});
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:HAND:OAK_SAPLING:null:mat=OAK_SAPLING:1:1.0",
				""});
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		addTechnology(
				"oaksapling", new String[] {"Eichensetzling", "Oaksapling"},
				TechnologyType.SIMPLE, 1, PlayerAssociatedType.SOLO, 0, "", "sapling", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Eichensetzling","&7Oaksapling"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Eichensetzling",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&eSchaltet folgendes frei:",
						"&fSetzten von Eichensetzling",
						"&7Technology Oaksapling",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&eUnlocks the following:",
						"&fMining of oaksapling"},
				new String[] {"&7Eichensetzling","&7Oaksapling"},
				Material.OAK_SAPLING, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Eichensetzling",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&eSchaltet folgendes frei:",
						"&fSetzten von Eichensetzling",
						"&7Technology Oaksapling",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&eUnlocks the following:",
						"&fMining of oaksapling"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Eichensetzling","&7Oaksapling"},
				Material.OAK_SAPLING, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Eichensetzling",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Geld",
						"&eSchaltet folgendes frei:",
						"&fSetzten von Eichensetzling",
						"&7Technology Oaksapling",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&eUnlocks the following:",
						"&fMining of oaksapling"},
				new String[] {"&bEichensetzling","&bOaksapling"},
				Material.OAK_SAPLING, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Eichensetzling",
						"&eSchaltet folgendes frei:",
						"&fSetzten von Eichensetzling",
						"&7Technology Oaksapling",
						"&eUnlocks the following:",
						"&fMining of oaksapling"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 25 * totalsolotech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 2.5 * totalsolotech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.25 * totalsolotech");
		toResCostMaterial = new LinkedHashMap<>();
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"PLACING:SPRUCE_SAPLING:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",""});
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:SPRUCE_SAPLING:null:mat=SPRUCE_SAPLING:1:1.0",""});
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:SPRUCE_SAPLING:null:mat=SPRUCE_SAPLING:1:1.0",""});
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		addTechnology(
				"sprucesapling", new String[] {"Fichtensetzling", "Sprucesapling"},
				TechnologyType.SIMPLE, 1, PlayerAssociatedType.SOLO, 1, "", "sapling", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Fichtensetzling","&7Sprucesapling"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Fichtensetzling",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fSetzten von Fichtensetzling",
						"&7Technology Sprucesapling",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eUnlocks the following:",
						"&fMining of oaksapling"},
				new String[] {"&7Fichtensetzling","&7Sprucesapling"},
				Material.SPRUCE_SAPLING, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Fichtensetzling",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fSetzten von Fichtensetzling",
						"&7Technology Sprucesapling",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eUnlocks the following:",
						"&fMining of oaksapling"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Fichtensetzling","&7Sprucesapling"},
				Material.SPRUCE_SAPLING, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Fichtensetzling",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fSetzten von Fichtensetzling",
						"&7Technology Sprucesapling",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eUnlocks the following:",
						"&fMining of oaksapling"},
				new String[] {"&bFichtensetzling","&bSprucesapling"},
				Material.SPRUCE_SAPLING, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Fichtensetzling",
						"&eSchaltet folgendes frei:",
						"&fSetzten von Fichtensetzling",
						"&7Technology Sprucesapling",
						"&eUnlocks the following:",
						"&fMining of oaksapling"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
	}
	
	private void tech_Woodworking_Wood() //INFO:Woodworking_Wood
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		toResCondition.put(1,new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"});
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(7, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(7, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(7, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
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
				"BREAKING:SPRUCE_LOG:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:BIRCH_LOG:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:JUNGLE_LOG:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ACACIA_LOG:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DARK_OAK_LOG:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MANGROVE_LOG:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:CHERRY_LOG:null:tool=HAND:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(2, new String[] {
				"BREAKING:OAK_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SPRUCE_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:BIRCH_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:JUNGLE_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ACACIA_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DARK_OAK_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MANGROVE_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:CHERRY_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(3, new String[] {
				"BREAKING:OAK_LOG:null:tool=STONE_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SPRUCE_LOG:null:tool=STONE_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:BIRCH_LOG:null:tool=STONE_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:JUNGLE_LOG:null:tool=STONE_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ACACIA_LOG:null:tool=STONE_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DARK_OAK_LOG:null:tool=STONE_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MANGROVE_LOG:null:tool=STONE_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:CHERRY_LOG:null:tool=STONE_AXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(4, new String[] {
				"BREAKING:OAK_LOG:null:tool=IRON_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SPRUCE_LOG:null:tool=IRON_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:BIRCH_LOG:null:tool=IRON_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:JUNGLE_LOG:null:tool=IRON_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ACACIA_LOG:null:tool=IRON_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DARK_OAK_LOG:null:tool=IRON_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MANGROVE_LOG:null:tool=IRON_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:CHERRY_LOG:null:tool=IRON_AXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(5, new String[] {
				"BREAKING:OAK_LOG:null:tool=GOLDEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SPRUCE_LOG:null:tool=GOLDEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:BIRCH_LOG:null:tool=GOLDEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:JUNGLE_LOG:null:tool=GOLDEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ACACIA_LOG:null:tool=GOLDEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DARK_OAK_LOG:null:tool=GOLDEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MANGROVE_LOG:null:tool=GOLDEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:CHERRY_LOG:null:tool=GOLDEN_AXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(6, new String[] {
				"BREAKING:OAK_LOG:null:tool=DIAMOND_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SPRUCE_LOG:null:tool=DIAMOND_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:BIRCH_LOG:null:tool=DIAMOND_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:JUNGLE_LOG:null:tool=DIAMOND_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ACACIA_LOG:null:tool=DIAMOND_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DARK_OAK_LOG:null:tool=DIAMOND_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MANGROVE_LOG:null:tool=DIAMOND_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:CHERRY_LOG:null:tool=DIAMOND_AXE:ttexp=0.01:vault=0.1:default=0.1"});
		rewardUnlockableInteractions.put(7, new String[] {
				"BREAKING:OAK_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:SPRUCE_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:BIRCH_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:JUNGLE_LOG:null:tool=WOODEN_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:ACACIA_LOG:null:tool=NETHERITE_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:DARK_OAK_LOG:null:tool=NETHERITE_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:MANGROVE_LOG:null:tool=NETHERITE_AXE:ttexp=0.01:vault=0.1:default=0.1",
				"BREAKING:CHERRY_LOG:null:tool=NETHERITE_AXE:ttexp=0.01:vault=0.1:default=0.1"});
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		rewardDropChance.put(1, new String[] {
				"BREAKING:HAND:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:HAND:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:HAND:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:HAND:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:HAND:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:HAND:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:HAND:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:HAND:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		rewardDropChance.put(2, new String[] {
				"BREAKING:WOODEN_AXE:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		rewardDropChance.put(3, new String[] {
				"BREAKING:STONE_AXE:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:STONE_AXE:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:STONE_AXE:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:STONE_AXE:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:STONE_AXE:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:STONE_AXE:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:STONE_AXE:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:STONE_AXE:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		rewardDropChance.put(4, new String[] {
				"BREAKING:IRON_AXE:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:IRON_AXE:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:IRON_AXE:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:IRON_AXE:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:IRON_AXE:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:IRON_AXE:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:IRON_AXE:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:IRON_AXE:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		rewardDropChance.put(5, new String[] {
				"BREAKING:GOLDEN_AXE:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		rewardDropChance.put(6, new String[] {
				"BREAKING:DIAMOND_AXE:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		rewardDropChance.put(7, new String[] {
				"BREAKING:NETHERITE_AXE:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance.put(1, new String[] {
				"BREAKING:HAND:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:HAND:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:HAND:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:HAND:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:HAND:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:HAND:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:HAND:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:HAND:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		rewardSilkTouchDropChance.put(2, new String[] {
				"BREAKING:WOODEN_AXE:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		rewardSilkTouchDropChance.put(3, new String[] {
				"BREAKING:WOODEN_AXE:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:WOODEN_AXE:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		rewardSilkTouchDropChance.put(4, new String[] {
				"BREAKING:IRON_AXE:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:IRON_AXE:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:IRON_AXE:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:IRON_AXE:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:IRON_AXE:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:IRON_AXE:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:IRON_AXE:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:IRON_AXE:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		rewardSilkTouchDropChance.put(5, new String[] {
				"BREAKING:GOLDEN_AXE:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:GOLDEN_AXE:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		rewardSilkTouchDropChance.put(6, new String[] {
				"BREAKING:DIAMOND_AXE:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:DIAMOND_AXE:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		rewardSilkTouchDropChance.put(7, new String[] {
				"BREAKING:NETHERITE_AXE:OAK_LOG:null:mat=OAK_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:SPRUCE_LOG:null:mat=SPRUCE_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:BIRCH_LOG:null:mat=BIRCH_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:JUNGLE_LOG:null:mat=JUNGLE_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:ACACIA_LOG:null:mat=ACACIA_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:DARK_OAK_LOG:null:mat=DARK_OAK_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:MANGROVE_LOG:null:mat=MANGROVE_LOG:1:1.0",
				"BREAKING:NETHERITE_AXE:CHERRY_LOG:null:mat=CHERRY_LOG:1:1.0"});
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		addTechnology(
				"woodenlog", new String[] {"Holzstamm", "Woodenlog"},
				TechnologyType.MULTIPLE, 7, PlayerAssociatedType.SOLO, 1, "", "wood", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Holzstamm","&7Woodenlog"}, Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Holzstamm",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von allen Holzstämme",
						"&7Technology Woodenlog",
						"&eCost:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of all woodenlog"},
				new String[] {"&7Eichenstamm","&7Oaklog"}, Material.OAK_LOG, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Holzstamm",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von allen Holzstämme",
						"&7Technology Woodenlog",
						"&eCost:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of all woodenlog"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Eichenstamm","&7Oaklog"}, Material.OAK_LOG, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Holzstamm",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eSchaltet folgendes frei:",
						"&fAbbau von allen Holzstämme",
						"&7Technology Woodenlog",
						"&eCost:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&f%costmaterial%",
						"&eUnlocks the following:",
						"&fMining of all woodenlog"},
				new String[] {"&7Eichenstamm","&7Oaklog"}, Material.OAK_LOG, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Holzstamm",
						"&eSchaltet folgendes frei:",
						"&fAbbau von allen Holzstämme",
						"&7Technology Woodenlog",
						"&eUnlocks the following:",
						"&fMining of all woodenlog"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1,new String[] {
						"if:(a):o_1",
						"output:o_1:true",
						"a:var1=hasresearchedtech,woodenlog,1:==:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(3, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(4, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(5, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(6, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostTTExp.put(7, "100 * techlev + 50 * techacq + 25 * totalsolotech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(3, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(4, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(5, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(6, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostVanillaExp.put(7, "10 * techlev + 5 * techacq + 2.5 * totalsolotech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(3, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(4, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(5, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(6, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
		toResCostMoney.put(7, "1 * techlev + 0.5 * techacq + 0.25 * totalsolotech");
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
				"CRAFTING:CHERRY_PLANKS:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",
				"BREAKING:OAK_PLANKS:null:tool=HAND",
				"BREAKING:SPRUCE_PLANKS:null:tool=HAND",
				"BREAKING:BIRCH_PLANKS:null:tool=HAND",
				"BREAKING:JUNGLE_PLANKS:null:tool=HAND",
				"BREAKING:ACACIA_PLANKS:null:tool=HAND",
				"BREAKING:DARK_OAK_PLANKS:null:tool=HAND",
				"BREAKING:MANGROVE_PLANKS:null:tool=HAND",
				"BREAKING:CHERRY_PLANKS:null:tool=HAND",
				"BREAKING:OAK_PLANKS:null:tool=WOODEN_AXE",
				"BREAKING:SPRUCE_PLANKS:null:tool=WOODEN_AXE",
				"BREAKING:BIRCH_PLANKS:null:tool=WOODEN_AXE",
				"BREAKING:JUNGLE_PLANKS:null:tool=WOODEN_AXE",
				"BREAKING:ACACIA_PLANKS:null:tool=WOODEN_AXE",
				"BREAKING:DARK_OAK_PLANKS:null:tool=WOODEN_AXE",
				"BREAKING:MANGROVE_PLANKS:null:tool=WOODEN_AXE",
				"BREAKING:CHERRY_PLANKS:null:tool=WOODEN_AXE",
				"BREAKING:OAK_PLANKS:null:tool=STONE_AXE",
				"BREAKING:SPRUCE_PLANKS:null:tool=STONE_AXE",
				"BREAKING:BIRCH_PLANKS:null:tool=STONE_AXE",
				"BREAKING:JUNGLE_PLANKS:null:tool=STONE_AXE",
				"BREAKING:ACACIA_PLANKS:null:tool=STONE_AXE",
				"BREAKING:DARK_OAK_PLANKS:null:tool=STONE_AXE",
				"BREAKING:MANGROVE_PLANKS:null:tool=STONE_AXE",
				"BREAKING:CHERRY_PLANKS:null:tool=STONE_AXE",
				"BREAKING:OAK_PLANKS:null:tool=IRON_AXE",
				"BREAKING:SPRUCE_PLANKS:null:tool=IRON_AXE",
				"BREAKING:BIRCH_PLANKS:null:tool=IRON_AXE",
				"BREAKING:JUNGLE_PLANKS:null:tool=IRON_AXE",
				"BREAKING:ACACIA_PLANKS:null:tool=IRON_AXE",
				"BREAKING:DARK_OAK_PLANKS:null:tool=IRON_AXE",
				"BREAKING:MANGROVE_PLANKS:null:tool=IRON_AXE",
				"BREAKING:CHERRY_PLANKS:null:tool=IRON_AXE",
				"BREAKING:OAK_PLANKS:null:tool=GOLDEN_AXE",
				"BREAKING:SPRUCE_PLANKS:null:tool=GOLDEN_AXE",
				"BREAKING:BIRCH_PLANKS:null:tool=GOLDEN_AXE",
				"BREAKING:JUNGLE_PLANKS:null:tool=GOLDEN_AXE",
				"BREAKING:ACACIA_PLANKS:null:tool=GOLDEN_AXE",
				"BREAKING:DARK_OAK_PLANKS:null:tool=GOLDEN_AXE",
				"BREAKING:MANGROVE_PLANKS:null:tool=GOLDEN_AXE",
				"BREAKING:CHERRY_PLANKS:null:tool=GOLDEN_AXE",
				"BREAKING:OAK_PLANKS:null:tool=DIAMOND_AXE",
				"BREAKING:SPRUCE_PLANKS:null:tool=DIAMOND_AXE",
				"BREAKING:BIRCH_PLANKS:null:tool=DIAMOND_AXE",
				"BREAKING:JUNGLE_PLANKS:null:tool=DIAMOND_AXE",
				"BREAKING:ACACIA_PLANKS:null:tool=DIAMOND_AXE",
				"BREAKING:DARK_OAK_PLANKS:null:tool=DIAMOND_AXE",
				"BREAKING:MANGROVE_PLANKS:null:tool=DIAMOND_AXE",
				"BREAKING:CHERRY_PLANKS:null:tool=DIAMOND_AXE",
				"BREAKING:OAK_PLANKS:null:tool=WOODEN_AXE",
				"BREAKING:SPRUCE_PLANKS:null:tool=WOODEN_AXE",
				"BREAKING:BIRCH_PLANKS:null:tool=WOODEN_AXE",
				"BREAKING:JUNGLE_PLANKS:null:tool=WOODEN_AXE",
				"BREAKING:ACACIA_PLANKS:null:tool=NETHERITE_AXE",
				"BREAKING:DARK_OAK_PLANKS:null:tool=NETHERITE_AXE",
				"BREAKING:MANGROVE_PLANKS:null:tool=NETHERITE_AXE",
				"BREAKING:CHERRY_PLANKS:null:tool=NETHERITE_AXE"});
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {
				"SHAPELESS:oak_planks",
				"SHAPELESS:spruce_planks",
				"SHAPELESS:birck_planks",
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
				"BREAKING:HAND:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0",
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
				"BREAKING:HAND:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:WOODEN_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:STONE_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:IRON_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:GOLDEN_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:OAK_PLANKS:null:mat=OAK_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:SPRUCE_PLANKS:null:mat=SPRUCE_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:BIRCH_PLANKS:null:mat=BIRCH_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:JUNGLE_PLANKS:null:mat=JUNGLE_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:ACACIA_PLANKS:null:mat=ACACIA_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:DARK_OAK_PLANKS:null:mat=DARK_OAK_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:MANGROVE_PLANKS:null:mat=MANGROVE_PLANKS:1:1.0",
				"BREAKING:DIAMOND_AXE:CHERRY_PLANKS:null:mat=CHERRY_PLANKS:1:1.0",
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
		addTechnology(
				"woodenplanks", new String[] {"Holzbretter", "Woodenplanks"},
				TechnologyType.SIMPLE, 1, PlayerAssociatedType.SOLO, 2, "", "wood", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:var1=hasresearchedtech,woodenlog,1:==:true"}, true,
				new String[] {"&7Holzbretter","&7Woodenplanks"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Holzbretter",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >Holzstämme< erforscht haben.",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von allen Holzbretter",
						"&7Technology Woodenplanks",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Woodenlog<.",
						"&eCost:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eUnlocks the following:",
						"&fCrafting of all woodenplanks"},
				new String[] {"&7Holzbretter","&7Woodenplanks"},
				Material.OAK_PLANKS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Holzbretter",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >Holzstämme< erforscht haben.",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von allen Holzbretter",
						"&7Technology Woodenplanks",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Woodenlog<.",
						"&eCost:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eUnlocks the following:",
						"&fCrafting of all woodenplanks"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Holzbretter","&7Woodenplanks"},
				Material.OAK_PLANKS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Holzbretter",
						"&cAnforderungen zum einsehen:",
						"&cMuss die Technology >Holzstämme< erforscht haben.",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von allen Holzbretter",
						"&7Technology Woodenplanks",
						"&cRequirements to view:",
						"&cMust have researched the Technology >Woodenlog<.",
						"&eCost:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eUnlocks the following:",
						"&fCrafting of all woodenplanks"},
				new String[] {"&7Holzbretter","&7Woodenplanks"},
				Material.OAK_PLANKS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Holzbretter",
						"&eSchaltet folgendes frei:",
						"&fHerstellen von allen Holzbretter",
						"&7Technology Woodenplanks",
						"&eUnlocks the following:",
						"&fCrafting of all woodenplanks"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry);
	}
	
	private void tech_Stonemason_Slabs() //INFO:Stonemason_Slabs
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 25 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 2.5 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.25 * totalsolotech");
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"PLACING:COBBLESTONE_SLAB:null:tool=HAND",
				"BREAKING:COBBLESTONE_SLAB:null:tool=HAND",
				"PLACING:COBBLESTONE_SLAB:null:tool=WOODEN_PICKAXE",
				"BREAKING:COBBLESTONE_SLAB:null:tool=WOODEN_PICKAXE",
				"PLACING:COBBLESTONE_SLAB:null:tool=STONE_PICKAXE",
				"BREAKING:COBBLESTONE_SLAB:null:tool=STONE_PICKAXE",
				"PLACING:COBBLESTONE_SLAB:null:tool=IRON_PICKAXE",
				"BREAKING:COBBLESTONE_SLAB:null:tool=IRON_PICKAXE",
				"PLACING:COBBLESTONE_SLAB:null:tool=GOLDEN_PICKAXE",
				"BREAKING:COBBLESTONE_SLAB:null:tool=GOLDEN_PICKAXE",
				"PLACING:COBBLESTONE_SLAB:null:tool=DIAMOND_PICKAXE",
				"BREAKING:COBBLESTONE_SLAB:null:tool=DIAMOND_PICKAXE",
				"PLACING:COBBLESTONE_SLAB:null:tool=NETHERITE_PICKAXE",
				"BREAKING:COBBLESTONE_SLAB:null:tool=NETHERITE_PICKAXE"});
		rewardUnlockableInteractions.put(2, new String[] {
				"PLACING:STONE_SLAB:null:tool=HAND",
				"BREAKING:STONE_SLAB:null:tool=HAND",
				"PLACING:STONE_SLAB:null:tool=WOODEN_PICKAXE",
				"BREAKING:STONE_SLAB:null:tool=WOODEN_PICKAXE",
				"PLACING:STONE_SLAB:null:tool=STONE_PICKAXE",
				"BREAKING:STONE_SLAB:null:tool=STONE_PICKAXE",
				"PLACING:STONE_SLAB:null:tool=IRON_PICKAXE",
				"BREAKING:STONE_SLAB:null:tool=IRON_PICKAXE",
				"PLACING:STONE_SLAB:null:tool=GOLDEN_PICKAXE",
				"BREAKING:STONE_SLAB:null:tool=GOLDEN_PICKAXE",
				"PLACING:STONE_SLAB:null:tool=DIAMOND_PICKAXE",
				"BREAKING:STONE_SLAB:null:tool=DIAMOND_PICKAXE",
				"PLACING:STONE_SLAB:null:tool=NETHERITE_PICKAXE",
				"BREAKING:STONE_SLAB:null:tool=NETHERITE_PICKAXE"});
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
		rewardSilkTouchDropChance.put(1, new String[] {
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
		addTechnology(
				"stoneslap_I", new String[] {"Steinstufen_I", "Stoneslaps_I"},
				TechnologyType.MULTIPLE, 2, PlayerAssociatedType.SOLO, 0, "", "stoneslaps", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Steinstufen I","&7Stoneslaps I"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Steinstufen I",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fHerstellen, Setzten und Abbauen",
						"&fvon Steinstufen/Bruchsteinstufen",
						"&7Technology Stoneslaps I",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&eUnlocks the following:",
						"&fCrafting, placing and mining",
						"&fof stoneslabs/cobblestoneslabs"},
				new String[] {"&7Steinstufen I","&7Stoneslaps I"},
				Material.STONE_SLAB, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Steinstufen I",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fHerstellen, Setzten und Abbauen",
						"&fvon Steinstufen/Bruchsteinstufen",
						"&7Technology Stoneslaps I",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&eUnlocks the following:",
						"&fCrafting, placing and mining",
						"&fof stoneslabs/cobblestoneslabs"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Steinstufen I","&7Stoneslaps I"},
				Material.STONE_SLAB, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Steinstufen I",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fHerstellen, Setzten und Abbauen",
						"&fvon Steinstufen/Bruchsteinstufen",
						"&7Technology Stoneslaps I",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&eUnlocks the following:",
						"&fCrafting, placing and mining",
						"&fof stoneslabs/cobblestoneslabs"},
				new String[] {"&bSteinstufen I","&bStoneslaps I"},
				Material.STONE_SLAB, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Steinsstufen I",
						"&eSchaltet folgendes frei:",
						"&fHerstellen, Setzten und Abbauen",
						"&fvon Steinstufen/Bruchsteinstufen",
						"&7Technology Stoneslaps I",
						"&eUnlocks the following:",
						"&fCrafting, placing and mining",
						"&fof stoneslabs/cobblestoneslabs"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Stonemason_Stonestairs() //INFO:Stonemason_Stonestairs
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:true"});
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 25 * totalsolotech");
		toResCostTTExp.put(2, "100 * techlev + 25 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 2.5 * totalsolotech");
		toResCostVanillaExp.put(2, "10 * techlev + 2.5 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.25 * totalsolotech");
		toResCostMoney.put(2, "1 * techlev + 0.25 * totalsolotech");
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] {
				"PLACING:COBBLESTONE_STAIRS:null:tool=HAND",
				"BREAKING:COBBLESTONE_STAIRS:null:tool=HAND",
				"PLACING:COBBLESTONE_STAIRS:null:tool=WOODEN_PICKAXE",
				"BREAKING:COBBLESTONE_STAIRSnull:tool=WOODEN_PICKAXE",
				"PLACING:COBBLESTONE_STAIRS:null:tool=STONE_PICKAXE",
				"BREAKING:COBBLESTONE_STAIRS:null:tool=STONE_PICKAXE",
				"PLACING:COBBLESTONE_STAIRS:null:tool=IRON_PICKAXE",
				"BREAKING:COBBLESTONE_STAIRS:null:tool=IRON_PICKAXE",
				"PLACING:COBBLESTONE_STAIRS:null:tool=GOLDEN_PICKAXE",
				"BREAKING:COBBLESTONE_STAIRS:null:tool=GOLDEN_PICKAXE",
				"PLACING:COBBLESTONE_STAIRS:null:tool=DIAMOND_PICKAXE",
				"BREAKING:COBBLESTONE_STAIRS:null:tool=DIAMOND_PICKAXE",
				"PLACING:COBBLESTONE_STAIRS:null:tool=NETHERITE_PICKAXE",
				"BREAKING:COBBLESTONE_STAIRS:null:tool=NETHERITE_PICKAXE"});
		rewardUnlockableInteractions.put(2, new String[] {
				"PLACING:STONE_STAIRS:null:tool=HAND",
				"BREAKING:STONE_STAIRS:null:tool=HAND",
				"PLACING:STONE_STAIRS:null:tool=WOODEN_PICKAXE",
				"BREAKING:STONE_STAIRS:null:tool=WOODEN_PICKAXE",
				"PLACING:STONE_STAIRS:null:tool=STONE_PICKAXE",
				"BREAKING:STONE_STAIRS:null:tool=STONE_PICKAXE",
				"PLACING:STONE_STAIRS:null:tool=IRON_PICKAXE",
				"BREAKING:STONE_STAIRS:null:tool=IRON_PICKAXE",
				"PLACING:STONE_STAIRS:null:tool=GOLDEN_PICKAXE",
				"BREAKING:STONE_STAIRS:null:tool=GOLDEN_PICKAXE",
				"PLACING:STONE_STAIRS:null:tool=DIAMOND_PICKAXE",
				"BREAKING:STONE_STAIRS:null:tool=DIAMOND_PICKAXE",
				"PLACING:STONE_STAIRS:null:tool=NETHERITE_PICKAXE",
				"BREAKING:STONE_STAIRS:null:tool=NETHERITE_PICKAXE"});
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {
				"SHAPED:cobblestone_stairs",
				"STONECUTTING:cobblestone_stairs_from_cobblestone_stonecutting"});
		rewardUnlockableRecipe.put(2, new String[] {
				"SHAPED:stone_slab",
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
				"BREAKING:HAND:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:WOODEN_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:STONE_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:IRON_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0"});
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
				"BREAKING:HAND:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:WOODEN_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:STONE_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:IRON_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:GOLDEN_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:DIAMOND_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0",
				"BREAKING:NETHERITE_PICKAXE:COBBLESTONE_STAIRS:null:mat=COBBLESTONE_STAIRS:1:1.0"});
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		addTechnology(
				"stonestairs_I", new String[] {"Steintreppen_I", "Stonestairs_I"},
				TechnologyType.MULTIPLE, 2, PlayerAssociatedType.SOLO, 0, "", "stonestairs", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Steintreppen I","&7Stonestairs I"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Steintreppen I",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fHerstellen, Setzten und Abbauen",
						"&fvon Bruchsteintreppen/Steintreppen",
						"&7Technology Stonestairs I",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&eUnlocks the following:",
						"&fCrafting, placing and mining",
						"&fof cobblestonestairs/stonestairs"},
				new String[] {"&7Steintreppen I","&7Stonestairs I"},
				Material.STONE_STAIRS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Steintreppen I",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fHerstellen, Setzten und Abbauen",
						"&fvon Bruchsteintreppen/Steintreppen",
						"&7Technology Stonestairs I",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&eUnlocks the following:",
						"&fCrafting, placing and mining",
						"&fof cobblestonestairs/stonestairs"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Steintreppen I","&7Stonestairs I"},
				Material.STONE_STAIRS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Steintreppen I",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fHerstellen, Setzten und Abbauen",
						"&fvon Bruchsteintreppen/Steintreppen",
						"&7Technology Stonestairs I",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney% Money",
						"&eUnlocks the following:",
						"&fCrafting, placing and mining",
						"&fof cobblestonestairs/stonestairs"},
				new String[] {"&bSteintreppen I","&bStonestairs I"},
				Material.STONE_STAIRS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Steintreppen I",
						"&eSchaltet folgendes frei:",
						"&fHerstellen, Setzten und Abbauen",
						"&fvon Bruchsteintreppen/Steintreppen",
						"&7Technology Stonestairs I",
						"&eUnlocks the following:",
						"&fCrafting, placing and mining",
						"&fof cobblestonestairs/stonestairs"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}
	
	private void tech_Tablerecipe_Furnancerecipe() //INFO:Tablerecipe_Furnancerecipe
	{
		LinkedHashMap<Integer, String[]> toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:var1=hasresearchedtech,furnace,1:==:true"});
		LinkedHashMap<Integer, String> toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 25 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 2.5 * totalsolotech");
		LinkedHashMap<Integer, String> toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.25 * totalsolotech");
		LinkedHashMap<Integer, String[]> toResCostMaterial = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] 
				{"MELTING:STONE:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",""});
		LinkedHashMap<Integer, String[]> rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {"FURNACE:stone",""});
		LinkedHashMap<Integer, String[]> rewardDropChance = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardSilkTouchDropChance = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardCommand = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardItem = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardModifier = new LinkedHashMap<>();
		LinkedHashMap<Integer, String[]> rewardValueEntry = new LinkedHashMap<>();
		addTechnology(
				"melting_cobblestone", new String[] {"Ofenrezept_Stein", "Furnacerecipe_Stone"},
				TechnologyType.MULTIPLE, 2, PlayerAssociatedType.SOLO, 0, "", "furnancerecipe", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Ofenrezept Stein","&7Furnacerecipe Stone"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Ofenrezept Stein",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fSchmelzen von Bruchstein in Stein",
						"&7Technology Furnacerecipe Stone",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eUnlocks the following:",
						"&fMelting of Cobblestone in Stone"},
				new String[] {"&7Ofenrezept Stein","&7Furnacerecipe Stone"},
				Material.STONE_STAIRS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Ofenrezept Stein",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fSchmelzen von Bruchstein in Stein",
						"&7Technology Furnacerecipe Stone",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eUnlocks the following:",
						"&fMelting of Cobblestone in Stone"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Ofenrezept Stein","&7Furnacerecipe Stone"},
				Material.STONE_STAIRS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Ofenrezept Stein",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fSchmelzen von Bruchstein in Stein",
						"&7Technology Furnacerecipe Stone",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eUnlocks the following:",
						"&fMelting of Cobblestone in Stone"},
				new String[] {"&bOfenrezept Stein","&bFurnacerecipe Stone"},
				Material.STONE_STAIRS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Ofenrezept Stein",
						"&eSchaltet folgendes frei:",
						"&fSchmelzen von Bruchstein in Stein",
						"&7Technology Furnacerecipe Stone",
						"&eUnlocks the following:",
						"&fMelting of Cobblestone in Stone"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
		toResCondition = new LinkedHashMap<>();
		toResCondition.put(1, new String[] {
				"if:(a):o_1",
				"output:o_1:true",
				"a:var1=hasresearchedtech,furnace,1:==:true"});
		toResCostTTExp = new LinkedHashMap<>();
		toResCostTTExp.put(1, "100 * techlev + 25 * totalsolotech");
		toResCostVanillaExp = new LinkedHashMap<>();
		toResCostVanillaExp.put(1, "10 * techlev + 2.5 * totalsolotech");
		toResCostMoney = new LinkedHashMap<>();
		toResCostMoney.put(1, "1 * techlev + 0.25 * totalsolotech");
		toResCostMaterial = new LinkedHashMap<>();
		rewardUnlockableInteractions = new LinkedHashMap<>();
		rewardUnlockableInteractions.put(1, new String[] 
				{"MELTING:IRON_INGOT:null:tool=HAND:ttexp=0.1:vault=0.5:default=0.5",""});
		rewardUnlockableRecipe = new LinkedHashMap<>();
		rewardUnlockableRecipe.put(1, new String[] {"FURNACE:stone",""});
		rewardDropChance = new LinkedHashMap<>();
		rewardSilkTouchDropChance = new LinkedHashMap<>();
		rewardCommand = new LinkedHashMap<>();
		rewardItem = new LinkedHashMap<>();
		rewardModifier = new LinkedHashMap<>();
		rewardValueEntry = new LinkedHashMap<>();
		addTechnology(
				"melting_raw_iron", new String[] {"Ofenrezept_Eisenbarren", "Furnacerecipe_Ironingot"},
				TechnologyType.SIMPLE, 1, PlayerAssociatedType.SOLO, 1, "", "furnancerecipe", 
				0, 0, 0, 0, 0, 0, 0, 0,
				new String[] { //ConditionToSee
						"if:(a):o_1",
						"output:o_1:true",
						"a:true"}, true,
				new String[] {"&7Ofenrezept Eisenbarren","&7Furnacerecipe Ironingot"},
				Material.BARRIER, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Ofenrezept Eisenbarren",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fSchmelzen von Roheisen in Eisenbarren",
						"&7Technology Furnacerecipe Ironingot",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eUnlocks the following:",
						"&fMelting of Rawiron in Ironingot"},
				new String[] {"&7Ofenrezept Eisenbarren","&7Furnacerecipe Ironingot"},
				Material.STONE_STAIRS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Ofenrezept Eisenbarren",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fSchmelzen von Roheisen in Eisenbarren",
						"&7Technology Furnacerecipe Ironingot",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eUnlocks the following:",
						"&fMelting of Rawiron in Ironingot"},
				toResCondition,	toResCostTTExp,	toResCostVanillaExp, toResCostMoney, toResCostMaterial,
				new String[] {"&7Ofenrezept Eisenbarren","&7Furnacerecipe Ironingot"},
				Material.STONE_STAIRS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Ofenrezept Eisenbarren",
						"&eKosten:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eSchaltet folgendes frei:",
						"&fSchmelzen von Roheisen in Eisenbarren",
						"&7Technology Furnacerecipe Ironingot",
						"&eCosts:",
						"&f%costttexp% | %costvanillaexp%",
						"&f%costmoney%",
						"&eUnlocks the following:",
						"&fMelting of Rawiron in Ironingot"},
				new String[] {"&bOfenrezept Eisenbarren","&bFurnacerecipe Ironingot"},
				Material.STONE_STAIRS, 1, new String[] {"",""}, new String[] {"",""}, new String[] {
						"&7Technologie Ofenrezept Eisenbarren",
						"&eSchaltet folgendes frei:",
						"&fSchmelzen von Roheisen in Eisenbarren",
						"&7Technology Furnacerecipe Ironingot",
						"&eUnlocks the following:",
						"&fMelting of Rawiron in Ironingot"},
				rewardUnlockableInteractions, rewardUnlockableRecipe, rewardDropChance, rewardSilkTouchDropChance, 
				rewardCommand, rewardItem, rewardModifier, rewardValueEntry
				);
	}

	private void tech_Tablerecipe_Enchantmentrecipe() //INFO:Tablerecipe_Enchantmentrecipe
	{
		
	}

	private void tech_Tablerecipe_Brewingrecipe() //INFO:Tablerecipe_Brewingrecipe
	{
		
	}

	private void tech_Tablerecipe_Anvilrecipe() //INFO:ablerecipe_Anvilrecipe
	{
		
	}
	
	private void tech_Tablerecipe_Forgingcerecipe() //INFO:Tablerecipe_Forgingcerecipe
	{
		
	}
	
	private void tech_Booster_Miningbooster() //INFO:Booster_Miningbooster
	{
		
	}
	
	private void tech_Booster_Craftingbooster() //INFO:Booster_Craftingbooster
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
			String[] canResDisplayname, Material canResMat, int canResAmount, String[] canResItemFlag, String[] canResEnchantments, String[] canResLore,
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
		one.put("RequirementToSee.ConditionQuery", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						toSeeConditionQuery));
		one.put("RequirementToSee.ShowDifferentItemIfYouNormallyDontSeeIt", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						showDifferentItemIfYouNormallyDontSeeIt}));
		//--- ToSee - ItemIfYouCannotSee ---
		one.put("RequirementToSee.ItemIfYouCannotSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						notSeeDisplayname));
		one.put("RequirementToSee.ItemIfYouCannotSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeMat.toString()}));
		one.put("RequirementToSee.ItemIfYouCannotSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						notSeeAmount}));
		one.put("RequirementToSee.ItemIfYouCannotSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						notSeeItemFlag));
		one.put("RequirementToSee.ItemIfYouCannotSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						notSeeEnchantments));
		one.put("RequirementToSee.ItemIfYouCannotSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						notSeeLore));
		//--- ToSee - ItemIfYouCanSee ---
		one.put("RequirementToSee.ItemIfYouCanSee.Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						canSeeDisplayname));
		one.put("RequirementToSee.ItemIfYouCanSee.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeMat.toString()}));
		one.put("RequirementToSee.ItemIfYouCanSee.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canSeeAmount}));
		one.put("RequirementToSee.ItemIfYouCanSee.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						canSeeItemFlag));
		one.put("RequirementToSee.ItemIfYouCanSee.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						canSeeEnchantments));
		one.put("RequirementToSee.ItemIfYouCanSee.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						canSeeLore));
		
		for(Entry<Integer, String[]> e : toResConditionQuery.entrySet())
		{
			one.put("RequirementToResearch.ConditionQuery."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					e.getValue()));
		}
		
		for(Entry<Integer, String> e : toResCostTTExp.entrySet())
		{
			one.put("RequirementToResearch.Costs.TTExp."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					e.getValue()}));
		}
		for(Entry<Integer, String> e : toResCostVanillaExp.entrySet())
		{
			one.put("RequirementToResearch.Costs.VanillaExp."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					e.getValue()}));
		}
		for(Entry<Integer, String> e : toResCostMoney.entrySet())
		{
			one.put("RequirementToResearch.Costs.Money."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					e.getValue()}));
		}
		for(Entry<Integer, String[]> e : toResCostMaterial.entrySet())
		{
			one.put("RequirementToResearch.Costs.Money."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER}, 
					e.getValue()));
		}
		//--- ToResearch - IfYouCanResearchIt ---
		one.put("RequirementToResearch.IfYouCanResearchIt.Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						canResDisplayname));
		one.put("RequirementToResearch.IfYouCanResearchIt.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canResMat.toString()}));
		one.put("RequirementToResearch.IfYouCanResearchIt.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						canResAmount}));
		one.put("RequirementToResearch.IfYouCanResearchIt.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						canResItemFlag));
		one.put("RequirementToResearch.IfYouCanResearchIt.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						canResEnchantments));
		one.put("RequirementToResearch.IfYouCanResearchIt.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						canResLore));
		//--- ToResearch - ItemIfYouHaveResearchedIt ---
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						hadResDisplayname));
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Material", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						hadResMat.toString()}));
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Amount", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						hadResAmount}));
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.ItemFlag", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						hadResItemFlag));
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Enchantment", new Language(new ISO639_2B[] {ISO639_2B.GER}, 
						hadResEnchantments));
		one.put("RequirementToResearch.ItemIfYouHaveResearchedIt.Lore", new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, 
						hadResLore));
		
		for(Entry<Integer, String[]> e : rewardUnlockableInteractions.entrySet())
		{
			one.put("Rewards.UnlockableInteractions."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
					e.getValue()));
		}
		for(Entry<Integer, String[]> e : rewardUnlockableRecipe.entrySet())
		{
			one.put("Rewards.UnlockableRecipe."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
					e.getValue()));
		}
		for(Entry<Integer, String[]> e : rewardDropChance.entrySet())
		{
			one.put("Rewards.DropChance."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
					e.getValue()));
		}
		for(Entry<Integer, String[]> e : rewardSilkTouchDropChance.entrySet())
		{
			one.put("Rewards.SilkTouchDropChance."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
					e.getValue()));
		}
		for(Entry<Integer, String[]> e : rewardCommand.entrySet())
		{
			one.put("Rewards.Command."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
					e.getValue()));
		}
		for(Entry<Integer, String[]> e : rewardItem.entrySet())
		{
			one.put("Rewards.Item."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
					e.getValue()));
		}
		for(Entry<Integer, String[]> e : rewardModifier.entrySet())
		{
			one.put("Rewards.Modifier."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
					e.getValue()));
		}
		for(Entry<Integer, String[]> e : rewardValueEntry.entrySet())
		{
			one.put("Rewards.ValueEntry."+e.getKey(), new Language(new ISO639_2B[] {ISO639_2B.GER},
					e.getValue()));
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