package edu.jonathan.lookforcardprices.searchengine.service.shop.eu;

import edu.jonathan.lookforcardprices.comom.MoneyUtil;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.spi.MoneyUtils;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;


//https://www.cardmarket.com/en/YuGiOh/MainPage/showSearchResult?searchFor=Mirror+force
public class CardMarketShopService extends SearchService {

	private int resultsPerPage = 12;

	@Override
	protected boolean isProductAvailable(Element productContainer) {
		return !"0".equals(productContainer.select("td:eq(6)").text().trim());
	}

	@Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "en/YuGiOh/MainPage/showSearchResult?" +
				"searchFor=" + URL_SEARCH_SAMPLE;
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
		return ResultNameFilter.noFilter();
	}

	@Override
	protected ResultPageSelectors getResultPageSelectors() {
		return new ResultPageSelectors() {
			@Override
			public String singleProduct() {
				return ".SearchTable > tbody > tr";
			}

			@Override
			public String productName() {
				return "td:eq(4)";
			}

			@Override
			public String productImageURL() {
				return "td:eq(0)";
			}

			@Override
			public String productPrice() {
				return "td:eq(7)";
			}
		};
	}

	protected String getItemUrl(Element productContainer) {
		return productContainer.select("td:eq(4) a").first().attr("href");
	}

	protected String getImageUrl(ResultPageSelectors selectors, Shop shop, Element productContainer) {
		return ""; //TODO this
	}

	@Override
	public String getCurrency() {
		return "EUR";
	}

	@Override
	protected Money getPriceFrom(String formattedValue) {

		if("-".equals(formattedValue.trim())) return null;

		Matcher matcher = MoneyUtil.MONEY_PATTERN.matcher(formattedValue.substring(0, formattedValue.length() - 2).trim());

		matcher.matches();

		Money amount = Money.of(Double.parseDouble(matcher.group(1) + "." + matcher.group(2)), getCurrency());

		return amount;
	}
}
