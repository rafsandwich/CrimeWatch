package project.crimewatch;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;

import project.crimewatch.ui.crimes.GetAPIData;


public class MainActivity extends AppCompatActivity {
    public static ArrayList<Crime> crimes = new ArrayList<Crime>();
    public static ArrayList<Crime> crimesAPI = new ArrayList<Crime>();
    private static final String TAG = "MainActivity";
    // This is the error code if user has the incorrect google play services installed for
    // maps API to function.
    private static final int ERROR_DIALOG_REQUEST = 9001;
    public static ArrayList<Crime> tempList = new ArrayList<>();
    public static String tempLat = "";
    public static String tempLong = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_crimes, R.id.navigation_map)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        // Check app can access map api
        checkServices();

        try {
            readCrimeData(crimes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMap() {

    }


    /**
     * Checks the google play services version to ensure maps API compatibility.
     * @return true if okay else false
     */
    public void checkServices() {
        Log.d(TAG, "checkServices: checking google play services version...");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            // Everything is fine and user can make API requests
            Log.d(TAG, "checkServices: Google Play Services is working.");
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // An error occurred but can be resolved e.g. outdated version
            Log.d(TAG, "checkServices: An error occurred but can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Unable to make map requests", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Reads the crime data from the CSV and inputs all oxford crimes into the crimes list.
     * @param crimes
     * @throws IOException
     */
    private void readCrimeData(ArrayList<Crime> crimes) throws IOException {

        InputStreamReader is = new InputStreamReader(getAssets().open("crime.csv"));
        BufferedReader reader = new BufferedReader(is);
        reader.readLine();
        String line;
        String[] values;

        while ((line = reader.readLine()) != null)
        {
            values = line.split(",");
            // If location contains oxford -- Will contain south and west oxford
            if (values[8].contains("Oxford"))
            {
                // Add new crime to the crime list -- Doesnt include last outcome as it keeps
                //throwing errors
                // TODO Include last outcome without errors
                crimes.add(new Crime(values[0], values[1], values[2],
                        values[3], values[4], values[5], values[6],
                        values[7], values[8], values[9]));
            }
        }
        reader.close();
    }

//    private void createCrimeList(SortedSet<String> crimeTypes) throws IOException {
//
//        InputStreamReader is = new InputStreamReader(getAssets().open("crime.csv"));
//        BufferedReader reader = new BufferedReader(is);
//        reader.readLine();
//        String line;
//        String[] values;
//
//        while ((line = reader.readLine()) != null) {
//            values = line.split(",");
//            crimeTypes.add(values[9]);//
//
//        }
//    }
}

