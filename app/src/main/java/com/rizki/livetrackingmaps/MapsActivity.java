package com.rizki.livetrackingmaps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rizki.livetrackingmaps.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private DatabaseReference reference;
    private LocationManager manager;

    private final int MIN_TIME = 1000; // 1 detik
    private final int MIN_DISTANCE = 1; // 1 meter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        reference = FirebaseDatabase.getInstance().getReference().child("Maps");
        //FirebaseDatabase.getInstance().getReference().setValue("This is tracker app");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLocationUpdates();
    }

    private void getLocationUpdates() {
        if(manager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                } else {
                    Toast.makeText(this, "No Provider Enabled", Toast.LENGTH_SHORT).show();
                }

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
            if(requestCode == 101){
                if(grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
                    getLocationUpdates();
                }else{
                    Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
                }
            }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng lokasiku = new LatLng(-6.839608884201539, 107.6052857179903);
        LatLng wisata1 = new LatLng(-6.833047981365829, 107.60561271093461);
        LatLng wisata2 = new LatLng(-6.848631426564443, 107.62595848133417);
        LatLng wisata3 = new LatLng(-6.843667437340559, 107.62429282954626);
        LatLng wisata4 = new LatLng(-6.841345966300139, 107.62287263311094);
        googleMap.setMapType(googleMap.MAP_TYPE_HYBRID);
        MarkerOptions options = new MarkerOptions().position(lokasiku).title("Lokasi Saat Ini");
        googleMap.addMarker(new MarkerOptions().position(wisata1).title("FarmHouse"));
        googleMap.addMarker(new MarkerOptions().position(wisata2).title("Dago Dream Park"));
        googleMap.addMarker(new MarkerOptions().position(wisata3).title("Sarae Hills"));
        googleMap.addMarker(new MarkerOptions().position(wisata4).title("D`Dieulands"));
        mMap.addMarker(new MarkerOptions().position(lokasiku).title("Marker in Lembang"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lokasiku,17));
        googleMap.addMarker(options);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            saveLocation(location);
        }else{
            Toast.makeText(this, "No Location", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLocation(Location location) {
        reference.setValue(location);
    }

    @Override
    public void onStatusChanged(String provider,int status, Bundle extras){

    }

    @Override
    public void onProviderEnabled(String provider){

    }

    @Override
    public void onProviderDisabled(String provider){

    }


}