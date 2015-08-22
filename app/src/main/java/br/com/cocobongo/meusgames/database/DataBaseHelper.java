package br.com.cocobongo.meusgames.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

import br.com.cocobongo.meusgames.Constantes;

/**
 * Created by gu on 21/08/15.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static DataBaseHelper dataBaseHelper;

    public static int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, Constantes.DATABASE_PATH, null, DATABASE_VERSION);
    }

    public static DataBaseHelper getHelper(Context context){
        if(null == dataBaseHelper){
            dataBaseHelper = OpenHelperManager.getHelper(context, DataBaseHelper.class);
        }
        return dataBaseHelper;
    }

    public static void destroy(){
        if(null != dataBaseHelper){
            OpenHelperManager.releaseHelper();
            dataBaseHelper = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
