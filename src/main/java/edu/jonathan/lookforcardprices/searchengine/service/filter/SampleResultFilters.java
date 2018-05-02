package edu.jonathan.lookforcardprices.searchengine.service.filter;

import edu.jonathan.lookforcardprices.comom.Util;

import java.util.Arrays;
import java.util.List;

public class SampleResultFilters {
	
	private static boolean useDenyWords = true;
	private static String[] denyWords = {"DENY"};


	public static ResultNameFilter containAllWords(){
		return new ResultNameFilter(){
			@Override
			public boolean isValid(String productName, String searchedName) {
				List<String> productNameWords = Arrays.asList( productName.toLowerCase().split(" ") );
				String[] searchedNameWords = searchedName.toLowerCase().split(" ");
				boolean matches = true;
				
				for( String word: searchedNameWords ){
					
					if( !wordContainsInList( productNameWords, word) ){
						matches = false;
						break;
					}
				}
				
				if( useDenyWords ){
					for( String word: denyWords ){
						
						if( wordContainsInList( productNameWords, word) ){
							matches = false;
							break;
						}
					}	
				}
				return matches;
			}

			private boolean wordContainsInList(List<String> productNameWords, String word) {
				boolean matches = false;

				for(String s : productNameWords){
					if( Util.replaceAllAccent(s).contains(word.toLowerCase()) ){
						matches = true;
						break;
					}
				}
				return matches;
			}
		};
	}
	

}
