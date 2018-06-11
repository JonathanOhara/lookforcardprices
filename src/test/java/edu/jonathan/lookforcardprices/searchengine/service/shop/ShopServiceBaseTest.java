package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.CDIExtension;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.ProductPrice;
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

        logger.info("Testing Available Product: "+availableProduct);
        logger.info("Price: "+availableProduct.getProductPrice());

        Assertions.assertFalse(availableProduct.getName().isEmpty());
        Assertions.assertTrue(availableProduct.isAvailable());

        Assertions.assertNotNull(availableProduct.getUrl());
        Assertions.assertFalse(availableProduct.getUrl().isEmpty());

        ProductPrice productPrice = availableProduct.getProductPrice().get();

        Assertions.assertNotNull(productPrice.getFormattedPrice());
        Assertions.assertFalse(productPrice.getFormattedPrice().isEmpty());
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

        logger.info("Testing unavailable Product: "+unavailableProduct);
        logger.info("Price: "+unavailableProduct.getProductPrice());

        Assertions.assertFalse(unavailableProduct.getName().isEmpty());
        Assertions.assertFalse(unavailableProduct.isAvailable());

        Assertions.assertNotNull(unavailableProduct.getUrl());
        Assertions.assertFalse(unavailableProduct.getUrl().isEmpty());

        ProductPrice productPrice = unavailableProduct.getProductPrice().get();

        if(priceAvailable){
            Assertions.assertNotNull(productPrice.getFormattedPrice());
            Assertions.assertFalse(productPrice.getFormattedPrice().isEmpty());
        }else{
            Assertions.assertEquals(productPrice.getFormattedPrice(),SearchService.PRODUCT_PRICE_NOT_AVAILABLE);
        }
    }

    protected abstract Shop getCurrentShop();

    protected abstract SearchService getSearchService();

    protected abstract SampleConfiguration getSampleConfigurationForAvailableProducts();

    protected abstract SampleConfiguration getSampleConfigurationForUnAvailableProducts();
}
