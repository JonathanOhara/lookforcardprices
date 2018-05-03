package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import org.jsoup.nodes.Element;

//https://mypcards.com/produto/index?ProdutoSearch%5Bquery%5D=Tuf%C3%A3o
public class MypDominionShopService extends SearchService{

	private int resultsPerPage = 20;

    @Override
    protected boolean isProductAvaliable(Element productContainer) {
        return !productContainer.select(".card-estatistica > b:eq(0)").text().isEmpty();
    }

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "produto/index?" +
				//"&results=" + resultsPerPage  +
				"&ProdutoSearch%5Bquery%5D=" + URL_SEARCH_SAMPLE;
	}

	@Override
	protected void setMaxResultsPerPage() {
		resultsPerPage = 50;
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
				return ".stream-list li";
			}

			@Override
			public String productName() {
				return ".card-name div:eq(0)";
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			} //BackgroundImage

			@Override
			public String productPrice() {
				return ".card-estatistica > b:eq(1)";
			}
		};
	}
}
