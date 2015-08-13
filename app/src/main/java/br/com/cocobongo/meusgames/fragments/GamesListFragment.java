package br.com.cocobongo.meusgames.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.cocobongo.meusgames.CadastroGameActivity;
import br.com.cocobongo.meusgames.GameActivity;
import br.com.cocobongo.meusgames.MeusGamesApplication;
import br.com.cocobongo.meusgames.R;
import br.com.cocobongo.meusgames.adapters.GamesAdapter;
import br.com.cocobongo.meusgames.modelos.Game;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gu on 12/08/15.
 */
public class GamesListFragment extends Fragment implements GamesAdapter.GamesAdapterListener {

    @Bind(R.id.lista_games)
    RecyclerView listaGames;

    public static GamesListFragment newInstance() {
        return new GamesListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_games, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListaGames();
        GamesAdapter gamesAdapter = new GamesAdapter(MeusGamesApplication.games, getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false);
        listaGames.setLayoutManager(layoutManager);
        listaGames.setHasFixedSize(true);  //otimiza a recycleview, falando que a dimensao do item nao muda
        listaGames.setAdapter(gamesAdapter);
    }

    private void initListaGames(){
        Game game1 = new Game();
        game1.setNome("Fifa 2015");
        game1.setAno(2015);
        game1.setPontuacao(9.5);
        game1.setImage("http://www.geracaogames.com.br/images_produtos/2e6acd34e5.jpg");

        Game game2 = new Game();
        game2.setNome("Dota 2");
        game2.setAno(2013);
        game2.setPontuacao(5);
        game2.setImage("http://shadowkeepers.com.br/images/skill/xIMG478082389_thumb.jpg.pagespeed.ic.Q5yh0uwH2r.jpg");

        Game game3 = new Game();
        game3.setNome("Clash of Clans");
        game3.setAno(2012);
        game3.setPontuacao(10);
        game3.setImage("http://shadowkeepers.com.br/images/skill/xIMG1754449374_thumb.jpg.pagespeed.ic.IytOTaLgn_.jpg");
        List<String> categorias = new ArrayList<String>();
        categorias.add("Estratégia");
        game3.setCategorias(categorias);

        Game game4 = new Game();
        game4.setNome("Batman Arkham Knight");
        game4.setAno(2015);
        game4.setPontuacao(8);
        game4.setImage("http://gamewebster.net/wp-content/uploads/2015/06/BatmanArkhamKnight-300x400.jpg");

//        List<Game> games = new ArrayList<Game>();
        MeusGamesApplication.games.clear();
        MeusGamesApplication.games.add(game1);
        MeusGamesApplication.games.add(game2);
        MeusGamesApplication.games.add(game3);
        MeusGamesApplication.games.add(game4);
    }

    @Override
    public void onGameSelected(Game game) {
        Intent intent = new Intent(getActivity(), GameActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_games, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add_game) {
            Intent intent = new Intent(getActivity(), CadastroGameActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
