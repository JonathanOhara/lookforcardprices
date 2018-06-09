package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.List;

//http://www.mtgcards.com.br/index.php?route=product/search&search=covil%20das%20trevas
public class MTGCardGamesShopService extends SearchService {

	private int resultsPerPage = 15;

    @Override
    protected boolean isProductAvailable(Element productContainer) {
		return productContainer.select(".cart > input").size() > 0;
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

	@Override
	protected String getPriceFrom(Element priceElement) {
		List<TextNode> textNodes = priceElement.textNodes();

		String price = "";
		for (TextNode textNode : textNodes){
			price += textNode.text().trim();
		}

		return price;
	}
}
