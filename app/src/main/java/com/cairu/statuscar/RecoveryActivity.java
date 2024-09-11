package com.cairu.statuscar;

import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cairu.statuscar.service.LoginCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecoveryActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private Button buttonSubmit;
    private OkHttpClient cliente;
    private Button buttonBack;  // Declaração do botão de voltar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonBack = findViewById(R.id.buttonBack);  // Inicialização do botão de voltar

        cliente = new OkHttpClient();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(RecoveryActivity.this, "Por favor, digite seu e-mail", Toast.LENGTH_SHORT).show();
                } else {
                    // Implementar a lógica para enviar a solicitação de recuperação de senha
                    recuperarSenha(email);
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


    public void recuperarSenha(String email) {
        String url = "http://186.247.89.58:8080/api/user/usersLogin" + email;
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(null, new byte[0]))
                .build();

        cliente.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(RecoveryActivity.this, "Erro na solicitação", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(RecoveryActivity.this, "Instruções de recuperação de senha enviadas", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RecoveryActivity.this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
