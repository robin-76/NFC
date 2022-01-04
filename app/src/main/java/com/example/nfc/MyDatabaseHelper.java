package com.example.nfc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Etudiant_Manager";
    private static final String TABLE_ETUDIANT = "Etudiant";
    private static final String COLUMN_ETUDIANT_ID ="Etudiant_Id";
    private static final String COLUMN_ETUDIANT_PRENOM ="Etudiant_Prenom";
    private static final String COLUMN_ETUDIANT_NOM = "Etudiant_Nom";
    private static final String COLUMN_ETUDIANT_UID = "Etudiant_Uid";

    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        String script = "CREATE TABLE " + TABLE_ETUDIANT + "("
                + COLUMN_ETUDIANT_ID + " INTEGER PRIMARY KEY," + COLUMN_ETUDIANT_PRENOM + " TEXT,"
                + COLUMN_ETUDIANT_NOM + " TEXT," + COLUMN_ETUDIANT_UID + " TEXT" + ")";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ETUDIANT);
        onCreate(db);
    }

    public void createDefaultEtudiantsIfNeed()  {
        int count = this.getEtudiantsCount();
        if(count ==0 ) {
            Etudiant etudiant1 = new Etudiant("Robin",
                    "Guyomar", 0123);
            Etudiant etudiant2 = new Etudiant("Test2",
                    "aaaa", 456);
            this.addEtudiant(etudiant1);
            this.addEtudiant(etudiant2);
        }
    }

    public void addEtudiant(Etudiant etudiant) {
        Log.i(TAG, "MyDatabaseHelper.addEtudiant ... " + etudiant.getNom());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ETUDIANT_PRENOM, etudiant.getPrenom());
        values.put(COLUMN_ETUDIANT_NOM, etudiant.getNom());
        values.put(COLUMN_ETUDIANT_UID, etudiant.getUid());

        db.insert(TABLE_ETUDIANT, null, values);

        db.close();
    }

    public Etudiant getEtudiant(int id) {
        Log.i(TAG, "MyDatabaseHelper.getEtudiant ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ETUDIANT, new String[] { COLUMN_ETUDIANT_ID,
                        COLUMN_ETUDIANT_PRENOM, COLUMN_ETUDIANT_NOM, COLUMN_ETUDIANT_UID }, COLUMN_ETUDIANT_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Etudiant etudiant = new Etudiant(cursor.getInt(0), cursor.getString(1),
                 cursor.getString(2), cursor.getInt(3));

        return etudiant;
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
                etudiant.setUid(cursor.getInt(3));
                etudiantList.add(etudiant);
            } while (cursor.moveToNext());
        }

        return etudiantList;
    }

    public int getEtudiantsCount() {
        Log.i(TAG, "MyDatabaseHelper.getEtudiantsCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_ETUDIANT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public int updateEtudiant(Etudiant etudiant) {
        Log.i(TAG, "MyDatabaseHelper.updateEtudiant ... "  + etudiant.getNom());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ETUDIANT_PRENOM, etudiant.getPrenom());
        values.put(COLUMN_ETUDIANT_NOM, etudiant.getNom());
        values.put(COLUMN_ETUDIANT_UID, etudiant.getUid());

        // updating row
        return db.update(TABLE_ETUDIANT, values, COLUMN_ETUDIANT_ID + " = ?",
                new String[]{String.valueOf(etudiant.getId())});
    }

    public void deleteEtudiant(Etudiant etudiant) {
        Log.i(TAG, "MyDatabaseHelper.updateEtudiant ... " + etudiant.getNom() );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ETUDIANT, COLUMN_ETUDIANT_ID + " = ?",
                new String[] { String.valueOf(etudiant.getId()) });
        db.close();
    }

}