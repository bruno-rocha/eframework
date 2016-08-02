package br.edu.ufcg.embedded.eframework.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.embedded.eframework.R;
import br.edu.ufcg.embedded.eframework.activities.MainActivity;
import br.edu.ufcg.embedded.eframework.dao.DataSource;
import br.edu.ufcg.embedded.eframework.models.Evento;
import br.edu.ufcg.embedded.eframework.utils.EventsAdapter;

public class CardFragment extends Fragment {

    private Context mContext;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Evento> eventos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_card, container, false);
        mContext = getContext();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        eventos = getEvents();

        setHasOptionsMenu(true);

        return view;
    }

    private void doTheSearch(String query) {
        String string = removerAcentos(query);
        ArrayList<Evento> result = new ArrayList<Evento>();
        DataSource dataSource = DataSource.getInstance(getContext());
        List<Evento> listEvents = dataSource.getEvents();
        if (!string.equals("")) {
            for (Evento item : listEvents) {
                String nome = removerAcentos(item.getNome().toLowerCase());
                if (nome.contains(query.toLowerCase())) {
                    result.add(item);
                }
            }
        }

        updateCards(listEvents, result, query);
    }

    private void updateCards(List<Evento> eventos, ArrayList<Evento> result, String busca) {
        if (result.size() > 0) {
            setAdapter(new EventsAdapter(result, getContext()));
        } else {
            if (busca.equals("")) {
                setAdapter(new EventsAdapter(eventos, getContext()));
            } else {
                setAdapter(new EventsAdapter(result, getContext()));
            }
        }
    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_card, menu);

        final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = (MenuItem) menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doTheSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doTheSearch(newText);
                return false;
            }

        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.goto_map:
//                MapFragment map_fragment = new MapFragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container, map_fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//
//            default:
//                break;
//
//        }
        return true;
    }


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
                                Evento evento = new Evento(nome, descricao, latitude, longitude, url_foto, false);
                                Log.d("TAG", evento.toString());
                                events.add(evento);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        adapter = new EventsAdapter(eventos, mContext);
                        recyclerView.setAdapter(adapter);

                        if (events.size() > 0){
                            DataSource dataSource = DataSource.getInstance(getContext());
                            dataSource.saveAllEventos(eventos);
                        }
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

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }
}
