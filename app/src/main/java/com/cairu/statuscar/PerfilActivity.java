package com.cairu.statuscar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PerfilActivity extends AppCompatActivity {
    private EditText editNome, editCpf, editTelefone, editEmail, editEndereco;
    private Button buttonUpdateUser;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        buttonUpdateUser = findViewById(R.id.buttonSave);
        Intent intent = getIntent();
        editNome = findViewById(R.id.editTextNome);
        editCpf = findViewById(R.id.editTextCpf);
        editCpf.setEnabled(false);
        editTelefone = findViewById(R.id.editTextTelefone);
        editEmail = findViewById(R.id.editTextEmail);
        editEndereco = findViewById(R.id.editTextEndereco);
        int id = intent.getIntExtra("id", -1);
        editNome.setText(intent.getStringExtra("nome"));
        editCpf.setText(intent.getStringExtra("cpf"));
        editTelefone.setText(intent.getStringExtra("telefone"));
        editEmail.setText(intent.getStringExtra("email"));
        editEndereco.setText(intent.getStringExtra("endereco"));
        String password = intent.getStringExtra("senha");

        buttonUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Exibir caixa de diálogo de confirmação
                new AlertDialog.Builder(PerfilActivity.this)
                        .setTitle("Confirmação")
                        .setMessage("Você tem certeza que deseja atualizar os dados do cliente?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Execute a operação de rede em uma thread separada
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            updateCliente(id, editNome.getText().toString(), editEmail.getText().toString(), editTelefone.getText().toString(), editCpf.getText().toString(), editEndereco.getText().toString(), password);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Cancelar a atualização
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    public void updateCliente(int id, String nome, String email, String telefone, String cpf, String endereco, String password) throws IOException {
        // Cria o JSON com os dados do cliente
        String json = "{"
                + "\"id\":" + id + ","  // Corrigido: sem aspas
                + "\"nome\":\"" + nome + "\","
                + "\"endereco\":\"" + endereco + "\","
                + "\"email\":\"" + email + "\","
                + "\"telefone\":\"" + telefone + "\","
                + "\"cpf\":\"" + cpf + "\","
                + "\"senha\":\"" + password + "\""
                + "}";

        // Cria o corpo da requisição com o JSON
        RequestBody body = RequestBody.create(
                json, MediaType.get("application/json; charset=utf-8"));

        // Monta a URL da requisição com o ID do cliente
        String url = "http://186.247.89.58:8080/api/user/update" + id;
        System.out.println(url);

        // Cria a requisição PUT
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        // Envia a requisição e processa a resposta
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                runOnUiThread(() -> Toast.makeText(PerfilActivity.this, "Cliente atualizado com sucesso!", Toast.LENGTH_SHORT).show());
            } else {
                runOnUiThread(() -> Toast.makeText(PerfilActivity.this, "Falha ao atualizar cliente: " + response.message(), Toast.LENGTH_SHORT).show());
            }
        }
    }
}
