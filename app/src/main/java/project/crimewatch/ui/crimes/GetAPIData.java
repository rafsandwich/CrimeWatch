package project.crimewatch.ui.crimes;
import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


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

                // Do something with API response

                //At the moment the response is put into a string and returned

                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    data += scanner.nextLine();
                }

                scanner.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}
