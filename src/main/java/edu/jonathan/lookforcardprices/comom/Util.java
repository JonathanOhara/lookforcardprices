package edu.jonathan.lookforcardprices.comom;

import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Util {
	
	private static String projectPath = null;
	private static String reportsPath = null;

	public static final String HTML_IMPORT_FOLDER = "imports";
	public static final String LOGS_FOLDER = "logs";

	protected static final Logger logger = Logger.getLogger(Util.class);

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

			File reportImportFile = new File(reportFile.getCanonicalPath(), HTML_IMPORT_FOLDER);
			if( !reportImportFile.exists() ){
				reportImportFile.mkdir();
			}

			File logFile = new File(reportFile, LOGS_FOLDER);
			if( !logFile.exists() ){
				logFile.mkdir();
			}

			reportsPath = reportFile.getCanonicalPath();
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public static void configureOutputToFileAndConsole(Logger logger, String fileName) {
		FileAppender appender = null;
		PatternLayout patternLayout = new PatternLayout();
		patternLayout.setConversionPattern("[%d{HH:mm:ss,SSS}]: %m%n");

		try {
			appender = new FileAppender(patternLayout,"./log/" + fileName.replaceAll(",","") + ".log",false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.addAppender(appender);
	}

	public static void configureOutputToFileAndConsole(String logName) {
		try {
			String logAddress = Util.getReportsPath() + "/" + LOGS_FOLDER +  "/" + logName.replaceAll(",","") + ".log";
			PrintStream fileStream = null;

			fileStream = new MyPrintStream(new FileOutputStream( logAddress, true ), System.out);

			System.setOut(fileStream);
			System.setErr(fileStream);
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public static List<String> read(File file) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(file));
		String line = "";
		List<String> stringList = new ArrayList<>();
		
		while (true) {
			if (line == null) break;
			line = buffRead.readLine();
			if (line != null && !line.trim().isEmpty()) stringList.add(line);
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

	public static String prepareUrlMode1(String searchPattern, String productName) {
		return searchPattern.replace(SearchService.URL_SEARCH_SAMPLE, stringToUrl( productName ) );
	}

	public static String prepareUrlMode2(String searchPattern, String productName) {
		return searchPattern.replace(SearchService.URL_SEARCH_SAMPLE, productName.replaceAll(" ", "%20") );
	}
	
	public static String completeURL(String baseUrl, String individualUrl){
		String absoluteUrl = individualUrl;

		if(individualUrl.isEmpty()){
			return "";
		}

		if(individualUrl.startsWith("http")){
			return individualUrl;
		}

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
    		   logger.debug("Directory copied from " + src + "  to " + dest);
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
    	    logger.debug("File copied from " + src + " to " + dest);
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
