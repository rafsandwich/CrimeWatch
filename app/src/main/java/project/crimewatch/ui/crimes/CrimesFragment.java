package project.crimewatch.ui.crimes;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import project.crimewatch.Crime;
import project.crimewatch.MainActivity;
import project.crimewatch.R;

// Crimes Page
public class CrimesFragment extends Fragment implements View.OnClickListener {
    private CrimesViewModel crimesViewModel;
    Button btn;
    TextView tv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        crimesViewModel =
                new ViewModelProvider(this).get(CrimesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_crimes, container, false);
        final TextView textView = root.findViewById(R.id.tvCrimeFragment);


        /*
        crimesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */

        btn = (Button)root.findViewById(R.id.btnDisplayCrimes);
        tv = (TextView)root.findViewById(R.id.tvDisplayCrimes);
        tv.setMovementMethod(new ScrollingMovementMethod());
        btn.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        String data = "";
        for(Crime crime : MainActivity.crimes)
        {
            data += crime.displayCrimeData() + "\n\n";
        }
        tv.setText(data);
    }
}