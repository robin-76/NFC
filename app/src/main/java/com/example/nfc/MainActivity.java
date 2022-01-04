package com.example.nfc;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final List<Etudiant> etudiantList = new ArrayList<>();
    private final List<Examen> examenList = new ArrayList<>();
    private MyDatabaseHelper db;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new MyDatabaseHelper(this);

        db.createDefaultEtudiantsIfNeed();
        List<Etudiant> list=  db.getAllEtudiants();
        this.etudiantList.addAll(list);

        for(Etudiant etd: etudiantList) {
            System.out.println ("etudiant : " + etd.getPrenom() + etd.getNom() + etd.getUid() +etd.getHeureDebut()+etd.getHeureFin());
        }

        db.createExamen();
        List<Examen> list2=  db.getAllExamens();
        this.examenList.addAll(list2);

        for(Examen exam: examenList) {
            System.out.println ("examen : " + exam.getDate() + exam.getMatiere() + exam.getProfesseur() +exam.getHeureDebut()+exam.getHeureFin());
        }

       /* someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        assert intent != null;

                        String prenom = intent.getStringExtra("prenom");
                        String nom = intent.getStringExtra("nom");
                        String uid = intent.getStringExtra("uid");

                        Etudiant nouveau = new Etudiant(prenom, nom, uid);
                        db.addEtudiant(nouveau);

                        list = db.getAllEtudiants();
                        etudiantList.addAll(list);

                        for(Etudiant elem: etudiantList) {
                            System.out.println ("etudiant : " + elem.getPrenom() + " " + elem.getNom() + " " + elem.getUid());
                        }
                    }
                }); */
    }

    public void scan(View v) {
        Intent scan = new Intent(this, Scan.class);
        startActivity(scan);
    }

    public void formulaireEtudiant(View v) {
        Intent intent = new Intent(this, FormulaireEtudiant.class);
        someActivityResultLauncher.launch(intent);
    }

    public void reset(View v) {
        db.deleteAllEtudiants();
    }

    public void quitter(View v) {
        this.finish();
    }
}