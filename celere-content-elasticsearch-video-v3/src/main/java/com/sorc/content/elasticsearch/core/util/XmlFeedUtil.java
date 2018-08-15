/**
 * 
 */
package com.sorc.content.elasticsearch.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author rakesh.moradiya
 *
 */
public class XmlFeedUtil {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	private static SimpleDateFormat sdf_yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd");
	
	public static String isStringNull(String param) {
		if(param != null) {
			return param;
		}
		return "";
	}	
	
	public static String getLastBuildDate()
	{
		try
		{
			return sdf.format(new Date());
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static String getYyyyMmDdStr(String dateStr)
	{
		try
		{
			return sdf_yyyy_mm_dd.format(sdf_yyyy_mm_dd.parse(dateStr));
		}
		catch(Exception e)
		{
			return "";
		}
	}
}
