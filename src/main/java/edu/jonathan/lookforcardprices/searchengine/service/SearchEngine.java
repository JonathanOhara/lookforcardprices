package edu.jonathan.lookforcardprices.searchengine.service;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.DomainShopService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.DuelShopService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import java.io.IOException;
import java.util.*;


@ApplicationScoped
public class SearchEngine {

    private Map<Shop, SearchService> shopsService;
    private List<String> searchList;

    public SearchEngine() {
        shopsService = new HashMap<>();
        searchList = new ArrayList<>();
    }

    public void registerBrazilianShops(){
        register(new Shop("Domain", "http://www.domaingames.com.br/"), CDI.current().select(DomainShopService.class).get() );
        register(new Shop("DuelShop", "https://www.duelshop.com.br/"), CDI.current().select(DuelShopService.class).get() );
        //https://www.prrjcards.com.br/
        //https://www.solosagrado.com.br/
        //https://www.lojadotoguro.com.br/
        //http://www.mtgcards.com.br/
        //https://www.legioncg.com.br/
        //https://www.fenixhousetcg.com.br/
        //https://mypcards.com/Dominion
        //https://www.coolstuffinc.com/
        //https://www.mercadolivre.com.br/
    }

    public void registerUSAShops(){

    }

    public void register(Shop shop, SearchService shopImplementation){
        shopsService.put(shop, shopImplementation);

    }

    public void registerProducts(List<String> searchList) {
        this.searchList = new ArrayList<>(searchList);
    }

    public Map<String, List<Product>> run(boolean maxResultsPerPage) throws IOException {
        Map<String, List<Product>> productsByName = new HashMap<>(searchList.size());

        Shop shop;
        SearchService searchService;

        for(String productName : searchList ) {
            String productMainName = productName;
            String otherName = null;

            if( productName.contains("|")){
                String[] productNames = productName.split("\\|");
                productMainName = productNames[0];
                otherName = productNames[1];
            }

            Util.configureOutputToFileAndConsole(productMainName);

            for (Map.Entry<Shop, SearchService> entry : shopsService.entrySet()) {
                shop = entry.getKey();
                searchService = entry.getValue();

                List<Product> productsFounded = Optional.ofNullable(productsByName.get(productMainName)).orElse(new ArrayList<>());

                productsFounded.addAll( searchService.run(shop, productMainName, maxResultsPerPage) );

                if(otherName != null)
                    productsFounded.addAll( searchService.run(shop, otherName, maxResultsPerPage) );

                productsByName.put( productMainName, productsFounded );
            }
        }

        return productsByName;
    }

    public void registerAllShops() {
        registerBrazilianShops();
        registerUSAShops();
    }
}
