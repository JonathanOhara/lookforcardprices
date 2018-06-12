package edu.jonathan.lookforcardprices;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.controller.ReportController;
import edu.jonathan.lookforcardprices.searchengine.controller.SearchController;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;


public class Main {

	/**
	 * TODO: dont let the product duplicate(check url)
	 * TODO: Thymeleaf instead builder with html
	 * TODO: web pages with all available products
	 * TODO: Cool n Stuff add card state
	 *
	 * IDEA: card state
	 */

	public static void main(String[] args) throws IOException, URISyntaxException {
		SeContainerInitializer cdiInitializer = SeContainerInitializer.newInstance();
		try (SeContainer container = cdiInitializer.initialize()) {

			SearchController searchController = container.select(SearchController.class).get();
			Map<String, List<Product>> results = searchController.run(createSearchListFromFile("/src/main/resources/searchList.txt"));

			ReportController reportController = container.select(ReportController.class).get();
			reportController.generateAllReports(results);
		}
	}

	private static List<String> createSearchListFromFile(String filePath) throws IOException {
		String productListFilePath = Util.getProjectPath() + filePath;
		File productListFile = new File( productListFilePath );
		return Util.read(productListFile);
	}


}
