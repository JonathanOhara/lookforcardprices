package edu.jonathan.lookforcardprices.searchengine.service;

import edu.jonathan.lookforcardprices.comom.Util;
import edu.jonathan.lookforcardprices.searchengine.domain.Product;
import edu.jonathan.lookforcardprices.searchengine.domain.ProductPrice;
import edu.jonathan.lookforcardprices.searchengine.domain.Shop;
import edu.jonathan.lookforcardprices.searchengine.service.shop.SearchService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;


public class GamesReportService {

	private StringBuilder htmlFinal;
	private StringBuilder htmlReport;
	private StringBuilder htmlSeekers;
	private StringBuilder htmlMeta;
	private StringBuilder htmlLog;

	public GamesReportService(String nameToSearch) {
		htmlFinal = new StringBuilder();
		
		htmlFinal.append("<!DOCTYPE html>\n");
		htmlFinal.append("<html>\n");
		htmlFinal.append("<head>\n");
		htmlFinal.append("\t<script src='./").append(Util.HTML_IMPORT_FOLDER).append("/jquery.js'></script>\n");
		htmlFinal.append("\t<script src='./").append(Util.HTML_IMPORT_FOLDER).append("/scripts.js'></script>\n");
		htmlFinal.append("\t<link rel='stylesheet' type='text/css' href='./").append(Util.HTML_IMPORT_FOLDER).append("/css.css'>\n");
		htmlFinal.append("\t<title>").append(nameToSearch).append("</title>\n");
		htmlFinal.append("</head>\n");
		htmlFinal.append("<body>\n");
		
		htmlFinal.append("\t<section class='wrapper' >\n");
		htmlFinal.append("\t\t<h1>").append(nameToSearch).append("</h1>\n");
		htmlFinal.append("\t\t<h2>Check other tabs to more information</h2>\n");
		htmlFinal.append("\t\t<ul class='tabs'>\n");
		htmlFinal.append("\t\t\t<li><a href='#tab1'>Result</a></li>\n");
		htmlFinal.append("\t\t\t\t<li><a href='#tab2'>Search Engines</a></li>\n");
		htmlFinal.append("\t\t\t<li><a href='#tab3'>Meta Data</a></li>\n");
		htmlFinal.append("\t\t\t<li><a href='#tab4'>Log</a></li>\n");
		htmlFinal.append("\t\t</ul>\n");
		htmlFinal.append("\t\t<div class='clr'></div>\n");
		htmlFinal.append("\t\t<section class='block'>\n");	
		
		htmlReport = new StringBuilder();
	}
	
	public void addReport( Shop shop, List<Product> products){
		htmlReport.append("\t<table style='width: 100%;'>\n");

		if( products != null && products.size() > 0 ){

			htmlReport.append("\t\t<thead>\n");
			htmlReport.append("\t\t\t<tr>\n");
			htmlReport.append("\t\t\t<th colspan=2>\n");
			htmlReport.append("\t\t\t\t<a href='").append( products.get(0).getSearchedURL() ).append("'>\n");
			htmlReport.append("\t\t\t\t\tStore: ").append(shop.getName()).append("\n");
			htmlReport.append("\t\t\t\t</a>\n");
			htmlReport.append("\t\t\t</th>\n");
			htmlReport.append("\t\t\t\t</tr>\n");
			htmlReport.append("\t\t</thead>\n");

			htmlReport.append("\t\t<tbody>");

			for( Product product: products){
				htmlReport.append("\t\t\t<tr ").append(" class='").append(product.isAvailable() ? "available" : "unavailable").append("' >\n");
				htmlReport.append("\t\t\t\t<td style='width: 80%;'>\n");
				htmlReport.append("\t\t\t\t\t<a href='");
				htmlReport.append(product.getUrl());
				htmlReport.append("'>\n");
				htmlReport.append(product.getName());
				htmlReport.append("\t\t\t\t\t</a>\n");
				htmlReport.append("\t\t\t\t</td>\n");
				htmlReport.append("\t\t\t\t<td style='width: 20%;'>\n");
				if( !product.isAvailable() )htmlReport.append("<strike>");
				htmlReport.append( product.getProductPrice().map(ProductPrice::getFormattedPrice).orElse(SearchService.PRODUCT_PRICE_NOT_AVAILABLE) );
				if( !product.isAvailable() )htmlReport.append("</strike>");
				htmlReport.append("\n");
				htmlReport.append("\t\t\t\t</td>\n");
				htmlReport.append("\t\t\t</tr>\n");
			}
		}else{

			htmlReport.append("\t\t\t<tr>\n");
			htmlReport.append("\t\t\t\t<td colspan=2>\n");
			htmlReport.append("\t\t\t\t\tNo results\n");
			htmlReport.append("\t\t\t\t</td>\n");
			htmlReport.append("\t\t\t</tr>\n");
		}
		htmlReport.append("\t\t</tbody>\n");
		
		htmlReport.append("\t</table>\n");
		htmlReport.append("<br>\n");
	}
	
