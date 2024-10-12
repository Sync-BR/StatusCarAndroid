package com.cairu.statuscar.dto;

import com.cairu.statuscar.model.StatusModel;
import com.cairu.statuscar.model.VeiculoModel;

public class veiculoStatusList {
    private VeiculoModel veiculo;
    private StatusModel status;

    public veiculoStatusList(VeiculoModel veiculo, StatusModel status) {
        this.veiculo = veiculo;
        this.status = status;
    }

    public VeiculoModel getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(VeiculoModel veiculo) {
        this.veiculo = veiculo;
    }

    public StatusModel getStatus() {
        return status;
    }

    public void setStatus(StatusModel status) {
        this.status = status;
    }
}
