package com.ashleyfigueira.loadinteractivetest.view;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ashleyfigueira.loadinteractivetest.R;
import com.ashleyfigueira.loadinteractivetest.geo.ClusterRenderer;
import com.ashleyfigueira.loadinteractivetest.geo.CustomInfoWindowAdapter;
import com.ashleyfigueira.loadinteractivetest.loader.DatabaseLoader;
import com.ashleyfigueira.loadinteractivetest.utils.Utils;
import com.ashleyfigueira.loadinteractivetest.geo.EstablishmentItem;
import com.ashleyfigueira.loadinteractivetest.model.DatabaseHandler;
import com.ashleyfigueira.loadinteractivetest.model.Establishment;
import com.ashleyfigueira.loadinteractivetest.model.EstablishmentsList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, Callback<EstablishmentsList>,
        LoaderManager.LoaderCallbacks<ArrayList<Establishment>>, ClusterManager.OnClusterClickListener<EstablishmentItem>, ClusterManager.OnClusterItemInfoWindowClickListener<EstablishmentItem>
{

    private String END_POINT = "http://www.mocky.io";
    private GoogleMap mMap;
    private ClusterManager<EstablishmentItem> mClusterManager;
    private DatabaseLoader mDatabaseLoader;
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //First check if we have previously saved data in DB
        //download from internet only if network connection is available
        if (Utils.getFromSharedPref(this, Utils.DB_KEY).equals("saved")){
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        } else if (Utils.isNetworkAvailable(this)){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(END_POINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            EstablishmentsService establishmentsService = retrofit.create(EstablishmentsService.class);
            Call<EstablishmentsList> callEstablishments = establishmentsService.getEstablishments();
            callEstablishments.enqueue(this);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        DatabaseHandler.getInstance(this).close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refreshCluster();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        setUpClusterer();
    }

    private void setUpClusterer()
    {
        mClusterManager = new ClusterManager<EstablishmentItem>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        mClusterManager.setRenderer(new ClusterRenderer(this, mMap, mClusterManager));
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
    }

    private void addItems(ArrayList<Establishment> data)
    {
        if (mClusterManager != null){
            for(Establishment establishment : data){
                LatLng latLng = new LatLng(establishment.getLocation().getLat(), establishment.getLocation().getLong());
                EstablishmentItem item = new EstablishmentItem(establishment.getName(), establishment.getPhone(),
                        latLng, establishment.getLocation().getName(), establishment.getDescription());
                mClusterManager.addItem(item);
            }
        }
        refreshCluster();
    }

    public void refreshCluster(){
        if (mClusterManager!=null){
            mClusterManager.cluster();
        }
    }

    /********************************************
     *********** MAP CLICK LISTENERS ************
     ********************************************/


    @Override
    public boolean onClusterClick(Cluster<EstablishmentItem> cluster)
    {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        final LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(EstablishmentItem establishmentItem)
    {
        Intent intent = new Intent(this, EstablishmentDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Utils.ESTABLISHMENT_NAME_KEY, establishmentItem.getName());
        bundle.putString(Utils.ESTABLISHMENT_PHONE_KEY, establishmentItem.getPhone());
        bundle.putString(Utils.ESTABLISHMENT_ADDRESS_KEY, establishmentItem.getAddress());
        bundle.putString(Utils.ESTABLISHMENT_DESCRIPTION_KEY, establishmentItem.getDescription());
        intent.putExtra(Utils.BUNDLE_KEY,bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_right);
    }

    /*******************************
     *********** RETROFIT ************
     *******************************/

    @Override
    public void onResponse(Call<EstablishmentsList> call, Response<EstablishmentsList> response)
    {
        if (response.isSuccessful()){
            EstablishmentsList establishmentList = response.body();
            ArrayList<Establishment> establishmentArrayList = new ArrayList<Establishment>();
            establishmentArrayList.addAll(establishmentList.getEstablishments());
            addItems(establishmentArrayList);
            DatabaseHandler.getInstance(this).addEstablishments(establishmentArrayList);
            Utils.saveToSharedPref(this, Utils.DB_KEY, "saved");
        } else {
            Toast.makeText(MapsActivity.this, getString(R.string.error_code) + response.code(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<EstablishmentsList> call, Throwable t)
    {
        Toast.makeText(MapsActivity.this, getString(R.string.download_failed) +  t.getMessage(), Toast.LENGTH_LONG).show();
    }

    /*******************************
     *********** LOADER ************
     *******************************/

    @Override
    public Loader<ArrayList<Establishment>> onCreateLoader(int id, Bundle args)
    {
        mDatabaseLoader = new DatabaseLoader(this);
        return mDatabaseLoader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Establishment>> loader, ArrayList<Establishment> data)
    {
        addItems(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Establishment>> loader)
    {
        //do nothing
    }
}
