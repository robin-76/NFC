package com.example.nfc;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormulaireExamen extends AppCompatActivity {
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;
    private EditText date;
    private EditText matiere;
    private EditText professeur;
    private EditText heureDebut;
    private EditText heureFin;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    private int lastSelectedHour = -1;
    private int lastSelectedMinute = -1;
    boolean debut = false;
    boolean fin = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_examen);

        date = findViewById(R.id.date);
        matiere = findViewById(R.id.matiere);
        professeur = findViewById(R.id.professeur);
        heureDebut = findViewById(R.id.heureDebut);
        heureFin = findViewById(R.id.heureFin);

        this.date.setOnClickListener(v -> buttonSelectDate());
        this.heureDebut.setOnClickListener(v -> {
            buttonSelectTime();
            fin = false;
            debut = true;
        });

        this.heureFin.setOnClickListener(v -> {
            buttonSelectTime();
            debut = false;
            fin = true;
        });

        final Calendar c = Calendar.getInstance();
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
    }

    private void buttonSelectDate() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

            lastSelectedYear = year;
            lastSelectedMonth = monthOfYear;
            lastSelectedDayOfMonth = dayOfMonth;
        };
        datePickerDialog = new DatePickerDialog(this,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
        datePickerDialog.show();
    }

    private void buttonSelectTime()  {
        if(this.lastSelectedHour == -1)  {
            final Calendar c = Calendar.getInstance();
            this.lastSelectedHour = c.get(Calendar.HOUR_OF_DAY);
            this.lastSelectedMinute = c.get(Calendar.MINUTE);
        }

        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            if(debut)
                heureDebut.setText(hourOfDay + ":" + minute );
            else
                heureFin.setText(hourOfDay + ":" + minute );

            lastSelectedHour = hourOfDay;
            lastSelectedMinute = minute;
        };
        timePickerDialog = new TimePickerDialog(this,
                    timeSetListener, lastSelectedHour, lastSelectedMinute, true);
        timePickerDialog.show();
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
