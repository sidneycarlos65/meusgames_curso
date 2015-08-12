package br.com.cocobongo.meusgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.com.cocobongo.meusgames.adapters.GamesAdapter;
import br.com.cocobongo.meusgames.modelos.Game;
import butterknife.Bind;
import butterknife.ButterKnife;

public class GamesActivity extends BaseActivity implements GamesAdapter.GamesAdapterListener {

    @Bind(R.id.lista_games)
    RecyclerView listaGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        ButterKnife.bind(this);
        initToolbar();

//        Game game1 = new Game();
//        game1.setNome("Fifa 2015");
//        game1.setAno(2015);
//        game1.setPontuacao(9.5);
//        game1.setImage("http://www.geracaogames.com.br/images_produtos/2e6acd34e5.jpg");
//
//        Game game2 = new Game();
//        game2.setNome("Dota 2");
//        game2.setAno(2013);
//        game2.setPontuacao(5);
//        game2.setImage("http://shadowkeepers.com.br/images/skill/xIMG478082389_thumb.jpg.pagespeed.ic.Q5yh0uwH2r.jpg");
//
//        Game game3 = new Game();
//        game3.setNome("Clash of Clans");
//        game3.setAno(2012);
//        game3.setPontuacao(10);
//        game3.setImage("http://shadowkeepers.com.br/images/skill/xIMG1754449374_thumb.jpg.pagespeed.ic.IytOTaLgn_.jpg");
//        List<String> categorias = new ArrayList<String>();
//        categorias.add("Estratégia");
//        game3.setCategorias(categorias);
//
//        Game game4 = new Game();
//        game4.setNome("Batman Arkham Knight");
//        game4.setAno(2015);
//        game4.setPontuacao(8);
//        game4.setImage("http://gamewebster.net/wp-content/uploads/2015/06/BatmanArkhamKnight-300x400.jpg");

//        List<Game> games = new ArrayList<Game>();
//        games.add(game1);
//        games.add(game2);
//        games.add(game3);
//        games.add(game4);

        GamesAdapter gamesAdapter = new GamesAdapter(MeusGamesApplication.games, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        listaGames.setLayoutManager(layoutManager);
        listaGames.setHasFixedSize(true);  //otimiza a recycleview, falando que a dimensao do item nao muda
        listaGames.setAdapter(gamesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_games, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_game) {
            Intent intent = new Intent(this, CadastroGameActivity.class);
            startActivityForResult(intent, 100);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGameSelected(Game game) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(RESULT_CANCELED == resultCode){
            return;
        }

        if(100 == requestCode && null != data){
            MeusGamesApplication.games.add((Game) data.getSerializableExtra("result"));
        }
        recreate();
    }
}
