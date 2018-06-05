package edu.jonathan.lookforcardprices.searchengine.domain;

import java.util.Objects;


public class Shop {
	
	private String name;
	private String mainUrl;

	public Shop(String nome, String mainUrl) {
		super();
		this.name = nome;
		this.mainUrl = mainUrl;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMainUrl() {
		return mainUrl;
	}
	public void setMainUrl(String mainUrl) {
		this.mainUrl = mainUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Shop shop = (Shop) o;
		return Objects.equals(name, shop.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
