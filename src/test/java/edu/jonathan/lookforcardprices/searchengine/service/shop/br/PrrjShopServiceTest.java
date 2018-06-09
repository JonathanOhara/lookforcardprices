package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.Resources;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SampleConfiguration;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.ShopServiceBaseTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class PrrjShopServiceTest extends ShopServiceBaseTest {

    @InjectMocks
    PrRjShopService searchService = new PrRjShopService();

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
        return new Shop("PrRj", "https://www.prrjcards.com.br/");
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/prrj/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Monster reborn";
            }

            @Override
            public int expectedSize() {
                return 9;
            }

            @Override
            public int listIndexToAsserts() {
                return 3;
            }
        };
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForUnAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/prrj/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Monster reborn";
            }

            @Override
            public int expectedSize() {
                return 9;
            }

            @Override
            public int listIndexToAsserts() {
                return 0;
            }
        };
    }
}
