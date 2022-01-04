package com.example.nfc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListeEtudiants extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_etudiants);

        Intent intent = getIntent();
        ArrayList<Etudiant> list = (ArrayList<Etudiant>) intent.getSerializableExtra("list");

        ArrayList<String> score = new ArrayList<>();
        ListView listView = findViewById(R.id.listView);

        for(Etudiant etudiant : list)
            score.add("ID : " + etudiant.getId() + "\nPrénom : " + etudiant.getPrenom() + "\nNom : "
                    + etudiant.getNom() + "\nUID : " + etudiant.getUid() + "\nDébut : "
                    + etudiant.getHeureDebut() + "\nFin : " + etudiant.getHeureFin());

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_liste_etudiants,
                R.id.textView, score);
        listView.setAdapter(arrayAdapter);
    }
}
