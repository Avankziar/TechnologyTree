package main.java.me.avankziar.tt.spigot.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.database.Language.ISO639_2B;

public class YamlHandler
{
	private TT plugin;
	private File config = null;
	private YamlConfiguration cfg = new YamlConfiguration();
	
	private File commands = null;
	private YamlConfiguration com = new YamlConfiguration();
	
	private String languages;
	private File language = null;
	private YamlConfiguration lang = new YamlConfiguration();
	private File cbmlanguage = null;
	private YamlConfiguration cbmlang = new YamlConfiguration();
	
	private LinkedHashMap<String, YamlConfiguration> itemGenerator = new LinkedHashMap<>();
	
	private LinkedHashMap<String, YamlConfiguration> mainCategories = new LinkedHashMap<>();
	private LinkedHashMap<String, YamlConfiguration> subCategories = new LinkedHashMap<>();
	private LinkedHashMap<String, YamlConfiguration> technologies = new LinkedHashMap<>();

	public YamlHandler(TT plugin)
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public YamlConfiguration getConfig()
	{
		return cfg;
	}
	
	public YamlConfiguration getCommands()
	{
		return com;
	}
	
	public YamlConfiguration getLang()
	{
		return lang;
	}
	
	public YamlConfiguration getCBMLang()
	{
		return cbmlang;
	}
	
	public LinkedHashMap<String, YamlConfiguration> getItemGenerators()
	{
		return itemGenerator;
	}
	
	public LinkedHashMap<String, YamlConfiguration> getMainCategories()
	{
		return mainCategories;
	}
	
	public LinkedHashMap<String, YamlConfiguration> getSubCategories()
	{
		return subCategories;
	}
	
	public LinkedHashMap<String, YamlConfiguration> getTechnologies()
	{
		return technologies;
	}
	
	private YamlConfiguration loadYamlTask(File file, YamlConfiguration yaml)
	{
		try 
		{
			yaml.load(file);
		} catch (IOException | InvalidConfigurationException e) 
		{
			TT.log.severe(
					"Could not load the %file% file! You need to regenerate the %file%! Error: ".replace("%file%", file.getName())
					+ e.getMessage());
			e.printStackTrace();
		}
		return yaml;
	}
	
