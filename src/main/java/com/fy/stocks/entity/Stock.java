package com.fy.stocks.entity;

import java.util.ArrayList;
import java.util.List;

public class Stock {
	private String name;
	private String code;
	private List<StockDayInfo> stockdays=new ArrayList<StockDayInfo>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<StockDayInfo> getStockdays() {
		return stockdays;
	}
	public void setStockdays(List<StockDayInfo> stockdays) {
		this.stockdays = stockdays;
	}
	public void pubStockDay(StockDayInfo dayinfo)
	{
		stockdays.add(dayinfo);
	}
	
	
}
