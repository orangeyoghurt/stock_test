package com.fy.stocks.entity;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class StockDayInfo {
	private float start;
	private float end;
	private float highest;
	private float lowest;
	private float money;
	private float exchange;
	private float pct;
	private DateTime dt;
	private List<String> news=new ArrayList<String>();
	public float getStart() {
		return start;
	}
	public void setStart(float start) {
		this.start = start;
	}
	public float getEnd() {
		return end;
	}
	public void setEnd(float end) {
		this.end = end;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	public float getPct() {
		return pct;
	}
	public void setPct(float pct) {
		this.pct = pct;
	}
	public DateTime getDt() {
		return dt;
	}
	public void setDt(DateTime dt) {
		this.dt = dt;
	}
	public List<String> getNews() {
		return news;
	}
	public void setNews(List<String> news) {
		this.news = news;
	}
	public float getExchange() {
		return exchange;
	}
	public void setExchange(float exchange) {
		this.exchange = exchange;
	}
	public float getLowest() {
		return lowest;
	}
	public void setLowest(float lowest) {
		this.lowest = lowest;
	}
	public float getHighest() {
		return highest;
	}
	public void setHighest(float highest) {
		this.highest = highest;
	}
	
}
