package com.cairu.statuscar.model;

import androidx.versionedparcelable.VersionedParcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StatusModel {
    private int id;
    private String status;
    private Date dataInicio;
    private Date dataFim;

    public StatusModel() {
        dataFim = new Date();

    }



    @Override
    public String toString() {
        return "StatusModel{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                '}';
    }
    public void setDataFimFromString(String dataString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            this.dataFim = df.parse(dataString); // Faz o parsing da string para Date
        } catch (ParseException e) {
            e.printStackTrace(); // Tratamento de erro se o parsing falhar
        }
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public static void main(String[] args) {
        System.out.println("statusModel.getDataInicio()");
    }

}
