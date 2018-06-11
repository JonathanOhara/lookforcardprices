package edu.jonathan.lookforcardprices.searchengine.domain;

import org.jsoup.nodes.Node;

import java.net.URL;
import java.util.Optional;


public class Product {
	private String name;
	private boolean available;
	private String imageUrl;
	private String url;
	private URL searchedURL;

	private Optional<ProductPrice> productPrice;

	private Shop shopFounded;
	private Node productContainer;
	
	public Product() {}

	public Product(String name, boolean available, Shop shopFounded, String imageUrl, String url, URL searchedURL, Node productContainer) {
		this(name, available, shopFounded, imageUrl, url, searchedURL, productContainer, null);
	}

	public Product(String name, boolean available, Shop shopFounded, String imageUrl, String url, URL searchedURL, Node productContainer, ProductPrice productPrice) {
		this.name = name;
		this.available = available;
		this.shopFounded = shopFounded;
		this.imageUrl = imageUrl;
		this.url = url;
		this.searchedURL = searchedURL;
		this.productContainer = productContainer;

		this.productPrice = Optional.ofNullable(productPrice);
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
//		float valueModificator = 1;
//		try{
//			String value = getFormattedPrice();
//
//			if( value.startsWith("$") ){ //Nintendo eShop
//				value = value.replace("$", "").trim();
//				value = value.replace("*", "").trim();
//
//				valueModificator = Keys.DOLAR_VALUE;
//			}
//
//			value = value.replace("por R$", "").trim();
//			value = value.replace("R$", "").trim();
//
//			if( value.contains(".") ){
//				if( value.indexOf(".") == value.length() - 2 || value.indexOf(".") == value.length() - 3 ){
//
//				}else{
//					value = value.replace(".", "").trim();
//				}
//			}
//
//			if( value.contains(",") ){
//				if( value.indexOf(",") == value.length() - 2 || value.indexOf(",") == value.length() - 3 ){
//					value = value.replace(",", ".").trim();
//				}else{
//					value = value.replace(",", "").trim();
//				}
//			}
//
//			returnValue = Float.parseFloat( value ) * valueModificator;
//		}catch(Exception e){
//			returnValue = 9999.99f;
//		}
		
		return returnValue;
	}

	public URL getSearchedURL() {
		return searchedURL;
	}

	public void setSearchedURL(URL searchedURL) {
		this.searchedURL = searchedURL;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public Node getProductContainer() {
		return productContainer;
	}

	public void setProductContainer(Node productContainer) {
		this.productContainer = productContainer;
	}

	public Optional<ProductPrice> getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Optional<ProductPrice> productPrice) {
		this.productPrice = productPrice;
	}

	@Override
	public String toString() {
		return "Product{" +
				"name='" + name + '\'' +
				", available=" + available +
				", imageUrl='" + imageUrl + '\'' +
				", url='" + url + '\'' +
				", searchedURL=" + searchedURL +
				", productPrice=" + productPrice +
				", shopFounded=" + shopFounded +
				'}';
	}
}
