package com.poseidon.dolphin.simulator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CurrencyTextParser {
	private static final String[] NUMERALS = {"", "십", "백", "천", "만", "십만", "백만", "천만", "억"};
	
	public static Long parse(final String source) {
		Pattern normalPattern = Pattern.compile("([0-9,]+)원");
		Matcher normalMatcher = normalPattern.matcher(source);
		if(normalMatcher.find()) {
			return Long.valueOf(normalMatcher.group(1).replaceAll(",", ""));
		}
		
		Pattern pattern = Pattern.compile("([0-9]+)([ㄱ-ㅎ가-힣]+)원");
		Matcher matcher = pattern.matcher(source);
		
		if(matcher.find()) {
			long prefix = Long.valueOf(matcher.group(1));
			long suffix = convertLongByNumeral(matcher.group(2));
			return prefix * suffix;
		}
		return null;
	}
	
	private static long convertLongByNumeral(String text) {
		int index = -1;
		for(int i=0; i<NUMERALS.length; i++) {
			if(NUMERALS[i].equals(text)) {
				index = i;
				break;
			}
		}
		return index > 0 ? (long)Math.pow(10, index) : 0l;
	}
	
}
