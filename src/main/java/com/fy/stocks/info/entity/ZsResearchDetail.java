package com.fy.stocks.info.entity;

import java.util.Date;

public class ZsResearchDetail {

	@Override
	public String toString() {
		return "ZsResearchDetail [author=" + author + ", reporttime="
				+ reporttime + ", importance=" + importance + ", industry="
				+ industry + ", stock=" + stock + ", code=" + code
				+ ", suggestion=" + suggestion + ", risk=" + risk
				+ ", reportname=" + reportname + ", filename=" + filename
				+ ", expect_price=" + expect_price + "]";
	}

	private String author;
	private Date reporttime=new Date();
	private String importance;
	private String industry;
	private String stock;
	private String code;
	private String suggestion;
	private String risk;
	private String reportname;
	private String filename;
	private String expect_price;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getReporttime() {
		return reporttime;
	}

	public void setReporttime(Date reporttime) {
		this.reporttime = reporttime;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public String getReportname() {
		return reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getExpect_price() {
		return expect_price;
	}

	public void setExpect_price(String expect_price) {
		if (this.expect_price != null) {
			this.expect_price = this.expect_price + "," + expect_price;
		} else {
			this.expect_price = expect_price;
		}
	}

}
