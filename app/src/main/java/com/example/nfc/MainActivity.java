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
    private MyDatabaseHelper db;
    private List<Etudiant> list;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new MyDatabaseHelper(this);

        someActivityResultLauncher = registerForActivityResult(
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
                });
    }

    public void scan(View v) {
        Intent scan = new Intent(this, Scan.class);
        startActivity(scan);
    }

    public void formulaireEtudiant(View v) {
        Intent intent = new Intent(this, FormulaireEtudiant.class);
        someActivityResultLauncher.launch(intent);
    }

    public void quitter(View v) {
        this.finish();
    }
}