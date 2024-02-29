package main.java.me.avankziar.tt.spigot;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.ifh.general.conditionqueryparser.ConditionQueryParser;
import main.java.me.avankziar.ifh.general.interfaces.PlayerTimes;
import main.java.me.avankziar.ifh.general.modifier.ModificationType;
import main.java.me.avankziar.ifh.general.modifier.Modifier;
import main.java.me.avankziar.ifh.general.valueentry.ValueEntry;
import main.java.me.avankziar.ifh.spigot.administration.Administration;
import main.java.me.avankziar.ifh.spigot.economy.Economy;
import main.java.me.avankziar.ifh.spigot.interfaces.BungeeOnlinePlayers;
import main.java.me.avankziar.ifh.spigot.interfaces.EnumTranslation;
import main.java.me.avankziar.ifh.spigot.tobungee.chatlike.BaseComponentToBungee;
import main.java.me.avankziar.ifh.spigot.tobungee.chatlike.MessageToBungee;
import main.java.me.avankziar.ifh.spigot.tobungee.commands.CommandToBungee;
import main.java.me.avankziar.tt.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmd.TTCommandExecutor;
import main.java.me.avankziar.tt.spigot.cmd.TabCompletion;
import main.java.me.avankziar.tt.spigot.cmd.TechGuiCommandExecutor;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckPlacedBlocks;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGExp;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGGiveItem;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGGroup;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGReload;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGTechInfo;
import main.java.me.avankziar.tt.spigot.cmd.tt.exp.ARGExp_Add;
import main.java.me.avankziar.tt.spigot.cmd.tt.exp.ARGExp_Set;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Application;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Application_Accept;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Application_Deny;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Application_List;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Application_Send;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Create;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Demote;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Donate;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_IncreaseLevel;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Info;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Invite;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Invite_Accept;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Invite_Deny;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Invite_Send;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Kick;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Leave;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_List;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_PlayerInfo;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_Promote;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_SetDefaultUpkeep;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_SetDescription;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_SetGrandMaster;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_SetIndividualUpkeep;
import main.java.me.avankziar.tt.spigot.cmd.tt.group.ARGGroup_SetPrivileges;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandExecuteType;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import main.java.me.avankziar.tt.spigot.database.MysqlSetup;
import main.java.me.avankziar.tt.spigot.database.SQLiteHandler;
import main.java.me.avankziar.tt.spigot.database.SQLiteSetup;
import main.java.me.avankziar.tt.spigot.database.YamlHandler;
import main.java.me.avankziar.tt.spigot.database.YamlManager;
import main.java.me.avankziar.tt.spigot.gui.listener.GuiPreListener;
import main.java.me.avankziar.tt.spigot.gui.listener.UpperListener;
import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.hook.PAPIHook;
import main.java.me.avankziar.tt.spigot.listener.BlockFormListener;
import main.java.me.avankziar.tt.spigot.listener.JoinQuitListener;
import main.java.me.avankziar.tt.spigot.listener.recipe.PrepareAnvilListener;
import main.java.me.avankziar.tt.spigot.listener.recipe.PrepareGrindstoneListener;
import main.java.me.avankziar.tt.spigot.listener.recipe.PrepareItemCraftListener;
import main.java.me.avankziar.tt.spigot.listener.recipe.PrepareSmithingListener;
import main.java.me.avankziar.tt.spigot.listener.recipe.RegisterBlockListener;
import main.java.me.avankziar.tt.spigot.listener.reward.BreakPlaceInteractListener;
import main.java.me.avankziar.tt.spigot.listener.reward.BreedListener;
import main.java.me.avankziar.tt.spigot.listener.reward.BrewListener;
import main.java.me.avankziar.tt.spigot.listener.reward.BucketEmptyFillListener;
import main.java.me.avankziar.tt.spigot.listener.reward.Cold_ForgingRenameListener;
import main.java.me.avankziar.tt.spigot.listener.reward.CookMeltSmeltSmokeListener;
import main.java.me.avankziar.tt.spigot.listener.reward.CraftItemListener;
import main.java.me.avankziar.tt.spigot.listener.reward.DryingListener;
import main.java.me.avankziar.tt.spigot.listener.reward.DyingHarmingKillingListener;
import main.java.me.avankziar.tt.spigot.listener.reward.EnchantListener;
import main.java.me.avankziar.tt.spigot.listener.reward.EntityInteractListener;
import main.java.me.avankziar.tt.spigot.listener.reward.ExplodeIgnitingListener;
import main.java.me.avankziar.tt.spigot.listener.reward.FertilizeListener;
import main.java.me.avankziar.tt.spigot.listener.reward.FishingListener;
import main.java.me.avankziar.tt.spigot.listener.reward.GrindstoneListener;
import main.java.me.avankziar.tt.spigot.listener.reward.HarvestListener;
import main.java.me.avankziar.tt.spigot.listener.reward.ItemBreakListener;
import main.java.me.avankziar.tt.spigot.listener.reward.ItemConsumeListener;
import main.java.me.avankziar.tt.spigot.listener.reward.ShearListener;
import main.java.me.avankziar.tt.spigot.listener.reward.SheepDyeListener;
import main.java.me.avankziar.tt.spigot.listener.reward.SmithingListener;
import main.java.me.avankziar.tt.spigot.listener.reward.StoneCutterListener;
import main.java.me.avankziar.tt.spigot.listener.reward.TameListener;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.Bypass;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.GroupPrivilege;
import main.java.me.avankziar.tt.spigot.objects.RewardType;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;

public class TT extends JavaPlugin
{
	public static Logger log;
	private static TT plugin;
	public String pluginName = "TechnologyTree";
	private YamlHandler yamlHandler;
	private YamlManager yamlManager;
	private MysqlSetup mysqlSetup;
	private MysqlHandler mysqlHandler;
	private SQLiteSetup sqlLiteSetup;
	private SQLiteHandler sqlLiteHandler;
	private Utility utility;
	private BackgroundTask backgroundTask;
	
