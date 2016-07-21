package br.edu.ufcg.embedded.eframework.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.embedded.eframework.R;
import br.edu.ufcg.embedded.eframework.models.Evento;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final float ZOOM_SCALE = 12.2f;
    private Context mContext;
    private SupportMapFragment sMapFragment;
    private LocationManager locationManager;
    private android.location.LocationListener locationListener;
    private boolean zoomCurrentLocation;

    private LatLng lastLocation = new LatLng(0,0);

    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        setUpMap();
        mContext = getContext();
        final List<Evento> listEvents = getEvents();

        zoomCurrentLocation = false;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                zoomCurrentLocation = false;
                Log.i("latlong", "lat: " + location.getLatitude() + "\n long: " + location.getLongitude());
                lastLocation = new LatLng(location.getLatitude(), location.getLongitude());
                setMarkers(map, listEvents);
                if (!zoomCurrentLocation){
                    zoomMapCurrentLocation();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 10);
            return null;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 5, locationListener);

        return view;
    }

    private void zoomMapCurrentLocation() {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(lastLocation, ZOOM_SCALE);
        map.animateCamera(cameraUpdate);
        zoomCurrentLocation = true;
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults){
//        switch (requestCode){
//            case 10:
//                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    configureButton();
//                return;
//        }
//
//    }
//
//    private void configureButton() {
//
//    }

    public List<Evento> getEvents() {
        final List<Evento> events = new ArrayList<>();
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mContext.getString(R.string.url_server),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object = (JSONObject) response.get(i);
                                String nome = object.getString("nome");
                                String descricao = object.getString("descricao");
                                double latitude = object.getDouble("latitude");
                                double longitude = object.getDouble("longitude");
                                String url_foto = object.getString("url_photo");
                                Evento evento = new Evento(nome, descricao, latitude, longitude, url_foto);
                                Log.d("TAG", evento.toString());
                                events.add(evento);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
//
//                        if (events.size() > 0){
//                            DataSource dataSource = DataSource.getInstance(getApplicationContext());
//                            dataSource.saveAllUsers(listaUsers);
//                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("eFramework", "Error: " + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(req);
        return events;
    }

    private void setMarkers(GoogleMap googleMap, List<Evento> listEvents){
        for (Evento e: listEvents) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(e.getLatitude(), e.getLongitude()))
                    .title(e.getNome()).snippet(e.getDescricao()));
        }
    }

    private void setUpMap() {
        // Try to obtain the map from the SupportMapFragment.
        sMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_container, sMapFragment);
        fragmentTransaction.commit();
        sMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
//        setMarkers(googleMap);
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(40.417325, 40.417325))
//                .title("Hello!"));
    }

    public GoogleMap getMap() {
        return map;
    }
}
