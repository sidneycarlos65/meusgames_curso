package br.com.cocobongo.meusgames.modelos;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by gu on 10/08/15.
 */
@DatabaseTable
public class Game implements Serializable {

    @SerializedName("_id")
    @DatabaseField(id=true)
    private String id;

    @DatabaseField
    private String nome;

    @DatabaseField
    private String descricao;

    @DatabaseField
    private int ano;

    @DatabaseField(columnName = "imagem")
    private String image;

    @DatabaseField
    private double pontuacao;

    @DatabaseField
    private String siteOficial;

    @ForeignCollectionField
    private Collection<Comentario> comentarios;

    private List<String> categorias;
    private List<String> plataformas;

    @DatabaseField(columnName = "categorias")
    private String categoriaString;

    @DatabaseField(columnName = "plataformas")
    private String plataformaString;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public double getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(double pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Collection<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Collection<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<String> getPlataformas() {
        return plataformas;
    }

    public void setPlataformas(List<String> plataformas) {
        this.plataformas = plataformas;
    }

    public String getSiteOficial() {
        return siteOficial;
    }

    public void setSiteOficial(String siteOficial) {
        this.siteOficial = siteOficial;
    }

    @Override
    public String toString() {
        return getNome();
    }

    public String getCategoriaString() {
        if(null != categorias){
            categoriaString = TextUtils.join(",", categorias);
        }
        return categoriaString;
    }

    public void setCategoriaString(String categoriaString) {
        if(null != categoriaString){
            this.categorias = Arrays.asList(categoriaString.split(","));
        }
        this.categoriaString = categoriaString;
    }

    public String getPlataformaString() {
        if(null != plataformas){
            plataformaString = TextUtils.join(",", plataformas);
        }
        return plataformaString;
    }

    public void setPlataformaString(String plataformaString) {
        if(null != plataformaString){
            this.plataformas = Arrays.asList(plataformaString.split(","));
        }
        this.plataformaString = plataformaString;
    }
}
