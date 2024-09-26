package com.cairu.statuscar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cairu.statuscar.model.ClienteModel;
import com.cairu.statuscar.service.ClienteService;
import com.cairu.statuscar.service.VeiculoService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class VeiculosActivity extends AppCompatActivity {
    private EditText editTextCarVeiculo, editTextCarPlaca, editTextCarModelo, editTextCarAno,editTextCarStatus;
    private Button buttonCarCadastrar, buttonCarVoltar;
    private Spinner spinnerClientes;
    private int idSelected;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addveiculos);
        editTextCarVeiculo = findViewById(R.id.editTextCarVeiculo);
        editTextCarPlaca = findViewById(R.id.editTextCarPlaca);
        editTextCarModelo = findViewById(R.id.editTextCarModelo);
        editTextCarAno = findViewById(R.id.editTextCarAno);

        buttonCarCadastrar = findViewById(R.id.buttonCarCadastrar);
        buttonCarVoltar = findViewById(R.id.buttonCarVoltar);

        spinnerClientes = findViewById(R.id.spinnerClientes);
        ClienteService clienteService = new ClienteService(VeiculosActivity.this);
        List<ClienteModel> clienteModels = clienteService.consumirClientes();
        System.out.println(clienteModels);
        // Cria um ArrayAdapter usando o layout padrão e a lista de clientes
  

        ArrayAdapter<ClienteModel> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,clienteModels
        );

        System.out.println("IDS: " +adapter.getContext());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Define o listener para o Spinner
        spinnerClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ClienteModel selectedCliente = (ClienteModel) parent.getItemAtPosition(position);
                // Agora você tem o objeto ClienteModel, pode acessar suas propriedades
                idSelected = selectedCliente.getId();
                System.out.println(idSelected+ "ID SELECIONADO");
                String nomeCliente = selectedCliente.getNome(); // Supondo que exista um método getNome() em ClienteModel
                Toast.makeText(VeiculosActivity.this, "Cliente selecionado: " + nomeCliente, Toast.LENGTH_SHORT).show();
                // Adicione a lógica que deseja com o cliente selecionado
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nada a fazer
            }
        });
        buttonCarCadastrar.setOnClickListener(v -> cadastrarVeiculo());
        buttonCarVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(VeiculosActivity.this, ConsultorActivity.class);
            startActivity(intent);
            finish();
        });
    }



    public void atualizarClientes(List<ClienteModel> clientes) {
        // Criar uma lista de nomes para o Spinner
        ArrayAdapter<ClienteModel> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                clientes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientes.setAdapter(adapter);
    }
    private void cadastrarVeiculo() {
        int id = idSelected;
        System.out.println("ID CHEGANDO: " +id);
        String veiculo = editTextCarVeiculo.getText().toString();
        String placa = editTextCarPlaca.getText().toString();
        String modelo = editTextCarModelo.getText().toString();
        int ano = Integer.parseInt(editTextCarAno.getText().toString());

        if (veiculo.isEmpty() || placa.isEmpty() || modelo.isEmpty() || ano <= 0) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }
        System.out.println("teste 1");
        VeiculoService veiculoService = new VeiculoService();
        veiculoService.cadastrarVeiculos(id,veiculo,placa, modelo,ano, this);
    }

    private void showBottomSheet() {
        BottomSheetMenuFragment bottomSheetMenuFragment = new BottomSheetMenuFragment();
        bottomSheetMenuFragment.show(getSupportFragmentManager(), bottomSheetMenuFragment.getTag());
    }

    public static class BottomSheetMenuFragment extends BottomSheetDialogFragment implements com.cairu.statuscar.BottomSheetMenuFragment {

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.bottom_sheet_menu, container, false);

            Button btnAddVehicle = view.findViewById(R.id.btnAddVehicle);
            Button btnRemoveVehicle = view.findViewById(R.id.btnRemoveVehicle);

            btnAddVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Adicionar lógica para adicionar veículo
                    Toast.makeText(getContext(), "Adicionar Veículo", Toast.LENGTH_SHORT).show();
                }
            });

            btnRemoveVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Adicionar lógica para remover veículo
                    Toast.makeText(getContext(), "Remover Veículo", Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }
    }
}