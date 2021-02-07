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

        locationPermission();
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googlemap) {
        mMap = googlemap;
        LatLng oxford = new LatLng(51.75222, -1.25596);
//        mMap.addMarker(new MarkerOptions().position(oxford).title("Central Oxford marker"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(oxford));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(oxford);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        markerOptions.title("Oxford Marker");
        markerOptions.snippet("Crime level: 5");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(oxford));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(oxford));
        mMap.addMarker(markerOptions);

    }

    /**
     * Permissions are good so initialize map
     */
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: Map is ready");
                mMap = googleMap;
                LatLng oxford = new LatLng(51.75222, -1.25596);
                mMap.addMarker(new MarkerOptions().position(oxford).title("Central Oxford marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(oxford));

            }
        });
    }

    /**
     * asks for location permission to use map api
     */
    private void locationPermission() {
        Log.d(TAG, "locationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationsPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    /**
     * checks and logs whether permission request failed.
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationsPermissionGranted = false;

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: permission failed");
                        mLocationsPermissionGranted = false;
                        return;
                    }
                }
                Log.d(TAG, "onRequestPermissionsResult: permission granted");
                mLocationsPermissionGranted = true;
                // Initialize the map
                Log.d(TAG, "onRequestPermissionsResult: Initializing map");
                initMap();
            }
        }
    }
}