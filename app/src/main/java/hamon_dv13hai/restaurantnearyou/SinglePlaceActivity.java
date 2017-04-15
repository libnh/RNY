package hamon_dv13hai.restaurantnearyou;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
/**
 * Controls the view and functionality of the second activity
 * when a restaurant been clicked.
 * Created by Hamon on 2016-03-03.
 */
public class SinglePlaceActivity extends Activity{

    AlertDialogManager alert = new AlertDialogManager();
    GooglePlaces googlePlaces;
    RestaurantDetails restaurantDetails;
    public static String KEY_REFERENCE = "reference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_place);
        Intent i = getIntent();
        String reference = i.getStringExtra(KEY_REFERENCE);
        new LoadSinglePlaceDetails().execute(reference);
    }


    /**
     * Background Async Task to Load Google places
     * */
    class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {
        /**
         * getting Profile JSON
         * */
        protected String doInBackground(String... args) {
            String reference = args[0];

            googlePlaces = new GooglePlaces();

            try {
                restaurantDetails = googlePlaces.getRestaurantDetails(reference);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Updating restaurants details into view
         * check status if ok update else Alert
         * Dialog of what went wrong.
         * if single Information is missing writes "not
         * present" on label
         * @param file_url
         */
        protected void onPostExecute(String file_url) {
                    if(restaurantDetails != null){
                        String status = restaurantDetails.status;
                        if(status.equals("OK")){
                            if (restaurantDetails.result != null) {
                                String name = restaurantDetails.result.name;
                                String address = restaurantDetails.result.formatted_address;
                                String phone = restaurantDetails.result.formatted_phone_number;

                                TextView lbl_name = (TextView) findViewById(R.id.name);
                                TextView lbl_address = (TextView) findViewById(R.id.address);
                                TextView lbl_phone = (TextView) findViewById(R.id.phone);

                                name = name == null ? "Not present" : name;
                                address = address == null ? "Not present" : address;
                                phone = phone == null ? "Not present" : phone;

                                lbl_name.setText(name);
                                lbl_address.setText(address);
                                lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
                            }
                        }
                        else if(status.equals("ZERO_RESULTS")){
                            alert.showAlertDialog(SinglePlaceActivity.this, "Near Places",
                                    "Sorry no restaurant found.");
                        }
                        else if(status.equals("UNKNOWN_ERROR"))
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry unknown error occured.");
                        }
                        else if(status.equals("OVER_QUERY_LIMIT"))
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry query limit to google places is reached");
                        }
                        else if(status.equals("REQUEST_DENIED"))
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry error occured. Request is denied");
                        }
                        else if(status.equals("INVALID_REQUEST"))
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry error occured. Invalid Request");
                        }
                        else
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry error occured.");
                        }
                    }else{
                        alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                "Sorry error occured.");
                    }

        }

    }

}

