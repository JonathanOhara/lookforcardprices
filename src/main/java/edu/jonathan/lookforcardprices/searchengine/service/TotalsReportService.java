package edu.jonathan.lookforcardprices.searchengine.service;

import edu.jonathan.lookforcardprices.comom.Keys;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;


public class TotalsReportService {

	private StringBuilder totalsPerProductContent = null;
	private StringBuilder totalsPerShopContent = null;

	public TotalsReportService() {}

	public void generateHeaders(){
		totalsPerProductContent.append("\"Shop\"").append(Keys.CSV_SEPARATOR).
				append("\"Name Searched\"").append(Keys.CSV_SEPARATOR).
				append("\"Name Found\"").append(Keys.CSV_SEPARATOR).
				append("\"Price\"").append(Keys.CSV_SEPARATOR).
				append("\"Price Float\"").append(Keys.CSV_SEPARATOR).
				append("\"URL\"\n");
		
		totalsPerShopContent.append("\"Shop\"").append(Keys.CSV_SEPARATOR).
					  append("\"Quantity\"\n");
	}
	
	public void generateContent(Map<Shop, List<Product>> productsByShop, String nameToSearch){
		NumberFormat z = NumberFormat.getCurrencyInstance();
		for(Entry<Shop, List<Product>> entry: productsByShop.entrySet()){
			Shop shop = entry.getKey();

			if( entry.getValue() != null ){
				for( Product product : entry.getValue() ){
					totalsPerProductContent.append( "\"" ).append( shop.getName() ).append( "\"" ).append(Keys.CSV_SEPARATOR).
							append( "\"" ).append( nameToSearch ).append( "\"" ).append(Keys.CSV_SEPARATOR).
							append( "\"" ).append( product.getName() ).append( "\"" ).append(Keys.CSV_SEPARATOR).
							append( "\"" ).append( product.getFormattedPrice() ).append( "\"" ).append(Keys.CSV_SEPARATOR).
							append( "\"" ).append( z.format( product.getFloatValue() ) ).append( "\"" ).append(Keys.CSV_SEPARATOR).
							append( "\"" ).append( product.getUrl() ).append( "\"\n" );
				}
			}else{
				totalsPerProductContent.append( "\"" ).append( shop.getName() ).append( "\"" ).append(Keys.CSV_SEPARATOR).
					append( "\"" ).append( nameToSearch ).append( "\"" ).append(Keys.CSV_SEPARATOR).
					append( "\"" ).append( "-" ).append( "\"" ).append(Keys.CSV_SEPARATOR).
					append( "\"" ).append( "-" ).append( "\"" ).append(Keys.CSV_SEPARATOR).
					append( "\"" ).append( z.format( 9999.99f ) ).append( "\"" ).append(Keys.CSV_SEPARATOR).
					append( "\"" ).append( shop.getMainUrl() ).append( "\"\n" );
			}
		}
	}
	
	private void computeShopsTotals(Map<Shop, List<Product>> productsByShop) {
		for( Entry<Shop, List<Product>> entry : productsByShop.entrySet() ){
			totalsPerShopContent.append( "\"" ).append( entry.getKey().getName() ).append( "\"" ).append(Keys.CSV_SEPARATOR).
						  append( "\"" ).append( entry.getValue().size() ).append( "\"\n" );
		}
		
	}
	
	public void closeAndWriteFile() throws IOException{
		
		File reportsDir = new File( Util.getReportsPath() ); 
		if (!reportsDir.exists()) {
			reportsDir.mkdir();
		}
		
		File dir = new File(reportsDir.getAbsolutePath() + "/Totals"); 
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		generateGamesTotalFile(dir);
		generateShopsTotalFile(dir);
	}

	private void generateGamesTotalFile(File dir) throws IOException {
		String csvDir;
		csvDir = dir.getAbsoluteFile() + "/Games_Total.csv";
		
		File file = new File( csvDir );
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(totalsPerProductContent.toString());
		
		bw.flush();
		bw.close();
	}
	
	private void generateShopsTotalFile(File dir) throws IOException {
		String csvDir;
		csvDir = dir.getAbsoluteFile() + "/Shops_Total.csv";
		
		File file = new File( csvDir );
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(totalsPerShopContent.toString());
		
		bw.flush();
		bw.close();
	}

	public void generateReportProductsByShop(String productName, List<Product> results) throws IOException, URISyntaxException {
		System.out.println("Generating Report for: "+productName);
		Util.configureOutputToFileAndConsole(productName);

		totalsPerProductContent = new StringBuilder();
		totalsPerShopContent = new StringBuilder();

		generateHeaders();

		Map<Shop, List<Product>> productsByShop = new TreeMap<>(Comparator.comparing(Shop::getName));

		for(Product product : results){
			List<Product> products = Optional.ofNullable(productsByShop.get(product.getShopFounded())).orElse(new ArrayList<>());
			products.add(product);
			productsByShop.put( product.getShopFounded(), products );
		}

		GamesReportService htmlReport = new GamesReportService(productName);

		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

		DateFormat df = new SimpleDateFormat("HH:mm");
		Date myDate = new Date(System.currentTimeMillis());
		String hora = df.format(myDate);

		Shop shop;
		List<Product> products;
		for( Map.Entry<Shop, List<Product>> entry: productsByShop.entrySet() ){
			shop = entry.getKey();
			products = entry.getValue();

			htmlReport.addReport( shop, products );
		}

		htmlReport.addOtherSeekers(productName);

		htmlReport.addMetaData(productName, 0, data, hora); // TODO Review this

		htmlReport.addLogTab( productName );

		htmlReport.closeAndWriteFile(productName);

		generateContent(productsByShop, productName);
		computeShopsTotals(productsByShop);

		closeAndWriteFile();
	}

}
