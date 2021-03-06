package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.Resources;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SampleConfiguration;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.ShopServiceBaseTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.IOException;


public class LigaMagicProductPageShopServiceTest extends ShopServiceBaseTest {

    @InjectMocks
    LigaMagicShopService searchService = new LigaMagicShopService();

    @Override
    @Test
    public void testUnavailableProducts() throws IOException {
        testUnavailableProducts(false);
    }

    @Override
    protected SearchService getSearchService(){
        return searchService;
    }

    @Override
    protected Shop getCurrentShop() {
        return new Shop("Toguro", "https://www.lojadotoguro.com.br/");
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/ligamagic/toguro_product_page.html");
            }

            @Override
            public String getSearchedTerm() {
                return "mirror force";
            }

            @Override
            public int expectedSize() {
                return 21;
            }

            @Override
            public int listIndexToAsserts() {
                return 0;
            }
        };
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForUnAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/ligamagic/toguro_product_page.html");
            }

            @Override
            public String getSearchedTerm() {
                return "mirror force";
            }

            @Override
            public int expectedSize() {
                return 21;
            }

            @Override
            public int listIndexToAsserts() {
                return 20;
            }
        };
    }
}
