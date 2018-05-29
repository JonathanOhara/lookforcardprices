package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.jsoup.nodes.Element;

//https://www.chq.com.br/Busca.aspx?strBusca=mirror+force
public class CHQShopService extends SearchService {

	private int resultsPerPage = 12;

    @Override
    protected boolean isProductAvailable(Element productContainer) {
        return productContainer.select("input[src=Eshop.Admin/Imagens/Templates/Minimalist/btn-comprar.gif]").size() == 1;
    }

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "Busca.aspx?" +
				"strBusca=" + URL_SEARCH_SAMPLE;
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
		return ResultNameFilter.noFilter();
	}

	@Override
	protected ResultPageSelectors getResultPageSelectors() {
		return new ResultPageSelectors() {
			@Override
			public String singleProduct() {
				return ".product-card";
			}

			@Override
			public String productName() {
				return ".title-product";
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			}

			@Override
			public String productPrice() {
				return ".price-product";
			}
		};
	}
}
