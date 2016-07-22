package br.edu.ufcg.embedded.eframework.utils;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.edu.ufcg.embedded.eframework.R;
import br.edu.ufcg.embedded.eframework.models.Evento;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsHolder> {

    private LayoutInflater mLayoutInflater;
    private List<Evento> eventos;
    private Context context;

    static class EventsHolder extends RecyclerView.ViewHolder{

        private final ImageView img;
        public TextView itemTitle;
        public CardView cardView;
        public RecyclerView recyclerView;

        public EventsHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            itemTitle = (TextView)itemView.findViewById(R.id.cardTitle);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
            img = (ImageView) itemView.findViewById(R.id.cardImage);


        }

    }

    public EventsAdapter(List<Evento> lista_eventos, Context context){
        eventos = lista_eventos;
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public EventsHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.card_layout, viewGroup, false);

        return new EventsHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsHolder viewHolder, final int i) {
        viewHolder.itemTitle.setText(eventos.get(i).getNome());
        Picasso.with(context).load(eventos.get(i).getUrlFoto()).into(viewHolder.img);
//        viewHolder.img.setBackgroundResource(eventos.);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setCancelable(false);
                dialog.setPositiveButton("Instalar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Instalando evento...", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setTitle(eventos.get(i).getNome());
                dialog.setMessage(eventos.get(i).getDescricao());
                dialog.create();
                dialog.show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }
}