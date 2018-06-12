package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.comom.MoneyUtil;
import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.javamoney.moneta.Money;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;

//http://www.domaingames.com.br/Ajax_Funcoes.asp?IsNovoCfg=true&ID_Categoria=&busca=Dark%20Renewal&Pagina=1&OrganizarPor=&ResultadoPorPagina=&Funcao=BuscaAvancada
public class DomainShopService extends SearchService {

	private int resultsPerPage = 12;

    @Override
    protected boolean isProductAvailable(Element productContainer) {
        return !productContainer.select(".estoque").text().trim().isEmpty();
    }

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "Ajax_Funcoes.asp?" +
				"IsNovoCfg=true" +
				"&Pagina=1" +
				"&OrganizarPor=3" +
				"&Funcao=BuscaAvancada" +
				"&ResultadoPorPagina=" + resultsPerPage  +
				"&busca=" + URL_SEARCH_SAMPLE;
	}

	@Override
	protected void setMaxResultsPerPage() {
		resultsPerPage = 21;
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
				return "#ListadeProdutosAvancada li";
			}

			@Override
			public String productName() {
				return ".card_name .portugues a";
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			}

			@Override
			public String productPrice() {
				return ".vista";
			}
		};
	}

	@Override
	protected Money getPriceFrom(String formattedValue) {
		Matcher matcher = MoneyUtil.MONEY_PATTERN.matcher(formattedValue.substring(2).trim());

		matcher.matches();

		Money amount = Money.of(Double.parseDouble(matcher.group(1) + "." + matcher.group(2)), getCurrency());

		return amount;
	}
}
