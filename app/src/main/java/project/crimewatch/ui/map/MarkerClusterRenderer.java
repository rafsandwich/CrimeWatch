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
            type = type.toLowerCase().replace("-", " ");

            switch (type) {
                case "public order":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.public_order));
                    break;
                case "vehicle crime":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_theft));
                    break;
                case "criminal damage and arson":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.arson));
                    break;
                case "violence and sexual offences":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.violence));
                    break;
                case "burglary":
                case "theft from the person":
                case "other theft":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.theft));
                    break;
                case "robbery":
                case "shoplifting":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.robbery));
                case "drugs":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.drugs));
                    break;
                case "bicycle theft":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_theft));
                    break;
                case "possession of weapons":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.possession));
                    break;
                case "anti social behaviour":
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.anti_social));
                    break;
                default:
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            }
    }
}
