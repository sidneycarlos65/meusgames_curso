package br.com.cocobongo.meusgames.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import java.util.List;

import br.com.cocobongo.meusgames.CadastroGameActivity;
import br.com.cocobongo.meusgames.GameActivity;
import br.com.cocobongo.meusgames.R;
import br.com.cocobongo.meusgames.adapters.GamesAdapter;
import br.com.cocobongo.meusgames.api.MeusGamesAPI;
import br.com.cocobongo.meusgames.helpers.Helpers;
import br.com.cocobongo.meusgames.modelos.Game;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gu on 12/08/15.
 */
public class GamesListFragment extends Fragment implements GamesAdapter.GamesAdapterListener {

    @Bind(R.id.lista_games)
    RecyclerView listaGames;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private GamesAdapter.GamesAdapterListener gamesAdapterListener;
    private GamesAdapter gamesAdapter;
    private MeusGamesAPI meusGamesAPI;

    public void setGamesAdapterListener(GamesAdapter.GamesAdapterListener gamesAdapterListener) {
        this.gamesAdapterListener = gamesAdapterListener;
    }

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        listaGames.setLayoutManager(layoutManager);
        listaGames.setHasFixedSize(true);  //otimiza a recycleview, falando que a dimensao do item nao muda

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getGames();
            }
        });

        getGames();

    }

    private void getGames() {

        final ProgressDialog progressDialog =
                ProgressDialog.show(getActivity(), getString(R.string.app_name), "Aguarde...");

        meusGamesAPI = new MeusGamesAPI(getActivity());
        meusGamesAPI.games(new FutureCallback<Response<List<Game>>>() {
            @Override
            public void onCompleted(Exception e, Response<List<Game>> result) {
                if (200 != result.getHeaders().code()) {
                    Toast.makeText(getActivity(), "Não foi possível recuperar os games!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                gamesAdapter = new GamesAdapter(result.getResult(), getActivity(),
                        GamesListFragment.this);
                listaGames.setAdapter(gamesAdapter);

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    @Override
    public void onGameSelected(Game game) {

        if (Helpers.isTablet(getActivity()) && gamesAdapterListener != null) {
            gamesAdapterListener.onGameSelected(game);
        } else {
            Intent intent = new Intent(getActivity(), GameActivity.class);
            intent.putExtra("game", game);
            startActivity(intent);
        }

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
            startActivityForResult(intent, 103);
        } else if (id == R.id.action_sync_games) {
            getGames();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_CANCELED == resultCode) {
            return;
        }

        if (103 == requestCode && resultCode == Activity.RESULT_OK && null != gamesAdapter) {
//            gamesAdapter.notifyDataSetChanged();
            getGames();
        }
    }
}
