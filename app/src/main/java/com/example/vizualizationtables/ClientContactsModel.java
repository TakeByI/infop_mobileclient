package com.example.vizualizationtables;

//КЛАСС ДЛЯ СОХРАНЕНИЯ И УПРАВЛЕНИЯ ИНФОРМАЦИЕЙ О КОНТАКТАХ ОТДЕЛЬНОГО КЛИЕНТА
public class ClientContactsModel {
    private String name;
    private String position;
    private String tel_work;
    private String tel_home;

    @Override
    public String toString() {
        return "ClientContactsModel{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", tel_work='" + tel_work + '\'' +
                ", tel_home='" + tel_home + '\'' +
                '}';
    }

    public ClientContactsModel() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTel_work() {
        return tel_work;
    }

    public void setTel_work(String tel_work) {
        this.tel_work = tel_work;
    }

    public String getTel_home() {
        return tel_home;
    }

    public void setTel_home(String tel_home) {
        this.tel_home = tel_home;
    }

    public ClientContactsModel(String name, String position, String tel_work, String tel_home) {
        this.name = name;
        this.position = position;
        this.tel_work = tel_work;
        this.tel_home = tel_home;
    }
}
