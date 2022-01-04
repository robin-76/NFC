package com.example.nfc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class FormulaireEtudiant extends AppCompatActivity {
    private EditText prenom;
    private EditText nom;
    private EditText uid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_etudiant);

        prenom = findViewById(R.id.prenom);
        nom = findViewById(R.id.nom);
        uid = findViewById(R.id.uid);
    }

    public void validate(View v) {
        Intent result = new Intent();
        result.putExtra("prenom", prenom.getText().toString());
        result.putExtra("nom", nom.getText().toString());
        result.putExtra("uid", uid.getText().toString());
        setResult(RESULT_OK, result);
        this.finish();
    }
}
