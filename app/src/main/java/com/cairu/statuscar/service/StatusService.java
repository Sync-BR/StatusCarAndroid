package com.cairu.statuscar.service;

import android.content.Context;
import androidx.annotation.NonNull;
import com.cairu.statuscar.model.StatusModel;
import com.cairu.statuscar.model.VeiculoModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.List;
import android.os.Handler;
import android.os.Looper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StatusService {

    private OkHttpClient client = new OkHttpClient();
    private Context context;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
private StatusModel statusAtual = new StatusModel();
    // Construtor para aceitar um Context
    public StatusService() {
        this.context = context;
        this.client = new OkHttpClient();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    // Altere o parâmetro para StatusCallback
    public StatusModel buscarStatus(int id) {

        String url = "http://186.247.89.58:8080/api/status/" +id;
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    StatusModel statusVeiculo = new Gson().fromJson(jsonData, StatusModel.class);
                    statusAtual = statusVeiculo;
                    // Verifique se statusVeiculo não é nulo e se contém o ID correto
                    if (statusVeiculo != null && statusVeiculo.getId() != 0) {
                     //   mainHandler.post(() -> callback.onStatusReceived(statusVeiculo)); // Chama o callback com o status
                    } else {
                        // Se o status é nulo ou ID é 0, chame o callback de falha
                    //    mainHandler.post(() -> callback.onFailure(new IOException("Status não encontrado ou ID inválido.")));
                    }
                } else {
                    // Se a resposta não for bem-sucedida, chame o callback de falha
                //    mainHandler.post(() -> callback.onFailure(new IOException("Código inesperado: " + response.code())));
                }
            }
        });
        return statusAtual;
    }

    public void updateStatus(String cpf, StatusCallback callback){
        String url = "http://186.247.89.58:8080/api/veiculos/consultar/veiculos/" + cpf;
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mainHandler.post(() -> callback.onFailure(e)); // Chama o callback de falha
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    List<VeiculoModel> veiculos = new Gson().fromJson(jsonData, new TypeToken<List<VeiculoModel>>() {}.getType());
              //      mainHandler.post(() -> callback.onSuccess(veiculos));
                    System.out.println(veiculos);
                } else {
                    mainHandler.post(() -> callback.onFailure(new IOException("Erro na requisição: " + response.message())));
                }
            }
        });
    }
}

interface StatusCallback {
    void onStatusReceived(StatusModel status);
    void onFailure(IOException e);
}
