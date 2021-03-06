package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.comom.MoneyUtil;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.javamoney.moneta.Money;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;

//Result Page:
//https://www.lojadotoguro.com.br/?view=ecom%2Fitens&id=71080&searchExactMatch=&busca=Mirror+Force
//https://www.hotyugioh.com.br/?view=ecom%2Fitens&id=155440&searchExactMatch=true&busca=mirror
//https://www.lojacentralgeek.com.br/?view=ecom%2Fitens&id=119884&searchExactMatch=true&busca=mirror+force
//https://www.fenixhousetcg.com.br/?view=ecom%2Fitens&id=71080&searchExactMatch=true&busca=mirror+force
//https://www.overrun.com.br/?view=ecom%2Fitens&id=25716&searchExactMatch=&busca=Ataque+link
public class LigaMagicShopService extends SearchService {

	private int resultsPerPage = 12;

	private Map<String, String> classNameCharacter = new HashMap<>();
	protected ResultMode resultMode = ResultMode.SEARCH_PAGE;

	@Override
	protected boolean isProductAvailable(Element productContainer) {
		switch (resultMode){
			case PRODUCT_PAGE:
				return productContainer.select("td:nth-child(7)").select("input").size() > 0 ;
			case SEARCH_PAGE:
				return true;
			default:
				Elements quantityLabelColumn = productContainer.select(".pQtyP");
				Element quantityElement = quantityLabelColumn.prev().get(0);

				//TODO quantity classes are ramdomily generated
				if( quantityElement.children().size() < 2 && quantityElement.child(0).classNames().contains("kPkVv") ){
					return false;
				}
				return true;
		}
	}

	@Override
	protected String getItemUrl(Element productContainer) {
		switch (resultMode){
			case PRODUCT_PAGE:
				return productContainer.ownerDocument().location();
			case SEARCH_PAGE:
			default:
				return super.getItemUrl(productContainer);
		}
	}

	@Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "?" +
				"view=ecom%2Fitens" +
                "&id=71080" +
                "&searchExactMatch=true" +
				"&busca=" + URL_SEARCH_SAMPLE;
	}

	@Override
	protected void setMaxResultsPerPage() {
		resultsPerPage = 24;
	}

	@Override
	protected String replaceUrlWithEncodedProductName(String url, String productName) {
		return Util.prepareUrlMode1( url, productName );
	}

	@Override
	protected ResultNameFilter getResultNameFilter() {
		return ResultNameFilter.ignoreCaseContains();
	}

	@Override
	protected List<Product> readProductsData(Elements els, ResultPageSelectors selectors, Shop shop, String productName, URL resultsPageURL, ResultNameFilter resultNameFilter) {
		List<Product> products = new ArrayList<>();

		String previewName = null;

		for( Element productContainer : els ){
			if( resultMode == ResultMode.PRODUCT_PAGE ){
				String pageTitle = productContainer.ownerDocument().title();
				previewName = pageTitle.substring(0, getTitleSeparator(pageTitle) - 1);
			}else if ( resultMode == ResultMode.SEARCH_PAGE ){
				previewName = Optional.ofNullable(selectors.productName()).map(s -> productContainer.select(s).text()).orElse(productName);
			}

			if( resultNameFilter.isValid(previewName, productName) ){
				logger.debug("\tAccepted by name filter: "+previewName);
				products.addAll( getProductList(selectors, shop, resultsPageURL, previewName, productContainer) );
			}else{
				logger.debug("\tRejected by name filter: "+previewName);
			}
		}

		return products;
	}

	protected int getTitleSeparator(String pageTitle) {
		return pageTitle.indexOf("|");
	}

	@Override
	protected String getFormattedPriceFrom(Element priceElement ){
		if(resultMode == ResultMode.PRODUCT_PAGE){

			Elements promotionalPrice = getPromotionalPrice(priceElement);

			if( promotionalPrice.size() == 0 ){
				return priceElement.text().trim();
			}else{
				return promotionalPrice.get(0).text().trim();
			}
		}else if(resultMode == ResultMode.SEARCH_PAGE){
			return PRODUCT_PRICE_NOT_AVAILABLE;
		}

		return null;
	}

	private Elements getPromotionalPrice(Element priceElement) {
		return priceElement.select("font");
	}

	@Override
	protected ResultPageSelectors getResultPageSelectors() {
		ResultPageSelectors resultPageSelectors = null;

		if( resultMode == ResultMode.PRODUCT_PAGE ){
			resultPageSelectors = new ResultPageSelectors() {
				@Override
				public String singleProduct() {
					return "div.itemMain > table > tbody > tr > td:nth-child(2) > div:nth-child(3) > table > tbody > tr";
				}
				@Override
				public String productName() {
					return null;
				}
				@Override
				public String productImageURL() {
					return "img:eq(0)";
				}
				@Override
				public String productPrice() {
					return ".itemPreco";
				}
			};
		}else if (resultMode == ResultMode.SEARCH_PAGE){
			resultPageSelectors = new ResultPageSelectors() {
				@Override
				public String singleProduct() {
					return ".pProdItens";
				}
				@Override
				public String productName() {
					return ".xtitleP";
				}
				@Override
				public String productImageURL() {
					return "img:eq(0)";
				}
				@Override
				public String productPrice() {
					return ".pPrecoP > div:nth-child(2)";
				}
			};
		}

		return resultPageSelectors;
	}

	@Override
	protected boolean isFirstResultTitle() {
		switch (resultMode){
			case PRODUCT_PAGE:
				return true;
			case SEARCH_PAGE:
			default:
				return false;
		}
	}

	@Override
	protected void afterResultListener(Document resultsPage) {

		String title = resultsPage.title();

		fillMapClassNamePriceCharacter();

		if(!title.contains("Busca por")){
			resultMode = ResultMode.PRODUCT_PAGE;
		}
	}

	private void fillMapClassNamePriceCharacter() {
		//TODO quantity classes are ramdomily generated
		classNameCharacter.put("aVsMv", "0");
		classNameCharacter.put("aHwGy", "1");
		classNameCharacter.put("fXaTg", "2");
		classNameCharacter.put("oHpDe", "3");
		classNameCharacter.put("nOsIx", "4");
		classNameCharacter.put("vUsEx", "5");
		classNameCharacter.put("sDuUk", "6");
		classNameCharacter.put("nUiHd", "7");
		classNameCharacter.put("vMjZv", "8");
		classNameCharacter.put("qBdZg", "9");
	}

	protected enum ResultMode{
		SEARCH_PAGE,
		PRODUCT_PAGE
	}

	@Override
	protected Money getPriceFrom(String formattedValue) {
		Matcher matcher = MoneyUtil.MONEY_PATTERN.matcher(formattedValue.substring(2).trim());

		matcher.matches();

		Money amount = Money.of(Double.parseDouble(matcher.group(1) + "." + matcher.group(2)), getCurrency());

		return amount;
	}
}
