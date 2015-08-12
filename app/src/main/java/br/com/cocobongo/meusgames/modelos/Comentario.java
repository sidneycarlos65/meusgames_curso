package br.com.cocobongo.meusgames.modelos;

import java.io.Serializable;

/**
 * Created by gu on 10/08/15.
 */
public class Comentario implements Serializable {

    private String id;
    private String descricao;
    private Usuario usuario;
    private Game game;
    private long data;

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
}
