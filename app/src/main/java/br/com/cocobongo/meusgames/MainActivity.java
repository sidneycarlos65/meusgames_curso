package br.com.cocobongo.meusgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import br.com.cocobongo.meusgames.fragments.GamesListFragment;
import br.com.cocobongo.meusgames.fragments.LoginFragment;
import br.com.cocobongo.meusgames.modelos.Game;
import br.com.cocobongo.meusgames.modelos.Usuario;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements LoginFragment.LoginFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();

        LoginFragment loginFragment = LoginFragment.newInstance(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, loginFragment).commit();
    }

    @Override
    public void onLogin(Usuario usuario) {
        GamesListFragment gamesListFragment = GamesListFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, gamesListFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_CANCELED == resultCode) {
            return;
        }

        if (100 == requestCode && null != data) {
            MeusGamesApplication.games.add((Game) data.getSerializableExtra("result"));
        }
        recreate();
    }
}
