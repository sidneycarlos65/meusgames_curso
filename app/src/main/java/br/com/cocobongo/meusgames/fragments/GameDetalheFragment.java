package br.com.cocobongo.meusgames.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.com.cocobongo.meusgames.R;
import br.com.cocobongo.meusgames.modelos.Game;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gu on 12/08/15.
 */
public class GameDetalheFragment extends Fragment {

    @Bind(R.id.img_game)
    ImageView imgGame;

    @Bind(R.id.text_nome)
    TextView textNome;

    @Bind(R.id.text_pontuacao)
    TextView textPontuacao;

    @Bind(R.id.text_ano)
    TextView textAno;

    @Bind(R.id.text_categorias)
    TextView textCategorias;

    @Bind(R.id.table_comentarios)
    TableLayout tableComentarios;

    private Game game;
    boolean favorito = false;

    public static GameDetalheFragment newInstance(Game game) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("game", game);
        GameDetalheFragment gameDetalheFragment = new GameDetalheFragment();
        gameDetalheFragment.setArguments(bundle);
        return gameDetalheFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalhe_game, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        game = (Game) getArguments().getSerializable("game");

        if(null != game){

            textNome.setText(game.getNome());
            textAno.setText(getString(R.string.lbl_ano, game.getAno()));
            textPontuacao.setText(getString(R.string.lbl_pontuacao, game.getPontuacao()));

            if(null != game.getCategorias()){
                textCategorias.setText(getString(R.string.lbl_categorias,
                        TextUtils.join(" ,", game.getCategorias())));
            }

            addComentarios();
            Picasso.with(getActivity())
                    .load(game.getImage())
                    .error(R.drawable.error)
                    .placeholder(R.drawable.anon_user)
                    .into(imgGame);

        }

    }

    @OnClick(R.id.btn_ver_mais)
    public void onClickBtnVerMais(View view) {
        addComentarios();
    }

    private void addComentarios() {
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.row_comentario,
                    tableComentarios, false);
            ImageView imgUser = (ImageView) row.findViewById(R.id.img_user);
            TextView nome = (TextView) row.findViewById(R.id.txt_nome_usuario);
            TextView comentario = (TextView) row.findViewById(R.id.txt_comentario);

            nome.setText("Usuário " + i);
            comentario.setText("Comentário " + i);

            tableComentarios.addView(row);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_game, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favoritar) {
            if (!favorito) {
                Toast.makeText(getActivity(), "Adicionado aos favoritos", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_favorite_white_24dp);
                favorito = true;
            } else {
                Toast.makeText(getActivity(), "Removido dos favoritos", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                favorito = false;
            }
        } else if (id == R.id.action_adicionar_biblioteca) {
            Toast.makeText(getActivity(), "Adicionado à biblioteca", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
