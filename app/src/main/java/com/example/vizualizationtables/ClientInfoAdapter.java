package com.example.vizualizationtables;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

//КЛАСС АДАПТЕР ДЛЯ ВЫВОДА КОНТАКТОВ КЛИЕНТА СПИСКОМ
public class ClientInfoAdapter extends ArrayAdapter<ClientContactsModel> {

    private Context context;
    private ArrayList<ClientContactsModel> arrayListClientInfo;

    public ClientInfoAdapter(@NonNull Context context, @NonNull ArrayList<ClientContactsModel> objects) {
        super(context, R.layout.item_client_info, objects);
        this.context = context;
        this.arrayListClientInfo = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_client_info, parent, false);
        TextView contactName  = (TextView) view.findViewById(R.id.contact_name);
        TextView contactPosition  = (TextView) view.findViewById(R.id.contact_position);
        TextView telWork  = (TextView) view.findViewById(R.id.tel_work);
        TextView telHome  = (TextView) view.findViewById(R.id.tel_home);
        ClientContactsModel client = this.arrayListClientInfo.get(position);

        contactName.setText("Имя отсутствует");
        contactPosition.setText("Должность отсутствует");
        telWork.setText("Рабочий телефон отсутствует");
        telHome.setText("Домашний телефон отсутствует");

        if (!client.getName().equals("")) contactName.setText(client.getName());
        if (!client.getPosition().equals("")) contactPosition.setText(client.getPosition());
        if (!client.getTel_work().equals("")) telWork.setText(client.getTel_work());
        if (!client.getTel_home().equals("")) telHome.setText(client.getTel_home());

        return view;
    }
}
