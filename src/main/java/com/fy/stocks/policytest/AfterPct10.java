package com.fy.stocks.policytest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.fy.stocks.entity.ExchangeResult;
import com.fy.stocks.entity.Stock;
import com.fy.stocks.entity.StockDayInfo;

public class AfterPct10 {
	private List<ExchangeResult> results = new ArrayList<ExchangeResult>();
	private Stock stock;
	private File output;

	public AfterPct10(Stock stock) {
		this.stock = stock;
	}

	public void run() {

		List<StockDayInfo> dayinfos = stock.getStockdays();
		StockDayInfo lastday = dayinfos.get(0);
		for (int i = 1; i < dayinfos.size(); i++) {
			StockDayInfo today=dayinfos.get(i);
			buySignal(stock.getCode(),lastday,today);
			for(ExchangeResult result:results)
			{
				result.today(today);
			}
			lastday=today;
			
		}

	};

	public boolean buySignal(String code, StockDayInfo lastday,
			StockDayInfo today) {
		if (lastday.getPct() > 9.9) {
			if (today.getHighest() == today.getLowest())// 一字板
			{
				return false;
			}
			results.add(new ExchangeResult(code, today));
			return true;
		}
		return false;
	}

}
