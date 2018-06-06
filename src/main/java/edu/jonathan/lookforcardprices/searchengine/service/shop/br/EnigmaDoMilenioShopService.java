package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.jsoup.nodes.Element;

//http://www.enigmadomilenio.com.br/loja/search?search_query=nekroz&orderby=position&orderway=desc&search_query=nekroz&submit_search=&n=90
public class EnigmaDoMilenioShopService extends SearchService {

	private int resultsPerPage = 18;

    @Override
    protected boolean isProductAvailable(Element productContainer) {
        return productContainer.select(".label-danger").size() == 0;
    }

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "loja/search?search_query=nekroz&orderby=position&orderway=desc" +
				"&n=" + resultsPerPage  +
				"&search_query=" + URL_SEARCH_SAMPLE;
	}

	@Override
	protected void setMaxResultsPerPage() {
		resultsPerPage = 90;
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
				return "#product_list > li";
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
				return ".product-price";
			}
		};
	}
}
