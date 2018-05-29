package edu.jonathan.lookforcardprices.searchengine.service.shop.en;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.ResultPageSelectors;
import edu.jonathan.lookforcardprices.searchengine.service.filter.ResultNameFilter;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//https://www.trollandtoad.com/products/search.php?search_category=&search_words=mirror&searchmode=basic
public class TrollAndToadShopService extends SearchService {

	private int resultsPerPage = 25;

	@Override
    protected boolean isProductAvailable(Element productContainer) {
        return productContainer.select("a.notify").size() == 0;
    }

    @Override
	protected String getSearchUrlSample(String mainUrl) {
		return mainUrl + "products/search.php?search_category=&searchmode=basic" +
//				"&resultsPerPage=" + resultsPerPage  +
				"&search_words=" + URL_SEARCH_SAMPLE;
	}

    @Override
    protected List<Product> readProductsData(Elements els, ResultPageSelectors selectors, Shop shop, String productName, URL resultsPageURL, ResultNameFilter resultNameFilter) {
        List<Product> products = new ArrayList<>(els.size());

        String previewName;

        for( Element productContainer : els ){
            previewName = Optional.ofNullable(selectors.productName()).map(s -> productContainer.select(s).text()).orElse(productName);

            System.out.println("\t\tPreview name: "+previewName);

            if( resultNameFilter.isValid(previewName, productName) ){

                for( Element productRow : productContainer.select(".search_result_conditions tr") ) {
                    getProductList(selectors, shop, resultsPageURL, products, previewName, productRow);
                }
            }else{
                System.out.println("\t\tRemoved by name filter...");
            }
        }

        return products;
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
		return ResultNameFilter.contains();
	}

	@Override
	protected ResultPageSelectors getResultPageSelectors() {
		return new ResultPageSelectors() {
			@Override
			public String singleProduct() {
				return ".search_result_wrapper";
			}

			@Override
			public String productName() {
				return "h2";
			}

			@Override
			public String productImageURL() {
				return "img:eq(0)";
			}

			@Override
			public String productPrice() {
				return ".price_text";
			}
		};
	}

	@Override
	public boolean hasPortugueseOption() {
		return false;
	}
}
