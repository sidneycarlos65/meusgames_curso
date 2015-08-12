package br.com.cocobongo.meusgames;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.com.cocobongo.meusgames.modelos.Game;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends BaseActivity {

    @Bind(R.id.img_game)
    ImageView imgGame;

    @Bind(R.id.table_comentarios)
    TableLayout tableComentarios;

    @Bind(R.id.text_nome)
    TextView textNome;

    @Bind(R.id.text_pontuacao)
    TextView textPontuacao;

    @Bind(R.id.text_ano)
    TextView textAno;

    @Bind(R.id.text_categorias)
    TextView textCategorias;

    private Game game;

    boolean favorito = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        initToolbar();

        Intent intent = getIntent();
        game = (Game) intent.getSerializableExtra("game");
        textNome.setText(game.getNome());
        textAno.setText(getString(R.string.lbl_ano, game.getAno()));
        textPontuacao.setText(getString(R.string.lbl_pontuacao, game.getPontuacao()));

        if(null != game.getCategorias()){
            textCategorias.setText(getString(R.string.lbl_categorias,
                    TextUtils.join(" ,", game.getCategorias())));
        }

        addComentarios();
        Picasso.with(this)
                .load(game.getImage())
                .error(R.drawable.error)
                .placeholder(R.drawable.anon_user)
                .into(imgGame);
    }

    @OnClick(R.id.btn_ver_mais)
    public void onClickBtnVerMais(View view) {
        addComentarios();
    }

    private void addComentarios() {
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.row_comentario,
                    tableComentarios, false);
            ImageView imgUser = (ImageView) row.findViewById(R.id.img_user);
            TextView nome = (TextView) row.findViewById(R.id.txt_nome_usuario);
            TextView comentario = (TextView) row.findViewById(R.id.txt_comentario);

            nome.setText("Usuário " + i);
            comentario.setText("Comentário " + i);

            tableComentarios.addView(row);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);

//        MenuItem menuItemFavoritar = menu.findItem(R.id.action_favoritar);

//        if(true){
//            menuItemFavoritar.setIcon(R.drawable.ic_favorite_black_24dp);
//        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favoritar) {
            if (!favorito) {
                Toast.makeText(this, "Adicionado aos favoritos", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_favorite_white_24dp);
                favorito = true;
            } else {
                Toast.makeText(this, "Removido dos favoritos", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                favorito = false;
            }
        } else if (id == R.id.action_adicionar_biblioteca) {
            Toast.makeText(this, "Adicionado à biblioteca", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
