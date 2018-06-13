package edu.jonathan.lookforcardprices.searchengine.service;

import edu.jonathan.lookforcardprices.CDIExtension;
import edu.jonathan.lookforcardprices.comom.CSVFileBuilder;
import edu.jonathan.lookforcardprices.comom.MoneyUtil;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.ProductPrice;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.javamoney.moneta.Money;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith({MockitoExtension.class, CDIExtension.class})
@RunWith(JUnitPlatform.class)
public class TotalsReportServiceTest {

	@Inject
	protected TotalsReportService service;

	@Test
	public void shouldGenerateProperlyHeaders(){
		service.generateHeaders();

		CSVFileBuilder totalsPerProductContent = service.getTotalsPerProductContent();
		CSVFileBuilder totalsPerShopContent = service.getTotalsPerShopContent();

		Assertions.assertEquals( totalsPerProductContent.getMaxColumns(), 6 );
		Assertions.assertEquals( totalsPerShopContent.getMaxColumns(), 2 );
	}

    @Test
    public void shouldGenerateProductsTotals() throws MalformedURLException {
        Map.Entry<Shop, List<Product>> entry;
        Map<Shop, List<Product>> productsByShop = new HashMap<>();

        Shop coolAndStuff = new Shop("Cool And Stuff", "https://www.coolstuffinc.com/");

        entry = new HashMap.SimpleEntry(coolAndStuff, new ArrayList<>());
        ProductPrice productPrice = new ProductPrice("$ 2.50", Money.of(2.50, MoneyUtil.DOLLAR));

        entry.getValue().add(new Product("Nome", true, coolAndStuff, "http://fakeImage.com", "http://fakeURL", new URL( coolAndStuff.getMainUrl()+ "/x" ), new Element("div"), productPrice ));

        productsByShop.put(entry.getKey(), entry.getValue());

        service.generateTotalsPerProductContent(productsByShop, "Mirror Force");

        CSVFileBuilder totalsPerProductContent = service.getTotalsPerProductContent();

        String csvContent = totalsPerProductContent.build();
        Assertions.assertEquals( totalsPerProductContent.getMaxColumns(), 6 );
        Assertions.assertTrue( csvContent.indexOf('\n') > -1 );
    }

    @Test
    public void shouldGenerateShopsTotals() throws IOException, URISyntaxException {
        List<Product> products = new ArrayList<>();

        Shop coolAndStuff = new Shop("Cool And Stuff", "https://www.coolstuffinc.com/");

        ProductPrice productPrice = new ProductPrice("$ 2.50", Money.of(2.50, MoneyUtil.DOLLAR));

        products.add(new Product("Nome", true, coolAndStuff, "http://fakeImage.com", "http://fakeURL", new URL( coolAndStuff.getMainUrl()+ "/x" ), new Element("div"), productPrice ));

        service.generateReportProductsByShop("Mirror Force", products);

        CSVFileBuilder totalsPerProductContent = service.getTotalsPerShopContent();

        String csvContent = totalsPerProductContent.build();
        Assertions.assertEquals( totalsPerProductContent.getMaxColumns(), 2 );
        Assertions.assertTrue( csvContent.indexOf('\n') > -1 );
    }

/*
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

	private void computeShopsTotals(Map<Shop, List<Product>> productsByShop) {
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
		logger.trace("Generating Report for: "+productName);
		Util.configureOutputToFileAndConsole(productName);

		totalsPerProductContent = CSVFileBuilder.createCSVFile();
		totalsPerShopContent = CSVFileBuilder.createCSVFile();

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
		for( Entry<Shop, List<Product>> entry: productsByShop.entrySet() ){
			shop = entry.getKey();
			products = entry.getValue();

			htmlReport.addReport( shop, products );
		}

		htmlReport.addOtherSeekers(productName);

		htmlReport.addMetaData(productName, 0, data, hora); // TODO Review this

		htmlReport.closeAndWriteFile(productName);

		generateTotalsPerProductContent(productsByShop, productName);
		computeShopsTotals(productsByShop);

		closeAndWriteFile();
	}
*/
}
