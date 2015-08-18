package br.com.cocobongo.meusgames.helpers;

import android.content.Context;

/**
 * Created by gu on 17/08/15.
 */
public class Helpers {

    public static boolean isTablet(Context context){
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }
}
