package com.cairu.statuscar;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cairu.statuscar.adapter.VeiculoAdapter;
import com.cairu.statuscar.model.VeiculoModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UsuarioActivity extends AppCompatActivity {
    private RecyclerView recyclerViewVeiculos;
    private VeiculosActivity veiculosActivity;
    private VeiculoAdapter veiculoAdapter;
    private List<VeiculoModel> veiculoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_inicial_cliente);
        int userId = getIntent().getIntExtra("userId",-1);
        int userRank = getIntent().getIntExtra("userRank",-1);
        System.out.println("ID: " +userId);
                recyclerViewVeiculos = findViewById(R.id.recyclerViewVeiculos);
                recyclerViewVeiculos.setLayoutManager(new LinearLayoutManager(this));

        getVeiculos(userId);
    }

    private void getVeiculos(int clienteID) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://186.247.89.58:8080/api/veiculos/consultar/veiculos/" + clienteID;
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(UsuarioActivity.this, "Falha ao buscar dados", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    Log.d("UsuarioActivity", "Resposta da API: " + jsonData);

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<VeiculoModel>>() {}.getType();
                    veiculoList = gson.fromJson(jsonData, listType);

                    runOnUiThread(() -> {
                        if (veiculoList != null && !veiculoList.isEmpty()) {
                            veiculoAdapter = new VeiculoAdapter(veiculoList);
                            recyclerViewVeiculos.setAdapter(veiculoAdapter);
                        } else {
                            Toast.makeText(UsuarioActivity.this, "Nenhum veículo encontrado", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e("UsuarioActivity", "Erro na resposta da API: " + response.message());
                }
            }
        });
    }
}
