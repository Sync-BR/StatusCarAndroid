package com.cairu.statuscar.model;

import java.io.Serializable;
import java.util.Date;

public class StatusModel implements Serializable {
    private int id;
    private String status;
    private Date dataInicio;
    private Date dataFim;

    public StatusModel() {
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
}
