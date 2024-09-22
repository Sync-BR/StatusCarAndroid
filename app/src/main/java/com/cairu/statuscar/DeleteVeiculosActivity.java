package com.cairu.statuscar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeleteVeiculosActivity extends AppCompatActivity {
    private Button pesquisar, voltar;
    private EditText placa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletarveiculo);
        placa = findViewById(R.id.editTextDeletePlaca);
        pesquisar = findViewById(R.id.buttonPesquisarPlaca);
        voltar = findViewById(R.id.buttonPesquisarVoltar);
        pesquisar.setOnClickListener(v -> {
            String placaTexto = placa.getText().toString().trim();
            System.out.println(placaTexto);
            if (!placaTexto.isEmpty()) {
                deletarVeiculo(placaTexto);
            } else {
                System.out.println("Por favor, insira uma placa.");
            }
        });
        voltar.setOnClickListener(v -> {
            Intent intent = new Intent(DeleteVeiculosActivity.this, ConsultorActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void deletarVeiculo(String placa){
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

}
