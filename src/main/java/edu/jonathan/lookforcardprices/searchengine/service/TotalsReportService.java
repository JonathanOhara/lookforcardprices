package edu.jonathan.lookforcardprices.searchengine.service;

import edu.jonathan.lookforcardprices.comom.CSVFileBuilder;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;


public class TotalsReportService {

	private CSVFileBuilder totalsPerProductContent = null;
	private CSVFileBuilder totalsPerShopContent = null;

	protected static final Logger logger = Logger.getLogger(TotalsReportService.class);

	public TotalsReportService() {
		totalsPerProductContent = CSVFileBuilder.createCSVFile();
		totalsPerShopContent = CSVFileBuilder.createCSVFile();
	}

	public void generateHeaders(){
		totalsPerProductContent.createLine().
				append("Shop").
				append("Name Searched").
				append("Name Found").
				append("Formatted Price").
				append("Price Converted to BRL").
				append("URL").
				buildLine();
		
		totalsPerShopContent.createLine().
				append("Shop").
				append("Quantity").
				buildLine();
	}
	
	public void generateTotalsPerProductContent(Map<Shop, List<Product>> productsByShop, String nameToSearch){
		for(Entry<Shop, List<Product>> entry: productsByShop.entrySet()){
			Shop shop = entry.getKey();

			if( entry.getValue() != null ){
				for( Product product : entry.getValue() ){
					if(!product.isAvailable()) continue;
					totalsPerProductContent.createLine().
							append( shop.getName() ).
							append( nameToSearch ).
							append( product.getName() ).
							append( product.getProductPrice().map( productPrice -> productPrice.getFormattedPrice() ).orElse(SearchService.PRODUCT_PRICE_NOT_AVAILABLE) ).
							append( product.getPriceInRealFormatted() ).
							append( product.getUrl() ).
							buildLine();
				}
			}else{
				totalsPerProductContent.createLine().
						append( shop.getName() ).
						append( nameToSearch ).
						append( "-" ).
						append( "-" ).
						append( 9999.99f ).
						append( shop.getMainUrl() ).
						buildLine();
			}
		}
	}
	
	private void generateTotalsPerShopContent(Map<Shop, List<Product>> productsByShop) {
		for( Entry<Shop, List<Product>> entry : productsByShop.entrySet() ){
			totalsPerShopContent.createLine().
					append( entry.getKey().getName() ).
					append( entry.getValue().size() ).
					buildLine();
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
		csvDir = dir.getAbsoluteFile() + "/Products_Total.csv";
		
		File file = new File( csvDir );
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(totalsPerProductContent.build());
		
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
		
		bw.write(totalsPerShopContent.build());
		
		bw.flush();
		bw.close();
	}

	public void generateReportProductsByShop(String productName, List<Product> results) throws IOException, URISyntaxException {
		logger.trace("Generating Report for: "+productName);

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

		htmlReport.closeAndWriteFile(productName);

		generateTotalsPerProductContent(productsByShop, productName);
		generateTotalsPerShopContent(productsByShop);

		closeAndWriteFile();
	}

	public CSVFileBuilder getTotalsPerProductContent() {
		return totalsPerProductContent;
	}

	public CSVFileBuilder getTotalsPerShopContent() {
		return totalsPerShopContent;
	}
}
