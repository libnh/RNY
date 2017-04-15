package hamon_dv13hai.restaurantnearyou;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controls the view and functionality of the initial activity
 * on application start.
 * Created by Hamon on 2016-03-03.
 */
public class MainActivity extends AppCompatActivity {

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    AlertDialogManager alert = new AlertDialogManager();
    GooglePlaces googlePlaces;
    RestaurantList nearRestaurants;
    GPSTracker gps;
    ListView lv;
    ArrayList<HashMap<String, String>> restaurantListItems = new ArrayList<>();
    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.list);
        /**
         * ListItem click event
         * On selecting a listitem SinglePlaceActivity is launched
         * */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
                Intent in = new Intent(getApplicationContext(),
                        SinglePlaceActivity.class);
                in.putExtra(KEY_REFERENCE, reference);
                startActivity(in);
            }
        });
    }

    /**
     * click event for button.
     * checks if internet connection exists and
     * if GPS is Enabled. if true start async task
     * to fill listview.
     * @param v
     */
    public void onClick(View v){
        if(restaurantListItems != null){
            restaurantListItems.clear();
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, restaurantListItems,
                    R.layout.listitem,
                    new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
                    R.id.reference, R.id.name });
            lv.setAdapter(adapter);
        }
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            alert.showAlertDialog(MainActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection");
            return;
        }
        gps = new GPSTracker(this);
        if (!gps.canGetLocation()) {
            alert.showGPSAlertDialog(MainActivity.this);
            return;
        }
        new LoadPlaces().execute();
    }

    /**
     * Background Async Task to Load Google places
     * */
    class LoadPlaces extends AsyncTask<String, String, String> {
        /**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {

            googlePlaces = new GooglePlaces();
            try {
                String types = "cafe|restaurant";
                double radius = 1000;
                nearRestaurants = googlePlaces.search(gps.getLatitude(),
                        gps.getLongitude(), radius, types);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Checks the status of restaurants
         * if ok update listView else show
         * Alert Dialog what went wrong
         * @param file_url
         */
        protected void onPostExecute(String file_url) {
                    String status = nearRestaurants.status;
                    if(status.equals("OK")){
                        if (nearRestaurants.results != null) {
                            for (Restaurant r : nearRestaurants.results) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put(KEY_REFERENCE, r.reference);
                                map.put(KEY_NAME, r.name);
                                restaurantListItems.add(map);
                            }

                            ListAdapter adapter = new SimpleAdapter(MainActivity.this, restaurantListItems,
                                    R.layout.listitem,
                                    new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
                                    R.id.reference, R.id.name });
                            lv.setAdapter(adapter);
                        }
                    }
                    else if(status.equals("ZERO_RESULTS")){
                        alert.showAlertDialog(MainActivity.this, "Near Places",
                                "Sorry no restaurants found near you");
                    }
                    else if(status.equals("UNKNOWN_ERROR"))
                    {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry unknown error occured.");
                    }
                    else if(status.equals("OVER_QUERY_LIMIT"))
                    {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry query limit to google places is reached");
                    }
                    else if(status.equals("REQUEST_DENIED"))
                    {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured. Request is denied");
                    }
                    else if(status.equals("INVALID_REQUEST"))
                    {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured. Invalid Request");
                    }
                    else
                    {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured.");
                    }


        }

    }
}
