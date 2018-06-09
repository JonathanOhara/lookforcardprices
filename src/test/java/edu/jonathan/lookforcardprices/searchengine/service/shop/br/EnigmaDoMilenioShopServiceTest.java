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


public class EnigmaDoMilenioShopServiceTest extends ShopServiceBaseTest {

    @InjectMocks
    EnigmaDoMilenioShopService searchService = new EnigmaDoMilenioShopService();

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
        return new Shop("Enigma do Milenio", "http://www.enigmadomilenio.com.br/");
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/enigmadomilenio/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Nekroz";
            }

            @Override
            public int expectedSize() {
                return 86;
            }

            @Override
            public int listIndexToAsserts() {
                return 19;
            }
        };
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForUnAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/enigmadomilenio/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Nekroz";
            }

            @Override
            public int expectedSize() {
                return 86;
            }

            @Override
            public int listIndexToAsserts() {
                return 0;
            }
        };
    }
}
