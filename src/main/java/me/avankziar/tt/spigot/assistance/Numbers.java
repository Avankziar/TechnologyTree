package main.java.me.avankziar.tt.spigot.assistance;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.database.Language.ISO639_2B;

public class Numbers
{
	public static String format(double d, int decimalPlaces, String thousandSeperator, String decimalSeperator)
	{
		BigDecimal result = new BigDecimal(d);
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

		symbols.setGroupingSeparator(thousandSeperator.charAt(0));
		symbols.setDecimalSeparator(decimalSeperator.charAt(0));
		formatter.setMaximumFractionDigits(decimalPlaces);
		formatter.setMinimumFractionDigits(decimalPlaces);
		formatter.setDecimalFormatSymbols(symbols);
		return String.valueOf(formatter.format(result));
	}
	
	public static String format(double d, int decimalPlaces)
	{
		String thousandSeperator = ",";
		String decimalSeperator = ".";
		if(TT.getPlugin().getYamlManager().getLanguageType() == ISO639_2B.GER)
		{
			thousandSeperator = ".";
			decimalSeperator = ",";
		}
		return format(d, decimalPlaces, thousandSeperator, decimalSeperator);
	}
	
	public static String format(double d)
	{
		String thousandSeperator = ",";
		String decimalSeperator = ".";
		if(TT.getPlugin().getYamlManager().getLanguageType() == ISO639_2B.GER)
		{
			thousandSeperator = ".";
			decimalSeperator = ",";
		}
		return format(d, 2, thousandSeperator, decimalSeperator);
	}
}