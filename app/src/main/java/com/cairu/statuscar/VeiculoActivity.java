package com.cairu.statuscar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cairu.statuscar.service.VeiculoService;
import okhttp3.OkHttpClient;
public class VeiculoActivity extends AppCompatActivity {
    private EditText editTextVeiculo, editTextModelo, editTextPlaca, editTextAno;
    private Button buttonCadastrarVeiculo;
    private VeiculoService veiculoService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculo);


        editTextVeiculo = findViewById(R.id.editTextCarVeiculo);
        editTextModelo = findViewById(R.id.editTextCarModelo);
        editTextPlaca = findViewById(R.id.editTextCarPlaca);
        editTextAno = findViewById(R.id.editTextCarAno);
        buttonCadastrarVeiculo = findViewById(R.id.buttonCarCadastrar);

        veiculoService = new VeiculoService(new OkHttpClient());

        buttonCadastrarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Captura os dados inseridos pelo usuário
                String veiculo = editTextVeiculo.getText().toString();
                String modelo = editTextModelo.getText().toString();
                String placa = editTextPlaca.getText().toString();
                String anoStr = editTextAno.getText().toString();

                // Verifica se todos os campos estão preenchidos
                if (veiculo.isEmpty() || modelo.isEmpty() || placa.isEmpty() || anoStr.isEmpty()) {
                    Toast.makeText(VeiculoActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    int ano = Integer.parseInt(anoStr); // Converte o ano para inteiro
                    // Chama o serviço para cadastrar o veículo
                    veiculoService.cadastrarVeiculo(veiculo, placa, modelo, ano, VeiculoActivity.this);
                }
            }
        });

        }

}
