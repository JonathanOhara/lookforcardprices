package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.comom.MoneyUtil;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.javamoney.moneta.Money;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Set;
import java.util.regex.Matcher;

//https://www.legioncg.com.br/?view=ecom%2Fitens&id=40666&searchExactMatch=true&busca=mirror+force
public class LegionCGShopService extends LigaMagicShopService {

	@Override
	protected String getFormattedPriceFrom(Element priceElement ){
		if(resultMode == ResultMode.PRODUCT_PAGE){

			Elements promotionalPrice = getPromotionalPrice(priceElement);
			
			if( promotionalPrice.size() == 0 ){
				return priceElement.text().trim();
			}else{
				return promotionalPrice.get(0).text().trim();
			}
		}else if(resultMode == ResultMode.SEARCH_PAGE){
			return PRODUCT_PRICE_NOT_AVAILABLE;
		}

		return null;
	}

	@Override
	protected ResultPageSelectors getResultPageSelectors() {
		ResultPageSelectors resultPageSelectors = null;

		if( resultMode == ResultMode.PRODUCT_PAGE ){
			resultPageSelectors = new ResultPageSelectors() {
				@Override
				public String singleProduct() {
					return "div.itemMain > table > tbody > tr > td:nth-child(2) > div:nth-child(3) > table > tbody > tr";
				}
				@Override
				public String productName() {
					return null;
				}
				@Override
				public String productImageURL() {
					return "img:eq(0)";
				}
				@Override
				public String productPrice() {
					return ".itemPreco";
				}
			};
		}else if (resultMode == ResultMode.SEARCH_PAGE){
			resultPageSelectors = new ResultPageSelectors() {
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
					return ".pPrecoPDesconto";
				}
			};
		}

		return resultPageSelectors;
	}

	@Override
	protected int getTitleSeparator(String pageTitle) {
		return pageTitle.indexOf("-");
	}

	private Elements getPromotionalPrice(Element priceElement) {
		return priceElement.select("font");
	}

}
