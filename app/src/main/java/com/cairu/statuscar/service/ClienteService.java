package com.cairu.statuscar.service;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cairu.statuscar.VeiculosActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import com.cairu.statuscar.model.ClienteModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ClienteService {
    private  OkHttpClient cliente;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private Context context;
    public ClienteService(Context context) {
        this.cliente = new OkHttpClient();
        this.context = context; // Contexto é atribuído
    }

    public List<ClienteModel> consumirClientes(){
        String url = "http://186.247.89.58:8080/api/allClientes";
        List<ClienteModel> listClientes = new ArrayList<>();
        Request request = new Request.Builder()
                .url(url)
                .build();
        cliente.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ((VeiculosActivity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Erro ao obter clientes", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Type clienteListType = new TypeToken<List<ClienteModel>>() {}.getType();
                    List<ClienteModel> clientes = new Gson().fromJson(responseBody, clienteListType);
                    listClientes.addAll(clientes);

                    ((VeiculosActivity) context).runOnUiThread(() -> {
                        // Atualizar a UI com a lista de clientes

                        ((VeiculosActivity) context).atualizarClientes(clientes);
                    });
                } else {
                    ((VeiculosActivity) context).runOnUiThread(() ->
                            Toast.makeText(context, "Erro ao obter clientes", Toast.LENGTH_SHORT).show());
                }
            }
        });
        return listClientes;
    }
}
