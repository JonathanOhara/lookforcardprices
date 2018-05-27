package edu.jonathan.lookforcardprices.searchengine.service;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import java.io.IOException;
import java.util.*;


@ApplicationScoped
public class SearchEngine {

    private Map<Shop, SearchService> shopsService;
    private List<String> searchList;

    public SearchEngine() {
        shopsService = new LinkedHashMap<>();
        searchList = new ArrayList<>();
    }

    public void registerBrazilianShops(){
        register(new Shop("Domain", "http://www.domaingames.com.br/"), CDI.current().select(DomainShopService.class).get() );
        register(new Shop("Duel Shop", "https://www.duelshop.com.br/"), CDI.current().select(DuelShopService.class).get() );
        register(new Shop("PrRj", "https://www.prrjcards.com.br/"), CDI.current().select(PrRjShopService.class).get() );
        register(new Shop("Solo Sagrado", "https://www.solosagrado.com.br/"), CDI.current().select(SoloSagradoShopService.class).get() );
        register(new Shop("Toguro", "https://www.lojadotoguro.com.br/"), CDI.current().select(LigaMagicShopService.class).get() );
        register(new Shop("Fenix House TCG", "https://www.fenixhousetcg.com.br/"), CDI.current().select(LigaMagicShopService.class).get() );
        register(new Shop("Central Geek", "https://www.lojacentralgeek.com.br/"), CDI.current().select(LigaMagicShopService.class).get() );
        register(new Shop("Hot yugioh", "https://www.hotyugioh.com.br/"), CDI.current().select(LigaMagicShopService.class).get() );
        register(new Shop("CHQ", "https://www.chq.com.br/"), CDI.current().select(CHQShopService.class).get() );
        register(new Shop("MTG Cards", "https://www.mtgcards.com.br/"), CDI.current().select(MTGCardGamesShopService.class).get() );
        register(new Shop("Legion CG", "https://www.legioncg.com.br/"), CDI.current().select(LegionCGShopService.class).get() );
        register(new Shop("Myp Cards", "https://mypcards.com/"), CDI.current().select(MypDominionShopService.class).get() );
        register(new Shop("DMG", "http://www.dmgcardshop.com/"), CDI.current().select(DmgShopService.class).get() );
        register(new Shop("Enigma do Milenio", "http://www.enigmadomilenio.com.br/"), CDI.current().select(EnigmaDoMilenioShopService.class).get() );
        register(new Shop("colyseum", "https://www.colyseum.com/"), CDI.current().select(ColyseumShopService.class).get() );
        register(new Shop("Mercado Livre", "https://www.mercadolivre.com.br/"), CDI.current().select(MercadoLivreShopService.class).get() );
    }

    public void registerUSAShops(){
        register(new Shop("Cool And Stuff", "https://www.coolstuffinc.com/"), CDI.current().select(CoolAndStuffShopService.class).get() );
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
        registerUSAShops();
        registerBrazilianShops();
    }
}
