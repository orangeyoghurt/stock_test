package com.fy.stocks.entity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Correl {

	private Stock standard;
	private Stock match;
	

	private double[] correls = new double[60];

	public Stock getStandard() {
		return standard;
	}

	public void setStandard(Stock standard) {
		this.standard = standard;
	}

	public Stock getMatch() {
		return match;
	}

	public void setMatch(Stock match) {
		this.match = match;
	};

	public void calculateCorrel1() {
		Sigma[] tmps=new Sigma[60];
		
		List<StockDayInfo> standards=standard.getStockdays();
		List<StockDayInfo> matchs=match.getStockdays();
		
		for(int i=5;i<60;i++)
		{
			Sigma sigma=new Sigma();
			tmps[i]=sigma;
			for(int j=0;j<standards.size()&&j+i<matchs.size();j++)
			{
				try{
				sigma.put(standards.get(j).getPct(), matchs.get(i+j).getPct());
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			correls[i]=sigma.correl();
		}
	}
	public void output(String path) throws IOException
	{
		File result=new File(path+standard.getCode()+"_"+match.getCode()+".csv");
	//	System.out.println(result.getPath()+" "+result.getName());
		
		FileOutputStream fos=new FileOutputStream(result);
		for(int i=0;i<correls.length;i++)
		{
			if(Math.abs(correls[i])<0.2)
			{
				continue;
			}
			if(!result.exists())
			{
				result.createNewFile();
				//fos
			}
			StockCorrel sc=new StockCorrel();
			sc.setName1(this.standard.getName());
			sc.setCode1(this.standard.getCode());
			sc.setName2(this.match.getName());
			sc.setCode2(this.match.getCode());
			sc.setOffset(i);
			sc.setCorrel(correls[i]);
			StringBuffer bf=new StringBuffer();
			bf.append(sc.getName1());
			bf.append(",");
			bf.append(sc.getCode1());
			bf.append(",");
			bf.append(sc.getName2());
			bf.append(",");
			bf.append(sc.getCode2());
			bf.append(",");
			bf.append(sc.getOffset());
			bf.append(",");
			bf.append(sc.getCorrel());
			bf.append("\r\n");
			fos.write((bf.toString()).getBytes());
			
		}
		
		fos.close();
		
		
	}
	
	
	class Sigma {
		private double sigmaxy = 0;
		private double sigmax = 0;
		private double sigmay = 0;
		private double sigmax2 = 0;
		private double sigmay2 = 0;
		private int n = 0;

		public void put(float x, float y) {
			sigmaxy =  new BigDecimal(sigmaxy + x * y).setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();
			sigmax = new BigDecimal(sigmax + x).setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();
			sigmay = new BigDecimal(sigmay + y).setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();
			sigmax2 = new BigDecimal(sigmax2 + x * x).setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();
			sigmay2 = new BigDecimal(sigmay2 + y * y).setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();
			n = n + 1;
		}//-8005.4530179683233205898416166029 7.7564

		public double correl() {
			double r=(sigmaxy - sigmax * sigmay / n)
					/ Math.sqrt((sigmax2 - sigmax * sigmax / n)
							* (sigmay2 - sigmay * sigmay / n));
		//	System.out.println(this.toString()+"r="+r);
			
			return r;
		}

		@Override
		public String toString() {
			return "Sigma [sigmaxy=" + sigmaxy + ", sigmax=" + sigmax
					+ ", sigmay=" + sigmay + ", sigmax2=" + sigmax2
					+ ", sigmay2=" + sigmay2 + ", n=" + n + "]";
		}

	}

}
