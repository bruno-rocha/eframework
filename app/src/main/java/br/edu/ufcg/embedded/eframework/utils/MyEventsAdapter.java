package br.edu.ufcg.embedded.eframework.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.edu.ufcg.embedded.eframework.R;
import br.edu.ufcg.embedded.eframework.models.Evento;

/**
 * Created by treinamento-asus on 01/08/2016.
 */
public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Evento> eventos;
    private Context mContext;

    public MyEventsAdapter(Context context, List<Evento> eventos){
        inflater = LayoutInflater.from(context);
        this.eventos = eventos;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Evento currentEvento = eventos.get(position);
        holder.title.setText(currentEvento.getNome());
        Picasso.with(mContext).load(eventos.get(position).getUrlFoto()).into(holder.icon);
    }

    @Override
    public int getItemCount() {

        return eventos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_title);
            icon = (ImageView) itemView.findViewById(R.id.item_icon);
        }
    }
}
