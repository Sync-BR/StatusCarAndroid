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

public class StatusConfirmacaoActivity extends AppCompatActivity{
    private TextView textStatusConfirmacao;
    private Button btnVoltarInicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_status_confirmacao);
        textStatusConfirmacao = findViewById(R.id.text_status_confirmacao);
        btnVoltarInicio = findViewById(R.id.btn_voltar_pg_inicial_consultor);
        btnVoltarInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatusConfirmacaoActivity.this, ConsultorActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.status_confirmacao), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recupera os dados do Intent
        Intent intent = getIntent();
        VeiculoModel veiculo = (VeiculoModel) intent.getSerializableExtra("veiculo");
        StatusModel status = (StatusModel) intent.getSerializableExtra("status");

        // Preencher os TextViews com os dados recebidos
        TextView textStatusConfirmacao = findViewById(R.id.text_status_confirmacao);
        textStatusConfirmacao.setText("Modelo: " + veiculo.getModelo() +
                "\nMarca: " + veiculo.getMarca() +
                "\nPlaca: " + veiculo.getPlaca() +
                "\nPrevisão: " + status.getDataFim() +
                "\nStatus Atual: " + status.getStatus());
    }
}
