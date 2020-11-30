package project.crimewatch.ui.crimes;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import java.util.Arrays;
import java.util.List;

import project.crimewatch.Crime;
import project.crimewatch.MainActivity;
import project.crimewatch.R;

// Crimes Page
public class CrimesFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private CrimesViewModel crimesViewModel;
    Button btn;
    TextView tv;
    Spinner ddCrimeTypes;
    Spinner ddDates;
    String inputCrimeType;
    String inputDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        crimesViewModel =
                new ViewModelProvider(this).get(CrimesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_crimes, container, false);

        // Find from layout
        btn = (Button)root.findViewById(R.id.btnDisplayCrimes);
        tv = (TextView)root.findViewById(R.id.tvDisplayCrimes);
        ddCrimeTypes = (Spinner)root.findViewById(R.id.ddCrimeTypes);
        ddDates = (Spinner)root.findViewById(R.id.ddDates);

        //Set default positions for dd lists
        ddCrimeTypes.setSelection(0);
        ddDates.setSelection(0);

        //Drop Down list stuff

        //Crime Types
        ArrayAdapter<CharSequence> adapterCrimeTypes = ArrayAdapter.createFromResource(requireActivity().getApplicationContext(),
                R.array.ddCrimeTypes, android.R.layout.simple_spinner_item);
        adapterCrimeTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddCrimeTypes.setAdapter(adapterCrimeTypes);

        //Dates
        ArrayAdapter<CharSequence> adapterDate = ArrayAdapter.createFromResource(requireActivity().getApplicationContext(),
                R.array.ddDates, android.R.layout.simple_spinner_item);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddDates.setAdapter(adapterDate);

        // Set layout stuff
        tv.setMovementMethod(new ScrollingMovementMethod());
        btn.setOnClickListener(this);
        ddCrimeTypes.setOnItemSelectedListener(this);
        ddDates.setOnItemSelectedListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        tv.setText(getData(inputDate, inputCrimeType));
    }

    public String getData(String date, String crimeType) {
        String data = "";

        // If the user wants all crimes from all dates
        if (inputCrimeType.equals("All Crimes") && inputDate.equals("All Dates"))
        {
            for (Crime crime : MainActivity.crimes)
            {
                data += crime.displayCrimeData() + "\n\n";
            }
        }

        // If the user wants all crimes, from a specific date
        else if (inputCrimeType.equals("All Crimes"))
        {
            for (Crime crime : MainActivity.crimes)
            {
                // If the crime happened on that Specific Date
                if(crime.getDate().equals(date))
                {
                    data += crime.displayCrimeData() + "\n\n";
                }
            }
        }

        // If the user wants specific crimes from all dates
        else if (inputDate.equals("All Dates"))
        {
            for(Crime crime : MainActivity.crimes)
            {
                // If the crime type is the specific crime type, regardless of date
                if(crime.getCrimeType().equals(crimeType))
                {
                    data += crime.displayCrimeData() + "\n\n";
                }
            }
        }

        // If the user wants a specific crime from specific date
        else
        {
            for (Crime crime : MainActivity.crimes)
            {
                if (crime.getCrimeType().equals(crimeType) && crime.getDate().equals(date))
                {
                    data += crime.displayCrimeData() + "\n\n";
                }
            }
        }


        return data;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.ddCrimeTypes)
        {
            inputCrimeType = (String) ddCrimeTypes.getItemAtPosition(position);
        }
        else if (parent.getId() == R.id.ddDates)
        {
            inputDate = (String)ddDates.getItemAtPosition(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}