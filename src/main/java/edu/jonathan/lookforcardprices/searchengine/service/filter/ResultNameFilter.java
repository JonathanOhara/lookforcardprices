package edu.jonathan.lookforcardprices.searchengine.service.filter;

@FunctionalInterface
public interface ResultNameFilter {
	boolean isValid( String productName, String searchedName );

	static ResultNameFilter noFilter(){
		return (productName, searchedName) -> true;
	}

	static ResultNameFilter ignoreCaseContains(){
		return (productName, searchedName) -> productName.toLowerCase().contains( searchedName.toLowerCase() );
	}
}
