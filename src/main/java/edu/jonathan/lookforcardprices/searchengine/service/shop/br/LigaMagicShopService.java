package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//Result Page:
//https://www.lojadotoguro.com.br/?view=ecom%2Fitens&id=71080&searchExactMatch=&busca=Mirror+Force
//https://www.hotyugioh.com.br/?view=ecom%2Fitens&id=155440&searchExactMatch=&busca=mirror
//Direct to Product:
//https://www.fenixhousetcg.com.br/?view=ecom%2Fitens&id=71080&searchExactMatch=&busca=Invoked+Raidjin
public class LigaMagicShopService extends SearchService {

	private int resultsPerPage = 12;

	private ResultMode resultMode = ResultMode.SEARCH_PAGE;

	@Override
	protected boolean isProductAvailable(Element productContainer) {
		switch (resultMode){
			case PRODUCT_PAGE:
				return !"0 unid.".equals(productContainer.select("td:nth-child(5)").text().trim());
			case SEARCH_PAGE:
			default:
				Elements quantityColumn = productContainer.select(".pQtyP");
				if( quantityColumn.size() > 0 && "0 unid.".equals(quantityColumn.text()) ){
					return false;
				}

				Elements divsForPrice = productContainer.select("table > tbody > tr > td:nth-child(1) > div:nth-child(1) div");
				boolean unavailable = divsForPrice.size() == 1 && divsForPrice.hasClass("vBsOa");
				return !unavailable;
		}
	}

	@Override
	protected String getItemUrl(Element productContainer) {
		switch (resultMode){
			case PRODUCT_PAGE:
				return productContainer.ownerDocument().location();
			case SEARCH_PAGE:
			default:
				return super.getItemUrl(productContainer);
		}
	}

	@Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "?" +
				"view=ecom%2Fitens" +
                "&id=71080" +
                "&searchExactMatch=true" +
				"&busca=" + URL_SEARCH_SAMPLE;
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
				switch (resultMode){
					case PRODUCT_PAGE:
						return "div.itemMain > table > tbody > tr > td:nth-child(2) > div:nth-child(3) > table > tbody > tr";
					case SEARCH_PAGE:
					default:
						return ".pProdItens";
				}
			}

			@Override
			public String productName() {
				switch (resultMode){
					case PRODUCT_PAGE:
					    //body > table > tbody > tr:nth-child(2) > td > table:nth-child(3) > tbody > tr:nth-child(1) > td:nth-child(2) > table > tbody > tr > td:nth-child(2) > div.itemMain > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td:nth-child(1) > div > b
						return null;
					case SEARCH_PAGE:
					default:
						return ".xtitleP";
				}
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			}

			@Override
			public String productPrice() {
				switch (resultMode){
					case PRODUCT_PAGE:
						return ".itemPreco";
					case SEARCH_PAGE:
					default:
						return ".pPrecoP";
				}
			}
		};
	}

	@Override
	protected boolean isFirstResultTitle() {
		switch (resultMode){
			case PRODUCT_PAGE:
				return true;
			case SEARCH_PAGE:
			default:
				return false;
		}
	}

	@Override
	protected void afterResultListener(Document resultsPage) {

		String title = resultsPage.title();

		if(!title.contains("Busca por")){
			resultMode = ResultMode.PRODUCT_PAGE;
		}
	}

	private enum ResultMode{
		SEARCH_PAGE,
		PRODUCT_PAGE
	}
}
