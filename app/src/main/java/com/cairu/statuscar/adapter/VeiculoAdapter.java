package com.cairu.statuscar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cairu.statuscar.R;
import com.cairu.statuscar.dto.veiculoStatusList; // Importa sua nova classe
import java.util.List;

public class VeiculoAdapter extends RecyclerView.Adapter<VeiculoAdapter.VeiculoViewHolder> {
    private List<veiculoStatusList> veiculoStatusList; // Altera o tipo da lista

    public VeiculoAdapter(List<veiculoStatusList> veiculoStatusList) {
        this.veiculoStatusList = veiculoStatusList;
    }

    @NonNull
    @Override
    public VeiculoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_veiculo, parent, false);
        return new VeiculoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VeiculoViewHolder holder, int position) {
        veiculoStatusList veiculoStatus = veiculoStatusList.get(position); // Obtém o item
        holder.textViewModelo.setText(veiculoStatus.getVeiculo().getModelo()); // Acessa o modelo do veículo
        holder.textViewPlaca.setText(veiculoStatus.getVeiculo().getPlaca()); // Acessa a placa do veículo
        holder.textViewStatus.setText(veiculoStatus.getStatus().getStatus()); // Acessa o status
    }

    @Override
    public int getItemCount() {
        return veiculoStatusList.size();
    }

    public static class VeiculoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewModelo;
        TextView textViewPlaca;
        TextView textViewStatus;

        public VeiculoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewModelo = itemView.findViewById(R.id.textViewModelo);
            textViewPlaca = itemView.findViewById(R.id.textViewPlaca);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
        }
    }
}
