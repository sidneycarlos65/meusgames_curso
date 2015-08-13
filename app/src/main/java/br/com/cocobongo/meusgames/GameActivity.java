package br.com.cocobongo.meusgames;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import br.com.cocobongo.meusgames.fragments.GameDetalheFragment;
import br.com.cocobongo.meusgames.modelos.Game;
import butterknife.ButterKnife;

public class GameActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        initToolbar();

        Game game = (Game) getIntent().getSerializableExtra("game");
        GameDetalheFragment gameDetalheFragment = GameDetalheFragment.newInstance(game);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, gameDetalheFragment).commit();
    }

}
