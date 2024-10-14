package com.cairu.statuscar.model;

import java.io.Serializable;

public class VeiculoModel implements Serializable {
    private int id;
    private int clienteID;
    private String modelo;
    private String placa;
    private String marca;
    private int ano;
    private String previsao; // Presumindo que você tenha um campo para a previsão
    private String statusAtual;

    public VeiculoModel()  {
    }

    @Override
    public String toString() {
        return placa;
    }
    //    @Override
//    public String toString() {
//        return "VeiculoModel{" +
//                "id=" + id +
//                ", clienteID=" + clienteID +
//                ", modelo='" + modelo + '\'' +
//                ", placa='" + placa + '\'' +
//                ", marca='" + marca + '\'' +
//                ", ano=" + ano +
//                ", previsao='" + previsao + '\'' +
//                ", statusAtual='" + statusAtual + '\'' +
//                '}';
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteID() {
        return clienteID;
    }

    public void setClienteID(int clienteID) {
        this.clienteID = clienteID;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getPrevisao() {
        return previsao;
    }

    public void setPrevisao(String previsao) {
        this.previsao = previsao;
    }

    public String getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(String statusAtual) {
        this.statusAtual = statusAtual;
    }
}
