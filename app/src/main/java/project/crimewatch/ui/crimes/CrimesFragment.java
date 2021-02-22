package project.crimewatch.ui.crimes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import project.crimewatch.Crime;
import project.crimewatch.MainActivity;
import project.crimewatch.R;

// Crimes Page
public class CrimesFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private CrimesViewModel crimesViewModel;
    Button btnDisplayCrimes;
    Button btnDisplayCrimesViaAPI;
    TextView tv;
    TextView tvMonths;
    TextView tvCrimes;

    // Checkbox stuff
    String[] monthArray = {"2020-09", "2020-10", "2020-11", "2020-11"};
    String[] crimeArray = {"All Crimes", "Other theft", "Public order", "Theft from the person",
            "Anti-social behaviour", "Violence and sexual offences", "Burglary", "Drugs", "Possession of weapons",
            "Vehicle crime", "Criminal damage and arson", "Shoplifting", "Bicycle theft", "Robbery"};
    ArrayList<Integer> monthList = new ArrayList<>();
    ArrayList<Integer> crimeList = new ArrayList<>();
    boolean[] selectedMonth;
    boolean[] selectedCrime;

    ArrayList<String> userCrimes = new ArrayList<>();
    ArrayList<String> userMonths = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        crimesViewModel =
                new ViewModelProvider(this).get(CrimesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_crimes, container, false);

        // Find from layout
        btnDisplayCrimes = (Button)root.findViewById(R.id.btnDisplayCrimes);
        btnDisplayCrimesViaAPI = (Button)root.findViewById(R.id.btnDisplayCrimesViaAPI) ;
        tv = (TextView)root.findViewById(R.id.tvDisplayCrimes);
        tvMonths = (TextView)root.findViewById(R.id.tvMonths);
        tvCrimes = (TextView)root.findViewById(R.id.tvCrimes);


        // Set layout stuff/Button event listeners
        tv.setMovementMethod(new ScrollingMovementMethod());
        btnDisplayCrimes.setOnClickListener(this);
        btnDisplayCrimesViaAPI.setOnClickListener(this);

        selectedMonth = new boolean[monthArray.length];
        selectedCrime = new boolean[crimeArray.length];

        tvMonths.setOnClickListener(this);
        tvCrimes.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.btnDisplayCrimes:
                tv.setText(getData(userCrimes, userMonths));
                break;
            case R.id.btnDisplayCrimesViaAPI:
                tv.setText(getDataAPI(tv));
                break;
            case R.id.tvMonths:
                checkBoxOnClickMonth(monthArray, selectedMonth, monthList, tvMonths);
                break;
            case R.id.tvCrimes:
                checkBoxOnClick(crimeArray, selectedCrime, crimeList, tvCrimes);
                break;

            default:
                break;
        }
    }


    public String getData(ArrayList<String> userCrimes, ArrayList<String> userMonths) {
        String data = "";

        //Loop through arraylist
        //loop through crimes
        //if arraylist i is equal to crime.date or crime.crimetype
        //add it to string

        // If all crimes has been selected, clear the list, add all the crime types
        // and remove "All Crimes".
        if (userCrimes.contains("All Crimes"))
        {
            userCrimes.clear();
            userCrimes.addAll(Arrays.asList(crimeArray));
            userCrimes.remove("All Crimes");
        }

        for(int i = 0; i < userCrimes.size(); i++)
        {
            for(Crime crime : MainActivity.crimes)
            {
                if(crime.getCrimeType().equals(userCrimes.get(i)))
                {
                    for(int j = 0; j < userMonths.size(); j++)
                    {
                        if (crime.getDate().equals(userMonths.get(j)))
                        {
                            data += crime.displayCrimeData() + "\n\n";
                        }
                    }
                }
            }
        }
        return data;
    }

    public String getDataAPI(TextView tv) {
        /**
         * new GetAPIData() returns an async task that needs to be executed, and once its completed get()
         * returns the data from doInBackground()
         */
        try {
            new GetAPIData().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        String data = "";

        for(Crime crime : MainActivity.crimesAPI)
        {
            data += crime.displayCrimeData() + "\n\n";
        }
        return data;
    }

    public void checkBoxOnClick(String[] optionsArray, boolean[] selectedOptions, ArrayList<Integer> optionsList,
                                TextView textView)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(textView.getResources().getResourceName(textView.getId()));

        if(textView.getResources().getResourceName(textView.getId()).equals("project.crimewatch:id/tvMonths"))
        {
            builder.setTitle("Select Month(s)");
        } else if (textView.getResources().getResourceName(textView.getId()).equals("project.crimewatch:id/tvCrimes"))
        {
            builder.setTitle("Select Crime Type(s)");
        }

        builder.setCancelable(false);

        builder.setMultiChoiceItems(optionsArray, selectedOptions, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked)
                {
                    optionsList.add(which);
                    Collections.sort(optionsList);
                }
                else
                {
                    optionsList.remove(which);
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder stringBuilder = new StringBuilder();

                for(int j = 0; j<optionsList.size(); j++)
                {
                    stringBuilder.append(optionsArray[optionsList.get(j)]);
                    userCrimes.add(optionsArray[optionsList.get(j)]);

                    if(j != optionsList.size()-1)
                    {
                        stringBuilder.append(", ");
                    }
                }
                textView.setText(stringBuilder.toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int j = 0; j<selectedOptions.length; j++)
                {
                    selectedOptions[j] = false;
                    optionsList.clear();

                    textView.setText("");
                }
            }
        });

        builder.show();
    }

    public void checkBoxOnClickMonth(String[] optionsArray, boolean[] selectedOptions, ArrayList<Integer> optionsList,
                                TextView textView)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(textView.getResources().getResourceName(textView.getId()));

        if(textView.getResources().getResourceName(textView.getId()).equals("project.crimewatch:id/tvMonths"))
        {
            builder.setTitle("Select Month(s)");
        } else if (textView.getResources().getResourceName(textView.getId()).equals("project.crimewatch:id/tvCrimes"))
        {
            builder.setTitle("Select Crime Type(s)");
        }

        builder.setCancelable(false);

        builder.setMultiChoiceItems(optionsArray, selectedOptions, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked)
                {
                    optionsList.add(which);
                    Collections.sort(optionsList);
                }
                else
                {
                    optionsList.remove(which);
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder stringBuilder = new StringBuilder();

                for(int j = 0; j<optionsList.size(); j++)
                {
                    stringBuilder.append(optionsArray[optionsList.get(j)]);
                    userMonths.add(optionsArray[optionsList.get(j)]);

                    if(j != optionsList.size()-1)
                    {
                        stringBuilder.append(", ");
                    }
                }
                textView.setText(stringBuilder.toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int j = 0; j<selectedOptions.length; j++)
                {
                    selectedOptions[j] = false;
                    optionsList.clear();

                    textView.setText("");
                }
            }
        });

        builder.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}