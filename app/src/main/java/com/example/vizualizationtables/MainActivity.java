package com.example.vizualizationtables;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ListView listClients;
    Button buttonUpdateCodeNameClients;
    ArrayList<ClientsCodeNameModel> arrayListCodeNameClients = null;
    ArrayList<ClientInfoModel> arrayListClientInfo = null; //СКОРЕЕ ВСЕГО DELETE
    private MyDbManager myDbManager;
    private ProgressDialog progressDialog;


    //КЛАСС ДЛЯ СОВЕРШЕНИЯ ДЕЙСТВИЙ В ПРИЛОЖЕНИИ В ПАРАЛЛЕЛЬНОМ ПОТОКЕ
    //В ДАННОМ СЛУЧАЕ ДЛЯ ВЫПОЛНЕНИЯ HTTP ЗАПРОСА
    class CodeNameClientsQueryTask extends AsyncTask<String, Void, String> {
        //В ЭТОМ МЕТОДЕ ВЫПОЛНЯЮТСЯ ДЕЙСТВИЯ В ПАРАЛЛЕЛЬНОМ ПОТОКЕ
        @Override
        protected String doInBackground(String... urls) {
            try {
                //ЗАПРОС
                return getCodeNameClients();
            } catch (Exception e) {
                // Handle the exception
                //Log.e("AsyncTaskError", "Error occurred in doInBackground()", e);

                //Toast.makeText(getApplicationContext(), "ОШИБКАААА", Toast.LENGTH_SHORT).show();
            }
            return "";
        }

        //В ЭТОМ МЕТОДЕ ПРОИСХОДИТ ВЗАИМОДЕЙСТВИЕ С ЭЛЕМЕНТАМИ ИНТЕРФЕЙСА
        @Override
        protected void onPostExecute(String response) {

            //ПАРСИНГ JSON, ПРИШЕДШЕГО ИЗ ПРОГРАММЫ ИНФО-ПРЕДПРИЯТИЕ
            if (!response.equals("")) {
                arrayListCodeNameClients = ParsingJsonClass.parseClients(response);
            } else {
                Toast.makeText(getApplicationContext(), "ОШИБКА! НЕ УДАЛОСЬ ВЫПОЛНИТЬ ЗАПРОС!", Toast.LENGTH_SHORT).show();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                return;
            }
            //ОЧИСТКА УЖЕ ИМЕВШЕЙСЯ БАЗЫ ДАННЫХ
            myDbManager.clearTable();

            //ПРОВЕРКА В ВИДУ ПОВТОРЯЮЩЕГОСЯ ИМЕНИ КЛИЕНТА
            myDbManager.insertToDb(arrayListCodeNameClients.get(1).getName(), arrayListCodeNameClients.get(1).getCode());
            //СОХРАНЕНИЕ ПОЛУЧЕННОЙ ИНФОРМАЦИИ О КЛИЕНТАХ (ИМЯ, КОД) В ЛОКАЛЬНУЮ БД SQLITE
            for (ClientsCodeNameModel client: arrayListCodeNameClients) {
                myDbManager.insertToDb(client.getName(), client.getCode());
            }
            //ЗАПОЛНЕНИЕ LISTVIEW КЛИЕНТАМИ
            ClientAdapter clientAdapter = new ClientAdapter(getApplicationContext(), arrayListCodeNameClients);
            listClients.setAdapter(clientAdapter);
            Toast.makeText(getApplicationContext(), "УСПЕШНО!", Toast.LENGTH_SHORT).show();
            //ЗАКРЫТИЕ ОКОШКА С ОЖИДАНИЕМ ЗАПРОСА
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listClients = findViewById(R.id.listClients);
        buttonUpdateCodeNameClients = findViewById(R.id.button);

        //СОЗДАНИЕ ЭКЗЕМПЛЯРА КЛАССА MyDbManager, УПРАВЛЯЮЩЕГО ЛОКАЛЬНОЙ БАЗОЙ ДАННЫХ,
        //И ОТКРЫТИЕ САМОЙ БАЗЫ ВНУТРИ ПРИЛОЖЕНИЯ
        myDbManager = new MyDbManager(this);
        myDbManager.openDb();

        //ВЫВЕДЕТСЯ ПРИ ПЕРВОМ ЗАПУСКЕ ПРИЛОЖЕНИЯ
        //ПОЛЬЗОВАТЕЛЬ, НИ РАЗУ НЕ СДЕЛАВШИЙ ЗАПРОСА В ИНФО-ПРЕДПРИЯТИЕ, УВИДИТ В LISTVIEW ЭТО
        arrayListCodeNameClients = new ArrayList<>(Arrays.asList(
                new ClientsCodeNameModel("Нет клиентов", ""),
                new ClientsCodeNameModel("Нет клиентов", ""),
                new ClientsCodeNameModel("Нет клиентов", ""),
                new ClientsCodeNameModel("Нет клиентов", ""),
                new ClientsCodeNameModel("Нет клиентов", "")));
        ClientAdapter clientAdapter = new ClientAdapter(getApplicationContext(), arrayListCodeNameClients);
        listClients.setAdapter(clientAdapter);

        //ПОЛУЧЕНИЕ КЛИЕНТОВ ИЗ ЛОКАЛЬНОЙ БАЗЫ ДАННЫХ
        arrayListCodeNameClients = myDbManager.getFromDb();

        //В СЛУЧАЕ НАЛИЧИЯ КЛИЕНТОВ В ЛОКАЛЬНОЙ БАЗЕ - ЗАПОЛНЕНИЕ LISTVIEW МАССИВОМ С КЛИЕНТАМИ
        //С ПОМОЩЬЮ СОБСТВЕННОГО АДАПТЕРА
        if (!arrayListCodeNameClients.isEmpty()) {
            clientAdapter = new ClientAdapter(getApplicationContext(), arrayListCodeNameClients);
            listClients.setAdapter(clientAdapter);
        }

        //ОБРАБОТКА НАЖАТИЯ НА КНОПКУ, ДЕЛАЮЩУЮ ЗАПРОС, ТЕМ САМЫМ ОБНОВЛЯЮЩУЮ ДАННЫЕ
        buttonUpdateCodeNameClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ВЫЗОВ ОКОШКА ОЖИДАНИЯ
                progressDialog = ProgressDialog.show(MainActivity.this, "Выполнение действия", "Подождите...");
                //ВЫЗОВ ЗАПРОСА
                new CodeNameClientsQueryTask().execute("123");
            }
        });

        //ОБРАБОТКА НАЖАТИЯ НА ЛЮБОЙ ЭЛЕМЕНТ LISTVIEW
        //ПРИ НАЖАТИИ СОЗДАЕТСЯ НОВАЯ АКТИВНОСТЬ, В КОТОРОЙ МОЖНО УВИДЕТЬ БОЛЕЕ ПОДРОБНУЮ ИНФОРМАЦИЮ О КЛИЕНТЕ
        listClients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ClientInfoActivity.class);
                String clientName = ((ClientAdapter) adapterView.getAdapter()).getClientName(i);
                intent.putExtra("nameMen", clientName);
                String clientCode = ((ClientAdapter) adapterView.getAdapter()).getClientCode(i);
                intent.putExtra("codeMen", clientCode);
                startActivity(intent);
            }
        });
    }

    //ФУНКЦИЯ, СОЗДАЮЩАЯ HTTP ЗАПРОС В ПРОГРАММУ ИНФО-ПРЕДПРИЯТИЕ
    //ВОЗВРАЩАЕТ СТРОКУ В ФОРМАТЕ JSON СО СПИСКОМ (ИМЯ КЛИЕНТА, КОД КЛИЕНТА)
    public static String getCodeNameClients() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://89.151.143.135:8103/GetClients").newBuilder();
        urlBuilder.addQueryParameter("token", "E820C969-A60A-4E19-A086-2BF07AAD2088");
        String url = urlBuilder.build().toString();
        System.out.println(url);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .cacheControl(new CacheControl.Builder().maxStale(30, TimeUnit.DAYS).build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code" + response);
            } else {
                return response.body().string();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDbManager.closeDb();
    }
}