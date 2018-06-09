package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.Resources;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SampleConfiguration;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.ShopServiceBaseTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.IOException;


public class ColyseumShopServiceTest extends ShopServiceBaseTest {

    @InjectMocks
    ColyseumShopService searchService = new ColyseumShopService();

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
        return new Shop("Colyseum", "https://www.colyseum.com/");
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/colyseum/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "mirror force";
            }

            @Override
            public int expectedSize() {
                return 20;
            }

            @Override
            public int listIndexToAsserts() {
                return 6;
            }
        };
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForUnAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/colyseum/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "mirror force";
            }

            @Override
            public int expectedSize() {
                return 20;
            }

            @Override
            public int listIndexToAsserts() {
                return 0;
            }
        };
    }
}
