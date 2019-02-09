package com.poseidon.dolphin.simulator.company;

import java.util.Arrays;

public enum Area {
	SEOUL("01", "서울"),
	BUSAN("02", "부산"),
	DAEGU("03", "대구"),
	INCHEON("04", "인천"),
	GWANGJU("05", "광주"),
	DAEJEON("06", "대전"),
	ULSAN("07", "울산"),
	SEJONG("08", "세종"),
	GYEONGGI("09", "경기"),
	GANGWON("10", "강원"),
	CHUNGBUK("11", "충북"),
	CHUNGNAM("12", "충남"),
	JEONBUK("13", "전북"),
	JEONNAM("14", "전남"),
	GYEONBUK("15", "경북"),
	GYEONNAM("16", "경남"),
	JEJU("17", "제주");
	
	private final String areaCode;
	private final String areaName;
	Area(final String areaCode, final String areaName) {
		this.areaCode = areaCode;
		this.areaName = areaName;
	}
	
	public String getAreaCode() {
		return areaCode;
	}
	public String getAreaName() {
		return areaName;
	}
	
	public static Area findByAreaCode(final String areaCode) {
		return Arrays.stream(values())
				.filter(area -> area.getAreaCode().equals(areaCode))
				.findAny()
				.orElseThrow(UnsupportedOperationException::new);
	}

}
