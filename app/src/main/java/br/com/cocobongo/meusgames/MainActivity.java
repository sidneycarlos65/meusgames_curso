package br.com.cocobongo.meusgames;

import android.content.Intent;
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

public class MainActivity extends BaseActivity {

    @Bind(R.id.edit_login)
    EditText editLogin;

    @Bind(R.id.edit_senha)
    EditText editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();
    }

    @OnClick(R.id.button_ok)
    public void onClickBtnOk(View view){
        if(!validaForm()){
            Intent intent = new Intent(this, GamesActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.button_cadastrar)
    public void onClickBtnCadastrar(View view){
        Intent intent = new Intent(this, CadastroActivity.class);
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
                    .playOn(findViewById(R.id.edit_login));
        }

        if(TextUtils.isEmpty(senha)){
            editSenha.setError(getString(R.string.msg_error_senha));
            YoYo.with(Techniques.Shake)
                    .duration(300)
                    .playOn(findViewById(R.id.edit_senha));
            error = true;
        }

        return error;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
