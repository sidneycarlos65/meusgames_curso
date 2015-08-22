package br.com.cocobongo.meusgames.modelos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by gu on 10/08/15.
 */
@DatabaseTable(tableName = "comentarios")
public class Comentario implements Serializable {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String descricao;

    @DatabaseField
    private long data;

    @DatabaseField
    private String idUsuario;

    @DatabaseField
    private String nomeUsuario;

    private Usuario usuario;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "idGame")
    private Game game;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public String getIdUsuario() {
        if(null != usuario){
            idUsuario = usuario.getId();
        }
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        if(null != usuario){
            this.idUsuario = usuario.getId();
            return;
        }
        this.idUsuario = idUsuario;
    }

    public String getNomeUsuario() {
        if(null != usuario){
            nomeUsuario = usuario.getNome();
        }
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        if(null != usuario){
            this.nomeUsuario = usuario.getNome();
            return;
        }
        this.nomeUsuario = nomeUsuario;
    }

}
