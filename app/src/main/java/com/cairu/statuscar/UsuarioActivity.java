package com.cairu.statuscar;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.cairu.statuscar.dto.veiculoStatusList;
import com.cairu.statuscar.interfaces.StatusCallback;
import com.cairu.statuscar.model.ClienteModel;
import com.cairu.statuscar.model.StatusModel;
import com.cairu.statuscar.model.VeiculoModel;
import com.cairu.statuscar.service.ClienteService;
import com.cairu.statuscar.service.NotificationService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UsuarioActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private OkHttpClient client = new OkHttpClient();
    private Context context;
    private ClienteService clienteService;
    private Spinner spinnerVeiculos;
    private List<VeiculoModel> veiculoList;
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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonVisualizar = findViewById(R.id.btn_visualizar);
        clienteService = new ClienteService(this, spinnerVeiculos);
        spinnerVeiculos = findViewById(R.id.spinnerVeiculos); // Spinner para exibir veículos
        // Ação ao clicar no botão "Visualizar"
        buttonVisualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsuarioActivity.this, StatusConfirmacaoActivity.class);

                // Assegure-se que os objetos veiculoSelecionado e status estão preenchidos
                if (veiculoSelecionado != null) {
                    // Chame o método para buscar o status relacionado ao veículo
                    buscarStatusDoVeiculo(veiculoSelecionado, new StatusCallback() {
                        @Override
                        public void onStatusReceived(StatusModel status) {
                            // Quando o status é recebido, crie o Intent e passe os dados
                            Intent intent = new Intent(UsuarioActivity.this, StatusVisualizacaoClienteActivity.class);

                            // Cria o objeto veiculoStatusList com o veículo e o status recebidos
                            veiculoStatusList veiculoStatus = new veiculoStatusList(veiculoSelecionado, status);

                            // Coloca o objeto no Intent
                            intent.putExtra("veiculoStatus", true);  // Passa o objeto serializado

                            // Inicia a nova Activity
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(IOException e) {

                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Lidar com o erro
                            Toast.makeText(UsuarioActivity.this, "Erro ao buscar status: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(UsuarioActivity.this, "Nenhum veículo selecionado", Toast.LENGTH_SHORT).show();
                }
            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
                return;
            }
        }


        String userCpf = getIntent().getStringExtra("cpf");

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_perfil) {
            //User profile screen
            Toast.makeText(this, "Abrindo perfil", Toast.LENGTH_SHORT).show();
            String userCpf = getIntent().getStringExtra("cpf");
            getClienteByID(userCpf, new ClienteCallback() {
                @Override
                public void onClienteReceived(ClienteModel cliente) {
                    Intent intent = new Intent(UsuarioActivity.this, PerfilActivity.class);
                    intent.putExtra("id", cliente.getId());
                    System.out.println("id passado: " + cliente.getId());
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

                }
            });

            return true;
        } else if (id == R.id.menu_sair) {
            Toast.makeText(this, "Saindo", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getClienteByID(String cpf, ClienteCallback callback) {
        System.out.println("id: " + cpf);
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

    private void buscarStatusDoVeiculo(VeiculoModel veiculo, StatusCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://186.247.89.58:8080/api/status/" + veiculo.getPlaca();  // Suponha que o status seja buscado por placa

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();
                    StatusModel status = gson.fromJson(jsonResponse, StatusModel.class);

                    runOnUiThread(() -> callback.onStatusReceived(status));
                } else {
                    runOnUiThread(() -> callback.onError("Erro ao buscar status: " + response.code()));
                }
            }
        });
    }








}

interface ClienteCallback {
    void onClienteReceived(ClienteModel cliente);

    void onError(String errorMessage);
}
