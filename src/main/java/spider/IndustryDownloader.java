package spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class IndustryDownloader {
	File list;

	public IndustryDownloader(String list) {

		this.list = new File(list);
	}

	public void download() throws IOException {
		if (list.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(list));
			String line = br.readLine();
			
			while (line != null) {
				System.out.println(line);
				String[] tmp = line.split(",");
				if (tmp.length > 0) {
					String[] name = tmp[0].split("_");
					this.downloadPage(name[0], new URL(tmp[1]));
				}
				line = br.readLine();
			}
		}
	}

	private String downloadPage(String filename, URL pageUrl) {
		try {
		//	System.out.println("download>>>>" + filename + pageUrl.getPath());
			// pageUrl.
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					pageUrl.openStream(), "GB2312"));
			/*
			 * System.out.println(pageUrl.getAuthority());
			 * System.out.println(pageUrl.getFile());
			 * System.out.println(pageUrl.getPath());
			 */
			File dir = new File(list.getParent());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// System.out.println(this.path + "/" + filename);
			File pagefile = new File(list.getParent() + "/" + filename);
			// + pageUrl.getFile().replace("?", "/") + ".html");
			if (!pagefile.exists()) {
				pagefile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(pagefile, true);

			String line;
			StringBuffer pageBuffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				// System.out.println(new String(line.getBytes("GBK"),"UTF-8"));
				// System.out.println(line);
				pageBuffer.append(line);
				// fos.write((line + "\r\n").getBytes());
			}
			// System.out.println(pageBuffer.toString());
			String page = pageBuffer.toString();

			page = page.replaceAll("<td *+>", " ");
			page = page.replaceAll("<tr *+>", "\r\n");
			page = page.replaceAll("<br *+/*+>", "\r\n");

			page = page.replaceAll("<[ \"/a-z=]*+>", "");

			fos.write(page.getBytes("GBK"));
			fos.flush();
			fos.close();
			return pageBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws IOException {
		IndustryDownloader id1 = new IndustryDownloader("D:/new_zszq_cf/T0002/export/industry/gainian/list.csv");
	
		IndustryDownloader id2 = new IndustryDownloader("D:/new_zszq_cf/T0002/export/industry/area/list.csv");
		IndustryDownloader id3 = new IndustryDownloader("D:/new_zszq_cf/T0002/export/industry/hangye/list.csv");
		id3.download();

		id1.download();
		id2.download();

	}
}
