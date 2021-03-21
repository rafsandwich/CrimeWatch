package project.crimewatch.ui.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//public class GeolocateMap extends AsyncTask<Void, Void, String> {
//
//    private MapViewModel mapViewModel;
//    private static final String TAG = "MapActivity";
//    private static final float DEFAULT_ZOOM = 15;
//    GoogleMap mMap;
//    private EditText mSearchText;
//
//
//
//    @Override
//    protected String doInBackground(Void... voids) {
//
//        private void geoLocate() {
//            Log.d(TAG, "geoLocate: geolocating");
//
//            String searchString = mSearchText.getText().toString();
//
//            Geocoder geocoder = new Geocoder(getActivity()); //MapActivity.this but this isn't an activity!
//            List<Address> list = new ArrayList<>();
//            try {
//                list = geocoder.getFromLocationName(searchString, 1);
//            }catch (IOException e){
//                Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
//            }
//
//            if(list.size() > 0){
//                Address address = list.get(0);
//                Log.d(TAG, "geoLocate: found a location" + address.toString());
//
//                moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
//            }
//        }
//
//        private void moveCamera(LatLng latLng, float zoom, String title){
//            Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//
//            MarkerOptions options = new MarkerOptions()
//                    .position(latLng)
//                    .title(title);
//            mMap.addMarker(options);
//        }
//
//        return null;
//    }
//
//
//    }

