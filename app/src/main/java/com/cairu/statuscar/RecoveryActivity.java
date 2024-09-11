package com.cairu.statuscar;

import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cairu.statuscar.service.RecoveryService;
import okhttp3.OkHttpClient;


public class RecoveryActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private Button buttonSubmit;
    private OkHttpClient cliente;
    private Button buttonBack;  // Declaração do botão de voltar
    private RecoveryService recoveryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonBack = findViewById(R.id.buttonBack);  

        cliente = new OkHttpClient();
        recoveryService = new RecoveryService();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(RecoveryActivity.this, "Por favor, digite seu e-mail", Toast.LENGTH_SHORT).show();
                } else {
                    // Implementar a lógica para enviar a solicitação de recuperação de senha
                    recoveryService.recuperarSenha(email, RecoveryActivity.this);
                }
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Finaliza a RecoveryActivity, voltando para a MainActivity
            }
        });

    }



}
