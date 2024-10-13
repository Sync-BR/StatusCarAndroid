package com.cairu.statuscar.service;

import android.content.Context;
import android.widget.Toast;
import com.cairu.statuscar.RecoveryActivity;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecoveryService {
    private final OkHttpClient cliente;

    public RecoveryService() {
        this.cliente = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Timeout de conexão
                .writeTimeout(30, TimeUnit.SECONDS)   // Timeout de escrita
                .readTimeout(30, TimeUnit.SECONDS)    // Timeout de leitura
                .build();
    }

    public void recuperarSenha(String email, Context context) {
        // Corrigido: adicionei uma barra antes de {email}
        String url = "http://186.247.89.58:8080/api/user/usersLogin/" + email; // Adicione a barra aqui

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(null, new byte[0]))
                .build();

        cliente.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Exibe mensagem de erro na UI
                ((RecoveryActivity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "Erro na solicitação: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Atualiza a UI com a resposta do servidor
                ((RecoveryActivity) context).runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Instruções de recuperação de senha enviadas", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
