package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public abstract class SearchService {

	public static final String URL_SEARCH_SAMPLE = "<SEARCH>";

	public List<Product> run(Shop shop, String productName, boolean maxResultsPerPage) throws IOException {
		System.out.println("Shop: "+shop.getName());
		long time = System.currentTimeMillis();

		URL resultsPageURL = prepareResultsPageURL(shop, productName, maxResultsPerPage);
		System.out.println("\tTime to Prepare URL: "+(System.currentTimeMillis() - time));

		Document resultsPage = readResults(resultsPageURL);
		System.out.println("\tTime to Reach URL: "+(System.currentTimeMillis() - time));

		List<Product> products = readProductsAt(resultsPage, shop, productName);
		System.out.println("\tTime to read all page products info: "+(System.currentTimeMillis() - time));

		System.out.println("\t\tProducts size: "+products.size());
		return products;
	}

	private URL prepareResultsPageURL(Shop shop, String productName, boolean maximumResultsPerPage) throws MalformedURLException {
		if( maximumResultsPerPage ){
			setMaxResultsPerPage();
		}

		URL resultsURL = new URL( replaceUrlWithEncodedProductName( getSearchUrlSample(shop.getMainUrl()), productName ) );

		return resultsURL;
	}

	private Document readResults(URL resultsPageURL) throws IOException {
		return Util.readUrlDocument( resultsPageURL.toString() );
	}

	protected List<Product> readProductsAt(Document resultsPage, Shop shop, String productName){
		ResultPageSelectors selectors = getResultPageSelectors();

		Elements els = resultsPage.select( selectors.singleProduct() );
		System.out.println("\t\tResults Elements: "+els.size());

		return readProductsData(els, selectors, shop, productName);
	}

	private List<Product> readProductsData(Elements els, ResultPageSelectors selectors, Shop shop, String productName) {
		List<Product> products = new ArrayList<>(els.size());

		String previewName, previewImageURL;
		String individualUrl, formattedPrice;
		boolean available;
		Element productContainer;

		ResultNameFilter resultNameFilter = getResultNameFilter();
		for( Element element : els ){
			productContainer = element;

			previewName = productContainer.select(selectors.productName()).text();

			System.out.println("\t\tPreview name: "+previewName);

			if( resultNameFilter.isValid(previewName, productName) ){
				previewImageURL = Util.completeURL( shop.getMainUrl(), productContainer.select( selectors.productImageURL() ).attr("src") );

				individualUrl = productContainer.select("a").first().attr("href");

				individualUrl = Util.completeURL( shop.getMainUrl(), individualUrl );

				formattedPrice = productContainer.select( selectors.productPrice() ).text();

				available = isProductAvaliable( productContainer );

				products.add( new Product(previewName, available, shop, previewImageURL, individualUrl, productContainer, formattedPrice ) );
			}else{
				System.out.println("\t\tRemoved by name filter...");
			}
		}

		return products;
	}

	protected abstract boolean isProductAvaliable(Element productContainer);

	protected abstract String getSearchUrlSample(String mainUrl);

	protected abstract void setMaxResultsPerPage();

	protected abstract String replaceUrlWithEncodedProductName(String url, String productName);

	protected abstract ResultNameFilter getResultNameFilter();

	protected abstract ResultPageSelectors getResultPageSelectors();

}
