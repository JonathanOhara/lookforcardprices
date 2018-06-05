package edu.jonathan.lookforcardprices.searchengine.service.shop;

import edu.jonathan.lookforcardprices.CDIExtension;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.UrlReaderService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.br.CHQShopService;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

@ExtendWith({MockitoExtension.class, CDIExtension.class})
@RunWith(JUnitPlatform.class)
public abstract class ShopServiceBaseTest  {

    @InjectMocks
    CHQShopService searchService = new CHQShopService();

    @Mock
    UrlReaderService urlReaderService;

    Shop currentShop;

    @BeforeAll
    static void setup() {

    }

    @BeforeEach
    void init() {
        System.out.println("@BeforeEach - executes before each test method in this class");
        currentShop = getCurrentShop();
    }

    @Test
    public void testAvailableProducts() throws IOException {
        SampleConfiguration sampleConfiguration = getSampleConfigurationForAvailableProducts();

        Mockito.when( urlReaderService.readUrlDocument( Mockito.anyString() ) ).thenReturn( Jsoup.parse( sampleConfiguration.getContent()  ) );

        List<Product> products = searchService.run(currentShop, sampleConfiguration.getSearchedTerm());

        Assertions.assertEquals(sampleConfiguration.expectedSize(), products.size());

        Product availableProduct = products.get(sampleConfiguration.listIndexToAsserts());
        Assertions.assertFalse(availableProduct.getName().isEmpty());
        Assertions.assertTrue(availableProduct.isAvaliable());

        Assertions.assertNotNull(availableProduct.getFormattedPrice().isEmpty());
        Assertions.assertFalse(availableProduct.getFormattedPrice().isEmpty());

        Assertions.assertNotNull(availableProduct.getUrl());
        Assertions.assertFalse(availableProduct.getUrl().isEmpty());
    }

    @Test
    public void testUnavailableProducts() throws IOException {

        SampleConfiguration sampleConfiguration = getSampleConfigurationForUnAvailableProducts();
        Mockito.when( urlReaderService.readUrlDocument( Mockito.anyString() ) ).thenReturn(Jsoup.parse( sampleConfiguration.getContent() ));

        List<Product> products = searchService.run(currentShop, sampleConfiguration.getSearchedTerm());

        Assertions.assertEquals(sampleConfiguration.expectedSize(), products.size());

        Product unavailableProductWithoutPrice = products.get(sampleConfiguration.listIndexToAsserts());
        Assertions.assertFalse(unavailableProductWithoutPrice.getName().isEmpty());
        Assertions.assertFalse(unavailableProductWithoutPrice.isAvaliable());

        Assertions.assertEquals(unavailableProductWithoutPrice.getFormattedPrice(), SearchService.PRODUCT_PRICE_NOT_AVAILABLE);

        Assertions.assertNotNull(unavailableProductWithoutPrice.getUrl());
        Assertions.assertFalse(unavailableProductWithoutPrice.getUrl().isEmpty());
    }

    protected abstract Shop getCurrentShop();

    protected abstract SampleConfiguration getSampleConfigurationForAvailableProducts();

    protected abstract SampleConfiguration getSampleConfigurationForUnAvailableProducts();
}
