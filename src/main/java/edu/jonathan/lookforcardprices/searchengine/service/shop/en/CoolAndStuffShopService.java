package edu.jonathan.lookforcardprices.searchengine.service.shop.en;

import edu.jonathan.lookforcardprices.comom.MoneyUtil;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.javamoney.moneta.Money;
import org.jsoup.nodes.Element;

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
	protected ResultNameFilter getResultNameFilter() {
		return ResultNameFilter.noFilter();
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
