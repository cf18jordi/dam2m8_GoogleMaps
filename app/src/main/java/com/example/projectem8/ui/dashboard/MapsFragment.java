package com.example.projectem8.ui.dashboard;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.example.projectem8.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;


public class MapsFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    String msg1 = "Waiting to get adress";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private GoogleMap map;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */


        @Override
        public void onMapReady(final GoogleMap map) {
            enableMyLocation();
            LatLng institute = new LatLng(41.41637924698498, 2.199256829917431);
            map.addMarker(new MarkerOptions().position(institute).title("Marker in IES Joan d'Austria"));
            map.moveCamera(CameraUpdateFactory.newLatLng(institute));

            //map.setOnMyLocationButtonClickListener();
            //map.setOnMyLocationClickListener(this);

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                    LatLng ubiOnClick = new LatLng(latLng.latitude, latLng.longitude);
                    Log.d(TAG, "onMapClick--->");
                    Log.d(TAG, "-----------------------------------------");
                    Log.d(TAG, "Informació referent a la ubicació:");
                    Log.d(TAG, "-----------------------------------------");
                    Log.d(TAG, "<|LATITUD|>    <" + latLng.latitude + ">");
                    Log.d(TAG, "<|LONGITUD|>   <" + latLng.longitude + ">");

                    getAddress(latLng.latitude, latLng.longitude);

                    map.moveCamera(CameraUpdateFactory.newLatLng(ubiOnClick));

                    enableMyLocation();


                    // add Maps Marker, and Marker information
                    try {
                        Geocoder geo = new Geocoder(MapsFragment.this.getActivity().getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (addresses.isEmpty()) {
                            Toast.makeText(getActivity().getApplicationContext(), msg1, Toast.LENGTH_LONG).show();
                        } else {
                            if (addresses.size() > 0) {


                                Toast.makeText(getActivity().getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName(), Toast.LENGTH_SHORT).show();
                                String address = (addresses.get(0).getFeatureName());

                                Toast.makeText(getActivity().getApplicationContext(), "Area administradora:- " + addresses.get(0).getAdminArea(), Toast.LENGTH_SHORT).show();
                                String address2 = (addresses.get(0).getAdminArea());

                                Toast.makeText(getActivity().getApplicationContext(), "Localitat:- " + addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
                                String address3 = (addresses.get(0).getLocality());
                                map.clear();
                                map.addMarker(new MarkerOptions().position(ubiOnClick).title("Adreça: " + address + ", " + address2 + ", " + address3 + "."));
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "No Location Name Found", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        private void enableMyLocation() {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (map != null) {
                    map.setMyLocationEnabled(true);

                }
            } else {
                // Permission to access the location is missing. Show rationale and request permission
                PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                        Manifest.permission.ACCESS_FINE_LOCATION, true);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void getAddress(double lat, double lng) {
        try {
            Geocoder geo = new Geocoder(MapsFragment.this.getActivity().getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(lat, lng, 1);
            if (addresses.isEmpty()) {
                Toast.makeText(getActivity().getApplicationContext(), msg1, Toast.LENGTH_LONG).show();
            } else {
                if (addresses.size() > 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName(), Toast.LENGTH_SHORT).show();
                    String address = (addresses.get(0).getFeatureName());

                    Toast.makeText(getActivity().getApplicationContext(), "Area administradora:- " + addresses.get(0).getAdminArea(), Toast.LENGTH_SHORT).show();
                    String address2 = (addresses.get(0).getAdminArea());

                    Toast.makeText(getActivity().getApplicationContext(), "Localitat:- " + addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
                    String address3 = (addresses.get(0).getLocality());
                }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "No Location Name Found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT)
                .show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG)
                .show();
    }

}
