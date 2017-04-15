package hamon_dv13hai.restaurantnearyou;

import com.google.api.client.util.Key;
import java.util.List;

/**
 * A list of all the restaurants found
 * Created by Hamon on 2016-03-08.
 */
public class RestaurantList{
    @Key
    public String status;
    @Key
    public List<Restaurant> results;
}
