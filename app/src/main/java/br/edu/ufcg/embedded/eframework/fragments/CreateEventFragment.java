package br.edu.ufcg.embedded.eframework.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.edu.ufcg.embedded.eframework.R;

/**
 * Created by Treinamento Asus on 04/08/2016.
 */
public class CreateEventFragment extends Fragment {
    public Button create_event_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_event, container, false);

        create_event_button = (Button) view.findViewById(R.id.create_event_button);
        create_event_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
