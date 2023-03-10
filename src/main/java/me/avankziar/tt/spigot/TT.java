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

import main.java.me.avankziar.ifh.general.bonusmalus.BonusMalus;
import main.java.me.avankziar.ifh.general.bonusmalus.BonusMalusType;
import main.java.me.avankziar.ifh.general.condition.Condition;
import main.java.me.avankziar.ifh.general.condition.ConditionQueryParser;
import main.java.me.avankziar.ifh.spigot.administration.Administration;
import main.java.me.avankziar.ifh.spigot.economy.Economy;
import main.java.me.avankziar.ifh.spigot.tobungee.commands.CommandToBungee;
import main.java.me.avankziar.tt.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmd.BaseCommandExecutor;
import main.java.me.avankziar.tt.spigot.cmd.TabCompletion;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandExecuteType;
import main.java.me.avankziar.tt.spigot.conditionbonusmalus.Bypass;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import main.java.me.avankziar.tt.spigot.database.MysqlSetup;
import main.java.me.avankziar.tt.spigot.database.YamlHandler;
import main.java.me.avankziar.tt.spigot.database.YamlManager;
import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.listener.recipe.PrepareAnvilListener;
import main.java.me.avankziar.tt.spigot.listener.recipe.PrepareGrindstoneListener;
import main.java.me.avankziar.tt.spigot.listener.recipe.PrepareItemCraftListener;
import main.java.me.avankziar.tt.spigot.listener.recipe.PrepareSmithingListener;
import main.java.me.avankziar.tt.spigot.listener.recipe.RegisterBlockListener;
import main.java.me.avankziar.tt.spigot.listener.reward.AnvilListener;
import main.java.me.avankziar.tt.spigot.listener.reward.BreakPlaceListener;
import main.java.me.avankziar.tt.spigot.listener.reward.BrewListener;
import main.java.me.avankziar.tt.spigot.listener.reward.EnchantListener;
import main.java.me.avankziar.tt.spigot.listener.reward.EntityBreedListener;
import main.java.me.avankziar.tt.spigot.listener.reward.EntityKillDeathListener;
import main.java.me.avankziar.tt.spigot.listener.reward.EntityTameListener;
import main.java.me.avankziar.tt.spigot.listener.reward.FertilizeListener;
import main.java.me.avankziar.tt.spigot.listener.reward.GrindstoneListener;
import main.java.me.avankziar.tt.spigot.listener.reward.SheepDyeWoolListener;
import main.java.me.avankziar.tt.spigot.listener.reward.SmeltListener;
import main.java.me.avankziar.tt.spigot.listener.reward.TNTListener;

public class TT extends JavaPlugin
{
	public static Logger log;
	private static TT plugin;
	public String pluginName = "TechnologyTree";
	private YamlHandler yamlHandler;
	private YamlManager yamlManager;
	private MysqlSetup mysqlSetup;
	private MysqlHandler mysqlHandler;
	private Utility utility;
	private BackgroundTask backgroundTask;
	
	private ArrayList<BaseConstructor> helpList = new ArrayList<>();
	private ArrayList<CommandConstructor> commandTree = new ArrayList<>();
	private LinkedHashMap<String, ArgumentModule> argumentMap = new LinkedHashMap<>();
	private ArrayList<String> players = new ArrayList<>();
	
	public static String infoCommandPath = "CmdTT";
	public static String infoCommand = "/";
	
	private Administration administrationConsumer;
	private Condition conditionConsumer;
	private ConditionQueryParser conditionQueryParserConsumer;
	private BonusMalus bonusMalusConsumer;
	
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
		
		utility = new Utility(plugin);
		backgroundTask = new BackgroundTask(this);
		
		setupBypassPerm();
		setupCommandTree();
		setupListeners();
		RecipeHandler.init();
		CatTechHandler.reload();
		setupIFHConsumer();
		RewardHandler.doRewardOfflinePlayerTask();
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
		infoCommand += plugin.getYamlHandler().getCommands().getString("base.Name");
		
		TabCompletion tab = new TabCompletion(plugin);
		
		CommandConstructor base = new CommandConstructor(CommandExecuteType.BASEMAIN, "base", false);
		registerCommand(base.getPath(), base.getName());
		getCommand(base.getName()).setExecutor(new BaseCommandExecutor(plugin, base));
		getCommand(base.getName()).setTabCompleter(tab);
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
	
	public ArrayList<String> getMysqlPlayers()
	{
		return players;
	}

	public void setMysqlPlayers(ArrayList<String> players)
	{
		this.players = players;
	}
	