	private ArrayList<BaseConstructor> helpList = new ArrayList<>();
	private ArrayList<CommandConstructor> commandTree = new ArrayList<>();
	private LinkedHashMap<String, ArgumentModule> argumentMap = new LinkedHashMap<>();
	
	public static String infoCommandPath = "CmdTT";
	public static String infoCommand = "/";
	
	private Administration administrationConsumer;
	private EnumTranslation enumTranslationConsumer;
	private ValueEntry valueEntryConsumer;
	private Modifier modifierConsumer;
	private ConditionQueryParser conditionQueryParserConsumer;
	private PlayerTimes playerTimesConsumer;
	private MessageToBungee messageToBungeeConsumer;
	private BaseComponentToBungee baseComponentToBungeeConsumer;
	private CommandToBungee commandToBungeeConsumer;
	private BungeeOnlinePlayers bungeeOnlinePlayersConsumer;
	
	private Economy ecoConsumer;
	
	private net.milkbowl.vault.economy.Economy vEco;
	
	public void onEnable()
	{
		plugin = this;
		log = getLogger();
		
		//https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=TT
		log.info(" ████████╗████████╗ | API-Version: "+plugin.getDescription().getAPIVersion());
		log.info(" ╚══██╔══╝╚══██╔══╝ | Author: "+plugin.getDescription().getAuthors().toString());
		log.info("    ██║      ██║    | Plugin Website: "+plugin.getDescription().getWebsite());
		log.info("    ██║      ██║    | Depend Plugins: "+plugin.getDescription().getDepend().toString());
		log.info("    ██║      ██║    | SoftDepend Plugins: "+plugin.getDescription().getSoftDepend().toString());
		log.info("    ╚═╝      ╚═╝    | LoadBefore: "+plugin.getDescription().getLoadBefore().toString());
		
		setupIFHAdministration();
		
		yamlHandler = new YamlHandler(this);
		
		String path = plugin.getYamlHandler().getConfig().getString("IFHAdministrationPath");
		boolean adm = plugin.getAdministration() != null 
				&& plugin.getYamlHandler().getConfig().getBoolean("useIFHAdministration")
				&& plugin.getAdministration().isMysqlPathActive(path);
		if(adm || yamlHandler.getConfig().getBoolean("Mysql.Status", false) == true)
		{
			mysqlHandler = new MysqlHandler(plugin);
			mysqlSetup = new MysqlSetup(plugin, adm, path);
		} else
		{
			log.severe("MySQL is not set in the Plugin " + pluginName + "!");
			Bukkit.getPluginManager().getPlugin(pluginName).getPluginLoader().disablePlugin(this);
			return;
		}
		
		sqlLiteHandler = new SQLiteHandler(plugin);
		sqlLiteSetup = new SQLiteSetup();
		
		utility = new Utility(plugin);
		backgroundTask = new BackgroundTask(this);
		
		EnumHandler.init();
		RecipeHandler.init();
		CatTechHandler.reload();
		ConfigHandler.init();
		
		setupBypassPerm();
		setupCommandTree();
		setupListeners();
		setupIFHConsumer();
		setupPlaceholderAPI();
		RewardHandler.doRewardOfflinePlayerTask();
	}
	
	public void onDisable()
	{
		Bukkit.resetRecipes();
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
		log.info(pluginName + " is disabled!");
	}

	public static TT getPlugin()
	{
		return plugin;
	}
	
	public YamlHandler getYamlHandler() 
	{
		return yamlHandler;
	}
	
	public YamlManager getYamlManager()
	{
		return yamlManager;
	}

	public void setYamlManager(YamlManager yamlManager)
	{
		this.yamlManager = yamlManager;
	}
	
	public MysqlSetup getMysqlSetup() 
	{
		return mysqlSetup;
	}
	
	public MysqlHandler getMysqlHandler()
	{
		return mysqlHandler;
	}
	
	public SQLiteSetup getSQLLiteSetup() 
	{
		return sqlLiteSetup;
	}
	
	public SQLiteHandler getSQLLiteHandler()
	{
		return sqlLiteHandler;
	}
	
	public Utility getUtility()
	{
		return utility;
	}
	
	public BackgroundTask getBackgroundTask()
	{
		return backgroundTask;
	}
	
	public String getServername()
	{
		return getPlugin().getAdministration() != null ? getPlugin().getAdministration().getSpigotServerName() 
				: getPlugin().getYamlHandler().getConfig().getString("ServerName");
	}
	
