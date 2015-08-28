package br.com.cocobongo.meusgames.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.cocobongo.meusgames.Constantes;
import br.com.cocobongo.meusgames.R;
import br.com.cocobongo.meusgames.modelos.Comentario;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gu on 27/08/15.
 */
public class ComentariosAdapter extends ArrayAdapter<Comentario> {

    public ComentariosAdapter(Context context, int resource, List<Comentario> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ComentarioViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comentario,
                    parent, false);
            viewHolder = new ComentarioViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ComentarioViewHolder) convertView.getTag();
        }

        Comentario comentario = getItem(position);
        if(null != comentario.getUsuario()){
            if(null != comentario.getUsuario().getFoto()){
                Picasso.with(getContext()).load(Constantes.getUrlImagem(comentario.getUsuario().getFoto()))
                        .into(viewHolder.imgUser);
            }
            viewHolder.textUser.setText(comentario.getUsuario().getNome());
        }
        viewHolder.textComentario.setText(comentario.getDescricao());

        return convertView;
    }

    public class ComentarioViewHolder {

        @Bind(R.id.img_user)
        ImageView imgUser;

        @Bind(R.id.text_user)
        TextView textUser;

        @Bind(R.id.text_comentario)
        TextView textComentario;

        public ComentarioViewHolder(View view){
            ButterKnife.bind(this, view);
        }

    }


}
