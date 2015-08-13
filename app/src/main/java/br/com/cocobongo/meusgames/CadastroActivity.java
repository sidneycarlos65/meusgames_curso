package br.com.cocobongo.meusgames;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CadastroActivity extends BaseActivity {

    @Bind(R.id.edit_nome)
    EditText editNome;

    @Bind(R.id.edit_data)
    EditText editData;

    @Bind(R.id.edit_email)
    EditText editEmail;

    @Bind(R.id.edit_endereco)
    EditText editEndereco;

    @Bind(R.id.edit_senha)
    EditText editSenha;

    @Bind(R.id.edit_confirmar_senha)
    EditText editConfirmarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        ButterKnife.bind(this);
        initToolbar();
    }

    @OnClick(R.id.btn_ok)
    public void onClickBtnOk(View view) {
        if(!validaForm()){
            finish();
        }
    }

    private boolean validaForm() {

        String nome = editNome.getText().toString();
        String data = editData.getText().toString();
        String email = editEmail.getText().toString();
        String endereco = editEndereco.getText().toString();
        String senha = editSenha.getText().toString();
        String confirmaSenha = editConfirmarSenha.getText().toString();
        boolean error = false;

        if (TextUtils.isEmpty(nome)) {
            editNome.setError(getString(R.string.required_nome));
            error = true;
            YoYo.with(Techniques.Shake).duration(300).playOn(editNome);
        }

        if (TextUtils.isEmpty(data)) {
            editData.setError(getString(R.string.required_data_nascimento));
            error = true;
            YoYo.with(Techniques.Shake).duration(300).playOn(editData);
        }

        if (TextUtils.isEmpty(email)) {
            editEmail.setError(getString(R.string.required_email));
            error = true;
            YoYo.with(Techniques.Shake).duration(300).playOn(editEmail);
        }

        if (TextUtils.isEmpty(endereco)) {
            editEndereco.setError(getString(R.string.required_endereco));
            error = true;
            YoYo.with(Techniques.Shake).duration(300).playOn(editEndereco);
        }

        if (TextUtils.isEmpty(senha)) {
            editSenha.setError(getString(R.string.required_senha));
            YoYo.with(Techniques.Shake).duration(300).playOn(editSenha);
            error = true;
        }

        if (TextUtils.isEmpty(confirmaSenha)) {
            editConfirmarSenha.setError(getString(R.string.required_confirmacao_senha));
            YoYo.with(Techniques.Shake).duration(300).playOn(editConfirmarSenha);
            error = true;
        }

        if (!TextUtils.isEmpty(senha) && !TextUtils.isEmpty(confirmaSenha)
                && !senha.equals(confirmaSenha)) {
            editConfirmarSenha.setError(getString(R.string.confirmacao_senha_errada));
            error = true;
        }

        return error;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
