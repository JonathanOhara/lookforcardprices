package edu.jonathan.lookforcardprices.searchengine.service.shop.en;

import edu.jonathan.lookforcardprices.comom.MoneyUtil;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.javamoney.moneta.Money;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

//https://www.coolstuffinc.com/main_search.php?pa=searchOnName&page=1&resultsPerPage=25&q=mirror+force
public class CoolAndStuffShopService extends SearchService {

	private int resultsPerPage = 25;

	@Override
    protected boolean isProductAvailable(Element productContainer) {
		String completePrice = Optional.ofNullable( productContainer.select(".pPrice").text() ).map( s -> s.trim() ).orElse("");
        return !completePrice.contains("Out of Stock");
    }

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "main_search.php?" +
				"&pa=searchOnName" +
				"&page=1" +
				"&resultsPerPage=" + resultsPerPage  +
				"&q=" + URL_SEARCH_SAMPLE;
	}

	@Override
	protected void setMaxResultsPerPage() {
		resultsPerPage = 50;
	}

	@Override
	protected String replaceUrlWithEncodedProductName(String url, String productName) {
		return Util.prepareUrlMode1( url, productName );
	}

	@Override
	protected List<Product> readProductsData(Elements els, ResultPageSelectors selectors, Shop shop, String productName, URL resultsPageURL, ResultNameFilter resultNameFilter) {
		List<Product> products = new ArrayList<>(els.size());

		String previewName;

		for( Element productByCollection : els ){

			for( Element productState: productByCollection.select(".userTable > tbody > tr" )){
				previewName = Optional.ofNullable(selectors.productName()).map(s -> productByCollection.select(s).text()).orElse(productName);

				previewName += " (" + productState.select(".pCondition").text() + ")";

				if( resultNameFilter.isValid(previewName, productName) ){
					logger.debug("\tAccepted by name filter: "+previewName);
					getProductList(selectors, shop, resultsPageURL, products, previewName, productState);
				}else{
					logger.debug("\tRejected by name filter: "+previewName);
				}
			}
		}

		return products;
	}

	@Override
	protected String getItemUrl(Element productState) {
		Element userTable = productState.parent().parent();
		Element tdProducts = userTable.parent();
		Element productMainContainer = tdProducts.parent();

		return productMainContainer.select("a").first().attr("href");
	}

	@Override
	protected ResultNameFilter getResultNameFilter() {
		return ResultNameFilter.ignoreCaseContains();
	}

	@Override
	protected ResultPageSelectors getResultPageSelectors() {
		return new ResultPageSelectors() {
			@Override
			public String singleProduct() {
				return "#searchResults > tbody > tr";
			}

			@Override
			public String productName() {
				return "h3 a";
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			}

			@Override
			public String productPrice() {
				return ".pPrice span";
			}
		};
	}

	@Override
	protected Money getPriceFrom(String formattedValue) {

		if( formattedValue.isEmpty() ) return null;

		Matcher matcher = MoneyUtil.MONEY_PATTERN.matcher(formattedValue.substring(1).trim());

		matcher.matches();

		Money amount = Money.of(Double.parseDouble(matcher.group(1) + "." + matcher.group(2)), getCurrency());

		return amount;
	}

	@Override
	public String getCurrency() {
		return MoneyUtil.DOLLAR.getCurrencyCode();
	}

	@Override
	public boolean hasPortugueseOption() {
		return false;
	}
}
