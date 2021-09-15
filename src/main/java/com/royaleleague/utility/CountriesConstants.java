package com.royaleleague.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountriesConstants {

  public final static String COUNTRIES = "COUNTRIES";

  public final static Map<String, String> mapOfCountries = new HashMap<String, String>() {
    {
      put("AT", "Austria");
      put("HR", "Croatia");
      put("DE", "Germany");
      put("US", "United States");
      put("UK", "United Kingdom");
      put("FR", "France");
      put("ES", "Spain");
      put("RU", "Russia");
      put("CA", "Canada");
      put("TR", "Turkey");
    }
  };

  public final static List<String> listOfCountriesCode = new ArrayList<>(mapOfCountries.keySet());

  public final static List<String> listOfCountriesName = new ArrayList<>(mapOfCountries.values());

}
