package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import org.jsoup.nodes.Element;

//https://www.legioncg.com.br/?view=ecom%2Fitens&page=1&id=40666&comdesconto=&fOrder=1&btFiltrar=Filtrar&fShow=160&busca=mirror+force
public class LegionCGShopService extends SearchService{

	private int resultsPerPage = 20;

    @Override
    protected boolean isProductAvaliable(Element productContainer) {
        return "0 unid.".equals(productContainer.select(".pQtyP").text());
    }

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "?view=ecom%2Fitens&page=1&id=40666&comdesconto=&fOrder=1&btFiltrar=Filtrar&" +
				"&Show=" + resultsPerPage +
				"&busca=" + URL_SEARCH_SAMPLE;
	}

	@Override
	protected void setMaxResultsPerPage() {
		resultsPerPage = 160;
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
				return ".pProdItens";
			}

			@Override
			public String productName() {
				return ".xtitleP";
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			}

			@Override
			public String productPrice() {
				return ".pPrecoP";
			}
		};
	}
}
