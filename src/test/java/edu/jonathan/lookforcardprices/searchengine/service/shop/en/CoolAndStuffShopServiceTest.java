package edu.jonathan.lookforcardprices.searchengine.service.shop.en;

import edu.jonathan.lookforcardprices.Resources;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SampleConfiguration;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.ShopServiceBaseTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.IOException;


public class CoolAndStuffShopServiceTest extends ShopServiceBaseTest {

    @InjectMocks
    CoolAndStuffShopService searchService = new CoolAndStuffShopService();

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
        return new Shop("Cool And Stuff", "https://www.coolstuffinc.com/");
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/coolandstuff/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "mirror force";
            }

            @Override
            public int expectedSize() {
                return 40;
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
                return Resources.getContentFromResourceFile("/samples/br/coolandstuff/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "mirror force";
            }

            @Override
            public int expectedSize() {
                return 40;
            }

            @Override
            public int listIndexToAsserts() {
                return 3;
            }
        };
    }
}
