package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SampleConfiguration;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.ShopServiceBaseTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


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
                String filePath = getClass().getResource("/samples/br/mypdominion/01.html").getPath();
                String mockContent = null;
                try {
                    mockContent = new String(Files.readAllBytes(Paths.get( filePath ) ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return mockContent;
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
                String filePath = getClass().getResource("/samples/br/mypdominion/01.html").getPath();
                String mockContent = null;
                try {
                    mockContent = new String(Files.readAllBytes(Paths.get( filePath ) ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return mockContent;
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
