package com.example.vizualizationtables;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

//КЛАСС, ПРЕДНАЗНАЧЕННЫЙ ДЛЯ ПАРСИНГА JSON СТРОК, ПРИХОДЯЩИХ ИЗ HTTP ЗАПРОСОВ К ПРОГРАММЕ ИНФО-ПРЕДПРИЯТИЕ
public class ParsingJsonClass {

    //ФУНКЦИЯ ПАРСИТ ПРИХОДЯЩИЕ ИМЕНА И КОДЫ КЛИЕНТОВ И ВЫДАЕТ ТОЛЬКО ARRAYLIST ИМЕН КЛИЕНТОВ
    public static ArrayList<String> parseNames(String jsonString) {
        ArrayList<String> arraysOfNames = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
            JSONArray namesJsonArray = (JSONArray) jsonObject.get("clients");
            for (int i = 0; i < namesJsonArray.size(); i++) {
                JSONObject nameJsonObject = (JSONObject) namesJsonArray.get(i);
                //System.out.println(nameJsonObject);

                System.out.println((String) nameJsonObject.get("name"));
                arraysOfNames.add((String) nameJsonObject.get("name"));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        return arraysOfNames;

    }

    //ФУНКЦИЯ ПАРСИТ ПРИХОДЯЩИЕ ИМЕНА И КОДЫ КЛИЕНТОВ И ВЫДАЕТ ARRAYLIST ИМЕН И КОДОВ КЛИЕНТОВ
    public static ArrayList<ClientsCodeNameModel> parseClients(String jsonString) {
        ArrayList<ClientsCodeNameModel> arraysOfClients = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
            JSONArray codeNamesJsonArray = (JSONArray) jsonObject.get("clients");
            for (int i = 0; i < codeNamesJsonArray.size(); i++) {
                JSONObject nameJsonObject = (JSONObject) codeNamesJsonArray.get(i);
                //System.out.println(nameJsonObject);

                System.out.println((String) nameJsonObject.get("kod"));
                System.out.println((String) nameJsonObject.get("name"));
                arraysOfClients.add(new ClientsCodeNameModel((String) nameJsonObject.get("name"), (String) nameJsonObject.get("kod")));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return arraysOfClients;
    }

    //ФУНКЦИЯ ПАРСИТ ПРИХОДЯЩУЮ ПОДРОБНУЮ ИНФОРМАЦИЮ О КЛИЕНТЕ
    public static ArrayList<ClientInfoModel> parseClientInfo(String jsonString) {
        ArrayList<ClientInfoModel> arraysOfClients = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
            JSONArray codeNamesJsonArray = (JSONArray) jsonObject.get("clients");

            /*JSONArray codeNamesJsonArray333 = (JSONArray) jsonObject.get("contancts");
            for (int i = 0; i < codeNamesJsonArray333.size(); i++) {
                JSONObject nameJsonObject = (JSONObject) codeNamesJsonArray333.get(i);
                System.out.println((String) nameJsonObject.get("name"));
            }*/

            for (int i = 0; i < codeNamesJsonArray.size(); i++) {
                JSONObject nameJsonObject = (JSONObject) codeNamesJsonArray.get(i);
                //System.out.println(nameJsonObject);

                //System.out.println((String) nameJsonObject.get("type"));
                //System.out.println((String) nameJsonObject.get("contancts"));
                JSONArray contactsJsonArray = (JSONArray) nameJsonObject.get("contancts");
                ArrayList<ClientContactsModel> arr = new ArrayList<>();
                for (int j = 0; j < contactsJsonArray.size(); j++) {
                    JSONObject nameJsonObject1 = (JSONObject) contactsJsonArray.get(j);
                    System.out.println((String) nameJsonObject1.get("name"));
                    arr.add(new ClientContactsModel((String) nameJsonObject1.get("name"),
                            (String) nameJsonObject1.get("position"),
                            (String) nameJsonObject1.get("tel_work"),
                            (String) nameJsonObject1.get("tel_home")));
                    System.out.println(arr);
                }

                arraysOfClients.add(new ClientInfoModel((String) nameJsonObject.get("type"),
                        (String) nameJsonObject.get("inn"),
                        (String) nameJsonObject.get("kpp"),
                        (String) nameJsonObject.get("address"),
                        (String) nameJsonObject.get("address_fact"),
                        (String) nameJsonObject.get("director"),
                        (String) nameJsonObject.get("email"),
                        (String) nameJsonObject.get("fax"),
                        arr
                        ));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return arraysOfClients;
    }
}
