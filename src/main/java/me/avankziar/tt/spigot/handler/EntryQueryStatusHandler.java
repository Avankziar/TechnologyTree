package main.java.me.avankziar.tt.spigot.handler;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nullable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.objects.EntryQueryType;
import main.java.me.avankziar.tt.spigot.objects.EntryStatusType;
import main.java.me.avankziar.tt.spigot.objects.mysql.GlobalEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.SoloEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.Technology;

public class EntryQueryStatusHandler
{
	private static TT plugin = TT.getPlugin();
	
	public static SoloEntryQueryStatus getSoloEntryHighestResearchLevel(UUID uuid, Technology t, EntryQueryType eqt, @Nullable EntryStatusType est)
	{
		ArrayList<SoloEntryQueryStatus> highestEntryResearchedList = null;
		if(est != null)
		{
			highestEntryResearchedList = SoloEntryQueryStatus.convert(
					plugin.getMysqlHandler().getList(Type.SOLO_ENTRYQUERYSTATUS,
					"`research_level` DESC", 0, 1,
					"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ? AND `status_type` = ?",
					uuid.toString(), t.getInternName(), eqt.toString(), est.toString()));
		} else
		{
			highestEntryResearchedList = SoloEntryQueryStatus.convert(
					plugin.getMysqlHandler().getList(Type.SOLO_ENTRYQUERYSTATUS,
					"`research_level` DESC", 0, 1,
					"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
					uuid.toString(), t.getInternName(), eqt.toString()));
		}
		return highestEntryResearchedList.size() == 0 ? null : highestEntryResearchedList.get(0);
	}
	
	public static GroupEntryQueryStatus getGroupEntryHighestResearchLevel(UUID uuid, Technology t, EntryQueryType eqt, @Nullable EntryStatusType est)
	{
		GroupData gd = GroupHandler.getGroup(uuid);
		String gn = "";
		if(gd != null)
		{
			gn = gd.getGroupName();
		}
		ArrayList<GroupEntryQueryStatus> highestEntryResearchedList = null;
		if(est != null)
		{
			highestEntryResearchedList = GroupEntryQueryStatus.convert(
					plugin.getMysqlHandler().getList(Type.GROUP_ENTRYQUERYSTATUS,
					"`research_level` DESC", 0, 1,
					"`group_name` = ? AND `intern_name` = ? AND `entry_query_type` = ? AND `status_type` = ?",
					gn, t.getInternName(), eqt.toString(), est.toString()));
		} else
		{
			highestEntryResearchedList = GroupEntryQueryStatus.convert(
					plugin.getMysqlHandler().getList(Type.GROUP_ENTRYQUERYSTATUS,
					"`research_level` DESC", 0, 1,
					"`group_name` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
					gn, t.getInternName(), eqt.toString()));
		}
		return highestEntryResearchedList.size() == 0 ? null : highestEntryResearchedList.get(0);
	}
	
	public static GlobalEntryQueryStatus getGlobalEntryHighestResearchLevel(Technology t, EntryQueryType eqt, @Nullable EntryStatusType est)
	{
		ArrayList<GlobalEntryQueryStatus> highestEntryResearchedList = null;
		if(est != null)
		{
			highestEntryResearchedList = GlobalEntryQueryStatus.convert(plugin.getMysqlHandler().getList(
					Type.GLOBAL_ENTRYQUERYSTATUS,
					"`research_level` DESC", 0, 1,
					"`intern_name` = ? AND `entry_query_type` = ? AND `status_type` = ?",
					t.getInternName(), eqt.toString(), est.toString()));
		} else
		{
			highestEntryResearchedList = GlobalEntryQueryStatus.convert(plugin.getMysqlHandler().getList(
					Type.GLOBAL_ENTRYQUERYSTATUS,
					"`research_level` DESC", 0, 1,
					"`intern_name` = ? AND `entry_query_type` = ?",
					t.getInternName(), eqt.toString()));
		}
		return highestEntryResearchedList.size() == 0 ? null : highestEntryResearchedList.get(0);
	}
}