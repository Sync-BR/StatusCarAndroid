package com.cairu.statuscar;

import static com.cairu.statuscar.R.id.btn_adicionar_veiculo;
import okhttp3.OkHttpClient;

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

public class ConsultorActivity  extends AppCompatActivity implements StatusCallback {
    private Button btnAdicionarVeiculo;
    private EditText editTextCpf;
    private Spinner spinnerVeiculos;
    private VeiculoModel veiculoSelecionado = new VeiculoModel();
    private StatusModel statusModel = new StatusModel();
    private StatusService statusService;
    private Button btnBuscar;
    private OkHttpClient client = new OkHttpClient();
    private ConsultorService consultorService;




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
        setContentView(R.layout.activity_tela_inicial_consultor);

        editTextCpf = findViewById(R.id.text_carro);
        spinnerVeiculos = findViewById(R.id.spinner_status);
        btnBuscar = findViewById(R.id.btn_buscar);

        consultorService = new ConsultorService(this, spinnerVeiculos);
        // Buscar veículos por cpf
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cpf = editTextCpf.getText().toString();
                if (!cpf.isEmpty()) {
                    System.out.println(cpf);
                    consultorService.buscarVeiculo(cpf);
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

                System.out.println("Log object: " +veiculoSelecionado.getId());
                if (item instanceof VeiculoModel) {
                    veiculoSelecionado = (VeiculoModel) item;
                    statusService = new StatusService();
                    // Exemplo de chamada para buscar o status
                    StatusModel state = statusService.buscarStatus(1);
                    System.out.println(state);
                    Intent intentStatus = new Intent(ConsultorActivity.this, StatusActivity.class);
                    System.out.println("ID DO VEICULO PARA STATUS: " +veiculoSelecionado.getId());
                    intentStatus.putExtra("modelo", veiculoSelecionado.getModelo());
                    intentStatus.putExtra("placa", veiculoSelecionado.getPlaca());
                    intentStatus.putExtra("previsao", state.getDataInicio());
                    intentStatus.putExtra("statusAtual", state.getDataFim());

                    // Iniciar a StatusActivity
             //       startActivity(intentStatus);
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

        btnAdicionarVeiculo = findViewById(btn_adicionar_veiculo);
        btnAdicionarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultorActivity.this, VeiculosActivity.class);
                startActivity(intent);
            }
        });
    }



}
