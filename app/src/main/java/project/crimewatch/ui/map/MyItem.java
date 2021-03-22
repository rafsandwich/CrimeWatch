package project.crimewatch.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String snippet;
    private final String type;

    public MyItem(double lat, double lng, String title, String snippet, String crimeType) {
        position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
        this.type = crimeType;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public String getType() {
        return type;
    }
}
