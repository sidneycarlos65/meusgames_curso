package br.com.cocobongo.meusgames.servicos;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import br.com.cocobongo.meusgames.Constantes;
import br.com.cocobongo.meusgames.helpers.HttpHelper;
import br.com.cocobongo.meusgames.modelos.Usuario;

/**
 * Created by gu on 18/08/15.
 */
public class UsuarioService {

    private static UsuarioService INSTANCE;

    public static UsuarioService getInstance(){
        if(null == INSTANCE){
            INSTANCE = new UsuarioService();
        }
        return INSTANCE;
    }

    private UsuarioService(){

    }

    public Usuario cadastrar(String nome, String email, String senha, String endereco){

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", nome);
        params.put("email", email);
        params.put("senha", senha);
        params.put("endereco", endereco);

        try {
            HttpHelper httpHelper = new HttpHelper();
            httpHelper.setContentType("application/json");
            String jsonUsuario = httpHelper.doPost(Constantes.URL_SIGNUP, params, "UTF-8");
            if(null != jsonUsuario){
                Gson gson = new Gson();
                Usuario usuario = gson.fromJson(jsonUsuario, Usuario.class);
                return usuario;
            }
        } catch (IOException e) {
            Log.e("UsuarioService", e.getMessage());
        }

        return null;
    }

}
