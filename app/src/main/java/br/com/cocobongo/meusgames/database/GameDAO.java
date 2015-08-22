package br.com.cocobongo.meusgames.database;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import br.com.cocobongo.meusgames.modelos.Game;

/**
 * Created by gu on 21/08/15.
 */
public class GameDAO {

    private Dao<Game, Integer> dao;
    private Context context;

    public GameDAO(Context context) throws SQLException {
        this.context = context;
        ConnectionSource connectionSource = DataBaseHelper.getHelper(context).getConnectionSource();
        dao = DaoManager.createDao(connectionSource, Game.class);
    }

    public boolean add(Game game) throws SQLException {
        if(dao.create(game) == 1){
            return true;
        }
        return false;
    }

    public boolean remove(Game game) throws SQLException {
        if(dao.delete(game) == 1){
            return true;
        }
        return false;
    }

    public List<Game> getAll() throws SQLException {
        return dao.queryForAll();
    }

    public List<Game> search(String query) throws SQLException {
        return dao.query(dao.queryBuilder().where().like("nome", query).prepare());
    }

    public Game findById(String id) throws SQLException {
        List<Game> games = dao.query(dao.queryBuilder().where().like("id", id).prepare());
        if(games.isEmpty()){
            return null;
        }
        return games.get(0);
    }

}
