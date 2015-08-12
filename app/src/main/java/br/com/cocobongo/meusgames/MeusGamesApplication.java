package br.com.cocobongo.meusgames;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import br.com.cocobongo.meusgames.modelos.Game;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by gu on 06/08/15.
 */
public class MeusGamesApplication extends Application {

    static List<Game> games = new ArrayList<Game>();

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

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
