package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import org.jsoup.nodes.Element;

//https://www.colyseum.com/loja/index.php?route=product/search&search=Brionac&limit=100
public class ColyseumShopService extends SearchService{

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
		return ResultNameFilter.noFilter();
	}

	@Override
	protected ResultPageSelectors getResultPageSelectors() {
		return new ResultPageSelectors() {
			@Override
			public String singleProduct() {
				return ".product-grid";
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
}
