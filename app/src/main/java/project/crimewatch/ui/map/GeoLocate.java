package project.crimewatch.ui.map;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Cannot do a network operation on a UI Thread so you cant GET request the API in the crime Fragment
 * so you need to create an async task.
 * */


public class GeoLocate extends AsyncTask<Object, Void, Address>
{
    final String TAG = "MapActivity";
    List<Address> list = new ArrayList<>();
    @Override
    protected Address doInBackground(Object... args) {
        // Cast all args from object to their types
        EditText mSearchText = (EditText)args[0];
        FragmentActivity getActivity = (FragmentActivity)args[1];
        GoogleMap mMap = (GoogleMap)args[2];
        Address addressTemp = (Address)args[3];

        String data = "";
        final float DEFAULT_ZOOM = 15;

        try {
            Log.d(TAG, "geoLocate: geolocating");

            String searchString = mSearchText.getText().toString();

            Geocoder geocoder = new Geocoder(getActivity); //MapActivity.this but this isn't an activity!

            try {
                list = geocoder.getFromLocationName(searchString, 1);
            }catch (IOException e){
                Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
            }

            if(list.size() > 0){
                Address address = list.get(0);
                Log.d(TAG, "geoLocate: found a location" + address.toString());

                //moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0), mMap);
            }
            else{Log.d(TAG, "geoLocate: could not find a location matching '" + mSearchText.getText().toString() + "'");}
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            return list.get(0);
        } catch(Exception e)
        {
            return null;
        }

    }
}
