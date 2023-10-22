package main.java.me.avankziar.tt.spigot.event;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import main.java.me.avankziar.tt.spigot.objects.ram.misc.RewardSummary;

public class PostRewardEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	private UUID playerUUID;
	private String playerName;
	private ArrayList<RewardSummary> rewardSummaryList;
	
	public PostRewardEvent(UUID playerUUID, String playerName, ArrayList<RewardSummary> rewardSummaryList)
	{
		setPlayerUUID(playerUUID);
		setPlayerName(playerName);
		setRewardSummaryList(rewardSummaryList);
	}
	
	public HandlerList getHandlers() 
    {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() 
    {
        return HANDLERS;
    }

	public UUID getPlayerUUID()
	{
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}

	public ArrayList<RewardSummary> getRewardSummaryList()
	{
		return rewardSummaryList;
	}

	public void setRewardSummaryList(ArrayList<RewardSummary> rewardSummaryList)
	{
		this.rewardSummaryList = rewardSummaryList;
	}
}