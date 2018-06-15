package edu.jonathan.lookforcardprices.searchengine.service.shop.eu;

import edu.jonathan.lookforcardprices.Resources;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SampleConfiguration;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.ShopServiceBaseTest;
import edu.jonathan.lookforcardprices.searchengine.service.shop.en.CoolAndStuffShopService;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.IOException;


public class CardMarketShopServiceTest extends ShopServiceBaseTest {

    @InjectMocks
    CardMarketShopService searchService = new CardMarketShopService();

    @Override
    protected SearchService getSearchService(){
        return searchService;
    }

    @Override
    protected Shop getCurrentShop() {
        return new Shop("Card Market", "https://www.cardmarket.com/en/YuGiOh/");
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/cardmarket/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Linkuriboh";
            }

            @Override
            public int expectedSize() {
                return 5;
            }

            @Override
            public int listIndexToAsserts() {
                return 1;
            }
        };
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForUnAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/cardmarket/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Linkuriboh";
            }

            @Override
            public int expectedSize() {
                return 5;
            }

            @Override
            public int listIndexToAsserts() {
                return 0;
            }
        };
    }
}
