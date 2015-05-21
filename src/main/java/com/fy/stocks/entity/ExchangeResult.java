package com.fy.stocks.entity;

import java.util.Date;

import org.joda.time.DateTime;

public class ExchangeResult {

	private DateTime in;
	private float price_in = 0;
	private String code;
	private float price_out_3d = 0;
	private float price_out_10d = 0;
	private float price_out_30d = 0;
	private float price_out_90d = 0;
	private float price_out_first_down = 0;

	private boolean downflag = false;
	private int i = 0;

	public ExchangeResult(String code, StockDayInfo today) {
		this.in = today.getDt();
		this.setCode(code);
		this.price_in = today.getHighest();
	}

	public void today(StockDayInfo today) {
		i++;
		if (i > 90) {
			return;
		}
		if (!downflag) {
			if (today.getPct() < 0) {
				price_out_first_down = today.getLowest();
			}
		}
		if (i == 3) {
			price_out_3d = today.getLowest();
		}
		if (i == 10) {
			price_out_10d = today.getLowest();
		}
		if (i == 30) {
			price_out_30d = today.getLowest();
		}
		if (i == 90) {
			price_out_90d = today.getLowest();
		}
	}

	public DateTime getIn() {
		return in;
	}

	public void setIn(DateTime in) {
		this.in = in;
	}

	public float getPrice_in() {
		return price_in;
	}

	public void setPrice_in(float price_in) {
		this.price_in = price_in;
	}

	public float getPrice_out_3d() {
		return price_out_3d;
	}

	public void setPrice_out_3d(float price_out_3d) {
		this.price_out_3d = price_out_3d;
	}

	public float getPrice_out_10d() {
		return price_out_10d;
	}

	public void setPrice_out_10d(float price_out_10d) {
		this.price_out_10d = price_out_10d;
	}

	public float getPrice_out_30d() {
		return price_out_30d;
	}

	public void setPrice_out_30d(float price_out_30d) {
		this.price_out_30d = price_out_30d;
	}

	public float getPrice_out_90d() {
		return price_out_90d;
	}

	public void setPrice_out_90d(float price_out_90d) {
		this.price_out_90d = price_out_90d;
	}

	public float getPrice_out_first_down() {
		return price_out_first_down;
	}

	public void setPrice_out_first_down(float price_out_first_down) {
		this.price_out_first_down = price_out_first_down;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
