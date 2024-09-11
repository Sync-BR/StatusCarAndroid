package com.cairu.statuscar.service;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.cairu.statuscar.MainActivity;
import com.cairu.statuscar.UsuarioActivity;

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

    public void login(String cpf, String password, LoginCallback callback){

        String url = "http://186.247.89.58:8080/api/user/login"+cpf+"password"+password;
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(null, new byte[0]))
                .build();


        //Enviar requisição
        cliente.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("Falhou: " +url);
                callback.onFailure(e);

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    System.out.println("Sucesso: " +url);

                        Intent intent = new Intent(context, UsuarioActivity.class);
                        context.startActivity(intent);

                    callback.onSuccess();
                }else {
                    System.out.println("conta incorreta: " +url);

                    callback.onFailure(new IOException("Login ou senha incorretar"));
                }
            }
        });
    }


}


