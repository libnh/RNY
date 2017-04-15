package hamon_dv13hai.restaurantnearyou;

import com.google.api.client.util.Key;
/**
 * Creates an object of a restaurants information
 * Created by Hamon on 2016-03-08.
 */
public class Restaurant {
    @Key
    public String id;

    @Key
    public String name;

    @Key
    public String reference;

    @Key
    public String formatted_address;

    @Key
    public String formatted_phone_number;

    @Override
    public String toString() {
        return name + " - " + id + " - " + reference;
    }

}
