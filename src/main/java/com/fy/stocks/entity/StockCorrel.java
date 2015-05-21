package com.fy.stocks.entity;

public class StockCorrel {

	private String name1;
	private String code1;
	private String name2;
	private String code2;
	private int offset;
	private double correl;
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getCode1() {
		return code1;
	}
	public void setCode1(String code1) {
		this.code1 = code1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public String getCode2() {
		return code2;
	}
	public void setCode2(String code2) {
		this.code2 = code2;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public double getCorrel() {
		return correl;
	}
	public void setCorrel(double correls) {
		this.correl = correls;
	}
	
}
