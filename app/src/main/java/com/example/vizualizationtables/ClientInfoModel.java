package com.example.vizualizationtables;

import java.util.ArrayList;

//КЛАСС ДЛЯ СОХРАНЕНИЯ И УПРАВЛЕНИЯ ПОДРОБНОЙ ИНФОРМАЦИЕЙ О КОНКРЕТНОМ КЛИЕНТЕ
public class ClientInfoModel {
    private String type;
    private String inn;
    private String kpp;
    private String address;
    private String address_fact;
    private String director;
    private String email;
    private String fax;
    private ArrayList<ClientContactsModel> contacts;

    public ClientInfoModel(String type, String inn, String kpp, String address, String address_fact, String director, String email, String fax, ArrayList<ClientContactsModel> contacts) {
        this.type = type;
        this.inn = inn;
        this.kpp = kpp;
        this.address = address;
        this.address_fact = address_fact;
        this.director = director;
        this.email = email;
        this.fax = fax;
        this.contacts = contacts;
    }

    public ClientInfoModel() {

    }

    @Override
    public String toString() {
        return "ClientInfoModel{" +
                "type='" + type + '\'' +
                ", inn='" + inn + '\'' +
                ", kpp='" + kpp + '\'' +
                ", address='" + address + '\'' +
                ", address_fact='" + address_fact + '\'' +
                ", director='" + director + '\'' +
                ", email='" + email + '\'' +
                ", fax='" + fax + '\'' +
                ", contacts=" + contacts +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_fact() {
        return address_fact;
    }

    public void setAddress_fact(String address_fact) {
        this.address_fact = address_fact;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public ArrayList<ClientContactsModel> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<ClientContactsModel> contacts) {
        this.contacts = contacts;
    }
}
