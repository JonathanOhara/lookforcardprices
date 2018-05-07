package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

//https://lista.mercadolivre.com.br/invoked-raidjin
public class MercadoLivreShopService extends SearchService{

	private int resultsPerPage = 12;

	@Override
	protected boolean isProductAvailable(Element productContainer) {
		return true;
	}

	@Override
	protected String getSearchUrlSample(String mainUrl) {
		return "https://lista.mercadolivre.com.br/" + URL_SEARCH_SAMPLE;
	}

	@Override
	protected void setMaxResultsPerPage() {
		resultsPerPage = 24;
	}

	@Override
	protected String replaceUrlWithEncodedProductName(String url, String productName) {
		return Util.prepareUrlMode2( url, productName );
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
				return ".searchResults li";
			}

			@Override
			public String productName() {
				return ".item__title";
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			}

			@Override
			public String productPrice() {
				return ".item__price";
			}
		};
	}
}
