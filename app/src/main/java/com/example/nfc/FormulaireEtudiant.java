package com.example.nfc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FormulaireEtudiant extends AppCompatActivity {
    private EditText prenom;
    private EditText nom;
    private EditText uid;
    private FileChooserFragment fragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_etudiant);

        prenom = findViewById(R.id.prenom);
        nom = findViewById(R.id.nom);
        uid = findViewById(R.id.uid);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        this.fragment = (FileChooserFragment) fragmentManager.findFragmentById(R.id.fragment_fileChooser);
    }

    public void importCSV(View v) throws IOException {
        String path = this.fragment.getPath();
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        InputStream is = new FileInputStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        reader.readLine();

        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            String[] info = line.split(";");
            Etudiant nouveau = new Etudiant(info[0], info[1], info[2], "", "");
            MyDatabaseHelper db = new MyDatabaseHelper(this);
            db.addEtudiant(nouveau);

            stringBuilder.append(line).append("\n");
        }
        is.close();
        setResult(RESULT_OK);
        this.finish();
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
