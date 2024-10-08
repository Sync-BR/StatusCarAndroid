package com.cairu.statuscar.service;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.cairu.statuscar.ConsultorActivity;
import com.cairu.statuscar.model.VeiculoModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class ConsultorService {
    private OkHttpClient client = new OkHttpClient();
    private Spinner spinnerVeiculos;
    private Context context;
    public ConsultorService(Context context, Spinner spinnerVeiculos) {
        this.context = context;
        this.spinnerVeiculos = spinnerVeiculos;
    }

    public ConsultorService() {
    }

    public void buscarVeiculo(String cpf){
        String url = "http://186.247.89.58:8080/api/veiculos/consultar/veiculos/"+cpf   ;
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    System.out.println(jsonData);
                    List<VeiculoModel> veiculos = new Gson().fromJson(jsonData, new TypeToken<List<VeiculoModel>>() {}.getType());

                    // Atualiza o Spinner na thread principal
                    ((AppCompatActivity) context).runOnUiThread(() -> {
                        atualizarSpinner(veiculos);
                    });
                }

            }
        });

    }
    private void atualizarSpinner(List<VeiculoModel> veiculos) {
        ArrayAdapter<VeiculoModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, veiculos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVeiculos.setAdapter(adapter);
      //  List<String> veiculosPlacas = new ArrayList<>();
      //  for(VeiculoModel veiculoModel: veiculos){
      //      veiculosPlacas.add(veiculoModel.getPlaca());
       // }

     //   ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, veiculosPlacas);
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        System.out.println(adapter);
        spinnerVeiculos.setAdapter(adapter);
    }
}
