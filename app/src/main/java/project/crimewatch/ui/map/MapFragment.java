package project.crimewatch.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import project.crimewatch.Crime;
import project.crimewatch.MainActivity;
import project.crimewatch.R;


import static java.lang.Double.parseDouble;

// Maps Page
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapViewModel mapViewModel;
    private static final String TAG = "MapActivity";

    private static final float DEFAULT_ZOOM = 15;
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
    private ClusterManager<MyItem> mclusterManager;
    Address addressTemp;

    SupportMapFragment mapFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) { //kept getting a null error this fixed
            FragmentManager fragMng = getParentFragmentManager();
            FragmentTransaction fragTrns = fragMng.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragTrns.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);

        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapViewModel.getText().observe(getViewLifecycleOwner(), s -> {

        });

        mSearchText = (EditText) root.findViewById(R.id.input_search);

        return root;
    }


    private void init(){
        Log.d(TAG, "init: initalising");

        this.mSearchText.setOnClickListener(viewOnClick -> this.mSearchText.setCursorVisible(true));

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) { //this allows enter to be used rather than submit button

                    mSearchText.setCursorVisible(false);
                    InputMethodManager imm = (InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                    //begin search
                    try {
                        addressTemp = new GeoLocate().execute(mSearchText, getActivity(), mMap, addressTemp).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    if(addressTemp != null)
                    {
                        MainActivity.tempLat = "" + addressTemp.getLatitude();
                        MainActivity.tempLong = "" + addressTemp.getLongitude();
                        nonOxCrimes();
                    } else {
                        //Toast.makeText(getActivity(), "Map problem", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("Location not found!!\nPlease enter more specific location details eg. country, postcode etc...");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Confirm",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
                return false;
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMapReady(GoogleMap googlemap) {
        mMap = googlemap;
        mclusterManager = new ClusterManager<MyItem>(getContext(), mMap);
        mclusterManager.setRenderer(new MarkerClusterRenderer(getContext(), mMap, mclusterManager));
        mMap.setOnCameraIdleListener(mclusterManager);
        mMap.setOnMarkerClickListener(mclusterManager);
        mMap.setOnInfoWindowClickListener(mclusterManager);
        addItems();
        mclusterManager.cluster();
        LatLng oxford = new LatLng(51.75222, -1.25596);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(oxford));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(oxford,10.0f));

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

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mMap.getMyLocation();

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
                
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                }

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void addItems() {

        for (int i = 0; i < MainActivity.crimes.size(); i++) {
            Crime crime = MainActivity.crimes.get(i);
            MyItem newItem = new MyItem(parseDouble(crime.getLongitude()), parseDouble(crime.getLatitude()), "Title " + i, "Snippet " + i, crime.getCrimeType());
            mclusterManager.addItem(newItem);
        }

    }
    private void moveCamera(LatLng latLng, float zoom, String title, GoogleMap mMap){


        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        /*
        //marker dropped everytime for demonstration, remove this if you just want functionality
        //of geolocate without extra markers
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
         */
    }

    public void nonOxCrimes()
    {
        // Clear the map and list ready for new pins
        mMap.clear();
        mclusterManager.clearItems();
        // Loop though 
        MainActivity.tempList.clear();

        try {
            new tempGetAPIData().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < MainActivity.tempList.size(); i++) {
            Crime crime = MainActivity.tempList.get(i);

            MyItem crimeItem = new MyItem(parseDouble(crime.getLatitude()), parseDouble(crime.getLongitude()), Integer.toString(crime.getUID()), crime.getDate(), crime.getCrimeType());
            mclusterManager.addItem(crimeItem);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(crimeItem.getPosition(), 11), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                }

                @Override
                public void onCancel() {

                }
            });
        }

    }


}