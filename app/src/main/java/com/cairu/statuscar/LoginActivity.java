package com.cairu.statuscar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cairu.statuscar.service.LoginCallback;
import com.cairu.statuscar.service.LoginService;
import com.cairu.statuscar.service.NotificationHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextCpf;
    private EditText editTextPassword;
    private Button buttonLogin;
    private LoginService loginService;
    private TextView textViwRecuperarSenha;
    private Button buttonCadastro;
    private NotificationHelper notificationHelper ;
    private static final String CHANNEL_ID = "statuscar_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        textViwRecuperarSenha = findViewById(R.id.editTextRecuperarSenha);
        textViwRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar a nova Activity
                Intent intent = new Intent(LoginActivity.this, RecoveryActivity.class);
                startActivity(intent);
            }
        });
        editTextCpf = findViewById(R.id.editTextCpf);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        LoginService loginService = new LoginService(this);
        //Configura botão de login
        buttonLogin.setOnClickListener(v -> {
            String cpf = editTextCpf.getText().toString();
            String password = editTextPassword.getText().toString();

            loginService.login(cpf, password, new LoginCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Login bem-sucedido", Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onFailure(Exception e) {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Falha no login: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }

            });
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}