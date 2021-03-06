package edu.jonathan.lookforcardprices.searchengine.service;

import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.br.*;
import edu.jonathan.lookforcardprices.searchengine.service.shop.en.CoolAndStuffShopService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.en.TrollAndToadShopService;
import edu.jonathan.lookforcardprices.searchengine.service.shop.eu.CardMarketShopService;
import org.apache.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@ApplicationScoped
public class SearchEngine {

    private Map<Shop, SearchService> shopsServiceMapping;
    private List<String> searchList;

    protected static final Logger logger = Logger.getLogger(SearchEngine.class);

    public SearchEngine() {
        shopsServiceMapping = new LinkedHashMap<>();
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
        register(new Shop("Loja Ilusões", "https://www.lojailusoes.com.br/"), CDI.current().select(LigaMagicShopService.class).get() );
        register(new Shop("CHQ", "https://www.chq.com.br/"), CDI.current().select(CHQShopService.class).get() );
        register(new Shop("MTG Cards", "https://www.mtgcards.com.br/"), CDI.current().select(MTGCardGamesShopService.class).get() );
        register(new Shop("Legion CG", "https://www.legioncg.com.br/"), CDI.current().select(LegionCGShopService.class).get() );
        register(new Shop("Myp Cards", "https://mypcards.com/"), CDI.current().select(MypDominionShopService.class).get() );
        register(new Shop("DMG", "http://www.dmgcardshop.com/"), CDI.current().select(DmgShopService.class).get() );
        register(new Shop("Enigma do Milenio", "http://www.enigmadomilenio.com.br/"), CDI.current().select(EnigmaDoMilenioShopService.class).get() );
        register(new Shop("Colyseum", "https://www.colyseum.com/"), CDI.current().select(ColyseumShopService.class).get() );
        register(new Shop("Dimensional Cards", "https://www.dimensionalcards.com.br/"), CDI.current().select(DimensionalCardsShopService.class).get() );
        register(new Shop("Mercado Livre", "https://www.mercadolivre.com.br/"), CDI.current().select(MercadoLivreShopService.class).get() );

    }

    public void registerUSAShops(){
        register(new Shop("Cool And Stuff", "https://www.coolstuffinc.com/"), CDI.current().select(CoolAndStuffShopService.class).get() );
        register(new Shop("Troll and Toad", "https://www.trollandtoad.com/"), CDI.current().select(TrollAndToadShopService.class).get() );
    }

    public void registerEUShops() {
        register(new Shop("Card Market", "https://www.cardmarket.com/"), CDI.current().select(CardMarketShopService.class).get() );
    }

    public void register(Shop shop, SearchService shopImplementation){
        shopsServiceMapping.put(shop, shopImplementation);

    }

    public void registerProducts(List<String> searchList) {
        this.searchList = new ArrayList<>(searchList);
    }

    public Map<String, Set<Product>> run(boolean maxResultsPerPage) {
        Map<String, Set<Product>> productsByName = new ConcurrentHashMap<>(searchList.size());

        for(String productName : searchList ) {

            String[] productNames = productName.split("\\|");

            String productMainName = productNames[0];
            String otherName = productNames.length > 1 ? productNames[1] : null;

            logger.info("Product (main): "+productMainName);

            if(otherName != null)
                logger.info("Product (other): "+otherName);

            shopsServiceMapping.entrySet().parallelStream().forEach(entry -> {
                Shop shop = entry.getKey();
                SearchService searchService = entry.getValue();

                Set<Product> productsFounded = new LinkedHashSet<>();

                productsFounded.addAll( searchService.run(shop, productMainName, maxResultsPerPage) );

                if(otherName != null && searchService.hasPortugueseOption()) {
                    productsFounded.addAll(searchService.run(shop, otherName, maxResultsPerPage));
                }

                synchronized(this) {
                    Set<Product> productsAlreadyFound = Optional.ofNullable(productsByName.get(productMainName)).orElse(new LinkedHashSet<>());
                    productsFounded.addAll( productsAlreadyFound );
                    productsByName.put( productMainName, productsFounded );
                }
            });
        }

        return productsByName;
    }

    public static void main(String[] args) {
        String test = "olaola";

        String[] ar = test.split("\\|");
        System.out.println(ar[0]);
    }

    public void registerAllShops() {
        registerUSAShops();
        registerEUShops();
        registerBrazilianShops();
    }
}
