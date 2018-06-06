package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.comom.Keys;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.UrlReaderService;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public abstract class SearchService {

	@Inject
	protected UrlReaderService urlReaderService;

	public static final String URL_SEARCH_SAMPLE = Keys.SEARCH_TEXT_TO_REPLACE;
	public static final String PRODUCT_PRICE_NOT_AVAILABLE = "Unable to get price";

	public List<Product> run(Shop shop, String productName) throws IOException {
		return run(shop, productName, false);
	}

	public List<Product> run(Shop shop, String productName, boolean maxResultsPerPage) throws IOException {
		System.out.println("Shop: "+shop.getName());
		long time = System.currentTimeMillis();

		URL resultsPageURL = prepareResultsPageURL(shop, productName, maxResultsPerPage);

		Document resultsPage = readResultsDocument(resultsPageURL);
		System.out.println("\tTime to Reach URL: "+(System.currentTimeMillis() - time));

//		System.out.println(resultsPage);
		afterResultListener(resultsPage);

		List<Product> products = readProductsAt(resultsPage, shop, productName, resultsPageURL);
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

	private Document readResultsDocument(URL resultsPageURL) throws IOException {
		return urlReaderService.readUrlDocument( resultsPageURL.toString() );
	}

	protected List<Product> readProductsAt(Document resultsPage, Shop shop, String productName, URL resultsPageURL){
		ResultPageSelectors selectors = getResultPageSelectors();

		Elements els = resultsPage.select( selectors.singleProduct() );
		System.out.println("\t\tResults Elements: "+els.size());

		els = cleanTitleResults(els);

		return readProductsData(els, selectors, shop, productName, resultsPageURL, getResultNameFilter());
	}

	private Elements cleanTitleResults(Elements els) {
		if(els.size() > 0 && isFirstResultTitle()){
			els.remove(0);
		}
		return els;
	}

	protected List<Product> readProductsData(Elements els, ResultPageSelectors selectors, Shop shop, String productName, URL resultsPageURL, ResultNameFilter resultNameFilter) {
		List<Product> products = new ArrayList<>(els.size());

		String previewName;

		for( Element productContainer : els ){

			previewName = Optional.ofNullable(selectors.productName()).map(s -> productContainer.select(s).text()).orElse(productName);

			System.out.println("\t\tPreview name: "+previewName);

			if( resultNameFilter.isValid(previewName, productName) ){
				getProductList(selectors, shop, resultsPageURL, products, previewName, productContainer);
			}else{
				System.out.println("\t\tRemoved by name filter...");
			}
		}

		return products;
	}

	protected void getProductList(ResultPageSelectors selectors, Shop shop, URL resultsPageURL, List<Product> products, String previewName, Element productContainer) {
		String previewImageURL;
		String individualUrl;
		String formattedPrice;
		boolean available;
		previewImageURL = Util.completeURL( shop.getMainUrl(), productContainer.select( selectors.productImageURL() ).attr("src") );

		individualUrl = getItemUrl(productContainer);

		individualUrl = Util.completeURL( shop.getMainUrl(), individualUrl );

		Elements priceElements = productContainer.select( selectors.productPrice() );

		if(priceElements.isEmpty()){
			products.add( new Product(previewName, false, shop, previewImageURL, individualUrl, resultsPageURL,productContainer, PRODUCT_PRICE_NOT_AVAILABLE ) );
		}

		for (Element priceElement : priceElements){
			formattedPrice = priceElement.text().trim();

			formattedPrice = formattedPrice.isEmpty() ? PRODUCT_PRICE_NOT_AVAILABLE : formattedPrice;

			available = isProductAvailable( productContainer );

			products.add( new Product(previewName, available, shop, previewImageURL, individualUrl, resultsPageURL,productContainer, formattedPrice ) );
		}
	}

	protected String getItemUrl(Element productContainer) {
		return productContainer.select("a").first().attr("href");
	}

	public boolean hasPortugueseOption(){
		return true;
	}

	protected void afterResultListener(Document resultsPage){}

	protected boolean isFirstResultTitle(){ return false; }

	protected abstract boolean isProductAvailable(Element productContainer);

	protected abstract String getSearchUrlSample(String mainUrl);

	protected abstract void setMaxResultsPerPage();

	protected abstract String replaceUrlWithEncodedProductName(String url, String productName);

	protected abstract ResultNameFilter getResultNameFilter();

	protected abstract ResultPageSelectors getResultPageSelectors();

}
