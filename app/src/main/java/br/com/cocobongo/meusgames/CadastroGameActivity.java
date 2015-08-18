package br.com.cocobongo.meusgames;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import br.com.cocobongo.meusgames.modelos.Game;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CadastroGameActivity extends BaseActivity {

    @Bind(R.id.cadastro_game_nome)
    EditText cadastroGameNome;

    @Bind(R.id.cadastro_game_descricao)
    EditText cadastroGameDescricao;

    @Bind(R.id.cadastro_game_ano)
    EditText cadastroGameAno;

    @Bind(R.id.cadastro_game_pontuacao)
    EditText cadastroGamePontuacao;

    @Bind(R.id.cadastro_game_siteoficial)
    EditText cadastroGameSiteoficial;

    @Bind(R.id.cadastro_game_categorias)
    EditText cadastroGameCategorias;

    @Bind(R.id.cadastro_game_plataformas)
    EditText cadastroGamePlataformas;

    @Bind(R.id.cadastro_game_imagem)
    ImageView cadastroGameImagem;

    Game game = new Game();

    private Uri mImageCaptureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_game);
        ButterKnife.bind(this);
        initToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro_game, menu);
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

    @OnClick(R.id.cadastro_game_imagem)
    public void OnClickImgGame(View view) {
        openDialogFoto();
    }

    private void openDialogFoto() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.label_imagem));
        builder.setMessage(getString(R.string.mensagem_alert_origem_foto));
        builder.setIcon(R.mipmap.ic_launcher);

        builder.setNegativeButton(R.string.label_camera, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                abrirCamera();
            }
        });

        builder.setPositiveButton(R.string.label_galeria, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                abrirGaleria();
            }

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Complete using"), 101);
    }

    private void abrirCamera() {
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "tmp_meugame_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        takePictureIntent.putExtra("return-data", true);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 102);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (RESULT_CANCELED == resultCode) {
            return;
        }

        if (101 == requestCode && null != data) {
            Uri uriFoto = data.getData();
            game.setImage(uriFoto.toString());

        }
        else if(102 == requestCode && null != mImageCaptureUri){
            game.setImage(mImageCaptureUri.toString());
        }

        if(null != game.getImage()){
            Picasso.with(this).load(game.getImage()).into(cadastroGameImagem);
        }

    }

    @OnClick(R.id.cadastro_game_salvar)
    public void onClickBtnSalvar(View view) {
        game.setNome(cadastroGameNome.getText().toString());
        game.setDescricao(cadastroGameDescricao.getText().toString());
        game.setAno(Integer.valueOf(cadastroGameAno.getText().toString()));
        game.setPontuacao(Double.valueOf(cadastroGamePontuacao.getText().toString()));
        game.setSiteOficial(cadastroGameSiteoficial.getText().toString());

        List<String> categorias = Arrays.asList(cadastroGameCategorias.getText().toString()
                .split(","));
        game.setCategorias(categorias);

        List<String> plataformas = Arrays.asList(cadastroGamePlataformas.getText().toString()
                .split(","));
        game.setPlataformas(plataformas);

        MeusGamesApplication.games.add(game);

        Toast.makeText(this, "Game cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putExtra("result", true);
        setResult(Activity.RESULT_OK, intent);

        finish();

    }
}
