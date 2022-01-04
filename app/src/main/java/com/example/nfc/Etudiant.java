package com.example.nfc;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Etudiant implements Serializable {

    private int id;
    private String prenom;
    private String nom;
    private String uid;

    public Etudiant() {}

    public Etudiant(String prenom, String nom, String uid) {
        this.prenom = prenom;
        this.nom = nom;
        this.uid = uid;
    }

    public Etudiant(int id, String prenom, String nom, String uid) {
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @NonNull
    @Override
    public String toString()  {
        return this.nom;
    }

}
