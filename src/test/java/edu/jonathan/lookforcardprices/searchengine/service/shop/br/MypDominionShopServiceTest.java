package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.Resources;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SampleConfiguration;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.ShopServiceBaseTest;
import org.mockito.InjectMocks;


public class MypDominionShopServiceTest extends ShopServiceBaseTest {

    @InjectMocks
    MypDominionShopService searchService = new MypDominionShopService();

    @Override
    protected SearchService getSearchService(){
        return searchService;
    }

    @Override
    protected Shop getCurrentShop() {
        return new Shop("Myp Cards", "https://mypcards.com/");
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/mypdominion/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Monster Reborn";
            }

            @Override
            public int expectedSize() {
                return 20;
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
                return Resources.getContentFromResourceFile("/samples/br/mypdominion/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "Monster Reborn";
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
