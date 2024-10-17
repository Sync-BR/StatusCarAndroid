package com.cairu.statuscar;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cairu.statuscar.adapter.VeiculoAdapter;
import com.cairu.statuscar.dto.veiculoStatusList;
import com.cairu.statuscar.interfaces.StatusCallback;
import com.cairu.statuscar.interfaces.VeiculoCallback;
import com.cairu.statuscar.model.ClienteModel;
import com.cairu.statuscar.model.StatusModel;
import com.cairu.statuscar.model.VeiculoModel;
import com.cairu.statuscar.service.NotificationHelper;
import com.cairu.statuscar.service.NotificationService;
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
    private NotificationService notificationService;
    private static final int REQUEST_CODE = 100;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_inicial_cliente);

        buttonPerfil = findViewById(R.id.btn_perfil);
        buttonVisualizar = findViewById(R.id.btn_visualizar);
        spinnerVeiculos = findViewById(R.id.spinnerVeiculos); // Spinner para exibir veículos
        clienteService = new ClienteService(this, spinnerVeiculos);

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

        //Botão Perfil
        buttonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userCpf = getIntent().getStringExtra("cpf");
                getClienteByID(userCpf, new ClienteCallback() {
                    @Override
                    public void onClienteReceived(ClienteModel cliente) {
                        // Use os dados do cliente aqui
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
                        // Lide com o erro aqui, se necessário
                    }
                });
            }
        });




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
            }
        }
        notificationService = new NotificationService(this);
        notificationService.enviarNotificao("Ford ka", "Em analise");
        System.out.println("ID: " +userId);
        recyclerViewVeiculos = findViewById(R.id.recyclerViewVeiculosRegistred);
                recyclerViewVeiculos.setLayoutManager(new LinearLayoutManager(this));


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

        clienteService.buscarVeiculo(userCpf, new VeiculoCallback() {
            @Override
            public void onVeiculosReceived(List<VeiculoModel> veiculos) {
                veiculoList = veiculos;

                // Adiciona um log para verificar se a lista de veículos foi recebida corretamente
                System.out.println("Veículos recebidos: " + veiculoList.toString());

                // Verifica se a lista não está vazia
                if (veiculoList != null && !veiculoList.isEmpty()) {
                    Toast.makeText(UsuarioActivity.this, "Veículos carregados com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UsuarioActivity.this, "Lista de veículos vazia", Toast.LENGTH_SHORT).show();
                }

                clienteService.atualizarSpinner(veiculoList); // Atualiza o Spinner com os veículos
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println("Erro ao buscar veículos: " + errorMessage);
                Toast.makeText(UsuarioActivity.this, "Erro ao buscar veículos", Toast.LENGTH_SHORT).show();
            }
        });

    }




    private void getVeiculos(String cpf) {
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

    public  void buscarStatusDoVeiculo(VeiculoModel veiculo, StatusCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://186.247.89.58:8080/api/veiculos/consultar/veiculos/" + cpf;
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

interface ClienteCallback {
    void onClienteReceived(ClienteModel cliente);

    void onError(String errorMessage);
}
