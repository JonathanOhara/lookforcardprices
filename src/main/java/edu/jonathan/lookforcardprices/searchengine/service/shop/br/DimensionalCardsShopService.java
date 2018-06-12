package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.comom.MoneyUtil;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.javamoney.moneta.Money;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;

//https://www.dimensionalcards.com.br/search/?q=mirror+force
public class DimensionalCardsShopService extends SearchService {

	private int resultsPerPage = 20;

    @Override
    protected boolean isProductAvailable(Element productContainer) {
        return productContainer.select(".label-no-stock").size() == 0;
    }

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "search/?" +
//				"&limit=" + resultsPerPage  +
				"q=" + URL_SEARCH_SAMPLE;
	}

	@Override
	protected void setMaxResultsPerPage() {
		resultsPerPage = 100;
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
	protected ResultPageSelectors getResultPageSelectors() {
		return new ResultPageSelectors() {
			@Override
			public String singleProduct() {
				return ".item-container";
			}

			@Override
			public String productName() {
				return ".item-info-container a";
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			}

			@Override
			public String productPrice() {
				return ".item-price";
			}
		};
	}


	@Override
	protected Money getPriceFrom(String formattedValue) {
		Matcher matcher = MoneyUtil.MONEY_PATTERN.matcher(formattedValue.substring(2).trim());

		matcher.matches();

		Money amount = Money.of(Double.parseDouble(matcher.group(1) + "." + matcher.group(2)), getCurrency());

		return amount;
	}
}
