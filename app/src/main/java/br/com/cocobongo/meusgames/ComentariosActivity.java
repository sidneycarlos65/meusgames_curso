package br.com.cocobongo.meusgames;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.List;

import br.com.cocobongo.meusgames.adapters.ComentariosAdapter;
import br.com.cocobongo.meusgames.api.MeusGamesAPI;
import br.com.cocobongo.meusgames.modelos.Comentario;
import br.com.cocobongo.meusgames.modelos.Game;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComentariosActivity extends BaseActivity {

    @Bind(R.id.list_view)
    ListView listView;

    @Bind(R.id.edit_comentario)
    EditText editComentario;

    @Bind(R.id.container_add_comentario)
    LinearLayout containerAddComentario;
    private Game game;
    private MeusGamesAPI meusGamesApi;
    private ComentariosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        ButterKnife.bind(this);
        initToolbar();

        game = (Game) getIntent().getSerializableExtra("game");
        meusGamesApi = new MeusGamesAPI(this);

        adapter = new ComentariosAdapter(ComentariosActivity.this,
                R.layout.item_comentario, new ArrayList<Comentario>());
        listView.setAdapter(adapter);

        getComentarios();
    }

    private void getComentarios(){
        meusGamesApi.getComentarios(game.getId(), new FutureCallback<List<Comentario>>() {
            @Override
            public void onCompleted(Exception e, List<Comentario> result) {
                if (result != null) {
                    adapter.addAll(result);
                } else {
                    Toast.makeText(ComentariosActivity.this, "Ops! Acho que deu ruim",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.btn_salvar)
    public void addComentario(){
        String descricao = editComentario.getText().toString();
        if(!TextUtils.isEmpty(descricao)){
            Comentario comentario = new Comentario();
            comentario.setGame(game);
            comentario.setDescricao(descricao);
            comentario.setData(System.currentTimeMillis());
            final ProgressDialog progressDialog = ProgressDialog.show(this,
                    getString(R.string.app_name), "Aguarde...");
            meusGamesApi.addComentario(game.getId(), comentario, new FutureCallback<Comentario>() {
                @Override
                public void onCompleted(Exception e, Comentario result) {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    if(null != result){
                        adapter.add(result);
                        editComentario.setText(null);
                    }
                    else{
                        Toast.makeText(ComentariosActivity.this, "Erro ao cadastrar o comentario",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comentarios, menu);
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
