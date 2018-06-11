package edu.jonathan.lookforcardprices.searchengine.domain;

import org.javamoney.moneta.Money;


public class ProductPrice {
	private String formattedPrice;
	private Money amount;

	public ProductPrice(String formattedPrice, Money amount) {
		this.formattedPrice = formattedPrice;
		this.amount = amount;
	}

	public String getFormattedPrice() {
		return formattedPrice;
	}

	public void setFormattedPrice(String formattedPrice) {
		this.formattedPrice = formattedPrice;
	}

	public Money getAmount() {
		return amount;
	}

	public void setAmount(Money amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "ProductPrice{" +
				"formattedPrice='" + formattedPrice + '\'' +
				", amount=" + amount +
				'}';
	}
}
