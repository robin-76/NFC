package com.example.nfc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class FormulaireExamen extends AppCompatActivity {
    private EditText date;
    private EditText matiere;
    private EditText professeur;
    private EditText heureDebut;
    private EditText heureFin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_examen);

        date = findViewById(R.id.date);
        matiere = findViewById(R.id.matiere);
        professeur = findViewById(R.id.professeur);
        heureDebut = findViewById(R.id.heureDebut);
        heureFin = findViewById(R.id.heureFin);
    }

    public void validate(View v) {
        Intent result = new Intent();
        result.putExtra("date", date.getText().toString());
        result.putExtra("matiere", matiere.getText().toString());
        result.putExtra("professeur", professeur.getText().toString());
        result.putExtra("heureDebut", heureDebut.getText().toString());
        result.putExtra("heureFin", heureFin.getText().toString());
        setResult(RESULT_OK, result);
        this.finish();
    }
}
