package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.CDIExtension;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.ProductPrice;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.UrlReaderService;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.apache.log4j.Logger;
import org.javamoney.moneta.Money;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import java.util.Optional;

@ExtendWith({MockitoExtension.class, CDIExtension.class})
@RunWith(JUnitPlatform.class)
public abstract class ShopServiceBaseTest  {

    protected SearchService searchService = null;

    @Mock
    protected UrlReaderService urlReaderService;

    protected static final Logger logger = Logger.getLogger(ShopServiceBaseTest.class);

    protected Shop currentShop;

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

        Document mockedDocument = new Document(searchService.getSearchUrlSample(currentShop.getMainUrl()));
        mockedDocument.append( sampleConfiguration.getContent() );

        Mockito.when( urlReaderService.readUrlDocument( Mockito.anyString() ) ).thenReturn( mockedDocument );

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

        logger.info("Product Price: "+productPrice);

        Assertions.assertNotNull(productPrice.getFormattedPrice());
        Assertions.assertFalse(productPrice.getFormattedPrice().isEmpty());
        Assertions.assertTrue(productPrice.getAmount().isGreaterThan(Money.of(0, searchService.getCurrency())));
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
        Assertions.assertFalse(unavailableProduct.isAvailable());

        Assertions.assertNotNull(unavailableProduct.getUrl());
        Assertions.assertFalse(unavailableProduct.getUrl().isEmpty());

        Optional<ProductPrice> productPrice = unavailableProduct.getProductPrice();

        if(priceAvailable){
            logger.info("Product Price: "+productPrice.get());
            Assertions.assertNotNull(productPrice.get().getFormattedPrice());
            Assertions.assertFalse(productPrice.get().getFormattedPrice().isEmpty());
            Assertions.assertTrue(productPrice.get().getAmount().isGreaterThan(Money.of(0, searchService.getCurrency())));
        }else{
            Assertions.assertEquals(productPrice,Optional.empty());
        }
    }

    protected abstract Shop getCurrentShop();

    protected abstract SearchService getSearchService();

    protected abstract SampleConfiguration getSampleConfigurationForAvailableProducts();

    protected abstract SampleConfiguration getSampleConfigurationForUnAvailableProducts();
}
