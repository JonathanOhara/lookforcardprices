package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.jsoup.nodes.Element;

//https://www.prrjcards.com.br/catalogsearch/result/index/?limit=36&q=monster+reborn
public class PrRjShopService extends SearchService {

	private int resultsPerPage = 12;

    @Override
    protected boolean isProductAvailable(Element productContainer) {
        return productContainer.select(".out-of-stock").size() == 0;
    }

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "catalogsearch/result/index/?" +
				"&limit=" + resultsPerPage  +
				"&q=" + URL_SEARCH_SAMPLE;
	}

	@Override
	protected void setMaxResultsPerPage() {
		resultsPerPage = 36;
	}

	@Override
	protected String replaceUrlWithEncodedProductName(String url, String productName) {
		return Util.prepareUrlMode1( url, productName );
	}

	@Override
	protected ResultNameFilter getResultNameFilter() {
		return ResultNameFilter.contains();
	}

	@Override
	protected ResultPageSelectors getResultPageSelectors() {
		return new ResultPageSelectors() {
			@Override
			public String singleProduct() {
				return ".products-grid .item";
			}

			@Override
			public String productName() {
				return ".product-name";
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			}

			@Override
			public String productPrice() {
				return ".price-box > p:last-child";
			}
		};
	}
}
