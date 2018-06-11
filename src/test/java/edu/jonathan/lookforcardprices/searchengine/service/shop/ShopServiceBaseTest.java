package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.CDIExtension;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.UrlReaderService;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

@ExtendWith({MockitoExtension.class, CDIExtension.class})
@RunWith(JUnitPlatform.class)
public abstract class ShopServiceBaseTest  {

    SearchService searchService = null;

    @Mock
    UrlReaderService urlReaderService;

    protected static final Logger logger = Logger.getLogger(ShopServiceBaseTest.class);

    Shop currentShop;

    @BeforeAll
    static void setup() {

    }

    @BeforeEach
    void init() {
        currentShop = getCurrentShop();
        searchService = getSearchService();
    }

    @Test
    public void testAvailableProducts() throws IOException {
        SampleConfiguration sampleConfiguration = getSampleConfigurationForAvailableProducts();

        Mockito.when( urlReaderService.readUrlDocument( Mockito.anyString() ) ).thenReturn( Jsoup.parse( sampleConfiguration.getContent()  ) );

        List<Product> products = searchService.run(currentShop, sampleConfiguration.getSearchedTerm());

        Assertions.assertEquals(sampleConfiguration.expectedSize(), products.size());

        Product availableProduct = products.get(sampleConfiguration.listIndexToAsserts());

        logger.trace("Testing Available Product: "+availableProduct);

        Assertions.assertFalse(availableProduct.getName().isEmpty());
        Assertions.assertTrue(availableProduct.isAvailable());

        Assertions.assertNotNull(availableProduct.getFormattedPrice().isEmpty());
        Assertions.assertFalse(availableProduct.getFormattedPrice().isEmpty());

        Assertions.assertNotNull(availableProduct.getUrl());
        Assertions.assertFalse(availableProduct.getUrl().isEmpty());
    }

    @Test
    public void testUnavailableProducts() throws IOException {
        testUnavailableProducts(false);
    }

    protected void testUnavailableProducts(boolean priceAvailable) throws IOException {
        SampleConfiguration sampleConfiguration = getSampleConfigurationForUnAvailableProducts();

        Mockito.when( urlReaderService.readUrlDocument( Mockito.anyString() ) ).thenReturn(Jsoup.parse( sampleConfiguration.getContent() ));

        List<Product> products = searchService.run(currentShop, sampleConfiguration.getSearchedTerm());

        Assertions.assertEquals(sampleConfiguration.expectedSize(), products.size());

        Product unavailableProduct = products.get(sampleConfiguration.listIndexToAsserts());

        logger.trace("Testing unavailable Product: "+unavailableProduct);

        Assertions.assertFalse(unavailableProduct.getName().isEmpty());
        Assertions.assertFalse(unavailableProduct.isAvailable());

        if(priceAvailable){
            Assertions.assertNotNull(unavailableProduct.getFormattedPrice().isEmpty());
            Assertions.assertFalse(unavailableProduct.getFormattedPrice().isEmpty());
        }else{
            Assertions.assertEquals(unavailableProduct.getFormattedPrice(),SearchService.PRODUCT_PRICE_NOT_AVAILABLE);
        }

        Assertions.assertNotNull(unavailableProduct.getUrl());
        Assertions.assertFalse(unavailableProduct.getUrl().isEmpty());
    }

    protected abstract Shop getCurrentShop();

    protected abstract SearchService getSearchService();

    protected abstract SampleConfiguration getSampleConfigurationForAvailableProducts();

    protected abstract SampleConfiguration getSampleConfigurationForUnAvailableProducts();
}
