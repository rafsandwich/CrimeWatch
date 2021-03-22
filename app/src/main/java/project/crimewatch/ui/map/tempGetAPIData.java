package project.crimewatch.ui.map;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import project.crimewatch.Crime;
import project.crimewatch.MainActivity;


/**
 * Cannot do a network operation on a UI Thread so you cant GET request the API in the crime Fragment
 * so you need to create an async task.
 * */


public class tempGetAPIData extends AsyncTask<Void, Void, String>
{

    @Override
    protected String doInBackground(Void... voids) {
        String data = "";


        try {
            // Set URL and make connection
            String lat = MainActivity.tempLat;
            String lon = MainActivity.tempLong;

            // Set URL and make connection
            /*
            String lat = MainActivity.tempLat;
            String lon = MainActivity.tempLong;

            String point11 = Double.toString(Integer.parseInt(lat) + 0.007);
            String point12 = Double.toString(Integer.parseInt(lon) - 0.01);

            String point21 = Double.toString(Integer.parseInt(lat) + 0.007);
            String point22 = Double.toString(Integer.parseInt(lon) + 0.01);

            String point31 = Double.toString(Integer.parseInt(lat) - 0.005);
            String point32 = lon;


            URL url = new URL("https://data.police.uk/api/crimes-street/all-crime?poly=" + point11 + "," + point12 + ":" + point21 + "," + point22 + ":" + point31 + "," + point32);*/


            URL url = new URL("https://data.police.uk/api/crimes-street/all-crime?lat=" + MainActivity.tempLat + "&lng=" + MainActivity.tempLong);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            // Make get request
            conn.setRequestMethod("GET");
            conn.connect();

            // Get the response code, and if it isnt successful then throw error
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                //At the moment the response is put into a string and returned
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    data += scanner.nextLine();
                }
                scanner.close();

                /***
                 * Parse response and add add crimes to crime list
                 * Will return crimes list in future and that will replace the need for csv's
                 */

                JSONArray jsonArray = new JSONArray(data);
                for(int count = 0; count < jsonArray.length(); count++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(count);
                    JSONObject location = jsonObject.getJSONObject("location");
                    Crime crime = new Crime();
                    crime.setCrimeID(jsonObject.getString("id"));
                    crime.setDate(jsonObject.getString("month"));
                    crime.setLatitude(location.getString("latitude"));
                    crime.setLongitude(location.getString("longitude"));
                    crime.setCrimeType(jsonObject.getString("category"));
                    crime.setUID(count);
                    MainActivity.tempList.add(crime);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}