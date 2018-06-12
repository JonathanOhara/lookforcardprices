package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.comom.MoneyUtil;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.javamoney.moneta.Money;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;

//https://lista.mercadolivre.com.br/invoked-raidjin
//https://lista.mercadolivre.com.br/brinquedos-hobbies/cards-card-games/card-game/yu-gi-oh/reviver-monstro#D[A:reviver-monstro,OC:MLB6901]
public class MercadoLivreShopService extends SearchService {

	private int resultsPerPage = 12;
	private boolean searchInYuGiOh = false;

	@Override
	protected boolean isProductAvailable(Element productContainer) {
		return true;
	}

	@Override
	protected String getSearchUrlSample(String mainUrl) {
		String urlText = "https://lista.mercadolivre.com.br/";
		if( searchInYuGiOh ){
			urlText += 	"brinquedos-hobbies/cards-card-games/card-game/yu-gi-oh/";
		}

		return urlText + URL_SEARCH_SAMPLE;
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
		return ResultNameFilter.ignoreCaseContains();
	}

	@Override
	protected ResultPageSelectors getResultPageSelectors() {
		return new ResultPageSelectors() {
			@Override
			public String singleProduct() {
				return "#searchResults > li";
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

	@Override
	protected Money getPriceFrom(String formattedValue) {
		String[] parts = formattedValue.split(" ");

		String realPart = parts[1];
		String centsPart  = parts.length > 2 ? parts[2] : "00";

		Money amount = Money.of(Double.parseDouble(realPart + "." + centsPart), getCurrency());

		return amount;
	}
}
