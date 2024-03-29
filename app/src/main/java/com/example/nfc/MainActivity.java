package com.example.nfc;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;

import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private final List<Etudiant> etudiantList = new ArrayList<>();
    private final List<Examen> examenList = new ArrayList<>();

    private MyDatabaseHelper db;

    private List<Etudiant> list;
    private List<Examen> list2;

    private ActivityResultLauncher<Intent> scan;
    private ActivityResultLauncher<Intent> formulaireEtudiant;
    private ActivityResultLauncher<Intent> choixExamen;
    private ActivityResultLauncher<Intent> formulaireExamen;

    private String date, matiere, professeur;
    private String heureDebut, heureFin;

    private String choixExam;
    private Button button;

    ImageView generatePDFimg;
    int pageHeight;
    int pagewidth;
    Bitmap bmp, scaledbmp;

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new MyDatabaseHelper(this);

        list =  db.getAllEtudiants();
        this.etudiantList.addAll(list);

        list2 = db.getAllExamens();
        this.examenList.addAll(list2);

        choixExam = "";
        date = "";
        matiere = "";
        professeur = "";
        heureDebut = "";
        heureFin = "";
        button = findViewById(R.id.choixExamen);

        pageHeight = 1120;
        pagewidth = 792;

        scan = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        assert intent != null;

                        String uid = intent.getStringExtra("uid");
                        String heure = intent.getStringExtra("heure");
                        String prenom = intent.getStringExtra("prenom");
                        String nom = intent.getStringExtra("nom");

                        boolean condition = false;
                        for(Etudiant etudiant : etudiantList) {
                            if(etudiant.getUid().equals(uid)) {
                                if(etudiant.getHeureDebut().equals("")){
                                    db.updateHeureEtudiant(etudiant, heure, prenom, nom, true);
                                    Toast.makeText(this, "Heure de début scanné", Toast.LENGTH_SHORT).show();
                                }
                                else if(etudiant.getHeureFin().equals("")) {
                                    db.updateHeureEtudiant(etudiant, heure, prenom, nom, false);
                                    Toast.makeText(this, "Heure de fin scanné", Toast.LENGTH_SHORT).show();
                                }
                                condition = true;
                            }
                        }
                        if(!condition) {
                            Etudiant nouveau = new Etudiant(prenom, nom, uid, heure, "");
                            db.addEtudiant(nouveau);

                            reset();
                        }
                        reset();
                    }
                });

        formulaireEtudiant = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();

                        if(intent!=null) {
                            String prenom = intent.getStringExtra("prenom");
                            String nom = intent.getStringExtra("nom");
                            String uid = intent.getStringExtra("uid");

                            Etudiant nouveau = new Etudiant(prenom, nom, uid, "", "");
                            db.addEtudiant(nouveau);
                        }

                        reset();
                    }
                });

        choixExamen = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        assert intent != null;

                        for(Etudiant etudiant : etudiantList)
                            db.deleteHeuresEtudiant(etudiant);
                        reset();

                        choixExam = intent.getStringExtra("choixExamen");
                        button.setText("Choix de l'examen " + " (" + choixExam + ")");

                        for(Examen examen : examenList) {
                            if(examen.getMatiere().equals(choixExam)) {
                                date = examen.getDate();
                                matiere = examen.getMatiere();
                                professeur = examen.getProfesseur();
                                heureDebut = examen.getHeureDebut();
                                heureFin = examen.getHeureFin();
                            }
                        }
                    }
                });

        formulaireExamen = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();

                        if(intent!=null) {
                            date = intent.getStringExtra("date");
                            matiere = intent.getStringExtra("matiere");
                            professeur = intent.getStringExtra("professeur");
                            heureDebut = intent.getStringExtra("heureDebut");
                            heureFin = intent.getStringExtra("heureFin");

                            Examen nouveau = new Examen(date, matiere, professeur, heureDebut, heureFin);
                            db.addExamen(nouveau);
                        }

                        reset();
                    }
                });

        generatePDFimg = findViewById(R.id.PDFimg);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.univ);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 350, 150, false);
        generatePDFimg.setOnClickListener(this::generatePDF);

        if (!checkPermission())
            requestPermission();
    }

    public void scan(View v) {
        ArrayList<Etudiant> list = new ArrayList<>(etudiantList);

        Intent intent = new Intent(this, Scan.class);
        intent.putExtra("list", list);
        scan.launch(intent);
    }

    public void formulaireEtudiant(View v) {
        Intent intent = new Intent(this, FormulaireEtudiant.class);
        formulaireEtudiant.launch(intent);
    }

    public void listeEtudiants(View v) {
        ArrayList<Etudiant> list = new ArrayList<>(etudiantList);

        Intent intent = new Intent(this, ListeEtudiants.class);
        intent.putExtra("list", list);
        startActivity(intent);
    }

    public void choixExamen(View v) {
        ArrayList<Examen> list = new ArrayList<>(examenList);

        Intent intent = new Intent(this, ChoixExamen.class);
        intent.putExtra("list", list);
        choixExamen.launch(intent);
    }

    public void formulaireExamen(View v) {
        Intent intent = new Intent(this, FormulaireExamen.class);
        formulaireExamen.launch(intent);
    }

    public void listeExamens(View v) {
        ArrayList<Examen> list = new ArrayList<>(examenList);

        Intent intent = new Intent(this, ListeExamens.class);
        intent.putExtra("list", list);
        startActivity(intent);
    }

    public void resetDB(View v) {
        db.deleteAllExamens();
        db.deleteAllEtudiants();
        reset();
    }

    public void reset() {
        etudiantList.clear();
        examenList.clear();

        list = db.getAllEtudiants();
        etudiantList.addAll(list);

        list2 = db.getAllExamens();
        examenList.addAll(list2);
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage)
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    public void generatePDF(View v) {
        if(matiere.equals(""))
            Toast.makeText(this, "Veuillez sélectionner un examen", Toast.LENGTH_SHORT).show();
        else {
            PdfDocument pdfDocument = new PdfDocument();

            Paint paint = new Paint();
            Paint title = new Paint();

            PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
            PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

            Canvas canvas = myPage.getCanvas();
            int xPos = (canvas.getWidth() / 2);
            canvas.drawBitmap(scaledbmp, 56, 40, paint);

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(20);
            title.setTextAlign(Paint.Align.CENTER);

            canvas.drawText(date, 720, 230, title);
            canvas.drawText(matiere, xPos, 265, title);
            canvas.drawText("(" + professeur + ")", xPos, 300, title);
            canvas.drawText(heureDebut + " - " + heureFin, xPos, 350, title);

            title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            title.setTextSize(15);
            title.setTextAlign(Paint.Align.CENTER);

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.rgb(0, 0, 0));
            paint.setStrokeWidth(5);

            canvas.drawText("Prénom", 210, 460, title);
            canvas.drawText("Nom", 340, 460, title);
            canvas.drawText("Heure début", 460, 460, title);
            canvas.drawText("Heure fin", 580, 460, title);
            canvas.drawRect(280, 475, 150, 430, paint);
            canvas.drawRect(400, 475, 150, 430, paint);
            canvas.drawRect(520, 475, 150, 430, paint);
            canvas.drawRect(640, 475, 150, 430, paint);

            title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            int x = 520;
            int y = 505;
            for(Etudiant etd : etudiantList) {
                canvas.drawText(etd.getPrenom(), 210, y, title);
                canvas.drawText(etd.getNom(), 340, y, title);
                canvas.drawText(etd.getHeureDebut(), 460, y, title);
                canvas.drawText(etd.getHeureFin(), 580, y, title);
                canvas.drawRect(280, x, 150, 430, paint);
                canvas.drawRect(400, x, 150, 430, paint);
                canvas.drawRect(520, x, 150, 430, paint);
                canvas.drawRect(640, x, 150, 430, paint);
                y = y + 40;
                x = x + 40;
            }

            pdfDocument.finishPage(myPage);
            File file = new File(Environment.getExternalStorageDirectory(), "FeuilleEmargement.pdf");

            try {
                pdfDocument.writeTo(new FileOutputStream(file));
                Toast.makeText(MainActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pdfDocument.close();
        }
    }

    public void quitter(View v) {
        this.finish();
    }
}