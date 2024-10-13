package com.cairu.statuscar;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cairu.statuscar.adapter.VeiculoAdapter;
import com.cairu.statuscar.dto.veiculoStatusList;
import com.cairu.statuscar.model.ClienteModel;
import com.cairu.statuscar.model.VeiculoModel;
import com.cairu.statuscar.service.ClienteService;
import com.cairu.statuscar.service.ConsultorService;
import com.cairu.statuscar.service.NotificationHelper;
import com.cairu.statuscar.service.NotificationService;
import com.cairu.statuscar.service.StatusService;
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
    private OkHttpClient client = new OkHttpClient();
    private Context context;
    private VeiculoAdapter veiculoAdapter;
    private ClienteService clienteService;
    private Spinner spinnerVeiculos;
    private List<VeiculoModel> veiculoList;
    private Button buttonUpdate;
    private Button buttonVisualizar;
    private VeiculoModel veiculoSelecionado = new VeiculoModel();
    private NotificationService notificationService;
    private static final int REQUEST_CODE = 100;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_inicial_cliente);
        buttonUpdate = findViewById(R.id.btn_perfil);
        buttonVisualizar = findViewById(R.id.btn_visualizar);
        spinnerVeiculos = findViewById(R.id.spinnerVeiculos); // Spinner para exibir veículos
        clienteService = new ClienteService(this, spinnerVeiculos);


        //int userId = getIntent().getIntExtra("userId", -1);
        //int tipo_usu = getIntent().getIntExtra("tipo_usu", -1);
        String userCpf = getIntent().getStringExtra("cpf");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
                return;
            }
        }


        //Botão Perfil
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClienteByID(userCpf, new ClienteCallback() {
                    @Override
                    public void onClienteReceived(ClienteModel cliente) {
                        // Use os dados do cliente aqui
                        Intent intent = new Intent(UsuarioActivity.this, PerfilActivity.class);
                        intent.putExtra("id" ,cliente.getId());
                        System.out.println("id passado: " +cliente.getId());
                        intent.putExtra("nome", cliente.getNome());
                        intent.putExtra("cpf", cliente.getCpf());
                        intent.putExtra("telefone", cliente.getTelefone());
                        intent.putExtra("email", cliente.getEmail());
                        intent.putExtra("endereco", cliente.getEndereco());
                        intent.putExtra("senha", cliente.getSenha());


                        startActivity(intent);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Lide com o erro aqui, se necessário
                    }
                });
            }
        });


        NotificationService notificationService = new NotificationService(this);
        notificationService.verificarNotificacao(userCpf, new NotificationService.NotificationCallback() {
            @Override
            public void onResult(final boolean success) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (success) {
                            // Notificação foi encontrada e enviada com sucesso
                            Toast.makeText(UsuarioActivity.this, "Notificação verificada com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println("falhou");
                            // Falha ao verificar a notificação
                            Toast.makeText(UsuarioActivity.this, "Falha ao verificar a notificação.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        clienteService.buscarVeiculo(userCpf);
    }

    private void getClienteByID(String cpf, ClienteCallback callback) {
        System.out.println("id: " +cpf);
        OkHttpClient client = new OkHttpClient();
        String url = "http://186.247.89.58:8080/api/user/consultar/" + cpf;
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(UsuarioActivity.this, "Falha ao buscar dados por id", Toast.LENGTH_SHORT).show();
                    callback.onError("Falha ao buscar dados por id");
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();

                    // Desserializa a resposta JSON para um objeto ClienteModel
                    ClienteModel cliente = gson.fromJson(jsonResponse, ClienteModel.class);

                    // Chamando o callback com os dados do cliente
                    runOnUiThread(() -> callback.onClienteReceived(cliente));
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(UsuarioActivity.this, "Erro ao buscar dados: " + response.code(), Toast.LENGTH_SHORT).show();
                        callback.onError("Erro ao buscar dados: " + response.code());
                    });
                }
            }
        });
    }
}
 interface ClienteCallback {
    void onClienteReceived(ClienteModel cliente);
    void onError(String errorMessage);
}
