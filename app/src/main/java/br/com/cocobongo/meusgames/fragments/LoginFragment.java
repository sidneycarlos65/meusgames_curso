package br.com.cocobongo.meusgames.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import br.com.cocobongo.meusgames.CadastroActivity;
import br.com.cocobongo.meusgames.R;
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
            listener.onLogin(new Usuario());  //TODO: mudar retorno para quando tiver o servico
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
}
