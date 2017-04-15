package hamon_dv13hai.restaurantnearyou;

import com.google.api.client.util.Key;

/**
 * Restaurants details for later use
 * Created by Hamon on 2016-03-08.
 */
public class RestaurantDetails {
    @Key
    public String status;
    @Key
    public Restaurant result;

    @Override
    public String toString() {
        if (result!=null) {
            return result.toString();
        }
        return super.toString();
    }
}
