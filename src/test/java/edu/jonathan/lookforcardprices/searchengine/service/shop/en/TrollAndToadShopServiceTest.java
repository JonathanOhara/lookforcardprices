package edu.jonathan.lookforcardprices.searchengine.service.shop.en;

import edu.jonathan.lookforcardprices.Resources;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SampleConfiguration;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.ShopServiceBaseTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.IOException;


public class TrollAndToadShopServiceTest extends ShopServiceBaseTest {

    @InjectMocks
    TrollAndToadShopService searchService = new TrollAndToadShopService();

    @Override
    @Test
    public void testUnavailableProducts() throws IOException {
        testUnavailableProducts(true);
    }

    @Override
    protected SearchService getSearchService(){
        return searchService;
    }

    @Override
    protected Shop getCurrentShop() {
        return new Shop("Troll and Toad", "https://www.trollandtoad.com/");
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/trollandtoad/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Nekroz of Trishula";
            }

            @Override
            public int expectedSize() {
                return 3;
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
                return Resources.getContentFromResourceFile("/samples/br/trollandtoad/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Nekroz of Trishula";
            }

            @Override
            public int expectedSize() {
                return 3;
            }

            @Override
            public int listIndexToAsserts() {
                return 2;
            }
        };
    }
}
