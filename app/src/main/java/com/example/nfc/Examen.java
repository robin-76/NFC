package com.example.nfc;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Examen implements Serializable {

    private int id;
    private String date;
    private String matiere;
    private String professeur;
    private String heureDebut;
    private String heureFin;

    public Examen() {}

    public Examen(String date, String matiere, String professeur, String heureDebut, String heureFin) {
        this.date = date;
        this.matiere = matiere;
        this.professeur = professeur;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getProfesseur() {
        return professeur;
    }

    public void setProfesseur(String professeur) {
        this.professeur = professeur;
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
        return this.matiere;
    }

}
