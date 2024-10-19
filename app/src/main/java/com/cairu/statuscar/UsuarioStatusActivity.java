package com.cairu.statuscar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cairu.statuscar.model.StatusModel;
import com.cairu.statuscar.service.ClienteService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UsuarioStatusActivity extends AppCompatActivity {
    private TextView textView;
    private ClienteService clienteService;
    private OkHttpClient cliente = new OkHttpClient();
    private Button btnVoltar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_visualizacao_cliente);
        textView = findViewById(R.id.text_status_confirmacao);
        btnVoltar = findViewById(R.id.btn_voltarInicio);
        clienteService = new ClienteService(this, null);
        Intent intent = getIntent();
        String modelo = intent.getStringExtra("modelo");
        String marca = intent.getStringExtra("marca");
        String placa = intent.getStringExtra("placa");
        int id = intent.getIntExtra("id", -1);
        System.out.println("id " +id);
        textView.setText("Marca: " + marca + "\nModelo: " + modelo + "\nPlaca: " + placa );
        getStatus(id);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent telaInicial = new Intent(UsuarioStatusActivity.this, UsuarioActivity.class);
//                startActivity(telaInicial);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("mensagem", "Dados do veículo atualizados com sucesso!");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
    public void getStatus(int id) {
        String url = "http://186.247.89.58:8080/api/veiculos/consultar/status/" + id;
        System.out.println("url: " +url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        cliente.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    StatusModel status = new Gson().fromJson(jsonData, new TypeToken<StatusModel>() {}.getType());
                    System.out.println("status: " +status);

                    // Atualiza a TextView com o status na thread principal
                    runOnUiThread(() -> {
                        // Pega o texto já existente no TextView e adiciona o status
                        String textoAtual = textView.getText().toString();
                        textView.setText(textoAtual + "\nStatus: " + status.getStatus()+ "\n Previsão: " +status.getDataFim());
                    });
                }
            }
        });
    }


}
