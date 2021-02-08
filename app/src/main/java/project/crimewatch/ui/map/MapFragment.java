package project.crimewatch.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import project.crimewatch.R;

// Maps Page
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapViewModel mapViewModel;
    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1122;
    private Boolean mLocationsPermissionGranted = false;
    GoogleMap mMap;

    SupportMapFragment mapFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) { //kept getting a null error this fixed
            FragmentManager fragMng = getFragmentManager();
            FragmentTransaction fragTrns = fragMng.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragTrns.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googlemap) {
        mMap = googlemap;
        LatLng oxford = new LatLng(51.75222, -1.25596);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(oxford);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        markerOptions.title("Oxford Marker");
        markerOptions.snippet("Crime level: 5");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(oxford));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(oxford));
        mMap.addMarker(markerOptions);
    }
}