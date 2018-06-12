package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.Resources;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SampleConfiguration;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.ShopServiceBaseTest;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;


public class LigaMagicSearchPageShopServiceTest extends ShopServiceBaseTest {

    @InjectMocks
    protected LigaMagicShopService searchService = new LigaMagicShopService();


    @Test
    public void testAvailableProducts() throws IOException {
        SampleConfiguration sampleConfiguration = getSampleConfigurationForAvailableProducts();

        Document mockedDocument = new Document(searchService.getSearchUrlSample(currentShop.getMainUrl()));
        mockedDocument.append( sampleConfiguration.getContent() );

        Mockito.when( urlReaderService.readUrlDocument( Mockito.anyString() ) ).thenReturn( mockedDocument );

        List<Product> products = searchService.run(currentShop, sampleConfiguration.getSearchedTerm());

        Assertions.assertEquals(sampleConfiguration.expectedSize(), products.size());

        Product availableProduct = products.get(sampleConfiguration.listIndexToAsserts());

        logger.info("Testing Available Product: "+availableProduct);
        logger.info("Price: "+availableProduct.getProductPrice());

        Assertions.assertNotNull(availableProduct.getUrl());
        Assertions.assertFalse(availableProduct.getUrl().isEmpty());
    }

    @Test
    public void testUnavailableProducts() throws IOException {
        testUnavailableProducts(false);
    }

    protected void testUnavailableProducts(boolean priceAvailable) throws IOException {
        SampleConfiguration sampleConfiguration = getSampleConfigurationForUnAvailableProducts();

        Document mockedDocument = new Document(searchService.getSearchUrlSample(currentShop.getMainUrl()));
        mockedDocument.append( sampleConfiguration.getContent() );

        Mockito.when( urlReaderService.readUrlDocument( Mockito.anyString() ) ).thenReturn( mockedDocument );

        List<Product> products = searchService.run(currentShop, sampleConfiguration.getSearchedTerm());

        Assertions.assertEquals(sampleConfiguration.expectedSize(), products.size());

        Product unavailableProduct = products.get(sampleConfiguration.listIndexToAsserts());

        logger.info("Testing unavailable Product: "+unavailableProduct);
        logger.info("Price: "+unavailableProduct.getProductPrice());

        Assertions.assertFalse(unavailableProduct.getName().isEmpty());

        Assertions.assertNotNull(unavailableProduct.getUrl());
        Assertions.assertFalse(unavailableProduct.getUrl().isEmpty());
    }

    @Override
    protected SearchService getSearchService(){
        return searchService;
    }

    @Override
    protected Shop getCurrentShop() {
        return new Shop("Toguro", "https://www.lojadotoguro.com.br/");
    }

    @Override
    protected SampleConfiguration getSampleConfigurationForAvailableProducts() {
        return new SampleConfiguration() {
            @Override
            public String getContent() {
                return Resources.getContentFromResourceFile("/samples/br/ligamagic/toguro_search_page.html");
            }

            @Override
            public String getSearchedTerm() {
                return "nekroz";
            }

            @Override
            public int expectedSize() {
                return 19;
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
                return Resources.getContentFromResourceFile("/samples/br/ligamagic/toguro_search_page.html");
            }

            @Override
            public String getSearchedTerm() {
                return "nekroz";
            }

            @Override
            public int expectedSize() {
                return 19;
            }

            @Override
            public int listIndexToAsserts() {
                return 4;
            }
        };
    }
}