	public void closeAndWriteFile(String nameToSearch) throws IOException{
		
		insertReportsInHtml();
		insertSeekersInHtml();
		insertMetaInHtml();
		insertLogInHtml();
		insertBottomJavascript();
		
		htmlFinal.append("\t\t</section>\n");
		htmlFinal.append("\t</section>\n");
		htmlFinal.append("</body>\n");
		htmlFinal.append("</html>\n");
		
		File dir = new File(Util.getReportsPath()); 
		if (!dir.exists()) {
			dir.mkdir();
		}
		dir = new File(dir, Util.HTML_IMPORT_FOLDER);
		if (!dir.exists()){
			dir.mkdir();
		}
		
		Util.copyFolder( new File(Util.getProjectPath() + "/src/main/resources/htmlImports"), dir);
				
		File file = new File(Util.getReportsPath() + "/" + nameToSearch + ".html");
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(htmlFinal.toString());
		
		bw.flush();
		bw.close();
	}
	
	private void insertReportsInHtml() {
		htmlFinal.append("<article id='tab1'>\n");
		htmlFinal.append("<br>\n");
		htmlFinal.append( htmlReport.toString() );
		htmlFinal.append("</article>\n");
	}
	
	private void insertSeekersInHtml() {
		htmlFinal.append("<article id='tab2'>\n");
		htmlFinal.append("<br>\n");
		htmlFinal.append( htmlSeekers.toString() );
		htmlFinal.append("</article>\n");
	}
	
	private void insertMetaInHtml() {
		htmlFinal.append("<article id='tab3'>\n");
		htmlFinal.append("<br>\n");
		htmlFinal.append( htmlMeta.toString() );
		htmlFinal.append("</article>\n");
	}
	
	private void insertLogInHtml() {
		htmlFinal.append("<article id='tab4'>\n");
		htmlFinal.append("<br>\n");
		htmlFinal.append( htmlLog.toString() );
		htmlFinal.append("</article>\n");
	}

	private void insertBottomJavascript() {
		htmlFinal.append("<script>\n");
		htmlFinal.append("startTabs();\n");
		htmlFinal.append("hideUnavailbleProducts();\n");
		htmlFinal.append("</script>\n");
		
	}

	public StringBuilder getHtmlFinal() {
		return htmlFinal;
	}

	public void setHtmlFinal(StringBuilder htmlFinal) {
		this.htmlFinal = htmlFinal;
	}

