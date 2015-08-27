package br.com.cocobongo.meusgames;

import android.app.Application;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.Headers;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.loader.AsyncHttpRequestFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import br.com.cocobongo.meusgames.database.DataBaseHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by gu on 06/08/15.
 */
public class MeusGamesApplication extends Application {

    public static String token;
    public static String idUsuario;

    @Override
    public void onCreate() {
        super.onCreate();

        getToken();
        initIon();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        File fileDatabase = new File(Constantes.DATABASE_PATH);

        if (!fileDatabase.exists()) {

            try {
                copyDatabase();
            } catch (IOException e) {
                Log.e("COPY_DATABASE", e.getMessage(), e);
            }
        }

    }

    public void saveToken(String tokenParam, long dataValidade, String idUsuario){

        try{

            token = tokenParam;

            File file = new File(getFilesDir() + "/ddd.txt");
            if(!file.exists()){
                file.createNewFile();
            }

            StringBuilder sb = new StringBuilder();
            sb.append(tokenParam).append("#").append(dataValidade).append("#").append(idUsuario);

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(sb.toString().getBytes("UTF-8"));
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch(IOException ioe){
            Log.e("MeusGamesApplication", ioe.getMessage(), ioe);
        }

    }

    public void getToken(){
        try {
            File file = new File(getFilesDir() + "/ddd.txt");
            if(!file.exists()){
                return;
            }

            FileInputStream fileInputStream = new FileInputStream(file);
            String value = getFileContent(fileInputStream);
            String[] stringSplit = value.split("#");

            long dataValidade = Long.parseLong(stringSplit[1]);

            if(dataValidade <= System.currentTimeMillis()){
                deleteFile(file.getName());
                return;
            }

            token = stringSplit[0];
            idUsuario = stringSplit[2];

        } catch (IOException e) {
            Log.e("MeusGamesApplication", e.getMessage(), e);
        }
    }

    public String getFileContent(FileInputStream fis){
        try {
            StringBuilder sb = new StringBuilder();
            Reader r = new InputStreamReader(fis, "UTF-8");
            char[] buf = new char[1024];
            int amt = r.read(buf);
            while(amt > 0){
                sb.append(buf, 0, amt);
                amt = r.read(buf);
            }
            return sb.toString();
        } catch (IOException e) {
            Log.e("MeusGamesApplication", e.getMessage(), e);
        }
        return null;
    }

    private void copyDatabase() throws IOException {

        File f = new File(Constantes.DATABASE_DIRECTORY);

        if (!f.exists()) {
            f.mkdirs();
        }

        //Open your local db as the input stream
        InputStream myinput = getAssets().open("database/"+ Constantes.DATABASE_NAME);


        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(Constantes.DATABASE_PATH);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer))>0) {
            myoutput.write(buffer,0,length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    private void initIon(){
        final AsyncHttpRequestFactory current = Ion.getDefault(getApplicationContext())
                .configure().getAsyncHttpRequestFactory();
        Ion.getDefault(getApplicationContext()).configure().setAsyncHttpRequestFactory(new AsyncHttpRequestFactory() {
            @Override
            public AsyncHttpRequest createAsyncHttpRequest(Uri uri, String method, Headers headers) {
                AsyncHttpRequest ret = current.createAsyncHttpRequest(uri, method, headers);
                ret.setTimeout(10000);

                if(!TextUtils.isEmpty(token)){
                    ret.addHeader("X-Access-Token", token);
                }

                return ret;
            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DataBaseHelper.destroy();
    }
}
