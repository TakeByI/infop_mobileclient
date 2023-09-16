package com.example.vizualizationtables;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ClientInfoActivity extends AppCompatActivity {
    private MyDbManager myDbManager;
    ArrayList<ClientInfoModel> arrayListClientInfo = null;
    TextView textViewEmail;
    TextView textViewType;
    TextView textViewInn;
    TextView textViewKpp;
    TextView textViewAddress;
    TextView textViewAddressFact;
    TextView textViewDirector;
    TextView textViewFax;
    ListView listViewContacts;
    TextView textViewNameClient;
    private ProgressDialog progressDialog;

    //КЛАСС ДЛЯ СОВЕРШЕНИЯ ДЕЙСТВИЙ В ПРИЛОЖЕНИИ В ПАРАЛЛЕЛЬНОМ ПОТОКЕ
    //В ДАННОМ СЛУЧАЕ ДЛЯ ВЫПОЛНЕНИЯ HTTP ЗАПРОСА
    class ClientInfoQueryTask extends AsyncTask<String, Void, String> {
        //В ЭТОМ МЕТОДЕ ВЫПОЛНЯЮТСЯ ДЕЙСТВИЯ В ПАРАЛЛЕЛЬНОМ ПОТОКЕ
        @Override
        protected String doInBackground(String... urls) {
            try {
                return getInfoAboutClient(urls[0]);
            } catch (Exception e) {
                // Handle the exception
                Log.e("AsyncTaskError", "Error occurred in doInBackground()", e);
            }
            return "";
        }

        //В ЭТОМ МЕТОДЕ ПРОИСХОДИТ ВЗАИМОДЕЙСТВИЕ С ЭЛЕМЕНТАМИ ИНТЕРФЕЙСА
        @Override
        protected void onPostExecute(String response) {
            if (!response.equals("")) {
                arrayListClientInfo = ParsingJsonClass.parseClientInfo(response);
            } else {
                Toast.makeText(getApplicationContext(), "ОШИБКА! НЕ УДАЛОСЬ ВЫПОЛНИТЬ ЗАПРОС!", Toast.LENGTH_SHORT).show();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                return;
            }
            //System.out.println(arrayListClientInfo);

            //ВЫВОД ДАННЫХ О КЛИЕНТЕ НА ЭКРАН
            textViewType.append(arrayListClientInfo.get(0).getType());
            textViewInn.append(arrayListClientInfo.get(0).getInn());
            textViewKpp.append(arrayListClientInfo.get(0).getKpp());
            textViewAddress.append(arrayListClientInfo.get(0).getAddress());
            textViewAddressFact.append(arrayListClientInfo.get(0).getAddress_fact());
            textViewDirector.append(arrayListClientInfo.get(0).getDirector());
            textViewEmail.append(arrayListClientInfo.get(0).getEmail());
            textViewFax.append(arrayListClientInfo.get(0).getFax());
            if (!arrayListClientInfo.get(0).getContacts().isEmpty()) {
                ClientInfoAdapter clientAdapter = new ClientInfoAdapter(getApplicationContext(), arrayListClientInfo.get(0).getContacts());
                listViewContacts.setAdapter(clientAdapter);
            }
            //ЗАКРЫТИЕ ОКОШКА С ОЖИДАНИЕМ ЗАПРОСА
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_info);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewAddressFact = findViewById(R.id.textViewAddressFact);
        textViewDirector = findViewById(R.id.textViewDirector);
        textViewFax = findViewById(R.id.textViewFax);
        textViewInn = findViewById(R.id.textViewInn);
        textViewKpp = findViewById(R.id.textViewKpp);
        textViewType = findViewById(R.id.textViewType);
        textViewNameClient = findViewById(R.id.textViewNameClient);
        listViewContacts = findViewById(R.id.listViewContacts);

        //ВЫЗОВ ОКОШКА ОЖИДАНИЯ
        progressDialog = ProgressDialog.show(this, "Выполнение действия", "Подождите...");

        //ЗАПОЛНЕНИЕ LISTVIEW С КОНТАКТАМИ КЛИЕНТА
        //ПРИ ОТСУТСТВИИ КОНТАКТОВ БУДЕТ ВИДЕН ОДИН ЭЛЕМЕНТ LISTVIEW С НАДПИСЬЮ: КОНТАКТЫ ОТСУТСТВУЮТ
        ClientInfoAdapter clientAdapter = new ClientInfoAdapter(getApplicationContext(),
                new ArrayList<ClientContactsModel>(Arrays.asList(
                        new ClientContactsModel("КОНТАКТЫ ОТСУТСТВУЮТ", "КОНТАКТЫ ОТСУТСТВУЮТ",
                                "КОНТАКТЫ ОТСУТСТВУЮТ", "КОНТАКТЫ ОТСУТСТВУЮТ"))));
        listViewContacts.setAdapter(clientAdapter);

        //ПОЛУЧЕНИЕ ДАННЫХ О КЛИЕНТЕ ИЗ MAINACTIVITY
        Intent intent = getIntent();
        String name = intent.getStringExtra("nameMen");
        String code = intent.getStringExtra("codeMen");

        //ЗАГОЛОВОК С НАЗВАНИЕМ КЛИЕНТА
        textViewNameClient.setText(name);

        //ВЫПОЛНЕНИЕ ЗАПРОСА ПРИ НАЛИЧИИ НЕПУСТОГО КОДА
        if (!code.equals("")) {
            new ClientInfoQueryTask().execute(code);
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(this, "НЕВОЗМОЖНО НАЙТИ КЛИЕНТА", Toast.LENGTH_SHORT).show();
        }


    }

    //ФУНКЦИЯ, СОЗДАЮЩАЯ HTTP ЗАПРОС В ПРОГРАММУ ИНФО-ПРЕДПРИЯТИЕ
    //ВОЗВРАЩАЕТ СТРОКУ В ФОРМАТЕ JSON СО ПОДРОБНОЙ ИНФОРМАЦИЕЙ О КОНКРЕТНОМ КЛИЕНТЕ
    public static String getInfoAboutClient(String codeClient) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://89.151.143.135:8103/GetClientInfo").newBuilder();
        urlBuilder.addQueryParameter("token", "");
        urlBuilder.addQueryParameter("kod", codeClient);
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
                String responseData = response.body().string();
                System.out.println(responseData);
                return responseData;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}