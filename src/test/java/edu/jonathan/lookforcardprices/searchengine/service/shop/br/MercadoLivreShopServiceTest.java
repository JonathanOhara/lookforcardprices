package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SampleConfiguration;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.ShopServiceBaseTest;
import org.junit.Ignore;
import org.mockito.InjectMocks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class MercadoLivreShopServiceTest extends ShopServiceBaseTest {

    @InjectMocks
    MercadoLivreShopService searchService = new MercadoLivreShopService();

    //Mercado Livre don't search unavailable products
    @Override
    @Ignore
    public void testUnavailableProducts() throws IOException {}

    @Override
    protected SearchService getSearchService(){
        return searchService;
    }

    @Override
    protected Shop getCurrentShop() {
        return new Shop("Mercado Livre", "https://www.mercadolivre.com.br/");
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                String filePath = getClass().getResource("/samples/br/mercadolivre/01.html").getPath();
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
                return "reviver monstro";
            }

            @Override
            public int expectedSize() {
                return 13;
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
                String filePath = getClass().getResource("/samples/br/domain/01.html").getPath();
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
                return "mirror force";
            }

            @Override
            public int expectedSize() {
                return 36;
            }

            @Override
            public int listIndexToAsserts() {
                return 19;
            }
        };
    }
}
