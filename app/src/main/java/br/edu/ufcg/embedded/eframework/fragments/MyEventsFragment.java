package br.edu.ufcg.embedded.eframework.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.edu.ufcg.embedded.eframework.R;
import br.edu.ufcg.embedded.eframework.dao.DataSource;
import br.edu.ufcg.embedded.eframework.models.Evento;

/**
 * Created by treinamento-asus on 28/07/2016.
 */
public class MyEventsFragment extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DataSource dataSource = DataSource.getInstance(getContext());
        List<Evento> eventos = dataSource.getEventsInteresse();
        Log.d("TESTE", eventos.toString());
    }
}
