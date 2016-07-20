package br.edu.ufcg.embedded.eframework.utils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.edu.ufcg.embedded.eframework.R;
import br.edu.ufcg.embedded.eframework.models.Evento;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsHolder> {

    private List<Evento> eventos;

    class EventsHolder extends RecyclerView.ViewHolder{

        public ImageView itemImage;
        public TextView itemTitle;

        public EventsHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.event_image);
            itemTitle = (TextView)itemView.findViewById(R.id.event_title);
        }
    }

    public EventsAdapter(List<Evento> lista_eventos){
        eventos = lista_eventos;
    }

    @Override
    public EventsHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        EventsHolder viewHolder = new EventsHolder(v);
        return new EventsHolder(v);
    }

    @Override
    public void onBindViewHolder(EventsHolder viewHolder, int i) {
        Evento evento = eventos.get(i);
        viewHolder.itemTitle.setText(evento.getNome());
        viewHolder.itemImage.setBackgroundColor(000000);
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }
}