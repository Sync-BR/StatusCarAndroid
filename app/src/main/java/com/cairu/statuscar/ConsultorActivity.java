package com.cairu.statuscar;

import okhttp3.OkHttpClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cairu.statuscar.interfaces.StatusCallback;
import com.cairu.statuscar.model.StatusModel;
import com.cairu.statuscar.model.VeiculoModel;
import com.cairu.statuscar.service.ConsultorService;
import com.cairu.statuscar.service.StatusService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class ConsultorActivity  extends AppCompatActivity implements StatusCallback {
    private Button btnAdicionarVeiculo;
    private Button buttonCadastro;
    private EditText editTextCpf;
    private Spinner spinnerVeiculos;
    private VeiculoModel veiculoSelecionado = new VeiculoModel();
    private StatusModel statusModel = new StatusModel();
    private StatusService statusService;
    private Button btnBuscar;
    private OkHttpClient client = new OkHttpClient();
    private ConsultorService consultorService;
    private Button btnConsutorEditar;



    @Override
    public String toString() {
        return "ConsultorActivity{" +
                "btnAdicionarVeiculo=" + btnAdicionarVeiculo +
                ", editTextCpf=" + editTextCpf +
                ", spinnerVeiculos=" + spinnerVeiculos +
                ", veiculoSelecionado=" + veiculoSelecionado +
                ", btnBuscar=" + btnBuscar +
                ", client=" + client +
                ", consultorService=" + consultorService +
                '}';
    }
    @Override
    public void onStatusReceived(StatusModel status) {
        // Lógica para lidar com o status recebido
        System.out.println("Status recebido: " + status);
    }

    @Override
    public void onFailure(IOException e) {
        // Lógica para lidar com falhas
        System.out.println("Erro ao buscar status: " + e.getMessage());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusModel state = new StatusModel();
        setContentView(R.layout.activity_tela_inicial_consultor);
        buttonCadastro = findViewById(R.id.buttonCadastrar);
        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultorActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });


        editTextCpf = findViewById(R.id.text_carro);
        spinnerVeiculos = findViewById(R.id.spinner_status);
        btnBuscar = findViewById(R.id.btn_buscar);
        btnConsutorEditar = findViewById(R.id.btn_consutor_editar);
        consultorService = new ConsultorService(this, spinnerVeiculos);
        // Buscar veículos por cpf
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cpf = editTextCpf.getText().toString();
                if (!cpf.isEmpty()) {
                    System.out.println(cpf);
                    consultorService.buscarVeiculo(cpf);
                    btnConsutorEditar.setEnabled(true);
                } else {
                    Toast.makeText(ConsultorActivity.this, "Por favor, insira um CPF.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerVeiculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                veiculoSelecionado = (VeiculoModel) parent.getItemAtPosition(position);

                if (item instanceof VeiculoModel) {
                    veiculoSelecionado = (VeiculoModel) item;
                    statusService = new StatusService();
                    // Exemplo de chamada para buscar o status
                  //  StatusModel state = statusService.buscarStatus((int) id);

                    Intent intentStatus = new Intent(ConsultorActivity.this, StatusActivity.class);

                    intentStatus.putExtra("modelo", veiculoSelecionado.getModelo());
                    intentStatus.putExtra("placa", veiculoSelecionado.getPlaca());
                    intentStatus.putExtra("previsao", state.getDataInicio());
                    intentStatus.putExtra("statusAtual", state.getDataFim());
                    intentStatus.putExtra("idVeiculo", veiculoSelecionado.getId());
                   // startActivity(intentStatus);


                    // Iniciar a StatusActivity
                 //   startActivity(intentStatus);
                    Toast.makeText(ConsultorActivity.this, "Placa selecionada: " + veiculoSelecionado.getPlaca(), Toast.LENGTH_SHORT).show();
                    // Aqui você pode fazer algo com a placa selecionada, como buscar mais informações.
                } else {
                    // Lidar com o caso em que o item não é do tipo esperado
                    Toast.makeText(ConsultorActivity.this, "Item selecionado não é um veículo.", Toast.LENGTH_SHORT).show();
                }

//                VeiculoModel veiculoSelecionado = (VeiculoModel)  parent.getItemAtPosition(position);
//                int idVeiculo = veiculoSelecionado.getId();
//                System.out.println("Id do veiculo selecionado: " +idVeiculo);
//                Toast.makeText(ConsultorActivity.this, "Placa selecionada: " + veiculoSelecionado.getPlaca(), Toast.LENGTH_SHORT).show();
//                // Aqui você pode fazer algo com a placa selecionada, como buscar mais informações.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ação quando nada é selecionado, se necessário.
            }
        });
        btnConsutorEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (veiculoSelecionado != null) {
                    // Iniciando a intent para StatusActivity
                    Intent intent = new Intent(ConsultorActivity.this, StatusActivity.class);

                    // Buscando o status do veículo
                    StatusModel statusModel = new StatusModel();

                    CompletableFuture<StatusModel> futureStatus = statusService.buscarStatus(veiculoSelecionado.getId());

                    futureStatus.thenAccept(status -> {
                        // Aqui você pode trabalhar com o status retornado
                        intent.putExtra("idDoVeiculo", veiculoSelecionado.getId());
                        intent.putExtra("modelo", veiculoSelecionado.getModelo());
                        intent.putExtra("placa", veiculoSelecionado.getPlaca());
                        intent.putExtra("previsao", status.getDataFim().toString());  // Previsão do status
                        intent.putExtra("statusAtual", status.getStatus());  // Status atual
                        intent.putExtra("idStatus", status.getId());

                        // Iniciando a StatusActivity
                        startActivity(intent);
                        statusModel.setDataFim(status.getDataFim());
                    }).exceptionally(e -> {
                        // Trate a falha
                        System.out.println("Erro ao buscar status: " + e.getMessage());
                        return null;
                    });

                    System.out.println("testa status " +futureStatus);
                    // Verificando o valor da previsão
                    Date dataFim = state.getDataFim();

                    // Formato da data para o padrão brasileiro

                    // Passando as informações do veículo e do status


                } else {
                    Toast.makeText(ConsultorActivity.this, "Por favor, selecione um veículo.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnAdicionarVeiculo = findViewById(R.id.btnAdicionarVeiculo);
        btnAdicionarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultorActivity.this, VeiculosActivity.class);
                startActivity(intent);
            }
        });
    }



}
