package br.com.cocobongo.meusgames;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import br.com.cocobongo.meusgames.helpers.HttpHelper;
import br.com.cocobongo.meusgames.modelos.Usuario;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CadastroActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

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

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private JsonObject jsonObject;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        ButterKnife.bind(this);
        initToolbar();
        createLocationRequest();
        jsonObject = new JsonObject();
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildGoogleApiClient();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @OnClick(R.id.btn_ok)
    public void onClickBtnOk(View view) {
        if(!validaForm()){

            String nome = editNome.getText().toString();
            String email = editEmail.getText().toString();
            String endereco = editEndereco.getText().toString();
            String senha = editSenha.getText().toString();

            jsonObject.addProperty("nome", nome);
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("senha", senha);
            jsonObject.addProperty("endereco", endereco);

            if (mCurrentLocation != null) {
                jsonObject.addProperty("latitude", mCurrentLocation.getLatitude());
                jsonObject.addProperty("longitude", mCurrentLocation.getLongitude());
            }

            new CadastroUsuarioTask().execute(jsonObject);

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

    protected void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
//        LocationServices.GeofencingApi - pega a localizacao apenas do GPS
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        new GeocoderTask(CadastroActivity.this).execute(mCurrentLocation);
        stopLocationUpdates();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mCurrentLocation != null) {
            jsonObject.addProperty("latitude", mCurrentLocation.getLatitude());
            jsonObject.addProperty("longitude", mCurrentLocation.getLongitude());
        }

        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * Classe para chamadas assincronas.
     * Parametros do AsyncTask<parametros, progresso, retorno>
     */
    private class CadastroUsuarioTask extends AsyncTask<JsonObject, Void, Usuario>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CadastroActivity.this,
                    getString(R.string.app_name), "Aguarde...");
        }

        @Override
        protected Usuario doInBackground(JsonObject... params) {

            String param = params[0].toString();

            try {
                HttpHelper httpHelper = new HttpHelper();
                httpHelper.setContentType("application/json");
                String jsonUsuario = httpHelper.doPost(Constantes.URL_SIGNUP, param, "UTF-8");

                if(null != jsonUsuario){
                    Gson gson = new Gson();
                    return gson.fromJson(jsonUsuario, Usuario.class);
                }

            } catch (IOException e) {
                Log.e("UsuarioService", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            super.onPostExecute(usuario);
            if(null != progressDialog && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            if(null != usuario){
                CadastroActivity.this.finish();
                Toast.makeText(CadastroActivity.this, "Cadastrado com sucesso",
                        Toast.LENGTH_SHORT);
            }
            else{
                Toast.makeText(CadastroActivity.this, "Ops! Erro ao cadastrar usuario",
                        Toast.LENGTH_SHORT);
            }

        }

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

    public class GeocoderTask extends AsyncTask<Location, Void, String>{

        private Context context;

        public GeocoderTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Location... params) {
            try {
                Location location = params[0];
                Geocoder geocoder = new Geocoder(context);
                List<Address> enderecos = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);

                if(null != enderecos && !enderecos.isEmpty()){
                    Address endereco = enderecos.get(0);
                    return endereco.getAddressLine(0) + ", " + endereco.getAddressLine(1);
                }
            } catch (IOException e) {
                Log.e("GeocoderTask", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(null != result){
                editEndereco.setText(result);
            }
        }
    }

}
