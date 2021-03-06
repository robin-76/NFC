package com.example.nfc;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Etudiant implements Serializable {

    private int id;
    private String prenom;
    private String nom;
    private String uid;
    private String heureDebut;
    private String heureFin;

    public Etudiant() {}

    public Etudiant(String prenom, String nom, String uid, String heureDebut, String heureFin) {
        this.prenom = prenom;
        this.nom = nom;
        this.uid = uid;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
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

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    @NonNull
    @Override
    public String toString()  {
        return this.nom;
    }

}