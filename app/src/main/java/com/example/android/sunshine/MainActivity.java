package com.example.android.sunshine;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import android.util.Log;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ForecastFragment extends Fragment {

        public ForecastFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ArrayList daily_forecast = new ArrayList();

            daily_forecast = getDailyForecast("05267", 7);

            ArrayAdapter daily_forecast_data = new ArrayAdapter(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, daily_forecast);

            ListView mlistView = (ListView) rootView.findViewById(R.id.listview_forecast);
            mlistView.setAdapter(daily_forecast_data);

            return rootView;
        }
    }

    public static ArrayList getDailyForecast(String location, Integer numberOfDays) {

        ArrayList daily_forecast = new ArrayList();

        daily_forecast.add("Today - Sunny - 88/63");
        daily_forecast.add("Tomorrow - Foggy - 70/46");
        daily_forecast.add("Weds - Cloudy - 72/63");
        daily_forecast.add("Thurs - Rainy - 64/51");
        daily_forecast.add("Fri - Foggy - 70/46");
        daily_forecast.add("Sat - Sunny - 76/68");
        daily_forecast.add("Sun - Sunny - 80/72");
        daily_forecast.add("Mon - Sunny - 88/63");
        daily_forecast.add("Tues - Foggy - 70/46");
        daily_forecast.add("Weds - Cloudy - 72/63");
        daily_forecast.add("Thurs - Rainy - 64/51");
        daily_forecast.add("Fri - Foggy - 70/46");
        daily_forecast.add("Sat - Sunny - 76/68");
        daily_forecast.add("Sun - Sunny - 80/72");

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        String tempURL = "";

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are available at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            tempURL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + location + "&mode=json&units=metric&cnt=" + numberOfDays.toString();
            //tempURL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7";
            URL url = new URL(tempURL);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                forecastJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                forecastJsonStr = null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("ForecastFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            forecastJsonStr = null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("ForecastFragment", "Error closing stream", e);
                }
            }
        }

        return daily_forecast;
    }
}
