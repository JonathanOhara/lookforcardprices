package edu.jonathan.lookforcardprices.searchengine.domain;

import edu.jonathan.lookforcardprices.comom.Keys;
import org.jsoup.nodes.Node;

import java.net.URL;


public class Product {
	private String name;
	private String imageUrl;
	private String url;
	private URL searchedURL;
	private String formattedPrice;
	private boolean avaliable;

	private Shop shopFounded;
	private Node productContainer;
	
	public Product() {}

	public Product(String name, boolean avaliable, Shop shopFounded, String imageUrl, String url, URL searchedURL, Node productContainer, String formattedPrice) {
		super();
		this.name = name;
		this.avaliable = avaliable;
		this.shopFounded = shopFounded;
		this.imageUrl = imageUrl;
		this.url = url;
		this.searchedURL = searchedURL;
		this.formattedPrice = formattedPrice;
		this.productContainer = productContainer;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public Shop getShopFounded() {
		return shopFounded;
	}

	public void setShopFounded(Shop shopFounded) {
		this.shopFounded = shopFounded;
	}

	public float getFloatValue(){
		float returnValue = 0;
		float valueModificator = 1;
		try{
			String value = getFormattedPrice();
			
			if( value.startsWith("$") ){ //Nintendo eShop
				value = value.replace("$", "").trim();
				value = value.replace("*", "").trim();
			
				valueModificator = Keys.DOLAR_VALUE;
			}
			
			value = value.replace("por R$", "").trim();
			value = value.replace("R$", "").trim();
			
			if( value.contains(".") ){
				if( value.indexOf(".") == value.length() - 2 || value.indexOf(".") == value.length() - 3 ){
					
				}else{
					value = value.replace(".", "").trim();		
				}
			}
			
			if( value.contains(",") ){
				if( value.indexOf(",") == value.length() - 2 || value.indexOf(",") == value.length() - 3 ){
					value = value.replace(",", ".").trim();
				}else{
					value = value.replace(",", "").trim();		
				}
			}
			
			returnValue = Float.parseFloat( value ) * valueModificator;
		}catch(Exception e){
			returnValue = 9999.99f;
		}
		
		return returnValue;
	}
	/*
	public static void main(String[] args) {
		Product product = new Product();
		
		product.setFormattedPrice("$19.99");
		System.out.println( product.getFloatValue() );
		
		product.setFormattedPrice("$19.99*");
		System.out.println( product.getFloatValue() );
		
		product.setFormattedPrice("por R$ 79,90");
		System.out.println( product.getFloatValue() );
		
		product.setFormattedPrice("R$ 99,90 (33% de desconto)");
		System.out.println( product.getFloatValue() );
		
		product.setFormattedPrice("R$ 219,90");
		System.out.println( product.getFloatValue() );
		
		product.setFormattedPrice("R$ 219.90");
		System.out.println( product.getFloatValue() );
		
		product.setFormattedPrice("219,9");
		System.out.println( product.getFloatValue() );

		product.setFormattedPrice("188.9");
		System.out.println( product.getFloatValue() );
		
		product.setFormattedPrice("1.000,01");
		System.out.println( product.getFloatValue() );
		
		product.setFormattedPrice("1,000.01");
		System.out.println( product.getFloatValue() );

		product.setFormattedPrice("Indispon�vel");
		System.out.println( product.getFloatValue() );
	}
	*/

	public URL getSearchedURL() {
		return searchedURL;
	}

	public void setSearchedURL(URL searchedURL) {
		this.searchedURL = searchedURL;
	}

	public String getFormattedPrice() {
		return formattedPrice;
	}

	public void setFormattedPrice(String formattedPrice) {
		this.formattedPrice = formattedPrice;
	}

	public boolean isAvaliable() {
		return avaliable;
	}

	public void setAvaliable(boolean avaliable) {
		this.avaliable = avaliable;
	}

	public Node getProductContainer() {
		return productContainer;
	}

	public void setProductContainer(Node productContainer) {
		this.productContainer = productContainer;
	}
}
