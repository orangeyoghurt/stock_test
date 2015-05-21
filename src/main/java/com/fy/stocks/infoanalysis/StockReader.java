package com.fy.stocks.infoanalysis;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;

import com.fy.stocks.entity.Stock;
import com.fy.stocks.entity.StockDayInfo;

public class StockReader {

	public static Stock stockload(File file) throws FileNotFoundException,
			ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String code = file.getName().replace(".TXT", "");
		Stock stock = new Stock();
		stock.setCode(code);
		stock.setName(code);
		String line;
		int linenum = 0;
		DateTime start;
		StockDayInfo pre = null;
		while (true) {

			try {
				line = br.readLine();
				if (line == null) {
					break;
				}
				linenum++;

				String[] tmp = line.split(",");
				if (tmp.length < 2) {
					continue;
				}
				StockDayInfo sdi = new StockDayInfo();

				DateTime dt = new DateTime(sdf.parse(tmp[0]));

				if (dt.dayOfWeek().get() == 5) {
					sdi.setDt(dt);
					sdi.setStart(Float.valueOf(tmp[1]));
					sdi.setHighest(Float.valueOf(tmp[2]));
					sdi.setLowest(Float.valueOf(tmp[3]));
					sdi.setEnd(Float.valueOf(tmp[4]));
					
					if (linenum == 1) {
						sdi.setPct((Float.valueOf(tmp[4]) / Float
								.valueOf(tmp[1])) * 100 - 100);
					} else {
						sdi.setPct((Float.valueOf(tmp[4]) / pre.getEnd()) * 100 - 100);
					}
					sdi.setExchange(Float.valueOf(tmp[5]));
					sdi.setMoney(Float.valueOf(tmp[6]));
					stock.pubStockDay(sdi);
					pre = sdi;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return stock;

	}
}
