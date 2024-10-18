package com.cairu.statuscar.service;


import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cairu.statuscar.ConsultorActivity;
import com.cairu.statuscar.VeiculosActivity;
import android.content.Context;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VeiculoService {
    private  OkHttpClient cliente;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public VeiculoService() {
        cliente = new OkHttpClient();
    }



    public void deletarVeiculo(String placa){
        String url = "http://186.247.89.58:8080/api/veiculos/deletar/"+placa;
        OkHttpClient cliente = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        cliente.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    System.out.println("Sucesso!");
                } else {
                    System.out.println("Falhou");
                }
            }
        });
    }

    public void cadastrarVeiculos(int clienteID, String modelo, String placa, String marca, int ano, String status, Context context){
        String url = "http://186.247.89.58:8080/api/veiculos/addveiculo/"+status;
        String json = criarJson(clienteID, modelo, placa, marca, ano);
        System.out.println(json);
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        cliente.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ((VeiculosActivity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Erro ao cadastrar um veiculo", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ((VeiculosActivity) context).runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Intent intent  = new Intent(context, ConsultorActivity.class);
                        context.startActivity(intent);
                        Toast.makeText(context, "Veiculo cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Erro no cadastro", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private static String criarJson(int clienteID, String modelo, String placa,String marca, int ano){
        return "{"
                + "\"clienteID\":" + clienteID + ","
                + "\"modelo\":\"" + modelo + "\","
                + "\"placa\":\"" + placa + "\","
                + "\"marca\":\"" + marca + "\","
                + "\"ano\":" + ano +
                 "}";
    }


}
