package br.edu.ufcg.embedded.eframework.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.edu.ufcg.embedded.eframework.R;
import br.edu.ufcg.embedded.eframework.dao.DataSource;
import br.edu.ufcg.embedded.eframework.models.Evento;
import br.edu.ufcg.embedded.eframework.utils.MyEventsAdapter;

public class MyEventsFragment extends android.support.v4.app.Fragment {

    private Context mContext;
    private RecyclerView recyclerView;
    private MyEventsAdapter adapter;
    private List<Evento> eventos;
    private DataSource dataSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);

        dataSource = DataSource.getInstance(getContext());

//        eventos = dataSource.getEventsInteresse();

        mContext = getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.my_events_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

//        adapter = new MyEventsAdapter(mContext, eventos);
//        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventos = dataSource.getEventsInteresse();
        adapter = new MyEventsAdapter(mContext, eventos);
        recyclerView.setAdapter(adapter);
    }
}
