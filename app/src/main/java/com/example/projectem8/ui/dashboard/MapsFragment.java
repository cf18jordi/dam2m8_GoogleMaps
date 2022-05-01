package com.example.projectem8.ui.dashboard;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projectem8.Interfice.ApiCall;
import com.example.projectem8.ApiThread;
import com.example.projectem8.Interfice.ApiCall2;
import com.example.projectem8.Model.ModelApi;
import com.example.projectem8.ModelFlickr.FlickrModel;
import com.example.projectem8.Glide.PageViewSlider;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.example.projectem8.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
            //enableMyLocation();
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

                    //enableMyLocation();


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

                                Log.d(TAG, "locationInfoResume--->");
                                Log.d(TAG, "---------------------------------------------------------|");
                                Log.d(TAG, "Informació referent a la ubicació:                       |");
                                Log.d(TAG, "---------------------------------------------------------|");
                                Log.d(TAG, "<|LATITUD|>              <" + latLng.latitude        + ">");
                                Log.d(TAG, "<|LONGITUD|>             <" + latLng.longitude       + ">");
                                Log.d(TAG, "<|LOCALITAT|>            <" + address3               + ">");
                                Log.d(TAG, "<|ADREÇA|>               <" + address                + ">");
                                Log.d(TAG, "<|AREA ADMINISTRADORA|>  <" + address2               + ">");
                                Log.d(TAG, "---------------------------------------------------------|");

                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "No Location Name Found", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        /*
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
        }*/
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

            ApiThread apiT = new ApiThread(lat, lng);
            apiT.execute();


            Retrofit retrofit1 = new Retrofit.Builder()
                    .baseUrl("https://api.sunrise-sunset.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl("https://www.flickr.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            String sLat = Double.toString(lat);
            String sLng = Double.toString(lng);

            ApiCall apiCall = retrofit1.create(ApiCall.class);
            Call<ModelApi> call = apiCall.getData(sLat, sLng);


            ApiCall2 apiCall2 = retrofit2.create(ApiCall2.class);
            Call<FlickrModel> call2 = apiCall2.getData(sLat, sLng);

            call.enqueue(new Callback<ModelApi>(){
                @Override
                public void onResponse(Call<ModelApi> call, Response<ModelApi> response) {

                    Log.i("testApi",
                            response.body().getStatus() + " - SUNRISE: "
                                    + response.body().getResults().getSunrise() + " - SUNSET: "
                                    + response.body().getResults().getSunset()
                    );
                }
                @Override
                public void onFailure(Call<ModelApi> call, Throwable t) {
                }
            });

            call2.enqueue(new Callback<FlickrModel>(){
                @Override
                public void onResponse(Call<FlickrModel> call, Response<FlickrModel> response) {

                    String secret = response.body().getPhotos().getPhoto().get(0).getSecret();
                    String id = response.body().getPhotos().getPhoto().get(0).getId();
                    String server = response.body().getPhotos().getPhoto().get(0).getServer();
                    String title = response.body().getPhotos().getPhoto().get(0).getTitle();

                    String url = "https://live.staticflickr.com/" + server +  "/" + id + "_" + secret + ".jpg";

                    /*Log.i("testApi", " - "
                            + response.body().getStat() + " - "
                            + id + " - "
                            + secret + " - "
                            + server + " - "
                            + title + " - "
                    );*/

                    ArrayList<String> urlImg = new ArrayList<>();

                    for(int i = 0 ; i<10 ; i++)  {
                        secret = response.body().getPhotos().getPhoto().get(i).getSecret();
                        id = response.body().getPhotos().getPhoto().get(i).getId();
                        server = response.body().getPhotos().getPhoto().get(i).getServer();
                        url = "https://live.staticflickr.com/" + server +  "/" + id + "_" + secret + ".jpg";
                        Log.d(TAG, "-----------------------------------------------------------------------------|");
                        Log.i("printImage", "URL IMAGE " + (i + 1) + " :"  + url + "  ||");
                        urlImg.add(url);
                    }

                    Intent intent = new Intent(getActivity(), PageViewSlider.class);
                    intent.putStringArrayListExtra("urls", urlImg);
                    startActivity(intent);

                    Log.d(TAG, "-----------------------------------------------------------------------------|");
                }
                @Override
                public void onFailure(Call<FlickrModel> call, Throwable t) {
                }
            });

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
