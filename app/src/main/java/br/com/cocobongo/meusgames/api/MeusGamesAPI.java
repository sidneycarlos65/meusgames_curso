package br.com.cocobongo.meusgames.api;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.io.File;
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
                .as(new TypeToken<List<Game>>() {
                })
                .withResponse().setCallback(callback);

    }

    public void cadastrarGame(Game game, FutureCallback<Response<Game>> callback){
        String token = MeusGamesApplication.token;
        Ion.with(context).load(Constantes.URL_GAMES).addHeader("X-Access-Token", token)
                .setJsonPojoBody(game).as(Game.class).withResponse().setCallback(callback);
    }

    public void upload(String idGame, File image, FutureCallback<Response<Game>> callback){
        String token = MeusGamesApplication.token;
        Ion.with(context).load(Constantes.URL_GAMES_UPLOAD_IMAGE + idGame)
                .addHeader("X-Access-Token", token)
                .setMultipartFile("file", image).as(Game.class).withResponse()
                .setCallback(callback);

    }

    public void registrarDeviceToken(String token, String idUsuario,
                                     FutureCallback<Usuario> callback){
        String url = String.format(Constantes.URL_DEVICE_TOKEN, idUsuario, token);
        String tokenAcesso = MeusGamesApplication.token;
        Ion.with(context).load(url).addHeader("X-Access-Token", tokenAcesso).as(Usuario.class)
                .setCallback(callback);
    }

}