	@SuppressWarnings("deprecation")
	private boolean writeFile(File file, YamlConfiguration yml, LinkedHashMap<String, Language> keyMap)
	{
		yml.options().header("For more explanation see \n Your pluginsite");
		for(String key : keyMap.keySet())
		{
			Language languageObject = keyMap.get(key);
			if(languageObject.languageValues.containsKey(plugin.getYamlManager().getLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInput(yml, keyMap, key, plugin.getYamlManager().getLanguageType());
			} else if(languageObject.languageValues.containsKey(plugin.getYamlManager().getDefaultLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInput(yml, keyMap, key, plugin.getYamlManager().getDefaultLanguageType());
			}
		}
		try
		{
			yml.save(file);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean loadYamlHandler()
	{
		plugin.setYamlManager(new YamlManager());
		if(!mkdirStaticFiles())
		{
			return false;
		}
		if(!mkdirDynamicFiles())
		{
			return false;
		}
		return true;
	}
	
	public boolean mkdirStaticFiles()
	{
		File directory = new File(plugin.getDataFolder()+"");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		config = new File(plugin.getDataFolder(), "config.yml");
		if(!config.exists()) 
		{
			TT.log.info("Create config.yml...");
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, config.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		cfg = loadYamlTask(config, cfg);
		if (cfg == null)
		{
			return false;
		}
		writeFile(config, cfg, plugin.getYamlManager().getConfigSpigotKey());
		
		languages = plugin.getAdministration() == null 
				? cfg.getString("Language", "ENG").toUpperCase() 
				: plugin.getAdministration().getLanguage();
		
		commands = new File(plugin.getDataFolder(), "commands.yml");
		if(!commands.exists()) 
		{
			TT.log.info("Create commands.yml...");
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, commands.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		com = loadYamlTask(commands, com);
		if (com == null)
		{
			return false;
		}
		writeFile(commands, com, plugin.getYamlManager().getCommandsKey());
		return true;
	}
	
	private boolean mkdirDynamicFiles()
	{
		List<Language.ISO639_2B> types = new ArrayList<Language.ISO639_2B>(EnumSet.allOf(Language.ISO639_2B.class));
		ISO639_2B languageType = ISO639_2B.ENG;
		for(ISO639_2B type : types)
		{
			if(type.toString().equals(languages))
			{
				languageType = type;
				break;
			}
		}
		plugin.getYamlManager().setLanguageType(languageType);
		if(!mkdirLanguage())
		{
			return false;
		}
		if(!mkdirItems())
		{
			return false;
		}
		if(!mkdirMainCategories())
		{
			return false;
		}
		if(!mkdirSubCategories())
		{
			return false;
		}
		if(!mkdirTechnologies())
		{
			return false;
		}
		return true;
	}
	
	private boolean mkdirLanguage()
	{
		String languageString = plugin.getYamlManager().getLanguageType().toString().toLowerCase();
		File directory = new File(plugin.getDataFolder()+"/Languages/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		language = new File(directory.getPath(), languageString+".yml");
		if(!language.exists()) 
		{
			TT.log.info("Create %lang%.yml...".replace("%lang%", languageString));
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, language.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		lang = loadYamlTask(language, lang);
		if (lang == null)
		{
			return false;
		}
		writeFile(language, lang, plugin.getYamlManager().getLanguageKey());
		cbmlanguage = new File(directory.getPath(), "cbm_"+languageString+".yml");
		if(!cbmlanguage.exists()) 
		{
			TT.log.info("Create cbm_%lang%.yml...".replace("%lang%", languageString));
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, cbmlanguage.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		cbmlang = loadYamlTask(cbmlanguage, cbmlang);
		if(cbmlang == null)
		{
			return false;
		}
		writeFile(cbmlanguage, cbmlang, plugin.getYamlManager().getConditionBonusMalusLanguageKey());
		return true;
	}
	
	private boolean mkdirItems()
	{
		File directory = new File(plugin.getDataFolder()+"/Items/");
		if(!directory.exists())
		{
			directory.mkdir();
			for(Entry<String, LinkedHashMap<String, Language>> e : plugin.getYamlManager().getItemGeneratorKey().entrySet())
			{
				File f = new File(directory.getPath(), e.getKey()+".yml");
				if(!f.exists()) 
				{
					TT.log.info("Create %f%.yml...".replace("%f%", e.getKey()));
					try(InputStream in = plugin.getResource("default.yml"))
					{
						Files.copy(in, f.toPath());
					} catch (IOException ex)
					{
						ex.printStackTrace();
					}
				}
				YamlConfiguration y = loadYamlTask(f, new YamlConfiguration());
				if(y == null)
				{
					return false;
				}
				writeFile(f, y, e.getValue());
				String name = f.getName().split(".")[0];
				getItemGenerators().put(name, y);
			}
			return true;
		}
		File[] listOfFiles = directory.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) 
		{
			if(!listOfFiles[i].isFile()) 
			{
				continue;
			}
			YamlConfiguration y = new YamlConfiguration();
			y = loadYamlTask(listOfFiles[i], y);
			String name = listOfFiles[i].getName().split(".")[0];
			getItemGenerators().put(name, y);
		}
		return true;
	}
	
	private boolean mkdirMainCategories()
	{
		File solo = new File(plugin.getDataFolder()+"/Solo/MainCategory/");
		File group = new File(plugin.getDataFolder()+"/Group/MainCategory/");
		File global = new File(plugin.getDataFolder()+"/Global/MainCategory/");
		if(!solo.exists() || !group.exists() || !global.exists())
		{
			if(!solo.exists())
			{
				solo.mkdir();
				if(!createAndLoadMap(solo, "solo",
						plugin.getYamlManager().getMainCategoryKey(), getMainCategories()))
				{
					return false;
				}
			}
			if(!group.exists())
			{
				group.mkdir();
				if(!createAndLoadMap(group, "group",
						plugin.getYamlManager().getMainCategoryKey(), getMainCategories()))
				{
					return false;
				}
			}
			if(!global.exists())
			{
				global.mkdir();
				if(!createAndLoadMap(global, "global",
						plugin.getYamlManager().getMainCategoryKey(), getMainCategories()))
				{
					return false;
				}
			}
			return true;
		}
		onlyLoadMap(solo, getMainCategories());
		onlyLoadMap(group, getMainCategories());
		onlyLoadMap(global, getMainCategories());
		return true;
	}
	
	private boolean mkdirSubCategories()
	{
		File solo = new File(plugin.getDataFolder()+"/Solo/SubCategory/");
		File group = new File(plugin.getDataFolder()+"/Group/SubCategory/");
		File global = new File(plugin.getDataFolder()+"/Global/SubCategory/");
		if(!solo.exists() || !group.exists() || !global.exists())
		{
			if(!solo.exists())
			{
				solo.mkdir();
				if(!createAndLoadMap(solo, "solo",
						plugin.getYamlManager().getSubCategoryKey(), getSubCategories()))
				{
					return false;
				}
			}
			if(!group.exists())
			{
				group.mkdir();
				if(!createAndLoadMap(group, "group",
						plugin.getYamlManager().getSubCategoryKey(), getSubCategories()))
				{
					return false;
				}
			}
			if(!global.exists())
			{
				global.mkdir();
				if(!createAndLoadMap(global, "global",
						plugin.getYamlManager().getSubCategoryKey(), getSubCategories()))
				{
					return false;
				}
			}
			return true;
		}
		onlyLoadMap(solo, getSubCategories());
		onlyLoadMap(group, getSubCategories());
		onlyLoadMap(global, getSubCategories());
		return true;
	}
	
	private boolean mkdirTechnologies()
	{
		File solo = new File(plugin.getDataFolder()+"/Solo/Technology/");
		File group = new File(plugin.getDataFolder()+"/Group/Technology/");
		File global = new File(plugin.getDataFolder()+"/Global/Technology/");
		if(!solo.exists() || !group.exists() || !global.exists())
		{
			if(!solo.exists())
			{
				solo.mkdir();
				if(!createAndLoadMap(solo, "solo",
						plugin.getYamlManager().getTechnologyKey(), getTechnologies()))
				{
					return false;
				}
			}
			if(!group.exists())
			{
				group.mkdir();
				if(!createAndLoadMap(group, "group",
						plugin.getYamlManager().getTechnologyKey(), getTechnologies()))
				{
					return false;
				}
			}
			if(!global.exists())
			{
				global.mkdir();
				if(!createAndLoadMap(global, "global",
						plugin.getYamlManager().getTechnologyKey(), getTechnologies()))
				{
					return false;
				}
			}
			return true;
		}
		onlyLoadMap(solo, getTechnologies());
		onlyLoadMap(group, getTechnologies());
		onlyLoadMap(global, getTechnologies());
		return true;
	}
	
	private boolean createAndLoadMap(File dir, String value,
			LinkedHashMap<String, LinkedHashMap<String, Language>> mapI, LinkedHashMap<String, YamlConfiguration> mapII)
	{
		for(Entry<String, LinkedHashMap<String, Language>> e : mapI.entrySet())
		{
			File f = new File(dir.getPath(), e.getKey()+".yml");
			if(!f.exists()) 
			{
				TT.log.info("Create %f%.yml...".replace("%f%", e.getKey()));
				try(InputStream in = plugin.getResource("default.yml"))
				{
					Files.copy(in, f.toPath());
				} catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
			YamlConfiguration y = loadYamlTask(f, new YamlConfiguration());
			if(y == null)
			{
				return false;
			}
			writeFile(f, y, e.getValue());
			mapII.put(e.getKey(), y);
		}
		return true;
	}
	
	private void onlyLoadMap(File directory, LinkedHashMap<String, YamlConfiguration> map)
	{
		File[] listOfFiles = directory.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) 
		{
			if(!listOfFiles[i].isFile()) 
			{
				continue;
			}
			YamlConfiguration y = new YamlConfiguration();
			y = loadYamlTask(listOfFiles[i], y);
			String name = listOfFiles[i].getName().split(".")[0];
			map.put(name, y);
		}
	}
}