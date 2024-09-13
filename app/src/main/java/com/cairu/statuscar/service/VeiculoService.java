package com.cairu.statuscar.service;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cairu.statuscar.model.VeiculoModel;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VeiculoService {
    private final OkHttpClient cliente;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public VeiculoService(OkHttpClient cliente) {
        this.cliente = cliente;
    }
/*
    public void cadastrarVeiculo(String veiculo, String placa, String modelo, int ano, final Context context){
        String url = "http://186.247.89.58:8080/api/veiculos/addveiculo";
        String json = criarJsonCadastro(veiculo,placa,modelo,ano);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        cliente.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Exibir erro para o usuário
                ((Veicu) context).runOnUiThread(() ->
                        Toast.makeText(context, "Falha na conexão com o servidor", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Exibir mensagem de sucesso
                    ((VeiculoActivity) context).runOnUiThread(() ->
                            Toast.makeText(context, "Veículo cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    // Exibir erro para o usuário
                    ((VeiculoActivity) context).runOnUiThread(() ->
                            Toast.makeText(context, "Erro ao cadastrar veículo", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }


    private String criarJsonCadastro(String veiculo, String placa, String modelo, int ano) {
        return "{"
                + "\"veiculo\":\"" + veiculo + "\","
                + "\"placa\":\"" + placa + "\","
                + "\"modelo\":\"" + modelo + "\","
                + "\"ano\":\"" + ano + "\""
                + "}";
    }

 */
}
