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

import org.apache.commons.io.FilenameUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.database.Language.ISO639_2B;
import main.java.me.avankziar.tt.spigot.gui.objects.GuiType;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;

public class YamlHandler
{
	private TT plugin;
	private File config = null;
	private YamlConfiguration cfg = new YamlConfiguration();
	
	private File commands = null;
	private YamlConfiguration com = new YamlConfiguration();
	
	private String languages = "ENG";
	private File language = null;
	private YamlConfiguration lang = new YamlConfiguration();
	private File mvelanguage = null;
	private YamlConfiguration mvelang = new YamlConfiguration();
	
	private File recipeList = null;
	private YamlConfiguration recipeLists = new YamlConfiguration();
	
	private LinkedHashMap<GuiType, YamlConfiguration> gui = new LinkedHashMap<>();
	
	private LinkedHashMap<String, YamlConfiguration> itemGenerator = new LinkedHashMap<>();
	
	private LinkedHashMap<String, YamlConfiguration> mainCategories = new LinkedHashMap<>();
	private LinkedHashMap<String, YamlConfiguration> subCategories = new LinkedHashMap<>();
	private LinkedHashMap<String, YamlConfiguration> technologies = new LinkedHashMap<>();
	
	private LinkedHashMap<String, YamlConfiguration> blastingRecipe = new LinkedHashMap<>();
	private LinkedHashMap<String, YamlConfiguration> campfireRecipe = new LinkedHashMap<>();
	private LinkedHashMap<String, YamlConfiguration> furnaceRecipe = new LinkedHashMap<>();
	private LinkedHashMap<String, YamlConfiguration> shapedRecipe = new LinkedHashMap<>();
	private LinkedHashMap<String, YamlConfiguration> shapelessRecipe = new LinkedHashMap<>();
	//private LinkedHashMap<String, YamlConfiguration> smithingTransformRecipe = new LinkedHashMap<>();
	//private LinkedHashMap<String, YamlConfiguration> smithingTrimRecipe = new LinkedHashMap<>();
	private LinkedHashMap<String, YamlConfiguration> smokingRecipe = new LinkedHashMap<>();
	private LinkedHashMap<String, YamlConfiguration> stonecuttingRecipe = new LinkedHashMap<>();

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
	
	public YamlConfiguration getMVELang()
	{
		return mvelang;
	}
	
	public YamlConfiguration getRecipeList()
	{
		return recipeLists;
	}
	
