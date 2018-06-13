package edu.jonathan.lookforcardprices.searchengine.domain;

import edu.jonathan.lookforcardprices.comom.MoneyUtil;
import org.javamoney.moneta.Money;
import org.jsoup.nodes.Node;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
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

	public double getPriceInReal() {
		return productPrice.map(price -> {
				if (price.getAmount().getCurrency().equals(MoneyUtil.REAL)) {
					return price.getAmount().getNumber().doubleValue();
				} else {
					return MoneyUtil.dollarToReal(price.getAmount()).getNumber().doubleValue();
				}
			}
		).orElse(0.0);
	}

	public String getPriceInRealFormatted() {
		return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format( Money.of(getPriceInReal(), MoneyUtil.REAL).getNumber() );
	}

}
