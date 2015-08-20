package br.com.cocobongo.meusgames.api;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.List;

import br.com.cocobongo.meusgames.Constantes;
import br.com.cocobongo.meusgames.MeusGamesApplication;
import br.com.cocobongo.meusgames.modelos.Game;
import br.com.cocobongo.meusgames.modelos.Usuario;

/**
 * Created by gu on 19/08/15.
 */
public class MeusGamesAPI {

    private Context context;

    public MeusGamesAPI(Context context) {
        this.context = context;
    }

    public void login(JsonObject param, FutureCallback<Response<Usuario>> callback){

        Ion.with(context).load(Constantes.URL_LOGIN).setJsonObjectBody(param).as(Usuario.class)
                .withResponse().setCallback(callback);

    }

    public void games(FutureCallback<Response<List<Game>>> callback){
        String token = MeusGamesApplication.token;
        Ion.with(context).load(Constantes.URL_GAMES).addHeader("X-Access-Token", token)
                .as(new TypeToken<List<Game>>() {})
                .withResponse().setCallback(callback);

    }

    public void cadastrarGame(Game game, FutureCallback<Response<Game>> callback){
        String token = MeusGamesApplication.token;
        Ion.with(context).load(Constantes.URL_GAMES).addHeader("X-Access-Token", token)
                .setJsonPojoBody(game).as(Game.class).withResponse().setCallback(callback);
    }

}
