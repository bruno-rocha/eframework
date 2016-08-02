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
        public ImageButton expand_button;
        public TextView itemResume;
        public ViewGroup linear_layout_details;
        public TextView itemDetails;
        public CompoundButton star_button;


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
            expand_button = (ImageButton) itemView.findViewById(R.id.expand_button);
            itemResume = (TextView) itemView.findViewById(R.id.cardResume);
            itemDetails = (TextView) itemView.findViewById(R.id.cardDetails);
            linear_layout_details = (ViewGroup) itemView.findViewById(R.id.layout_expand);
            star_button = (ToggleButton) itemView.findViewById(R.id.star_btn);

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
        Picasso.with(context).load(eventos.get(i).getUrlFoto()).into(viewHolder.img);
//        viewHolder.img.setBackgroundResource(eventos.);
        viewHolder.expand_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (viewHolder.linear_layout_details.getVisibility() == View.GONE) {
                    ExpandAndCollapseViewUtil.expand(viewHolder.linear_layout_details, DURATION);
                    viewHolder.itemDetails.setText(eventos.get(i).getDescricao());
                    viewHolder.expand_button.setImageResource(R.mipmap.ic_more);
                    rotate(viewHolder, 180.0f);

                } else {
                    ExpandAndCollapseViewUtil.collapse(viewHolder.linear_layout_details, DURATION);
                    viewHolder.expand_button.setImageResource(R.mipmap.ic_less);
                    rotate(viewHolder, -180.0f);

                }

            }

        });

        viewHolder.expand_button.setImageResource(R.mipmap.ic_more);


        viewHolder.star_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    eventos.get(i).setInteresse(true);
                    Toast.makeText(context, "Você tem interesse neste evento", Toast.LENGTH_SHORT).show();
                } else {
                    eventos.get(i).setInteresse(false);
                    Toast.makeText(context, "Você não tem interesse neste evento", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void rotate(EventsHolder viewHolder, float toDegrees) {
        Animation animation = new RotateAnimation(0.0f, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(DURATION);
        viewHolder.expand_button.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }
}