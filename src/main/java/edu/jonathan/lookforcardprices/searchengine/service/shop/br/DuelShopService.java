package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.javamoney.moneta.Money;
import org.jsoup.nodes.Element;

//https://www.duelshop.com.br/procurar?controller=search&orderby=position&orderway=desc&search_query=Mirror+Force
public class DuelShopService extends SearchService {

	private int resultsPerPage = 16;

    @Override
    protected boolean isProductAvailable(Element productContainer) {
        return productContainer.select(".produto-indisponivel").size() == 0;
    }

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "procurar?" +
				"controller=search" +
				"&orderby=position" +
				"&orderway=desc" +
				"&n=" + resultsPerPage +
				"&search_query=" + URL_SEARCH_SAMPLE;
	}

	@Override
	protected void setMaxResultsPerPage() {
		resultsPerPage = 40;
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
				return ".product_list  li";
			}

			@Override
			public String productName() {
				return "h5";
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			}

			@Override
			public String productPrice() {
				return ".right-block .content_price .price";
			}
		};
	}

	@Override
	protected Money getPriceFrom(String formattedValue) {
		return null;
	}
}
