package edu.jonathan.lookforcardprices.searchengine.service.shop.br;

import edu.jonathan.lookforcardprices.CDIExtension;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.UrlReaderService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@ExtendWith({MockitoExtension.class, CDIExtension.class})
@RunWith(JUnitPlatform.class)
public class CHQShopServiceTest {

    @InjectMocks
    CHQShopService searchService = new CHQShopService();

    @Mock
    UrlReaderService urlReaderService;

    Shop currentShop;

    @BeforeAll
    static void setup() {
        System.out.println("@BeforeAll - executes once before all test methods in this class");
    }

    @BeforeEach
    void init() {
        System.out.println("@BeforeEach - executes before each test method in this class");
        currentShop = new Shop("CHQ", "https://www.chq.com.br/");
    }

    @Test
    public void testAvaliableProducts() throws IOException {
        String filePath = getClass().getResource("/samples/br/chq/01.html").getPath();
        String mockContent = new String(Files.readAllBytes(Paths.get( filePath ) ));
        Mockito.when( urlReaderService.readUrlDocument( Mockito.anyString() ) ).thenReturn(Jsoup.parse(mockContent));

        List<Product> products = searchService.run(currentShop, "mirror force");

        Assertions.assertEquals(48, products.size());

        Product availableProduct = products.get(0);
        Assertions.assertFalse(availableProduct.getName().isEmpty());
        Assertions.assertTrue(availableProduct.isAvaliable());

        Assertions.assertNotNull(availableProduct.getFormattedPrice().isEmpty());
        Assertions.assertFalse(availableProduct.getFormattedPrice().isEmpty());

        Assertions.assertNotNull(availableProduct.getUrl());
        Assertions.assertFalse(availableProduct.getUrl().isEmpty());
    }

    @Test
    public void testUnavaliableProducts() throws IOException {
        String filePath = getClass().getResource("/samples/br/chq/01.html").getPath();
        String mockContent = new String(Files.readAllBytes(Paths.get( filePath ) ));
        Mockito.when( urlReaderService.readUrlDocument( Mockito.anyString() ) ).thenReturn(Jsoup.parse(mockContent));

        List<Product> products = searchService.run(currentShop, "mirror force");

        Assertions.assertEquals(48, products.size());

        Product unavailableProductWithoutPrice = products.get(8);
        Assertions.assertFalse(unavailableProductWithoutPrice.getName().isEmpty());
        Assertions.assertFalse(unavailableProductWithoutPrice.isAvaliable());

        Assertions.assertEquals(unavailableProductWithoutPrice.getFormattedPrice(), SearchService.PRODUCT_PRICE_NOT_AVAILABLE);

        Assertions.assertNotNull(unavailableProductWithoutPrice.getUrl());
        Assertions.assertFalse(unavailableProductWithoutPrice.getUrl().isEmpty());
    }
}
