package com.cairu.statuscar.interfaces;

import com.cairu.statuscar.model.VeiculoModel;
import java.util.List;

public interface VeiculoCallback {
    void onVeiculosReceived(List<VeiculoModel> veiculos);  // Chamado quando a lista de veículos é recebida
    void onError(String errorMessage);  // Chamado quando ocorre um erro
}

