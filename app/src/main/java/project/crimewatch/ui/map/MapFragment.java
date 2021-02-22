package project.crimewatch.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.ArrayList;

import project.crimewatch.R;

// Maps Page
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapViewModel mapViewModel;
    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1122;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    private EditText mSearchText;

    private FusedLocationProviderClient mFusedLocationProviderClient;
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

        mSearchText = (EditText) root.findViewById(R.id.input_search);

        return root;
    }

    private void init(){
        Log.d(TAG, "init: initalising");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) { //this allows enter to be used rather than submit button

                    //begin search
                    geoLocate();

                }
                return false;
            }
        });
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity()); //MapActivity.this but this isn't an activity!
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found a location" + address.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMapReady(GoogleMap googlemap) {
        mMap = googlemap;
//        LatLng oxford = new LatLng(51.75222, -1.25596);
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(oxford);
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//        markerOptions.title("Oxford Marker");
//        markerOptions.snippet("Crime level: 5");
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(oxford));
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(oxford));
//        mMap.addMarker(markerOptions);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1200000);
        mLocationRequest.setFastestInterval(1200000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
               // mFusedLocationProviderClient.removeLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                checkLocationPermission();
            }
        } else {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }

        init();

    }
}