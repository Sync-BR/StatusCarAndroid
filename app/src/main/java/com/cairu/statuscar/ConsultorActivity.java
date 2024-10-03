package com.cairu.statuscar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cairu.statuscar.adapter.VeiculoAdapter;

public class ConsultorActivity  extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VeiculoAdapter veiculoAdapter;
    private  Context context;

    public ConsultorActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial_consultor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_consultores, menu);
        System.out.println("menu: " +menu.toString());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Long id = (long) item.getItemId();
        System.out.println(id);
        if(id == 2131231019L){
            Intent telaAddVeiculo = new Intent(this, VeiculosActivity.class);
            startActivity(telaAddVeiculo);
            Toast.makeText(this, "Adicionar Veículo selecionado", Toast.LENGTH_SHORT).show();
            return true;
        } else if(id == 213123101L){
            Intent telaDeletarVeiculo = new Intent(this, DeleteVeiculosActivity.class);
            startActivity(telaDeletarVeiculo);
        }


        /*
        if (item.getItemId() == R.id.menu_adicionar) {
            return true;
        } else if (item.getItemId() == R.id.menu_apagar) {
            Toast.makeText(this, "Remover Veículo selecionado", Toast.LENGTH_SHORT).show();
            return true;
        }

         */
        return super.onOptionsItemSelected(item);
    }

}
