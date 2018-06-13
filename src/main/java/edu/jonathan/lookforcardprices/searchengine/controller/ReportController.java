package edu.jonathan.lookforcardprices.searchengine.controller;

import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.service.TotalsReportService;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


public class ReportController {

    @Inject private TotalsReportService totalsReport;

    public void generateAllReports(Map<String, Set<Product>> results) throws IOException, URISyntaxException {
        for (Map.Entry<String, Set<Product>> result: results.entrySet()) {
            totalsReport.generateReportProductsByShop(result.getKey(), new ArrayList<>(result.getValue()));
        }
    }

}
