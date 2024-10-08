package com.cairu.statuscar.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.widget.Toast;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.cairu.statuscar.R;
import com.cairu.statuscar.UsuarioActivity;
import com.cairu.statuscar.adapter.VeiculoAdapter;
import com.cairu.statuscar.model.NotificacaoModel;
import com.cairu.statuscar.model.StatusModel;
import com.cairu.statuscar.model.VeiculoModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationService extends AppCompatActivity {
    private Context context;
    private static final String CANAL_ID = "my_channel_id";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final int REQUEST_CODE = 100; //

    private OkHttpClient client = new OkHttpClient();
    private List<NotificacaoModel> notificacaoModels;
    private List<VeiculoModel> veiculos ;


    public NotificationService(Context context) {
        this.context = context;
        createNotificationChannel();
        notificacaoModels = new ArrayList<>();
        veiculos = new ArrayList<>();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CANAL_ID, "Nome do Canal", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CANAL_ID, "Nome do Canal", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    public void consultarNotificacoesPorPlaca(String placa) {
        String url = "http://186.247.89.58:8080/notification/consultar/" + placa;
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Lida com falhas de conexão ou requisição
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();
                    NotificacaoModel notification = gson.fromJson(jsonResponse, NotificacaoModel.class);
                    NotificacaoModel nt = notification;
                    criarNotificacao(nt);  // Passar um objeto ao invés de uma lista

                } else {
                    System.out.println("Erro ao consultar notificações: " + response.code());
                }
            }
        });
    }


    public void criarNotificacao(NotificacaoModel notificacaoModel) {
        System.out.println("criarNotificacao "+notificacaoModel);
        notificacaoModel.setData(null);
        // Converte o objeto NotificationModel em JSON
        String url = "http://186.247.89.58:8080/notification/enviar" ;

        String json = new Gson().toJson(notificacaoModel);
        System.out.println("json " +json);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Lida com a falha
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("Notificação enviada com sucesso: " + response.code());
                } else {
                    System.out.println("Erro ao enviar notificação: " + response.code());
                }
            }
        });
    }
    public void enviarNotificao(String status, String veiculo){

        System.out.println("Enviando notificação para o veículo: " + veiculo);
        Intent intent = new Intent(context, UsuarioActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CANAL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Status do veiculo alterado")
                .setContentText("O Status do veiculo "+veiculo+ " Foi alterado para " +status)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        System.out.println(notificationManager);
        if (notificationManager != null) {
       //     notificationManager.notify(4, builder.build());
            notificationManager.notify((int) System.currentTimeMillis(), builder.build()); // Gera um ID único

        }
    }


    public boolean verificarNotificacao(String cpf) {
        System.out.println(cpf);
        OkHttpClient client = new OkHttpClient();
        String url = "http://186.247.89.58:8080/notification/consultarID/" + cpf;
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Aqui, você deve usar um Contexto válido. Como agora é um Service, use 'NotificationService.this'.
                Toast.makeText(NotificationService.this, "Falha ao buscar dados", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    if (jsonData != null && !jsonData.isEmpty()) {
                        int id = Integer.parseInt(jsonData.trim());
                        if (id > 0) {
                            enviarNotificao("Pendente", "Veiculo alterado");
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(NotificationService.this, "Nenhuma notificação encontrada", Toast.LENGTH_SHORT).show());
                    }
                } else {
                 //   runOnUiThread(() -> Toast.makeText(NotificationService.this, "Erro na resposta da API", Toast.LENGTH_SHORT).show());
                }
            }
        });
        return false; // O retorno do método pode não ser útil aqui devido à natureza assíncrona
    }



}
