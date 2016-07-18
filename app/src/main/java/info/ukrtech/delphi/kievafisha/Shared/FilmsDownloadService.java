package info.ukrtech.delphi.kievafisha.Shared;

/**
 * Created by Delphi on 12.05.2015.
 */

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FilmsDownloadService extends Service{



    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    public static boolean serviceState=false;
    private String dat_pokaz;
    private String tab_name;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            sendResponseFromService(null, 5);
            downloadDataFromWeb();
            stopSelf(msg.arg1);
        }
    }


    @Override
    public void onCreate() {
        serviceState=true;
        HandlerThread thread = new HandlerThread("ServiceStartArguments",1);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        dat_pokaz = intent.getStringExtra("dat_pokaz"); //Дата выборки
        tab_name = intent.getStringExtra("tab_name"); //Из какой таблицы выбирать

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);
        // Если сервис завершился, то рестарт отсюда
        return START_STICKY;
    }


    void sendResponseFromService(Bundle bundle, int status){
        Intent intent = new Intent(SharedMethods.ACTION_DOWNLOAD_FILMS_LIST);
        intent.putExtra("status", status); //5 - Начало , 10 - Окончание
        intent.putExtra("data", bundle);
        sendBroadcast(intent);
    }



    @Override
    public void onDestroy() {
        serviceState=false;
    }



    //Загрузка данных
    public void downloadDataFromWeb() {
        new Thread(new Runnable() {
            public void run() {
                /*Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();*/

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SharedMethods.base_url)

                        .addConverterFactory(GsonConverterFactory.create())
                        .build();


                Calendar c = Calendar.getInstance();

                /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String dat_pokaz = df.format(c.getTime());*/

                Api service = retrofit.create(Api.class);
                Call<List<FilmsItem>> call = service.filmsList(dat_pokaz, tab_name);

                call.enqueue(new Callback<List<FilmsItem>>() {
                    @Override
                    public void onResponse(Call<List<FilmsItem>> call, Response<List<FilmsItem>> response) {
                        if (response.isSuccessful()) {

                            SharedMethods.inetFilms = response.body();

                            //Нужно сохранить в БД
                            SharedMethods.saveToDB(getApplicationContext(), dat_pokaz, tab_name);

                            sendResponseFromService(null, 10);
                        } else {
                            try {
                                SharedMethods.logMessage(" DownloadFilms #1" + response.errorBody().string());
                            } catch (IOException e) {
                                SharedMethods.logMessage(" DownloadFilms #2" + e.getMessage());
                            }
                            SharedMethods.hideProgressDialog();
                            Toast.makeText(getApplicationContext(), "Ошибка при получении данных", Toast.LENGTH_SHORT);
                            sendResponseFromService(null, 10);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FilmsItem>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                        SharedMethods.hideProgressDialog();
                        sendResponseFromService(null, 10);
                    }
                });


            }
        }).start();

    }




    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}