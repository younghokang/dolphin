package com.poseidon.dolphin.simulator;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.springframework.util.StringUtils;

public class CurrencyTextParserTests {
	
	@Test
	public void givenTextThenConvertLong() {
		assertThat(CurrencyTextParser.parse("5억원")).isEqualTo(500000000l);
		assertThat(CurrencyTextParser.parse("10억원")).isEqualTo(1000000000l);
		assertThat(CurrencyTextParser.parse("1천만원")).isEqualTo(10000000l);
		assertThat(CurrencyTextParser.parse("3백만원")).isEqualTo(3000000l);
		assertThat(CurrencyTextParser.parse("20만원")).isEqualTo(200000l);
		assertThat(CurrencyTextParser.parse("120만원")).isEqualTo(1200000l);
	}
	
	@Test
	public void givenOriginalTextThenConvertLong() {
		assertThat(CurrencyTextParser.parse("최소금액 10만원이상")).isEqualTo(100000l);
		assertThat(CurrencyTextParser.parse("1인한도 : 월30만원")).isEqualTo(300000l);
		assertThat(CurrencyTextParser.parse("계약금액 1백만원 이상")).isEqualTo(1000000l);
		assertThat(CurrencyTextParser.parse("월10만원이상 20만원이하")).isEqualTo(100000l);
		assertThat(CurrencyTextParser.parse("최저 1만원 ~ 20만원이내")).isEqualTo(10000l);
		assertThat(CurrencyTextParser.parse("1인당 가입한도: 월 300만원")).isEqualTo(3000000l);
		assertThat(CurrencyTextParser.parse("초회 및 매회 입금 1만원이상 분기별 300만원이내")).isEqualTo(10000l);
		assertThat(CurrencyTextParser.parse("정액적립식:10,000원 이상 자유적립식: 1,000원 이상")).isEqualTo(10000l);
		assertThat(CurrencyTextParser.parse("자유적립식인 경우 2회차 이후 월1천만원 이내")).isEqualTo(10000000l);
	}
	
	@Test
	public void test() {
		String text = "최소금액 10만원 이상";
		text = StringUtils.trimAllWhitespace(text);
		Pattern pattern = Pattern.compile("([0-9]+)([ㄱ-ㅎ가-힣]+)원(이상|~)");
		Matcher matcher = pattern.matcher(text);
		if(matcher.find()) {
			assertThat(matcher.groupCount()).isEqualTo(3);
			assertThat(matcher.group(1)).isEqualTo("10");
			assertThat(matcher.group(2)).isEqualTo("만");
			assertThat(matcher.group(3)).isEqualTo("이상");
		}
		
		text = "최저 1만원 ~ 20만원이내";
		text = StringUtils.trimAllWhitespace(text);
		matcher = pattern.matcher(text);
		if(matcher.find()) {
			assertThat(matcher.groupCount()).isEqualTo(3);
			assertThat(matcher.group(1)).isEqualTo("1");
			assertThat(matcher.group(2)).isEqualTo("만");
			assertThat(matcher.group(3)).isEqualTo("~");
		}
	}
	
}
