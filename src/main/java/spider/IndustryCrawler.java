package spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndustryCrawler {
	/*
	 * disallowListCache缓存robot不允许搜索的URL。 Robot协议在Web站点的根目录下设置一个robots.txt文件,
	 * 规定站点上的哪些页面是限制搜索的。 搜索程序应该在搜索过程中跳过这些区域,下面是robots.txt的一个例子: # robots.txt for
	 * http://somehost.com/ User-agent: * Disallow: /cgi-bin/ Disallow:
	 * /registration # /Disallow robots on registration page Disallow: /login
	 */
	private HashMap<String, ArrayList<String>> disallowListCache = new HashMap<String, ArrayList<String>>();
	ArrayList<String> errorList = new ArrayList<String>();// 错误信息
	ArrayList<String> result = new ArrayList<String>(); // 搜索到的结果
	String startUrl;// 开始搜索的起点
	int maxUrl;// 最大处理的url数
	String searchString;// 要搜索的字符串(英文)
	String path;// 页面文件保存地址
	boolean caseSensitive = false;// 是否区分大小写
	boolean limitHost = false;// 是否在限制的主机内搜索
	String pattern;
	FileOutputStream fos2;

	public IndustryCrawler(String startUrl, int maxUrl, String searchString,
			String path,String pattern) throws FileNotFoundException {
		// this.startUrl = startUrl;
		this.maxUrl = maxUrl;
		this.searchString = searchString;
		this.path = path;
		this.pattern=pattern;

	}

	public ArrayList<String> getResult() {
		return result;
	}

	public void run(String startUrl) throws IOException {// 启动搜索线程
		fos2 = new FileOutputStream(new File(path + "/list.csv"), true);
		crawl(startUrl, maxUrl, searchString, limitHost, caseSensitive);
		fos2.close();
	}

	// 检测URL格式
	private URL verifyUrl(String url) {
		// 只处理HTTP URLs.
		if (!url.toLowerCase().startsWith("http://"))
			return null;
		URL verifiedUrl = null;
		try {
			verifiedUrl = new URL(url);
		} catch (Exception e) {
			return null;
		}
		return verifiedUrl;
	}

	// 检测robot是否允许访问给出的URL.
	private boolean isRobotAllowed(URL urlToCheck) {
		String host = urlToCheck.getHost().toLowerCase();// 获取给出RUL的主机
		// System.out.println("主机="+host);

		// 获取主机不允许搜索的URL缓存
		ArrayList<String> disallowList = disallowListCache.get(host);

		// 如果还没有缓存,下载并缓存。
		if (disallowList == null) {
			disallowList = new ArrayList<String>();
			try {
				URL robotsFileUrl = new URL("http://" + host + "/robots.txt");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(robotsFileUrl.openStream()));

				// 读robot文件，创建不允许访问的路径列表。
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.indexOf("Disallow:") == 0) {// 是否包含"Disallow:"
						String disallowPath = line.substring("Disallow:"
								.length());// 获取不允许访问路径

						// 检查是否有注释。
						int commentIndex = disallowPath.indexOf("#");
						if (commentIndex != -1) {
							disallowPath = disallowPath.substring(0,
									commentIndex);// 去掉注释
						}

						disallowPath = disallowPath.trim();
						System.out.println("disallowPath " + disallowPath);
						disallowList.add(disallowPath);
					}
				}
				// 缓存此主机不允许访问的路径。
				disallowListCache.put(host, disallowList);
			} catch (Exception e) {
				return true; // web站点根目录下没有robots.txt文件,返回真
			}
		}
		String file = urlToCheck.getFile();
		for (int i = 0; i < disallowList.size(); i++) {
			String disallow = disallowList.get(i);
			if (file.startsWith(disallow)) {
				return false;
			}
		}
		return true;
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
			File dir = new File(this.path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// System.out.println(this.path + "/" + filename);
			File pagefile = new File(this.path + "/" + filename);
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

	// 解析页面并找出链接
	private ArrayList<String> retrieveLinks(URL pageUrl, String pageContents,
			HashSet crawledList, boolean limitHost) {
		// 用正则表达式编译链接的匹配模式。
		// Pattern p = Pattern.compile("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]",
		// Pattern.CASE_INSENSITIVE);
		Pattern p = Pattern
				.compile(
						//"\\s+href=[\"/stock/blockperformancedetail]([^\"]+)[\"|>]>([A-Z\\-\u4e00-\u9fa5]+)<",
						//"\\s+href=[\"//stock/industry]([^\"]+)[\"|>]>([A-Z\\-\u4e00-\u9fa5]+)<",
						pattern,
						Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(pageContents);

		ArrayList<String> linkList = new ArrayList<String>();
		while (m.find()) {
			String link = m.group(1).trim();
			String type = m.group(2).trim();
		//	 System.out.println("URLMATCH>>>>" + link + "," + type);
			if (link.length() < 1) {
				continue;
			}
			if (link.charAt(0) == '#') { // 跳过链到本页面内链接。
				continue;
			}
			if (link.indexOf("mailto:") != -1) {
				continue;
			}
			if (link.toLowerCase().indexOf("javascript") != -1) {
				continue;
			}
			if (link.indexOf("://") == -1) {
				if (link.charAt(0) == '/') {// 处理绝对地
					link = "http://" + pageUrl.getHost() + link;
				} else {
					String file = pageUrl.getFile();
					if (file.indexOf('/') == -1) {// 处理相对地址
						link = "http://" + pageUrl.getHost() + "/" + link;
					} else {
						String path = file.substring(0,
								file.lastIndexOf('/') + 1);
						link = "http://" + pageUrl.getHost() + path + link;
					}
				}
			}
			int index = link.indexOf('#');
			if (index != -1) {
				link = link.substring(0, index);
			}
			URL verifiedLink = verifyUrl(link);
			if (verifiedLink == null) {
				continue;
			}
			if (limitHost/* 如果限定主机，排除那些不合条件的URL */
					&& !pageUrl.getHost().toLowerCase()
							.equals(verifiedLink.getHost().toLowerCase())) {
				continue;
			}
			// 跳过那些已经处理的链接.
			if (crawledList.contains(link.replace("amp;", ""))) {
				continue;
			}
			// System.out.println(link);
			if (this.searchStringMatches(link, this.searchString, caseSensitive)) {
				System.out.println("Match>>>>>>>>>>>>>" + type + "," + link);
				linkList.add(type + "###" + link.replace("amp;", ""));
				try {
					for (int i = 1; i <= 15; i++) {
						if(link.indexOf("_1_1_1")>0||link.indexOf("_2_1_1")>0)
						{fos2.write((type
								+ i
								+ ","
								+ link.replace("amp;", "")
										.replace("_1_1_1", "_1_1_" + i)
										.replace("_2_1_1", "_2_1_" + i) + "\r\n")
								.getBytes("GBK"));}
						else
						{
							fos2.write((type+i+","+link.replace("amp;", "").replace(".shtml", "_2_1_"+i+".html")+"\r\n").getBytes("GBK"));
						}
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return (linkList);
	}

	// 搜索下载Web页面的内容，判断在该页面内有没有指定的搜索字符串

	private boolean searchStringMatches(String pageContents,
			String searchString, boolean caseSensitive) {
		String searchContents = pageContents;
		if (!caseSensitive) {// 如果不区分大小写
			searchContents = pageContents.toLowerCase();
		}

		Pattern p = Pattern.compile("[\\s]+");
		String[] terms = p.split(searchString);
		for (int i = 0; i < terms.length; i++) {
			/*
			 * if (caseSensitive) { if (searchContents.indexOf(terms[i]) == -1)
			 * { return false; } } else { if
			 * (searchContents.indexOf(terms[i].toLowerCase()) == -1) { return
			 * false; } }
			 */
			if (caseSensitive) {
				if (searchContents.indexOf(terms[i]) >= 0) {
					return true;
				}
			} else {
				if (searchContents.indexOf(terms[i].toLowerCase()) >= 0) {
					return true;
				}
			}
		}

		return false;
	}

	// 执行实际的搜索操作
	public ArrayList<String> crawl(String startUrl, int maxUrls,
			String searchString, boolean limithost, boolean caseSensitive) {

		System.out.println("searchString=" + searchString);
		HashSet<String> crawledList = new HashSet<String>();
		LinkedHashSet<String> toCrawlList = new LinkedHashSet<String>();

		if (maxUrls < 1) {
			errorList.add("InvalidMax URLs value.");
			System.out.println("Invalid Max URLs value.");
		}

		if (searchString.length() < 1) {
			errorList.add("Missing SearchString.");
			System.out.println("Missing searchString");
		}

		if (errorList.size() > 0) {
			System.out.println("err!!!");
			return errorList;
		}

		// 从开始URL中移出www
		// startUrl = removeWwwFromUrl(startUrl);
		toCrawlList.add(startUrl);
		while (toCrawlList.size() > 0) {

			if (maxUrls != -1) {
				if (crawledList.size() == maxUrls) {
					break;
				}
			}
			// Get URL at bottom of the list.
			String url = toCrawlList.iterator().next();
			// System.out.println(url);
			String[] tmp = url.split("###");
			if (tmp.length > 1) {
				url = tmp[1];
				toCrawlList.remove(tmp[0] + "###" + tmp[1]);
			} else {
				tmp[0] = "other";
				toCrawlList.remove(url);
			}
			// Convert string url to URL object.te
			crawledList.add(url);
			System.out.println("contains " + url + ","
					+ crawledList.contains(url));
			// Remove URL from the to crawl list.

			URL verifiedUrl = verifyUrl(url);
			// Skip URL if robots are not allowed toaccess it.
			// if (!isRobotAllowed(verifiedUrl)) {
			// continue;
			// }
			// 增加已处理的URL到crawledList

			String pageContents = downloadPage(tmp[0], verifiedUrl);
			if (pageContents != null && pageContents.length() > 0) {
				// 从页面中获取有效的链接
				ArrayList<String> links = retrieveLinks(verifiedUrl,
						pageContents, crawledList, limitHost);
				toCrawlList.addAll(links);
				if (searchStringMatches(pageContents, searchString,
						caseSensitive)) {
					result.add(url);
					// System.out.println(url);
				}
			}

		}
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int max = 100;

		IndustryCrawler crawler;
		try {
			crawler = new IndustryCrawler(
					"http://quote.stockstar.com/stock/industry.shtml",
					max, "stock/industry",
					"D:/new_zszq_cf/T0002/export/industry/hangye",
					"\\s+href=[\"//stock/industry]([^\"]+)[\"|>]>([A-Z\\-\u4e00-\u9fa5]+)<");
			crawler.run("http://quote.stockstar.com/stock/industryrank_1_1_1_1.html");
			crawler.run("http://quote.stockstar.com/stock/industryrank_1_1_1_2.html");
			crawler.run("http://quote.stockstar.com/stock/industryrank_1_1_1_3.html");
			
			crawler = new IndustryCrawler(
					"http://quote.stockstar.com/stock/blockrank_5.shtml",
					max, "stock/blockperformance",
					"D:/new_zszq_cf/T0002/export/industry/gainian",
					"\\s+href=[\"/stock/blockperformance]([^\"]+)[\"|>]>([A-Z\\-\u4e00-\u9fa5]+)<");
			
		
			 crawler.run("http://quote.stockstar.com/stock/blockrank_5_1_1_1.html");
			 crawler.run("http://quote.stockstar.com/stock/blockrank_5_1_1_2.html");
			 crawler.run("http://quote.stockstar.com/stock/blockrank_5_1_1_3.html");
				crawler = new IndustryCrawler(
						"http://quote.stockstar.com/stock/blockrank_3.shtml",
						max, "stock/blockperformance",
						"D:/new_zszq_cf/T0002/export/industry/area",
						"\\s+href=[\"/stock/blockperformance]([^\"]+)[\"|>]>([A-Z\\-\u4e00-\u9fa5]+)<");
				
			 crawler.run("http://quote.stockstar.com/stock/blockrank_3_1_1_1.html");
			 crawler.run("http://quote.stockstar.com/stock/blockrank_3_1_1_2.html");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
