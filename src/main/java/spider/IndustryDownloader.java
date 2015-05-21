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
	
	public void download() throws IOException
	{
		if(list.exists())
		{
			BufferedReader br=new BufferedReader(new FileReader(list));
			String line=br.readLine();
			while(line!=null)
			{
				String[] tmp=line.split(",");
				if(tmp.length>0)
				{
					this.downloadPage(tmp[0], new URL(tmp[1]));
				}
			}
		}
	}
	private String downloadPage(String filename, URL pageUrl) {
		try {
			System.out.println("download>>>>" + filename + pageUrl.getPath());
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
			File pagefile = new File(list.getParent()+ "/" + filename);
			// + pageUrl.getFile().replace("?", "/") + ".html");
			if (!pagefile.exists()) {
				pagefile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(pagefile);

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
}
