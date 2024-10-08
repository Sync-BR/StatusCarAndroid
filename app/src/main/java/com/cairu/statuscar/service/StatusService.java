package com.cairu.statuscar.service;

import android.content.Context;
import androidx.annotation.NonNull;
import com.cairu.statuscar.model.StatusModel;
import com.cairu.statuscar.model.VeiculoModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import android.os.Handler;
import android.os.Looper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StatusService {

    private OkHttpClient client = new OkHttpClient();
    private Context context;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Gson gson = new Gson();

    public StatusModel statusAtual = new StatusModel();
    public StatusService() {
        this.context = context;
        this.client = new OkHttpClient();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void updateStatus(int id, StatusModel updatedStatus) {
        System.out.println(updatedStatus);
        String url = "http://186.247.89.58:8080/api/status/" + id;
        String json = gson.toJson(updatedStatus);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                } else {
                    System.out.println("Erro: " + response.code());
                }
            }
        });
    }

    public CompletableFuture<StatusModel> buscarStatus(int id) {
        String url = "http://186.247.89.58:8080/api/status/" + id;
        CompletableFuture<StatusModel> future = new CompletableFuture<>();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    StatusModel statusVeiculo = new Gson().fromJson(jsonData, StatusModel.class);
                    future.complete(statusVeiculo);
                } else {
                    future.completeExceptionally(new IOException("Código inesperado: " + response.code()));
                }
            }
        });
        return future;
    }


}

interface StatusCallback {
    void onStatusReceived(StatusModel status);
    void onFailure(IOException e);
}
