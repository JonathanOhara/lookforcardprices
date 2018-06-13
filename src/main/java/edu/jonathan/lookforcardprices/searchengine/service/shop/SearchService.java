package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.comom.Keys;
import edu.jonathan.lookforcardprices.comom.MoneyUtil;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.ProductPrice;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.UrlReaderService;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import org.apache.log4j.Logger;
import org.javamoney.moneta.Money;
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

	protected static final Logger logger = Logger.getLogger(SearchService.class);

	public static final String URL_SEARCH_SAMPLE = Keys.SEARCH_TEXT_TO_REPLACE;
	public static final String PRODUCT_PRICE_NOT_AVAILABLE = "Unable to get price";

	public List<Product> run(Shop shop, String productName) throws IOException {
		return run(shop, productName, false);
	}

	public List<Product> run(Shop shop, String productName, boolean maxResultsPerPage) {
		Util.configureOutputToFileAndConsole( logger, shop.getName() + "/" + productName);

		logger.debug("Shop: "+shop.getName());
		long time = System.currentTimeMillis();

		URL resultsPageURL = prepareResultsPageURL(shop, productName, maxResultsPerPage);

		Document resultsPage = readResultsDocument(resultsPageURL);
		logger.trace("\tTime to Reach URL: "+(System.currentTimeMillis() - time));

		afterResultListener(resultsPage);

		List<Product> products = readProductsAt(resultsPage, shop, productName, resultsPageURL);
		logger.trace("\tTime to read all page products info: "+(System.currentTimeMillis() - time));

		logger.debug("\tProducts size: "+products.size());
		return products;
	}

	private URL prepareResultsPageURL(Shop shop, String productName, boolean maximumResultsPerPage) {
		if( maximumResultsPerPage ){
			setMaxResultsPerPage();
		}

		URL resultsURL = null;
		try {
			resultsURL = new URL( replaceUrlWithEncodedProductName( getSearchUrlSample(shop.getMainUrl()), productName ) );
		} catch (MalformedURLException e) {
			logger.error(e);
		}

		return resultsURL;
	}

	private Document readResultsDocument(URL resultsPageURL) {
		return urlReaderService.readUrlDocument( resultsPageURL.toString() );
	}

	protected List<Product> readProductsAt(Document resultsPage, Shop shop, String productName, URL resultsPageURL){
		ResultPageSelectors selectors = getResultPageSelectors();

		Elements els = resultsPage.select( selectors.singleProduct() );
		logger.trace("\tResults Elements: "+els.size());

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
		List<Product> products = new ArrayList<>();

		String previewName;

		for( Element productContainer : els ){

			previewName = Optional.ofNullable(selectors.productName()).map(s -> productContainer.select(s).text()).orElse(productName);

			if( resultNameFilter.isValid(previewName, productName) ){
				logger.debug("\tAccepted by name filter: "+previewName);
				products.addAll( getProductList(selectors, shop, resultsPageURL, previewName, productContainer) );
			}else{
				logger.debug("\tRejected by name filter: "+previewName);
			}
		}

		return products;
	}

	protected List<Product> getProductList(ResultPageSelectors selectors, Shop shop, URL resultsPageURL, String previewName, Element productContainer) {
		List<Product> products = new ArrayList<>();
		String previewImageURL;
		String individualUrl;
		String formattedPrice;
		boolean available;
		previewImageURL = Util.completeURL( shop.getMainUrl(), productContainer.select( selectors.productImageURL() ).attr("src") );

		individualUrl = getItemUrl(productContainer);

		individualUrl = Util.completeURL( shop.getMainUrl(), individualUrl );

		Elements priceElements = productContainer.select( selectors.productPrice() );

		if(priceElements.isEmpty()){
			products.add( new Product(previewName, false, shop, previewImageURL, individualUrl, resultsPageURL,productContainer ) );
		}

		for (Element priceElement : priceElements){
			Money price = null;
			available = isProductAvailable(productContainer);

			formattedPrice = getFormattedPriceFrom(priceElement);

			if( !formattedPrice.isEmpty() && !formattedPrice.equals(PRODUCT_PRICE_NOT_AVAILABLE) ){
				price = getPriceFrom(formattedPrice);
			}

			ProductPrice productPrice = price == null || price.getNumber().doubleValueExact() == 0.0 ? null : new ProductPrice(formattedPrice, price);

			products.add( new Product(previewName, available, shop, previewImageURL, individualUrl, resultsPageURL,productContainer, productPrice ) );
		}

		return products;
	}

	protected String getFormattedPriceFrom(Element priceElement) {
		return priceElement.text().trim();
	}

	protected abstract Money getPriceFrom(String formattedValue);

	protected String getItemUrl(Element productContainer) {
		return productContainer.select("a").first().attr("href");
	}

	public String getCurrency(){
		return MoneyUtil.REAL.getCurrencyCode();
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
