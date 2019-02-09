package com.poseidon.dolphin.simulator.company;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

import com.poseidon.dolphin.api.fss.company.Area;
import com.poseidon.dolphin.api.fss.company.converter.AreaConverter;

@Embeddable
public class FinanceCompanyOption {
	@Convert(converter=AreaConverter.class)
	private Area area;
	private boolean exist;
	
	protected FinanceCompanyOption() {}
	
	public FinanceCompanyOption(Area area, boolean exist) {
		super();
		this.area = area;
		this.exist = exist;
	}
	
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public boolean isExist() {
		return exist;
	}
	public void setExist(boolean exist) {
		this.exist = exist;
	}
	
	@Override
	public String toString() {
		return "FinanceCompanyOption [area=" + area + ", exist=" + exist + "]";
	}
}
