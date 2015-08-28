package br.com.cocobongo.meusgames.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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

import com.koushikdutta.async.future.FutureCallback;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.List;

import br.com.cocobongo.meusgames.ComentariosActivity;
import br.com.cocobongo.meusgames.Constantes;
import br.com.cocobongo.meusgames.R;
import br.com.cocobongo.meusgames.api.MeusGamesAPI;
import br.com.cocobongo.meusgames.database.GameDAO;
import br.com.cocobongo.meusgames.modelos.Comentario;
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
                    .load(Constantes.getUrlImagem(game.getImage()))
                    .error(R.drawable.error)
                    .placeholder(R.drawable.anon_user)
                    .into(imgGame);

        }

    }

    private boolean isFavorito(String gameId) {
        try {
            GameDAO gameDAO = new GameDAO(getActivity());
            return null != gameDAO.findById(gameId);
        } catch (SQLException e) {
            Log.e("GameDetalheFragment", e.getMessage(), e);
        }
        return false;
    }

    @OnClick(R.id.btn_ver_mais)
    public void onClickBtnVerMais(View view) {
        Intent intent = new Intent(getActivity(), ComentariosActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);
    }

    private void addComentarios() {
        new MeusGamesAPI(getActivity()).getComentarios(game.getId(), new FutureCallback<List<Comentario>>() {
            @Override
            public void onCompleted(Exception e, List<Comentario> result) {
                int tamanho = result.size();
                if(tamanho > 3){
                    tamanho = 3;
                }
                for (int i = 0; i < tamanho; i++) {
                    TableRow row = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.row_comentario,
                            tableComentarios, false);
                    ImageView imgUser = (ImageView) row.findViewById(R.id.img_user);
                    TextView nome = (TextView) row.findViewById(R.id.txt_nome_usuario);
                    TextView comentario = (TextView) row.findViewById(R.id.txt_comentario);

                    nome.setText(result.get(i).getNomeUsuario());
                    comentario.setText(result.get(i).getDescricao());

                    tableComentarios.addView(row);
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_game, menu);

        if(isFavorito(game.getId())){
            MenuItem item = menu.findItem(R.id.action_favoritar);
            item.setIcon(R.drawable.ic_favorite_white_24dp);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.action_favoritar) {

                GameDAO dao = new GameDAO(getActivity());
                if(!isFavorito(game.getId())){
                    boolean result = dao.add(game);
                    String msg = getString(R.string.msg_erro_favoritar);

                    if(result){
                        msg = getString(R.string.msg_adicionado_favoritos);
                        item.setIcon(R.drawable.ic_favorite_white_24dp);
                    }
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean result = dao.remove(game);
                    String msg = getString(R.string.msg_erro_remover_favorito);
                    if(result){
                        msg = getString(R.string.msg_removido_favoritos);
                        item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                    }
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }

            }
            else if (id == R.id.action_adicionar_biblioteca) {
                Toast.makeText(getActivity(), "Adicionado à biblioteca", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            Log.e("GameDetalheFragment", e.getMessage(), e);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void onClickEmail(View view){

        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:sidneycarlos65@gmail.com"));
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"meusgames@gmail.com"});
        email.putExtra(Intent.EXTRA_TEXT, game.getSiteOficial());
        email.putExtra(Intent.EXTRA_SUBJECT, game.getNome());

        if(email.resolveActivity(getActivity().getPackageManager()) != null){
            startActivity(email);
        }

    }


    @OnClick(R.id.img_game)
    public void onClickShare(View view){

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, game.getNome());
        if(share.resolveActivity(getActivity().getPackageManager()) != null){
            startActivity(share);
        }

    }

}
