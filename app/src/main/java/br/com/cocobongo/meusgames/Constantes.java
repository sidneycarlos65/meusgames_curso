package br.com.cocobongo.meusgames;

/**
 * Created by gu on 18/08/15.
 */
public class Constantes {

    public static final String URL = "http://192.168.0.6:3000";
    public static final String URL_LOGIN = URL + "/users/login";
    public static final String URL_SIGNUP = URL + "/users/signup";
    public static final String URL_DEVICE_TOKEN = URL + "/users/%s/deviceToken/%s";
    public static final String URL_GAMES = URL + "/games";
    public static final String URL_GAMES_UPLOAD_IMAGE = URL + "/games/uploadPhoto/";

    public static String getUrlImagem(String imagem){
        StringBuilder sb = new StringBuilder(URL);
        sb.append("/images/").append(imagem);
        return sb.toString();
    }

    public static final int HTTP_CODE_200_SUCCESS = 200;

    public static String DATABASE_DIRECTORY = "/data/data/br.com.cocobongo.meusgames/databases";
    public static String DATABASE_NAME = "meusgames.db";
    public static String DATABASE_PATH = DATABASE_DIRECTORY + "/" + DATABASE_NAME;

}
