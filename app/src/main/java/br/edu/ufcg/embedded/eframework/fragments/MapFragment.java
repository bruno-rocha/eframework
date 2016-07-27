package br.edu.ufcg.embedded.eframework.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.embedded.eframework.R;
import br.edu.ufcg.embedded.eframework.activities.MainActivity;
import br.edu.ufcg.embedded.eframework.models.Evento;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final float ZOOM_SCALE = 12.2f;
    private Context mContext;
    private SupportMapFragment sMapFragment;
    private LocationManager locationManager;
    private String locationProvider;
    private android.location.LocationListener locationListener;
    private Location lastKnownLocation;
    private View rootView;

    private boolean markerDescriptionVisible;
    private boolean isMarkerSelected;


    private boolean myLocationWasClicked;
    private LatLng myLocation;
    private Location mLocation;
    private final GoogleMap.OnMyLocationChangeListener mLocationListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mLocation = location;
            if (myLocationWasClicked) {
                myLocationWasClicked = false;
                moveToMyLocation();
            }
        }
    };

    private static GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);


        setUpMap();
        mContext = getContext();
        final List<Evento> listEvents = getEvents();
        setHasOptionsMenu(true);

        locationProvider = LocationManager.GPS_PROVIDER;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                Log.i("latlong", "lat: " + location.getLatitude() + "\n long: " + location.getLongitude());
                zoomMapAtLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                zoomMapAtLocation(lastKnownLocation);
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

        return rootView;
    }

    private void moveToMyLocation() {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLocation, ZOOM_SCALE);
        map.animateCamera(cameraUpdate);
    }

    public static void zoomMapAtLocation(Location location) {
        if (location != null){
            LatLng lastKnownLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(lastKnownLatLng, ZOOM_SCALE);
            map.animateCamera(cameraUpdate);
        }
    }


    public List<Evento> getEvents() {
        final List<Evento> events = new ArrayList<>();
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mContext.getString(R.string.url_server),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
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
                                setMarker(map, evento);
                            } catch (Exception e) {
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

    private void setMarker(GoogleMap googleMap, Evento event) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(event.getLatitude(), event.getLongitude()))
                    .title(event.getNome()).snippet(event.getDescricao()));
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
        getEvents();

        googleMap.setOnMyLocationChangeListener(mLocationListener);

        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        googleMap.setOnMarkerClickListener(this);

        View parteTotalView = rootView.findViewById(R.id.tela_total);
        parteTotalView.setVisibility(View.INVISIBLE);
        View parte1View = rootView.findViewById(R.id.tela_parte1);
        disable(parte1View);
        View parte2View = rootView.findViewById(R.id.tela_parte2);
        disable(parte2View);
        View parte3View = rootView.findViewById(R.id.tela_parte3);
        disable(parte3View);
        View parte4View = rootView.findViewById(R.id.tela_parte4);
        disable(parte4View);
        View parte5View = rootView.findViewById(R.id.tela_parte5);
        disable(parte5View);
        View parte6View = rootView.findViewById(R.id.tela_parte6);
        disable(parte6View);
        View parte7View = rootView.findViewById(R.id.tela_parte7);
        disable(parte7View);
    }

    private void disable(View parte1View) {
        final View parteTotalView = rootView.findViewById(R.id.tela_total);


        parte1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (parteTotalView.getVisibility() == View.VISIBLE) {
                    parteTotalView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public static GoogleMap getMap() {
        return map;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.goto_cards:
                CardFragment card_fragment = new CardFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, card_fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            default:
                break;

        }
        return true;
    }

    @Override
    public boolean onMarkerClick(Marker markerAux) {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(markerAux.getPosition())
                .zoom(map.getCameraPosition().zoom)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        View parteTotalView = rootView.findViewById(R.id.tela_total);
        parteTotalView.setVisibility(View.VISIBLE);
        markerDescriptionVisible = true;
        isMarkerSelected = true;
        parteTotalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        preencheCaixa(markerAux);
        return true;
    }

    private void preencheCaixa(final Marker markerAux) {
        TextView tvNome = ((TextView) rootView.findViewById(R.id.nomeDialog));
        tvNome.setText(markerAux.getTitle());
        TextView tvDescricao = ((TextView) rootView.findViewById(R.id.nomeDescription));
        tvDescricao.setText(markerAux.getSnippet());
        ImageView ivRota = ((ImageView) rootView.findViewById(R.id.iv_rota));
//        TextView ivRotaName = ((TextView) rootView.findViewById(R.id.iv_rota_name));

        View.OnClickListener onTracarClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracar(markerAux);
            }
        };

        ivRota.setOnClickListener(onTracarClick);
//        ivRotaName.setOnClickListener(onTracarClick);
    }

    private void tracar(final Marker markerAux) {
        final LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { //if gps is disabled
//            displayPromptForEnablingGPS();
        } else {
            new TracaRota().execute(markerAux);
        }
    }

    private class TracaRota extends AsyncTask<Marker, Void, Location> {

        private Marker markerAsync;
        private ProgressDialog dialog;
        private boolean locationIsNull = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(getActivity(),
                    getString(R.string.aguarde), getString(R.string.capturando_posicao));
            dialog.setCancelable(true);
        }

        @Override
        protected Location doInBackground(Marker... params) {
            markerAsync = params[0];

            while (locationIsNull) {
                locationIsNull = mLocation == null;
            }
            return mLocation;
        }

        @Override
        protected void onPostExecute(Location myActualLocation) {
            super.onPostExecute(myActualLocation);
            LatLng markerPosition = markerAsync.getPosition();
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" + myActualLocation.getLatitude() + "," + myActualLocation.getLongitude()
                            + "&daddr=" + markerPosition.latitude + "," + markerPosition.longitude));
            intent.setComponent(new ComponentName("com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity"));
            myLocation = null;
            dialog.dismiss();
            startActivity(intent);
        }
    }
}
