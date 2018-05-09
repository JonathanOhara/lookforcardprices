package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

//http://www.dmgcardshop.com/pesquisa?controller=search&orderby=position&orderway=desc&search_query=Prepara%C3%A7%C3%A3o+de+Ritos
public class DmgShopService extends SearchService{

	private int resultsPerPage = 20;

    @Override
    protected boolean isProductAvailable(Element productContainer) {
        return !productContainer.select(".price").text().isEmpty();
    }

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "pesquisa?" +
				"&controller=search" +
				"&orderby=position" +
				"&orderway=desc" +
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
				return ".ajax_block_product";
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
				return ".price";
			}
		};
	}
}
