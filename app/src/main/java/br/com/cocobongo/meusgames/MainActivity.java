package br.com.cocobongo.meusgames;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;

import br.com.cocobongo.meusgames.adapters.GamesAdapter;
import br.com.cocobongo.meusgames.fragments.GameDetalheFragment;
import br.com.cocobongo.meusgames.fragments.GamesListFragment;
import br.com.cocobongo.meusgames.fragments.LoginFragment;
import br.com.cocobongo.meusgames.helpers.Helpers;
import br.com.cocobongo.meusgames.modelos.Game;
import br.com.cocobongo.meusgames.modelos.Usuario;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements LoginFragment.LoginFragmentListener {

    @Bind(R.id.container_list)
    @Nullable
    FrameLayout containerList;

    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Helpers.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();

        if (MeusGamesApplication.token == null) {
            loginFragment = LoginFragment.newInstance(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, loginFragment).commit();
        } else {
            addFragmentGames();
        }
    }

    @Override
    public void onLogin(Usuario usuario) {

        addFragmentGames();
    }

    private void addFragmentGames() {
        final FragmentManager fragmentManager = getSupportFragmentManager();

        if (Helpers.isTablet(this) && containerList != null) {
            containerList.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().remove(loginFragment).commit();
            GamesListFragment gamesFragment = (GamesListFragment) fragmentManager
                    .findFragmentById(R.id.fragment_games_list);

            gamesFragment.setGamesAdapterListener(new GamesAdapter.GamesAdapterListener() {
                @Override
                public void onGameSelected(Game game) {
                    GameDetalheFragment gameDetalheFragment = GameDetalheFragment.newInstance(game);
                    fragmentManager.beginTransaction().replace(R.id.container, gameDetalheFragment)
                            .commit();
                }
            });

        } else {
            GamesListFragment gamesListFragment = GamesListFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.container, gamesListFragment).commit();
        }
    }

}
