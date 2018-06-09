package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.Resources;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SampleConfiguration;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.ShopServiceBaseTest;
import org.junit.Ignore;
import org.mockito.InjectMocks;

import java.io.IOException;


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
                return Resources.getContentFromResourceFile("/samples/br/mercadolivre/01.html");
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
                return Resources.getContentFromResourceFile("/samples/br/mercadolivre/01.html");
            }

            @Override
            public String getSearchedTerm() {
                return "mirror force";
            }

            @Override
            public int expectedSize() {
                return 13;
            }

            @Override
            public int listIndexToAsserts() {
                return 12;
            }
        };
    }
}
