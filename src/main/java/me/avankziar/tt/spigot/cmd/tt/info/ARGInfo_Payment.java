package main.java.me.avankziar.tt.spigot.cmd.tt.info;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;
import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.RewardType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SimpleUnlockedInteraction;

public class ARGInfo_Payment extends ArgumentModule
{	
	public ARGInfo_Payment(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt info payment [material/entitytype] [tooltype]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		Material mat = null;
		EntityType ent = null;
		ToolType tool = null;
		if(args.length >= 4)
		{
			try
			{
				tool = ToolType.valueOf(args[3]);
			} catch(Exception e)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.NoToolType")));
				return;
			}
		}
		if(args.length >= 3)
		{
			//Argument
			try
			{
				mat = Material.valueOf(args[2]);
			} catch(Exception e)
			{
				try
				{
					ent = EntityType.valueOf(args[2]);
				} catch(Exception e1)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.NoMaterialOrEntity")));
					return;
				}
			}
		} else if(player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() != Material.AIR)
		{
			//Inventory
			mat = player.getInventory().getItemInMainHand().getType();
		} else
		{
			//In Sight
			RayTraceResult rtr = player.getWorld().rayTrace(player.getEyeLocation(), player.getEyeLocation().getDirection(), 10.0,
									FluidCollisionMode.NEVER, false, 0.0, t -> (!t.getUniqueId().toString().equals(player.getUniqueId().toString())));
			if(rtr == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.NoMaterialOrEntityInSight")));
				return;
			} else if(rtr.getHitEntity() != null)
			{
				ent = rtr.getHitEntity().getType();
			} else if(rtr.getHitBlock() != null)
			{
				mat = rtr.getHitBlock().getType();
			}
		}
		if(ent != null)
		{
			entity(player, ent, tool);
		} else if(mat != null)
		{
			material(player, mat, tool);
		}
	}
	
	public static LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, Double>>> fakeRewardPlayer(EventType[] et, ToolType[] tool, Material material)
	{
		LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, Double>>> mapI = new LinkedHashMap<>();
		LinkedHashMap<Material, LinkedHashMap<EventType, Double>> mapII = new LinkedHashMap<>();
		LinkedHashMap<EventType, Double> mapIII = new LinkedHashMap<>();
		double d = 1.0;
		for(EventType eventType : et)
		{
			mapIII.put(eventType, d);
		}
		mapII.put(material, mapIII);
		if(tool == null)
		{
			for(ToolType t : ToolType.values())
			{
				mapI.put(t, mapII);
			}
		} else
		{
			for(ToolType t : tool)
			{
				mapI.put(t, mapII);
			}
		}
		return mapI;
	}
	
	public static LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>>> fakeRewardPlayer(EventType[] et, ToolType[] tool, EntityType entityType)
	{
		LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>>> mapI = new LinkedHashMap<>();
		LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>> mapII = new LinkedHashMap<>();
		LinkedHashMap<EventType, Double> mapIII = new LinkedHashMap<>();
		double d = 1.0;
		for(EventType eventType : et)
		{
			mapIII.put(eventType, d);
		}
		mapII.put(entityType, mapIII);
		if(tool == null)
		{
			for(ToolType t : ToolType.values())
			{
				mapI.put(t, mapII);
			}
		} else
		{
			for(ToolType t : tool)
			{
				mapI.put(t, mapII);
			}
		}
		return mapI;
	}
	
	private void entity(Player player, EntityType ent, ToolType tool)
	{
		UUID uuid = player.getUniqueId();
		ArrayList<ToolType> tools = new ArrayList<>(Arrays.asList(ToolType.values()));
		if(tool != null)
		{
			tools = new ArrayList<>(Arrays.asList(tool));
		}
		ArrayList<String> tx = new ArrayList<>();
		tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.Headline")
				.replace("%value%", plugin.getEnumTl() != null ? plugin.getEnumTl().getLocalization(ent) : ent.toString()));
		tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.Info"));
		final LinkedHashMap<RewardType, LinkedHashMap<EventType, Double>> eB = PlayerHandler.externBoosterMap.containsKey(uuid) ? PlayerHandler.externBoosterMap.get(uuid) : new LinkedHashMap<>();
		final LinkedHashMap<EventType, Double> exBoTTExp = eB.containsKey(RewardType.TECHNOLOGYTREE_EXP) ? eB.get(RewardType.TECHNOLOGYTREE_EXP) : new LinkedHashMap<>();
		final LinkedHashMap<EventType, Double> exBoVExp = eB.containsKey(RewardType.VANILLA_EXP) ? eB.get(RewardType.VANILLA_EXP) : new LinkedHashMap<>();
		final LinkedHashMap<EventType, Double> exBoMoney = eB.containsKey(RewardType.MONEY) ? eB.get(RewardType.MONEY) : new LinkedHashMap<>();
		final LinkedHashMap<EventType, Double> exBoCmd = eB.containsKey(RewardType.COMMAND) ? eB.get(RewardType.COMMAND) : new LinkedHashMap<>();
		LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>>> matMap = fakeRewardPlayer(EventType.values(), tools.toArray(new ToolType[tools.size()]), ent);
		for(Entry<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>>> entry0 : matMap.entrySet())
		{
			if(!tools.contains(entry0.getKey()))
			{
				continue;
			}
			ToolType t = entry0.getKey();
			LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>> mapI = matMap.containsKey(t) ? matMap.get(t) : new LinkedHashMap<>();
			for(Entry<EntityType, LinkedHashMap<EventType, Double>> entry : mapI.entrySet())
			{
				if(ent != entry.getKey())
				{
					continue;
				}
				for(Entry<EventType, Double> entryII : entry.getValue().entrySet())
				{
					EventType et = entryII.getKey();
					double amount = entryII.getValue();
					double exBTTExp = exBoTTExp != null ? (exBoTTExp.get(et) != null ? exBoTTExp.get(et).doubleValue() : 0.0) : 0.0;
					double exBVExp = exBoVExp != null ? (exBoVExp.get(et) != null ? exBoVExp.get(et).doubleValue() : 0.0) : 0.0;
					double exBMoney = exBoMoney != null ? (exBoMoney.get(et) != null ? exBoMoney.get(et).doubleValue() : 0.0) : 0.0;
					double exBCmd = exBoCmd != null ? (exBoCmd.get(et) != null ? exBoCmd.get(et).doubleValue() : 0.0) : 0.0;
					if(PlayerHandler.entityTypeInteractionMap.containsKey(uuid)
							&& PlayerHandler.entityTypeInteractionMap.get(uuid).containsKey(t)
							&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(t).containsKey(ent)
							&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(t).get(ent).containsKey(et))
						
					{
						SimpleUnlockedInteraction sui = PlayerHandler.entityTypeInteractionMap.get(uuid).get(t).get(ent).get(et);
						if(sui == null)
						{
							continue;
						}
						double toGiveTTExp = sui.getTechnologyExperience() * amount * (1 + exBTTExp);
						double toGiveVanillaExp = sui.getVanillaExperience() * amount * (1 + exBVExp);
						ArrayList<String> toGiveMoney = new ArrayList<>();
						ArrayList<String> toGiveCmd = new ArrayList<>();
						for(Entry<String, Double> s : sui.getMoneyMap().entrySet())
						{
							double moneyAmount = s.getValue() * amount * (1 + exBMoney);
							String value = "";
							String total = "";
							if("vault".equals(s.getKey()))
							{
								value = plugin.getVaultEco() != null ? s.getValue()+" "+plugin.getVaultEco().currencyNamePlural() : s.getValue()+" Vault";
								total = plugin.getVaultEco() != null ? moneyAmount+" "+plugin.getVaultEco().currencyNamePlural() : moneyAmount+" Vault";
							} else
							{
								EconomyCurrency ec = null;
								if(s.getKey().equalsIgnoreCase("default"))
								{
									ec = plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL);
								} else
								{
									ec = plugin.getIFHEco().getCurrency(s.getKey());
								}
								Account ac = plugin.getIFHEco().getDefaultAccount(uuid, AccountCategory.MAIN, ec);
								value = plugin.getIFHEco().format(s.getValue(), ec,
										plugin.getIFHEco().getDefaultGradationQuantity(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalPlaces(ac.getCurrency()),
										plugin.getIFHEco().getDefaultUseSIPrefix(ac.getCurrency()), plugin.getIFHEco().getDefaultUseSymbol(ac.getCurrency()),
										plugin.getIFHEco().getDefaultThousandSeperator(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalSeperator(ac.getCurrency()));
								total = plugin.getIFHEco().format(moneyAmount, ec,
										plugin.getIFHEco().getDefaultGradationQuantity(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalPlaces(ac.getCurrency()),
										plugin.getIFHEco().getDefaultUseSIPrefix(ac.getCurrency()), plugin.getIFHEco().getDefaultUseSymbol(ac.getCurrency()),
										plugin.getIFHEco().getDefaultThousandSeperator(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalSeperator(ac.getCurrency()));
							}
							toGiveMoney.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.Money")
									.replace("%value%", value)
									.replace("%externbooster%", getPercent(exBMoney))
									.replace("%total%", total));
						}
						for(Entry<String, Double> s : sui.getCommandMap().entrySet())
						{
							double cmdAmount = s.getValue() * amount * (1 + exBCmd);
							toGiveCmd.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.Command")
									.replace("%cmd%", s.getKey())
									.replace("%value%", String.valueOf(s.getValue()))
									.replace("%externbooster%", getPercent(exBCmd))
									.replace("%total%", String.valueOf(cmdAmount)));
						}
						if(toGiveTTExp > 0 || toGiveVanillaExp > 0 || toGiveMoney.size() > 0 || toGiveCmd.size() > 0)
						{
							tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.ToolTypeEventType")
									.replace("%tool%", plugin.getYamlHandler().getLang().getString("Tools."+entry0.getKey().toString()))
									.replace("%event%", plugin.getYamlHandler().getLang().getString("Events."+et.toString())));
						}
						if(toGiveTTExp != 0)
						{
							tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.TTExp")
									.replace("%value%", String.valueOf(sui.getTechnologyExperience()))
									.replace("%externbooster%", getPercent(exBTTExp))
									.replace("%total%", String.valueOf(toGiveTTExp)));
						}
						if(toGiveVanillaExp != 0)
						{
							tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.VanillaExp")
									.replace("%value%", String.valueOf(sui.getVanillaExperience()))
									.replace("%externbooster%", getPercent(exBVExp))
									.replace("%total%", String.valueOf(toGiveVanillaExp)));
						}
						if(toGiveMoney.size() > 0)
						{
							for(String s : toGiveMoney)
							{
								tx.add(s);
							}
						}
						if(toGiveCmd.size() > 0)
						{
							for(String s : toGiveCmd)
							{
								tx.add(s);
							}
						}
					}
				}
			}
		}
		for(String s : tx)
		{
			player.sendMessage(ChatApi.tl(s));
		}
	}
	
	private void material(Player player, Material mat, ToolType tool)
	{
		UUID uuid = player.getUniqueId();
		ArrayList<ToolType> tools = new ArrayList<>(Arrays.asList(ToolType.values()));
		if(tool != null)
		{
			tools = new ArrayList<>(Arrays.asList(tool));
		}
		ArrayList<String> tx = new ArrayList<>();
		tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.Headline")
				.replace("%value%", plugin.getEnumTl() != null ? plugin.getEnumTl().getLocalization(mat) : mat.toString()));
		tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.Info"));
		final LinkedHashMap<RewardType, LinkedHashMap<EventType, Double>> eB = PlayerHandler.externBoosterMap.containsKey(uuid) ? PlayerHandler.externBoosterMap.get(uuid) : new LinkedHashMap<>();
		final LinkedHashMap<EventType, Double> exBoTTExp = eB.containsKey(RewardType.TECHNOLOGYTREE_EXP) ? eB.get(RewardType.TECHNOLOGYTREE_EXP) : new LinkedHashMap<>();
		final LinkedHashMap<EventType, Double> exBoVExp = eB.containsKey(RewardType.VANILLA_EXP) ? eB.get(RewardType.VANILLA_EXP) : new LinkedHashMap<>();
		final LinkedHashMap<EventType, Double> exBoMoney = eB.containsKey(RewardType.MONEY) ? eB.get(RewardType.MONEY) : new LinkedHashMap<>();
		final LinkedHashMap<EventType, Double> exBoCmd = eB.containsKey(RewardType.COMMAND) ? eB.get(RewardType.COMMAND) : new LinkedHashMap<>();
		LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, Double>>> matMap = fakeRewardPlayer(EventType.values(), tools.toArray(new ToolType[tools.size()]), mat);
		
		for(Entry<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, Double>>> entry0 : matMap.entrySet())
		{
			if(!tools.contains(entry0.getKey()))
			{
				continue;
			}
			ToolType t = entry0.getKey();
			LinkedHashMap<Material, LinkedHashMap<EventType, Double>> mapI = matMap.containsKey(t) ? matMap.get(t) : new LinkedHashMap<>();
			for(Entry<Material, LinkedHashMap<EventType, Double>> entry : mapI.entrySet())
			{
				Material m = entry.getKey();
				LinkedHashMap<EventType, Double> mapII = mapI.containsKey(m) ? mapI.get(m) : new LinkedHashMap<>();
				for(Entry<EventType, Double> entryII : mapII.entrySet())
				{
					EventType et = entryII.getKey();
					double amount = entryII.getValue();
					double exBTTExp = exBoTTExp != null ? (exBoTTExp.get(et) != null ? exBoTTExp.get(et).doubleValue() : 0.0) : 0.0;
					double exBVExp = exBoVExp != null ? (exBoVExp.get(et) != null ? exBoVExp.get(et).doubleValue() : 0.0) : 0.0;
					double exBMoney = exBoMoney != null ? (exBoMoney.get(et) != null ? exBoMoney.get(et).doubleValue() : 0.0) : 0.0;
					double exBCmd = exBoCmd != null ? (exBoCmd.get(et) != null ? exBoCmd.get(et).doubleValue() : 0.0) : 0.0;
					if(PlayerHandler.materialInteractionMap.containsKey(uuid)
							&& PlayerHandler.materialInteractionMap.get(uuid).containsKey(t)
							&& PlayerHandler.materialInteractionMap.get(uuid).get(t).containsKey(m)
							&& PlayerHandler.materialInteractionMap.get(uuid).get(t).get(m).containsKey(et))
						
					{
						SimpleUnlockedInteraction sui = PlayerHandler.materialInteractionMap.get(uuid).get(t).get(m).get(et);
						if(sui == null)
						{
							continue;
						}
						double toGiveTTExp = sui.getTechnologyExperience() * amount * (1 + exBTTExp);
						double toGiveVanillaExp = sui.getVanillaExperience() * amount * (1 + exBVExp);
						ArrayList<String> toGiveMoney = new ArrayList<>();
						ArrayList<String> toGiveCmd = new ArrayList<>();
						for(Entry<String, Double> s : sui.getMoneyMap().entrySet())
						{
							double moneyAmount = s.getValue() * amount * (1 + exBMoney);
							String value = "";
							String total = "";
							if("vault".equals(s.getKey()))
							{
								value = plugin.getVaultEco() != null ? s.getValue()+" "+plugin.getVaultEco().currencyNamePlural() : s.getValue()+" Vault";
								total = plugin.getVaultEco() != null ? moneyAmount+" "+plugin.getVaultEco().currencyNamePlural() : moneyAmount+" Vault";
							} else
							{
								EconomyCurrency ec = null;
								if(s.getKey().equalsIgnoreCase("default"))
								{
									ec = plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL);
								} else
								{
									ec = plugin.getIFHEco().getCurrency(s.getKey());
								}
								Account ac = plugin.getIFHEco().getDefaultAccount(uuid, AccountCategory.MAIN, ec);
								value = plugin.getIFHEco().format(s.getValue(), ec,
										plugin.getIFHEco().getDefaultGradationQuantity(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalPlaces(ac.getCurrency()),
										plugin.getIFHEco().getDefaultUseSIPrefix(ac.getCurrency()), plugin.getIFHEco().getDefaultUseSymbol(ac.getCurrency()),
										plugin.getIFHEco().getDefaultThousandSeperator(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalSeperator(ac.getCurrency()));
								total = plugin.getIFHEco().format(moneyAmount, ec,
										plugin.getIFHEco().getDefaultGradationQuantity(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalPlaces(ac.getCurrency()),
										plugin.getIFHEco().getDefaultUseSIPrefix(ac.getCurrency()), plugin.getIFHEco().getDefaultUseSymbol(ac.getCurrency()),
										plugin.getIFHEco().getDefaultThousandSeperator(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalSeperator(ac.getCurrency()));
							}
							toGiveMoney.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.Money")
									.replace("%value%", value)
									.replace("%externbooster%", getPercent(exBMoney))
									.replace("%total%", total));
						}
						for(Entry<String, Double> s : sui.getCommandMap().entrySet())
						{
							double cmdAmount = s.getValue() * amount * (1 + exBCmd);
							toGiveCmd.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.Command")
									.replace("%cmd%", s.getKey())
									.replace("%value%", String.valueOf(s.getValue()))
									.replace("%externbooster%", getPercent(exBCmd))
									.replace("%total%", String.valueOf(cmdAmount)));
						}
						if(toGiveTTExp > 0 || toGiveVanillaExp > 0 || toGiveMoney.size() > 0 || toGiveCmd.size() > 0)
						{
							tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.ToolTypeEventType")
									.replace("%tool%", plugin.getYamlHandler().getLang().getString("Tools."+entry0.getKey().toString()))
									.replace("%event%", plugin.getYamlHandler().getLang().getString("Events."+et.toString())));
						}
						if(toGiveTTExp != 0)
						{
							tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.TTExp")
									.replace("%value%", String.valueOf(sui.getTechnologyExperience()))
									.replace("%externbooster%", getPercent(exBTTExp))
									.replace("%total%", String.valueOf(toGiveTTExp)));
						}
						if(toGiveVanillaExp != 0)
						{
							tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.VanillaExp")
									.replace("%value%", String.valueOf(sui.getVanillaExperience()))
									.replace("%externbooster%", getPercent(exBVExp))
									.replace("%total%", String.valueOf(toGiveVanillaExp)));
						}
						if(toGiveMoney.size() > 0)
						{
							for(String s : toGiveMoney)
							{
								tx.add(s);
							}
						}
						if(toGiveCmd.size() > 0)
						{
							for(String s : toGiveCmd)
							{
								tx.add(s);
							}
						}
					}
				}
			}
		}
		for(String s : tx)
		{
			player.sendMessage(ChatApi.tl(s));
		}
	}
	
	private String getPercent(double factor)
	{
		if(factor >= 0)
		{
			double v = factor*100;
			return plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.PositivePercent")
					.replace("%value%", String.valueOf(v));
		} else
		{
			double v = factor*-100;
			return plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.NegativePercent")
					.replace("%value%", String.valueOf(v));
		}
	}
}