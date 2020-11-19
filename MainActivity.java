package project.crimewatch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

import project.crimewatch.ui.EmergencyContact;


public class MainActivity extends AppCompatActivity {
    int violentCrimes = 0;
    int publicOrderCrimes = 0;
    int burglary = 0;
    int otherThefts = 0;
    int otherCrimes = 0;
    int antiSocial_behaviour = 0;
    int drugs = 0;
    int vehicleCrimes = 0;
    int arson = 0;
    int shoplifting = 0;
    int bicycleTheft = 0;

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
        //emergency contact details
        EmergencyContact contact = new EmergencyContact("999","https://www.police.uk/pu/contact-the-police/report-a-crime-incident/");
        Button btn = (Button) findViewById(R.id.button);
        Button btn2 = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("emergency","999");
                Toast.makeText(getApplicationContext(),contact.getEmergencyNum(),Toast.LENGTH_LONG).show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link_button(contact.getWebReport());
            }
        });

        //Dropdown list

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.offencetype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);




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

    //method for accessing the link

    public void link_button(String url){
        Intent intent = new Intent (Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    //Responding to the dropdown list
    public void crimedata(ArrayList<Crime> crimes){
        for(int i = 0; i < crimes.size(); i++){


            String s = crimes.get(i).getCrimeType();
            if( s == "Anti-social behaviour" ){
                antiSocial_behaviour += 1;

            }
            else if (s == "Violence and sexual offences"){
                violentCrimes += 1;

            }
            else if(s == "Public Order"){
                publicOrderCrimes += 1;

            }
            else if(s == "Burglary"){
                burglary += 1;

            }
            else if (s == "Bicycle theft"){
                bicycleTheft += 1;

            }
            else if (s == "Shoplifing"){
                shoplifting += 1;

            }
            else if (s == " Other theft" ){
                otherThefts += 1;
            }
            else if (s == "Other crime"){
                otherCrimes += 1;
            }
            else if ( s == "Criminal damage and arson"){
                arson += 1;
            }
            else if (s == "Drugs"){
                drugs += 1;
            }
            else if (s == "Vehicle crime"){
                vehicleCrimes += 1;
            }
        }
    }








}

