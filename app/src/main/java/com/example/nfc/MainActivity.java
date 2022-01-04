package com.example.nfc;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final List<Etudiant> etudiantList = new ArrayList<>();
    private final List<Examen> examenList = new ArrayList<>();

    private MyDatabaseHelper db;

    private List<Etudiant> list;
    private List<Examen> list2;

    private ActivityResultLauncher<Intent> scan;
    private ActivityResultLauncher<Intent> formulaireEtudiant;
    private ActivityResultLauncher<Intent> choixExamen;
    private ActivityResultLauncher<Intent> formulaireExamen;

    private String choix;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new MyDatabaseHelper(this);

        list =  db.getAllEtudiants();
        this.etudiantList.addAll(list);

        list2 = db.getAllExamens();
        this.examenList.addAll(list2);

        choix = "";
        button = findViewById(R.id.choixExamen);

        scan = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        assert intent != null;

                        String uid = intent.getStringExtra("uid");
                        String heure = intent.getStringExtra("heure");
                        String prenom = intent.getStringExtra("prenom");
                        String nom = intent.getStringExtra("nom");

                        boolean condition = false;
                        for(Etudiant etudiant : etudiantList) {
                            if(etudiant.getUid().equals(uid)) {
                                if(etudiant.getHeureDebut().equals(""))
                                    db.updateHeureEtudiant(etudiant, heure, prenom, nom, true);
                                else if(etudiant.getHeureFin().equals(""))
                                    db.updateHeureEtudiant(etudiant, heure, prenom, nom, false);
                                condition = true;
                            }
                        }
                        if(!condition) {
                            Etudiant nouveau = new Etudiant(prenom, nom, uid, heure, "");
                            db.addEtudiant(nouveau);

                            list = db.getAllEtudiants();
                            etudiantList.addAll(list);
                        }

                        reset();
                    }
                });

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

        choixExamen = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        assert intent != null;

                        choix = intent.getStringExtra("choixExamen");
                        button.setText("Choix de l'examen " + " (" + choix + ")");
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
        ArrayList<Etudiant> list = new ArrayList<>(etudiantList);

        Intent intent = new Intent(this, Scan.class);
        intent.putExtra("list", list);
        scan.launch(intent);
    }

    public void formulaireEtudiant(View v) {
        Intent intent = new Intent(this, FormulaireEtudiant.class);
        formulaireEtudiant.launch(intent);
    }

    public void listeEtudiants(View v) {
        ArrayList<Etudiant> list = new ArrayList<>(etudiantList);

        Intent intent = new Intent(this, ListeEtudiants.class);
        intent.putExtra("list", list);
        startActivity(intent);
    }

    public void choixExamen(View v) {
        ArrayList<Examen> list = new ArrayList<>(examenList);

        Intent intent = new Intent(this, ChoixExamen.class);
        intent.putExtra("list", list);
        choixExamen.launch(intent);
    }

    public void formulaireExamen(View v) {
        Intent intent = new Intent(this, FormulaireExamen.class);
        formulaireExamen.launch(intent);
    }

    public void listeExamens(View v) {
        ArrayList<Examen> list = new ArrayList<>(examenList);

        Intent intent = new Intent(this, ListeExamens.class);
        intent.putExtra("list", list);
        startActivity(intent);
    }

    public void resetHeures(View v) {
        for(Etudiant etudiant : etudiantList)
            db.deleteHeuresEtudiant(etudiant);
        reset();
    }

    public void resetDB(View v) {
        db.deleteAllExamens();
        db.deleteAllEtudiants();
        reset();
    }

    public void reset() {
        etudiantList.clear();
        examenList.clear();

        list = db.getAllEtudiants();
        etudiantList.addAll(list);

        list2 = db.getAllExamens();
        examenList.addAll(list2);
    }

    public void quitter(View v) {
        this.finish();
    }
}