package br.edu.ufcg.embedded.eframework.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.embedded.eframework.R;
import br.edu.ufcg.embedded.eframework.dao.DataSource;
import br.edu.ufcg.embedded.eframework.models.Evento;
import br.edu.ufcg.embedded.eframework.utils.MyEventsAdapter;

/**
 * Created by treinamento-asus on 28/07/2016.
 */
public class MyEventsFragment extends android.support.v4.app.Fragment {

    private Context mContext;
    private RecyclerView recyclerView;
    private MyEventsAdapter adapter;
    private List<Evento> eventos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        mContext = getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.my_events_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        eventos = getEvents();
        return view;
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

                        adapter = new MyEventsAdapter(mContext, eventos);
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
}
