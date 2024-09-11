package com.cairu.statuscar.service;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cairu.statuscar.CadastroActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CadastroService {
    private final OkHttpClient cliente;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public CadastroService() {
        this.cliente = new OkHttpClient();
    }

    public void cadastrarUsuario(String nome, String cpf, String telefone, String email, String endereco, String login, String senha, Context context) {
        String url = "http://186.247.89.58:8080/api/user/add";
        String rank = "usuario";
        String json = criarJsonCadastro(nome, cpf, telefone, email, endereco, login, senha, rank);
        System.out.println("Json: " + json);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        cliente.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ((CadastroActivity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Erro ao cadastrar", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ((CadastroActivity) context).runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Erro no cadastro", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private String criarJsonCadastro(String nome, String cpf, String telefone, String email, String endereco, String login, String senha, String rank) {
        return "{"
                + "\"nome\":\"" + nome + "\","
                + "\"cpf\":\"" + cpf + "\","
                + "\"telefone\":\"" + telefone + "\","
                + "\"email\":\"" + email + "\","
                + "\"endereco\":\"" + endereco + "\","
                + "\"login\":\"" + login + "\","
                + "\"senha\":\"" + senha + "\","
                + "\"rank\":\"" + rank + "\""
                + "}";
    }
}
