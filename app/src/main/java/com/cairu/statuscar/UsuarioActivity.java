package com.cairu.statuscar;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cairu.statuscar.adapter.VeiculoAdapter;
import com.cairu.statuscar.dto.veiculoStatusList;
import com.cairu.statuscar.model.ClienteModel;
import com.cairu.statuscar.model.VeiculoModel;
import com.cairu.statuscar.service.ClienteService;
import com.cairu.statuscar.service.ConsultorService;
import com.cairu.statuscar.service.NotificationHelper;
import com.cairu.statuscar.service.NotificationService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

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
    private Button buttonUpdate, buttonVisualizarVeiculo;
    private Spinner spinnerVeiculos;
    private VeiculoModel veiculo = new VeiculoModel();
    private ClienteService clienteService;
    private VeiculoModel veiculoSelecionado;
    private NotificationService notificationService;
    private static final int REQUEST_CODE = 100;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        carregarDados();
        String userCpf = getIntent().getStringExtra("cpf");
        setContentView(R.layout.activity_tela_inicial_cliente);
        buttonUpdate = findViewById(R.id.btn_perfil);
        buttonVisualizarVeiculo = findViewById(R.id.btn_visualizar);
        spinnerVeiculos = findViewById(R.id.spinner_veiculo_list);

     //    int userId = getIntent().getIntExtra("userId", -1);
      //   int userRank = getIntent().getIntExtra("userRank", -1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
                return;
            }
        }
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
        clienteService = new ClienteService(this, spinnerVeiculos);
        clienteService.buscarVeiculo(userCpf);
        buttonVisualizarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (veiculoSelecionado != null) { // Verifica se um veículo foi selecionado
                    Intent telaVisualizarStatus = new Intent(UsuarioActivity.this, UsuarioStatusActivity.class);
                    telaVisualizarStatus.putExtra("id", veiculoSelecionado.getId());
                    telaVisualizarStatus.putExtra("modelo", veiculoSelecionado.getModelo());
                    telaVisualizarStatus.putExtra("marca", veiculoSelecionado.getMarca());
                    telaVisualizarStatus.putExtra("placa", veiculoSelecionado.getPlaca());
                    clienteService.getStatus(veiculoSelecionado.getId()); // Mantenha essa chamada se necessário
                    startActivity(telaVisualizarStatus);
                } else {
                    // Opcional: exibir mensagem ao usuário se nenhum veículo estiver selecionado
                    Toast.makeText(UsuarioActivity.this, "Selecione um veículo antes de visualizar!", Toast.LENGTH_SHORT).show();
                }
            }
        });

// Mantenha o listener do Spinner para apenas armazenar a seleção
        spinnerVeiculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                veiculoSelecionado = (VeiculoModel) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                veiculoSelecionado = null; // Reseta a seleção se nada for selecionado
            }
        });
//        spinnerVeiculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Object item = adapterView.getItemAtPosition(i);
//                veiculoSelecionado = (VeiculoModel) adapterView.getItemAtPosition(i);
//                Intent telaVisualizarStatus = new Intent(UsuarioActivity.this, UsuarioStatusActivity.class);
//                telaVisualizarStatus.putExtra("id", veiculoSelecionado.getId());
//                telaVisualizarStatus.putExtra("modelo", veiculoSelecionado.getModelo());
//                telaVisualizarStatus.putExtra("marca", veiculoSelecionado.getMarca());
//                telaVisualizarStatus.putExtra("placa", veiculoSelecionado.getPlaca());
//                clienteService.getStatus(veiculoSelecionado.getId());
//                startActivity(telaVisualizarStatus);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        recyclerViewVeiculos = findViewById(R.id.recyclerViewVeiculosRegistred);
//        recyclerViewVeiculos.setLayoutManager(new LinearLayoutManager(this));
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
//                recyclerViewVeiculos.getContext(),
//                ((LinearLayoutManager) recyclerViewVeiculos.getLayoutManager()).getOrientation()
//        );
//        recyclerViewVeiculos.addItemDecoration(dividerItemDecoration);
        getVeiculos(userCpf);

    }
    @Override
    protected void onResume() {
        super.onResume();
        carregarDados(); // Atualiza os dados toda vez que a atividade é retomada
    }

    private void carregarDados() {
        // Sua lógica para carregar dados, como fazer requisições para API, etc.
    }
    private void getClienteByID(String cpf, ClienteCallback callback) {
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


    private void getVeiculos(String cpf) {
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
                    System.out.println("jsonData: " + jsonData);

                    // Ajuste para deserializar a lista de veiculoStatusList
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<veiculoStatusList>>() {}.getType(); // Verifique se é veiculoStatusList
                    List<veiculoStatusList> veiculoStatusList = gson.fromJson(jsonData, listType); // Altere para veiculoStatusList

                    runOnUiThread(() -> {
                        if (veiculoStatusList != null && !veiculoStatusList.isEmpty()) {
                            veiculoAdapter = new VeiculoAdapter(veiculoStatusList); // Certifique-se de que o adaptador aceita veiculoStatusList
//                            recyclerViewVeiculos.setAdapter(veiculoAdapter);
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