	public void addOtherSeekers(String nameToSearch) throws MalformedURLException, URISyntaxException {
		htmlSeekers = new StringBuilder();
		htmlSeekers.append("<table style='width: 100%;'>\n");
		htmlSeekers.append("\t<thead>\n");
		htmlSeekers.append("\t\t<tr>\n");
		htmlSeekers.append("\t\t\t<th colspan=2>\n");
		htmlSeekers.append("\t\t\t\t<b>Other search engines</b>\n");
		htmlSeekers.append("\t\t\t</th>\n");
		htmlSeekers.append("\t\t</tr>\n");	
		htmlSeekers.append("\t</thead>\n");
		
		htmlSeekers.append("\t<tbody>\n");

		htmlSeekers.append("\t\t<tr>\n");
		htmlSeekers.append("\t\t\t<td style='width: 50%;'>\n");
		htmlSeekers.append("\t\t\t\tBuscape\n");
		htmlSeekers.append("\t\t\t</td>\n");
		htmlSeekers.append("\t\t\t<td width=50%>\n");
		htmlSeekers.append("\t\t\t\t<a href='http://www.buscape.com.br/cprocura?produto=" ).append( Util.stringToUrl(nameToSearch) ).append( "&fromSearchBox=true'> Click here </a>\n");
		htmlSeekers.append("\t\t\t</td>\n");
		htmlSeekers.append("\t\t</tr>\n");
		
		htmlSeekers.append("\t\t<tr>\n");
		htmlSeekers.append("\t\t\t<td style='width: 50%;'>\n");
		htmlSeekers.append("\t\t\t\tBondFaro\n");
		htmlSeekers.append("\t\t\t</td>\n");
		htmlSeekers.append("\t\t\t<td width=50%>\n");
		htmlSeekers.append("\t\t\t\t<a href='http://www.bondfaro.com.br/cprocura?produto=" ).append( Util.stringToUrl(nameToSearch) ).append( "&fromSearchBox=true'> Click here </a>\n");
		htmlSeekers.append("\t\t\t</td>\n");
		htmlSeekers.append("\t\t</tr>\n");
		
		htmlSeekers.append("\t\t<tr>\n");
		htmlSeekers.append("\t\t\t<td style='width: 50%;'>\n");
		htmlSeekers.append("\t\t\t\tShopping UOL\n");
		htmlSeekers.append("\t\t\t</td>\n");
		htmlSeekers.append("\t\t\t<td width=50%>\n");
		htmlSeekers.append("\t\t\t\t<a href='http://shopping.uol.com.br/busca.html?natural=sim&q=" ).append( Util.stringToUrl(nameToSearch) ).append( "'> Click here </a>\n");
		htmlSeekers.append("\t\t\t</td>\n");
		htmlSeekers.append("\t\t</tr>\n");
		
		htmlSeekers.append("\t\t<tr>\n");
		htmlSeekers.append("\t\t\t<td style='width: 50%;'>\n");
		htmlSeekers.append("\t\t\t\tGoogle Shopping\n");
		htmlSeekers.append("\t\t\t</td>\n");
		htmlSeekers.append("\t\t\t<td width=50%>\n");
		htmlSeekers.append("\t\t\t\t<a href='https://www.google.com.br/search?q=google&oq=google&aqs=chrome..69i57j69i60l3j69i65l2.679j0j7&sourceid=chrome&es_sm=93&ie=UTF-8#q=" ).append( Util.stringToUrl(nameToSearch) ).append( "&tbm=shop'> Click here </a>\n");
		htmlSeekers.append("\t\t\t</td>\n");
		htmlSeekers.append("\t\t</tr>\n");
		
		htmlSeekers.append("\t\t<tr>\n");
		htmlSeekers.append("\t\t\t<td style='width: 50%;'>\n");
		htmlSeekers.append("\t\t\t\tZOOM\n");
		htmlSeekers.append("\t\t\t</td>\n");
		htmlSeekers.append("\t\t\t<td width=50%>\n");
		htmlSeekers.append("\t\t\t\t<a href='http://www.zoom.com.br/search?q=" ).append( Util.stringToUrl(nameToSearch) ).append( "'> Click here </a>\n");
		htmlSeekers.append("\t\t\t</td>\n");
		htmlSeekers.append("\t\t</tr>\n");
		
		htmlSeekers.append("\t\t<tr>\n");
		htmlSeekers.append("\t\t\t<td style='width: 50%;'>\n");
		htmlSeekers.append("\t\t\t\tJa cotei\n");
		htmlSeekers.append("\t\t\t</td>\n");
		htmlSeekers.append("\t\t\t<td width=50%>\n");
		htmlSeekers.append("\t\t\t\t<a href='http://www.jacotei.com.br/busca/?texto=" ).append( Util.stringToUrl(nameToSearch) ).append( "'> Click here </a>\n");
		htmlSeekers.append("\t\t\t</td>\n");
		htmlSeekers.append("\t\t</tr>\n");

		htmlSeekers.append("\t</tbody>\n");
		
		htmlSeekers.append("</table>\n");
		htmlSeekers.append("<br>\n");
	}

