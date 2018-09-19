package com.example.delivery.delivery.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.delivery.delivery.CircleImageView;
import com.example.delivery.delivery.Constant;
import com.example.delivery.delivery.R;
import com.example.delivery.delivery.Utility;
import com.example.delivery.delivery.entity.Delivery;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String TAG = DetailsActivity.class.getSimpleName();

    private Delivery mDelivery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        setupToolbar();
        initMapFragment();
        extractData();
        setData();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.msg_delivery_details));
    }

    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.g_map);
        mapFragment.getMapAsync(this);
    }

    private void extractData() {
        mDelivery = (Delivery) getIntent().getSerializableExtra(Constant.KEY_DELIVERY);
        Log.e(TAG, "_log : extractData : delivery : " + mDelivery.toString());
        if(mDelivery == null) finish();
    }

    private void setData() {
        String message = !TextUtils.isEmpty(mDelivery.getDescription()) ? mDelivery.getDescription() : "";
        String address = mDelivery.getLocation() != null && mDelivery.getLocation().getAddress() != null && !TextUtils.isEmpty(mDelivery.getLocation().getAddress()) ?
                mDelivery.getLocation().getAddress() : getString(R.string.msg_default_address);
        TextView tvDescription = findViewById(R.id.text_description);
        tvDescription.setText(getString(R.string.msg_description, message, address));

        CircleImageView civ = findViewById(R.id.image_delivery);
        Utility.loadImage(this, civ, mDelivery.getImageUrl());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);

        if(mDelivery.getLocation() != null) {
            LatLng latLng = new LatLng(mDelivery.getLocation().getLat(), mDelivery.getLocation().getLng());
            map.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(mDelivery.getLocation().getAddress() != null && !TextUtils.isEmpty(mDelivery.getLocation().getAddress())? mDelivery.getLocation().getAddress() : getString(R.string.msg_default_marker)));
            map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(
                    new LatLng(mDelivery.getLocation().getLat(), mDelivery.getLocation().getLng())).zoom(12).build()));
        }

    }
}
