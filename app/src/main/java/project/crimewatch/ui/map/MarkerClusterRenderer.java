package project.crimewatch.ui.map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import project.crimewatch.R;

public class MarkerClusterRenderer extends DefaultClusterRenderer<MyItem> {
    public MarkerClusterRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
            String type = item.getType();
            switch (type) {
                case "Public order":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.public_order));
                    break;
                case "Vehicle crime":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_theft));
                    break;
                case "Criminal damage and arson":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.arson));
                    break;
                case "Violence and sexual offences":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.violence));
                    break;
                case "Burglary":
                case "Theft from the person":
                case "Other theft":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.theft));
                    break;
                case "Robbery":
                case "Shoplifting":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.robbery));
                case "Drugs":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.drugs));
                    break;
                case "Bicycle theft":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_theft));
                    break;
                case "Possession of weapons":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.possession));
                    break;
                case "Anti-social behaviour":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.anti_social));
                    break;
                default:
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            }
    }
}
