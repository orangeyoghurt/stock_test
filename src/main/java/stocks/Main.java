package stocks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import com.fy.stocks.entity.Correl;
import com.fy.stocks.entity.Stock;
import com.fy.stocks.infoanalysis.StockReader;

public class Main {

	public static void main(String[] args) throws ParseException, IOException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		Main m = new Main();
		m.calculateCorrels(new File("D:/new_zszq_cf/T0002/export/hangye"));
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}

	public void calculateCorrels(File file) throws ParseException, IOException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
			
				//
				if (f.isDirectory()) {
					calculateCorrels(f);
					continue;
				} else {
					if (f.getName().indexOf("TXT") > 0) {
						System.out.println(f.getPath());
						for (File f2 : files) {
							if (f2.isFile()) {
								if (f2.getName().indexOf("TXT") > 0) {
									r(f, f2);
								}
							}

						}
					}
				}

			}
		}

	}

	public void r(File f1, File f2) throws ParseException, IOException {
		Stock standard = StockReader.stockload(f1);
		Stock match = StockReader.stockload(f2);
		Correl correl = new Correl();
		correl.setMatch(match);
		correl.setStandard(standard);
		correl.calculateCorrel1();
		String path = f1.getParent() + "/results/";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		correl.output(path);
	}

}
