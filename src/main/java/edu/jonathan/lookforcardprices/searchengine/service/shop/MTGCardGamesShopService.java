package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import org.jsoup.nodes.Element;

//http://www.mtgcards.com.br/index.php?route=product/search&search=covil%20das%20trevas
public class MTGCardGamesShopService extends SearchService{

	private int resultsPerPage = 15;

    @Override
    protected boolean isProductAvaliable(Element productContainer) {
		return "0 unid".equals( productContainer.select(":nth-child(2) td:eq(1)").text() );
	}

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "index.php?route=product/search" +
				"&limit=" + resultsPerPage +
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
		return ResultNameFilter.noFilter();
	}

	@Override
	protected ResultPageSelectors getResultPageSelectors() {
		return new ResultPageSelectors() {
			@Override
			public String singleProduct() {
				return ".product-list > div";
			}

			@Override
			public String productName() {
				return ".name";
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
}