	private void setupCommandTree()
	{		
		infoCommand += plugin.getYamlHandler().getCommands().getString("tt.Name");
		
		TabCompletion tab = new TabCompletion(plugin);
		
		ArrayList<String> players = new ArrayList<>();
		for(PlayerData pd : PlayerData.convert(plugin.getMysqlHandler().getFullList(
				MysqlHandler.Type.PLAYERDATA, "`player_name` ASC", "`id` > ?", 0)))
		{
			players.add(pd.getName());
		}
		
		LinkedHashMap<Integer, ArrayList<String>> playerMapI = new LinkedHashMap<>();
		playerMapI.put(1, players);
		LinkedHashMap<Integer, ArrayList<String>> playerMapII = new LinkedHashMap<>();
		playerMapII.put(2, players);
		LinkedHashMap<Integer, ArrayList<String>> playerMapIII = new LinkedHashMap<>();
		playerMapIII.put(3, players);
		
		ArrayList<String> groups = new ArrayList<>();
		for(GroupData gd : GroupData.convert(plugin.getMysqlHandler().getFullList(
				MysqlHandler.Type.GROUP_DATA, "`group_name` ASC", "`id` > ?", 0)))
		{
			groups.add(gd.getGroupName());
		}
		LinkedHashMap<Integer, ArrayList<String>> groupMapI = new LinkedHashMap<>();
		groupMapI.put(1, groups);
		LinkedHashMap<Integer, ArrayList<String>> groupMapII = new LinkedHashMap<>();
		groupMapII.put(2, groups);
		LinkedHashMap<Integer, ArrayList<String>> groupMapIII = new LinkedHashMap<>();
		groupMapIII.put(3, groups);
		
		ArrayList<String> grouprank = new ArrayList<>();
		for(GroupHandler.Position gp : GroupHandler.Position.values())
		{
			grouprank.add(gp.toString());
		}
		LinkedHashMap<Integer, ArrayList<String>> groupRankMapI = new LinkedHashMap<>();
		groupRankMapI.put(1, grouprank);
		LinkedHashMap<Integer, ArrayList<String>> groupRankMapII = new LinkedHashMap<>();
		groupRankMapII.put(2, grouprank);
		LinkedHashMap<Integer, ArrayList<String>> groupRankMapIII = new LinkedHashMap<>();
		groupRankMapIII.put(3, grouprank);
		
		LinkedHashMap<Integer, ArrayList<String>> promoteDemoteMap = new LinkedHashMap<>();
		promoteDemoteMap.put(2, players);
		promoteDemoteMap.put(3, grouprank);
		
		LinkedHashMap<Integer, ArrayList<String>> grandMasterMap = new LinkedHashMap<>();
		grandMasterMap.put(2, players);
		grandMasterMap.put(3, groups);
		
		ArrayList<String> groupprivileges = new ArrayList<>();
		for(GroupPrivilege gp : GroupPrivilege.values())
		{
			groupprivileges.add(gp.toString());
		}
		LinkedHashMap<Integer, ArrayList<String>> privilegesMap = new LinkedHashMap<>();
		privilegesMap.put(2, players);
		privilegesMap.put(3, groupprivileges);
		
		LinkedHashMap<Integer, ArrayList<String>> techMapI = new LinkedHashMap<>();
		ArrayList<String> techList = new ArrayList<>();
		for(String ts : CatTechHandler.technologyMapSolo.keySet())
		{
			techList.add(ts);
		}
		for(String ts : CatTechHandler.technologyMapGroup.keySet())
		{
			techList.add(ts);
		}
		for(String ts : CatTechHandler.technologyMapGlobal.keySet())
		{
			techList.add(ts);
		}
		techMapI.put(1, techList);
		
		ArgumentConstructor checkeventaction = new ArgumentConstructor(CommandExecuteType.TT_CHECKEVENTACTION,
													"tt_checkeventaction", 0, 0, 0, false, null);
		new ARGCheckEventAction(checkeventaction);
		ArgumentConstructor checkplacedblocks = new ArgumentConstructor(CommandExecuteType.TT_CHECKPLACEDBLOCKS,
													"tt_checkplacedblocks", 0, 0, 0, false, null);
		new ARGCheckPlacedBlocks(checkplacedblocks);
		ArgumentConstructor giveitem = new ArgumentConstructor(CommandExecuteType.TT_GIVEITEM, "tt_giveitem", 0, 2, 2, false, null);
		new ARGGiveItem(giveitem);
		ArgumentConstructor reload = new ArgumentConstructor(CommandExecuteType.TT_RELOAD, "tt_reload", 0, 0, 0, false, null);
		new ARGReload(reload);
		ArgumentConstructor techinfo = new ArgumentConstructor(CommandExecuteType.TT_TECHINFO, "tt_techinfo", 0, 1, 2, false, techMapI);
		new ARGTechInfo(techinfo);
		
		ArgumentConstructor exp_add = new ArgumentConstructor(CommandExecuteType.TT_EXP_ADD, "tt_exp_add", 1, 3, 3, false, playerMapII);
		new ARGExp_Add(exp_add);
		ArgumentConstructor exp_set = new ArgumentConstructor(CommandExecuteType.TT_EXP_SET, "tt_exp_set", 1, 3, 3, false, playerMapII);
		new ARGExp_Set(exp_set);
		ArgumentConstructor exp = new ArgumentConstructor(CommandExecuteType.TT_EXP, "tt_exp", 0, 0, 0, false, null,
				exp_add, exp_set);
		new ARGExp(exp);
		
		ArgumentConstructor gr_app_accept = new ArgumentConstructor(CommandExecuteType.TT_GROUP_APPLICATION_ACCEPT,
				"tt_group_application_accept", 2, 3, 3, false, playerMapIII);
		new ARGGroup_Application_Accept(gr_app_accept);
		ArgumentConstructor gr_app_deny = new ArgumentConstructor(CommandExecuteType.TT_GROUP_APPLICATION_DENY,
				"tt_group_application_deny", 2, 3, 3, false, playerMapIII);
		new ARGGroup_Application_Deny(gr_app_deny);
		ArgumentConstructor gr_app_list = new ArgumentConstructor(CommandExecuteType.TT_GROUP_APPLICATION_LIST,
				"tt_group_application_list", 2, 2, 3, false, groupMapIII);
		new ARGGroup_Application_List(gr_app_list);
		ArgumentConstructor gr_app_send = new ArgumentConstructor(CommandExecuteType.TT_GROUP_APPLICATION_SEND,
				"tt_group_application_send", 2, 3, 3, false, groupMapIII);
		new ARGGroup_Application_Send(gr_app_send);
		ArgumentConstructor gr_application = new ArgumentConstructor(CommandExecuteType.TT_GROUP_APPLICATION,
				"tt_group_application", 1, 1, 1, false, null,
				gr_app_accept, gr_app_deny, gr_app_list, gr_app_send);
		new ARGGroup_Application(gr_application);
		ArgumentConstructor gr_create = new ArgumentConstructor(CommandExecuteType.TT_GROUP_CREATE,
				"tt_group_create", 1, 1, 2, false, null);
		new ARGGroup_Create(gr_create);
		ArgumentConstructor gr_demote = new ArgumentConstructor(CommandExecuteType.TT_GROUP_DEMOTE,
				"tt_group_demote", 1, 3, 3, false, promoteDemoteMap);
		new ARGGroup_Demote(gr_demote);
		ArgumentConstructor gr_donate = new ArgumentConstructor(CommandExecuteType.TT_GROUP_DONATE,
				"tt_group_donate", 1, 2, 3, false, groupMapIII);
		new ARGGroup_Donate(gr_donate);
		ArgumentConstructor gr_increaselevel = new ArgumentConstructor(CommandExecuteType.TT_GROUP_INCREASELEVEL,
				"tt_group_increaselevel", 1, 1, 2, false, groupMapII);
		new ARGGroup_IncreaseLevel(gr_increaselevel);
		ArgumentConstructor gr_info = new ArgumentConstructor(CommandExecuteType.TT_GROUP_INFO,
				"tt_group_info", 1, 1, 2, false, groupMapII);
		new ARGGroup_Info(gr_info);
		ArgumentConstructor gr_invite_accept = new ArgumentConstructor(CommandExecuteType.TT_GROUP_INVITE_ACCEPT,
				"tt_group_invite_accept", 2, 3, 3, false, groupMapIII);
		new ARGGroup_Invite_Accept(gr_invite_accept);
		ArgumentConstructor gr_invite_deny = new ArgumentConstructor(CommandExecuteType.TT_GROUP_INVITE_DENY,
				"tt_group_invite_deny", 2, 3, 3, false, groupMapIII);
		new ARGGroup_Invite_Deny(gr_invite_deny);
		ArgumentConstructor gr_invite_send = new ArgumentConstructor(CommandExecuteType.TT_GROUP_INVITE_SEND,
				"tt_group_invite_send", 2, 3, 3, false, playerMapIII);
		new ARGGroup_Invite_Send(gr_invite_send);
		ArgumentConstructor gr_invite = new ArgumentConstructor(CommandExecuteType.TT_GROUP_INVITE,
				"tt_group_invite", 1, 1, 1, false, null,
				gr_invite_accept, gr_invite_deny, gr_invite_send);
		new ARGGroup_Invite(gr_invite);
		ArgumentConstructor gr_kick = new ArgumentConstructor(CommandExecuteType.TT_GROUP_KICK,
				"tt_group_kick", 1, 2, 2, false, playerMapII);
		new ARGGroup_Kick(gr_kick);
		ArgumentConstructor gr_leave = new ArgumentConstructor(CommandExecuteType.TT_GROUP_LEAVE,
				"tt_group_leave", 1, 1, 2, false, null);
		new ARGGroup_Leave(gr_leave);
		ArgumentConstructor gr_list = new ArgumentConstructor(CommandExecuteType.TT_GROUP_LIST,
				"tt_group_list", 1, 1, 2, false, null);
		new ARGGroup_List(gr_list);
		ArgumentConstructor gr_playerinfo = new ArgumentConstructor(CommandExecuteType.TT_GROUP_PLAYERINFO,
				"tt_group_playerinfo", 1, 1, 2, false, playerMapII);
		new ARGGroup_PlayerInfo(gr_playerinfo);
		ArgumentConstructor gr_promote = new ArgumentConstructor(CommandExecuteType.TT_GROUP_PROMOTE,
				"tt_group_promote", 1, 3, 3, false, promoteDemoteMap);
		new ARGGroup_Promote(gr_promote);
		ArgumentConstructor gr_setdefaultupkeep = new ArgumentConstructor(CommandExecuteType.TT_GROUP_SETDEFAULT_UPKEEP,
				"tt_group_setdefaultupkeep", 1, 3, 3, false, groupRankMapII);
		new ARGGroup_SetDefaultUpkeep(gr_setdefaultupkeep);
		ArgumentConstructor gr_setdescription = new ArgumentConstructor(CommandExecuteType.TT_GROUP_SETDESCRIPTION,
				"tt_group_setdescription", 1, 2, 999, false, null);
		new ARGGroup_SetDescription(gr_setdescription);
		ArgumentConstructor gr_setgrandmaster = new ArgumentConstructor(CommandExecuteType.TT_GROUP_SETGRANDMASTER,
				"tt_group_setgrandmaster", 1, 2, 3, false, grandMasterMap);
		new ARGGroup_SetGrandMaster(gr_setgrandmaster);
		ArgumentConstructor gr_setindividualupkeep = new ArgumentConstructor(CommandExecuteType.TT_GROUP_SETINDIVIDUAL_UPKEEP,
				"tt_group_setindividualupkeep", 1, 3, 3, false, playerMapII);
		new ARGGroup_SetIndividualUpkeep(gr_setindividualupkeep);
		ArgumentConstructor gr_setprivileges = new ArgumentConstructor(CommandExecuteType.TT_GROUP_SETPRIVILEGES,
				"tt_group_setprivileges", 1, 3, 3, false, privilegesMap);
		new ARGGroup_SetPrivileges(gr_setprivileges);
		
		ArgumentConstructor group = new ArgumentConstructor(CommandExecuteType.TT_GROUP, "tt_group", 0, 0, 0, false, null,
				gr_application, gr_create, gr_demote, gr_donate, gr_increaselevel, gr_info, gr_invite,
				gr_kick, gr_leave, gr_list, gr_playerinfo, gr_promote, gr_setdefaultupkeep, gr_setdescription, gr_setgrandmaster,
				gr_setindividualupkeep, gr_setprivileges);
		new ARGGroup(group);
		
		CommandConstructor tt = new CommandConstructor(CommandExecuteType.TT, "tt", false,
				checkeventaction, checkplacedblocks, exp, giveitem, group, reload, techinfo);
		registerCommand(tt.getPath(), tt.getName());
		getCommand(tt.getName()).setExecutor(new TTCommandExecutor(plugin, tt));
		getCommand(tt.getName()).setTabCompleter(tab);
		
		CommandConstructor techgui = new CommandConstructor(CommandExecuteType.TECHGUI, "techgui", false);
		registerCommand(techgui.getPath(), tt.getName());
		getCommand(techgui.getName()).setExecutor(new TechGuiCommandExecutor(plugin, techgui));
		getCommand(techgui.getName()).setTabCompleter(tab);
	}
	
