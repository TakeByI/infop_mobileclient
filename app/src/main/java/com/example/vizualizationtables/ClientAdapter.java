package com.example.vizualizationtables;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

//КЛАСС АДАПТЕР ДЛЯ ВЫВОДА КЛИЕНТОВ СПИСКОМ
public class ClientAdapter extends ArrayAdapter<ClientsCodeNameModel> {

    private Context context;
    private ArrayList<ClientsCodeNameModel> arrayListCodeNameClients;

    public ClientAdapter(@NonNull Context context, @NonNull ArrayList<ClientsCodeNameModel> objects) {
        super(context, R.layout.item_client, objects);
        this.context = context;
        this.arrayListCodeNameClients = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.item_client, parent, false);
        TextView clientName  = (TextView) view.findViewById(R.id.client_name);
        ClientsCodeNameModel client = this.arrayListCodeNameClients.get(position);
        clientName.setText(client.getName());
        return view;
    }

    public String getClientName(int position) {
        ClientsCodeNameModel client = arrayListCodeNameClients.get(position);
        return client.getName();
    }

    public String getClientCode(int position) {
        ClientsCodeNameModel client = arrayListCodeNameClients.get(position);
        return client.getCode();
    }
}
