package com.fy.stocks.infoanalysis;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fy.stocks.info.entity.ZsResearchDetail;

public class ZsResearchDetailReader {

	private String author_flag = "研究员";
	private String code_flag = "相关股票代码";
	private String expect_price_flag = "目标价";
	private String importance_falg = "重要性";
	private String industry_flag = "行业/子行业";
	private String reportname_flag = "报告名称";
	private String reporttime_flag = "报告日期";
	private String risk_flag = "风险提示";
	private String stock_flag = "公司名称及代码";
	private String suggestion_flag = "投资建议";

	public ZsResearchDetail read(File file) throws IOException {
		ZsResearchDetail detail = new ZsResearchDetail();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int i = 0;
		while (true) {
			String line = reader.readLine();
			if (line == null || line.length() == 0) {
				i++;
				if (i > 2) {
					break;
				}
				continue;
			}
			this.setAuthor(line, detail);
			this.setCode(line, detail);
			this.setExpect_price(line, detail);
			this.setImportance(line, detail);
			this.setIndustry(line, detail);
			this.setReportname(line, detail);
			try {
				this.setReporttime(line, detail);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.setRisk(line, detail);
			this.setStock(line, detail);
			this.setSuggestion(line, detail);
		}

		return detail;
	}

	private void setAuthor(String line, ZsResearchDetail detail) {
		if (line.indexOf(author_flag) > 0) {
			detail.setAuthor(line.replaceAll(author_flag, "")
					.replaceAll("：", "").trim());
		}
	}

	private void setCode(String line, ZsResearchDetail detail) {
		if (line.indexOf(code_flag) > 0) {
			detail.setCode(line.replaceAll(code_flag, "").replaceAll("：", "")
					.trim());
		}
	}

	private void setExpect_price(String line, ZsResearchDetail detail) {
		if (line.indexOf(expect_price_flag) > 0) {
			int index = line.indexOf(expect_price_flag);
			String subline = line.substring(index,
					Math.min(index + 10, line.length()));
			Pattern p2 = Pattern.compile("(\\d+.*\\d+)");
			Matcher m2 = p2.matcher(subline);
			while (m2.find()) {
				String expect_pirce = m2.group(1).trim();
				System.out.println(expect_pirce);
				if (expect_pirce.length() < 1) {
					continue;
				}
				detail.setExpect_price(expect_pirce);
				// System.out.println(link);
			}
			
		}
	}

	private void setImportance(String line, ZsResearchDetail detail) {
		if (line.indexOf(importance_falg) > 0) {
			detail.setImportance(line.replaceAll(importance_falg, "")
					.replaceAll("：", "").trim());
		}
	}

	private void setIndustry(String line, ZsResearchDetail detail) {
		if (line.indexOf(industry_flag) > 0) {
			detail.setIndustry(line.replaceAll(industry_flag, "")
					.replaceAll("：", "").trim());
		}
	}

	private void setReportname(String line, ZsResearchDetail detail) {
		if (line.indexOf(reportname_flag) > 0) {
			detail.setReportname(line.replaceAll(reportname_flag, "")
					.replaceAll("：", "").trim());
		}
	}

	private void setReporttime(String line, ZsResearchDetail detail)
			throws ParseException {
		if (line.indexOf(reporttime_flag) > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyymmdd");

			detail.setReporttime(sdf.parse(line.replaceAll(reporttime_flag, "")
					.replaceAll("：", "").trim()));
		}
	}

	private void setRisk(String line, ZsResearchDetail detail) {
		if (line.indexOf(risk_flag) > 0) {
			detail.setRisk(line.replaceAll(risk_flag, "").replaceAll("：", "")
					.trim());
		}
	}

	private void setStock(String line, ZsResearchDetail detail) {
		if (line.indexOf(stock_flag) > 0) {
			detail.setStock(line.replaceAll(stock_flag, "").replaceAll("：", "")
					.trim());
		}
	}

	private void setSuggestion(String line, ZsResearchDetail detail) {
		if (line.indexOf(suggestion_flag) > 0) {
			detail.setSuggestion(line.replaceAll(suggestion_flag, "")
					.replaceAll("：", "").trim());
		}
	}
	
	public  static void main(String[] args) throws IOException
	{
		ZsResearchDetailReader reader=new ZsResearchDetailReader();
		
		ZsResearchDetail detail=reader.read(new File("D:/new_zszq_cf/T0002/export/zsdownload/researchcontroller/detail/id=164907&code=2.html"));
		
		System.out.println(detail);
	}
}
