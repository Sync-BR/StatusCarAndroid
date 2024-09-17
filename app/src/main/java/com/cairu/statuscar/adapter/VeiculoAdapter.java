package com.cairu.statuscar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cairu.statuscar.R;
import com.cairu.statuscar.model.VeiculoModel;

import java.util.List;

public class VeiculoAdapter extends RecyclerView.Adapter<VeiculoAdapter.VeiculoViewHolder> {
    private List<VeiculoModel> veiculoList;

    public VeiculoAdapter(List<VeiculoModel> veiculoList) {
        this.veiculoList = veiculoList;
    }

    @NonNull
    @Override
    public VeiculoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_veiculo, parent, false);
        return new VeiculoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VeiculoViewHolder holder, int position) {
        VeiculoModel veiculo = veiculoList.get(position);
        holder.textViewModelo.setText(veiculo.getModelo());
        holder.textViewPlaca.setText(veiculo.getPlaca());
        holder.textViewStatus.setText(veiculo.getStatus());
    }

    @Override
    public int getItemCount() {
        return veiculoList.size();
    }

    public static class VeiculoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewModelo;
        TextView textViewPlaca;
        TextView textViewStatus;
        public VeiculoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewModelo = itemView.findViewById(R.id.textViewVeiculoModelo);
            textViewPlaca = itemView.findViewById(R.id.textViewVeiculoPlaca);
            textViewStatus = itemView.findViewById(R.id.textViewStatusVeiculos);
        }
    }
}
