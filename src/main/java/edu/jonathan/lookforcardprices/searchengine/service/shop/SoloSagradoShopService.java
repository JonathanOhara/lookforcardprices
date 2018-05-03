package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import org.jsoup.nodes.Element;


//https://www.solosagrado.com.br/busca?pg=15&pesq=mirror&categoria=&view=&ord=2&pagina=1&qtdview=24
public class SoloSagradoShopService extends SearchService{

	private int resultsPerPage = 12;

	@Override
	protected boolean isProductAvaliable(Element productContainer) {
		return !"Indisponível".equals(productContainer.select(".produto-qtd").text());
	}

	@Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "busca?" +
                "pg=15" +
				"&categoria=" +
				"&qtdview=" + resultsPerPage  +
				"&pesq=" + URL_SEARCH_SAMPLE;
	}

	@Override
	protected void setMaxResultsPerPage() {
		resultsPerPage = 24;
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
				return ".products_container .product_item";
			}

			@Override
			public String productName() {
				return "figcaption h5 a:eq(0)";
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			}

			@Override
			public String productPrice() {
				return ".scheme_color > span:last-child";
			}
		};
	}
}
