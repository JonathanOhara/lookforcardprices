package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.Resources;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SampleConfiguration;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.ShopServiceBaseTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.IOException;


public class LegionCGShopServiceTest extends ShopServiceBaseTest {

    @InjectMocks
    LegionCGShopService searchService = new LegionCGShopService();

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
        return new Shop("Legion CG", "https://www.legioncg.com.br/");
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/legioncg/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Trickstar Candina";
            }

            @Override
            public int expectedSize() {
                return 2;
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
                return Resources.getContentFromResourceFile("/samples/br/legioncg/02.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Mirror force";
            }

            @Override
            public int expectedSize() {
                return 34;
            }

            @Override
            public int listIndexToAsserts() {
                return 0;
            }
        };
    }
}
