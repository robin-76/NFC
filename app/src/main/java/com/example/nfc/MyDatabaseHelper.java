package com.example.nfc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "Manager";

    private static final String TABLE_ETUDIANT = "Etudiant";
    private static final String COLUMN_ETUDIANT_ID ="Etudiant_Id";
    private static final String COLUMN_ETUDIANT_PRENOM ="Etudiant_Prenom";
    private static final String COLUMN_ETUDIANT_NOM = "Etudiant_Nom";
    private static final String COLUMN_ETUDIANT_UID = "Etudiant_Uid";
    private static final String COLUMN_ETUDIANT_HEUREDEBUT = "Etudiant_HeureDebut";
    private static final String COLUMN_ETUDIANT_HEUREFIN = "Etudiant_HeureFin";

    private static final String TABLE_EXAMEN = "Examen";
    private static final String COLUMN_EXAMEN_ID ="Examen_Id";
    private static final String COLUMN_EXAMEN_DATE ="Examen_Date";
    private static final String COLUMN_EXAMEN_MATIERE = "Examen_Matiere";
    private static final String COLUMN_EXAMEN_PROFESSEUR = "Examen_Professeur";
    private static final String COLUMN_EXAMEN_HEUREDEBUT = "Examen_HeureDebut";
    private static final String COLUMN_EXAMEN_HEUREFIN = "Examen_HeureFin";

    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");

        String script = "CREATE TABLE " + TABLE_ETUDIANT + "("
                + COLUMN_ETUDIANT_ID + " INTEGER PRIMARY KEY," + COLUMN_ETUDIANT_PRENOM + " TEXT,"
                + COLUMN_ETUDIANT_NOM + " TEXT," + COLUMN_ETUDIANT_UID + " TEXT,"
                + COLUMN_ETUDIANT_HEUREDEBUT + " TEXT," + COLUMN_ETUDIANT_HEUREFIN + " TEXT" + ")";

        String script2 = "CREATE TABLE " + TABLE_EXAMEN + "("
                + COLUMN_EXAMEN_ID + " INTEGER PRIMARY KEY," + COLUMN_EXAMEN_DATE + " TEXT,"
                + COLUMN_EXAMEN_MATIERE + " TEXT," + COLUMN_EXAMEN_PROFESSEUR + " TEXT,"
                + COLUMN_EXAMEN_HEUREDEBUT + " TEXT," + COLUMN_EXAMEN_HEUREFIN + " TEXT" + ")";

        db.execSQL(script);
        db.execSQL(script2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ETUDIANT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMEN);
        onCreate(db);
    }

    public void addEtudiant(Etudiant etudiant) {
        Log.i(TAG, "MyDatabaseHelper.addEtudiant ... " + etudiant.getNom());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ETUDIANT_PRENOM, etudiant.getPrenom());
        values.put(COLUMN_ETUDIANT_NOM, etudiant.getNom());
        values.put(COLUMN_ETUDIANT_UID, etudiant.getUid());
        values.put(COLUMN_ETUDIANT_HEUREDEBUT, etudiant.getHeureDebut());
        values.put(COLUMN_ETUDIANT_HEUREFIN, etudiant.getHeureFin());

        db.insert(TABLE_ETUDIANT, null, values);
        db.close();
    }

    public void addExamen(Examen examen) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EXAMEN_DATE, examen.getDate());
        values.put(COLUMN_EXAMEN_MATIERE, examen.getMatiere());
        values.put(COLUMN_EXAMEN_PROFESSEUR, examen.getProfesseur());
        values.put(COLUMN_EXAMEN_HEUREDEBUT, examen.getHeureDebut());
        values.put(COLUMN_EXAMEN_HEUREFIN, examen.getHeureFin());

        db.insert(TABLE_EXAMEN, null, values);
        db.close();
    }

    public int updateHeureEtudiant(Etudiant etudiant, String heure, String prenom, String nom,
                                   boolean heureDebut) {
        Log.i(TAG, "MyDatabaseHelper.updateHeureEtudiant ... ");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if(heureDebut) {
            values.put(COLUMN_ETUDIANT_HEUREDEBUT, heure);
            values.put(COLUMN_ETUDIANT_PRENOM, prenom);
            values.put(COLUMN_ETUDIANT_NOM, nom);
        } else {
            values.put(COLUMN_ETUDIANT_HEUREFIN, heure);
            values.put(COLUMN_ETUDIANT_PRENOM, prenom);
            values.put(COLUMN_ETUDIANT_NOM, nom);
        }

        // updating row
        return db.update(TABLE_ETUDIANT, values, COLUMN_ETUDIANT_ID + " = ?",
                new String[]{String.valueOf(etudiant.getId())});
    }


    public List<Etudiant> getAllEtudiants() {
        Log.i(TAG, "MyDatabaseHelper.getAllEtudiants ... " );

        List<Etudiant> etudiantList = new ArrayList<Etudiant>();
        String selectQuery = "SELECT  * FROM " + TABLE_ETUDIANT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Etudiant etudiant = new Etudiant();
                etudiant.setId(Integer.parseInt(cursor.getString(0)));
                etudiant.setPrenom(cursor.getString(1));
                etudiant.setNom(cursor.getString(2));
                etudiant.setUid(cursor.getString(3));
                etudiant.setHeureDebut(cursor.getString(4));
                etudiant.setHeureFin(cursor.getString(5));
                etudiantList.add(etudiant);
            } while (cursor.moveToNext());
        }
        return etudiantList;
    }

    public List<Examen> getAllExamens() {
        List<Examen> examenList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_EXAMEN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Examen examen = new Examen();
                examen.setId(Integer.parseInt(cursor.getString(0)));
                examen.setDate(cursor.getString(1));
                examen.setMatiere(cursor.getString(2));
                examen.setProfesseur(cursor.getString(3));
                examen.setHeureDebut(cursor.getString(4));
                examen.setHeureFin(cursor.getString(5));
                examenList.add(examen);
            } while (cursor.moveToNext());
        }
        return examenList;
    }

    public int deleteHeuresEtudiant(Etudiant etudiant) {
        Log.i(TAG, "MyDatabaseHelper.deleteHeuresEtudiant ... ");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ETUDIANT_HEUREDEBUT, "");
        values.put(COLUMN_ETUDIANT_HEUREFIN, "");

        // updating row
        return db.update(TABLE_ETUDIANT, values, COLUMN_ETUDIANT_ID + " = ?",
                new String[]{String.valueOf(etudiant.getId())});
    }

    public void deleteAllEtudiants() {
        Log.i(TAG, "MyDatabaseHelper.deleteAllEtudiants ... " );

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_ETUDIANT);
    }

    public void deleteAllExamens() {
        Log.i(TAG, "MyDatabaseHelper.deleteAllExamens ... " );

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_EXAMEN);
    }
}