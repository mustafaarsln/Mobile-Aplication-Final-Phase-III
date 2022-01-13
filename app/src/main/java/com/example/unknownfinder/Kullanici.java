// This java file and kullanici_ogesi.xml written by Kıvanç Erbudak.

package com.example.unknownfinder;

public class Kullanici {

    private String k_id;
    private String k_name;
    private String k_phone;
    private String k_surname;
    private String k_email;

    public Kullanici() {
    }

    public Kullanici(String k_id, String k_name, String k_phone, String k_surname, String k_email) {
        this.k_id = k_id;
        this.k_name = k_name;
        this.k_phone = k_phone;
        this.k_surname = k_surname;
        this.k_email = k_email;
    }

    public String getK_id() {
        return k_id;
    }

    public void setK_id(String k_id) {
        this.k_id = k_id;
    }

    public String getK_name() {
        return k_name;
    }

    public void setK_name(String k_name) {
        this.k_name = k_name;
    }

    public String getK_phone() {
        return k_phone;
    }

    public void setK_phone(String k_phone) {
        this.k_phone = k_phone;
    }

    public String getK_surname() {
        return k_surname;
    }

    public void setK_surname(String k_surname) {
        this.k_surname = k_surname;
    }

    public String getK_email() {
        return k_email;
    }

    public void setK_email(String k_email) {
        this.k_email = k_email;
    }
}