	public void setupListeners()
	{
		PluginManager pm = getServer().getPluginManager();
		
		//Registering Blocks, f.e. furnace etc.
		pm.registerEvents(new RegisterBlockListener(), plugin);
		//Recipe, to cancel if the recipe isnt unlocked
		pm.registerEvents(new PrepareAnvilListener(), plugin);
		pm.registerEvents(new PrepareGrindstoneListener(), plugin);
		pm.registerEvents(new PrepareItemCraftListener(), plugin);
		pm.registerEvents(new PrepareSmithingListener(), plugin);
		
		
		//Reward and so
		pm.registerEvents(new AnvilListener(), plugin);
		pm.registerEvents(new BreakPlaceListener(), plugin);
		pm.registerEvents(new BrewListener(), plugin);
		pm.registerEvents(new EnchantListener(), plugin);
		pm.registerEvents(new EntityBreedListener(), plugin);
		pm.registerEvents(new EntityKillDeathListener(), plugin);
		pm.registerEvents(new EntityTameListener(), plugin);
		pm.registerEvents(new FertilizeListener(), plugin);
		pm.registerEvents(new GrindstoneListener(), plugin);
		pm.registerEvents(new SheepDyeWoolListener(), plugin);
		pm.registerEvents(new SmeltListener(), plugin);
		pm.registerEvents(new TNTListener(), plugin);
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
		CatTechHandler.reload();
		RecipeHandler.init();
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
		setupIFHBonusMalus();
		setupIFHCommandToBungee();
		setupIFHCondition();
		setupIFHConditionQueryParser();
		setupIFHEconomy();
	}
	
	private void setupIFHBonusMalus() 
	{
		if(!new ConfigHandler().isMechanicBonusMalusEnabled())
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
				    RegisteredServiceProvider<main.java.me.avankziar.ifh.general.bonusmalus.BonusMalus> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 main.java.me.avankziar.ifh.general.bonusmalus.BonusMalus.class);
				    if(rsp == null) 
				    {
				    	//Check up to 20 seconds after the start, to connect with the provider
				    	i++;
				        return;
				    }
				    bonusMalusConsumer = rsp.getProvider();
				    log.info(pluginName + " detected InterfaceHub >>> BonusMalus.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}
				if(getBonusMalus() != null)
				{
					//Bypass CountPerm init
					List<Bypass.Counter> list = new ArrayList<Bypass.Counter>(EnumSet.allOf(Bypass.Counter.class));
					for(Bypass.Counter ept : list)
					{
						if(getBonusMalus().isRegistered(ept.getBonusMalus()))
						{
							continue;
						}
						BonusMalusType bmt = null;
						switch(ept)
						{
						case REGISTER_BLOCK:
							bmt = BonusMalusType.UP;
							break;
						}
						List<String> lar = plugin.getYamlHandler().getCBMLang().getStringList(ept.toString()+".Explanation");
						getBonusMalus().register(
								ept.getBonusMalus(),
								plugin.getYamlHandler().getCBMLang().getString(ept.toString()+".Displayname", ept.toString()),
								bmt,
								lar.toArray(new String[lar.size()]));
					}
				}
			}
        }.runTaskTimer(plugin, 20L, 20*2);
	}
	
	public BonusMalus getBonusMalus()
	{
		return bonusMalusConsumer;
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
	
	public void setupIFHCondition()
	{
		if(!new ConfigHandler().isMechanicConditionEnabled())
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
				    RegisteredServiceProvider<main.java.me.avankziar.ifh.general.condition.Condition> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 main.java.me.avankziar.ifh.general.condition.Condition.class);
				    if(rsp == null) 
				    {
				    	i++;
				        return;
				    }
				    conditionConsumer = rsp.getProvider();
				    log.info(pluginName + " detected InterfaceHub >>> Condition.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}
				if(getCondition() != null)
				{
					//Command Bonus/Malus init
					for(BaseConstructor bc : getCommandHelpList())
					{
						if(!bc.isPutUpCmdPermToConditionSystem())
						{
							continue;
						}
						if(getCondition().isRegistered(bc.getConditionPath()))
						{
							continue;
						}
						String[] ex = {plugin.getYamlHandler().getCommands().getString(bc.getPath()+".Explanation")};
						getCondition().register(
								bc.getConditionPath(),
								plugin.getYamlHandler().getCommands().getString(bc.getPath()+".Displayname", "Command "+bc.getName()),
								ex);
					}
					//Bypass Perm Bonus/Malus init
					List<Bypass.Permission> list = new ArrayList<Bypass.Permission>(EnumSet.allOf(Bypass.Permission.class));
					for(Bypass.Permission ept : list)
					{
						if(getCondition().isRegistered(ept.getCondition()))
						{
							continue;
						}
						List<String> lar = plugin.getYamlHandler().getCBMLang().getStringList(ept.toString()+".Explanation");
						getCondition().register(
								ept.getCondition(),
								plugin.getYamlHandler().getCBMLang().getString(ept.toString()+".Displayname", ept.toString()),
								lar.toArray(new String[lar.size()]));
					}
				}
			}
        }.runTaskTimer(plugin, 0L, 20*2);
	}
	
	public Condition getCondition()
	{
		return conditionConsumer;
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
				    RegisteredServiceProvider<main.java.me.avankziar.ifh.general.condition.ConditionQueryParser> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 main.java.me.avankziar.ifh.general.condition.ConditionQueryParser.class);
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