	public void setupBypassPerm()
	{
		String path = "Count.";
		for(Bypass.Counter bypass : new ArrayList<Bypass.Counter>(EnumSet.allOf(Bypass.Counter.class)))
		{
			if(!bypass.forPermission())
			{
				continue;
			}
			Bypass.set(bypass, yamlHandler.getCommands().getString(path+bypass.toString()));
		}
		path = "Bypass.";
		for(Bypass.Permission bypass : new ArrayList<Bypass.Permission>(EnumSet.allOf(Bypass.Permission.class)))
		{
			Bypass.set(bypass, yamlHandler.getCommands().getString(path+bypass.toString()));
		}
	}
	
	public ArrayList<BaseConstructor> getCommandHelpList()
	{
		return helpList;
	}
	
	public void addingCommandHelps(BaseConstructor... objects)
	{
		for(BaseConstructor bc : objects)
		{
			helpList.add(bc);
		}
	}
	
	public ArrayList<CommandConstructor> getCommandTree()
	{
		return commandTree;
	}
	
	public CommandConstructor getCommandFromPath(String commandpath)
	{
		CommandConstructor cc = null;
		for(CommandConstructor coco : getCommandTree())
		{
			if(coco.getPath().equalsIgnoreCase(commandpath))
			{
				cc = coco;
				break;
			}
		}
		return cc;
	}
	
