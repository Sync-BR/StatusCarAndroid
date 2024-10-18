package com.cairu.statuscar.model;

import java.util.Date;

public class NotificacaoModel {
    private int id_Notification;
    private int id_Veiculo;
    private int id_Status;
    private String descricao;
    private Date data;

    public NotificacaoModel() {
        this.data = new Date();
    }


    @Override
    public String toString() {
        return "NotificationModel{" +
                "id_Notification=" + id_Notification +
                ", id_Veiculo=" + id_Veiculo +
                ", id_Status=" + id_Status +
                ", descricao='" + descricao + '\'' +
                ", data=" + data +
                '}';
    }

    public int getId_Notification() {
        return id_Notification;
    }

    public void setId_Notification(int id_Notification) {
        this.id_Notification = id_Notification;
    }

    public int getId_Veiculo() {
        return id_Veiculo;
    }

    public void setId_Veiculo(int id_Veiculo) {
        this.id_Veiculo = id_Veiculo;
    }

    public int getId_Status() {
        return id_Status;
    }

    public void setId_Status(int id_Status) {
        this.id_Status = id_Status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
