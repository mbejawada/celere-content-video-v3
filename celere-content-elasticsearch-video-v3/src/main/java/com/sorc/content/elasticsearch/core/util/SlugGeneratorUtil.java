/**
 * 
 */
package com.sorc.content.elasticsearch.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author rakesh.moradiya
 *
 */
public class SlugGeneratorUtil {

	private static final char HYPEN_CHAR 		= '-';
	private static final char UNDERSCORE_CHAR 	= '_';
	private static final char AMPERSAND_CHAR 	= '&';
	
	public static String urlDeliminator = String.valueOf(HYPEN_CHAR); 	
	
	public static  String generateSlug(String str) {
		if (str == null || str.trim().length() == 0) { 
			return null;
		}
		String res = StringUtils.stripAccents(str);			
		res = removeNotSupportedChars(res);
		res = trimDuplicates(res);
		res = res.toLowerCase();		
		return res;
	}
	
	private static String trimDuplicates(String seq) {
		String res = seq.trim();
		res = res.replaceAll("( |_|-)+", String.valueOf(urlDeliminator));
		res = res.replaceAll("(&)+", "and");		
		return res;
	}
	
	private static String removeNotSupportedChars(CharSequence seq) {
		StringBuilder sb = new StringBuilder();
		int len = seq.length();
		for(int i=0; i < len; i++) {
			char ch = seq.charAt(i);
			if (Character.isSpaceChar(ch)){
				sb.append(' ');
			}
			if (Character.isLetterOrDigit(ch) || ch == UNDERSCORE_CHAR || ch == HYPEN_CHAR || ch == AMPERSAND_CHAR) {
				sb.append(ch);
			} 
		}
		
		return sb.toString();
	}
	
	public String getUrlDeliminator() {
		return urlDeliminator;
	}


	@SuppressWarnings("static-access")
	public void setUrlDeliminator(String urlDeliminator) {
		this.urlDeliminator = urlDeliminator;
	}
}