	public CommandConstructor getCommandFromCommandString(String command)
	{
		CommandConstructor cc = null;
		for(CommandConstructor coco : getCommandTree())
		{
			if(coco.getName().equalsIgnoreCase(command))
			{
				cc = coco;
				break;
			}
		}
		return cc;
	}
	
	public void registerCommand(String... aliases) 
	{
		PluginCommand command = getCommand(aliases[0], plugin);
	 
		command.setAliases(Arrays.asList(aliases));
		getCommandMap().register(plugin.getDescription().getName(), command);
	}
	 
	private static PluginCommand getCommand(String name, TT plugin) 
	{
		PluginCommand command = null;
	 
		try 
		{
			Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			c.setAccessible(true);
	 
			command = c.newInstance(name, plugin);
		} catch (SecurityException e) 
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		} catch (InstantiationException e) 
		{
			e.printStackTrace();
		} catch (InvocationTargetException e) 
		
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e) 
		{
			e.printStackTrace();
		}
	 
		return command;
	}
	 
	private static CommandMap getCommandMap() 
	{
		CommandMap commandMap = null;
	 
		try {
			if (Bukkit.getPluginManager() instanceof SimplePluginManager) 
			{
				Field f = SimplePluginManager.class.getDeclaredField("commandMap");
				f.setAccessible(true);
	 
				commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
			}
		} catch (NoSuchFieldException e) 
		{
			e.printStackTrace();
		} catch (SecurityException e) 
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		}
	 
		return commandMap;
	}
	
	public LinkedHashMap<String, ArgumentModule> getArgumentMap()
	{
		return argumentMap;
	}
	
	public void setupListeners()
	{
		PluginManager pm = getServer().getPluginManager();
		
		//GuiEvents
		pm.registerEvents(new GuiPreListener(plugin), plugin);
		pm.registerEvents(new UpperListener(plugin), plugin);
		
		pm.registerEvents(new JoinQuitListener(), plugin);
		
		//Dryconcrete etc. delete from SQLLite
		pm.registerEvents(new BlockFormListener(), plugin);
		
		//Registering Blocks, f.e. furnace etc.
		pm.registerEvents(new RegisterBlockListener(), plugin);
		
		//Recipe, to cancel if the recipe isnt unlocked
		pm.registerEvents(new PrepareAnvilListener(), plugin);
		pm.registerEvents(new PrepareGrindstoneListener(), plugin);
		pm.registerEvents(new PrepareItemCraftListener(), plugin);
		pm.registerEvents(new PrepareSmithingListener(), plugin);
		
		//Reward and so
		pm.registerEvents(new BreakPlaceInteractListener(), plugin);
		pm.registerEvents(new BreedListener(), plugin);
		pm.registerEvents(new BrewListener(), plugin);
		pm.registerEvents(new BucketEmptyFillListener(), plugin);
		pm.registerEvents(new Cold_ForgingRenameListener(), plugin);
		pm.registerEvents(new CookMeltSmeltSmokeListener(), plugin);
		pm.registerEvents(new CraftItemListener(), plugin);
		pm.registerEvents(new DyingHarmingKillingListener(), plugin);
		pm.registerEvents(new DryingListener(), plugin);
		pm.registerEvents(new EnchantListener(), plugin);
		pm.registerEvents(new ExplodeIgnitingListener(), plugin);
		pm.registerEvents(new FertilizeListener(), plugin);
		pm.registerEvents(new FishingListener(), plugin);
		pm.registerEvents(new GrindstoneListener(), plugin);
		pm.registerEvents(new HarvestListener(), plugin);
		pm.registerEvents(new ItemBreakListener(), plugin);
		pm.registerEvents(new ItemConsumeListener(), plugin);
		pm.registerEvents(new EntityInteractListener(), plugin);
		pm.registerEvents(new ShearListener(), plugin);
		pm.registerEvents(new SheepDyeListener(), plugin);
		pm.registerEvents(new SmithingListener(), plugin);
		pm.registerEvents(new StoneCutterListener(), plugin);
		pm.registerEvents(new TameListener(), plugin);		
	}
	
	public boolean reload()
	{
		log.info("Reload plugin...");
		Bukkit.getScheduler().cancelTasks(this);
		
		setupIFHAdministration();
		
		yamlHandler = new YamlHandler(this);
		
		String path = plugin.getYamlHandler().getConfig().getString("IFHAdministrationPath");
		boolean adm = plugin.getAdministration() != null 
				&& plugin.getYamlHandler().getConfig().getBoolean("useIFHAdministration")
				&& plugin.getAdministration().isMysqlPathActive(path);
		if(adm || yamlHandler.getConfig().getBoolean("Mysql.Status", false) == true)
		{
			mysqlHandler = new MysqlHandler(plugin);
			mysqlSetup = new MysqlSetup(plugin, adm, path);
		} else
		{
			log.severe("MySQL is not set in the Plugin " + pluginName + "!");
			Bukkit.getPluginManager().getPlugin(pluginName).getPluginLoader().disablePlugin(this);
			log.info("Reload failed!");
			return false;
		}
		
		sqlLiteHandler = new SQLiteHandler(plugin);
		sqlLiteSetup = new SQLiteSetup();
		
		utility = new Utility(plugin);
		backgroundTask = new BackgroundTask(this);
		
		EnumHandler.init();
		RecipeHandler.init();
		CatTechHandler.reload();
		ConfigHandler.init();
		PlayerHandler.reload();
		
		RewardHandler.doRewardOfflinePlayerTask();
		
		for(Player player : Bukkit.getOnlinePlayers())
		{
			PlayerHandler.joinPlayer(player);
		}
		log.info("Reload complete!");
		return true;
	}
	
	public boolean existHook(String externPluginName)
	{
		if(plugin.getServer().getPluginManager().getPlugin(externPluginName) == null)
		{
			return false;
		}
		log.info(pluginName+" hook with "+externPluginName);
		return true;
	}
	
	private void setupPlaceholderAPI()
	{
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
		{
			PAPIHook papi = new PAPIHook(plugin);
			if(!papi.isRegistered())
			{
				new PAPIHook(plugin).register();
			}          
            return;
		}
		return;
	}
	
	private void setupIFHAdministration()
	{ 
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
		RegisteredServiceProvider<main.java.me.avankziar.ifh.spigot.administration.Administration> rsp = 
                getServer().getServicesManager().getRegistration(Administration.class);
		if (rsp == null) 
		{
		   return;
		}
		administrationConsumer = rsp.getProvider();
		log.info(pluginName + " detected InterfaceHub >>> Administration.class is consumed!");
	}
	
	public Administration getAdministration()
	{
		return administrationConsumer;
	}
	
	public void setupIFHConsumer()
	{
		setupIFHValueEntry();
		setupIFHModifier();
		setupIFHEnumTranslation();
		setupIFHCommandToBungee();
		setupIFHMessageToBungee();
		setupIFHConditionQueryParser();
		setupIFHEconomy();
		setupIFHPlayerTimes();
		setupIFHBungeeOnlinePlayers();
	}
	
	public void setupIFHValueEntry()
	{
		if(!new ConfigHandler().isMechanicValueEntryEnabled())
		{
			return;
		}
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
        new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
				try
				{
					if(i == 20)
				    {
						cancel();
				    	return;
				    }
				    RegisteredServiceProvider<main.java.me.avankziar.ifh.general.valueentry.ValueEntry> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 main.java.me.avankziar.ifh.general.valueentry.ValueEntry.class);
				    if(rsp == null) 
				    {
				    	i++;
				        return;
				    }
				    valueEntryConsumer = rsp.getProvider();
				    log.info(pluginName + " detected InterfaceHub >>> ValueEntry.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}
				if(getValueEntry() != null)
				{
					for(BaseConstructor bc : getCommandHelpList())
					{
						if(!bc.isPutUpCmdPermToValueEntrySystem())
						{
							continue;
						}
						if(getValueEntry().isRegistered(bc.getValueEntryPath()))
						{
							continue;
						}
						getValueEntry().register(
								bc.getValueEntryPath(),
								bc.getValueEntryDisplayName(),
								bc.getValueEntryExplanation());
					}
					String dn = plugin.getYamlHandler().getMVELang().getString("ValueEntry.Material.Displayname");
					List<String> expl = plugin.getYamlHandler().getMVELang().getStringList("ValueEntry.Material.Explanation");
					for(Material m : Material.values())
					{
						for(EventType et : EventType.values())
						{
							for(RewardType r : RewardType.values())
							{
								String path = CatTechHandler.getValueEntry(r, et, m, null);
								if(getValueEntry().isRegistered(path))
								{
									continue;
								}
								getValueEntry().register(
										path,
										dn
										.replace("%m%", EnumHandler.getName(m))
										.replace("%et%", EnumHandler.getName(et))
										.replace("%r%", EnumHandler.getName(r)),
										expl.toArray(new String[expl.size()]));
							}
						}
					}
					dn = plugin.getYamlHandler().getMVELang().getString("ValueEntry.Entity.Displayname");
					expl = plugin.getYamlHandler().getMVELang().getStringList("ValueEntry.Entity.Explanation");
					for(EntityType e : EntityType.values())
					{
						for(EventType et : EventType.values())
						{
							for(RewardType r : RewardType.values())
							{
								String path = CatTechHandler.getValueEntry(r, et, null, e);
								if(getValueEntry().isRegistered(path))
								{
									continue;
								}
								getValueEntry().register(
										path,
										dn
										.replace("%e%", EnumHandler.getName(e))
										.replace("%et%", EnumHandler.getName(et))
										.replace("%r%", EnumHandler.getName(r)),
										expl.toArray(new String[expl.size()]));
							}
						}
					}
				}
			}
        }.runTaskTimerAsynchronously(plugin, 20L, 20*2);
	}
	
	public ValueEntry getValueEntry()
	{
		return valueEntryConsumer;
	}
	
	private void setupIFHModifier() 
	{
		if(!new ConfigHandler().isMechanicModifierEnabled())
		{
			return;
		}
        if(Bukkit.getPluginManager().getPlugin("InterfaceHub") == null) 
        {
            return;
        }
        new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
				try
				{
					if(i == 20)
				    {
						cancel();
						return;
				    }
				    RegisteredServiceProvider<main.java.me.avankziar.ifh.general.modifier.Modifier> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 main.java.me.avankziar.ifh.general.modifier.Modifier.class);
				    if(rsp == null) 
				    {
				    	//Check up to 20 seconds after the start, to connect with the provider
				    	i++;
				        return;
				    }
				    modifierConsumer = rsp.getProvider();
				    log.info(pluginName + " detected InterfaceHub >>> Modifier.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}
				if(getModifier() != null)
				{
					for(Bypass.Counter ept : Bypass.Counter.values())
					{
						if(getModifier().isRegistered(ept.getModification()))
						{
							continue;
						}
						ModificationType modt = null;
						switch(ept)
						{
						case REGISTER_BLOCK_:
							modt = ModificationType.UP;
							break;
						}
						List<String> lar = plugin.getYamlHandler().getMVELang().getStringList(ept.toString()+".Explanation");
						getModifier().register(
								ept.getModification(),
								plugin.getYamlHandler().getMVELang().getString(ept.toString()+".Displayname", ept.toString()),
								modt,
								lar.toArray(new String[lar.size()]));
					}
					String dn = plugin.getYamlHandler().getMVELang().getString("Modifier.Material.Displayname");
					List<String> expl = plugin.getYamlHandler().getMVELang().getStringList("Modifier.Entity.Explanation");
					for(Material m : Material.values())
					{
						for(EventType et : EventType.values())
						{
							for(RewardType r : RewardType.values())
							{
								String path = CatTechHandler.getModifier(r, et, m, null);
								if(getValueEntry().isRegistered(path))
								{
									continue;
								}
								getValueEntry().register(
										path,
										dn
										.replace("%m%", EnumHandler.getName(m))
										.replace("%et%", EnumHandler.getName(et))
										.replace("%r%", EnumHandler.getName(r)),
										expl.toArray(new String[expl.size()]));
							}
						}
					}
					dn = plugin.getYamlHandler().getMVELang().getString("Modifier.Entity.Displayname");
					expl = plugin.getYamlHandler().getMVELang().getStringList("Modifier.Entity.Explanation");
					for(EntityType e : EntityType.values())
					{
						for(EventType et : EventType.values())
						{
							for(RewardType r : RewardType.values())
							{
								String path = CatTechHandler.getModifier(r, et, null, e);
								if(getValueEntry().isRegistered(path))
								{
									continue;
								}
								getValueEntry().register(
										path,
										dn
										.replace("%e%", EnumHandler.getName(e))
										.replace("%et%", EnumHandler.getName(et))
										.replace("%r%", EnumHandler.getName(r)),
										expl.toArray(new String[expl.size()]));
							}
						}
					}
				}
			}
        }.runTaskTimerAsynchronously(plugin, 20L, 20*2);
	}
	
	public Modifier getModifier()
	{
		return modifierConsumer;
	}
	
	private void setupIFHEnumTranslation() 
	{
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
        new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
				try
				{
					if(i == 20)
				    {
						cancel();
				    	return;
				    }
				    RegisteredServiceProvider<main.java.me.avankziar.ifh.spigot.interfaces.EnumTranslation> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 main.java.me.avankziar.ifh.spigot.interfaces.EnumTranslation.class);
				    if(rsp == null) 
				    {
				    	i++;
				        return;
				    }
				    enumTranslationConsumer = rsp.getProvider();
				    log.info(pluginName + " detected InterfaceHub >>> EnumTranslation.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}			    
			}
        }.runTaskTimer(plugin, 0L, 20*2);
	}
	
	public EnumTranslation getEnumTl()
	{
		return enumTranslationConsumer;
	}
	
	public void setupIFHCommandToBungee()
	{
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
        new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
				try
				{
					if(i == 20)
				    {
						cancel();
				    	return;
				    }
				    RegisteredServiceProvider<main.java.me.avankziar.ifh.spigot.tobungee.commands.CommandToBungee> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 main.java.me.avankziar.ifh.spigot.tobungee.commands.CommandToBungee.class);
				    if(rsp == null) 
				    {
				    	i++;
				        return;
				    }
				    commandToBungeeConsumer = rsp.getProvider();
				    log.info(pluginName + " detected InterfaceHub >>> CommandToBungee.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}
			}
        }.runTaskTimer(plugin, 0L, 20*2);
	}
	
	public CommandToBungee getCommandToBungee()
	{
		return commandToBungeeConsumer;
	}
	
	public void setupIFHMessageToBungee()
	{
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
        new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
				try
				{
					if(i == 20)
				    {
						cancel();
				    	return;
				    }
				    RegisteredServiceProvider<main.java.me.avankziar.ifh.spigot.tobungee.chatlike.MessageToBungee> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 main.java.me.avankziar.ifh.spigot.tobungee.chatlike.MessageToBungee.class);
				    if(rsp == null) 
				    {
				    	i++;
				        return;
				    }
				    messageToBungeeConsumer = rsp.getProvider();
				    log.info(pluginName + " detected InterfaceHub >>> MessageToBungee.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}
			}
        }.runTaskTimer(plugin, 0L, 20*2);
	}
	
	public MessageToBungee getMessageToBungee()
	{
		return messageToBungeeConsumer;
	}
	
	public void setupIFHBaseComponentToBungee()
	{
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
        new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
				try
				{
					if(i == 20)
				    {
						cancel();
				    	return;
				    }
				    RegisteredServiceProvider<main.java.me.avankziar.ifh.spigot.tobungee.chatlike.BaseComponentToBungee> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 main.java.me.avankziar.ifh.spigot.tobungee.chatlike.BaseComponentToBungee.class);
				    if(rsp == null) 
				    {
				    	i++;
				        return;
				    }
				    baseComponentToBungeeConsumer = rsp.getProvider();
				    log.info(pluginName + " detected InterfaceHub >>> BaseComponentToBungee.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}
			}
        }.runTaskTimer(plugin, 0L, 20*2);
	}
	
	public BaseComponentToBungee getBaseComponentToBungee()
	{
		return baseComponentToBungeeConsumer;
	}
	
	public void setupIFHConditionQueryParser()
	{
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
        new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
				try
				{
					if(i == 20)
				    {
						cancel();
				    	return;
				    }
				    RegisteredServiceProvider<main.java.me.avankziar.ifh.general.conditionqueryparser.ConditionQueryParser> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 main.java.me.avankziar.ifh.general.conditionqueryparser.ConditionQueryParser.class);
				    if(rsp == null) 
				    {
				    	i++;
				        return;
				    }
				    conditionQueryParserConsumer = rsp.getProvider();
				    log.info(pluginName + " detected InterfaceHub >>> ConditionQueryParser.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}
			}
        }.runTaskTimer(plugin, 0L, 20*2);
	}
	
	public ConditionQueryParser getConditionQueryParser()
	{
		return conditionQueryParserConsumer;
	}
	
	private void setupIFHEconomy()
    {
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")
				&& !plugin.getServer().getPluginManager().isPluginEnabled("Vault")) 
	    {
			log.severe("Plugin InterfaceHub or Vault are missing!");
			log.severe("Disable "+pluginName+"!");
			Bukkit.getPluginManager().getPlugin(pluginName).getPluginLoader().disablePlugin(plugin);
	    	return;
	    }
		if(plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub"))
		{
			RegisteredServiceProvider<main.java.me.avankziar.ifh.spigot.economy.Economy> rsp = 
	                getServer().getServicesManager().getRegistration(Economy.class);
			if (rsp == null) 
			{
				RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp2 = getServer()
		        		.getServicesManager()
		        		.getRegistration(net.milkbowl.vault.economy.Economy.class);
		        if (rsp2 == null) 
		        {
		        	log.severe("A economy plugin which supported InterfaceHub or Vault is missing!");
					log.severe("Disable "+pluginName+"!");
					Bukkit.getPluginManager().getPlugin(pluginName).getPluginLoader().disablePlugin(plugin);
		            return;
		        }
		        vEco = rsp2.getProvider();
		        log.info(pluginName + " detected Vault >>> Economy.class is consumed!");
				return;
			}
			ecoConsumer = rsp.getProvider();
			log.info(pluginName + " detected InterfaceHub >>> Economy.class is consumed!");
		} else
		{
			RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = getServer()
	        		.getServicesManager()
	        		.getRegistration(net.milkbowl.vault.economy.Economy.class);
	        if (rsp == null) 
	        {
	        	log.severe("A economy plugin which supported Vault is missing!");
				log.severe("Disable "+pluginName+"!");
				Bukkit.getPluginManager().getPlugin(pluginName).getPluginLoader().disablePlugin(plugin);
	            return;
	        }
	        vEco = rsp.getProvider();
	        log.info(pluginName + " detected Vault >>> Economy.class is consumed!");
		}
        return;
    }
	
	private void setupIFHPlayerTimes() 
	{
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
        new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
				try
				{
					if(i == 20)
				    {
						cancel();
				    	return;
				    }
				    RegisteredServiceProvider<main.java.me.avankziar.ifh.general.interfaces.PlayerTimes> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 main.java.me.avankziar.ifh.general.interfaces.PlayerTimes.class);
				    if(rsp == null) 
				    {
				    	i++;
				        return;
				    }
				    playerTimesConsumer = rsp.getProvider();
				    log.info(pluginName + " detected InterfaceHub >>> PlayerTimes.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}			    
			}
        }.runTaskTimer(plugin, 0L, 20*2);
	}
	
	public PlayerTimes getPlayerTimes()
	{
		return playerTimesConsumer;
	}
	
	private void setupIFHBungeeOnlinePlayers() 
	{
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
        new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
				try
				{
					if(i == 20)
				    {
						cancel();
				    	return;
				    }
				    RegisteredServiceProvider<main.java.me.avankziar.ifh.spigot.interfaces.BungeeOnlinePlayers> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 main.java.me.avankziar.ifh.spigot.interfaces.BungeeOnlinePlayers.class);
				    if(rsp == null) 
				    {
				    	i++;
				        return;
				    }
				    bungeeOnlinePlayersConsumer = rsp.getProvider();
				    log.info(pluginName + " detected InterfaceHub >>> BungeeOnlinePlayers.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}			    
			}
        }.runTaskTimer(plugin, 0L, 20*2);
	}
	
	public BungeeOnlinePlayers getBungeeOnlinePlayers()
	{
		return bungeeOnlinePlayersConsumer;
	}
	
	public Economy getIFHEco()
	{
		return this.ecoConsumer;
	}
	
	public net.milkbowl.vault.economy.Economy getVaultEco()
	{
		return this.vEco;
	}
}