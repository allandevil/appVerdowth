package br.com.virtualdatabase.verdowth;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created on 06/11/16.
 */

public class Localidade {

    private LatLng coordenadas;
    private String nomeLocalidade;
    private String descricaoCurta;
    private String descricaoLonga;
    private Double Latitude;
    private Double Longitude;
    private Double preco;
    private String produto;


    public Localidade(Double latitude, Double longitude, String produto, Double preco) {
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.produto = produto;
        this.preco = preco;


    }


    /*public Localidade(Double latitude, Double longitude, String produto) {
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.produto = produto;
        this.preco = 5.0;
    }*/

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public LatLng getCoordenadas() {
        LatLng latLng = new LatLng(this.Latitude,this.Longitude);
        return latLng;

    }

    public void setCoordenadas(LatLng coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getNomeLocalidade() {
        return nomeLocalidade;
    }

    public void setNomeLocalidade(String nomeLocalidade) {
        this.nomeLocalidade = nomeLocalidade;
    }

    public String getDescricaoCurta() {
        return descricaoCurta;
    }

    public void setDescricaoCurta(String descricaoCurta) {
        this.descricaoCurta = descricaoCurta;
    }

    public String getDescricaoLonga() {
        return descricaoLonga;
    }

    public void setDescricaoLonga(String descricaoLonga) {
        this.descricaoLonga = descricaoLonga;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public  void Maroto(){

    }
}
