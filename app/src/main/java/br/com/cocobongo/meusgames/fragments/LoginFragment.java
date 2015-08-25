package br.com.cocobongo.meusgames.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import br.com.cocobongo.meusgames.CadastroActivity;
import br.com.cocobongo.meusgames.Constantes;
import br.com.cocobongo.meusgames.MeusGamesApplication;
import br.com.cocobongo.meusgames.R;
import br.com.cocobongo.meusgames.api.MeusGamesAPI;
import br.com.cocobongo.meusgames.helpers.HttpHelper;
import br.com.cocobongo.meusgames.modelos.Usuario;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gu on 12/08/15.
 */
public class LoginFragment extends Fragment {

    @Bind(R.id.edit_login)
    EditText editLogin;

    @Bind(R.id.edit_senha)
    EditText editSenha;

    private MeusGamesAPI meusGamesAPI;

    private LoginFragmentListener listener;

    public static LoginFragment newInstance(LoginFragmentListener listener) {
        LoginFragment fragment = new LoginFragment();
        fragment.setListener(listener);
        return fragment;
    }

    /**
     * Um fragment não pode ter um construtor com parametros
     */
    public LoginFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meusGamesAPI = new MeusGamesAPI(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setListener(LoginFragmentListener listener) {
        this.listener = listener;
    }

    public interface LoginFragmentListener{
        void onLogin(Usuario usuario);
    }

    @OnClick(R.id.button_ok)
    public void onClickBtnOk(View view){
        if(!validaForm()){

            String email = editLogin.getText().toString();
            String senha = editSenha.getText().toString();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("senha", senha);

            final ProgressDialog progressDialog =
                    ProgressDialog.show(getActivity(), getString(R.string.app_name), "Aguarde...");
            progressDialog.show();

            meusGamesAPI.login(jsonObject, new FutureCallback<Response<Usuario>>() {
                @Override
                public void onCompleted(Exception e, Response<Usuario> result) {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    int status = result.getHeaders().code();

                    if (status != 200 || e != null) {
                        Toast.makeText(getActivity(), "Usuário não encontrado",
                                Toast.LENGTH_LONG).show();

                        return;
                    }

                    MeusGamesApplication app =
                            (MeusGamesApplication) getActivity().getApplication();
                    Usuario usuario = result.getResult();
                    app.saveToken(usuario.getToken(), usuario.getValidadeToken(), usuario.getId());

                    listener.onLogin(usuario);
                }
            });

        }
    }

    @OnClick(R.id.button_cadastrar)
    public void onClickBtnCadastrar(View view){
        Intent intent = new Intent(getActivity(), CadastroActivity.class);
        startActivity(intent);
    }

    private boolean validaForm(){

        String login = editLogin.getText().toString();
        String senha = editSenha.getText().toString();
        boolean error = false;

        if(TextUtils.isEmpty(login)){
            editLogin.setError(getString(R.string.msg_error_login));
            error = true;
            YoYo.with(Techniques.Shake)
                    .duration(300)
                    .playOn(editLogin);
        }

        if(TextUtils.isEmpty(senha)){
            editSenha.setError(getString(R.string.msg_error_senha));
            YoYo.with(Techniques.Shake)
                    .duration(300)
                    .playOn(editSenha);
            error = true;
        }

        return error;
    }

    private class LoginAyncTask extends AsyncTask<JsonObject, Void, Usuario>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), getString(R.string.app_name),
                    "Aguarde...");
        }

        @Override
        protected Usuario doInBackground(JsonObject... params) {

            String param = params[0].toString();

            try {
                HttpHelper httpHelper = new HttpHelper();
                httpHelper.setContentType("application/json");
                String jsonUsuario = httpHelper.doPost(Constantes.URL_LOGIN, param, "UTF-8");
                return new Gson().fromJson(jsonUsuario, Usuario.class);
            } catch (IOException e) {
                Log.e("LoginAsyncTask", e.getMessage());
            } catch (JsonSyntaxException jse){
                return null;
            }

            return null;

        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            super.onPostExecute(usuario);
            if(null != progressDialog && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            if(usuario != null){
                Toast.makeText(getActivity(), "Bem vindo " + usuario.getNome(), Toast.LENGTH_SHORT)
                        .show();
                listener.onLogin(usuario);
            }
            else{
                Toast.makeText(getActivity(), "Login/Senha inválidos", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
