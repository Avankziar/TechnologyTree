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
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.ifh.general.conditionqueryparser.ConditionQueryParser;
import main.java.me.avankziar.ifh.general.modifier.ModificationType;
import main.java.me.avankziar.ifh.general.modifier.Modifier;
import main.java.me.avankziar.ifh.general.valueentry.ValueEntry;
import main.java.me.avankziar.ifh.spigot.administration.Administration;
import main.java.me.avankziar.ifh.spigot.economy.Economy;
import main.java.me.avankziar.ifh.spigot.interfaces.EnumTranslation;
import main.java.me.avankziar.ifh.spigot.tobungee.commands.CommandToBungee;
import main.java.me.avankziar.tt.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmd.TTCommandExecutor;
import main.java.me.avankziar.tt.spigot.cmd.TabCompletion;
import main.java.me.avankziar.tt.spigot.cmd.TechGuiCommandExecutor;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckPlacedBlocks;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGTechInfo;
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
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
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
import main.java.me.avankziar.tt.spigot.listener.reward.TameListener;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.Bypass;
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
	
	private CommandToBungee commandToBungeeConsumer;
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
		//RewardHandler.doRewardOfflinePlayerTask(); ADDME
	}
	
	public void onDisable()
	{
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
		ArgumentConstructor techinfo = new ArgumentConstructor(CommandExecuteType.TT_TECHINFO, "tt_techinfo", 0, 1, 2, false, techMapI);
		new ARGTechInfo(techinfo);
		
		CommandConstructor tt = new CommandConstructor(CommandExecuteType.TT, "tt", false,
				checkeventaction, checkplacedblocks, techinfo);
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
		pm.registerEvents(new TameListener(), plugin);		
	}
	
	public boolean reload()
	{
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
			return false;
		}
		RecipeHandler.init();
		CatTechHandler.reload();
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
            new PAPIHook(plugin).register();
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
		setupIFHConditionQueryParser();
		setupIFHEconomy();
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
				}
			}
        }.runTaskTimer(plugin, 0L, 20*2);
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
					List<Bypass.Counter> list = new ArrayList<Bypass.Counter>(EnumSet.allOf(Bypass.Counter.class));
					for(Bypass.Counter ept : list)
					{
						if(!getModifier().isRegistered(ept.getModification()))
						{
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
					}
				}
			}
        }.runTaskTimer(plugin, 20L, 20*2);
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
		if(!new ConfigHandler().isMechanicCommandToBungeeEnabled())
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
	
	public void setupIFHConditionQueryParser()
	{
		if(!new ConfigHandler().isMechanicConditionQueryParserEnabled())
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
	
	public Economy getIFHEco()
	{
		return this.ecoConsumer;
	}
	
	public net.milkbowl.vault.economy.Economy getVaultEco()
	{
		return this.vEco;
	}
}