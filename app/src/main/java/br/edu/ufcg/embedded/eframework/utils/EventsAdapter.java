package br.edu.ufcg.embedded.eframework.utils;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import br.edu.ufcg.embedded.eframework.R;
import br.edu.ufcg.embedded.eframework.dao.DataSource;
import br.edu.ufcg.embedded.eframework.models.Evento;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsHolder> {

    private LayoutInflater mLayoutInflater;
    private List<Evento> eventos;
    private Context context;
    private static final int DURATION = 250;

    static class EventsHolder extends RecyclerView.ViewHolder{

        private final ImageView img;
        public TextView itemTitle;
        public CardView cardView;
        public RecyclerView recyclerView;
        public CompoundButton star_button;
        public Button install_button;


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
            star_button = (ToggleButton) itemView.findViewById(R.id.star_btn);
            install_button = (Button) itemView.findViewById(R.id.install_button);



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
    public void onBindViewHolder(final EventsHolder viewHolder, final int i) {
        viewHolder.itemTitle.setText(eventos.get(i).getNome());
        viewHolder.star_button.setChecked(eventos.get(i).haveInteresse());
        Picasso.with(context).load(eventos.get(i).getUrlFoto()).into(viewHolder.img);
//        viewHolder.img.setBackgroundResource(eventos.);
        viewHolder.install_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setCancelable(true);
                dialog.setPositiveButton(context.getString(R.string.install), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, context.getString(R.string.installing), Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setTitle(eventos.get(i).getNome());
                dialog.setMessage(eventos.get(i).getDescricao());
                dialog.create();
                dialog.show();
            }
        });

        viewHolder.star_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.star_button.isChecked()) {
                    eventos.get(i).setInteresse(true);
                    Toast.makeText(context, "Você tem interesse neste evento", Toast.LENGTH_SHORT).show();
                } else {
                    eventos.get(i).setInteresse(false);
                    Toast.makeText(context, "Você não tem interesse neste evento", Toast.LENGTH_SHORT).show();
                }
                DataSource dataSource = DataSource.getInstance(context);
                dataSource.saveFavorito(eventos.get(i));
            }
        });

//        viewHolder.star_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    eventos.get(i).setInteresse(true);
//                    Toast.makeText(context, "Você tem interesse neste evento", Toast.LENGTH_SHORT).show();
//                } else {
//                    eventos.get(i).setInteresse(false);
//                    Toast.makeText(context, "Você não tem interesse neste evento", Toast.LENGTH_SHORT).show();
//                }
//                DataSource dataSource = DataSource.getInstance(context);
//                dataSource.saveFavorito(eventos.get(i));
//            }
//        });
    }

    public void swap(List<Evento> eventos){
        this.eventos.clear();
        this.eventos.addAll(eventos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }
}