package hamon_dv13hai.restaurantnearyou;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;

/**
 * Makes request to the Google Places API to get
 * location of specific places
 * Created by Hamon on 2016-03-08.
 */
public class GooglePlaces {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final String API_KEY = "AIzaSyCvo_bFWf6qlqtKiouWnM6x2H5UUm3BEfc";
    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
    private double latitude;
    private double longitude;
    private double radius;

    /**
     * Searching for restaurants
     * @param latitude - latitude of place
     * @params longitude - longitude of place
     * @param radius - radius of searchable area
     * @param types - type of place to search
     * @return list of restaurants
     * */
    public RestaurantList search(double latitude, double longitude, double radius, String types)
            throws Exception {

        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;

        try {

            HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
            HttpRequest request = httpRequestFactory
                    .buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
            request.getUrl().put("key", API_KEY);
            request.getUrl().put("location", this.latitude + "," +this.longitude);
            request.getUrl().put("radius", this.radius);
            request.getUrl().put("sensor", "false");
            if(types != null)
                request.getUrl().put("types", types);

            RestaurantList list = request.execute().parseAs(RestaurantList.class);

            return list;

        } catch (HttpResponseException e) {
            return null;
        }

    }

    /**
     * Searching restaurants full details
     * @param reference - reference id of restaurant
     *                 - which you will get in search api request
     * */
    public RestaurantDetails getRestaurantDetails(String reference) throws Exception {
        try {

            HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
            HttpRequest request = httpRequestFactory
                    .buildGetRequest(new GenericUrl(PLACES_DETAILS_URL));
            request.getUrl().put("key", API_KEY);
            request.getUrl().put("reference", reference);
            request.getUrl().put("sensor", "false");

            RestaurantDetails place = request.execute().parseAs(RestaurantDetails.class);

            return place;

        } catch (HttpResponseException e) {
            throw e;
        }
    }

    /**
     * Creating http request Factory
     * */
    public static HttpRequestFactory createRequestFactory(
            final HttpTransport transport) {
        return transport.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                HttpHeaders headers = new HttpHeaders();
                headers.setUserAgent("restaurant");
                request.setHeaders(headers);
                JsonObjectParser parser = new JsonObjectParser(new JacksonFactory());
                request.setParser(parser);

            }
        });
    }

}
