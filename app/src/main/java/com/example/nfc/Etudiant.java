package com.example.nfc;

import java.io.Serializable;

public class Etudiant implements Serializable {

    private int id;
    private String prenom;
    private String nom;
    private int uid;

    public Etudiant() {}

    public Etudiant(String prenom, String nom, int uid) {
        this.prenom = prenom;
        this.nom = nom;
        this.uid = uid;
    }

    public Etudiant(int id, String prenom, String nom, int uid) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString()  {
        return this.nom;
    }

}
