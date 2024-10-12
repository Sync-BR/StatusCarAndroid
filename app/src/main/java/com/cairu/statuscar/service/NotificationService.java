package com.cairu.statuscar.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.cairu.statuscar.R;
import com.cairu.statuscar.UsuarioActivity;
import com.cairu.statuscar.model.NotificacaoModel;
import com.cairu.statuscar.model.VeiculoModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationService extends AppCompatActivity {
    private Context context;
    private static final String CANAL_ID = "my_channel_id";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();
    private List<NotificacaoModel> notificacaoModels;
    private List<VeiculoModel> veiculos;

    public NotificationService(Context context) {
        this.context = context;
        createNotificationChannel();
        notificacaoModels = new ArrayList<>();
        veiculos = new ArrayList<>();
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

    public void consultarNotificacoesPorPlaca(String placa, NotificacaoModel notificacaoModel) {
        System.out.println("Teste de notificação: " +notificacaoModel);
        String url = "http://186.247.89.58:8080/notification/consultar/" + placa;
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Lida com falhas de conexão ou requisição
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    NotificacaoModel notification = new Gson().fromJson(jsonResponse, NotificacaoModel.class);
                    if (notification.getId_Veiculo() == 0) {
                        criarNotificacao(notificacaoModel);  // Passar um objeto ao invés de uma lista

                    } else {
                           // deleteNotification(notificacaoModel.getId_Notification());

                    }
                } else {
                    System.out.println("Erro ao consultar notificações: " + response.code());
                }
            }
        });
    }

    public boolean deleteNotification(int id) {
        String url = "http://186.247.89.58:8080/notification/delete/by/" + id;
        Request request = new Request.Builder()
                .url(url)
                .delete() // Método DELETE
                .build();

        try {
            // Chamada síncrona
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // Notificação deletada com sucesso
                showToast("Notificação deletada com sucesso");
                return true; // Sucesso
            } else {
                // Falha ao deletar a notificação
                showToast("Falha ao deletar a notificação");
                return false; // Falha
            }
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Erro de rede");
            return false; // Falha devido a erro de rede
        }
    }

    private void showToast(final String message) {
        // Verifique se estamos na UI thread
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        });
    }

    public void criarNotificacao(NotificacaoModel notificacaoModel) {
        notificacaoModel.setDescricao("O Status do seu veiculo foi atualizado!");
        String url = "http://186.247.89.58:8080/notification/enviar";
        String json = new Gson().toJson(notificacaoModel);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder().url(url).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("Notificação enviada com sucesso: " + response.code());
                } else {
                    System.out.println("Erro ao enviar notificação: " + response.code());
                    System.out.println("Erro detalhes: " + response.body().string()); // Log do corpo da resposta
                }
            }
        });
    }

    public void enviarNotificao(String status, String veiculo, int statusID) {
        System.out.println("statusID" +statusID);
        Intent intent = new Intent(context, UsuarioActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CANAL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Status do veículo alterado")
                .setContentText("O Status do veículo " + veiculo + " foi alterado")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_HIGH);



        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            int notificationId = (int) System.currentTimeMillis(); // Gera um ID único
            notificationManager.notify(notificationId, builder.build());

            // Agendar a remoção da notificação após 10 segundos (10000 ms)
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                StatusService statusService = new StatusService();
                try {
                    int statusRetornado = statusService.buscarStatus(statusID).get().getId(); // Bloqueia até o retorno do ID
                    System.out.println("Status ID retornado: " + statusRetornado);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                System.out.println("Busca status " +statusService.buscarStatus(statusID));
            }, 10000);
        }
    }

    public void verificarNotificacao(String cpf, NotificationCallback callback) {
        String url = "http://186.247.89.58:8080/notification/consultarID/" + cpf;
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(NotificationService.this, "Falha ao buscar dados", Toast.LENGTH_SHORT).show();
                    callback.onResult(false); // Chama o callback com false
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    if (jsonData != null && !jsonData.isEmpty()) {
                        int id = Integer.parseInt(jsonData.trim());
                        if (id > 0) {
                            enviarNotificao("", "Veículo alterado", id);
                            callback.onResult(true); // Chama o callback com true
                        }
                    } else {
                        //       Toast.makeText(NotificationService.this, "Nenhuma notificação encontrada", Toast.LENGTH_SHORT).show();
//                        runOnUiThread(() -> {
//                            callback.onResult(false); // Chama o callback com false
//                        });
                    }
                } else {
                    runOnUiThread(() -> {
//                        Toast.makeText(NotificationService.this, "Erro na resposta da API", Toast.LENGTH_SHORT).show();
                        callback.onResult(false); // Chama o callback com false
                    });
                }
            }
        });
    }

    public interface NotificationCallback {
        void onResult(boolean success);
    }
}
