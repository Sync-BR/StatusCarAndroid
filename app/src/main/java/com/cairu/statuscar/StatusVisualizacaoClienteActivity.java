package com.cairu.statuscar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cairu.statuscar.model.StatusModel;
import com.cairu.statuscar.model.VeiculoModel;


public class StatusVisualizacaoClienteActivity extends AppCompatActivity {
    private TextView textStatusVisualizacaoCliente;
    private Button btnVoltarInicioCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_status_visualizacao_cliente);
        textStatusVisualizacaoCliente = findViewById(R.id.text_status_visualizacao_cliente);
        btnVoltarInicioCliente = findViewById(R.id.btn_voltar_pg_inicial_cliente);
        btnVoltarInicioCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatusVisualizacaoClienteActivity.this, UsuarioActivity.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.status_visualizacao_cliente), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recupera os dados do Intent
        Intent intent = getIntent();
        VeiculoModel veiculo = (VeiculoModel) intent.getSerializableExtra("veiculo");
        StatusModel status = (StatusModel) intent.getSerializableExtra("status");

        // Preencher os TextViews com os dados recebidos
        TextView textStatusConfirmacao = findViewById(R.id.text_status_visualizacao_cliente);
        textStatusConfirmacao.setText("Modelo: " + veiculo.getModelo() +
                "\nMarca: " + veiculo.getMarca() +
                "\nPlaca: " + veiculo.getPlaca() +
                "\nPrevisão: " + status.getDataFim() +
                "\nStatus Atual: " + status.getStatus());
    }
}

