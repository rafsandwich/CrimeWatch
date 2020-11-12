package project.crimewatch;

import android.os.Bundle;

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


public class MainActivity extends AppCompatActivity {

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

        // List of crime objects
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        try {
            readCrimeData(crimes);
        } catch (IOException e) {
            e.printStackTrace();
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
}