package com.example.nfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.euicc.EuiccInfo;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final List<Etudiant> etudiantList = new ArrayList<>();
    private MyDatabaseHelper db;
    private List<Etudiant> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new MyDatabaseHelper(this);
    }

    public void scan(View v) {
        Intent scan = new Intent(this, Scan.class);
        startActivity(scan);
    }

    public void formulaireEtudiant(View v) {
        Intent formulaireEtudiant = new Intent(this, FormulaireEtudiant.class);
        startActivityForResult(formulaireEtudiant, 1);
    }

    public void onActivityResult(int code, int status, Intent intent) {
        super.onActivityResult(code, status, intent);
        // Play activity
        if(code == 1) {
            if(status == RESULT_OK) {
                String prenom = intent.getStringExtra("prenom");
                String nom = intent.getStringExtra("nom");
                String uid = intent.getStringExtra("uid");

                Etudiant nouveau = new Etudiant(prenom, nom, uid);
                db.addEtudiant(nouveau);

                list = db.getAllEtudiants();
                this.etudiantList.addAll(list);

                for(Etudiant elem: etudiantList) {
                    System.out.println ("etudiant : " + elem.getPrenom() + " " + elem.getNom() + " " + elem.getUid());
                }
            }
        }
    }

}