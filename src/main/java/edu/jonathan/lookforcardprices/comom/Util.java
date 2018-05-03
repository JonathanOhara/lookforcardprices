package edu.jonathan.lookforcardprices.comom;

import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Util {
	
	private static String projectPath = null;
	private static String reportsPath = null;
	
	static{
		try {
			String data, hora;
			data = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			
			DateFormat df = new SimpleDateFormat("HH");
			Date myDate = new Date(System.currentTimeMillis());
			hora = df.format(myDate);
			
			projectPath = new File(".").getCanonicalPath();
			
			File reportFile = new File("./reports/" + data + " " + hora );
			if( !reportFile.exists() ){
				reportFile.mkdir();
			}
			
			reportsPath = reportFile.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void configureOutputToFileAndConsole(String logName) throws FileNotFoundException, IOException {
		String logAddress = Util.getReportsPath() + "/" + logName + ".log";
		PrintStream fileStream = new MyPrintStream(new FileOutputStream( logAddress, true ), System.out);

		System.setOut(fileStream);
		System.setErr(fileStream);
	}

	public static List<String> read(File file) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(file));
		String line = "";
		List<String> stringList = new ArrayList<String>();
		
		while (true) {
			if (line == null) break;
			line = buffRead.readLine();
			if (line != null) stringList.add(line);
		}
		
		buffRead.close();
		
		return stringList;
	}
	
	public static String getProjectPath() throws IOException{
		return projectPath;
	}
	
	public static String getReportsPath() throws IOException{
		return reportsPath;
	}
	

	public static String stringToUrl(String urlStr){
		return URLEncoder.encode(urlStr);
	}
	
	public static Document readUrlDocument(String url) throws IOException{
		System.out.println(url);
		Document doc = null;
		try{
			doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
					.referrer("http://www.google.com")
					.timeout(20000)
					.get();
		}catch(Exception e){
			System.out.println(e.getCause() + "Try to connect by another way..");
			doc = parseDocument( readUrl(url, null) );
		}
		return doc;
	}
	
	public static String readUrl(String url, String charset){
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader in = getPageBuffer(url, charset);

			if( in != null){
				String inputLine;
		        while ((inputLine = in.readLine()) != null){
		            builder.append(inputLine);
		        }
		        in.close();
			}
		}catch (MalformedURLException e) { 
			e.printStackTrace();
		}catch (IOException e) {   
			e.printStackTrace();
		}
		
		if( builder.toString().contains("") ){
    		System.out.println("Possivelmente Ocorreu Erro ao read a pagina");
    	}
		
		return builder.toString();
	}
	
	private static BufferedReader getPageBuffer(String url, String charset)throws IOException {
		URL oracle = null;
		URLConnection yc = null;
		BufferedReader in = null;
		
		for( int i = 0; i < 10; i++ ){
			try{
				oracle = new URL( url );
				yc = oracle.openConnection();
				if( charset == null || charset.isEmpty() ){
					in = new BufferedReader(new InputStreamReader( yc.getInputStream() ));
				}else{
					in = new BufferedReader(new InputStreamReader( yc.getInputStream(), charset ));
				}
				
				break;
			}catch( ConnectException e){
				System.out.println("ConnectException - Tentando novamente["+i+"]: "+url);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
			}catch( UnknownHostException e){
				System.out.println("UnknownHostException - Tentando novamente["+i+"]: "+url);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
			}
			
		}
		
		return in;
	}
	
	public static Document parseDocument(String html){
		Document document = null;
		try{
			document = Jsoup.parse(html);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	public static String prepareUrlMode1(String searchPattern, String productName) {
		return searchPattern.replace(SearchService.URL_SEARCH_SAMPLE, stringToUrl( productName ) );
	}

	public static String prepareUrlMode2(String searchPattern, String productName) {
		return searchPattern.replace(SearchService.URL_SEARCH_SAMPLE, productName.replaceAll(" ", "%20") );
	}
	
	public static String completeURL(String baseUrl, String individualUrl){
		String absoluteUrl = individualUrl;
		
		if( !individualUrl.contains( baseUrl ) ){
			if(individualUrl.charAt(0) == '/'){
				absoluteUrl = baseUrl + individualUrl.substring(1);	
			}else{
				absoluteUrl = baseUrl + individualUrl;
			}
		}
		
		return absoluteUrl;
	}
	
	public static void copyFolder(File src, File dest) throws IOException{
	 
    	if(src.isDirectory()){
 
    		//if directory not exists, create it
    		if(!dest.exists()){
    		   dest.mkdir();
    		   System.out.println("Directory copied from " 
                              + src + "  to " + dest);
    		}
 
    		//list all the directory contents
    		String files[] = src.list();
 
    		for (String file : files) {
    		   //construct the src and dest file structure
    		   File srcFile = new File(src, file);
    		   File destFile = new File(dest, file);
    		   //recursive copy
    		   copyFolder(srcFile,destFile);
    		}
 
    	}else{
    		//if file, then copy it
    		//Use bytes stream to support all file types
    		InputStream in = new FileInputStream(src);
    		OutputStream out = new FileOutputStream(dest); 
 
    	    byte[] buffer = new byte[1024];
 
    	    int length;
    	    //copy the file content in bytes 
    	    while ((length = in.read(buffer)) > 0){
    	    	out.write(buffer, 0, length);
    	    }
 
    	    in.close();
    	    out.close();
    	    System.out.println("File copied from " + src + " to " + dest);
    	}
    }

	public static String replaceAllAccent(String replacement) {
		String replacedString = replacement;
		if (replacedString == null) {
			return null;
		}
		replacedString = replacedString.toLowerCase();
		replacedString = replacedString.replaceAll("[�����]", "a");
		replacedString = replacedString.replaceAll("[�����]".toUpperCase(), "a".toUpperCase());
		replacedString = replacedString.replaceAll("[����]", "e");
		replacedString = replacedString.replaceAll("[����]".toUpperCase(), "e".toUpperCase());
		replacedString = replacedString.replaceAll("[����]", "i");
		replacedString = replacedString.replaceAll("[����]".toUpperCase(), "i".toUpperCase());
		replacedString = replacedString.replaceAll("[������]", "o");
		replacedString = replacedString.replaceAll("[�����]".toUpperCase(), "o".toUpperCase());
		replacedString = replacedString.replaceAll("[����]", "u");
		replacedString = replacedString.replaceAll("[����]".toUpperCase(), "u".toUpperCase());
		replacedString = replacedString.replaceAll("[�]", "c");
		replacedString = replacedString.replaceAll("[�]".toUpperCase(), "c".toUpperCase());
		replacedString = replacedString.replaceAll("[�]", "n");
		replacedString = replacedString.replaceAll("[�]".toUpperCase(), "n".toUpperCase());
		replacedString = replacedString.replaceAll("[&]", "");

		return replacedString;
	}
}
