package project.crimewatch.ui.crimes;

import android.os.AsyncTask;

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


public class GetAPIData extends AsyncTask<Void, Void, String>
{

    @Override
    protected String doInBackground(Void... voids) {
        String data = "";


        try {
            // Set URL and make connection
            URL url = new URL("https://data.police.uk/api/crimes-street/all-crime?poly=51.79434003249623,%20-1.2859140839927892:51.765558739829544,%20-1.1799990230151511:51.71347214694486,%20-1.1861788320835385:51.71708825736227,%20-1.250036859123541");
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
                    MainActivity.crimesAPI.add(crime);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
