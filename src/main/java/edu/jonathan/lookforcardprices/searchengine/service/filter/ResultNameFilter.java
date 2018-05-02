package edu.jonathan.lookforcardprices.searchengine.service.filter;

@FunctionalInterface
public interface ResultNameFilter {
	boolean isValid( String productName, String searchedName );

	static ResultNameFilter noFilter(){
		return new ResultNameFilter(){
			@Override
			public boolean isValid(String productName, String searchedName) {
				return true;
			}
		};
	}

	static ResultNameFilter contains(){
		return new ResultNameFilter(){
			@Override
			public boolean isValid(String productName, String searchedName) {
				return productName.toLowerCase().contains( searchedName.toLowerCase() );
			}
		};
	}
}
