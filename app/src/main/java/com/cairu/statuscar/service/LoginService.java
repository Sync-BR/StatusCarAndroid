package com.cairu.statuscar.service;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.cairu.statuscar.ConsultorActivity;
import com.cairu.statuscar.VeiculosActivity;
import com.cairu.statuscar.UsuarioActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;


public class LoginService {
    private LoginService loginService;
    private OkHttpClient cliente;
    private  Context context;

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    public LoginService(Context context) {
        this.context = context;
        this.cliente = new OkHttpClient();
    }

    public void login(String cpf, String password, LoginCallback callback) {
        System.out.println("cpf: " + cpf + " senha: " + password);
        String url = "http://186.247.89.58:8080/api/user/login/" + cpf + "/" + password; // Corrigido aqui
        System.out.println(url);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(null, new byte[0])) // Como não há corpo, pode ser null
                .build();

        cliente.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        int userRank = jsonResponse.getInt("rank");
                        // Exibe os valores ou passe-os para a próxima activity
                        System.out.println("Rank do usuário: " + userRank);

                        switch (userRank){
                            case 0:
                                Intent intentUsuarioActivity = new Intent(context, UsuarioActivity.class);
                      //          intentUsuarioActivity.putExtra("userId", userId);
                                intentUsuarioActivity.putExtra("userRank", userRank);
                                context.startActivity(intentUsuarioActivity);
                                break;
                            case 1:
                                Intent intentConsultoresActivity = new Intent(context, ConsultorActivity.class);
                            //    intentConsultoresActivity.putExtra("userId", userId);
                                intentConsultoresActivity.putExtra("userRank", userRank);
                                context.startActivity(intentConsultoresActivity);
                                break;

                        }
                        callback.onSuccess();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFailure(e);
                    }
                } else {
                    System.out.println("conta incorreta: " + url);
                    callback.onFailure(new IOException("Login ou senha incorretos"));
                }
            }
        });
    }



}


