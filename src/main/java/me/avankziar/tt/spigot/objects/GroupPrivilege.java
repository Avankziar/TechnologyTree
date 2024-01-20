package main.java.me.avankziar.tt.spigot.objects;

import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public enum GroupPrivilege
{
	CAN_SETINDIVIDUAL_DAILY_UPKEEP(0),
	CAN_KICK(0),
	CAN_SETDEFAULT_DAILY_UPKEEP(0),
	CAN_PROMOTE(1),
	CAN_DEMOTE(1),
	CAN_INCREASE_LEVEL(1),
	CAN_RESEARCH(2),
	CAN_SENDINVITE(2),
	CAN_ACCEPT_APPLICATION(2);
	
	private int causalRank; //The Rank from GroupHandler.Position, which the player1 needed at least to change the privilege of a other player2
							//ONLY if the player1 has the privilege also already active
	
	GroupPrivilege(int causalRank)
	{
		this.causalRank = causalRank;
	}
	
	public int getCausalRank()
	{
		return this.causalRank;
	}
	
	public boolean getCausalBoolean(GroupPlayerAffiliation gpa)
	{
		switch(this)
		{
		case CAN_SETINDIVIDUAL_DAILY_UPKEEP: return gpa.isCanSetIndividualDailyUpkeep();
		case CAN_KICK: return gpa.isCanKick();
		case CAN_SETDEFAULT_DAILY_UPKEEP: return gpa.isCanSetDefaultDailyUpkeep();
		case CAN_PROMOTE: return gpa.isCanPromote();
		case CAN_DEMOTE: return gpa.isCanDemote();
		case CAN_INCREASE_LEVEL: return gpa.isCanIncreaseLevel();
		case CAN_RESEARCH: return gpa.isCanResearch();
		case CAN_SENDINVITE: return gpa.isCanInvite();
		case CAN_ACCEPT_APPLICATION: return gpa.isCanAcceptApplication();
		default: return false;
		}
	}
}
