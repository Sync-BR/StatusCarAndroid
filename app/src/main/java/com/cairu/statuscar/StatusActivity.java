package com.cairu.statuscar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.cairu.statuscar.interfaces.StatusCallback;
import com.cairu.statuscar.model.StatusModel;
import com.cairu.statuscar.model.VeiculoModel;
import com.cairu.statuscar.service.StatusService;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;


public class StatusActivity extends AppCompatActivity  implements StatusCallback {
    private OkHttpClient client = new OkHttpClient();
    private Context context;
    private TextView textModelo, textPrevisao, textStatus, textPlaca;
    private Spinner spinner;
    private StatusService statusService;
    private VeiculoModel veiculoSelecionado;
    private StatusModel status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_consultor);

        // Inicializa o TextView
        TextView textCarroStatus = findViewById(R.id.text_carroStatus);

        // Recebe os dados passados pela Intent
        Intent intent = getIntent();
        String modelo = intent.getStringExtra("modelo");
        String placa = intent.getStringExtra("placa");
        String previsao = intent.getStringExtra("previsao");
        String statusAtual = intent.getStringExtra("statusAtual");
        textCarroStatus.setText("Modelo: " + modelo +
                "\nPlaca: " + placa +
                "\nPrevisão: " + previsao +
                "\nStatus Atual: " + statusAtual);
        // Recebe os dados passados pela Intent
        String cpf = intent.getStringExtra("cpf");

        // Inicializa o StatusService
        statusService = new StatusService();
        statusService.buscarStatus(1);



    }
    @Override
    public void onStatusReceived(StatusModel status) {
        // Aqui você recebe o status
        System.out.println("Status recebido: " + status);
    }

    @Override
    public void onFailure(IOException e) {
        // Aqui você lida com a falha
        System.out.println("Erro ao buscar status: " + e.getMessage());
    }
    private void atualizarSpinner(List<VeiculoModel> veiculos) {
        // Cria um ArrayAdapter para o Spinner
        ArrayAdapter<VeiculoModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, veiculos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        StatusService statusService = new StatusService();

        // Define a ação ao selecionar um item
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Pega o veículo selecionado
                VeiculoModel veiculoSelecionado = (VeiculoModel) parent.getItemAtPosition(position);

                // Atualiza os TextViews com os dados do veículo selecionado
                statusService.buscarStatus(veiculoSelecionado.getId());
                System.out.println("teste 1 " +status);

                preencherDadosVeiculo(veiculoSelecionado, status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ação quando nada é selecionado (opcional)
            }
        });
    }


    private void preencherDadosVeiculo(VeiculoModel veiculo, StatusModel status) {
        System.out.println("1 "+status);
        System.out.println("pa");
        TextView textCarro = findViewById(R.id.text_carroStatus);
        textCarro.setText("Modelo: " + veiculo.getModelo() +
                "\nPlaca: " + "veiculo.getPlaca()" +
                "\nPrevisão: " + status.getDataInicio() +
                "\nStatus Atual: " + "status.getDataFim()");
    }




}
