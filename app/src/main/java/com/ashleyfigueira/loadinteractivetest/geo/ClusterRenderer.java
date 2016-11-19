package com.ashleyfigueira.loadinteractivetest.geo;

import android.content.Context;

import com.ashleyfigueira.loadinteractivetest.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by ashleyfigueira on 19/11/16.
 */

public class ClusterRenderer extends DefaultClusterRenderer<EstablishmentItem>
{
    public ClusterRenderer(Context context, GoogleMap map, ClusterManager<EstablishmentItem> clusterManager)
    {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(EstablishmentItem item, MarkerOptions markerOptions)
    {
        markerOptions.title(item.getName().toUpperCase())
                .snippet(item.getDescription())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
    }
}
