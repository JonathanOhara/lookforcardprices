package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.comom.MoneyUtil;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.javamoney.moneta.Money;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;

//https://mypcards.com/produto/index?ProdutoSearch%5Bquery%5D=monster+reborn
public class MypDominionShopService extends SearchService {

	private int resultsPerPage = 20;

    @Override
    protected boolean isProductAvailable(Element productContainer) {
        return !productContainer.select(".card-estatistica > b:eq(0)").text().isEmpty();
    }

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "produto/index?" +
				//"&results=" + resultsPerPage  +
				"ProdutoSearch%5Bquery%5D=" + URL_SEARCH_SAMPLE;
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
				return "ul.stream-list > li.stream-item";
			}

			@Override
			public String productName() {
				return ".card-name";
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			} //BackgroundImage

			@Override
			public String productPrice() {
				return ".card-estatistica > b:eq(1)";
			}
		};
	}

	@Override
	protected Money getPriceFrom(String formattedValue) {
		Matcher matcher = MoneyUtil.MONEY_PATTERN.matcher(formattedValue.trim());

		matcher.matches();

		Money amount = Money.of(Double.parseDouble(matcher.group(1) + "." + matcher.group(2)), getCurrency());

		return amount;
	}
}
