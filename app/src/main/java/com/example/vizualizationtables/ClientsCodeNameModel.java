package com.example.vizualizationtables;

//КЛАСС ДЛЯ СОХРАНЕНИЯ И УПРАВЛЕНИЯ ИНФОРМАЦИЕЙ О ВСЕХ КЛИЕНТАХ (ИМЯ, КОД)
public class ClientsCodeNameModel {
    private String name;
    private String code;

    public ClientsCodeNameModel(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Clients {" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
