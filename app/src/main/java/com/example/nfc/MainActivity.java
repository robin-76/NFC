package com.example.nfc;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final List<Etudiant> etudiantList = new ArrayList<>();
    private final List<Examen> examenList = new ArrayList<>();

    private List<Etudiant> list;
    private List<Examen> list2;

    private MyDatabaseHelper db;
    private ActivityResultLauncher<Intent> formulaireEtudiant;
    private ActivityResultLauncher<Intent> formulaireExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new MyDatabaseHelper(this);

        db.createDefaultEtudiantsIfNeed();
        list =  db.getAllEtudiants();
        this.etudiantList.addAll(list);

        db.createExamen();
        list2 = db.getAllExamens();
        this.examenList.addAll(list2);

        formulaireEtudiant = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        assert intent != null;

                        String prenom = intent.getStringExtra("prenom");
                        String nom = intent.getStringExtra("nom");
                        String uid = intent.getStringExtra("uid");

                        Etudiant nouveau = new Etudiant(prenom, nom, uid, "", "");
                        db.addEtudiant(nouveau);

                        list = db.getAllEtudiants();
                        etudiantList.addAll(list);
                    }
                });

        formulaireExamen = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        assert intent != null;

                        String date = intent.getStringExtra("date");
                        String matiere = intent.getStringExtra("matiere");
                        String professeur = intent.getStringExtra("professeur");
                        String heureDebut = intent.getStringExtra("heureDebut");
                        String heureFin = intent.getStringExtra("heureFin");

                        Examen nouveau = new Examen(date, matiere, professeur, heureDebut, heureFin);
                        db.addExamen(nouveau);

                        list2 = db.getAllExamens();
                        examenList.addAll(list2);
                    }
                });
    }

    public void scan(View v) {
        Intent scan = new Intent(this, Scan.class);
        startActivity(scan);
    }

    public void formulaireEtudiant(View v) {
        Intent intent = new Intent(this, FormulaireEtudiant.class);
        formulaireEtudiant.launch(intent);
    }

    public void listeEtudiants(View v) {
        ArrayList<Etudiant> list = new ArrayList<>(etudiantList);

        Intent listeEtudiants = new Intent(this, ListeEtudiants.class);
        listeEtudiants.putExtra("list", list);
        startActivity(listeEtudiants);
    }

    public void formulaireExamen(View v) {
        Intent intent = new Intent(this, FormulaireExamen.class);
        formulaireExamen.launch(intent);
    }

    public void reset(View v) {
        db.deleteAllExamens();
        db.deleteAllEtudiants();
    }

    public void quitter(View v) {
        this.finish();
    }
}