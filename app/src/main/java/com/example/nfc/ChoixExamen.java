package com.example.nfc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChoixExamen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerChoix;

    private ArrayList<String> choix;

    private String choixExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_examen);

        spinnerChoix = findViewById(R.id.spinner);
        spinnerChoix.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        ArrayList<Examen> list = (ArrayList<Examen>) intent.getSerializableExtra("list");

        choix = new ArrayList<>();

        for(Examen examen : list)
            choix.add(examen.getMatiere());

        // Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aaHunter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choix);
        aaHunter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Setting the ArrayAdapter data on the Spinner
        spinnerChoix.setAdapter(aaHunter);
        spinnerChoix.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinner)
            choixExamen = choix.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        choixExamen = choix.get(0);
    }

    public void validate(View v) {
        Intent result = new Intent();
        result.putExtra("choixExamen", choixExamen);
        setResult(RESULT_OK, result);
        this.finish();
    }
}
