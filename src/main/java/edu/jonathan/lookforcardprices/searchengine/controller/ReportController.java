package edu.jonathan.lookforcardprices.searchengine.controller;

import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.service.TotalsReportService;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;


public class ReportController {

    @Inject private TotalsReportService totalsReport;

    public void generateAllReports(Map<String, List<Product>> results) throws IOException, URISyntaxException {
        for (Map.Entry<String, List<Product>> result: results.entrySet()) {
            totalsReport.generateReportProductsByShop(result.getKey(), result.getValue());
        }
    }

}
