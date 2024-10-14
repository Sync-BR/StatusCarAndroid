package com.cairu.statuscar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cairu.statuscar.interfaces.StatusCallback;
import com.cairu.statuscar.model.NotificacaoModel;
import com.cairu.statuscar.model.StatusModel;
import com.cairu.statuscar.model.VeiculoModel;
import com.cairu.statuscar.service.ConsultorService;
import com.cairu.statuscar.service.NotificationService;
import com.cairu.statuscar.service.StatusService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;

public class StatusActivity extends AppCompatActivity implements StatusCallback {
    private OkHttpClient client = new OkHttpClient();
    private Context context;
    private TextView textModelo, textPrevisao, textStatus, textPlaca;
    private Spinner spinner;
    private StatusService statusService;
    private VeiculoModel veiculoSelecionado;
    private StatusModel status;
    private EditText editTextDate;
    private int idStatus;
    private int idVeiculo;
    private Date dateFinal;
    private Button btnAtualizar;

    public int getIdStatus() {
        return idStatus;
    }

    public int getIdVeiculo() {
        return idVeiculo;
    }

    @Override
    public String toString() {
        return "StatusActivity{" +
                "client=" + client +
                ", context=" + context +
                ", textModelo=" + textModelo +
                ", textPrevisao=" + textPrevisao +
                ", textStatus=" + textStatus +
                ", textPlaca=" + textPlaca +
                ", spinner=" + spinner +
                ", statusService=" + statusService +
                ", veiculoSelecionado=" + veiculoSelecionado +
                ", status=" + status +
                '}';
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_consultor); // Certifique-se de usar o layout correto
        spinner = findViewById(R.id.spinner_status_update);
        btnAtualizar = findViewById(R.id.btn_alterarStatus);
        // Recuperar os dados que foram passados
        Intent intent = getIntent();
        String modelo = intent.getStringExtra("modelo");
        String placa = intent.getStringExtra("placa");
        String previsao = intent.getStringExtra("previsao");
        String statusAtual = intent.getStringExtra("statusAtual");
        this.idStatus = intent.getIntExtra("idStatus", -1);
        this.idVeiculo = intent.getIntExtra("idDoVeiculo", -1);
        System.out.println("id Status " +idStatus+ " id veiculo " +idVeiculo);
        statusService = new StatusService();
        editTextDate = findViewById(R.id.editTextDateUpdate);
        editTextDate.setOnClickListener(v -> {
            // Obter a data atual
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Criar e exibir o DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    StatusActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Atualizar o EditText com a data selecionada
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        System.out.println(selectedDate);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String formattedDate = dateFormat.format(calendar.getTime());
                        try {
                            dateFinal = dateFormat.parse(formattedDate);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        editTextDate.setText(formattedDate);
                        System.out.println(formattedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        ArrayAdapter<CharSequence> Arrayadapter = ArrayAdapter.createFromResource(this, R.array.status_array, android.R.layout.simple_spinner_item);
        Arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(Arrayadapter);
        TextView textCarroStatus = findViewById(R.id.text_carroStatus);

        // Atualizando o TextView com os dados do veículo
        String textoStatus = "Modelo: " + modelo + "\n" +
                "Placa: " + placa + "\n" +
                "Previsão: " + previsao + "\n" +
                "Status Atual: " + statusAtual;

        textCarroStatus.setText(textoStatus);
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cria um AlertDialog para confirmação
                new AlertDialog.Builder(StatusActivity.this)
                        .setTitle("Confirmar Atualização")
                        .setMessage("Você tem certeza que deseja atualizar o status?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (idStatus != 0) {
                                    // Obtém o status selecionado no Spinner
                                    StatusModel status = new StatusModel();
                                    status.setId(idStatus);
                                    status.setStatus(spinner.getSelectedItem().toString());
                                    // Chama o método para atualizar o status
                                    statusService.updateStatus(idStatus, status);
                                    Toast.makeText(StatusActivity.this, "Status atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                                    NotificationService notificationService = new NotificationService(StatusActivity.this);
                                    NotificacaoModel notificacaoModel = new NotificacaoModel();
                                    notificacaoModel.setId_Veiculo(getIdVeiculo());
                                    notificacaoModel.setId_Status(getIdStatus());
                                    notificacaoModel.setDescricao("Seu status do veiculo foi alterado");
                                    notificacaoModel.setDescricao("O Veiculo " + modelo + "Foi alterado para o status" + status.getStatus());
                                  //  notificationService.criarNotificacao(notificacaoModel);
                                    notificationService.consultarNotificacoesPorPlaca(placa, notificacaoModel);
                                    System.out.println("notificacaoModel: " +notificacaoModel);

                                } else {
                                    System.out.println("Erro: veiculoSelecionado é null");
                                }
                            }
                        })
                        .setNegativeButton("Não", null) // Ação ao clicar em "Não"
                        .show(); // Mostra o AlertDialog
            }
        });
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

    @Override
    public void onError(String message) {
        System.out.println("Erro ao buscar status");
    }

    private void atualizarSpinner(List<VeiculoModel> veiculos) {
        // Cria um ArrayAdapter para o Spinner
        ArrayAdapter<VeiculoModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, veiculos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Define a ação ao selecionar um item
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Pega o veículo selecionado
                veiculoSelecionado = (VeiculoModel) parent.getItemAtPosition(position);

                // Atualiza os TextViews com os dados do veículo selecionado
                statusService.buscarStatus(veiculoSelecionado.getId());
                System.out.println("teste 1 " + status);

                preencherDadosVeiculo(veiculoSelecionado, status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ação quando nada é selecionado (opcional)
            }
        });
    }

    private void preencherDadosVeiculo(VeiculoModel veiculo, StatusModel status) {
        System.out.println("1 " + status);
        System.out.println("pa");
        TextView textCarro = findViewById(R.id.text_carroStatus);
        textCarro.setText("Modelo: " + veiculo.getModelo() +
                "\nPlaca: " + veiculo.getPlaca() +
                "\nPrevisão: " + status.getDataInicio() +
                "\nStatus Atual: " + status.getDataFim());
    }
}
