package com.cairu.statuscar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cairu.statuscar.service.CadastroService;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CadastroActivity extends AppCompatActivity {
    private EditText editTextNome, editTextCpf, editTextTelefone, editTextEmail, editTextEndereco, editTextLogin, editTextSenha;
    private Button buttonCadastrar;
    private CadastroService cadastroService;
    private Button buttonBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicializa os campos
        buttonBack = findViewById(R.id.buttonCadastroVoltar);
        editTextNome = findViewById(R.id.editTextNome);
        editTextCpf = findViewById(R.id.editTextCpf);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextEndereco = findViewById(R.id.editTextEndereco);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextSenha = findViewById(R.id.editTextSenha);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);
        cadastroService = new CadastroService();
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Captura os dados inseridos pelo usuário
                String nome = editTextNome.getText().toString();
                String cpf = editTextCpf.getText().toString();
                String telefone = editTextTelefone.getText().toString();
                String email = editTextEmail.getText().toString();
                String endereco = editTextEndereco.getText().toString();
                String login = editTextLogin.getText().toString();
                String senha = editTextSenha.getText().toString();

                // Verifica se todos os campos estão preenchidos
                if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || email.isEmpty() || endereco.isEmpty() || login.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("Cair aqui");
                    // Chama o serviço para cadastrar o usuário
                    cadastroService.cadastrarUsuario(nome, cpf, telefone, email, endereco, login, senha, CadastroActivity.this);
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
