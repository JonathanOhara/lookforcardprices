package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.Resources;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SampleConfiguration;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.ShopServiceBaseTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.IOException;


public class MTGShopServiceTest extends ShopServiceBaseTest {

    @InjectMocks
    MTGCardGamesShopService searchService = new MTGCardGamesShopService();

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
        return new Shop("MTG Cards", "https://www.mtgcards.com.br/");
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/mtg/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Nekroz";
            }

            @Override
            public int expectedSize() {
                return 15;
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
                return Resources.getContentFromResourceFile("/samples/br/mtg/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Nekroz";
            }

            @Override
            public int expectedSize() {
                return 15;
            }

            @Override
            public int listIndexToAsserts() {
                return 6;
            }
        };
    }
}
