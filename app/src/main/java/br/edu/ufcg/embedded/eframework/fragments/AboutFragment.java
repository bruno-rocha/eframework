package br.edu.ufcg.embedded.eframework.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.edu.ufcg.embedded.eframework.R;

/**
 * Created by Treinamento Asus on 04/08/2016.
 */
public class AboutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_layout, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
