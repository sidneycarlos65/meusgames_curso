package br.com.cocobongo.meusgames.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.cocobongo.meusgames.Constantes;
import br.com.cocobongo.meusgames.R;
import br.com.cocobongo.meusgames.modelos.Game;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gu on 10/08/15.
 */
public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder>{

    private List<Game> games;
    private Context context;
    private GamesAdapterListener listener;

    public GamesAdapter(@NonNull List<Game> games, Context context, GamesAdapterListener listener) {
        this.games = games;
        this.context = context;
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.card_view_game)
        CardView cardViewGame;

        @Bind(R.id.image_game)
        ImageView imageGame;

        @Bind(R.id.nome_game)
        TextView nomeGame;

        @Bind(R.id.ano_game)
        TextView anoGame;

        @Bind(R.id.pontuacao_game)
        TextView pontuacaoGame;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_view_game)
        public void OnClickItemList(View view){
            if(null != listener){
                Game game = (Game) view.getTag();
                listener.onGameSelected(game);
            }
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Game game = games.get(position);
        holder.nomeGame.setText(game.getNome());
        holder.anoGame.setText(context.getString(R.string.lbl_ano, game.getAno()));
        holder.pontuacaoGame.setText(context.getString(R.string.lbl_pontuacao, game.getPontuacao()));
        Picasso.with(context).load(Constantes.getUrlImagem(game.getImage())).into(holder.imageGame);
        holder.cardViewGame.setTag(game);

    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public interface GamesAdapterListener{

        void onGameSelected(Game game);

    }

}
