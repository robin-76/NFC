package com.example.nfc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListeExamens extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_examens);

        Intent intent = getIntent();
        ArrayList<Examen> list = (ArrayList<Examen>) intent.getSerializableExtra("list");

        ArrayList<String> score = new ArrayList<>();
        ListView listView = findViewById(R.id.listView);

        TextView nombre = findViewById(R.id.nombre);
        nombre.setText(" Nombre total d'examens : " + list.size());

        for(Examen examen : list)
            score.add("Matière : " + examen.getMatiere() + "\nProfesseur : " + examen.getProfesseur()
                    + "\nDate : " + examen.getDate()
                    + "\nDébut : " + examen.getHeureDebut() + "h\nFin : " + examen.getHeureFin() + "h");

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_liste_examens,
                R.id.textView, score);
        listView.setAdapter(arrayAdapter);
    }
}

