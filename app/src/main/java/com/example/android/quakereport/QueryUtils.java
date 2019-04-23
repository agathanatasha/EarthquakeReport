package com.example.android.quakereport;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static ArrayList<Earthquake> fetchEarthquakesData (String urlString) {
        URL url = createUrl(urlString);
        String jsonResponse = makeHttpRequest(url);
        return extractEarthquakes(jsonResponse);
    }


    private static URL createUrl(String str) {
        URL urlObject = null;
        try {
            urlObject = new URL(str);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating url (createUrl method) " + e);
        }
        return urlObject;
    }

    private static String makeHttpRequest(URL url) {
        HttpURLConnection connection = null;
        String jsonResponse = "";
        InputStream stream = null;
        if (url == null) {
            return jsonResponse;
        }
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(1000);
            connection.setConnectTimeout(1500);
            connection.connect();

            if (connection.getResponseCode() == 200) {
                stream = connection.getInputStream();
                jsonResponse = readFromStream(stream);
            } else {
                Log.e(LOG_TAG, "Failed to retrieve data from source. Error code "
                    + connection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to open connection with provided url (makeHttpRequest)");
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (stream != null) {
            InputStreamReader streamReader =
                new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return builder.toString();
    }

    private static ArrayList<Earthquake> extractEarthquakes(String jsonString) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject jsonResponse = new JSONObject(jsonString);
            JSONArray earthquakeList = jsonResponse.optJSONArray("features");
            for (int i = 0; i < earthquakeList.length(); i++) {
                JSONObject currentEarthquakeProperties =
                    earthquakeList.getJSONObject(i).optJSONObject("properties");
                double magnitude = currentEarthquakeProperties.getDouble("mag");
                String location = currentEarthquakeProperties.getString("place");
                long time = currentEarthquakeProperties.getLong("time");
                String url = currentEarthquakeProperties.getString("url");
                earthquakes.add(new Earthquake(location, magnitude, time, url));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}