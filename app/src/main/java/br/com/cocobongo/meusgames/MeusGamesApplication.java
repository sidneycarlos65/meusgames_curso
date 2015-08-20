package br.com.cocobongo.meusgames;

import android.app.Application;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import br.com.cocobongo.meusgames.modelos.Game;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by gu on 06/08/15.
 */
public class MeusGamesApplication extends Application {

    public static List<Game> games;
    public static String token;

    @Override
    public void onCreate() {
        super.onCreate();

        games = new ArrayList<Game>();
        getToken();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

    }

    public void saveToken(String tokenParam, long dataValidade){

        try{

            token = tokenParam;

            File file = new File(getFilesDir() + "/ddd.txt");
            if(!file.exists()){
                file.createNewFile();
            }

            StringBuilder sb = new StringBuilder();
            sb.append(tokenParam).append("#").append(dataValidade);

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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
