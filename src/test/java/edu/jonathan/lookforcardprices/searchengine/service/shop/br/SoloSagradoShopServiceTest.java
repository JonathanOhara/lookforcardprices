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


public class SoloSagradoShopServiceTest extends ShopServiceBaseTest {

    @InjectMocks
    SoloSagradoShopService searchService = new SoloSagradoShopService();

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
                return Resources.getContentFromResourceFile("/samples/br/solosagrado/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Nekroz";
            }

            @Override
            public int expectedSize() {
                return 12;
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
                return Resources.getContentFromResourceFile("/samples/br/solosagrado/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Nekroz";
            }

            @Override
            public int expectedSize() {
                return 12;
            }

            @Override
            public int listIndexToAsserts() {
                return 10;
            }
        };
    }
}