	public YamlConfiguration getGui(GuiType guiType)
	{
		return gui.get(guiType);
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
	
	public LinkedHashMap<String, YamlConfiguration> getBlastingRecipe()
	{
		return blastingRecipe;
	}
	
	public LinkedHashMap<String, YamlConfiguration> getCampfireRecipe()
	{
		return campfireRecipe;
	}
	
	public LinkedHashMap<String, YamlConfiguration> getFurnaceRecipe()
	{
		return furnaceRecipe;
	}
	
	public LinkedHashMap<String, YamlConfiguration> getShapedRecipe()
	{
		return shapedRecipe;
	}
	
	public LinkedHashMap<String, YamlConfiguration> getShapelessRecipe()
	{
		return shapelessRecipe;
	}
	
	/*public LinkedHashMap<String, YamlConfiguration> getSmithingTransformRecipe()
	{
		return smithingTransformRecipe;
	}
	
	public LinkedHashMap<String, YamlConfiguration> getSmithingTrimRecipe()
	{
		return smithingTrimRecipe;
	}*/
	
	public LinkedHashMap<String, YamlConfiguration> getSmokingRecipe()
	{
		return smokingRecipe;
	}
	
	public LinkedHashMap<String, YamlConfiguration> getStonecuttingRecipe()
	{
		return stonecuttingRecipe;
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
		recipeList = new File(plugin.getDataFolder(), "recipelist.yml");
		if(!recipeList.exists()) 
		{
			TT.log.info("Create recipelist.yml...");
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, recipeList.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		recipeLists = loadYamlTask(recipeList, recipeLists);
		if (recipeLists == null)
		{
			return false;
		}
		writeFile(recipeList, recipeLists, plugin.getYamlManager().getRecipeListKey());
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
		if(!mkdirGUIs())
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
		if(!mkdirRecipe())
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
		mvelanguage = new File(directory.getPath(), "mve_"+languageString+".yml");
		if(!mvelanguage.exists()) 
		{
			TT.log.info("Create mve_%lang%.yml...".replace("%lang%", languageString));
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, mvelanguage.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		mvelang = loadYamlTask(mvelanguage, mvelang);
		if(mvelang == null)
		{
			return false;
		}
		writeFile(mvelanguage, mvelang, plugin.getYamlManager().getModifierValueEntryLanguageKey());
		return true;
	}
	
	private boolean mkdirGUIs()
	{
		String languageString = plugin.getYamlManager().getLanguageType().toString().toLowerCase();
		File directory = new File(plugin.getDataFolder()+"/Gui/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		List<GuiType> list = new ArrayList<GuiType>(EnumSet.allOf(GuiType.class));
		for(GuiType g : list)
		{
			File gf = new File(directory.getPath(), languageString+"_"+g.toString()+".yml");
			if(gf.exists())
			{
				YamlConfiguration gui = loadYamlTask(gf, new YamlConfiguration());
				if (gui == null)
				{
					return false;
				}
				if(plugin.getYamlManager().getGuiKey(g) == null)
				{
					return false;
				}
				this.gui.put(g, gui);
				continue;
			}
			TT.log.info("Create %lang%.yml...".replace("%lang%", languageString+"_"+g.toString()));
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, gf.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			YamlConfiguration gui = loadYamlTask(gf, new YamlConfiguration());
			if (gui == null)
			{
				return false;
			}
			if(plugin.getYamlManager().getGuiKey(g) == null)
			{
				return false;
			}
			writeFile(gf, gui, plugin.getYamlManager().getGuiKey(g));
			this.gui.put(g, gui);
		}
		return true;
	}
	
	private boolean mkdirItems()
	{
		File directory = new File(plugin.getDataFolder()+"/Items/");
		if(!directory.exists())
		{
			directory.mkdir();
			if(!createAndLoadMap(directory,
					plugin.getYamlManager().getItemGeneratorKey(), getItemGenerators()))
			{
				return false;
			}
		} else
		{
			onlyDeepLoadMap(directory, getItemGenerators());
		}
		TT.log.info("Loaded %x% Items.".replace("%x%", String.valueOf(getItemGenerators().size())));
		return true;
	}
	
	private boolean mkdirMainCategories()
	{
		File solo = new File(plugin.getDataFolder()+"/Tech/Solo/MainCategory/");
		File group = new File(plugin.getDataFolder()+"/Tech/Group/MainCategory/");
		File global = new File(plugin.getDataFolder()+"/Tech/Global/MainCategory/");
		if(!solo.exists())
		{
			solo.mkdirs();
			if(!createAndLoadMap(solo,
					plugin.getYamlManager().getMainCategoryKey().get(PlayerAssociatedType.SOLO), getMainCategories()))
			{
				return false;
			}
		} else
		{
			onlyDeepLoadMap(solo, getMainCategories());
		}
		if(!group.exists())
		{
			group.mkdirs();
			if(!createAndLoadMap(group,
					plugin.getYamlManager().getMainCategoryKey().get(PlayerAssociatedType.GROUP), getMainCategories()))
			{
				return false;
			}
		} else
		{
			onlyDeepLoadMap(group, getMainCategories());
		}
		if(!global.exists())
		{
			global.mkdirs();
			if(!createAndLoadMap(global,
					plugin.getYamlManager().getMainCategoryKey().get(PlayerAssociatedType.GLOBAL), getMainCategories()))
			{
				return false;
			}
		} else
		{
			onlyDeepLoadMap(global, getMainCategories());
		}
		TT.log.info("Loaded %x% MainCategories.".replace("%x%", String.valueOf(getMainCategories().size())));
		return true;
	}
	
	private boolean mkdirSubCategories()
	{
		File solo = new File(plugin.getDataFolder()+"/Tech/Solo/SubCategory/");
		File group = new File(plugin.getDataFolder()+"/Tech/Group/SubCategory/");
		File global = new File(plugin.getDataFolder()+"/Tech/Global/SubCategory/");
		if(!solo.exists())
		{
			solo.mkdirs();
			if(!createAndLoadMap(solo,
					plugin.getYamlManager().getSubCategoryKey().get(PlayerAssociatedType.SOLO), getSubCategories()))
			{
				return false;
			}
		} else
		{
			onlyDeepLoadMap(solo, getSubCategories());
		}
		if(!group.exists())
		{
			group.mkdirs();
			if(!createAndLoadMap(group,
					plugin.getYamlManager().getSubCategoryKey().get(PlayerAssociatedType.GROUP), getSubCategories()))
			{
				return false;
			}
		} else
		{
			onlyLoadMap(group, getSubCategories());
		}
		if(!global.exists())
		{
			global.mkdirs();
			if(!createAndLoadMap(global,
					plugin.getYamlManager().getSubCategoryKey().get(PlayerAssociatedType.GLOBAL), getSubCategories()))
			{
				return false;
			}
		} else
		{
			onlyDeepLoadMap(global, getSubCategories());
		}
		TT.log.info("Loaded %x% SubCategories.".replace("%x%", String.valueOf(getSubCategories().size())));
		return true;
	}
	
	private boolean mkdirTechnologies()
	{
		File solo = new File(plugin.getDataFolder()+"/Tech/Solo/Technology/");
		File group = new File(plugin.getDataFolder()+"/Tech/Group/Technology/");
		File global = new File(plugin.getDataFolder()+"/Tech/Global/Technology/");
		if(!solo.exists())
		{
			solo.mkdirs();
			if(!createAndLoadMap(solo,
					plugin.getYamlManager().getTechnologyKey().get(PlayerAssociatedType.SOLO), getTechnologies()))
			{
				return false;
			}
		} else
		{
			onlyDeepLoadMap(solo, getTechnologies());
		}
		if(!group.exists())
		{
			group.mkdirs();
			if(!createAndLoadMap(group,
					plugin.getYamlManager().getTechnologyKey().get(PlayerAssociatedType.GROUP), getTechnologies()))
			{
				return false;
			}
		} else
		{
			onlyDeepLoadMap(group, getTechnologies());
		}
		if(!global.exists())
		{
			global.mkdirs();
			if(!createAndLoadMap(global,
					plugin.getYamlManager().getTechnologyKey().get(PlayerAssociatedType.GLOBAL), getTechnologies()))
			{
				return false;
			}
		} else
		{
			onlyDeepLoadMap(global, getTechnologies());
		}
		TT.log.info("Loaded %x% Technologies.".replace("%x%", String.valueOf(getTechnologies().size())));
		return true;
	}
	
	private boolean mkdirRecipe()
	{
		File blasting = new File(plugin.getDataFolder()+"/Recipe/Blasting/");
		File campfire = new File(plugin.getDataFolder()+"/Recipe/Campfire/");
		File furnace = new File(plugin.getDataFolder()+"/Recipe/Furnace/");
		File shaped = new File(plugin.getDataFolder()+"/Recipe/Shaped/");
		File shapeless = new File(plugin.getDataFolder()+"/Recipe/Shapeless/");
		//File smithingTransform = new File(plugin.getDataFolder()+"/Recipe/SmithingTransform/");
		//File smithingTrim = new File(plugin.getDataFolder()+"/Recipe/SmithingTrim/");
		File smoking = new File(plugin.getDataFolder()+"/Recipe/Smoking/");
		File stonecutting = new File(plugin.getDataFolder()+"/Recipe/Stonecutting/");
		if(!blasting.exists())
		{
			blasting.mkdir();
			if(!createAndLoadMap(blasting,
					plugin.getYamlManager().getBlastingRecipeKey(), getBlastingRecipe()))
			{
				return false;
			}
		} else
		{
			onlyLoadMap(blasting, getBlastingRecipe());
		}
		TT.log.info("Loaded %x% BlastingRecipes.".replace("%x%", String.valueOf(getBlastingRecipe().size())));
		if(!campfire.exists())
		{
			campfire.mkdir();
			if(!createAndLoadMap(campfire,
					plugin.getYamlManager().getCampfireRecipeKey(), getCampfireRecipe()))
			{
				return false;
			}
		} else
		{
			onlyLoadMap(campfire, getCampfireRecipe());
		}
		TT.log.info("Loaded %x% CampfireRecipes.".replace("%x%", String.valueOf(getCampfireRecipe().size())));
		if(!furnace.exists())
		{
			furnace.mkdir();
			if(!createAndLoadMap(furnace,
					plugin.getYamlManager().getFurnaceRecipeKey(), getFurnaceRecipe()))
			{
				return false;
			}
		} else
		{
			onlyLoadMap(furnace, getFurnaceRecipe());
		}
		TT.log.info("Loaded %x% FurnaceRecipes.".replace("%x%", String.valueOf(getFurnaceRecipe().size())));
		if(!shaped.exists())
		{
			shaped.mkdir();
			if(!createAndLoadMap(shaped,
					plugin.getYamlManager().getShapedRecipeKey(), getShapedRecipe()))
			{
				return false;
			}
		} else
		{
			onlyLoadMap(shaped, getShapedRecipe());
		}
		TT.log.info("Loaded %x% ShapedRecipes.".replace("%x%", String.valueOf(getShapedRecipe().size())));
		if(!shapeless.exists())
		{
			shapeless.mkdir();
			if(!createAndLoadMap(shapeless,
					plugin.getYamlManager().getShapelessRecipeKey(), getShapelessRecipe()))
			{
				return false;
			}
		} else
		{
			onlyLoadMap(shapeless, getShapelessRecipe());
		}
		TT.log.info("Loaded %x% ShapelessRecipes.".replace("%x%", String.valueOf(getShapelessRecipe().size())));
		/*if(!smithingTransform.exists())
		{
			smithingTransform.mkdir();
			if(!createAndLoadMap(smithingTransform,
					plugin.getYamlManager().getSmithingTransformRecipeKey(), getSmithingTransformRecipe()))
			{
				return false;
			}
		} else
		{
			onlyLoadMap(smithingTransform, getSmithingTransformRecipe());
		}
		TT.log.info("Loaded %x% SmithingRecipes.".replace("%x%", String.valueOf(getSmithingTransformRecipe().size())));*/
		/*if(!smithingTrim.exists())
		{
			smithingTrim.mkdir();
			if(!createAndLoadMap(smithingTrim,
					plugin.getYamlManager().getSmithingTrimRecipeKey(), getSmithingTrimRecipe()))
			{
				return false;
			}
		} else
		{
			onlyLoadMap(smithingTrim, getSmithingTrimRecipe());
		}*/
		if(!smoking.exists())
		{
			smoking.mkdirs();
			if(!createAndLoadMap(smoking,
					plugin.getYamlManager().getSmokingRecipeKey(), getSmokingRecipe()))
			{
				return false;
			}
		} else
		{
			onlyLoadMap(smoking, getSmokingRecipe());
		}
		TT.log.info("Loaded %x% SmokingRecipes.".replace("%x%", String.valueOf(getSmokingRecipe().size())));
		if(!stonecutting.exists())
		{
			stonecutting.mkdirs();
			if(!createAndLoadMap(stonecutting,
					plugin.getYamlManager().getStonecuttingRecipeKey(), getStonecuttingRecipe()))
			{
				return false;
			}
		} else
		{
			onlyLoadMap(stonecutting, getStonecuttingRecipe());
		}
		TT.log.info("Loaded %x% StonecuttingRecipes.".replace("%x%", String.valueOf(getStonecuttingRecipe().size())));
		return true;
	}
	
	private boolean createAndLoadMap(File dir,
			LinkedHashMap<String, LinkedHashMap<String, Language>> mapI, LinkedHashMap<String, YamlConfiguration> mapII)
	{
		if(mapI == null)
		{
			return true;
		}
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		for(Entry<String, LinkedHashMap<String, Language>> e : mapI.entrySet())
		{
			File f = new File(dir.getPath(), e.getKey()+".yml");
			if(!f.exists()) 
			{
				//TT.log.info("Create %f%.yml...".replace("%f%", e.getKey()));
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
			YamlConfiguration y = loadYamlTask(listOfFiles[i], new YamlConfiguration());
			map.put(FilenameUtils.removeExtension(listOfFiles[i].getName()), y);
		}
	}
	
	private void onlyDeepLoadMap(File directory, LinkedHashMap<String, YamlConfiguration> map)
	{
		File[] listOfFiles = directory.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) 
		{
			if(!listOfFiles[i].isFile()) 
			{
				onlyDeepLoadMap(listOfFiles[i], map);
				continue;
			}
			YamlConfiguration y = loadYamlTask(listOfFiles[i], new YamlConfiguration());
			map.put(FilenameUtils.removeExtension(listOfFiles[i].getName()), y);
		}
	}
}