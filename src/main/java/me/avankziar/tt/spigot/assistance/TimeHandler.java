package main.java.me.avankziar.tt.spigot.assistance;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class TimeHandler
{
	private final static long ss = 1000;
	private static long mm = 1000*60;
	private static long HH = 1000*60*60;
	private static long dd = 1000*60*60*24;
	private static long MM = 1000*60*60*24*30;
	private final static long yyyy = 1000*60*60*24*365;
	
	public static String getDateTime(long l)
	{
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss"));
	}
	
	public static long getDateTime(String l)
	{
		return LocalDateTime.parse(l, DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss"))
				.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
	public static String getDate(long l)
	{
		Date date = new Date(l);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); 
		sdf.setTimeZone(TimeZone.getDefault()); 
		return sdf.format(date);
	}
	
	public static long getDate(String l)
	{
		Instant instant = Instant.now();
		ZoneId systemZone = ZoneId.systemDefault();
		ZoneOffset currentOffsetForMyZone = systemZone.getRules().getOffset(instant);
		
		return LocalDate.parse(l, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
				.atTime(LocalTime.MIDNIGHT).toEpochSecond(currentOffsetForMyZone)*1000;
	}
	
	public static String getTime(long l)
	{
		return new Time(l).toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}
	
	public static String getRepeatingTime(long l, String timeformat) // yyyy-dd-HH:mm
	{
		long ll = l;
		String year = "";
		int y = 0;
		while(ll >= yyyy)
		{
			ll = ll - yyyy;
			y++;
		}
		year += String.valueOf(y);
		String month = "";
		int M = 0;
		while(ll >= MM)
		{
			ll = ll - MM;
			M++;
		}
		month += String.valueOf(M);
		String day = "";
		int d = 0;
		while(ll >= dd)
		{
			ll = ll - dd;
			d++;
		}
		day += String.valueOf(d);
		int H = 0;
		String hour = "";
		while(ll >= HH)
		{
			ll = ll - HH;
			H++;
		}
		if(H < 10)
		{
			hour += String.valueOf(0);
		}
		hour += String.valueOf(H);
		int m = 0;
		String min = "";
		while(ll >= mm)
		{
			ll = ll - mm;
			m++;
		}
		if(m < 10)
		{
			min += String.valueOf(0);
		}
		min += String.valueOf(m)+":";
		int s = 0;
		String sec = "";
		while(ll >= ss)
		{
			ll = ll - ss;
			s++;
		}
		if(s < 10)
		{
			sec += String.valueOf(0);
		}
		sec+= String.valueOf(s);
		String time = timeformat.replace("yyyy", year)
								.replace("MM", month)
								.replace("dd", day)
								.replace("HH", hour)
								.replace("mm", min)
								.replace("ss", sec);
		return time;
	}
	
	public static long getRepeatingTime(String l) //yyyy-MM-dd-HH:mm
	{
		String[] a = l.split("-");
		if(!MatchApi.isInteger(a[0]))
		{
			return 0;
		}
		int y = Integer.parseInt(a[0]);
		int M = Integer.parseInt(a[1]);
		int d = Integer.parseInt(a[2]);
		String[] b = a[3].split(":");
		if(!MatchApi.isInteger(b[0]))
		{
			return 0;
		}
		if(!MatchApi.isInteger(b[1]))
		{
			return 0;
		}
		int H = Integer.parseInt(b[0]);
		int m = Integer.parseInt(b[1]);
		long time = y*yyyy+M*MM+d*dd + H*HH + m*mm;
		return time;
	}
	
	public static long getRepeatingTimeShort(String l) //dd-HH-mm
	{
		String[] a = l.split("-");
		if(!MatchApi.isInteger(a[0]))
		{
			return 0;
		}
		int d = Integer.parseInt(a[0]);
		String[] b = a[1].split(":");
		if(!MatchApi.isInteger(b[0]))
		{
			return 0;
		}
		if(!MatchApi.isInteger(b[1]))
		{
			return 0;
		}
		int H = Integer.parseInt(b[0]);
		int m = Integer.parseInt(b[1]);
		long time = d*dd + H*HH + m*mm;
		return time;
	}
	
	public static long getRepeatingTimeShortV2(String l) //dd-HH-mm
	{
		String[] a = l.split("-");
		if(!MatchApi.isInteger(a[0]))
		{
			return 0;
		}
		int d = Integer.parseInt(a[0]);
		if(!MatchApi.isInteger(a[1]))
		{
			return 0;
		}
		if(!MatchApi.isInteger(a[2]))
		{
			return 0;
		}
		int H = Integer.parseInt(a[1]);
		int m = Integer.parseInt(a[2]);
		long time = d*dd + H*HH + m*mm;
		return time;
	}
	
	public static long getTiming(String l) //dd-HH-mm(-ss)
	{
		long expirationDate = 0L;
		String[] split = l.split("-");
		if(split.length == 3)
		{
			if(split[0].endsWith("d"))
			{
				expirationDate += Long.valueOf(split[0].substring(0, split[0].length()-1)) * 24 * 60 * 60 * 1000;
			}
			if(split[1].endsWith("H"))
			{
				expirationDate += Long.valueOf(split[1].substring(0, split[1].length()-1)) * 60 * 60 * 1000;
			}
			if(split[2].endsWith("m"))
			{
				expirationDate += Long.valueOf(split[2].substring(0, split[2].length()-1)) * 60 * 1000;
			}
		} else if(split.length == 4)
		{
			if(split[0].endsWith("d"))
			{
				expirationDate += Long.valueOf(split[0].substring(0, split[0].length()-1)) * 24 * 60 * 60 * 1000;
			}
			if(split[1].endsWith("H"))
			{
				expirationDate += Long.valueOf(split[1].substring(0, split[1].length()-1)) * 60 * 60 * 1000;
			}
			if(split[2].endsWith("m"))
			{
				expirationDate += Long.valueOf(split[2].substring(0, split[2].length()-1)) * 60 * 1000;
			}
			if(split[3].endsWith("s"))
			{
				expirationDate += Long.valueOf(split[3].substring(0, split[3].length()-1)) * 1000;
			}
		}
		return expirationDate;
	}
}