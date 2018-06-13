package edu.jonathan.lookforcardprices.searchengine.controller;

import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.service.SearchEngine;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchController {

    @Inject private SearchEngine searchEngine;

    public Map<String, Set<Product>> run(List<String> searchList) throws IOException {
        searchEngine.registerAllShops();
        searchEngine.registerProducts(searchList);

        return searchEngine.run(false);
    }
}
