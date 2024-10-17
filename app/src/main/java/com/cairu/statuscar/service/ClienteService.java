package com.cairu.statuscar.service;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cairu.statuscar.VeiculosActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import com.cairu.statuscar.interfaces.VeiculoCallback;
import com.cairu.statuscar.model.ClienteModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ClienteService {
    private  OkHttpClient cliente;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private Context context;
    public ClienteService(Context context) {
        this.cliente = new OkHttpClient();
        this.context = context; // Contexto é atribuído
    }

    public List<ClienteModel> consumirClientes(){
        String url = "http://186.247.89.58:8080/api/allClientes";
        List<ClienteModel> listClientes = new ArrayList<>();
        Request request = new Request.Builder()
                .url(url)
                .build();
        cliente.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ((VeiculosActivity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Erro ao obter clientes", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Type clienteListType = new TypeToken<List<ClienteModel>>() {}.getType();
                    List<ClienteModel> clientes = new Gson().fromJson(responseBody, clienteListType);
                    listClientes.addAll(clientes);

                    ((VeiculosActivity) context).runOnUiThread(() -> {
                        // Atualizar a UI com a lista de clientes

                        ((VeiculosActivity) context).atualizarClientes(clientes);
                    });
                } else {
                    ((VeiculosActivity) context).runOnUiThread(() ->
                            Toast.makeText(context, "Erro ao obter clientes", Toast.LENGTH_SHORT).show());
                }
            }
        });
        return listClientes;
    }

    public void buscarVeiculo(String cpf, VeiculoCallback callback){
        ArrayList<VeiculoModel> lista = new ArrayList<VeiculoModel>();

        VeiculoModel veiculo1 = new VeiculoModel();
        veiculo1.setAno(2021);
        veiculo1.setPlaca("ACC2201");
        veiculo1.setMarca("Fiat");
        veiculo1.setModelo("Palio");
        veiculo1.setPrevisao("2022-12-01 12:00:00");

        VeiculoModel veiculo2 = new VeiculoModel();
        veiculo2.setAno(2022);
        veiculo2.setPlaca("ACC2202");
        veiculo2.setMarca("Fiat");
        veiculo2.setModelo("Uno");
        veiculo2.setPrevisao("2022-12-01 12:00:00");

        VeiculoModel veiculo3 = new VeiculoModel();
        veiculo3.setAno(2022);
        veiculo3.setPlaca("ACC2202");
        veiculo3.setMarca("Fiat");
        veiculo3.setModelo("Uno");
        veiculo3.setPrevisao("2022-12-01 12:00:00");

        lista.add(veiculo1);
        lista.add(veiculo2);
        lista.add(veiculo3);

        atualizarSpinner(lista);

        /*String url = "http://186.247.89.58:8080/api/veiculos/consultar/veiculos/cpf/"+cpf   ;
        System.out.println(cpf);
        Request request = new Request.Builder()
                .url(url)
                .build();

        cliente.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    System.out.println(jsonData);
                    List<VeiculoModel> veiculos = new Gson().fromJson(jsonData, new TypeToken<List<VeiculoModel>>() {}.getType());

                    // Atualiza o Spinner na thread principal
                    ((AppCompatActivity) context).runOnUiThread(() -> {
                        atualizarSpinner(veiculos);
                    });
                }

            }
        });*/

    }


    public void atualizarSpinner(List<VeiculoModel> veiculos) {

        // Verifica se a lista de veículos não está vazia
        if (veiculos != null && !veiculos.isEmpty()) {
            System.out.println("Populando o Spinner com veículos: " + veiculos.toString());

        ArrayAdapter<VeiculoModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, veiculos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVeiculos.setAdapter(adapter);

        System.out.println(adapter);
        spinnerVeiculos.setAdapter(adapter);
        } else {
            System.out.println("A lista de veículos está vazia ou nula");
        }

        //  List<String> veiculosPlacas = new ArrayList<>();
        //  for(VeiculoModel veiculoModel: veiculos){
        //      veiculosPlacas.add(veiculoModel.getPlaca());
        // }

        //   ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, veiculosPlacas);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }


}