	public void addMetaData(String nameToSearch, long time, String date, String hora) {
		htmlMeta = new StringBuilder();
		htmlMeta.append("<table style='width=100%;'>\n");
		htmlMeta.append("\t<thead>\n");
		htmlMeta.append("\t\t<tr>\n");
		htmlMeta.append("\t\t\t<th colspan=2>\n");
		htmlMeta.append("\t\t\t\t<b>Meta Data</b>\n");
		htmlMeta.append("\t\t\t</th>\n");
		htmlMeta.append("\t\t</tr>\n");	
		htmlMeta.append("\t</thead>\n");
		
		htmlMeta.append("\t<tbody>\n");

		htmlMeta.append("\t\t<tr>\n");
		htmlMeta.append("\t\t\t<td style='width: 50%;'>\n");
		htmlMeta.append("\t\t\t\tSearched term\n");
		htmlMeta.append("\t\t\t</td>\n");
		htmlMeta.append("\t\t\t<td style='width: 50%;'>\n");
		htmlMeta.append(nameToSearch);
		htmlMeta.append("\t\t\t\t</td>\n");
		htmlMeta.append("\t\t</tr>\n");
		
		htmlMeta.append("\t\t<tr>\n");
		htmlMeta.append("\t\t\t<td style='width: 50%;'>\n");
		htmlMeta.append("\t\t\t\tTime to search\n");
		htmlMeta.append("\t\t\t</td>\n");
		htmlMeta.append("\t\t\t<td style='width: 50%;'>\n");
		htmlMeta.append(time);
		htmlMeta.append("\t\t\t</td>\n");
		htmlMeta.append("\t\t</tr>\n");
		
		htmlMeta.append("\t\t<tr>\n");
		htmlMeta.append("\t\t\t<td style='width: 50%;'>\n");
		htmlMeta.append("\t\t\t\tDate\n");
		htmlMeta.append("\t\t\t</td>\n");
		htmlMeta.append("\t\t\t<td style='width: 50%;'>\n");
		htmlMeta.append(date);
		htmlMeta.append("\t\t\t</td>\n");
		htmlMeta.append("\t\t</tr>\n");
		
		htmlMeta.append("\t\t<tr>\n");
		htmlMeta.append("\t\t\t<td style='width: 50%;'>\n");
		htmlMeta.append("\t\t\t\tTime\n");
		htmlMeta.append("\t\t\t</td>\n");
		htmlMeta.append("\t\t\t<td style='width: 50%;'>\n");
		htmlMeta.append(hora);
		htmlMeta.append("\t\t\t</td>\n");
		htmlMeta.append("\t\t</tr>\n");
	
		htmlMeta.append("\t</tbody>\n");
		
		htmlMeta.append("</table>\n");
		
		htmlMeta.append("<br>\n");
	}

	public void addLogTab(String productName) {
		htmlLog = new StringBuilder();

		htmlLog.append("<iframe src='./logs/" ).append( productName ).append( ".log' style='width:100%; height:800px border: 0px; none;'>\n");
		htmlLog.append("</iframe>\n");
		
	}
	
	
}
