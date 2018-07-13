package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.comom.MoneyUtil;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.javamoney.moneta.Money;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;

//https://www.colyseum.com/loja/index.php?route=product/search&search=Brionac&limit=100
public class ColyseumShopService extends SearchService {

	private int resultsPerPage = 20;

	@Override
    protected boolean isProductAvailable(Element productContainer) {
        return !"Fora de Estoque".equalsIgnoreCase(productContainer.select(".button-group").text().trim());
    }

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "loja/index.php?route=product/search" +
				"&limit=" + resultsPerPage  +
				"&search=" + URL_SEARCH_SAMPLE;
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
				return ".product-list";
			}

			@Override
			public String productName() {
				return ".caption > h4";
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			}

			@Override
			public String productPrice() {
				return ".price";
			}
		};
	}

	@Override
	protected String getFormattedPriceFrom(Element priceElement) {

		Elements promoPrice = priceElement.select(".price-new");
		if( promoPrice.size() > 0 ){
			return promoPrice.text().trim();
		}
		return priceElement.text().trim();
	}

	@Override
	protected Money getPriceFrom(String formattedValue) {
		Matcher matcher = MoneyUtil.MONEY_PATTERN.matcher(formattedValue.substring(2).trim());

		matcher.matches();

		Money amount = Money.of(Double.parseDouble(matcher.group(1) + "." + matcher.group(2)), getCurrency());

		return amount;
	}

}