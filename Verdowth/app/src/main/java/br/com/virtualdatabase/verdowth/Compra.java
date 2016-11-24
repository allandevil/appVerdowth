package br.com.virtualdatabase.verdowth;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by marcoscesteves on 21/11/16.
 */

public class Compra implements Serializable {

    private String name;
    private Integer quantity;
    private Double unitaryPrice;
    private LatLng consumidor_local;
    private LatLng fornecedor_local;


    public Compra(String name, Integer quantity, Double unitaryPrice, LatLng consumidor, LatLng fornecedor){
        this.name = name;
        this.quantity = quantity;
        this.unitaryPrice = unitaryPrice;
        this.consumidor_local = consumidor;
        this.fornecedor_local = fornecedor;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitaryPrice() {
        return unitaryPrice;
    }

    public void setUnitaryPrice(Double unitaryPrice) {
        this.unitaryPrice = unitaryPrice;
    }

}
